package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Invoice;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.invoices.InvoicePayementAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class PaymentActivity extends to PrinterActivity
 * to print payment
 */
public class PaymentActivity extends PrinterActivity implements PrintResponseCallback<ResponseArrayObject> {
    private ListView lvPayment;
    private Button btnKonfirmPayment;
    private TextView tvTanggal;
    private TextView tvPaymentNo;
    private TextView tvTotalPayment;
    private TextView tvTotalInvoice;
    private TextView tvNumberOfInvoice;
    private static final String TAG = PaymentActivity.class.getSimpleName();
    List<Invoice> invoices;
    private static final int REQUEST_DETAIL_PAYMENT = 3;
    private static final int REQUEST_PAYMENT_METHOD = 4;
    private Gson gson;
    private int receiptPrinted = 0;
    int totalPayment = 0;
    InvoicePayementAdapter paymentAdapter;
    private boolean isKontrabonSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        activity = PaymentActivity.this;

        gson = new Gson();
        customerCode = getIntent().getStringExtra("cust_code");
        customerName = getIntent().getStringExtra("cust_name");
        invoices = new ArrayList<>();

        invoices = gson.fromJson(getIntent().getStringExtra("selected_invoices"), new TypeToken<List<Invoice>>() {
        }.getType());

        setTitle(customerName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        LayoutInflater inflater = LayoutInflater.from(this);
        lvPayment = (ListView) findViewById(R.id.lv_payment);

        View header = inflater.inflate(R.layout.header_activity_payment, null);
        lvPayment.addHeaderView(header);

        tvTanggal = (TextView) header.findViewById(R.id.tv_tanggal);
        tvPaymentNo = (TextView) header.findViewById(R.id.tv_payment_no);
        tvNumberOfInvoice = (TextView) header.findViewById(R.id.tv_number_of_invoice);
        tvTotalInvoice = (TextView) header.findViewById(R.id.tv_total_invoice);
        tvTotalPayment = (TextView) header.findViewById(R.id.tv_total_payment);

        tvTanggal.setText(ConversionDate.getInstance().today());
        tvPaymentNo.setText("-");
        tvNumberOfInvoice.setText(String.valueOf(invoices.size()));
        int total = 0;
        for (int i = 0; i < invoices.size(); i++) {
            total += invoices.get(i).getInvoiceAmount();
        }
        tvTotalInvoice.setText("Rp." + Currency.priceWithoutDecimal(total) + ",-");
        tvTotalPayment.setText("0");

        paymentAdapter = new InvoicePayementAdapter(context, 1, invoices);
        lvPayment.setAdapter(paymentAdapter);
        lvPayment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) { //detail paymnet method
                    Intent intent = new Intent(context, MultiplePaymentMethodActivity.class);
                    intent.putExtra("id", i - 1);
                    intent.putExtra("id_invoice", invoices.get(i - 1).getIdInvoice());
                    intent.putExtra("cust_name", customerName);
                    intent.putExtra("cust_code", customerCode);
                    intent.putExtra("invoice_amount", invoices.get(i - 1).getInvoiceAmount());
                    intent.putExtra("invoice", gson.toJson(invoices.get(i - 1)));
                    startActivityForResult(intent, REQUEST_PAYMENT_METHOD);
                }
            }
        });

        btnKonfirmPayment = (Button) findViewById(R.id.btn_konfirm_payment);
        btnKonfirmPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (totalPayment == 0 && !isKontrabonSet) {
                        Toasty.info(context, "Silahkan masukan total pembayaran dan metode pembayaran").show();
                        btnKonfirmPayment.setEnabled(true);
                        return;
                    } else {
                        // check logo client
                        checkLogoClient();
                        checkBluetooth();
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.toString());
                }

            }
        });
    }

    @Override
    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {

        switch (mRequestCode) {
            case REQUEST_DETAIL_PAYMENT:
                if (mResultCode == Activity.RESULT_OK) {
                    invoices = gson.fromJson(mDataIntent.getStringExtra("selected_invoices"), new TypeToken<List<Invoice>>() {
                    }.getType());
                    setTotalPayment();
                }
                break;
            case REQUEST_PAYMENT_METHOD:
                if (mResultCode == Activity.RESULT_OK) {
                    try {
                        if (invoices.get(mDataIntent.getIntExtra("id", 0)) != null) {
                            List<PaymentInfo> paymentInfos = new ArrayList<>();


                            PaymentInfo paymentInfo = new PaymentInfo();
                            paymentInfo.setType("cash");
                            int totalCash = 0;
                            if (!mDataIntent.getStringExtra("total_cash").isEmpty()) {
                                totalCash = Integer.valueOf(mDataIntent.getStringExtra("total_cash"));
                            }
                            paymentInfo.setTotal(totalCash);

                            paymentInfos.add(paymentInfo);

                            paymentInfo = new PaymentInfo();
                            paymentInfo.setType("giro");
                            int totalGiro = 0;
                            if (!mDataIntent.getStringExtra("total_giro").isEmpty()) {
                                totalGiro = Integer.valueOf(mDataIntent.getStringExtra("total_giro"));
                            }
                            paymentInfo.setTotal(totalGiro);
                            // @BUG: account_name_cheque harusnya, account_name_giro
                            paymentInfo.setAccountName(mDataIntent.getStringExtra("account_name_giro"));
                            paymentInfo.setAccountNo(mDataIntent.getStringExtra("account_no_giro"));
                            paymentInfo.setBankName(mDataIntent.getStringExtra("bank_name_giro"));
                            paymentInfo.setDueDate(mDataIntent.getStringExtra("due_date_giro"));
                            paymentInfo.setTransferTo(mDataIntent.getStringExtra("transfer_to_giro"));
                            paymentInfo.setListPhotoGiro(mDataIntent.getStringArrayListExtra("path_photo_giro"));

                            paymentInfos.add(paymentInfo);

                            paymentInfo = new PaymentInfo();
                            paymentInfo.setType("cek");
                            int totalCheque = 0;
                            if (!mDataIntent.getStringExtra("total_cheque").isEmpty()) {
                                totalCheque = Integer.valueOf(mDataIntent.getStringExtra("total_cheque"));
                            }

                            paymentInfo.setTotal(totalCheque);
                            // @BUG: account_name_giro harusnya, account_name_cheque
                            paymentInfo.setAccountName(mDataIntent.getStringExtra("account_name_cheque"));
                            paymentInfo.setAccountNo(mDataIntent.getStringExtra("account_no_cheque"));
                            paymentInfo.setBankName(mDataIntent.getStringExtra("bank_name_cheque"));
                            paymentInfo.setDueDate(mDataIntent.getStringExtra("due_date_cheque"));
                            paymentInfo.setTransferTo(mDataIntent.getStringExtra("transfer_to_cheque"));
                            paymentInfo.setListPhotoCheque(mDataIntent.getStringArrayListExtra("path_photo_cheque"));

                            paymentInfos.add(paymentInfo);

                            paymentInfo = new PaymentInfo();
                            paymentInfo.setType("trf");
                            int totalTransfer = 0;
                            if (!mDataIntent.getStringExtra("total_transfer").isEmpty()) {
                                totalTransfer = Integer.valueOf(mDataIntent.getStringExtra("total_transfer"));
                            }

                            paymentInfo.setTotal(totalTransfer);
                            paymentInfo.setAccountName(mDataIntent.getStringExtra("account_name_transfer"));
                            paymentInfo.setAccountNo(mDataIntent.getStringExtra("account_no_transfer"));
                            paymentInfo.setBankName(mDataIntent.getStringExtra("bank_name_transfer"));
                            paymentInfo.setDueDate(mDataIntent.getStringExtra("due_date_transfer"));
                            paymentInfo.setTransferTo(mDataIntent.getStringExtra("transfer_to_transfer"));
                            paymentInfo.setListPhotoTransfer(mDataIntent.getStringArrayListExtra("path_photo_transfer"));

                            paymentInfos.add(paymentInfo);

                            paymentInfo = new PaymentInfo();
                            paymentInfo.setType("kontrabon");
                            int totalKontraBon = 0;

                            if(!mDataIntent.getStringExtra("due_date_kontrabon").isEmpty()){
                                isKontrabonSet = true;
                            } else {
                                isKontrabonSet = false;
                            }
                            paymentInfo.setTotal(totalKontraBon);
                            paymentInfo.setDueDate(mDataIntent.getStringExtra("due_date_kontrabon"));
                            paymentInfo.setListPhotoKontraBon(mDataIntent.getStringArrayListExtra("path_photo_kontrabon"));

                            // if (!mDataIntent.getStringExtra("total_kontrabon").isEmpty()) {
                            //     totalKontraBon = Integer.valueOf(mDataIntent.getStringExtra("total_kontrabon"));
                            // }

                            paymentInfos.add(paymentInfo);

                            invoices.get(mDataIntent.getIntExtra("id", 0)).setPaymentInfos(paymentInfos);
                            invoices.get(mDataIntent.getIntExtra("id", 0)).setPaymentAmount(totalCash + totalCheque + totalGiro + totalTransfer + totalKontraBon);
                            paymentAdapter.notifyDataSetChanged();
                            setTotalPayment();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                super.onActivityResult(mRequestCode, mResultCode, mDataIntent);
                break;
        }
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
     * set total payment
     */
    private void setTotalPayment() {
        int total = 0;
        totalPayment = 0;
        for (int i = 0; i < invoices.size(); i++) {
            if(invoices.get(i).getPaymentAmount()!=null){
                total += invoices.get(i).getPaymentAmount();
                totalPayment += invoices.get(i).getPaymentAmount();
            }
        }
        tvTotalPayment.setText("Rp " + Currency.priceWithoutDecimal(total) + ",-");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(Activity.RESULT_OK);
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

    @Override
    public void showErrorPopup() {
        final Dialog dialog = new Dialog(PaymentActivity.this);
        dialog.setContentView(R.layout.custom_allert_confirm_payment);
        // Custom Android Allert Dialog Title
        dialog.setTitle("");

        Button btnTry = (Button) dialog.findViewById(R.id.btn_tryagain);
        // Click cancel to dismiss android custom dialog box
        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        Button btnProcess = (Button) dialog.findViewById(R.id.btn_process);
        // Click cancel to dismiss android custom dialog box
        btnProcess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postDataRequest(PaymentActivity.this);
                dialog.dismiss();
            }
        });
        if (dialog != null) {
            dialog.show();
        }
    }

    /**
     * print data payment
     */
    @Override
    public void printData() {

        try {
            postDataRequest(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * post data payment to server
     */
    private void postDataRequest(final PrintResponseCallback callback) {
        try {
            btnKonfirmPayment.setEnabled(false);
            if (totalPayment == 0 && !isKontrabonSet) {
                //Toasty.info(context,"Silahkan masukan total pembayaran untuk invoice").show();
                btnKonfirmPayment.setEnabled(true);
                return;
            }
            UserUtil userUtil = UserUtil.getInstance(context);
            ApiInterface service = ApiClient.getInstance(context);
            JsonObject jsonObject = new JsonObject();
            JsonArray jsonElements = new JsonArray();
            int invoiceAmmount = 0;
            int paymentAmmount = 0;
            for (int i = 0; i < invoices.size(); i++) {
                JsonObject objectInvoice = new JsonObject();
                objectInvoice.addProperty("invoice_id", invoices.get(i).getIdInvoice());
                objectInvoice.addProperty("invoice_amount", invoices.get(i).getInvoiceAmount());
                objectInvoice.addProperty("payment_amount", invoices.get(i).getPaymentAmount());

                JsonArray paymentInfos = new JsonArray();
                List<PaymentInfo> paymentInfoList = invoices.get(i).getPaymentInfos();
                if(paymentInfoList!=null){
                    for (int k = 0; k < paymentInfoList.size(); k++) {
                        JsonObject paymentInfo = new JsonObject();

                        try {
                            if (paymentInfoList.get(k).getTotal() > 0 || (paymentInfoList.get(k).getTotal()==0 && paymentInfoList.get(k).getType().equals("kontrabon")) ) {
                                if (paymentInfoList.get(k).getType().equalsIgnoreCase("cash")) {
                                    paymentInfo.addProperty("payment_method", paymentInfoList.get(k).getType());
                                    paymentInfo.addProperty("total_payment", paymentInfoList.get(k).getTotal());
                                } else {

                                    if (paymentInfoList.get(k).getType().equalsIgnoreCase("giro") && paymentInfoList.get(k).getListPhotoGiro().size() > 0) {
                                        JsonArray path = new JsonArray();
                                        for(int l = 0; l < paymentInfoList.get(k).getListPhotoGiro().size(); l++ ) {
                                            path.add(convertImageTo64(paymentInfoList.get(k).getListPhotoGiro().get(l)).replaceAll("\n",""));
                                        }
                                        paymentInfo.add("file", path);
                                    }

                                    if (paymentInfoList.get(k).getType().equalsIgnoreCase("cek") && paymentInfoList.get(k).getListPhotoCheque().size() > 0) {
                                        JsonArray path = new JsonArray();
                                        for(int l = 0; l < paymentInfoList.get(k).getListPhotoCheque().size(); l++ ) {
                                            path.add(convertImageTo64(paymentInfoList.get(k).getListPhotoCheque().get(l)).replaceAll("\n",""));
                                        }
                                        paymentInfo.add("file", path);
                                    }

                                    if (paymentInfoList.get(k).getType().equalsIgnoreCase("trf") && paymentInfoList.get(k).getListPhotoTransfer().size() > 0) {
                                        JsonArray path = new JsonArray();
                                        for(int l = 0; l < paymentInfoList.get(k).getListPhotoTransfer().size(); l++ ) {
                                            path.add(convertImageTo64(paymentInfoList.get(k).getListPhotoTransfer().get(l)).replaceAll("\n",""));
                                        }
                                        paymentInfo.add("file", path);
                                    }

                                    if(paymentInfoList.get(k).getType().equalsIgnoreCase("kontrabon") && paymentInfoList.get(k).getListPhotoKontraBon().size() > 0){
                                        JsonArray path = new JsonArray();
                                        for(int l = 0; l < paymentInfoList.get(k).getListPhotoKontraBon().size(); l++ ) {
                                            path.add(convertImageTo64(paymentInfoList.get(k).getListPhotoKontraBon().get(l)).replaceAll("\n",""));
                                        }
                                        paymentInfo.add("file", path);
                                    }

                                    paymentInfo.addProperty("payment_method", paymentInfoList.get(k).getType());
                                    paymentInfo.addProperty("total_payment", paymentInfoList.get(k).getTotal());
                                    paymentInfo.addProperty("account_no", paymentInfoList.get(k).getAccountNo());
                                    paymentInfo.addProperty("account_name", paymentInfoList.get(k).getAccountName());
                                    paymentInfo.addProperty("bank_name", paymentInfoList.get(k).getBankName());
                                    paymentInfo.addProperty("due_date", paymentInfoList.get(k).getDueDate());
                                    paymentInfo.addProperty("transfer_to", paymentInfoList.get(k).getTransferTo());
                                }
                                paymentInfos.add(paymentInfo);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                objectInvoice.add("payment_info", paymentInfos);
                jsonElements.add(objectInvoice);

                invoiceAmmount += (invoices.get(i).getInvoiceAmount()!=null) ? invoices.get(i).getInvoiceAmount(): 0;
                paymentAmmount += (invoices.get(i).getPaymentAmount()!=null) ? invoices.get(i).getPaymentAmount(): 0;
            }
            jsonObject.add("invoice", jsonElements);
            jsonObject.addProperty("visit_plan_id", PlanUtil.getInstance(context).getPlanId());
            jsonObject.addProperty("customer_code", customerCode);
            jsonObject.addProperty("invoice_amount", invoiceAmmount);
            jsonObject.addProperty("payment_amount", paymentAmmount);


            jsonObject.addProperty("receipt_printed", receiptPrinted);

            if (Constants.DEBUG) {
                Log.i("datapayment", jsonObject.toString());
            }

            Call<ResponseArrayObject> call = service.salesPaymentPost("JWT " + userUtil.getJWTTOken(), jsonObject);

            call.enqueue(new Callback<ResponseArrayObject>() {
                @Override
                public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                    if (response.body() != null && response.body().getError() == 0) {
                        // String data = response.body().getData().toString();
                        callback.onSuccess(response);
                        gotoDetailCustomer();
                    }
                    btnKonfirmPayment.setEnabled(true);
                }

                @Override
                public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                    callback.onFailure("Failed request to server", t);
                    btnKonfirmPayment.setEnabled(true);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * convert image to String base64
     * @param path
     * @return String base64
     */
    private String convertImageTo64(String path) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, Constants.MAX_SIZE_QUALITY_IMAGE, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        // Log.i(">>> foto",encodedImage);
        return encodedImage;

    }

    /**
     * convert string base64 to bitmap
     * @param base64Encoded
     * @return Bitmap
     */
    private Bitmap convertBase64ToImage(String base64Encoded){
        final byte[] decodedBytes = Base64.decode(base64Encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }


    /*
        Save print data to server
        if success -> onSuccess
        if failed -> onFailure
     */
    @Override
    public void onSuccess(Response<ResponseArrayObject> response) {
        // Fungsi Print
        try {
            outputStream = mBluetoothSocket.getOutputStream();
            for (int p = 1; p <= Constants.TOTAL_PRINT; p++) {
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
                // printText(leftRightAlign(UserUtil.getInstance(getApplicationContext()).getUsername(), today));
                printText(leftRightAlign(UserUtil.getInstance(getApplicationContext()).getName(), today));
                printNewLine();
                printBorder();
                printNewLine();
                printCustomWithoutNewLine("INVOICE          NOMINAL           BAYAR", 0, 0);
                printNewLine();
                printBorder();
                int totalNominal = 0;
                int totalBayar = 0;
                String norekTrf = "";
                String namerekTrf = "";
                String torekTrf = "";

                String norekGiro = "";
                String namerekGiro = "";
                String torekGiro = "";

                String norekCek = "";

                String bankTrf = "";
                String bankkGiro = "";
                String bankkCek = "";

                String dueDateKontrabon = "";

                for (int i = 0; i < invoices.size(); i++) {
                    totalNominal += invoices.get(i).getInvoiceAmount();
                    String nominal = "Rp." + Currency.priceWithoutDecimal(invoices.get(i).getInvoiceAmount());
                    String idInvoice = invoices.get(i).getIdInvoice();
                    if (i >= 1) {
                        printNewLine();
                        printNewLine();
                    }
                    printCustom(idInvoice, 0, 0);
                    printCustom(nominal, 0, 1);
                    List<PaymentInfo> paymentInfoList = invoices.get(i).getPaymentInfos();
                    for (int k = 0; k < paymentInfoList.size(); k++) {
                        if (paymentInfoList.get(k).getTotal() > 0 || paymentInfoList.get(k).getType().equalsIgnoreCase("kontrabon")) {
                            if (paymentInfoList.get(k).getType().equalsIgnoreCase("cek")) {
                                norekCek = paymentInfoList.get(k).getAccountNo();
                                /*
                                if (paymentInfoList.get(k).getBankName().length() > 3) {
                                   bankkCek = paymentInfoList.get(k).getBankName().substring(0, 3);
                                } else {
                                   bankkCek = paymentInfoList.get(k).getBankName();
                                }
                                */
                                bankkCek = paymentInfoList.get(k).getBankName();
                            }

                            if (paymentInfoList.get(k).getType().equalsIgnoreCase("giro")) {
                                norekGiro = paymentInfoList.get(k).getAccountNo();
                                namerekGiro = paymentInfoList.get(k).getAccountName();
                                torekGiro = paymentInfoList.get(k).getTransferTo();
                                /*
                                if (paymentInfoList.get(k).getBankName().length() > 3) {
                                   bankkGiro = paymentInfoList.get(k).getBankName().substring(0, 3);
                                } else {
                                   bankkGiro = paymentInfoList.get(k).getBankName();
                                }
                                */
                                bankkGiro = paymentInfoList.get(k).getBankName();
                            }

                            if (paymentInfoList.get(k).getType().equalsIgnoreCase("trf")) {
                                norekTrf = paymentInfoList.get(k).getAccountNo();
                                namerekTrf = paymentInfoList.get(k).getAccountName();
                                torekTrf = paymentInfoList.get(k).getTransferTo();
                                /*
                                if (paymentInfoList.get(k).getBankName().length() > 3) {
                                    bankTrf = paymentInfoList.get(k).getBankName().substring(0, 3);
                                } else {
                                    bankTrf = paymentInfoList.get(k).getBankName();
                                }
                                */
                                bankTrf = paymentInfoList.get(k).getBankName();
                            }

                            if (paymentInfoList.get(k).getType().equalsIgnoreCase("kontrabon")) {
                                dueDateKontrabon = paymentInfoList.get(k).getDueDate();
                            }

                            totalBayar += paymentInfoList.get(k).getTotal();
                            String totalPaymentMethod = "Rp." + Currency.priceWithoutDecimal(paymentInfoList.get(k).getTotal());

                            printText(leftRightAlign(paymentInfoList.get(k).getType().toUpperCase(), totalPaymentMethod));
                            printNewLine();
                        }

                        // if(paymentInfoList.get(k).getType().equalsIgnoreCase("kontrabon") && isKontrabonSet){
                        //     dueDateKontrabon = paymentInfoList.get(k).getDueDate();
                        //     totalBayar += paymentInfoList.get(k).getTotal();
                        //     String totalPaymentMethod = "Rp." + Currency.priceWithoutDecimal(paymentInfoList.get(k).getTotal());
                        //
                        //     printText(leftRightAlign(paymentInfoList.get(k).getType().toUpperCase(), totalPaymentMethod));
                        // }
                    }

                    if (norekTrf.length() > 0) {
                        printNewLine();
                        printCustomWithoutNewLine(" NO. TRF   ", 0, 0);
                        printCustomWithoutNewLine(norekTrf, 0, 1);
                        printNewLine();
                        printCustomWithoutNewLine(" " + bankTrf.toUpperCase(), 0, 0);
                        printNewLine();
                        printCustomWithoutNewLine(" A.n : ", 0, 0);
                        printCustomWithoutNewLine(namerekTrf, 0, 1);
                        printNewLine();
                        printCustomWithoutNewLine(" TRF Ke : ", 0, 0);
                        printCustomWithoutNewLine(torekTrf, 0, 1);
                        printNewLine();
                    }

                    if (norekCek.length() > 0) {
                        printNewLine();
                        printCustomWithoutNewLine(" NO. CEK   ", 0, 0);
                        printCustomWithoutNewLine(norekCek, 0, 1);
                        printNewLine();
                        printCustomWithoutNewLine(" " + bankkCek.toUpperCase(), 0, 0);
                        printNewLine();
                    }

                    if (norekGiro.length() > 0) {
                        printNewLine();
                        printCustomWithoutNewLine(" NO. GIRO   ", 0, 0);
                        printCustomWithoutNewLine(norekGiro, 0, 1);
                        printNewLine();
                        printCustomWithoutNewLine(" " + bankkGiro.toUpperCase(), 0, 0);
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
                String totalNominalString = "Rp." + Currency.priceWithoutDecimal(totalNominal);
                String totalBayarString = "Rp." + Currency.priceWithoutDecimal(totalBayar);
                String totalSisaString = "Rp." + Currency.priceWithoutDecimal(totalNominal - totalBayar);

                printText(leftRightAlign("TOTAL TAGIHAN", totalNominalString));
                printNewLine();

                printText(leftRightAlign("TOTAL BAYAR", totalBayarString));
                printNewLine();

                printText(leftRightAlign("SISA", totalSisaString));
                printNewLine();
                printNewLine();

                printNewLine();
                printCustom(Constants.TERM_AND_CONDITION_TITLE, 0, 0);
                printCustom(Constants.TERM_AND_CONDITION_BODY, 0, 0);
                // printCustom("- Pembayaran menggunakan Cheque/Bilyet Giro ditunjukan atas nama PT..", 0, 0);
//                printCustom("- Apabila pembayaran melalui transfer, mohon dilengkapi dengan bukti transfer.", 0, 0);

                printNewLine();
                if (Constants.DEV_MODE == false) {
                    printCustom(codePrint, 0, 1);
                } else {
                    printCustom(codePrint + " - Under Development Mode", 0, 1);
                }

                printNewLine();
                printNewLine();
            }

            /*if (mBluetoothAdapter != null)
                mBluetoothAdapter.disable();

            closeSocket(mBluetoothSocket);*/

        } catch (Exception e) {
            Log.e(TAG, "Exe ", e);
        }

        receiptPrinted = 1;

    }

    @Override
    public void onFailure(String message, Throwable t) {
        Toasty.info(context, "Gagal mengirim data ke server, Silahkan coba lagi!").show();
        t.printStackTrace();
    }
}