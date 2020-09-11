package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class VisitPlanRoutePolyline
 */
public class VisitPlanRoutePolyline {
    @SerializedName("points")
    @Expose
    private String points;
    private final static long serialVersionUID = 2493416303651228789L;

    /**
     * Gets points.
     *
     * @return the points
     */
    public String getPoints() {
        return points;
    }

    /**
     * Sets points.
     *
     * @param points the points
     */
    public void setPoints(String points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("points", points).toString();
    }
}
