package id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment;

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

import id.co.beton.saleslogistic_trackingsystem.Adapter.OrderAndPaymentAdapter;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.R;

import java.util.ArrayList;

/**
 * Class OrderAndPaymentFragment
 * Fragment for Order and Payment
 */
public class OrderAndPaymentFragment extends Fragment {
    private static final String TAG = OrderAndPaymentFragment.class.getSimpleName();
    private TabLayout tabLayout;
    private ViewPager mViewPager;
    private OnFragmentInteractionListener mListener;
    private OrderAndPaymentAdapter orderAndPaymentAdapter;
    ArrayList<String> tabName;

    public OrderAndPaymentFragment() {
    }
    public static OrderAndPaymentFragment newInstance(String navigation) {
        OrderAndPaymentFragment fragment = new OrderAndPaymentFragment();
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
        tabLayout.addTab(tabLayout.newTab().setText("Invoice"));
        tabName.add("Invoice");

        tabLayout.addTab(tabLayout.newTab().setText("Payment"));
        tabName.add("Payment");

        tabLayout.addTab(tabLayout.newTab().setText("Request Order"));
        tabName.add("Request Order");

        // tabLayout.addTab(tabLayout.newTab().setText("Sales Order"));
        // tabName.add("Sales Order");

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        orderAndPaymentAdapter = new OrderAndPaymentAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
        mViewPager.setAdapter(orderAndPaymentAdapter);

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
        orderAndPaymentAdapter = new OrderAndPaymentAdapter(getChildFragmentManager(),tabLayout.getTabCount(),tabName);
        mViewPager.setAdapter(orderAndPaymentAdapter);

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
