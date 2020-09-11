package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Class Contact
 */
public class Contacts {
    @SerializedName("job_position")
    @Expose
    private String job_position;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("phone")
    @Expose
    private String phone;

    /**
     * Gets email.
     *
     * @return the email
     */
    public String getJob_position() {
        return job_position;
    }

    /**
     * Sets email.
     *
     * @param email the email
     */
    public void setJob_position(String email) {
        this.job_position= job_position;
    }

    /**
     * Gets mobile.
     *
     * @return the mobile
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets mobile.
     *
     * @param note the mobile
     */
    public void setNote(String note) {
        this.note= note;
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
}
