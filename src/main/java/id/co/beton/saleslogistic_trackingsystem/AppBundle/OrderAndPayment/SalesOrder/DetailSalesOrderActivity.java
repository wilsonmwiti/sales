package id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.SalesOrder;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.detailsalesorder.ProductAdapter;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.Product;
import id.co.beton.saleslogistic_trackingsystem.Model.SalesOrder;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.util.ArrayList;

/**
 * Class DetailSalesOrderActivity
 * detail data sales order
 */
public class DetailSalesOrderActivity extends AppCompatActivity {
    private TextView text_view_for_tab_selection;
    private OnFragmentInteractionListener mListener;
    private ArrayList<Product> products;
    private static final String TAG = DetailSalesOrderActivity.class.getSimpleName();
    private ListView lvOrder;
    private SalesOrder salesOrder;
    private Gson gson;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_sales_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        gson = new Gson();
        salesOrder = gson.fromJson(getIntent().getStringExtra("sales_order"), SalesOrder.class);

        setTitle(salesOrder.getCustomer().getNama());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        LayoutInflater inflater = LayoutInflater.from(this);
        lvOrder = (ListView)findViewById(R.id.lv_detail_sales_order);
        View header = inflater.inflate(R.layout.header_visit_plan_detail_sales_order,null);
        lvOrder.addHeaderView(header);
//
        ProductAdapter ProductAdapter=new ProductAdapter(this,1,salesOrder.getProduct());
        lvOrder.setAdapter(ProductAdapter);

        initValue();
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


    public void initValue(){
        try{
            TextView tvDateVP = (TextView)findViewById(R.id.tv_tanggal);
            TextView tvNoRO  = (TextView) findViewById(R.id.tv_ro_order);
            TextView tvStatusOrder  = (TextView) findViewById(R.id.tv_status);
            TextView tvSODate  = (TextView)findViewById(R.id.tv_sales_order_date);
            TextView tvSONo  = (TextView)findViewById(R.id.tv_sales_order_no);
            TextView tvTotal  = (TextView)findViewById(R.id.tv_total_sales_order);
            TextView tvOrderBy  = (TextView)findViewById(R.id.tv_order_by);
            TextView tvDeliveryAddress  = (TextView)findViewById(R.id.tv_delivery_address);

            if (salesOrder.getInvoiceDate()!=null){
                String invoiceDate = ConversionDate.getInstance().fullFormatDate(salesOrder.getInvoiceDate());
                tvDateVP.setText(invoiceDate);

            }

            tvNoRO.setText(salesOrder.getCode());
            tvStatusOrder.setText(salesOrder.getStatus());
            tvSODate.setText("-");
            tvSONo.setText("-");
            tvTotal.setText("Rp. "+ Currency.priceWithDecimal(salesOrder.getInvoiceAmount()));
            tvOrderBy.setText(salesOrder.getUser().getEmployeeName());
            tvDeliveryAddress.setText(salesOrder.getCustomer().getAlamat());
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
