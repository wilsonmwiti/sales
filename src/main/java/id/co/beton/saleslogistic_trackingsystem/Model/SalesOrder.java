package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class SalesOrder
 */
public class SalesOrder {
    /**
     * The Product.
     */
    @SerializedName("product")
    public List<Product> product;

    /**
     * The Code.
     */
    @SerializedName("code")
    public String code;

    /**
     * The Import by.
     */
    @SerializedName("import_by")
    public int importBy;

    /**
     * The Notes.
     */
    @SerializedName("notes")
    public String notes;

    /**
     * The Sales group.
     */
    @SerializedName("sales_group")
    public String salesGroup;

    /**
     * The Customer branch code.
     */
    @SerializedName("customer_branch_code")
    public String customerBranchCode;

    /**
     * The Type.
     */
    @SerializedName("type")
    public String type;

    /**
     * The Invoice amount.
     */
    @SerializedName("invoice_amount")
    public int invoiceAmount;

    /**
     * The Update date.
     */
    @SerializedName("update_date")
    public String updateDate;

    /**
     * The Import date.
     */
    @SerializedName("import_date")
    public String importDate;

    /**
     * The Invoice date.
     */
    @SerializedName("invoice_date")
    public String invoiceDate;

    /**
     * The Invoice code.
     */
    @SerializedName("invoice_code")
    public String invoiceCode;

    /**
     * The Cycle number.
     */
    @SerializedName("cycle_number")
    public String cycleNumber;

    /**
     * The User code.
     */
    @SerializedName("user_code")
    public String userCode;

    /**
     * The Packing slip code.
     */
    @SerializedName("packing_slip_code")
    public String packingSlipCode;

    /**
     * The Packing slip date.
     */
    @SerializedName("packing_slip_date")
    public String packingSlipDate;

    /**
     * The Create date.
     */
    @SerializedName("create_date")
    public String createDate;

    /**
     * The Customer code.
     */
    @SerializedName("customer_code")
    public String customerCode;

    /**
     * The User.
     */
    @SerializedName("user")
    public User user;

    /**
     * The Customer.
     */
    @SerializedName("customer")
    public Customer customer;

    /**
     * The Division code.
     */
    @SerializedName("division_code")
    public String divisionCode;

    /**
     * The Status.
     */
    @SerializedName("status")
    public String status;

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
     * Gets import by.
     *
     * @return the import by
     */
    public int getImportBy() {
        return importBy;
    }

    /**
     * Sets import by.
     *
     * @param importBy the import by
     */
    public void setImportBy(int importBy) {
        this.importBy = importBy;
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
     * Gets customer branch code.
     *
     * @return the customer branch code
     */
    public String getCustomerBranchCode() {
        return customerBranchCode;
    }

    /**
     * Sets customer branch code.
     *
     * @param customerBranchCode the customer branch code
     */
    public void setCustomerBranchCode(String customerBranchCode) {
        this.customerBranchCode = customerBranchCode;
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
     * Gets invoice amount.
     *
     * @return the invoice amount
     */
    public int getInvoiceAmount() {
        return invoiceAmount;
    }

    /**
     * Sets invoice amount.
     *
     * @param invoiceAmount the invoice amount
     */
    public void setInvoiceAmount(int invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
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
     * Gets invoice date.
     *
     * @return the invoice date
     */
    public String getInvoiceDate() {
        return invoiceDate;
    }

    /**
     * Sets invoice date.
     *
     * @param invoiceDate the invoice date
     */
    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    /**
     * Gets invoice code.
     *
     * @return the invoice code
     */
    public String getInvoiceCode() {
        return invoiceCode;
    }

    /**
     * Sets invoice code.
     *
     * @param invoiceCode the invoice code
     */
    public void setInvoiceCode(String invoiceCode) {
        this.invoiceCode = invoiceCode;
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

    /**
     * Gets packing slip code.
     *
     * @return the packing slip code
     */
    public String getPackingSlipCode() {
        return packingSlipCode;
    }

    /**
     * Sets packing slip code.
     *
     * @param packingSlipCode the packing slip code
     */
    public void setPackingSlipCode(String packingSlipCode) {
        this.packingSlipCode = packingSlipCode;
    }

    /**
     * Gets packing slip date.
     *
     * @return the packing slip date
     */
    public String getPackingSlipDate() {
        return packingSlipDate;
    }

    /**
     * Sets packing slip date.
     *
     * @param packingSlipDate the packing slip date
     */
    public void setPackingSlipDate(String packingSlipDate) {
        this.packingSlipDate = packingSlipDate;
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
}
