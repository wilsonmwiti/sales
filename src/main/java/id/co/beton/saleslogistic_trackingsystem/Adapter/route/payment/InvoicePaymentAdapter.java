package id.co.beton.saleslogistic_trackingsystem.Adapter.route.payment;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.util.List;

/**
 * Class InvoicePaymentAdapter
 * Adapter for invoice payment
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Payment.DetailPaymentVisitPlanActivity}
 */
public class InvoicePaymentAdapter extends ArrayAdapter<Invoice> {
    private Context context;
    private List<Invoice> paymentMobileInvoices;

    public InvoicePaymentAdapter(@NonNull Context context, int resource, @NonNull List<Invoice> paymentMobileInvoices) {
        super(context, resource, paymentMobileInvoices);
        this.context=context;
        this.paymentMobileInvoices = paymentMobileInvoices;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try{
            Invoice paymentMobileData = getItem(position);
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.content_list_detail_payment,parent,false);
            }
            TextView tvPaymentNo = (TextView) convertView.findViewById(R.id.tv_payment_no);
            TextView tvTotalInvoice = (TextView) convertView.findViewById(R.id.tv_total_invoice);
            TextView tvPaymentTotal = (TextView) convertView.findViewById(R.id.tv_total_payment);

            tvPaymentNo.setText(paymentMobileData.getInvoiceId());
            tvTotalInvoice.setText("Rp "+ Currency.priceWithoutDecimal(paymentMobileData.getInvoiceAmount())+",-");
            tvPaymentTotal.setText("Rp "+Currency.priceWithoutDecimal(paymentMobileData.getPaymentAmount())+",-");
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
