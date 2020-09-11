package id.co.beton.saleslogistic_trackingsystem.Adapter.customer;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.co.beton.saleslogistic_trackingsystem.AppBundle.Customer.AddCustomerParentActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.Customer;
import id.co.beton.saleslogistic_trackingsystem.R;

public class AddCustomerParentAdapter extends ArrayAdapter<Customer>
        implements Filterable {
    private Context context;
    private List<Customer> customers;
    private List<Customer> customerFiltered;
    private RadioButton selected = null;
    private int selectedPosition = -1;

    public AddCustomerParentAdapter(@NonNull Context context, int resource, @NonNull List<Customer> customers){
        super(context, resource, customers);
        this.context = context;
        this.customers = customers;
        this.customerFiltered = customers;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Customer customer = getItem(position);

        final Holder holder;

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_new_visit_plan,parent,false);

            holder = new Holder();
            holder.tvNewCustomerName = (TextView) convertView.findViewById(R.id.tv_new_cust_name);
            holder.tvNewCustomerCode = (TextView) convertView.findViewById(R.id.tv_new_cust_code);
            holder.tvNewCustomerAddress = (TextView) convertView.findViewById(R.id.tv_new_cust_address);
            holder.rbSelectNewCustomer = (RadioButton) convertView.findViewById(R.id.rb_new_cust);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.rbSelectNewCustomer.setChecked(position == selectedPosition);
        holder.rbSelectNewCustomer.setTag(position);
        holder.rbSelectNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPosition = (Integer) v.getTag();
                AddCustomerParentActivity.customerSelected(selectedPosition);
                notifyDataSetChanged();
                if(Constants.DEBUG){
                    Log.d("Clicked", String.valueOf(selectedPosition));
                    Log.d("Customer Code", customers.get(selectedPosition).getCode());
                }
            }
        });

        holder.tvNewCustomerName.setText(customer.getNama());
        holder.tvNewCustomerCode.setText(customer.getCode());
        holder.tvNewCustomerAddress.setText(customer.getAlamat());

        return convertView;
    }

    static class Holder {
        TextView tvNewCustomerName;
        TextView tvNewCustomerCode;
        TextView tvNewCustomerAddress;
        RadioButton rbSelectNewCustomer;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    customerFiltered = customers;
                } else {
                    List<Customer> filteredList = new ArrayList<Customer>();
                    for (Customer row : customers) {
                        if (row.getNama().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }
                    customerFiltered = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = customerFiltered;
                filterResults.count = customerFiltered.size();
                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                customerFiltered = (ArrayList<Customer>) filterResults.values;
                notifyDataSetChanged();
                clear();
                for(int i = 0, l = customerFiltered.size(); i < l; i++)
                    add(customerFiltered.get(i));
                notifyDataSetInvalidated();
            }
        };
    }
}
