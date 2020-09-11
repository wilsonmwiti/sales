package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Report;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.DetailPlanActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.DetectFakeGPS;
import id.co.beton.saleslogistic_trackingsystem.Utils.DistanceCalculator;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class ReportFragment
 * list of available report
 */
public class ReportFragment extends Fragment {
    private LinearLayout lrNFC, lrTokoTutup, lrPrint, lrOther, lrReasonNFC, lrReasonPrint, lrReasonOther, lrReasonTokoTutup, lrLocation, lrReasonLocation;

    private TextView tvReasonNFC, tvReasonOther, tvReasonPrinter, tvReasonTokoTutup, tvReasonLocation;
    private Button btnSubmitNFC, btnSubmitPrinter, btnSubmitOther, btnSubmitTokoTutup, btnSubmitLocation;
    private EditText etContentReasonNFC, etContentReasonOther, etContentReasonPrinter, getEtContentReasonTokoTutup, etContentReasonLocation;
    private Context context;
    private String customerCode, customerName, nfcCode;
    private FusedLocationProviderClient mFusedLocationClient;
    private Destination destination;
    private double distance, userLatitude, userLongitude;

    public static boolean isSubmitTokoTutup = false;

    public static void setSubmitTokoTutup(boolean b){
        isSubmitTokoTutup = b;
    }

    public static boolean getSubmitTokoTutup(){
        return isSubmitTokoTutup;
    }

    /*
    * custom function
    * */
    public static String report_lokasi;
    public static void setReport_lokasi(String report){
        report_lokasi = report;
    }
    public static String getReport_lokasi(){
        return report_lokasi;
    }

    public static String report_toko_tutup;
    public static void setReport_toko_tutup(String report){
        report_toko_tutup = report;
    }
    public static String getReport_toko_tutup(){
        return report_toko_tutup;
    }

    public static String report_cetak;
    public static void setReport_cetak(String report){
        report_cetak = report;
    }
    public static String getReport_cetak(){
        return report_cetak;
    }

    public static String report_lainnya;
    public static void setReport_lainnya(String report){
        report_lainnya = report;
    }
    public static String getReport_lainnya(){
        return report_lainnya;
    }

    /*
    * custom function end
    * */



