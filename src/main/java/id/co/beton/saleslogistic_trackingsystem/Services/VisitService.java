package id.co.beton.saleslogistic_trackingsystem.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.annotation.Nullable;

import com.google.gson.JsonObject;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Class VisitService
 * service for visit sales
 */
public class VisitService extends Service  {

    public static final long NOTIFY_INTERVAL_VISIT = 10000; //satuan ms repeat each 10 seconds
    private Timer mTimerVisit = null;
    private Handler mHandler = new Handler();
    private static int notifVisitTime=0;
    public VisitService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mTimerVisit = new Timer();
        mTimerVisit.scheduleAtFixedRate(new VisitTimeCheck(), 5, NOTIFY_INTERVAL_VISIT);
    }

    class VisitTimeCheck extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ceckMax();
                }
            });
        }
    }

    /**
     * check time while sales visit customer
     * send alert if time visit more than allowed time for visit
     */
    private void ceckMax(){
        UserUtil userUtil = UserUtil.getInstance(getApplicationContext());
        if(userUtil.getBooleanProperty(Constants.START_VISIT_TIME_SERVICE)){
            if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
                // add checking status alert Unloading Time
                if(userUtil.statusAlertUnloadingTime()){
                    if(userUtil.checkMaxUnloadingTime()){
                        ServicesNotification servicesNotification =new ServicesNotification(getApplicationContext());
                        servicesNotification.showSimpleNotification("PERINGATAN","Waktu unloading anda melebih waktu");
                        postAlert("Waktu unloading melebihi batas maksimal","alert");
                        userUtil.setBooleanProperty(Constants.START_VISIT_TIME_SERVICE,false);
                        stopService(new Intent(this, VisitService.class));
                        mTimerVisit.cancel();
                        mTimerVisit.purge();
                    }

                    if((userUtil.getLongVisit()>=1200 && userUtil.getLongVisit()<1800 && notifVisitTime==0)
                            ||(userUtil.getLongVisit()>=1800 && userUtil.getLongVisit()<3000 && notifVisitTime==1)
                            || (userUtil.getLongVisit()>=3000 && userUtil.getLongVisit()<3600 && notifVisitTime==2)){
                        ServicesNotification servicesNotification =new ServicesNotification(getApplicationContext());
                        long sisaWaktu = userUtil.getMaxUnloading()-userUtil.getLongVisit();
                        servicesNotification.showSimpleNotification("PERINGATAN!","Sisa waktu kunjungan anda " +sisaWaktu/60+" Menit");

                        notifVisitTime++;
                    }
                }
            }else{
                // add checking status alert Visit Time
                if(userUtil.statusAlertVisitTime()){
                    if(userUtil.checkMaxVisitTime()){ //show notif and push to server
                        ServicesNotification servicesNotification =new ServicesNotification(getApplicationContext());
                        servicesNotification.showSimpleNotification("PERINGATAN","Waktu kunjungan anda melebih waktu");
                        postAlert("Waktu visit time melebihi batas maksimal","alert");
                        userUtil.setBooleanProperty(Constants.START_VISIT_TIME_SERVICE,false);
                        stopService(new Intent(this, VisitService.class));
                        mTimerVisit.cancel();
                        mTimerVisit.purge();
                    }

                    if((userUtil.getLongVisit()>=1200 && userUtil.getLongVisit()<1800 && notifVisitTime==0)
                            ||(userUtil.getLongVisit()>=1800 && userUtil.getLongVisit()<3000 && notifVisitTime==1)
                            || (userUtil.getLongVisit()>=3000 && userUtil.getLongVisit()<3600 && notifVisitTime==2)){
                        ServicesNotification servicesNotification =new ServicesNotification(getApplicationContext());
                        long sisaWaktu = userUtil.getMaxVisitTime()-userUtil.getLongVisit();
                        servicesNotification.showSimpleNotification("PERINGATAN!","Sisa waktu kunjungan anda " +sisaWaktu/60+" Menit");

                        notifVisitTime++;
                    }
                }
            }
        }else{
            mTimerVisit.cancel();
            mTimerVisit.purge();
        }
    }

    /**
     * post/send data alert to backend
     * @param reason
     * @param type
     */
    private void postAlert(String reason, String type){
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(getApplicationContext());
        /*Create handle for the RetrofitInstance interface*/
        ApiInterface apiInterface = ApiClient.getInstance(getApplicationContext());

        JsonObject gsonObject = new JsonObject();
        JsonObject des =new JsonObject();

        gsonObject.addProperty("type", type);
        gsonObject.add("description", des);
        gsonObject.addProperty("notes", reason);
        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            gsonObject.addProperty("delivery_plan_id", PlanUtil.getInstance(getApplicationContext()).getPlanId());
        }else{
            gsonObject.addProperty("visit_plan_id", PlanUtil.getInstance(getApplicationContext()).getPlanId());
        }


        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseArrayObject> call = apiInterface.permissionAlertPost("JWT "+userUtil.getJWTTOken(), gsonObject);
        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {


            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {

            }

        });
    }
}
