package id.co.beton.saleslogistic_trackingsystem.Developer;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import id.co.beton.saleslogistic_trackingsystem.R;

/**
 * Class TapNfcSimulationService
 * service for develop Simulation Tap NFC
 */
public class TapNfcSimulationService extends Service {
    private WindowManager mWindowManager;
    private View mFloatingWidget;
    private EditText nfcCodeInput;
    private TextView tapLocation;
    private Button tapNfc, tapNfcCustomer;
    private int type = 0;
    final public static int TAP_OFFICE = 1;
    final public static int TAP_CUSTOMER_IN = 2;
    final public static int TAP_CUSTOMER_OUT = 3;
    public int TAP_TYPE = 2;
    private String CUSTOMER_CODE;
    private String ROLE;


    public TapNfcSimulationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        type = intent.getIntExtra("type", 0);
        TAP_TYPE = intent.getIntExtra("tap_type", 0);
        if (intent.hasExtra("customer_code")) {
            CUSTOMER_CODE = intent.getStringExtra("customer_code");
            Log.e("TapNfcSimulatorService", "Customer Code : " + String.valueOf(CUSTOMER_CODE));
        }

        if (intent.hasExtra("role")) {
            ROLE = intent.getStringExtra("role");
        }
        Log.e("TapNfcSimulatorService", "Masuk On Start Command");
        Log.e("TapNfcSimulatorService", "Type : " + String.valueOf(type));
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatingWidget = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        final Context context = this;

        Log.e("TapNfcSimulationService", "Intent : " + type);
        tapNfc = (Button) mFloatingWidget.findViewById(R.id.tap_nfc);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                PixelFormat.TRANSLUCENT);


        final WindowManager.LayoutParams paramsSecond = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);


        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;
        params.dimAmount = (float) 0.5;

        paramsSecond.gravity = Gravity.TOP | Gravity.LEFT;
        paramsSecond.x = 0;
        paramsSecond.y = 100;


        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingWidget, paramsSecond);

        final View collapsedView = mFloatingWidget.findViewById(R.id.collapse_view);
        final View expandedView = mFloatingWidget.findViewById(R.id.expanded_container);
        final View expandedViewCustomer = mFloatingWidget.findViewById(R.id.expanded_container_customer);
        nfcCodeInput = (EditText) mFloatingWidget.findViewById(R.id.nfc_code_input);
        tapLocation = (TextView) mFloatingWidget.findViewById(R.id.tap_location);
        tapNfcCustomer = (Button) mFloatingWidget.findViewById(R.id.tap_nfc_customer);

        ImageView closeButtonCollapsed = (ImageView) mFloatingWidget.findViewById(R.id.close_btn);
        ImageView closeButtonCollapsedCustomer = (ImageView) mFloatingWidget.findViewById(R.id.close_button_customer);

        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
            }
        });
        ImageView closeButton = (ImageView) mFloatingWidget.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWindowManager.updateViewLayout(mFloatingWidget, paramsSecond);
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
        });

        closeButtonCollapsedCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWindowManager.updateViewLayout(mFloatingWidget, paramsSecond);
                collapsedView.setVisibility(View.VISIBLE);
                expandedViewCustomer.setVisibility(View.GONE);
            }
        });

        mFloatingWidget.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = params.x;
                        initialY = params.y;

                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                collapsedView.setVisibility(View.GONE);
                                if (TAP_TYPE == TapNfcSimulationService.TAP_OFFICE || TAP_TYPE == 0) {
                                    expandedView.setVisibility(View.VISIBLE);
                                } else {
                                    if (TAP_TYPE == TapNfcSimulationService.TAP_CUSTOMER_OUT) {
                                        nfcCodeInput.setVisibility(View.GONE);
                                        tapLocation.setText(CUSTOMER_CODE);
                                        tapNfcCustomer.setText("Tap Out");
                                    }
                                    mWindowManager.updateViewLayout(mFloatingWidget, params);
                                    expandedViewCustomer.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        paramsSecond.x = initialX + (int) (event.getRawX() - initialTouchX);
                        paramsSecond.y = initialY + (int) (event.getRawY() - initialTouchY);
                        if (TAP_TYPE == TapNfcSimulationService.TAP_CUSTOMER_OUT || TAP_TYPE == TapNfcSimulationService.TAP_CUSTOMER_IN) {
                            expandedViewCustomer.setVisibility(View.GONE);
                        } else {
                            expandedView.setVisibility(View.GONE);
                        }
                        collapsedView.setVisibility(View.VISIBLE);
                        mWindowManager.updateViewLayout(mFloatingWidget, paramsSecond);
                        return true;
                }
                return false;
            }
        });
        tapNfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TapNfcSimulatorService", "Tap Start / Tap Stop");
                Intent i = new Intent(context.getApplicationContext(), NfcSimulationReceiver.class);
                i.putExtra("type", type);
                context.sendBroadcast(i);
            }
        });

        tapNfcCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Role", ROLE);
                if (TAP_TYPE == TapNfcSimulationService.TAP_CUSTOMER_IN || TAP_TYPE == TapNfcSimulationService.TAP_CUSTOMER_OUT) {
                    Intent i = new Intent(context.getApplicationContext(), NfcSimulationReceiver.class);
                    if (ROLE.equals("sales")) {
                        i.putExtra("role", NfcSimulationReceiver.IS_SALES);
                    } else {
                        i.putExtra("role", NfcSimulationReceiver.IS_DRIVER);
                    }
                    if (TAP_TYPE == TapNfcSimulationService.TAP_CUSTOMER_IN) {
                        Log.e("TapNfcSimulatorService", "Tap IN");
                        i.putExtra("type", NfcSimulationReceiver.TAP_IN);
                        i.putExtra("customer_code", nfcCodeInput.getText().toString());
                    } else {
                        Log.e("TapNfcSimulatorService", "Tap Out");
                        i.putExtra("type", NfcSimulationReceiver.TAP_OUT);
                        i.putExtra("customer_code", CUSTOMER_CODE);
                    }
                    context.sendBroadcast(i);
                } else {
                    Toast.makeText(context, "Jenis TAP NFC tidak diketahui", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isViewCollapsed() {
        return mFloatingWidget == null || mFloatingWidget.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingWidget != null) mWindowManager.removeView(mFloatingWidget);
    }
}