package id.co.beton.saleslogistic_trackingsystem;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.DetailPlanActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;

import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Services.AllService;
import id.co.beton.saleslogistic_trackingsystem.Services.BreakTime;
import id.co.beton.saleslogistic_trackingsystem.Services.VisitService;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * base Activity, to load data Location, setup permission, start service, etc
 */
public class BaseActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private String TAG = BaseActivity.class.getSimpleName();
    public static final int REQUEST_LOCATION = 001;
    protected Context context;
    protected GoogleApiClient googleApiClient;
    protected PendingResult<LocationSettingsResult> pendingResult;
    protected LocationSettingsRequest.Builder locationSettingsRequest;
    protected Activity activity;
    protected LocationRequest locationRequest;
    private LocationManager mLocationManager;
    private FusedLocationProviderClient mFusedLocationClient;
    public Location currentLocation;
    public String customerCode = "";
    public String customerName = "";

    public BaseActivity() {
        Log.i(TAG, "constructor");
    }

    /**
     * function to enable GPS of Device
     */
    protected void enableGps() {
        googleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API).addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        googleApiClient.connect();
        mLocationSetting();
    }

    /**
     * function to set location/GPS settings
     */
    private void mLocationSetting() {
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);

        locationSettingsRequest = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        mResult();

    }

    public void mResult() {
        pendingResult = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, locationSettingsRequest.build());
        pendingResult.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();


                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(activity, REQUEST_LOCATION);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }

        });
    }


    //callback method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_LOCATION:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        break;
                    case Activity.RESULT_CANCELED:
                        Toasty.error(context, "Anda harus mengaktifkan GPS", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                break;
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * function to start Break Time Service
     */
    protected void startServiceTimer() {
        int restTime = 3600; //stauan detik
        if (UserUtil.getInstance(context).getRestTime() > 0) {
            restTime = UserUtil.getInstance(context).getRestTime();
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        String date_time = simpleDateFormat.format(calendar.getTime());

        UserUtil.getInstance(context).setStringProperty(Constants.DATA_TIME, date_time);
        //UserUtil.getInstance(context).setIntProperty(Constants.SECONDS,restTime);

        Intent intent_service = new Intent(context, BreakTime.class);
        context.startService(intent_service);
    }

    /**
     * function to start all registered services
     * called when user Tap START
     */
    protected void starAllService() {
        Intent intent_service = new Intent(context, AllService.class);
        context.startService(intent_service);
    }

    /**
     * function to start visit service for sales
     * called when sales Check IN in Customer
     */
    protected void startVisitService() {
        Intent intent_service = new Intent(context, VisitService.class);
        context.startService(intent_service);
    }

    /**
     * function to stop visit service for sales
     * called when sales Check Out from Customer
     */
    protected void stopVisitSevice() {
        Intent intent_service = new Intent(context, VisitService.class);
        context.stopService(intent_service);
    }

    /**
     * function to stop all registered service
     * called when user Tap END
     */
    protected void stopAllService() {
        Intent intent_service = new Intent(context, AllService.class);
        context.stopService(intent_service);
    }

    /**
     * function for checking whether service running or not
     * @param serviceClass
     * @return Boolean
     */
    protected boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }

    /**
     * function onCreateOptionsMenu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    /**
     * function to check allowed permission
     */
    public void checkRunTimePermission() {
        String[] permissionArrays = new String[]{Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.LOCATION_HARDWARE};

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissionArrays, 11111);
        } else {
            // if already permition granted
            // PUT YOUR ACTION (Like Open cemara etc..)
        }
    }

    /**
     * function to get result from allowed permission
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean openActivityOnce = true;
        boolean openDialogOnce = true;
        boolean isPermitted;
        if (requestCode == 11111) {
            for (int i = 0; i < grantResults.length; i++) {
                String permission = permissions[i];

                isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;

                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // user rejected the permission
                    if (Build.VERSION.SDK_INT >= 23) {
                        boolean showRationale = shouldShowRequestPermissionRationale(permission);
                        if (!showRationale) {
                            //execute when 'never Ask Again' tick and permission dialog not show
                        } else {
                            if (openDialogOnce) {
                                alertView();
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * function to show alert if any permission not granted
     */
    private void alertView() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity);

        dialog.setTitle("Permission Denied")
                .setInverseBackgroundForced(true)
                //.setIcon(R.drawable.ic_info_black_24dp)
                .setMessage("Without those permission the app is unable to save your profile. App needs to save profile image in your external storage and also need to get profile image from camera or external storage.Are you sure you want to deny this permission?")

                .setNegativeButton("I'M SURE", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.dismiss();
                    }
                })
                .setPositiveButton("RE-TRY", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.dismiss();
                        checkRunTimePermission();

                    }
                }).show();
    }

    /**
     * function to get last location of device
     */
    protected void getLastKnownLocation() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);

        final List<Location> listLocation = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toasty.info(context, "GPS tidak aktif atau GPS hilang").show();
        } else {
            mFusedLocationClient.getLastLocation().addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        onLocationChanged(location);
                    }
                }

            });
            mFusedLocationClient.getLastLocation().addOnCompleteListener(activity, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.getResult() != null) {
                        onLocationChanged(task.getResult());
                    }
                }
            });
            mFusedLocationClient.getLastLocation().addOnFailureListener(activity, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("error fused location", e.getMessage());
                    e.printStackTrace();
                }
            });
        }
    }

    /**
     * function to update value when device location changes
     * @param location
     */
    public void onLocationChanged(Location location) {
        // New location has now been determined
        currentLocation = location;
        Log.d("GPS", "BaseActivity");
    }


    /**
     * function to go to detail customer
     */
    protected void gotoDetailCustomer() {
        UserUtil userUtil = UserUtil.getInstance(context);
        //query for detail and goto detail plan
        ApiInterface service = ApiClient.getInstance(context);
        PlanUtil planUtil = PlanUtil.getInstance(context);

        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            Call<ResponseObject> call = service.getDeliveryPlanDetailByCustomer("JWT " + userUtil.getJWTTOken(), planUtil.getPlanId(), customerCode);
            call.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (response.body() != null && response.body().getError() == 0) { //if sukses
                        try {

                            Gson gson = new Gson();
                            final Destination destination = gson.fromJson(response.body().getData().toString(), Destination.class);
                            if (destination.getCustomerCode() != null) {
                                Intent intent = new Intent(context, DetailPlanActivity.class);
                                intent.putExtra("destination", gson.toJson(destination, new TypeToken<Destination>() {
                                }.getType()));
                                intent.putExtra("customer_code", destination.getCustomerCode());
                                intent.putExtra("customer_name", destination.getCustomerName());
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {

                }
            });
        } else {
            Call<ResponseObject> call = service.getVisitPlanDetailByCustomer("JWT " + userUtil.getJWTTOken(), planUtil.getPlanId(), customerCode);

            /*Log the URL called*/
            if (Constants.DEBUG) {
                Log.i("URL Called", call.request().url() + "");
            }
            call.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (response.body() != null && response.body().getError() == 0) { //if sukses
                        try {

                            Gson gson = new Gson();
                            final Destination destination = gson.fromJson(response.body().getData().toString(), Destination.class);
                            if (destination.getCustomerCode() != null) {
                                Intent intent = new Intent(context, DetailPlanActivity.class);
                                intent.putExtra("destination", gson.toJson(destination, new TypeToken<Destination>() {
                                }.getType()));
                                intent.putExtra("customer_code", destination.getCustomerCode());
                                intent.putExtra("customer_name", destination.getCustomerName());

                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {

                }
            });
        }
    }

    /**
     * function to logout and clear shared preferences
     * stop all service and go back to Login screen
     * @param jwt
     * @param flag
     */
    public void logout(String jwt, boolean... flag) {
        boolean forcedLogout = false;
        if(flag!=null){
            if(flag.length>0){
                if(flag[0]){
                    forcedLogout = true;
                }
            }
        }
        final boolean finalForcedLogout = forcedLogout;
        final ApiInterface apiInterface = ApiClient.getInstance(context);
        Call<ResponseArrayObject> call = apiInterface.logout(jwt);
        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                UserUtil.getInstance(context).reset();
                if(!finalForcedLogout){
                    PlanUtil.getInstance(context).reset();
                    // stop all service
                    stopAllService();
                    stopVisitSevice();
                    Intent intent = new Intent(context, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                Toasty.error(context, "Terjadi kesalahan server").show();
            }
        });

    }

}
