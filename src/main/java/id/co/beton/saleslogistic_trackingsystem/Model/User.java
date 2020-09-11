package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class User
 */
public class User {

    private final static long serialVersionUID = 731977942805737327L;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("jwt_access_token")
    @Expose
    private String jwtAccessToken;
    @SerializedName("mobile_privilege")
    @Expose
    private String mobilePrivilege;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;

    private String password;

    @SerializedName("nip")
    @Expose
    private String nip;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("employee_id")
    @Expose
    private Integer employeeId;
    @SerializedName("employee_name")
    @Expose
    private String employeeName;
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("setting")
    @Expose
    private Setting setting;

    @SerializedName("branch")
    @Expose
    private Branch branch;

    @SerializedName("division")
    @Expose
    private Division division;

    @SerializedName("branch_id")
    private int branchId;

    @SerializedName("is_collector_only")
    @Expose
    private int isCollectorOnly;

    @SerializedName("is_can_collect")
    @Expose
    private int isCanCollect;

    /**
     * Gets setting.
     *
     * @return the setting
     */
    public Setting getSetting() {
        return setting;
    }

    /**
     * Sets setting.
     *
     * @param setting the setting
     */
    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    /**
     * Gets employee name.
     *
     * @return the employee name
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * Sets employee name.
     *
     * @param employeeName the employee name
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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
     * Gets employee id.
     *
     * @return the employee id
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

    /**
     * Sets employee id.
     *
     * @param employeeId the employee id
     */
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * Gets nip.
     *
     * @return the nip
     */
    public String getNip() {
        return nip;
    }

    /**
     * Sets nip.
     *
     * @param nip the nip
     */
    public void setNip(String nip) {
        this.nip = nip;
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
     * Gets password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets jwt access token.
     *
     * @return the jwt access token
     */
    public String getJwtAccessToken() {
        return jwtAccessToken;
    }

    /**
     * Sets jwt access token.
     *
     * @param jwtAccessToken the jwt access token
     */
    public void setJwtAccessToken(String jwtAccessToken) {
        this.jwtAccessToken = jwtAccessToken;
    }

    /**
     * Gets mobile privilege.
     *
     * @return the mobile privilege
     */
    public String getMobilePrivilege() {
        return mobilePrivilege;
    }

    /**
     * Sets mobile privilege.
     *
     * @param mobilePrivilege the mobile privilege
     */
    public void setMobilePrivilege(String mobilePrivilege) {
        this.mobilePrivilege = mobilePrivilege;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets branch.
     *
     * @return the branch
     */
    public Branch getBranch() {
        return branch;
    }

    /**
     * Sets branch.
     *
     * @param branch the branch
     */
    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    /**
     * Gets division.
     *
     * @return the division
     */
    public Division getDivision() {
        return division;
    }

    /**
     * Sets division.
     *
     * @param division the division
     */
    public void setDivision(Division division) {
        this.division = division;
    }

    /**
     * Get is collector only int.
     *
     * @return the int
     */
    public int getIsCollectorOnly(){ return isCollectorOnly; }

    /**
     * Set is collector only.
     *
     * @param isCollectorOnly the is collector only
     */
    public void setIsCollectorOnly(int isCollectorOnly){ this.isCollectorOnly = isCollectorOnly;}

    /**
     * Get is can collect int.
     *
     * @return the int
     */
    public int getIsCanCollect(){ return isCanCollect; }

    /**
     * Set is can collect.
     *
     * @param isCanCollect the is can collect
     */
    public void setIsCanCollect(int isCanCollect){ this.isCanCollect = isCanCollect; }

}