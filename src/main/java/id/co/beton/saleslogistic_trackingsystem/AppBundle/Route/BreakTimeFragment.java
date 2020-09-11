package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.JsonObject;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Services.BreakTime;
import id.co.beton.saleslogistic_trackingsystem.Services.ServicesNotification;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class BreakTimeFragment
 */
public class BreakTimeFragment extends Fragment {
    private static final String TAG = BreakTimeFragment.class.getSimpleName();
    private TextView tvTimer;
    private OnFragmentInteractionListener mListener;
    private Button btnTakeBreak;
    private  Context context;
    private ServicesNotification servicesNotification;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String date_time;
    SharedPreferences mpref;
    SharedPreferences.Editor mEditor;
    private int restTime;

    public BreakTimeFragment() {
    }

    public static BreakTimeFragment newInstance(int position) {
        BreakTimeFragment fragment = new BreakTimeFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_break_time, container, false);
        view.setTag(TAG);
        context = container.getContext();

        btnTakeBreak = (Button)view.findViewById(R.id.btn_take_break);
        tvTimer = view.findViewById(R.id.tv_timer);

        servicesNotification =new ServicesNotification(container.getContext());

        mpref = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mpref.edit();

        initTime();

        if(!UserUtil.getInstance(container.getContext()).getStatusBreakTime()){
            btnTakeBreak.setText("ISTIRAHAT !");
            btnTakeBreak.setPadding(30,0,30,0);
            btnTakeBreak.setBackground(view.getResources().getDrawable(R.drawable.bg_nav));
            //stopServiceTimer();
        }else{
            btnTakeBreak.setText("STOP !");
            btnTakeBreak.setPadding(20,0,20,0);
            btnTakeBreak.setBackground(view.getResources().getDrawable(R.drawable.bg_red));
        }

        btnTakeBreak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserUtil.getInstance(container.getContext()).getStatusBreakTime()){
                    servicesNotification.showSimpleNotification("Berhenti istirahat","Anda berhenti istirahat");
                    btnTakeBreak.setText("ISTIRAHAT !");
                    btnTakeBreak.setPadding(30,0,30,0);
                    btnTakeBreak.setBackground(view.getResources().getDrawable(R.drawable.bg_nav));
                    stopServiceTimer();
                }else{
                    btnTakeBreak.setText("STOP !");
                    btnTakeBreak.setPadding(20,0,20,0);
                    btnTakeBreak.setBackground(view.getResources().getDrawable(R.drawable.bg_red));
                    servicesNotification.showWithButtonNotification("break","Istirahat","Anda sedang isitirahat");
                    UserUtil.getInstance(context).setStatusBreakTime(true);
                    UserUtil.getInstance(context).setStatusStopAllServiceRunning(true);
                    startServiceTimer();
                }
            }
        });
        return view;
    }

    private void initTime(){
        restTime =UserUtil.getInstance(context).getRestTime();
        long int_timer = TimeUnit.SECONDS.toMillis(restTime);

        long diffSeconds = int_timer / 1000 % 60;
        long diffMinutes = int_timer / (60 * 1000) % 60;
        long diffHours = int_timer / (60 * 60 * 1000) % 24;

        String str_timer="00:00:00";
        if(int_timer>=0){
            String difminit= diffMinutes+"";
            if(diffMinutes<10){
                difminit = "0"+diffMinutes;
            }
            String difsecond =diffSeconds+"";
            if (diffSeconds<10){
                difsecond="0"+diffSeconds;
            }
            str_timer = "0"+diffHours + ":" + difminit + ":" + difsecond;
        }else{
            String difminit= (diffMinutes*-1)+"";
            if(diffMinutes > -10){
                difminit = "0"+(diffMinutes*-1);
            }
            String difsecond =(diffSeconds*-1)+"";
            if (diffSeconds>-10){
                difsecond="0"+(diffSeconds*-1);
            }
            str_timer = "-0"+(diffHours*-1) + ":" + difminit + ":" + difsecond;
        }
        tvTimer.setText(str_timer);

    }

    /**
     * start service break time
     */
    private void startServiceTimer(){
        restTime =UserUtil.getInstance(context).getRestTime();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        date_time = simpleDateFormat.format(calendar.getTime());

        UserUtil.getInstance(context).setStringProperty(Constants.DATA_TIME,date_time);
        UserUtil.getInstance(context).setIntProperty(Constants.SECONDS,restTime);
        UserUtil.getInstance(context).setLongBreakTime(0);

        Intent intent_service = new Intent(context, BreakTime.class);
        context.startService(intent_service);

    }

    /**
     * stop service break time
     */
    private void stopServiceTimer(){

        Intent intent_service = new Intent(context, BreakTime.class);
        context.stopService(intent_service);

        UserUtil userUtil = UserUtil.getInstance(context);
        userUtil.setStatusBreakTime(false);
        userUtil.setStatusStopAllServiceRunning(false);
        userUtil.removeByKey(Constants.DATA_TIME);
//        userUtil.removeByKey(Constants.SECONDS);
        addBreakTime();
        //call long break time to server
    }

    /**
     * send data breaktime to server
     */
    private void addBreakTime(){
        UserUtil userUtil = UserUtil.getInstance(getContext());

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);
        int longBreakTime = userUtil.getLongBreakTime();

        JsonObject jsonObject =new JsonObject();
        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            jsonObject.addProperty("delivery_plan_id", PlanUtil.getInstance(context).getPlanId());
        }else{
            jsonObject.addProperty("visit_plan_id", PlanUtil.getInstance(context).getPlanId());
        }

        jsonObject.addProperty("break_time",longBreakTime);
        jsonObject.addProperty("customer_code","");

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseArrayObject> call = null;
        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            call = service.addBreakTimeDrive("JWT "+userUtil.getJWTTOken(),jsonObject);
        }else{
            call = service.addBreakTimeSales("JWT "+userUtil.getJWTTOken(),jsonObject);
        }

        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                if(Constants.DEBUG){
                    Log.i(TAG,"sukses post add break time");
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {

            }
        });

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String str_time = UserUtil.getInstance(context).getStringProperty(Constants.TIME);
            tvTimer.setText(str_time);

        }
    };

    @Override
    public void onPause() {
        super.onPause();
        context.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        context.registerReceiver(broadcastReceiver,new IntentFilter(BreakTime.str_receiver));

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
}
