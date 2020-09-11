package id.co.beton.saleslogistic_trackingsystem;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Class Change Password
 */
public class ChangePsswordActivity extends AppCompatActivity {

    private EditText oldPassword,newPassword,confirmPassword;
    private Button btnRresetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pssword);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Back");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        oldPassword = (EditText) findViewById(R.id.et_previous_password);
        newPassword = (EditText) findViewById(R.id.et_new_password);
        confirmPassword = (EditText) findViewById(R.id.et_new_password_confirm);
        btnRresetPassword = (Button) findViewById(R.id.btn_reset_password);

        btnRresetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitRestPassword();
            }
        });
    }

    private void submitRestPassword(){

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
