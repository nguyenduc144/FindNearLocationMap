package wonolo.findnearlocationmap;


import com.google.android.gms.maps.model.LatLng;

public class LocationItem {
    private String mName;
    private String mAddress;
    private LatLng mLocation;

    public String getmAddress() {
        return mAddress;
    }

    public LatLng getmLocation() {
        return mLocation;
    }

    public String getmName() {
        return mName;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public void setmLocation(LatLng mLocation) {
        this.mLocation = mLocation;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }
}

