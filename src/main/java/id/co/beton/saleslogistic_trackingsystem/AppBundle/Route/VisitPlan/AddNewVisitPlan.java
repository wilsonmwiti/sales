package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.VisitPlan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.AddNewVisitPlanAdapter;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.AddNewVisitPlanAdapterMycustomer;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Customer.SearchMycustomerActivity;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Customer.SearchMycustomerVisitActivity;
import id.co.beton.saleslogistic_trackingsystem.BaseActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Customer;
import id.co.beton.saleslogistic_trackingsystem.Model.Mycustomer;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.DetectFakeGPS;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class AddNewVisitPlan
 * add nearby customer to destination
 */
public class AddNewVisitPlan extends BaseActivity {
    private static final String TAG = AddNewVisitPlan.class.getSimpleName();
    public ListView lvNewCustomer;
    private static Button btnSubmitCustomer, btnSearch;
    private static TextView tvNewCustomer;
    private static LinearLayout llNewVisitCustomer;
    private Context context;
    LayoutInflater inflater;

    protected Activity activity;
    private UserUtil userUtil;
    private ApiInterface service;

    protected double lat = 0, lng = 0;
    protected Location location = null;
    public Location currentLocation;
    private LocationManager mLocationManager = null;
    protected LocationRequest locationRequest;
    private FusedLocationProviderClient mFusedLocationClient;

    public static int selected;

//    public static List<Customer> listCustomers;
    public static List<Mycustomer> listCustomers;

    private List<String> customerExist = new ArrayList<>();
//    private AddNewVisitPlanAdapter addNewVisitPlanAdapter;
    private AddNewVisitPlanAdapterMycustomer addNewVisitPlanAdapter;
    private String myVersionName;
    private static int REQUEST_CODE = 1001;
    private DialogCustom dialogCustom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_visit_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        context = getApplicationContext();
        context = AddNewVisitPlan.this;
        userUtil = UserUtil.getInstance(context);
        service = ApiClient.getInstance(context);
        dialogCustom = new DialogCustom(context, "mohon tunggu, sedang menghubungi server..");

        selected = -1;

        // load version name
        myVersionName = userUtil.getVersionApps(context);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("v" + myVersionName);

        customerExist = getIntent().getStringArrayListExtra("customerExist");

        Log.d("Customer Exist", customerExist.toString());

        lvNewCustomer = (ListView) findViewById(R.id.lv_detail_new_customer);
        llNewVisitCustomer = (LinearLayout) findViewById(R.id.ll_add_customer_dest);

        /* add footer to listview*/
        inflater = getLayoutInflater();
//        ViewGroup footer = (ViewGroup)inflater.inflate(R.layout.footer_search_mycustomer_visit, lvNewCustomer,false);
//        lvNewCustomer.addFooterView(footer,null,false);

        tvNewCustomer = (TextView) findViewById(R.id.tv_new_visit_customer);

        btnSubmitCustomer = (Button) findViewById(R.id.btn_add_customer);
        btnSubmitCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCustomer();
            }
        });

        location = currentLocation;
        if (location == null) {
            location = getLastLocationService();
        }
        if(Constants.DEBUG){
            Log.d(TAG, "Latitude " + location.getLatitude());
            Log.d(TAG, "Longitude " + location.getLongitude());
        }

        if (location != null) {
            if(Constants.IS_CHECK_FAKE_GPS){
                if(DetectFakeGPS.getInstance().isMockLocationOn(location, context)){
                    DetectFakeGPS.getInstance().alertMockLocation(this);
//                    Toasty.error(context, "Matikan Aplikasi Fake GPS dan Restart Device Android Anda").show();
                    return;
                }
            }
            loadDataNearby();
        } else {
            getLastKnownLocation();
            Toasty.error(context, "Lokasi tidak terbaca, pastikan GPS aktif").show();
        }
    }

    /**
     * get data nearby customer to server
     */
    private void loadDataNearby(){
        dialogCustom.show();

        /*Create handle for the RetrofitInstance interface*/
        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.customerNearby("JWT " + userUtil.getJWTTOken(), location.getLatitude(), location.getLongitude(), 100.0, 1, 15);

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, final Response<ResponseObject> response) {
                if (response.body() != null && response.body().getError() == 0) { //if sukses
                    try {
                        Gson gson = new Gson();
                        JsonObject jsonObject = response.body().getData();
                        JsonArray arrData = jsonObject.getAsJsonArray("data");
                        if(arrData.size() >0){
                            listCustomers = new ArrayList<>();
                            for(int i=0; i<arrData.size(); i++){
                                Mycustomer customer = gson.fromJson(arrData.get(i), Mycustomer.class);
                                if(!customerExist.contains(customer.getCode())){
                                    listCustomers.add(customer);
                                }
                            }

                            if(listCustomers.size() > 0){
                                btnSubmitCustomer.setVisibility(View.VISIBLE);
//                                addNewVisitPlanAdapter = new AddNewVisitPlanAdapter(context, 1, listCustomers);
                                addNewVisitPlanAdapter = new AddNewVisitPlanAdapterMycustomer(context, 1, listCustomers);

                                lvNewCustomer.setAdapter(addNewVisitPlanAdapter );
                                initSearchButton();
                            } else {
                                btnSubmitCustomer.setVisibility(View.GONE);
                                Toasty.info(context,"Tidak ada customer baru").show();
                            }

                        } else {
                            btnSubmitCustomer.setVisibility(View.GONE);
                            location = getLastLocationService();
                            Toasty.info(context, "Tidak ada customer terdekat").show();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toasty.error(context, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                }
                dialogCustom.hidden();
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                dialogCustom.hidden();
                Toasty.error(context, "Kesalahan server (failure call)", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSearchButton(){
        // Add a footer to the ListView
        Log.d("try", "init search button");
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer_search_mycustomer_visit, lvNewCustomer,false);
        lvNewCustomer.addFooterView(footer,null,false);

        btnSearch = (Button) footer.findViewById(R.id.btn_search_mycustomer);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchMycustomerVisitActivity.class);
                intent.putStringArrayListExtra("customerExist", (ArrayList<String>) customerExist);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
    }

    /**
     * send data new customer to server
     */
    public void submitCustomer(){
        if(selected < 0){
            Toasty.info(context, "Silahkan pilih satu customer").show();
        } else {
            PlanUtil planUtil = PlanUtil.getInstance(context);
            JsonObject data = new JsonObject();
            data.addProperty("code", listCustomers.get(selected).getCode());

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
                        setResult(RESULT_OK, null);
                        selected = -1;
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                    Toasty.error(context, "Kesalahan server").show();
                }
            });
        }
    }

    public static void customerSelected(int position){
        selected = position;

        String custNewVisit = listCustomers.get(selected).getNama();
        if(custNewVisit != null){
//            tvNewCustomer.setVisibility(View.VISIBLE);
            llNewVisitCustomer.setVisibility(View.VISIBLE);
            tvNewCustomer.setText(custNewVisit);

        }
        // Log.d(TAG, listCustomers.get(selected).toString());
    }

    private void clearCheckedList(int except){
        Log.d(TAG, "Clearcheckedlist _ " + listCustomers.get(selected).toString());
        for(int i = 0; i < listCustomers.size(); i++){
            if(i != except){
                lvNewCustomer.setSelected(false);
                lvNewCustomer.setItemChecked(i, false);
            }
        }
    }

    /**
     * get last know location device
     * @return
     */
    private Location getLastLocationService() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = null;
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }

            l = mLocationManager.getLastKnownLocation(provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
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
