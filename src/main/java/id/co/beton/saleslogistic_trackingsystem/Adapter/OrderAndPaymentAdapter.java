package id.co.beton.saleslogistic_trackingsystem.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.Invoice.InvoiceFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.Payment.PaymentFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.RequestOrder.RequestOrderFragment;

import java.util.ArrayList;

/**
 * Class OrderAndPaymentAdapter
 * Adapter for Order and Payment
 * {@link id.co.beton.saleslogistic_trackingsystem.AppBundle.OrderAndPayment.OrderAndPaymentFragment}
 */
public class OrderAndPaymentAdapter extends FragmentPagerAdapter {

    int mNumOfTabs;
    ArrayList<String> tabName;
    public OrderAndPaymentAdapter(FragmentManager fm, int NumOfTabs, ArrayList<String> tabName) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.tabName=tabName;
    }

    @Override
    public Fragment getItem(int position) {
        // if(position==0){
        //     InvoiceFragment comn=new InvoiceFragment();
        //     return comn.newInstance(tabName.get(position));
        // }else if(position==1){
        //     PaymentFragment paymentFragment = new PaymentFragment();
        //     return paymentFragment.newInstance(tabName.get(position));
        // }else if(position==2){
        //     RequestOrderFragment requestOrderFragment =new RequestOrderFragment();
        //     return requestOrderFragment.newInstance(tabName.get(position));
        // }else  {
        //     SalesOrderFragment salesOrderFragment = new SalesOrderFragment();
        //     return salesOrderFragment.newInstance(tabName.get(position));
        // }

        if(position==0){
            InvoiceFragment comn=new InvoiceFragment();
            return comn.newInstance(tabName.get(position));
        }else if(position==1){
            PaymentFragment paymentFragment = new PaymentFragment();
            return paymentFragment.newInstance(tabName.get(position));
        }else {
            RequestOrderFragment requestOrderFragment =new RequestOrderFragment();
            return requestOrderFragment.newInstance(tabName.get(position));
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
