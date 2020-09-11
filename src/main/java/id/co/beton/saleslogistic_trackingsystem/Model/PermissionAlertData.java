package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class PermissionAlertData
 */
public class PermissionAlertData {
    @SerializedName("approval_by")
    @Expose
    private Integer approvalBy;
    @SerializedName("create_by")
    @Expose
    private Integer createBy;
    @SerializedName("date")
    @Expose
    private String date;
    /*@SerializedName("description")
    @Expose
    private PermissionAlertDataDescription description;*/
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("is_approved")
    @Expose
    private Integer isApproved;
    @SerializedName("is_rejected")
    @Expose
    private Integer isRejected;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("reject_by")
    @Expose
    private Object rejectBy;
    @SerializedName("subject")
    @Expose
    private String subject;
    @SerializedName("type")
    @Expose
    private String type;
    private final static long serialVersionUID = 1473915046692281425L;

    /**
     * Gets approval by.
     *
     * @return the approval by
     */
    public Integer getApprovalBy() {
        return approvalBy;
    }

    /**
     * Sets approval by.
     *
     * @param approvalBy the approval by
     */
    public void setApprovalBy(Integer approvalBy) {
        this.approvalBy = approvalBy;
    }

    /**
     * Gets create by.
     *
     * @return the create by
     */
    public Integer getCreateBy() {
        return createBy;
    }

    /**
     * Sets create by.
     *
     * @param createBy the create by
     */
    public void setCreateBy(Integer createBy) {
        this.createBy = createBy;
    }

    /**
     * Gets date.
     *
     * @return the date
     */
    public String getDate() {
        return date;
    }

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /*public PermissionAlertDataDescription getDescription() {
        return description;
    }

    public void setDescription(PermissionAlertDataDescription description) {
        this.description = description;
    }*/

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Gets is approved.
     *
     * @return the is approved
     */
    public Integer getIsApproved() {
        return isApproved;
    }

    /**
     * Sets is approved.
     *
     * @param isApproved the is approved
     */
    public void setIsApproved(Integer isApproved) {
        this.isApproved = isApproved;
    }

    /**
     * Gets is rejected.
     *
     * @return the is rejected
     */
    public Integer getIsRejected() {
        return isRejected;
    }

    /**
     * Sets is rejected.
     *
     * @param isRejected the is rejected
     */
    public void setIsRejected(Integer isRejected) {
        this.isRejected = isRejected;
    }

    /**
     * Gets notes.
     *
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Sets notes.
     *
     * @param notes the notes
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * Gets reject by.
     *
     * @return the reject by
     */
    public Object getRejectBy() {
        return rejectBy;
    }

    /**
     * Sets reject by.
     *
     * @param rejectBy the reject by
     */
    public void setRejectBy(Object rejectBy) {
        this.rejectBy = rejectBy;
    }

    /**
     * Gets subject.
     *
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * Sets subject.
     *
     * @param subject the subject
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }
}
