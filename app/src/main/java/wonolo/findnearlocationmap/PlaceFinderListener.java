package wonolo.findnearlocationmap;


import java.util.ArrayList;

public interface PlaceFinderListener {
    void onPlaceFinderStart();
    void onPlaceFinderSuccess(ArrayList<LocationItem> item);
    void addMarkerNearMyLocation(ArrayList<LocationItem> item);
}
