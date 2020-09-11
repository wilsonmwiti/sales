package id.co.beton.saleslogistic_trackingsystem.Adapter.customer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Customer.EditCustomerActivity;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.MainActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.Contacts;
import id.co.beton.saleslogistic_trackingsystem.Model.Mycustomer;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListMycustomerCategoryAdapter extends ArrayAdapter<Mycustomer>
        implements Filterable {
    private Context context;
    private List<Mycustomer> customers;
    private List<Mycustomer> customerFiltered;
    private int selectedPosition = -1;
    private String customerId;
    private boolean isFilter;
    private int originLength;
    private HashMap<String, String> hashMap;

    public ListMycustomerCategoryAdapter(@NonNull Context context, int resource, int originLength, boolean isFilter, @NonNull List<Mycustomer> customers){
        super(context, resource, customers);
        this.context = context;
        this.customers = customers;
        this.customerFiltered = customers;
        this.isFilter = isFilter;
        this.originLength = originLength;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Mycustomer customer = getItem(position);

        final Holder holder;

        convertView = LayoutInflater.from(context).inflate(R.layout.content_list_mycustomer,parent,false);

        holder = new Holder();
        holder.tvMycustomerName = (TextView) convertView.findViewById(R.id.tv_mycustomer_name);
        holder.tvMycustomerAddress= (TextView) convertView.findViewById(R.id.tv_mycustomer_address);
        holder.fab_edit = (FloatingActionButton) convertView.findViewById(R.id.fab_edit_mycustomer);
        holder.fab_delete = (FloatingActionButton) convertView.findViewById(R.id.fab_del_mycustomer);
        holder.delimiter = (LinearLayout) convertView.findViewById(R.id.delimiter_mycustomer);

        customerId = customer.getCode();

        holder.tvMycustomerName.setText(customer.getNama());
        holder.tvMycustomerAddress.setText(customer.getAlamat());

        //method
        holder.fab_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d("DELETE", "delete " + customers.get(position).getCode());
                showDialogDeleteCustomer(customer);
            }
        });

        holder.fab_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("EDIT", "EDIT " + customers.get(position).getCode());
                Intent intent = new Intent(context, EditCustomerActivity.class);
                intent.putExtra("customer_id", customers.get(position).getCode());

                    HashMap<String, String> customerE = new HashMap<>();
                    customerE.put("name", customer.getNama());
                    customerE.put("address", customer.getAlamat());
                    customerE.put("phone", customer.getPhone());
                    customerE.put("latitude", String.valueOf(customer.getLat()));
                    customerE.put("longitude", String.valueOf(customer.getLng()));
                    customerE.put("isBranch", String.valueOf(customer.getIsBranch()));
                    customerE.put("parent_code", String.valueOf(customer.getParentCode()));
                    customerE.put("category", String.valueOf(customer.getCategory()));

                    Object object = customer.getContacts();

                try{
                    String contactStr = objectToJSONArray(object).get(0).toString();
                    Log.d("tojsonarray", contactStr);

//                    String length = String.valueOf(objectToJSONArray(object).length());
//                    Log.d("length", length);

                    Gson gson = new Gson();
                    Contacts contacts = gson.fromJson(contactStr, Contacts.class);
                    customerE.put("contactName", contacts.getName());
                    customerE.put("contactPhone", contacts.getPhone());
                    customerE.put("contactJob", contacts.getJob_position());
                    customerE.put("contactNote", contacts.getNote());
//                    Log.d("gson", contacts.getName());

                }
                catch (Exception e){
                    Log.d("tojsonarray", "ERROR" + e.toString());
                }

                intent.putExtra("data", customerE);
                context.startActivity(intent);
            }
        });


        if(!isFilter){
            if(originLength>0){
                if(originLength < this.customers.size()){
                    if(originLength == (position+1)){
                        holder.delimiter.setVisibility(View.VISIBLE);
                    } else {
                        holder.delimiter.setVisibility(View.GONE);
                    }
                }
            }
        } else {
            holder.delimiter.setVisibility(View.GONE);
        }

        return convertView;
    }

    static class Holder {
        TextView tvMycustomerName;
        TextView tvMycustomerAddress;
        FloatingActionButton fab_edit;
        FloatingActionButton fab_delete;
        LinearLayout delimiter;
    }

    private void showDialogDeleteCustomer(final Mycustomer customer){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Konfirmasi");
        String message = "Yakin akan menghapus : " + customer.getNama() + ", dari daftar customer anda?";
        alertDialogBuilder
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete customer
                        deleteCustomer(customer);
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public static JSONObject objectToJSONObject(Object object){
        Object json = null;
        JSONObject jsonObject = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject) json;
        }
        return jsonObject;
    }

    public static JSONArray objectToJSONArray(Object object){
        Object json = null;
        JSONArray jsonArray = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONArray) {
            jsonArray = (JSONArray) json;
        }
        return jsonArray;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    customerFiltered = customers;
                } else {
                    List<Mycustomer> filteredList = new ArrayList<Mycustomer>();
                    for (Mycustomer row : customers) {
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    customerFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = customerFiltered;
                filterResults.count = customerFiltered.size();
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                customerFiltered = (ArrayList<Mycustomer>) filterResults.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = customerFiltered.size(); i < l; i++)
                    add(customerFiltered.get(i));
                notifyDataSetInvalidated();
            }
        };
    }

    UserUtil userUtil;
    private ApiInterface service;
    private DialogCustom dialogCustom;

    private void deleteCustomer(Mycustomer customer){

        userUtil = UserUtil.getInstance(context);
        dialogCustom = new DialogCustom(context);
        service = ApiClient.getInstance(context);

        JsonObject data = new JsonObject();
        data.addProperty("username", Constants.APPS);
        data.addProperty("api_key", Constants.API_KEY);
        data.addProperty("username_id", userUtil.getId());
        data.addProperty("customer_id", customer.getCode());

        /*Create handle for the RetrofitInstance interface*/

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseArrayObject> call = service.deleteCustomer(data);

        dialogCustom.show();

        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                if (response.body() != null && response.body().getError() == 0) {
                    //if sukses
                    Toasty.success(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    dialogCustom.hidden();
                    backToMycustomer(context);
                } else {
                    Toasty.error(context,"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    dialogCustom.hidden();
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                Toasty.error(context,"Terjadi kesalahan server").show();
                Log.d("throwable", t.toString());
                dialogCustom.hidden();
            }
        });
    }

    private void backToMycustomer(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        Bundle args = new Bundle();
        args.putString("refresh", "ok");
        intent.putExtras(args);
        context.startActivity(intent);
    }
}
