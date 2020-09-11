package id.co.beton.saleslogistic_trackingsystem.Adapter.route.packingSlip;

import android.app.Activity;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.PackingSlipActivity;
import id.co.beton.saleslogistic_trackingsystem.Model.PackingSlip;
import id.co.beton.saleslogistic_trackingsystem.R;

import java.util.List;

/**
 * Class PackingSlipConfirmAdapter
 * Adapter for confirmation packing slip
 * {@link PackingSlipActivity}
 */
public class PackingSlipConfirmAdapter extends ArrayAdapter<PackingSlip> {
    private Context context;
    private List<PackingSlip> packingSlips;

    public PackingSlipConfirmAdapter(@NonNull Context context, int resource, @NonNull List<PackingSlip> packingSlips) {
        super(context, resource, packingSlips);
        this.packingSlips = packingSlips;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        PackingSlip packingSlip =getItem(position);

        Holder holder=null;
        if(convertView==null){
            try{
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                convertView = LayoutInflater.from(context).inflate(R.layout.content_route_list_packing_slips, parent, false);

                holder = new Holder();
                holder.tvCustomerName = (TextView) convertView.findViewById(R.id.tv_customer_name);
                holder.tvAddress = (TextView)convertView.findViewById(R.id.tv_address);
                holder.tvPackingSlipNo  =(TextView) convertView.findViewById(R.id.tv_packing_slip_no);
                holder.tvSalesOrderNo = (TextView) convertView.findViewById(R.id.tv_sales_order_no);
                holder.cbSelectpackingSlip = (CheckBox)convertView.findViewById(R.id.cb_select_packing_slip);
                holder.cbSelectpackingSlip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                        packingSlips.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                        PackingSlipActivity.packingSlipSelected();
                    }
                });
                convertView.setTag(holder);
                convertView.setTag(R.id.cb_select_invoice, holder.cbSelectpackingSlip);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            holder = (Holder) convertView.getTag();
        }

        try{
            holder.cbSelectpackingSlip.setTag(position); // This line is important.
            holder.cbSelectpackingSlip.setChecked(packingSlips.get(position).getSelected());
            holder.tvCustomerName.setText(packingSlip.getCustomerCode());
            holder.tvAddress.setVisibility(View.GONE);
            holder.tvSalesOrderNo.setVisibility(View.GONE);
            holder.tvPackingSlipNo.setText(packingSlip.getIdPackingSlip());
        }catch (Exception e){
            Log.e("packing Slip confirm",e.toString());
        }

        return convertView;
    }

    static class Holder {
        TextView tvCustomerName;
        TextView tvAddress;
        CheckBox cbSelectpackingSlip;
        TextView tvPackingSlipNo;
        TextView tvSalesOrderNo;
    }
}
