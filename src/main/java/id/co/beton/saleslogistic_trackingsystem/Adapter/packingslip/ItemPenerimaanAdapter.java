package id.co.beton.saleslogistic_trackingsystem.Adapter.packingslip;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.Product;
import id.co.beton.saleslogistic_trackingsystem.R;

import java.util.List;

//import id.co.beton.saleslogistic_trackingsystem.model.Item;
//import id.co.beton.saleslogistic_trackingsystem.model.ModelPackingSlip.ProductItem;

/**
 * Class ItemPenerimaanAdapter
 * Adapter for reveiced Packing slip
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.PackingSlip.PenerimaanPackingSlipActivity}
 */
public class ItemPenerimaanAdapter extends ArrayAdapter<Product> {
    private Context context;
    private List<Product> products;
    private TextView tvItemName;
    private TextView tvQty;
    private EditText et_qty_diterima;
    private EditText et_catatan;
    public ItemPenerimaanAdapter(@NonNull Context context, int resource, @NonNull List<Product> productItems) {
        super(context, resource, productItems);
        this.products = productItems;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try{
            final Product product = products.get(position);
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.content_list_item_penerimaan,parent,false);
            }
            tvItemName = convertView.findViewById(R.id.tv_item_name);
            tvQty = convertView.findViewById(R.id.tv_item_qty);
            et_qty_diterima = convertView.findViewById(R.id.et_qty_diterima);
            tvItemName.setText(product.getProductName());
            tvQty.setText(""+product.getQuantity());
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
