package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Memo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;

import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.model.Line;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.DetailPlanActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.Memo;
import id.co.beton.saleslogistic_trackingsystem.Model.Mymemo;
import id.co.beton.saleslogistic_trackingsystem.Model.Photo;
import id.co.beton.saleslogistic_trackingsystem.Model.User;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.ImagePicker;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.constraintlayout.widget.Constraints.TAG;
import static id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Report.ReportFragment.getReport_cetak;
import static id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Report.ReportFragment.getReport_lainnya;
import static id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Report.ReportFragment.getReport_lokasi;
import static id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Report.ReportFragment.getReport_toko_tutup;
import static id.co.beton.saleslogistic_trackingsystem.Configuration.Constants.API_KEY;
import static id.co.beton.saleslogistic_trackingsystem.Configuration.Constants.APPS;

/**
 * Class MemoFragment
 * to add visit/delivery summary
 */
public class MemoFragment extends Fragment {
    private Context context;

    private Destination destination;
    private PlanUtil planUtil;
//    private Memo memo;
    private Mymemo memo;

    private DialogCustom dialogCustom;
    private LinearLayout llImageCustomer, llCompetitor, llImageCompetitor;
    private LinearLayout llContentCompetitor, llIsNcOption;
    private CheckBox cbIsNC;
    private ScrollView llScrollParent;
    private NestedScrollView llScrollImage, llScrollCompetitor;
    private CheckBox cbCompetitor;
    private Button btnSubmitMemo, btnUpdateMemo ;
    private Spinner spn_category_visit;
    private EditText etMemo;
    private ImageView imgTakePhoto, imgTakePhotoCompetitor;

    private String customerCode, nfcCode, key_const_memo;
    private String mCurrentPhotoPath;
    private static final String IMAGE_DIRECTORY = "/tracking-system";
    private int GALLERY = 100, CAMERA = 200;

    private List<EditText> allEDs;
    private List<String> allImgs;

    String category_visit[];
    private static String category_visit_choose;

    private void setCategory_visit_choose(String category){
        this.category_visit_choose = category;
    }

    private static String getCategory_visit_choose(){
        return category_visit_choose;
    }

    private List<EditText> allEDCompetitor;
    private List<String> allImgCompetitor;

    private int imagePost;
    private int countPhotoCustomer = 0;
    private int countPhotoCompetitor = 0;
    private boolean isCompetitorExist = false;

    private JsonObject jsonObject;
    private JsonArray jsonElements;

    protected UserUtil userUtil;
    private ApiInterface service;
    protected Location location = null;
    private LocationManager mLocationManager = null;

    private ArrayAdapter<String> adapterCategoryVisit;

    private boolean IS_NC = false;

    private void setIS_NC(boolean b){
        this.IS_NC = b;
    }

    private boolean getIs_NC(){
        return IS_NC;
    }

    public MemoFragment() {
        // Required empty public constructor
    }

