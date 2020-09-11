package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DataPlan implements Serializable {
    @SerializedName("total")
    private int total;

    @SerializedName("data")
    private List<Plan> data;

    @SerializedName("total_filter")
    private int totalFilter;

    @SerializedName("has_next")
    private boolean hasNext;

    @SerializedName("has_prev")
    private boolean hasPrev;

    /**
     * Set total.
     *
     * @param total the total
     */
    public void setTotal(int total){
        this.total = total;
    }

    /**
     * Get total int.
     *
     * @return the int
     */
    public int getTotal(){
        return total;
    }

    /**
     * Set data.
     *
     * @param data the data
     */
    public void setData(List<Plan> data){
        this.data = data;
    }

    /**
     * Get data list.
     *
     * @return the list
     */
    public List<Plan> getData(){
        return data;
    }

    /**
     * Set total filter.
     *
     * @param totalFilter the total filter
     */
    public void setTotalFilter(int totalFilter){
        this.totalFilter = totalFilter;
    }

    /**
     * Get total filter int.
     *
     * @return the int
     */
    public int getTotalFilter(){
        return totalFilter;
    }

    /**
     * Set has next.
     *
     * @param hasNext the has next
     */
    public void setHasNext(boolean hasNext){
        this.hasNext = hasNext;
    }

    /**
     * Is has next boolean.
     *
     * @return the boolean
     */
    public boolean isHasNext(){
        return hasNext;
    }

    /**
     * Set has prev.
     *
     * @param hasPrev the has prev
     */
    public void setHasPrev(boolean hasPrev){
        this.hasPrev = hasPrev;
    }

    /**
     * Is has prev boolean.
     *
     * @return the boolean
     */
    public boolean isHasPrev(){
        return hasPrev;
    }
}
