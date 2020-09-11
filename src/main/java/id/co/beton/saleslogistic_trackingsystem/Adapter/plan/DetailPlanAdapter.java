package id.co.beton.saleslogistic_trackingsystem.Adapter.plan;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.R;

public class DetailPlanAdapter extends ArrayAdapter<Destination> {
    private Context context;
    private List<Destination> destinations;

    public DetailPlanAdapter(@NonNull Context context, int resource, @NonNull List<Destination> destinations){
        super(context, resource, destinations);
        this.context = context;
        this.destinations = destinations;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Destination destination = getItem(position);

        final DetailPlanAdapter.Holder holder;

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_destination, parent,false);

            holder = new DetailPlanAdapter.Holder();
            holder.tvNewCustomerName = (TextView) convertView.findViewById(R.id.tv_detail_name_customer);
            holder.tvNewCustomerAddress = (TextView) convertView.findViewById(R.id.tv_detail_address_customer);
            holder.tvNewCustomerCode = (TextView) convertView.findViewById(R.id.tv_detail_code_customer);

            convertView.setTag(holder);
        } else {
            holder = (DetailPlanAdapter.Holder) convertView.getTag();
        }

        holder.tvNewCustomerName.setText(destination.getCustomerName());
        holder.tvNewCustomerAddress.setText(destination.getAddress());
        holder.tvNewCustomerCode.setText(destination.getCustomerCode());

        return convertView;
    }

    static class Holder {
        TextView tvNewCustomerName;
        TextView tvNewCustomerCode;
        TextView tvNewCustomerAddress;
    }
}
