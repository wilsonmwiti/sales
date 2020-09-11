package id.co.beton.saleslogistic_trackingsystem.Adapter.route.invoices;

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

import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.InvoicesActivity;
import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.util.List;

/**
 * Class InvoiceConfirmAdapter
 * Adapter for confirmation invoice
 * {@link InvoicesActivity}
 */
public class InvoiceConfirmAdapter extends ArrayAdapter<Invoice> {
    private Context context;
    private List<Invoice> invoices;

    public InvoiceConfirmAdapter(@NonNull Context context, int resource, @NonNull List<Invoice> invoices) {
        super(context, resource, invoices);
        this.invoices = invoices;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Invoice invoice = getItem(position);

        Holder holder;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = LayoutInflater.from(context).inflate(R.layout.content_route_list_invoices, parent, false);
            holder = new Holder();
            holder.tvCustomerName = (TextView) convertView.findViewById(R.id.tv_customer_name);
            holder.tvAddress = (TextView) convertView.findViewById(R.id.tv_address);
            holder.tvTotalInvoice = (TextView) convertView.findViewById(R.id.tv_total_invoice);
            holder.tvInvNo = (TextView) convertView.findViewById(R.id.tv_invoice_no);
            holder.tvDueDate = (TextView) convertView.findViewById(R.id.tv_due_date);
            holder.tvPackingSlipNo = (TextView) convertView.findViewById(R.id.tv_packing_slip_no);
            holder.cbSelectInvoice = (CheckBox) convertView.findViewById(R.id.cb_select_invoice);
            holder.cbSelectInvoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    invoices.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                    InvoicesActivity.InvoiceSelected();
                }
            });

            convertView.setTag(holder);
            convertView.setTag(R.id.cb_select_invoice, holder.cbSelectInvoice);
        }else{
            holder = (Holder) convertView.getTag();
        }

        try{
            holder.cbSelectInvoice.setTag(position); // This line is important.
            holder.cbSelectInvoice.setChecked(invoices.get(position).isSelected());


            holder.tvCustomerName.setText(invoice.getCustomerName());
            holder.tvAddress.setText(invoice.getAddress());
            Currency currency  = Currency.getInstance();
            holder.tvTotalInvoice.setText("Rp "+currency.priceWithoutDecimal(invoice.getInvoiceAmount()));
            holder.tvInvNo.setText(invoice.getIdInvoice());
            holder.tvDueDate.setText(ConversionDate.getInstance().fullFormatDate(invoice.getPackingSlipDate()));

            holder.tvPackingSlipNo.setText(invoice.getPackingSlipCode());
        }catch (Exception e){
            Log.e("invoice confirm adapter",e.toString());
        }

        return convertView;
    }

    static class Holder {
        TextView tvCustomerName;
        TextView tvAddress;
        TextView tvTotalInvoice;
        TextView tvInvNo;
        CheckBox cbSelectInvoice;
        TextView tvDueDate;
        TextView tvPackingSlipNo;
    }
}
