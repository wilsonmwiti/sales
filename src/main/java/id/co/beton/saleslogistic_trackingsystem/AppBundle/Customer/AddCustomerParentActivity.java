package id.co.beton.saleslogistic_trackingsystem.AppBundle.Customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Adapter.customer.AddCustomerParentAdapter;
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

public class AddCustomerParentActivity extends AppCompatActivity {
    private static final String TAG = AddCustomerParentActivity.class.getSimpleName();
    private ListView lvCustomer;
    private Button btnSubmitCustomer;
    private DialogCustom dialogCustom;

    private UserUtil userUtil;
    private Context context;

    private static int selected = -1;
    private static List<Customer> listCustomers;
    private AddCustomerParentAdapter addCustomerParentAdapter;

    protected Activity activity;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customer_parent);

        setTitle("Search Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        EditText etSearchCustomer = (EditText) findViewById(R.id.et_search_customer);

        context = AddCustomerParentActivity.this;
        userUtil = UserUtil.getInstance(context);
        dialogCustom = new DialogCustom(context);

        lvCustomer = (ListView) findViewById(R.id.lv_detail_customer_parent);
        btnSubmitCustomer = (Button) findViewById(R.id.btn_add_customer_parent);
        btnSubmitCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCustomerParent();
            }
        });

        searchView = findViewById(R.id.search_customer);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.info(context,"test search").show();
            }
        });

        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)){
                    //Text is cleared, do your thing
                    loadData(false);
                } else {
                    addCustomerParentAdapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    //Text is cleared, do your thing
                    loadData(false);
                } else {
                    addCustomerParentAdapter.getFilter().filter(newText);
                }
                return false;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                loadData(false);
                return false;
            }
        });

        loadData(true);
    }

    private void loadData(boolean showLoading){
        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.customerAll("JWT " + userUtil.getJWTTOken(),1, 10);

        if(showLoading){
            dialogCustom.show();
        }

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body() != null && response.body().getError() == 0) { //if sukses
                    try{
                        Gson gson = new Gson();
                        JsonObject jsonObject = response.body().getData();
                        JsonArray arrData = jsonObject.getAsJsonArray("data");
                        if(arrData.size() >0){
                            listCustomers = new ArrayList<>();
                            for(int i=0; i<arrData.size(); i++){
                                Customer customer = gson.fromJson(arrData.get(i), Customer.class);
                                listCustomers.add(customer);
                            }
                            if(listCustomers.size() > 0){
                                btnSubmitCustomer.setVisibility(View.VISIBLE);
                                addCustomerParentAdapter = new AddCustomerParentAdapter(context, 1, listCustomers);
                                lvCustomer.setAdapter(addCustomerParentAdapter);
                            }
                        } else {
                            btnSubmitCustomer.setVisibility(View.GONE);
                            Toasty.info(context, "Tidak ada data customer").show();
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
                        Toasty.error(context, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
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

    public static void customerSelected(int position){
        selected = position;
    }

    private void submitCustomerParent(){
        if(selected < 0){
            Toasty.info(context, "Silahkan pilih satu customer").show();
        } else {
            Intent intent = new Intent();
            intent.putExtra("parent_customer", listCustomers.get(selected).getCode());
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}
