package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Info;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.MainActivity;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.DetailPlanActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Services.VisitService;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;
import id.co.beton.saleslogistic_trackingsystem.Utils.DetectFakeGPS;
import id.co.beton.saleslogistic_trackingsystem.Utils.DistanceCalculator;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

/**
 * Class InfoFragment
 * info detail customer
 */
public class InfoFragment extends Fragment {
    private static final String TAG = InfoFragment.class.getSimpleName();
    private Context context;
    String nfcCode;
    String customerCode;
    String customerName;
    // private Destination destination;

    protected boolean canTapIn = false;

    Double customerLatitude;
    Double customerLongitude;
    private Button btnCheckIn, btnCheckOut;
    private boolean isSet = false;
    private boolean customCheckin = false;
    private boolean customCheckout = false;

    private TextView tvCustomerName,tvArrivedAt,tvDeparture,tvTime,tvAddress,tvNamaPIC,tvPhone,
            tvPosisiPIC,tvInvoices,tvLastVisited,tvLastOrdered,tvNotes,tvJmlInvoice, tvJumPackingSlip,tvPackingSlipNo;

    private UserUtil userUtil;
    protected Location location = null;
    private LocationManager mLocationManager = null;

    private ApiInterface service;

    public InfoFragment() {
        // Required empty public constructor
    }

