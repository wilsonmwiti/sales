package id.co.beton.saleslogistic_trackingsystem.Adapter.packingslip;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.PackingSlip.DetailPackingSlipActivity;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.PackingSlip;
import id.co.beton.saleslogistic_trackingsystem.R;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

/**
 * Class PackingSlipAdapter
 * Adapter for packing slip
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.PackingSlip.PackingSlipFragment}
 */
public class PackingSlipAdapter extends ArrayAdapter<PackingSlip> {
    private Context context;
    private ArrayList<PackingSlip> packingSlipId;
    private ArrayList<Destination> destinationItems;
    private String date;
    public PackingSlipAdapter(@NonNull Context context, int resource, @NonNull ArrayList<PackingSlip> packingSlipIdItems, @NonNull ArrayList<Destination> destinationItems, String date) {
        super(context, resource, packingSlipIdItems);
        this.context = context;
        this.packingSlipId =packingSlipIdItems;
        this.destinationItems=destinationItems;
        this.date = date;
    }

    public Destination getDestinationItem(String customer_code){
        Destination item = new Destination();
        Integer position = null;
        for (Integer i=0;i<destinationItems.size();i++){
            if(destinationItems.get(i).getCustomerCode().equals(customer_code)){
                position = i;
            }
        }

        if (position == null){
            return null;
        }else{
            return destinationItems.get(position);
        }

    }
    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PackingSlip packingSlip = getItem(position);
        final Destination destinationItem =  getDestinationItem(packingSlip.getCustomerCode());

        if(convertView==null){
            convertView = LayoutInflater.from(context).inflate(R.layout.content_list_packing_slip,parent,false);
        }

        try{
            RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);
            TextView tvNamaCustomer = (TextView) convertView.findViewById(R.id.tv_nama_customer);
            TextView tvAlamatCustomer = (TextView) convertView.findViewById(R.id.tv_alamat_customer);
            TextView tvPackingNo = (TextView) convertView.findViewById(R.id.tv_packing_slip_no);
            TextView tvJumlahItem = (TextView) convertView.findViewById(R.id.tv_jumlah_item);
            TextView tvStatus = (TextView) convertView.findViewById(R.id.tv_status);
            TextView tvSalesOrderno = (TextView) convertView.findViewById(R.id.tv_sales_order_no);
            TextView tvTanggalSO = (TextView) convertView.findViewById(R.id.tv_tanggal_so);
            TextView tvSalesRep  =(TextView) convertView.findViewById(R.id.tv_sales_rep);
            if(destinationItem!=null){
                tvNamaCustomer.setText(destinationItem.getCustomerName()+"");
                tvAlamatCustomer.setText(destinationItem.getAddress()+"");
                tvSalesRep.setText(destinationItem.getPicName());
            }else{
                tvNamaCustomer.setText("-");
                tvAlamatCustomer.setText("-");
            }
            tvPackingNo.setText(packingSlip.getIdPackingSlip()+"");
            tvJumlahItem.setText(packingSlip.getProduct().size()+"");
            tvStatus.setText(packingSlip.getStatus()+"");

            tvSalesOrderno.setText(packingSlip.getSalesOrderId());
            tvTanggalSO.setText(date);


            layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(destinationItem!=null){
                        Gson gson = new Gson();
                        String packingSlipItem           = gson.toJson(packingSlip);
                        String destinationPackingItem    = gson.toJson(destinationItem);

                        Intent intent = new Intent(getContext(), DetailPackingSlipActivity.class);
                        intent.putExtra("packingSlipItem",packingSlipItem);
                        intent.putExtra("destinationPackingItem",destinationPackingItem);
                        intent.putExtra("datePackingItem",date);
                        intent.putExtra("status_from_detail_customer",false);
                        v.getContext().startActivity(intent);
                    }else{
                        Toasty.info(context,"Customer tidak ada").show();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }
}
