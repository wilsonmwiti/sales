package id.co.beton.saleslogistic_trackingsystem.Adapter.plan;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import id.co.beton.saleslogistic_trackingsystem.Model.Plan;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.ConversionDate;

public class PlanAdapter extends ArrayAdapter<Plan> {
    private Context context;
    private List<Plan> plans;

    public  PlanAdapter(@NonNull Context context, int resource, @NonNull List<Plan> plans){
        super(context, resource, plans);
        this.context =context;
        this.plans = plans;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try{
            Plan plan = getItem(position);
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.content_list_plan,parent,false);
            }
            TextView tvIdPlan = (TextView) convertView.findViewById(R.id.tv_id_plan);
            TextView tvAsset = (TextView) convertView.findViewById(R.id.tv_asset);
            TextView tvCreateDate = (TextView) convertView.findViewById(R.id.tv_created_date);
            TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date_plan);
            TextView tvTotalVisit = (TextView) convertView.findViewById(R.id.tv_total_visit);
            TextView tvTotalPackingSlip = (TextView) convertView.findViewById(R.id.tv_total_packingslip);

            String planId = "";
            String assets = "-";
            String createDate = "-";
            String datePlan = "-";
            String totalCustomer = "0";
            String totalPackingSlip = "0";
            if(plan.getId()!=null){
                planId = plan.getId().toString();
            }
            if(plan.getAssetId()!=null){
                assets = plan.getAsset().getName() +", " + plan.getAsset().getCode();
            }
            if(plan.getCreateDate()!=null){
                createDate = plan.getCreateDate();
            }
            if(plan.getDate()!=null){
                datePlan = ConversionDate.getInstance().fullFormatDate(plan.getDate());
            }
            if(plan.getTotalCustomer()!=null){
                totalCustomer = plan.getTotalCustomer().toString();
            }
            if(plan.getTotalPackingSlip()!=null){
                totalPackingSlip = plan.getTotalPackingSlip().toString();
            }

            tvIdPlan.setText(planId);
            tvAsset.setText(assets);
            tvCreateDate.setText(createDate);
            tvDate.setText(datePlan);
            tvTotalVisit.setText(totalCustomer);
            tvTotalPackingSlip.setText(totalPackingSlip);

        } catch (Exception e){
            e.printStackTrace();
        }
        return convertView;
    }
}
