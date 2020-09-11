package id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.detailpayment;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.PaymentInfo;
import id.co.beton.saleslogistic_trackingsystem.R;

import java.util.List;

/**
 * Class PaymentInfoAdapter
 *
 * {@link PaymentAdapter}
 */
public class PaymentInfoAdapter extends ArrayAdapter<PaymentInfo> {
    private Context context;
    private List<PaymentInfo> paymentInfos;

    public PaymentInfoAdapter(@NonNull Context context, int resource, @NonNull List<PaymentInfo> paymentInfos) {
        super(context, resource, paymentInfos);
        this.context=context;
        this.paymentInfos = paymentInfos;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PaymentInfo paymentInfo = paymentInfos.get(position);
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_payment_info,parent,false);
        }
        try{
            TextView tvPayamentMethod = (TextView) convertView.findViewById(R.id.tv_payment_method);
            TextView tvBank  = (TextView)convertView.findViewById(R.id.tv_bank);
            TextView tvAccountNo  = (TextView)convertView.findViewById(R.id.tv_account_no);
            TextView tvAccountName  = (TextView)convertView.findViewById(R.id.tv_account_name);
            TextView tvTransfeTo  = (TextView)convertView.findViewById(R.id.tv_transfer_to);

            tvPayamentMethod.setText(paymentInfo.getType().toUpperCase());
            if(!paymentInfo.getType().contentEquals("cash")){
                tvBank.setText(paymentInfo.getBankName()+"");
                tvAccountNo.setText(paymentInfo.getAccountNo()+"");
                tvAccountName.setText(paymentInfo.getAccountName()+"");
                tvTransfeTo.setText(paymentInfo.getTransferTo()+"");
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
