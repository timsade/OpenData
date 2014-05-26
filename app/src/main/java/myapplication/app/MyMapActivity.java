package myapplication.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MyMapActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map);
        createMapView();
    }

    /** Local variables **/
    GoogleMap googleMap;

    private void createMapView(){
        /**
         * Catch the null pointer exception that
         * may be thrown when initialising the map
         */
        try {
            if(null == googleMap){
                googleMap = ((MapFragment) getFragmentManager().findFragmentById(
                        R.id.mapView)).getMap();

                if(null == googleMap) {
                    Toast.makeText(getApplicationContext(),
                            "Error creating map", Toast.LENGTH_SHORT).show();
                }
            }

        } catch (NullPointerException exception){
            Log.e("mapApp", exception.toString());
        }

        ArrayList<Place> temp = LstPlaces.getLstPlaces();
        for(Place p : temp)
        {
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                    .title(p.getNamePlace()));
        }

    }
}