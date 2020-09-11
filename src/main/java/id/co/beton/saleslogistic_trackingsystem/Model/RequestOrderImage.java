package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class RequestOrderImage
 */
public class RequestOrderImage {
    @SerializedName("data")
    @Expose
    private List<RequestOrderImageData> requestOrderImageData;

    @SerializedName("has_next")
    @Expose
    private Boolean hasNext;

    @SerializedName("has_prev")
    @Expose
    private Boolean hasPrev;

    @SerializedName("total")
    @Expose
    private Integer total;

    /**
     * Gets request order image data.
     *
     * @return the request order image data
     */
    public List<RequestOrderImageData> getRequestOrderImageData() {
        return requestOrderImageData;
    }

    /**
     * Sets request order image data.
     *
     * @param requestOrderImageData the request order image data
     */
    public void setRequestOrderImageData(List<RequestOrderImageData> requestOrderImageData) {
        this.requestOrderImageData = requestOrderImageData;
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
}
