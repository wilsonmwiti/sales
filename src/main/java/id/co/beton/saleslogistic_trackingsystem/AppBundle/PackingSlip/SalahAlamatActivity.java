package id.co.beton.saleslogistic_trackingsystem.AppBundle.PackingSlip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Pic.PICActivity;
import id.co.beton.saleslogistic_trackingsystem.BaseActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.PackingSlip;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class SalahAlamatActivity
 *
 */
public class SalahAlamatActivity extends BaseActivity {
    private Destination destinationItem;
    private PackingSlip packingSlipIdItem;
    private EditText etKeteranganText;
    private TextView tvNamaPIC,tvAlamatPengiriman;
    private ImageView imvPic;
    private DialogCustom dialogCustom;
    private TextView tvDatePenolakan;
    private TextView tvPackingSlipNo;
    private Button btnOk;
    private static final int requestContact = 0;
    private static final int requestAddress = 1;
    String contacts="";
    String address="";
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salah_alamat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        activity = SalahAlamatActivity.this;

        gson = new Gson();
        destinationItem = gson.fromJson(getIntent().getStringExtra("destinationPackingItem"), Destination.class);
        packingSlipIdItem = gson.fromJson(getIntent().getStringExtra("packingSlipItem"), PackingSlip.class);

        customerCode = destinationItem.getCustomerCode();
        customerName = destinationItem.getCustomerName();

        dialogCustom = new DialogCustom(context);
        setTitle(destinationItem.getCustomerName());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        try{
            etKeteranganText = (EditText)findViewById(R.id.et_keterangan);
            tvNamaPIC = (TextView) findViewById(R.id.tv_nama_pic);
            imvPic = (ImageView) findViewById(R.id.imv_pic);
            tvAlamatPengiriman = (TextView) findViewById(R.id.tv_alamat_pengiriman);
            tvAlamatPengiriman.setText(destinationItem.getAddress());

            tvDatePenolakan = findViewById(R.id.tv_date_penolakan);
            tvPackingSlipNo = findViewById(R.id.tv_packing_slip_no);

            tvDatePenolakan.setText(setDate());
            tvPackingSlipNo.setText(packingSlipIdItem.getIdPackingSlip());
            btnOk = findViewById(R.id.btn_ok);

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    postRejected();
                }
            });

            imvPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoChangePIC();
                }
            });

            LinearLayout ll_pic = (LinearLayout) findViewById(R.id.ll_pic);
            ll_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoChangePIC();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private String setDate(){
        String today = ConversionDate.getInstance().today();
        return today;
    }


    private void gotoChangePIC(){
        Intent intent =new Intent(context,PICActivity.class);
        intent.putExtra("cust_name",destinationItem.getCustomerName());
        intent.putExtra("cust_code",destinationItem.getCustomerCode());

        startActivityForResult(intent,requestContact);
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
     * send data rejected to server
     */
    private void postRejected(){
        final ApiInterface apiInterface = ApiClient.getInstance(context);
        JsonObject reason_reject = new JsonObject();
        reason_reject.addProperty("reason",getIntent().getStringExtra("jenis_batal_order"));
        reason_reject.addProperty("notes", etKeteranganText.getText().toString().replaceAll("\n"," "));

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
                    Toasty.info(context,"Berhasil kirim").show();
                    //setResult(Activity.RESULT_OK,null);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestContact && resultCode == Activity.RESULT_OK) {
            contacts = data.getStringExtra("detail_contact");
            tvNamaPIC.setText(contacts);
        }else if(requestCode==requestAddress && resultCode==Activity.RESULT_OK){
            address = data.getStringExtra("detail_address");
            tvAlamatPengiriman.setText(address);
        }
    }
}
