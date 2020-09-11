package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Order;

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
import id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.detailrequestorder.ProductAdapter;
import id.co.beton.saleslogistic_trackingsystem.Model.RequestOrderAndSalesOrderData;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

/**
 * Class DetailVisitPlanSalesOrderActivity
 * detail of data sales order
 */
public class DetailVisitPlanSalesOrderActivity extends AppCompatActivity {

    private static final String TAG = DetailVisitPlanSalesOrderActivity.class.getSimpleName();
    private ListView lvOrder;
    private TextView tvTanggal,tvSalesOrderDate,tvSalesOrderNo,tvStatus,tvOrderBy,tvDeliveryAddress,tvTotalSalesOrder;
    private RequestOrderAndSalesOrderData requestOrderAndSalesOrderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_visit_plan_sales_order);
        try{
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle(getIntent().getStringExtra("cust_name"));

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);


            LayoutInflater inflater = LayoutInflater.from(this);
            lvOrder = (ListView)findViewById(R.id.lv_detail_visitplan_sales_order);
            View header = inflater.inflate(R.layout.header_detail_visitplan_sales_order,null);
            tvTanggal = (TextView) header.findViewById(R.id.tv_tanggal);
            tvSalesOrderDate = (TextView) header.findViewById(R.id.tv_sales_order_date);
            tvSalesOrderNo = (TextView) header.findViewById(R.id.tv_sales_order_no);
            tvStatus = (TextView) header.findViewById(R.id.tv_status);
            tvOrderBy = (TextView) header.findViewById(R.id.tv_order_by);
            tvDeliveryAddress = (TextView) header.findViewById(R.id.tv_delivery_address);
            tvTotalSalesOrder = (TextView) header.findViewById(R.id.tv_total_sales_order);

            lvOrder.addHeaderView(header);
            String jsonSO =  getIntent().getStringExtra("list_request_sales_order");
            if(jsonSO!=null){
                Gson gson = new Gson();
                requestOrderAndSalesOrderData = new RequestOrderAndSalesOrderData();
                requestOrderAndSalesOrderData = gson.fromJson(jsonSO, RequestOrderAndSalesOrderData.class);

                ConversionDate conversionDate = ConversionDate.getInstance();
                tvTanggal.setText(conversionDate.fullFormatDate(requestOrderAndSalesOrderData.getCreateDate()));
                tvSalesOrderNo.setText(requestOrderAndSalesOrderData.getCode());
                tvStatus.setText(requestOrderAndSalesOrderData.getStatus());
                tvOrderBy.setText(requestOrderAndSalesOrderData.getUser().getEmployeeName());
                tvDeliveryAddress.setText(requestOrderAndSalesOrderData.getDeliveryAddress());
                tvSalesOrderDate.setText(conversionDate.fullFormatDate(requestOrderAndSalesOrderData.getCreateDate()));
                tvTotalSalesOrder.setText("Rp "+Currency.priceWithoutDecimal(requestOrderAndSalesOrderData.getInvoiceAmount()));

                if(requestOrderAndSalesOrderData.getProduct()!=null){
                    ProductAdapter ProductAdapter=new ProductAdapter(this,1,requestOrderAndSalesOrderData.getProduct());
                    lvOrder.setAdapter(ProductAdapter);
                }else{
                    lvOrder.setAdapter(null);
                }
            }
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
