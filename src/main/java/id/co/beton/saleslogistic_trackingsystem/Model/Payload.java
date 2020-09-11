package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class Payload
 */
public class Payload implements Serializable {

    @SerializedName("time")
    private int time;

    /**
     * Set time.
     *
     * @param time the time
     */
    public void setTime(int time){
        this.time = time;
    }

    /**
     * Get time int.
     *
     * @return the int
     */
    public int getTime(){
        return time;
    }
}
