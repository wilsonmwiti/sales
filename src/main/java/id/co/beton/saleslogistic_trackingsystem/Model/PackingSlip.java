package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class PackingSlip
 */
public class PackingSlip {

    @SerializedName("product")
    private List<Product> product;

    @SerializedName("id_packing_slip")
    private String idPackingSlip;

    @SerializedName("is_confirm")
    private int isConfirm;

    @SerializedName("sales_order_id")
    private String salesOrderId;

    @SerializedName("customer_code")
    private String customerCode;

    @SerializedName("status")
    private String status;

    @SerializedName("user")
    private User user;

    private Boolean isSelected=false;

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
     * Gets id packing slip.
     *
     * @return the id packing slip
     */
    public String getIdPackingSlip() {
        return idPackingSlip;
    }

    /**
     * Sets id packing slip.
     *
     * @param idPackingSlip the id packing slip
     */
    public void setIdPackingSlip(String idPackingSlip) {
        this.idPackingSlip = idPackingSlip;
    }

    /**
     * Gets is confirm.
     *
     * @return the is confirm
     */
    public int getIsConfirm() {
        return isConfirm;
    }

    /**
     * Sets is confirm.
     *
     * @param isConfirm the is confirm
     */
    public void setIsConfirm(int isConfirm) {
        this.isConfirm = isConfirm;
    }

    /**
     * Gets sales order id.
     *
     * @return the sales order id
     */
    public String getSalesOrderId() {
        return salesOrderId;
    }

    /**
     * Sets sales order id.
     *
     * @param salesOrderId the sales order id
     */
    public void setSalesOrderId(String salesOrderId) {
        this.salesOrderId = salesOrderId;
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
     * Gets selected.
     *
     * @return the selected
     */
    public Boolean getSelected() {
        return isSelected;
    }

    /**
     * Sets selected.
     *
     * @param selected the selected
     */
    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
