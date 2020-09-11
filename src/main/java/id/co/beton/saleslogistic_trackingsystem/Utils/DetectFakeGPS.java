package id.co.beton.saleslogistic_trackingsystem.Utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class DetectFakeGPS
 * get list forbidden apk from server and check location whether from mock application or GPS Device
 */
public class DetectFakeGPS {
    private static DetectFakeGPS instance = null;

    /**
     * Instantiates a new Detect fake gps.
     */
    public DetectFakeGPS(){
    }

    /**
     * Get instance detect fake gps.
     *
     * @return the detect fake gps
     */
    public static DetectFakeGPS getInstance(){
        if(instance==null){
            instance = new DetectFakeGPS();
        }

        return instance;
    }

    /**
     * get list Fake GPS apps from hardcoded
     *
     * @return list
     */
    public List<String> getBlackListApps(){
        List<String> listApps = new ArrayList<>();
        listApps.add("com.fakegps.mock");
        listApps.add("com.lexa.fakegps");
        listApps.add("com.pe.fakegps");
        listApps.add("com.pe.fakegpsrun");
        listApps.add("org.hola.gpslocation");
        listApps.add("com.gsmartstudio.fakegps");
        listApps.add("com.ltp.pro.fakelocation");
        listApps.add("com.theappninjas.gpsjoystick");
        listApps.add("com.theappninjas.fakegpsjoystick");
        listApps.add("com.incorporateapps.fakegps.fre");
        listApps.add("com.marlon.floating.fake.location");
        listApps.add("com.xdoapp.virtualphonenavigation");
        listApps.add("com.usefullapps.fakegpslocationpro");
        listApps.add("com.blogspot.newapphorizons.fakegps");
        listApps.add("com.dreams.studio.apps.fake.gps.loaction.changer");
        // listApps.add("");
        // listApps.add("");
        return listApps;
    }

    /**
     * check location whether from mock application or GPS Device
     *
     * @param location the location
     * @param context  the context
     * @return boolean boolean
     */
//    public boolean isMockLocationOn(Location location, Context context) {
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//            return location.isFromMockProvider();
//        } else {
//            String mockLocation = "0";
//            try {
//                mockLocation = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            return !mockLocation.equals("0");
//        }
//    }

    /*
     * custom function
     * */
    public static boolean isMockLocationOn(Location location, Context context) {
        Log.d("GPS", "IsmocklocationON");
        boolean isMock = false;
        if (android.os.Build.VERSION.SDK_INT >= 18) {
            isMock = location.isFromMockProvider();
        } else {
            //isMock = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ALLOW_MOCK_LOCATION).equals("0");
            if (Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ALLOW_MOCK_LOCATION).equals("0"))
                isMock = false;
            else {
                isMock = true;
            }
        }
        return isMock;
    }

    /**
     * get list apps from device which need mock permission
     *
     * @param context the context
     * @return list of fake location apps
     */
    public JsonObject getListOfFakeLocationApps(Context context) {
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonElements = new JsonArray();

        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo applicationInfo : packages) {
            try {
                PackageInfo packageInfo = pm.getPackageInfo(applicationInfo.packageName, PackageManager.GET_PERMISSIONS);

                // Get Permissions
                String[] requestedPermissions = packageInfo.requestedPermissions;

                if (requestedPermissions != null) {
                    for (int i = 0; i < requestedPermissions.length; i++) {
                        if (requestedPermissions[i].equals("android.permission.ACCESS_MOCK_LOCATION")
                                && !applicationInfo.packageName.equals(context.getPackageName())) {

                            JsonObject jsonInfo = new JsonObject();
                            jsonInfo.addProperty("package_id", applicationInfo.packageName);
                            jsonInfo.addProperty("package_name", applicationInfo.loadLabel(pm).toString());
                            jsonElements.add(jsonInfo);
                        }
                    }
                    jsonObject.add("mock_app", jsonElements);
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return jsonObject;
    }


    public void alertMockLocation(Activity activity) {
        final Activity current = activity;

        AlertDialog.Builder dialog = new AlertDialog.Builder(current);

        dialog.setTitle("MOCK LOCATION DETECTED!")
                .setInverseBackgroundForced(true)
                //.setIcon(R.drawable.ic_info_black_24dp)
                .setMessage("Please uninstall fake gps application then restart the phone")
                .setPositiveButton("Sure OK, I'll do it", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialoginterface, int i) {
                        dialoginterface.dismiss();
                        current.finish();
                    }
                }).show();
    }

}
