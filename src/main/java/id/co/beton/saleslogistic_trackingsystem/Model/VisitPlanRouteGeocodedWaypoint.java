package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Class VisitPlanRouteGeocodedWaypoint
 */
public class VisitPlanRouteGeocodedWaypoint {
    @SerializedName("geocoder_status")
    @Expose
    private String geocoderStatus;
    @SerializedName("place_id")
    @Expose
    private String placeId;
    @SerializedName("types")
    @Expose
    private List<String> types = null;
    private final static long serialVersionUID = 7059256344606202224L;

    /**
     * Gets geocoder status.
     *
     * @return the geocoder status
     */
    public String getGeocoderStatus() {
        return geocoderStatus;
    }

    /**
     * Sets geocoder status.
     *
     * @param geocoderStatus the geocoder status
     */
    public void setGeocoderStatus(String geocoderStatus) {
        this.geocoderStatus = geocoderStatus;
    }

    /**
     * Gets place id.
     *
     * @return the place id
     */
    public String getPlaceId() {
        return placeId;
    }

    /**
     * Sets place id.
     *
     * @param placeId the place id
     */
    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    /**
     * Gets types.
     *
     * @return the types
     */
    public List<String> getTypes() {
        return types;
    }

    /**
     * Sets types.
     *
     * @param types the types
     */
    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("geocoderStatus", geocoderStatus).append("placeId", placeId).append("types", types).toString();
    }
}
