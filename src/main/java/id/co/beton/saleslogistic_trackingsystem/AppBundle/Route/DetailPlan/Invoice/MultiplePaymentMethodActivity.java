package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Invoice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
import id.co.beton.saleslogistic_trackingsystem.Model.PaymentInfo;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;
import id.co.beton.saleslogistic_trackingsystem.Utils.ImagePicker;

/**
 * Class MultiplePaymentMethodActivity
 * class to select payment method
 */
public class MultiplePaymentMethodActivity extends AppCompatActivity {
    private LinearLayout llCash, llGiro, llCheque, llTransfer, llKontrabon;
    private LinearLayout llImageGiro, llImageCheque, llImageTransfer, llImageKontraBon;
    private LinearLayout llContentllGiro, llContentChaque, llContentTransfer, llContentCash, llContentKontraBon;
    private TextView tvTotalCash;
    private EditText etTotalCash;
    private TextView tvAccountNameGiro, tvGiroNo, tvBankGiro, tvDueDateGiro, tvTotalGiro, tvGiroTo, tvBuktiGiro;
    private EditText etAccountNameGiro, etGiroNo, etBankGiro, etDueDateGiro, etTotalGiro, etGiroTo;
    private TextView tvAccountNameCheque, tvChequeNo, tvBankCheque, tvDueDateCheque, tvTotalCheque, tvBuktiCheque;
    private EditText etAccountNameCheque, etChequeNo, etBankCheque, etDueDateCheque, etTotalCheque;
    private TextView tvBankTransfer, tvAccountNo, tvAccountNameTransfer, tvTransferTo, tvTotalTransfer, tVBuktiTrf;
    private EditText etBankTransfer, etAccountNo, etAccountNameTransfer, etTransferTo, etTotalTransfer;
    private TextView tvDueDateKontraBon, tvTotalKontraBon;
    private EditText etDueDateKontraBon, etTotalKontraBon;
    private Button btnSavePaymentMethod;
    private TextView tvInvoice;

    private Animation animationUp;
    private Animation animationDown;
    private static final String TAG = PaymentMethodActivity.class.getSimpleName();
    String customerName, customerCode;
    private Context context;
    private Gson gson;

    private ImageView imgGiroUpload;
    private ImageView imgChequeUpload;
    private ImageView imgTransferUpload;
    private ImageView imgKontraBonUpload;

    private static final String IMAGE_DIRECTORY = "/tracking-system";
    private int GALLERY = 100, CAMERA = 200;

    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    int totalInvoice = 0;
    private TextView tvTotalInvoice;
    private int idInvoice;
    private int takeImage;
    String mCurrentPhotoPath;
    private Invoice selectedInvoice;

    private List<String> listPhotoGiro = new ArrayList<String>();
    private List<String> listPhotoCheque = new ArrayList<String>();
    private List<String> listPhotoTransfer = new ArrayList<String>();
    private List<String> listPhotoKontraBon = new ArrayList<String>();

