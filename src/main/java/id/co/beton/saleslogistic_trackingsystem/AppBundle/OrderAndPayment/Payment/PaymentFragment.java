package id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.Payment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.PaymentAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.DataSalesPayment;
import id.co.beton.saleslogistic_trackingsystem.Model.Payment;
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
 */
public class PaymentFragment extends Fragment {
    private static final String TAG = PaymentFragment.class.getSimpleName();
    private TextView text_view_for_tab_selection;
    private OnFragmentInteractionListener mListener;
    private List<Payment> payments = new ArrayList<>();
    private ListView lvOrder;
    private SwipeRefreshLayout swipeContainer;
    DialogCustom dialogCustom;
    private Context context;
    private GsonBuilder builder;
    private Gson newGson;
    Gson gson =new Gson();
    public PaymentFragment() {
    }

    public static PaymentFragment newInstance(String tabSelected) {
        PaymentFragment fragment = new PaymentFragment();
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

        builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        newGson = builder.create();

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData(view,container);
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        lvOrder = (ListView) view.findViewById(R.id.lv_payment_and_order);
        loadData(view,container);
        lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    System.out.println("Payment Fragment");
                    // String detailPayment  = gson.toJson(payments.get(i));
                    String detailPayment  = newGson.toJson(payments.get(i));

                    Intent intent = new Intent(container.getContext(), DetailPaymentActivity.class);
                    intent.putExtra("detailPayment",detailPayment);
                    intent.putExtra("cuustomer_code", payments.get(i).getCustomerCode());
                    startActivity(intent);
                }catch (NumberFormatException e){
                    Log.e(TAG,e.getMessage());
                }
            }
        });
        lvOrder.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int topRowVerticalPosition = (lvOrder== null || lvOrder.getChildCount() == 0) ? 0 : lvOrder.getChildAt(0).getTop();
                swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

            }
        });

        return view;
    }

    /**
     * get data payment from server
     * @param view
     * @param container
     */
    private void loadData(View view, final ViewGroup container){
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(getContext());

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.salesPayment("JWT "+userUtil.getJWTTOken(), PlanUtil.getInstance(context).getPlanId());

        /*Log the URL called*/
        if(Constants.DEBUG){
            Log.i("URL Called", call.request().url() + "");
        }

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    try{
                        DataSalesPayment salesPayment = gson.fromJson(response.body().getData().toString(), DataSalesPayment.class);
                        payments = salesPayment.getData();

                        PaymentAdapter paymentAdapter=new PaymentAdapter(container.getContext(),1,payments);
                        lvOrder.setAdapter(paymentAdapter);

                        // lvOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        //     @Override
                        //     public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        //         try{
                        //             String detailPayment  = gson.toJson(payments.get(i));
                        //             Intent intent = new Intent(container.getContext(), DetailPaymentActivity.class);
                        //             // intent.putExtra("detailPayment",detailPayment);
                        //             intent.putExtra("detailPayment","");
                        //             intent.putExtra("cuustomer_code", payments.get(i).getCustomerCode());
                        //             startActivity(intent);
                        //         }catch (NumberFormatException e){
                        //             Log.e(TAG,e.getMessage());
                        //         }
                        //     }
                        // });
                    }catch (Exception e){
                        e.printStackTrace();
                        Log.e(TAG,e.toString());
                    }

                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();

                    }catch (Exception e){
                        Toasty.error(getContext(),"Gagal mengambil data.", Toast.LENGTH_SHORT).show();

                    }
                }

                swipeContainer.setRefreshing(false);

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                swipeContainer.setRefreshing(false);
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
