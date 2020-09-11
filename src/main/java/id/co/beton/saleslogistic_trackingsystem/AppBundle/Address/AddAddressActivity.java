package id.co.beton.saleslogistic_trackingsystem.AppBundle.Address;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AutocompleteResultType;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.R;

/**
 * Class AddAddressActivity
 * select new address location on map
 */
public class AddAddressActivity extends AppCompatActivity implements
        OnMapReadyCallback,
            GoogleMap.OnMarkerDragListener {

    private static final String TAG = AddAddressActivity.class.getSimpleName();
    private GoogleMap mMap;
    String customerCode;
    String customerName;
    private Context context;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    LocationManager mLocationManager;
    private static String address;
    private static LatLng latLng;
    private double lat=0,lng=0;
    private Button btnAddAddress;
    private TextView textAddress;
    PlaceAutocompleteFragment autocompleteFragment;

    @BindView(R.id.autocomplete)
    PlacesAutocompleteTextView mAutocomplete;

    private Double latitude;
    private Double longitude;

    private void setLocation(Double latitude, Double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    private Double getLatitude(){
        return latitude;
    }

    private Double getLongitude(){
        return longitude;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        customerCode = getIntent().getStringExtra("cust_code");
        customerName = getIntent().getStringExtra("cust_name");

        setTitle(customerName);
        context = this;
        address="";
        btnAddAddress = (Button) findViewById(R.id.btn_add_address);
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if (address.isEmpty()) {
                        Toasty.info(context, "Silahkan pilih alamat atau drag icon terlebih dahulu").show();
                    } else {
                        lat = latLng.latitude;
                        lng = latLng.longitude;

                        Intent intent = new Intent();
                        intent.putExtra("lat", lat);
                        intent.putExtra("lng", lng);
                        intent.putExtra("new_address", address);
                        setResult(RESULT_OK, intent);
                        finish();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        /* test code */

//        setContentView(R.layout.activity_places_autocomplete);
        ButterKnife.bind(this);

        mAutocomplete = (PlacesAutocompleteTextView) findViewById(R.id.autocomplete);

        mAutocomplete.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final com.seatgeek.placesautocomplete.model.Place place) {

                mAutocomplete.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(final PlaceDetails details) {
                        Double lat = details.geometry.location.lat;
                        Double lng = details.geometry.location.lng;
                        setLocation(lat, lng);

                        String placeUrl = details.url;

                        setLocation(details.geometry.location.lat, details.geometry.location.lng);

                        Toasty.info(context, "location \n lat: " + String.valueOf(getLatitude())
                                + ", long: " + String.valueOf(getLongitude() + "\n url : " + placeUrl)
                        ).show();

                        setLocation(details.geometry.location.lat, details.geometry.location.lng);
                    }

                    @Override
                    public void onFailure(final Throwable failure) {
                        Log.d("test", "failure " + failure);
                    }
                });
            }
        });

        /* test code end*/
        textAddress = (TextView) findViewById(R.id.tv_text_address);

        autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        View viewSearch = ((View)findViewById(R.id.place_autocomplete_search_button));
        viewSearch.setVisibility(View.GONE);

        View viewX = ((View)findViewById(R.id.place_autocomplete_clear_button));
        viewX.setVisibility(View.GONE);

        EditText etSearch = ((EditText)findViewById(R.id.place_autocomplete_search_input));
        etSearch.setVisibility(View.GONE);


//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO: Get info about the selected place.
//                System.out.println("Place: " + place.getAddress());
//                address = place.getAddress().toString();
//                latLng = place.getLatLng();
//                setLocation(place.getLatLng().latitude, place.getLatLng().longitude);
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//                Log.e(TAG, "An error occurred: " + status);
//            }
//        });


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocationIfPermitted();
        // Add a marker in Sydney and move the camera
        Location location = getLastKnownLocation();
        if (location != null) {
            setLocation(location.getLatitude(), location.getLongitude());
        } else {
            Toasty.info(context, "GPS anda tidak aktif").show();
            setLocation(-34, 151);
        }

        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                // Creating a marker
                MarkerOptions markerOptions = new MarkerOptions();
                // Setting the position for the marker
                markerOptions.position(latLng);
                // Setting the title for the marker.
                // This will be displayed on taping the marker
                // Clears the previously touched position
                mMap.clear();
                // Animating to the touched position
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                // Placing a marker on the touched position
                mMap.addMarker(markerOptions);

                mAutocomplete.setText("");

                setLocation(latLng.latitude, latLng.longitude);
            }
        });
    }

    /**
     * set marker current address to map
     * @param lat
     * @param lng
     */
    private void setLocation(double lat, double lng) {
        LatLng currenctLocation = new LatLng(lat, lng);
        //set default address based on lat lng
        //setAddressByLatLng(lat,lng);
        MarkerOptions markerOptions = new MarkerOptions().position(currenctLocation).title("Lokasi pengiriman");
        markerOptions.draggable(true);

        mMap.clear();
        mMap.addMarker(markerOptions);
        mMap.setOnMarkerDragListener(this);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(currenctLocation));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMinZoomPreference(15);

        setAddressByLatLng(lat, lng);
    }

    /**
     * set permission to access location
     */
    private void enableMyLocationIfPermitted() {
        if (ContextCompat.checkSelfPermission(context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
        }
    }

    /**
     * get last location
     * @return Location
     */
    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = null;
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

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    /**
     * get position after finish drag
     * @param marker
     */
    @Override
    public void onMarkerDragEnd(Marker marker) {
        double lat = marker.getPosition().latitude;
        double lng = marker.getPosition().longitude;
        // String alamat = marker.getTitle();
        setAddressByLatLng(lat,lng);
    }

    /**
     * set location by marker position
     * @param lat
     * @param lng
     */
    private void setAddressByLatLng(double lat,double lng){
        try{

            Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

            if (geocoder.isPresent()) {
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                Address returnAddress = addresses.get(0);

                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < returnAddress.getMaxAddressLineIndex(); i++) {
                    sb.append(returnAddress.getAddressLine(i)).append(" ");
                }
                sb.append(returnAddress.getLocality()).append(" ");
                sb.append(returnAddress.getPostalCode()).append(" ");
                sb.append(returnAddress.getCountryName());

                // address = sb.toString();
                // latLng = new LatLng(lat,lng);
                // autocompleteFragment.setText(address);

                if (addresses.size() > 0) {
                    latLng = new LatLng(lat,lng);
                    address = addresses.get(0).getAddressLine(0);
                    textAddress.setText(address);
                    mAutocomplete.setText(address);
                    autocompleteFragment.setText(address);
                }
            }else{
                Toasty.info(context,"Geocoder not present").show();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
