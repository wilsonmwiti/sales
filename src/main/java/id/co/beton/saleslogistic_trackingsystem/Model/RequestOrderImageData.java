package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class RequestOrderImageData
 */
public class RequestOrderImageData {
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("file")
    @Expose
    private String file;
    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("request_order_id")
    @Expose
    private int requestOrderId;
    @SerializedName("update_date")
    @Expose
    private String updateDate;

    /**
     * Gets create date.
     *
     * @return the create date
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Sets create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    /**
     * Gets file.
     *
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * Sets file.
     *
     * @param file the file
     */
    public void setFile(String file) {
        this.file = file;
    }

    /**
     * Gets filename.
     *
     * @return the filename
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Sets filename.
     *
     * @param filename the filename
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets request order id.
     *
     * @return the request order id
     */
    public int getRequestOrderId() {
        return requestOrderId;
    }

    /**
     * Sets request order id.
     *
     * @param requestOrderId the request order id
     */
    public void setRequestOrderId(int requestOrderId) { this.requestOrderId = requestOrderId;}

    /**
     * Gets update date.
     *
     * @return the update date
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * Sets update date.
     *
     * @param updateDate the update date
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }
}
