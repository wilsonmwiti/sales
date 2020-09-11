package id.co.beton.saleslogistic_trackingsystem.Adapter.route.invoices;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.util.List;

/**
 * Class InvoicesDetailPaymentAdapter
 * Adapter for detail invoice payment
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Invoice.DetailInvoicePaymentActivity}
 */
public class InvoicesDetailPaymentAdapter extends ArrayAdapter<Invoice> {
    private Context context;
    private List<Invoice> invoice;
    public InvoicesDetailPaymentAdapter(@NonNull Context context, int resource, @NonNull List<Invoice> invoices) {
        super(context, resource, invoices);
        this.invoice = invoices;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Invoice invoice = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_invoice_payement, parent, false);
        }

        try{
            TextView tvInvNo = (TextView) convertView.findViewById(R.id.tv_invoice_no);
            TextView tvTotalInvoice = (TextView) convertView.findViewById(R.id.tv_total_invoice);
            EditText etDefaultTotalInvoice = (EditText)convertView.findViewById(R.id.et_default_total_invoice);

            tvInvNo.setText(invoice.getIdInvoice());
            tvTotalInvoice.setText("Rp."+ Currency.priceWithoutDecimal(invoice.getInvoiceAmount())+",-");
            etDefaultTotalInvoice.setTag(position);
            if(invoice.getPaymentAmount()!=null){
                etDefaultTotalInvoice.setText(invoice.getPaymentAmount()+"");
            }else{
                etDefaultTotalInvoice.setText(0+"");
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return convertView;
    }
}
