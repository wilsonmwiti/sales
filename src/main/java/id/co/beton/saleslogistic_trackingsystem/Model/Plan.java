package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Class Plan
 */
public class Plan {
    @SerializedName("approval_by")
    @Expose
    private Integer approvalBy;
    @SerializedName("change_route")
    @Expose
    private List<VisitPlanRoute> changeRoute=null;
    @SerializedName("asset_id")
    @Expose
    private Integer assetId;
    @SerializedName("asset")
    @Expose
    private Asset asset;
    @SerializedName("create_by")
    @Expose
    private Integer createBy;
    @SerializedName("create_date")
    @Expose
    private String createDate;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("destination")
    @Expose
    private List<Destination> destination = null;
    @SerializedName("edit_data")
    @Expose
    private Object editData;
    @SerializedName("end_route_branch")
    @Expose
    private RouteBranch endRouteBranch;
    @SerializedName("end_route_branch_id")
    @Expose
    private Integer endRouteBranchId;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("invoice_id")
    @Expose
    private List<Invoice> invoiceId = null;

    @SerializedName("packing_slip_id")
    @Expose
    private List<PackingSlip> packingSlipsId = null;

    @SerializedName("is_approval")
    @Expose
    private Integer isApproval;
    @SerializedName("is_deleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("route")
    @Expose(serialize = false, deserialize = false)
    private VisitPlanRoute route = null;
    @SerializedName("start_route_branch")
    @Expose
    private RouteBranch startRouteBranch;
    @SerializedName("start_route_branch_id")
    @Expose
    private Integer startRouteBranchId;
    @SerializedName("total_customer")
    @Expose
    private Integer totalCustomer;
    @SerializedName("total_invoice")
    @Expose
    private Integer totalInvoice=0;
    @SerializedName("total_packing_slip")
    @Expose
    private Integer totalPackingSlip=0;
    @SerializedName("update_date")
    @Expose
    private String updateDate;
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("user_id")
    @Expose
    private Integer userId;
    @SerializedName("is_use_route")
    @Expose
    private Integer isUseRoute;
    private final static long serialVersionUID = -3130909108026122644L;
    @SerializedName("destination_order")
    @Expose()
    private List<DestinationOrder> destinationOrder;
    @SerializedName("destination_new")
    @Expose
    private List<Destination> destinationNew;

    /**
     * Gets destination order.
     *
     * @return the destination order
     */
    public List<DestinationOrder> getDestinationOrder() {
        return destinationOrder;
    }

    /**
     * Sets destination order.
     *
     * @param destinationOrder the destination order
     */
    public void setDestinationOrder(List<DestinationOrder> destinationOrder) {
        this.destinationOrder = destinationOrder;
    }

    /**
     * Gets destination new.
     *
     * @return the destination new
     */
    public List<Destination> getDestinationNew() { return destinationNew;}

    /**
     * Set destination new.
     *
     * @param destinationNew the destination new
     */
    public void setDestinationNew(List<Destination> destinationNew){
        this.destinationNew = destinationNew;
    }

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
     * Gets asset id.
     *
     * @return the asset id
     */
    public Integer getAssetId() {
        return assetId;
    }

    /**
     * Sets asset.
     *
     * @param asset the asset
     */
    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    /**
     * Gets asset.
     *
     * @return the asset
     */
    public Asset getAsset() {
        return asset;
    }

    /**
     * Sets asset id.
     *
     * @param assetId the asset id
     */
    public void setAssetId(Integer assetId) {
        this.assetId = assetId;
    }

    /**
     * Gets change route.
     *
     * @return the change route
     */
    public List<VisitPlanRoute> getChangeRoute() {
        return changeRoute;
    }

    /**
     * Sets change route.
     *
     * @param route the route
     */
    public void setChangeRoute(List<VisitPlanRoute> route) {
        this.changeRoute = route;
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

    /**
     * Gets destination.
     *
     * @return the destination
     */
    public List<Destination> getDestination() {
        return destination;
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
     * Gets edit data.
     *
     * @return the edit data
     */
    public Object getEditData() {
        return editData;
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
     * Gets end route branch.
     *
     * @return the end route branch
     */
    public RouteBranch getEndRouteBranch() {
        return endRouteBranch;
    }

    /**
     * Sets end route branch.
     *
     * @param endRouteBranch the end route branch
     */
    public void setEndRouteBranch(RouteBranch endRouteBranch) {
        this.endRouteBranch = endRouteBranch;
    }

    /**
     * Gets end route branch id.
     *
     * @return the end route branch id
     */
    public Integer getEndRouteBranchId() {
        return endRouteBranchId;
    }

    /**
     * Sets end route branch id.
     *
     * @param endRouteBranchId the end route branch id
     */
    public void setEndRouteBranchId(Integer endRouteBranchId) {
        this.endRouteBranchId = endRouteBranchId;
    }

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
     * Gets invoice id.
     *
     * @return the invoice id
     */
    public List<Invoice> getInvoiceId() {
        return invoiceId;
    }

    /**
     * Sets invoice id.
     *
     * @param invoiceId the invoice id
     */
    public void setInvoiceId(List<Invoice> invoiceId) {
        this.invoiceId = invoiceId;
    }

    /**
     * Gets is approval.
     *
     * @return the is approval
     */
    public Integer getIsApproval() {
        return isApproval;
    }

    /**
     * Sets is approval.
     *
     * @param isApproval the is approval
     */
    public void setIsApproval(Integer isApproval) {
        this.isApproval = isApproval;
    }

    /**
     * Gets is deleted.
     *
     * @return the is deleted
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * Sets is deleted.
     *
     * @param isDeleted the is deleted
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * Gets route.
     *
     * @return the route
     */
    public VisitPlanRoute getRoute() {
        return route;
    }

    /**
     * Sets route.
     *
     * @param route the route
     */
    public void setRoute(VisitPlanRoute route) {
        this.route = route;
    }

    /**
     * Gets start route branch.
     *
     * @return the start route branch
     */
    public RouteBranch getStartRouteBranch() {
        return startRouteBranch;
    }

    /**
     * Sets start route branch.
     *
     * @param startRouteBranch the start route branch
     */
    public void setStartRouteBranch(RouteBranch startRouteBranch) {
        this.startRouteBranch = startRouteBranch;
    }

    /**
     * Gets start route branch id.
     *
     * @return the start route branch id
     */
    public Integer getStartRouteBranchId() {
        return startRouteBranchId;
    }

    /**
     * Sets start route branch id.
     *
     * @param startRouteBranchId the start route branch id
     */
    public void setStartRouteBranchId(Integer startRouteBranchId) {
        this.startRouteBranchId = startRouteBranchId;
    }

    /**
     * Gets total customer.
     *
     * @return the total customer
     */
    public Integer getTotalCustomer() {
        return totalCustomer;
    }

    /**
     * Sets total customer.
     *
     * @param totalCustomer the total customer
     */
    public void setTotalCustomer(Integer totalCustomer) {
        this.totalCustomer = totalCustomer;
    }

    /**
     * Gets total invoice.
     *
     * @return the total invoice
     */
    public Integer getTotalInvoice() {
        return totalInvoice;
    }

    /**
     * Sets total invoice.
     *
     * @param totalInvoice the total invoice
     */
    public void setTotalInvoice(Integer totalInvoice) {
        this.totalInvoice = totalInvoice;
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
     * Sets update date.
     *
     * @param updateDate the update date
     */
    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
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
     * Sets user.
     *
     * @param user the user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * Sets user id.
     *
     * @param userId the user id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * Gets packing slips id.
     *
     * @return the packing slips id
     */
    public List<PackingSlip> getPackingSlipsId() {
        return packingSlipsId;
    }

    /**
     * Sets packing slips id.
     *
     * @param packingSlipsId the packing slips id
     */
    public void setPackingSlipsId(List<PackingSlip> packingSlipsId) {
        this.packingSlipsId = packingSlipsId;
    }

    /**
     * Gets total packing slip.
     *
     * @return the total packing slip
     */
    public Integer getTotalPackingSlip() {
        return totalPackingSlip;
    }

    /**
     * Sets total packing slip.
     *
     * @param totalPackingSlip the total packing slip
     */
    public void setTotalPackingSlip(Integer totalPackingSlip) {
        this.totalPackingSlip = totalPackingSlip;
    }

    /**
     * Get is use route integer.
     *
     * @return the integer
     */
    public Integer getIsUseRoute(){return isUseRoute;}

    /**
     * Set is use route.
     *
     * @param isUseRoute the is use route
     */
    public void setIsUseRoute(Integer isUseRoute){ this.isUseRoute = isUseRoute;}

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("approvalBy", approvalBy).append("assetId", assetId).append("createBy", createBy).append("createDate", createDate).append("date", date).append("destination", destination).append("editData", editData).append("endRouteBranch", endRouteBranch).append("endRouteBranchId", endRouteBranchId).append("id", id).append("invoiceId", invoiceId).append("isApproval", isApproval).append("isDeleted", isDeleted).append("change_route", changeRoute).append("route", route).append("startRouteBranch", startRouteBranch).append("startRouteBranchId", startRouteBranchId).append("totalCustomer", totalCustomer).append("totalInvoice", totalInvoice).append("updateDate", updateDate).append("user", user).append("userId", userId).toString();
    }
}
