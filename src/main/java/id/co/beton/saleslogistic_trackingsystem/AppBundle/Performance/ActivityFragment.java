package id.co.beton.saleslogistic_trackingsystem.AppBundle.Performance;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.Statistic;
import id.co.beton.saleslogistic_trackingsystem.Model.StatisticUser;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Class ActivityFragment
 * data report Activity sales/logistic
 *
 */
public class ActivityFragment extends Fragment {
    private static final String TAG = ActivityFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    private TextView tvTotalVisitedCustomer;
    private TextView tvTotalNewPlaces;
    private TextView tvTotalVisitTime;
    private TextView tvTotalVisistCanceled;
    private TextView tvTotalAlert;
    private TextView tvTotalPermission;
    //private TextView tvTotalIdleTime;
    private TextView tvTotalDrivingTime;
    private TextView tvTotalBreakTime;
    private TextView tvTotalReportNFC;
    private TextView tvTotalReportLocation;
    private ProgressBar progressBar;
    private TextView tv_today;
    private ApiInterface service;
    UserUtil userUtil;

    public ActivityFragment() {
    }

    public static ActivityFragment newInstance(String tabSelected) {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        args.putString("position", tabSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_list_performance_activity, container, false);
        view.setTag(TAG);

        try{
            tvTotalVisitedCustomer = view.findViewById(R.id.tv_total_visited_customer);
            tvTotalNewPlaces = view.findViewById(R.id.tv_total_new_places);
            tvTotalVisitTime = view.findViewById(R.id.tv_total_visit_time);
            tvTotalVisistCanceled = view.findViewById(R.id.tv_total_visist_canceled);
            tvTotalAlert = view.findViewById(R.id.tv_total_alert);
            tvTotalPermission = view.findViewById(R.id.tv_total_permission);
            //tvTotalIdleTime = view.findViewById(R.id.tv_total_idle_time);
            tvTotalDrivingTime = view.findViewById(R.id.tv_total_driving_time);
            tvTotalBreakTime = view.findViewById(R.id.tv_total_break_time);
            // tvTotalReportNFC = view.findViewById(R.id.tv_total_report_nfc);
            tvTotalReportLocation = view.findViewById(R.id.tv_total_report_location);
            progressBar = view.findViewById(R.id.progressBar);
            tv_today = view.findViewById(R.id.tv_today);
            tv_today.setText(ConversionDate.getInstance().today());

            userUtil = UserUtil.getInstance(container.getContext());
            service = ApiClient.getInstance(container.getContext());

            if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
                TextView tvDelivery = view.findViewById(R.id.tv_delivery);
                TextView tvNewStopPoint = view.findViewById(R.id.tv_new_stop_point);
                TextView tvUnloadingTime = view.findViewById(R.id.tv_unloading_time);
                TextView tvDeliveryCancelled = view.findViewById(R.id.tv_delivery_cancelled);
                TextView tvStoptime = view.findViewById(R.id.tv_stop_time);
                TextView tvLocationTime = view.findViewById(R.id.tv_location_time);
                TextView tvSatuanStopTime =  view.findViewById(R.id.tv_satuan_stop_time);
                LinearLayout llVisitPlan = view.findViewById(R.id.ll_visit_plan);

                llVisitPlan.setVisibility(View.GONE);
                tvDelivery.setText(R.string.delivery);
                tvNewStopPoint.setText(R.string.new_stop_point);
                tvUnloadingTime.setText(R.string.unloading_time);
                tvDeliveryCancelled.setText("Undelivered");
                // tvStoptime.setText("Report NFC");
                tvLocationTime.setText("Report Location");
                tvSatuanStopTime.setText("Time(s)");
            }

            loadData();
        }catch (Exception e){
            e.printStackTrace();
        }


        return view;
    }

    public void updateStatistic(Statistic statistic){
        try{
            if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
                float full = (statistic.getPlan()>0)?statistic.getPlan():1;
                float progress = (statistic.getVisited()>0)?statistic.getVisited():1;
                float percent;

                percent = (progress/full)*100;

                if(Constants.DEBUG){
                    Log.wtf(TAG, "(progress/full) : "+(progress/full));
                    Log.wtf(TAG, "Percent : "+percent);
                }

                progressBar.setProgress((int) percent);
            }else{
                float full = (statistic.getInvoice()>0)?statistic.getInvoice():1;
                float progress = (statistic.getPayment()>0)?statistic.getPayment():1;
                float percent;

                percent = (progress/full)*100;

                if(Constants.DEBUG){
                    Log.wtf(TAG, "(progress/full) : "+(progress/full));
                    Log.wtf(TAG, "Percent : "+percent);
                }

                progressBar.setProgress((int) percent);
            }


            tvTotalVisitedCustomer.setText(""+statistic.getVisited());
            tvTotalNewPlaces.setText(""+statistic.getPlan());
            tvTotalVisitTime.setText(""+statistic.getVisitTime());
            tvTotalVisistCanceled.setText(""+statistic.getCancel());
            tvTotalAlert.setText(""+statistic.getAlert());
            tvTotalPermission.setText(""+statistic.getPermission());
            //tvTotalIdleTime.setText("-");
            tvTotalDrivingTime.setText(""+statistic.getDrivingTime());
            tvTotalBreakTime.setText(""+statistic.getBreakTime());
            // tvTotalReportNFC.setText(""+statistic.getReportNfc());
            tvTotalReportLocation.setText("" + statistic.getReportLocation());
        }catch (Exception e){
            e.printStackTrace();
        }
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void loadData(){

        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(getContext());

        /*Create handle for the RetrofitInstance interface*/


        Map<String, String> params = new HashMap<String, String>();
//        params.put("user_id", "[74]");
        params.put("user_id", "["+UserUtil.getInstance(getActivity().getApplicationContext()).getId()+"]");

        /*Call the method with parameter in the interface to get the data*/
        Call<StatisticUser> call=null;
        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            call = service.userStatistic("JWT "+userUtil.getJWTTOken(),"logistic",params);
        }else{
            call = service.userStatistic("JWT "+userUtil.getJWTTOken(),"sales",params);
        }

        /*Callback*/
        call.enqueue(new Callback<StatisticUser>() {
            @Override
            public void onResponse(Call<StatisticUser> call, Response<StatisticUser> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    updateStatistic(response.body().getData().get(""+UserUtil.getInstance(getActivity().getApplicationContext()).getId()));

                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toasty.error(getContext(),"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<StatisticUser> call, Throwable t) {
                Toasty.error(getContext(),"Terjadi kesalahan server").show();
                if(Constants.DEBUG){
                    Log.e(TAG, t.getMessage());
                }
            }
        });
    }

}
