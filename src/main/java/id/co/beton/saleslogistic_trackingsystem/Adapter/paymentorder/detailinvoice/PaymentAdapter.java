package id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.detailinvoice;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.Payment;
import id.co.beton.saleslogistic_trackingsystem.R;

import java.util.ArrayList;

/**
 * Class PaymentAdapter
 * Adapter for Detail invoice
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Inbox.DetailInboxActivity}
 */
public class PaymentAdapter extends ArrayAdapter<Payment> {
    private Context context;
    private ArrayList<Payment> payments;

    public PaymentAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Payment> payments) {
        super(context, resource, payments);
        this.context=context;
        this.payments = payments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Payment payment = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_payment_invoice_detail,parent,false);
        }
        try{
            TextView tvCustomer = (TextView) convertView.findViewById(R.id.tv_nama_customer);
            TextView tvPaymentNo = (TextView) convertView.findViewById(R.id.tv_payment_no);
            TextView tvPyamnetDate = (TextView) convertView.findViewById(R.id.tv_payment_date);
            TextView tvPaymentTotal = (TextView) convertView.findViewById(R.id.tv_payment_total);
            TextView tvNoInvoide  =(TextView) convertView.findViewById(R.id.tv_no_invoice);
            TextView tvPyamentMethod = (TextView) convertView.findViewById(R.id.tv_payment_method);
            TextView tvReceive  =(TextView) convertView.findViewById(R.id.tv_receive_no);
            TextView tvProsessedBy = (TextView) convertView.findViewById(R.id.tv_processed_by);
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
