package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Class VisitPlanRoute
 */
public class VisitPlanRoute {
    @SerializedName("customer_code")
    @Expose
    private String customerCode="";

    @SerializedName("geocoded_waypoints")
    @Expose
    private List<VisitPlanRouteGeocodedWaypoint> geocodedWaypoints = null;

    @SerializedName("routes")
    @Expose
    private List<VisitPlanRouteSub> routes = null;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("error_message")
    @Expose
    private String errorMessage;

    private final static long serialVersionUID = -3370664093249884490L;

    /**
     * Get customer code string.
     *
     * @return the string
     */
    public String getCustomerCode(){
        return customerCode;
    }

    /**
     * Set customer code.
     *
     * @param val the val
     */
    public void setCustomerCode(String val){
        this.customerCode = val;
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
     * Sets geocoded waypoints.
     *
     * @param geocodedWaypoints the geocoded waypoints
     */
    public void setGeocodedWaypoints(List<VisitPlanRouteGeocodedWaypoint> geocodedWaypoints) {
        this.geocodedWaypoints = geocodedWaypoints;
    }

    /**
     * Gets routes.
     *
     * @return the routes
     */
    public List<VisitPlanRouteSub> getRoutes() {
        return routes;
    }

    /**
     * Sets routes.
     *
     * @param routes the routes
     */
    public void setRoutes(List<VisitPlanRouteSub> routes) {
        this.routes = routes;
    }

    /**
     * Gets status.
     *
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param status the status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Gets error message.
     *
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Sets error message.
     *
     * @param errorMessage the error message
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("geocodedWaypoints", geocodedWaypoints).append("routes", routes).append("status", status).toString();
    }
}
