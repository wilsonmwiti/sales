package id.co.beton.saleslogistic_trackingsystem.Adapter.route.payment;

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
 * Adapter for payment
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Payment.PaymentFragment}
 */
public class PaymentAdapter extends ArrayAdapter<Payment> {
    private Context context;
    private List<Payment> paymentMobileDatas;
    public PaymentAdapter(@NonNull Context context, int resource, @NonNull List<Payment> paymentMobileDatas) {
        super(context, resource, paymentMobileDatas);
        this.context=context;
        this.paymentMobileDatas = paymentMobileDatas;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try{
            Payment paymentMobileData = getItem(position);

            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.content_list_detail_visit_payment,parent,false);
            }

            TextView tvPaymentNo = (TextView) convertView.findViewById(R.id.tv_payment_no);
            TextView tvPaymentTotal = (TextView) convertView.findViewById(R.id.tv_payment_total);
            TextView tvStatus = (TextView)convertView.findViewById(R.id.tv_status);
            TextView tvNoInvoice  =(TextView) convertView.findViewById(R.id.tv_no_invoice);
            TextView tvPyamentMethod = (TextView) convertView.findViewById(R.id.tv_payment_method);
            TextView tvReceive  =(TextView) convertView.findViewById(R.id.tv_receive_no);
            TextView tvProsessedBy = (TextView) convertView.findViewById(R.id.tv_processed_by);

            tvPaymentNo.setText(paymentMobileData.getCode());
            if(paymentMobileData.getPaymentAmount()==0){
                tvPaymentTotal.setText("Rp. 0.00");
            } else {
                tvPaymentTotal.setText("Rp. "+ Currency.priceWithDecimal(paymentMobileData.getPaymentAmount()));
            }
            tvStatus.setText(paymentMobileData.getPaymentStatus());
            /*tvPyamentMethod.setText("-");
            if(paymentMobileData.getPaymentInfo().size()>0){
                for (PaymentInfo paymentInfo: paymentMobileData.getPaymentInfo()) {
                    tvPyamentMethod.setText(paymentInfo.getAccountName());
                }
            }*/

            tvNoInvoice.setText(""+paymentMobileData.getInvoice().size());
            tvReceive.setText(paymentMobileData.getReceiptCode());
            tvProsessedBy.setText(""+paymentMobileData.getUser().getEmployeeName());
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }
}
