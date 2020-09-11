package id.co.beton.saleslogistic_trackingsystem.AppBundle.Inbox;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import id.co.beton.saleslogistic_trackingsystem.Adapter.inbox.InboxAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.Data;
import id.co.beton.saleslogistic_trackingsystem.Model.DataInbox;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class InboxFragment
 * Fragment inbox
 */
public class InboxFragment extends Fragment {
    private static final String TAG = InboxFragment.class.getSimpleName();
    private OnFragmentInteractionListener mListener;
    private ListView listView;
    private Gson gson;
    private List<DataInbox> inboxes = new ArrayList<>();
    private int page = 1;
    private int totalData = 0;
    private ApiInterface service;

    private SwipeRefreshLayout swipeContainer;
    private boolean loadMore =false;
    InboxAdapter inboxAdapter;

    public InboxFragment() {
    }

    public static InboxFragment newInstance(String tabSelected) {
        InboxFragment fragment = new InboxFragment();
        Bundle args = new Bundle();
        args.putString("position", tabSelected);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.content_payment_and_order, container, false);
        view.setTag(TAG);
        service = ApiClient.getInstance(container.getContext());

        listView = (ListView)view.findViewById(R.id.lv_payment_and_order);
        initData(""+page);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gson = new Gson();
                String inboxVal    = gson.toJson(inboxes.get(i));
                Intent intent = new Intent(getContext(), DetailInboxActivity.class);
                intent.putExtra("inbox",inboxVal);
                startActivity(intent);
            }
        });

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                inboxes = new ArrayList<>();
                page=1;
                initData("1");
            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (listView== null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();
                swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);

                if(firstVisibleItem+visibleItemCount == totalItemCount && totalItemCount!=0)
                {
                    if(!loadMore) {
                        //cek perlu load atau tidak
                        if(page* Constants.LIMIT_REQUEST<totalData+Constants.LIMIT_REQUEST){
                            loadMore=true;
                            loadMore(page+"");
                        }
                    }
                }

            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return view;
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

    private void loadMore(String strPage){
        UserUtil userUtil = UserUtil.getInstance(getContext());

        /*Create handle for the RetrofitInstance interface*/

        Map<String, String> params = new HashMap<String, String>();
        params.put("page", strPage);
        params.put("limit", Constants.LIMIT_REQUEST+"");
        params.put("order_by_column","date");
        params.put("order_direction","desc");

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.inbox("JWT "+userUtil.getJWTTOken(),params);
        /*Log the URL called*/
        if(Constants.DEBUG){
            Log.i("URL Called", call.request().url() + "");
        }

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    Gson gson =new Gson();
                    Data inbox = gson.fromJson(response.body().getData().toString(), Data.class);
                    inboxes.addAll(inbox.getData());
                    page++;
                    loadMore=false;
                    inboxAdapter.notifyDataSetChanged();
                    swipeContainer.setRefreshing(false);
                }else{
                    try{
                        swipeContainer.setRefreshing(false);
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toasty.error(getContext(),"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                swipeContainer.setRefreshing(false);
                Toasty.error(getActivity(), "Kesalahan server inbox",
                        Toast.LENGTH_SHORT).show();
                if(Constants.DEBUG){
                    Log.wtf(TAG, "Koneksi Internet Bermasalah");
                }
            }
        });
    }


    private void initData(String strPage){
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(getContext());

        /*Create handle for the RetrofitInstance interface*/

        Map<String, String> params = new HashMap<String, String>();
        params.put("page", strPage);
        params.put("limit", Constants.LIMIT_REQUEST+"");
        params.put("order_by_column","date");
        params.put("order_direction","desc");

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call = service.inbox("JWT "+userUtil.getJWTTOken(),params);
        /*Log the URL called*/
        if(Constants.DEBUG){
            Log.i("URL Called", call.request().url() + "");
        }

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    Gson gson =new Gson();
                    Data inbox = gson.fromJson(response.body().getData().toString(), Data.class);
                    totalData = inbox.getTotalFilter();
                    inboxes.addAll(inbox.getData());

                    page++;
                    loadMore=false;

                    inboxAdapter=new InboxAdapter(getContext(),1,inboxes);
                    listView.setAdapter(inboxAdapter);
                    swipeContainer.setRefreshing(false);

                }else{
                    try{
                        swipeContainer.setRefreshing(false);
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(getContext(),jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        Toasty.error(getContext(),"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                swipeContainer.setRefreshing(false);
                Toasty.error(getActivity(), "Kesalahan server inbox",
                        Toast.LENGTH_SHORT).show();
                if(Constants.DEBUG){
                    Log.wtf(TAG, "Koneksi Internet Bermasalah");
                }
            }
        });
    }


    /*private AbsListView.OnScrollListener onScrollListener() {
        return new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                int threshold = 1;
                int count = listView.getCount();
                if (scrollState == SCROLL_STATE_IDLE) {
                    if (listView.getLastVisiblePosition() >= count - threshold) {
                        if(count >= (page*Constants.LIMIT_REQUEST)){
                            page++;
                            initData(""+page);
                            Log.d("page", ""+page);
                            Log.i(TAG, "loading more data");
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }

        };
    }*/
}
