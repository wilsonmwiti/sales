package id.co.beton.saleslogistic_trackingsystem.Utils;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Class SalesRequest for Sales Request
 */
public class SalesRequest extends Utils {
    private static SalesRequest instance;
    private String[] listKey= new String[]{"customer_code","is_special_order","contacts","delivery_address","notes"};

    /**
     * Gets instance.
     *
     * @param context the context
     * @return the instance
     */
    public static SalesRequest getInstance(Context context) {
        if (instance == null) {
            instance = new SalesRequest();
            instance.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        }

        return instance;
    }

    /**
     * Get customer code string.
     *
     * @return the string
     */
    public String getCustomerCode(){
        return getStringProperty("customer_code");
    }

    /**
     * Set customer code.
     *
     * @param value the value
     */
    public void setCustomerCode(String value){
        setStringProperty("customer_code",value);
    }

    /**
     * Get is special order string.
     *
     * @return the string
     */
    public String getIsSpecialOrder(){
        return getStringProperty("is_special_order");
    }

    /**
     * Set is special order.
     *
     * @param value the value
     */
    public void setIsSpecialOrder(String value){
        setStringProperty("is_special_order",value);
    }

    /**
     * Get contacts string.
     *
     * @return the string
     */
    public String getContacts(){
        return getStringProperty("contacts");
    }

    /**
     * Set contacts.
     *
     * @param value the value
     */
    public void setContacts(String value){
        setStringProperty("contacts",value);
    }

    /**
     * Get delivery address string.
     *
     * @return the string
     */
    public String getDeliveryAddress(){
        return getStringProperty("delivery_address");
    }

    /**
     * Set delivery address.
     *
     * @param value the value
     */
    public void setDeliveryAddress(String value){
        setStringProperty("delivery_address",value);
    }

    /**
     * Get notes string.
     *
     * @return the string
     */
    public String getNotes(){
        return getStringProperty("notes");
    }

    /**
     * Set notes.
     *
     * @param value the value
     */
    public void setNotes(String value){
        setStringProperty("notes",value);
    }


    /**
     * Clear sales request.
     */
    public void clearSalesRequest(){
        for (int i=0;i<listKey.length;i++){
            removeByKey(listKey[i]);
        }
    }
}
