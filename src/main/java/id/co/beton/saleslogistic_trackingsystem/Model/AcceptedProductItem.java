package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class AcceptedProductItem
 */
public class AcceptedProductItem implements Serializable {

    @SerializedName("quantity")
    private double quantity;

    @SerializedName("notes")
    private String notes;

    @SerializedName("item_number")
    private String itemNumber;

    @SerializedName("accepted_quantity")
    private double acceptedQuantity;

    @SerializedName("product_name")
    private String productName;

    /**
     * Instantiates a new Accepted product item.
     *
     * @param quantity         the quantity
     * @param notes            the notes
     * @param itemNumber       the item number
     * @param acceptedQuantity the accepted quantity
     * @param productName      the product name
     */
    public AcceptedProductItem(double quantity, String notes, String itemNumber, double acceptedQuantity, String productName) {
        this.quantity = quantity;
        this.notes = notes;
        this.itemNumber = itemNumber;
        this.acceptedQuantity = acceptedQuantity;
        this.productName = productName;
    }

    /**
     * Set quantity.
     *
     * @param quantity the quantity
     */
    public void setQuantity(double quantity){
        this.quantity = quantity;
    }

    /**
     * Get quantity double.
     *
     * @return the double
     */
    public double getQuantity(){
        return quantity;
    }

    /**
     * Set notes.
     *
     * @param notes the notes
     */
    public void setNotes(String notes){
        this.notes = notes;
    }

    /**
     * Get notes string.
     *
     * @return the string
     */
    public String getNotes(){
        return notes;
    }

    /**
     * Set item number.
     *
     * @param itemNumber the item number
     */
    public void setItemNumber(String itemNumber){
        this.itemNumber = itemNumber;
    }

    /**
     * Get item number string.
     *
     * @return the string
     */
    public String getItemNumber(){
        return itemNumber;
    }

    /**
     * Set accepted quantity.
     *
     * @param acceptedQuantity the accepted quantity
     */
    public void setAcceptedQuantity(double acceptedQuantity){
        this.acceptedQuantity = acceptedQuantity;
    }

    /**
     * Get accepted quantity double.
     *
     * @return the double
     */
    public double getAcceptedQuantity(){
        return acceptedQuantity;
    }

    /**
     * Set product name.
     *
     * @param productName the product name
     */
    public void setProductName(String productName){
        this.productName = productName;
    }

    /**
     * Get product name string.
     *
     * @return the string
     */
    public String getProductName(){
        return productName;
    }
}
