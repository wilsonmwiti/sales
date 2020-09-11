package id.co.beton.saleslogistic_trackingsystem.AppBundle.PackingSlip;

import android.os.Bundle;
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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import id.co.beton.saleslogistic_trackingsystem.BaseActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.PackingSlip;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class PembatalanOrderActivity
 * class for cancelling order
 */
public class PembatalanOrderActivity extends BaseActivity {
    private EditText et_keterangan_penolakan;
    private DialogCustom dialogCustom;
    private Button btn_ok;
    private TextView tv_date_penolakan;
    private TextView tv_packing_slip_no;

    private Destination destinationItem;
    private PackingSlip packingSlipIdItem;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pembatalan_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        activity = PembatalanOrderActivity.this;

        try{
            gson = new Gson();
            destinationItem = gson.fromJson(getIntent().getStringExtra("destinationPackingItem"), Destination.class);
            packingSlipIdItem = gson.fromJson(getIntent().getStringExtra("packingSlipItem"), PackingSlip.class);
            customerCode = destinationItem.getCustomerCode();
            customerName = destinationItem.getCustomerName();

            dialogCustom = new DialogCustom(context);

            setTitle("Back");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            et_keterangan_penolakan =  findViewById(R.id.et_keterangan_penolakan);
            btn_ok = findViewById(R.id.btn_ok);
            tv_date_penolakan = findViewById(R.id.tv_date_penolakan);
            tv_packing_slip_no = findViewById(R.id.tv_packing_slip_no);

            tv_date_penolakan.setText(setDate());
            tv_packing_slip_no.setText(packingSlipIdItem.getIdPackingSlip());
            btn_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postRejected();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String setDate(){
        Date curDate = new Date();
        SimpleDateFormat format = new SimpleDateFormat();
        String DateToStr = format.format(curDate);
        format = new SimpleDateFormat("E, dd-MM-yyyy");
        DateToStr = format.format(curDate);
        return DateToStr;
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

    /**
     * post data rejected to server
     */
    private void postRejected(){
        final ApiInterface apiInterface = ApiClient.getInstance(context);
        JsonObject reason_reject = new JsonObject();
        reason_reject.addProperty("reason",getIntent().getStringExtra("jenis_batal_order"));
        reason_reject.addProperty("notes",et_keterangan_penolakan.getText().toString().replaceAll("\n"," "));

        JsonObject gsonObject = new JsonObject();
        gsonObject.addProperty("delivery_plan_id", PlanUtil.getInstance(this).getPlanId());
        gsonObject.addProperty("customer_code", destinationItem.getCustomerCode());
        gsonObject.addProperty("rejected_by",getIntent().getStringExtra("rejected_by"));
        gsonObject.add("reason_reject",reason_reject);

        JsonArray jsonElements = new JsonArray();
        for (int i=0; i<packingSlipIdItem.getProduct().size();i++){
            JsonObject dataProd = new JsonObject();
            dataProd.addProperty("item_number",packingSlipIdItem.getProduct().get(i).getItemNumber());
            dataProd.addProperty("product_name",packingSlipIdItem.getProduct().get(i).getProductName());
            dataProd.addProperty("quantity",packingSlipIdItem.getProduct().get(i).getQuantity());
            jsonElements.add(dataProd);
        }

        gsonObject.add("product",jsonElements);


        if(Constants.DEBUG){
            //print parameter
            Log.i("PARAMETER REJECTED", "PARAMETER REJECTED  " + gsonObject);
        }

        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(this);
        dialogCustom.show();

        Call<ResponseArrayObject> call =apiInterface.rejected("JWT "+userUtil.getJWTTOken(),gsonObject,packingSlipIdItem.getIdPackingSlip());
        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    /*Intent intent = new Intent(context, MainActivity.class);
                    startActivity(intent);
                    finish();*/
                    gotoDetailCustomer();
                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }catch (Exception e){
                        dialogCustom.hidden();
                        Toasty.error(context,"Terjadi kesalahan",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                if(Constants.DEBUG){
                    Log.e("Response",call.toString());
                }
                Toasty.error(context,"Terjadi kesalahan",Toast.LENGTH_SHORT).show();
                dialogCustom.hidden();
            }

        });
    }

}
