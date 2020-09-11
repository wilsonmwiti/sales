package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Class Customer
 */
public class Mycustomer {
    @SerializedName("address")
    @Expose
    private String alamat;
    @SerializedName("approval_by")
    @Expose
    private Integer approvalBy;
    @SerializedName("branch")
    @Expose
    private List<Branch> branch = null;
    @SerializedName("business_activity")
    @Expose
    private Object businessActivity;
    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("contacts")
    @Expose
    private Object contacts;

    @SerializedName("create_by")
    @Expose
    private Object createBy;
    @SerializedName("create_date")
    @Expose
    private Object createDate;
    @SerializedName("edit_data")
    @Expose
    private Object editData;
    @SerializedName("email")
    @Expose
    private Object email;
    @SerializedName("import_by")
    @Expose
    private Integer importBy;
    @SerializedName("import_date")
    @Expose
    private String importDate;
    @SerializedName("is_approval")
    @Expose
    private Integer isApproval;
    @SerializedName("is_branch")
    @Expose
    private Integer isBranch;
    @SerializedName("is_deleted")
    @Expose
    private Integer isDeleted;
    @SerializedName("lat")
    @Expose
    private Object lat;
    @SerializedName("lng")
    @Expose
    private Object lng;
    @SerializedName("name")
    @Expose
    private String nama;
    @SerializedName("nfcid")
    @Expose
    private Object nfcid;
    @SerializedName("parent_code")
    @Expose
    private String parentCode;
    @SerializedName("password")
    @Expose
    private Object password;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("update_date")
    @Expose
    private String updateDate;
    @SerializedName("username")
    @Expose
    private Object username;

    @SerializedName("category")
    @Expose
    private Object category;

    @SerializedName("list_address")
    @Expose
    private List<String> listAddress;

    @SerializedName("list_contacts")
    @Expose
    private List<Contact> listContacts;

    private String jam;
    private int totalInvoice;

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param val the val
     */
    public void setCode(String val) {
        this.code = val;
    }

    /**
     * Gets nama.
     *
     * @return the nama
     */
    public String getNama() {
        return nama;
    }

    /**
     * Sets nama.
     *
     * @param nama the nama
     */
    public void setNama(String nama) {
        this.nama = nama;
    }

    /**
     * Gets alamat.
     *
     * @return the alamat
     */
    public String getAlamat() {
        return alamat;
    }

    /**
     * Sets alamat.
     *
     * @param namaJalan the nama jalan
     */
    public void setAlamat(String namaJalan) {
        this.alamat = namaJalan;
    }

    /**
     * Gets jam.
     *
     * @return the jam
     */
    public String getJam() {
        return jam;
    }

    /**
     * Sets jam.
     *
     * @param jam the jam
     */
    public void setJam(String jam) {
        this.jam = jam;
    }

    /**
     * Gets total invoice.
     *
     * @return the total invoice
     */
    public int getTotalInvoice() {
        return totalInvoice;
    }

