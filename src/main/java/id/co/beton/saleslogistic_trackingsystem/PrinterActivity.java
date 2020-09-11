package id.co.beton.saleslogistic_trackingsystem;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.gson.JsonObject;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Printer.DeviceListActivity;
import id.co.beton.saleslogistic_trackingsystem.Printer.PrinterCommands;
import id.co.beton.saleslogistic_trackingsystem.Printer.PrinterUtils;
import id.co.beton.saleslogistic_trackingsystem.Printer.UnicodeFormatter;

import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.UUID;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class Printer
 * accommodate all print activities
 */
public class PrinterActivity extends BaseActivity implements Runnable {
    protected String TAG = PrinterActivity.class.getSimpleName();

    protected   BluetoothAdapter mBluetoothAdapter;
    protected UUID applicationUUID = UUID
            .fromString("00001101-0000-1000-8000-00805F9B34FB");
    /*protected Context context;
    protected Activity activity;*/
    protected static final int REQUEST_CONNECT_DEVICE = 99;
    protected static final int REQUEST_ENABLE_BT = 2;
    protected BluetoothSocket mBluetoothSocket;
    protected BluetoothDevice mBluetoothDevice;
    protected ProgressDialog mBluetoothConnectProgressDialog;
    protected static OutputStream outputStream;
    protected String codePrint ="";

    public PrinterActivity() {
    }

