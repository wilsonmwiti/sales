package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Map;

/**
 * Class StatisticUser
 */
public class StatisticUser implements Serializable {

    @SerializedName("data")
    @Expose
    private Map<String, Statistic> data;

    @SerializedName("error")
    private int error;

    @SerializedName("message")
    private String message;


    /**
     * Gets data.
     *
     * @return the data
     */
    public Map<String, Statistic> getData() {
        return data;
    }

    /**
     * Sets data.
     *
     * @param data the data
     */
    public void setData(Map<String, Statistic> data) {
        this.data = data;
    }

    /**
     * Set error.
     *
     * @param error the error
     */
    public void setError(int error){
        this.error = error;
    }

    /**
     * Get error int.
     *
     * @return the int
     */
    public int getError(){
        return error;
    }

    /**
     * Set message.
     *
     * @param message the message
     */
    public void setMessage(String message){
        this.message = message;
    }

    /**
     * Get message string.
     *
     * @return the string
     */
    public String getMessage(){
        return message;
    }
}
