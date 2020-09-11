package id.co.beton.saleslogistic_trackingsystem.Services;

import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import java.util.Map;


/**
 * Class MyFirebaseMessagingService
 * to get firebase message from backend
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    final Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a data payload.
        try {
            Log.e(TAG, "Dapat pesan firebase dari backend");
            if (remoteMessage.getData().size() > 0) {
                final Map<String, String> data = remoteMessage.getData();
                ServicesNotification servicesNotification = new ServicesNotification(getBaseContext());
                servicesNotification.showSimpleNotification(data.get("title"), data.get("text"));
                if (data.get("type").contentEquals("break_time")) {
                    int restTime = UserUtil.getInstance(getBaseContext()).getRestTime();
                    String addTime = (String) data.get("description");
                    UserUtil.getInstance(getBaseContext()).setRestTime(restTime + Integer.parseInt(addTime));
                    UserUtil.getInstance(getBaseContext()).setIntProperty(Constants.SECONDS, restTime + Integer.parseInt(addTime));

                } else if (data.get("type").contentEquals("error_nfc")) {
                    Log.e(TAG, "Dapat Type Error NFC");
                    Log.e(TAG, "Customer : " + data.get("customer_code"));
                    Log.e(TAG, "Type Tap : " + data.get("type_tap"));
                    if (data.get("type_tap").equalsIgnoreCase("in")) {
                        Log.e(TAG, "Diberikan Tap In");
                        UserUtil userUtil = UserUtil.getInstance(getBaseContext());
                        userUtil.setStringProperty(Constants.NFC_CODE, data.get("customer_code"));
                        userUtil.setTimeVisitCustomer();
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                Toasty.success(getBaseContext(), "Tap in di customer " + data.get("customer_code") + " telah disetujui", Toasty.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.e(TAG, "Masuk Ke Else");
                        UserUtil.getInstance(getBaseContext()).setStringProperty(Constants.NFC_CODE, "");
                        UserUtil.getInstance(getBaseContext()).setNfcCodeRoute(data.get("customer_code"));
                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            public void run() {
                                Toasty.success(getBaseContext(), "Tap out di customer " + data.get("customer_code") + " telah disetujui", Toasty.LENGTH_SHORT).show();
                            }
                        });
                        //UserUtil.getInstance(getBaseContext()).setLatLngCustomer(Double.valueOf(data.get("lat")),Double.valueOf(data.get("lng")));
                    }
                } else if (data.get("type").contentEquals("visit_time")) {
                    int restTime = UserUtil.getInstance(getBaseContext()).getIntProperty(Constants.ADDITIONAL_TIME_PERMISSION);
                    String addTime = (String) data.get("description");
                    UserUtil.getInstance(getBaseContext()).setIntProperty(Constants.ADDITIONAL_TIME_PERMISSION, restTime + Integer.parseInt(addTime));
                } else if (data.get("type").contentEquals("closed")) {
                    UserUtil.getInstance(getBaseContext()).setStringProperty(Constants.NFC_CODE, "");
                    UserUtil.getInstance(getBaseContext()).setNfcCodeRoute(data.get("customer_code"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
