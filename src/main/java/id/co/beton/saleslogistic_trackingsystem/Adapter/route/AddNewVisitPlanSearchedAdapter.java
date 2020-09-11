package id.co.beton.saleslogistic_trackingsystem.Adapter.route;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import id.co.beton.saleslogistic_trackingsystem.AppBundle.Customer.SearchMycustomerVisitActivity;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.VisitPlan.AddNewVisitPlan;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.Customer;
import id.co.beton.saleslogistic_trackingsystem.R;

/**
 * Class AddNewVisitPlanAdapter
 * Adapter for add new Visit/customer
 * {@link AddNewVisitPlan}
 */

public class AddNewVisitPlanSearchedAdapter extends ArrayAdapter<Customer>{

    private AdapterCallback adapterCallback;

    private Context context;
    private List<Customer> customers;
    //    private RadioButton selected = null;

    public AddNewVisitPlanSearchedAdapter(@NonNull Context context, int resource, @NonNull List<Customer> customers) {
        super(context, resource, customers);
        this.context = context;
        this.customers = customers;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
        final Customer customer = getItem(position);

        final Holder holder;

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_new_visit_plan_searched,parent,false);

            holder = new Holder();
            holder.tvNewCustomerName = (TextView) convertView.findViewById(R.id.tv_new_cust_name);
            holder.tvNewCustomerCode = (TextView) convertView.findViewById(R.id.tv_new_cust_code);
            holder.tvNewCustomerAddress = (TextView) convertView.findViewById(R.id.tv_new_cust_address);
//            holder.rbSelectNewCustomer = (RadioButton) convertView.findViewById(R.id.rb_new_cust);
//            holder.rgSelectNewCustomer = (RadioGroup) convertView.findViewById(R.id.rg_new_cust);
            holder.btnSetNewCustomer = (Button) convertView.findViewById(R.id.btn_setNewCustomerSearched);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        holder.tvNewCustomerName.setText(customer.getNama());
        holder.tvNewCustomerCode.setText(customer.getCode());
        holder.tvNewCustomerAddress.setText(customer.getAlamat());

        holder.btnSetNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String customerName = customers.get(position).getNama();
//                AddNewVisitPlan.customerSelected(position);

                adapterCallback.onMethodCallback(customer);
            }
        });

        return convertView;
    }

    class Holder {
        TextView tvNewCustomerName;
        TextView tvNewCustomerCode;
        TextView tvNewCustomerAddress;
        Button btnSetNewCustomer;
    }

    /*
    * adapter callback
    * */

    public void setAdapterCallback(AdapterCallback callback){
        this.adapterCallback = callback;
    }

    public interface AdapterCallback {
        void onMethodCallback(Customer customer);
    }
}
