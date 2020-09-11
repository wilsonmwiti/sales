package id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.RequestOrder;

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
import id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.RequestOrderAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.DataRequestOrder;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class RequestOrderFragment
 * Fragment request order
 */
public class RequestOrderFragment extends Fragment {
    private static final String TAG = RequestOrderFragment.class.getSimpleName();
    private TextView text_view_for_tab_selection;
    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private DataRequestOrder requestOrder;
    private SwipeRefreshLayout swipeContainer;
    DialogCustom dialogCustom;
    private Context context;

    public RequestOrderFragment() {
    }

    public static RequestOrderFragment newInstance(String tabSelected) {
        RequestOrderFragment fragment = new RequestOrderFragment();
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

        try{
            context =container.getContext();
            dialogCustom = new DialogCustom(context);

            loadData(view,getContext());
            listView = (ListView)view.findViewById(R.id.lv_payment_and_order);


            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    try{
                        Gson gson               = new Gson();
                        String requestOrderVal    = gson.toJson(requestOrder.getData().get(i));
                        Intent intent = new Intent(getContext(), DetailRequestOrderActivity.class);
                        intent.putExtra("request_order",requestOrderVal);
                        startActivity(intent);
                    }catch (NumberFormatException e){
                        Log.e(TAG,e.getMessage());
                    }

                }
            });

            // Lookup the swipe container view
            swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);

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
            // Setup refresh listener which triggers new data loading
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData(view,getContext());
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    /**
     * get data request order from server
     * @param view
     * @param mContext
     */
    private void loadData(final View view, final Context mContext){
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(getContext());

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        Call<ResponseObject> call = service.requestOrder("JWT "+userUtil.getJWTTOken(), ""+ PlanUtil.getInstance(getActivity().getApplicationContext()).getPlanId());

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
                        requestOrder = gson.fromJson(response.body().getData().toString(), DataRequestOrder.class);

                        if(Constants.DEBUG){
                            Log.i(TAG, requestOrder.toString());
                        }

                        RequestOrderAdapter requestOrderAdapter =new RequestOrderAdapter(mContext,1, requestOrder.getData());
                        listView.setAdapter(requestOrderAdapter);
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
                swipeContainer.setRefreshing(false);
                Toasty.error(getContext(),"Terjadi kesalahan server").show();
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
