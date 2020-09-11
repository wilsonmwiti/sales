package id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.RequestOrder;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;

import java.util.List;

/**
 * Class RequestOrderAdapter
 * adapter for request order
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.RequestOrder.RequestOrderFragment}
 */
public class RequestOrderAdapter extends ArrayAdapter<RequestOrder> {

    private Context context;
    private List<RequestOrder> requestOrders;

    public RequestOrderAdapter(@NonNull Context context, int resource, @NonNull List<RequestOrder> requestOrders) {
        super(context, resource, requestOrders);
        this.context =context;
        this.requestOrders = requestOrders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RequestOrder requestOrder = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_request_order,parent,false);
        }
        try{
            TextView tvCustomer = (TextView) convertView.findViewById(R.id.tv_nama_customer);
            TextView tvRequestNo = (TextView) convertView.findViewById(R.id.tv_request_no);
            TextView tvOrderDate = (TextView) convertView.findViewById(R.id.tv_order_date);
            TextView tvStatus = (TextView) convertView.findViewById(R.id.tv_status);

            tvCustomer.setText(requestOrder.getCustomer().getNama());
            tvRequestNo.setText(requestOrder.getCode());
            String dateRO   = ConversionDate.getInstance().fullFormatDate(requestOrder.getCreateDate());
            tvOrderDate.setText(dateRO);
            tvStatus.setText(requestOrder.getType());
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
