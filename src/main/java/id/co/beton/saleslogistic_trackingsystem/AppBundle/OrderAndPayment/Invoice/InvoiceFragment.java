package id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.Invoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.InvoiceAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
import id.co.beton.saleslogistic_trackingsystem.Model.Plan;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class InvoiceFragment
 * Fragment invoice
 */
public class InvoiceFragment extends Fragment {
    private static final String TAG = InvoiceFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private List<Invoice> invoices = new ArrayList<>();
    private List<Destination> destinations = new ArrayList<>();
    private InvoiceAdapter invoiceAdapter;
    private ListView lvInvoice;
    private SwipeRefreshLayout swipeContainer;
    DialogCustom dialogCustom;
    private Context context;
    public InvoiceFragment() {
    }

    public static InvoiceFragment newInstance(String tabSelected) {
        InvoiceFragment fragment = new InvoiceFragment();
        Bundle args = new Bundle();
        args.putString("position", tabSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.content_payment_and_order, container, false);
        view.setTag(TAG);

        context =container.getContext();
        dialogCustom = new DialogCustom(context);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                loadData(view,container);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lvInvoice = (ListView)view.findViewById(R.id.lv_payment_and_order);
        loadData(view,container);

        lvInvoice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                try{
                    Gson gson               = new Gson();
                    String invoiceVal       = gson.toJson(invoices.get(i));
//                    String destinationVal   = gson.toJson(destinations.get(i));
                    Intent intent = new Intent(container.getContext(), DetailInvoiceActivity.class);
                    intent.putExtra("invoice",invoiceVal);
                    intent.putExtra("destination",invoices.get(i).getCustomerName());
//                    intent.putExtra("destination",destinationVal);
                    startActivity(intent);
                }
                catch (NumberFormatException e){
                    Log.e(TAG,e.getMessage());
                }
            }
        });

        lvInvoice.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int topRowVerticalPosition = (lvInvoice== null || lvInvoice.getChildCount() == 0) ? 0 : lvInvoice.getChildAt(0).getTop();
                swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

            }
        });

        return view;
    }

    /**
     * load data invoice from server
     * @param view
     * @param container
     */
    private void loadData(final View view, final ViewGroup container){

        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(getContext());

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.visitPlan("JWT "+userUtil.getJWTTOken());
        /*Log the URL called*/
        if(Constants.DEBUG){
            Log.i("URL Called", call.request().url() + "");
        }

        dialogCustom.show();
        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    try{
                        Gson gson =new Gson();

                        Plan plan = gson.fromJson(response.body().getData().toString(), Plan.class);
                        invoices = plan.getInvoiceId();
                        destinations = plan.getDestination();

                        invoiceAdapter = new InvoiceAdapter(container.getContext(),1,invoices,destinations);
                        lvInvoice.setAdapter(invoiceAdapter);
                    }catch (Exception e){
                        Log.e(TAG,e.toString());
                    }


                    //loop statement

                    dialogCustom.hidden();

                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }catch (Exception e){
                        Toasty.error(getContext(),"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }
                }

                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
//                loading.dismiss();
                swipeContainer.setRefreshing(false);
                dialogCustom.hidden();
                Toasty.error(context,"Terjadi kesalahan server").show();
            }
        });

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
    }
}
