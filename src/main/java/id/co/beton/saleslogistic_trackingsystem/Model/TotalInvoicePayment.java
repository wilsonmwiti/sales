package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class TotalInvoicePayment
 */
public class TotalInvoicePayment implements Serializable {

    @SerializedName("payment")
    private int payment;

    @SerializedName("invoice")
    private int invoice;

    /**
     * Set payment.
     *
     * @param payment the payment
     */
    public void setPayment(int payment){
        this.payment = payment;
    }

    /**
     * Get payment int.
     *
     * @return the int
     */
    public int getPayment(){
        return payment;
    }

    /**
     * Set invoice.
     *
     * @param invoice the invoice
     */
    public void setInvoice(int invoice){
        this.invoice = invoice;
    }

    /**
     * Get invoice int.
     *
     * @return the int
     */
    public int getInvoice(){
        return invoice;
    }
}

