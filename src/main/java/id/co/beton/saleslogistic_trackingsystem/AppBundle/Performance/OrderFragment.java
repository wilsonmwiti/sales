package id.co.beton.saleslogistic_trackingsystem.AppBundle.Performance;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.DataSalesInvoice;
import id.co.beton.saleslogistic_trackingsystem.Model.Statistic;
import id.co.beton.saleslogistic_trackingsystem.Model.StatisticUser;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Class OrderFragment
 * data report order sales/logistic
 *
 */
public class OrderFragment extends Fragment {
    private static final String TAG = OrderFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    private TextView tvTotalRequestOrder;
    private TextView tvTotalRoSpecial;
    private TextView tvTotalRoCanceled;
    private TextView tvTotalSoCanceled;
    private TextView tvTotalPackingSlip;
    private TextView tvTotalDelivered;
    private TextView tvTotalInvoiceDua;
    //private TextView tvTotalJatuhTempo;
    private TextView tvTotalReprintPayment;
    private TextView tvTotalPaymentCanceled;
    // private TextView tvTotalNvcReport;
    private TextView tvTotalLocationReport;
    private TextView tvTotalPrinterReport;
    private TextView tvTotalSalesOrder;
    private TextView tvTotalInvoiceHarga;
    private TextView tvTotalPaymentDua;
    private TextView tvTotalInvoice;
    private TextView tvTotalPayment;
    private TextView tvTotalSalesOrderPositif;
    private TextView tv_total_payment_positif;
    private TextView tv_today;
    UserUtil userUtil;
    private ApiInterface service;

    LinearLayout lrActivity1,lrActivity2,lrActivity3,lrActivity4,lrActivity5,lrActivity6,ll_ro_so_canceled;
    private ProgressBar progressBar;
    private DataSalesInvoice dataSalesInvoice;

    public OrderFragment() {
    }

    public static OrderFragment newInstance(String tabSelected) {
        OrderFragment fragment = new OrderFragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_list_performance_order, container, false);
        view.setTag(TAG);

        try{
            tvTotalRequestOrder = view.findViewById(R.id.tv_total_request_order);
            tvTotalRoSpecial = view.findViewById(R.id.tv_total_ro_special);
            tvTotalRoCanceled = view.findViewById(R.id.tv_total_ro_canceled);
            tvTotalSoCanceled = view.findViewById(R.id.tv_total_so_canceled);
            tvTotalPackingSlip = view.findViewById(R.id.tv_total_packing_slip);
            tvTotalDelivered = view.findViewById(R.id.tv_total_delivered);
            tvTotalInvoiceDua = view.findViewById(R.id.tv_total_invoice_dua);
            //tvTotalJatuhTempo = view.findViewById(R.id.tv_total_jatuh_tempo);
            tvTotalReprintPayment = view.findViewById(R.id.tv_total_reprint_payment);
            tvTotalPaymentCanceled = view.findViewById(R.id.tv_total_payment_canceled);
            // tvTotalNvcReport = view.findViewById(R.id.tv_total_nvc_report);
            tvTotalLocationReport = view.findViewById(R.id.tv_total_report_location);
            tvTotalPrinterReport = view.findViewById(R.id.tv_total_printer_report);
            tvTotalSalesOrder = view.findViewById(R.id.tv_total_sales_order);
            tvTotalInvoiceHarga = view.findViewById(R.id.tv_total_invoice_harga);
            tvTotalPaymentDua = view.findViewById(R.id.tv_total_payment_dua);
            tvTotalInvoice = view.findViewById(R.id.tv_total_invoice);
            tvTotalPayment = view.findViewById(R.id.tv_total_payment);
            progressBar = view.findViewById(R.id.progressBar);
            tvTotalSalesOrderPositif = view.findViewById(R.id.tv_total_sales_order_positif);
            tv_total_payment_positif= view.findViewById(R.id.tv_total_payment_positif);
            tv_today = view.findViewById(R.id.tv_today);
            tv_today.setText(ConversionDate.getInstance().today());

            lrActivity1 = (LinearLayout) view.findViewById(R.id.lr_delivery_1);
            lrActivity2 = (LinearLayout) view.findViewById(R.id.lr_delivery_2);
            lrActivity3 = (LinearLayout) view.findViewById(R.id.lr_delivery_3);
            lrActivity4 = (LinearLayout) view.findViewById(R.id.lr_delivery_4);
            lrActivity5 = (LinearLayout) view.findViewById(R.id.lr_delivery_5);
            lrActivity6 = (LinearLayout) view.findViewById(R.id.lr_delivery_6);
            ll_ro_so_canceled = (LinearLayout) view.findViewById(R.id.ll_ro_so_canceled);

            userUtil = UserUtil.getInstance(container.getContext());
            service = ApiClient.getInstance(container.getContext());

            if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){

                TextView tvDelivery = view.findViewById(R.id.tv_terkirim);
                TextView tvNewStopPoint = view.findViewById(R.id.tv_dibatalkan);
                TextView tvUnloadingTime = view.findViewById(R.id.tv_salah_alamat);
                TextView tvDeliveryCancelled = view.findViewById(R.id.tv_pindah_alamat);
                TextView tvOrderPerformance = view.findViewById(R.id.tv_order_performance);
                TextView tvInvoice = view.findViewById(R.id.tv_invoice);
                TextView tvTotalInvoices = view.findViewById(R.id.tv_total_invoices);
                TextView tvTotalPayments = view.findViewById(R.id.tv_total_payments);

                tvDelivery.setText("Delivered");
                tvNewStopPoint.setText("Undelivered");
                tvUnloadingTime.setText(R.string.salah_alamat);
                tvDeliveryCancelled.setText(R.string.pindah_alamat);

                tvOrderPerformance.setText(R.string.delivery_performance);
                tvInvoice.setText(R.string.delivery);
                tvTotalInvoices.setText("Total Packing Slip");
                tvTotalPayments.setText(R.string.total_delivered);

                lrActivity1.setVisibility(View.GONE);
                lrActivity2.setVisibility(View.GONE);
                lrActivity3.setVisibility(View.GONE);
                lrActivity4.setVisibility(View.GONE);
                lrActivity5.setVisibility(View.GONE);
                lrActivity6.setVisibility(View.GONE);
                ll_ro_so_canceled.setVisibility(View.GONE);
            }
            ll_ro_so_canceled.setVisibility(View.GONE);

