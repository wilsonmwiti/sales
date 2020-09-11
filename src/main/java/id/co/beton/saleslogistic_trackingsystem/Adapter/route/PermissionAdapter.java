package id.co.beton.saleslogistic_trackingsystem.Adapter.route;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.PermissionAlertData;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;

import java.util.List;

/**
 * Class PermissionAdapter
 * Adapter for permission
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.Permission.PermissionFragment}
 */
public class PermissionAdapter extends ArrayAdapter<PermissionAlertData> {
    private Context context;
    private List<PermissionAlertData> permissions;

    public PermissionAdapter(@NonNull Context context, int resource, @NonNull List<PermissionAlertData> permissions) {
        super(context, resource, permissions);
        this.context = context;
        this.permissions =permissions;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try{
            PermissionAlertData permission = getItem(position);
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.content_list_permission,parent,false);
            }
            TextView tvJam = (TextView) convertView.findViewById(R.id.tv_jam);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            TextView tvKeterangan = (TextView) convertView.findViewById(R.id.tv_keterangan);
            ImageView iconPermission = (ImageView) convertView.findViewById(R.id.iv_icon_permission);
            TextView tv_status = (TextView) convertView.findViewById(R.id.tv_status);

            tvJam.setText(ConversionDate.getInstance().fullFormatDate(permission.getDate()));

            if(permission.getType().equalsIgnoreCase("break_time") || permission.getType().equalsIgnoreCase("visit_time")){
                iconPermission.setImageResource(R.drawable.ic_warning);
            }else{
                iconPermission.setImageResource(R.drawable.ic_access_time);
            }

            tvTitle.setText(permission.getSubject());
            tvKeterangan.setText(permission.getNotes());
            if(permission.getIsApproved()==1){
                tv_status.setText("Approved");
            }else if(permission.getIsRejected()==1){
                tv_status.setText("Rejected");
            }else{
                tv_status.setText("Waiting for approval");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
