package id.co.beton.saleslogistic_trackingsystem.Utils;

import android.content.Context;
import android.preference.PreferenceManager;

import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Model.Asset;


/**
 * Class PlanUtil
 * to set plan id, invoice and packing slip size, and setting whether using route or not
 */
public class PlanUtil extends Utils {
    private static PlanUtil instance;
    private String[] listKey= new String[]{Constants.PLAN_ID};

    /**
     * Gets instance.
     *
     * @param context the context
     * @return the instance
     */
    public static PlanUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PlanUtil();
            instance.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return instance;
    }

    /**
     * Set plan id.
     *
     * @param value the value
     */
    public void setPlanId(Integer value){
        setIntProperty(Constants.PLAN_ID,value);
    }

    /**
     * Get plan id integer.
     *
     * @return the integer
     */
    public Integer getPlanId(){
        return  getIntProperty(Constants.PLAN_ID);
    }

    /**
     * Clear data plan.
     */
    public void clearDataPlan(){
        for (int i=0;i<listKey.length;i++){
            removeByKey(listKey[i]);
        }
    }

    /**
     * Set invoice size.
     *
     * @param value the value
     */
    public void setInvoiceSize(Integer value){ setIntProperty(Constants.INVOICE_SIZE,value); }

    /**
     * Gets invoice size.
     *
     * @return the invoice size
     */
    public Integer getInvoiceSize() {return getIntProperty(Constants.INVOICE_SIZE);}

    /**
     * Set packing slip size.
     *
     * @param value the value
     */
    public void setPackingSlipSize(Integer value){ setIntProperty(Constants.PACKING_SLIP_SIZE,value); }

    /**
     * Gets packing slip size.
     *
     * @return the packing slip size
     */
    public Integer getPackingSlipSize() {return getIntProperty(Constants.PACKING_SLIP_SIZE);}

    /**
     * Set is using route.
     *
     * @param value the value
     */
    public void setIsUsingRoute(Integer value ){
        setIntProperty(Constants.IS_USING_ROUTE, value);
    }

    /**
     * Is using route boolean.
     *
     * @return the boolean
     */
    public boolean isUsingRoute(){
        return getIntProperty(Constants.IS_USING_ROUTE) == 1;
    }

    public void setIsPlanCustome(Integer value){ setIntProperty(Constants.CUSTOM_PLAN, value);}

    public boolean isPlanCustome(){ return getIntProperty(Constants.CUSTOM_PLAN) == 1; }

    public void setAsset(String assetCode, String assetName){
        setStringProperty(Constants.ASSET_CODE, assetCode);
        setStringProperty(Constants.ASSET_NAME, assetName);
    }

    public String[] getAsset(){
        String[] asset = new String[2];
        asset[0] = getStringProperty(Constants.ASSET_CODE);
        asset[1] = getStringProperty(Constants.ASSET_NAME);

        return asset;
    }
}
