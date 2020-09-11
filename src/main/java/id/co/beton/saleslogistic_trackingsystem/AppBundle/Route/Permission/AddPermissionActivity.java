package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.Permission;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

/**
 * Class AddPermissionActivity
 * show list all available activity
 */
public class AddPermissionActivity extends AppCompatActivity {
    private TextView tvChangeRoute,tvBreakTime,tvVisitTime;
    private ImageView btnChangeRoute,btnBreakTIme,btnVisitTime;
    private LinearLayout llRencanaKunjungan,llIstirahat,llWaktuKunjungan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_permission);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        llRencanaKunjungan = (LinearLayout) findViewById(R.id.ll_rencana_kunjungan);
        llRencanaKunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoChangeRoute();
            }
        });
        if(PlanUtil.getInstance(this).isUsingRoute()){
            llRencanaKunjungan.setVisibility(View.VISIBLE);
        } else {
            llRencanaKunjungan.setVisibility(View.GONE);
        }

        llIstirahat = (LinearLayout) findViewById(R.id.ll_istirahat);
        llIstirahat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoExtendTime("Break");
            }
        });

        llWaktuKunjungan = (LinearLayout)findViewById(R.id.ll_waktu_kunjungan);
        llWaktuKunjungan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoExtendTime("Visit");
            }
        });

        UserUtil userUtil = UserUtil.getInstance(this);

        tvVisitTime =(TextView) findViewById(R.id.tv_visit_time);
        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER))
        {
            tvVisitTime.setText("Perpanjangan waktu unloading");
        }
    }

    private void gotoChangeRoute(){
        Intent intent =new Intent(this, ChangeRouteActivity.class);
        startActivity(intent);
    }

    private void gotoExtendTime(String jenisExtendTime){
        Intent intent =new Intent(this,ExtendTimeActivity.class);
        intent.putExtra("jenisExtendTime",jenisExtendTime);
        startActivity(intent);
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
