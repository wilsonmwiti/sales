package id.co.beton.saleslogistic_trackingsystem.Adapter.route;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Class DeliveryPlanAdapter
 * Adapter for delivery plan and implement filter
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.VisitPlan.VisitPlanFragment}
 */
public class DeliveryPlanAdapter extends ArrayAdapter<Destination>
        implements Filterable {
    private Context context;
    private List<Destination> destinations;
    private List<Destination> destinationFiltered;

    public DeliveryPlanAdapter(@NonNull Context context, int resource, @NonNull List<Destination> destinations) {
        super(context, resource, destinations);
        this.context =context;
        this.destinations = destinations;
        this.destinationFiltered = destinations;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       try{
           Destination destinations = getItem(position);
           if(convertView==null){
               convertView = LayoutInflater.from(context).inflate(R.layout.content_list_delivery_plan,parent,false);
           }
           TextView tvNamaCustomer = (TextView) convertView.findViewById(R.id.tv_nama_customer);
           TextView tvNamaJalan = (TextView) convertView.findViewById(R.id.tv_nama_jalan);
           TextView tvJam = (TextView) convertView.findViewById(R.id.tv_jam);
           TextView tvPackingSlip = (TextView) convertView.findViewById(R.id.tv_packing_slip);
           TextView tvPackingSlipNo = (TextView) convertView.findViewById(R.id.tv_no_packing_slip);

           tvNamaCustomer.setText(destinations.getCustomerName());
           tvNamaJalan.setText(destinations.getAddress());
           tvJam.setText("");
           tvPackingSlip.setText(String.valueOf(destinations.getTotalPackingSlip()));
           tvPackingSlipNo.setText("");
           if(destinations.getPackingSlips().size()>0){
               for (int i=0; i<destinations.getPackingSlips().size();i++){
                   tvPackingSlipNo.append(destinations.getPackingSlips().get(i).getIdPackingSlip());
                   tvPackingSlipNo.append("\n");
               }
           }
       }catch (Exception e){
           e.printStackTrace();
       }

        return convertView;
    }

    /**
     * set filterable
     * @return Result Filter
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    destinationFiltered = destinations;
                } else {
                    List<Destination> filteredList = new ArrayList<Destination>();
                    for (Destination row : destinations) {
                        if (row.getCustomerName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    destinationFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = destinationFiltered;
                filterResults.count = destinationFiltered.size();
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                destinationFiltered = (ArrayList<Destination>) filterResults.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = destinationFiltered.size(); i < l; i++)
                    add(destinationFiltered.get(i));
                notifyDataSetInvalidated();
            }
        };
    }
}