    public static MemoFragment newInstance(int tabSelected) {
        MemoFragment fragment = new MemoFragment();
        Bundle args = new Bundle();
        args.putInt("position", tabSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        context = container.getContext();
        userUtil = UserUtil.getInstance(context);
        planUtil = PlanUtil.getInstance(context);
        service = ApiClient.getInstance(context);

        final View view;
        /* jika sales pilih layout dengan kategori kunjungan */
        view = inflater.inflate(R.layout.content_detail_visit_plan_memo_sales, container, false);

        dialogCustom = new DialogCustom(context);

        customerCode = getArguments().getString("cust_code");

        Gson gson = new Gson();

        destination = gson.fromJson(getArguments().getString("destination"), new TypeToken<Destination>() {
        }.getType());

        nfcCode = userUtil.getStringProperty(Constants.NFC_CODE);

        location = getLastLocationService();

        allEDs   = new ArrayList<>();
        allImgs  = new ArrayList<>();
        allEDs.clear();
        allImgs.clear();

        allEDCompetitor  = new ArrayList<>();
        allImgCompetitor = new ArrayList<>();
        allEDCompetitor.clear();
        allImgCompetitor.clear();

        countPhotoCustomer = 0;
        countPhotoCompetitor = 0;

        llContentCompetitor = (LinearLayout) view.findViewById(R.id.ll_content_competitor);

        llScrollParent = (ScrollView) view.findViewById(R.id.scroll_parent);

        llIsNcOption = (LinearLayout) view.findViewById(R.id.ll_is_nc_option);
        cbIsNC = (CheckBox) view.findViewById(R.id.cb_is_nc);
        cbIsNC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIsNC();
            }
        });

        cbCompetitor = (CheckBox) view.findViewById(R.id.cb_select_competitor);
        cbCompetitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCompetitor();
            }
        });

        llScrollCompetitor = (NestedScrollView) view.findViewById(R.id.ll_scroll_competitor);
        llScrollCompetitor.setVisibility(View.GONE);

        llCompetitor = (LinearLayout) view.findViewById(R.id.ll_competitor);
        llCompetitor.setVisibility(View.GONE);

        llImageCompetitor = (LinearLayout) view.findViewById(R.id.ll_image_competitor);

        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            llContentCompetitor.setVisibility(View.GONE);
        }

        llScrollImage = (NestedScrollView) view.findViewById(R.id.ll_scroll_image);
        llScrollImage.setVisibility(View.GONE);

        llImageCustomer = (LinearLayout) view.findViewById(R.id.ll_image_customer);

        etMemo = (EditText) view.findViewById(R.id.et_memo);

        //get the spinner from the xml.
        category_visit = getResources().getStringArray(R.array.category_visit);

        spn_category_visit = (Spinner) view.findViewById(R.id.spn_category_visit);

        adapterCategoryVisit = new ArrayAdapter<String>(
                context, R.layout.support_simple_spinner_dropdown_item,category_visit
        );

        spn_category_visit.setAdapter(adapterCategoryVisit);
        spn_category_visit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String cat;
                switch (i){
                    case 1 : cat = "Cari info"; break;
                    case 2 : cat = "Telp proyek"; break;
                    case 3 : cat = "Prospecting"; break;
                    case 4 : cat = "Presentasi"; break;
                    case 5 : cat = "Menawarkan"; break;
                    case 6 : cat = "Follow up"; break;
                    case 7 : cat = "Negosiasi"; break;
                    case 8 : cat = "Confirm order"; break;
                    case 9 : cat = "Closing order"; break;
                    default: cat = ""; break;
                }
                setCategory_visit_choose(cat);
                if(!getCategory_visit_choose().equalsIgnoreCase("")){
                    cbIsNC.setChecked(false);
                    try{
                        if(memo.getNc() != null){

                            if(memo.getNc().equalsIgnoreCase("y")){
                                setIS_NC(true);
                                if( memo.getCategory_visit().equalsIgnoreCase(cat)){
                                    cbIsNC.setChecked(true);
                                }
                            }
                        }
                    }catch(Exception e){
                        Log.d("memo_nc", e.toString());
                    }
                    isNCOptionShow();
                }else{
                    isNCOptionHide();
                }
                checkIsNC();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        btnSubmitMemo = (Button) view.findViewById(R.id.btn_submit_memo);
        btnSubmitMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()) validateMemo(1);
            }
        });

        btnUpdateMemo = (Button) view.findViewById(R.id.btn_update_memo);
        btnUpdateMemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()) validateMemo(2);
            }
        });

        imgTakePhoto = (ImageView) view.findViewById(R.id.img_take_photo);
        imgTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation()){
                    if(countPhotoCustomer < Constants.MAX_COUNT_IMAGE_MEMO){
                        imagePost = 1;
                        // showPictureDialog();
                        takePhotoFromCamera();
                    } else {
                        Toasty.info(context, "Photo kunjungan sudah mencapai batas yang diizinkan").show();
                    }
                }
            }
        });

        imgTakePhotoCompetitor = (ImageView) view.findViewById(R.id.img_take_photo_competitor);
        imgTakePhotoCompetitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(checkValidasi()){
                if(validation()){
                    if(countPhotoCompetitor < Constants.MAX_COUNT_IMAGE_MEMO){
                        imagePost = 2;
                        // showPictureDialog();
                        takePhotoFromCamera();
                    } else {
                        Toasty.info(context, "Photo produk kompetitor sudah mencapai batas yang diizinkan").show();
                    }
                }
            }
        });

        loadDataMemo();

        return view;
    }

    private boolean validation(){
        /* validate category visit */
        /* if user is collector then allow to not choose category visit */
        if(getCategory_visit_choose() == null || getCategory_visit_choose().equals("")){
            Toasty.error(context, "Pilih tujuan visit").show();
            return false ;
        }

        if (nfcCode.equals(userUtil.getStringProperty(Constants.CURRENT_PAGE_PLAN))) {
            return true;
        } else {
            // check ke list visited customer
            ArrayList<String> set= userUtil.getArrayList(Constants.LIST_VISITED_CUSTOMER);
            if(set != null){
                if(set.size()>0){
                    List<String> listVisitedCustomer = new ArrayList<>(set);
                    System.out.println("listVisitedCustomer = " + listVisitedCustomer);
                    System.out.println("Current Page Plan = " + userUtil.getStringProperty(Constants.CURRENT_PAGE_PLAN));
                    if(listVisitedCustomer.contains(userUtil.getStringProperty(Constants.CURRENT_PAGE_PLAN))){
                        return true;
                    }
                }
            }

            Toasty.info(context, "Anda belum mengunjungi customer ini.").show();
            return false;
        }
    }

    private void isNCOptionShow(){
        llIsNcOption.setVisibility(View.VISIBLE);
    }

    private void isNCOptionHide(){
        llIsNcOption.setVisibility(View.GONE);
    }

    private int getCategory_visit_choose(String category){
        int c = 0;
        if(category.equalsIgnoreCase("Cari info")) c = 1;
        else if(category.equalsIgnoreCase("Telp proyek")) c = 2;
        else if(category.equalsIgnoreCase("Prospecting")) c = 3;
        else if(category.equalsIgnoreCase("Presentasi")) c = 4;
        else if(category.equalsIgnoreCase("Menawarkan")) c = 5;
        else if(category.equalsIgnoreCase("Follow up")) c = 6;
        else if(category.equalsIgnoreCase("Negosiasi")) c = 7;
        else if(category.equalsIgnoreCase("Confirm order")) c = 8;
        else if(category.equalsIgnoreCase("Closing order")) c = 9 ;

        return c;
    }

    private void checkCompetitor(){
        if(cbCompetitor.isChecked()){
            isCompetitorExist = true;
            llCompetitor.setVisibility(View.VISIBLE);
            llScrollParent.smoothScrollTo(0, llScrollParent.getBottom());
        } else {
            isCompetitorExist = false;
            llCompetitor.setVisibility(View.GONE);
        }
    }

    private void checkIsNC(){
        if(cbIsNC.isChecked()){
            setIS_NC(true);
            Log.d("NC", "set to be true");
        } else {
            setIS_NC(false);
            Log.d("NC", "set to false");
        }
    }

    private void validateMemo(int post){

        if(!etMemo.getText().toString().equals("")){
            if(allImgs.size()>0){
                if(isCompetitorExist){
                    if(allImgCompetitor.size()>0){
                        postMemo(post);
                    } else {
                        Toasty.info(context, "Ambil Photo Produk Kompetitor minimal 1 kali").show();
                    }
                } else {
                    if (userUtil.getRoleUser().equals(Constants.ROLE_SALES)) {
                        if(allImgCompetitor.size()>0){
                            // show dialog photo competitor exist
                            showDialogPost(1, post);
                        } else {
                            // show dialog competitor is not checked
                            showDialogPost(2, post);
                        }
                    } else {
                        postMemo(post);
                    }
                }
            } else {
                Toasty.info(context, "Ambil Photo Lokasi minimal 1 kali").show();
            }
        } else {
            Toasty.info(context, "Silahkan isi memo terlebih dahulu").show();
        }
    }

    private void postMemo(final int post){

        /*Loading*/
        dialogCustom.show();

        jsonObject = new JsonObject();
        /* custom function */
        jsonObject.addProperty("username", APPS);
        jsonObject.addProperty("api_key", API_KEY);
        jsonObject.addProperty("username_code", userUtil.getId());
        /* custom function end*/
        jsonObject.addProperty("plan_id", planUtil.getPlanId());
        jsonObject.addProperty("customer_code", customerCode);
        jsonObject.addProperty("notes", etMemo.getText().toString());
        jsonElements = new JsonArray();
        for(int l=0; l < countPhotoCustomer; l++){
            JsonObject imageVisit = new JsonObject();
            imageVisit.addProperty("desc", allEDs.get(l).getText().toString().replaceAll("\n", "\\\\n"));
            imageVisit.addProperty("image", convertImageTo64(allImgs.get(l)).replaceAll("\n","")); //convert image
            jsonElements.add(imageVisit);
        }
        jsonObject.add("visit_images", jsonElements);

        jsonElements = new JsonArray();
        if(isCompetitorExist){
            jsonObject.addProperty("have_competitor", 1);
            for(int i=0; i < countPhotoCompetitor; i++){
                JsonObject imageInfo = new JsonObject();
                imageInfo.addProperty("desc", allEDCompetitor.get(i).getText().toString().replaceAll("\n", "\\\\n"));
                imageInfo.addProperty("image", convertImageTo64(allImgCompetitor.get(i)).replaceAll("\n","")); //convert image
                jsonElements.add(imageInfo);
            }
        } else {
            jsonObject.addProperty("have_competitor", 0);
        }
        jsonObject.add("competitor_images", jsonElements);

        jsonObject.addProperty("category_visit", getCategory_visit_choose());
        checkIsNC();
        if(getIs_NC()){
            jsonObject.addProperty("nc", "Y");
        }else{
            jsonObject.addProperty("nc", "N");
        }

        Call<ResponseArrayObject> call;
        /* custom function*/
        call = service.updateVisitPlanSummaryCisangkan(jsonObject);

        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                try{
                    if (response.body() != null && response.body().getError() == 0) {
                        dialogCustom.hidden();
                        String message;
                        if(post==1){
                            message = "Tambah memo berhasil";
                            btnSubmitMemo.setVisibility(View.GONE);
                            btnUpdateMemo.setVisibility(View.VISIBLE);
                        } else {
                            message = "Edit memo berhasil";
                        }
                        Toasty.info(context, message).show();
                    } else {
                        dialogCustom.hidden();
                        Toasty.error(context, response.body().getMessage()).show();
                    }
                } catch(Exception e){
                    dialogCustom.hidden();
                    Toasty.error(context, "Gagal tambah/update memo").show();
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                dialogCustom.hidden();
                Toasty.error(context, "Gagal tambah/update memo").show();
            }
        });
    }

    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(context);
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

    private int dpToPx(int dp) {
        float density = context.getResources()
                .getDisplayMetrics()
                .density;
        return Math.round((float) dp * density);
    }

    private void updateLayout(final int flag, final Bitmap bitmap, String et_content ){
        final int post;
        final String savedImage = saveImage(bitmap);

        if(flag == 1){
            post = countPhotoCustomer;
            llScrollImage.setVisibility(View.VISIBLE);
            llImageCustomer.setVisibility(View.VISIBLE);
        } else {
            post = countPhotoCompetitor;
            llScrollCompetitor.setVisibility(View.VISIBLE);
        }

        final LinearLayout parent = new LinearLayout(context);
        parent.setOrientation(LinearLayout.HORIZONTAL);
        parent.setBackground(context.getResources().getDrawable(R.drawable.bg_white_with_radius));

        LinearLayout.LayoutParams paramParent = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramParent.setMargins(0, 10, 0, 10);
        parent.setLayoutParams(paramParent);
        //==========================================================================================
        LinearLayout leftLayout = new LinearLayout(context);
        leftLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams paramLeftLayout = new LinearLayout.LayoutParams
                (0, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramLeftLayout.weight = (float) 0.2;
        paramLeftLayout.setMargins(10, 0, 0, 0);
        leftLayout.setLayoutParams(paramLeftLayout);

        ImageView imageView = new ImageView(context);
        imageView.setId(post);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
        imageView.setImageBitmap(bitmap);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.showImage(context,bitmap);
            }
        });
        leftLayout.addView(imageView);

        Button btnClear = new Button(context);
        btnClear.setBackground(context.getResources().getDrawable(R.drawable.bg_red));
        btnClear.setText("Hapus");
        btnClear.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
        btnClear.setPadding(0,10,0,10);
        btnClear.setTextColor(context.getResources().getColor(R.color.white));

        int width  = dpToPx(40); //convertDpToPx(context, getResources().getDimension(R.dimen.width_btn_clear));
        int height = dpToPx(25); //getResources().getDimension(R.dimen.height_btn_clear);
        LinearLayout.LayoutParams paramButton = new LinearLayout.LayoutParams(width, height);
        paramButton.gravity = Gravity.CENTER_VERTICAL;
        paramButton.setMargins(10, 10, 0, 0);
        btnClear.setLayoutParams(paramButton);

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogClear(flag, parent, savedImage);
            }
        });
        leftLayout.addView(btnClear);
        // ========================================================================================
        LinearLayout rightLayout = new LinearLayout(context);
        rightLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout.LayoutParams paramRightLayout = new LinearLayout.LayoutParams
                (0, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramRightLayout.weight = (float) 0.8;
        paramRightLayout.setMargins(10, 0, 10, 0);
        rightLayout.setLayoutParams(paramRightLayout);

        final EditText et_desc = new EditText(context);
        et_desc.setId(post);
        et_desc.setHint("Deskripsi");
        et_desc.setBackground(context.getResources().getDrawable(R.drawable.bg_input));
        et_desc.setMaxLines(3);
        et_desc.setPadding(0,0,0,10);
        if(et_content != null)
            et_desc.setText(et_content);
        rightLayout.addView(et_desc);

        parent.addView(leftLayout);
        parent.addView(rightLayout);

        if(flag == 1){
            llImageCustomer.addView(parent);
            allEDs.add(et_desc);
            allImgs.add(savedImage);
            countPhotoCustomer++;
        } else {
            llImageCompetitor.addView(parent);
            allEDCompetitor.add(et_desc);
            allImgCompetitor.add(savedImage);
            countPhotoCompetitor++;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Uri contentURI = data.getData();
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentURI);
                        updateLayout(imagePost, bitmap, null);
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toasty.info(context, "Gagal!").show();
                    }
                }
            }
        } else if (requestCode == CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                // Bitmap fullImage = BitmapFactory.decodeFile(mCurrentPhotoPath);
                Bitmap fullImage = ImagePicker.getImageFromResult(context, resultCode, data, mCurrentPhotoPath);
                updateLayout(imagePost, fullImage, null);
            }
        }
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera()  {
        /*Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoPath);
        startActivityForResult(intent, CAMERA);*/
        dispatchTakePictureIntent();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File

            }
            // Continue only if the File was successfully created
            try{
                Uri photoURI = FileProvider.getUriForFile(context,
                        "id.co.beton.saleslogistic_trackingsystem.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA);
            } catch (ActivityNotFoundException e){
                e.printStackTrace();
            }

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        return image;
    }

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
            MediaScannerConnection.scanFile(context,
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

    private String convertImageTo64(String path) {
        Bitmap bm = BitmapFactory.decodeFile(path);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, Constants.MAX_SIZE_QUALITY_IMAGE, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encodedImage;
    }

    private Bitmap convertBase64ToImage(String base64Encoded){
        final byte[] decodedBytes = Base64.decode(base64Encoded, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void showDialogPost(int flag, final int post){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Produk Kompetitor");
        String message;
        if(flag==1){
            // photo competitor exist
            message = "Photo produk kompetitor sudah diambil, lanjutkan kirim memo tanpa produk kompetitor ?";
        } else {
            // competitor checkbox uncheck
            message = "Data Produk Kompetitor tidak ada, lanjutkan kirim memo tanpa produk kompetitor ?";
        }

        alertDialogBuilder
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postMemo(post);
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
    // private void showDialogClear(final int flag, final EditText text, final LinearLayout layout, final String image){
    private void showDialogClear(final int flag, final LinearLayout layout, final String image){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        alertDialogBuilder.setTitle("Hapus");

        alertDialogBuilder
                .setMessage("Hapus Photo dan Deskripsi ?")
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // text.setText("");
                        layout.removeAllViews();
                        layout.setVisibility(View.GONE);

                        if(flag == 1){
                        // if(imagePost == 1){
                            // get cleared index
                            int postDelete = allImgs.indexOf(image);
                            allEDs.remove(postDelete);
                            allImgs.remove(postDelete);
                            countPhotoCustomer--;
                            if(countPhotoCustomer==0){
                                llScrollImage.setVisibility(View.GONE);
                            }

                        } else {
                            int postDelete = allImgCompetitor.indexOf(image);
                            allEDCompetitor.remove(postDelete);
                            allImgCompetitor.remove(postDelete);
                            countPhotoCompetitor--;
                            if(countPhotoCompetitor==0){
                                llScrollCompetitor.setVisibility(View.GONE);
                            }
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

    private Location getLastLocationService() {
        mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = null;
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }

            l = mLocationManager.getLastKnownLocation(provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    private void loadDataMemo(){

        Call<ResponseObject> call;
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            /* API service for Driver */
            call = service.getDeliveryPlanSummary("JWT "+userUtil.getJWTTOken(), planUtil.getPlanId(), customerCode);
        } else {
            /* API service for Sales */
            call = service.getVisitPlanSummaryCisangkan(                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         "JWT "+userUtil.getJWTTOken(), planUtil.getPlanId(), customerCode);
        }

        /*Log the URL called*/
        if(Constants.DEBUG){
            Log.i("URL Called", call.request().url() + "");
        }                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                       

        /*Loading*/
        dialogCustom.show();

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    try{
                        Gson gson =new Gson();
//                        memo = gson.fromJson(response.body().getData().toString(), Memo.class);
                        memo = gson.fromJson(response.body().getData().toString(), Mymemo.class);

                        etMemo.setText(memo.getNotes());

                        // update list photo visit
                        for(Photo photo : memo.getVisitImages()){
                            // Bitmap fullImage = BitmapFactory.decodeFile(photo.getImage());
                            Bitmap fullImage = convertBase64ToImage(photo.getImage());
                            updateLayout(1, fullImage, photo.getDesc());
                        }

                        // category visit
                        if( memo.getCategory_visit() != null){
                            setCategory_visit_choose(memo.getCategory_visit());
                            spn_category_visit.setSelection(getCategory_visit_choose(getCategory_visit_choose()));
                        }else{
                            spn_category_visit.setSelection(0);
                        }

                        //is_nc
                        if( memo.getNc().equalsIgnoreCase("y")){
                            setIS_NC(true);
                            cbIsNC.setChecked(true);
                            isNCOptionShow();
                        }else{
                            cbIsNC.setChecked(false);
                        }

                        if(memo.getHaveCompetitor()==1){
                            if(memo.getCompetitorImages().size()>0){
                                for(Photo photo : memo.getCompetitorImages()){
                                    // Bitmap fullImage = BitmapFactory.decodeFile(photo.getImage());
                                    Bitmap fullImage = convertBase64ToImage(photo.getImage());
                                    updateLayout(2, fullImage, photo.getDesc());
                                }
                                cbCompetitor.setChecked(true);
                                isCompetitorExist = true;
                                llCompetitor.setVisibility(View.VISIBLE);
                                llScrollCompetitor.setVisibility(View.VISIBLE);
                            }
                        }

                        // update button
                        btnSubmitMemo.setEnabled(false);
                        btnSubmitMemo.setVisibility(View.GONE);
                        btnUpdateMemo.setVisibility(View.VISIBLE);

                        dialogCustom.hidden();
                    }catch (Exception e){
                        e.printStackTrace();
                        dialogCustom.hidden();
                    }
                }else{
                    try{
                        dialogCustom.hidden();
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");
                        if(message.equals("This summary doesn't exist")){
                            message = "Memo kunjungan belum dibuat";
                        }
//                        Toasty.error(getContext(), message, Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Memo kunjungan belum dibuat");
                    }catch (Exception e){
                        dialogCustom.hidden();
                        Toasty.error(getContext(),"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
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

}
