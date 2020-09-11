package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.DetailVisitPlanAdapter;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.PackingSlip.PackingSlipFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Info.InfoFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Invoice.InvoiceFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Order.OrderFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Payment.PaymentFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Memo.MemoFragment;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Report.ReportFragment;
import id.co.beton.saleslogistic_trackingsystem.BaseActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Developer.NfcSimulationReceiver;
import id.co.beton.saleslogistic_trackingsystem.Developer.TapNfcSimulationService;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

/**
 * Class DetailPlanActivity
 * detail of activity per customer
 */
// public class DetailPlanActivity extends NfcActivity {
public class DetailPlanActivity extends BaseActivity {
    private static final String TAG = DetailPlanActivity.class.getSimpleName();

    /*
    *  properties of report
    * */
//    public static String report_lokasi;
//    public static void setReport_lokasi(String report){
//        report_lokasi = report;
//    }
//    public static String getReport_lokasi(){
//        return report_lokasi;
//    }
//
//    public static String report_toko_tutup;
//    public static void setReport_toko_tutup(String report){
//        report_toko_tutup = report;
//    }
//    public static String getReport_toko_tutup(){
//        return report_toko_tutup;
//    }
//
//    public static String report_cetak;
//    public static void setReport_cetak(String report){
//        report_cetak = report;
//    }
//    public static String getReport_cetak(){
//        return report_cetak;
//    }
//
//    public static String report_lainnya;
//    public static void setReport_lainnya(String report){
//        report_lainnya = report;
//    }
//    public static String getReport_lainnya(){
//        return report_lainnya;
//    }

    /*
    *  properties of report end
    * */

    private Bundle inBundle;

    // This is our tablayout
    private TabLayout tabLayout;

    // This is our viewPager
    private ViewPager viewPager;

    // Fragment
    InfoFragment infoFragment;
    InvoiceFragment invoiceFragment;
    OrderFragment orderFragment;
    PaymentFragment paymentFragment;
    ReportFragment reportFragment;
    PackingSlipFragment packingSlipFragment;
    MemoFragment memoFragment;