    /**
     * Sets total invoice.
     *
     * @param totalInvoice the total invoice
     */
    public void setTotalInvoice(int totalInvoice) {
        this.totalInvoice = totalInvoice;
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
     * Gets branch.
     *
     * @return the branch
     */
    public List<Branch> getBranch() {
        return branch;
    }

    /**
     * Sets branch.
     *
     * @param branch the branch
     */
    public void setBranch(List<Branch> branch) {
        this.branch = branch;
    }

    /**
     * Gets business activity.
     *
     * @return the business activity
     */
    public Object getBusinessActivity() {
        return businessActivity;
    }

    /**
     * Sets business activity.
     *
     * @param businessActivity the business activity
     */
    public void setBusinessActivity(Object businessActivity) {
        this.businessActivity = businessActivity;
    }


    /**
     * Gets contacts.
     *
     * @return the contacts
     */
    public Object getContacts() {
        return contacts;
    }

    /**
     * Sets contacts.
     *
     * @param contacts the contacts
     */
    public void setContacts(Object contacts) {
        this.contacts = contacts;
    }

    /**
     * Gets create by.
     *
     * @return the create by
     */
    public Object getCreateBy() {
        return createBy;
    }

    /**
     * Sets create by.
     *
     * @param createBy the create by
     */
    public void setCreateBy(Object createBy) {
        this.createBy = createBy;
    }

    /**
     * Gets create date.
     *
     * @return the create date
     */
    public Object getCreateDate() {
        return createDate;
    }

    /**
     * Sets create date.
     *
     * @param createDate the create date
     */
    public void setCreateDate(Object createDate) {
        this.createDate = createDate;
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
     * Gets email.
     *
     * @return the email
     */
    public Object getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(Object email) {
        this.email = email;
    }

    /**
     * Gets import by.
     *
     * @return the import by
     */
    public Integer getImportBy() {
        return importBy;
    }

    /**
     * Sets import by.
     *
     * @param importBy the import by
     */
    public void setImportBy(Integer importBy) {
        this.importBy = importBy;
    }

    /**
     * Gets import date.
     *
     * @return the import date
     */
    public String getImportDate() {
        return importDate;
    }

    /**
     * Sets import date.
     *
     * @param importDate the import date
     */
    public void setImportDate(String importDate) {
        this.importDate = importDate;
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
     * Gets is branch.
     *
     * @return the is branch
     */
    public Integer getIsBranch() {
        return isBranch;
    }

    /**
     * Sets is branch.
     *
     * @param isBranch the is branch
     */
    public void setIsBranch(Integer isBranch) {
        this.isBranch = isBranch;
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
     * Gets lat.
     *
     * @return the lat
     */
    public Object getLat() {
        return lat;
    }

    /**
     * Sets lat.
     *
     * @param lat the lat
     */
    public void setLat(Object lat) {
        this.lat = lat;
    }

    /**
     * Gets lng.
     *
     * @return the lng
     */
    public Object getLng() {
        return lng;
    }

    /**
     * Sets lng.
     *
     * @param lng the lng
     */
    public void setLng(Object lng) {
        this.lng = lng;
    }

    /**
     * Gets nfcid.
     *
     * @return the nfcid
     */
    public Object getNfcid() {
        return nfcid;
    }

    /**
     * Sets nfcid.
     *
     * @param nfcid the nfcid
     */
    public void setNfcid(Object nfcid) {
        this.nfcid = nfcid;
    }

    /**
     * Gets parent code.
     *
     * @return the parent code
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * Sets parent code.
     *
     * @param parentCode the parent code
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public Object getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(Object password) {
        this.password = password;
    }

    /**
     * Gets phone.
     *
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * Sets phone.
     *
     * @param phone the phone
     */
    public void setPhone(String phone) {
        this.phone = phone;
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
     * Gets username.
     *
     * @return the username
     */
    public Object getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(Object username) {
        this.username = username;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public Object getCategory() {
        return category;
    }

    /**
     * Sets username.
     *
     * @param category the username
     */
    public void setCategory(Object category) {
        this.category = category;
    }

    /**
     * Gets list address.
     *
     * @return the list address
     */
    public List<String> getListAddress() {
        return listAddress;
    }

    /**
     * Sets list address.
     *
     * @param listAddress the list address
     */
    public void setListAddress(List<String> listAddress) {
        this.listAddress = listAddress;
    }

    /**
     * Gets list contacts.
     *
     * @return the list contacts
     */
    public List<Contact> getListContacts() {
        return listContacts;
    }

    /**
     * Sets list contacts.
     *
     * @param listContacts the list contacts
     */
    public void setListContacts(List<Contact> listContacts) {
        this.listContacts = listContacts;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("address", alamat).append("approvalBy", approvalBy).append("branch", branch).append("businessActivity", businessActivity).append("code", code).append("contacts", contacts).append("createBy", createBy).append("createDate", createDate).append("editData", editData).append("email", email).append("importBy", importBy).append("importDate", importDate).append("isApproval", isApproval).append("isBranch", isBranch).append("isDeleted", isDeleted).append("lat", lat).append("lng", lng).append("name", nama).append("nfcid", nfcid).append("parentCode", parentCode).append("password", password).append("phone", phone).append("updateDate", updateDate).append("username", username).toString();
    }
}
