package id.co.beton.saleslogistic_trackingsystem.Adapter.inbox;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import id.co.beton.saleslogistic_trackingsystem.Model.DataInbox;
import id.co.beton.saleslogistic_trackingsystem.R;

import java.util.List;

/**
 * Class InboxAdapter
 * adapter for data inbox
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Inbox.InboxFragment}
 */
public class InboxAdapter extends ArrayAdapter<DataInbox> {
    private Context context;
    private List<DataInbox> inboxes;

    public InboxAdapter(@NonNull Context context, int resource, @NonNull List<DataInbox> inboxes) {
        super(context, resource, inboxes);
        this.context = context;
        this.inboxes=inboxes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        try{
            DataInbox inbox = getItem(position);
            if(convertView==null){
                convertView = LayoutInflater.from(context).inflate(R.layout.content_list_inbox,parent,false);
            }
            TextView tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            TextView tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            TextView tvCategory = (TextView) convertView.findViewById(R.id.tv_category_message);

            tvDate.setText(inbox.getDate());
            tvTitle.setText(inbox.getTitle());
            tvCategory.setText(inbox.getCategory());
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertView;
    }
}
