package id.co.beton.saleslogistic_trackingsystem.AppBundle.Customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

import com.google.common.collect.ObjectArrays;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Address.AddAddressActivity;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.MainActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditCustomerActivity extends AppCompatActivity {

    private static final String TAG = EditCustomerActivity.class.getSimpleName();
    private static final int REQUEST_ADD_ADDRESS=1;
    private static final int REQUEST_ADD_CUSTOMER=2;

    private String code;
    private String name;
    private String address;
    private String phone;
    private double latitude;
    private double longitude;
    private boolean isBranch;
    private String parent_code;
    private String contactName;
    private String contactJob;
    private String contactPhone;
    private String contactNote;

    private int selectedCategory = 0;

    private void setSelectedCategory(int category){
        this.selectedCategory = category;
    }

    private int getSelectedCategory(){
        return selectedCategory;
    }

    private Spinner spn_category_customer;

    private Context context;
    private UserUtil userUtil;

    private TextView tv_header_form, tv_code;
    private EditText et_name, et_address, et_phone, et_latitude, et_longitude, et_parent_code;
    private EditText et_name_contact, et_job_contact, et_phone_contact, et_note_contact;
    private CheckBox cb_is_branch;
    private Button btn_get_address, btn_submit_customer;

    private DialogCustom dialogCustom;
    private ApiInterface service;

    private static String category_customer_choose;

    private void setCategory_customer_choose(String category){
        this.category_customer_choose = category;
    }

    private static String getCategory_customer_choose(){
        return category_customer_choose;
    }

//    private Object object;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tv_header_form = (TextView) findViewById(R.id.header_form_customer);
        //check if intent contain data
        Intent intent = getIntent();
        if (!intent.hasExtra("customer_id")) {
            Toasty.error(context, "Error getting data customer");
            finish();
        }

        assignCustomer(intent);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = EditCustomerActivity.this;
        userUtil = UserUtil.getInstance(context);
        dialogCustom = new DialogCustom(context);
        service = ApiClient.getInstance(context);

        // mode update customer
        code = intent.getStringExtra("customer_id");
        tv_header_form.setText("Update data konsumen");
        setTitle("Edit Customer");

        tv_code = (TextView) findViewById(R.id.tv_code);
        tv_code.setText(code);

        et_name = (EditText) findViewById(R.id.et_name);
        et_name.setText(name);

        et_phone = (EditText) findViewById(R.id.et_phone);
        et_phone.setText(phone);

        et_address = (EditText) findViewById(R.id.et_address);
        et_address.setText(address);

        et_latitude = (EditText) findViewById(R.id.et_latitude);
        et_latitude.setText(String.valueOf(latitude));

        et_longitude = (EditText) findViewById(R.id.et_longitude);
        et_longitude.setText(String.valueOf(longitude));

//        cb_is_branch = (CheckBox) findViewById(R.id.cb_is_branch);

//        et_parent_code = (EditText) findViewById(R.id.et_parent_code);
//        et_parent_code.setFocusable(false); // disable edit parent code

        et_name_contact = (EditText) findViewById(R.id.et_name_contact);
        et_name_contact.setText(contactName);

        et_job_contact = (EditText) findViewById(R.id.et_job_contact);
        et_job_contact.setText(contactJob);

        et_phone_contact = (EditText) findViewById(R.id.et_phone_contact);
        et_phone_contact.setText(contactPhone);

        et_note_contact = (EditText) findViewById(R.id.et_note_contact);
        et_note_contact.setText(contactNote);

        btn_get_address = (Button) findViewById(R.id.btn_get_address);
        btn_submit_customer = (Button) findViewById(R.id.btn_submit_customer);

//        cb_is_branch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                checkIsBrach();
//            }
//        });

        String [] category_customer = getResources().getStringArray(R.array.category_customer);

        ArrayAdapter<String> adapterCategoryCustomer = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, category_customer
        );

        spn_category_customer = (Spinner) findViewById(R.id.spn_category_customer_edit);
        spn_category_customer.setAdapter(adapterCategoryCustomer);

        spn_category_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String cat ;
                switch (i){
                    case 1 : cat = "Developer"; break;
                    case 2 : cat = "End user"; break;
                    case 3 : cat = "Government"; break;
                    case 4 : cat = "Konsultan"; break;
                    case 5 : cat = "Pemborong"; break;
                    case 6 : cat = "Penyalur"; break;
                    default: cat = ""; break;
                }
                setCategory_customer_choose(cat);
                Log.d(TAG,"category customer : " + cat + ", spinner : " + getCategory_customer_choose());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });



        spn_category_customer.setSelection(getSelectedCategory());

        /*
         * spinner end
         * */

        btn_get_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddAddressActivity.class);
                startActivityForResult(intent, REQUEST_ADD_ADDRESS);
            }
        });

        btn_submit_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validationForm();
            }
        });

    }

    private void assignCustomer(Intent intent){
        if(intent.hasExtra("data")){
            HashMap<String, String> Mycustomer = (HashMap<String, String>)intent.getSerializableExtra("data");
            this.name = Mycustomer.get("name");
            this.address = Mycustomer.get("address");
            this.phone = Mycustomer.get("phone");
            this.latitude = Double.parseDouble(Mycustomer.get("latitude"));
            this.longitude = Double.parseDouble(Mycustomer.get("longitude"));
            try{
                setCategory_customer_choose(Mycustomer.get("category"));
                Log.d("category1", Mycustomer.get("category"));

                if(Mycustomer.get("category").equalsIgnoreCase("Developer")){
                    selectedCategory = 1;
                }else if(Mycustomer.get("category").equalsIgnoreCase("End user")){
                    selectedCategory = 2;
                }else if(Mycustomer.get("category").equalsIgnoreCase("Government")){
                    selectedCategory = 3;
                }else if(Mycustomer.get("category").equalsIgnoreCase("Konsultan")){
                    selectedCategory = 4;
                }else if(Mycustomer.get("category").equalsIgnoreCase("Pemborong")){
                    selectedCategory = 5;
                }else if(Mycustomer.get("category").equalsIgnoreCase("Penyalur")){
                    selectedCategory = 6;
                }else{
                    selectedCategory = 0;
                }

                setSelectedCategory(selectedCategory);

                Log.d("category__selected", String.valueOf(getSelectedCategory()));
            }catch (Exception e){
                Log.d("category", e.toString());
            }

            if(Integer.valueOf(Mycustomer.get("isBranch")) == 1){
                this.isBranch = true;
            }else{
                this.isBranch = false;
            }
            this.parent_code = Mycustomer.get("parent_code");
            this.contactName = Mycustomer.get("contactName");
            this.contactJob = Mycustomer.get("contactJob");
            this.contactPhone = Mycustomer.get("contactPhone");
            this.contactNote = Mycustomer.get("contactNote");
        }
    }

    private boolean nameAllow(String word){
        boolean b = true;
        if(word.contains("'") || word.contains("\"") || word.contains("\\") || word.contains("/")){
            b = false;
        }
        return b;
    }

    private void validationForm(){

        // name
        name = et_name.getText().toString();
        if(name.equals("")){
            Toasty.error(context, "Nama tidak boleh kosong").show();
            et_name.requestFocus();
            return;
        }

        if(!nameAllow(name)){
            Toasty.error(context, "Nama boleh mengandung karakter [' \" \\ /] ").show();
            et_name.requestFocus();
            return;
        }

        // address
        address = et_address.getText().toString();
        if(et_address.getText().toString().equals("")){
            Toasty.error(context, "Alamat tidak boleh kosong").show();
            et_address.requestFocus();
            return;
        }

        // coordinate latitude
        latitude = Double.valueOf(et_latitude.getText().toString());
        if(et_latitude.getText().toString().equals("")){
            Toasty.error(context, "Latitude tidak boleh kosong").show();
            et_latitude.requestFocus();
            return;
        }

        // coordinate longitude
        longitude = Double.valueOf(et_longitude.getText().toString());
        if(et_longitude.getText().toString().equals("")){
            Toasty.error(context, "Longitude tidak boleh kosong").show();
            et_longitude.requestFocus();
            return;
        }

        // parent code
//        parent_code = et_parent_code.getText().toString();

        // is branch
//        if(cb_is_branch.isChecked()){
//            if(parent_code.equals("")){
//                isBranch = false;
//            } else {
//                isBranch = true;
//            }
//        } else
//            isBranch = false;

        // contact name
        contactName = et_name_contact.getText().toString();
        if(et_name_contact.getText().toString().equals("")){
            Toasty.error(context, "Kontak Nama tidak boleh kosong").show();
            et_name_contact.requestFocus();
            return;
        }

        phone = et_phone.getText().toString();
        contactJob = et_job_contact.getText().toString();
        contactPhone = et_phone_contact.getText().toString();
        contactNote = et_note_contact.getText().toString();

        submitCustomer();
    }

    private void checkIsBrach(){
        if(cb_is_branch.isChecked()){
            isBranch = true;
            et_parent_code.setVisibility(View.VISIBLE);
            Intent intent = new Intent(context, AddCustomerParentActivity.class);
            startActivityForResult(intent, REQUEST_ADD_CUSTOMER);
        } else {
            isBranch = false;
            et_parent_code.setVisibility(View.GONE);
            et_parent_code.setText("");
            parent_code = "";
        }
    }

    private void submitCustomer(){
        JsonObject data = new JsonObject();
        /*
        * addtional variable
        * */
        data.addProperty("username", Constants.APPS);
        data.addProperty("api_key", Constants.API_KEY);
        /**/
        data.addProperty("address", address);
        data.addProperty("code", code);
        data.addProperty("email", "");
        data.addProperty("lat", latitude);
        data.addProperty("lng", longitude);
        data.addProperty("name", name);
        data.addProperty("nfcid", code);
        data.addProperty("parent_code", "");
        data.addProperty("password", "");
        data.addProperty("phone", phone);
//        data.addProperty("username", "");
        data.addProperty("category", getCategory_customer_choose());

        JsonArray business = new JsonArray();
        business.add("order_sales");
        business.add("delivery");
        business.add("invoicing");
        data.add("business_activity", business);
        if(isBranch){
            data.addProperty("parent_code", parent_code);
        } else {
            data.addProperty("parent_code", "");
        }
        data.addProperty("is_branch", isBranch);

        // data contact
        JsonObject contact = new JsonObject();
        contact.addProperty("name", contactName);
        contact.addProperty("job_position", contactJob);
        contact.addProperty("note", contactNote);
        contact.addProperty("phone", contactPhone);
        contact.addProperty("email", "");
        contact.addProperty("mobile", "");

        JsonObject notif = new JsonObject();
        notif.addProperty("delivery_received", "");
        notif.addProperty("delivery_rejected", "");
        notif.addProperty("invoice_reminder", "");
        notif.addProperty("nfc_not_read", "");
        notif.addProperty("payment_confirmation", "");
        notif.addProperty("payment_receipt_not_read", "");
        notif.addProperty("payment_received", "");
        notif.addProperty("request_order_create", "");
        notif.addProperty("sales_order_return", "");
        notif.addProperty("sales_order_status_changed", "");
        notif.addProperty("visit_plan_reminder", "");
        contact.add("notifications", notif);

        JsonArray arrayContact = new JsonArray();
        arrayContact.add(contact);

        data.add("contacts", arrayContact);
        postData(data);
    }

    private void postData(JsonObject data){

        Call<ResponseArrayObject> call = service.updateMycustomer(data);
        dialogCustom.show();

        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                if (response.body()!=null && response.body().getError()==0) { //if sukses
                    // Update user, add data new customer to customer_id
                    Toasty.success(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dialogCustom.hidden();
                    backToMycustomer();
                } else {
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }catch (Exception e){
                        dialogCustom.hidden();
                        Toasty.error(context,"Terjadi kesalahan.",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                Toasty.error(context,"Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                dialogCustom.hidden();
            }
        });
    }

//    private void updateListCustomer(){
//        JsonObject data = new JsonObject();
//        data.addProperty("id", userUtil.getId());
//        /*Create handle for the RetrofitInstance interface*/
//        /*Call the method with parameter in the interface to get the data*/
//        Call<ResponseArrayObject> call = service.updateListCustomer("JWT " + userUtil.getJWTTOken(), code, data);
//
//        call.enqueue(new Callback<ResponseArrayObject>() {
//            @Override
//            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
//                if (response.body() != null && response.body().getError() == 0) { //if sukses
//                    Toasty.success(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                    dialogCustom.hidden();
//                    backToMycustomer();
//                } else {
//                    try{
//                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
//                        Toasty.error(context,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
//                        dialogCustom.hidden();
//                    }catch (Exception e){
//                        Toasty.error(context,"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
//                        dialogCustom.hidden();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
//                Toasty.error(context,"Terjadi kesalahan server").show();
//                dialogCustom.hidden();
//            }
//        });
//    }

    private void backToMycustomer(){
        Intent intent = new Intent(context, MainActivity.class);
        Bundle args = new Bundle();
        args.putString("refresh", "ok");
        intent.putExtras(args);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_ADD_ADDRESS && resultCode== Activity.RESULT_OK){
            address   = data.getStringExtra("new_address");
            latitude  = data.getDoubleExtra("lat",0);
            longitude = data.getDoubleExtra("lng",0);
            et_address.setText(address);
            et_latitude.setText(String.valueOf(latitude));
            et_longitude.setText(String.valueOf(longitude));
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
