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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.R;

/**
 * Class VisitPlanAdapter
 * Adapter for visit plan and implements filter
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.VisitPlan.VisitPlanFragment}
 */
public class VisitPlanAdapter extends ArrayAdapter<Destination>
        implements Filterable {

    private Context context;
    private List<Destination> destinations;
    private List<Destination> destinationFiltered;
    private int originLength;
    private boolean isFilter;

    public VisitPlanAdapter(@NonNull Context context, int resource, int originLength, boolean isFilter, @NonNull List<Destination> destinations) {
        super(context, resource, destinations);
        this.context =context;
        this.destinations = destinations;
        this.destinationFiltered = destinations;
        this.originLength = originLength;
        this.isFilter = isFilter;
    }



    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try{
            Destination destinations = getItem(position);

            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.content_list_visit_plan,parent,false);
            }
            LinearLayout delimiter = (LinearLayout) convertView.findViewById(R.id.delimiter_plan);
            TextView tvNamaCustomer = (TextView) convertView.findViewById(R.id.tv_nama_customer);
            TextView tvNamaJalan = (TextView) convertView.findViewById(R.id.tv_nama_jalan);
            TextView tvJam = (TextView) convertView.findViewById(R.id.tv_jam);
            TextView tvNoInvoice = (TextView) convertView.findViewById(R.id.tv_no_invoice);

            if(!isFilter){
                if(originLength>0){
                    if(originLength < this.destinations.size()){
                        if(originLength == (position+1)){
                            delimiter.setVisibility(View.VISIBLE);
                        } else {
                            delimiter.setVisibility(View.GONE);
                        }
                    }
                }
            } else {
                delimiter.setVisibility(View.GONE);
            }

            tvNamaCustomer.setText(destinations.getCustomerName());
            tvNamaJalan.setText(destinations.getAddress());
            tvJam.setText("");
            tvNoInvoice.setText(String.valueOf(destinations.getTotalInvoice()));
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }

    /**
     * set filterable
     * @return Resut Filter
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
                    isFilter = true;
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
                isFilter = true;
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
