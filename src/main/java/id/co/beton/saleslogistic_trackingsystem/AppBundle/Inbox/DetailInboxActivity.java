package id.co.beton.saleslogistic_trackingsystem.AppBundle.Inbox;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.gson.Gson;
import id.co.beton.saleslogistic_trackingsystem.Model.DataInbox;
import id.co.beton.saleslogistic_trackingsystem.R;

/**
 * Class DetailInboxActivity
 * detail data inbox
 */
public class DetailInboxActivity extends AppCompatActivity {

    private static final String TAG = "DetailInboxActivity";
    private TextView tvTitle, tvDate, tvMessage;
    private String title, date, message, category;
    private Gson gson;
    private DataInbox inbox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_inbox);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        gson = new Gson();
        inbox = gson.fromJson(getIntent().getStringExtra("inbox"), DataInbox.class);

        title       = inbox.getTitle().toString();
        date        = inbox.getDate().toString();
        category    = inbox.getCategory();
        message     = inbox.getMessage().toString();

        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvMessage = (TextView) findViewById(R.id.tv_message);

        tvTitle.setText(title);
        tvDate.setText(date);
        tvMessage.setText(message);

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

