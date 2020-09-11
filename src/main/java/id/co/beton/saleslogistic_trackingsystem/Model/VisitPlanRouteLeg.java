package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Class VisitPlanRouteLeg
 */
public class VisitPlanRouteLeg {
    @SerializedName("distance")
    @Expose
    private VisitPlanRouteTextValue distance;
    @SerializedName("duration")
    @Expose
    private VisitPlanRouteTextValue duration;
    @SerializedName("end_address")
    @Expose
    private String endAddress;
    @SerializedName("end_location")
    @Expose
    private VisitPlanRouteBoundLatLong endLocation;
    @SerializedName("start_address")
    @Expose
    private String startAddress;
    @SerializedName("start_location")
    @Expose
    private VisitPlanRouteBoundLatLong startLocation;
    @SerializedName("steps")
    @Expose
    private List<VisitPlanRouteLegStep> steps = null;
    @SerializedName("traffic_speed_entry")
    @Expose
    private List<Object> trafficSpeedEntry = null;
    @SerializedName("via_waypoint")
    @Expose
    private List<Object> viaWaypoint = null;
    private final static long serialVersionUID = 3979311239508826509L;

    /**
     * Gets distance.
     *
     * @return the distance
     */
    public VisitPlanRouteTextValue getDistance() {
        return distance;
    }

    /**
     * Sets distance.
     *
     * @param distance the distance
     */
    public void setDistance(VisitPlanRouteTextValue distance) {
        this.distance = distance;
    }

    /**
     * Gets duration.
     *
     * @return the duration
     */
    public VisitPlanRouteTextValue getDuration() {
        return duration;
    }

    /**
     * Sets duration.
     *
     * @param duration the duration
     */
    public void setDuration(VisitPlanRouteTextValue duration) {
        this.duration = duration;
    }

    /**
     * Gets end address.
     *
     * @return the end address
     */
    public String getEndAddress() {
        return endAddress;
    }

    /**
     * Sets end address.
     *
     * @param endAddress the end address
     */
    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    /**
     * Gets end location.
     *
     * @return the end location
     */
    public VisitPlanRouteBoundLatLong getEndLocation() {
        return endLocation;
    }

    /**
     * Sets end location.
     *
     * @param endLocation the end location
     */
    public void setEndLocation(VisitPlanRouteBoundLatLong endLocation) {
        this.endLocation = endLocation;
    }

    /**
     * Gets start address.
     *
     * @return the start address
     */
    public String getStartAddress() {
        return startAddress;
    }

    /**
     * Sets start address.
     *
     * @param startAddress the start address
     */
    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    /**
     * Gets start location.
     *
     * @return the start location
     */
    public VisitPlanRouteBoundLatLong getStartLocation() {
        return startLocation;
    }

    /**
     * Sets start location.
     *
     * @param startLocation the start location
     */
    public void setStartLocation(VisitPlanRouteBoundLatLong startLocation) {
        this.startLocation = startLocation;
    }

    /**
     * Gets steps.
     *
     * @return the steps
     */
    public List<VisitPlanRouteLegStep> getSteps() {
        return steps;
    }

    /**
     * Sets steps.
     *
     * @param steps the steps
     */
    public void setSteps(List<VisitPlanRouteLegStep> steps) {
        this.steps = steps;
    }

    /**
     * Gets traffic speed entry.
     *
     * @return the traffic speed entry
     */
    public List<Object> getTrafficSpeedEntry() {
        return trafficSpeedEntry;
    }

    /**
     * Sets traffic speed entry.
     *
     * @param trafficSpeedEntry the traffic speed entry
     */
    public void setTrafficSpeedEntry(List<Object> trafficSpeedEntry) {
        this.trafficSpeedEntry = trafficSpeedEntry;
    }

    /**
     * Gets via waypoint.
     *
     * @return the via waypoint
     */
    public List<Object> getViaWaypoint() {
        return viaWaypoint;
    }

    /**
     * Sets via waypoint.
     *
     * @param viaWaypoint the via waypoint
     */
    public void setViaWaypoint(List<Object> viaWaypoint) {
        this.viaWaypoint = viaWaypoint;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("distance", distance).append("duration", duration).append("endAddress", endAddress).append("endLocation", endLocation).append("startAddress", startAddress).append("startLocation", startLocation).append("steps", steps).append("trafficSpeedEntry", trafficSpeedEntry).append("viaWaypoint", viaWaypoint).toString();
    }
}
