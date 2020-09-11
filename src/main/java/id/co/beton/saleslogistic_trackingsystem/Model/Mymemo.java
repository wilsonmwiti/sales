package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Class Memo
 */
public class Mymemo {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("plan_id")
    @Expose
    private Integer plan_id;

    @SerializedName("customer_code")
    @Expose
    private String customer_code;

    @SerializedName("notes")
    @Expose
    private String notes;

    @SerializedName("category_visit")
    @Expose
    private String category_visit;

    @SerializedName("nc")
    @Expose
    private String nc;

    @SerializedName("visit_images")
    @Expose
    private List<Photo> visitImages = null;

    @SerializedName("have_competitor")
    @Expose
    private Integer haveCompetitor;

    @SerializedName("competitor_images")
    @Expose
    private List<Photo> competitorImages = null;

    @SerializedName("create_date")
    @Expose
    private String createDate;

    @SerializedName("update_date")
    @Expose
    private String updateDate;

    @SerializedName("created_by")
    @Expose
    private Integer createdBy;

    /**
     * Gets id.
     *
     * @return the id
     */
    public Integer getId() { return id; }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(Integer id) { this.id = id; }

    /**
     * Gets plan id.
     *
     * @return the plan id
     */
    public Integer getPlanId() { return plan_id; }

    /**
     * Sets plan id.
     *
     * @param plan_id the plan id
     */
    public void setPlanId(Integer plan_id) { this.plan_id = plan_id; }

    /**
     * Gets customer code.
     *
     * @return the customer code
     */
    public String getCustomerCode() { return customer_code; }

    /**
     * Sets customer code.
     *
     * @param customer_code the customer code
     */
    public void setCustomerCode(String customer_code) { this.customer_code = customer_code; }

    /**
     * Gets notes.
     *
     * @return the notes
     */
    public String getNotes() { return notes; }

    /**
     * Sets notes.
     *
     * @param notes the notes
     */
    public void setNotes(String notes) { this.notes = notes; }

    /**
     * Gets category visit.
     *
     * @return the notes
     */
    public String getCategory_visit() { return category_visit; }

    /**
     * Sets category visit.
     *
     * @param category_visit the notes
     */
    public void setCategory_visit(String category_visit) { this.notes = category_visit; }

    /**
     * Gets category visit.
     *
     * @return the notes
     */
    public String getNc() { return nc; }

    /**
     * Sets category visit.
     *
     * @param nc the notes
     */
    public void setNc(String nc) { this.nc= nc; }

    /**
     * Gets visit images.
     *
     * @return the visit images
     */
    public List<Photo> getVisitImages() { return visitImages ; }

    /**
     * Sets visit images.
     *
     * @param visitImages the visit images
     */
    public void setVisitImages(List<Photo> visitImages) { this.visitImages = visitImages; }

    /**
     * Gets have competitor.
     *
     * @return the have competitor
     */
    public Integer getHaveCompetitor() { return haveCompetitor; }

    /**
     * Sets have competitor.
     *
     * @param haveCompetitor the have competitor
     */
    public void setHaveCompetitor(Integer haveCompetitor) { this.haveCompetitor = haveCompetitor; }

    /**
     * Gets competitor images.
     *
     * @return the competitor images
     */
    public List<Photo> getCompetitorImages() { return competitorImages ; }

    /**
     * Sets competitor images.
     *
     * @param competitorImages the competitor images
     */
    public void setCompetitorImages(List<Photo> competitorImages) { this.competitorImages = competitorImages; }

    /**
     * Gets create date.
     *
     * @return the create date
     */
    public String getCreateDate() { return createDate; }

    /**
     * Sets create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(String createDate) { this.createDate = createDate; }

    /**
     * Gets update date.
     *
     * @return the update date
     */
    public String getUpdateDate() { return updateDate; }

    /**
     * Sets update date.
     *
     * @param updateDate the update date
     */
    public void setUpdateDate(String updateDate) { this.updateDate = updateDate; }

    /**
     * Gets create by.
     *
     * @return the create by
     */
    public Integer getCreateBy() { return createdBy; }

    /**
     * Sets created by.
     *
     * @param createdBy the created by
     */
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    /*
    @SerializedName("memo")
    @Expose
    private String memo;

    @SerializedName("photo_visit")
    @Expose
    private List<Photo> listPhotoVisit = null;

    @SerializedName("photo_competitor")
    @Expose
    private List<Photo> listPhotoCompetitor = null;


    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getCustomerCode() {
        return customer_code;
    }

    public void setCustomerCode(String customer_code) {
        this.customer_code = customer_code;
    }

    public Integer getPlanId() {
        return plan_id;
    }

    public void setPlanId(Integer plan_id) {
        this.plan_id = plan_id;
    }

    public List<Photo> getListPhotoVisit() {
        return listPhotoVisit ;
    }

    public void setListPhotoVisit(List<Photo> listPhotoVisit) {
        this.listPhotoVisit = listPhotoVisit ;
    }

    public List<Photo> getListPhotoCompetitor(){ return listPhotoCompetitor; }

    public void setListPhotoCompetitor(List<Photo> listPhotoCompetitor){
        this.listPhotoCompetitor = listPhotoCompetitor ;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("memo", memo).append("customer_code", customer_code).append("plan_id", plan_id).toString();
    }
    */
}
