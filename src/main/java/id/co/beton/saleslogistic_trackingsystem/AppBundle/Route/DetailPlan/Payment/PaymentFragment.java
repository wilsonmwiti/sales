package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Payment;

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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import id.co.beton.saleslogistic_trackingsystem.Adapter.route.payment.PaymentAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.Payment;
import id.co.beton.saleslogistic_trackingsystem.Model.PaymentMobile;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class PaymentFragment
 * list of data payment
 */
public class PaymentFragment extends Fragment {
    private static final String TAG = PaymentFragment.class.getSimpleName();
    private TextView text_view_for_tab_selection;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Payment> paymentMobileDatas;
    private ListView lvOrder;
    private Context context;
    String customerName;
    String customerCode;
    PaymentAdapter paymentAdapter;
    LayoutInflater inflater;
    private SwipeRefreshLayout swipeContainer;
    private View header;

    public PaymentFragment() {
    }

    public static id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.Payment.PaymentFragment newInstance(String tabSelected) {
        id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.Payment.PaymentFragment fragment = new id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.Payment.PaymentFragment();
        Bundle args = new Bundle();
        args.putString("position", tabSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.content_detail_visit_plan_payment, container, false);
        view.setTag(TAG);


        context = container.getContext();
        customerCode = getArguments().getString("cust_code");
        customerName = getArguments().getString("cust_name");
        this.inflater = inflater;
        lvOrder = (ListView) view.findViewById(R.id.lv_detail_visit_plan_payment);

        loadData();

        header = inflater.inflate(R.layout.header_payment,null);
        lvOrder.addHeaderView(header);


        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                loadData();
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        return view;
    }

    public void loadData() {
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(context);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.salesPaymentMobile("JWT " + userUtil.getJWTTOken(), PlanUtil.getInstance(context).getPlanId(),customerCode);

        /*Log the URL called*/
        if (Constants.DEBUG) {
            Log.i("URL Called", call.request().url() + "");
        }

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    Gson gson =new Gson();
                    final PaymentMobile paymentMobile = gson.fromJson(response.body().getData().toString(), PaymentMobile.class);

                    if(Constants.DEBUG){
                        Log.i(TAG, paymentMobile.toString());
                    }

                    /**
                     * get data payment based on customer code
                     */
                    final List<Payment> newPayment = new ArrayList<>();
                    if(paymentMobile.getData().size() > 0){
                        for(int i=0; i<paymentMobile.getData().size(); i++){
                            if(paymentMobile.getData().get(i).getCustomerCode().equals(customerCode)){
                                newPayment.add(paymentMobile.getData().get(i));
                            }
                        }
                    }

                    // paymentAdapter=new PaymentAdapter(context,1, paymentMobile.getData());
                    paymentAdapter=new PaymentAdapter(context,1, newPayment);
                    lvOrder.setAdapter(paymentAdapter);

                    lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            if(i!=0){
                                Gson gsonException = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();
                                // Gson gson = new Gson();
                                // List<Payment> paymentOnly = paymentMobile.getData();
                                List<Payment> paymentOnly = newPayment;
                                Payment oncePaymentOnly = paymentOnly.get(i-1);
                                String json = gsonException.toJson(oncePaymentOnly, Payment.class);
                                Intent intent = new Intent(context, DetailPaymentVisitPlanActivity.class);
                                intent.putExtra("detail_payment",json);
                                intent.putExtra("cust_name",customerName);
                                intent.putExtra("cust_code",customerCode);
                                startActivity(intent);
                            }
                        }
                    });

                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toasty.error(getContext(),"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                }

                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);

//                loading.dismiss();
                Toasty.error(context,"Terjadi kesalahan server").show();
            }
        });
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
