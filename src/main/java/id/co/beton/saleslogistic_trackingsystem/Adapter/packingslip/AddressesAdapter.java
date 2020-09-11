package id.co.beton.saleslogistic_trackingsystem.Adapter.packingslip;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.R;

import java.util.List;

/**
 * Class AddressesAdapter
 * adapter for address
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Address.AddressesActivity}
 */
public class AddressesAdapter extends ArrayAdapter<String> {

    private Context context;

    public AddressesAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try{
            String address = getItem(position);
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.content_list_address,parent,false);
            }
            TextView tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            tvAddress.setText(address);
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
