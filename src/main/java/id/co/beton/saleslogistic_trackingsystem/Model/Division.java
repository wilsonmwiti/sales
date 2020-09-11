package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Class Division
 */
public class Division {
    @SerializedName("division_code")
    @Expose
    private String divisionCode;
    @SerializedName("division_name")
    @Expose
    private String divisionName;
    @SerializedName("id")
    @Expose
    private Integer id;
    private final static long serialVersionUID = -1133013169993071428L;

    /**
     * Gets division code.
     *
     * @return the division code
     */
    public String getDivisionCode() {
        return divisionCode;
    }

    /**
     * Sets division code.
     *
     * @param divisionCode the division code
     */
    public void setDivisionCode(String divisionCode) {
        this.divisionCode = divisionCode;
    }

    /**
     * Gets division name.
     *
     * @return the division name
     */
    public String getDivisionName() {
        return divisionName;
    }

    /**
     * Sets division name.
     *
     * @param divisionName the division name
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("divisionCode", divisionCode).append("divisionName", divisionName).append("id", id).toString();
    }
}
