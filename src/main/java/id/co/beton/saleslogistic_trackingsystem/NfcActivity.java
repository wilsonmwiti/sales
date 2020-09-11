package id.co.beton.saleslogistic_trackingsystem;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.os.Build;
import android.os.Parcelable;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.DetailPlanActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.ParsedNdefRecord;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Nfc.NdefMessageParser;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.DetectFakeGPS;
import id.co.beton.saleslogistic_trackingsystem.Utils.DistanceCalculator;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class NFC Activity extend to Base Activity
 * triggered when user Tap NFC tag to device
 */
public class NfcActivity extends BaseActivity {
    private String TAG = NfcActivity.class.getSimpleName();
    protected NfcAdapter nfcAdapter;
    protected PendingIntent pendingIntent;
    public int type = 0;
    //protected String customerCode;
    protected double lat = 0, lng = 0;
    public Location location = null;

    private LocationManager mLocationManager = null;
    public UserUtil userUtil = UserUtil.getInstance(context);
    private ApiInterface service;
    protected boolean canTapIn = false;

    public NfcActivity() {
    }

    public NfcActivity(Context context) {
        service = ApiClient.getInstance(context);
        this.context = context;
    }

    /**
     * function to check type of nfc tag
     * @param tag
     * @return String of result nfc read
     */
    private String dumpTagData(Tag tag) {
        StringBuilder sb = new StringBuilder();
        byte[] id = tag.getId();
        sb.append("ID (hex): ").append(toHex(id)).append('\n');
        sb.append("ID (reversed hex): ").append(toReversedHex(id)).append('\n');
        sb.append("ID (dec): ").append(toDec(id)).append('\n');
        sb.append("ID (reversed dec): ").append(toReversedDec(id)).append('\n');

        String prefix = "android.nfc.tech.";
        sb.append("Technologies: ");
        for (String tech : tag.getTechList()) {
            sb.append(tech.substring(prefix.length()));
            sb.append(", ");
        }

        sb.delete(sb.length() - 2, sb.length());

        for (String tech : tag.getTechList()) {
            if (tech.equals(MifareClassic.class.getName())) {
                sb.append('\n');
                String type = "Unknown";

                try {
                    MifareClassic mifareTag = MifareClassic.get(tag);

                    switch (mifareTag.getType()) {
                        case MifareClassic.TYPE_CLASSIC:
                            type = "Classic";
                            break;
                        case MifareClassic.TYPE_PLUS:
                            type = "Plus";
                            break;
                        case MifareClassic.TYPE_PRO:
                            type = "Pro";
                            break;
                    }
                    sb.append("Mifare Classic type: ");
                    sb.append(type);
                    sb.append('\n');

                    sb.append("Mifare size: ");
                    sb.append(mifareTag.getSize() + " bytes");
                    sb.append('\n');

                    sb.append("Mifare sectors: ");
                    sb.append(mifareTag.getSectorCount());
                    sb.append('\n');

                    sb.append("Mifare blocks: ");
                    sb.append(mifareTag.getBlockCount());
                } catch (Exception e) {
                    sb.append("Mifare classic error: " + e.getMessage());
                }
            }

            if (tech.equals(MifareUltralight.class.getName())) {
                sb.append('\n');
                MifareUltralight mifareUlTag = MifareUltralight.get(tag);
                String type = "Unknown";
                switch (mifareUlTag.getType()) {
                    case MifareUltralight.TYPE_ULTRALIGHT:
                        type = "Ultralight";
                        break;
                    case MifareUltralight.TYPE_ULTRALIGHT_C:
                        type = "Ultralight C";
                        break;
                }
                sb.append("Mifare Ultralight type: ");
                sb.append(type);
            }
        }

        return sb.toString();
    }

