package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class Setting
 */
public class Setting {
    @SerializedName("max_breaktime_time")
    @Expose
    private Integer maxBreakTime;

    @SerializedName("max_idle_time")
    @Expose
    private Integer maxIdleTime;

    @SerializedName("max_length_unloading")
    @Expose
    private Integer maxLengthUnloading;

    @SerializedName("max_length_visit_time")
    @Expose
    private Integer maxLengthVisitTime;

    @SerializedName("alert_wrong_route")
    @Expose
    private Integer alertWrongRoute;

    @SerializedName("alert_break_time")
    @Expose
    private Integer alertBreakTime;

    @SerializedName("logo_image")
    @Expose
    private String logoImage;

    @SerializedName("blacklist_apps")
    @Expose
    private String blacklistApps;

    /**
     * Get blacklist apps string.
     *
     * @return the string
     */
    public String getBlacklistApps(){ return blacklistApps; }

    /**
     * Set blacklist apps.
     *
     * @param blacklistApps the blacklist apps
     */
    public void setBlacklistApps(String blacklistApps){ this.blacklistApps = blacklistApps; }

    /**
     * Get logo image string.
     *
     * @return the string
     */
    public String getLogoImage(){ return logoImage; }

    /**
     * Set logo image.
     *
     * @param logoImage the logo image
     */
    public void setLogoImage(String logoImage){ this.logoImage = logoImage; }

    /**
     * Gets max break time.
     *
     * @return the max break time
     */
    public Integer getMaxBreakTime() {
        //return 10;
        return maxBreakTime*60; //convert to detik
    }

    /**
     * Sets max break time.
     *
     * @param maxBreakTime the max break time
     */
    public void setMaxBreakTime(Integer maxBreakTime) {
        this.maxBreakTime = maxBreakTime;
    }

    /**
     * Gets max length unloading.
     *
     * @return the max length unloading
     */
    public Integer getMaxLengthUnloading() {
        return maxLengthUnloading*60;
    }

    /**
     * Sets max length unloading.
     *
     * @param maxLengthUnloading the max length unloading
     */
    public void setMaxLengthUnloading(Integer maxLengthUnloading) {
        this.maxLengthUnloading = maxLengthUnloading;
    }

    /**
     * Gets max length visit time.
     *
     * @return the max length visit time
     */
    public Integer getMaxLengthVisitTime() {
        return maxLengthVisitTime*60;
    }

    /**
     * Sets max length visit time.
     *
     * @param maxLengthVisitTime the max length visit time
     */
    public void setMaxLengthVisitTime(Integer maxLengthVisitTime) {
        this.maxLengthVisitTime = maxLengthVisitTime;
    }

    /**
     * Gets max idle time.
     *
     * @return the max idle time
     */
    public Integer getMaxIdleTime() {
        return maxIdleTime*60;
    }

    /**
     * Sets max idle time.
     *
     * @param maxIdleTime the max idle time
     */
    public void setMaxIdleTime(Integer maxIdleTime) {
        this.maxIdleTime = maxIdleTime;
    }

    /**
     * Get alert wrong route integer.
     *
     * @return the integer
     */
    public Integer getAlertWrongRoute(){return alertWrongRoute;}

    /**
     * Set alert wrong route.
     *
     * @param alertWrongRoute the alert wrong route
     */
    public void setAlertWrongRoute(Integer alertWrongRoute){ this.alertWrongRoute = alertWrongRoute; }

    /**
     * Get alert break time integer.
     *
     * @return the integer
     */
    public Integer getAlertBreakTime(){return alertBreakTime;}

    /**
     * Set alert break time.
     *
     * @param alertBreakTime the alert break time
     */
    public void setAlertBreakTime(Integer alertBreakTime){ this.alertBreakTime = alertBreakTime; }
}
