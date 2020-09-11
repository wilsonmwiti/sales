package id.co.beton.saleslogistic_trackingsystem.Adapter.packingslip;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.Contact;
import id.co.beton.saleslogistic_trackingsystem.R;

import java.util.List;

/**
 * Class PICAdapter
 * Adapter for list PIC
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Pic.PICActivity}
 */
public class PICAdapter extends ArrayAdapter<Contact> {
    private Context context;

    public PICAdapter(@NonNull Context context, int resource, @NonNull List<Contact> objects) {
        super(context, resource, objects);
        this.context =context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Contact contact = getItem(position);
        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_pic,parent,false);
        }
        TextView tvNamaPIc = (TextView) convertView.findViewById(R.id.tv_nama_pic);
        tvNamaPIc.setText(contact.getName());

        return convertView;
    }
}