    private int countImageGiro = 0;
    private int countImageCheque = 0;
    private int countImageTransfer = 0;
    private int countImageKontraBon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_payment_method);

        gson = new Gson();
        selectedInvoice = gson.fromJson(getIntent().getStringExtra("invoice"), Invoice.class);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        customerCode = getIntent().getStringExtra("cust_code");
        customerName = getIntent().getStringExtra("cust_name");
        context = this;

        setTitle(customerName);
        myCalendar = Calendar.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        listPhotoGiro.clear();
        listPhotoCheque.clear();
        listPhotoTransfer.clear();
        listPhotoKontraBon.clear();

        tvInvoice = (TextView) findViewById(R.id.tv_invoice);
        tvInvoice.setText(getIntent().getStringExtra("id_invoice"));

        totalInvoice = getIntent().getIntExtra("invoice_amount", 0);

        idInvoice = getIntent().getIntExtra("id", 0);

        tvTotalInvoice = (TextView) findViewById(R.id.tv_total_invoice);
        tvTotalInvoice.setText("Rp." + Currency.priceToString(totalInvoice));

        btnSavePaymentMethod = (Button) findViewById(R.id.btn_save_payment_method);
        btnSavePaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //cek total jika melebihi notif
                try {
                    // @NOTE: Dev Mode
                    if (Constants.DEV_MODE == true) {
                        etTotalCash.setText("10");

                        etAccountNameGiro.setText("Yuni");
                        etBankGiro.setText("BRI");
                        etGiroNo.setText("123456");
                        etGiroTo.setText("Pipamas - BCA - 123445663");
                        etDueDateGiro.setText("18-12-30");
                        etTotalGiro.setText("20");

                        etAccountNameCheque.setText("Yuni");
                        etBankCheque.setText("BNI");
                        etChequeNo.setText("123456");
                        etDueDateCheque.setText("18-12-30");
                        etTotalCheque.setText("20");

                        etAccountNameTransfer.setText("Yuni");
                        etBankTransfer.setText("BCA");
                        etAccountNo.setText("123456");
                        etTransferTo.setText("Pipamas - BCA - 123445663");
                        etTotalTransfer.setText("20");
                    }

                    int totalCash = 0, totalCheque = 0, totalGiro = 0, totalTransfer = 0, totalKontraBon = 0;
                    if (etTotalCash.getText().length() > 0) {
                        totalCash = Integer.valueOf(etTotalCash.getText().toString());
                    }

                    if (etTotalCheque.getText().length() > 0) {
                        totalCheque = Integer.valueOf(etTotalCheque.getText().toString());
                    }

                    if (etTotalGiro.getText().length() > 0) {
                        totalGiro = Integer.valueOf(etTotalGiro.getText().toString());
                    }

                    if (etTotalTransfer.getText().length() > 0) {
                        totalTransfer = Integer.valueOf(etTotalTransfer.getText().toString());
                    }

                    if(etTotalKontraBon.getText().length() >0 ){
                        totalKontraBon = Integer.valueOf(etTotalKontraBon.getText().toString());
                    }

                    if (totalCash + totalCheque + totalGiro + totalTransfer + totalKontraBon > totalInvoice) {
                        Toasty.info(context, "Total pembayaran melebihi total invoice").show();
                    } else {
                        Intent intent = new Intent();
                        intent.putExtra("id", idInvoice);

                        intent.putExtra("total_cash", etTotalCash.getText() + "");

                        intent.putExtra("account_name_giro", etAccountNameGiro.getText() + "");
                        intent.putExtra("bank_name_giro", etBankGiro.getText() + "");
                        intent.putExtra("account_no_giro", etGiroNo.getText() + "");
                        intent.putExtra("transfer_to_giro", etGiroTo.getText() + "");
                        intent.putExtra("due_date_giro", etDueDateGiro.getText() + "");
                        intent.putExtra("total_giro", etTotalGiro.getText() + "");
                        intent.putStringArrayListExtra("path_photo_giro", (ArrayList<String>) listPhotoGiro);
                        // intent.putExtra("path_photo_giro", realPathGiro);

                        intent.putExtra("account_name_cheque", etAccountNameCheque.getText() + "");
                        intent.putExtra("bank_name_cheque", etBankCheque.getText() + "");
                        intent.putExtra("account_no_cheque", etChequeNo.getText() + "");
                        intent.putExtra("transfer_to_cheque", "");
                        intent.putExtra("due_date_cheque", etDueDateCheque.getText() + "");
                        intent.putExtra("total_cheque", etTotalCheque.getText() + "");
                        intent.putStringArrayListExtra("path_photo_cheque", (ArrayList<String>) listPhotoCheque);
                        // intent.putExtra("path_photo_cheque", realPathCheque);

                        intent.putExtra("account_name_transfer", etAccountNameTransfer.getText() + "");
                        intent.putExtra("bank_name_transfer", etBankTransfer.getText() + "");
                        intent.putExtra("account_no_transfer", etAccountNo.getText() + "");
                        intent.putExtra("transfer_to_transfer", etTransferTo.getText() + "");
                        intent.putExtra("due_date_transfer", "");
                        intent.putExtra("total_transfer", etTotalTransfer.getText() + "");
                        intent.putStringArrayListExtra("path_photo_transfer", (ArrayList<String>) listPhotoTransfer);
                        // intent.putExtra("path_photo_transfer", realPathTransfer);

                        intent.putExtra("due_date_kontrabon", etDueDateKontraBon.getText() + "");
                        intent.putExtra("total_kontrabon", etTotalKontraBon.getText() + "");
                        intent.putStringArrayListExtra("path_photo_kontrabon", (ArrayList<String>) listPhotoKontraBon);
                        // intent.putExtra("path_photo_kontrabon", realPathKontrabon);

                        setResult(Activity.RESULT_OK, intent);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Cash
        llCash = (LinearLayout) findViewById(R.id.ll_cash);
        llContentCash = (LinearLayout) findViewById(R.id.ll_content_cash);
        tvTotalCash = (TextView) findViewById(R.id.tv_total_cash);
        etTotalCash = (EditText) findViewById(R.id.et_total_cash);

        //giro
        llGiro = (LinearLayout) findViewById(R.id.ll_giro);
        llContentllGiro = (LinearLayout) findViewById(R.id.ll_content_giro);
        llImageGiro = (LinearLayout) findViewById(R.id.ll_image_giro);

        tvAccountNameGiro = (TextView) findViewById(R.id.tv_account_name_giro);
        tvGiroNo = (TextView) findViewById(R.id.tv_giro_no);
        tvBankGiro = (TextView) findViewById(R.id.tv_bank_giro);
        tvDueDateGiro = (TextView) findViewById(R.id.tv_due_date_giro);
        tvTotalGiro = (TextView) findViewById(R.id.tv_total_giro);
        tvGiroTo = (TextView) findViewById(R.id.tv_giro_to);
        tvBuktiGiro = (TextView) findViewById(R.id.tv_bukti_giro);
        etAccountNameGiro = (EditText) findViewById(R.id.et_account_name_giro);
        etGiroNo = (EditText) findViewById(R.id.et_giro_no);
        etBankGiro = (EditText) findViewById(R.id.et_bank_giro);
        etDueDateGiro = (EditText) findViewById(R.id.et_due_date_giro);
        etTotalGiro = (EditText) findViewById(R.id.et_total_giro);
        etGiroTo = (EditText) findViewById(R.id.et_giro_to);
        imgGiroUpload = (ImageView) findViewById(R.id.img_bukti_giro);
        imgGiroUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countImageGiro < Constants.MAX_COUNT_IMAGE) {
                    takeImage = 1;
                    // showPictureDialog();
                    takePhotoFromCamera();
                } else {
                    Toasty.info(context, "Photo bukti giro sudah mencapai batas yang diizinkan").show();
                }

            }
        });


        etDueDateGiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(1);
            }
        });


        //cek
        llCheque = (LinearLayout) findViewById(R.id.ll_cheque);
        llContentChaque = (LinearLayout) findViewById(R.id.ll_content_cheque);
        llImageCheque = (LinearLayout) findViewById(R.id.ll_image_cheque);

        tvAccountNameCheque = (TextView) findViewById(R.id.tv_account_name_cheque);
        tvChequeNo = (TextView) findViewById(R.id.tv_chaque_no);
        tvBankCheque = (TextView) findViewById(R.id.tv_bank_cheque);
        tvDueDateCheque = (TextView) findViewById(R.id.tv_due_date_cheque);
        tvTotalCheque = (TextView) findViewById(R.id.tv_total_cheque);
        tvBuktiCheque = (TextView) findViewById(R.id.tv_bukti_cheque);
        etAccountNameCheque = (EditText) findViewById(R.id.et_account_name_cheque);
        etChequeNo = (EditText) findViewById(R.id.et_chaque_no);
        etBankCheque = (EditText) findViewById(R.id.et_bank_cheque);
        etDueDateCheque = (EditText) findViewById(R.id.et_due_date_cheque);
        etTotalCheque = (EditText) findViewById(R.id.et_total_ceque);
        imgChequeUpload = (ImageView) findViewById(R.id.img_bukti_cheque);
        imgChequeUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countImageCheque < Constants.MAX_COUNT_IMAGE) {
                    takeImage = 2;
                    // showPictureDialog();
                    takePhotoFromCamera();
                } else {
                    Toasty.info(context, "Photo bukti Cek sudah mencapai batas yang diizinkan").show();
                }

            }
        });

        etDueDateCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(0);
            }
        });

        //bank transfer
        llTransfer = (LinearLayout) findViewById(R.id.ll_transsfer);
        llContentTransfer = (LinearLayout) findViewById(R.id.ll_content_bank_transfer);
        llImageTransfer = (LinearLayout) findViewById(R.id.ll_image_transfer);

        tvBankTransfer = (TextView) findViewById(R.id.tv_bank_name_transfer);
        tvAccountNo = (TextView) findViewById(R.id.tv_account_no_transfer);
        tvAccountNameTransfer = (TextView) findViewById(R.id.tv_account_name_transfer);
        tvTransferTo = (TextView) findViewById(R.id.tv_transfer_to_account);
        etBankTransfer = (EditText) findViewById(R.id.et_bank_name_transfer);
        tvTotalTransfer = (TextView) findViewById(R.id.tv_total_transfer);
        tVBuktiTrf = (TextView) findViewById(R.id.tv_bukti_trf);
        etAccountNo = (EditText) findViewById(R.id.et_account_no_transfer);
        etAccountNameTransfer = (EditText) findViewById(R.id.et_account_name_transfer);
        etTransferTo = (EditText) findViewById(R.id.et_transfer_to_account);
        etTotalTransfer = (EditText) findViewById(R.id.et_total_transfer);
        imgTransferUpload = (ImageView) findViewById(R.id.img_bukti_trf);
        imgTransferUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(countImageTransfer < Constants.MAX_COUNT_IMAGE){
                    takeImage = 3;
                    // showPictureDialog();
                    takePhotoFromCamera();
                } else {
                    Toasty.info(context, "Photo bukti transfer sudah mencapai batas yang diizinkan").show();
                }
            }
        });

        // kotra bon
        llKontrabon = (LinearLayout) findViewById(R.id.ll_kontrabon);
        llContentKontraBon = (LinearLayout) findViewById(R.id.ll_content_kontrabon);
        llImageKontraBon = (LinearLayout) findViewById(R.id.ll_image_kontrabon);

        tvDueDateKontraBon = (TextView) findViewById(R.id.tv_date_kontrabon);
        tvTotalKontraBon = (TextView) findViewById(R.id.tv_total_kontrabon);
        etTotalKontraBon = (EditText) findViewById(R.id.et_total_kontrabon);
        etDueDateKontraBon = (EditText) findViewById(R.id.et_due_date_kontrabon);
        etDueDateKontraBon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(2);
            }
        });
        imgKontraBonUpload = (ImageView) findViewById(R.id.img_bukti_kontrabon);
        imgKontraBonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(countImageKontraBon < Constants.MAX_COUNT_IMAGE){
                    takeImage = 4;
                    // showPictureDialog();
                    takePhotoFromCamera();
                } else {
                    Toasty.info(context, "Photo bukti kontra bon sudah mencapai batas yang diizinkan").show();
                }
            }
        });



        /*
         * This is Bad code
         * don't try this at home or i will haunt you everyday
         *
         * Really Hate this :((
         * Cz will produce huge bug
         */
        if (selectedInvoice.getPaymentInfos() != null) {
            List<PaymentInfo> paymentsInfo = selectedInvoice.getPaymentInfos();
            PaymentInfo cashPayment = paymentsInfo.get(0);
            PaymentInfo giroPayment = paymentsInfo.get(1);
            PaymentInfo chequePayment = paymentsInfo.get(2);
            PaymentInfo transferPayment = paymentsInfo.get(3);
            PaymentInfo contrabonPayment = paymentsInfo.get(4);

            //Set Cash
            etTotalCash.setText(validateTotalValue(cashPayment.getTotal()));

            //Set Giro
            etAccountNameGiro.setText(giroPayment.getAccountName());
            etBankGiro.setText(giroPayment.getBankName());
            etGiroNo.setText(giroPayment.getAccountNo());
            etGiroTo.setText(giroPayment.getTransferTo());
            etDueDateGiro.setText(giroPayment.getDueDate());
            etTotalGiro.setText(validateTotalValue(giroPayment.getTotal()));
            for (String image: giroPayment.getListPhotoGiro()) {
                updateLayout(BitmapFactory.decodeFile(image), 1);
            }

            //Set Cheque
            etAccountNameCheque.setText(chequePayment.getAccountName());
            etBankCheque.setText(chequePayment.getBankName());
            etChequeNo.setText(chequePayment.getAccountNo());
            etDueDateCheque.setText(chequePayment.getDueDate());
            etTotalCheque.setText(validateTotalValue(chequePayment.getTotal()));
            for (String image: chequePayment.getListPhotoCheque()) {
                updateLayout(BitmapFactory.decodeFile(image), 2);
            }

            //Set Transfer
            etAccountNameTransfer.setText(transferPayment.getAccountName());
            etBankTransfer.setText(transferPayment.getBankName());
            etAccountNo.setText(transferPayment.getAccountNo());
            etTransferTo.setText(transferPayment.getTransferTo());
            etTotalTransfer.setText(validateTotalValue(transferPayment.getTotal()));
            for (String image: transferPayment.getListPhotoTransfer()) {
                updateLayout(BitmapFactory.decodeFile(image), 3);
            }

            //Set ContraBon
            etDueDateKontraBon.setText(contrabonPayment.getDueDate());
            etTotalKontraBon.setText(validateTotalValue(contrabonPayment.getTotal()));
            for (String image : contrabonPayment.getListPhotoKontraBon()) {
                updateLayout(BitmapFactory.decodeFile(image), 4);
            }
        }

        llContentCash.setVisibility(View.GONE);
        tvTotalCash.setVisibility(View.GONE);
        etTotalCash.setVisibility(View.GONE);

        llContentllGiro.setVisibility(View.GONE);
        llImageGiro.setVisibility(View.GONE);
        tvAccountNameGiro.setVisibility(View.GONE);
        tvGiroNo.setVisibility(View.GONE);
        tvBankGiro.setVisibility(View.GONE);
        tvDueDateGiro.setVisibility(View.GONE);
        tvTotalGiro.setVisibility(View.GONE);
        tvBuktiGiro.setVisibility(View.GONE);
        etAccountNameGiro.setVisibility(View.GONE);
        etGiroNo.setVisibility(View.GONE);
        etBankGiro.setVisibility(View.GONE);
        etDueDateGiro.setVisibility(View.GONE);
        etTotalGiro.setVisibility(View.GONE);
        imgGiroUpload.setVisibility(View.GONE);

        llContentChaque.setVisibility(View.GONE);
        llImageCheque.setVisibility(View.GONE);
        tvAccountNameCheque.setVisibility(View.GONE);
        tvChequeNo.setVisibility(View.GONE);
        tvBankCheque.setVisibility(View.GONE);
        tvDueDateCheque.setVisibility(View.GONE);
        tvTotalCheque.setVisibility(View.GONE);
        tvBuktiCheque.setVisibility(View.GONE);
        etAccountNameCheque.setVisibility(View.GONE);
        etChequeNo.setVisibility(View.GONE);
        etBankCheque.setVisibility(View.GONE);
        etDueDateCheque.setVisibility(View.GONE);
        etTotalGiro.setVisibility(View.GONE);
        imgChequeUpload.setVisibility(View.GONE);

        llContentTransfer.setVisibility(View.GONE);
        llImageTransfer.setVisibility(View.GONE);
        tvBankTransfer.setVisibility(View.GONE);
        tvAccountNo.setVisibility(View.GONE);
        tvAccountNameTransfer.setVisibility(View.GONE);
        tvTransferTo.setVisibility(View.GONE);
        tvTotalTransfer.setVisibility(View.GONE);
        tVBuktiTrf.setVisibility(View.GONE);
        etBankTransfer.setVisibility(View.GONE);
        etAccountNo.setVisibility(View.GONE);
        etAccountNameTransfer.setVisibility(View.GONE);
        etTransferTo.setVisibility(View.GONE);
        etTotalTransfer.setVisibility(View.GONE);
        imgTransferUpload.setVisibility(View.GONE);

        llContentKontraBon.setVisibility(View.GONE);
        llImageKontraBon.setVisibility(View.GONE);
        tvDueDateKontraBon.setVisibility(View.GONE);
        tvTotalKontraBon.setVisibility(View.GONE);
        etDueDateKontraBon.setVisibility(View.GONE);
        etTotalKontraBon.setVisibility(View.GONE);
        imgKontraBonUpload.setVisibility(View.GONE);

        animationUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        llCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llContentCash.isShown()) {
                    llContentCash.setVisibility(View.GONE);
                    tvTotalCash.setVisibility(View.GONE);
                    etTotalCash.setVisibility(View.GONE);
                } else {
                    llContentCash.setVisibility(View.VISIBLE);
                    tvTotalCash.setVisibility(View.VISIBLE);
                    etTotalCash.setVisibility(View.VISIBLE);
                }
            }
        });

        llGiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llContentllGiro.isShown() || llImageGiro.isShown() || tvAccountNameGiro.isShown() || tvGiroNo.isShown() || tvBankGiro.isShown() || tvDueDateGiro.isShown() || etAccountNameGiro.isShown() || etGiroNo.isShown() || etBankGiro.isShown() || etDueDateGiro.isShown()) {
                    llContentllGiro.setVisibility(View.GONE);
                    llImageGiro.setVisibility(View.GONE);
                    tvAccountNameGiro.setVisibility(View.GONE);
                    tvGiroNo.setVisibility(View.GONE);
                    tvBankGiro.setVisibility(View.GONE);
                    tvDueDateGiro.setVisibility(View.GONE);
                    tvBuktiGiro.setVisibility(View.GONE);
                    etAccountNameGiro.setVisibility(View.GONE);
                    etGiroNo.setVisibility(View.GONE);
                    etBankGiro.setVisibility(View.GONE);
                    etDueDateGiro.setVisibility(View.GONE);
                    tvTotalGiro.setVisibility(View.GONE);
                    etTotalGiro.setVisibility(View.GONE);
                    imgGiroUpload.setVisibility(View.GONE);
                } else {
                    llContentllGiro.setVisibility(View.VISIBLE);
                    llImageGiro.setVisibility(View.VISIBLE);
                    tvAccountNameGiro.setVisibility(View.VISIBLE);
                    tvGiroNo.setVisibility(View.VISIBLE);
                    tvBankGiro.setVisibility(View.VISIBLE);
                    tvDueDateGiro.setVisibility(View.VISIBLE);
                    tvBuktiGiro.setVisibility(View.VISIBLE);
                    etAccountNameGiro.setVisibility(View.VISIBLE);
                    etGiroNo.setVisibility(View.VISIBLE);
                    etBankGiro.setVisibility(View.VISIBLE);
                    etDueDateGiro.setVisibility(View.VISIBLE);
                    tvTotalGiro.setVisibility(View.VISIBLE);
                    etTotalGiro.setVisibility(View.VISIBLE);
                    imgGiroUpload.setVisibility(View.VISIBLE);
                }
            }
        });

        llCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llContentChaque.isShown() || llImageCheque.isShown() || tvAccountNameCheque.isShown() || tvChequeNo.isShown() || tvBankCheque.isShown() || tvDueDateCheque.isShown() || etAccountNameCheque.isShown() || etChequeNo.isShown() || etBankCheque.isShown() || etDueDateCheque.isShown()) {
                    llContentChaque.setVisibility(View.GONE);
                    llImageCheque.setVisibility(View.GONE);
                    tvAccountNameCheque.setVisibility(View.GONE);
                    tvChequeNo.setVisibility(View.GONE);
                    tvBankCheque.setVisibility(View.GONE);
                    tvDueDateCheque.setVisibility(View.GONE);
                    tvBuktiCheque.setVisibility(View.GONE);
                    etAccountNameCheque.setVisibility(View.GONE);
                    etChequeNo.setVisibility(View.GONE);
                    etBankCheque.setVisibility(View.GONE);
                    etDueDateCheque.setVisibility(View.GONE);
                    tvTotalCheque.setVisibility(View.GONE);
                    etTotalCheque.setVisibility(View.GONE);
                    imgChequeUpload.setVisibility(View.GONE);
                } else {
                    llContentChaque.setVisibility(View.VISIBLE);
                    llImageCheque.setVisibility(View.VISIBLE);
                    tvAccountNameCheque.setVisibility(View.VISIBLE);
                    tvChequeNo.setVisibility(View.VISIBLE);
                    tvBankCheque.setVisibility(View.VISIBLE);
                    tvDueDateCheque.setVisibility(View.VISIBLE);
                    tvBuktiCheque.setVisibility(View.VISIBLE);
                    etAccountNameCheque.setVisibility(View.VISIBLE);
                    etChequeNo.setVisibility(View.VISIBLE);
                    etBankCheque.setVisibility(View.VISIBLE);
                    etDueDateCheque.setVisibility(View.VISIBLE);
                    tvTotalCheque.setVisibility(View.VISIBLE);
                    etTotalCheque.setVisibility(View.VISIBLE);
                    imgChequeUpload.setVisibility(View.VISIBLE);
                }
            }
        });

        llTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (llContentTransfer.isShown() || llImageTransfer.isShown() || tvBankTransfer.isShown() || tvAccountNo.isShown() || tvAccountNameTransfer.isShown() || tvTransferTo.isShown() || etBankTransfer.isShown() || etAccountNo.isShown() || etAccountNameTransfer.isShown() || etTransferTo.isShown()) {
                    llContentTransfer.setVisibility(View.GONE);
                    llImageTransfer.setVisibility(View.GONE);
                    tvBankTransfer.setVisibility(View.GONE);
                    tvAccountNo.setVisibility(View.GONE);
                    tvAccountNameTransfer.setVisibility(View.GONE);
                    tvTransferTo.setVisibility(View.GONE);
                    etBankTransfer.setVisibility(View.GONE);
                    etAccountNo.setVisibility(View.GONE);
                    etAccountNameTransfer.setVisibility(View.GONE);
                    etTransferTo.setVisibility(View.GONE);
                    tvTotalTransfer.setVisibility(View.GONE);
                    etTotalTransfer.setVisibility(View.GONE);
                    imgTransferUpload.setVisibility(View.GONE);
                    tVBuktiTrf.setVisibility(View.GONE);
                } else {
                    llContentTransfer.setVisibility(View.VISIBLE);
                    llImageTransfer.setVisibility(View.VISIBLE);
                    tvBankTransfer.setVisibility(View.VISIBLE);
                    tvAccountNo.setVisibility(View.VISIBLE);
                    tvAccountNameTransfer.setVisibility(View.VISIBLE);
                    tvTransferTo.setVisibility(View.VISIBLE);
                    etBankTransfer.setVisibility(View.VISIBLE);
                    etAccountNo.setVisibility(View.VISIBLE);
                    etAccountNameTransfer.setVisibility(View.VISIBLE);
                    etTransferTo.setVisibility(View.VISIBLE);
                    tvTotalTransfer.setVisibility(View.VISIBLE);
                    etTotalTransfer.setVisibility(View.VISIBLE);
                    imgTransferUpload.setVisibility(View.VISIBLE);
                    tVBuktiTrf.setVisibility(View.VISIBLE);
                }
            }
        });

        llKontrabon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(llContentKontraBon.isShown() || llImageKontraBon.isShown() || tvDueDateKontraBon.isShown() || tvTotalKontraBon.isShown() || etDueDateKontraBon.isShown() || etTotalKontraBon.isShown()){
                    llContentKontraBon.setVisibility(View.GONE);
                    llImageKontraBon.setVisibility(View.GONE);
                    tvDueDateKontraBon.setVisibility(View.GONE);
                    tvTotalKontraBon.setVisibility(View.GONE);
                    etDueDateKontraBon.setVisibility(View.GONE);
                    etTotalKontraBon.setVisibility(View.GONE);
                    imgKontraBonUpload.setVisibility(View.GONE);
                } else {
                    llContentKontraBon.setVisibility(View.VISIBLE);
                    llImageKontraBon.setVisibility(View.VISIBLE);
                    tvDueDateKontraBon.setVisibility(View.VISIBLE);
                    tvTotalKontraBon.setVisibility(View.GONE);
                    etDueDateKontraBon.setVisibility(View.VISIBLE);
                    etTotalKontraBon.setVisibility(View.GONE);
                    imgKontraBonUpload.setVisibility(View.VISIBLE);
                }
            }
        });
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

    /**
     * show dialog to select source of image
     */
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Pilih dari galery",
                "Ambil dari kamera"};
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    private String validateTotalValue(int total) {
        return total == 0 ? "" : String.valueOf(total);
    }

    /**
     * choose image from gallery
     */
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    /**
     * choose image from camera
     */
    private void takePhotoFromCamera()  {
        /*Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath);
        startActivityForResult(intent, CAMERA);*/
        dispatchTakePictureIntent();
    }

    /**
     *
     * @param dp
     * @return
     */
    private int dpToPx(int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    /**
     * update layout of page,
     * set image summary and image product competitor if exist
     * @param post
     * @param bitmap
     * @param methode
     * @param image
     */
    private void mainLayout(int post, final Bitmap bitmap, final int methode, final String image){
        final LinearLayout parent = new LinearLayout(context);
        parent.setOrientation(LinearLayout.VERTICAL);
        parent.setGravity(Gravity.CENTER_HORIZONTAL);

        LinearLayout.LayoutParams paramParent = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramParent.setMargins(0, 10, 0, 0);
        parent.setLayoutParams(paramParent);

        ImageView imageView = new ImageView(context);
        imageView.setId(post);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(150, 150));
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.showImage(context,bitmap);
                // showImage(bitmap);
            }
        });
        parent.addView(imageView);

        Button btnClear = new Button(context);

        int width  = dpToPx(40);
        int height = dpToPx(25);
        LinearLayout.LayoutParams paramButton = new LinearLayout.LayoutParams(width, height);
        paramButton.setMargins(0, 20, 0, 20);
        btnClear.setLayoutParams(paramButton);

        btnClear.setBackground(context.getResources().getDrawable(R.drawable.bg_red));
        btnClear.setText("Clear");
        btnClear.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        btnClear.setTextColor(context.getResources().getColor(R.color.white));
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogClear(methode, parent, image);
            }
        });
        parent.addView(btnClear);
        if(methode==1){
            llImageGiro.addView(parent);
        } else if(methode==2){
            llImageCheque.addView(parent);
        } else if(methode==3){
            llImageTransfer.addView(parent);
        } else if(methode==4){
            llImageKontraBon.addView(parent);
        }
    }

    /**
     * update layout based on method payment
     * @param bitmap
     * @param methode
     */
    private void updateLayout(Bitmap bitmap, final int methode){
        final String savedImage = saveImage(bitmap);

        if(methode==1){ // giro
            int post = countImageGiro;
            llImageGiro.setVisibility(View.VISIBLE);
            mainLayout(post, bitmap, methode, savedImage);
            listPhotoGiro.add(savedImage);
            countImageGiro++;

        } else if(methode==2){ // cheque
            int post = countImageCheque;
            llImageCheque.setVisibility(View.VISIBLE);
            mainLayout(post, bitmap, methode, savedImage);
            listPhotoCheque.add(savedImage);
            countImageCheque++;

        } else if(methode==3){ // transfer
            int post = countImageTransfer;
            llImageTransfer.setVisibility(View.VISIBLE);
            mainLayout(post, bitmap, methode, savedImage);
            listPhotoTransfer.add(savedImage);
            countImageTransfer++;

        } else if(methode==4){ // kontrabon
            int post = countImageKontraBon;
            llImageKontraBon.setVisibility(View.VISIBLE);
            mainLayout(post, bitmap, methode, savedImage);
            listPhotoKontraBon.add(savedImage);
            countImageKontraBon++;

        }
    }

    /**
     * show popup clear image
     * @param methode
     * @param layout
     * @param image
     */
    private void showDialogClear(final int methode, final LinearLayout layout, final String image){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Clear");

        alertDialogBuilder
                .setMessage("Hapus Photo ?")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        if(methode==1){
                            layout.removeAllViews();
                            layout.setVisibility(View.GONE);

                            int postDelete = listPhotoGiro.indexOf(image);
                            listPhotoGiro.remove(postDelete);
                            countImageGiro--;

                        } else if(methode==2){
                            layout.removeAllViews();
                            layout.setVisibility(View.GONE);

                            int postDelete = listPhotoCheque.indexOf(image);
                            listPhotoCheque.remove(postDelete);
                            countImageCheque--;

                        } else if(methode==3){
                            layout.removeAllViews();
                            layout.setVisibility(View.GONE);

                            int postDelete = listPhotoTransfer.indexOf(image);
                            listPhotoTransfer.remove(postDelete);
                            countImageTransfer--;

                        } else if(methode==4){
                            layout.removeAllViews();
                            layout.setVisibility(View.GONE);

                            int postDelete = listPhotoKontraBon.indexOf(image);
                            listPhotoKontraBon.remove(postDelete);
                            countImageKontraBon--;

                        }
                    }
                })
                .setNegativeButton("Tidak",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        updateLayout(bitmap, takeImage);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toasty.info(context, "Failed!").show();
                    }
                }
            }
        } else if (requestCode == CAMERA) {
            if (resultCode == RESULT_OK) {
                try{
                    Bitmap fullImage = ImagePicker.getImageFromResult(this, resultCode, data, mCurrentPhotoPath);
                    updateLayout(fullImage, takeImage);
                } catch (Exception e){
                    Toasty.info(context, "Gagal Mengambil Gambar, silahkan ulangi").show();
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * create image file
     * @return File
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * take image from camera
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                photoFile = null;
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                try{
                    Uri photoURI = FileProvider.getUriForFile(this,
                            "id.co.beton.saleslogistic_trackingsystem.fileprovider",
                            photoFile);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                    startActivityForResult(takePictureIntent, CAMERA);
                } catch (ActivityNotFoundException e){
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * save image to internal storage
     * @param myBitmap
     * @return String image path
     */
    public String saveImage(Bitmap myBitmap) {
        Bitmap newBimpap = scaleDown(myBitmap, Constants.MAX_SIZE_IMAGE, false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        newBimpap.compress(Bitmap.CompressFormat.JPEG, Constants.MAX_SIZE_QUALITY_IMAGE, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            if (Constants.DEBUG) {
                Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            }

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    /**
     * scaled down Bitmap
     * @param realImage
     * @param maxImageSize
     * @param filter
     * @return
     */
    public static Bitmap scaleDown(Bitmap realImage, float maxImageSize,
                                   boolean filter) {
        float ratio = Math.min(
                (float) maxImageSize / realImage.getWidth(),
                (float) maxImageSize / realImage.getHeight());
        int width = Math.round((float) ratio * realImage.getWidth());
        int height = Math.round((float) ratio * realImage.getHeight());

        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, width,
                height, filter);
        return newBitmap;
    }

    /**
     * show calender dialog
     * @param type
     */
    private void showCalender(final int type) {
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(type);
            }
        };
        new DatePickerDialog(context, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    /**
     * update label of date
     * @param type
     */
    private void updateLabel(int type) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(type == 0){
            etDueDateCheque.setText(sdf.format(myCalendar.getTime()));
        } else if (type == 1) {
            etDueDateGiro.setText(sdf.format(myCalendar.getTime()));
        } else if (type == 2){
            etDueDateKontraBon.setText(sdf.format(myCalendar.getTime()));
        }
    }
}
