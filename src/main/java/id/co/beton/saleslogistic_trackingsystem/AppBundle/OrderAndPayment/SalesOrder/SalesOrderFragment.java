package id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.SalesOrder;

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
import id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.SalesOrderAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.DataSalesOrder;
import id.co.beton.saleslogistic_trackingsystem.Model.SalesOrder;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class SalesOrderFragment
 * fragment sales order
 */
public class SalesOrderFragment extends Fragment {
    private static final String TAG = SalesOrderFragment.class.getSimpleName();
    private TextView text_view_for_tab_selection;
    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private ArrayList<SalesOrder> salesOrders;
    private DataSalesOrder salesOrder;
    SwipeRefreshLayout swipeContainer;
    DialogCustom dialogCustom;
    private Context context;

    public SalesOrderFragment() {
    }

    public static SalesOrderFragment newInstance(String tabSelected) {
        SalesOrderFragment fragment = new SalesOrderFragment();
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
                loadData(view,getContext());
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        salesOrders = new ArrayList<>();
        listView= (ListView)view.findViewById(R.id.lv_payment_and_order);

        loadData(view,container.getContext());

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    Gson gson               = new Gson();
                    String salesOrderVal    = gson.toJson(salesOrder.getData().get(i));
                    Intent intent = new Intent(getContext(), DetailSalesOrderActivity.class);
                    intent.putExtra("sales_order",salesOrderVal);
                    startActivity(intent);
                }catch (NumberFormatException e){
                    Log.e(TAG,e.getMessage());
                }

            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int topRowVerticalPosition = (listView== null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();
                swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

            }
        });
        return view;
    }

    /**
     * load data sales order from server
     * @param view
     * @param mContext
     */
    private void loadData(final View view, final Context mContext){
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(getContext());

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        /*Call the method with parameter in the interface to get the data*/
   //     Call<ResponseObject> call = service.salesOrder("JWT "+userUtil.getJWTTOken(), PlanUtil.getInstance(context).getPlanId());
        Call<ResponseObject> call = service.salesOrder("JWT "+userUtil.getJWTTOken(), ""+ PlanUtil.getInstance(getActivity().getApplicationContext()).getPlanId());

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
                        Gson gson =new Gson();
                        salesOrder = gson.fromJson(response.body().getData().toString(), DataSalesOrder.class);

                        if(Constants.DEBUG){
                            Log.i(TAG, salesOrder.toString());
                        }

                        Log.i("Sales_order_data",""+salesOrder.getData().size());
                        //loop statement
                        SalesOrderAdapter salesOrderAdapter = new SalesOrderAdapter(mContext,1,salesOrder.getData());
                        listView.setAdapter(salesOrderAdapter);
                    }catch (Exception e){
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
//                loading.dismiss();
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
