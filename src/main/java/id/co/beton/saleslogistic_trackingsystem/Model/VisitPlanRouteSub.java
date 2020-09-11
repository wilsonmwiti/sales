package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Class VisitPlanRouteSub
 */
public class VisitPlanRouteSub {
    @SerializedName("bounds")
    @Expose
    private VisitPlanRouteBound bounds;
    @SerializedName("copyrights")
    @Expose
    private String copyrights;
    @SerializedName("legs")
    @Expose
    private List<VisitPlanRouteLeg> legs = null;
    @SerializedName("overview_polyline")
    @Expose
    private VisitPlanRoutePolyline overviewPolyline;
    @SerializedName("summary")
    @Expose
    private String summary;
    @SerializedName("warnings")
    @Expose
    private List<Object> warnings = null;
    @SerializedName("waypoint_order")
    @Expose
    private List<Integer> waypointOrder = null;
    private final static long serialVersionUID = -9030537958585313216L;

    /**
     * Gets bounds.
     *
     * @return the bounds
     */
    public VisitPlanRouteBound getBounds() {
        return bounds;
    }

    /**
     * Sets bounds.
     *
     * @param bounds the bounds
     */
    public void setBounds(VisitPlanRouteBound bounds) {
        this.bounds = bounds;
    }

    /**
     * Gets copyrights.
     *
     * @return the copyrights
     */
    public String getCopyrights() {
        return copyrights;
    }

    /**
     * Sets copyrights.
     *
     * @param copyrights the copyrights
     */
    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    /**
     * Gets legs.
     *
     * @return the legs
     */
    public List<VisitPlanRouteLeg> getLegs() {
        return legs;
    }

    /**
     * Sets legs.
     *
     * @param legs the legs
     */
    public void setLegs(List<VisitPlanRouteLeg> legs) {
        this.legs = legs;
    }

    /**
     * Gets overview polyline.
     *
     * @return the overview polyline
     */
    public VisitPlanRoutePolyline getOverviewPolyline() {
        return overviewPolyline;
    }

    /**
     * Sets overview polyline.
     *
     * @param overviewPolyline the overview polyline
     */
    public void setOverviewPolyline(VisitPlanRoutePolyline overviewPolyline) {
        this.overviewPolyline = overviewPolyline;
    }

    /**
     * Gets summary.
     *
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Sets summary.
     *
     * @param summary the summary
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Gets warnings.
     *
     * @return the warnings
     */
    public List<Object> getWarnings() {
        return warnings;
    }

    /**
     * Sets warnings.
     *
     * @param warnings the warnings
     */
    public void setWarnings(List<Object> warnings) {
        this.warnings = warnings;
    }

    /**
     * Gets waypoint order.
     *
     * @return the waypoint order
     */
    public List<Integer> getWaypointOrder() {
        return waypointOrder;
    }

    /**
     * Sets waypoint order.
     *
     * @param waypointOrder the waypoint order
     */
    public void setWaypointOrder(List<Integer> waypointOrder) {
        this.waypointOrder = waypointOrder;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("bounds", bounds).append("copyrights", copyrights).append("legs", legs).append("overviewPolyline", overviewPolyline).append("summary", summary).append("warnings", warnings).append("waypointOrder", waypointOrder).toString();
    }
}
