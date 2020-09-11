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
import android.widget.LinearLayout;

import com.google.gson.Gson;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.PackingSlip;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

/**
 * Class PenolakanPackingSlipActivity
 * class for rejecting Packing Slip
 */
public class PenolakanPackingSlipActivity extends AppCompatActivity {
    private Destination destinationItem;
    private PackingSlip packingSlipIdItem;
    private String date;
    private Gson gson;
    private LinearLayout llPembatalanOrder,llBukanPesananToko,llSalahAlamatPengiriman,llKirimKetempatLain;
    private Context context;
    DialogCustom dialogCustom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_penolakan_packing_slip);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        gson = new Gson();
        destinationItem = gson.fromJson(getIntent().getStringExtra("destinationPackingItem"), Destination.class);
        packingSlipIdItem = gson.fromJson(getIntent().getStringExtra("packingSlipItem"), PackingSlip.class);
        date = getIntent().getStringExtra("datePackingItem");
        dialogCustom = new DialogCustom(context);

        setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        context= this;

        llPembatalanOrder = (LinearLayout) findViewById(R.id.ll_pembatalan_order);
        llBukanPesananToko =(LinearLayout) findViewById(R.id.ll_bukan_pesanan_toko);
        llSalahAlamatPengiriman  =(LinearLayout) findViewById(R.id.ll_salah_alamat_pengiriman);
        llKirimKetempatLain = (LinearLayout) findViewById(R.id.ll_kirim_ketempat_lain);

        llPembatalanOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSalahAlamat("cancel_order");
            }
        });

        llBukanPesananToko.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSalahAlamat("wrong_order");
            }
        });

        llSalahAlamatPengiriman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSalahAlamat("wrong_address");
            }
        });

        llKirimKetempatLain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoSalahAlamat("another_address");
            }
        });
    }

    private void gotoBatalOrder(String jenisBatalOrder){
        Intent intent =new Intent(context,PembatalanOrderActivity.class);
        intent.putExtra("destinationPackingItem",getIntent().getStringExtra("destinationPackingItem"));
        intent.putExtra("packingSlipItem",getIntent().getStringExtra("packingSlipItem"));
        intent.putExtra("datePackingItem",getIntent().getStringExtra("datePackingItem"));
        intent.putExtra("rejected_by", UserUtil.getInstance(context).getName());
        intent.putExtra("jenis_batal_order",jenisBatalOrder);
        startActivity(intent);
    }

    private void gotoSalahAlamat(String jenisBatalOrder){
        Intent intent =new Intent(context,SalahAlamatActivity.class);
        intent.putExtra("destinationPackingItem",getIntent().getStringExtra("destinationPackingItem"));
        intent.putExtra("packingSlipItem",getIntent().getStringExtra("packingSlipItem"));
        intent.putExtra("datePackingItem",getIntent().getStringExtra("datePackingItem"));
        intent.putExtra("rejected_by", UserUtil.getInstance(context).getName());
        intent.putExtra("jenis_batal_order",jenisBatalOrder);
        startActivityForResult(intent,100);
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
        if(resultCode== Activity.RESULT_OK && requestCode==100){
            setResult(Activity.RESULT_OK,null);
            finish();
        }
    }
}
