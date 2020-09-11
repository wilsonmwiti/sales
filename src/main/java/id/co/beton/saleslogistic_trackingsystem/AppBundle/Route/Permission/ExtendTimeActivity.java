package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.Permission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.MainActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.PermissionAlertDataDescription;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class ExtendTimeActivity
 * request permission extend time for user
 */
public class ExtendTimeActivity extends AppCompatActivity {
    private static final String TAG = "ExtendTimeActivity";

    private Context context;
    DialogCustom dialogCustom;
    private String jenisExtend; //Visit atau Break
    private TextView tvJenisExtend;
    private Button btnSubmitExtend;
    private EditText etExtendTime,etAlasan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extend_time);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Back");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.context = this;

        Intent myIntent = getIntent(); // gets the previously created intent
        jenisExtend = myIntent.getStringExtra("jenisExtendTime");
        tvJenisExtend = (TextView) findViewById(R.id.tv_jenis_extend);
        btnSubmitExtend =(Button)findViewById(R.id.btn_submit_extend);
        etExtendTime =(EditText) findViewById(R.id.et_extend_time);
        etAlasan =(EditText) findViewById(R.id.et_alasan);

        dialogCustom = new DialogCustom(context);

        if(!jenisExtend.isEmpty() && jenisExtend.equals("Visit")){
            tvJenisExtend.setText("waktu kunjungan");
        }

        UserUtil userUtil = UserUtil.getInstance(this);
        if(!jenisExtend.isEmpty() && jenisExtend.equals("Visit") && userUtil.getRoleUser().equals(Constants.ROLE_DRIVER))
        {
            tvJenisExtend.setText("waktu unloading");
        }

        btnSubmitExtend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etExtendTime.getText().toString().length()==0 || etAlasan.getText().toString().length()==0){
                    Toasty.info(context,"Extend time dan reason harus di isi", Toast.LENGTH_SHORT).show();
                }else{
                    String type = "break_time";
                    if(!jenisExtend.isEmpty() && jenisExtend.equals("Visit")) {
                        type = "visit_time";
                    }
                    if(Integer.valueOf(etExtendTime.getText().toString())>60*24){
                        Toasty.info(context,"Extend time melebihi 24 jam", Toast.LENGTH_SHORT).show();
                    }else{
                        postExtendTime(etExtendTime.getText().toString(), etAlasan.getText().toString(), type);
                    }
                }
            }
        });
    }

    /**
     * send data request permission to server
     * @param description
     * @param reason
     * @param type
     */
    private void postExtendTime(String description, String reason, String type){
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(context);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface apiInterface = ApiClient.getInstance(context);

        JsonObject gsonObject = new JsonObject();
        // default subject
        String subject = "Extend Time";

        if(type.equals("visit_time")){
            subject = "Visit Time";
            gsonObject.addProperty("customer_code", userUtil.getNfcCode());
        }

        PermissionAlertDataDescription permissionAlertDataDescription = new PermissionAlertDataDescription();
        permissionAlertDataDescription.setTime(Integer.parseInt(description));
        permissionAlertDataDescription.setCustomerCode(userUtil.getNfcCode());

        gsonObject.addProperty("type", type);
        gsonObject.addProperty("subject", subject);
        gsonObject.addProperty("description", new Gson().toJson(permissionAlertDataDescription));
        gsonObject.addProperty("notes", reason);
        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            gsonObject.addProperty("delivery_plan_id", PlanUtil.getInstance(context).getPlanId());
        }else{
            gsonObject.addProperty("visit_plan_id", PlanUtil.getInstance(context).getPlanId());
        }

        if(Constants.DEBUG){
            //print parameter
            Log.i(TAG, "PARAMETER POST EXTEND TIME" + gsonObject);
        }

        /*Show custom dialog*/
        dialogCustom.show();

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseArrayObject> call = apiInterface.permissionAlertPost("JWT "+userUtil.getJWTTOken(), gsonObject);
        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {

                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    Toasty.success(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    gotoFragmentPermission();
                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }catch (Exception e){
                        dialogCustom.hidden();
                        Toasty.error(context,"Terjadi kesalahan.",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                if(Constants.DEBUG){
                    Log.e(TAG,"failure "+call.toString());
                    Log.e(TAG,"failure "+t.toString());
                }
                Toasty.error(context,"Terjadi kesalahan",Toast.LENGTH_SHORT).show();
                dialogCustom.hidden();
            }

        });
    }

    /**
     * go back to fragment permission
     */
    private void gotoFragmentPermission() {
        dialogCustom.hidden();
        Intent intent = new Intent(context, MainActivity.class);
        Bundle args = new Bundle();
        args.putInt("position",2);
        intent.putExtras(args);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
