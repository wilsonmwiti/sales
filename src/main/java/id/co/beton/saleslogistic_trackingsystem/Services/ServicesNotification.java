package id.co.beton.saleslogistic_trackingsystem.Services;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import androidx.core.app.NotificationCompat;

import id.co.beton.saleslogistic_trackingsystem.R;


/**
 * Class ServicesNotification
 * for push notification to device
 */
public class ServicesNotification {

    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;
    private Bitmap icon;
    private Context context;
    String channelId = "channel_track_syistem";
    CharSequence channelName = "Break Time";
    int importance = NotificationManager.IMPORTANCE_LOW;

    public ServicesNotification(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            notificationManager.createNotificationChannel(notificationChannel);
            notificationManager.createNotificationChannelGroup(new NotificationChannelGroup("01", "tracking"));
        }
        clearAllNotifications();
        this.context=context;

    }

    public  void showSimpleNotification(String title,String text){
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.notification_icon);
        notificationBuilder = new NotificationCompat.Builder(context,channelId)
                .setContentTitle(title)
                .setContentText(text)
                .setChannelId(channelId)
                .setLargeIcon(icon)
                .setSmallIcon(R.drawable.ic_notif);

        sendNotification();
    }

    public void showWithButtonNotification(String type,String title, String text){
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                R.drawable.notification_icon);
        notificationBuilder = new NotificationCompat.Builder(context,channelId)
                .setSmallIcon(R.drawable.ic_notif)
                .setContentTitle(title)
                .setLargeIcon(icon)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                .setContentText(text);
        Intent stopIntent = new Intent(context,context.getClass());
        stopIntent.setAction("STOP");
        stopIntent.putExtra("type",type);
        PendingIntent pendingIntent =PendingIntent.getActivity(context,1,stopIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.addAction(R.drawable.ic_stop_black_24dp,"STOP",pendingIntent);

        sendNotification();
    }

    private void sendNotification() {
        Intent notificationIntent = new Intent(context,context.getClass());
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        notificationBuilder.setContentIntent(contentIntent);
        android.app.Notification notification = notificationBuilder.build();
        notification.flags |= android.app.Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= android.app.Notification.DEFAULT_SOUND;

        notificationManager.notify(1, notification);
    }

    private void clearAllNotifications() {
        if (notificationManager != null) {
            notificationManager.cancelAll();
        }
    }
}
