package id.co.beton.saleslogistic_trackingsystem;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Plan;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.DetectFakeGPS;
import id.co.beton.saleslogistic_trackingsystem.Utils.DistanceCalculator;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class StateActivity
 * this Class called when setting general of company not using NFC
 * (tap start,end,checkin,checkout Not Using nfc)
 */
public class StateActivity extends BaseActivity {
    private String TAG = StateActivity.class.getSimpleName();
    private TextView tvTitleStart;
    private int state;
    protected double lat = 0, lng = 0;
    protected Location location = null;
    protected UserUtil userUtil = UserUtil.getInstance(context);
    private ApiInterface service = ApiClient.getInstance(context);
    private LocationManager mLocationManager = null;
    private DialogCustom dialogCustom;
    private String myVersionName;
    private Plan plan = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Back");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = this;
        activity = StateActivity.this;

        // load version name
        myVersionName = userUtil.getVersionApps(context);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("v" + myVersionName);

        tvTitleStart = (TextView) findViewById(R.id.tv_title_start);
        dialogCustom = new DialogCustom(context);

        location = currentLocation;
        if (location == null) {
            location = getLastLocationService();
        }
        state = getIntent().getExtras().getInt("type_state"); //3 is start 4 is end
        if (state == 3) {
            startState();
        } else if (state == 4) {
            tvTitleStart.setText("Pengecekan state sebagai tanda aktivitas berakhir!");
            endState();
        } else if (state == 5) {
            tvTitleStart.setText("Checkin di Branch");
            checkinState();
        } else if (state == 6) {
            tvTitleStart.setText("Checkout di Branch");
            checkoutState();
        }
    }

    /**
     * function to start activity (tap start)
     * check fake gps and location with start branch
     * if valid, start all service, send location via socket
     * and send data activity to server
     */
    public void startState() {
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        float[] koordinatOfficeStart = userUtil.getKoordinatOfficeStart();
        String nfcCodeStart = userUtil.getNFCodeOfficeStart();
        if (!userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE)) {
            if (location != null) {
                if (Constants.IS_CHECK_FAKE_GPS) {
                    if (DetectFakeGPS.getInstance().isMockLocationOn(location, context)) {
                        DetectFakeGPS.getInstance().alertMockLocation(this);
                        getLastKnownLocation();
//                        tvTitleStart.setText("Matikan Aplikasi Fake GPS dan Restart Device Android Anda");
//                        Toasty.error(context, "Matikan Aplikasi Fake GPS dan Restart Device Android Anda").show();
                        setResult(RESULT_CANCELED, null);
                        finish();
                    }
                }
                if (koordinatOfficeStart[0] != 0) {
                    // kondisi ketika visit plan sudah digenerate oleh Admin
                    double jarak = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(koordinatOfficeStart[0], koordinatOfficeStart[1]));
                    if (jarak < Constants.TOLERANSI_JARAK) {
                        setStateStart(false, nfcCodeStart);
                    } else {
                        if(userUtil.getRoleUser().equals(Constants.ROLE_SALES)){
                            showDialogConfirmation(4, nfcCodeStart);
                        } else {
                            getLastKnownLocation();
                            Toasty.error(context, "Jarak terlalu jauh dari kantor").show();
                            setResult(RESULT_CANCELED, null);
                            finish();
                        }
                    }
                } else {
                    if(userUtil.getRoleUser().equals(Constants.ROLE_SALES)){
                        // kondisi ketika belum ada visit plan dan user Tap Start
                        // kondisi ketika visit plan belum ter-generate
                        // TODO Generate Visit Plan
                        showDialogConfirmation(1, "");
                    } else {
                        Toasty.error(context, "Silahkan load halaman visit plan", Toasty.LENGTH_LONG).show();
                        setResult(RESULT_CANCELED, null);
                        finish();
                    }
                }
            } else {
                getLastKnownLocation();
                Toasty.error(context, "Lokasi tidak terbaca, pastikan GPS aktif").show();
                setResult(RESULT_CANCELED, null);
                finish();
            }

        } else {
            Toasty.error(context, "Anda sudah Melakukan Start").show();
            setResult(RESULT_CANCELED, null);
            finish();
        }
    }

    /**
     *
     */
    public void endState() {
        if (userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE)) {
            if (location != null) {
                if (Constants.IS_CHECK_FAKE_GPS) {
                    if (DetectFakeGPS.getInstance().isMockLocationOn(location, context)) {
                        DetectFakeGPS.getInstance().alertMockLocation(this);
//                        getLastKnownLocation();
//                        tvTitleStart.setText("Matikan Aplikasi Fake GPS dan Restart Device Android Anda");
//                        Toasty.error(context, "Matikan Aplikasi Fake GPS dan Restart Device Android Anda").show();
                        setResult(RESULT_CANCELED, null);
                        finish();
                    }
                }
                DistanceCalculator distanceCalculator = new DistanceCalculator();

                // cek koordinat with office if visit plan generated by admin from frontend
                // if visit plan generated by user it self, disable checking distance user with branch
                float[] koordinatOfficeEnd = userUtil.getKoordinatOfficeEnd();
                String nfcCodeEnd = userUtil.getNFCodeOfficeEnd();
                PlanUtil planUtil = PlanUtil.getInstance(context);
                if(planUtil.isPlanCustome()){
                    showDialogConfirmation(2, nfcCodeEnd);
                } else {
                    if (koordinatOfficeEnd[0] != 0) {
                        double jarak = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(koordinatOfficeEnd[0], koordinatOfficeEnd[1]));
                        if (jarak < Constants.TOLERANSI_JARAK) {
                            showDialogConfirmation(2, nfcCodeEnd);
                        } else {
                            // dibuka untuk semua sales dan logistik
                            // bisa tap end dimana saja
                            if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER) || userUtil.getRoleUser().equals(Constants.ROLE_SALES)){
                                showDialogConfirmation(3, nfcCodeEnd);
                            } else {
                                getLastKnownLocation();
                                Toasty.error(context, "Jarak terlalu jauh dari kantor").show();
                                setResult(RESULT_CANCELED, null);
                                finish();    
                            }
                        }
                    } else {
                        Toasty.error(context, "Silahkan load halaman visit plan").show();
                        setResult(RESULT_CANCELED, null);
                        finish();
                    }
                }
            } else {
                Toasty.error(context, "Lokasi tidak terbaca, pastikan GPS aktif").show();
                setResult(RESULT_CANCELED, null);
                finish();
            }
        } else {
            Toasty.error(context, "Anda belum melakukan Start").show();
            setResult(RESULT_CANCELED, null);
            finish();
        }
    }

    private void setStateStart(boolean customTapStart, String nfcCode) {
        userUtil.setBooleanProperty(Constants.STATUS_OUT_OFFICE, true);
        userUtil.setBooleanProperty(Constants.STATUS_IN_OFFICE, false);

        // set default variable for calculate distance service
        userUtil.setOdometer(0);
        userUtil.setDistancePoint(0);
        userUtil.setDistanceLastLocation(location.getLatitude(), location.getLongitude());

        postState("START", nfcCode, customTapStart);
        starAllService();
        userUtil.setNfcCodeRoute(nfcCode);

        //set user
        Socket socketUserSet;
        try {
            socketUserSet = IO.socket(Constants.API_SERVER_ADDR);
            socketUserSet.connect();
            JSONObject jsonObject = new JSONObject();
            if (location != null) {
                if (Constants.DEBUG) {
                    Log.i("lat", location.getLatitude() + "");
                    Log.i("lng", location.getLongitude() + "");
                }
                jsonObject.put("lat", location.getLatitude());
                jsonObject.put("lng", location.getLongitude());
            } else {
                jsonObject.put("lat", "9999");
                jsonObject.put("lng", "999");
            }

            jsonObject.put("user_id", userUtil.getId());
            jsonObject.put("name", userUtil.getName());
            jsonObject.put("job_function", userUtil.getRoleUser());
            jsonObject.put("branch_id", userUtil.getBranchId());
            jsonObject.put("division_id", userUtil.getDivisionId());

            // add asset information
            jsonObject.put("asset_code", "");
            jsonObject.put("asset_name", "");
            if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
                String[] asset =  PlanUtil.getInstance(context).getAsset();
                jsonObject.put("asset_code", asset[0]);
                jsonObject.put("asset_name", asset[1]);
            }

            socketUserSet.emit("user-set", jsonObject);

            Toasty.info(context, "Start Berhasil").show();
            tvTitleStart.setText("Start dimulai");
            setResult(RESULT_OK, null);
            finish();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setStateEnd(boolean customTapEnd, String nfcCode) {

        // check status Constants.NFC_CODE,
        // if null it means user tap out
        // if containt value it means user did not tap out, forced tapout
        if(!userUtil.getStringProperty(Constants.NFC_CODE).equals("")){
            System.out.println("user tidak tap out di customer = " + userUtil.getStringProperty(Constants.NFC_CODE));
            // forcedTapout(userUtil.getStringProperty(Constants.NFC_CODE));
            forcedTapoutSynchronous(userUtil.getStringProperty(Constants.NFC_CODE));
        }

        userUtil.setBooleanProperty(Constants.STATUS_OUT_OFFICE, false);
        userUtil.setBooleanProperty(Constants.STATUS_IN_OFFICE, true);

        // if custom branch, set coordinate and address custom branch
        if(PlanUtil.getInstance(context).isPlanCustome()){
            userUtil.setKoordinatCustomOfficeEnd(location.getLatitude(), location.getLongitude());
            userUtil.setAddressCustomOfficeEnd(setAddressByLatLng(location.getLatitude(), location.getLongitude()));
        }

        postState("STOP", nfcCode, customTapEnd);

        //unset user
        Socket socketUserSet;
        try {
            socketUserSet = IO.socket(Constants.API_SERVER_ADDR);
            socketUserSet.connect();
            JSONObject jsonObject = new JSONObject();
            if (location != null) {
                jsonObject.put("lat", location.getLatitude());
                jsonObject.put("lng", location.getLongitude());
            } else {
                jsonObject.put("lat", "9999");
                jsonObject.put("lng", "999");
            }

            jsonObject.put("user_id", userUtil.getId());
            jsonObject.put("name", userUtil.getName());
            jsonObject.put("job_function", userUtil.getRoleUser());
            jsonObject.put("branch_id", userUtil.getBranchId());
            jsonObject.put("division_id", userUtil.getDivisionId());

            socketUserSet.emit("user-unset", jsonObject);

            stopAllService();
            stopVisitSevice();
            userUtil.setNfcCodeRoute("");
            Toasty.info(context, "End Berhasil").show();
            tvTitleStart.setText("Aktivitas Berakhir");
            setResult(RESULT_OK, null);
            finish();

            // forced to logout
            // logout("JWT "+userUtil.getJWTTOken());

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkinState() {
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        float[] koordinatOfficeStart = userUtil.getKoordinatOfficeStart();
        String nfcCodeStart = userUtil.getNFCodeOfficeStart();
        if (userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE)) {
            if (location != null) {
                if (Constants.IS_CHECK_FAKE_GPS) {
                    if (DetectFakeGPS.getInstance().isMockLocationOn(location, context)) {
                        DetectFakeGPS.getInstance().alertMockLocation(this);
//                        getLastKnownLocation();
//                        tvTitleStart.setText("Matikan Aplikasi Fake GPS dan Restart Device Android Anda");
//                        Toasty.error(context, "Matikan Aplikasi Fake GPS dan Restart Device Android Anda").show();
                        return;
                    }
                }
                if (koordinatOfficeStart[0] != 0) {
                    double jarak = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(koordinatOfficeStart[0], koordinatOfficeStart[1]));
                    if (jarak < Constants.TOLERANSI_JARAK) {

                        // check status Constants.NFC_CODE,
                        // if null it means user tap out
                        // if containt value it means user did not tap out, forced tapout
                        if(!userUtil.getStringProperty(Constants.NFC_CODE).equals("")){
                            // forcedTapout(userUtil.getStringProperty(Constants.NFC_CODE));
                            forcedTapoutSynchronous(userUtil.getStringProperty(Constants.NFC_CODE));
                        }

                        // set Constant.NFCCode
                        userUtil.setStringProperty(Constants.NFC_CODE, nfcCodeStart);

                        userUtil.setBooleanProperty(Constants.STATUS_CHECKIN_BRANCH, true);
                        postState("IN", nfcCodeStart, false);
                        Toasty.info(context, "Checkin di Branch Berhasil").show();

                        setResult(RESULT_OK, null);
                        finish();

                    } else {
                        getLastKnownLocation();
                        Toasty.error(context, "Jarak terlalu jauh dari kantor").show();
                    }
                } else {
                    Toasty.error(context, "Silahkan load halaman visit plan").show();
                }

            } else {
                getLastKnownLocation();
                Toasty.error(context, "Lokasi tidak terbaca, pastikan GPS aktif").show();
            }
        } else {
            Toasty.error(context, "Anda belum melakukan Start").show();
        }
    }

    private void checkoutState() {
        DistanceCalculator distanceCalculator = new DistanceCalculator();
        float[] koordinatOfficeStart = userUtil.getKoordinatOfficeStart();
        String nfcCodeStart = userUtil.getNFCodeOfficeStart();
        if (userUtil.getBooleanProperty(Constants.STATUS_CHECKIN_BRANCH)) {
            if (location != null) {
                if (Constants.IS_CHECK_FAKE_GPS) {
                    if (DetectFakeGPS.getInstance().isMockLocationOn(location, context)) {
                        DetectFakeGPS.getInstance().alertMockLocation(this);
//                        getLastKnownLocation();
//                        tvTitleStart.setText("Matikan Aplikasi Fake GPS dan Restart Device Android Anda");
//                        Toasty.error(context, "Matikan Aplikasi Fake GPS dan Restart Device Android Anda").show();
                        return;
                    }
                }
                if (koordinatOfficeStart[0] != 0) {
                    double jarak = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(koordinatOfficeStart[0], koordinatOfficeStart[1]));
                    if (jarak < Constants.TOLERANSI_JARAK) {
                        userUtil.setStringProperty(Constants.NFC_CODE, "");
                        userUtil.setBooleanProperty(Constants.STATUS_CHECKOUT_BRANCH, true);
                        postState("OUT", nfcCodeStart, false);
                        Toasty.info(context, "Checkout di Branch Berhasil").show();

                        setResult(RESULT_OK, null);
                        finish();
                    } else {
                        getLastKnownLocation();
                        Toasty.error(context, "Jarak terlalu jauh dari kantor").show();
                    }
                } else {
                    Toasty.error(context, "Silahkan load halaman visit plan").show();
                }

            } else {
                getLastKnownLocation();
                Toasty.error(context, "Lokasi tidak terbaca, pastikan GPS aktif").show();
            }
        } else {
            Toasty.error(context, "Anda belum melakukan Checkin").show();
        }
    }

    private void postState(String type, String nfcCode, boolean customTapEnd) {
        PlanUtil planUtil = PlanUtil.getInstance(context);

        JsonObject data = new JsonObject();
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            data.addProperty("delivery_plan_id", planUtil.getPlanId());
        } else {
            data.addProperty("visit_plan_id", planUtil.getPlanId());
        }
        data.addProperty("tap_nfc_type", type);
        data.addProperty("nfc_code", nfcCode);
        if (planUtil.isPlanCustome() || customTapEnd) {
            JsonObject breadcrumb = new JsonObject();
            breadcrumb.addProperty("latitude", location.getLatitude());
            breadcrumb.addProperty("longitude", location.getLongitude());
            breadcrumb.addProperty("address", setAddressByLatLng(location.getLatitude(), location.getLongitude()));
            data.add("route_breadcrumb", breadcrumb);
        } else {
            data.addProperty("route_breadcrumb", "");
        }

        if (type.equals("START") || type.equals("STOP")) {
            data.addProperty("distance", "");
            data.addProperty("total_distance", userUtil.getOdometer());
        } else if (type.equals("IN")) {
            UserUtil.getInstance(context).calculateDistancePoint(location.getLatitude(), location.getLongitude());
            data.addProperty("distance", UserUtil.getInstance(context).getDistancePoint());
            data.addProperty("total_distance", "");
        } else if (type.equals("OUT")) {
            data.addProperty("distance", "");
            data.addProperty("total_distance", "");
        }


        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseArrayObject> call = null;
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            call = service.postTapNfcDriver("JWT " + userUtil.getJWTTOken(), data);
        } else {
            call = service.postTapNfcSales("JWT " + userUtil.getJWTTOken(), data);
        }

        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                if (response.body() != null && response.body().getError() == 0) {
                    // Log.d(">>> from backend", response.body().getMessage());
                    // Log.d(">>> from backend", response.body().getData().toString());

                    // Toasty.info(context, "Sukses tap NFC", 70000).show();
                    try {
                        Vibrator v = (Vibrator) getSystemService(VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(Constants.VIBRATE_DURATION, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(Constants.VIBRATE_DURATION);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                Toasty.error(context, "Gagal membaca State").show();
            }
        });
    }

    private void showDialogConfirmation(final int flag, final String code){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Konfirmasi");
        String message = "";
        if(flag==1){
            message = "Apakah Anda yakin akan memulai akivitas di lokasi sekarang ?";
        } else if(flag==2){
            message = "Apakah Anda yakin akan mengakhiri aktivitas ?";
        } else if(flag==3){
            message = "Apakah Anda yakin akan mengakhiri aktivitas di lokasi sekarang?";
        } else if(flag==4){
            message = "Apakah Anda yakin akan memulai aktivitas diluar lokasi branch?";
        }

        alertDialogBuilder
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(flag==1){
                            generateVisitPlan();
                        } else if(flag==2){
                            setStateEnd(false, code);
                        } else if(flag==3){
                            setStateEnd(true, code);
                        } else if(flag==4){
                            setStateStart(true, code);
                        }
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        setResult(RESULT_CANCELED, null);
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        
    }

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

    private String setAddressByLatLng(double lat, double lng) {
        String address = "";
        try {
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

            if (geocoder.isPresent()) {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                Address returnAddress = addresses.get(0);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < returnAddress.getMaxAddressLineIndex(); i++) {
                    sb.append(returnAddress.getAddressLine(i)).append(" ");
                }
                sb.append(returnAddress.getLocality()).append(" ");
                sb.append(returnAddress.getPostalCode()).append(" ");
                sb.append(returnAddress.getCountryName());

                if (addresses.size() > 0) {
                    address = addresses.get(0).getAddressLine(0);
                }
            } else {
                Toasty.info(context, "Geocoder not present").show();
            }
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

    /**
     * Function to generate visit plan by user sales
     * {"id":null,"user_id":35,"date":"2019-08-01","asset_id":null,"invoice_id":[],"destination":[],"destination_order":[],"end_route_branch_id":1,"route":[],"start_route_branch_id":1,"is_use_route":0}
     */
    private void generateVisitPlan() {
        JsonObject data = new JsonObject();
        data.addProperty("id", "");
        data.addProperty("user_id", UserUtil.getInstance(context).getId());
        // DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDateandTime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        data.addProperty("date", currentDateandTime);
        data.addProperty("asset_id", "");
        data.addProperty("invoice_id", "[]");
        data.addProperty("destination", "[]");
        data.addProperty("destination_order", "");
        data.addProperty("route", "");
        data.addProperty("start_route_branch_id", 1);
        data.addProperty("end_route_branch_id", 1);
        data.addProperty("is_use_route", 0);
        // System.out.println(data);
        dialogCustom.show();

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseArrayObject> call = service.postVisitPlan("JWT " + userUtil.getJWTTOken(), data);

        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                if (response.body() != null && response.body().getError() == 0) {
                    loadNewDataVisit();
                } else {
                    Toasty.error(context, "Gagal Generate Visit Plan").show();
                    dialogCustom.hidden();
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                Toasty.error(context, "Gagal Generate Visit Plan").show();
                dialogCustom.hidden();
            }
        });
    }

    private void loadNewDataVisit() {
        /*Create handle for the RetrofitInstance interface*/

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.visitPlan("JWT " + userUtil.getJWTTOken());
        ;

        /*Log the URL called*/
        if (Constants.DEBUG) {
            Log.i("URL Called", call.request().url() + "");
        }

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body() != null && response.body().getError() == 0) {
                    try {
                        if (response.body().getData().get("is_use_route").getAsInt() == 1) {
                            // using route
                            Gson gson = new Gson();
                            plan = gson.fromJson(response.body().getData().toString(), Plan.class);
                        } else {
                            // not using route <exclude key route>
                            GsonBuilder builder = new GsonBuilder();
                            builder.excludeFieldsWithoutExposeAnnotation();
                            Gson gson = builder.create();
                            plan = gson.fromJson(gson.toJson(response.body().getData()), Plan.class);
                        }

                        // TODO: Setup Variable Plan
                        PlanUtil.getInstance(context).setPlanId(plan.getId());
                        PlanUtil.getInstance(context).setIsUsingRoute(plan.getIsUseRoute());
                        PlanUtil.getInstance(context).isUsingRoute();
                        PlanUtil.getInstance(context).getPlanId();
                        // set plan type, type custom, generated by user it self
                        PlanUtil.getInstance(context).setIsPlanCustome(1);

                        // TODO: Setup Coordinate Start & End Branch
                        userUtil.setKoordinatOfficeStart(plan.getStartRouteBranch().getLat(), plan.getStartRouteBranch().getLng());
                        userUtil.setNFCodeOfficeStart(String.valueOf(plan.getStartRouteBranchId()));

                        userUtil.setKoordinatOfficeEnd(plan.getEndRouteBranch().getLat(), plan.getEndRouteBranch().getLng());
                        userUtil.setNFCodeOfficeEnd(String.valueOf(plan.getEndRouteBranchId()));

                        // TODO: Setup Coordinate and address form Custom Start
                        userUtil.setKoordinatCustomOfficeStart(location.getLatitude(), location.getLongitude());
                        userUtil.setAddressCustomOfficeStart(setAddressByLatLng(location.getLatitude(), location.getLongitude()));

                        System.out.println("Plan ID = " + plan.getId());

                        directSetActivity();

                        dialogCustom.hidden();
                    } catch (Exception e) {
                        e.printStackTrace();
                        dialogCustom.hidden();
                        Toasty.error(context, "Gagal load data visit plan.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toasty.error(context, "Gagal load data visit plan").show();
                    dialogCustom.hidden();
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toasty.error(context, "Gagal load data visit plan").show();
                dialogCustom.hidden();
            }
        });
    }

    private void directSetActivity() {
        String nfcCode = "1";
        userUtil.setBooleanProperty(Constants.STATUS_OUT_OFFICE, true);
        userUtil.setBooleanProperty(Constants.STATUS_IN_OFFICE, false);

        // set default variable for calculate distance service
        userUtil.setOdometer(0);
        userUtil.setDistancePoint(0);
        userUtil.setDistanceLastLocation(location.getLatitude(), location.getLongitude());
        postState("START", nfcCode, false);
        starAllService();
        userUtil.setNfcCodeRoute(nfcCode);

        //set user
        Socket socketUserSet;
        try {
            socketUserSet = IO.socket(Constants.API_SERVER_ADDR);
            socketUserSet.connect();
            JSONObject jsonObject = new JSONObject();
            if (location != null) {
                if (Constants.DEBUG) {
                    Log.i("lat", location.getLatitude() + "");
                    Log.i("lng", location.getLongitude() + "");
                }
                jsonObject.put("lat", location.getLatitude());
                jsonObject.put("lng", location.getLongitude());
            } else {
                jsonObject.put("lat", "9999");
                jsonObject.put("lng", "999");
            }

            jsonObject.put("user_id", userUtil.getId());
            jsonObject.put("name", userUtil.getName());
            jsonObject.put("job_function", userUtil.getRoleUser());
            jsonObject.put("branch_id", userUtil.getBranchId());
            jsonObject.put("division_id", userUtil.getDivisionId());

            // add asset information
            jsonObject.put("asset_code", "");
            jsonObject.put("asset_name", "");
            if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
                String[] asset =  PlanUtil.getInstance(context).getAsset();
                jsonObject.put("asset_code", asset[0]);
                jsonObject.put("asset_name", asset[1]);
            }

            socketUserSet.emit("user-set", jsonObject);

            Toasty.info(context, "Start Berhasil").show();
            tvTitleStart.setText("State Start dimulai");
            setResult(RESULT_OK, null);
            finish();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void forcedTapoutSynchronous(final String customerCode){
        new Thread(new Runnable(){
            @Override
            public void run() {
                PlanUtil planUtil = PlanUtil.getInstance(context);
                JsonObject data = new JsonObject();
                if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
                    data.addProperty("delivery_plan_id", planUtil.getPlanId());
                } else {
                    data.addProperty("visit_plan_id", planUtil.getPlanId());
                }
                data.addProperty("tap_nfc_type", "OUT");
                data.addProperty("nfc_code", customerCode);
                data.addProperty("route_breadcrumb", "");
                data.addProperty("distance", "");
                data.addProperty("total_distance", "");

                Call<ResponseArrayObject> call = null;
                if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
                    call = service.postTapNfcDriver("JWT " + userUtil.getJWTTOken(), data);
                } else {
                    call = service.postTapNfcSales("JWT " + userUtil.getJWTTOken(), data);
                }
                try {
                    Response<ResponseArrayObject> response = call.execute(); //execute() for synchronous operations
                    if (response.body() != null && response.body().getError() == 0) {
                        // set all variable like process tap out
                        userUtil.setNfcCodeRoute(customerCode);
                        stopVisitSevice();
                        userUtil.setIntProperty(Constants.ADDITIONAL_TIME_PERMISSION, 0);
                        userUtil.removeStartVisit();
                        userUtil.setstatusStopServiceIdleTime();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                        Calendar calendar = Calendar.getInstance();
                        userUtil.setStringProperty(Constants.TAP_OUT_TIMEOUT, dateFormat.format(calendar.getTime()));
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void forcedTapout(String customerCode){
        // clear NFC_CODE
        // userUtil.setStringProperty(Constants.NFC_CODE, "");

        // post data to backend
        postState("OUT", customerCode, false);

        // set all variable like process tap out
        userUtil.setNfcCodeRoute(customerCode);
        stopVisitSevice();
        userUtil.setIntProperty(Constants.ADDITIONAL_TIME_PERMISSION, 0);
        userUtil.removeStartVisit();
        userUtil.setstatusStopServiceIdleTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        userUtil.setStringProperty(Constants.TAP_OUT_TIMEOUT, dateFormat.format(calendar.getTime()));
    }

}
