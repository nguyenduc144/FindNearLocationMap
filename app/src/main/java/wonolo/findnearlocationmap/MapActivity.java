package wonolo.findnearlocationmap;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static wonolo.findnearlocationmap.R.id.myMap;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, DirectionFinderListener,PlaceFinderListener{
    private GoogleMap mMap;
    private double  mDestinationLatitude, mDestinationLongitude;
    private ProgressDialog mProgressDialog;
    private List<Marker> mOriginMarkers;
    private List<Marker> mDestinationMarkers;
    private List<Polyline> mPolylinePaths;
    private String mName;
    private LatLng mMyLocation;
    private String mChoose;
    private String mNameLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(myMap);
        mapFragment.getMapAsync(this);
        Bundle bundle = getIntent().getExtras();
        mName = bundle.getString("NAME");
        mChoose = "";
        if(bundle.getString("CHOOSE")!=null){
            mChoose = bundle.getString("CHOOSE");
        }
        mNameLocation = bundle.getString("LOCATION");
        mMyLocation = new LatLng(MainActivity.sStandingLatitude,MainActivity.sStandingLongitude);
        if(mChoose.equals("map")){
            try {
                new PlaceFinder(this, MainActivity.sStandingLatitude, MainActivity.sStandingLongitude,mNameLocation,this).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            try {
                if(bundle.getString("LATITUDE")!=null && bundle.getString("LONGITUDE")!=null){
                    mDestinationLatitude = Double.parseDouble(bundle.getString("LATITUDE"));
                    mDestinationLongitude = Double.parseDouble(bundle.getString("LONGITUDE"));
                }
                new DirectionFinder(this,this,MainActivity.sStandingLatitude,MainActivity.sStandingLongitude, mDestinationLatitude, mDestinationLongitude).execute();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onDirectionFinderStart() {
        mProgressDialog = ProgressDialog.show(this,"Please wait...","Finding direction..",true);
        if (mOriginMarkers != null) {
            for (Marker marker : mOriginMarkers) {
                marker.remove();
            }
        }
        if (mDestinationMarkers != null) {
            for (Marker marker : mDestinationMarkers) {
                marker.remove();
            }
        }
        if (mPolylinePaths != null) {
            for (Polyline polyline: mPolylinePaths) {
                polyline.remove();
            }
        }
    }

    @Override
    public void onDirectionFinderSuccess(ArrayList<Route> item) {
        mProgressDialog.dismiss();
        mPolylinePaths = new ArrayList<>();
        mOriginMarkers = new ArrayList<>();
        mDestinationMarkers = new ArrayList<>();
        for (Route route : item) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(route.getmStartLocation(), 16));
            mOriginMarkers.add(mMap.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                    .title(route.getmStartAddress())
                    .position(route.getmStartLocation())));
            mDestinationMarkers.add(mMap.addMarker(new MarkerOptions()
                    .title(mName)
                    .position(route.getmEndLocation())));
            PolylineOptions polylineOptions = new PolylineOptions()
                    .geodesic(true)
                    .color(Color.RED)
                    .width(10);
            for (int i = 0; i < route.getmPoints().size(); i++)
                polylineOptions.add(route.getmPoints().get(i));
            mPolylinePaths.add(mMap.addPolyline(polylineOptions));
        }
    }

    @Override
    public void onPlaceFinderStart() {

    }

    @Override
    public void onPlaceFinderSuccess(ArrayList<LocationItem> item) {

    }

    @Override
    public void addMarkerNearMyLocation(ArrayList<LocationItem> item) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mMyLocation, 16));
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .title("My location")
                .position(mMyLocation));
        for (LocationItem locationItem : item ) {
            mMap.addMarker(new MarkerOptions()
                    .title(locationItem.getmName())
                    .position(locationItem.getmLocation()));
        }
    }
}
