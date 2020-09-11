package id.co.beton.saleslogistic_trackingsystem.AppBundle.Place;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.AddressComponent;
import com.seatgeek.placesautocomplete.model.AddressComponentType;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import id.co.beton.saleslogistic_trackingsystem.R;

public class PlaceAutocompleteActivity extends AppCompatActivity {

    @BindView(R.id.autocomplete)
    PlacesAutocompleteTextView mAutocomplete;

    @BindView(R.id.street)
    TextView mStreet;

    @BindView(R.id.city)
    TextView mCity;

    @BindView(R.id.state)
    TextView mState;

    @BindView(R.id.zip)
    TextView mZip;

    private Context context;
    private Location location;
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
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.context = PlaceAutocompleteActivity.this;

        setContentView(R.layout.activity_places_autocomplete);
        ButterKnife.bind(this);

        mAutocomplete = (PlacesAutocompleteTextView) findViewById(R.id.autocomplete);

        mAutocomplete.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(final Place place) {

                mAutocomplete.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(final PlaceDetails details) {
                        Log.d("test", "details " + details);
//                        mStreet.setText(details.name);
                        Double lat = details.geometry.location.lat;
                        Double lng = details.geometry.location.lng;
                        setLocation(lat, lng);

                        String placeUrl = details.url;

                        setLocation(details.geometry.location.lat, details.geometry.location.lng);

                        Toasty.info(context, "location \n lat: " + String.valueOf(getLatitude())
                                + ", long: " + String.valueOf(getLongitude() + "\n url : " + placeUrl)
                        ).show();


//                        for (AddressComponent component : details.address_components) {
//                            for (AddressComponentType type : component.types) {
//                                switch (type) {
//                                    case STREET_NUMBER:
//                                        break;
//                                    case ROUTE:
//                                        break;
//                                    case NEIGHBORHOOD:
//                                        break;
//                                    case SUBLOCALITY_LEVEL_1:
//                                        break;
//                                    case SUBLOCALITY:
//                                        break;
//                                    case LOCALITY:
//                                        mCity.setText(component.long_name);
//                                        break;
//                                    case ADMINISTRATIVE_AREA_LEVEL_1:
//                                        mState.setText(component.short_name);
//                                        break;
//                                    case ADMINISTRATIVE_AREA_LEVEL_2:
//                                        break;
//                                    case COUNTRY:
//                                        break;
//                                    case POSTAL_CODE:
//                                        mZip.setText(component.long_name);
//                                        break;
//                                    case POLITICAL:
//                                        break;
//                                }
//                            }
//                        }

                    }

                    @Override
                    public void onFailure(final Throwable failure) {
                        Log.d("test", "failure " + failure);
                    }
                });



            }
        });

//        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
//                .setResultCallback(new ResultCallback<PlaceBuffer>() {
//                    @Override
//                    public void onResult(PlaceBuffer places) {
//                        if (places.getStatus().isSuccess()) {
//                            final Place myPlace = places.get(0);
//                            LatLng queriedLocation = myPlace.getLatLng();
//                            Log.v("Latitude is", "" + queriedLocation.latitude);
//                            Log.v("Longitude is", "" + queriedLocation.longitude);
//                        }
//                        places.release();
//                    }
//                });
    }

}

