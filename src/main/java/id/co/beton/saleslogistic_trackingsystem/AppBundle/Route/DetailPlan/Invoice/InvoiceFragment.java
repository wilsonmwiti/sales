package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Invoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.invoices.InvoiceAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
import id.co.beton.saleslogistic_trackingsystem.Model.Plan;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * Class InvoiceFragment
 * list of data invoice
 */
public class InvoiceFragment extends Fragment {
    private static final String TAG = InvoiceFragment.class.getSimpleName();
    private static List<Invoice> invoices;
    private ListView lvVisitPlan;
    private CheckBox cbSelectAll;
    private static RelativeLayout rlShowCheck;
    private InvoiceAdapter invoiceConfirmAdapter;
    private static TextView tvTotalInvoice;
    private static TextView tvNumberOfInvoice;
    private TextView tvTotalInvoices;
    private TextView tvNumberOfInvoices;
    private TextView tvTanggal;
    private Context context;
    private Plan plan;
    private Destination destination;
    String customerName;
    String customerCode;
    private static final int REQUEST_PROSES_PAYMENT=1;
    String nfcCode;

    public InvoiceFragment() {
        // Required empty public constructor
    }
    public static InvoiceFragment newInstance(int tabSelected) {
        InvoiceFragment fragment = new InvoiceFragment();
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
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.content_detail_visit_plan_invoice, container, false);

        customerCode = getArguments().getString("cust_code");
        customerName = getArguments().getString("cust_name");

        lvVisitPlan = (ListView) v.findViewById(R.id.lv_invoice);
        tvNumberOfInvoice = (TextView) v.findViewById(R.id.tv_number_of_invoice);
        tvTotalInvoice = (TextView) v.findViewById(R.id.tv_total_invoice);

        View header = inflater.inflate(R.layout.header_visit_plan_invoice,null);
        lvVisitPlan.addHeaderView(header);
        LinearLayout llSelectAll  = (LinearLayout) header.findViewById(R.id.ll_select_all);

        rlShowCheck = (RelativeLayout)v.findViewById(R.id.rl_show_check);

        // data
        invoices = new ArrayList<>();
        context = container.getContext();
        nfcCode = UserUtil.getInstance(context).getStringProperty(Constants.NFC_CODE);

        if (getArguments() != null) {
            Gson gson = new Gson();

            //this.plan = gson.fromJson(getArguments().getString("visit_plan"), new TypeToken<Plan>(){}.getType());
            this.destination = gson.fromJson(getArguments().getString("destination"), new TypeToken<Destination>(){}.getType());

            invoices = destination.getInvoices();

            // textview buat header
            tvTanggal = (TextView) v.findViewById(R.id.tv_tanggal);
            tvNumberOfInvoices = (TextView) v.findViewById(R.id.tv_number_of_invoices);
            tvTotalInvoices = (TextView) v.findViewById(R.id.tv_total_invoices);

            tvTanggal.setText(ConversionDate.getInstance().today());

            if(destination.getInvoices().size()==0){
                llSelectAll.setVisibility(View.GONE);
            }

            tvNumberOfInvoices.setText(String.valueOf(destination.getInvoices().size()));
            tvTotalInvoices.setText("Rp."+ Currency.priceWithoutDecimal(destination.getInvoiceAmmount()));
        }

        invoiceConfirmAdapter = new InvoiceAdapter(container.getContext(),1,destination.getInvoices());
        lvVisitPlan.setAdapter(invoiceConfirmAdapter);


        rlShowCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Constants.DEBUG){
                    Intent intent = new Intent(container.getContext(), PaymentActivity.class);
                    List<Invoice> selectedInvoice = new ArrayList<>();
                    for (int k=0;k<invoices.size();k++){
                        if (invoices.get(k).isSelected()){
                            selectedInvoice.add(invoices.get(k));
                        }
                    }
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<Invoice>>() {}.getType();
                    String json = gson.toJson(selectedInvoice, type);
                    intent.putExtra("selected_invoices",json);
                    intent.putExtra("cust_name",customerName);
                    intent.putExtra("cust_code",customerCode);

                    startActivityForResult(intent,REQUEST_PROSES_PAYMENT);
                }else{
                    if(nfcCode.trim().contentEquals(customerCode.trim())){
                        Intent intent = new Intent(container.getContext(), PaymentActivity.class);
                        List<Invoice> selectedInvoice = new ArrayList<>();
                        for (int k=0;k<invoices.size();k++){
                            if (invoices.get(k).isSelected()){
                                selectedInvoice.add(invoices.get(k));
                            }
                        }
                        Gson gson = new Gson();
                        Type type = new TypeToken<List<Invoice>>() {}.getType();
                        String json = gson.toJson(selectedInvoice, type);
                        intent.putExtra("selected_invoices",json);
                        intent.putExtra("cust_name",customerName);
                        intent.putExtra("cust_code",customerCode);

                        startActivityForResult(intent,REQUEST_PROSES_PAYMENT);
                    }else{
                        Toasty.info(context,"Pastikan anda telah Checkin untuk customer "+customerName).show();
                    }
                }
            }
        });

        selectAll(v);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==REQUEST_PROSES_PAYMENT && resultCode== Activity.RESULT_OK){
            invoiceConfirmAdapter.notifyDataSetChanged();
        }
    }

    public void selectAll(View view){
        cbSelectAll = (CheckBox)view.findViewById(R.id.cb_select_all);

        cbSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((CheckBox)v).isChecked())
                {
                    for (int i=0;i<invoices.size();i++){
                        invoices.get(i).setSelected(true);
                    }

                    invoiceConfirmAdapter.notifyDataSetChanged();
                    lvVisitPlan.setAdapter(invoiceConfirmAdapter);
                    InvoiceFragment.InvoiceSelected();
                    rlShowCheck.setVisibility(View.VISIBLE);

                }
                else
                {
                    for (int i=0;i<invoices.size();i++){
                        invoices.get(i).setSelected(false);
                    }
                    invoiceConfirmAdapter.notifyDataSetChanged();
                    lvVisitPlan.setAdapter(invoiceConfirmAdapter);
                    InvoiceFragment.InvoiceSelected();
                    rlShowCheck.setVisibility(View.GONE);
                }
            }
        });
    }

    public static void InvoiceSelected()
    {
        Integer tmpTotalInvoice = 0;
        int tmpNumberInvoice= 0;
        for (int i=0;i<invoices.size();i++){
            if (invoices.get(i).isSelected()){
                tmpTotalInvoice = tmpTotalInvoice + invoices.get(i).getInvoiceAmount();
                tmpNumberInvoice++;
            }
        }

        if (tmpNumberInvoice > 0)
            rlShowCheck.setVisibility(View.VISIBLE);
        else
            rlShowCheck.setVisibility(View.GONE);

        tvTotalInvoice.setText("Rp."+ Currency.priceWithoutDecimal(tmpTotalInvoice)+",-");
        tvNumberOfInvoice.setText(String.valueOf(tmpNumberInvoice));
    }

}
