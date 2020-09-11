package id.co.beton.saleslogistic_trackingsystem.AppBundle.Route;


import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.Adapter.route.RouteMapAdapter;
import id.co.beton.saleslogistic_trackingsystem.Configuration.Constants;
import id.co.beton.saleslogistic_trackingsystem.Customlayout.DialogCustom;
import id.co.beton.saleslogistic_trackingsystem.Interfaces.OnFragmentInteractionListener;
import id.co.beton.saleslogistic_trackingsystem.Model.Destination;
import id.co.beton.saleslogistic_trackingsystem.Model.DestinationOrder;
import id.co.beton.saleslogistic_trackingsystem.Model.Plan;
import id.co.beton.saleslogistic_trackingsystem.Model.StepRouteMap;
import id.co.beton.saleslogistic_trackingsystem.Model.VisitPlanRoute;
import id.co.beton.saleslogistic_trackingsystem.Model.VisitPlanRouteBoundLatLong;
import id.co.beton.saleslogistic_trackingsystem.Model.VisitPlanRouteLeg;
import id.co.beton.saleslogistic_trackingsystem.Model.VisitPlanRouteLegStep;
import id.co.beton.saleslogistic_trackingsystem.Model.VisitPlanRoutePolyline;
import id.co.beton.saleslogistic_trackingsystem.Model.VisitPlanRouteSub;
import id.co.beton.saleslogistic_trackingsystem.R;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiClient;
import id.co.beton.saleslogistic_trackingsystem.Rest.ApiInterface;
import id.co.beton.saleslogistic_trackingsystem.Rest.ResponseObject;
import id.co.beton.saleslogistic_trackingsystem.Utils.DistanceCalculator;
import id.co.beton.saleslogistic_trackingsystem.Utils.KonversiWaktu;
import id.co.beton.saleslogistic_trackingsystem.Utils.PlanUtil;
import id.co.beton.saleslogistic_trackingsystem.Utils.UserUtil;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class MapFragment
 *
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = MapFragment.class.getSimpleName();

    private OnFragmentInteractionListener mListener;
    private Context context;
    DialogCustom dialogCustom;
    Polyline line;
    Location location;
    DistanceCalculator distanceCalculator = new DistanceCalculator();
    private GoogleMap googleMap;
    private SupportMapFragment mMapFragment;
    private LatLngBounds.Builder builder;
    private CameraUpdate cameraUpdate;

    private SlidingUpPanelLayout mLayout;
    private LinearLayout llLayoutMap;

    private ArrayList<StepRouteMap> stepRouteMaps;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_PERMISSIONS = 100;
    boolean boolean_permission;
    SharedPreferences mPref;
    SharedPreferences.Editor medit;
    Double latitude,longitude;
    Geocoder geocoder;
    FloatingActionButton btnGps;
    private FloatingActionButton refreshMap;
    List<Marker> markers = new ArrayList<>();
    List<DestinationOrder> newDestinationOrder;
    List<Destination> destinations = new ArrayList<>();
    View view;
    public Plan plan=null;
    public VisitPlanRouteSub rute=null;
    public VisitPlanRouteSub changeRute=null;
    public int indexActive=0;
    public Boolean flagRute=true;
    public List<VisitPlanRouteBoundLatLong> kordinatStartEnd;
    public List<VisitPlanRouteBoundLatLong> kordinatStartEnd2;
    public List<VisitPlanRouteBoundLatLong> kordinatStartEndCR;
    public List<VisitPlanRouteBoundLatLong> kordinatStartEnd2CR;
    public ListView lvRute;
    private UserUtil userUtil;
    private PlanUtil planUtil;
    RouteMapAdapter routeMapAdapter;
    LayoutInflater inflater;

    // public MapFragment() {
    // }

    public static MapFragment newInstance(int position) {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_visit_plan, container, false);
        view.setTag(TAG);

        // ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMapAsync(this);
        mMapFragment = (SupportMapFragment) (getChildFragmentManager().findFragmentById(R.id.map));
        mMapFragment.getMapAsync(this);

        context = container.getContext();
        userUtil = UserUtil.getInstance(context);
        planUtil = PlanUtil.getInstance(context);
        dialogCustom = new DialogCustom(context);

        this.inflater =inflater;

        loadData(true);

        llLayoutMap = view.findViewById(R.id.ll_layout_map);
        refreshMap = (FloatingActionButton) view.findViewById(R.id.refresh_map);
        refreshMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData(true);
            }
        });

        geocoder = new Geocoder(getContext(), Locale.getDefault());
        mPref = PreferenceManager.getDefaultSharedPreferences(context);
        medit = mPref.edit();
        fn_permission();

        mLayout = view.findViewById(R.id.sl_map_visit_plan);

        return view;
    }

    /**
     * draw changes route,
     * if sales request permission of change route
     * @param plan
     */
    private void drawChangeRoute(Plan plan){
        String nfcCode = userUtil.getNfcCodeRoute();
        try{
            if(plan!=null && plan.getChangeRoute()!=null){

                Collections.reverse(plan.getChangeRoute());
                for(VisitPlanRoute visitPlanRoute : plan.getChangeRoute()){

                    if(visitPlanRoute.getCustomerCode().equals(nfcCode)){
                        changeRute = visitPlanRoute.getRoutes().get(0);

                        int legCountCR = changeRute.getLegs().size();

                        if (Constants.DEBUG) {
                            Log.i(TAG, "Leg Count CR : " + legCountCR);
                        }

                        for(int iA=0; iA<legCountCR; iA++){
                            getRoute(changeRute, iA, true, false);
                        }
                        break;
                    }

                }

            }}catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * draw route to customer location
     * @param plan
     */
    private void drawRute(Plan plan){
        String nfcCode = userUtil.getNfcCodeRoute();

        try{
            if(plan!=null && plan.getDestinationOrder()!=null){
                for(DestinationOrder destinationOrder : plan.getDestinationOrder()){
                    if(Constants.DEBUG){
                        Log.i(TAG, "NFC Code DO : "+destinationOrder.getNfcCode());
                        Log.i(TAG, "NFC Code : "+nfcCode);
                    }

                    if(destinationOrder.getNfcCode().equals(nfcCode)){
                        //googleMap.clear();

                        rute = plan.getRoute().getRoutes().get(0);

                        getRoute(rute, destinationOrder.getOrder(), true, true);

                        break;
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * load data visit/delivery plan
     * @param showLoading
     */
    private void loadData(boolean showLoading){
        /*Create handle for the RetrofitInstance interface*/
        ApiInterface service = ApiClient.getInstance(context);

        /*Call the method with parameter in the interface to get the data*/
        Call<ResponseObject> call =null;
        if(userUtil.getRoleUser().equals(Constants.ROLE_DRIVER)){
            call = service.getDeliveryPlan("JWT "+userUtil.getJWTTOken(), planUtil.getPlanId());
        }else{
            call = service.visitPlan("JWT "+userUtil.getJWTTOken());
        }

        /*Log the URL called*/
        if(Constants.DEBUG){
            System.out.println("Loading Page Google Map");
            Log.i("URL Called", call.request().url() + "");
        }

        if(showLoading){
            /*Loading*/
            dialogCustom.show();
        }

        /*Callback*/
        call.enqueue(new Callback<ResponseObject>() {
            @Override
            public void onResponse(Call<ResponseObject> call, Response<ResponseObject> response) {
                if (response.body()!=null && response.body().getError()==0){ //if sukses
                    try{
                        // Gson gson =new Gson();
                        // plan = gson.fromJson(response.body().getData().toString(), Plan.class);

                        if(response.body().getData().get("is_use_route").getAsInt()==1){
                            // using route
                            Gson gson = new Gson();
                            plan = gson.fromJson(response.body().getData().toString(), Plan.class);
                        } else {
                            // not using route <exclude key route>
                            GsonBuilder builder = new GsonBuilder();
                            builder.excludeFieldsWithoutExposeAnnotation();
                            Gson gson = builder.create();
                            plan = gson.fromJson(gson.toJson(response.body().getData()), Plan.class);
                        }

                        if(Constants.DEBUG){
                            Log.i(TAG, plan.getRoute().toString());
                            Log.wtf(TAG, plan.getDestinationOrder().get(0).getOrder().toString());
                        }
                        if(PlanUtil.getInstance(context).isUsingRoute()){
                            // change route
                            if(plan.getChangeRoute()!=null && plan.getChangeRoute().size()>0){
                                changeRute = plan.getChangeRoute().get(0).getRoutes().get(0);
                            }

                            if(view!=null){
                                updateLayout(true);

                                if(plan.getRoute().getRoutes().size()>0){
                                    rute = plan.getRoute().getRoutes().get(0);
                                }

                                // set camera area
                                setCameraCoordinationBounds(rute);
                            }
                            googleMap.clear();
                            googleMap.setTrafficEnabled(true);
                            // draw changerute berdasarkan nfc
                            drawChangeRoute(plan);
                            drawRute(plan);
                        } else {
                            if(view != null){
                                updateLayout(false);
                            }
                            googleMap.clear();
                            googleMap.setTrafficEnabled(true);

                            markers = new ArrayList<>();
                            destinations = new ArrayList<>();
                            if(!PlanUtil.getInstance(context).isPlanCustome()){
                                drawMarkerBranch();
                            }
                            drawMarkerCustomer();
                            boundMarker(markers);
                        }

                        dialogCustom.hidden();
                    }catch (Exception e){
                        e.printStackTrace();
                        System.out.println("Gagal Proses Data");
                        dialogCustom.hidden();
                    }
                }else{
                    try{
                        dialogCustom.hidden();
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        String message = jsonObject.getString("message");
                        if(message.equals("This visit plan doesn't exist")){
                            message = "visit plan belum dibuat";
                        }
                        Toasty.error(getContext(), message, Toast.LENGTH_SHORT).show();
                    }catch (Exception e){
                        dialogCustom.hidden();
                        Toasty.error(getContext(),"Gagal mengambil data.", Toast.LENGTH_SHORT).show();
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
     * update page map
     * if use route, show route on map and steps route
     * if not using route, show marker customer
     * @param useRoute
     */
    private void updateLayout(boolean useRoute){
        if(useRoute){
            lvRute = view.findViewById(R.id.lv_rutedilalui);
            lvRute.setVisibility(View.VISIBLE);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 0, 50);
            llLayoutMap.setLayoutParams(layoutParams);

            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mMapFragment.getView().getLayoutParams();
            params.bottomMargin = 50;
            mMapFragment.getView().setLayoutParams(params);
        } else {
            lvRute = view.findViewById(R.id.lv_rutedilalui);
            lvRute.setVisibility(View.GONE);

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams
                    (RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            layoutParams.setMargins(0, 0, 0, 0);
            llLayoutMap.setLayoutParams(layoutParams);

            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mMapFragment.getView().getLayoutParams();
            params.bottomMargin = 0;
            mMapFragment.getView().setLayoutParams(params);
        }
    }

    /**
     * draw custom marker
     * @param flag
     * @param markerText
     * @return bitmap image
     */
    private Bitmap drawPinMarker(int flag, String markerText){
        Bitmap bitmap = null;
        try{
            int id;
            if(flag==1){
                id = R.layout.custom_marker_branch;
            } else {
                id = R.layout.custom_marker;
            }
            LinearLayout linearLayout = (LinearLayout) this.getLayoutInflater().inflate(id, null, false);
            linearLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
            linearLayout.layout(0, 0, linearLayout.getMeasuredWidth(), linearLayout.getMeasuredHeight());

            TextView tv_cust_code = linearLayout.findViewById(R.id.tv_cust_marker);
            tv_cust_code.setText(markerText);
            linearLayout.setDrawingCacheEnabled(true);
            linearLayout.buildDrawingCache();
            bitmap = linearLayout.getDrawingCache();
        } catch(Exception e){
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * draw marker branch
     */
    private void drawMarkerBranch(){
        if(plan!=null){
            if(plan.getStartRouteBranchId().equals(plan.getEndRouteBranchId())){ // start & end in the same coordinate
                Bitmap bitmap = drawPinMarker(1, "Start - End");
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(plan.getStartRouteBranch().getLat(), plan.getStartRouteBranch().getLng()))
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                        .title(plan.getStartRouteBranch().getAddress())
                        .visible(true);
                Marker marker = googleMap.addMarker(markerOptions);
                markers.add(marker);

            } else {
                // draw marker Start
                Bitmap bitmapStart = drawPinMarker(1, "Start");
                MarkerOptions markerOptionsStart = new MarkerOptions()
                        .position(new LatLng(plan.getStartRouteBranch().getLat(), plan.getStartRouteBranch().getLng()))
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapStart))
                        .title(plan.getStartRouteBranch().getAddress())
                        .visible(true);
                Marker markerStart = googleMap.addMarker(markerOptionsStart);
                markers.add(markerStart);

                // draw marker End
                Bitmap bitmapEnd = drawPinMarker(1, "End");
                MarkerOptions markerOptionsEnd = new MarkerOptions()
                        .position(new LatLng(plan.getEndRouteBranch().getLat(), plan.getEndRouteBranch().getLng()))
                        .icon(BitmapDescriptorFactory.fromBitmap(bitmapEnd))
                        .title(plan.getEndRouteBranch().getAddress())
                        .visible(true);
                Marker markerEnd = googleMap.addMarker(markerOptionsEnd);
                markers.add(markerEnd);
            }
        }
    }

    /**
     * draw marker customer origin and customer new (if added)
     */
    private void drawMarkerCustomer(){
        if(plan!=null){
            // check destination origin
            if(plan.getDestination()!=null){
                destinations.addAll(plan.getDestination());
                for(int i=0; i<destinations.size(); i++){
                    Bitmap bitmap = drawPinMarker(2, destinations.get(i).getCustomerCode());
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(destinations.get(i).getLat(), destinations.get(i).getLng()))
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            .title(destinations.get(i).getAddress())
                            .visible(true);
                    Marker marker = googleMap.addMarker(markerOptions);
                    markers.add(marker);
                }
            }

            // check destination new
            if(plan.getDestinationNew()!=null){
                destinations.addAll(plan.getDestinationNew());
                for(int i=0; i<destinations.size(); i++){
                    Bitmap bitmap = drawPinMarker(2, destinations.get(i).getCustomerCode());
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(new LatLng(destinations.get(i).getLat(), destinations.get(i).getLng()))
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))
                            .title(destinations.get(i).getAddress())
                            .visible(true);
                    Marker marker = googleMap.addMarker(markerOptions);
                    markers.add(marker);
                }
            }
            // boundMarker(markers);
        }
    }

    /**
     * set position maps, make all marker visible
     * @param markerList
     */
    private void boundMarker(List<Marker> markerList){
        if(markerList.size()>0){
            builder = new LatLngBounds.Builder();
            for (Marker m : markerList) {
                builder.include(m.getPosition());
            }
            /**initialize the padding for map boundary*/
            int padding = 150;
            /**create the bounds from latlngBuilder to set into map camera*/
            LatLngBounds bounds = builder.build();
            /**create the camera with bounds and padding to set into map*/
            cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            /**call the map call back to know map is loaded or not*/
            googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    /**set animated zoom camera into map*/
                    if(googleMap!=null){
                        googleMap.animateCamera(cameraUpdate);
                    }
                }
            });
        }

    }

    private GoogleMap.OnMyLocationChangeListener onMyLocationChangeListener =
            new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    if(plan!=null){
                        //drawChangeRoute(plan);
                        //drawRute(plan);
                    }
                }
            };

    private List<VisitPlanRouteBoundLatLong> getRoute(VisitPlanRouteSub rute, int index, Boolean showPolyMark, Boolean isRoute){
        try{
            int duration=0;
            double distance=0;

            List<VisitPlanRouteBoundLatLong> kordinatSE = new ArrayList<>();

            List<Marker> markerRute = new ArrayList<>();
            List<Polyline> polylineRute = new ArrayList<>();

            VisitPlanRouteLeg leg = rute.getLegs().get(index);


            if(isRoute){
                markerRute.add(googleMap.addMarker(new MarkerOptions()
                        .position(leg.getStartLocation().getCoordination())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue))
                        .title(leg.getStartAddress())
                ));
                if(showPolyMark) {
                    markerRute.add(googleMap.addMarker(new MarkerOptions()
                            .position(leg.getEndLocation().getCoordination())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_blue))
                            .title(leg.getEndAddress())
                    ));
                }
                userUtil.setKoordinarAwal(leg.getStartLocation().getLat(),leg.getStartLocation().getLng());
                userUtil.setKoordinarAkhir(leg.getEndLocation().getLat(),leg.getEndLocation().getLng());
            }else{ // change route
                if(index==0){
                    userUtil.setKoordinarAwal(leg.getStartLocation().getLat(),leg.getStartLocation().getLng());
                    userUtil.setKoordinarTengah(leg.getEndLocation().getLat(),leg.getEndLocation().getLng());
                }else if(index==1){
                    userUtil.setKoordinarTengah(leg.getStartLocation().getLat(),leg.getStartLocation().getLng());
                    userUtil.setKoordinarAkhir(leg.getEndLocation().getLat(),leg.getEndLocation().getLng());
                }

                markerRute.add(googleMap.addMarker(new MarkerOptions()
                        .position(leg.getStartLocation().getCoordination())
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red))
                        .title(leg.getStartAddress())
                ));
                if(showPolyMark) {
                    markerRute.add(googleMap.addMarker(new MarkerOptions()
                            .position(leg.getEndLocation().getCoordination())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_red))
                            .title(leg.getEndAddress())
                    ));
                }
            }

            kordinatSE.add(leg.getStartLocation());
            kordinatSE.add(leg.getEndLocation());

            // hitung durasi dalam detik
            duration = leg.getDuration().getValue();

            // hitungan distance dalam km
            distance = leg.getDistance().getValue()/1000;

            // create line
            if(showPolyMark){
                for (VisitPlanRouteLegStep step : leg.getSteps()) {
                    VisitPlanRoutePolyline overviewPolylines = step.getPolyline();
                    String encodedString = overviewPolylines.getPoints();
                    List<LatLng> listPolyline = decodePoly(encodedString);

                    if(isRoute) {
                        PolylineOptions options = new PolylineOptions().width(5).color(Color.rgb(57, 147, 213)).geodesic(true);
                        for (int z = 0; z < listPolyline.size(); z++) {
                            LatLng point = listPolyline.get(z);
                            options.add(point);
                        }
                        polylineRute.add(googleMap.addPolyline(options));
                    }else{
                        PolylineOptions options = new PolylineOptions().width(5).color(Color.rgb(255, 0, 0)).geodesic(true);
                        for (int z = 0; z < listPolyline.size(); z++) {
                            LatLng point = listPolyline.get(z);
                            options.add(point);
                        }
                        polylineRute.add(googleMap.addPolyline(options));
                    }
                }
            }

            // buat rute asli nya aja
            if(isRoute){
                // durasi
                KonversiWaktu konversiWaktu = new KonversiWaktu(duration);
                // jarak
                Locale fmtLocale = new Locale("id");
                NumberFormat formatter = NumberFormat.getInstance(fmtLocale);
                formatter.setMaximumFractionDigits(2);
                if(Constants.DEBUG) {
                    Log.d(TAG, formatter.format(distance));
                }
                try {
                    TextView estimasi = getActivity().findViewById(R.id.tv_estimasi);
                    estimasi.setText(leg.getDistance().getText()+" - "+ konversiWaktu.getWaktu());
                }catch (NullPointerException e){
                    Log.e(TAG,e.getMessage());
                }

                // init array
                stepRouteMaps = new ArrayList<>();

                List<VisitPlanRouteLegStep> stepList = leg.getSteps();

                for(Integer i=0; i<stepList.size(); i++){
                    StepRouteMap stepRouteMap = new StepRouteMap();
                    stepRouteMap.setAlamat(stepList.get(i).getHtmlInstructions());
                    stepRouteMap.setDistanceText(stepList.get(i).getDistance().getText());
                    stepRouteMap.setDistanceValue(stepList.get(i).getDistance().getValue());
                    stepRouteMap.setDurationText(stepList.get(i).getDuration().getText());
                    stepRouteMap.setDurationValue(stepList.get(i).getDuration().getValue());
                    if(stepList.get(i).getManeuver() != null){
                        stepRouteMap.setManeuver(stepList.get(i).getManeuver());
                    }else{
                        stepRouteMap.setManeuver("");
                    }

                    stepRouteMaps.add(stepRouteMap);
                }

                //adapter
                routeMapAdapter = new RouteMapAdapter(getActivity(),1, stepRouteMaps);

                lvRute.setAdapter(routeMapAdapter);
            }

            return kordinatSE;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null;
    }

    /**
     * set page map to make all marker visible
     * @param route
     */
    private void setCameraCoordinationBounds(VisitPlanRouteSub route) {
        LatLng southwest = route.getBounds().getSouthwest().getCoordination();
        LatLng northeast = route.getBounds().getNortheast().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        if(googleMap!=null){
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
            googleMap.setTrafficEnabled(true);
        }
    }

    /**
     * decode polyline from waypoint google to list latitude longitude
     * @param encoded
     * @return list latitude longitude
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

    /**
     * permission to access location device
     */
    private void fn_permission() {
        if ((ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION))) {

            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                }, REQUEST_PERMISSIONS);
            }
        } else {
            boolean_permission = true;
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
                    Toasty.info(getContext(),"Please allow the permission", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(googleMap != null){ //prevent crashing if the map doesn't exist yet (eg. on starting activity)
            googleMap.clear();
            googleMap.setTrafficEnabled(true);
            if(PlanUtil.getInstance(context).isUsingRoute()){
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                drawChangeRoute(plan);
                drawRute(plan);
                routeMapAdapter = new RouteMapAdapter(getActivity(),1, null);
            } else {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
                if(!PlanUtil.getInstance(context).isPlanCustome()){
                    drawMarkerBranch();
                }
                drawMarkerCustomer();
                boundMarker(markers);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        //context.unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.setOnMyLocationButtonClickListener(onMyLocationButtonClickListener);
        googleMap.setOnMyLocationClickListener(onMyLocationClickListener);
        googleMap.setOnMyLocationChangeListener(onMyLocationChangeListener);
        enableMyLocationIfPermitted();

        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.setMinZoomPreference(11);
        googleMap.setTrafficEnabled(true);
    }

    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (googleMap != null) {
            googleMap.setMyLocationEnabled(true);
        }
    }

    private GoogleMap.OnMyLocationButtonClickListener onMyLocationButtonClickListener =
            new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
                    googleMap.setMinZoomPreference(15);
                    return false;
                }
            };

    private GoogleMap.OnMyLocationClickListener onMyLocationClickListener =
            new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {

                    googleMap.setMinZoomPreference(12);

                    if(Constants.DEBUG){
                        Log.i(TAG, "Lat : "+ location.getLatitude()+", Long : "+location.getLongitude());
                        CircleOptions circleOptions = new CircleOptions();
                        circleOptions.center(new LatLng(location.getLatitude(), location.getLongitude()));
                        circleOptions.radius(Constants.TOLERANSI_JARAK);
                        circleOptions.fillColor(Color.RED);
                        circleOptions.strokeWidth(6);
                        googleMap.addCircle(circleOptions);
                    }
                }
            };


}
