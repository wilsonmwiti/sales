package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Invoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.invoices.InvoicesDetailPaymentAdapter;
import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Class DetailInvoicePaymentActivity
 * detail of data invoice
 */
public class DetailInvoicePaymentActivity extends AppCompatActivity {
    private List<Invoice> invoices;
    private ListView lvInvoice;
    private Context context;
    private Button btnOk;
    private static final String TAG = DetailInvoicePaymentActivity.class.getSimpleName();
    String customerName,customerCode;
    InvoicesDetailPaymentAdapter invoiceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_invoice_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context =this;

        Gson gson = new Gson();
        customerCode = getIntent().getStringExtra("cust_code");
        customerName = getIntent().getStringExtra("cust_name");
        invoices = new ArrayList<>();

        invoices = gson.fromJson(getIntent().getStringExtra("selected_invoices"),new TypeToken<List<Invoice>>(){}.getType());

        setTitle(customerName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        LayoutInflater inflater = LayoutInflater.from(this);
        lvInvoice = (ListView)findViewById(R.id.lv_payment);
        View header = inflater.inflate(R.layout.header_activity_detail_invoice_payment,null);
        lvInvoice.addHeaderView(header);
        invoiceAdapter=new InvoicesDetailPaymentAdapter(context,1,invoices);
        lvInvoice.setAdapter(invoiceAdapter);

        int total=0;
        for (int i=0;i<invoices.size();i++){
            total+= invoices.get(i).getInvoiceAmount();
        }


        TextView tvDateVP = (TextView) header.findViewById(R.id.tv_tanggal);
        TextView tvPaymentNo  = (TextView) header.findViewById(R.id.tv_payment_no);
        TextView tvNumberOfInvoice  = (TextView) header.findViewById(R.id.tv_number_of_invoice);
        TextView tvTotalInvoice  = (TextView) header.findViewById(R.id.tv_total_invoice);
        EditText etTotalPayment  = (EditText) header.findViewById(R.id.et_total_payment);

        tvDateVP.setText(ConversionDate.getInstance().today());
        tvPaymentNo.setText("-");
        tvNumberOfInvoice.setText(String.valueOf(invoices.size()));
        tvTotalInvoice.setText("Rp."+ Currency.priceWithoutDecimal(total)+",-");

        etTotalPayment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(editable.length()>0){
                    setTotalPayment(Integer.valueOf(editable.toString()));
                }
            }
        });


        btnOk = (Button) findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Gson gson = new Gson();
                Type type = new TypeToken<List<Invoice>>() {}.getType();
                String json = gson.toJson(invoices, type);
                intent.putExtra("selected_invoices",json);
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });
    }

    private void setTotalPayment(int total){
        int tempTotal = total;
        for (int i=0; i<invoices.size();i++){
            if(tempTotal >= invoices.get(i).getInvoiceAmount()){
                invoices.get(i).setPaymentAmount(invoices.get(i).getInvoiceAmount());
                tempTotal -= invoices.get(i).getInvoiceAmount();
            }else{
                invoices.get(i).setPaymentAmount(tempTotal);
                tempTotal=0;
            }
        }
        invoiceAdapter.notifyDataSetChanged();
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
