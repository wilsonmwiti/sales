package id.co.beton.saleslogistic_trackingsystem.Adapter.packingslip;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.PackingSlip;
import id.co.beton.saleslogistic_trackingsystem.R;

import java.util.List;

/**
 * Class PackingSlipAdapterDetailPlan
 * Adapter for detail PackingSlip
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.PackingSlip.PackingSlipFragment}
 */
public class PackingSlipAdapterDetailPlan extends ArrayAdapter<PackingSlip> {

    private Context context;
    private List<PackingSlip> PackingSlips;
    private String customerName;
    private String customerAddres;

    public PackingSlipAdapterDetailPlan(@NonNull Context context, int resource, @NonNull List<PackingSlip> PackingSlips, String customerName, String customerAddres) {
        super(context, resource, PackingSlips);
        this.context = context;
        this.PackingSlips = PackingSlips;
        this.customerAddres=customerAddres;
        this.customerName =customerName;
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PackingSlip packingSlip = getItem(position);

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_packing_slip,parent,false);
        }

        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
        TextView tvNamaCustomer = (TextView) convertView.findViewById(R.id.tv_nama_customer);
        TextView tvAlamatCustomer = (TextView) convertView.findViewById(R.id.tv_alamat_customer);
        TextView tvPackingNo = (TextView) convertView.findViewById(R.id.tv_packing_slip_no);
        TextView tvJumlahItem = (TextView) convertView.findViewById(R.id.tv_jumlah_item);
        TextView tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
        TextView tvSalesOrderno = (TextView) convertView.findViewById(R.id.tv_sales_order_no);
        TextView tvTanggalSO = (TextView) convertView.findViewById(R.id.tv_tanggal_so);
        TextView tvSalesRep  =(TextView) convertView.findViewById(R.id.tv_sales_rep);

        tvNamaCustomer.setText(customerName);
        tvAlamatCustomer.setText(customerAddres);
        tvPackingNo.setText(packingSlip.getIdPackingSlip());
        tvJumlahItem.setText(packingSlip.getProduct().size()+"");
        tvStatus.setText(packingSlip.getStatus()+"");

        tvSalesOrderno.setText(packingSlip.getSalesOrderId());
        tvTanggalSO.setText("-");
        tvSalesRep.setText("-");

        return convertView;
    }
}
