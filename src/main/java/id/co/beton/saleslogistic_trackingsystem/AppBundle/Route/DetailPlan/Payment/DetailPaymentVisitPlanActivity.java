package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Payment;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import id.co.beton.saleslogistic_trackingsystem.Adapter.route.payment.InvoicePaymentAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.Payment;
import id.co.beton.saleslogistic_trackingsystem.Model.PaymentInfo;
import id.co.beton.saleslogistic_trackingsystem.PrinterActivity;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class DetailPaymentVisitPlanActivity
 * detail of data payment
 */

public class DetailPaymentVisitPlanActivity extends PrinterActivity {
    private String TAG =DetailPaymentVisitPlanActivity.class.getSimpleName();
    private ListView lvOrder;
    Button btnPrint,btnCancel;
    Payment paymentMobileData;
    private int receiptPrinted,receiptReprint,paymentId;
    private String paymentCode;
    UserUtil userUtil;
    private ApiInterface service;
    boolean statusRequestReprint=false;
    String nfcCode;

    @Override
    protected void onCreate(Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_payment_visit_plan);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        customerCode = getIntent().getStringExtra("cust_code");
        customerName = getIntent().getStringExtra("cust_name");
        setTitle(customerName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        context =this;
        nfcCode = UserUtil.getInstance(context).getStringProperty(Constants.NFC_CODE);

        activity = DetailPaymentVisitPlanActivity.this;
        btnPrint = (Button)findViewById(R.id.btn_print_receipt);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nfcCode.trim().contentEquals(customerCode.trim())){
                    rePrint();
                }else{
                    Toasty.info(context,"Pastikan anda telah Checkin untuk customer "+customerName).show();
                }
            }
        });

        btnCancel = (Button)findViewById(R.id.btn_cancel_payment);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nfcCode.trim().contentEquals(customerCode.trim())){
                    final Dialog dialog = new Dialog(DetailPaymentVisitPlanActivity.this);
                    dialog.setContentView(R.layout.custom_allert_cancel);
                    // Custom Android Allert Dialog Title
                    dialog.setTitle("");

                    Button btnTry = (Button) dialog.findViewById(R.id.btn_confirm);
                    // Click cancel to dismiss android custom dialog box
                    btnTry.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancelPayment();
                            dialog.dismiss();
                        }
                    });

                    dialog.show();
                }else{
                    Toasty.info(context,"Pastikan anda telah tap NFC untuk customer "+customerName).show();
                }
            }
        });

        userUtil = UserUtil.getInstance(context);
        service = ApiClient.getInstance(context);

        loadData();
    }

    private void loadData(){
        /*Get user info*/
        String jsonSO =  getIntent().getStringExtra("detail_payment");
        if(jsonSO!=null){
            Gson gson = new Gson();
            paymentMobileData = new Payment();
            paymentMobileData =gson.fromJson(jsonSO,Payment.class);

            LayoutInflater inflater = LayoutInflater.from(this);
            lvOrder = (ListView)findViewById(R.id.lv_detail_payment);
            View header = inflater.inflate(R.layout.header_route_payement,null);
            TextView tvDateVP = (TextView)header.findViewById(R.id.tv_tanggal);
            TextView tvPaymentNo  = (TextView) header.findViewById(R.id.tv_payment_no);
            TextView tvReceiptNo  = (TextView) header.findViewById(R.id.tv_receive_no);
            TextView tvStatus  = (TextView)header.findViewById(R.id.tv_status);
            TextView tvNoOfInvoice  = (TextView)header.findViewById(R.id.tv_no_invoice);
            TextView tvTotalInvoice  = (TextView)header.findViewById(R.id.tv_total_invoice);
            TextView tvTotalPayment  = (TextView)header.findViewById(R.id.tv_total_payment);
            TextView tvInvoiceBalance  = (TextView)header.findViewById(R.id.tv_invoice_balance);

            tvDateVP.setText(ConversionDate.getInstance().today());
            tvPaymentNo.setText(paymentMobileData.getCode());
            tvReceiptNo.setText(paymentMobileData.getReceiptCode());
            tvStatus.setText(paymentMobileData.getPaymentStatus());
//            tvNoOfInvoice.setText(paymentMobileData.getInvoice().size()+"");
            tvNoOfInvoice.setText(""+paymentMobileData.getInvoice().get(0).getInvoiceId());
            int totalInvoice = paymentMobileData.getTotalInvoiceAmount();
            int totalPayment = paymentMobileData.getTotalInvoicePayment();
            tvTotalInvoice.setText("Rp "+ Currency.priceWithoutDecimal(totalInvoice)+",-");
            tvTotalPayment.setText("Rp "+Currency.priceWithoutDecimal(totalPayment)+",-");
            tvInvoiceBalance.setText("Rp "+Currency.priceWithoutDecimal(totalInvoice-totalPayment)+",-");

            lvOrder.addHeaderView(header);
            InvoicePaymentAdapter invoicePaymentAdapter=new InvoicePaymentAdapter(context,1, paymentMobileData.getInvoice());
            lvOrder.setAdapter(invoicePaymentAdapter);

            receiptPrinted = paymentMobileData.getReceiptPrinted();
            receiptReprint = paymentMobileData.getReceiptReprint();
            paymentId = paymentMobileData.getId();
            paymentCode = paymentMobileData.getCode();

            if(paymentMobileData.getIsConfirmCancel()==1){
                btnCancel.setVisibility(View.GONE);
                btnPrint.setVisibility(View.GONE);
            }

            if(paymentMobileData.getIsConfirm()==1){
                btnCancel.setVisibility(View.GONE);
                btnPrint.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * function for reprint payment
     */
    private void rePrint(){
        btnPrint.setEnabled(false);
        if(receiptReprint==receiptPrinted || receiptReprint>=receiptPrinted){ //lakukan proses print dan submit telah print ke server
            checkLogoClient();
            checkBluetooth();
            btnPrint.setEnabled(false);
        }else{ //ajukan request print ke server
            if(!statusRequestReprint){
                sentPermssionRrequestPrint();
            }else{
                Toasty.info(context,"Pengajuan telah dikirm").show();
            }
        }
    }

    /**
     * permission to cancel payment
     */
    private void cancelPayment(){

        if(paymentId > 0 ){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("is_confirm_cancel",1);
            Call<ResponseArrayObject> call = service.cancelPayment("JWT "+userUtil.getJWTTOken(),paymentId,jsonObject);
            call.enqueue(new Callback<ResponseArrayObject>() {
                @Override
                public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                    if(response.body()!=null&& response.body().getError()==0){
                        Toasty.info(context,"Pembatalan telah diajukan").show();
                    }else{
                        Toasty.info(context,"Terjadi kesalahan").show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                    Toasty.error(context,"Terjadi kesalahan server").show();
                }
            });
        }
    }

    /**
     * print data payment
     */
    @Override
    public void printData() {
        // Fungsi Print
        try {
            outputStream = mBluetoothSocket
                    .getOutputStream();
            resetPrint();
            resetPrint();

            String today = ConversionDate.getInstance().today();
            // printPhoto(R.drawable.logo_print_xenos);
            // printNewLine();
            printLogoClient();
            printCustomWithoutNewLine(customerCode, 0, 0);
            printNewLine();
            printCustomWithoutNewLine(customerName, 0, 0);
            printNewLine();
            // printText(leftRightAlign(UserUtil.getInstance(context).getUsername(),today));
            printText(leftRightAlign(UserUtil.getInstance(context).getName(),today));
            printNewLine();
            printBorder();
            printNewLine();
            printCustomWithoutNewLine("INVOICE          NOMINAL           BAYAR",0,0);
            printNewLine();
            printBorder();
            int totalNominal=0;
            int totalBayar=0;
            String norekTrf="";
            String namerekTrf = "";
            String torekTrf = "";

            String norekGiro = "";
            String namerekGiro = "";
            String torekGiro = "";

            String norekCek="";

            String bankTrf="";
            String bankkGiro="";
            String bankkCek="";

            String dueDateKontrabon = "";

            for(int i=0;i<paymentMobileData.getInvoice().size();i++){
                totalNominal += paymentMobileData.getInvoice().get(i).getInvoiceAmount();
                String nominal = "Rp."+Currency.priceWithoutDecimal(paymentMobileData.getInvoice().get(i).getInvoiceAmount());
                String idInvoice =paymentMobileData.getInvoice().get(i).getInvoiceId();
                //printText(idInvoice+"");
                printCustom(idInvoice,0,0);
                printCustom(nominal,0,1);
                List<PaymentInfo> paymentInfoList = paymentMobileData.getInvoice().get(i).getPaymentInfos();
                for(int k=0;k<paymentInfoList.size();k++){
                    if(paymentInfoList.get(k).getTotal()>0 ||
                            (paymentInfoList.get(k).getTotal()==0 && paymentInfoList.get(k).getType().equals("kontrabon"))
                    ){
                        if(paymentInfoList.get(k).getType().equalsIgnoreCase("cek")){
                            norekCek = paymentInfoList.get(k).getAccountNo();
                            bankkCek = paymentInfoList.get(k).getBankName();
                            }

                        if(paymentInfoList.get(k).getType().equalsIgnoreCase("giro")){
                            norekGiro = paymentInfoList.get(k).getAccountNo();
                            namerekGiro = paymentInfoList.get(k).getAccountName();
                            torekGiro = paymentInfoList.get(k).getTransferTo();
                            bankkGiro = paymentInfoList.get(k).getBankName();
                            }

                        if(paymentInfoList.get(k).getType().equalsIgnoreCase("trf")){
                            norekTrf = paymentInfoList.get(k).getAccountNo();
                            namerekTrf = paymentInfoList.get(k).getAccountName();
                            torekTrf = paymentInfoList.get(k).getTransferTo();
                            bankTrf = paymentInfoList.get(k).getBankName();
                            }

                        if (paymentInfoList.get(k).getType().equalsIgnoreCase("kontrabon")) {
                            dueDateKontrabon = paymentInfoList.get(k).getDueDate();
                        }


                        totalBayar += paymentInfoList.get(k).getTotal();
                        String totalPaymentMethod = "Rp."+Currency.priceWithoutDecimal(paymentInfoList.get(k).getTotal());
                        printText(leftRightAlign(paymentInfoList.get(k).getType().toUpperCase(),totalPaymentMethod));

                        printNewLine();
                    }
                }
                printNewLine();

                if(norekTrf.length()>0){
                    printCustomWithoutNewLine("NO. TRF   ",0,0);
                    printCustomWithoutNewLine(norekTrf,0,1);
                    printNewLine();
                    printCustomWithoutNewLine(bankTrf.toUpperCase(), 0, 0);
                    printNewLine();
                    printCustomWithoutNewLine(" A.n : ", 0, 0);
                    printCustomWithoutNewLine(namerekTrf, 0, 1);
                    printNewLine();
                    printCustomWithoutNewLine(" TRF Ke : ", 0, 0);
                    printCustomWithoutNewLine(torekTrf, 0, 1);
                    printNewLine();
                }

                if(norekCek.length()>0){
                    printCustomWithoutNewLine("NO. CEK   ",0,0);
                    printCustomWithoutNewLine(norekCek,0,1);
                    printNewLine();
                    printCustomWithoutNewLine(bankkCek.toUpperCase(), 0, 0);
                    printNewLine();
                    printNewLine();
                }

                if(norekGiro.length()>0){
                    printCustomWithoutNewLine("NO. GIRO   ",0,0);
                    printCustomWithoutNewLine(norekGiro,0,1);
                    printNewLine();
                    printCustomWithoutNewLine(bankkGiro.toUpperCase(), 0, 0);
                    printNewLine();
                    printCustomWithoutNewLine(" Nama Pengirim : ", 0, 0);
                    printCustomWithoutNewLine(namerekGiro, 0, 1);
                    printNewLine();
                    printCustomWithoutNewLine(" Penerima Giro : ", 0, 0);
                    printCustomWithoutNewLine(torekGiro, 0, 1);
                    printNewLine();
                }

                if(dueDateKontrabon.length()>0){
                    printNewLine();
                    printCustomWithoutNewLine(" Due Date Kontrabon  ", 0, 0);
                    printCustomWithoutNewLine(dueDateKontrabon, 0, 1);
                    printNewLine();
                }
            }
            printBorder();
            printNewLine();
            String totalNominalString = "Rp."+Currency.priceWithoutDecimal(totalNominal);
            String totalBayarString = "Rp."+Currency.priceWithoutDecimal(totalBayar);
            String totalSisaString = "Rp." + Currency.priceWithoutDecimal(totalNominal - totalBayar);

            printText(leftRightAlign("TOTAL TAGIHAN",totalNominalString));
            printNewLine();

            printText(leftRightAlign("TOTAL BAYAR",totalBayarString));
            printNewLine();

            printText(leftRightAlign("SISA", totalSisaString));
            printNewLine();
            printNewLine();

            printNewLine();
            printText(Constants.TERM_AND_CONDITION_TITLE);
            printNewLine();
            printText(Constants.TERM_AND_CONDITION_BODY);
            printNewLine();
            // printText("- Pembayaran menggunakan Cheque/Bilyet Giro ditunjukan atas nama PT..");
//            printNewLine();
//            printText("- Apabila pembayaran melalui transfer, mohon dilengkapi dengan bukti transfer.");
            printNewLine();
            printCustom(codePrint,0,1);
            printNewLine();
            printNewLine();

        } catch (Exception e) {
            Log.e(TAG, "Exe ", e);
        }

        //submit telah print ke server
        submitRequestReprint();
    }

    /**
     * submit request reprint
     */
    private void submitRequestReprint(){
        if(paymentId > 0 ){
            Call<ResponseArrayObject> call = service.salesPaymentReprint("JWT "+userUtil.getJWTTOken(),paymentId);
            call.enqueue(new Callback<ResponseArrayObject>() {
                @Override
                public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                    if(response.body()!=null&& response.body().getError()==0){
                       gotoDetailCustomer();
                    }else{
                        //Toasty.info(context,"Terjadi kesalahan").show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                    //Toasty.info(context,"Terjadi kesalahan").show();
                }
            });
        }
    }

    /**
     * sent request reprint activity to server
     */
    private void sentPermssionRrequestPrint(){
        if(paymentId > 0 ){
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("type","print");
            jsonObject.addProperty("notes","");
            jsonObject.addProperty("customer_code",customerCode);
            jsonObject.addProperty("visit_plan_id", PlanUtil.getInstance(context).getPlanId());

            JsonObject jsonObjectDesc = new JsonObject();
            jsonObjectDesc.addProperty("customer_code",customerCode);
            jsonObjectDesc.addProperty("payment_id",paymentId);
            jsonObjectDesc.addProperty("payment_code",paymentCode);

            jsonObject.add("description",jsonObjectDesc);
            Call<ResponseArrayObject> call = service.permissionAlertPost("JWT "+userUtil.getJWTTOken(),jsonObject);
            call.enqueue(new Callback<ResponseArrayObject>() {
                @Override
                public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                    if(response.body()!=null&& response.body().getError()==0){
                        statusRequestReprint =true;
                        Toasty.info(context,"Cetak ulang telah diajukan").show();
                    }else{
                        Toasty.info(context,"Terjadi kesalahan").show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                    Toasty.info(context,"Terjadi kesalahan").show();
                }
            });
        }
    }

    /**
     * convert drawable to bitmap
     * @param drawable
     * @return Bitmap
     */
    public static Bitmap drawableToBitmap (Drawable drawable) {
        Bitmap bitmap = null;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if(bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if(drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * check logo client from internal storage
     */
    private void checkLogoClient(){
        // check directory
        String storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File file = new File(storageDir, Constants.FILENAME_LOGO);
        if(!file.exists()){
            /**
             *  change logo to default logo
             *  generate file from drawable
             */
            try {
                Bitmap defaultLogo = BitmapFactory.decodeResource(context.getResources(), R.drawable.logo_print_beton);
                FileOutputStream out = new FileOutputStream(file);
                defaultLogo.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * convert base64 string to image
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
