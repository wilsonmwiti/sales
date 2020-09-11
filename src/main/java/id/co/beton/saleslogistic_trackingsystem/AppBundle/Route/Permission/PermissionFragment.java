package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.Permission;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.PermissionAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.PermissionAlert;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class PermissionFragment
 *
 */

public class PermissionFragment extends Fragment {
    private static final String TAG = PermissionFragment.class.getSimpleName();
    private TextView text_view_for_tab_selection;
    private OnFragmentInteractionListener mListener;
    private ListView lvPermission;
    private FloatingActionButton addPermission;
    private Context context;
    DialogCustom dialogCustom;
    PermissionAdapter permissionAdapter;
    LayoutInflater inflater;
    private SwipeRefreshLayout swipeContainer;
    private View header;

    public static PermissionFragment newInstance(int position) {
        PermissionFragment fragment = new PermissionFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.content_permission, container, false);
        view.setTag(TAG);


        lvPermission = (ListView)view.findViewById(R.id.lv_permission);
        this.header = inflater.inflate(R.layout.header_permission,null);
        lvPermission.addHeaderView(header);

        context = container.getContext();

        dialogCustom = new DialogCustom(context);

        addPermission  =(FloatingActionButton) view.findViewById(R.id.add_permission);
        addPermission.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserUtil.getInstance(context).getBooleanProperty(Constants.STATUS_OUT_OFFICE)){
                    Intent intent = new Intent(context, AddPermissionActivity.class);
                    startActivity(intent);
                } else {
                    Toasty.info(context, "Anda belum memulai Tap Start dikantor.").show();
                }
            }
        });
        this.inflater =inflater;

        loadData(true);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                loadData(true);
            }
        });

        lvPermission.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                int topRowVerticalPosition = (lvPermission== null || lvPermission.getChildCount() == 0) ? 0 : lvPermission.getChildAt(0).getTop();
                swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return view;
    }

    private void loadData(boolean showLoading) {
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(getContext());

        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.permissionAlert("JWT " + userUtil.getJWTTOken(), 1, 50);

        /*Log the URL called*/
        if (Constants.DEBUG) {
            Log.wtf("URL Called", call.request().url() + "");
        }

        if(showLoading){
            /*Loading*/
            dialogCustom.show();
        }

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    try{
                        Gson gson =new Gson();
                        PermissionAlert permissionAlert = gson.fromJson(response.body().getData().toString(), PermissionAlert.class);

                        permissionAdapter = new PermissionAdapter(context,2,permissionAlert.getData());
                        lvPermission.setAdapter(permissionAdapter);

                        TextView tvTotalAlert = (TextView) header.findViewById(R.id.tv_total_alert);

                        tvTotalAlert.setText(permissionAlert.getTotalFilter().toString());

                        // Now we call setRefreshing(false) to signal refresh has finished
                        swipeContainer.setRefreshing(false);
                        dialogCustom.hidden();

                    }catch (IllegalStateException e){
                        Log.e(TAG,e.getMessage());
                        dialogCustom.hidden();
                        Toasty.error(context,e.getMessage()).show();
                    }catch (Exception ex){
                        Log.e(TAG,ex.getMessage());
                        dialogCustom.hidden();
                        Toasty.error(context,ex.getMessage()).show();
                    }
                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        dialogCustom.hidden();
                        Toasty.error(getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        dialogCustom.hidden();
                        Toasty.error(getContext(),"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }

                    // Now we call setRefreshing(false) to signal refresh has finished
                    swipeContainer.setRefreshing(false);
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
                dialogCustom.hidden();
                Toasty.error(getActivity(), "keslahan server permission",
                        Toast.LENGTH_SHORT).show();
                if(Constants.DEBUG){
                    Log.wtf(TAG, t.getMessage());
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
