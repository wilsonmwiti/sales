package id.co.beton.saleslogistic_trackingsystem.AppBundle.Plan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Adapter.plan.PlanAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.DataPlan;
import id.co.beton.saleslogistic_trackingsystem.Model.Plan;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PlanFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlanFragment extends Fragment {
    private static final String TAG = PlanFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;

    private List<Plan> plans= new ArrayList<>();
    private ListView lvDetailPlan;
    private Context context;
    private DialogCustom dialogCustom;
    private UserUtil userUtil;
    private PlanUtil planUtil;
    private PlanAdapter planAdapter;

    private SwipeRefreshLayout swipeContainer;

    public PlanFragment() {
        // Required empty public constructor
    }


    public static PlanFragment newInstance(int tabSelected) {
        PlanFragment fragment = new PlanFragment();
        Bundle args = new Bundle();
        args.putInt("position", tabSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.content_plan, container, false);
        view.setTag(TAG);
        try{
            context = container.getContext();
            userUtil = UserUtil.getInstance(context);
            dialogCustom = new DialogCustom(context);
            planUtil = PlanUtil.getInstance(context);

            // Lookup the swipe container view
            swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
            // Setup refresh listener which triggers new data loading
            swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    loadData(true);
                }
            });
            // Configure the refreshing colors
            swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            lvDetailPlan = (ListView)view.findViewById(R.id.lv_plan);
            View header = inflater.inflate(R.layout.header_select_plan,null,false);
            lvDetailPlan.addHeaderView(header);

            lvDetailPlan.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    int topRowVerticalPosition = (lvDetailPlan == null || lvDetailPlan.getChildCount() == 0) ? 0 : lvDetailPlan.getChildAt(0).getTop();
                    swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
                }
            });

            loadData(true);

        } catch(Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    private void loadData(boolean showLoading){
        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.getAllDeliveryPlan("JWT " + userUtil.getJWTTOken());

        /*Log the URL called*/
        if(Constants.DEBUG){
            Log.i("URL Called", call.request().url() + "");
        }

        if(showLoading){
            dialogCustom.show();
        }

        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body()!=null && response.body().getError()==0) { //if sukses
                    try{
                        Gson gson =new Gson();
                        DataPlan dataPlan = gson.fromJson(response.body().getData().toString(), DataPlan.class);
                        plans = dataPlan.getData();

                        planAdapter = new PlanAdapter(context,1, plans);
                        lvDetailPlan.setAdapter(planAdapter);

                        lvDetailPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if(position!=0){
                                    Gson gson = new Gson();
                                    Plan detailPlan = plans.get(position-1);
                                    Intent intent = new Intent(context, DetailPlanActivity.class);
                                    intent.putExtra("detail_plan", gson.toJson(detailPlan, Plan.class));
                                    intent.putExtra("plan_id",detailPlan.getId().toString());
                                    startActivity(intent);
                                }
                            }
                        });

                        dialogCustom.hidden();
                        swipeContainer.setRefreshing(false);
                    } catch (Exception e){
                        e.printStackTrace();
                        dialogCustom.hidden();
                        swipeContainer.setRefreshing(false);
                    }
                } else {
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                        swipeContainer.setRefreshing(false);
                    }catch (Exception e){
                        Toasty.error(getContext(),"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                        swipeContainer.setRefreshing(false);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                System.out.println("======= debug error =======");
                Toasty.error(getContext(),"kesalahan server", Toast.LENGTH_SHORT).show();
                dialogCustom.hidden();
                swipeContainer.setRefreshing(false);
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

}
