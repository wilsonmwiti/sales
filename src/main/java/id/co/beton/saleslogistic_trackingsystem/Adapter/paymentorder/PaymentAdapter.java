package id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder;

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
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.util.List;

/**
 * Class PaymentAdapter
 * Adapter for Payment
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.Payment.PaymentFragment}
 */
public class PaymentAdapter extends ArrayAdapter<Payment> {
    private Context context;
    private List<Payment> payments;

    public PaymentAdapter(@NonNull Context context, int resource, @NonNull List<Payment> payments) {
        super(context, resource, payments);
        this.context=context;
        this.payments = payments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Payment payment = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_payment,parent,false);
        }
        try{
            TextView tvCustomer = (TextView) convertView.findViewById(R.id.tv_nama_customer);
            TextView tvPaymentNo = (TextView) convertView.findViewById(R.id.tv_payment_no);
            TextView tvPaymentDate = (TextView) convertView.findViewById(R.id.tv_payment_date);
            TextView tvPaymentTotal = (TextView) convertView.findViewById(R.id.tv_payment_total);
            TextView tvNoInvoice  =(TextView) convertView.findViewById(R.id.tv_no_invoice);
            TextView tvPaymentMethod = (TextView) convertView.findViewById(R.id.tv_payment_method);
            TextView tvReceiveNo  =(TextView) convertView.findViewById(R.id.tv_receive_no);
            TextView tvProsessedBy = (TextView) convertView.findViewById(R.id.tv_processed_by);
            TextView tv_status = (TextView)convertView.findViewById(R.id.tv_status);

            tvCustomer.setText(payment.getCustomerCode());
            tvPaymentNo.setText(payment.getCode());
            tvPaymentDate.setText(payment.getPaymentDate());
            if(payment.getPaymentAmount()==0){
                tvPaymentTotal.setText("Rp. 0.00");
            } else {
                tvPaymentTotal.setText("Rp. "+Currency.priceWithDecimal(payment.getPaymentAmount()));
            }

            tv_status.setText(payment.getPaymentStatus());
            tvNoInvoice.setText(""+payment.getInvoice().get(0).getInvoiceId());
            //tvPaymentMethod.setText(payment.getPaymentMethod());
            tvReceiveNo.setText(payment.getReceiptCode());
            tvProsessedBy.setText(""+payment.getUser().getEmployeeName());
        }catch (Exception e){
            e.printStackTrace();
        }


        return convertView;
    }
}
