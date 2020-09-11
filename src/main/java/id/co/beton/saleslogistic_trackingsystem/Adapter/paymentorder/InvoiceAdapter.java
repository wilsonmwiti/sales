package id.co.beton.saleslogistic_trackingsystem.Adapter.paymentorder;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.util.List;

/**
 * Class InvoiceAdapter
 * Adapter for Invoice
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.Invoice.InvoiceFragment}
 */
public class InvoiceAdapter extends ArrayAdapter<Invoice> {

    private Context context;
    private List<Invoice> invoices;
    private List<Destination> destinations;

    public InvoiceAdapter(@NonNull Context context, int resource, @NonNull List<Invoice> invoices, @NonNull List<Destination> destinations) {
        super(context, resource, invoices);
        this.context = context;
        this.invoices = invoices;
        this.destinations = destinations;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try{
            Invoice invoice = getItem(position);

            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.content_list_invoice,parent,false);
            }

            TextView tvCustomer = (TextView) convertView.findViewById(R.id.tv_nama_customer);
            TextView tvNoInvoice = (TextView) convertView.findViewById(R.id.tv_no_invoice);
            TextView tvDueDate = (TextView) convertView.findViewById(R.id.tv_due_date);
            TextView tvTotalInvoice = (TextView) convertView.findViewById(R.id.tv_total_harga_invoice);
            tvCustomer.setText(invoice.getCustomerName());
            tvDueDate.setText(invoice.getPackingSlipDate());
            tvNoInvoice.setText(invoice.getIdInvoice());
            tvTotalInvoice.setText("Rp. "+ Currency.priceWithDecimal(invoice.getInvoiceAmount()));
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
