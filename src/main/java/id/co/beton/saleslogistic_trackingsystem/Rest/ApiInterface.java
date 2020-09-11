package id.co.beton.saleslogistic_trackingsystem.Rest;

import com.google.gson.JsonObject;
import id.co.beton.saleslogistic_trackingsystem.Model.StatisticUser;
import id.co.beton.saleslogistic_trackingsystem.Model.VisitPlanRoute;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;


/**
 * Class ApiInterface
 * list of endpoint on server
 * using retrofit2
 */
public interface ApiInterface {

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("auth")
    Call<ResponseObject> login(@Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("customer")
    Call<ResponseArrayObject> addCustomer(@Header("Authorization") String jwt, @Body JsonObject data);

    @GET("customer")
    Call<ResponseObject> customerAll(@Header("Authorization") String jwt, @Query("page") int page, @Query("limit") int limit);

    @GET("customer/{customer_id}")
    Call<ResponseObject> customerDetail(@Header("Authorization") String jwt, @Path("customer_id") String id);

    @GET("/customer/check/nearby")
    Call<ResponseObject> customerNearby(@Header("Authorization") String jwt, @Query("lat") Double lat, @Query("lng") Double lng, @Query("distance") Double distance, @Query("page") int page, @Query("limit") int limit);

    @GET("user/profile/{username}")
    Call<ResponseObject> profile(@Header("Authorization") String jwt, @Path("username") String username);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @PUT("/user/add/customer/{customer_code}")
    Call<ResponseArrayObject> updateListCustomer(@Header("Authorization") String jwt, @Path("customer_code") String customer_code, @Body JsonObject data);

    @GET("visit/plan")
    Call<ResponseObject> visitPlan(@Header("Authorization") String jwt);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @PUT("/visit/plan/{plan_id}/add/route")
    Call<ResponseArrayObject> addNewRoute(@Header("Authorization") String jwt, @Path("plan_id") int plantId , @Body JsonObject data);

    @GET("permission_alert")
    Call<ResponseObject> permissionAlert(@Header("Authorization") String jwt, @Query("page") int page, @Query("limit") int limit);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("permission_alert")
    Call<ResponseArrayObject> permissionAlertPost(@Header("Authorization") String jwt, @Body JsonObject data);

    @GET("permission_alert/{id}")
    Call<ResponseObject> permissionAlertDetail(@Header("Authorization") String jwt, @Path("id") Integer id);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @PUT("sales/invoice/confirm")
    Call<ResponseObject> salesInvoiceConfirm(@Header("Authorization") String jwt, @Body JsonObject data);

    @GET("sales/request/{customer_id}")
    Call<ResponseObject> salesRequest(@Header("Authorization") String jwt, @Path("customer_id") String id);

    @GET("sales/request/{request_id}/image") // sales/request/10/image
    Call<ResponseObject> salesRequestImage(@Header("Authorization") String jwt,@Path("request_id") int requestId);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("sales/payment")
    Call<ResponseArrayObject> salesPaymentPost(@Header("Authorization") String jwt, @Body JsonObject data);

    @GET("sales/payment/mobile")
    Call<ResponseObject> salesPaymentMobile(@Header("Authorization") String jwt, @Query("visit_plan_id") int visitPlanId,@Query("customer_code") String customerCode);

    @GET("sales/payment/mobile/{payment_id}")
    Call<ResponseObject> salesPaymentMobileDetail(@Header("Authorization") String jwt, @Path("payment_id") int paymentId);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @PUT("sales/payment/{payment_id}")
    Call<ResponseObject> salesPaymentConfirm(@Header("Authorization") String jwt, @Path("payment_id") String paymentId , @Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @PUT("sales/payment/{payment_id}/reprint")
    Call<ResponseArrayObject> salesPaymentReprint(@Header("Authorization") String jwt, @Path("payment_id") Integer paymentId );

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("sales/request")
    Call<ResponseArrayObject> salesRequestPost(@Header("Authorization") String jwt, @Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("device_token")
    Call<ResponseObject> deviceToken(@Header("Authorization") String jwt, @Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("activity/sales")
    Call<ResponseArrayObject> postTapNfcSales(@Header("Authorization") String jwt, @Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("activity/logistic")
    Call<ResponseArrayObject> postTapNfcDriver(@Header("Authorization") String jwt, @Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @PUT("visit/plan/{visit_plan_id}/invoice/confirm")
    Call<ResponseArrayObject> confirmInvoice(@Header("Authorization") String jwt,@Path("visit_plan_id") Integer id, @Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("sales/request/{customer_code}")
    Call<ResponseObject> salesRequestGet(@Header("Authorization") String jwt, @Path("customer_code") String customerCode);

    @GET
    Call<VisitPlanRoute> getRute(@Url String url);

    @GET("sales/payment/mobile")
    Call<ResponseObject> salesPayment(@Header("Authorization") String jwt,@Query("visit_plan_id") int vistiPlanId);

    @PUT("sales/payment/mobile/{payment_id}")
    Call<ResponseArrayObject> cancelPayment(@Header("Authorization") String jwt,@Path("payment_id") Integer paymentId,@Body JsonObject data);

    @POST("activity/sales/break_time")
    Call<ResponseArrayObject> addBreakTimeSales(@Header("Authorization") String jwt, @Body JsonObject data);

    @POST("activity/logistic/break_time")
    Call<ResponseArrayObject> addBreakTimeDrive(@Header("Authorization") String jwt, @Body JsonObject data);

    @GET("visit/plan/{delivery_plan_id}/order")
    Call<ResponseObject> salesOrder(@Header("Authorization") String jwt, @Path("delivery_plan_id") String visitPlanId);

    @GET("visit/plan/{delivery_plan_id}/request")
    Call<ResponseObject> requestOrder(@Header("Authorization") String jwt, @Path("delivery_plan_id") String visitPlanId);

    @POST("activity/sales/breadcrumb")
    Call<ResponseArrayObject> postBreadCumbsSales(@Header("Authorization") String jwt, @Body JsonObject data);

    @POST("activity/logistic/breadcrumb")
    Call<ResponseArrayObject> postBreadCumbsDriver(@Header("Authorization") String jwt, @Body JsonObject data);

    @GET("delivery/plan")
    Call<ResponseObject> getDeliveryPlan(@Header("Authorization") String jwt, @Query("plan_id") int planId);

    // @GET("delivery/plan")
    // Call<ResponseObject> getDeliveryPlanById(@Header("Authorization") String jwt, @Query("plan_id") int planId);

    @GET("delivery/plan/list")
    // Call<ResponseObject> getAllDeliveryPlan(@Header("Authorization") String jwt, @Query("type") String type, @Query("page") int page, @Query("limit") int limit, @Query("page_filter") String page_filter);
    Call<ResponseObject> getAllDeliveryPlan(@Header("Authorization") String jwt);

    @GET("delivery/plan/{delivery_plan_id}/{customer_code}")
    Call<ResponseObject> getDeliveryPlanCustomer(@Header("Authorization") String jwt,@Path("delivery_plan_id") Integer planId,@Path("customer_code") String customerCode);

    @PUT("delivery/plan/{delivery_plan_id}/packingslip/confirm")
    Call<ResponseArrayObject> confirmPackingSlip(@Header("Authorization") String jwt,@Path("delivery_plan_id") Integer planId,@Body JsonObject data);

    @GET("inbox")
    Call<ResponseObject> inbox(@Header("Authorization") String jwt,@QueryMap Map<String, String> params);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("packing/slip/{packing_id}/accept")
    Call<ResponseArrayObject> accepted(@Header("Authorization") String jwt,@Body JsonObject data,@Path("packing_id") String packing_id);

    @GET("statistic/sales/invoice")
    Call<ResponseObject> salesInvoice(@Header("Authorization") String jwt);

    @GET("/statistic/{job}/performance")
    Call<StatisticUser> userStatistic(@Header("Authorization") String jwt,@Path("job")String job, @QueryMap Map<String, String> params);

    @GET("visit/plan/{delivery_plan_id}/{customer_code}")
    Call<ResponseObject> detailVisitPlanByCustomerCode(@Header("Authorization") String jwt, @Path("delivery_plan_id") String visitPlanId,@Path("customer_code") String customerCode);

    @GET("visit/plan/{plan_id}/{customer_code}")
    Call<ResponseObject> getVisitPlanDetailByCustomer(@Header("Authorization") String jwt,@Path("plan_id") int planId,@Path("customer_code") String customerCode);

    @GET("delivery/plan/{plan_id}/{customer_code}")
    Call<ResponseObject> getDeliveryPlanDetailByCustomer(@Header("Authorization") String jwt,@Path("plan_id") int planId,@Path("customer_code") String customerCode);

    @POST("activity/{job}/idle_time")
    Call<ResponseArrayObject> addidleTime(@Header("Authorization") String jwt,@Path("job") String job, @Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("packing/slip/{packing_id}/reject")
    Call<ResponseArrayObject> rejected(@Header("Authorization") String jwt,@Body JsonObject data,@Path("packing_id") String packing_id);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("logout")
    Call<ResponseArrayObject> logout(@Header("Authorization") String jwt);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @GET("print/unique/code")
    Call<JsonObject> codePrint(@Header("Authorization") String jwt);

    @GET("setting/general")
    Call<ResponseObject> settingGeneral(@Header("Authorization") String jwt);

    @GET("/visit/plan/{plan_id}/summary/{customer_code}")
    Call<ResponseObject>  getVisitPlanSummary(@Header("Authorization") String jwt, @Path("plan_id") int planId,@Path("customer_code") String customerCode);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @PUT("/visit/plan/{plan_id}/summary/{customer_code}")
    Call<ResponseArrayObject>  updateVisitPlanSummary(@Header("Authorization") String jwt, @Path("plan_id") int planId,@Path("customer_code") String customerCode, @Body JsonObject data);

    @GET("/delivery/plan/{plan_id}/summary/{customer_code}")
    Call<ResponseObject>  getDeliveryPlanSummary(@Header("Authorization") String jwt, @Path("plan_id") int planId,@Path("customer_code") String customerCode);
    @Headers({"Accept: application/json", "Content-Type: application/json"})

    @PUT("/delivery/plan/{plan_id}/summary/{customer_code}")
    Call<ResponseArrayObject>  updateDeliveryPlanSummary(@Header("Authorization") String jwt, @Path("plan_id") int planId,@Path("customer_code") String customerCode, @Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("visit/plan")
    Call<ResponseArrayObject> postVisitPlan(@Header("Authorization") String jwt, @Body JsonObject data);

    //custom function
    @Headers({"Accept: application/json", "Content-Type: application/json;charset=utf-8"})
    @POST("cisangkan/mycustomer")
    Call<ResponseArrayObject> addMycustomer(@Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json;charset=utf-8"})
    @POST("cisangkan/mycustomer/summary")
    Call<ResponseObject> getMycustomerSummary(@Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json;charset=utf-8"})
    @PUT("cisangkan/mycustomer")
    Call<ResponseArrayObject> updateMycustomer(@Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json;charset=utf-8"})
    @POST("cisangkan/update/user/customer")
    Call<ResponseObject> updateCustomersSales(@Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json;charset=utf-8"})
    @POST("cisangkan/delete/customer")
    Call<ResponseArrayObject> deleteCustomer(@Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json;charset=utf-8"})
    @POST("cisangkan/mycustomer/searchonly")
    Call<ResponseObject> searchMycustomer(@Body JsonObject data);

    @PUT("cisangkan/delivery/plan/{plan_id}/summary/{customer_code}")
    Call<ResponseArrayObject>  updateDeliveryPlanSummaryCisangkan(
            @Path("plan_id") int planId,
            @Path("customer_code") String customerCode,
            @Body JsonObject data);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @PUT("cisangkan/visit/plan/summary/update_split_img")
    Call<ResponseArrayObject>  updateVisitPlanSummaryCisangkan(@Body JsonObject data);

    @GET("cisangkan/visit/plan/{plan_id}/summary/{customer_code}")
    Call<ResponseObject>  getVisitPlanSummaryCisangkan(@Header("Authorization") String jwt, @Path("plan_id") int planId,@Path("customer_code") String customerCode);

    @Headers({"Accept: application/json", "Content-Type: application/json"})
    @POST("cisangkan/mycustomer/last_insert_today")
    Call<ResponseObject> getLastInsertToday(@Body JsonObject data);
}