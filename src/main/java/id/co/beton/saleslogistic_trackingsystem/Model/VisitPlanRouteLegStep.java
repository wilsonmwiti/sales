package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class VisitPlanRouteLegStep
 */
public class VisitPlanRouteLegStep {
    @SerializedName("distance")
    @Expose
    private VisitPlanRouteTextValue distance;
    @SerializedName("duration")
    @Expose
    private VisitPlanRouteTextValue duration;
    @SerializedName("end_location")
    @Expose
    private VisitPlanRouteBoundLatLong endLocation;
    @SerializedName("html_instructions")
    @Expose
    private String htmlInstructions;
    @SerializedName("polyline")
    @Expose
    private VisitPlanRoutePolyline polyline;
    @SerializedName("start_location")
    @Expose
    private VisitPlanRouteBoundLatLong startLocation;
    @SerializedName("travel_mode")
    @Expose
    private String travelMode;
    @SerializedName("maneuver")
    @Expose
    private String maneuver;
    private final static long serialVersionUID = 4388230275154970857L;

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
     * Gets html instructions.
     *
     * @return the html instructions
     */
    public String getHtmlInstructions() {
        return htmlInstructions;
    }

    /**
     * Sets html instructions.
     *
     * @param htmlInstructions the html instructions
     */
    public void setHtmlInstructions(String htmlInstructions) {
        this.htmlInstructions = htmlInstructions;
    }

    /**
     * Gets polyline.
     *
     * @return the polyline
     */
    public VisitPlanRoutePolyline getPolyline() {
        return polyline;
    }

    /**
     * Sets polyline.
     *
     * @param polyline the polyline
     */
    public void setPolyline(VisitPlanRoutePolyline polyline) {
        this.polyline = polyline;
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
     * Gets travel mode.
     *
     * @return the travel mode
     */
    public String getTravelMode() {
        return travelMode;
    }

    /**
     * Sets travel mode.
     *
     * @param travelMode the travel mode
     */
    public void setTravelMode(String travelMode) {
        this.travelMode = travelMode;
    }

    /**
     * Gets maneuver.
     *
     * @return the maneuver
     */
    public String getManeuver() {
        return maneuver;
    }

    /**
     * Sets maneuver.
     *
     * @param maneuver the maneuver
     */
    public void setManeuver(String maneuver) {
        this.maneuver = maneuver;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("distance", distance).append("duration", duration).append("endLocation", endLocation).append("htmlInstructions", htmlInstructions).append("polyline", polyline).append("startLocation", startLocation).append("travelMode", travelMode).append("maneuver", maneuver).toString();
    }
}
