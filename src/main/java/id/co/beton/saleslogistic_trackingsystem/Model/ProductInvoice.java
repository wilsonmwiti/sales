package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Class ProductInvoice
 */
public class ProductInvoice {

    @SerializedName("brand_name")
    @Expose
    private String brandName;
    @SerializedName("discount")
    @Expose
    private Double discount;
    @SerializedName("dpn")
    @Expose
    private Double dpn;
    @SerializedName("dpp")
    @Expose
    private Double dpp;
    @SerializedName("item_number")
    @Expose
    private String itemNumber;
    @SerializedName("line_amount_gross")
    @Expose
    private Double lineAmountGross;
    @SerializedName("net_amount")
    @Expose
    private Double netAmount;
    @SerializedName("product_name")
    @Expose
    private String productName;
    @SerializedName("quantity")
    @Expose
    private Integer quantity;
    @SerializedName("site")
    @Expose
    private String site;
    @SerializedName("total_discount")
    @Expose
    private Double totalDiscount;
    @SerializedName("unit_price")
    @Expose
    private Integer unitPrice;

    /**
     * Gets brand name.
     *
     * @return the brand name
     */
    public String getBrandName() {
        return brandName;
    }

    /**
     * Sets brand name.
     *
     * @param brandName the brand name
     */
    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    /**
     * Gets discount.
     *
     * @return the discount
     */
    public Double getDiscount() {
        return discount;
    }

    /**
     * Sets discount.
     *
     * @param discount the discount
     */
    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    /**
     * Gets dpn.
     *
     * @return the dpn
     */
    public Double getDpn() {
        return dpn;
    }

    /**
     * Sets dpn.
     *
     * @param dpn the dpn
     */
    public void setDpn(Double dpn) {
        this.dpn = dpn;
    }

    /**
     * Gets dpp.
     *
     * @return the dpp
     */
    public Double getDpp() {
        return dpp;
    }

    /**
     * Sets dpp.
     *
     * @param dpp the dpp
     */
    public void setDpp(Double dpp) {
        this.dpp = dpp;
    }

    /**
     * Gets item number.
     *
     * @return the item number
     */
    public String getItemNumber() {
        return itemNumber;
    }

    /**
     * Sets item number.
     *
     * @param itemNumber the item number
     */
    public void setItemNumber(String itemNumber) {
        this.itemNumber = itemNumber;
    }

    /**
     * Gets line amount gross.
     *
     * @return the line amount gross
     */
    public Double getLineAmountGross() {
        return lineAmountGross;
    }

    /**
     * Sets line amount gross.
     *
     * @param lineAmountGross the line amount gross
     */
    public void setLineAmountGross(Double lineAmountGross) {
        this.lineAmountGross = lineAmountGross;
    }

    /**
     * Gets net amount.
     *
     * @return the net amount
     */
    public Double getNetAmount() {
        return netAmount;
    }

    /**
     * Sets net amount.
     *
     * @param netAmount the net amount
     */
    public void setNetAmount(Double netAmount) {
        this.netAmount = netAmount;
    }

    /**
     * Gets product name.
     *
     * @return the product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets product name.
     *
     * @param productName the product name
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Gets quantity.
     *
     * @return the quantity
     */
    public Integer getQuantity() {
        return quantity;
    }

    /**
     * Sets quantity.
     *
     * @param quantity the quantity
     */
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    /**
     * Gets site.
     *
     * @return the site
     */
    public String getSite() {
        return site;
    }

    /**
     * Sets site.
     *
     * @param site the site
     */
    public void setSite(String site) {
        this.site = site;
    }

    /**
     * Gets total discount.
     *
     * @return the total discount
     */
    public Double getTotalDiscount() {
        return totalDiscount;
    }

    /**
     * Sets total discount.
     *
     * @param totalDiscount the total discount
     */
    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    /**
     * Gets unit price.
     *
     * @return the unit price
     */
    public Integer getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets unit price.
     *
     * @param unitPrice the unit price
     */
    public void setUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
    }
}
