package id.co.beton.saleslogistic_trackingsystem.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import id.co.beton.saleslogistic_trackingsystem.AppBundle.Performance.ActivityFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Performance.OrderFragment;

import java.util.ArrayList;

/**
 * Class PerformanceAdapter
 * Adapter for performance
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Performance.PerformanceFragment}
 */
public class PerformanceAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;
    ArrayList<String> tabName;
    public PerformanceAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String> tabName) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.tabName=tabName;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            ActivityFragment comn=new ActivityFragment();
            return comn.newInstance(tabName.get(position));
        }else{
            OrderFragment orderFragment = new OrderFragment();
            return orderFragment.newInstance(tabName.get(position));
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
