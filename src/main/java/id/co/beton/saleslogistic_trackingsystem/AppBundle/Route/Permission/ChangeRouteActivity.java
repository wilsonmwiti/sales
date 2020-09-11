package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route.Permission;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import id.co.beton.saleslogistic_trackingsystem.AppBundle.MainActivity;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Model.DestinationOrder;
import id.co.beton.saleslogistic_trackingsystem.Model.Plan;
import id.co.beton.saleslogistic_trackingsystem.Model.VisitPlanRoute;
import id.co.beton.saleslogistic_trackingsystem.Model.VisitPlanRouteBoundLatLong;
import id.co.beton.saleslogistic_trackingsystem.Model.VisitPlanRouteLeg;
import id.co.beton.saleslogistic_trackingsystem.Model.VisitPlanRouteLegStep;
import id.co.beton.saleslogistic_trackingsystem.Model.VisitPlanRoutePolyline;
import id.co.beton.saleslogistic_trackingsystem.Model.VisitPlanRouteSub;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseArrayObject;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.DistanceCalculator;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelSlideListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout.PanelState;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class ChangeRouteActivity
 * request permission change route for user
 */
public class ChangeRouteActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final String TAG = ChangeRouteActivity.class.getSimpleName();
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;

    EditText etReason;
    Button btnAddRoutePoint;
    Button btnApprove;

    private GoogleMap googleMap;
    private Context context;
    private SlidingUpPanelLayout mLayout;
    DialogCustom dialogCustom;
    View view;
    public VisitPlanRouteSub rute = null;
    public int indexActive = 0;
    public List<VisitPlanRouteBoundLatLong> kordinatStartEnd;
    public List<VisitPlanRouteBoundLatLong> kordinatStartEnd2;
    DistanceCalculator distanceCalculator = new DistanceCalculator();
    public Double lat;
    public Double lon;
    public List<Marker> markerRute = new ArrayList<>();
    private List<VisitPlanRouteBoundLatLong> kordinatSE;
    Polyline line;
    private VisitPlanRoute vpRoute=null;
    LocationManager mLocationManager;
    UserUtil userUtil;
    private ApiInterface service;
    Double latAwal,lngAwal,latAkhir,lngAkhir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_route);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Change Route Permission");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        context = this;

        userUtil = UserUtil.getInstance(context);
        service = ApiClient.getInstance(context);

        dialogCustom = new DialogCustom(context);

        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);

        loadData();

        mLayout = findViewById(R.id.sliding_layout);
        mLayout.addPanelSlideListener(new PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.i(TAG, "onPanelSlide, offset " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, PanelState newState) {
                Log.i(TAG, "onPanelStateChanged " + newState);
            }
        });
        mLayout.setFadeOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(PanelState.COLLAPSED);
            }
        });

        btnAddRoutePoint = findViewById(R.id.pick_place);
        btnAddRoutePoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(Constants.DEBUG){
                    Log.wtf(TAG, "Size markerRute "+markerRute.size());
                }

                if(markerRute.size()>1){
                    try{

                        // cek index
//                        int indexna=1;
//                        if(markerRute.size()>2){
//                            indexna = 2;
//                        }
                        // index 0 untuk marker baru
                        Double lats = markerRute.get(0).getPosition().latitude;
                        Double lons = markerRute.get(0).getPosition().longitude;
                        loadMap(lats, lons);
                    }catch (NullPointerException|IndexOutOfBoundsException e){
                        Log.e(TAG,"get marker route on click add route point 2 "+ e.getMessage());
                        //Toasty.error(context,"error get marker route on click add route point 2 "+e.getMessage()).show();
                    }
                }
            }
        });

        etReason = findViewById(R.id.et_reason);

        btnApprove = findViewById(R.id.btn_approve);
        btnApprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(etReason.getText().toString().length()==0 || vpRoute==null){
                    Toasty.info(context,"Change route dan reason harus terisi", Toast.LENGTH_SHORT).show();
                }else{
                    postChangeRoute(etReason.getText().toString(), "routes");
                }
            }
        });
    }

    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l =null;
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            }

            l = mLocationManager.getLastKnownLocation(provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }

    /**
     * send data permission change route to server
     * @param reason
     * @param type
     */
    private void postChangeRoute(String reason, String type){
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(context);

        /*Create handle for the RetrofitInstance interface*/


        JsonObject gsonObject = new JsonObject();

        if(Constants.DEBUG){
            Log.wtf(TAG, vpRoute.toString());
        }

        gsonObject.addProperty("type", type);
        gsonObject.addProperty("subject", "Change Route");
        gsonObject.addProperty("description", new Gson().toJson(vpRoute));
        gsonObject.addProperty("notes", reason);
        gsonObject.addProperty("customer_code", userUtil.getNfcCodeRoute());
        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            gsonObject.addProperty("delivery_plan_id", PlanUtil.getInstance(context).getPlanId());
        }else{
            gsonObject.addProperty("visit_plan_id", PlanUtil.getInstance(context).getPlanId());
        }


        if(Constants.DEBUG){
            //print parameter
            Log.i(TAG, "PARAMETER POST EXTEND TIME" + gsonObject);
        }

        /*Show custom dialog*/
        dialogCustom.show();

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseArrayObject> call = service.permissionAlertPost("JWT "+userUtil.getJWTTOken(), gsonObject);
        call.enqueue(new Callback<ResponseArrayObject>() {
            @Override
            public void onResponse(Call<ResponseArrayObject> call, Response<ResponseArrayObject> response) {

                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    Toasty.success(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    gotoFragmentPermission();
                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }catch (Exception e){
                        dialogCustom.hidden();
                        Toasty.error(context,"Terjadi kesalahan.",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseArrayObject> call, Throwable t) {
                if(Constants.DEBUG){
                    Log.e(TAG,"failure "+call.toString());
                    Log.e(TAG,"failure "+t.toString());
                }
                Toasty.error(context,"Terjadi kesalahan",Toast.LENGTH_SHORT).show();
                dialogCustom.hidden();
            }

        });
    }

    /**
     * go back to fragment permission
     */
    private void gotoFragmentPermission() {
        try{
            Intent intent = new Intent(context, MainActivity.class);
            Bundle args = new Bundle();
            args.putInt("position",2);
            intent.putExtras(args);
            startActivity(intent);
            finish();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadData() {
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(context);
        PlanUtil planUtil = PlanUtil.getInstance(context);

        /*Create handle for the RetrofitInstance interface*/
        Call<ResponseObject> call;
        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            call = service.getDeliveryPlan("JWT " + userUtil.getJWTTOken(), planUtil.getPlanId());
        }else{
            call = service.visitPlan("JWT " + userUtil.getJWTTOken());
        }

        /*Log the URL called*/
        if (Constants.DEBUG) {
            Log.i("URL Called", call.request().url() + "");
        }

        /*Loading*/
        dialogCustom.show();

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body() != null && response.body().getError() == 0) { //if sukses
                    Gson gson = new Gson();
                    Plan plan = gson.fromJson(response.body().getData().toString(), Plan.class);

                    if (Constants.DEBUG) {
                        Log.i(TAG, plan.getRoute().toString());
                    }

                    drawRute(plan);

                    dialogCustom.hidden();
                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }catch (Exception e){
                        Toasty.error(context,"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseObject> call, Throwable t) {
                dialogCustom.hidden();
                Toasty.error(context,"Terjadi kesalahan server").show();
            }
        });
    }

    /**
     * draw route
     * @param plan
     */
    private void drawRute(Plan plan){
        String nfcCode = userUtil.getNfcCodeRoute();

        for(DestinationOrder destinationOrder : plan.getDestinationOrder()){
            if(Constants.DEBUG){
                Log.wtf(TAG, "NFC Code DO : "+destinationOrder.getNfcCode());
                Log.wtf(TAG, "NFC Code : "+nfcCode);
            }

            if(destinationOrder.getNfcCode().equals(nfcCode)){
                rute = plan.getRoute().getRoutes().get(0);

                // marker baru
                markerRute = new ArrayList<>();

                Location location = getLastKnownLocation();

                // buat marker yg bsa di drag (lokasi baru)
                LatLng markerPoint = new LatLng(location.getLatitude(), location.getLongitude());
                // marker index 0
                markerRute.add(googleMap.addMarker(new MarkerOptions()
                        .position(markerPoint)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red))
                        .title("Lokasi baru")
                ));

                markerRute.get(0).setDraggable(true);
                latAwal = rute.getLegs().get(destinationOrder.getOrder()).getStartLocation().getLat();
                lngAwal = rute.getLegs().get(destinationOrder.getOrder()).getStartLocation().getLng();

                latAkhir = rute.getLegs().get(destinationOrder.getOrder()).getEndLocation().getLat();
                lngAkhir = rute.getLegs().get(destinationOrder.getOrder()).getEndLocation().getLng();

                getRoute(rute, destinationOrder.getOrder(), true);
                setCameraCoordinationBounds(rute);
                break;
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    boolean_permission = true;
                    enableMyLocationIfPermitted();
                } else {
                    Toasty.info(this,"Please allow the permission", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setTrafficEnabled(true);
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // TODO Auto-generated method stub
                try{
//                    // cek index
//                    int indexna=1;
//                    if(markerRute.size()>2){
//                        indexna = 2;
//                    }
                    markerRute.get(0).setPosition(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude));
                }catch (IndexOutOfBoundsException|NullPointerException e ){
                    Log.e(TAG,"get marker route onmap ready 2 "+e.getMessage());
                    Toasty.error(context,"error get marker route onmap ready 2 "+e.getMessage()).show();
                }

            }

            @Override
            public void onMarkerDragStart(Marker marker) {
                // TODO Auto-generated method stub
                // Here your code
                if(Constants.DEBUG){
                    Log.i(TAG, "Dragging Start");
                }
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // TODO Auto-generated method stub
                // Toast.makeText(context, "Dragging",
                // Toast.LENGTH_SHORT).show();
                if(Constants.DEBUG){
                    Log.i(TAG, "Dragging");
                }
            }
        });

        enableMyLocationIfPermitted();
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMinZoomPreference(11);
    }

    /**
     * set zoom of display to show all route
     * @param route
     */
    private void setCameraCoordinationBounds(VisitPlanRouteSub route) {
        LatLng southwest = route.getBounds().getSouthwest().getCoordination();
        LatLng northeast = route.getBounds().getNortheast().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    private List<VisitPlanRouteBoundLatLong> getRoute(VisitPlanRouteSub rute, int index, Boolean showPolyMark){
        kordinatSE = new ArrayList<>();

        List<Polyline> polylineRute = new ArrayList<>();

        VisitPlanRouteLeg leg = rute.getLegs().get(index);
        // index 1
        markerRute.add(googleMap.addMarker(new MarkerOptions()
                .position(leg.getStartLocation().getCoordination())
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue))
                .title(leg.getStartAddress())
        ));

        kordinatSE.add(leg.getStartLocation());

        if(showPolyMark) {
            // index 2 kalo di show
            markerRute.add(googleMap.addMarker(new MarkerOptions()
                    .position(leg.getEndLocation().getCoordination())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue))
                    .title(leg.getEndAddress())
            ));

        }
        kordinatSE.add(leg.getEndLocation());

        // create line
        if(showPolyMark){
            for (VisitPlanRouteLegStep step : leg.getSteps()) {
                VisitPlanRoutePolyline overviewPolylines = step.getPolyline();
                String encodedString = overviewPolylines.getPoints();
                List<LatLng> listPolyline = decodePoly(encodedString);

                if (Constants.DEBUG) {
//                Log.i(TAG, "Total Polyline : " + listPolyline.size());
                }

                PolylineOptions options = new PolylineOptions().width(5).color(Color.rgb(57, 147, 213)).geodesic(true);
                for (int z = 0; z < listPolyline.size(); z++) {
                    LatLng point = listPolyline.get(z);
                    options.add(point);
                }
                polylineRute.add(googleMap.addPolyline(options));
            }
        }

        return kordinatSE;
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void parseDataRute(VisitPlanRoute visitPlanRoute){
        this.vpRoute = visitPlanRoute;
    }

    /**
     * load map with new location
     * @param newLat
     * @param newLon
     */
    private void loadMap(Double newLat, Double newLon) {
        /*Get user info*/
        UserUtil userUtil = UserUtil.getInstance(context);

        /*Create handle for the RetrofitInstance interface*/

        /*Make url*/
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(markerRute.get(1).getPosition().latitude)); //markerRute.get(1).getPosition().latitude
        urlString.append(",");
        urlString.append(Double.toString(markerRute.get(1).getPosition().longitude)); //markerRute.get(1).getPosition().longitude
        urlString.append("&destination=");// to
        urlString.append(Double.toString(markerRute.get(2).getPosition().latitude)); //markerRute.get(2).getPosition().latitude
        urlString.append(",");
        urlString.append(Double.toString(markerRute.get(2).getPosition().longitude)); //markerRute.get(2).getPosition().longitude
        urlString.append("&waypoints=");// waypoint (lokasi baru)
        urlString.append(Double.toString(newLat));
        urlString.append(",");
        urlString.append(Double.toString(newLon));
        urlString.append("&sensor=false&mode=driving&alternatives=true&language=id&key="+Constants.API_GOOGLE_MAP);

        /*Call the method with parameter in the interface to get the data*/
        Call<VisitPlanRoute> call = service.getRute(urlString.toString());

        /*Log the URL called*/
        if (Constants.DEBUG) {
            Log.i("URL Called", call.request().url() + "");
        }

        /*Loading*/
        dialogCustom.show();

        /*Callback*/
        call.enqueue(new Callback<VisitPlanRoute>() {
            @Override
            public void onResponse(Call<VisitPlanRoute> call, Response<VisitPlanRoute> response) {
                if (response.body() != null) { //if sukses

                    VisitPlanRoute visitPlanRoute = response.body();

                    parseDataRute(visitPlanRoute);

                    // buat marker lagi
                    try{
                        if(visitPlanRoute.getRoutes().size()>0){
                            VisitPlanRouteSub route = visitPlanRoute.getRoutes().get(0);
                            googleMap.clear();

                            int legCount = route.getLegs().size();

                            Log.wtf(TAG, "Leg Count : "+legCount);

                            ListView lvRute = findViewById(R.id.lv_rute);
                            final List<String> listRute = new ArrayList<>();

                            for (int index = 0; index < legCount; index++) {
                                VisitPlanRouteLeg leg = route.getLegs().get(index);
                                // start
                                if(index==1){ // buat yg tengah
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(leg.getStartLocation().getCoordination())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red))
                                            .title(leg.getStartAddress())
                                    ).setDraggable(true);
                                }else{
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(leg.getStartLocation().getCoordination())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue))
                                            .title(leg.getStartAddress())
                                    );
                                }
                                listRute.add(leg.getStartAddress());

                                if (index == legCount - 1) {
                                    // end
                                    googleMap.addMarker(new MarkerOptions()
                                            .position(leg.getEndLocation().getCoordination())
                                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue))
                                            .title(leg.getEndAddress())
                                    );
                                    listRute.add(leg.getEndAddress());
                                }
                                // create line
                                VisitPlanRoutePolyline overviewPolylines = route.getOverviewPolyline();
                                String encodedString = overviewPolylines.getPoints();
                                List<LatLng> listPolyline = decodePoly(encodedString);

                                PolylineOptions options = new PolylineOptions().width(5).color(Color.rgb(57, 147, 213)).geodesic(true);
                                for (int z = 0; z < listPolyline.size(); z++) {
                                    LatLng point = listPolyline.get(z);
                                    options.add(point);
                                }
                                line = googleMap.addPolyline(options);
                            }

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                                    context,
                                    android.R.layout.simple_list_item_1,
                                    listRute ){
                                @Override
                                public View getView(int position, View convertView, ViewGroup parent) {
                                    TextView textView = (TextView) super.getView(position, convertView, parent);
                                    // Set
                                    textView.setText(Html.fromHtml(listRute.get(position)));
                                    return textView;
                                }
                            };
                            lvRute.setAdapter(arrayAdapter);
                        }else{
                            Toasty.error(context, visitPlanRoute.getErrorMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }catch (Exception e){
                        Toasty.error(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    dialogCustom.hidden();
                }else{
                    try{
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        Toasty.error(context,jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }catch (Exception e){
                        Toasty.error(context,"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
                        dialogCustom.hidden();
                    }
                }

            }

            @Override
            public void onFailure(Call<VisitPlanRoute> call, Throwable t) {
                dialogCustom.hidden();
                Toasty.error(context, "Koneksi Internet Bermasalah",
                        Toast.LENGTH_SHORT).show();
                if(Constants.DEBUG){
                    Log.wtf(TAG, "Koneksi Internet Bermasalah");
                }
            }
        });
    }

    /**
     * function to decode waypoint to list of Latitude Longitude (point)
     * @param encoded
     * @return
     */
    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }
}
