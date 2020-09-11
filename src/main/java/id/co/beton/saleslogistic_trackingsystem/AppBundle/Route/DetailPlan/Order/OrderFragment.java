package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Order;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.order.RequestOrderAndSalesOrderAdapter;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.RequestOrder.DetailRequestOrderActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.RequestOrderAndSalesOrder;
import id.co.beton.saleslogistic_trackingsystem.Model.RequestOrderAndSalesOrderData;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class OrderFragment
 * list data request order
 */

public class OrderFragment extends Fragment {
    private static final String TAG = OrderFragment.class.getSimpleName();
    private Context context;
    private ListView lvOrderVisitPlan;
    LayoutInflater inflater;
    RequestOrderAndSalesOrderAdapter requestOrderAndSalesOrderAdapter;
    String customerName;
    String customerCode;
    String nfcCode;
    private SwipeRefreshLayout swipeContainer;
    private View header;

    public OrderFragment() {
        // Required empty public constructor
    }

    public static OrderFragment newInstance(String tabSelected) {
        OrderFragment fragment = new OrderFragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.content_detail_visit_plan_order, container, false);
        try {
            lvOrderVisitPlan = (ListView) view.findViewById(R.id.lv_order_detail_visit_plan);
            this.header = inflater.inflate(R.layout.header_request_order_and_sales_order, null);
            lvOrderVisitPlan.addHeaderView(header);

            view.setTag(TAG);

            context = container.getContext();

            this.inflater = inflater;
            nfcCode = UserUtil.getInstance(context).getStringProperty(Constants.NFC_CODE);

            if (getArguments() != null) {
                this.customerCode = getArguments().getString("cust_code");
                this.customerName = getArguments().getString("cust_name");

                loadData();
            }

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

            FloatingActionButton fbAdd = (FloatingActionButton) view.findViewById(R.id.fab);
            fbAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // BUG: Pusat masalah, dimana sales / driver dapat melakukan pesanan walaupun belum di lokasi
                    /*
                     * Alasan Bug:
                     *   Sebab kode dibawah hanya membaca customerCode, dan jika customerCode dan NFC Code sama maka kode di prosses
                     * Solusi :
                     *   customerCode dan NFC Code akan di kosongkan setelah tap NFC jika sales / driver belum di lokasi customer atau karena kegagalan tap NFC.
                     */
                    boolean isTapIn = false;
                    if (Constants.DEV_MODE) {
                        isTapIn = true;
                    } else {
                        isTapIn = nfcCode.trim().contentEquals(customerCode.trim());
                    }
                    if (isTapIn) {
                        Intent intent = new Intent(getContext(), NewRequestOrderActivity.class);
                        intent.putExtra("cust_name", customerName);
                        intent.putExtra("cust_code", customerCode);
                        startActivityForResult(intent, 99);
                    } else {
                        Toasty.info(context, "Pastikan anda telah checkin di customer " + customerName).show();
                    }

                    /*Intent intent = new Intent(getContext(), NewRequestOrderActivity.class);
                    intent.putExtra("cust_name",customerName);
                    intent.putExtra("cust_code",customerCode);
                    startActivityForResult(intent,99);*/
                }
            });

            lvOrderVisitPlan.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView absListView, int i) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                    int topRowVerticalPosition = (lvOrderVisitPlan == null || lvOrderVisitPlan.getChildCount() == 0) ? 0 : lvOrderVisitPlan.getChildAt(0).getTop();
                    swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    public void loadData() {
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(context);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.salesRequestGet("JWT " + userUtil.getJWTTOken(), customerCode);

        /*Log the URL called*/
        if (Constants.DEBUG) {
            Log.i("URL Called", call.request().url() + "");
        }

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body() != null && response.body().getError() == 0) { //if sukses
                    try {
                        Gson gson = new Gson();
                        final RequestOrderAndSalesOrder requestOrderAndSalesOrder = gson.fromJson(response.body().getData().toString(), RequestOrderAndSalesOrder.class);

                        requestOrderAndSalesOrderAdapter = new RequestOrderAndSalesOrderAdapter(context, 1, requestOrderAndSalesOrder.getRequestOrderAndSalesOrderData());
                        lvOrderVisitPlan.setAdapter(requestOrderAndSalesOrderAdapter);

                        TextView tvDate = header.findViewById(R.id.tv_tanggal);
                        TextView tvNumberOfOrder = header.findViewById(R.id.tv_number_of_orders);
                        TextView tvTotalSalesOrder = header.findViewById(R.id.tv_total_sales_order);
                        tvDate.setText(ConversionDate.getInstance().today());
                        tvNumberOfOrder.setText(requestOrderAndSalesOrder.getTotalFilter() + "");
                        tvTotalSalesOrder.setText("Rp " + Currency.priceWithoutDecimal(requestOrderAndSalesOrder.getTotalAmount()));

                        lvOrderVisitPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                if (i != 0) {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(requestOrderAndSalesOrder.getRequestOrderAndSalesOrderData().get(i - 1), RequestOrderAndSalesOrderData.class);

                                    if (requestOrderAndSalesOrder.getRequestOrderAndSalesOrderData().get(i - 1).getType().equalsIgnoreCase("Request Order")) {
                                        Intent intent = new Intent(getContext(), DetailRequestOrderActivity.class);
                                        intent.putExtra("request_order", json);
                                        intent.putExtra("cust_name", customerName);
                                        intent.putExtra("cust_code", customerCode);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(getContext(), DetailVisitPlanSalesOrderActivity.class);
                                        intent.putExtra("list_request_sales_order", json);
                                        intent.putExtra("cust_name", customerName);
                                        intent.putExtra("cust_code", customerCode);
                                        startActivity(intent);
                                    }
                                }
                            }
                        });

                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeContainer.setRefreshing(false);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, e.getMessage());
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toasty.error(context, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }

                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                }

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);

                Toasty.error(context, "Terjadi kesalahan server").show();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == 99) {
            loadData();
        }
    }
}