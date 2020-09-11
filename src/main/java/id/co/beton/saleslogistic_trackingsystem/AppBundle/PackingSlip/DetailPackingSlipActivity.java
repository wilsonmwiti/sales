package id.co.beton.saleslogistic_trackingsystem.AppBundle.PackingSlip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import id.co.beton.saleslogistic_trackingsystem.Adapter.packingslip.ItemAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.PackingSlip;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import es.dmoral.toasty.Toasty;

/**
 * Class DetailPackingSlipActivity
 * data detail packing slip
 */
public class DetailPackingSlipActivity extends AppCompatActivity {
    private static final String TAG = DetailPackingSlipActivity.class.getSimpleName();
    private ListView lvItem;
    private Button btnPenerimaan,btnPenolakan;
    private Context context;
    private Bundle inBundle;
    private LinearLayout llAlamat;
    private Gson gson;
    private Destination destination;
    private PackingSlip packingSlipId;
    private String date;

    private TextView tv_date;
    private TextView tv_packing_slip_no;
    private TextView tv_sales_order_no;
    private TextView tv_sales_order_date;
    private TextView tv_status;
    private TextView tv_nama_pic;

    private TextView tv_sales_rep;
    private TextView tv_order_by;
    private TextView tv_address;
    String nfcCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_packing_slip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try{
            gson = new Gson();
            destination = gson.fromJson(getIntent().getStringExtra("destinationPackingItem"), Destination.class);
            packingSlipId = gson.fromJson(getIntent().getStringExtra("packingSlipItem"), PackingSlip.class);
            date = getIntent().getStringExtra("datePackingItem");

            if(destination!=null){
                getSupportActionBar().setTitle(destination.getCustomerName());
            }else{
                getSupportActionBar().setTitle("-");
            }

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            context = this;

            lvItem = (ListView) findViewById(R.id.lv_item);
            ItemAdapter itemAdapter =new ItemAdapter(this,1,packingSlipId.getProduct());
            lvItem.setAdapter(itemAdapter);
            View header = getLayoutInflater().inflate(R.layout.header_detail_packing_slip,null);

            TextView tvStatus = (TextView )header.findViewById(R.id.tv_status);
            tvStatus.setText(packingSlipId.getStatus());

            /*TextView tvAlasan = (TextView )header.findViewById(R.id.tv_alasan);
            tvStatus.setText(packingSlipId.get());*/

            lvItem.addHeaderView(header);
            llAlamat = (LinearLayout) header.findViewById(R.id.ll_alamat);

            initUI();
            initValue();

            if(!getIntent().getBooleanExtra("status_from_detail_customer",false)){
                btnPenerimaan.setVisibility(View.GONE);
                btnPenolakan.setVisibility(View.GONE);
            }else{
                nfcCode = UserUtil.getInstance(context).getStringProperty(Constants.NFC_CODE);
                if(packingSlipId.getStatus().toLowerCase().contains("tolak") ||
                        packingSlipId.getStatus().toLowerCase().contains("terima")){
                    btnPenolakan.setVisibility(View.GONE);
                    btnPenerimaan.setVisibility(View.GONE);
                }


                btnPenolakan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(nfcCode.trim().contentEquals(destination.getCustomerCode().trim())){
                            Intent intent = new Intent(context,PenolakanPackingSlipActivity.class);
                            intent.putExtra("destinationPackingItem",getIntent().getStringExtra("destinationPackingItem"));
                            intent.putExtra("packingSlipItem",getIntent().getStringExtra("packingSlipItem"));
                            intent.putExtra("datePackingItem",getIntent().getStringExtra("datePackingItem"));
                            startActivityForResult(intent,99);
                        }else{
                            Toasty.info(context,"Pastikan anda telah checkin untuk customer "+destination.getCustomerName()).show();
                        }

                    }
                });

                btnPenerimaan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(nfcCode.trim().contentEquals(destination.getCustomerCode().trim())){
                            Intent intent = new Intent(context,PenerimaanPackingSlipActivity.class);
                            intent.putExtra("destinationPackingItem",getIntent().getStringExtra("destinationPackingItem"));
                            intent.putExtra("packingSlipItem",getIntent().getStringExtra("packingSlipItem"));
                            intent.putExtra("datePackingItem",getIntent().getStringExtra("datePackingItem"));
                            startActivityForResult(intent,99);
                        }else{
                            Toasty.info(context,"Pastikan anda telah checkin untuk customer "+destination.getCustomerName()).show();
                        }

                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public void initUI(){
        try{
            tv_date = findViewById(R.id.tv_date);
            tv_packing_slip_no = findViewById(R.id.tv_packing_slip_no);
            tv_sales_order_no = findViewById(R.id.tv_sales_order_no);
            tv_sales_order_date = findViewById(R.id.tv_sales_order_date);
            tv_status = findViewById(R.id.tv_status);
            tv_nama_pic = findViewById(R.id.tv_nama_pic);

            tv_sales_rep = findViewById(R.id.tv_sales_rep);
            tv_order_by = findViewById(R.id.tv_order_by);
            tv_address = findViewById(R.id.tv_address);

            btnPenerimaan = (Button) findViewById(R.id.btn_penerimaan);
            btnPenolakan = (Button) findViewById(R.id.btn_penolakan);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void initValue(){
        try{
            tv_date.setText(date);
            tv_packing_slip_no.setText(packingSlipId.getIdPackingSlip());
            tv_sales_order_no.setText(packingSlipId.getSalesOrderId());
            tv_sales_order_date.setText(date);
//        tv_status.setText(packingSlipIdItem.getIsConfirm());
            if(destination!=null){
                tv_nama_pic.setText(destination.getPicName());
                tv_order_by.setText(destination.getPicName());
                tv_address.setText(destination.getAddress());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

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
        if(resultCode== Activity.RESULT_OK && requestCode==99){
            finish();
        }
    }
}
