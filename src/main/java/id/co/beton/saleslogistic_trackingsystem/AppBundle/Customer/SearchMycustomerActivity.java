package id.co.beton.saleslogistic_trackingsystem.AppBundle.Customer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
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
import id.co.beton.saleslogistic_trackingsystem.Adapter.customer.ListMycustomerAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Customer;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class MyCustomerFragment
 *
 */

public class SearchMycustomerActivity extends AppCompatActivity {
    private int REQUEST_CODE = 1001;
    private static final String TAG = SearchMycustomerActivity.class.getSimpleName();

    private TextView tvHeaderSearchMycustomer;
    private EditText etSearchedCustomer;

    private Button btn_search;

    private String searchedCustomer;

    private ListView lvSearchedMycustomer;

    private Context context;
    private DialogCustom dialogCustom;
    private LayoutInflater inflater;

    private UserUtil userUtil;

    private static List<Customer> listCustomers;
    private ListMycustomerAdapter listSearchedMycustomerAdapter;

    protected Activity activity;

    public static int totalCustomer;

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

        setContentView(R.layout.activity_search_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Search Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = SearchMycustomerActivity.this;
        userUtil = UserUtil.getInstance(context);
        dialogCustom = new DialogCustom(context);
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
                                listCustomers.add(customer);
                            }
                            setTotalCustomer(listCustomers.size());
                            String titleHeader = "showing " + getTotalCustomer() + " result";
                            if(listCustomers.size() > 0){
                                // set adapter
                                listSearchedMycustomerAdapter = new ListMycustomerAdapter(context, 1, 0, false, listCustomers);
                                lvSearchedMycustomer.setAdapter(listSearchedMycustomerAdapter);
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

}
