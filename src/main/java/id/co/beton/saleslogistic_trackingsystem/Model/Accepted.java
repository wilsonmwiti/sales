package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Class Accepted
 */
public class Accepted implements Serializable {

    @SerializedName("product")
    private List<AcceptedProductItem> product;

    @SerializedName("customer_code")
    private String customerCode;


    @SerializedName("delivery_plan_id")
    private int deliveryPlanId;

    @SerializedName("acepted_by")
    private String aceptedBy;

    /**
     * Set product.
     *
     * @param product the product
     */
    public void setProduct(List<AcceptedProductItem> product){
        this.product = product;
    }

    /**
     * Get product list.
     *
     * @return the list
     */
    public List<AcceptedProductItem> getProduct(){
        return product;
    }

    /**
     * Set delivery plan id.
     *
     * @param deliveryPlanId the delivery plan id
     */
    public void setDeliveryPlanId(int deliveryPlanId){
        this.deliveryPlanId = deliveryPlanId;
    }

    /**
     * Get delivery plan id int.
     *
     * @return the int
     */
    public int getDeliveryPlanId(){
        return deliveryPlanId;
    }

    /**
     * Set acepted by.
     *
     * @param aceptedBy the acepted by
     */
    public void setAceptedBy(String aceptedBy){
        this.aceptedBy = aceptedBy;
    }

    /**
     * Get acepted by string.
     *
     * @return the string
     */
    public String getAceptedBy(){
        return aceptedBy;
    }

    /**
     * Gets customer code.
     *
     * @return the customer code
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * Sets customer code.
     *
     * @param customerCode the customer code
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }


}
