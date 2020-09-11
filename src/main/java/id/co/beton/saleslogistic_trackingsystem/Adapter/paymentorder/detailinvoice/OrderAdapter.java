package id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.detailinvoice;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.ProductInvoice;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.util.List;

/**
 * Class OrderAdapter
 * Adapter for Detail invoice
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.Invoice.DetailInvoiceActivity}
 */
public class OrderAdapter extends ArrayAdapter<ProductInvoice> {
    private Context context;
    private List<ProductInvoice> productInvoices;

    public OrderAdapter(@NonNull Context context, int resource, @NonNull List<ProductInvoice> productInvoices) {
        super(context, resource, productInvoices);
        this.context=context;
        this.productInvoices = productInvoices;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ProductInvoice productInvoice = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_order_invoice_detail,parent,false);
        }
        try{
            TextView tvItemName = (TextView) convertView.findViewById(R.id.tv_item_name);
            TextView tvQty = (TextView) convertView.findViewById(R.id.tv_qty);
            TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price);
            TextView tvTotalPurchase = (TextView) convertView.findViewById(R.id.tv_total_purchase);

            tvItemName.setText(productInvoice.getProductName());
            tvQty.setText(""+productInvoice.getQuantity());
            tvPrice.setText("Rp. "+ Currency.priceWithDecimal(productInvoice.getUnitPrice()));
            // tvTotalPurchase.setText("Rp. "+ Currency.priceWithDecimal(productInvoice.getLineAmountGross()));
            tvTotalPurchase.setText("Rp. "+ Currency.doublePriceWithDecimal(productInvoice.getLineAmountGross()));
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
