package id.co.beton.saleslogistic_trackingsystem.AppBundle;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import id.co.beton.saleslogistic_trackingsystem.AppBundle.Customer.AddCustomerActivity;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Customer.MyCustomerFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Inbox.InboxFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.OrderAndPaymentFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.PackingSlip.PackingSlipFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Performance.PerformanceFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Plan.PlanFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.RouteFragment;
import id.co.beton.saleslogistic_trackingsystem.BaseActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.Plan;
import id.co.beton.saleslogistic_trackingsystem.ProfileActivity;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Services.AllService;
import id.co.beton.saleslogistic_trackingsystem.Services.BreakTime;
import id.co.beton.saleslogistic_trackingsystem.Services.VisitService;
import id.co.beton.saleslogistic_trackingsystem.StateActivity;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class MainActivity
 * extend to BaseActivity if not using NFC
 *
 */
// public class MainActivity extends NfcActivity
public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnFragmentInteractionListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TextView textUsername, textNik,textPh, textVersionApp;
    private ImageView imgUser;
    private BreakTime breakTimeService;
    private AllService allService;
    private int REQUEST_CODE = 100;
    private UserUtil userUtil;
    private Plan plan=null;
    private PlanUtil planUtil;
    private String myVersionName;
    public Boolean SHOW_MYCUSTOMER_PAGE = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            setContentView(R.layout.activity_main);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            context  = this;
            activity = MainActivity.this;

            // type =1;

            //check break time services
            breakTimeService = new BreakTime(context);
            userUtil = UserUtil.getInstance(context);

            planUtil = PlanUtil.getInstance(context);
            if(userUtil.getStatusBreakTime()){
                if(!isMyServiceRunning(breakTimeService.getClass())){
                    //running service
                    startServiceTimer();
                }
            }

            // load version name
            myVersionName = userUtil.getVersionApps(context);
            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            mTitle.setText("v" + myVersionName);

            allService = new AllService(context);
            if(userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE)){
                if(!isMyServiceRunning(allService.getClass())){
                    starAllService();
                }
            }
            if(userUtil.getBooleanProperty(Constants.START_VISIT_TIME_SERVICE)){
                VisitService visitService = new VisitService();
                if(!isMyServiceRunning(visitService.getClass())){
                    startVisitService();
                }
            }
            // check gps enable
            LocationManager locationManager =(LocationManager) getSystemService(LOCATION_SERVICE);

            enableGps();
            getLastKnownLocation();

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) { //handling data payload firebasea
                Log.e(TAG,"ada nilai dr inten");

                if(bundle.getString("refresh").equals("ok")){
                    SHOW_MYCUSTOMER_PAGE = true;
                }
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            //hidden packing slip jika login driver
            Menu nav_Menu = navigationView.getMenu();

            if(Constants.DEBUG){
                Log.d(TAG, "Role : " + userUtil.getRoleUser());
            }

            // kondisi sales
            if(userUtil.getRoleUser().equals(Constants.ROLE_SALES)){
                //hidden menu if mobile_privilage not sales
                nav_Menu.findItem(R.id.nav_packing_slip).setVisible(false);
                nav_Menu.findItem(R.id.nav_customer).setVisible(true);
            }else{
                showDialogUnrelevant();
            }

            // Hide menu add customer untuk demo
            // TODO(irfan): nanti di unhide ketika sudah lancar
            nav_Menu.findItem(R.id.nav_customer).setVisible(false);
            nav_Menu.findItem(R.id.nav_order_and_payment).setVisible(false);


            try{
                View header = navigationView.getHeaderView(0);
                textUsername = (TextView) header.findViewById(R.id.txt_nama_user);
                textUsername.setText(userUtil.getName());

                textUsername.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gotoChangePassword();
                    }
                });
                textNik = (TextView) header.findViewById(R.id.txt_nik_user);
                textNik.setText(userUtil.getNip());
                textNik.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gotoChangePassword();
                    }
                });
                textPh = (TextView) header.findViewById(R.id.txt_ph_user);
                textPh.setText(userUtil.getPhone());

                textPh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gotoChangePassword();
                    }
                });
                imgUser = (ImageView) header.findViewById(R.id.img_foto_user);
                imgUser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        gotoChangePassword();
                    }
                });

                textVersionApp = (TextView) header.findViewById(R.id.version_app);
                textVersionApp.setText("v" + myVersionName);

                homeFragment();

                checkRunTimePermission();
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void gotoChangePassword(){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {

        /**
         * custom code
         */
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                if (!fragment.isVisible()) continue;

                if (fragment instanceof MyCustomerFragment && ((MyCustomerFragment) fragment).onBackPressed()) {
                    refreshPage();
                }
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment = null;
        Class fragmentClass = null;
        Bundle args = new Bundle();

        if(id == R.id.nav_mycustomer){
            fragmentClass = MyCustomerFragment.class;
            args.putString("refresh", "no");
        }else if (id == R.id.nav_routes) {
            fragmentClass = RouteFragment.class;
            args.putInt("position",0);
            args.putString("tab","Import");
        }else if (id == R.id.nav_order_and_payment) {
            fragmentClass = OrderAndPaymentFragment.class;
            args.putString("tab","RequestOrder And PaymentDummy");
        }else if (id == R.id.nav_packing_slip) {
            fragmentClass = PackingSlipFragment.class;
            args.putString("tab","RequestOrder And PaymentDummy");
        }else if (id == R.id.nav_inbox) {
            fragmentClass = InboxFragment.class;
            args.putString("tab","inbox");
        }else if (id == R.id.nav_performance) {
            fragmentClass = PerformanceFragment.class;
            args.putString("tab","RequestOrder And PaymentDummy");
        }else if(id == R.id.nav_customer){
            Intent intent=new Intent(context, AddCustomerActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }else if (id == R.id.nav_signout) {
            if(Constants.DEBUG){
                logout("JWT "+UserUtil.getInstance(context).getJWTTOken());
            }else{
                if(UserUtil.getInstance(context).getBooleanProperty(Constants.STATUS_IN_OFFICE) ||
                        !UserUtil.getInstance(context).getBooleanProperty(Constants.STATUS_OUT_OFFICE)){
                    logout("JWT "+UserUtil.getInstance(context).getJWTTOken());
                }else{
                    Toasty.info(context,"Anda belum kembali di kantor").show();
                }
            }
        }else if (id == R.id.nav_start_nfc) {
            if(PlanUtil.getInstance(context).getPlanId()!=0){
                checkDataInvoice();
            } else {
                Intent intent=new Intent(context, StateActivity.class);
                intent.putExtra("type_state", 3);
                startActivityForResult(intent, REQUEST_CODE);

            }
        }else if (id == R.id.nav_end_nfc) {
            // Intent intent=new Intent(context,TapNfcActivity.class);
            // intent.putExtra("type_tap_nfc",4);
            Intent intent=new Intent(context, StateActivity.class);
            intent.putExtra("type_state", 4);
            startActivityForResult(intent, REQUEST_CODE);
            // startActivity(intent);
        }else if (id == R.id.nav_checkin_nfc) {
            Intent intent=new Intent(context, StateActivity.class);
            intent.putExtra("type_state", 5);
            startActivityForResult(intent, REQUEST_CODE);
            // startActivity(intent);
        }else if (id == R.id.nav_checkout_nfc) {
            Intent intent=new Intent(context, StateActivity.class);
            intent.putExtra("type_state", 6);
            startActivityForResult(intent, REQUEST_CODE);
            // startActivity(intent);
        }

        if(fragmentClass!=null){
            try {
                fragment = (Fragment) fragmentClass.newInstance();
                fragment.setArguments(args);
                // Insert the fragment by replacing any existing fragment
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.fragment_layout_for_activity_navigation, fragment).addToBackStack(null).commit();
            } catch (Exception e) {
                Log.e(MainActivity.class.getSimpleName(),e.toString());
                e.printStackTrace();
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentMessage(int TAG, String data) {

    }


    public void homeFragment(){
        try {
            Bundle args = new Bundle();
            Class fragmentClass;
            if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER) && planUtil.getPlanId()==0){
                fragmentClass = PlanFragment.class;
                args.putString("route","Welcome");
                if(getIntent().getAction()!=null){
                    args.putInt("position",3);
                }else{
                    args.putInt("position",0);
                }
                if(getIntent().hasExtra("position")){
                    args.putInt("position",getIntent().getExtras().getInt("position"));
                }
            } else {

                /*
                * check if need refresh mycustomer fragment
                * */

                fragmentClass = RouteFragment.class;

                if(SHOW_MYCUSTOMER_PAGE){
                    Log.d(TAG, "detach mycustomer fragment");
                    // Reload current fragment

                    fragmentClass = MyCustomerFragment.class;

                    try {
                        Fragment fragment;
                        fragment = (Fragment) fragmentClass.newInstance();
                        fragment.setArguments(args);

                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .remove(fragment)
                                .commit();
                    } catch (Exception e) {
                        Log.e(MainActivity.class.getSimpleName(),e.toString());
                        e.printStackTrace();
                    }

                    SHOW_MYCUSTOMER_PAGE = false;
                }
                args.putString("route","Welcome");

                if(getIntent().getAction()!=null){
                    args.putInt("position",3);
                }else{
                    args.putInt("position",0);
                }

                if(getIntent().hasExtra("position")){
                    args.putInt("position",getIntent().getExtras().getInt("position"));
                }
            }

            Fragment fragment = (Fragment) fragmentClass.newInstance();
            fragment.setArguments(args);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_layout_for_activity_navigation, fragment).commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    /**
     * process to Tap Activity
     * show info if invoice not confirm
     */
    private void processTapActivity(){
        if(userUtil.getBooleanProperty(Constants.STATUS_CONFIRM_INVOICE)){
            Intent intent=new Intent(context, StateActivity.class);
            intent.putExtra("type_state", 3);
            startActivityForResult(intent, REQUEST_CODE);
        } else {
            if(userUtil.getRoleUser().equals(Constants.ROLE_SALES)){
                Toasty.info(context,"Lakukan Konfirmasi Invoice Terlebih Dahulu").show();
            }else{ //logisticts
                Toasty.info(context,"Lakukan Konfirmasi Packing Slip Terlebih Dahulu").show();
            }
        }
    }

    /**
     * check data invoice before tap start
     * to prevent any invoice created after sales login to mobile
     */
    private void checkDataInvoice(){
        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);
        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call;
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            call = service.getDeliveryPlan("JWT " + userUtil.getJWTTOken(), planUtil.getPlanId());
        } else {
            call = service.visitPlan("JWT " + userUtil.getJWTTOken());
        }

        /*Log the URL called*/
        if (Constants.DEBUG) {
            Log.i("URL Called", call.request().url() + "");
        }

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                try{
                    // Gson gson = new Gson();
                    // final Plan plan = gson.fromJson(response.body().getData().toString(), Plan.class);

                    if(response.body().getData().get("is_use_route").getAsInt()==1){
                        Gson gson = new Gson();
                        plan = gson.fromJson(response.body().getData().toString(), Plan.class);
                    } else {
                        GsonBuilder builder = new GsonBuilder();
                        builder.excludeFieldsWithoutExposeAnnotation();
                        Gson gson = builder.create();
                        plan = gson.fromJson(gson.toJson(response.body().getData()), Plan.class);
                    }

                    if(userUtil.getRoleUser().equals(Constants.ROLE_SALES)){
                        if(Constants.DEBUG){
                            System.out.println("Invoice ID = " + plan.getInvoiceId().size());
                            System.out.println("Prev Invoice Size = " + PlanUtil.getInstance(context).getInvoiceSize());
                            System.out.println("Status Confirm Invoice = " + userUtil.getBooleanProperty(Constants.STATUS_CONFIRM_INVOICE));
                        }
                        // todo check size prev invoice
                        if(PlanUtil.getInstance(context).getInvoiceSize() == 0){

                            if(plan.getInvoiceId() != null && plan.getInvoiceId().size() > 0){
                                userUtil.setBooleanProperty(Constants.STATUS_CONFIRM_INVOICE,false);
                                Toasty.info(context,"Terdapat Invoice Baru, Lakukan Konfirmasi Terlebih Dahulu").show();
                                refreshPage();
                            }
                        }
                        processTapActivity();

                    } else {
                        if(Constants.DEBUG){
                            System.out.println("Packing Slip ID = " + plan.getPackingSlipsId().size());
                            System.out.println("Prev Packing Slip Size = " + PlanUtil.getInstance(context).getPackingSlipSize());
                            System.out.println("Status Confirm Invoice = " + userUtil.getBooleanProperty(Constants.STATUS_CONFIRM_INVOICE));
                        }
                        // todo check size prev packingslip
                        if(PlanUtil.getInstance(context).getPackingSlipSize() == 0){

                            if (plan.getPackingSlipsId() != null && plan.getPackingSlipsId().size() > 0){
                                userUtil.setBooleanProperty(Constants.STATUS_CONFIRM_INVOICE,false);
                                Toasty.info(context,"Terdapat Packing Slip Baru, Lakukan Konfirmasi Terlebih Dahulu").show();
                                refreshPage();
                            }
                        }
                        processTapActivity();
                    }

                } catch (Exception e){
                    e.printStackTrace();
                    Toasty.error(context, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toasty.error(context, "Kesalahan server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshPage(){
        // loading page
        Intent refresh = new Intent(this, MainActivity.class);
        refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(refresh);
        this.finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            // loading page
            Log.d("RESULTCODE", "refresh");
            Intent refresh = new Intent(this, MainActivity.class);
            refresh.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(refresh);
            this.finish();
        }
    }

    private void showDialogUnrelevant(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        StringBuilder message = new StringBuilder();
        message.append("Hanya untuk divisi sales");

        alertDialogBuilder.setTitle("Application Unrelevant");
        alertDialogBuilder
                .setMessage(message.toString())
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        logout("JWT "+UserUtil.getInstance(context).getJWTTOken());
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


}