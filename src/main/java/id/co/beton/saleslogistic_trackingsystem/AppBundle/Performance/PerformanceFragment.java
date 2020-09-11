package id.co.beton.saleslogistic_trackingsystem.AppBundle.Performance;

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

import id.co.beton.saleslogistic_trackingsystem.Adapter.PerformanceAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import java.util.ArrayList;

/**
 * Class PerformanceFragment
 */
public class PerformanceFragment extends Fragment {
    private static final String TAG = PerformanceFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private OnFragmentInteractionListener mListener;
    private PerformanceAdapter performanceAdapter;
    ArrayList<String> tabName;

    public PerformanceFragment() {
    }
    public static PerformanceFragment newInstance(String navigation) {
        PerformanceFragment fragment = new PerformanceFragment();
        Bundle args = new Bundle();
        args.putString("position", navigation);
        fragment.setArguments(args);
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
        mViewPager=(ViewPager)view.findViewById(R.id.container);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabName=new ArrayList<String>();

        //create tab

        UserUtil  userUtil  = UserUtil.getInstance(container.getContext());

        tabLayout.addTab(tabLayout.newTab().setText("Activities"));
        tabName.add("Activities");

        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            tabLayout.addTab(tabLayout.newTab().setText("Delivery"));
            tabName.add("Delivery");
        }else{
            tabLayout.addTab(tabLayout.newTab().setText("Orders"));
            tabName.add("Orders");
        }

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        performanceAdapter = new PerformanceAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
        mViewPager.setAdapter(performanceAdapter);

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

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Note that we are passing childFragmentManager, not FragmentManager
        performanceAdapter = new PerformanceAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
        mViewPager.setAdapter(performanceAdapter);

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
