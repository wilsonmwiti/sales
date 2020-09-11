package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Order;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.order.NewRequestOrderAdapter;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Address.AddressesActivity;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Pic.PICActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Product;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.ImagePicker;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class NewRequestOrderActivity
 * add new data request order
 */
public class NewRequestOrderActivity extends AppCompatActivity {
    Button btnSumbmit, btnAdd;
    LinearLayout lrOrderBy, lrDeliveryAddress;
    private static final String TAG = NewRequestOrderActivity.class.getSimpleName();
    NewRequestOrderAdapter ProductAdapter;
    private Product product;
    private ArrayList<Product> products;
    private ListView lvNewRequestOrder;
    DialogCustom dialogCustom;
    boolean flag=false;
    String customerCode;
    String customerName;
    String mCurrentPhotoPath;

    ImageView imgsepecial,imgUpload;
    private TextView tvTanggal,tvRoORder,tvOrderBy,tvDeliveryAddress;
    private Context context;
    private static final int requestContact = 0;
    private static final int requestAddress = 1;
    String contacts="";
    String address="";

    private static final String IMAGE_DIRECTORY = "/tracking-system";
    private int GALLERY = 100, CAMERA = 200;
    private String realPath;
    private double lat=0,lng=0;

    private EditText etDetailPengiriman;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_request_order);

        context =this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try{
            customerCode = getIntent().getStringExtra("cust_code");
            customerName = getIntent().getStringExtra("cust_name");
            dialogCustom = new DialogCustom(context);

            setTitle(customerName);

            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);

            btnSumbmit = (Button)findViewById(R.id.btn_submit_order);
            btnSumbmit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogCustom.show();
                    postDataServer();
                }
            });
            // List
            LayoutInflater inflater = LayoutInflater.from(this);
            lvNewRequestOrder = (ListView)findViewById(R.id.lv_new_request_order);
            View header = inflater.inflate(R.layout.header_new_request_order,null);

            etDetailPengiriman = (EditText) header.findViewById(R.id.et_detail_pengiriman);
            tvTanggal = (TextView) header.findViewById(R.id.tv_tanggal);
            tvRoORder = (TextView) header.findViewById(R.id.tv_ro_order);
            tvOrderBy = (TextView) header.findViewById(R.id.tv_order_by);
            tvDeliveryAddress = (TextView) header.findViewById(R.id.tv_delivery_address);

            //tvOrderBy.setText(contacts);
            //tvDeliveryAddress.setText(address);
            tvTanggal.setText(ConversionDate.getInstance().today());
            tvRoORder.setText("-");

            lvNewRequestOrder.addHeaderView(header);

            products =new ArrayList<>();
            /*for (int i=1;i<=1;i++){
                Product product=new Product();
                product.setBrandName("asd");
                product.setQuantity(0);

                products.add(product);
            }*/

            ProductAdapter=new NewRequestOrderAdapter(this,1,products);
            lvNewRequestOrder.setAdapter(ProductAdapter);
            lvNewRequestOrder.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
            lvNewRequestOrder.setItemsCanFocus(true);

            View footer = inflater.inflate(R.layout.footer_new_request_order,null);

            lvNewRequestOrder.addFooterView(footer);

            btnAdd = (Button)footer.findViewById(R.id.btn_add);
            imgUpload = (ImageView) footer.findViewById(R.id.img_upload);

            btnAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lvNewRequestOrder.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
                    Product product = new Product();
                    product.setQuantity(0);
                    products.add(product);
                    ProductAdapter.notifyDataSetChanged();
                }
            });

            imgUpload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showPictureDialog();
                }
            });


            lrOrderBy = (LinearLayout)findViewById(R.id.rl_order_by);
            lrDeliveryAddress = (LinearLayout)findViewById(R.id.rl_delivery_address);
            imgsepecial = (ImageView)findViewById(R.id.img_set_special);

            imgsepecial.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!flag) {
                        imgsepecial.setBackgroundResource(R.drawable.bg_set_special_green);
                        flag=true;
                    }else {
                        imgsepecial.setBackgroundResource(R.drawable.bg_set_special_white);
                        flag=false;
                    }
                }
            });


            lrOrderBy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NewRequestOrderActivity.this, PICActivity.class);
                    intent.putExtra("cust_name",customerName);
                    intent.putExtra("cust_code",customerCode);
                    startActivityForResult(intent,requestContact);
                }
            });

            lrDeliveryAddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(NewRequestOrderActivity.this, AddressesActivity.class);
                    intent.putExtra("cust_name",customerName);
                    intent.putExtra("cust_code",customerCode);
                    startActivityForResult(intent,requestAddress);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == requestContact && resultCode == Activity.RESULT_OK) {
            contacts = data.getStringExtra("detail_contact");
            tvOrderBy.setText(contacts);
        }else if(requestCode==requestAddress && resultCode==Activity.RESULT_OK){
            address = data.getStringExtra("detail_address");
            lat = data.getDoubleExtra("lat",0);
            lng = data.getDoubleExtra("lng",0);
            tvDeliveryAddress.setText(address);
        }else if (requestCode == GALLERY) {
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
                // Bitmap fullImage = BitmapFactory.decodeFile(mCurrentPhotoPath);
                Bitmap fullImage = ImagePicker.getImageFromResult(this, resultCode, data, mCurrentPhotoPath);
                imgUpload.setImageBitmap(fullImage);
                realPath = saveImage(fullImage);
            }

        }
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

    private void takePhotoFromCamera() {
//        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        startActivityForResult(intent, CAMERA);
        dispatchTakePictureIntent();
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

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "id.co.beton.saleslogistic_trackingsystem.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA);
            }
        }
    }

    /**
     * Create image File
     * @return Image
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
     * scale down image
     * @param realImage
     * @param maxImageSize
     * @param filter
     * @return Scaled Image
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
     * save image to internal storage
     * @param myBitmap
     * @return String Path Image
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
     * convert string path image to encoded image
     * @param path
     * @return
     */
    private String convertImageTo64(String path){
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
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
     * send data to server
     */
    private void postDataServer(){
        try{
            btnSumbmit.setEnabled(false);
            if((contacts==null || contacts.isEmpty()) || (address==null || address.isEmpty())){
                Toasty.error(context,"Silahkan pilih PIC dan alamat pengiriman terlebih dahulu").show();
                btnSumbmit.setEnabled(true);
                dialogCustom.hidden();
                return;
            }

            UserUtil userUtil = UserUtil.getInstance(getApplicationContext());

        /*Create handle for the RetrofitInstance interface*/
            ApiInterface service = ApiClient.getInstance(context);

            JsonObject jsonObject =new JsonObject();
            jsonObject.addProperty("customer_code",customerCode);
            if(flag){
                jsonObject.addProperty("is_special_order",1);
            }else{
                jsonObject.addProperty("is_special_order",0);
            }
            jsonObject.addProperty("contacts",contacts);
            jsonObject.addProperty("delivery_address",address);
            jsonObject.addProperty("lat",lat);
            jsonObject.addProperty("lng",lng);
            if(etDetailPengiriman.getText()!=null && etDetailPengiriman.getText().length()>0){
                jsonObject.addProperty("notes",etDetailPengiriman.getText().toString());
            }else {
                jsonObject.addProperty("notes","");
            }
            JsonArray jsonElements = new JsonArray();
            for (int i=0; i<products.size();i++){
                if(products.get(i).getQuantity()!=0 && products.get(i).getQuantity()>0){
                    JsonObject dataProd = new JsonObject();
                    dataProd.addProperty("item_name",products.get(i).getBrandName());
                    dataProd.addProperty("qty",products.get(i).getQuantity());
                    jsonElements.add(dataProd);
                }
            }

            jsonObject.add("product",jsonElements);
            if(realPath!=null && realPath.length()>0){
                jsonObject.addProperty("files",convertImageTo64(realPath));
            }else{
                jsonObject.addProperty("files","");
            }
            if(Constants.DEBUG){

                Log.i(TAG,"parameter sales request"+jsonObject.toString());
            }
        /*Call the method with parameter in the interface to get the data*/
            Call<ResponseArrayObject> call = service.salesRequestPost("JWT "+userUtil.getJWTTOken(),jsonObject);
            call.enqueue(new Callback<ResponseArrayObject>() {
                @Override
                public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                    if(response.body()!=null && response.body().getError()==0){
                        Toasty.info(context,"Sukses save").show();
                        dialogCustom.hidden();
                        btnSumbmit.setEnabled(true);
                        setResult(Activity.RESULT_OK,null);
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                    btnSumbmit.setEnabled(true);
                    Toasty.error(context,t.getMessage()).show();
                    Log.e(TAG,t.getMessage().toString());
                    dialogCustom.hidden();
                    Toasty.error(context,"Terjadi kesalahan server").show();
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
