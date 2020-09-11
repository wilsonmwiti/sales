package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Order;

import android.content.Context;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.detailrequestorder.ProductAdapter;
import id.co.beton.saleslogistic_trackingsystem.Model.RequestOrderAndSalesOrderData;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;

/**
 * Class DetailVisitPlanRequestOrderActivity
 * detail of data request order
 */
public class DetailVisitPlanRequestOrderActivity extends AppCompatActivity {

    private static final String TAG = DetailVisitPlanRequestOrderActivity.class.getSimpleName();
    private ListView lvOrder;
    private TextView tvTanggal,tvRoOrder,tvStatus,tvOrderBy,tvDeliveryAddress, tvDetailPengiriman;
    private ImageView imgSetSpecial;
    private Context context;
    private Gson gson;
    private RequestOrderAndSalesOrderData requestOrderAndSalesOrderData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request_order);

        try{
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            setTitle(getIntent().getStringExtra("cust_name"));
            context = this;

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            String jsonSO =  getIntent().getStringExtra("request_order");
            Gson gson = new Gson();
            requestOrderAndSalesOrderData = new RequestOrderAndSalesOrderData();
            requestOrderAndSalesOrderData = gson.fromJson(jsonSO, RequestOrderAndSalesOrderData.class);

            LayoutInflater inflater = LayoutInflater.from(this);
            lvOrder = (ListView)findViewById(R.id.lv_detail_request_order);
            View header = inflater.inflate(R.layout.header_visit_plan_detail_request,null);
            lvOrder.addHeaderView(header);

            tvTanggal = (TextView) header.findViewById(R.id.tv_tanggal);
            tvRoOrder = (TextView) header.findViewById(R.id.tv_ro_order);
            tvStatus = (TextView) header.findViewById(R.id.tv_status);
            tvOrderBy = (TextView) header.findViewById(R.id.tv_order_by);
            tvDeliveryAddress = (TextView) header.findViewById(R.id.tv_delivery_address);
            tvDetailPengiriman = (TextView) header.findViewById(R.id.tv_detail_pengiriman);

            imgSetSpecial = (ImageView) header.findViewById(R.id.img_set_special);
            if(requestOrderAndSalesOrderData.getProduct()!=null){
                ProductAdapter ProductAdapter=new ProductAdapter(this,1,requestOrderAndSalesOrderData.getProduct());
                lvOrder.setAdapter(ProductAdapter);
            }else{
                lvOrder.setAdapter(null);
            }


            ConversionDate conversionDate = ConversionDate.getInstance();
            tvTanggal.setText(conversionDate.fullFormatDate(requestOrderAndSalesOrderData.getCreateDate()));
            tvRoOrder.setText(requestOrderAndSalesOrderData.getCode());
            tvStatus.setText(requestOrderAndSalesOrderData.getType());
            tvOrderBy.setText(requestOrderAndSalesOrderData.getUser().getEmployeeName());
            tvDeliveryAddress.setText(requestOrderAndSalesOrderData.getDeliveryAddress());
            tvDetailPengiriman.setText(requestOrderAndSalesOrderData.getNotes());

            if(requestOrderAndSalesOrderData.getIsSpecialOrder()==1){
                imgSetSpecial.setImageResource(R.drawable.bg_set_special_green);
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
