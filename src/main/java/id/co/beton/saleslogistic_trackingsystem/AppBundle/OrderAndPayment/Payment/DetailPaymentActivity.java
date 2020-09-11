package id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.Payment;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.detailpayment.PaymentAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
import id.co.beton.saleslogistic_trackingsystem.Model.Payment;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Class DetailPaymentActivity
 * detail data payment
 */
public class DetailPaymentActivity extends AppCompatActivity {
    private TextView tvDateVP;
    private TextView tvPaymentNo;
    private TextView tvReceiptNo;
    private TextView tvStatus;
    private TextView tvNoOfInvoice;
    private TextView tvTotalInvoice;
    private TextView tvTotalPayment;
    private TextView tvInvoiceBalance;
    private OnFragmentInteractionListener mListener;
    private List<Invoice> detailInvoices = new ArrayList<>();
    private ListView lvOrder;
    private Payment detailPayment;
    Context context;

    private static final String TAG = DetailPaymentActivity.class.getSimpleName();
    Button btnPrint,btnCancel;
    Gson gson = new Gson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_visit_plan_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context =this;
        try{
            Bundle inBundle = getIntent().getExtras();
            gson = new Gson();

            detailPayment = gson.fromJson(getIntent().getStringExtra("detailPayment"), Payment.class);
            setTitle(detailPayment.getCustomerCode());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


            Log.d("Response_detail_payment",getIntent().getStringExtra("detailPayment"));

            LayoutInflater inflater = LayoutInflater.from(this);
            lvOrder = (ListView)findViewById(R.id.lv_detail_payment);
            View header = inflater.inflate(R.layout.header_route_payement,null);
            lvOrder.addHeaderView(header);
            /*View footer = inflater.inflate(R.layout.footer_route_payment,null);
            lvOrder.addFooterView(footer);*/

            initComponent();
            initValue();
            detailInvoices = detailPayment.getInvoice();

            PaymentAdapter paymentAdapter=new PaymentAdapter(this,1,detailInvoices);
            lvOrder.setAdapter(paymentAdapter);
        }catch (Exception e){
            e.printStackTrace();
        }

        //loadData();
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


    public void initComponent(){

        try{
            tvDateVP = (TextView)findViewById(R.id.tv_tanggal);
            tvPaymentNo  = (TextView) findViewById(R.id.tv_payment_no);
            tvReceiptNo  = (TextView) findViewById(R.id.tv_receive_no);
            tvStatus  = (TextView)findViewById(R.id.tv_status);
            tvNoOfInvoice  = (TextView)findViewById(R.id.tv_no_invoice);
            tvTotalInvoice  = (TextView)findViewById(R.id.tv_total_invoice);
            tvTotalPayment  = (TextView)findViewById(R.id.tv_total_payment);
            tvInvoiceBalance  = (TextView)findViewById(R.id.tv_invoice_balance);

            //footer
            /*tvPaymentMethod  = (TextView)findViewById(R.id.tv_payment_method);
            tvBank  = (TextView)findViewById(R.id.tv_bank);
            tvAccountNo  = (TextView)findViewById(R.id.tv_account_no);
            tvAccountName  = (TextView)findViewById(R.id.tv_account_name);
            tvTransfeTo  = (TextView)findViewById(R.id.tv_transfer_to);
            imgBuktiPembayaran = (ImageView)findViewById(R.id.img_bukti_pembayaran);*/
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void initValue(){
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(detailPayment.getPaymentDate().toString());
            String hari = new SimpleDateFormat("EE, yyyy-MM-dd", Locale.ENGLISH).format(date);
            tvDateVP.setText(hari);
        } catch (ParseException e) {
            if(Constants.DEBUG){
                Log.wtf(TAG, e.getMessage());
                e.printStackTrace();
            }
        }

        try{
            tvStatus.setText(detailPayment.getPaymentStatus());
            tvNoOfInvoice.setText(""+detailPayment.getInvoice().get(0).getInvoiceId());
            tvTotalInvoice.setText("Rp. "+Currency.priceWithDecimal(detailPayment.getInvoiceAmount()));
            tvTotalPayment.setText("Rp. "+Currency.priceWithDecimal(detailPayment.getPaymentAmount()));
            tvInvoiceBalance.setText("Rp. "+Currency.priceWithDecimal(detailPayment.getInvoiceAmount()-detailPayment.getPaymentAmount()));
            /*tvPaymentMethod.setText(detailPayment.getPaymentMethod());
            tvBank.setText(detailPayment.getPaymentInfo().getBankName());
            tvAccountNo.setText(detailPayment.getPaymentInfo().getAccountNo());
            tvAccountName.setText(detailPayment.getPaymentInfo().getAccountName());
            tvTransfeTo.setText(detailPayment.getPaymentInfo().getTransferTo());*/
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
