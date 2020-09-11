package id.co.beton.saleslogistic_trackingsystem.Adapter.route.invoices;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.Invoice;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.Currency;

import java.util.List;

/**
 * Class InvoicePayementAdapter
 * Adapter for invoice payment
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Invoice.PaymentActivity}
 */
public class InvoicePayementAdapter extends ArrayAdapter<Invoice> {
    private Context context;
    private List<Invoice> invoices;
    public InvoicePayementAdapter(@NonNull Context context, int resource, @NonNull List<Invoice> invoices) {
        super(context, resource, invoices);
        this.invoices = invoices;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Invoice invoice = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_payment_invoice, parent, false);
        }

        try{
            TextView tvNo = (TextView) convertView.findViewById(R.id.no);
            TextView tvInvoiceNumber = (TextView) convertView.findViewById(R.id.tv_invoice_no);
            TextView tvTotalInvoice = (TextView) convertView.findViewById(R.id.tv_total_invoice);
            TextView tvTotalCash = (TextView) convertView.findViewById(R.id.tv_total_cash);
            TextView tvTotalGiro = (TextView) convertView.findViewById(R.id.tv_total_giro);
            TextView tvTotalCheque = (TextView) convertView.findViewById(R.id.tv_total_cheque);
            TextView tvTotalTransfer = (TextView) convertView.findViewById(R.id.tv_total_transfer);
            TextView tvTotalKontrabon = (TextView) convertView.findViewById(R.id.tv_total_kontrabon);

            tvNo.setText((position+1)+".");
            tvInvoiceNumber.setText(invoice.getIdInvoice());
            tvTotalInvoice.setText("Rp."+ Currency.priceWithoutDecimal(invoice.getInvoiceAmount())+",-");
            int totalCash = invoice.getTotalPaymentByMethod("cash");
            tvTotalCash.setText("Rp."+Currency.priceWithoutDecimal(totalCash));

            int totalGiro = invoice.getTotalPaymentByMethod("giro");
            tvTotalGiro.setText("Rp."+Currency.priceWithoutDecimal(totalGiro));

            int totalCheque = invoice.getTotalPaymentByMethod("cek");
            tvTotalCheque.setText("Rp."+Currency.priceWithoutDecimal(totalCheque));

            int totalTransfer = invoice.getTotalPaymentByMethod("trf");
            tvTotalTransfer.setText("Rp."+Currency.priceWithoutDecimal(totalTransfer));

            int totalKontraBon = invoice.getTotalPaymentByMethod("kontrabon");
            // tvTotalKontrabon.setText("Rp."+Currency.priceWithoutDecimal(totalKontraBon));
            tvTotalKontrabon.setText("Rp.0");

        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
