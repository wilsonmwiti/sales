package id.co.beton.saleslogistic_trackingsystem;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.MainActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Setting;
import id.co.beton.saleslogistic_trackingsystem.Model.User;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Services.AllService;
import id.co.beton.saleslogistic_trackingsystem.Utils.DetectFakeGPS;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LOGIN";
    private EditText etPassword;
    private EditText etEmail;
    private TextView versionApp;
    private Context context;
    DialogCustom dialogCustom;
    UserUtil userUtil;
    Setting setting;
    AllService allService;
    private ApiInterface service;

    /**
     * function onCreate Login Activity
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnSign = findViewById(R.id.btn_sign);
        etEmail = findViewById(R.id.et_email);

        /*
        * custom function
        * */
        getPref();

        etPassword = findViewById(R.id.et_password);
        versionApp = findViewById(R.id.version_app);
        context = this;
        dialogCustom = new DialogCustom(context);
        service = ApiClient.getInstance(context);

        FirebaseApp.initializeApp(context);
        allService = new AllService(context);
        userUtil = UserUtil.getInstance(context);
        if (userUtil.isLoggedIn()) {
            if(userUtil.getRoleUser().equals(Constants.ROLE_SALES)){
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date date = new Date();
                try {
                    Date lastLogin = dateFormat.parse(UserUtil.getInstance(context).getLastLogin());
                    long diff = date.getTime() - lastLogin.getTime();
                    long day = (diff / (1000 * 60 * 60 * 24));
                    if (day >= 1) {
                        // forced logout
                        logout("JWT "+userUtil.getJWTTOken(), true);
                        // UserUtil.getInstance(context).reset();
                    } else {
                        gotoMainAcivity();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                gotoMainAcivity();
            }
        }

        btnSign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constants.DEBUG) {
                    Log.d(TAG, "Email" + etEmail.getText().toString());
                }
                if (etEmail.getText().toString().length() == 0 || etPassword.getText().toString().length() == 0) {
                    Toasty.info(context, "Username dan password harus di isi", Toast.LENGTH_SHORT).show();
                } else {
                    postLogin(etEmail.getText().toString(), etPassword.getText().toString());
                }
            }
        });

        // load version name
        String myVersionName = userUtil.getVersionApps(context);
        versionApp.setText("v" + myVersionName);

        // add version name
        // String myVersionName = "";
        // try {
        //     PackageManager packageManager = getApplicationContext().getPackageManager();
        //     String packageName = getApplicationContext().getPackageName();
        //     myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        // } catch (PackageManager.NameNotFoundException e) {
        //     e.printStackTrace();
        // }
        // versionApp.setText("V" + myVersionName);
    }

    /**
     * function to check the entered username and password
     * @param username
     * @param password
     */
    private void postLogin(final String username, String password) {

        JsonObject gsonObject = new JsonObject();

        gsonObject.addProperty("username", username);
        gsonObject.addProperty("password", password);

        if (Constants.DEBUG) {
            //print parameter
            Log.i(TAG, "PARAMETER LOGIN  " + gsonObject);
        }
        dialogCustom.show();
        Call<ResponseObject> call = service.login(gsonObject);
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body() != null && response.body().getError() == 0) { //if sukses
                    Gson gson = new Gson();
                    User user = gson.fromJson(response.body().getData().toString(), User.class);
                    UserUtil.getInstance(context).signIn(user);
                    final String deviceToken = FirebaseInstanceId.getInstance().getToken();
                    JsonObject token = new JsonObject();

                    token.addProperty("token", deviceToken);

                    Call<ResponseObject> call1 = service.deviceToken("JWT " + UserUtil.getInstance(context).getJWTTOken(), token);
                    call1.enqueue(new Callback<ResponseObject>() {
                        @Override
                        public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                            if (Constants.DEBUG) {
                                Log.i(TAG, "Sukses post token");
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseObject> call, Throwable t) {

                        }
                    });
                    loadDataSetting();
                    /*
                    *
                    * custom function
                    * */
                    setPref(username);

                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context, jsonObject.getString("message"), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Log.e(TAG, response.body().toString());
                        Toasty.error(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                    dialogCustom.hidden();
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                if (Constants.DEBUG) {
                    Log.e(TAG, call.toString());
                    Log.e(TAG, t.toString());
                }
                String responseError = t.toString();
                if (responseError.contains("Failed to connect to")) {
                    Toasty.error(context, "Periksa koneksi internet anda", Toast.LENGTH_SHORT).show();
                } else {
                    Toasty.error(context, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                }
                dialogCustom.hidden();
            }

        });
    }

    /**
     * function to call Activity MainActivity
     */
    private void gotoMainAcivity() {
        dialogCustom.hidden();
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * function to check whether on an android device there are applications that are not permitted
     * (Fake GPS application)
     */
    private void checkMockApps(){
        dialogCustom.hidden();

        boolean found = false;
        List<String> disallowApps = new ArrayList<>();
        if(setting.getBlacklistApps() != null){
            String[] listApps = setting.getBlacklistApps().split(",");
            if(listApps.length>0){
                List<String> blackListApps = new ArrayList<>(Arrays.asList(listApps));
                JsonObject mockLocationApps = DetectFakeGPS.getInstance().getListOfFakeLocationApps(context);
                JsonArray jsonArray = mockLocationApps.getAsJsonArray("mock_app");

                if(jsonArray.size()>0){
                    for(int i=0; i<jsonArray.size(); i++){
                        JsonObject jsonObject = jsonArray.get(i).getAsJsonObject();
                        if(blackListApps.contains(jsonObject.get("package_id").toString().replace("\"", ""))) {
                            disallowApps.add(jsonObject.get("package_name").toString().replace("\"", ""));
                            found = true;
                        }
                    }
                }
                if(Constants.DEBUG){
                    System.out.println("Definition Blacklist Apps " + blackListApps);
                    System.out.println("Application Mock Location " + jsonArray);
                    System.out.println("Application Mock Location Filtered " + disallowApps);
                }
            }
        }
        if(found){
            if(Constants.DEBUG){
                showDialogAlert(disallowApps);
                Toasty.error(context, "Mendeteksi Aplikasi Terlarang").show();
                gotoMainAcivity();
            }else{
                showDialogAlert(disallowApps);
                logout("JWT "+UserUtil.getInstance(context).getJWTTOken(), true);
                Toasty.error(context, "Mendeteksi Aplikasi Terlarang").show();
            }
        } else {
            gotoMainAcivity();
        }
    }

    /**
     * function to load Setting General from Backend
     */
    private void loadDataSetting(){
        Call<ResponseObject> callService = service.settingGeneral("JWT "+userUtil.getJWTTOken());

        /*Log the URL called*/
        if(Constants.DEBUG){
            Log.i("URL Called", callService.request().url() + "");
        }

        callService.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> callService, Response<ResponseObject> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    try{
                        Gson gson =new Gson();
                        setting = gson.fromJson(response.body().getData().toString(), Setting.class);
                        downloadImageLogo();

                        if(Constants.IS_CHECK_FAKE_GPS){
                            checkMockApps();
                        } else{
                            gotoMainAcivity();
                        }

                        // comment one of two below
                        // checkMockApps();
                        // gotoMainAcivity();
                    }catch (Exception e){
                        Log.e(TAG,e.toString());
                    }

                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toasty.error(context,"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> callService, Throwable t) {
                Toasty.error(context,"Terjadi kesalahan server").show();
            }
        });
    }

    /**
     * function to create image logo from setting
     * used as a logo on the print receipt (invoice)
     */
    private void downloadImageLogo(){
        try{
            String rawImage = setting.getLogoImage();
            if(rawImage!=null){
                Bitmap imageBitmap = convertBase64ToImage(rawImage);
                String storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                File file = new File(storageDir, Constants.FILENAME_LOGO);
                FileOutputStream out = new FileOutputStream(file);
                imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } else {
                try {
                    Bitmap defaultLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_print_beton);
                    String storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
                    File file = new File(storageDir, Constants.FILENAME_LOGO);
                    FileOutputStream out = new FileOutputStream(file);
                    defaultLogo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    out.flush();
                    out.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Function for convert string base64encode to image Bitmap
     * @param base64Encoded
     * @return image Bitmap
     */
    private Bitmap convertBase64ToImage(String base64Encoded){
        final byte[] decodedBytes = Base64.decode(base64Encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    /**
     * function for show info list of forbidden application
     * @param listApps
     */
    private void showDialogAlert(List listApps){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        StringBuilder message = new StringBuilder();
        message.append("Silahkan hapus aplikasi berikut: ");
        message.append(System.getProperty("line.separator"));
        for(int i =0; i<listApps.size(); i++){
            message.append(" - ");
            message.append(listApps.get(i));
            message.append(System.getProperty("line.separator"));
        }

        alertDialogBuilder.setTitle("Aplikasi Terlarang");
        alertDialogBuilder
                .setMessage(message.toString())
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    /**
     * function onBackPress
     */
    @Override
    public void onBackPressed() {
        System.exit(0);
    }

    /*
     *  custom function
     *
     * save and load username as preferences
     * */


    public void setPref(String username){
        SharedPreferences pref = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString("pref_username", username);
        edit.commit();
    }

    private void getPref(){
        SharedPreferences pref = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String username = pref.getString("pref_username", "");
        etEmail.setText(username);
    }

}
