package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Class DataPackingSlip
 */
public class DataPackingSlip implements Serializable {

    @SerializedName("date")
    private String date;

    @SerializedName("edit_data")
    private Object editData;

    @SerializedName("total_customer")
    private int totalCustomer;

    @SerializedName("destination")
    private List<Destination> destination;

    @SerializedName("approval_by")
    private int approvalBy;

    @SerializedName("asset_id")
    private Object assetId;

    @SerializedName("is_approval")
    private int isApproval;

    @SerializedName("update_date")
    private String updateDate;

    @SerializedName("create_by")
    private int createBy;

    @SerializedName("start_route_branch_id")
    private int startRouteBranchId;

    @SerializedName("end_route_branch_id")
    private int endRouteBranchId;

    @SerializedName("is_deleted")
    private int isDeleted;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("total_packing_slip")
    private int totalPackingSlip;

    @SerializedName("id")
    private int id;

    @SerializedName("create_date")
    private String createDate;

    @SerializedName("user")
    private User user;

    @SerializedName("packing_slip_id")
    private List<PackingSlip> packingSlipId;

    /**
     * Sets date.
     *
     * @param date the date
     */
    public void setDate(String date) {
        this.date = date;
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
     * Sets edit data.
     *
     * @param editData the edit data
     */
    public void setEditData(Object editData) {
        this.editData = editData;
    }

    /**
     * Gets edit data.
     *
     * @return the edit data
     */
    public Object getEditData() {
        return editData;
    }

    /**
     * Sets total customer.
     *
     * @param totalCustomer the total customer
     */
    public void setTotalCustomer(int totalCustomer) {
        this.totalCustomer = totalCustomer;
    }

    /**
     * Gets total customer.
     *
     * @return the total customer
     */
    public int getTotalCustomer() {
        return totalCustomer;
    }

    /**
     * Sets destination.
     *
     * @param destination the destination
     */
    public void setDestination(List<Destination> destination) {
        this.destination = destination;
    }

    /**
     * Gets destination.
     *
     * @return the destination
     */
    public List<Destination> getDestination() {
        return destination;
    }

    /**
     * Sets approval by.
     *
     * @param approvalBy the approval by
     */
    public void setApprovalBy(int approvalBy) {
        this.approvalBy = approvalBy;
    }

    /**
     * Gets approval by.
     *
     * @return the approval by
     */
    public int getApprovalBy() {
        return approvalBy;
    }

    /**
     * Sets asset id.
     *
     * @param assetId the asset id
     */
    public void setAssetId(Object assetId) {
        this.assetId = assetId;
    }

    /**
     * Gets asset id.
     *
     * @return the asset id
     */
    public Object getAssetId() {
        return assetId;
    }

    /**
     * Sets is approval.
     *
     * @param isApproval the is approval
     */
    public void setIsApproval(int isApproval) {
        this.isApproval = isApproval;
    }

    /**
     * Gets is approval.
     *
     * @return the is approval
     */
    public int getIsApproval() {
        return isApproval;
    }

    /**
     * Sets update date.
     *
     * @param updateDate the update date
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    /**
     * Gets update date.
     *
     * @return the update date
     */
    public String getUpdateDate() {
        return updateDate;
    }

    /**
     * Sets create by.
     *
     * @param createBy the create by
     */
    public void setCreateBy(int createBy) {
        this.createBy = createBy;
    }

    /**
     * Gets create by.
     *
     * @return the create by
     */
    public int getCreateBy() {
        return createBy;
    }

    /**
     * Sets start route branch id.
     *
     * @param startRouteBranchId the start route branch id
     */
    public void setStartRouteBranchId(int startRouteBranchId) {
        this.startRouteBranchId = startRouteBranchId;
    }

    /**
     * Gets start route branch id.
     *
     * @return the start route branch id
     */
    public int getStartRouteBranchId() {
        return startRouteBranchId;
    }

    /**
     * Sets end route branch id.
     *
     * @param endRouteBranchId the end route branch id
     */
    public void setEndRouteBranchId(int endRouteBranchId) {
        this.endRouteBranchId = endRouteBranchId;
    }

    /**
     * Gets end route branch id.
     *
     * @return the end route branch id
     */
    public int getEndRouteBranchId() {
        return endRouteBranchId;
    }

    /**
     * Sets is deleted.
     *
     * @param isDeleted the is deleted
     */
    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * Gets is deleted.
     *
     * @return the is deleted
     */
    public int getIsDeleted() {
        return isDeleted;
    }


    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Sets total packing slip.
     *
     * @param totalPackingSlip the total packing slip
     */
    public void setTotalPackingSlip(int totalPackingSlip) {
        this.totalPackingSlip = totalPackingSlip;
    }

    /**
     * Gets total packing slip.
     *
     * @return the total packing slip
     */
    public int getTotalPackingSlip() {
        return totalPackingSlip;
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
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
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
     * Gets create date.
     *
     * @return the create date
     */
    public String getCreateDate() {
        return createDate;
    }

    /**
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets user.
     *
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets packing slip id.
     *
     * @param packingSlipId the packing slip id
     */
    public void setPackingSlipId(List<PackingSlip> packingSlipId) {
        this.packingSlipId = packingSlipId;
    }

    /**
     * Gets packing slip id.
     *
     * @return the packing slip id
     */
    public List<PackingSlip> getPackingSlipId() {
        return packingSlipId;
    }
}
