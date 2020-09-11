package id.co.beton.saleslogistic_trackingsystem.Adapter.packingslip;

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

import java.util.List;

/**
 * Class ItemAdapter
 * Adapter for packing slip product
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.PackingSlip.DetailPackingSlipActivity}
 */
public class ItemAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> products;
    public ItemAdapter(@NonNull Context context, int resource, @NonNull List<Product> products) {
        super(context, resource, products);
        this.products = products;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Product product = products.get(position);
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_item,parent,false);
        }
        try{
            TextView tvItemName = (TextView) convertView.findViewById(R.id.tv_item_name);
            TextView tvQty = (TextView)convertView.findViewById(R.id.tv_item_qty);
            TextView tvQtyTerima = (TextView)convertView.findViewById(R.id.tv_item_qty_diterima);

            tvItemName.setText(product.getProductName());
            tvQty.setText(""+product.getQuantity());
            tvQtyTerima.setText(""+product.getAcceptedQuantity());
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
