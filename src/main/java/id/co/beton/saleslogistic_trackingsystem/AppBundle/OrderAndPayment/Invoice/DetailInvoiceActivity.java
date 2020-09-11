package id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.Invoice;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.detailinvoice.OrderAdapter;
import id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.detailinvoice.PaymentAdapter;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;

import id.co.beton.saleslogistic_trackingsystem.Model.Payment;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.util.ArrayList;

/**
 * Class DetailInvoiceActivity
 * detail data invoice
 */
public class DetailInvoiceActivity extends AppCompatActivity {

    private static final String TAG = "DetailInvoiceActivity";
    private TextView tvNoInvoice, tvDueDate, tvTotalInvoice, tvCustomerName;
    private ArrayList<Payment> payments;

    private ListView lvPayment;
    private ListView lvOrder;
    private Invoice invoice;
    private Destination destination;
    private String mTitle;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_invoice);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try{
            Bundle inBundle = getIntent().getExtras();
            gson = new Gson();
            invoice = gson.fromJson(getIntent().getStringExtra("invoice"), Invoice.class);
            mTitle = getIntent().getStringExtra("destination");
//            destination = gson.fromJson(getIntent().getStringExtra("destination"), Destination.class);

            setTitle(mTitle);
//            setTitle(destination.getCustomerName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            tvNoInvoice = (TextView) findViewById(R.id.tv_no_invoice);
            tvTotalInvoice = (TextView) findViewById(R.id.tv_total_harga_invoice);

            tvNoInvoice.setText(invoice.getIdInvoice());
            tvTotalInvoice.setText("Rp. "+ Currency.priceWithDecimal(invoice.getInvoiceAmount()));

            //set dummy data
            payments = new ArrayList<>();

            PaymentAdapter paymentAdapter = new PaymentAdapter(this,1,payments);

            lvPayment = (ListView) findViewById(R.id.lv_payment);
            lvPayment.setAdapter(paymentAdapter);

            OrderAdapter orderAdapter = new OrderAdapter(this,1,invoice.getProduct());
            lvOrder = (ListView) findViewById(R.id.lv_order);

            lvOrder.setAdapter(orderAdapter);
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
}
