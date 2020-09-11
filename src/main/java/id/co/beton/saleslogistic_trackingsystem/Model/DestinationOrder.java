package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class DestinationOrder
 */
public class DestinationOrder {
    @SerializedName("lat")
    @Expose
    private double lat;

    @SerializedName("lng")
    @Expose
    private double lng;

    @SerializedName("nfc_code")
    @Expose
    private String nfcCode;

    @SerializedName("order")
    @Expose
    private Integer order;

    /**
     * Gets lat.
     *
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * Sets lat.
     *
     * @param lat the lat
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Gets lng.
     *
     * @return the lng
     */
    public double getLng() {
        return lng;
    }

    /**
     * Sets lng.
     *
     * @param lng the lng
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * Gets nfc code.
     *
     * @return the nfc code
     */
    public String getNfcCode() {
        return nfcCode;
    }

    /**
     * Sets nfc code.
     *
     * @param nfcCode the nfc code
     */
    public void setNfcCode(String nfcCode) {
        this.nfcCode = nfcCode;
    }

    /**
     * Gets order.
     *
     * @return the order
     */
    public Integer getOrder() {
        return order;
    }

    /**
     * Sets order.
     *
     * @param order the order
     */
    public void setOrder(Integer order) {
        this.order = order;
    }
}
