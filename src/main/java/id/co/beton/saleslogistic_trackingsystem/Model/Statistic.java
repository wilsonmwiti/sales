package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class Statistic
 */
public class Statistic implements Serializable {

    @SerializedName("alert")
    @Expose
    private Integer alert;
    @SerializedName("break_time")
    @Expose
    private Integer breakTime;
    @SerializedName("cancel")
    @Expose
    private Integer cancel;
    @SerializedName("driving_time")
    @Expose
    private Integer drivingTime;
    @SerializedName("invoice")
    @Expose
    private Integer invoice;
    @SerializedName("invoice_amount")
    @Expose
    private Integer invoiceAmount;
    @SerializedName("payment")
    @Expose
    private Integer payment;
    @SerializedName("payment_amount")
    @Expose
    private Integer paymentAmount;
    @SerializedName("payment_cancel")
    @Expose
    private Integer paymentCancel;
    @SerializedName("permission")
    @Expose
    private Integer permission;
    @SerializedName("plan")
    @Expose
    private Integer plan;
    @SerializedName("report_nfc")
    @Expose
    private Integer reportNfc;
    @SerializedName("report_location")
    @Expose
    private Integer reportLocation;
    @SerializedName("report_print")
    @Expose
    private Integer reportPrint;
    @SerializedName("reprint")
    @Expose
    private Integer reprint;
    @SerializedName("request_order")
    @Expose
    private Integer requestOrder;
    @SerializedName("request_order_special")
    @Expose
    private Integer requestOrderSpecial;
    @SerializedName("sales_order")
    @Expose
    private Integer salesOrder;
    @SerializedName("sales_order_amount")
    @Expose
    private Integer salesOrderAmount;
    @SerializedName("visit_time")
    @Expose
    private Integer visitTime;
    @SerializedName("visited")
    @Expose
    private Integer visited;
    @SerializedName("packing_slip")
    @Expose
    private Integer packingSlip;
    @SerializedName("packing_slip_accept")
    @Expose
    private Integer packingSlipAccept;
    @SerializedName("packing_slip_cancel")
    @Expose
    private Integer packingSlipCancel;

    @SerializedName("payment_amount_wo_confirm")
    @Expose
    private Integer paymentAmountWoConfirm;

    @SerializedName("payment_wo_confirm")
    @Expose
    private Integer paymentWoConfirm;

    /**
     * Gets alert.
     *
     * @return the alert
     */
    public Integer getAlert() {
        return alert;
    }

    /**
     * Sets alert.
     *
     * @param alert the alert
     */
    public void setAlert(Integer alert) {
        this.alert = alert;
    }

    /**
     * Gets break time.
     *
     * @return the break time
     */
    public Integer getBreakTime() {
        return breakTime;
    }

    /**
     * Sets break time.
     *
     * @param breakTime the break time
     */
    public void setBreakTime(Integer breakTime) {
        this.breakTime = breakTime;
    }

    /**
     * Gets cancel.
     *
     * @return the cancel
     */
    public Integer getCancel() {
        return cancel;
    }

    /**
     * Sets cancel.
     *
     * @param cancel the cancel
     */
    public void setCancel(Integer cancel) {
        this.cancel = cancel;
    }

    /**
     * Gets driving time.
     *
     * @return the driving time
     */
    public Integer getDrivingTime() {
        return drivingTime;
    }

    /**
     * Sets driving time.
     *
     * @param drivingTime the driving time
     */
    public void setDrivingTime(Integer drivingTime) {
        this.drivingTime = drivingTime;
    }

    /**
     * Gets invoice.
     *
     * @return the invoice
     */
    public Integer getInvoice() {
        return invoice;
    }

