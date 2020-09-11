package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Invoice;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import es.dmoral.toasty.Toasty;

/**
 * Class PaymentMethodActivity
 *
 */
public class PaymentMethodActivity extends AppCompatActivity {
    private LinearLayout llCash,llGiro,llCheque,llTransfer;
    private LinearLayout llContentllGiro,llContentChaque,llContentTransfer;
    private TextView tvAccountNameGiro,tvGiroNo,tvBankGiro,tvDueDateGiro;
    private EditText etAccountNameGiro,etGiroNo,etBankGiro,etDueDateGiro;
    private TextView tvAccountNameCheque,tvChequeNo,tvBankCheque,tvDueDateCheque;
    private EditText etAccountNameCheque,etChequeNo,etBankCheque,etDueDateCheque;
    private TextView tvBankTransfer,tvAccountNo,tvAccountNameTransfer,tvTransferTo;
    private EditText etBankTransfer,etAccountNo,etAccountNameTransfer,etTransferTo;
    private Button btnGiro, btnCheque,btnTransfer;
    private Animation animationUp;
    private Animation animationDown;
    private static final String TAG = PaymentMethodActivity.class.getSimpleName();
    String customerName,customerCode;
    private Context context;
    ImageView imgUpload;
    private static final String IMAGE_DIRECTORY = "/tracking-system";
    private int GALLERY = 100, CAMERA = 200;
    private String realPath;
    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_method);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        customerCode = getIntent().getStringExtra("cust_code");
        customerName = getIntent().getStringExtra("cust_name");
        context =this;

        setTitle(customerName);
        myCalendar = Calendar.getInstance();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Cash
        llCash = (LinearLayout)findViewById(R.id.ll_cash);
        imgUpload = (ImageView)findViewById(R.id.img_bukti_trf);
        imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });

        //giro
        llGiro = (LinearLayout)findViewById(R.id.ll_giro);
        llContentllGiro = (LinearLayout)findViewById(R.id.ll_content_giro);
        tvAccountNameGiro = (TextView)findViewById(R.id.tv_account_name_giro);
        tvGiroNo = (TextView)findViewById(R.id.tv_giro_no);
        tvBankGiro = (TextView)findViewById(R.id.tv_bank_giro);
        tvDueDateGiro = (TextView)findViewById(R.id.tv_due_date_giro);
        etAccountNameGiro = (EditText)findViewById(R.id.et_account_name_giro);
        etGiroNo = (EditText)findViewById(R.id.et_giro_no);
        etBankGiro = (EditText)findViewById(R.id.et_bank_giro);
        etDueDateGiro = (EditText)findViewById(R.id.et_due_date_giro);
        btnGiro = (Button)findViewById(R.id.btn_submit_giro);

        etDueDateGiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(1);
            }
        });

        btnGiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etAccountNameGiro.getText().toString().length()==0 ||
                        etBankGiro.getText().toString().length()==0 ||
                        etGiroNo.getText().toString().length()==0){
                    Toasty.info(context,"Silahkan lengkapi form giro").show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("payment_method","giro");
                intent.putExtra("account_name",etAccountNameGiro.getText().toString()+"");
                intent.putExtra("bank_name",etBankGiro.getText().toString()+"");
                intent.putExtra("account_no",etGiroNo.getText().toString()+"");
                intent.putExtra("transfer_to","");
                intent.putExtra("due_date",etDueDateGiro.getText().toString()+"");
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        //cek
        llCheque = (LinearLayout)findViewById(R.id.ll_cheque);
        llContentChaque = (LinearLayout)findViewById(R.id.ll_content_cheque);
        tvAccountNameCheque = (TextView)findViewById(R.id.tv_account_name_cheque);
        tvChequeNo = (TextView)findViewById(R.id.tv_chaque_no);
        tvBankCheque = (TextView)findViewById(R.id.tv_bank_cheque);
        tvDueDateCheque = (TextView)findViewById(R.id.tv_due_date_cheque);
        etAccountNameCheque = (EditText)findViewById(R.id.et_account_name_cheque);
        etChequeNo = (EditText)findViewById(R.id.et_chaque_no);
        etBankCheque = (EditText)findViewById(R.id.et_bank_cheque);
        etDueDateCheque = (EditText)findViewById(R.id.et_due_date_cheque);
        btnCheque = (Button)findViewById(R.id.btn_submit_chaque);

        etDueDateCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCalender(0);
            }
        });

        btnCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etAccountNameCheque.getText().toString().length()==0 ||
                        etBankCheque.getText().toString().length()==0 ||
                        etChequeNo.getText().toString().length()==0){
                    Toasty.info(context,"Silahkan lengkapi form Cheque").show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("payment_method","cheque");
                intent.putExtra("account_name",etAccountNameCheque.getText().toString()+"");
                intent.putExtra("bank_name",etBankCheque.getText().toString()+"");
                intent.putExtra("account_no",etChequeNo.getText().toString()+"");
                intent.putExtra("transfer_to","");
                intent.putExtra("due_date",etDueDateCheque.getText().toString()+"");
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        //bank transfer
        llTransfer = (LinearLayout)findViewById(R.id.ll_transsfer);
        llContentTransfer = (LinearLayout)findViewById(R.id.ll_content_bank_transfer);
        tvBankTransfer = (TextView)findViewById(R.id.tv_bank_name_transfer);
        tvAccountNo = (TextView)findViewById(R.id.tv_account_no_transfer);
        tvAccountNameTransfer = (TextView)findViewById(R.id.tv_account_name_transfer);
        tvTransferTo = (TextView)findViewById(R.id.tv_transfer_to_account);
        etBankTransfer = (EditText)findViewById(R.id.et_bank_name_transfer);
        etAccountNo = (EditText)findViewById(R.id.et_account_no_transfer);
        etAccountNameTransfer = (EditText)findViewById(R.id.et_account_name_transfer);
        etTransferTo = (EditText)findViewById(R.id.et_transfer_to_account);
        btnTransfer = (Button)findViewById(R.id.btn_submit_transfer);
        btnTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etAccountNameTransfer.getText().toString().length()==0 ||
                        etBankTransfer.getText().toString().length()==0 ||
                        etAccountNo.getText().toString().length()==0){
                    Toasty.info(context,"Silahkan lengkapi form transfer").show();
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("payment_method","transfer");
                intent.putExtra("account_name",etAccountNameTransfer.getText().toString()+"");
                intent.putExtra("bank_name",etBankTransfer.getText().toString()+"");
                intent.putExtra("account_no",etAccountNo.getText().toString()+"");
                intent.putExtra("transfer_to",etTransferTo.getText().toString()+"");
                intent.putExtra("due_date","");
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        llContentllGiro.setVisibility(View.GONE);
        tvAccountNameGiro.setVisibility(View.GONE);
        tvGiroNo.setVisibility(View.GONE);
        tvBankGiro.setVisibility(View.GONE);
        tvDueDateGiro.setVisibility(View.GONE);
        etAccountNameGiro.setVisibility(View.GONE);
        etGiroNo.setVisibility(View.GONE);
        etBankGiro.setVisibility(View.GONE);
        etDueDateGiro.setVisibility(View.GONE);
        btnGiro.setVisibility(View.GONE);

        llContentChaque.setVisibility(View.GONE);
        tvAccountNameCheque.setVisibility(View.GONE);
        tvChequeNo.setVisibility(View.GONE);
        tvBankCheque.setVisibility(View.GONE);
        tvDueDateCheque.setVisibility(View.GONE);
        etAccountNameCheque.setVisibility(View.GONE);
        etChequeNo.setVisibility(View.GONE);
        etBankCheque.setVisibility(View.GONE);
        etDueDateCheque.setVisibility(View.GONE);
        btnCheque.setVisibility(View.GONE);

        llContentTransfer.setVisibility(View.GONE);
        tvBankTransfer.setVisibility(View.GONE);
        tvAccountNo.setVisibility(View.GONE);
        tvAccountNameTransfer.setVisibility(View.GONE);
        tvTransferTo.setVisibility(View.GONE);
        etBankTransfer.setVisibility(View.GONE);
        etAccountNo.setVisibility(View.GONE);
        etAccountNameTransfer.setVisibility(View.GONE);
        etTransferTo.setVisibility(View.GONE);
        btnTransfer.setVisibility(View.GONE);



        animationUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        animationDown = AnimationUtils.loadAnimation(this, R.anim.slide_down);

        llCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("payment_method","cash");
                setResult(Activity.RESULT_OK,intent);
                finish();
            }
        });

        llGiro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llContentllGiro.isShown()||tvAccountNameGiro.isShown()||tvGiroNo.isShown()||tvBankGiro.isShown()||tvDueDateGiro.isShown()||etAccountNameGiro.isShown()||etGiroNo.isShown()||etBankGiro.isShown()||etDueDateGiro.isShown()||btnGiro.isShown()){
                    llContentllGiro.setVisibility(View.GONE);
                    tvAccountNameGiro.setVisibility(View.GONE);
                    tvGiroNo.setVisibility(View.GONE);
                    tvBankGiro.setVisibility(View.GONE);
                    tvDueDateGiro.setVisibility(View.GONE);
                    etAccountNameGiro.setVisibility(View.GONE);
                    etGiroNo.setVisibility(View.GONE);
                    etBankGiro.setVisibility(View.GONE);
                    etDueDateGiro.setVisibility(View.GONE);
                    btnGiro.setVisibility(View.GONE);
                }
                else{
                    llContentllGiro.setVisibility(View.VISIBLE);
                    tvAccountNameGiro.setVisibility(View.VISIBLE);
                    tvGiroNo.setVisibility(View.VISIBLE);
                    tvBankGiro.setVisibility(View.VISIBLE);
                    tvDueDateGiro.setVisibility(View.VISIBLE);
                    etAccountNameGiro.setVisibility(View.VISIBLE);
                    etGiroNo.setVisibility(View.VISIBLE);
                    etBankGiro.setVisibility(View.VISIBLE);
                    etDueDateGiro.setVisibility(View.VISIBLE);
                    btnGiro.setVisibility(View.VISIBLE);
                }
            }
        });

        llCheque.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llContentChaque.isShown()||tvAccountNameCheque.isShown()||tvChequeNo.isShown()||tvBankCheque.isShown()||tvDueDateCheque.isShown()||etAccountNameCheque.isShown()||etChequeNo.isShown()||etBankCheque.isShown()||etDueDateCheque.isShown()||btnCheque.isShown()){
                    llContentChaque.setVisibility(View.GONE);
                    tvAccountNameCheque.setVisibility(View.GONE);
                    tvChequeNo.setVisibility(View.GONE);
                    tvBankCheque.setVisibility(View.GONE);
                    tvDueDateCheque.setVisibility(View.GONE);
                    etAccountNameCheque.setVisibility(View.GONE);
                    etChequeNo.setVisibility(View.GONE);
                    etBankCheque.setVisibility(View.GONE);
                    etDueDateCheque.setVisibility(View.GONE);
                    btnCheque.setVisibility(View.GONE);
                }
                else{
                    llContentChaque.setVisibility(View.VISIBLE);
                    tvAccountNameCheque.setVisibility(View.VISIBLE);
                    tvChequeNo.setVisibility(View.VISIBLE);
                    tvBankCheque.setVisibility(View.VISIBLE);
                    tvDueDateCheque.setVisibility(View.VISIBLE);
                    etAccountNameCheque.setVisibility(View.VISIBLE);
                    etChequeNo.setVisibility(View.VISIBLE);
                    etBankCheque.setVisibility(View.VISIBLE);
                    etDueDateCheque.setVisibility(View.VISIBLE);
                    btnCheque.setVisibility(View.VISIBLE);
                }
            }
        });

        llTransfer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(llContentTransfer.isShown()||tvBankTransfer.isShown()||tvAccountNo.isShown()||tvAccountNameTransfer.isShown()||tvTransferTo.isShown()||etBankTransfer.isShown()||etAccountNo.isShown()||etAccountNameTransfer.isShown()||etTransferTo.isShown()||btnTransfer.isShown()){
                    llContentTransfer.setVisibility(View.GONE);
                    tvBankTransfer.setVisibility(View.GONE);
                    tvAccountNo.setVisibility(View.GONE);
                    tvAccountNameTransfer.setVisibility(View.GONE);
                    tvTransferTo.setVisibility(View.GONE);
                    etBankTransfer.setVisibility(View.GONE);
                    etAccountNo.setVisibility(View.GONE);
                    etAccountNameTransfer.setVisibility(View.GONE);
                    etTransferTo.setVisibility(View.GONE);
                    btnTransfer.setVisibility(View.GONE);
                }
                else{
                    llContentTransfer.setVisibility(View.VISIBLE);
                    tvBankTransfer.setVisibility(View.VISIBLE);
                    tvAccountNo.setVisibility(View.VISIBLE);
                    tvAccountNameTransfer.setVisibility(View.VISIBLE);
                    tvTransferTo.setVisibility(View.VISIBLE);
                    etBankTransfer.setVisibility(View.VISIBLE);
                    etAccountNo.setVisibility(View.VISIBLE);
                    etAccountNameTransfer.setVisibility(View.VISIBLE);
                    etTransferTo.setVisibility(View.VISIBLE);
                    btnTransfer.setVisibility(View.VISIBLE);
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
     * show dialog to choose source of image
     */
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Pilih dari galery",
                "Ambil dari kamera" };
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
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY) {
            if(resultCode==RESULT_OK){
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                        realPath = saveImage(bitmap);
                        Toasty.info(context ,"Image Saved!").show();
                        imgUpload.setImageBitmap(bitmap);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toasty.info(context ,"Failed!").show();
                    }
                }

            }
        } else if (requestCode == CAMERA) {
            if(resultCode==RESULT_OK){
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                imgUpload.setImageBitmap(thumbnail);
                realPath = saveImage(thumbnail);
                Toasty.info(context ,"Image Saved!").show();
            }

        }
    }

    /**
     * save image
     * @param myBitmap
     * @return String path image
     */
    public String saveImage(Bitmap myBitmap) {
        Bitmap newBimpap = scaleDown(myBitmap,500,false);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        newBimpap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
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
            if(Constants.DEBUG){
                Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            }

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    /**
     * scale down image
     * @param realImage
     * @param maxImageSize
     * @param filter
     * @return Scaled image
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
    private void showCalender(final int type){
        date = new DatePickerDialog.OnDateSetListener()  {
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
     * update label
     * @param type
     */
    private void updateLabel(int type) {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        if(type==1){
            etDueDateGiro.setText(sdf.format(myCalendar.getTime()));
        }else{
            etDueDateCheque.setText(sdf.format(myCalendar.getTime()));
        }

    }
}
