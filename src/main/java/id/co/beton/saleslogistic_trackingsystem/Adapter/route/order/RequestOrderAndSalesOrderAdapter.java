package id.co.beton.saleslogistic_trackingsystem.Adapter.route.order;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.RequestOrderAndSalesOrderData;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.util.List;

/**
 * Class RequestOrderAndSalesOrderAdapter
 * Adapter for request order and sales order
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Order.OrderFragment}
 */
public class RequestOrderAndSalesOrderAdapter extends ArrayAdapter<RequestOrderAndSalesOrderData> {
    private Context context;
    private List<RequestOrderAndSalesOrderData> requestOrderAndSalesOrderData;
    public RequestOrderAndSalesOrderAdapter(@NonNull Context context, int resource, @NonNull List<RequestOrderAndSalesOrderData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.requestOrderAndSalesOrderData =objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        RequestOrderAndSalesOrderData requestOrderAndSalesOrderData = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_request_and_sales_order,parent,false);
        }

        try{
            TextView tvRequestOrderNo = convertView.findViewById(R.id.tv_request_order_no);
            TextView tvOrderDate = convertView.findViewById(R.id.tv_order_date);
            TextView tvStatus = convertView.findViewById(R.id.tv_status_request_order);
            TextView tvInvoiceAmmount = convertView.findViewById(R.id.tv_invoice_amount);

            tvRequestOrderNo.setText(requestOrderAndSalesOrderData.getCode());
            tvOrderDate.setText(ConversionDate.getInstance().fullFormatDate(requestOrderAndSalesOrderData.getCreateDate()));
            if(requestOrderAndSalesOrderData.getStatus()!=null){
                tvStatus.setText(requestOrderAndSalesOrderData.getStatus());
            }else{
                tvStatus.setText(requestOrderAndSalesOrderData.getType());
            }

            if(requestOrderAndSalesOrderData.getInvoiceAmount()!=null && requestOrderAndSalesOrderData.getInvoiceAmount()!=0){
                tvInvoiceAmmount.setVisibility(View.VISIBLE);
                tvInvoiceAmmount.setText("Rp "+Currency.priceWithoutDecimal(requestOrderAndSalesOrderData.getInvoiceAmount()));
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
