package id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.detailsalesorder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.Product;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.util.List;

/**
 * Class ProductAdapter
 * Adapter for list product sales order
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.SalesOrder.DetailSalesOrderActivity}
 */
public class ProductAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> products;

    public ProductAdapter(@NonNull Context context, int resource, @NonNull List<Product> products) {
        super(context, resource, products);
        this.context =context;
        this.products = products;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Product products = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_detail_sales_order,parent,false);
        }
        TextView tvItem = (TextView) convertView.findViewById(R.id.tv_item_name);
        TextView tvQty = (TextView) convertView.findViewById(R.id.tv_qty);
        TextView tvPrice = (TextView) convertView.findViewById(R.id.tv_price_item);
        TextView tvTotalPrice = (TextView) convertView.findViewById(R.id.tv_total_purchase);


        try{
            tvItem.setText(products.getProductName());
            tvQty.setText(""+products.getQuantity());
            tvPrice.setText("Rp. "+ Currency.doublePriceWithDecimal(products.getUnitPrice()));
            // tvPrice.setText("Rp. "+ Currency.priceWithDecimal(products.getUnitPrice()));
            Integer amount = products.getNetAmount().intValue();
            tvTotalPrice.setText("Rp. "+ Currency.priceWithDecimal(amount));
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
