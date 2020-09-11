package id.co.beton.saleslogistic_trackingsystem.AppBundle.Customer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.AddNewVisitPlanAdapter;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.AddNewVisitPlanSearchedAdapter;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.MainActivity;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.VisitPlan.AddNewVisitPlan;
import id.co.beton.saleslogistic_trackingsystem.BaseActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Customer;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class MyCustomerFragment
 *
 */

public class SearchMycustomerVisitActivity extends BaseActivity
        implements AddNewVisitPlanSearchedAdapter.AdapterCallback {

    private int REQUEST_CODE = 1001;
    private static final String TAG = SearchMycustomerVisitActivity.class.getSimpleName();

    private TextView tvHeaderSearchMycustomer;
    private EditText etSearchedCustomer;

    private Button btn_search;

    private String searchedCustomer;

    private ListView lvSearchedMycustomer;

    private Context context;
    private DialogCustom dialogCustom;
    private LayoutInflater inflater;

    protected Activity activity;
    private UserUtil userUtil;
    private ApiInterface service;

    private static List<Customer> listCustomers;

    private void setListCustomers(List<Customer> list){
        this.listCustomers = list;
    }

    private List<Customer> getListCustomers(){
        return listCustomers;
    }

    public static Customer customerChoosed;

    public void setCustomerChoosed(Customer customer){
        this.customerChoosed = customer;
    }

    public static Customer getCustomerChoosed(){
        return customerChoosed;
    }

    private List<String> customerExist = new ArrayList<>();
    private AddNewVisitPlanSearchedAdapter addNewVisitPlanSearchedAdapter;


    public static int totalCustomer;
    public static int selected;
    public static String customerNewVisit;

    public static void setCustomerNewVisit(String customer){
        customerNewVisit = customer;
    }

    public static String getCustomerNewVisit(){
        return customerNewVisit;
    }

    public void setTotalCustomer(int total){
        totalCustomer = total;
    }

    public int getTotalCustomer(){
        return totalCustomer;
    }

    private void setSearchedCustomer(String customer){
        this.searchedCustomer = customer;
    }

    private String getSearchedCustomer(){
        return searchedCustomer;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        customerExist = getIntent().getStringArrayListExtra("customerExist");

        setContentView(R.layout.activity_search_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Search Customer visit");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = SearchMycustomerVisitActivity.this;
        userUtil = UserUtil.getInstance(context);
        dialogCustom = new DialogCustom(context);
        service = ApiClient.getInstance(context);

        etSearchedCustomer = (EditText) findViewById(R.id.et_search_customer);
        lvSearchedMycustomer = (ListView) findViewById(R.id.lv_search_all_mycustomer);

        inflater = getLayoutInflater();
        ViewGroup header = (ViewGroup)inflater.inflate(R.layout.header_searched_mycustomer, lvSearchedMycustomer,false);
        tvHeaderSearchMycustomer = (TextView) header.findViewById(R.id.tv_header_searched_mycustomer);

        lvSearchedMycustomer.addHeaderView(header);
        lvSearchedMycustomer.setVisibility(View.GONE);

        btn_search = (Button) findViewById(R.id.btn_search_customer);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputIsValid()){
                    loadData(true);
                }
            }
        });
    }

    private boolean inputIsValid(){
        Boolean valid = true;
        setSearchedCustomer(etSearchedCustomer.getText().toString());
        if(getSearchedCustomer().contains("\"") || getSearchedCustomer().contains("\'")){
            Toasty.error(context, "input tidak valid");
            valid = false;
        }
        if(getSearchedCustomer().equals("")){
            Toasty.error(context, "isi dulu nama customer").show();
            valid = false;
        }
        if(getSearchedCustomer().length() < 4){
            Toasty.error(context, "kata pencarian minimal 4 karakter").show();
            valid = false;
        }
        return valid;
    }

    // custom function
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * load data customer
     * @param showLoading
     */
    private void loadData(boolean showLoading){
        dialogCustom.show();
        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);
        //custom function
        String search = getSearchedCustomer();
        JsonObject data = new JsonObject();
        data.addProperty("username", Constants.APPS);
        data.addProperty("api_key", Constants.API_KEY);
        data.addProperty("username_id", userUtil.getId());
        data.addProperty("search", search);

        Call<ResponseObject> call = service.searchMycustomer(data);

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body() != null && response.body().getError() == 0) { //if sukses
                    try{
                        Gson gson = new Gson();
                        JsonObject jsonObject = response.body().getData();
                        JsonArray arrData = jsonObject.getAsJsonArray("mycustomer");
                        if(arrData.size() >0){
                            listCustomers = new ArrayList<>();
                            for(int i=0; i<arrData.size(); i++){
                                Customer customer = gson.fromJson(arrData.get(i), Customer.class);
                                if(!customerExist.contains(customer.getCode())){
                                    listCustomers.add(customer);
                                }
                            }

                            setTotalCustomer(listCustomers.size());
                            String titleHeader = "showing " + getTotalCustomer() + " result";
                            if(listCustomers.size() > 0){
                                // set adapter
                                addNewVisitPlanSearchedAdapter = new AddNewVisitPlanSearchedAdapter(context, 1, listCustomers);
                                addNewVisitPlanSearchedAdapter.setAdapterCallback((SearchMycustomerVisitActivity.this));
                                lvSearchedMycustomer.setAdapter(addNewVisitPlanSearchedAdapter);
                                lvSearchedMycustomer.setVisibility(View.VISIBLE);
                            }
                            tvHeaderSearchMycustomer.setText(titleHeader);
                        }else{
                            Toasty.info(context, "data tidak ditemukan.. \nperiksa kembali keyword yang digunakan", Toast.LENGTH_SHORT).show();
                        }

                        dialogCustom.hidden();
                    } catch (Exception e){
                        dialogCustom.hidden();
                        Toasty.error(context, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        dialogCustom.hidden();
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        dialogCustom.hidden();
                        Toasty.error(context, "Gagal mengambil data...", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                dialogCustom.hidden();
                Toasty.error(context, "Kesalahan server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * send data new customer to server
     */

    private void confirmAddNewVisit(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Konfirmasi");
        String message = "Apakah Anda akan menambahkan kunjungan baru ke " + getCustomerChoosed().getNama();
        alertDialogBuilder
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        submitCustomer(getCustomerChoosed());
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void submitCustomer(Customer customer){
        PlanUtil planUtil = PlanUtil.getInstance(context);
        JsonObject data = new JsonObject();
        data.addProperty("code", customer.getCode());

        Log.d(TAG, data.toString());
        Log.d(TAG, String.valueOf(planUtil.getPlanId()));

        /*Create handle for the RetrofitInstance interface*/
        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseArrayObject> call = service.addNewRoute("JWT " + userUtil.getJWTTOken(), planUtil.getPlanId(), data);
        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                if(response.body()!=null && response.body().getError()==0){
                    Toasty.info(context, "Selesai menambah kunjungan baru").show();
//                    setResult(RESULT_OK, null);
//                    finish();

                    refresh();
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                Toasty.error(context, "Kesalahan server").show();
            }
        });
    }

    private void refresh(){
        Intent refresh = new Intent(this, MainActivity.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(refresh);
    }

    @Override
    public void onMethodCallback(Customer customer) {
        Log.d("method", "call from callback " + customer.getNama());
        setCustomerChoosed(customer);
        confirmAddNewVisit();
    //        submitCustomer(customer);
    }
}
