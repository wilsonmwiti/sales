package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.packingSlip.PackingSlipConfirmAdapter;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.MainActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.PackingSlip;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * class PackingSlipActivity
 * to confirm packingslip
 */
public class PackingSlipActivity extends AppCompatActivity {
    private Context context;
    private static final String TAG = PackingSlipActivity.class.getSimpleName();
    private ListView lvPackingSlip;
    private CheckBox cbSelectAll;
    private static RelativeLayout rlShowCheck;
    private static TextView tvJumlahPackingSlip;
    private static List<PackingSlip> packingSlips;
    private PackingSlipConfirmAdapter packingSlipConfirmAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_packing_slip);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Back");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.context = this;

        ConversionDate conversionDate = ConversionDate.getInstance();

        String jsonInvoices =  getIntent().getStringExtra("list_packing_slip");
        if(jsonInvoices!=null){
            try{
                Gson gson = new Gson();
                Type type = new TypeToken<List<PackingSlip>>(){}.getType();
                packingSlips = new ArrayList<>();
                packingSlips = gson.fromJson(jsonInvoices, type);

                LayoutInflater inflater = LayoutInflater.from(this);
                lvPackingSlip = (ListView)findViewById(R.id.lv_packing_slip);
                tvJumlahPackingSlip = (TextView)findViewById(R.id.tv_jumlah_packing_slip);

                packingSlipConfirmAdapter = new PackingSlipConfirmAdapter(context,1,packingSlips);
                lvPackingSlip.setAdapter(packingSlipConfirmAdapter);

                View header = inflater.inflate(R.layout.header_activity_packing_slip,null);
                TextView tvTanggal = (TextView)header.findViewById(R.id.tv_tanggal);

                tvTanggal.setText(ConversionDate.getInstance().today());

                lvPackingSlip.addHeaderView(header);

                TextView tvJumlahPackingSlipHeader = (TextView) header.findViewById(R.id.tv_jumlah_packing_slip_header);
                tvJumlahPackingSlipHeader.setText(packingSlips.size()+"");

                rlShowCheck = (RelativeLayout) findViewById(R.id.rl_show_check);

                rlShowCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get selected invoice and sent to server
                        confirmPackingSlip();
                    }
                });

                checkAll();
                checkState();
            }catch (Exception e){
                Log.e(TAG,e.toString());
            }
        }
    }

    /**
     * send changes to server
     */
    private void confirmPackingSlip() {
        final UserUtil userUtil = UserUtil.getInstance(context);
        PlanUtil planUtil = PlanUtil.getInstance(context);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);
        JsonArray jsonElements = new JsonArray();
        for (int i=0;i<packingSlips.size();i++){
            if (packingSlips.get(i).getSelected()){
                jsonElements.add(packingSlips.get(i).getIdPackingSlip());
            }
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("packing_slip_id",jsonElements);

        Call<ResponseArrayObject> call = service.confirmPackingSlip("JWT "+userUtil.getJWTTOken(), planUtil.getPlanId(),jsonObject);
        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                if(response.body()!=null && response.body().getError()==0){
                    userUtil.setBooleanProperty(Constants.STATUS_CONFIRM_INVOICE,true);
                    Toasty.info(context,response.body().getMessage()).show();
                    Intent intent = new Intent(context,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                Toasty.error(context,"Terjadi kesalahan server").show();
            }
        });
    }

    /**
     * check all packingslip if checkbox 'select all' selected
     */
    public void checkAll(){
        cbSelectAll = (CheckBox)findViewById(R.id.cb_select_all);
        cbSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox)v).isChecked()){
                    for (int i=0;i<packingSlips.size();i++){
                        packingSlips.get(i).setSelected(true);
                    }
                    lvPackingSlip.setAdapter(packingSlipConfirmAdapter);
                    cbSelectAll.setBackgroundResource(R.drawable.checked);
                    packingSlipSelected();
                    rlShowCheck.setVisibility(View.VISIBLE);
                }else{
                    for (int i=0;i<packingSlips.size();i++){
                        packingSlips.get(i).setSelected(false);
                    }
                    packingSlipSelected();
                    lvPackingSlip.setAdapter(packingSlipConfirmAdapter);
                    rlShowCheck.setVisibility(View.VISIBLE);
                    // rlShowCheck.setVisibility(View.GONE);
                }

            }
        });
    }

    /**
     * check status packingslip,
     * checked if confirmed
     */
    private void checkState(){
        int totalSelect =0;

        for(int i=0; i<packingSlips.size(); i++){
            if(packingSlips.get(i).getIsConfirm()==1){
                packingSlips.get(i).setSelected(true);
                totalSelect++;
            }
        }

        // check is all invoice checked ?
        if(totalSelect==packingSlips.size()){

            cbSelectAll.setChecked(true);
            lvPackingSlip.setAdapter(packingSlipConfirmAdapter);
            cbSelectAll.setBackgroundResource(R.drawable.checked);
            packingSlipSelected();
        }

        tvJumlahPackingSlip.setText(totalSelect+"");
    }

    /**
     * function to set footer when packingslip checked
     */
    public static void packingSlipSelected() {
        int totalSelect =0;
        for (int i=0;i<packingSlips.size();i++){
           if(packingSlips.get(i).getSelected()){
               totalSelect++;
           }
        }
        if (totalSelect > 0)
            rlShowCheck.setVisibility(View.VISIBLE);
        else
            rlShowCheck.setVisibility(View.VISIBLE);
            // rlShowCheck.setVisibility(View.GONE);

        tvJumlahPackingSlip.setText(totalSelect+"");
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
