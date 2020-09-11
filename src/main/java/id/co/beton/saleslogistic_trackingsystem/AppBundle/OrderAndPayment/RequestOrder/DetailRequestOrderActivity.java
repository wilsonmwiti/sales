package id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.RequestOrder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.detailrequestorder.ProductAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.RequestOrder;
import id.co.beton.saleslogistic_trackingsystem.Model.RequestOrderImage;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.ImagePicker;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class DetailRequestOrderActivity
 * detail data request order
 */
public class DetailRequestOrderActivity extends AppCompatActivity {

    private static final String TAG = DetailRequestOrderActivity.class.getSimpleName();
    private ListView lvOrder;
    private LinearLayout llRequestImage;
    private TextView tvTanggal,tvRoOrder,tvStatus,tvOrderBy,tvDeliveryAddress, tvDetailPengiriman;
    private ImageView imgSetSpecial, imgRequestOrder;
    private Context context;
    private Gson gson;
    private RequestOrder requestOrder;
    private DialogCustom dialogCustom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_request_order);
        context = this;
        dialogCustom = new DialogCustom(context);
        try{
            gson = new Gson();
            requestOrder = gson.fromJson(getIntent().getStringExtra("request_order"), RequestOrder.class);

            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            setTitle(requestOrder.getCustomer().getNama());

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            LayoutInflater inflater = LayoutInflater.from(this);
            lvOrder = (ListView)findViewById(R.id.lv_detail_request_order);
            View header = inflater.inflate(R.layout.header_visit_plan_detail_request,null);
            lvOrder.addHeaderView(header);

            llRequestImage = header.findViewById(R.id.ll_request_image);
            tvTanggal = (TextView) header.findViewById(R.id.tv_tanggal);
            tvRoOrder = (TextView) header.findViewById(R.id.tv_ro_order);
            tvStatus = (TextView) header.findViewById(R.id.tv_status);
            tvOrderBy = (TextView) header.findViewById(R.id.tv_order_by);
            tvDeliveryAddress = (TextView) header.findViewById(R.id.tv_delivery_address);
            tvDetailPengiriman = (TextView) header.findViewById(R.id.tv_detail_pengiriman);

            imgSetSpecial = (ImageView) header.findViewById(R.id.img_set_special);
            imgRequestOrder = (ImageView) header.findViewById(R.id.img_request_order);

            if(requestOrder.getProduct()!=null){
                ProductAdapter ProductAdapter=new ProductAdapter(this,1,requestOrder.getProduct());
                lvOrder.setAdapter(ProductAdapter);
            }else{
                lvOrder.setAdapter(null);
            }

            loadDataImage();

            ConversionDate conversionDate = ConversionDate.getInstance();
            tvTanggal.setText(conversionDate.fullFormatDate(requestOrder.getCreateDate()));
            tvRoOrder.setText(requestOrder.getCode());
            tvStatus.setText(requestOrder.getType());
            tvOrderBy.setText(requestOrder.getUser().getEmployeeName());
            tvDeliveryAddress.setText(requestOrder.getDeliveryAddress());
            tvDetailPengiriman.setText(requestOrder.getNotes());

            if(requestOrder.getIsSpecialOrder()==1){
                imgSetSpecial.setImageResource(R.drawable.bg_set_special_green);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    /**
     * load data image request order from server
     */
    private void loadDataImage(){
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(context);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.salesRequestImage("JWT " + userUtil.getJWTTOken(), requestOrder.getId());

        /*Log the URL called*/
        if (Constants.DEBUG) {
            Log.i("URL Called", call.request().url() + "");
        }
        dialogCustom.show();
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body() != null && response.body().getError() == 0) { //if sukses
                    try{
                        Gson gson = new Gson();
                        final RequestOrderImage requestOrderImage = gson.fromJson(response.body().getData().toString(), RequestOrderImage.class);
                        if(requestOrderImage.getRequestOrderImageData().get(0).getFile()==null){
                            llRequestImage.setVisibility(View.GONE);
                        } else {
                            llRequestImage.setVisibility(View.VISIBLE);
                            Bitmap fullImage = convertBase64ToImage(requestOrderImage.getRequestOrderImageData().get(0).getFile());
                            updateLayout(fullImage);
                        }

                        dialogCustom.hidden();
                    } catch(Exception e){
                        e.printStackTrace();
                        dialogCustom.hidden();
                    }
                } else {
                    try{
                        dialogCustom.hidden();
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        dialogCustom.hidden();
                        Toasty.error(context, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                dialogCustom.hidden();
            }
        });
    }

    /**
     * set image to layout and set popup image when onclick thumbnail
     * @param bitmap
     */
    private void updateLayout(final Bitmap bitmap){
        imgRequestOrder.setImageBitmap(bitmap);
        imgRequestOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.showImage(context, bitmap);
                // showImage(bitmap);
            }
        });
    }

    /**
     * convert string base64 to image
     * @param base64Encoded
     * @return Bitmap
     */
    private Bitmap convertBase64ToImage(String base64Encoded){
        final byte[] decodedBytes = Base64.decode(base64Encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
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
