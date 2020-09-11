package id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder.detailrequestorder;

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
 * Class ProductAdapter
 * Adapter for list product request order
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.RequestOrder.DetailRequestOrderActivity}
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Order.DetailVisitPlanRequestOrderActivity}
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Order.DetailVisitPlanSalesOrderActivity}
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
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_detail_request_order,parent,false);
        }
        TextView tvItem = (TextView) convertView.findViewById(R.id.tv_item_name);
        TextView tvQty = (TextView) convertView.findViewById(R.id.tv_qty);


        try{
            tvItem.setText(products.getProductName());
            tvQty.setText(products.getQuantity()+"");
        }catch (NullPointerException e){

        }

        return convertView;
    }
}
