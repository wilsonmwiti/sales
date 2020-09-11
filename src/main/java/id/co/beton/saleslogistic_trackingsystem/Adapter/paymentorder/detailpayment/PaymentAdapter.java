package id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.detailpayment;

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
 * Class PaymentAdapter
 * Adapter for detail Payment
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.Payment.DetailPaymentActivity}
 *
 */
public class PaymentAdapter extends ArrayAdapter<Invoice> {
    private Context context;
    private List<Invoice> paymentMobileInvoices;
    PaymentInfoAdapter paymentInfoAdapter;
    public PaymentAdapter(@NonNull Context context, int resource, @NonNull List<Invoice> paymentMobileInvoices) {
        super(context, resource, paymentMobileInvoices);
        this.context=context;
        this.paymentMobileInvoices = paymentMobileInvoices;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Invoice paymentMobileInvoice = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_detail_payment,parent,false);
        }
        try{
            TextView tvPaymentNo = (TextView) convertView.findViewById(R.id.tv_payment_no);
            TextView tvTotalInvoice = (TextView) convertView.findViewById(R.id.tv_total_invoice);
            TextView tvPaymentTotal = (TextView) convertView.findViewById(R.id.tv_total_payment);
            /*ListView lv_payment_meethod =(ListView)convertView.findViewById(R.id.lv_payment_meethod);
            if(paymentMobileInvoice.getPaymentInfos()!=null && paymentMobileInvoice.getPaymentInfos().size()>0){
                paymentInfoAdapter = new PaymentInfoAdapter(context,2,paymentMobileInvoices.get(position).getPaymentInfos());
                lv_payment_meethod.setAdapter(paymentInfoAdapter);
            }else{
                lv_payment_meethod.setAdapter(null);
            }*/

            tvPaymentNo.setText(paymentMobileInvoice.getInvoiceId());
            tvTotalInvoice.setText("Rp. "+ Currency.priceWithDecimal(paymentMobileInvoice.getInvoiceAmount()));
            tvPaymentTotal.setText("Rp. "+ Currency.priceWithDecimal(paymentMobileInvoice.getPaymentAmount()));

        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
