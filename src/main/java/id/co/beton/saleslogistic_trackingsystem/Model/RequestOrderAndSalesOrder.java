package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class RequestOrderAndSalesOrder
 */
public class RequestOrderAndSalesOrder {

    @SerializedName("data")
    @Expose
    private List<RequestOrderAndSalesOrderData> requestOrderAndSalesOrderData;

    @SerializedName("has_next")
    @Expose
    private Boolean hasNext;

    @SerializedName("has_prev")
    @Expose
    private Boolean hasPrev;

    @SerializedName("total")
    @Expose
    private Integer total;

    @SerializedName("total_filter")
    @Expose
    private Integer totalFilter;

    /**
     * Gets request order and sales order data.
     *
     * @return the request order and sales order data
     */
    public List<RequestOrderAndSalesOrderData> getRequestOrderAndSalesOrderData() {
        return requestOrderAndSalesOrderData;
    }

    /**
     * Sets request order and sales order data.
     *
     * @param requestOrderAndSalesOrderData the request order and sales order data
     */
    public void setRequestOrderAndSalesOrderData(List<RequestOrderAndSalesOrderData> requestOrderAndSalesOrderData) {
        this.requestOrderAndSalesOrderData = requestOrderAndSalesOrderData;
    }

    /**
     * Gets has next.
     *
     * @return the has next
     */
    public Boolean getHasNext() {
        return hasNext;
    }

    /**
     * Sets has next.
     *
     * @param hasNext the has next
     */
    public void setHasNext(Boolean hasNext) {
        this.hasNext = hasNext;
    }

    /**
     * Gets has prev.
     *
     * @return the has prev
     */
    public Boolean getHasPrev() {
        return hasPrev;
    }

    /**
     * Sets has prev.
     *
     * @param hasPrev the has prev
     */
    public void setHasPrev(Boolean hasPrev) {
        this.hasPrev = hasPrev;
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
     * Gets total filter.
     *
     * @return the total filter
     */
    public Integer getTotalFilter() {
        return totalFilter;
    }

    /**
     * Sets total filter.
     *
     * @param totalFilter the total filter
     */
    public void setTotalFilter(Integer totalFilter) {
        this.totalFilter = totalFilter;
    }

    /**
     * Get total amount integer.
     *
     * @return the integer
     */
    public Integer getTotalAmount(){
        Integer sum=0;
        for (int i=0; i< getRequestOrderAndSalesOrderData().size();i++){
            if(getRequestOrderAndSalesOrderData().get(i).getInvoiceAmount()!=null && getRequestOrderAndSalesOrderData().get(i).getInvoiceAmount()>0){
                sum+= getRequestOrderAndSalesOrderData().get(i).getInvoiceAmount();
            }

        }

        return sum;
    }
}
