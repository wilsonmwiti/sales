package id.co.beton.saleslogistic_trackingsystem.Adapter.route.order;

import android.content.Context;
import android.content.DialogInterface;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RelativeLayout;

import id.co.beton.saleslogistic_trackingsystem.Model.Product;
import id.co.beton.saleslogistic_trackingsystem.R;

import java.util.ArrayList;

/**
 * Class NewRequestOrderAdapter
 * Adapter for add new request order
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Order.NewRequestOrderActivity}
 */
public class NewRequestOrderAdapter extends ArrayAdapter<Product> {
    private ArrayList<Product> products;

    public NewRequestOrderAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Product> products) {
        super(context, resource, products);
        this.products = products;
    }
    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Product product = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        convertView = inflater.inflate(R.layout.content_list_new_request_order, parent, false);
        EditText tvItem = (EditText) convertView.findViewById(R.id.et_nama_product);
        EditText tvQty = (EditText) convertView.findViewById(R.id.et_qty);
        RelativeLayout delete = (RelativeLayout) convertView.findViewById(R.id.delete);

        tvItem.setText(product.getBrandName());
        tvQty.setText(String.valueOf(product.getQuantity()));

        delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                deleteItemFromList(v, position);
                    }
                });

        tvItem.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        product.setBrandName(editable.toString());
                    }
                });

        tvQty.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if(editable.length()>0){
                            product.setQuantity(Double.parseDouble(editable.toString()));
                        }
                    }
                });

        return convertView;
    }
    private void deleteItemFromList(View v, final int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());

        //builder.setTitle("Dlete ");
        builder.setMessage("Delete Product ?")
                .setCancelable(false)
                .setPositiveButton("CONFIRM",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                products.remove(position);
                                notifyDataSetChanged();

                            }
                        })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {


                    }
                });

        builder.show();

    }

    static class Holder {
        EditText tvItem;
        EditText tvQty;
        RelativeLayout delete;
    }
}