    /**
     * function to convert bytes to hex format
     * @param bytes
     * @return string of converted value
     */
    private String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = bytes.length - 1; i >= 0; --i) {
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
            if (i > 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    /**
     * function to reverse byte hex
     * @param bytes
     * @return string hex
     */
    private String toReversedHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; ++i) {
            if (i > 0) {
                sb.append(" ");
            }
            int b = bytes[i] & 0xff;
            if (b < 0x10)
                sb.append('0');
            sb.append(Integer.toHexString(b));
        }
        return sb.toString();
    }

    /**
     * function to convert byte to decimal
     * @param bytes
     * @return decimal value
     */
    private long toDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = 0; i < bytes.length; ++i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    /**
     * function to reverse bytes decimal
     * @param bytes
     * @return decimal value
     */
    private long toReversedDec(byte[] bytes) {
        long result = 0;
        long factor = 1;
        for (int i = bytes.length - 1; i >= 0; --i) {
            long value = bytes[i] & 0xffl;
            result += value * factor;
            factor *= 256l;
        }
        return result;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (nfcAdapter != null) {
            if (!nfcAdapter.isEnabled())
                showNFCSetting();

            nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        }
    }

    /**
     * function to show NFC setting
     * called when user not turn NFC on yet
     */
    protected void showNFCSetting() {
        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        startActivity(intent);
    }

    /**
     * function onNewIntent
     * @param intent
     */
    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        resolveIntent(intent);
    }

    /**
     * function to read NFC Tag
     * @param intent
     */
    private void resolveIntent(Intent intent) {
        String action = intent.getAction();

        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            NdefMessage[] msgs;

            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];

                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            } else {
                byte[] empty = new byte[0];
                byte[] id = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);
                Tag tag = (Tag) intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                byte[] payload = dumpTagData(tag).getBytes();
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, id, payload);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
            }

            displayMsgs(msgs);
        }
    }

    /**
     * function to read NFC Tag and stored to variable customerCode
     * customerCode used for compare with current customer
     * @param msgs
     */
    public void displayMsgs(NdefMessage[] msgs) {
        if (msgs == null || msgs.length == 0)
            return;

        StringBuilder builder = new StringBuilder();
        List<ParsedNdefRecord> records = NdefMessageParser.parse(msgs[0]);
        final int size = records.size();
        // @BUG: Kemungkinan celah NFC Masuk
        for (int i = 0; i < size; i++) {
            ParsedNdefRecord record = records.get(i);
            String str = record.str();
            if (str.toLowerCase().contains("nfc_code")) {
                if (str.split(":").length > 0) {
                    customerCode = str.split(":")[1].trim();
                }
            }
            builder.append(str).append("\n");
        }

        if (Constants.DEBUG) {
            Log.i(TAG, builder.toString());
        }
        sentDataNfc();
    }

    /**
     * function to set variable global customerCode
     * @param customerCode
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * function to process result of NFC Read
     * type = 1 -> Check In in customer
     * type = 2 -> Check Out from customer
     * type = 3 -> Tap In in Start Branch
     * type = 4 -> Tap Out in End Branch
     */
    public void sentDataNfc() {
        Log.e("NfcActivity", "Type : " + type);
        Log.e("NfcActivity", "Customer Code : " + customerCode);
        // BUG: Kemungkinan celah Tap NFC
        if (customerCode != null && !customerCode.isEmpty()) {
            location = currentLocation;
            if (location == null) {
                location = getLastLocationService();
            }
            if(location != null){
                if(Constants.IS_CHECK_FAKE_GPS){
                    if(DetectFakeGPS.getInstance().isMockLocationOn(location, context)){
                        DetectFakeGPS.getInstance().alertMockLocation(this);
                        getLastKnownLocation();
//                        Toasty.error(context, "Matikan Aplikasi Fake GPS dan Restart Device Android Anda").show();
                        return;
                    }
                }
            }
            if (type == 1) {
                gotoDetailCustomer();
            } else if (type == 0) {
                //detect jarak jika kurang dr 100 proses
                //cek apakah sudah tap in dan lebih dr 1 menit
                if (location != null) {
                    if (userUtil.getStringProperty(Constants.NFC_CODE).equals("") || userUtil.getStringProperty(Constants.NFC_CODE).equals(null)) {
                        Toasty.info(context, "Anda belum tap in").show();
                    } else {
                        if (userUtil.getStringProperty(Constants.NFC_CODE).equals(userUtil.getStringProperty(Constants.CURRENT_PAGE_PLAN))) {
                            if (userUtil.getTimeVisitCustomer() != "") {
                                if (userUtil.checkTimeVisitCustomer()) {
                                    DistanceCalculator distanceCalculator = new DistanceCalculator();
                                    double jarak = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(lat, lng));
                                    if (jarak < Constants.TOLERANSI_JARAK) {
                                        userUtil.setStringProperty(Constants.NFC_CODE, "");
                                        postNfc("OUT");
                                        userUtil.setNfcCodeRoute(customerCode);
                                        stopVisitSevice();
                                        userUtil.getInstance(getBaseContext()).setIntProperty(Constants.ADDITIONAL_TIME_PERMISSION, 0);
                                        userUtil.removeStartVisit();
                                        userUtil.setstatusStopServiceIdleTime();
                                        //Toasty.info(context,"Anda berhasil tap out dari "+customerName,6000).show();

                                        Toasty.info(context, "Anda sekarang Tap Out di customer " + customerName).show();

                                        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                                        Calendar calendar = Calendar.getInstance();
                                        userUtil.setStringProperty(Constants.TAP_OUT_TIMEOUT, dateFormat.format(calendar.getTime()).toString());
                                        finish();
                                    } else {
                                        Toasty.info(context, "Anda tidak berada di customer " + customerName).show();
                                    }
                                } else {
                                    Toasty.info(context, "Waktu kunjungan terlalu sebentar").show();
                                }
                            } else {
                                Toasty.info(context, "Anda belum tap in").show();
                            }
                        } else {
                            Toasty.info(context, "Lokasi Tap Out tidak sesuai dengan lokasi Anda Tap In sebelumnya.").show();
                        }
                    }

                } else {
                    Toasty.error(context, "Lokasi tidak terbaca, pastikan GPS aktif").show();
                }
            } else if (type == 3) {
                if (location != null) {
                    DistanceCalculator distanceCalculator = new DistanceCalculator();
                    //cek koordinat with office
                    float[] koordinatOffice = userUtil.getKoordinatOffice();
                    String nfcCodeOffice = userUtil.getNFCodeOffice();
                    if (!userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE)) {
                        if (koordinatOffice[0] != 0) {
                            if (customerCode.equalsIgnoreCase(nfcCodeOffice)) {
                                double jarak = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(koordinatOffice[0], koordinatOffice[1]));
                                if (jarak < Constants.TOLERANSI_JARAK) {
                                    userUtil.setBooleanProperty(Constants.STATUS_OUT_OFFICE, true);
                                    userUtil.setBooleanProperty(Constants.STATUS_IN_OFFICE, false);
                                    //data.addProperty("tap_nfc_type","START");
                                    postNfc("START");

                                    //start all service
                                    starAllService();
                                    userUtil.setNfcCodeRoute(customerCode);
                                    //statusPostNFC = true;
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

                                        socketUserSet.emit("user-set", jsonObject);

                                    } catch (URISyntaxException e) {
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    getLastKnownLocation();
                                    Toasty.error(context, "Jarak terlalu jauh dari kantor").show();
                                }
                            } else {
                                Toasty.error(context, "Branch tidak ada di daftar").show();
                            }
                        } else {
                            Toasty.error(context, "Silahkan load halaman visit plan").show();
                        }
                    } else {
                        Toasty.error(context, "Anda belum melakukan Tap Start NFC").show();
                    }

                } else {
                    Toasty.error(context, "Lokasi tidak terbaca, pastikan GPS aktif").show();
                }
            } else if (type == 4) {
                if (userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE)) {
                    if (location != null) {
                        DistanceCalculator distanceCalculator = new DistanceCalculator();
                        //cek koordinat with office
                        float[] koordinatOffice = userUtil.getKoordinatOffice();
                        String nfcCodeOffice = userUtil.getNFCodeOffice();

                        if (koordinatOffice[0] != 0) {
                            if (customerCode.equalsIgnoreCase(nfcCodeOffice)) {
                                double jarak = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(koordinatOffice[0], koordinatOffice[1]));
                                if (jarak < Constants.TOLERANSI_JARAK) {
                                    //statusPostNFC = true;
                                    userUtil.setBooleanProperty(Constants.STATUS_OUT_OFFICE, false);
                                    userUtil.setBooleanProperty(Constants.STATUS_IN_OFFICE, true);
                                    //data.addProperty("tap_nfc_type","STOP");
                                    postNfc("STOP");
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

                                    } catch (URISyntaxException e) {
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    stopAllService();
                                    stopVisitSevice();
                                    userUtil.setNfcCodeRoute("");
                                } else {
                                    getLastKnownLocation();
                                    Toasty.error(context, "Jarak terlalu jauh dari kantor").show();
                                }
                            } else {
                                Toasty.error(context, "Branch tidak ada di daftar").show();
                            }
                        } else {
                            Toasty.error(context, "Silahkan load halaman visit plan").show();
                        }

                    } else {
                        Toasty.error(context, "Lokasi tidak terbaca, pastikan GPS aktif").show();
                    }
                } else {
                    Toasty.info(context, "Anda belum melakukan Tap Start NFC").show();
                }
            }
        } else {
            Toasty.error(context, "NFC Tidak sesuai format").show();
        }
    }

    /**
     * function to send data to backend
     * sales/driver activity
     * @param type
     */
    private void postNfc(String type) {
        PlanUtil planUtil = PlanUtil.getInstance(context);


        JsonObject data = new JsonObject();
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            data.addProperty("delivery_plan_id", planUtil.getPlanId());
        } else {
            data.addProperty("visit_plan_id", planUtil.getPlanId());
        }
        data.addProperty("tap_nfc_type", type);
        data.addProperty("nfc_code", customerCode);
        data.addProperty("route_breadcrumb", "");


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
//                    Toasty.info(context, "Sukses tap NFC", 70000).show();
                    try {
                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
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
                Toasty.error(context, "Gagal membaca NFC").show();
            }
        });
    }

    /**
     * function to go to detail customer after user Tap NFC
     */
    @Override
    protected void gotoDetailCustomer() {
        boolean statusTapIn = userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE);
        if (statusTapIn) {
            //query for detail and goto detail plan
            PlanUtil planUtil = PlanUtil.getInstance(context);
            location = currentLocation;
            if (location == null) {
                location = getLastLocationService();
            }

            if (userUtil.getStringProperty(Constants.TAP_OUT_TIMEOUT).equals("") || userUtil.getStringProperty(Constants.TAP_OUT_TIMEOUT).equals(null)) {
                // Jika Tap Out Timeout Kosong
                canTapIn = true;
            } else {
                try {
                    Date date_current, timeOutCheck;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    Calendar calendar = Calendar.getInstance();
                    String strDate = simpleDateFormat.format(calendar.getTime());
                    date_current = simpleDateFormat.parse(strDate);
                    timeOutCheck = simpleDateFormat.parse(userUtil.getStringProperty(Constants.TAP_OUT_TIMEOUT));

                    long diff = date_current.getTime() - timeOutCheck.getTime();
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
                    if (seconds > Constants.TAP_OUT_TIME_TIMEOUT) {
                        canTapIn = true;
                        userUtil.setStringProperty(Constants.TAP_OUT_TIMEOUT, "");
                    } else {
                        Toasty.info(context, seconds + " Detik lagi sebelum melakukan tap in kembali").show();
                    }
                } catch (Exception e) {
                    Toasty.info(context, "Kesalahan Aplikasi - Pada Saat Tap In").show();
                }
            }


            if (canTapIn) {
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
                                        if (location != null) {
                                            if(Constants.IS_CHECK_FAKE_GPS){
                                                if(DetectFakeGPS.getInstance().isMockLocationOn(location, context)){
                                                    DetectFakeGPS.getInstance().alertMockLocation(NfcActivity.this);
                                                    getLastKnownLocation();
//                                                    Toasty.error(context, "Matikan Aplikasi Fake GPS dan Restart Device Android Anda").show();
                                                    return;
                                                }
                                            }

                                            DistanceCalculator distanceCalculator = new DistanceCalculator();
                                            double jarak = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(destination.getLat(), destination.getLng()));
                                            if (jarak < Constants.TOLERANSI_JARAK) {
                                                // BUG: NFC Code dan Customer Code Global Variable di terapkan
                                                userUtil.setStringProperty(Constants.NFC_CODE, customerCode);
                                                userUtil.setStringProperty(Constants.CURRENT_PAGE_PLAN, destination.getCustomerCode());
                                                postNfc("IN");
                                                // @BUG: Validasi
                                                Intent intent = new Intent(context, DetailPlanActivity.class);

                                                userUtil.setstatusStopServiceIdleTime();
                                                userUtil.setStarUnloading();
                                                userUtil.setTimeVisitCustomer();

                                                startVisitService();
                                                intent.putExtra("destination", gson.toJson(destination, new TypeToken<Destination>() {
                                                }.getType()));
                                                intent.putExtra("customer_code", destination.getCustomerCode());
                                                intent.putExtra("customer_name", destination.getCustomerName());

                                                Toasty.info(context, "Anda sedang Tap In di customer " + destination.getCustomerName()).show();
                                                startActivity(intent);
                                            } else {
                                                getLastKnownLocation();
                                                Toasty.info(context, "Anda belum berada di customer " + destination.getCustomerName()).show();
                                            }

                                        } else {
                                            location = getLastLocationService();
                                            Toasty.error(context, "Lokasi tidak terbaca, pastikan GPS aktif").show();
                                        }
                                    } else {
                                        Toasty.error(context, "Customer tidak terdaftar").show();
                                    }
                                } catch (Exception e) {
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
                                        if (location != null) {
                                            if(Constants.IS_CHECK_FAKE_GPS){
                                                if(DetectFakeGPS.getInstance().isMockLocationOn(location, context)){
                                                    DetectFakeGPS.getInstance().alertMockLocation(NfcActivity.this);
                                                    getLastKnownLocation();
                                                    Toasty.error(context, "Matikan Aplikasi Fake GPS dan Restart Device Android Anda").show();
                                                    return;
                                                }
                                            }
                                            DistanceCalculator distanceCalculator = new DistanceCalculator();
                                            double jarak = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(destination.getLat(), destination.getLng()));
                                            if (jarak < Constants.TOLERANSI_JARAK) {
                                                // BUG: NFC Code dan Customer Code Global Variable di terapkan
                                                userUtil.setStringProperty(Constants.NFC_CODE, customerCode);
                                                userUtil.setStringProperty(Constants.CURRENT_PAGE_PLAN, destination.getCustomerCode());
                                                postNfc("IN");

                                                userUtil.setStarVisit();
                                                userUtil.setstatusStopServiceIdleTime();
                                                startVisitService();
                                                userUtil.setTimeVisitCustomer();

                                                Intent intent = new Intent(context, DetailPlanActivity.class);
                                                intent.putExtra("destination", gson.toJson(destination, new TypeToken<Destination>() {
                                                }.getType()));
                                                intent.putExtra("customer_code", destination.getCustomerCode());
                                                intent.putExtra("customer_name", destination.getCustomerName());

                                                Toasty.info(context, "Anda sedang Tap In di customer " + destination.getCustomerName()).show();
                                                startActivity(intent);
                                            } else {
                                                getLastKnownLocation();
                                                Toasty.info(context, "Anda belum berada di customer " + destination.getCustomerName()).show();
                                            }
                                        } else {
                                            Toasty.error(context, "Lokasi tidak terbaca, pastikan GPS aktif").show();
                                        }
                                    } else {
                                        Toasty.error(context, "Customer tidak terdaftar").show();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseObject> call, Throwable t) {
                            Toasty.error(context, "Tidak ada koneksi internet").show();
                        }
                    });
                }
            }
            canTapIn = false;
        } else {
            Toasty.info(context, "Anda belum Tap Start dikantor.").show();
        }
    }

    /**
     * function to get Last Location
     * @return Location
     */
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

}