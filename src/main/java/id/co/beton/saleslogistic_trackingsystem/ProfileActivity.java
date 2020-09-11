package id.co.beton.saleslogistic_trackingsystem;

import android.app.AlertDialog;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Profile;

import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class ProfileActivity
 */
public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = ProfileActivity.class.getSimpleName();

    private Context context;
    DialogCustom dialogCustom;
    private TextView tvUserCode,tvUserName,tvBranch,tvDivision,tvChangePassword;
    private ImageView arrowRight;
    private CircleImageView profilePicture;
    private static final String IMAGE_DIRECTORY = "/tracking-system";
    private int GALLERY = 1, CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Back");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context =this;

        dialogCustom = new DialogCustom(context);

        tvUserCode = (TextView) findViewById(R.id.tv_user_code);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvBranch = (TextView) findViewById(R.id.tv_branch);
        tvDivision = (TextView) findViewById(R.id.tv_division);
        /*tvChangePassword = (TextView) findViewById(R.id.tv_change_password);

        tvChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswod();
            }
        });
        arrowRight  =(ImageView) findViewById(R.id.btn_arrow_right);
        arrowRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changePasswod();
            }
        });*/

        profilePicture = (CircleImageView) findViewById(R.id.iv_profile_picture);
        /*profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPictureDialog();
            }
        });*/

        loadData();
    }

    /**
     * function to get data profile from backend
     * data stored in Class Profile
     */
    private void loadData() {
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(this);

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.profile("JWT " + userUtil.getJWTTOken(), userUtil.getUsername());

        /*Log the URL called*/
        if(Constants.DEBUG){
            Log.wtf("URL Called", call.request().url() + "");
        }

        /*Loading*/
        dialogCustom.show();

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    Gson gson =new Gson();
                    Profile profile = gson.fromJson(response.body().getData().toString(), Profile.class);

                    if(Constants.DEBUG){
                        Log.wtf(TAG, profile.toString());
                    }

                    tvUserCode.setText(profile.getNip());
                    tvUserName.setText(profile.getUsername());
                    tvBranch.setText(profile.getBranch().getName());
                    tvDivision.setText(profile.getDivision().getDivisionName());

                    dialogCustom.hidden();
                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(getBaseContext(), jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }catch (Exception e){
                        Toasty.error(getBaseContext(),"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                dialogCustom.hidden();
                Toasty.error(context,"Terjadi kesalahan server").show();
            }
        });
    }

    /**
     * function to show selection image,
     * whether from capture camera or select from gallery
     */
    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
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
     * function to get photo from gallery
     */
    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    /**
     * function to get photo from camera
     */
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    /**
     * function to get result from activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY && resultCode==RESULT_OK) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    Toasty.info(ProfileActivity.this ,"Image Saved!").show();
                    profilePicture.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toasty.info(ProfileActivity.this ,"Failed!").show();
                }
            }

        } else if (requestCode == CAMERA) {
            if(resultCode==RESULT_OK){
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                profilePicture.setImageBitmap(thumbnail);
                saveImage(thumbnail);
                Toasty.info(ProfileActivity.this ,"Image Saved!").show();
            }
        }
    }

    /**
     * function to scaledown/resize image
     * @param realImage
     * @param maxImageSize
     * @param filter
     * @return image Bitmap
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
     * function to save image to internal storage
     * @param myBitmap
     * @return
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
     * function onOptionsItemSelected
     * @param item
     * @return boolean
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * function to call activity for change password user
     */
    private void changePasswod(){
        Intent intent = new Intent(this,ChangePsswordActivity.class);
        startActivity(intent);
    }

    /**
     * function onCreateOptionsMenu
     * @param menu
     * @return boolean
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
