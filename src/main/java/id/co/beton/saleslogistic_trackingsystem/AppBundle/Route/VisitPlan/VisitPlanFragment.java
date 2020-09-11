package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.VisitPlan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.DeliveryPlanAdapter;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.VisitPlanAdapter;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.MainActivity;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.DetailPlanActivity;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.InvoicesActivity;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.PackingSlipActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Developer.NfcSimulationReceiver;
import id.co.beton.saleslogistic_trackingsystem.Developer.TapNfcSimulationService;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.Customer;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.DestinationOrder;
import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
import id.co.beton.saleslogistic_trackingsystem.Model.PackingSlip;
import id.co.beton.saleslogistic_trackingsystem.Model.Plan;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;

/**
 * Class VisitPlanFragment
 */
public class VisitPlanFragment extends Fragment {
    private static final String TAG = VisitPlanFragment.class.getSimpleName();
    private TextView text_view_for_tab_selection;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Customer> customers;
    private ListView lvVisitPlan;
    private LinearLayout llFooter;
    private Context context;
    DialogCustom dialogCustom;
    VisitPlanAdapter visitPlanAdapter;
    DeliveryPlanAdapter deliveryPlanAdapter;

    LayoutInflater inflater;
    private View header;
    private UserUtil userUtil;
    private PlanUtil planUtil;
    List<Destination> newDestination = new ArrayList<>();
    List<Destination> allDestinations = new ArrayList<>();
    List<DestinationOrder> newDestinationOrder = new ArrayList<>();
    List<String> customerExist = new ArrayList<>();
    private Intent intent;

    private SwipeRefreshLayout swipeContainer;
    private SearchView searchView;
    private FloatingActionButton addVisitCustomer;
    private int REQUEST_CODE = 300;
    private Plan plan=null;

    protected Location location = null;
    private LocationManager mLocationManager = null;

    public VisitPlanFragment() {
    }

