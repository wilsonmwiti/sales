package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class Invoice
 */
public class Invoice{

    @SerializedName("customer_code")
    @Expose
    private String customerCode;

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("customer_name")
    @Expose
    private String customerName;

    @SerializedName("id_invoice")
    @Expose
    private String idInvoice;
    @SerializedName("invoice_amount")
    @Expose
    private Integer invoiceAmount=0;
    @SerializedName("is_confirm")
    @Expose
    private Integer isConfirm;
    @SerializedName("packing_slip_code")
    @Expose
    private String packingSlipCode;
    @SerializedName("packing_slip_date")
    @Expose
    private String packingSlipDate;
    @SerializedName("product")
    @Expose
    private List<ProductInvoice> product = null;
    @SerializedName("sales_order_id")
    @Expose
    private String salesOrderId;
    @SerializedName("payment_amount")
    @Expose
    private Integer paymentAmount;

    private boolean isSelected;

    @SerializedName("invoice_id")
    @Expose
    private String invoiceId;

    @SerializedName("payment_info")
    @Expose
    private List<PaymentInfo> paymentInfos;

    /**
     * Is selected boolean.
     *
     * @return the boolean
     */
    public boolean isSelected() {
        return isSelected;
    }

    /**
     * Sets selected.
     *
     * @param selected the selected
     */
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    /**
     * Gets payment amount.
     *
     * @return the payment amount
     */
    public Integer getPaymentAmount() {
        return paymentAmount;
    }

    /**
     * Sets payment amount.
     *
     * @param paymentAmount the payment amount
     */
    public void setPaymentAmount(Integer paymentAmount) {
        this.paymentAmount = paymentAmount;
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
     * Gets id invoice.
     *
     * @return the id invoice
     */
    public String getIdInvoice() {
        return idInvoice;
    }

    /**
     * Sets id invoice.
     *
     * @param idInvoice the id invoice
     */
    public void setIdInvoice(String idInvoice) {
        this.idInvoice = idInvoice;
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
     * Gets is confirm.
     *
     * @return the is confirm
     */
    public Integer getIsConfirm() {
        return isConfirm;
    }

    /**
     * Sets is confirm.
     *
     * @param isConfirm the is confirm
     */
    public void setIsConfirm(Integer isConfirm) {
        this.isConfirm = isConfirm;
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
     * Gets product.
     *
     * @return the product
     */
    public List<ProductInvoice> getProduct() {
        return product;
    }

    /**
     * Sets product.
     *
     * @param product the product
     */
    public void setProduct(List<ProductInvoice> product) {
        this.product = product;
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
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets customer name.
     *
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets customer name.
     *
     * @param customerName the customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets invoice id.
     *
     * @return the invoice id
     */
    public String getInvoiceId() {
        return invoiceId;
    }

    /**
     * Sets invoice id.
     *
     * @param invoiceId the invoice id
     */
    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * Gets payment infos.
     *
     * @return the payment infos
     */
    public List<PaymentInfo> getPaymentInfos() {
        return paymentInfos;
    }

    /**
     * Sets payment infos.
     *
     * @param paymentInfos the payment infos
     */
    public void setPaymentInfos(List<PaymentInfo> paymentInfos) {
        this.paymentInfos = paymentInfos;
    }

    /**
     * Get total payment by method int.
     *
     * @param type the type
     * @return the int
     */
    public int getTotalPaymentByMethod(String type){
        int total=0;
        if(getPaymentInfos()!=null){
            for(int i=0; i<getPaymentInfos().size();i++){
                if(getPaymentInfos().get(i).getType().toLowerCase().equalsIgnoreCase(type)){
                    total = getPaymentInfos().get(i).getTotal();
                    break;
                }
            }
        }

        return total;
    }
}