    /**
     * function to check whether bluetooth is active or not
     */
    protected void checkBluetooth(){
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        getCodePrint();
        if (mBluetoothAdapter == null) {
            if(Constants.DEBUG){
                Log.e(TAG,"bluetooth error");
            }
            Toasty.info(context,"Bluetooth tidak aktif").show();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent,
                        REQUEST_ENABLE_BT);
            } else {
                Intent connectIntent = new Intent(context,
                        DeviceListActivity.class);
                startActivityForResult(connectIntent,
                        REQUEST_CONNECT_DEVICE);
            }
        }
    }

    /**
     * function to get print code from backend
     *
     */
    protected void getCodePrint(){
        final ApiInterface apiInterface = ApiClient.getInstance(context);
        String jwt = UserUtil.getInstance(context).getJWTTOken();
        Call<JsonObject> call =apiInterface.codePrint("JWT "+ jwt);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                try{
                    if(response.body().get("data").toString().length()>0){
                        codePrint = response.body().get("data").toString();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {

            }
        });
    }

    /**
     * function to connecting to bluetooth
     */
    @Override
    public void run() {
        try {
            mBluetoothSocket = mBluetoothDevice
                    .createRfcommSocketToServiceRecord(applicationUUID);
            mBluetoothAdapter.cancelDiscovery();
            mBluetoothSocket.connect();
            mHandler.sendEmptyMessage(0);
        } catch (IOException eConnectException) {
            if(mBluetoothConnectProgressDialog!=null){
                mBluetoothConnectProgressDialog.cancel();
            }
            showToast();
            Log.d(TAG, "CouldNotConnectToSocket", eConnectException);
            closeSocket(mBluetoothSocket);
            return;
        }
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
    }

    @Override
    public void onBackPressed() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (Exception e) {
            Log.e("Tag", "Exe ", e);
        }
        setResult(RESULT_CANCELED);
        finish();
    }

    /**
     * function to show message (toast) when printer failed to connect
     */
    public void showToast()
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showErrorPopup();
                Toasty.error(context,"Gagal terhubung pada printer").show();
            }
        });
    }

    /**
     * function to close socket connection to bluetooth printer
     * @param nOpenSocket
     */
    protected void closeSocket(BluetoothSocket nOpenSocket) {
        try {
            nOpenSocket.close();
            Log.d(TAG, "SocketClosed");
        } catch (IOException ex) {
            if(mBluetoothConnectProgressDialog!=null){
                mBluetoothConnectProgressDialog.cancel();
            }
            Toasty.error(context,"Failed to close socket").show();
        }
    }

    /**
     * function to get result of bluetooth connection activity
     * @param mRequestCode
     * @param mResultCode
     * @param mDataIntent
     */
    @Override
    public void onActivityResult(int mRequestCode, int mResultCode,
                                 Intent mDataIntent) {
        super.onActivityResult(mRequestCode, mResultCode, mDataIntent);

        switch (mRequestCode) {
            case REQUEST_CONNECT_DEVICE:
                if (mResultCode == RESULT_OK) {
                    Bundle mExtra = mDataIntent.getExtras();

                    String mDeviceAddress = mExtra.getString("DeviceAddress");

                    mBluetoothDevice = mBluetoothAdapter
                            .getRemoteDevice(mDeviceAddress);
                    mBluetoothConnectProgressDialog = ProgressDialog.show(this,
                            "Connecting...", mBluetoothDevice.getName() + " : "
                                    + mBluetoothDevice.getAddress(), true, false);

                    Thread mBlutoothConnectThread = new Thread(this);
                    mBlutoothConnectThread.start();

                }
                break;

            case REQUEST_ENABLE_BT:
                if (mResultCode == RESULT_OK) {
//                    ListPairedDevices();
                    Intent connectIntent = new Intent(context,
                            DeviceListActivity.class);
                    startActivityForResult(connectIntent, REQUEST_CONNECT_DEVICE);
                } else {
                    Toasty.info(context,"Bluetooth tidak aktif").show();
                }
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBluetoothConnectProgressDialog.dismiss();

            printData();
        }
    };

    /**
     * function to convert integer to byte array
     * @param value
     * @return byte
     */
    protected static byte intToByteArray(int value) {
        byte[] b = ByteBuffer.allocate(4).putInt(value).array();

        for (int k = 0; k < b.length; k++) {
            System.out.println("Selva  [" + k + "] = " + "0x"
                    + UnicodeFormatter.byteToHex(b[k]));
        }

        return b[3];
    }

    public byte[] sel(int val) {
        ByteBuffer buffer = ByteBuffer.allocate(2);
        buffer.putInt(val);
        buffer.flip();
        return buffer.array();
    }

    protected void printData(){
    }

    /**
     * function to print custom position with new line at the end
     * @param msg
     * @param size
     * @param align
     */
    protected void printCustom(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }

            switch (align){
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function to print custom position without new line at the end
     * @param msg
     * @param size
     * @param align
     */
    protected void printCustomWithoutNewLine(String msg, int size, int align) {
        //Print config "mode"
        byte[] cc = new byte[]{0x1B,0x21,0x03};  // 0- normal size text
        //byte[] cc1 = new byte[]{0x1B,0x21,0x00};  // 0- normal size text
        byte[] bb = new byte[]{0x1B,0x21,0x08};  // 1- only bold text
        byte[] bb2 = new byte[]{0x1B,0x21,0x20}; // 2- bold with medium text
        byte[] bb3 = new byte[]{0x1B,0x21,0x10}; // 3- bold with large text
        try {
            switch (size){
                case 0:
                    outputStream.write(cc);
                    break;
                case 1:
                    outputStream.write(bb);
                    break;
                case 2:
                    outputStream.write(bb2);
                    break;
                case 3:
                    outputStream.write(bb3);
                    break;
            }

            switch (align){
                case 0:
                    //left align
                    outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
                    break;
                case 1:
                    //center align
                    outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                    break;
                case 2:
                    //right align
                    outputStream.write(PrinterCommands.ESC_ALIGN_RIGHT);
                    break;
            }
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function to print logo client as header
     * check image size first,
     * resize if size of image not standard
     */
    protected  void printLogoClient(){
        String storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        File file = new File(storageDir, Constants.FILENAME_LOGO);
        Bitmap originBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
        if(Constants.DEBUG){
            System.out.println("Image Width = " + originBitmap.getWidth());
            System.out.println("Image Heigh = " + originBitmap.getHeight());
        }
        final int destWidth = 300;//or the width you need
        if(originBitmap.getWidth() > destWidth){
            float aspectRatio = originBitmap.getWidth() / (float) originBitmap.getHeight();
            int height  = Math.round(destWidth / aspectRatio);
            originBitmap = Bitmap.createScaledBitmap(originBitmap, destWidth, height, true);
        }

        try {
            if(originBitmap!=null){
                byte[] command = PrinterUtils.decodeBitmap(originBitmap);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Photo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    /**
     * function to print image from given parameter
     * @param img
     */
    protected void printPhoto(int img) {
        try {
            Bitmap bmp = BitmapFactory.decodeResource(getResources(),
                    img);
            if(bmp!=null){
                byte[] command = PrinterUtils.decodeBitmap(bmp);
                outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
                printText(command);
            }else{
                Log.e("Print Memo error", "the file isn't exists");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("PrintTools", "the file isn't exists");
        }
    }

    /**
     * function to print unique code
     * the unique code is obtained from backend
     */
    protected void printUnicode(){
        try {
            outputStream.write(PrinterCommands.ESC_ALIGN_CENTER);
            printText(PrinterUtils.UNICODE_TEXT);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function to print text that given from parameter type string
     * @param msg
     */
    protected void printText(String msg) {
        try {
            // Print normal text
            outputStream.write(msg.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * function to print border
     * border generated from '-' symbol
     */
    protected void printBorder(){
        String a="";
        for(int i=0;i<42;i++){
            a+="-";
        }
        printText(a);
    }

    /**
     * function to print text that given from parameter type byte
     * @param msg
     */
    protected void printText(byte[] msg) {
        try {
            // Print normal text
            outputStream.write(msg);
            printNewLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function to set left and right align
     * @param str1
     * @param str2
     * @return
     */
    protected String leftRightAlign(String str1, String str2) {
        String ans = str1 +str2;
        if(ans.length() <42){
            int n = (41 - (str1.length() + str2.length()));
            String empty="";
            for (int i=0;i<n;i++){
                empty+=" ";
            }
            ans = str1 + empty + str2;
        }
        return ans;
    }

    /**
     * function to print new line
     */
    protected void printNewLine() {
        try {
            outputStream.write(PrinterCommands.FEED_LINE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * function to show alert error when printer failed to connect
     */
    public void showErrorPopup(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_allert);
        // Custom Android Allert Dialog Title
        dialog.setTitle("");

        Button btnTry = (Button) dialog.findViewById(R.id.btn_tryagain);
        // Click cancel to dismiss android custom dialog box
        btnTry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    /**
     * function to reset previous setting printer
     */
    public static void resetPrint() {
        try{
            outputStream.write(PrinterCommands.ESC_FONT_COLOR_DEFAULT);
            outputStream.write(PrinterCommands.FS_FONT_ALIGN);
            outputStream.write(PrinterCommands.ESC_ALIGN_LEFT);
            outputStream.write(PrinterCommands.ESC_CANCEL_BOLD);
//            outputStream.write(PrinterCommands.SELECT_FONT_A);
            outputStream.write(PrinterCommands.LF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
