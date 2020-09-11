package id.co.beton.saleslogistic_trackingsystem.Services;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.util.Log;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.model.LatLng;
import com.google.errorprone.annotations.DoNotMock;
import com.google.gson.JsonObject;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.MainActivity;
import id.co.beton.saleslogistic_trackingsystem.BaseActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.LoginActivity;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.DetectFakeGPS;
import id.co.beton.saleslogistic_trackingsystem.Utils.DistanceCalculator;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Class AllService
 * class for activate all related service
 *
 */
public class AllService extends Service {
    public static String str_receiver = "id.co.beton.saleslogistic_trackingsystem";

    boolean isGPSEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    //    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimerBattery = null;
    private Timer mTimerGPS = null;
    private Timer mTimerBreadCumbs = null;
    private Timer mTimerDistance = null;
    public static final long NOTIFY_INTERVAL_BATTERY = 1800000; //satuan ms repeat each 30 minutes
    public static final long NOTIFY_INTERVAL_COORDINAT_GPS = 10000; //satuan ms repeat each 10 second
    public static final long NOTIFY_INTERVAL_CALCULATE_DISTANCE = 5000; //satuan ms repeat each 10 second

    public static final long NOTIFY_BREADCUMBS = 60000; //satuan ms repeat each 1 MENIT

    public int meter = 0;
    public static int alertBatery = 0;
    private static int alertWrongWay = 0;
    private static int alertIdle = 0;
    private Location locationNow = null;
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;

    public boolean MOCK_DETECTED = false;

