package wonolo.findnearlocationmap;


import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class PlaceFinder {
    private static final String PLACE_URL_API = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
    private static final String GOOGLE_PLACE_KEY = "AIzaSyBGWc8FIzZFylqhEI9WkTUtJJV1sz6jzT0";
    private Context mContext;
    private String mType;
    private double mMapLatitude, mMapLongitude;
    private int mRadiusPlace = 2000;
    private PlaceFinderListener mListener;
    private ArrayList<LocationItem> mLocationItems;
    public PlaceFinder(Context context, double map_latitude, double map_longitude, String type, PlaceFinderListener listener){
        this.mMapLatitude = map_latitude;
        this.mMapLongitude = map_longitude;
        this.mContext = context;
        this.mType = type;
        this.mListener=listener;
    }
    public void execute() throws UnsupportedEncodingException {
//        listener.onPlaceFinderStart();
        new ReadJSON().execute(createUrl());
    }
    private String createUrl() throws UnsupportedEncodingException {
        return PLACE_URL_API + "location=" + mMapLatitude +","+ mMapLongitude +
                "&radius="+mRadiusPlace +"&type="+ mType + "&key=" +
                GOOGLE_PLACE_KEY;
    }
    private class ReadJSON extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            return readContentURL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                parseJSonNearPlace(s);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    private void parseJSonNearPlace(String data) throws JSONException {
        if (data == null)
            return;
        mLocationItems = new ArrayList<>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonLocationItems = jsonData.getJSONArray("results");
        for (int i = 0; i < jsonLocationItems.length(); i++) {
            JSONObject jsonLocationItem = jsonLocationItems.getJSONObject(i);
            LocationItem locationItem = new LocationItem();
            JSONObject jsonGeometry = jsonLocationItem.getJSONObject("geometry");
            JSONObject jsonLocation = jsonGeometry.getJSONObject("location");
            if(jsonLocation.has("lat") && jsonLocation.has("lng") &&
                    isCheckString(jsonLocation.getString("lat")) && isCheckString(jsonLocation.getString("lng"))){
                locationItem.setmLocation(new LatLng(jsonLocation.getDouble("lat"),jsonLocation.getDouble("lng")));
            }
            if(jsonLocationItem.has("name") && isCheckString(jsonLocationItem.getString("name"))){
                locationItem.setmName(jsonLocationItem.getString("name"));
            }
            if(jsonLocationItem.has("vicinity") && isCheckString(jsonLocationItem.getString("vicinity"))){
                locationItem.setmAddress(jsonLocationItem.getString("vicinity"));
            }
            mLocationItems.add(locationItem);
        }
            mListener.addMarkerNearMyLocation(mLocationItems);
            mListener.onPlaceFinderSuccess(mLocationItems);
    }
    private String readContentURL(String theUrl){
        StringBuilder content = new StringBuilder();
        try    {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
        return content.toString();
    }
    public boolean isCheckString(String s){
        if((s!=null) && (s!="") && (s!=" ")) {
            return true;
        }
        return false;
    }
}
