package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class RequestOrderAndSalesOrderData
 */
public class RequestOrderAndSalesOrderData {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("contacts")
    @Expose
    private String contacts;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("customer")
    @Expose
    private Customer customer;
    @SerializedName("customer_code")
    @Expose
    private String customerCode;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("delivery_address")
    @Expose
    private String deliveryAddress;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("is_special_order")
    @Expose
    private Integer isSpecialOrder;
    @SerializedName("lat")
    @Expose
    private Object lat;
    @SerializedName("lng")
    @Expose
    private Object lng;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("update_date")
    @Expose
    private String updateDate;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("customer_branch_code")
    @Expose
    private Object customerBranchCode;
    @SerializedName("cycle_number")
    @Expose
    private String cycleNumber;
    @SerializedName("division_code")
    @Expose
    private String divisionCode;
    @SerializedName("import_by")
    @Expose
    private Integer importBy;
    @SerializedName("import_date")
    @Expose
    private String importDate;
    @SerializedName("invoice_amount")
    @Expose
    private Integer invoiceAmount;
    @SerializedName("invoice_code")
    @Expose
    private Object invoiceCode;
    @SerializedName("invoice_date")
    @Expose
    private Object invoiceDate;
    @SerializedName("packing_slip_code")
    @Expose
    private Object packingSlipCode;
    @SerializedName("packing_slip_date")
    @Expose
    private Object packingSlipDate;
    @SerializedName("product")
    @Expose
    private List<Product> product = null;
    @SerializedName("sales_group")
    @Expose
    private String salesGroup;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("user_code")
    @Expose
    private String userCode;

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Gets contacts.
     *
     * @return the contacts
     */
    public String getContacts() {
        return contacts;
    }

    /**
     * Sets contacts.
     *
     * @param contacts the contacts
     */
    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    /**
     * Gets create date.
     *
     * @return the create date
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Sets create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets customer.
     *
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Sets customer.
     *
     * @param customer the customer
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
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

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * Gets delivery address.
     *
     * @return the delivery address
     */
    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    /**
     * Sets delivery address.
     *
     * @param deliveryAddress the delivery address
     */
    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets is special order.
     *
     * @return the is special order
     */
    public Integer getIsSpecialOrder() {
        return isSpecialOrder;
    }

    /**
     * Sets is special order.
     *
     * @param isSpecialOrder the is special order
     */
    public void setIsSpecialOrder(Integer isSpecialOrder) {
        this.isSpecialOrder = isSpecialOrder;
    }

    /**
     * Gets lat.
     *
     * @return the lat
     */
    public Object getLat() {
        return lat;
    }

    /**
     * Sets lat.
     *
     * @param lat the lat
     */
    public void setLat(Object lat) {
        this.lat = lat;
    }

    /**
     * Gets lng.
     *
     * @return the lng
     */
    public Object getLng() {
        return lng;
    }

    /**
     * Sets lng.
     *
     * @param lng the lng
     */
    public void setLng(Object lng) {
        this.lng = lng;
    }

    /**
     * Gets notes.
     *
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets notes.
     *
     * @param notes the notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets update date.
     *
     * @return the update date
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * Sets update date.
     *
     * @param updateDate the update date
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Gets customer branch code.
     *
     * @return the customer branch code
     */
    public Object getCustomerBranchCode() {
        return customerBranchCode;
    }

    /**
     * Sets customer branch code.
     *
     * @param customerBranchCode the customer branch code
     */
    public void setCustomerBranchCode(Object customerBranchCode) {
        this.customerBranchCode = customerBranchCode;
    }

    /**
     * Gets cycle number.
     *
     * @return the cycle number
     */
    public String getCycleNumber() {
        return cycleNumber;
    }

    /**
     * Sets cycle number.
     *
     * @param cycleNumber the cycle number
     */
    public void setCycleNumber(String cycleNumber) {
        this.cycleNumber = cycleNumber;
    }

    /**
     * Gets division code.
     *
     * @return the division code
     */
    public String getDivisionCode() {
        return divisionCode;
    }

    /**
     * Sets division code.
     *
     * @param divisionCode the division code
     */
    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    /**
     * Gets import by.
     *
     * @return the import by
     */
    public Integer getImportBy() {
        return importBy;
    }

    /**
     * Sets import by.
     *
     * @param importBy the import by
     */
    public void setImportBy(Integer importBy) {
        this.importBy = importBy;
    }

    /**
     * Gets import date.
     *
     * @return the import date
     */
    public String getImportDate() {
        return importDate;
    }

    /**
     * Sets import date.
     *
     * @param importDate the import date
     */
    public void setImportDate(String importDate) {
        this.importDate = importDate;
    }

    /**
     * Gets invoice amount.
     *
     * @return the invoice amount
     */
    public Integer getInvoiceAmount() {
        return invoiceAmount;
    }

    /**
     * Sets invoice amount.
     *
     * @param invoiceAmount the invoice amount
     */
    public void setInvoiceAmount(Integer invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    /**
     * Gets invoice code.
     *
     * @return the invoice code
     */
    public Object getInvoiceCode() {
        return invoiceCode;
    }

    /**
     * Sets invoice code.
     *
     * @param invoiceCode the invoice code
     */
    public void setInvoiceCode(Object invoiceCode) {
        this.invoiceCode = invoiceCode;
    }

    /**
     * Gets invoice date.
     *
     * @return the invoice date
     */
    public Object getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * Sets invoice date.
     *
     * @param invoiceDate the invoice date
     */
    public void setInvoiceDate(Object invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**
     * Gets packing slip code.
     *
     * @return the packing slip code
     */
    public Object getPackingSlipCode() {
        return packingSlipCode;
    }

    /**
     * Sets packing slip code.
     *
     * @param packingSlipCode the packing slip code
     */
    public void setPackingSlipCode(Object packingSlipCode) {
        this.packingSlipCode = packingSlipCode;
    }

    /**
     * Gets packing slip date.
     *
     * @return the packing slip date
     */
    public Object getPackingSlipDate() {
        return packingSlipDate;
    }

    /**
     * Sets packing slip date.
     *
     * @param packingSlipDate the packing slip date
     */
    public void setPackingSlipDate(Object packingSlipDate) {
        this.packingSlipDate = packingSlipDate;
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

    /**
     * Gets sales group.
     *
     * @return the sales group
     */
    public String getSalesGroup() {
        return salesGroup;
    }

    /**
     * Sets sales group.
     *
     * @param salesGroup the sales group
     */
    public void setSalesGroup(String salesGroup) {
        this.salesGroup = salesGroup;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets user code.
     *
     * @return the user code
     */
    public String getUserCode() {
        return userCode;
    }

    /**
     * Sets user code.
     *
     * @param userCode the user code
     */
    public void setUserCode(String userCode) {
        this.userCode = userCode;
    }
}
