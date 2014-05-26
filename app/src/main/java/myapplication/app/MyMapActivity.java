package myapplication.app;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
            System.out.println(p.getTagId());

            switch (p.getTagId()) {
                case 1:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.animation)));
                    break;
                case 2:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.sport)));
                    break;
                case 3:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.tourisme)));
                    break;
                case 4:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.culte)));
                    break;
                case 5:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.parking)));
                    break;
                case 6:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.autotrement)));
                    break;
                case 7:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.velhop)));
                    break;
                case 8:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.potable)));
                    break;
                case 9:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.toilette)));
                    break;
                case 10:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.divers)));
                    break;
                case 11:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.promenade)));
                    break;
                case 12:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.vert)));
                    break;
                case 13:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.jeu)));
                    break;
                case 14:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace())
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.fontaine)));
                    break;
                default:
                    googleMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getGpsXPlace(), p.getGpsYPlace()))
                            .title(p.getNamePlace()));
                    break;
            }
        }

    }
}