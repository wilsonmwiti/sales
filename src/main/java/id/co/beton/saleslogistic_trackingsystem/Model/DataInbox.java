package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class DataInbox
 */
public class DataInbox implements Serializable {

    @SerializedName("date")
    private String date;

    @SerializedName("payload")
    private Payload payload;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("id")
    private int id;

    @SerializedName("category")
    private String category;

    @SerializedName("create_date")
    private String createDate;

    @SerializedName("message")
    private String message;

    @SerializedName("title")
    private String title;

    @SerializedName("update_date")
    private String updateDate;

    /**
     * Set date.
     *
     * @param date the date
     */
    public void setDate(String date){
        this.date = date;
    }

    /**
     * Get date string.
     *
     * @return the string
     */
    public String getDate(){
        return date;
    }

    /**
     * Set payload.
     *
     * @param payload the payload
     */
    public void setPayload(Payload payload){
        this.payload = payload;
    }

    /**
     * Get payload payload.
     *
     * @return the payload
     */
    public Payload getPayload(){
        return payload;
    }

    /**
     * Set user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId){
        this.userId = userId;
    }

    /**
     * Get user id int.
     *
     * @return the int
     */
    public int getUserId(){
        return userId;
    }

    /**
     * Set id.
     *
     * @param id the id
     */
    public void setId(int id){
        this.id = id;
    }

    /**
     * Get id int.
     *
     * @return the int
     */
    public int getId(){
        return id;
    }

    /**
     * Set category.
     *
     * @param category the category
     */
    public void setCategory(String category){
        this.category = category;
    }

    /**
     * Get category string.
     *
     * @return the string
     */
    public String getCategory(){
        return category;
    }

    /**
     * Set create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(String createDate){
        this.createDate = createDate;
    }

    /**
     * Get create date string.
     *
     * @return the string
     */
    public String getCreateDate(){
        return createDate;
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

    /**
     * Set title.
     *
     * @param title the title
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * Get title string.
     *
     * @return the string
     */
    public String getTitle(){
        return title;
    }

    /**
     * Set update date.
     *
     * @param updateDate the update date
     */
    public void setUpdateDate(String updateDate){
        this.updateDate = updateDate;
    }

    /**
     * Get update date string.
     *
     * @return the string
     */
    public String getUpdateDate(){
        return updateDate;
    }
}
