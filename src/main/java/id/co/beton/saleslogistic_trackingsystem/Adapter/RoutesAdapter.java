package id.co.beton.saleslogistic_trackingsystem.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.BreakTimeFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.MapFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.Permission.PermissionFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.VisitPlan.VisitPlanFragment;

import java.util.ArrayList;

/**
 * Class RoutesAdapter
 * Adapter for for Route
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.RouteFragment}
 */
public class RoutesAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;
    ArrayList<String> tabName;
    public RoutesAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String> tabName) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.tabName=tabName;
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            VisitPlanFragment comn=new VisitPlanFragment();
            return comn.newInstance(position);
        }else if(position==1){
            MapFragment map = new MapFragment();
            return map.newInstance(position);
        }else if(position==2){
            PermissionFragment permissionFragment =new PermissionFragment();
            return permissionFragment.newInstance(position);
        }else {
            BreakTimeFragment breakTimeFragment = new BreakTimeFragment();
            return breakTimeFragment.newInstance(position);
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
