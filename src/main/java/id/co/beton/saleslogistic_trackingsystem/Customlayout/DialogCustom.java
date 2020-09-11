package id.co.beton.saleslogistic_trackingsystem.Customlayout;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Class DialogCustom
 * to show loading dialog
 */
public class DialogCustom {
    private Context context;
    private ProgressDialog progressDialog;

    public DialogCustom(Context context, String message){
        this.context = context;
        progressDialog= new ProgressDialog(context);
        progressDialog.setMessage(message); // Setting Message
        progressDialog.setTitle(""); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
    }

    public DialogCustom(Context context) {
        this.context = context;
        progressDialog= new ProgressDialog(context);
        progressDialog.setMessage("Loading..."); // Setting Message
        progressDialog.setTitle(""); // Setting Title
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER); // Progress Dialog Style Spinner
        progressDialog.setCancelable(false);
    }

    public void show(){
        progressDialog.show();
    }

    public void hidden(){
        progressDialog.dismiss();
    }
}
