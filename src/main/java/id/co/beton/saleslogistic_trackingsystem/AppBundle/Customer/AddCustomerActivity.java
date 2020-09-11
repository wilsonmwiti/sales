package id.co.beton.saleslogistic_trackingsystem.AppBundle.Customer;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.regex.Pattern;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Adapter.customer.ListMycustomerCategoryAdapter;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Address.AddAddressActivity;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.MainActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Mycustomer;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddCustomerActivity extends AppCompatActivity {
    private static final String TAG = AddCustomerActivity.class.getSimpleName();
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

    private Context context;
    private UserUtil userUtil;

    private TextView tv_header_form;
    private Spinner spn_category_customer;
    private EditText et_code, et_name, et_address, et_phone, et_latitude, et_longitude, et_parent_code;
    private EditText et_name_contact, et_job_contact, et_phone_contact, et_note_contact;
    private CheckBox cb_is_branch, cb_is_generate;
    private Button btn_get_address, btn_submit_customer;

    private DialogCustom dialogCustom;
    private ApiInterface service;

    String[] category_customer;
    private static String category_customer_choose;

    private void setCategory_customer_choose(String category){
        this.category_customer_choose = category;
    }

    private String last_insert;

    private void setLast_insert(String last_insert) {
        this.last_insert = last_insert;
    }

    private String getLast_insert(){
        return last_insert;
    }

    private static String getCategory_customer_choose(){
        return category_customer_choose;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_customer);
        setContentView(R.layout.activity_add_customer_w_category);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Add Customer");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = AddCustomerActivity.this;
        userUtil = UserUtil.getInstance(context);
        dialogCustom = new DialogCustom(context);
        service = ApiClient.getInstance(context);

        category_customer = getResources().getStringArray(R.array.category_customer);

        spn_category_customer = (Spinner) findViewById(R.id.spn_category_customer);
        ArrayAdapter<String> adapterCategoryCustomer = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item,category_customer
        );
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
        /*
        * spinner end
        * */
        et_code = (EditText) findViewById(R.id.et_code);
        et_name = (EditText) findViewById(R.id.et_name);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_address = (EditText) findViewById(R.id.et_address);
        et_latitude = (EditText) findViewById(R.id.et_latitude);
        et_longitude = (EditText) findViewById(R.id.et_longitude);
        cb_is_branch = (CheckBox) findViewById(R.id.cb_is_branch);
        cb_is_generate = (CheckBox) findViewById(R.id.cb_is_generate);
        et_parent_code = (EditText) findViewById(R.id.et_parent_code);
        et_parent_code.setFocusable(false); // disable edit parent code

        et_name_contact = (EditText) findViewById(R.id.et_name_contact);
        et_job_contact = (EditText) findViewById(R.id.et_job_contact);
        et_phone_contact = (EditText) findViewById(R.id.et_phone_contact);
        et_note_contact = (EditText) findViewById(R.id.et_note_contact);

        btn_get_address = (Button) findViewById(R.id.btn_get_address);
        btn_submit_customer = (Button) findViewById(R.id.btn_submit_customer);

        cb_is_branch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIsBrach();
            }
        });

        cb_is_generate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIsGenerate();
            }
        });

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
        /* get last insert customer */
        countMycustomerInsertedToday();
    }

    private boolean nameAllow(String word){
        boolean b = true;
        if(word.contains("'") || word.contains("\"") || word.contains("\\") || word.contains("/")){
            b = false;
        }
        return b;
    }

    private void validationForm(){
        // code
        String pattern = "[A-Za-z0-9]+";
        code = et_code.getText().toString().toUpperCase();
        if(code.equals("")){
            Toasty.error(context, "Kode tidak boleh kosong").show();
            et_code.requestFocus();
            return;
        }
        if(!Pattern.matches(pattern, code)){
            Toasty.error(context, "Kode hanya boleh alphanumeric").show();
            et_code.requestFocus();
            return;
        }

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
        parent_code = et_parent_code.getText().toString();

        // is branch
        if(cb_is_branch.isChecked()){
            if(parent_code.equals("")){
                isBranch = false;
            } else {
                isBranch = true;
            }
        } else
            isBranch = false;

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

    private String countMycustomerInsertedToday(){
        /* show loading */
        dialogCustom.show();

        /* get last customer inserted by user id */
        JsonObject data = new JsonObject();

        data.addProperty("username", Constants.APPS);
        data.addProperty("api_key", Constants.API_KEY);
        data.addProperty("username_code", userUtil.getId());

        Call<ResponseObject> call = service.getLastInsertToday(data);

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body() != null && response.body().getError() == 0) { //if sukses
                    try{
                        Gson gson = new Gson();
                        JsonObject jsonObject = response.body().getData();

                        final String last_insert = jsonObject.get("count").getAsString();
                        Log.d("LASTINSErt", last_insert);
                        setLast_insert(last_insert);

                        dialogCustom.hidden();
                    } catch (Exception e){
                        dialogCustom.hidden();
                        Toasty.error(context, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    dialogCustom.hidden();
                    Toasty.error(context, "fail to generate.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                dialogCustom.hidden();
                Toasty.error(context, "failure call", Toast.LENGTH_SHORT).show();
            }
        });
        return null;
    }

    private String generateCode2(){
        String generated = null;
        String userid, yearStr, dayOfYear, lastInsert;

        if(userUtil.getId() < 100){
            userid = String.valueOf(userUtil.getId()) + "0";
        }else{
            userid = String.valueOf(userUtil.getId());
        }

        /* two digits year*/
        Calendar ca1 = Calendar.getInstance();
        int year = ca1.get(Calendar.YEAR) - 2000;
        /* inisial T dimulai dari 2020, B untuk 2021, dst jika melewati 2026 maka random A - S*/

        String[] A_S ={"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S"};

        Random r=new Random();
        int randomAlphabet = r.nextInt(A_S.length);

        switch (year){
            case 20 : yearStr = "T"; break;
            case 21 : yearStr = "U"; break;
            case 22 : yearStr = "V"; break;
            case 23 : yearStr = "W"; break;
            case 24 : yearStr = "X"; break;
            case 25 : yearStr = "Y"; break;
            case 26 : yearStr = "Z"; break;
            default: yearStr = A_S[randomAlphabet]; break;
        }

        /* date of the year*/
        int doy = ca1.get(Calendar.DAY_OF_YEAR);;

        if(doy < 100){
            if(doy < 10){
                dayOfYear = "00" + String.valueOf(doy);
            }else{
                dayOfYear = "0" + String.valueOf(doy);
            }
        }else{
            dayOfYear = String.valueOf(doy);
        }

        int last = Integer.valueOf(getLast_insert()) + 1;
        if(last < 10){
            lastInsert = "0" + String.valueOf(last);
        }else{
            lastInsert = String.valueOf(last);
        }

        if(lastInsert == null){
            generated = generateCode();
        }else{
            generated = userid + yearStr + dayOfYear + lastInsert;
        }

        return generated;
    }

    private String generateCode(){

        String userid = String.valueOf(userUtil.getId());
        int year = Calendar.getInstance().get(Calendar.YEAR)-2000;
        /* inisial A dimulai dari 2001, B untuk 2002, dst */
        String yearStr;
        switch (year){
            case 20 : yearStr = "T";
            case 21 : yearStr = "U";
            case 22 : yearStr = "V";
            case 23 : yearStr = "W";
            case 24 : yearStr = "X";
            case 25 : yearStr = "Y";
            case 26 : yearStr = "Z";
            default: yearStr = String.valueOf(year);
        }

        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        String monthStr;
        switch (month){
            case 1: monthStr = "A"; break; case 2: monthStr = "B"; break; case 3: monthStr = "C"; break; case 4: monthStr = "D"; break; case 5: monthStr = "E"; break; case 6: monthStr = "F"; break;
            case 7: monthStr = "G"; break; case 8: monthStr = "H"; break; case 9: monthStr = "I"; break; case 10: monthStr = "J"; break; case 11: monthStr = "K"; break; default: monthStr = "L"; break;
        }

        int day = Calendar.getInstance().get(Calendar.DATE);
        int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        String hourStr;
        switch (hour){
            case 1: hourStr = "A"; break; case 2: hourStr = "B"; break; case 3: hourStr = "C"; break; case 4: hourStr = "D"; break; case 5: hourStr = "E"; break; case 6: hourStr = "F"; break;
            case 7: hourStr = "G"; break; case 8: hourStr = "H"; break; case 9: hourStr = "I"; break; case 10: hourStr = "J"; break; case 11: hourStr = "K"; break; case 12: hourStr = "L"; break;
            case 13: hourStr = "M"; break; case 14: hourStr = "N"; break; case 15: hourStr = "O"; break; case 16: hourStr = "P"; break; case 17: hourStr = "Q"; break; case 18: hourStr = "R"; break;
            case 19: hourStr = "S"; break; case 20: hourStr = "T"; break; case 21: hourStr = "U"; break; case 22: hourStr = "V"; break; case 23: hourStr = "W"; break; default: hourStr = "X"; break;
        }
        int minute = Calendar.getInstance().get(Calendar.MINUTE)+1;
        int second = Calendar.getInstance().get(Calendar.SECOND);

        /* date of the year*/
        Calendar ca1 = Calendar.getInstance();
        int dayOfYear=ca1.get(Calendar.DAY_OF_YEAR);;

        String dateCode = String.valueOf(dayOfYear) +hourStr +String.valueOf(minute) +String.valueOf(second);
        return userid +dateCode;
    }

    private void checkIsGenerate(){
        if(cb_is_generate.isChecked()){
            et_code.setText(generateCode2());
        } else {
            et_code.setText("");
        }
    }

    private void submitCustomer(){
        JsonObject data = new JsonObject();

        data.addProperty("username", Constants.APPS);
        data.addProperty("api_key", Constants.API_KEY);
        data.addProperty("username_code", userUtil.getId());

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
        /*
        * adding customer category
        * */
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
        Log.d("post", data.toString());
        postData(data);
    }

    private void postData(JsonObject data){
        /*Create handle for the RetrofitInstance interface*/

        /*Call the method with parameter in the interface to get the data*/
//        Call<ResponseArrayObject> call = service.addCustomer(data);

        Call<ResponseArrayObject> call = service.addMycustomer(data);

        dialogCustom.show();

        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                if (response.body()!=null && response.body().getError()==0) { //if sukses
                    // Update user, add data new customer to customer_id
                    updateListCustomer();
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

    private void backToMycustomer(){
        Intent intent = new Intent(context, MainActivity.class);
        Bundle args = new Bundle();
        args.putString("refresh", "ok");
        intent.putExtras(args);
        startActivity(intent);
        finish();
    }

    private void updateListCustomer(){
        JsonObject data = new JsonObject();
        data.addProperty("id", userUtil.getId());

        /*Create handle for the RetrofitInstance interface*/

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseArrayObject> call = service.updateListCustomer("JWT " + userUtil.getJWTTOken(), code, data);

        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                if (response.body() != null && response.body().getError() == 0) { //if sukses
                    Toasty.success(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dialogCustom.hidden();
                    backToMycustomer();
                } else {
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }catch (Exception e){
                        Toasty.error(context,"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                Toasty.error(context,"Terjadi kesalahan server").show();
                dialogCustom.hidden();
            }
        });
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

        if(requestCode==REQUEST_ADD_CUSTOMER && resultCode== Activity.RESULT_OK){
            parent_code = data.getStringExtra("parent_customer");
            et_parent_code.setText(parent_code);
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