    public static InfoFragment newInstance(int tabSelected) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putInt("position", tabSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        context = container.getContext();
        userUtil = UserUtil.getInstance(context);
        nfcCode = userUtil.getStringProperty(Constants.NFC_CODE);
        customerCode = getArguments().getString("cust_code");
        customerName = getArguments().getString("cust_name");
        service = ApiClient.getInstance(context);
        // Gson gson = new Gson();
        // destination = gson.fromJson(getArguments().getString("destination"), new TypeToken<Destination>() {}.getType());

        View view=null;
        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            view = inflater.inflate(R.layout.content_detail_visit_plan_info_driver, container, false);
            view.setTag(TAG);
            if (getArguments() != null) {
                Gson gson = new Gson();
                Destination destination = gson.fromJson(getArguments().getString("destination"), new TypeToken<Destination>(){}.getType());
                customerLatitude  = destination.getLat();
                customerLongitude = destination.getLng();

                tvCustomerName = view.findViewById(R.id.tv_customer_name);
                tvAddress = view.findViewById(R.id.tv_address);
                tvNamaPIC = view.findViewById(R.id.tv_nama_pic);
                tvPhone = view.findViewById(R.id.tv_phone);
                tvPosisiPIC = view.findViewById(R.id.tv_position_pic);
                tvNotes = view.findViewById((R.id.tv_notes));
                tvJumPackingSlip = view.findViewById(R.id.tv_jml_packing_slip);
                tvLastOrdered = view.findViewById(R.id.tv_last_ordered);
                tvPackingSlipNo = view.findViewById(R.id.tv_packing_slip_no);
                tvArrivedAt = view.findViewById(R.id.tv_arrived_at);
                tvDeparture = view.findViewById(R.id.tv_departure);
                btnCheckIn = view.findViewById(R.id.btn_checkin);
                btnCheckOut = view.findViewById(R.id.btn_checkout);

                btnCheckIn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonCheckinSubmit();
                    }
                });
                if(userUtil.getStringProperty(Constants.CURRENT_PAGE_PLAN).equals(userUtil.getStringProperty(Constants.NFC_CODE))){
                    btnCheckIn.setEnabled(false);
                } else {
                    btnCheckIn.setEnabled(true);
                }

                btnCheckOut.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        buttonCheckoutSubmit();
                    }
                });

                /**
                 * BUG: arrival time di destination null,
                 * resolved ketika onResume di visitPlanFragment dilakukan loadData
                 * solusi:
                 *  panggil api service untuk me-set nilai textView
                 *
                 **/
                if (destination.getArrivalTime() == null || destination.getDepartureTime() == null) {
                    PlanUtil planUtil = PlanUtil.getInstance(context);
                    Call<ResponseObject> call;
                    call = service.getDeliveryPlanDetailByCustomer("JWT " + userUtil.getJWTTOken(), planUtil.getPlanId(), customerCode);

                    /*Log the URL called*/
                    if (Constants.DEBUG) {
                        Log.i("URL Called", call.request().url() + "");
                    }

                    call.enqueue(new Callback<ResponseObject>() {
                        @Override
                        public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                            if (response.body() != null && response.body().getError() == 0) { //if sukses
                                try {
                                    final Gson gson = new Gson();
                                    Destination dest = gson.fromJson(response.body().getData().toString(), Destination.class);

                                    /* set textView at here */
                                    tvCustomerName.setText(dest.getCustomerName());
                                    tvAddress.setText(dest.getAddress());
                                    tvPhone.setText(dest.getPicMobile());
                                    tvNotes.setText(dest.getNote());
                                    tvNamaPIC.setText(dest.getPicName());
                                    tvPosisiPIC.setText(dest.getPicJobPosition());
                                    tvJumPackingSlip.setText(dest.getPackingSlips().size()+"");
                                    tvPackingSlipNo.setText("");
                                    for (int i=0;i<dest.getPackingSlips().size();i++){
                                        tvPackingSlipNo.append(dest.getPackingSlips().get(i).getIdPackingSlip());
                                        tvPackingSlipNo.append("\n");
                                    }
                                    try{
                                        String lastOrder = ConversionDate.getInstance().fullFormatDate(dest.getLastDeliveryDate());
                                        tvLastOrdered.setText(lastOrder);
                                        if(dest.getArrivalTime()!=null){
                                            String[] arrive = dest.getArrivalTime().split(" ");
                                            if(arrive!=null && arrive.length>0){
                                                tvArrivedAt.setText(arrive[1]);
                                            }else{
                                                // BUG: Pusat masalah, dimana driver dapat melakukan pesanan walaupun belum di lokasi
                                                /*
                                                 * Alasan Bug:
                                                 *   Sebab kode dibawah hanya membaca customerCode, dan jika customer code tidak kosong maka akan di prosses
                                                 * Solusi :
                                                 *   customerCode akan di kosongkan setelah tap NFC jika driver belum di lokasi customer.
                                                 */
                                                if(nfcCode.trim().contentEquals(customerCode.trim())){
                                                    ConversionDate conversionDate = new ConversionDate();
                                                    tvArrivedAt.setText(conversionDate.timeNow());
                                                }else{
                                                    tvArrivedAt.setText("-");
                                                }
                                            }
                                        }else{
                                            // BUG: Pusat masalah, dimana driver dapat melakukan pesanan walaupun belum di lokasi
                                            /*
                                             * Alasan Bug:
                                             *   Sebab kode dibawah hanya membaca customerCode, dan jika customer code tidak kosong maka akan di prosses
                                             * Solusi :
                                             *   customerCode akan di kosongkan setelah tap NFC jika driver belum di lokasi customer.
                                             */
                                            if(nfcCode.trim().contentEquals(customerCode.trim())){
                                                ConversionDate conversionDate = new ConversionDate();
                                                tvArrivedAt.setText(conversionDate.timeNow());
                                            }else{
                                                tvArrivedAt.setText("-");
                                            }
                                        }

                                        if(dest.getDepartureTime()!=null){
                                            String[] departure = dest.getDepartureTime().split(" ");
                                            // BUG: Pusat masalah, dimana driver dapat melakukan pesanan walaupun belum di lokasi
                                            /*
                                             * Alasan Bug:
                                             *   Sebab kode dibawah hanya membaca customerCode, dan jika customer code tidak kosong maka akan di prosses
                                             * Solusi :
                                             *   customerCode akan di kosongkan setelah tap NFC jika driver belum di lokasi customer.
                                             */
                                            if(departure!=null && departure.length>0){
                                                tvDeparture.setText(departure[1]);
                                            }else{
                                                tvDeparture.setText("-");
                                            }
                                        }else{
                                            tvDeparture.setText("-");
                                        }

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }

                                    // set isSet to true
                                    isSet = true;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseObject> call, Throwable t) {
                            Toasty.error(context, "Tidak ada koneksi internet").show();
                        }
                    });
                }

                if(!isSet){
                    try{
                        tvCustomerName.setText(destination.getCustomerName());
                        tvAddress.setText(destination.getAddress());
                        tvPhone.setText(destination.getPicMobile());
                        tvNotes.setText(destination.getNote());
                        tvNamaPIC.setText(destination.getPicName());
                        tvPosisiPIC.setText(destination.getPicJobPosition());
                        tvJumPackingSlip.setText(destination.getPackingSlips().size()+"");
                        tvPackingSlipNo.setText("");
                        for (int i=0;i<destination.getPackingSlips().size();i++){
                            tvPackingSlipNo.append(destination.getPackingSlips().get(i).getIdPackingSlip());
                            tvPackingSlipNo.append("\n");
                        }
                        try{
                            String lastOrder = ConversionDate.getInstance().fullFormatDate(destination.getLastDeliveryDate());
                            tvLastOrdered.setText(lastOrder);
                            if(destination.getArrivalTime()!=null){
                                String[] arrive = destination.getArrivalTime().split(" ");
                                if(arrive!=null && arrive.length>0){
                                    tvArrivedAt.setText(arrive[1]);
                                }else{
                                    // BUG: Pusat masalah, dimana driver dapat melakukan pesanan walaupun belum di lokasi
                                    /*
                                     * Alasan Bug:
                                     *   Sebab kode dibawah hanya membaca customerCode, dan jika customer code tidak kosong maka akan di prosses
                                     * Solusi :
                                     *   customerCode akan di kosongkan setelah tap NFC jika driver belum di lokasi customer.
                                     */
                                    if(nfcCode.trim().contentEquals(customerCode.trim())){
                                        ConversionDate conversionDate = new ConversionDate();
                                        tvArrivedAt.setText(conversionDate.timeNow());
                                    }else{
                                        tvArrivedAt.setText("-");
                                    }
                                }
                            }else{
                                // BUG: Pusat masalah, dimana driver dapat melakukan pesanan walaupun belum di lokasi
                                /*
                                 * Alasan Bug:
                                 *   Sebab kode dibawah hanya membaca customerCode, dan jika customer code tidak kosong maka akan di prosses
                                 * Solusi :
                                 *   customerCode akan di kosongkan setelah tap NFC jika driver belum di lokasi customer.
                                 */
                                if(nfcCode.trim().contentEquals(customerCode.trim())){
                                    ConversionDate conversionDate = new ConversionDate();
                                    tvArrivedAt.setText(conversionDate.timeNow());
                                }else{
                                    tvArrivedAt.setText("-");
                                }
                            }

                            if(destination.getDepartureTime()!=null){
                                String[] depature = destination.getDepartureTime().split(" ");
                                // BUG: Pusat masalah, dimana driver dapat melakukan pesanan walaupun belum di lokasi
                                /*
                                 * Alasan Bug:
                                 *   Sebab kode dibawah hanya membaca customerCode, dan jika customer code tidak kosong maka akan di prosses
                                 * Solusi :
                                 *   customerCode akan di kosongkan setelah tap NFC jika driver belum di lokasi customer.
                                 */
                                if(depature!=null && depature.length>0){
                                    tvDeparture.setText(depature[1]);
                                }else{
                                    tvDeparture.setText("-");
                                }
                            }else{
                                tvDeparture.setText("-");
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }else{
            view = inflater.inflate(R.layout.content_detail_visit_plan_info, container, false);
            view.setTag(TAG);

            if (getArguments() != null) {
                try{
                    Gson gson = new Gson();
                    Destination destination = gson.fromJson(getArguments().getString("destination"), new TypeToken<Destination>(){}.getType());
                    customerLatitude = destination.getLat();
                    customerLongitude = destination.getLng();

                    tvArrivedAt = view.findViewById(R.id.tv_arrived_at);
                    tvDeparture = view.findViewById(R.id.tv_departure);
                    tvTime = view.findViewById(R.id.tv_time_end_point);
                    tvCustomerName = view.findViewById(R.id.tv_customer_name);
                    tvAddress = view.findViewById(R.id.tv_address);
                    tvNamaPIC = view.findViewById(R.id.tv_nama_pic);
                    tvPhone = view.findViewById(R.id.tv_phone);
                    tvPosisiPIC = view.findViewById(R.id.tv_position_pic);
                    tvInvoices = view.findViewById(R.id.tv_invoices);
                    tvLastVisited = view.findViewById(R.id.tv_last_visited);
                    tvLastOrdered = view.findViewById(R.id.tv_last_ordered);
                    tvNotes = view.findViewById((R.id.tv_notes));
                    tvJmlInvoice = view.findViewById(R.id.tv_jml_invoice);
                    btnCheckIn = view.findViewById(R.id.btn_checkin);
                    btnCheckOut = view.findViewById(R.id.btn_checkout);

                    btnCheckIn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            buttonCheckinSubmit();
                        }
                    });
                    if(userUtil.getStringProperty(Constants.CURRENT_PAGE_PLAN).equals(userUtil.getStringProperty(Constants.NFC_CODE))){
                        btnCheckIn.setEnabled(false);
                    } else {
                        btnCheckIn.setEnabled(true);
                    }

                    btnCheckOut.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            buttonCheckoutSubmit();
                        }
                    });

                    /**
                     * BUG: arrival time di destination null,
                     * resolved ketika onResume di visitPlanFragment dilakukan loadData
                     * solusi:
                     *  panggil api service untuk me-set nilai textView
                     *
                     **/
                    if (destination.getArrivalTime() == null || destination.getDepartureTime() == null) {
                        PlanUtil planUtil = PlanUtil.getInstance(context);
                        Call<ResponseObject> call;
                        call = service.getVisitPlanDetailByCustomer("JWT " + userUtil.getJWTTOken(), planUtil.getPlanId(), customerCode);

                        /*Log the URL called*/
                        if (Constants.DEBUG) {
                            Log.i("URL Called", call.request().url() + "");
                        }

                        call.enqueue(new Callback<ResponseObject>() {
                            @Override
                            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                                if (response.body() != null && response.body().getError() == 0) { //if sukses
                                    try {
                                        final Gson gson = new Gson();
                                        Destination dest = gson.fromJson(response.body().getData().toString(), Destination.class);

                                        /* set textView at here */
                                        try {
                                            if (dest.getArrivalTime() != null) {
                                                String[] arrive = dest.getArrivalTime().split(" ");
                                                if (arrive != null && arrive.length > 0) {
                                                    tvArrivedAt.setText(arrive[1]);
                                                } else {
                                                    // BUG: Pusat masalah, dimana driver dapat melakukan pesanan walaupun belum di lokasi
                                                    /*
                                                     * Alasan Bug:
                                                     *   Sebab kode dibawah hanya membaca customerCode, dan jika customer code tidak kosong maka akan di prosses
                                                     * Solusi :
                                                     *   customerCode akan di kosongkan setelah tap NFC jika sales belum di lokasi customer.
                                                     */
                                                    if (nfcCode.trim().contentEquals(customerCode.trim())) {
                                                        ConversionDate conversionDate = new ConversionDate();
                                                        tvArrivedAt.setText(conversionDate.timeNow());
                                                    } else {
                                                        tvArrivedAt.setText("-");
                                                    }
                                                }
                                            } else {
                                                // BUG: Pusat masalah, dimana driver dapat melakukan pesanan walaupun belum di lokasi
                                                /*
                                                 * Alasan Bug:
                                                 *   Sebab kode dibawah hanya membaca customerCode, dan jika customer code tidak kosong maka akan di prosses
                                                 * Solusi :
                                                 *   customerCode akan di kosongkan setelah tap NFC jika sales belum di lokasi customer.
                                                 */
                                                if (nfcCode.trim().contentEquals(customerCode.trim())) {
                                                    ConversionDate conversionDate = new ConversionDate();
                                                    tvArrivedAt.setText(conversionDate.timeNow());
                                                } else {
                                                    tvArrivedAt.setText("-");
                                                }
                                            }
                                            if (dest.getDepartureTime() != null) {
                                                String[] departure = dest.getDepartureTime().split(" ");
                                                if (departure.length > 0) {
                                                    tvDeparture.setText(departure[1]);
                                                } else {
                                                    tvDeparture.setText("-");
                                                }
                                            } else {
                                                tvDeparture.setText("-");
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        tvLastVisited.setText(ConversionDate.getInstance().fullFormatDate(dest.getLastVisitedDate()));
                                        tvLastOrdered.setText(ConversionDate.getInstance().fullFormatDate(dest.getLastRequestOrder()));
                                        tvInvoices.setText("Rp. " + Currency.priceWithoutDecimal(dest.getInvoiceAmmount()));
                                        tvJmlInvoice.setText(" " + dest.getInvoices().size() + "");
                                        tvCustomerName.setText(dest.getCustomerName());
                                        tvAddress.setText(dest.getAddress());
                                        tvPhone.setText(dest.getPicMobile());
                                        tvNotes.setText(dest.getNote());
                                        tvNamaPIC.setText(dest.getPicName());
                                        tvPosisiPIC.setText(dest.getPicJobPosition());

                                        // set isSet to true
                                        isSet = true;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseObject> call, Throwable t) {
                                Toasty.error(context, "Tidak ada koneksi internet").show();
                            }
                        });
                    }

                    if(!isSet){
                        try{
                            if(destination.getArrivalTime()!=null){
                                String[] arrive = destination.getArrivalTime().split(" ");
                                if(arrive!=null && arrive.length>0){
                                    tvArrivedAt.setText(arrive[1]);
                                }else{
                                    // BUG: Pusat masalah, dimana driver dapat melakukan pesanan walaupun belum di lokasi
                                    /*
                                     * Alasan Bug:
                                     *   Sebab kode dibawah hanya membaca customerCode, dan jika customer code tidak kosong maka akan di prosses
                                     * Solusi :
                                     *   customerCode akan di kosongkan setelah tap NFC jika sales belum di lokasi customer.
                                     */
                                    if(nfcCode.trim().contentEquals(customerCode.trim())){
                                        ConversionDate conversionDate = new ConversionDate();
                                        tvArrivedAt.setText(conversionDate.timeNow());
                                    }else{
                                        tvArrivedAt.setText("-");
                                    }
                                }
                            }else{
                                // BUG: Pusat masalah, dimana driver dapat melakukan pesanan walaupun belum di lokasi
                                /*
                                 * Alasan Bug:
                                 *   Sebab kode dibawah hanya membaca customerCode, dan jika customer code tidak kosong maka akan di prosses
                                 * Solusi :
                                 *   customerCode akan di kosongkan setelah tap NFC jika sales belum di lokasi customer.
                                 */
                                if(nfcCode.trim().contentEquals(customerCode.trim())){
                                    ConversionDate conversionDate = new ConversionDate();
                                    tvArrivedAt.setText(conversionDate.timeNow());
                                }else{
                                    tvArrivedAt.setText("-");
                                }
                            }

                            if(destination.getDepartureTime()!=null){
                                String[] depature = destination.getDepartureTime().split(" ");
                                if(depature.length>0){
                                    tvDeparture.setText(depature[1]);
                                }else{
                                    tvDeparture.setText("-");
                                }
                            }else{
                                tvDeparture.setText("-");
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        tvLastVisited.setText(ConversionDate.getInstance().fullFormatDate(destination.getLastVisitedDate()));
                        tvLastOrdered.setText(ConversionDate.getInstance().fullFormatDate(destination.getLastRequestOrder()));
                        tvInvoices.setText("Rp. "+Currency.priceWithoutDecimal(destination.getInvoiceAmmount()));
                        tvJmlInvoice.setText(" "+destination.getInvoices().size()+"");
                        tvCustomerName.setText(destination.getCustomerName());
                        tvAddress.setText(destination.getAddress());
                        tvPhone.setText(destination.getPicMobile());
                        tvNotes.setText(destination.getNote());
                        tvNamaPIC.setText(destination.getPicName());
                        tvPosisiPIC.setText(destination.getPicJobPosition());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return view;
    }

    private void processCheckinSubmit(Destination destination){
        final Gson gson = new Gson();
        // check status Constants.NFC_CODE,
        // if null it means user tap out
        // if containt value it means user did not tap out, forced tapout
        if(!userUtil.getStringProperty(Constants.NFC_CODE).equals("")){
            // forcedTapout(userUtil.getStringProperty(Constants.NFC_CODE));
            forcedTapoutSynchronous(userUtil.getStringProperty(Constants.NFC_CODE));
        }

        userUtil.setStringProperty(Constants.NFC_CODE, customerCode);
        userUtil.setStringProperty(Constants.CURRENT_PAGE_PLAN, destination.getCustomerCode());
        postState("IN", customerCode, false);

        // set service
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            userUtil.setstatusStopServiceIdleTime();
            userUtil.setStarUnloading();
            userUtil.setTimeVisitCustomer();
            /* call service startVisitService() */
            getActivity().startService(new Intent(getActivity(), VisitService.class));
        } else {
            userUtil.setStarVisit();
            userUtil.setstatusStopServiceIdleTime();
            /* call service startVisitService() */
            getActivity().startService(new Intent(getActivity(), VisitService.class));
            userUtil.setTimeVisitCustomer();

        }

        //set list visited customer
        setListVisitedCustomer();

        // Add re-set user to socket IO
        // prevent server restart
        userSetSocketIO();

        // disable button after checkin
        btnCheckIn.setEnabled(false);

        Toasty.info(context, "Anda sedang Checkin di customer "+customerName).show();
        Intent intent = new Intent(context, DetailPlanActivity.class);
        intent.putExtra("destination", gson.toJson(destination, new TypeToken<Destination>() {
        }.getType()));
        intent.putExtra("customer_code", destination.getCustomerCode());
        intent.putExtra("customer_name", destination.getCustomerName());
        startActivity(intent);
        getActivity().finish();
    }

    private void setListVisitedCustomer(){
        ArrayList<String> set= userUtil.getArrayList(Constants.LIST_VISITED_CUSTOMER);
        if(set==null){
            ArrayList<String> newList= new ArrayList<>();
            newList.add(customerCode);
            userUtil.saveArrayList(Constants.LIST_VISITED_CUSTOMER, newList);
        } else {
            ArrayList<String> listVisitedCustomer = new ArrayList<>(set);
            listVisitedCustomer.add(customerCode);
            userUtil.saveArrayList(Constants.LIST_VISITED_CUSTOMER, listVisitedCustomer);
        }

    }

    /**
     * checkin to customer
     */
    private void buttonCheckinSubmit(){

        boolean statusTapIn = userUtil.getBooleanProperty(Constants.STATUS_OUT_OFFICE);
        if (statusTapIn) {
            PlanUtil planUtil = PlanUtil.getInstance(context);

            /*
            if (userUtil.getStringProperty(Constants.TAP_OUT_TIMEOUT).equals("") || userUtil.getStringProperty(Constants.TAP_OUT_TIMEOUT).equals(null)) {
                // Jika Tap Out Timeout Kosong
                canTapIn = true;
            }
            else {
                try {
                    Date date_current, timeOutCheck;
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
                    Calendar calendar = Calendar.getInstance();
                    String strDate = simpleDateFormat.format(calendar.getTime());
                    date_current = simpleDateFormat.parse(strDate);
                    timeOutCheck = simpleDateFormat.parse(userUtil.getStringProperty(Constants.TAP_OUT_TIMEOUT));

                    long diff = date_current.getTime() - timeOutCheck.getTime();
                    long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);
                    if (seconds > Constants.TAP_OUT_TIME_TIMEOUT) {
                        canTapIn = true;
                        userUtil.setStringProperty(Constants.TAP_OUT_TIMEOUT, "");
                    } else {
                        Toasty.info(context, seconds + " Detik lagi sebelum melakukan Check in kembali").show();
                    }
                } catch (Exception e) {
                    Toasty.info(context, "Kesalahan Aplikasi - Pada Saat Check in").show();
                }
            }
            */

            // Remove canTapIn, cz its useless like you
//            if (canTapIn) {
            if (location == null) {
                location = getLastLocationService();
            }

            Call<ResponseObject> call;
            if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
                /* API service for Driver */
                call = service.getDeliveryPlanDetailByCustomer("JWT " + userUtil.getJWTTOken(), planUtil.getPlanId(), customerCode);
            } else {
                /* API service for Sales */
                call = service.getVisitPlanDetailByCustomer("JWT " + userUtil.getJWTTOken(), planUtil.getPlanId(), customerCode);
            }

            // call.execute()
            call.enqueue(new Callback<ResponseObject>() {
                @Override
                public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                    if (response.body() != null && response.body().getError() == 0) { //if sukses
                        try {
                            final Gson gson = new Gson();
                            final Destination destination = gson.fromJson(response.body().getData().toString(),Destination.class);
                            if (destination.getCustomerCode() != null) {
                                //cek jarak koordinat user dng customer
                                if (location != null) {
                                    if(Constants.IS_CHECK_FAKE_GPS){
                                        if(DetectFakeGPS.getInstance().isMockLocationOn(location, context)){
                                            location = getLastLocationService();
//                                            Toasty.error(context, "Matikan Aplikasi Fake GPS dan Restart Device Android Anda").show();
                                            DetectFakeGPS.getInstance().alertMockLocation(getActivity());
                                            return;
                                        }
                                    }

                                    DistanceCalculator distanceCalculator = new DistanceCalculator();
                                    double jarak = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(customerLatitude, customerLongitude));
                                    if (jarak < Constants.TOLERANSI_JARAK) {
                                        customCheckin = false;
                                        processCheckinSubmit(destination);
                                    } else {
                                        showDialogCustomCheckin(destination);

                                        /*
                                         *  Asalnya sales harus check in di daerah customer
                                         *  tapi sekarang di ubah menjadi semua staff bisa check in dimana saja
                                         *
                                        if(userUtil.isCollectorOnly() || userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
                                            showDialogCustomCheckin(destination);
                                        } else {
                                            Toasty.info(context, "Anda belum berada di Lokasi customer ").show();
                                        }
                                         */
                                    }
                                } else {
                                    location = getLastLocationService();
                                    Toasty.error(context, "Lokasi tidak terbaca, pastikan GPS aktif").show();
                                }
                            } else {
                                Toasty.error(context, "Customer tidak terdaftar").show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseObject> call, Throwable t) {
                    Toasty.error(context, "Tidak ada koneksi internet").show();
                }
            });
//            }
        } else {
            Toasty.info(context, "Anda belum memulai Tap Start dikantor.").show();
        }
    }

    private void processCheckoutSubmit(){
        // function from userUtil,
        // for collector, set START_VISIT_CUSTOMER if Confirm OKE to checkout
        userUtil.setStringProperty(Constants.START_VISIT_CUSTOMER,"");

        userUtil.setStringProperty(Constants.NFC_CODE, "");

        postState("OUT", customerCode, false);

        // userUtil.setNfcCodeRoute(customerCode);
        if(isNextDestinationValid(userUtil.getNfcCodeRoute(), customerCode)){
            userUtil.setNfcCodeRoute(customerCode);
        }

        /* stop service VisitService() */ //stopVisitSevice();
        getActivity().stopService(new Intent(getActivity(), VisitService.class));

        userUtil.setIntProperty(Constants.ADDITIONAL_TIME_PERMISSION, 0);
        userUtil.removeStartVisit();
        userUtil.setstatusStopServiceIdleTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        userUtil.setStringProperty(Constants.TAP_OUT_TIMEOUT, dateFormat.format(calendar.getTime()));
        // Add re-set user to socket IO
        // prevent server restart
        userSetSocketIO();

        Toasty.info(context, "Checkout dari Customer " + customerName + " Berhasil").show();
        getActivity().finish();
    }

    /**
     * checkout from customer
     */
    private void buttonCheckoutSubmit(){
        if (location == null) {
            location = getLastLocationService();
        }

        if(userUtil.getNfcCode().equals(null) || userUtil.getNfcCode().equals("")){
            Toasty.info(context, "Anda belum melakukan checkin").show();
        } else {
            if (userUtil.getStringProperty(Constants.NFC_CODE).equals(userUtil.getStringProperty(Constants.CURRENT_PAGE_PLAN))) {
                if (userUtil.getTimeVisitCustomer() != "") {
                    if (userUtil.checkTimeVisitCustomer()) {
                        //cek jarak koordinat user dng customer
//                        if (location != null) {
                            if(Constants.IS_CHECK_FAKE_GPS){
                                if(DetectFakeGPS.getInstance().isMockLocationOn(location, context)){
                                    location = getLastLocationService();
                                    DetectFakeGPS.getInstance().alertMockLocation(getActivity());
//                                    Toasty.error(context, "Matikan Aplikasi Fake GPS dan Restart Device Android Anda").show();
                                    return;
                                }
                            }
                            DistanceCalculator distanceCalculator = new DistanceCalculator();
                            double jarak = distanceCalculator.greatCircleInMeters(new LatLng(location.getLatitude(), location.getLongitude()), new LatLng(customerLatitude, customerLongitude));
                            if (jarak < Constants.TOLERANSI_JARAK) {
                                customCheckout = false;
                                processCheckoutSubmit();

                            } else {
                                showDialogCustomCheckout();
//                                if(userUtil.isCollectorOnly() || userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
//                                    showDialogCustomCheckout();
//                                } else {
//                                    Toasty.info(context, "Anda belum berada di Lokasi customer ").show();
//                                }
                            }
//                        } else {
//                            location = getLastLocationService();
//                            Toasty.error(context, "Lokasi tidak terbaca, pastikan GPS aktif").show();
//                        }
                    } else {
                        Toasty.info(context, "Waktu kunjungan terlalu sebentar").show();
                    }
                } else {
                    Toasty.info(context, "Anda belum checkin in").show();
                }
            } else {
                Toasty.info(context, "Lokasi Check Out tidak sesuai dengan lokasi Anda Check In sebelumnya.").show();
            }
        }
    }

    private boolean isNextDestinationValid(String previousCode, String currentCode){
        try{
            boolean result = false;
            ArrayList<String> set= userUtil.getArrayList(Constants.LIST_DESTINATION);
            List<String> listDestination = new ArrayList<>(set);

            int index = listDestination.indexOf(previousCode);
            int nextIndex = index+1;
            String nextCode = listDestination.get(nextIndex);
            if(currentCode.equals(nextCode) ){
                result = true;
            }

            // System.out.println("listDestination = " + listDestination );
            // System.out.println("previousCode = " + previousCode );
            // System.out.println("currentCode = " + currentCode );
            // System.out.println("nextIndex = " + nextIndex );
            // System.out.println("nextCode = " + nextCode );

            return result;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /**
     * send data activity to server
     * @param type
     * @param customerCode
     * @param forcedCheckout
     */
    private void postState(String type, String customerCode, boolean forcedCheckout) {
        PlanUtil planUtil = PlanUtil.getInstance(context);
        JsonObject data = new JsonObject();
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            data.addProperty("delivery_plan_id", planUtil.getPlanId());
        } else {
            data.addProperty("visit_plan_id", planUtil.getPlanId());
        }
        data.addProperty("tap_nfc_type", type);
        data.addProperty("nfc_code", customerCode);

        if(forcedCheckout){
            data.addProperty("route_breadcrumb", "");
        } else {
            if(userUtil.isCollectorOnly() || userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
                if(customCheckin || customCheckout){
                    JsonObject breadcrumb = new JsonObject();
                    breadcrumb.addProperty("latitude", location.getLatitude());
                    breadcrumb.addProperty("longitude", location.getLongitude());
                    breadcrumb.addProperty("address", setAddressByLatLng(location.getLatitude(), location.getLongitude()));
                    data.add("route_breadcrumb", breadcrumb);
                } else {
                    data.addProperty("route_breadcrumb", "");
                }
            } else {
                data.addProperty("route_breadcrumb", "");
            }
        }

        // calculate distancePoint
        if(type.equals("IN")){
            userUtil.calculateDistancePoint(location.getLatitude(), location.getLongitude());
            data.addProperty("distance", userUtil.getDistancePoint());
        } else{
            data.addProperty("distance", "");
        }
        data.addProperty("total_distance", "");

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseArrayObject> call = null;
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            call = service.postTapNfcDriver("JWT " + userUtil.getJWTTOken(), data);
        } else {
            call = service.postTapNfcSales("JWT " + userUtil.getJWTTOken(), data);
        }

        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {
                if (response.body() != null && response.body().getError() == 0) {
                    Log.d(">>> from backend", response.body().getData().toString());
                    try {
                        Vibrator v = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            v.vibrate(VibrationEffect.createOneShot(Constants.VIBRATE_DURATION, VibrationEffect.DEFAULT_AMPLITUDE));
                        } else {
                            //deprecated in API 26
                            v.vibrate(Constants.VIBRATE_DURATION);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                t.printStackTrace();
                Toasty.error(context, "Gagal membaca State").show();
            }
        });
    }

    private void forcedTapoutSynchronous(final String customerCode){
        new Thread(new Runnable(){
            @Override
            public void run() {
                try{
                    PlanUtil planUtil = PlanUtil.getInstance(context);
                    JsonObject data = new JsonObject();
                    if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
                        data.addProperty("delivery_plan_id", planUtil.getPlanId());
                    } else {
                        data.addProperty("visit_plan_id", planUtil.getPlanId());
                    }
                    data.addProperty("tap_nfc_type", "OUT");
                    data.addProperty("nfc_code", customerCode);
                    data.addProperty("route_breadcrumb", "");
                    data.addProperty("distance", "");
                    data.addProperty("total_distance", "");

                    Call<ResponseArrayObject> call = null;
                    if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
                        call = service.postTapNfcDriver("JWT " + userUtil.getJWTTOken(), data);
                    } else {
                        call = service.postTapNfcSales("JWT " + userUtil.getJWTTOken(), data);
                    }
                    try {
                        Response<ResponseArrayObject> response = call.execute(); //execute() for synchronous operations
                        if (response.body() != null && response.body().getError() == 0) {
                            // set all variable like process tap out
                            userUtil.setNfcCodeRoute(customerCode);
                            getActivity().stopService(new Intent(getActivity(), VisitService.class));
                            userUtil.setIntProperty(Constants.ADDITIONAL_TIME_PERMISSION, 0);
                            userUtil.removeStartVisit();
                            userUtil.setstatusStopServiceIdleTime();

                            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                            Calendar calendar = Calendar.getInstance();
                            userUtil.setStringProperty(Constants.TAP_OUT_TIMEOUT, dateFormat.format(calendar.getTime()));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void forcedTapout(String customerCode){
        // clear NFC_CODE
        // userUtil.setStringProperty(Constants.NFC_CODE, "");

        // post data to backend
        postState("OUT", customerCode, true);

        // set all variable like process tap out
        userUtil.setNfcCodeRoute(customerCode);
        getActivity().stopService(new Intent(getActivity(), VisitService.class));
        userUtil.setIntProperty(Constants.ADDITIONAL_TIME_PERMISSION, 0);
        userUtil.removeStartVisit();
        userUtil.setstatusStopServiceIdleTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        userUtil.setStringProperty(Constants.TAP_OUT_TIMEOUT, dateFormat.format(calendar.getTime()));
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

    private void userSetSocketIO(){
        //set user
        Socket socketUserSet;
        try {
            socketUserSet = IO.socket(Constants.API_SERVER_ADDR);
            socketUserSet.connect();
            JSONObject jsonObject = new JSONObject();
            if (location != null) {
                if (Constants.DEBUG) {
                    Log.i("lat", location.getLatitude() + "");
                    Log.i("lng", location.getLongitude() + "");
                }
                jsonObject.put("lat", location.getLatitude());
                jsonObject.put("lng", location.getLongitude());
            } else {
                jsonObject.put("lat", "9999");
                jsonObject.put("lng", "999");
            }

            jsonObject.put("user_id", userUtil.getId());
            jsonObject.put("name", userUtil.getName());
            jsonObject.put("job_function", userUtil.getRoleUser());
            jsonObject.put("branch_id", userUtil.getBranchId());
            jsonObject.put("division_id", userUtil.getDivisionId());

            // add asset information
            jsonObject.put("asset_code", "");
            jsonObject.put("asset_name", "");
            if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
                String[] asset =  PlanUtil.getInstance(context).getAsset();
                jsonObject.put("asset_code", asset[0]);
                jsonObject.put("asset_name", asset[1]);
            }

            socketUserSet.emit("user-set", jsonObject);

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void showDialogCustomCheckin(final Destination destination){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Konfirmasi");
        String message = "Anda tidak berada dilokasi customer " + customerName + ", apakah Anda yakin akan melakukan checkin di lokasi sekarang ?";
        alertDialogBuilder
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        customCheckin = true;
                        processCheckinSubmit(destination);
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

    private void showDialogCustomCheckout(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setTitle("Konfirmasi");
        String message = "Apakah Anda yakin akan melakukan checkout di lokasi sekarang ?";
        alertDialogBuilder
                .setMessage(message)
                .setIcon(R.mipmap.ic_launcher)
                .setCancelable(false)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        customCheckout = true;
                        processCheckoutSubmit();
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

    private String setAddressByLatLng(double lat, double lng) {
        String address = "";
        try {
            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

            if (geocoder.isPresent()) {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                Address returnAddress = addresses.get(0);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < returnAddress.getMaxAddressLineIndex(); i++) {
                    sb.append(returnAddress.getAddressLine(i)).append(" ");
                }
                sb.append(returnAddress.getLocality()).append(" ");
                sb.append(returnAddress.getPostalCode()).append(" ");
                sb.append(returnAddress.getCountryName());

                if (addresses.size() > 0) {
                    address = addresses.get(0).getAddressLine(0);
                }
            } else {
                Toasty.info(context, "Geocoder not present").show();
            }
            return address;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return address;
    }

}


