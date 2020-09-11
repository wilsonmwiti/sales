package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.beton.saleslogistic_trackingsystem.Adapter.RoutesAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import java.util.ArrayList;

/**
 * Class RouteFragment
 * for main fragment
 */
public class RouteFragment extends Fragment {
    private static final String TAG = RouteFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private OnFragmentInteractionListener mListener;
    private RoutesAdapter routesAdapter;
    ArrayList<String> tabName;
    private Bundle args;

    public RouteFragment() {
    }
    public static RouteFragment newInstance(int navigation) {
        RouteFragment fragment = new RouteFragment();
        Bundle b = new Bundle();
        b.putInt("position", navigation);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_routes,container,false);
        try{
            args = new Bundle();
            args = getArguments();

            mViewPager=(ViewPager)view.findViewById(R.id.container);
            tabLayout = (TabLayout) view.findViewById(R.id.tabs);
            tabName=new ArrayList<String>();

            UserUtil userUtil = UserUtil.getInstance(container.getContext());
            //create tab
            if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
                tabLayout.addTab(tabLayout.newTab().setText("Delivery Plan"));
                tabName.add("Delivery Plan");
            }else{
                tabLayout.addTab(tabLayout.newTab().setText("Visit Plan"));
                tabName.add("Visit Plan");
            }

            tabLayout.addTab(tabLayout.newTab().setText("Map"));
            tabName.add("MapFragment");

            tabLayout.addTab(tabLayout.newTab().setText("Permission"));
            tabName.add("PermissionFragment");

            tabLayout.addTab(tabLayout.newTab().setText("Break Time"));
            tabName.add("Break Time");

            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            routesAdapter = new RoutesAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
            mViewPager.setAdapter(routesAdapter);

            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    mViewPager.setCurrentItem(tab.getPosition());
                    getChildFragmentManager().beginTransaction().addToBackStack(null).commit();
                }
                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }
                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });


        }catch (Exception e){
            e.printStackTrace();
        }

        return view;
    }

    private void updateTabSelection(int position){
        mViewPager.setCurrentItem(position);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Note that we are passing childFragmentManager, not FragmentManager
        routesAdapter = new RoutesAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
        mViewPager.setAdapter(routesAdapter);
        int position = getArguments().getInt("position");
        updateTabSelection(position);
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

