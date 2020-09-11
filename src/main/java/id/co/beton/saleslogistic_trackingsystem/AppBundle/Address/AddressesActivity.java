package id.co.beton.saleslogistic_trackingsystem.AppBundle.Address;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import id.co.beton.saleslogistic_trackingsystem.Adapter.packingslip.AddressesAdapter;
import id.co.beton.saleslogistic_trackingsystem.Model.Customer;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class AddressesActivity
 */
public class AddressesActivity extends AppCompatActivity {

    private Context context;
    private ListView lvAddress;
    String customerCode;
    String customerName;
    AddressesAdapter addressesAdapter;
    Customer customer;
    private static final int REQUEST_ADD_ADDRESS=1;
    private double lat=0,lng=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addresses);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        customerCode = getIntent().getStringExtra("cust_code");
        customerName = getIntent().getStringExtra("cust_name");

        setTitle(customerName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //customerName = inBundle.get("customer_name").toString();
        context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_address);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context,AddAddressActivity.class);
                intent.putExtra("cust_name",customerName);
                intent.putExtra("cust_code",customerCode);
                startActivityForResult(intent,REQUEST_ADD_ADDRESS);
            }
        });
        lvAddress = (ListView) findViewById(R.id.lv_address);

        loadData();
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

    /**
     * load data customer detail from server
     */
    private void loadData(){
        UserUtil userUtil = UserUtil.getInstance(context);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.customerDetail("JWT " + userUtil.getJWTTOken(), customerCode);

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if(response.body()!=null && response.body().getError()==0){
                    Gson gson =new Gson();
                    customer = gson.fromJson(response.body().getData().toString(), Customer.class);
                    addressesAdapter = new AddressesAdapter(context,1,customer.getListAddress());
                    lvAddress.setAdapter(addressesAdapter);

                    lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent = new Intent();
                            intent.putExtra("lat", lat);
                            intent.putExtra("lng", lng);
                            intent.putExtra("detail_address", customer.getListAddress().get(i));
                            setResult(Activity.RESULT_OK,intent);
                            finish();
                        }
                    });
                }else {
                    Toasty.info(context,"Terjadi kesalahan server").show();
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toasty.error(context,"Terjadi kesalahan server").show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_ADD_ADDRESS && resultCode==Activity.RESULT_OK){
            String address =data.getStringExtra("new_address");
            lat= data.getDoubleExtra("lat",0);
            lng= data.getDoubleExtra("lng",0);
            customer.getListAddress().add(address);
            addressesAdapter.notifyDataSetChanged();
        }
    }
}
