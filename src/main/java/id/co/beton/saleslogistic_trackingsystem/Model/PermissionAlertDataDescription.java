package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Class PermissionAlertDataDescription
 */
public class PermissionAlertDataDescription {

    @SerializedName("time")
    @Expose
    private Integer time;
    private final static long serialVersionUID = -3046775894392882416L;

    @SerializedName("customer_code")
    @Expose
    private String customerCode=null;

    @SerializedName("geocoded_waypoints")
    @Expose
    private List<VisitPlanRouteGeocodedWaypoint> geocodedWaypoints = null;

    @SerializedName("routes")
    @Expose
    private List<VisitPlanRouteSub> routes = null;

    /**
     * Gets time.
     *
     * @return the time
     */
    public Integer getTime() {
        return time;
    }

    /**
     * Sets time.
     *
     * @param time the time
     */
    public void setTime(Integer time) {
        this.time = time;
    }

    /**
     * Gets customer code.
     *
     * @return the customer code
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * Gets geocoded waypoints.
     *
     * @return the geocoded waypoints
     */
    public List<VisitPlanRouteGeocodedWaypoint> getGeocodedWaypoints() {
        return geocodedWaypoints;
    }

    /**
     * Gets routes.
     *
     * @return the routes
     */
    public List<VisitPlanRouteSub> getRoutes() {
        return routes;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("time", time).toString();
    }

    /**
     * Sets customer code.
     *
     * @param customerCode the customer code
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }
}
