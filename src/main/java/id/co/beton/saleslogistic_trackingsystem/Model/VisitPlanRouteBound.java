package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class VisitPlanRouteBound
 */
public class VisitPlanRouteBound {
    @SerializedName("northeast")
    @Expose
    private VisitPlanRouteBoundLatLong northeast;
    @SerializedName("southwest")
    @Expose
    private VisitPlanRouteBoundLatLong southwest;
    private final static long serialVersionUID = -3501004085010507179L;

    /**
     * Gets northeast.
     *
     * @return the northeast
     */
    public VisitPlanRouteBoundLatLong getNortheast() {
        return northeast;
    }

    /**
     * Sets northeast.
     *
     * @param northeast the northeast
     */
    public void setNortheast(VisitPlanRouteBoundLatLong northeast) {
        this.northeast = northeast;
    }

    /**
     * Gets southwest.
     *
     * @return the southwest
     */
    public VisitPlanRouteBoundLatLong getSouthwest() {
        return southwest;
    }

    /**
     * Sets southwest.
     *
     * @param southwest the southwest
     */
    public void setSouthwest(VisitPlanRouteBoundLatLong southwest) {
        this.southwest = southwest;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("northeast", northeast).append("southwest", southwest).toString();
    }
}