    /**
     * Sets invoice.
     *
     * @param invoice the invoice
     */
    public void setInvoice(Integer invoice) {
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
     * Gets payment.
     *
     * @return the payment
     */
    public Integer getPayment() {
        return payment;
    }

    /**
     * Sets payment.
     *
     * @param payment the payment
     */
    public void setPayment(Integer payment) {
        this.payment = payment;
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
     * Gets payment cancel.
     *
     * @return the payment cancel
     */
    public Integer getPaymentCancel() {
        return paymentCancel;
    }

    /**
     * Sets payment cancel.
     *
     * @param paymentCancel the payment cancel
     */
    public void setPaymentCancel(Integer paymentCancel) {
        this.paymentCancel = paymentCancel;
    }

    /**
     * Gets permission.
     *
     * @return the permission
     */
    public Integer getPermission() {
        return permission;
    }

    /**
     * Sets permission.
     *
     * @param permission the permission
     */
    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    /**
     * Gets plan.
     *
     * @return the plan
     */
    public Integer getPlan() {
        return plan;
    }

    /**
     * Sets plan.
     *
     * @param plan the plan
     */
    public void setPlan(Integer plan) {
        this.plan = plan;
    }

    /**
     * Gets report location.
     *
     * @return the report location
     */
    public Integer getReportLocation() {
        return reportLocation;
    }

    /**
     * Sets report location.
     *
     * @param reportLocation the report location
     */
    public void setReportLocation(Integer reportLocation) {
        this.reportLocation = reportLocation;
    }

    /**
     * Gets report nfc.
     *
     * @return the report nfc
     */
    public Integer getReportNfc() {
        return reportNfc;
    }

    /**
     * Sets report nfc.
     *
     * @param reportNfc the report nfc
     */
    public void setReportNfc(Integer reportNfc) {
        this.reportNfc = reportNfc;
    }

    /**
     * Gets report print.
     *
     * @return the report print
     */
    public Integer getReportPrint() {
        return reportPrint;
    }

    /**
     * Sets report print.
     *
     * @param reportPrint the report print
     */
    public void setReportPrint(Integer reportPrint) {
        this.reportPrint = reportPrint;
    }

    /**
     * Gets reprint.
     *
     * @return the reprint
     */
    public Integer getReprint() {
        return reprint;
    }

    /**
     * Sets reprint.
     *
     * @param reprint the reprint
     */
    public void setReprint(Integer reprint) {
        this.reprint = reprint;
    }

    /**
     * Gets request order.
     *
     * @return the request order
     */
    public Integer getRequestOrder() {
        return requestOrder;
    }

    /**
     * Sets request order.
     *
     * @param requestOrder the request order
     */
    public void setRequestOrder(Integer requestOrder) {
        this.requestOrder = requestOrder;
    }

    /**
     * Gets request order special.
     *
     * @return the request order special
     */
    public Integer getRequestOrderSpecial() {
        return requestOrderSpecial;
    }

    /**
     * Sets request order special.
     *
     * @param requestOrderSpecial the request order special
     */
    public void setRequestOrderSpecial(Integer requestOrderSpecial) {
        this.requestOrderSpecial = requestOrderSpecial;
    }

    /**
     * Gets sales order.
     *
     * @return the sales order
     */
    public Integer getSalesOrder() {
        return salesOrder;
    }

    /**
     * Sets sales order.
     *
     * @param salesOrder the sales order
     */
    public void setSalesOrder(Integer salesOrder) {
        this.salesOrder = salesOrder;
    }

    /**
     * Gets sales order amount.
     *
     * @return the sales order amount
     */
    public Integer getSalesOrderAmount() {
        return salesOrderAmount;
    }

    /**
     * Sets sales order amount.
     *
     * @param salesOrderAmount the sales order amount
     */
    public void setSalesOrderAmount(Integer salesOrderAmount) {
        this.salesOrderAmount = salesOrderAmount;
    }

    /**
     * Gets visit time.
     *
     * @return the visit time
     */
    public Integer getVisitTime() {
        return visitTime;
    }

    /**
     * Sets visit time.
     *
     * @param visitTime the visit time
     */
    public void setVisitTime(Integer visitTime) {
        this.visitTime = visitTime;
    }

    /**
     * Gets visited.
     *
     * @return the visited
     */
    public Integer getVisited() {
        return visited;
    }

    /**
     * Sets visited.
     *
     * @param visited the visited
     */
    public void setVisited(Integer visited) {
        this.visited = visited;
    }

    /**
     * Gets packing slip.
     *
     * @return the packing slip
     */
    public Integer getPackingSlip() {
        return packingSlip;
    }

    /**
     * Sets packing slip.
     *
     * @param packingSlip the packing slip
     */
    public void setPackingSlip(Integer packingSlip) {
        this.packingSlip = packingSlip;
    }

    /**
     * Gets packing slip accept.
     *
     * @return the packing slip accept
     */
    public Integer getPackingSlipAccept() {
        return packingSlipAccept;
    }

    /**
     * Sets packing slip accept.
     *
     * @param packingSlipAccept the packing slip accept
     */
    public void setPackingSlipAccept(Integer packingSlipAccept) {
        this.packingSlipAccept = packingSlipAccept;
    }

    /**
     * Gets packing slip cancel.
     *
     * @return the packing slip cancel
     */
    public Integer getPackingSlipCancel() {
        return packingSlipCancel;
    }

    /**
     * Sets packing slip cancel.
     *
     * @param packingSlipCancel the packing slip cancel
     */
    public void setPackingSlipCancel(Integer packingSlipCancel) {
        this.packingSlipCancel = packingSlipCancel;
    }

    /**
     * Gets payment amount wo confirm.
     *
     * @return the payment amount wo confirm
     */
    public Integer getPaymentAmountWoConfirm() {
        return paymentAmountWoConfirm;
    }

    /**
     * Sets payment amount wo confirm.
     *
     * @param paymentAmountWoConfirm the payment amount wo confirm
     */
    public void setPaymentAmountWoConfirm(Integer paymentAmountWoConfirm) {
        this.paymentAmountWoConfirm = paymentAmountWoConfirm;
    }

    /**
     * Gets payment wo confirm.
     *
     * @return the payment wo confirm
     */
    public Integer getPaymentWoConfirm() {
        return paymentWoConfirm;
    }

    /**
     * Sets payment wo confirm.
     *
     * @param paymentWoConfirm the payment wo confirm
     */
    public void setPaymentWoConfirm(Integer paymentWoConfirm) {
        this.paymentWoConfirm = paymentWoConfirm;
    }
}
