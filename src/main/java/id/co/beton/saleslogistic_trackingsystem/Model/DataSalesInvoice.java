package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class DataSalesInvoice
 */
public class DataSalesInvoice implements Serializable {

    @SerializedName("total_amount")
    private TotalAmount totalAmount;

    @SerializedName("total_invoice_payment")
    private TotalInvoicePayment totalInvoicePayment;

    /**
     * Set total amount.
     *
     * @param totalAmount the total amount
     */
    public void setTotalAmount(TotalAmount totalAmount){
        this.totalAmount = totalAmount;
    }

    /**
     * Get total amount total amount.
     *
     * @return the total amount
     */
    public TotalAmount getTotalAmount(){
        return totalAmount;
    }


    /**
     * Set total invoice payment.
     *
     * @param totalInvoicePayment the total invoice payment
     */
    public void setTotalInvoicePayment(TotalInvoicePayment totalInvoicePayment){
        this.totalInvoicePayment = totalInvoicePayment;
    }

    /**
     * Get total invoice payment total invoice payment.
     *
     * @return the total invoice payment
     */
    public TotalInvoicePayment getTotalInvoicePayment(){
        return totalInvoicePayment;
    }
}
