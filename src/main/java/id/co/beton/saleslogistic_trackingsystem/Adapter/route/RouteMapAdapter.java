package id.co.beton.saleslogistic_trackingsystem.Adapter.route;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.StepRouteMap;
import id.co.beton.saleslogistic_trackingsystem.R;

import java.util.ArrayList;

/**
 * Class RouteMapAdapter
 * Adapter for route map
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.MapFragment}
 */
public class RouteMapAdapter extends ArrayAdapter<StepRouteMap> {

    private Context context;
    private ArrayList<StepRouteMap> stepRouteMaps;

    public RouteMapAdapter(@NonNull Context context, int resource, @NonNull ArrayList<StepRouteMap> stepRouteMaps) {
        super(context, resource, stepRouteMaps);
        this.stepRouteMaps = stepRouteMaps;
        this.context =context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try{
            StepRouteMap stepRouteMap = getItem(position);
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.content_list_routes_map,parent,false);
            }
//        TextView tvNama = (TextView) convertView.findViewById(R.id.tv_nama);
            ImageView ivIconRute = (ImageView) convertView.findViewById(R.id.iv_icon_rute);
            TextView tvAlamat = (TextView) convertView.findViewById(R.id.tv_alamat);
            TextView tvJarak = (TextView) convertView.findViewById(R.id.tv_jarak);

            tvAlamat.setText(Html.fromHtml(stepRouteMap.getAlamat()));
            tvJarak.setText(stepRouteMap.getDistanceText());

            if(stepRouteMap.getManeuver().equals("turn-left")){
                ivIconRute.setImageResource(R.drawable.direction_turn_left);
            }else if(stepRouteMap.getManeuver().equals("turn-right")){
                ivIconRute.setImageResource(R.drawable.direction_turn_right);
            }else{
                ivIconRute.setImageResource(R.drawable.marker_blue);
            }
        }catch (Exception e){
            e.printStackTrace();;
        }

        return convertView;
    }
}
