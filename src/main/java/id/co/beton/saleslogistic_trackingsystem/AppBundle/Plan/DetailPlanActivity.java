package id.co.beton.saleslogistic_trackingsystem.AppBundle.Plan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Adapter.plan.DetailPlanAdapter;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.MainActivity;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.Plan;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

public class DetailPlanActivity extends AppCompatActivity {
    private String TAG = DetailPlanActivity.class.getSimpleName();
    private Context context;
    private UserUtil userUtil;
    private PlanUtil planUtil;
    private Plan plan;
    private List<Destination> destinationList = new ArrayList<>();
    private DetailPlanAdapter detailPlanAdapter;

    private ListView lvDestination;
    private TextView tvPlanId, tvAsset, tvDate, tvCreateDate, tvTotalVisit, tvTotalPackingSlip;
    private Button btnSelectPlan;
    private String planId, asset, datePlan, createDate, totalCustomer, totalPackingSlip;
    private String dataDetailPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        planId = getIntent().getStringExtra("plan_id");
        dataDetailPlan =  getIntent().getStringExtra("detail_plan");

        setTitle("Plan ID: " + planId);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context  = this;
        userUtil = UserUtil.getInstance(context);
        planUtil = PlanUtil.getInstance(context);

        tvPlanId = (TextView) findViewById(R.id.tv_id_plan);
        tvDate = (TextView) findViewById(R.id.tv_date_plan);
        tvAsset = (TextView) findViewById(R.id.tv_asset);
        tvCreateDate = (TextView) findViewById(R.id.tv_created_date);
        tvTotalVisit = (TextView) findViewById(R.id.tv_total_visit);
        tvTotalPackingSlip = (TextView) findViewById(R.id.tv_total_packingslip);
        lvDestination = (ListView) findViewById(R.id.lv_detail_destination);
        btnSelectPlan = (Button) findViewById(R.id.btn_select_plan);
        btnSelectPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogConfirmation();
            }
        });

        LayoutInflater inflater = LayoutInflater.from(this);
        View header = inflater.inflate(R.layout.header_list_visit,null);
        lvDestination.addHeaderView(header);
        loadData();
    }

    private void loadData(){
        if(dataDetailPlan!=null){
            Gson gson = new Gson();
            plan = new Plan();
            plan =gson.fromJson(dataDetailPlan, Plan.class);

            asset = "-";
            datePlan = "-";
            createDate = "-";
            totalCustomer = "0";
            totalPackingSlip = "0";

            if(plan.getAssetId()!=null){
                asset = plan.getAsset().getName() +", " + plan.getAsset().getCode();
            }
            if(plan.getCreateDate()!=null){
                createDate = plan.getCreateDate();
            }
            if(plan.getDate()!=null){
                datePlan = ConversionDate.getInstance().fullFormatDate(plan.getDate());
            }
            if(plan.getTotalCustomer()!=null){
                totalCustomer = plan.getTotalCustomer().toString();
            }
            if(plan.getTotalPackingSlip()!=null){
                totalPackingSlip = plan.getTotalPackingSlip().toString();
            }

            tvPlanId.setText(planId);
            tvAsset.setText(asset);
            tvDate.setText(datePlan);
            tvCreateDate.setText(createDate);
            tvTotalVisit.setText(totalCustomer);
            tvTotalPackingSlip.setText(totalPackingSlip);

            if(plan.getDestination().size()>0){
                destinationList.addAll(plan.getDestination());
                detailPlanAdapter = new DetailPlanAdapter(context, 1, destinationList);
                lvDestination.setAdapter(detailPlanAdapter);
            }
        }
    }

    private void showDialogConfirmation(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Konfirmasi");
        String message = "Apakah Anda yakin memilih delivery plan ini?";
        alertDialogBuilder
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toasty.info(context, "Delivery Plan berhasil dipilih", Toasty.LENGTH_SHORT).show();
                        planUtil.setPlanId(Integer.valueOf(planId));
                        Intent intent = new Intent(context, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();

                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
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
