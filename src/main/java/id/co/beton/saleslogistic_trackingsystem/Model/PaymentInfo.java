package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Class PaymentInfo
 */
public class PaymentInfo implements Serializable {

    @SerializedName("account_name")
    @Expose
    private String accountName;
    @SerializedName("account_no")
    @Expose
    private String accountNo;
    @SerializedName("bank_name")
    @Expose
    private String bankName;
    @SerializedName("transfer_to")
    @Expose
    private String transferTo;

    @SerializedName("due_date")
    @Expose
    private String dueDate;

    @SerializedName("payment_method")
    @Expose
    private String type;

    @SerializedName("total_payment")
    @Expose
    private Integer total;

    @SerializedName("file")
    @Expose(serialize = false, deserialize = false)
    private List<String> file = null;

    // @SerializedName("file")
    // @Expose(serialize = false)
    // private String file=null;

    private List<String> listPhotoGiro;
    private List<String> listPhotoCheque;
    private List<String> listPhotoTransfer;
    private List<String> listPhotoKontraBon;

    /**
     * Set account name.
     *
     * @param accountName the account name
     */
    public void setAccountName(String accountName){
        this.accountName = accountName;
    }

    /**
     * Get account name string.
     *
     * @return the string
     */
    public String getAccountName(){
        return accountName;
    }

    /**
     * Set bank name.
     *
     * @param bankName the bank name
     */
    public void setBankName(String bankName){
        this.bankName = bankName;
    }

    /**
     * Get bank name string.
     *
     * @return the string
     */
    public String getBankName(){
        return bankName;
    }

    /**
     * Set transfer to.
     *
     * @param transferTo the transfer to
     */
    public void setTransferTo(String transferTo){
        this.transferTo = transferTo;
    }

    /**
     * Get transfer to string.
     *
     * @return the string
     */
    public String getTransferTo(){
        return transferTo;
    }

    /**
     * Set account no.
     *
     * @param accountNo the account no
     */
    public void setAccountNo(String accountNo){
        this.accountNo = accountNo;
    }

    /**
     * Get account no string.
     *
     * @return the string
     */
    public String getAccountNo(){
        return accountNo;
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
     * Gets total.
     *
     * @return the total
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * Sets total.
     *
     * @param total the total
     */
    public void setTotal(Integer total) {
        this.total = total;
    }

    /**
     * Gets due date.
     *
     * @return the due date
     */
    public String getDueDate() {
        return dueDate;
    }

    /**
     * Sets due date.
     *
     * @param dueDate the due date
     */
    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * Get list photo giro list.
     *
     * @return the list
     */
    public List<String> getListPhotoGiro(){return listPhotoGiro; }

    /**
     * Set list photo giro.
     *
     * @param listPhotoGiro the list photo giro
     */
    public void setListPhotoGiro(List<String> listPhotoGiro){ this.listPhotoGiro = listPhotoGiro;}

    /**
     * Get list photo cheque list.
     *
     * @return the list
     */
    public List<String> getListPhotoCheque(){return listPhotoCheque; }

    /**
     * Set list photo cheque.
     *
     * @param listPhotoCheque the list photo cheque
     */
    public void setListPhotoCheque(List<String> listPhotoCheque){ this.listPhotoCheque= listPhotoCheque;}

    /**
     * Get list photo transfer list.
     *
     * @return the list
     */
    public List<String> getListPhotoTransfer(){return listPhotoTransfer; }

    /**
     * Set list photo transfer.
     *
     * @param listPhotoTransfer the list photo transfer
     */
    public void setListPhotoTransfer(List<String> listPhotoTransfer){ this.listPhotoTransfer= listPhotoTransfer;}

    /**
     * Gets list photo kontra bon.
     *
     * @return the list photo kontra bon
     */
    public List<String> getListPhotoKontraBon() {
        return listPhotoKontraBon;
    }

    /**
     * Sets list photo kontra bon.
     *
     * @param listPhotoKontraBon the list photo kontra bon
     */
    public void setListPhotoKontraBon(List<String> listPhotoKontraBon) { this.listPhotoKontraBon = listPhotoKontraBon; }

    // public String getFile() {
    //     return file;
    // }
    // public void setFile(String file) {
    //     this.file = file;
    // }

    /**
     * Gets file.
     *
     * @return the file
     */
    public List<String> getFile() {return file;}

    /**
     * Set file.
     *
     * @param file the file
     */
    public void setFile(List<String> file){ this.file = file;}
}
