package id.co.beton.saleslogistic_trackingsystem;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Developer.TapNfcSimulationService;

/**
 * Class TapNfcActivity extend to NfcActivity
 * called when user Tap Start or End at Branch
 *
 */
public class TapNfcActivity extends NfcActivity {
    private String TAG = TapNfcActivity.class.getSimpleName();
    private TextView tvTitleNfc;
    private Intent intent;
    private String myVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tap_nfc);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Back");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = this;
        activity = TapNfcActivity.this;

        // load version name
        myVersionName = userUtil.getVersionApps(context);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        mTitle.setText("v" + myVersionName);

        tvTitleNfc = (TextView) findViewById(R.id.tv_title_nfc);

        enableGps();

        getLastKnownLocation();

        type = getIntent().getExtras().getInt("type_tap_nfc"); //3 is start 4 is end
        if (Constants.DEV_MODE) {
            intent = new Intent(TapNfcActivity.this, TapNfcSimulationService.class);
            intent.putExtra("type", type);
            startService(intent);
        }
        Log.e("TapNfcActivity", String.valueOf(type));
        if (type == 4) {
            tvTitleNfc.setText("Silahkan tap NFC sebagai tanda Daily Visit Berakhir!");
        }
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (nfcAdapter == null) {
            Toasty.info(this, "This device does'n support NFC", Toast.LENGTH_SHORT).show();
        } else {
            if (!nfcAdapter.isEnabled()) {
                Toasty.info(this, "Silahkan aktifkan fitur NFC", Toast.LENGTH_SHORT).show();
                showNFCSetting();
            }
        }
        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, this.getClass())
                        .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Constants.DEV_MODE) {
            stopService(intent);
        }
    }
}
