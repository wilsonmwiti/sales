package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class RequestOrder
 */
public class RequestOrder {
    @SerializedName("date")
    private String date;

    @SerializedName("delivery_address")
    private String deliveryAddress;

    @SerializedName("code")
    private String code;

    @SerializedName("notes")
    private String notes;

    @SerializedName("lng")
    private String lng;

    @SerializedName("is_special_order")
    private int isSpecialOrder;

    @SerializedName("type")
    private String type;

    @SerializedName("update_date")
    private String updateDate;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("id")
    private int id;

    @SerializedName("create_date")
    private String createDate;

    @SerializedName("customer_code")
    private String customerCode;

    @SerializedName("user")
    private User user;

    @SerializedName("contacts")
    private String contacts;

    @SerializedName("lat")
    private String lat;

    @SerializedName("customer")
    private Customer customer;

    @SerializedName("product")
    @Expose
    private List<Product> product = null;

    /**
     * Set date.
     *
     * @param date the date
     */
    public void setDate(String date){
        this.date = date;
    }

    /**
     * Get date string.
     *
     * @return the string
     */
    public String getDate(){
        return date;
    }

    /**
     * Set delivery address.
     *
     * @param deliveryAddress the delivery address
     */
    public void setDeliveryAddress(String deliveryAddress){
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * Get delivery address string.
     *
     * @return the string
     */
    public String getDeliveryAddress(){
        return deliveryAddress;
    }

    /**
     * Set code.
     *
     * @param code the code
     */
    public void setCode(String code){
        this.code = code;
    }

    /**
     * Get code string.
     *
     * @return the string
     */
    public String getCode(){
        return code;
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
     * Set lng.
     *
     * @param lng the lng
     */
    public void setLng(String lng){
        this.lng = lng;
    }

    /**
     * Get lng string.
     *
     * @return the string
     */
    public String getLng(){
        return lng;
    }

    /**
     * Set is special order.
     *
     * @param isSpecialOrder the is special order
     */
    public void setIsSpecialOrder(int isSpecialOrder){
        this.isSpecialOrder = isSpecialOrder;
    }

    /**
     * Get is special order int.
     *
     * @return the int
     */
    public int getIsSpecialOrder(){
        return isSpecialOrder;
    }

    /**
     * Set type.
     *
     * @param type the type
     */
    public void setType(String type){
        this.type = type;
    }

    /**
     * Get type string.
     *
     * @return the string
     */
    public String getType(){
        return type;
    }

    /**
     * Set update date.
     *
     * @param updateDate the update date
     */
    public void setUpdateDate(String updateDate){
        this.updateDate = updateDate;
    }

    /**
     * Get update date string.
     *
     * @return the string
     */
    public String getUpdateDate(){
        return updateDate;
    }

    /**
     * Set user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId){
        this.userId = userId;
    }

    /**
     * Get user id int.
     *
     * @return the int
     */
    public int getUserId(){
        return userId;
    }

    /**
     * Set id.
     *
     * @param id the id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Get id int.
     *
     * @return the int
     */
    public int getId(){
        return id;
    }

    /**
     * Set create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(String createDate){
        this.createDate = createDate;
    }

    /**
     * Get create date string.
     *
     * @return the string
     */
    public String getCreateDate(){
        return createDate;
    }

    /**
     * Set customer code.
     *
     * @param customerCode the customer code
     */
    public void setCustomerCode(String customerCode){
        this.customerCode = customerCode;
    }

    /**
     * Get customer code string.
     *
     * @return the string
     */
    public String getCustomerCode(){
        return customerCode;
    }

    /**
     * Set user.
     *
     * @param user the user
     */
    public void setUser(User user){
        this.user = user;
    }

    /**
     * Get user user.
     *
     * @return the user
     */
    public User getUser(){
        return user;
    }

    /**
     * Set contacts.
     *
     * @param contacts the contacts
     */
    public void setContacts(String contacts){
        this.contacts = contacts;
    }

    /**
     * Get contacts string.
     *
     * @return the string
     */
    public String getContacts(){
        return contacts;
    }

    /**
     * Set lat.
     *
     * @param lat the lat
     */
    public void setLat(String lat){
        this.lat = lat;
    }

    /**
     * Get lat string.
     *
     * @return the string
     */
    public String getLat(){
        return lat;
    }

    /**
     * Set customer.
     *
     * @param customer the customer
     */
    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    /**
     * Get customer customer.
     *
     * @return the customer
     */
    public Customer getCustomer(){
        return customer;
    }

    /**
     * Gets product.
     *
     * @return the product
     */
    public List<Product> getProduct() {
        return product;
    }

    /**
     * Sets product.
     *
     * @param product the product
     */
    public void setProduct(List<Product> product) {
        this.product = product;
    }
}
