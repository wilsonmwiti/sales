package id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.SalesOrder;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.util.List;

/**
 * Class SalesOrderAdapter
 * Adapter for sales order
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.SalesOrder.SalesOrderFragment}
 */
public class SalesOrderAdapter extends ArrayAdapter<SalesOrder> {
    private Context context;
    private List<SalesOrder> salesOrders;

    public SalesOrderAdapter(@NonNull Context context, int resource, @NonNull List<SalesOrder> salesOrders) {
        super(context, resource, salesOrders);
        this.context=context;
        this.salesOrders=salesOrders;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        SalesOrder salesOrder = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_sales_order,parent,false);
        }
        try{
            TextView tvCustomer = (TextView) convertView.findViewById(R.id.tv_nama_customer);
            TextView tvRequestNo = (TextView) convertView.findViewById(R.id.tv_request_no);
            TextView tvTotal = (TextView) convertView.findViewById(R.id.tv_total_harga_invoice);
            TextView tvOrderDate = (TextView) convertView.findViewById(R.id.tv_order_date);
            TextView tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
            TextView tvSalesOrderDate = (TextView) convertView.findViewById(R.id.tv_sales_order_date);
            TextView tvSalesOrderNo = (TextView) convertView.findViewById(R.id.tv_sales_order_no);
            TextView tvPackingSlipDate = (TextView) convertView.findViewById(R.id.tv_packing_slip_date);
            TextView tvPackingSlipNo= (TextView) convertView.findViewById(R.id.tv_packing_slip_no);

            tvCustomer.setText(salesOrder.getCustomer().getNama());
            tvRequestNo.setText(salesOrder.getCode());
            tvTotal.setText("Rp. "+ Currency.priceWithDecimal(salesOrder.getInvoiceAmount()));
            String salesDate = ConversionDate.getInstance().fullFormatDate(salesOrder.getCreateDate());
            tvOrderDate.setText(salesDate);
            tvStatus.setText(salesOrder.getStatus());
//        tvSalesOrderDate.setText(salesOrder.getSalesOrderDate());
//        tvSalesOrderNo.setText(salesOrder.getSalesOrderNo());
            tvPackingSlipDate.setText(salesOrder.getPackingSlipDate());
            tvPackingSlipNo.setText(salesOrder.getPackingSlipCode());
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
