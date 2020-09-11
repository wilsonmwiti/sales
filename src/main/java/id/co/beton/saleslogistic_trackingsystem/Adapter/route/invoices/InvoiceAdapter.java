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
import java.util.List;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Invoice.InvoiceFragment;
import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

/**
 * Class InvoiceAdapter
 * adapter for invoice
 * {@link InvoiceFragment}
 */
public class InvoiceAdapter extends ArrayAdapter<Invoice> {
    private Context context;
    private List<Invoice> invoices;

    public InvoiceAdapter(@NonNull Context context, int resource, @NonNull List<Invoice> invoices) {
        super(context, resource, invoices);
        this.invoices = invoices;
        this.context =context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Invoice invoice = getItem(position);

        Holder holder;
        if(convertView==null){
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = LayoutInflater.from(context).inflate(R.layout.content_route_list_invoice, parent,false);

            holder = new Holder();
            holder.tvInvoiceNo = (TextView) convertView.findViewById(R.id.tv_invoice_no);
            holder.tvTotalInvoice = (TextView) convertView.findViewById(R.id.tv_total_invoice);
            holder.tvDueDate = (TextView) convertView.findViewById(R.id.tv_due_date);
            holder.tvSlesOderNo = (TextView) convertView.findViewById(R.id.tv_sales_order_no);
            holder.cbSelectInvoice = (CheckBox) convertView.findViewById(R.id.cb_select_invoice);
            holder.tvPackingSlipDate = (TextView) convertView.findViewById(R.id.tv_packing_slip_date);
            holder.tvPackingSlipNo = (TextView) convertView.findViewById(R.id.tv_packing_slip_no);
            holder.cbSelectInvoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int getPosition = (Integer) buttonView.getTag();  // Here we get the position that we have set for the checkbox using setTag.
                    invoices.get(getPosition).setSelected(buttonView.isChecked()); // Set the value of checkbox to maintain its state.
                    InvoiceFragment.InvoiceSelected();
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
            ConversionDate conversionDate = ConversionDate.getInstance();
            holder.tvInvoiceNo.setText(invoice.getIdInvoice());
            holder.tvTotalInvoice.setText("Rp."+Currency.priceWithoutDecimal(invoice.getInvoiceAmount())+",-");
            holder.tvDueDate.setText("-");
            holder.tvSlesOderNo.setText(invoice.getSalesOrderId());
            holder.tvPackingSlipDate.setText(invoice.getPackingSlipDate());
            holder.tvPackingSlipNo.setText(invoice.getPackingSlipCode());
            // Log.d(">>> Position", String.valueOf(position));
            // Log.d(">>> ID Invoice", invoice.getIdInvoice());
        } catch (Exception e){
            Log.e("invoice adapter",e.toString());
        }
        return convertView;
    }

    static class Holder {
        TextView tvInvoiceNo;
        TextView tvTotalInvoice;
        TextView tvDueDate;
        TextView tvSlesOderNo;
        CheckBox cbSelectInvoice;
        TextView tvPackingSlipDate;
        TextView tvPackingSlipNo;
    }

}