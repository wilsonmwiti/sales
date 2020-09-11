package id.co.beton.saleslogistic_trackingsystem.AppBundle.Customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Adapter.customer.ListMycustomerCategoryAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;

import id.co.beton.saleslogistic_trackingsystem.Model.Mycustomer;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class MyCustomerFragment
 *
 */

public class MyCustomerFragment extends Fragment {
    private int REQUEST_CODE = 1001;
    private static final String TAG = MyCustomerFragment.class.getSimpleName();
    private TextView text_view_for_tab_selection;
    private Button btnSearch;
    private TextView mycustomerTotal;
    private OnFragmentInteractionListener mListener;
    private ListView lvMycustomer;
    private FloatingActionButton addMycustomer;
    private SearchView searchView;

    private Context context;
    DialogCustom dialogCustom;
    LayoutInflater inflater;
    private View view;

    private UserUtil userUtil;

//    private static List<Customer> listCustomers;
    private static List<Mycustomer> listCustomers;
//    private ListMycustomerAdapter listMycustomerAdapter;
    private ListMycustomerCategoryAdapter listMycustomerAdapter;

    protected Activity activity;

    public static int totalCustomer;

    public void setTotalCustomer(int total){
        totalCustomer = total;
    }

    public int getTotalCustomer(){
        return totalCustomer;
    }

    public boolean onBackPressed(){
        return true;
    };

    private boolean hasFooter = false;

    public MyCustomerFragment(){
        //empty constructor
    }

    public static MyCustomerFragment newInstance(int position) {
        MyCustomerFragment fragment = new MyCustomerFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_mycustomer, container, false);
        view.setTag(TAG);

        userUtil = UserUtil.getInstance(context);
        searchView = (SearchView) view.findViewById(R.id.searchFilter_mycustomer);
        lvMycustomer = (ListView) view.findViewById(R.id.lv_mycustomer);

        mycustomerTotal = (TextView) view.findViewById(R.id.tv_mycustomer_total);
        context = container.getContext();

        dialogCustom = new DialogCustom(context);

        addMycustomer  = (FloatingActionButton) view.findViewById(R.id.fab_add_new_customer);
        addMycustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddCustomerActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
//                Intent intent = new Intent(context, AddAddressActivity.class);
//                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        this.inflater = inflater;

        loadData(true);

        /**
         * function to search customer by customer name
         */
        searchView = view.findViewById(R.id.searchFilter_mycustomer);
        searchView.setVisibility(View.GONE);
        SearchView.OnQueryTextListener textChangeListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (TextUtils.isEmpty(query)){
                    //Text is cleared, do your thing
                    refreshAdapter(false);
                } else {
                    listMycustomerAdapter.getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText)){
                    //Text is cleared, do your thing
                    refreshAdapter(false);
                } else {
                    listMycustomerAdapter.getFilter().filter(newText);
                }
                return false;
            }
        };
        searchView.setOnQueryTextListener(textChangeListener);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                refreshAdapter(false);
                return false;
            }
        });

        return view;
    }

    private void initSearchButton(){
        // Add a footer to the ListView
        Log.d("try", "init search button");
        ViewGroup footer = (ViewGroup) inflater.inflate(R.layout.footer_search_mycustomer, lvMycustomer,false);
        lvMycustomer.addFooterView(footer,null,false);

        btnSearch = (Button) footer.findViewById(R.id.btn_search_mycustomer);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, SearchMycustomerActivity.class);
                startActivity(intent);
            }
        });
        hasFooter = true;
    }

    /**
     * function to refresh page
     * @param flagLoading
     */
    private void refreshAdapter(boolean flagLoading){
        listMycustomerAdapter = new ListMycustomerCategoryAdapter(context,1,0, false, listCustomers);
        // loadDataSales(flagLoading);
        loadData(flagLoading);
    }

    // custom function

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("destroy", "destroy fragment");
        if(isRemoving()){
            Log.d("destroy", "removeing fragment");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("detach", "detach fragment");
        if(isRemoving()){
            Log.d("detach", "removing fragment");
        }
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    /**
     * load data customer
     * @param showLoading
     */
    private void loadData(boolean showLoading){
        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        //custom function
        JsonObject data = new JsonObject();
        data.addProperty("username", Constants.APPS);
        data.addProperty("api_key", Constants.API_KEY);
        data.addProperty("username_code", userUtil.getUsername());
        Call<ResponseObject> call = service.getMycustomerSummary(data);

        if(showLoading){
            dialogCustom.show();
        }

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body() != null && response.body().getError() == 0) { //if sukses
                    try{
                        Gson gson = new Gson();
                        JsonObject jsonObject = response.body().getData();

                        String showing;
                        String maxCustomer;
                        String totalStr = jsonObject.get("total").getAsString();
                        int total = Integer.parseInt(totalStr);
                        if(total < 25){
                            showing = String.valueOf(total);
                            maxCustomer = String.valueOf(total);
                        }else{
                            showing = String.valueOf(25);
                            maxCustomer = totalStr;
                        }

                        if(Integer.valueOf(maxCustomer) > 999){
                            maxCustomer = "999+";
                        }


                        JsonArray arrData = jsonObject.getAsJsonArray("mycustomer");
                        if(arrData.size() >0){
                            listCustomers = new ArrayList<>();
                            for(int i=0; i<arrData.size(); i++){
//                                Customer customer = gson.fromJson(arrData.get(i), Customer.class);
                                Mycustomer mycustomer = gson.fromJson(arrData.get(i), Mycustomer.class);
                                listCustomers.add(mycustomer);
                            }

                            setTotalCustomer(listCustomers.size());

                            if(listCustomers.size() > 0){
                                // set adapter
                                listMycustomerAdapter = new ListMycustomerCategoryAdapter(context, 1, 0, false, listCustomers);
                                lvMycustomer.setAdapter(listMycustomerAdapter);
                                searchView.setVisibility(View.VISIBLE);
                                String titleHeader = "showing last " + showing + " , from " + maxCustomer + " of customer";
                                mycustomerTotal.setText(titleHeader);
                                if(!hasFooter){
                                    initSearchButton();
                                }
                                Log.d("mylog", titleHeader);
                            }
                        }

                        dialogCustom.hidden();
                    } catch (Exception e){
                        dialogCustom.hidden();
                        Toasty.error(context, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    try {
                        dialogCustom.hidden();
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        dialogCustom.hidden();
                        Toasty.error(context, "Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                dialogCustom.hidden();
                Toasty.error(context, "Kesalahan server", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
