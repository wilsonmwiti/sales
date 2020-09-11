package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.invoices.InvoiceConfirmAdapter;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.MainActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class InvoicesActivity
 * to confirm invoice
 */
public class InvoicesActivity extends AppCompatActivity {

    private static final String TAG = InvoicesActivity.class.getSimpleName();
    private ListView lvInvoice;
    private CheckBox cbSelectAll;
    private static RelativeLayout rlShowCheck;
    private static TextView tvTotalInvoice;
    private static TextView tvNumberOfInvoice;
    private InvoiceConfirmAdapter invoiceConfirmAdapter;
    private Context context;
    private static List<Invoice> invoiceList;
    private static  TextView tvTanggalFaktur;
    private TextView tvNumberOfInvoiceHeader;
    private TextView tvTotalInvoiceHeader;
    private static Currency currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view;
        setContentView(R.layout.activity_invoices);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Back");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.context = this;
        ConversionDate conversionDate = ConversionDate.getInstance();
        currency  = Currency.getInstance();
        String jsonInvoices =  getIntent().getStringExtra("list_invoices");
        if(jsonInvoices!=null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<Invoice>>(){}.getType();
            invoiceList = new ArrayList<>();
            invoiceList = gson.fromJson(jsonInvoices, type);

            LayoutInflater inflater = LayoutInflater.from(this);
            lvInvoice = (ListView)findViewById(R.id.lv_invoice);
            tvNumberOfInvoice = (TextView)findViewById(R.id.tv_number_of_invoice);
            tvTotalInvoice = (TextView)findViewById(R.id.tv_total_invoice);

            invoiceConfirmAdapter =new InvoiceConfirmAdapter(this,1, invoiceList);
            lvInvoice.setAdapter(invoiceConfirmAdapter);

            View header = inflater.inflate(R.layout.header_activity_invoice,null);
            lvInvoice.addHeaderView(header);

            tvTanggalFaktur = (TextView) header.findViewById(R.id.tv_tanggal);
            tvTanggalFaktur.setText(conversionDate.today());

            tvNumberOfInvoiceHeader = (TextView) header.findViewById(R.id.tv_number_of_invoices_header);
            tvTotalInvoiceHeader  = (TextView) header.findViewById(R.id.tv_total_invoice_header);

            tvNumberOfInvoiceHeader.setText(invoiceList.size()+"");
            int sum=0;
            for (int i=0;i<invoiceList.size();i++){
                sum+=invoiceList.get(i).getInvoiceAmount();
            }

            tvTotalInvoiceHeader.setText("Rp "+currency.priceWithoutDecimal(sum));

            rlShowCheck = (RelativeLayout) findViewById(R.id.rl_show_check);

            rlShowCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //get selected invoice and sent to server
                    confirmInvoice();
                }
            });

            checkAll();
            checkState();
        }
    }

    /**
     * send data confirm invoice to server
     */
    private void confirmInvoice(){
        final UserUtil userUtil = UserUtil.getInstance(context);
        PlanUtil planUtil = PlanUtil.getInstance(context);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);
        JsonArray jsonElements = new JsonArray();
        for (int i=0;i<invoiceList.size();i++){
            if (invoiceList.get(i).isSelected()){
                jsonElements.add(invoiceList.get(i).getIdInvoice());
            }
        }

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("invoice_id",jsonElements);

        Call<ResponseArrayObject> call = service.confirmInvoice("JWT "+userUtil.getJWTTOken(), planUtil.getPlanId(),jsonObject);
        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                if(response.body()!=null && response.body().getError()==0){
                    userUtil.setBooleanProperty(Constants.STATUS_CONFIRM_INVOICE,true);
                    Toasty.info(context,response.body().getMessage()).show();
                    Intent intent = new Intent(context,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                Toasty.error(context, "Gagal mengirim data").show();
            }
        });
    }

    /**
     * check all invoice if checkbox 'select all' selected
     */
    public void checkAll(){
        cbSelectAll = (CheckBox)findViewById(R.id.cb_select_all);
        cbSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((CheckBox)v).isChecked())
                {
                    for (int i=0;i<invoiceList.size();i++){
                        invoiceList.get(i).setSelected(true);
                    }
                    //   invoiceConfirmAdapter.notifyDataSetChanged();
                    lvInvoice.setAdapter(invoiceConfirmAdapter);
                    cbSelectAll.setBackgroundResource(R.drawable.checked);
                    InvoicesActivity.InvoiceSelected();
                    rlShowCheck.setVisibility(View.VISIBLE);

                }
                else
                {
                    for (int i=0;i<invoiceList.size();i++){
                        invoiceList.get(i).setSelected(false);
                    }
                    // invoiceConfirmAdapter.notifyDataSetChanged();
                    lvInvoice.setAdapter(invoiceConfirmAdapter);
                    InvoicesActivity.InvoiceSelected();
                    rlShowCheck.setVisibility(View.VISIBLE);
                    // rlShowCheck.setVisibility(View.GONE);
                }
            }
        });
    }

    /**
     * check status invoice,
     * checked if confirmed
     */
    private void checkState(){
        int tmpTotalInvoice = 0;
        int tmpNumberInvoice= 0;

        for(int i=0; i<invoiceList.size(); i++){
            if(invoiceList.get(i).getIsConfirm()==1){
                invoiceList.get(i).setSelected(true);
                tmpTotalInvoice = tmpTotalInvoice + invoiceList.get(i).getInvoiceAmount();
                tmpNumberInvoice++;
            }
        }

        // check is all invoice checked ?
        if(tmpNumberInvoice==invoiceList.size()){

            cbSelectAll.setChecked(true);
            lvInvoice.setAdapter(invoiceConfirmAdapter);
            cbSelectAll.setBackgroundResource(R.drawable.checked);
            InvoicesActivity.InvoiceSelected();
        }

        tvTotalInvoice.setText("Rp "+currency.priceWithoutDecimal(tmpTotalInvoice));
        tvNumberOfInvoice.setText(""+tmpNumberInvoice);
    }

    /**
     * function to set footer when invoice checked
     */
    public static void InvoiceSelected()
    {
        int tmpTotalInvoice = 0;
        int tmpNumberInvoice= 0;
        for (int i=0;i<invoiceList.size();i++){
            if (invoiceList.get(i).isSelected()){
                tmpTotalInvoice = tmpTotalInvoice + invoiceList.get(i).getInvoiceAmount();
                tmpNumberInvoice++;
            }
        }

        if (tmpNumberInvoice > 0)
            rlShowCheck.setVisibility(View.VISIBLE);
        else
            rlShowCheck.setVisibility(View.VISIBLE);
            // rlShowCheck.setVisibility(View.GONE);

        tvTotalInvoice.setText("Rp "+currency.priceWithoutDecimal(tmpTotalInvoice));
        tvNumberOfInvoice.setText(""+tmpNumberInvoice);
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
