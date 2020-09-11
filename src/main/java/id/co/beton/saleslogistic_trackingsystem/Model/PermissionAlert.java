package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Class PermissionAlert
 */
public class PermissionAlert {
    @SerializedName("data")
    @Expose
    private List<PermissionAlertData> data = null;
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
    private final static long serialVersionUID = -2440893038904380529L;

    /**
     * Gets data.
     *
     * @return the data
     */
    public List<PermissionAlertData> getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(List<PermissionAlertData> data) {
        this.data = data;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("data", data).append("hasNext", hasNext).append("hasPrev", hasPrev).append("total", total).append("totalFilter", totalFilter).toString();
    }
}