    public ReportFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.content_detail_visit_plan_report, container, false);
        context = container.getContext();
        try {
            customerCode = getArguments().getString("cust_code");
            customerName = getArguments().getString("cust_name");

            /* report */
            setReport_lokasi(getArguments().getString("report_lokasi"));
            setReport_toko_tutup(getArguments().getString("report_toko_tutup"));
            setReport_cetak(getArguments().getString("report_cetak"));
            setReport_lainnya(getArguments().getString("report_lainnya"));
            /* report end*/

            Gson gson = new Gson();
            destination = gson.fromJson(getArguments().getString("destination"), new TypeToken<Destination>() {
            }.getType());

            nfcCode = UserUtil.getInstance(context).getStringProperty(Constants.NFC_CODE);

            lrNFC = (LinearLayout) view.findViewById(R.id.lr_nfc);
            lrLocation = (LinearLayout) view.findViewById(R.id.lr_location);
            lrTokoTutup = (LinearLayout) view.findViewById(R.id.lr_toko_tutup);
            lrPrint = (LinearLayout) view.findViewById(R.id.lr_printer);
            lrOther = (LinearLayout) view.findViewById(R.id.lr_other);
            lrReasonNFC = (LinearLayout) view.findViewById(R.id.lr_reason_nfc);
            lrReasonLocation = (LinearLayout) view.findViewById(R.id.lr_reason_location);
            lrReasonTokoTutup = (LinearLayout) view.findViewById(R.id.lr_reason_toko_tutup);
            lrReasonOther = (LinearLayout) view.findViewById(R.id.lr_reason_other);
            lrReasonPrint = (LinearLayout) view.findViewById(R.id.lr_reason_printer);
            tvReasonNFC = (TextView) view.findViewById(R.id.tv_reason_nfc);
            tvReasonLocation = (TextView) view.findViewById(R.id.tv_reason_location);
            tvReasonTokoTutup = (TextView) view.findViewById(R.id.tv_reason_toko_tutup);
            tvReasonOther = (TextView) view.findViewById(R.id.tv_reason_other);
            tvReasonPrinter = (TextView) view.findViewById(R.id.tv_reason_printer);
            etContentReasonNFC = (EditText) view.findViewById(R.id.et_content_reason_nfc);
            etContentReasonLocation = (EditText) view.findViewById(R.id.et_content_reason_location);
            getEtContentReasonTokoTutup = (EditText) view.findViewById(R.id.et_content_reason_toko_tutup);
            etContentReasonOther = (EditText) view.findViewById(R.id.et_content_reason_other);
            etContentReasonPrinter = (EditText) view.findViewById(R.id.et_content_reason_printer);
            btnSubmitNFC = (Button) view.findViewById(R.id.btn_submit_nfc);
            btnSubmitLocation = (Button) view.findViewById(R.id.btn_submit_location);
            btnSubmitTokoTutup = (Button) view.findViewById(R.id.btn_submit_toko_tutup);
            btnSubmitOther = (Button) view.findViewById(R.id.btn_submit_other);
            btnSubmitPrinter = (Button) view.findViewById(R.id.btn_submit_printer);

            lrReasonNFC.setVisibility(View.GONE);
            lrReasonLocation.setVisibility(View.GONE);
            lrReasonTokoTutup.setVisibility(View.GONE);
            lrReasonOther.setVisibility(View.GONE);
            lrReasonPrint.setVisibility(View.GONE);
            tvReasonNFC.setVisibility(View.GONE);
            tvReasonLocation.setVisibility(View.GONE);
            tvReasonTokoTutup.setVisibility(View.GONE);
            tvReasonOther.setVisibility(View.GONE);
            tvReasonPrinter.setVisibility(View.GONE);
            etContentReasonNFC.setVisibility(View.GONE);
            etContentReasonLocation.setVisibility(View.GONE);
            getEtContentReasonTokoTutup.setVisibility(View.GONE);
            etContentReasonOther.setVisibility(View.GONE);
            etContentReasonPrinter.setVisibility(View.GONE);
            btnSubmitNFC.setVisibility(View.GONE);
            btnSubmitLocation.setVisibility(View.GONE);
            btnSubmitTokoTutup.setVisibility(View.GONE);
            btnSubmitOther.setVisibility(View.GONE);
            btnSubmitPrinter.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);


        btnSubmitNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtil.getInstance(context).getBooleanProperty(Constants.STATUS_OUT_OFFICE)) {
                    if (etContentReasonNFC.getText().toString().length() > 9) {
                        //cek jarak koordinat user dng customer
                        if ((ContextCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED)) {

                        } else {
                            mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        DistanceCalculator distanceCalculator = new DistanceCalculator();
                                        double jarak = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(destination.getLat(), destination.getLng()));
                                        if (Constants.DEBUG) {
                                            Log.i("jarak ke cusutomer ", jarak + "");
                                        }
                                        if (jarak < Constants.TOLERANSI_JARAK) {
                                            sentReport(etContentReasonNFC.getText().toString(), "nfc");
                                            etContentReasonNFC.setText("");
                                        } else {
                                            Toasty.info(context, "Anda belum berada di customer " + customerName).show();
                                        }
                                    } else {
                                        Toasty.error(context, "Pastikan gps anda aktif").show();
                                    }
                                }
                            });
                            mFusedLocationClient.getLastLocation().addOnFailureListener(getActivity(), new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("error fused location", e.getMessage());
                                    e.printStackTrace();
                                }
                            });
                        }

                    } else {
                        Toasty.info(context, "Alasan terlalu pendek").show();
                    }
                } else {
                    Toasty.info(context, "Anda belum melakukan Checkin").show();
                }

            }
        });

        btnSubmitLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserUtil.getInstance(context).getBooleanProperty(Constants.STATUS_OUT_OFFICE)) {
                    if (etContentReasonLocation.getText().toString().length() > 9) {
                        //cek jarak koordinat user dng customer
                        if ((ContextCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED)) {

                        } else {
                            mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        if(Constants.IS_CHECK_FAKE_GPS){
                                            if(DetectFakeGPS.getInstance().isMockLocationOn(location, context)){
                                                DetectFakeGPS.getInstance().alertMockLocation(getActivity());
//                                                Toasty.error(context, "Matikan Aplikasi Fake GPS dan Restart Device Android Anda").show();
                                                return;
                                            }
                                        }
                                        DistanceCalculator distanceCalculator = new DistanceCalculator();
                                        distance = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(destination.getLat(), destination.getLng()));
                                        userLatitude  = location.getLatitude();
                                        userLongitude = location.getLongitude();
                                        if (Constants.DEBUG) {
                                            Log.i("jarak ke customer ", distance + "");
                                        }
                                        sentReport(etContentReasonLocation.getText().toString(), "location");

                                        /* custom function */
//                                        btnSubmitLocation.setVisibility(View.GONE);
//                                        setReport_lokasi(etContentReasonLocation.getText().toString());
                                        /* custom function end*/

//                                        etContentReasonLocation.setText("");
                                    } else {
                                        Toasty.error(context, "Pastikan gps anda aktif").show();
                                    }
                                }
                            });
                            mFusedLocationClient.getLastLocation().addOnFailureListener(getActivity(), new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("error fused location", e.getMessage());
                                    e.printStackTrace();
                                }
                            });
                        }

                    } else {
                        Toasty.info(context, "Alasan terlalu pendek").show();
                    }
                } else {
                    Toasty.info(context, "Anda belum melakukan Checkin").show();
                }
            }
        });


        btnSubmitTokoTutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (UserUtil.getInstance(context).getBooleanProperty(Constants.STATUS_OUT_OFFICE)) {
                    if (getEtContentReasonTokoTutup.getText().toString().length() > 9) {
                        //cek jarak koordinat user dng customer
                        if ((ContextCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(context,
                                Manifest.permission.ACCESS_FINE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED)) {

                        } else {
                            mFusedLocationClient.getLastLocation().addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    if (location != null) {
                                        if(Constants.IS_CHECK_FAKE_GPS){
                                            if(DetectFakeGPS.getInstance().isMockLocationOn(location, context)){
                                                DetectFakeGPS.getInstance().alertMockLocation(getActivity());
//                                                Toasty.error(context, "Matikan Aplikasi Fake GPS dan Restart Device Android Anda").show();
                                                return;
                                            }
                                        }
                                        DistanceCalculator distanceCalculator = new DistanceCalculator();
                                        double jarak = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(destination.getLat(), destination.getLng()));
                                        if (Constants.DEBUG) {
                                            Log.i("jarak ke cusutomer ", jarak + "");
                                        }
                                        if (jarak < Constants.TOLERANSI_JARAK) {
                                            sentReport(getEtContentReasonTokoTutup.getText().toString(), "closed");

                                            /* custom function */
//                                            setReport_toko_tutup(getEtContentReasonTokoTutup.getText().toString());
//                                            setSubmitTokoTutup(true);
                                            /* custom function end */

//                                            getEtContentReasonTokoTutup.setText("");
                                        } else {
                                            String message = "Anda belum berada di customer " + customerName + ", cek lokasi anda pada Map";
                                            Toasty.info(context, message).show();
                                        }

                                    } else {
                                        Toasty.error(context, "Pastikan gps anda aktif").show();
                                    }
                                }
                            });
                            mFusedLocationClient.getLastLocation().addOnFailureListener(getActivity(), new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("error fused location", e.getMessage());
                                    e.printStackTrace();
                                }
                            });
                        }

                    } else {
                        Toasty.info(context, "Alasan terlalu pendek").show();
                    }
                } else {
                    Toasty.info(context, "Anda belum melakukan Checkin").show();
                }