    private Context mCOntext;
    private ApiInterface service;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.d("GPS", "AllService");
            locationNow = location;
            mLastLocation.set(location);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    }

    private Socket socketUserSet;

    {
        try {
            socketUserSet = IO.socket(Constants.API_SERVER_ADDR);
        } catch (URISyntaxException e) {
        }
    }

    private Socket socketChangeLocation;

    {
        try {
            socketChangeLocation = IO.socket(Constants.API_SERVER_ADDR);
        } catch (URISyntaxException e) {
            Log.e(str_receiver, e.getMessage());
        }
    }

    public AllService(Context applicationContext) {
        super();
        // connect to socketUserSet
    }

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("ActivityName: ", "socket onLogin");
            JSONObject data = (JSONObject) args[0];

            Log.wtf("Datana", data.toString());
        }
    };

    public AllService() {
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };


    /*
     * custom function
     * */
    public static boolean isLocationFromMockProvider(Context context, Location location) {
        boolean isMock = false;
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            isMock = location.isFromMockProvider();
        } else {
            //isMock = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
            if (Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
                isMock = false;
            else {
                isMock = true;
            }
        }
        return isMock;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mCOntext = getApplicationContext();
        service = ApiClient.getInstance(mCOntext);

        alertWrongWay = 0;
        mTimerBattery = new Timer();
        mTimerBattery.scheduleAtFixedRate(new AllService.TimeDisplayTimerTask(), 5, NOTIFY_INTERVAL_BATTERY);

        mTimerGPS = new Timer();
        mTimerGPS.scheduleAtFixedRate(new AllService.TimerTaskToGetLocation(), 5, NOTIFY_INTERVAL_COORDINAT_GPS);
        socketChangeLocation.connect();

        mTimerBreadCumbs = new Timer();
        mTimerBreadCumbs.scheduleAtFixedRate(new AllService.TimerTaskBreadCumbs(), 5, NOTIFY_BREADCUMBS);

        mTimerDistance = new Timer();
        mTimerDistance.scheduleAtFixedRate(new AllService.TimerTaskDistance(), 5, NOTIFY_INTERVAL_CALCULATE_DISTANCE);

        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i("All service", "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d("All service", "network provider does not exist, " + ex.getMessage());
        }

    }

    /*@Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            this.location = location;
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }*/

    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    checkBattery();
                }

            });
        }
    }

    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    checkCurrentLocation();
                }
            });
        }
    }

    private class TimerTaskBreadCumbs extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    sentBreadCumbs();
                }
            });
        }
    }

    private class TimerTaskDistance extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    calculateDistance();
                }
            });
        }
    }

    /**
     * send last position (latitude & longitude) device to backend
     */
    private void sentBreadCumbs() {
        if (UserUtil.getInstance(getApplicationContext()).getBooleanProperty(Constants.STATUS_OUT_OFFICE)) {
            UserUtil userUtil = UserUtil.getInstance(getApplicationContext());
            PlanUtil planUtil = PlanUtil.getInstance(getApplicationContext());
            /*Create handle for the RetrofitInstance interface*/
            if (locationNow == null) {
                locationNow = getLastKnownLocation();
            }

            if (locationNow != null) {
                try {

                    JsonObject jsonObject = new JsonObject();
                    if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
                        jsonObject.addProperty("delivery_plan_id", planUtil.getPlanId());
                    } else {
                        jsonObject.addProperty("visit_plan_id", planUtil.getPlanId());
                    }

                    jsonObject.addProperty("lat", locationNow.getLatitude());
                    jsonObject.addProperty("lng", locationNow.getLongitude());
                    Call<ResponseArrayObject> call = null;
                    if (UserUtil.getInstance(getApplicationContext()).getRoleUser().equals(Constants.ROLE_DRIVER)) {
                        call = service.postBreadCumbsDriver("JWT " + userUtil.getJWTTOken(), jsonObject);
                    } else {
                        call = service.postBreadCumbsSales("JWT " + userUtil.getJWTTOken(), jsonObject);
                    }

                    call.enqueue(new Callback<ResponseArrayObject>() {
                        @Override
                        public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {

                        }

                        @Override
                        public void onFailure(Call<ResponseArrayObject> call, Throwable t) {

                        }
                    });
                } catch (Exception e) {
                    Log.e("all service breadcumbs ", e.toString());
                }

            }
        } else {
            mTimerBreadCumbs.cancel();
            mTimerBreadCumbs.purge();
        }

    }

    /**
     * send any alert to server
     * @param reason
     * @param type
     */
    private void postAlert(String reason, String type) {
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(getApplicationContext());
        /*Create handle for the RetrofitInstance interface*/

        JsonObject gsonObject = new JsonObject();
        JsonObject des = new JsonObject();

        gsonObject.addProperty("type", type);
        gsonObject.add("description", des);
        gsonObject.addProperty("notes", reason);
        gsonObject.addProperty("customer_code", "");
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            gsonObject.addProperty("delivery_plan_id", PlanUtil.getInstance(getApplicationContext()).getPlanId());
        } else {
            gsonObject.addProperty("visit_plan_id", PlanUtil.getInstance(getApplicationContext()).getPlanId());
        }


        /*Show custom dialog*/

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseArrayObject> call = service.permissionAlertPost("JWT " + userUtil.getJWTTOken(), gsonObject);
        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {


            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {

            }

        });
    }

    private void addidleTime(long idle_time) {
        UserUtil userUtil = UserUtil.getInstance(getApplicationContext());

        /*Create handle for the RetrofitInstance interface*/

        JsonObject jsonObject = new JsonObject();
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            jsonObject.addProperty("delivery_plan_id", PlanUtil.getInstance(getApplicationContext()).getPlanId());
        } else {
            jsonObject.addProperty("visit_plan_id", PlanUtil.getInstance(getApplicationContext()).getPlanId());
        }

        jsonObject.addProperty("idle_time", idle_time);
        jsonObject.addProperty("customer_code", "");

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseArrayObject> call = null;
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            call = service.addidleTime("JWT " + userUtil.getJWTTOken(), "logistic", jsonObject);
        } else {
            call = service.addidleTime("JWT " + userUtil.getJWTTOken(), "sales", jsonObject);
        }

        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {

            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {

            }
        });
    }

    /**
     * function calculate distance previous with current location
     */
    private void doCalculateDistance(){
        // update location and odometer
        UserUtil.getInstance(getApplicationContext()).updateDistanceLocation(locationNow.getLatitude(), locationNow.getLongitude());

        String currentCheckin   = UserUtil.getInstance(getApplicationContext()).getNfcCode();
        String prevCheckin      = UserUtil.getInstance(getApplicationContext()).getStringProperty(Constants.LAST_CHECKIN);

        if (!currentCheckin.equals("")) { // if checkin
            if(!currentCheckin.equals(prevCheckin)){
                UserUtil.getInstance(getApplicationContext()).setStringProperty(Constants.LAST_CHECKIN, currentCheckin);
                System.out.println(UserUtil.getInstance(getApplicationContext()).getStringProperty(Constants.LAST_CHECKIN));
            }
        }
    }

    /**
     * validation location before calculate distance
     */
    private void calculateDistance(){
        if (UserUtil.getInstance(getApplicationContext()).getBooleanProperty(Constants.STATUS_OUT_OFFICE)) {
            if (!UserUtil.getInstance(getApplicationContext()).getStatusStopAllServiceRunning()) {
                // System.out.println("Service Calculate Distance");

                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!isGPSEnable) {
                    if (Constants.DEBUG) {
                        Log.e("service", "gps tidak aktif");
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (Constants.DEBUG) {
                            Log.e("service", "need permission gps");
                        }
                    }
                    locationNow = getLastKnownLocation();
                    if (locationNow != null) {
                        if(Constants.IS_CHECK_FAKE_GPS){
                            if(!DetectFakeGPS.getInstance().isMockLocationOn(locationNow, getApplicationContext())){
                                doCalculateDistance();
                            }
                        } else {
                            doCalculateDistance();
                        }
                    }
                }
            }
        } else {
            mTimerDistance.cancel();
            mTimerDistance.purge();
        }
    }

    /**
     * function check current location
     * post alert if stay to long
     */

    private void doCheckCurrentLocation(){

        if(isLocationFromMockProvider(mCOntext, locationNow)){
            MOCK_DETECTED = true;
        }

        Log.e("Data Lokasi", "Lokasi");
        Log.e("latitude", locationNow.getLatitude() + "");
        Log.e("longitude", locationNow.getLongitude() + "");
        if (Constants.DEBUG) {
            Log.i("latitude", locationNow.getLatitude() + "");
            Log.i("longitude", locationNow.getLongitude() + "");
        }
        this.latitude = locationNow.getLatitude();
        this.longitude = locationNow.getLongitude();

        if (!UserUtil.getInstance(getApplicationContext()).getStatusStopAllServiceRunning()) {
            // add checking status alert idle
            if(UserUtil.getInstance(getApplicationContext()).statusAlertIdle()){
                if (UserUtil.getInstance(getApplicationContext()).checkIdleTime(latitude, longitude)) {
                    if (alertIdle == 0 && !UserUtil.getInstance(getApplicationContext()).getStatusStopServiceIdleTime()) {
                        ServicesNotification servicesNotification = new ServicesNotification(getApplicationContext());
                        servicesNotification.showSimpleNotification("PERINGATAN!", "Anda diam terlalu lama");
                        postAlert("Diam terlalu lama", "alert");
                        long idleTime = UserUtil.getInstance(getApplicationContext()).idleTime();
                        addidleTime(idleTime);
                        alertIdle++;
                    }
                } else {
                    alertIdle = 0;
                }
            }
        }

        // add checking status using route or not
        if(PlanUtil.getInstance(getApplicationContext()).isUsingRoute()){
            // add checking status alert wrong route
            if(UserUtil.getInstance(getApplicationContext()).statusAlertWrongRoute()){
                //cek idle time
                //check salah jalur dengan menghitung jarak
                float[] koorAwal = UserUtil.getInstance(getApplicationContext()).getKoordinatAwal();
                float[] koorAkhir = UserUtil.getInstance(getApplicationContext()).getKoordinatAkhir();
                float[] koorTengah = UserUtil.getInstance(getApplicationContext()).getKoordinatTengah();

                try {
                    if (koorAwal[0] != 0 && koorAkhir[0] != 0) {
                        if (UserUtil.getInstance(getApplicationContext()).getNfcCode() == "") {
                            DistanceCalculator distanceCalculator = new DistanceCalculator();
                            if (koorTengah[0] != 0) {
                                double totalJarakAwalKeTengah = distanceCalculator.greatCircleInMeters(new LatLng(koorAwal[0], koorAwal[1]), new LatLng(koorTengah[0], koorTengah[1]));
                                double totalJarakTengahKeAkhir = distanceCalculator.greatCircleInMeters(new LatLng(koorTengah[0], koorTengah[1]), new LatLng(koorAkhir[0], koorAkhir[1]));
                                double totalJarakAwalKeKoordinatHp = distanceCalculator.greatCircleInMeters(new LatLng(koorAwal[0], koorAwal[1]), new LatLng(latitude, longitude));
                                double totalJarakAkhirKeKoordinatHp = distanceCalculator.greatCircleInMeters(new LatLng(latitude, longitude), new LatLng(koorAkhir[0], koorAkhir[1]));

                                if (totalJarakAwalKeKoordinatHp + totalJarakAkhirKeKoordinatHp > totalJarakAwalKeTengah + totalJarakTengahKeAkhir + Constants.TOLERANSI_JARAK) {
                                    ServicesNotification servicesNotification = new ServicesNotification(getApplicationContext());
                                    servicesNotification.showSimpleNotification("Salah Jalur", "Anda telah keluar dari jalur");
                                    if (alertWrongWay == 0) {
                                        postAlert("Salah jalur", "alert");
                                        alertWrongWay++;
                                    }

                                }
                            } else {
                                double totalJarakAwalKeTujuan = distanceCalculator.greatCircleInMeters(new LatLng(koorAwal[0], koorAwal[1]), new LatLng(koorAkhir[0], koorAkhir[1]));
                                double totalJarakAwalKeKoordinatHp = distanceCalculator.greatCircleInMeters(new LatLng(koorAwal[0], koorAwal[1]), new LatLng(latitude, longitude));
                                double totalJarakAkhirKeKoordinatHp = distanceCalculator.greatCircleInMeters(new LatLng(latitude, longitude), new LatLng(koorAkhir[0], koorAkhir[1]));

                                if (totalJarakAwalKeKoordinatHp + totalJarakAkhirKeKoordinatHp > totalJarakAwalKeTujuan + Constants.TOLERANSI_JARAK) {
                                    ServicesNotification servicesNotification = new ServicesNotification(getApplicationContext());
                                    servicesNotification.showSimpleNotification("Salah Jalur", "Anda telah keluar dari jalur");
                                    if (alertWrongWay == 0) {
                                        postAlert("Salah jalur", "alert");
                                        alertWrongWay++;
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("All service", e.getMessage());
                }
            }
        }
        emitLatLng(locationNow.getLatitude(), locationNow.getLongitude());
    }

    /**
     * validation location (whether from mock application or not) before check current location
     */
    private void checkCurrentLocation() {
        if (UserUtil.getInstance(getApplicationContext()).getBooleanProperty(Constants.STATUS_OUT_OFFICE)) {
            if (UserUtil.getInstance(getApplicationContext()).getStatusStopAllServiceRunning() == false) {
                locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
                isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

                if (!isGPSEnable) {
                    if (Constants.DEBUG) {
                        Log.e("service", "gps tidak aktif");
                    }
                } else {
                    if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        if (Constants.DEBUG) {
                            Log.e("service", "need permission gps");
                        }
                    }

                    locationNow = getLastKnownLocation();

                    if (locationNow != null) {
                        if(Constants.IS_CHECK_FAKE_GPS){
                            if(!DetectFakeGPS.getInstance().isMockLocationOn(locationNow, getApplicationContext())){
                                doCheckCurrentLocation();
                            }
                        } else {
                            doCheckCurrentLocation();
                        }
                    } else {
                        Log.e(str_receiver, "Location null");
                    }
                }
            }
        } else {
            mTimerGPS.cancel();
            mTimerGPS.purge();
            socketChangeLocation.disconnect();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i("All service", "fail to remove location listener, ignore", ex);
                }
            }
        }
    }

    /**
     * get last know location
     * @return Location
     */
    private Location getLastKnownLocation() {
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

    /**
     * send location to server via socket connection
     * @param lat
     * @param lng
     */
    private void emitLatLng(double lat, double lng) {
        try {
            JSONObject jsonObject = new JSONObject();
            if (socketChangeLocation.connected()) {
                jsonObject.put("user_id", UserUtil.getInstance(getApplicationContext()).getId());
//                jsonObject.put("user_id",39);
                jsonObject.put("lat", lat);
                jsonObject.put("lng", lng);
                socketChangeLocation.emit("change-location", jsonObject);
            } else {
                socketChangeLocation.connect();
            }

        } catch (JSONException e) {
            Log.e(str_receiver, e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * function to check capacity battery device,
     * send alert to server if below 50%
     */
    public void checkBattery() {
        //keep service alive when status out office still true
        if (UserUtil.getInstance(getApplicationContext()).getBooleanProperty(Constants.STATUS_OUT_OFFICE)) {
            int total = getBatteryPercentage(getApplicationContext());

            /*
            OLD Code

            if ((total <= 50 && alertBatery == 0) || (total <= 30 && alertBatery == 1) || (total <= 20 && alertBatery == 2) || (total <= 10 && alertBatery == 3)) {
                postAlert("Sisa baterai " + total, "alert");
                alertBatery++;
            }
            */

            if (total <= Constants.MIN_BATTERY_PERCENTAGE_ALERT && alertBatery < 1)  {
                postAlert("Sisa baterai " + total, "alert");
                alertBatery++;
            }


            if (Constants.DEBUG) {
                Log.i(str_receiver, "sisa baterai " + total + "");
            }
        } else {
            mTimerBattery.cancel();
            mTimerBattery.purge();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * function to get percentage of battery status
     * @param context
     * @return
     */
    public static int getBatteryPercentage(Context context) {

        try {
            IntentFilter iFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
            Intent batteryStatus = context.registerReceiver(null, iFilter);

            int level = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) : -1;
            int scale = batteryStatus != null ? batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1) : -1;

            float batteryPct = level / (float) scale;

            return (int) (batteryPct * 100);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private void initializeLocationManager() {
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }
}
