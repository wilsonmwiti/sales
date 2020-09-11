package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class Profile
 */
public class Profile {
    @SerializedName("branch")
    @Expose
    private Branch branch;
    @SerializedName("division")
    @Expose
    private Division division;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nip")
    @Expose
    private String nip;
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("username")
    @Expose
    private String username;
    private final static long serialVersionUID = 8898355742567744226L;

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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("branch", branch).append("division", division).append("name", name).append("nip", nip).append("phone", phone).append("username", username).toString();
    }
}