    public static VisitPlanFragment newInstance(int tabSelected) {
        VisitPlanFragment fragment = new VisitPlanFragment();
        Bundle args = new Bundle();
        args.putInt("position", tabSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.content_visit_plan, container, false);
        view.setTag(TAG);

        customers = new ArrayList<>();
        context = container.getContext();

        userUtil = UserUtil.getInstance(context);
        planUtil = PlanUtil.getInstance(context);

        dialogCustom = new DialogCustom(context);

        if (location == null) {
            location = getLastLocationService();
        }

        this.inflater = inflater;

        lvVisitPlan = (ListView) view.findViewById(R.id.lv_visit_plan);
        header = inflater.inflate(R.layout.header_visit_plan, null);
        lvVisitPlan.addHeaderView(header);

        llFooter = (LinearLayout) view.findViewById(R.id.ll_footer);

        // button add new visit
        addVisitCustomer = view.findViewById(R.id.add_visit_customer);

        /**
         * set function add visit customer and confirmation invoice based on type user
         * collector only, can collect, not both
         */
        if(userUtil.getRoleUser().equals(Constants.ROLE_SALES)){
            if(userUtil.isCollectorOnly()){
                // perubahan untuk kasus invoice ditengah jalan, add new visit harus enable
                addVisitCustomer.show();
                llFooter.setVisibility(View.VISIBLE);
            }
            if(userUtil.isCanCollect()){
                addVisitCustomer.show();
                llFooter.setVisibility(View.VISIBLE);
            }
            if(!userUtil.isCollectorOnly() && !userUtil.isCanCollect() ){
                addVisitCustomer.show();
                llFooter.setVisibility(View.GONE);
            }

            addVisitCustomer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE)) {
                        Intent intent = new Intent(context, AddNewVisitPlan.class);
                        intent.putStringArrayListExtra("customerExist", (ArrayList<String>) customerExist);
                        startActivityForResult(intent, REQUEST_CODE);
                        // startActivity(intent);
                    } else {
                        Toasty.error(context, "Anda belum melakukan Tap Start").show();
                    }
                }
            });
        } else {
            addVisitCustomer.hide();
        }

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshAdapter(true);
                if(userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE)){
                    userSetSocketIO();
                }
            }
        });

        lvVisitPlan.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int topRowVerticalPosition = (lvVisitPlan == null || lvVisitPlan.getChildCount() == 0) ? 0 : lvVisitPlan.getChildAt(0).getTop();
                swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        /**
         * function to search customer by customer name
         */
        searchView = view.findViewById(R.id.searchFilter);
        searchView.setVisibility(View.GONE);
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)){
                    //Text is cleared, do your thing
                    refreshAdapter(false);
                } else {
                    if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
                        deliveryPlanAdapter.getFilter().filter(query);
                    } else {
                        visitPlanAdapter.getFilter().filter(query);
                    }
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    //Text is cleared, do your thing
                    refreshAdapter(false);
                } else {
                    if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
                        deliveryPlanAdapter.getFilter().filter(newText);
                    } else {
                        visitPlanAdapter.getFilter().filter(newText);
                    }
                }
                return false;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                refreshAdapter(false);
                return false;
            }
        });

        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            TextView tvFooter = (TextView) view.findViewById(R.id.tv_footer);
            tvFooter.setText("Penerimaan Packing Slip");

            TextView tvRencanaKunjugan = (TextView) header.findViewById(R.id.tv_rencana_kunjungan);
            tvRencanaKunjugan.setText("Jadwal Pengiriman");

            TextView tvTotalKunjungan = (TextView) header.findViewById(R.id.tv_total_kunjungan);
            tvTotalKunjungan.setText("Total Pengiriman");

            TextView tvTotalInvoice = (TextView) header.findViewById(R.id.tv_jumlah_invoice);
            tvTotalInvoice.setText("Jumlah Packing Slip");
            // loadDataDriver(true);
            loadData(true);
        } else if (userUtil.getRoleUser().equals(Constants.ROLE_SALES)) {
            // loadDataSales(true);
            loadData(true);
        } else {
            Toasty.error(context, "ROLE TIDAK DIKENAL").show();
        }

        return view;
    }

    /**
     * function to refresh page
     * @param flagLoading
     */
    private void refreshAdapter(boolean flagLoading){
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            deliveryPlanAdapter = new DeliveryPlanAdapter(context, 1, null);
            // loadDataDriver(flagLoading);
            loadData(flagLoading);
        } else if (userUtil.getRoleUser().equals(Constants.ROLE_SALES)) {
            visitPlanAdapter = new VisitPlanAdapter(context, 1, 0, false,null);
            // loadDataSales(flagLoading);
            loadData(flagLoading);
        } else {
            Toasty.error(context, "ROLE TIDAK DIKENAL").show();
        }
    }

    /**
     * function to set list with route
     * based on destination_order
     * @param plan
     */
    private void setListWithRoute(Plan plan){
        if (plan.getDestinationOrder().size() > 0) {

            // set variable for list_destination
            ArrayList<String> listDestination = new ArrayList<>();

            // TODO: reorder List Destination
            newDestinationOrder = new ArrayList<>();
            for(int i=0; i<plan.getDestinationOrder().size(); i++){
                for(int j=0; j<plan.getDestinationOrder().size(); j++){
                    DestinationOrder destinationOrder = plan.getDestinationOrder().get(j);
                    if (plan.getDestinationOrder().get(j).getOrder() == i) {
                        newDestinationOrder.add(destinationOrder);

                        // add nfc code to listDestination
                        listDestination.add(destinationOrder.getNfcCode());
                    }
                }
            }

            // save to shared preference
            userUtil.saveArrayList(Constants.LIST_DESTINATION, listDestination);

            int last_index = plan.getDestinationOrder().size()-1;
            userUtil.setKoordinatOfficeStart(newDestinationOrder.get(0).getLat(), newDestinationOrder.get(0).getLng());
            userUtil.setNFCodeOfficeStart(newDestinationOrder.get(0).getNfcCode());

            userUtil.setKoordinatOfficeEnd(newDestinationOrder.get(last_index).getLat(), newDestinationOrder.get(last_index).getLng());
            userUtil.setNFCodeOfficeEnd(newDestinationOrder.get(last_index).getNfcCode());

            // TODO: Setup Array allDestinations
            customerExist = new ArrayList<>();
            newDestination = new ArrayList<>();
            allDestinations = new ArrayList<>();

            for (DestinationOrder des : newDestinationOrder) {
                for (Destination destination : plan.getDestination()) {
                    if (destination.getCustomerCode().equals(des.getNfcCode())) {
                        newDestination.add(destination);
                        customerExist.add(des.getNfcCode());
                        allDestinations.add(destination);
                        break;
                    }
                }
            }

            // TODO: Check New Destination
            if(plan.getDestinationNew()!=null){
                if(plan.getDestinationNew().size() > 0){
                    for(Destination destinationNew : plan.getDestinationNew()){
                        customerExist.add(destinationNew.getCustomerCode());
                        allDestinations.add(destinationNew);
                    }
                }
            }
        }
    }

    /**
     * function to set list with route
     * based on destination
     * @param plan
     */
    private void setListWithoutRoute(Plan plan){
        // TODO: Setup Coordinate Start & End Branch
        userUtil.setKoordinatOfficeStart(plan.getStartRouteBranch().getLat(), plan.getStartRouteBranch().getLng());
        userUtil.setNFCodeOfficeStart(String.valueOf(plan.getStartRouteBranchId()));

        userUtil.setKoordinatOfficeEnd(plan.getEndRouteBranch().getLat(), plan.getEndRouteBranch().getLng());
        userUtil.setNFCodeOfficeEnd(String.valueOf(plan.getEndRouteBranchId()));

        // TODO: Setup Array allDestinations
        customerExist = new ArrayList<>();
        newDestination = new ArrayList<>();
        allDestinations = new ArrayList<>();

        if(plan.getDestination() == null){
            // check only destination new
            if(plan.getDestinationNew()!=null){
                if (plan.getDestinationNew().size() > 0){
                    for(Destination destinationNew : plan.getDestinationNew()){
                        customerExist.add(destinationNew.getCustomerCode());
                        allDestinations.add(destinationNew);
                    }
                }
            }
        } else {
            // check destination origin and destination new
            if(plan.getDestination().size() > 0){
                for (Destination destination : plan.getDestination()) {
                    newDestination.add(destination);
                    customerExist.add(destination.getCustomerCode());
                    allDestinations.add(destination);
                }
            }

            // TODO: Check New Destination
            if(plan.getDestinationNew()!=null){
                if(plan.getDestinationNew().size() > 0){
                    for(Destination destinationNew : plan.getDestinationNew()){
                        customerExist.add(destinationNew.getCustomerCode());
                        allDestinations.add(destinationNew);
                    }
                }
            }
        }
    }

    /**
     * load data visit/delivery plan
     * @param showLoading
     */
    private void loadData(boolean showLoading){
        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call;
        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            call = service.getDeliveryPlan("JWT " + userUtil.getJWTTOken(), planUtil.getPlanId());
        } else {
            call = service.visitPlan("JWT " + userUtil.getJWTTOken());
        }
        /*Log the URL called*/
        if (Constants.DEBUG) {
            Log.i("URL Called", call.request().url() + "");
        }

        if(showLoading){
            /*Loading*/
            dialogCustom.show();
        }

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body() != null && response.body().getError() == 0) { //if sukses
                    try {
                        Gson gson = new Gson();
                        plan = gson.fromJson(response.body().getData().toString(), Plan.class);

                        // TODO: Setup Variable Plan
                        PlanUtil.getInstance(context).setPlanId(plan.getId());
                        PlanUtil.getInstance(context).setIsUsingRoute(plan.getIsUseRoute());
                        PlanUtil.getInstance(context).isUsingRoute();
                        PlanUtil.getInstance(context).getPlanId();
                        // set asset data
                        if(plan.getAssetId()!=null){
                            if(plan.getAssetId()>0){
                                PlanUtil.getInstance(context).setAsset(plan.getAsset().getCode(), plan.getAsset().getName());
                            } else {
                                PlanUtil.getInstance(context).setAsset("","");
                            }
                        }

                        // TODO: Order List Customer by isUsingRoute
                        if(PlanUtil.getInstance(context).isUsingRoute()){
                            setListWithRoute(plan);
                        } else {
                            setListWithoutRoute(plan);
                        }
                        // System.out.println("List Destination di visit plan" + userUtil.getStringSets(Constants.LIST_DESTINATION));
                        if(userUtil.getRoleUser().equals(Constants.ROLE_SALES)){ // Role Sales
                            // setup Invoice Size
                            PlanUtil.getInstance(context).setInvoiceSize(plan.getInvoiceId().size());

                            // TODO: Setup Adapter
                            visitPlanAdapter = new VisitPlanAdapter(context, 1, newDestination.size(), false, allDestinations);
                            lvVisitPlan.setAdapter(visitPlanAdapter);
                            lvVisitPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (i != 0) {
                                        Intent intent = new Intent(context, DetailPlanActivity.class);
                                        Gson gson = new Gson();
                                        userUtil.setStringProperty(Constants.CURRENT_PAGE_PLAN, allDestinations.get(i - 1).getCustomerCode());
                                        intent.putExtra("destination", gson.toJson(allDestinations.get(i - 1), new TypeToken<Destination>() {
                                        }.getType()));
                                        intent.putExtra("customer_code", allDestinations.get(i - 1).getCustomerCode());
                                        intent.putExtra("customer_name", allDestinations.get(i - 1).getCustomerName());
                                        startActivity(intent);
                                    }
                                }
                            });

                            TextView tvDateVP = (TextView) header.findViewById(R.id.tv_date_vp);
                            TextView tvTotalVisitPlace = (TextView) header.findViewById(R.id.tv_total_visit_place);
                            TextView tvTotalNumberInvoice = (TextView) header.findViewById(R.id.tv_total_number_invoice);
                            TextView tvStartingPoint = (TextView) header.findViewById(R.id.tv_starting_point);
                            TextView tvDeparture = (TextView) header.findViewById(R.id.tv_depature);
                            TextView tvEstimation = (TextView) header.findViewById(R.id.tv_estimation);

                            tvDateVP.setText(ConversionDate.getInstance().fullFormatDate(plan.getDate()));
                            tvTotalVisitPlace.setText(plan.getTotalCustomer().toString());
                            tvTotalNumberInvoice.setText(plan.getTotalInvoice().toString());

                            // set address starting point, check whether start point is custom branch or not
                            if(PlanUtil.getInstance(context).isPlanCustome()){
                                tvStartingPoint.setText(userUtil.getAddressCustomOfficeStart());
                            } else {
                                tvStartingPoint.setText(plan.getStartRouteBranch().getName());
                            }

                            if (plan.getInvoiceId() == null || plan.getInvoiceId().size() <= 0) {
                                llFooter.setVisibility(View.GONE);
                            }

                            if(plan.getTotalInvoice() == 0){
                                userUtil.setBooleanProperty(Constants.STATUS_CONFIRM_INVOICE,true);
                                llFooter.setVisibility(View.GONE);
                            } else {
                                if(userUtil.isCollectorOnly() || userUtil.isCanCollect()){
                                    llFooter.setVisibility(View.VISIBLE);
                                }
                                if(!userUtil.isCollectorOnly() && !userUtil.isCanCollect() ){
                                    userUtil.setBooleanProperty(Constants.STATUS_CONFIRM_INVOICE,true);
                                    llFooter.setVisibility(View.GONE);
                                }

                                // hide when user TAP START
                                if((!userUtil.getBooleanProperty(Constants.STATUS_IN_OFFICE)) && (userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE))){
                                    llFooter.setVisibility(View.GONE);
                                }
                                // hide when user TAP STOP
                                if((userUtil.getBooleanProperty(Constants.STATUS_IN_OFFICE)) && (!userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE))){
//                                    llFooter.setVisibility(View.GONE);
                                }
                            }

                            llFooter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, InvoicesActivity.class);
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<Invoice>>() {
                                    }.getType();
                                    String json = gson.toJson(plan.getInvoiceId(), type);
                                    intent.putExtra("list_invoices", json);
                                    startActivity(intent);
                                }
                            });
                        } else { // Role Driver/Logistic
                            // setup Packing Slip Size
                            PlanUtil.getInstance(context).setPackingSlipSize(plan.getPackingSlipsId().size());

                            // TODO: Setup Adapter
                            deliveryPlanAdapter = new DeliveryPlanAdapter(context, 1, newDestination);
                            lvVisitPlan.setAdapter(deliveryPlanAdapter);
                            lvVisitPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    if (i != 0) {
                                        Intent intent = new Intent(context, DetailPlanActivity.class);
                                        Gson gson = new Gson();

                                        // Variable Tambahan
                                        userUtil.setStringProperty(Constants.CURRENT_PAGE_PLAN, newDestination.get(i - 1).getCustomerCode());
                                        intent.putExtra("destination", gson.toJson(newDestination.get(i - 1), new TypeToken<Destination>() {
                                        }.getType()));
                                        intent.putExtra("customer_code", newDestination.get(i - 1).getCustomerCode());
                                        intent.putExtra("customer_name", newDestination.get(i - 1).getCustomerName());
                                        startActivity(intent);
                                    }
                                }
                            });

                            TextView tvDateVP = (TextView) header.findViewById(R.id.tv_date_vp);
                            TextView tvTotalVisitPlace = (TextView) header.findViewById(R.id.tv_total_visit_place);
                            TextView tvTotalNumberInvoice = (TextView) header.findViewById(R.id.tv_total_number_invoice);
                            TextView tvStartingPoint = (TextView) header.findViewById(R.id.tv_starting_point);
                            TextView tvDeparture = (TextView) header.findViewById(R.id.tv_depature);
                            TextView tvEstimation = (TextView) header.findViewById(R.id.tv_estimation);

                            tvDateVP.setText(ConversionDate.getInstance().fullFormatDate(plan.getDate()));
                            tvTotalVisitPlace.setText(plan.getTotalCustomer().toString());
                            tvTotalNumberInvoice.setText(plan.getTotalPackingSlip().toString());
                            // set address starting point, check whether start point is custom branch or not
                            if(PlanUtil.getInstance(context).isPlanCustome()){
                                tvStartingPoint.setText(userUtil.getAddressCustomOfficeStart());
                            } else {
                                tvStartingPoint.setText(plan.getStartRouteBranch().getName());
                            }
                            // tvStartingPoint.setText(plan.getStartRouteBranch().getName());

                            if (plan.getPackingSlipsId() == null || plan.getPackingSlipsId().size() <= 0) {
                                llFooter.setVisibility(View.GONE);
                            } else {
                                llFooter.setVisibility(View.VISIBLE);
                            }

                            if(plan.getTotalPackingSlip() == 0){
                                userUtil.setBooleanProperty(Constants.STATUS_CONFIRM_INVOICE,true);
                                llFooter.setVisibility(View.GONE);
                            } else {
                                llFooter.setVisibility(View.VISIBLE);

                                // hide when user TAP START
                                if((!userUtil.getBooleanProperty(Constants.STATUS_IN_OFFICE)) && (userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE))){
                                    llFooter.setVisibility(View.GONE);
                                }
                                // hide when user TAP STOP
                                if((userUtil.getBooleanProperty(Constants.STATUS_IN_OFFICE)) && (!userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE))){
//                                    llFooter.setVisibility(View.GONE);
                                }
                            }

                            llFooter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(context, PackingSlipActivity.class);
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<PackingSlip>>() {
                                    }.getType();
                                    String json = gson.toJson(plan.getPackingSlipsId(), type);
                                    intent.putExtra("list_packing_slip", json);
                                    startActivity(intent);
                                }
                            });
                        }

                        // show filter function
                        searchView.setVisibility(View.VISIBLE);
                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeContainer.setRefreshing(false);

                        dialogCustom.hidden();
                    } catch (Exception e){
                        e.printStackTrace();
                        swipeContainer.setRefreshing(false);
                        dialogCustom.hidden();
                        Toasty.error(context, "Gagal Proses Data Visit/Delivery.", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    try {
                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeContainer.setRefreshing(false);
                        llFooter.setVisibility(View.GONE);
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        dialogCustom.hidden();
                        String message = jsonObject.getString("message");
                        if(message.equals("This visit plan doesn't exist")){
                            message = "visit plan belum dibuat";
                        }
                        Toasty.error(context, message, Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeContainer.setRefreshing(false);
                        dialogCustom.hidden();
                        Toasty.error(context, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });
    }

    private void userSetSocketIO(){
        //set user
        Socket socketUserSet;
        try {
            socketUserSet = IO.socket(Constants.API_SERVER_ADDR);
            socketUserSet.connect();
            JSONObject jsonObject = new JSONObject();
            if (location != null) {
                if (Constants.DEBUG) {
                    Log.i("lat", location.getLatitude() + "");
                    Log.i("lng", location.getLongitude() + "");
                }
                jsonObject.put("lat", location.getLatitude());
                jsonObject.put("lng", location.getLongitude());
            } else {
                jsonObject.put("lat", "9999");
                jsonObject.put("lng", "999");
            }

            jsonObject.put("user_id", userUtil.getId());
            jsonObject.put("name", userUtil.getName());
            jsonObject.put("job_function", userUtil.getRoleUser());
            jsonObject.put("branch_id", userUtil.getBranchId());
            jsonObject.put("division_id", userUtil.getDivisionId());

            // add asset information
            jsonObject.put("asset_code", "");
            jsonObject.put("asset_name", "");
            if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
                String[] asset =  PlanUtil.getInstance(context).getAsset();
                jsonObject.put("asset_code", asset[0]);
                jsonObject.put("asset_name", asset[1]);
            }

            socketUserSet.emit("user-set", jsonObject);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private Location getLastLocationService() {
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = null;
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }

            l = mLocationManager.getLastKnownLocation(provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            /**
             * refresh page
             */
            Intent refresh = new Intent(context, MainActivity.class);
            refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(refresh);
            getActivity().finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Constants.DEV_MODE) {
            context.stopService(intent);
        }
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Constants.DEV_MODE) {
            intent = new Intent(context, TapNfcSimulationService.class);
            intent.putExtra("type", NfcSimulationReceiver.TAP_IN);
            intent.putExtra("tap_type", TapNfcSimulationService.TAP_CUSTOMER_IN);
            intent.putExtra("role", userUtil.getRoleUser());
            context.startService(intent);
        }
        loadData(false);
    }
}