//                if(isSubmitTokoTutup){
//                    btnSubmitTokoTutup.setVisibility(View.GONE);
//                }else{
//                    btnSubmitTokoTutup.setVisibility(View.VISIBLE);
//                }
            }
        });

        btnSubmitOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nfcCode.trim().contentEquals(customerCode.trim())) {
                    if (etContentReasonOther.getText().toString().length() > 9) {
                        sentReport(etContentReasonOther.getText().toString(), "other");

                        /* custom function */
//                        setReport_lainnya(etContentReasonOther.getText().toString());
//                        btnSubmitOther.setVisibility(view.GONE);
                        /* custom function end */
//                        etContentReasonOther.setText("");
                    } else {
                        Toasty.info(context, "Alasan terlalu pendek").show();
                    }

                } else {
                    Toasty.info(context, "Pastikan anda telah Checkin untuk customer " + customerName).show();
                }
            }
        });

        btnSubmitPrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nfcCode.trim().contentEquals(customerCode.trim())) {
                    if (etContentReasonPrinter.getText().toString().length() > 9) {
                        sentReport(etContentReasonPrinter.getText().toString(), "print");
//                        etContentReasonPrinter.setText("");
                        /*
                        *  custom function
                        * */
//                        setReport_cetak(etContentReasonPrinter.getText().toString());
//                        btnSubmitPrinter.setVisibility(view.GONE);

                        /*
                        *  custom function end
                        * */
                    } else {
                        Toasty.info(context, "Alasan terlalu pendek").show();
                    }
                } else {
                    Toasty.info(context, "Pastikan anda telah Checkin untuk customer " + customerName).show();
                }
            }
        });

        /**
         * hide report type NFC if not using nfc
         */
        lrNFC.setVisibility(View.GONE);
        lrNFC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lrReasonNFC.isShown() || tvReasonNFC.isShown() || etContentReasonNFC.isShown() || btnSubmitNFC.isShown()) {
                    lrReasonNFC.setVisibility(View.GONE);
                    tvReasonNFC.setVisibility(View.GONE);
                    etContentReasonNFC.setVisibility(View.GONE);
                    btnSubmitNFC.setVisibility(View.GONE);
                } else {
                    lrReasonNFC.setVisibility(View.VISIBLE);
                    tvReasonNFC.setVisibility(View.VISIBLE);
                    etContentReasonNFC.setVisibility(View.VISIBLE);
                    btnSubmitNFC.setVisibility(View.VISIBLE);
                }
            }
        });

        lrLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lrReasonLocation.isShown() || tvReasonLocation.isShown() || etContentReasonLocation.isShown() || btnSubmitLocation.isShown()) {
                    lrReasonLocation.setVisibility(View.GONE);
                    tvReasonLocation.setVisibility(View.GONE);
                    etContentReasonLocation.setVisibility(View.GONE);
                    btnSubmitLocation.setVisibility(View.GONE);
                } else {
                    lrReasonLocation.setVisibility(View.VISIBLE);
                    tvReasonLocation.setVisibility(View.VISIBLE);
                    etContentReasonLocation.setVisibility(View.VISIBLE);
                    btnSubmitLocation.setVisibility(View.VISIBLE);
                }
            }
        });

        lrTokoTutup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lrReasonTokoTutup.isShown() || tvReasonTokoTutup.isShown() || getEtContentReasonTokoTutup.isShown() || btnSubmitTokoTutup.isShown()) {
                    lrReasonTokoTutup.setVisibility(View.GONE);
                    tvReasonTokoTutup.setVisibility(View.GONE);
                    getEtContentReasonTokoTutup.setVisibility(View.GONE);
                    btnSubmitTokoTutup.setVisibility(View.GONE);
                } else {
                    lrReasonTokoTutup.setVisibility(View.VISIBLE);
                    tvReasonTokoTutup.setVisibility(View.VISIBLE);
                    getEtContentReasonTokoTutup.setVisibility(View.VISIBLE);
                    btnSubmitTokoTutup.setVisibility(View.VISIBLE);
                }
            }
        });
        lrOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lrReasonOther.isShown() || tvReasonOther.isShown() || etContentReasonOther.isShown() || btnSubmitOther.isShown()) {
                    lrReasonOther.setVisibility(View.GONE);
                    tvReasonOther.setVisibility(View.GONE);
                    etContentReasonOther.setVisibility(View.GONE);
                    btnSubmitOther.setVisibility(View.GONE);
                } else {
                    lrReasonOther.setVisibility(View.VISIBLE);
                    tvReasonOther.setVisibility(View.VISIBLE);
                    etContentReasonOther.setVisibility(View.VISIBLE);
                    btnSubmitOther.setVisibility(View.VISIBLE);
                }
            }
        });

        lrPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lrReasonPrint.isShown() || tvReasonPrinter.isShown() || etContentReasonPrinter.isShown() || btnSubmitPrinter.isShown()) {
                    lrReasonPrint.setVisibility(View.GONE);
                    tvReasonPrinter.setVisibility(View.GONE);
                    etContentReasonPrinter.setVisibility(View.GONE);
                    btnSubmitPrinter.setVisibility(View.GONE);
                } else {
                    lrReasonPrint.setVisibility(View.VISIBLE);
                    tvReasonPrinter.setVisibility(View.VISIBLE);
                    etContentReasonPrinter.setVisibility(View.VISIBLE);
                    btnSubmitPrinter.setVisibility(View.VISIBLE);
                }
            }
        });

        if (UserUtil.getInstance(context).getRoleUser().equals(Constants.ROLE_DRIVER)) {
            lrPrint.setVisibility(View.GONE);
            lrReasonPrint.setVisibility(View.GONE);
        }

        /* custom function */

        if(getReport_lokasi() != null){
            disableComponent(btnSubmitLocation, etContentReasonLocation);
        }

        /* custom function end*/


        return view;
    }

    private void disableComponent(Button button, EditText editText){
        button.setVisibility(View.GONE);
        editText.setClickable(false);
        editText.setFocusableInTouchMode(false);
    }

    /**
     * send data report to server
     * @param note
     * @param type
     */
    private void sentReport(String note, String type) {
        ApiInterface service = ApiClient.getInstance(context);
        UserUtil userUtil = UserUtil.getInstance(context);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", "report");
        jsonObject.addProperty("notes", note);
        jsonObject.addProperty("customer_code", customerCode);

        JsonObject jsonObjectDesc = new JsonObject();
        jsonObjectDesc.addProperty("type", type);
        if(type.equals("nfc") || type.equals("closed")){
            UserUtil.getInstance(context).calculateDistancePoint(userLatitude, userLongitude);
            jsonObjectDesc.addProperty("distance", UserUtil.getInstance(context).getDistancePoint());
        } else if(type.equals("location")){
            jsonObjectDesc.addProperty("distance", distance);
            jsonObjectDesc.addProperty("customer_latitude", destination.getLat());
            jsonObjectDesc.addProperty("customer_longitude", destination.getLng());
            jsonObjectDesc.addProperty("user_latitude", userLatitude);
            jsonObjectDesc.addProperty("user_longitude", userLongitude);
        }
        jsonObject.add("description", jsonObjectDesc);
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            jsonObject.addProperty("delivery_plan_id", PlanUtil.getInstance(context).getPlanId());
        } else {
            jsonObject.addProperty("visit_plan_id", PlanUtil.getInstance(context).getPlanId());
        }

        Call<ResponseArrayObject> call = service.permissionAlertPost("JWT " + userUtil.getJWTTOken(), jsonObject);
        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                if (response.body() != null && response.body().getError() == 0) {
                    Toasty.info(context, "Laporan berhasil dikirim").show();
                } else {
                    Toasty.info(context, "Terjadi kesalahan").show();
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                Toasty.error(context, "Terjadi kesalahan server").show();
            }
        });
    }
}
