package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class DataSalesOrder
 */
public class DataSalesOrder {
    /**
     * The Total.
     */
    @SerializedName("total")
    public int total;

    /**
     * The Data.
     */
    @SerializedName("data")
    public List<SalesOrder> data;

    /**
     * The Total filter.
     */
    @SerializedName("total_filter")
    public int totalFilter;

    /**
     * The Has next.
     */
    @SerializedName("has_next")
    public boolean hasNext;

    /**
     * The Has prev.
     */
    @SerializedName("has_prev")
    public boolean hasPrev;

    /**
     * Gets total.
     *
     * @return the total
     */
    public int getTotal() {
        return total;
    }

    /**
     * Sets total.
     *
     * @param total the total
     */
    public void setTotal(int total) {
        this.total = total;
    }

    /**
     * Gets data.
     *
     * @return the data
     */
    public List<SalesOrder> getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(List<SalesOrder> data) {
        this.data = data;
    }

    /**
     * Gets total filter.
     *
     * @return the total filter
     */
    public int getTotalFilter() {
        return totalFilter;
    }

    /**
     * Sets total filter.
     *
     * @param totalFilter the total filter
     */
    public void setTotalFilter(int totalFilter) {
        this.totalFilter = totalFilter;
    }

    /**
     * Is has next boolean.
     *
     * @return the boolean
     */
    public boolean isHasNext() {
        return hasNext;
    }

    /**
     * Sets has next.
     *
     * @param hasNext the has next
     */
    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    /**
     * Is has prev boolean.
     *
     * @return the boolean
     */
    public boolean isHasPrev() {
        return hasPrev;
    }

    /**
     * Sets has prev.
     *
     * @param hasPrev the has prev
     */
    public void setHasPrev(boolean hasPrev) {
        this.hasPrev = hasPrev;
    }
}
