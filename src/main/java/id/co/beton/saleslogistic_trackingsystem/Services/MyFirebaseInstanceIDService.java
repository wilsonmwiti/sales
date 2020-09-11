package id.co.beton.saleslogistic_trackingsystem.Services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.JsonObject;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class MyFirebaseInstanceIDService
 *
 */
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG =MyFirebaseInstanceIDService.class.getSimpleName() ;

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        pushTokenToserver(refreshedToken);

    }

    private void pushTokenToserver(String deviceToken){
        final ApiInterface apiInterface = ApiClient.getInstance(getApplicationContext());
        JsonObject token = new JsonObject();

        token.addProperty("token",deviceToken);
        Call<ResponseObject> call1 = apiInterface.deviceToken(UserUtil.getInstance(getApplicationContext()).getJWTTOken(),token);
        call1.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if(Constants.DEBUG){
                    Log.i(TAG,"Sukses post token");
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {

            }
        });
    }
}