            loadData();
        }catch (Exception e){
            e.printStackTrace();
        }
        //salesInvoice();

        return view;
    }

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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void updateStatistic(Statistic statistic){
        try{
            if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
                float full = (statistic.getPlan()>0)?statistic.getPlan():1;
                float progress = (statistic.getVisited()>0)?statistic.getVisited():1;
                float percent;

                percent = (progress/full)*100;

                if(Constants.DEBUG){
                    Log.wtf(TAG, "(progress/full) : "+(progress/full));
                    Log.wtf(TAG, "Percent : "+percent);
                }

                progressBar.setProgress((int) percent);

                tvTotalInvoice.setText(statistic.getPackingSlip()+""); //total packing slip
                tvTotalPayment.setText(statistic.getPackingSlipAccept()+"");
                tvTotalRequestOrder.setText(statistic.getPackingSlipAccept()+"");
                tvTotalRoSpecial.setText(""+statistic.getPackingSlipCancel());

                tvTotalRoCanceled.setText("-"); //hide
                tvTotalSoCanceled.setText("-"); //hide
            }else{
                tvTotalInvoice.setText("Rp. "+ Currency.priceWithoutDecimal(statistic.getInvoiceAmount()));
                tvTotalPayment.setText("Rp. "+Currency.priceWithoutDecimal(statistic.getPaymentAmount()));

                float full = (statistic.getInvoice()>0)?statistic.getInvoice():1;
                float progress = (statistic.getPayment()>0)?statistic.getPayment():1;
                float percent;

                percent = (progress/full)*100;

                if(Constants.DEBUG){
                    Log.wtf(TAG, "(progress/full) : "+(progress/full));
                    Log.wtf(TAG, "Percent : "+percent);
                }

                progressBar.setProgress((int) percent);

                tvTotalRequestOrder.setText(""+statistic.getRequestOrder());
                tvTotalRoSpecial.setText(""+statistic.getRequestOrderSpecial());

                tvTotalRoCanceled.setText("-"); //hide
                tvTotalSoCanceled.setText("-"); //hide

                tvTotalPackingSlip.setText(""+statistic.getPackingSlip());
                tvTotalDelivered.setText("-");
                //tvTotalJatuhTempo.setText("-");


                tvTotalReprintPayment.setText(""+statistic.getReprint());
                tvTotalPaymentCanceled.setText(""+statistic.getPaymentCancel());
                // tvTotalNvcReport.setText(""+statistic.getReportNfc());
                tvTotalLocationReport.setText(""+statistic.getReportLocation());
                tvTotalPrinterReport.setText(""+statistic.getReportPrint());

                tvTotalSalesOrder.setText(""+statistic.getSalesOrder());
                tvTotalSalesOrderPositif.setText("+"+Currency.priceWithoutDecimal(statistic.getSalesOrderAmount()));

                tvTotalInvoiceHarga.setText("Rp. "+Currency.priceWithoutDecimal(statistic.getInvoiceAmount()));
                tvTotalInvoiceDua.setText(""+statistic.getInvoice());

                tv_total_payment_positif.setText("Rp. "+Currency.priceWithoutDecimal(statistic.getPaymentAmountWoConfirm()));
                tvTotalPaymentDua.setText(""+statistic.getPaymentWoConfirm());
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    private void loadData(){

        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(getContext());

        /*Create handle for the RetrofitInstance interface*/


        Map<String, String> params = new HashMap<String, String>();

//        params.put("user_id", "[79]");
        params.put("user_id", "["+UserUtil.getInstance(getActivity().getApplicationContext()).getId()+"]");

        /*Call the method with parameter in the interface to get the data*/
        Call<StatisticUser> call=null;
        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            call = service.userStatistic("JWT "+userUtil.getJWTTOken(),"logistic",params);
        }else{
            call = service.userStatistic("JWT "+userUtil.getJWTTOken(),"sales",params);
        }

        /*Callback*/
        call.enqueue(new Callback<StatisticUser>() {
            @Override
            public void onResponse(Call<StatisticUser> call, Response<StatisticUser> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    Gson gson =new Gson();
                    updateStatistic(response.body().getData().get(""+UserUtil.getInstance(getActivity().getApplicationContext()).getId()));
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
            public void onFailure(Call<StatisticUser> call, Throwable t) {
                Toasty.error(getActivity(), "Koneksi Internet Bermasalah",
                        Toast.LENGTH_SHORT).show();
                if(Constants.DEBUG){
                    Log.wtf(TAG, "Koneksi Internet Bermasalah");
                }
            }
        });
    }
}
