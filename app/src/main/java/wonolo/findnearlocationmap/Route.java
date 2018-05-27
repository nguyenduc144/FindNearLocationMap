package wonolo.findnearlocationmap;


import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class Route {
    private String mEndAddress;
    private LatLng mEndLocation;
    private String mStartAddress;
    private LatLng mStartLocation;
    private List<LatLng> mPoints;
    private String mNameRoute;


    public LatLng getmEndLocation() {
        return mEndLocation;
    }

    public LatLng getmStartLocation() {
        return mStartLocation;
    }

    public String getmEndAddress() {
        return mEndAddress;
    }

    public String getmStartAddress() {
        return mStartAddress;
    }


    public void setmEndAddress(String mEndAddress) {
        this.mEndAddress = mEndAddress;
    }

    public void setmEndLocation(LatLng mEndLocation) {
        this.mEndLocation = mEndLocation;
    }

    public void setmStartAddress(String mStartAddress) {
        this.mStartAddress = mStartAddress;
    }

    public void setmStartLocation(LatLng mStartLocation) {
        this.mStartLocation = mStartLocation;
    }

    public List<LatLng> getmPoints() {
        return mPoints;
    }

    public void setmPoints(List<LatLng> mPoints) {
        this.mPoints = mPoints;
    }

    public String getmNameRoute() {
        return mNameRoute;
    }

    public void setmNameRoute(String mNameRoute) {
        this.mNameRoute = mNameRoute;
    }
}
