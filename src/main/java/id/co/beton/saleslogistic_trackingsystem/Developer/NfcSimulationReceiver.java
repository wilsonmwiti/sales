package id.co.beton.saleslogistic_trackingsystem.Developer;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;

import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.NfcActivity;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class NfcSimulationReceiver
 * broadcast receiver for develop Simulation Tap NFC
 */
public class NfcSimulationReceiver extends BroadcastReceiver {
    private int type = 0;
    private String customerCode;
    private int role;
    public static final int TAP_START = 3;
    public static final int TAP_STOP = 4;
    public static final int TAP_IN = 1;
    public static final int TAP_OUT = 0;

    public static final int IS_SALES = 0;
    public static final int IS_DRIVER = 1;
    private Context context;
    protected boolean processStatus = false;

    private Location currentBestLocation = null;

    @Override
    public void onReceive(final Context context, Intent intent) {
        this.processStatus = false;
        type = intent.getIntExtra("type", -1);
        if (intent.hasExtra("customer_code")) {
            customerCode = intent.getStringExtra("customer_code");
        }

        if (intent.hasExtra("role")) {
            role = intent.getIntExtra("role", -1);
        }
        Log.e("NfcSimulationReceiver", "Data Type yang diterima : " + type);
        final NfcActivity nfcActivity = new NfcActivity(context);
        UserUtil userUtil = nfcActivity.userUtil;
        this.context = context;
        final Location location = new Location("");

        if (type == NfcSimulationReceiver.TAP_START || type == NfcSimulationReceiver.TAP_STOP) {
            float[] koordinatOffice = userUtil.getKoordinatOffice();
            double lat = (double) koordinatOffice[0];
            double lng = (double) koordinatOffice[1];

            location.setLatitude(lat);
            location.setLongitude(lng);
            nfcActivity.currentLocation = location;
            nfcActivity.type = type;
            customerCode = userUtil.getNFCodeOffice();
            processStatus = true;
        } else if (type == NfcSimulationReceiver.TAP_IN || type == NfcSimulationReceiver.TAP_OUT) {
            Log.e("NfcSimulationReceiver", "Masuk Tap In");
            PlanUtil planUtil = PlanUtil.getInstance(context);
            id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface service = id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient.getClient(id.co.beton.saleslogistic_trackingsystem.Configuration.Constants.API_SERVER_ADDR).create(id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface.class);
            Call<ResponseObject> call;
            if (role == NfcSimulationReceiver.IS_SALES) {
                call = service.getVisitPlanDetailByCustomer("JWT " + userUtil.getJWTTOken(), planUtil.getPlanId(), customerCode);
            } else {
                call = service.getDeliveryPlanDetailByCustomer("JWT " + userUtil.getJWTTOken(), planUtil.getPlanId(), customerCode);
            }
            call.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (response.body() != null && response.body().getError() == 0) { //if sukses
                        try {
                            Gson gson = new Gson();
                            final Destination destination = gson.fromJson(response.body().getData().toString(), Destination.class);
                            if (destination.getCustomerCode() != null) {

                                double lat = destination.getLat();
                                double lng = destination.getLng();
                                Log.e("Latitude", String.valueOf(destination.getLat()));
                                Log.e("Longitude", String.valueOf(destination.getLng()));
                                location.setLatitude(lat);
                                location.setLongitude(lng);
                                if (type == NfcSimulationReceiver.TAP_IN) {
                                    nfcActivity.type = NfcSimulationReceiver.TAP_IN;
                                    Toast.makeText(context, "Harap Tunggu Proses Tap In", Toast.LENGTH_SHORT).show();
                                } else {
                                    nfcActivity.type = NfcSimulationReceiver.TAP_OUT;
                                    Toast.makeText(context, "Harap Tunggu Proses Tap Out", Toast.LENGTH_SHORT).show();
                                }
                                nfcActivity.currentLocation = location;
                                nfcActivity.setCustomerCode(customerCode);
                                nfcActivity.sentDataNfc();
                            } else {
                                Toasty.error(context, "Customer tidak terdaftar").show();
                            }
                        } catch (Exception e) {
                            Log.e("NfcSimulationReceiver", "Kesalahan mengambil data customer");
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    Toasty.error(context, "Tidak ada koneksi internet").show();
                }
            });
        } else {
            Toasty.error(context, "Tap NFC Tidak dikenali").show();
        }

        if (processStatus) {
            Toast.makeText(context, "Sedang di proses", Toast.LENGTH_SHORT).show();
            nfcActivity.setCustomerCode(customerCode);
            nfcActivity.sentDataNfc();
        }
    }
}