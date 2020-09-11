package id.co.beton.saleslogistic_trackingsystem.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import androidx.annotation.Nullable;
import android.util.Log;

import com.google.gson.JsonObject;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class BreakTime
 * service for breaktime sales/logistic
 */
public class BreakTime extends Service {

    public BreakTime(Context applicationContext) {
        super();
    }

    public BreakTime() {
    }

    public static String str_receiver = "id.co.beton.saleslogistic_trackingsystem.fragment.route";

    private Handler mHandler = new Handler();

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String strDate;
    Date date_current, date_diff;
    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;

    private Timer mTimer = null;
    public static final long NOTIFY_INTERVAL = 1000; //every seconds
    Intent intent;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private static int countNotifBreakTime=0;
    private static int alertPostBreakTime=0;

    @Override
    public void onCreate() {
        super.onCreate();

        mpref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mEditor = mpref.edit();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        mTimer = new Timer();
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 5, NOTIFY_INTERVAL);
        intent = new Intent(str_receiver);
    }


    class TimeDisplayTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(new Runnable() {

                @Override
                public void run() {
                    calendar = Calendar.getInstance();
                    simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    strDate = simpleDateFormat.format(calendar.getTime());

                    countTimer();
                    int longBreakTime = UserUtil.getInstance(getApplicationContext()).getLongBreakTime();
                    UserUtil.getInstance(getApplicationContext()).setLongBreakTime(longBreakTime+1);
                }

            });
        }

    }

    public String countTimer() {
        try {
            date_current = simpleDateFormat.parse(strDate);
        } catch (Exception e) {

        }
        try {
            date_diff = simpleDateFormat.parse(UserUtil.getInstance(getApplicationContext()).getStringProperty(Constants.DATA_TIME));
        } catch (Exception e) {

        }
        if(UserUtil.getInstance(getApplicationContext()).getStatusBreakTime()){
            try {
                long diff = date_current.getTime() - date_diff.getTime();

                int int_second = Integer.valueOf(UserUtil.getInstance(getApplicationContext()).getIntProperty(Constants.SECONDS));

                long int_timer = TimeUnit.SECONDS.toMillis(int_second);
                long long_hours = int_timer - diff;
                long diffSeconds2 = long_hours / 1000 % 60;
                long diffMinutes2 = long_hours / (60 * 1000) % 60;
                long diffHours2 = long_hours / (60 * 60 * 1000) % 24;

                if((diffMinutes2==5 && countNotifBreakTime==2 )||(diffMinutes2==15 && countNotifBreakTime==1 )||(diffMinutes2==30 && countNotifBreakTime==0 )){
                    ServicesNotification servicesNotification =new ServicesNotification(getApplicationContext());
                    servicesNotification.showSimpleNotification("Istirahat","Sisa waktu istirahat anda "+diffMinutes2+" menit");
                    countNotifBreakTime++;
                }

                if(long_hours==0 || (diffSeconds2==-5 && diffMinutes2==0) || (diffSeconds2==-30 && diffMinutes2==0) || (diffMinutes2==0 && diffSeconds2==-50)){
                    //notif waktu istirahat habis
                    ServicesNotification servicesNotification =new ServicesNotification(getApplicationContext());
                    servicesNotification.showSimpleNotification("PERINGATAN!","Waktu Isitirahat anda sudah habis");
                    if(alertPostBreakTime==0){
                        // add checking status alert break time
                        if(UserUtil.getInstance(getApplicationContext()).statusAlertBreakTime()){
                            postAlert("Melebihi waktu istirahat","alert");
                        }
                        alertPostBreakTime++;
                    }
                }

                if (long_hours > 0) {
                    String difminit = diffMinutes2+"";
                    if(diffMinutes2<10){
                        difminit="0"+(diffMinutes2);
                    }
                    String difsecond =diffSeconds2+"";
                    if (diffSeconds2<10){
                        difsecond="0"+(diffSeconds2);
                    }
                    String str_testing = "0"+(diffHours2) + ":" + difminit + ":" + difsecond;

                    long seconds = diffSeconds2+TimeUnit.MINUTES.toSeconds(diffMinutes2)+TimeUnit.HOURS.toSeconds(diffHours2);
                    fn_update(str_testing,(int)seconds);
                } else {
                    UserUtil.getInstance(getApplicationContext()).setStatusStopAllServiceRunning(false);
                    String difminit = (diffMinutes2*-1)+"";
                    if(diffMinutes2 > -10){
                        difminit="0"+(diffMinutes2*-1);
                    }
                    String difsecond =(diffSeconds2*-1)+"";
                    if (diffSeconds2>-10){
                        difsecond="0"+(diffSeconds2*-1);
                    }
                    String str_testing = "-0"+(diffHours2*-1) + ":" + difminit + ":" + difsecond;

                    long seconds = diffSeconds2+TimeUnit.MINUTES.toSeconds(diffMinutes2)+TimeUnit.HOURS.toSeconds(diffHours2);
                    fn_update(str_testing,(int)seconds);
                }


            }catch (Exception e){
                Log.e(str_receiver,e.toString());
                mTimer.cancel();
                mTimer.purge();
            }
        }else{
            mTimer.cancel();
            mTimer.purge();
        }


        return "";

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("Service finish","Finish");
    }

    private void fn_update(String str_time,int second){
        UserUtil.getInstance(getApplicationContext()).setStringProperty(Constants.TIME,str_time);
        UserUtil.getInstance(getApplicationContext()).setRestTime(second);
        sendBroadcast(intent);
    }

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
        gsonObject.addProperty("customer_code", "");
        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            gsonObject.addProperty("delivery_plan_id", PlanUtil.getInstance(getApplicationContext()).getPlanId());
        }else{
            gsonObject.addProperty("visit_plan_id", PlanUtil.getInstance(getApplicationContext()).getPlanId());
        }


        /*Show custom dialog*/

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
