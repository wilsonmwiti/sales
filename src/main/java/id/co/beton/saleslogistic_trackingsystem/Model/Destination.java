package id.co.beton.saleslogistic_trackingsystem.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Class Destination
 */
public class Destination {
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("customer_code")
    @Expose
    private String customerCode;
    @SerializedName("customer_email")
    @Expose
    private Object customerEmail;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("lat")
    @Expose
    private double lat;
    @SerializedName("lng")
    @Expose
    private double lng;
    @SerializedName("nfcid")
    @Expose
    private String nfcid;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("order_route")
    @Expose
    private Integer orderRoute=null;
    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("pic_job_position")
    @Expose
    private String picJobPosition;

    @SerializedName("pic_mobile")
    @Expose
    private String picMobile;

    @SerializedName("pic_name")
    @Expose
    private String picName;

    @SerializedName("invoice")
    @Expose
    private List<Invoice> invoices=null;

    @SerializedName("packing_slip")
    @Expose
    private List<PackingSlip> packingSlips=null;

    @SerializedName("total_invoice")
    @Expose
    private Integer totalInvoice=0;

    @SerializedName("total_packing_slip")
    @Expose
    private Integer totalPackingSlip=0;

    @SerializedName("arrival_time")
    @Expose
    private String arrivalTime;

    @SerializedName("departure_time")
    @Expose
    private String departureTime;

    @SerializedName("last_delivery_date")
    @Expose
    private String lastDeliveryDate;

    @SerializedName("last_request_order")
    @Expose
    private String lastRequestOrder;

    @SerializedName("last_visited_date")
    @Expose
    private String lastVisitedDate;


    private final static long serialVersionUID = -6675536706855185217L;

    /**
     * Gets address.
     *
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * Sets address.
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * Gets customer code.
     *
     * @return the customer code
     */
    public String getCustomerCode() {
        return customerCode;
    }

    /**
     * Sets customer code.
     *
     * @param customerCode the customer code
     */
    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    /**
     * Gets customer email.
     *
     * @return the customer email
     */
    public Object getCustomerEmail() {
        return customerEmail;
    }

    /**
     * Sets customer email.
     *
     * @param customerEmail the customer email
     */
    public void setCustomerEmail(Object customerEmail) {
        this.customerEmail = customerEmail;
    }

    /**
     * Gets customer name.
     *
     * @return the customer name
     */
    public String getCustomerName() {
        return customerName;
    }

    /**
     * Sets customer name.
     *
     * @param customerName the customer name
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * Gets lat.
     *
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * Sets lat.
     *
     * @param lat the lat
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * Gets lng.
     *
     * @return the lng
     */
    public double getLng() {
        return lng;
    }

    /**
     * Sets lng.
     *
     * @param lng the lng
     */
    public void setLng(double lng) {
        this.lng = lng;
    }

    /**
     * Gets nfcid.
     *
     * @return the nfcid
     */
    public String getNfcid() {
        return nfcid;
    }

    /**
     * Sets nfcid.
     *
     * @param nfcid the nfcid
     */
    public void setNfcid(String nfcid) {
        this.nfcid = nfcid;
    }

    /**
     * Gets note.
     *
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets note.
     *
     * @param note the note
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * Gets order route.
     *
     * @return the order route
     */
    public Integer getOrderRoute() {
        return orderRoute;
    }

    /**
     * Sets order route.
     *
     * @param orderRoute the order route
     */
    public void setOrderRoute(Integer orderRoute) {
        this.orderRoute = orderRoute;
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
     * Gets invoices.
     *
     * @return the invoices
     */
    public List<Invoice> getInvoices() {
        return invoices;
    }

    /**
     * Sets invoices.
     *
     * @param invoices the invoices
     */
    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
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
     * Gets pic job position.
     *
     * @return the pic job position
     */
    public String getPicJobPosition() {
        return picJobPosition;
    }

    /**
     * Sets pic job position.
     *
     * @param picJobPosition the pic job position
     */
    public void setPicJobPosition(String picJobPosition) {
        this.picJobPosition = picJobPosition;
    }

    /**
     * Gets pic mobile.
     *
     * @return the pic mobile
     */
    public String getPicMobile() {
        return picMobile;
    }

    /**
     * Sets pic mobile.
     *
     * @param picMobile the pic mobile
     */
    public void setPicMobile(String picMobile) {
        this.picMobile = picMobile;
    }

    /**
     * Gets pic name.
     *
     * @return the pic name
     */
    public String getPicName() {
        return picName;
    }

    /**
     * Sets pic name.
     *
     * @param picName the pic name
     */
    public void setPicName(String picName) {
        this.picName = picName;
    }

    /**
     * Gets packing slips.
     *
     * @return the packing slips
     */
    public List<PackingSlip> getPackingSlips() {
        return packingSlips;
    }

    /**
     * Sets packing slips.
     *
     * @param packingSlips the packing slips
     */
    public void setPackingSlips(List<PackingSlip> packingSlips) {
        this.packingSlips = packingSlips;
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

    @Override
    public String toString() {
        // return new ToStringBuilder(this).append("address", address).append("customerCode", customerCode).append("customerEmail", customerEmail).append("customerName", customerName).append("lat", lat).append("lng", lng).append("nfcid", nfcid).append("orderRoute", orderRoute).append("phone", phone).toString();
        return new ToStringBuilder(this).append("customerName", customerName).append("address", address).append("customerCode", customerCode).append("customerEmail", customerEmail).append("lat", lat).append("lng", lng).append("nfcid", nfcid).append("orderRoute", orderRoute).append("phone", phone).toString();
    }

    /**
     * Get invoice ammount integer.
     *
     * @return the integer
     */
    public Integer getInvoiceAmmount(){
        Integer sum=0;
        for (int i=0; i<getInvoices().size();i++){
            sum += getInvoices().get(i).getInvoiceAmount();
        }

        return sum;
    }

    /**
     * Gets arrival time.
     *
     * @return the arrival time
     */
    public String getArrivalTime() {
        return arrivalTime;
    }

    /**
     * Sets arrival time.
     *
     * @param arrivalTime the arrival time
     */
    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    /**
     * Gets departure time.
     *
     * @return the departure time
     */
    public String getDepartureTime() {
        return departureTime;
    }

    /**
     * Sets departure time.
     *
     * @param departureTime the departure time
     */
    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    /**
     * Gets last delivery date.
     *
     * @return the last delivery date
     */
    public String getLastDeliveryDate() {
        return lastDeliveryDate;
    }

    /**
     * Sets last delivery date.
     *
     * @param lastDeliveryDate the last delivery date
     */
    public void setLastDeliveryDate(String lastDeliveryDate) {
        this.lastDeliveryDate = lastDeliveryDate;
    }

    /**
     * Gets last request order.
     *
     * @return the last request order
     */
    public String getLastRequestOrder() {
        return lastRequestOrder;
    }

    /**
     * Sets last request order.
     *
     * @param lastRequestOrder the last request order
     */
    public void setLastRequestOrder(String lastRequestOrder) {
        this.lastRequestOrder = lastRequestOrder;
    }

    /**
     * Gets last visited date.
     *
     * @return the last visited date
     */
    public String getLastVisitedDate() {
        return lastVisitedDate;
    }

    /**
     * Sets last visited date.
     *
     * @param lastVisitedDate the last visited date
     */
    public void setLastVisitedDate(String lastVisitedDate) {
        this.lastVisitedDate = lastVisitedDate;
    }
}
