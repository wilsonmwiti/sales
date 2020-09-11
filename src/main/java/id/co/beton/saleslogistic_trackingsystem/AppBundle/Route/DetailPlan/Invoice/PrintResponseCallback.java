package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.DetailPlan.Invoice;

import retrofit2.Response;

public interface PrintResponseCallback<T> {
    void onSuccess(Response<T> response);
    void onFailure(String message, Throwable t);
}