    String[] tabTitle;
    UserUtil userUtil;
    private Intent intent;
    private String myVersionName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_detail_visit_plan);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            Bundle inBundle = getIntent().getExtras();

            String customerName = inBundle.get("customer_name").toString();
            setTitle(customerName);

            context = this;
            activity = DetailPlanActivity.this;
            userUtil = UserUtil.getInstance(context);

            // load version name
            myVersionName = userUtil.getVersionApps(context);
            TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
            mTitle.setText("v" + myVersionName);

            enableGps();
            getLastKnownLocation();

            try {
                Gson gson = new Gson();
                Destination destination = gson.fromJson(getIntent().getExtras().get("destination").toString(), new TypeToken<Destination>() {
                }.getType());
                // lat = destination.getLat();
                // lng = destination.getLng();

                customerName = destination.getCustomerName();

                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);

                //Initializing viewPager
                viewPager = (ViewPager) findViewById(R.id.viewpager);

                setupViewPager(viewPager);

                //Initializing the tablayout
                tabLayout = (TabLayout) findViewById(R.id.tablayout);
                tabLayout.setupWithViewPager(viewPager);

                try {
                    setupTabIcons();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // nfcAdapter = NfcAdapter.getDefaultAdapter(this);
                //
                // if (nfcAdapter == null) {
                //     Toasty.info(this, "This device does'n support NFC", Toast.LENGTH_SHORT).show();
                // } else {
                //     if (!nfcAdapter.isEnabled()) {
                //         Toasty.info(this, "Silahkan aktifkan fitur NFC", Toast.LENGTH_SHORT).show();
                //         showNFCSetting();
                //     }
                // }
                //
                // type = 0;
                //
                // pendingIntent = PendingIntent.getActivity(context, 0,
                //         new Intent(this, this.getClass())
                //                 .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);


                viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    }

                    @Override
                    public void onPageSelected(int position) {
                        viewPager.setCurrentItem(position, false);

                    }

                    @Override
                    public void onPageScrollStateChanged(int state) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                //Intent intent = new Intent(context, MainActivity.class);
                //startActivity(intent);
                finish();
                break;
            case R.id.logo:
                Toasty.info(context, "Silahkan Check In").show();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /*@Override
    public boolean onPreparePanel(int featureId, View view, Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.second,menu);
        return true;
    }*/

    private void setupViewPager(ViewPager viewPager) {
        DetailVisitPlanAdapter adapter = new DetailVisitPlanAdapter(getSupportFragmentManager());

        inBundle = getIntent().getExtras();

        Bundle bundle = new Bundle();
        bundle.putString("visit_plan", "");
        bundle.putString("destination", inBundle.get("destination").toString());
        bundle.putString("cust_code", inBundle.get("customer_code").toString());
        bundle.putString("cust_name", inBundle.get("customer_name").toString());
        Log.e(TAG, "Status Dev Mode :" + Constants.DEV_MODE);
        if (Constants.DEV_MODE) {
            intent = new Intent(context, TapNfcSimulationService.class);
            intent.putExtra("type", NfcSimulationReceiver.TAP_IN);
            intent.putExtra("tap_type", TapNfcSimulationService.TAP_CUSTOMER_OUT);
            intent.putExtra("customer_code", inBundle.get("customer_code").toString());
            intent.putExtra("role", userUtil.getRoleUser());
            context.startService(intent);
        }
        if (userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)) {
            // tabTitle = new String[]{"Info", "Packing Slip", "Report"};
            tabTitle = new String[]{"Info", "Packing Slip", "Report", "Memo"};

            // set Fragment class Arguments
            infoFragment = new InfoFragment();
            infoFragment.setArguments(bundle);

            packingSlipFragment = new PackingSlipFragment();
            packingSlipFragment.setArguments(bundle);

            reportFragment = new ReportFragment();
            reportFragment.setArguments(bundle);

            memoFragment = new MemoFragment();
            memoFragment.setArguments(bundle);

            adapter.addFragment(infoFragment, "Info");
            adapter.addFragment(packingSlipFragment, "Packing Slip");
            adapter.addFragment(reportFragment, "Report");
            adapter.addFragment(memoFragment, "Memo");
        } else {
            // tabTitle = new String[]{"Info", "Invoice", "Order", "Payment", "Report"};
            if(userUtil.isCollectorOnly()){
                tabTitle = new String[]{"Info", "Invoice", "Payment", "Report", "Memo"};
            } else {
                tabTitle = new String[]{"Info", "Invoice", "Order", "Payment", "Report", "Memo"};
            }

            tabTitle = new String[]{"Info", "Report", "Memo"};


            // set Fragment class Arguments
            infoFragment = new InfoFragment();
            infoFragment.setArguments(bundle);
//
//            invoiceFragment = new InvoiceFragment();
//            invoiceFragment.setArguments(bundle);

//            if(!userUtil.isCollectorOnly()){
//                orderFragment = new OrderFragment();
//                orderFragment.setArguments(bundle);
//            }
//
//            paymentFragment = new PaymentFragment();
//            paymentFragment.setArguments(bundle);

            reportFragment = new ReportFragment();
            reportFragment.setArguments(bundle);

            memoFragment = new MemoFragment();
            memoFragment.setArguments(bundle);

            adapter.addFragment(infoFragment, "Info");
//            adapter.addFragment(invoiceFragment, "Invoice");
//            if(!userUtil.isCollectorOnly()){
//                adapter.addFragment(orderFragment, "Order");
//            }
//            adapter.addFragment(paymentFragment, "Payment");
            adapter.addFragment(reportFragment, "Report");
            adapter.addFragment(memoFragment, "Memo");
        }

        viewPager.setAdapter(adapter);
    }

    private View prepareTabView(int pos) {
        View view = getLayoutInflater().inflate(R.layout.custom_tab, null);
        TextView tv_title = (TextView) view.findViewById(R.id.tv_title);
        tv_title.setText(tabTitle[pos]);

        return view;
    }

    private void setupTabIcons() {

        for (int i = 0; i < tabTitle.length; i++) {
            /*TabLayout.Tab tabitem = tabLayout.newTab();
            tabitem.setCustomView(prepareTabView(i));
            tabLayout.addTab(tabitem);*/

            tabLayout.getTabAt(i).setCustomView(prepareTabView(i));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final int unmaskedRequestCode = requestCode & 0x0000ffff;

        Log.d("onActivityResult", "requestCode = " + unmaskedRequestCode);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(unmaskedRequestCode, resultCode, data);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (Constants.DEV_MODE) {
            context.stopService(intent);
        }
    }
}
