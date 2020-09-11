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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import id.co.beton.saleslogistic_trackingsystem.Adapter.packingslip.ItemPenerimaanAdapter;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Pic.PICActivity;
import id.co.beton.saleslogistic_trackingsystem.BaseActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Accepted;
import id.co.beton.saleslogistic_trackingsystem.Model.AcceptedProductItem;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.PackingSlip;
import id.co.beton.saleslogistic_trackingsystem.Model.Product;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class PenerimaanPackingSlipActivity
 * class for accepting order
 */
public class PenerimaanPackingSlipActivity extends BaseActivity {
    //private ArrayList<Item> items;
    private ListView lvItem;
    private Gson gson;
    private Destination destination;
    private PackingSlip packingSlip;
    private TextView tvPackingSlipNo;
    private TextView tv_pic;
    private Button btnKonfirmasi;
    EditText etQty;
    EditText etNote;
    Product pi;
    private Accepted accepted = new Accepted();
    DialogCustom dialogCustom;
    private static final int requestContact = 0;
    private static final int requestAddress = 1;
    String contacts="";
    String address="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penerimaan_packing_slip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        activity = PenerimaanPackingSlipActivity.this;

        try{
            gson = new Gson();
            destination = gson.fromJson(getIntent().getStringExtra("destinationPackingItem"), Destination.class);
            packingSlip = gson.fromJson(getIntent().getStringExtra("packingSlipItem"), PackingSlip.class);
            //date = getIntent().getStringExtra("datePackingItem");
            customerCode = destination.getCustomerCode();
            customerName = destination.getCustomerName();
            dialogCustom = new DialogCustom(context);

            setTitle(destination.getCustomerName());

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


            LinearLayout llPic = (LinearLayout) findViewById(R.id.ll_pic);
            llPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoChangePIC();
                }
            });

            lvItem = (ListView) findViewById(R.id.lv_item);
            //tvNoPb = findViewById(R.id.tv_packing_slip_no);
            tvPackingSlipNo = findViewById(R.id.tv_packing_slip_no);
            btnKonfirmasi = findViewById(R.id.btn_konfirmasi);
            tv_pic  = findViewById(R.id.tv_pic);
            btnKonfirmasi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDataAccepted();
                }
            });
            tvPackingSlipNo.setText(packingSlip.getIdPackingSlip());

            ItemPenerimaanAdapter itemAdapter =new ItemPenerimaanAdapter(context,1, packingSlip.getProduct());
            lvItem.setAdapter(itemAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void gotoChangePIC(){
        Intent intent =new Intent(context,PICActivity.class);
        intent.putExtra("cust_name",destination.getCustomerCode());
        intent.putExtra("cust_code",destination.getCustomerCode());
        startActivityForResult(intent,requestContact);
    }

    public void getDataAccepted(){
        if(tv_pic.getText().toString().contains("Pilih")){
            Toasty.error(context,"PIC belum dipilih", Toast.LENGTH_SHORT).show();
            return;
        }else{
            try{
                accepted.setAceptedBy(tv_pic.getText().toString());
                accepted.setDeliveryPlanId(PlanUtil.getInstance(this).getPlanId());
                List<AcceptedProductItem> productItems = new ArrayList<>();
                boolean statusMoreStock = false;
                boolean statusQuantityNull  = false;
                for (int i=0;i<lvItem.getCount();i++){
                    pi = packingSlip.getProduct().get(i);
                    View vaCurrent = lvItem.getChildAt(i);
                    etQty = vaCurrent.findViewById(R.id.et_qty_diterima);
                    etNote = vaCurrent.findViewById(R.id.et_catatan);

                    if(etQty.getText().toString().equals("")){
                        statusQuantityNull = true;
                        break;
                    } else {
                        if(Double.parseDouble(etQty.getText().toString())>pi.getQuantity()){
                            statusMoreStock =true;
                            break;
                        }
                    }

                    AcceptedProductItem productItem = new AcceptedProductItem(pi.getQuantity(),etNote.getText().toString(),pi.getItemNumber(),Double.parseDouble(etQty.getText().toString()),pi.getProductName().toString());
                    productItems.add(productItem);
                }
                accepted.setProduct(productItems);

                if(statusMoreStock || statusQuantityNull){
                    if(statusMoreStock){
                        Toasty.error(context,"Qty terima melebihi qty kirim", Toast.LENGTH_SHORT).show();
                    }
                    if(statusQuantityNull){
                        Toasty.error(context,"Qty harus diisi", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    postAccepted();
                }

                // if(statusMoreStock){
                //     Toasty.error(context,"Qty terima melebihi qty kirim", Toast.LENGTH_SHORT).show();
                // }else{
                //     postAccepted();
                // }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


    private void postAccepted(){
        final ApiInterface apiInterface = ApiClient.getInstance(context);
        JsonObject gsonObject = new JsonObject();
        gsonObject.addProperty("delivery_plan_id",PlanUtil.getInstance(context).getPlanId());
        gsonObject.addProperty("customer_code",destination.getCustomerCode());

        JsonArray jsonElements = new JsonArray();
        for (int i=0; i<accepted.getProduct().size();i++){
            JsonObject dataProd = new JsonObject();
            dataProd.addProperty("item_number",accepted.getProduct().get(i).getItemNumber());
            dataProd.addProperty("product_name",accepted.getProduct().get(i).getProductName());
            dataProd.addProperty("quantity",accepted.getProduct().get(i).getQuantity());
            dataProd.addProperty("accepted_quantity",accepted.getProduct().get(i).getAcceptedQuantity());
            dataProd.addProperty("notes",accepted.getProduct().get(i).getNotes().replaceAll("\n"," "));
            jsonElements.add(dataProd);
        }

        gsonObject.add("product",jsonElements);
        gsonObject.addProperty("accepted_by",accepted.getAceptedBy());

        if(Constants.DEBUG){
            //print parameter
            Log.i("PARAMETER ACCEPTED", "PARAMETER ACCEPTED  " + gsonObject);
        }

        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(this);
        dialogCustom.show();
        Call<ResponseArrayObject> call =apiInterface.accepted("JWT "+userUtil.getJWTTOken(),gsonObject, packingSlip.getIdPackingSlip());
        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                dialogCustom.hidden();
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    gotoDetailCustomer();
                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toasty.error(context,"Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                if(Constants.DEBUG){
                    Log.e("Response",call.toString());
                }
                Toasty.error(context,"Terjadi kesalahan server").show();
                dialogCustom.hidden();
            }

        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestContact && resultCode == Activity.RESULT_OK) {
            contacts = data.getStringExtra("detail_contact");
            tv_pic.setText(contacts);
        }else if(requestCode==requestAddress && resultCode==Activity.RESULT_OK){
            address = data.getStringExtra("detail_address");
        }
    }
}
