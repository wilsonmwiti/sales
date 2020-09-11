package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.SerializedName;

/**
 * Class Crew
 */
public class Crew {
    @SerializedName("plat_no")
    private String vehicleId;
    private String vehicleType;
    private String kurir;
    private Double latitude;
    private Double longtitude;
    private String currentLocation;
    private String icon;

    /**
     * Set vehicle type.
     *
     * @param val the val
     */
    public void setVehicleType(String val){
        this.vehicleType = val;
    }

    /**
     * Get vehicle type string.
     *
     * @return the string
     */
    public String getVehicleType(){
        return vehicleType;
    }

    /**
     * Set vehicle id.
     *
     * @param val the val
     */
    public void setVehicleId(String val){
        this.vehicleId = val;
    }

    /**
     * Get vehicle id string.
     *
     * @return the string
     */
    public String getVehicleId(){
        return vehicleId;
    }

    /**
     * Set latitude.
     *
     * @param val the val
     */
    public void setLatitude(Double val){
        this.latitude = val;
    }

    /**
     * Get latitude double.
     *
     * @return the double
     */
    public Double getLatitude(){
        return latitude;
    }

    /**
     * Set longtitude.
     *
     * @param val the val
     */
    public void setLongtitude(Double val){
        this.longtitude = val;
    }

    /**
     * Get longtitude double.
     *
     * @return the double
     */
    public Double getLongtitude(){
        return longtitude;
    }

    /**
     * Set icon.
     *
     * @param val the val
     */
    public void setIcon(String val){
        this.icon = val;
    }

    /**
     * Get icon string.
     *
     * @return the string
     */
    public String getIcon(){
        return icon;
    }
}
