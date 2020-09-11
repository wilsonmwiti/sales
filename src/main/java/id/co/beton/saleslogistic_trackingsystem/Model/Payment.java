package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Class Payment
 */
public class Payment {
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("create_by")
    @Expose
    private Integer createBy;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("customer_code")
    @Expose
    private String customerCode;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("invoice")
    @Expose
    private List<Invoice> invoice = null;
    @SerializedName("invoice_amount")
    @Expose
    private Integer invoiceAmount;
    @SerializedName("is_canceled")
    @Expose
    private Integer isCanceled;
    @SerializedName("is_confirm")
    @Expose
    private Integer isConfirm;
    @SerializedName("is_confirm_cancel")
    @Expose
    private Integer isConfirmCancel;
    @SerializedName("payment_amount")
    @Expose
    private Integer paymentAmount;
    @SerializedName("payment_date")
    @Expose
    private String paymentDate;
    @SerializedName("payment_info")
    @Expose
    private List<PaymentInfo> paymentInfo;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("receipt_code")
    @Expose
    private String receiptCode;
    @SerializedName("receipt_printed")
    @Expose
    private Integer receiptPrinted;
    @SerializedName("receipt_reprint")
    @Expose
    private Integer receiptReprint;
    @SerializedName("update_date")
    @Expose
    private String updateDate;
    @SerializedName("visit_plan_id")
    @Expose
    private Integer visitPlanId;

    @SerializedName("user")
    @Expose
    private User user;

    private final static long serialVersionUID = -3073806224225234542L;

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
     * Gets create by.
     *
     * @return the create by
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * Sets create by.
     *
     * @param createBy the create by
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
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
     * Gets invoice.
     *
     * @return the invoice
     */
    public List<Invoice> getInvoice() {
        return invoice;
    }

    /**
     * Sets invoice.
     *
     * @param invoice the invoice
     */
    public void setInvoice(List<Invoice> invoice) {
        this.invoice = invoice;
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
     * Gets is canceled.
     *
     * @return the is canceled
     */
    public Integer getIsCanceled() {
        return isCanceled;
    }

    /**
     * Sets is canceled.
     *
     * @param isCanceled the is canceled
     */
    public void setIsCanceled(Integer isCanceled) {
        this.isCanceled = isCanceled;
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
     * Gets is confirm cancel.
     *
     * @return the is confirm cancel
     */
    public Integer getIsConfirmCancel() {
        return isConfirmCancel;
    }

    /**
     * Sets is confirm cancel.
     *
     * @param isConfirmCancel the is confirm cancel
     */
    public void setIsConfirmCancel(Integer isConfirmCancel) {
        this.isConfirmCancel = isConfirmCancel;
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
     * Gets payment date.
     *
     * @return the payment date
     */
    public String getPaymentDate() {
        return paymentDate;
    }

    /**
     * Sets payment date.
     *
     * @param paymentDate the payment date
     */
    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    /**
     * Gets payment info.
     *
     * @return the payment info
     */
    public List<PaymentInfo> getPaymentInfo() {
        return paymentInfo;
    }

    /**
     * Sets payment info.
     *
     * @param paymentInfo the payment info
     */
    public void setPaymentInfo(List<PaymentInfo> paymentInfo) {
        this.paymentInfo = paymentInfo;
    }

    /**
     * Gets payment method.
     *
     * @return the payment method
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Sets payment method.
     *
     * @param paymentMethod the payment method
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Gets payment status.
     *
     * @return the payment status
     */
    public String getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * Sets payment status.
     *
     * @param paymentStatus the payment status
     */
    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * Gets receipt code.
     *
     * @return the receipt code
     */
    public String getReceiptCode() {
        return receiptCode;
    }

    /**
     * Sets receipt code.
     *
     * @param receiptCode the receipt code
     */
    public void setReceiptCode(String receiptCode) {
        this.receiptCode = receiptCode;
    }

    /**
     * Gets receipt printed.
     *
     * @return the receipt printed
     */
    public Integer getReceiptPrinted() {
        return receiptPrinted;
    }

    /**
     * Sets receipt printed.
     *
     * @param receiptPrinted the receipt printed
     */
    public void setReceiptPrinted(Integer receiptPrinted) {
        this.receiptPrinted = receiptPrinted;
    }

    /**
     * Gets receipt reprint.
     *
     * @return the receipt reprint
     */
    public Integer getReceiptReprint() {
        return receiptReprint;
    }

    /**
     * Sets receipt reprint.
     *
     * @param receiptReprint the receipt reprint
     */
    public void setReceiptReprint(Integer receiptReprint) {
        this.receiptReprint = receiptReprint;
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
     * Gets visit plan id.
     *
     * @return the visit plan id
     */
    public Integer getVisitPlanId() {
        return visitPlanId;
    }

    /**
     * Sets visit plan id.
     *
     * @param visitPlanId the visit plan id
     */
    public void setVisitPlanId(Integer visitPlanId) {
        this.visitPlanId = visitPlanId;
    }

    /**
     * Get total invoice amount integer.
     *
     * @return the integer
     */
    public Integer getTotalInvoiceAmount(){
        int sum =0;
        for (int i=0; i<getInvoice().size();i++){
            sum+= getInvoice().get(i).getInvoiceAmount();
        }

        return sum;
    }

    /**
     * Get total invoice payment integer.
     *
     * @return the integer
     */
    public Integer getTotalInvoicePayment(){
        int sum =0;
        for (int i=0; i<getInvoice().size();i++){
            sum+= getInvoice().get(i).getPaymentAmount();
        }

        return sum;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("code", code).append("createBy", createBy).append("createDate", createDate).append("customerCode", customerCode).append("id", id).append("invoice", invoice).append("invoiceAmount", invoiceAmount).append("isCanceled", isCanceled).append("isConfirm", isConfirm).append("isConfirmCancel", isConfirmCancel).append("paymentAmount", paymentAmount).append("paymentDate", paymentDate).append("paymentInfo", paymentInfo).append("paymentMethod", paymentMethod).append("paymentStatus", paymentStatus).append("receiptCode", receiptCode).append("receiptPrinted", receiptPrinted).append("receiptReprint", receiptReprint).append("updateDate", updateDate).append("visitPlanId", visitPlanId).toString();
    }
}
