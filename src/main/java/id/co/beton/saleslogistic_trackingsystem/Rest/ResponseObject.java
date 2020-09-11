package id.co.beton.saleslogistic_trackingsystem.Rest;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;


/**
 * Class ResponseObject
 * get response object from server
 */
public class ResponseObject {

    @SerializedName("message")
    private String message;

    @SerializedName("error")
    private int error;

    @SerializedName("data")
    private JsonObject data;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }
}
