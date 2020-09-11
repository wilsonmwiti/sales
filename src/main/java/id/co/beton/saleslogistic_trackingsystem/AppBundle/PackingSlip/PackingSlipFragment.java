package id.co.beton.saleslogistic_trackingsystem.AppBundle.PackingSlip;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import id.co.beton.saleslogistic_trackingsystem.Adapter.packingslip.PackingSlipAdapter;
import id.co.beton.saleslogistic_trackingsystem.Adapter.packingslip.PackingSlipAdapterDetailPlan;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.DataPackingSlip;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.PackingSlip;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class PackingSlipFragment
 * list data packing slip
 */
public class PackingSlipFragment extends Fragment {
    private static final String TAG = PackingSlipFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private ArrayList<Destination> destinationItems;
    private ArrayList<PackingSlip> packingSlipId;
    private ApiInterface service;

    private ListView lvPackingSlip;

    public PackingSlipFragment() {
    }

    public static PackingSlipFragment newInstance(String tabSelected) {
        PackingSlipFragment fragment = new PackingSlipFragment();
        Bundle args = new Bundle();
        args.putString("position", tabSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_packing_slip, container, false);
        view.setTag(TAG);
        packingSlipId = new ArrayList<>();
        destinationItems = new ArrayList<>();
        service = ApiClient.getInstance(container.getContext());

        lvPackingSlip = (ListView)view.findViewById(R.id.lv_packing_slip);

        PackingSlipAdapter packingSlipAdapter = new PackingSlipAdapter(container.getContext(),1, packingSlipId,destinationItems,"");
        lvPackingSlip.setAdapter(packingSlipAdapter);
        View header = inflater.inflate(R.layout.header_packing_slip,null,false);
        lvPackingSlip.addHeaderView(header);
        if (getArguments().getString("destination") != null) {
            Gson gson = new Gson();
            final Destination destination = gson.fromJson(getArguments().getString("destination"), new TypeToken<Destination>(){}.getType());
            PackingSlipAdapterDetailPlan psAdapter=new PackingSlipAdapterDetailPlan(container.getContext(),1, destination.getPackingSlips(),destination.getCustomerName(),destination.getAddress());
            lvPackingSlip.setAdapter(psAdapter);

            lvPackingSlip.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    if(i>0){
                        try{
                            Gson gson = new Gson();
                            String packingSlipItem           = gson.toJson(destination.getPackingSlips().get(i-1));
                            String destinationPackingItem    = gson.toJson(destination);

                            Intent intent = new Intent(getContext(), DetailPackingSlipActivity.class);
                            intent.putExtra("packingSlipItem",packingSlipItem);
                            intent.putExtra("destinationPackingItem",destinationPackingItem);
                            String today =ConversionDate.getInstance().today();
                            intent.putExtra("datePackingItem",today);
                            intent.putExtra("status_from_detail_customer",true);
                            startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            });
        }else{
            loadData();
        }

        return view;
    }

    private void loadData(){

        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(getContext());
        PlanUtil planUtil = PlanUtil.getInstance(getContext());

        /*Create handle for the RetrofitInstance interface*/


        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.getDeliveryPlan("JWT "+userUtil.getJWTTOken(), planUtil.getPlanId());
        /*Log the URL called*/
        if(Constants.DEBUG){
            Log.i("URL Called", call.request().url() + "");
        }

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    Gson gson =new Gson();
                    DataPackingSlip data = gson.fromJson(response.body().getData().toString(), DataPackingSlip.class);
                    packingSlipId.addAll(data.getPackingSlipId());
                    destinationItems.addAll(data.getDestination());

                    PackingSlipAdapter psAdapter=new PackingSlipAdapter(getContext(),1, packingSlipId,destinationItems,data.getDate());
                    lvPackingSlip.setAdapter(psAdapter);

                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toasty.error(getContext(),"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                Toasty.error(getActivity(), "Terjadi kesalahan server",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    /*@Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
