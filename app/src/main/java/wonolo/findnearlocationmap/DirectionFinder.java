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
import java.util.List;

public class DirectionFinder {
    private static final String DIRECTION_URL_API = "https://maps.googleapis.com/maps/api/directions/json?";
    private static final String GOOGLE_API_KEY = "AIzaSyAxTKkV1-wXoE1rzwkNruE6jtWx2Rl-2xo";
    private DirectionFinderListener mListener;
    private Context mContext;
    private double mMapLatitude, mMapLongitude, mDestinationLatitude, mDestinationLongitude;
    public DirectionFinder(DirectionFinderListener listener,Context context, double map_latitude,
                           double map_longitude,double destination_latitude,
                           double destination_longitude){
        this.mListener = listener;
        this.mMapLatitude = map_latitude;
        this.mMapLongitude = map_longitude;
        this.mDestinationLatitude = destination_latitude;
        this.mDestinationLongitude = destination_longitude;
        this.mContext = context;
    }
    public void execute() throws UnsupportedEncodingException {
        mListener.onDirectionFinderStart();
        new ReadJSON().execute(createUrl());
    }

    private String createUrl() throws UnsupportedEncodingException {
        return DIRECTION_URL_API + "origin=" + mMapLatitude+","+ mMapLongitude +
                "&destination=" + mDestinationLatitude +","+ mDestinationLongitude + "&key=" +
                GOOGLE_API_KEY;
    }
    private class ReadJSON extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            return readContentURL(params[0]);
        }
        @Override
        protected void onPostExecute(String s) {
            try {
                parseJSonDirection(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
    private void parseJSonDirection(String data) throws JSONException {
        if (data == null)
            return;

        ArrayList<Route> routes = new ArrayList<Route>();
        JSONObject jsonData = new JSONObject(data);
        JSONArray jsonRoutes = jsonData.getJSONArray("routes");
        for (int i = 0; i < jsonRoutes.length(); i++) {
            JSONObject jsonRoute = jsonRoutes.getJSONObject(i);
            Route route = new Route();

            JSONObject overview_polylineJson = jsonRoute.getJSONObject("overview_polyline");
            JSONArray jsonLegs = jsonRoute.getJSONArray("legs");
            JSONObject jsonLeg = jsonLegs.getJSONObject(0);
            JSONObject jsonEndLocation = jsonLeg.getJSONObject("end_location");
            JSONObject jsonStartLocation = jsonLeg.getJSONObject("start_location");

            if(jsonLeg.has("end_address") && isCheckString(jsonLeg.getString("end_address"))){
                route.setmEndAddress(jsonLeg.getString("end_address"));
            }
            if(jsonLeg.has("start_address") && isCheckString(jsonLeg.getString("start_address"))){
                route.setmStartAddress(jsonLeg.getString("start_address"));
            }
            if(jsonStartLocation.has("lat") && jsonStartLocation.has("lng") &&
                    isCheckString(jsonStartLocation.getString("lat")) && isCheckString(jsonStartLocation.getString("lng"))){
                route.setmStartLocation(new LatLng(jsonStartLocation.getDouble("lat"), jsonStartLocation.getDouble("lng")));
            }
            if(jsonEndLocation.has("lat") && jsonEndLocation.has("lng") &&
                    isCheckString(jsonEndLocation.getString("lat")) && isCheckString(jsonEndLocation.getString("lng"))){
                route.setmEndLocation(new LatLng(jsonEndLocation.getDouble("lat"), jsonEndLocation.getDouble("lng")));
            }
            if(overview_polylineJson.has("points") && isCheckString(overview_polylineJson.getString("points"))){
                route.setmPoints(decodePolyLine(overview_polylineJson.getString("points")));
            }
            routes.add(route);
        }
        mListener.onDirectionFinderSuccess(routes);
    }
    private List<LatLng> decodePolyLine(final String poly) {
        int len = poly.length();
        int index = 0;
        List<LatLng> decoded = new ArrayList<LatLng>();
        int lat = 0;
        int lng = 0;
        while (index < len) {
            int b;
            int shift = 0;
            int result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;
            shift = 0;
            result = 0;
            do {
                b = poly.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;
            decoded.add(new LatLng(
                    lat / 100000d, lng / 100000d
            ));
        }
        return decoded;
    }
    private String readContentURL(String theUrl){
        StringBuilder content = new StringBuilder();
        try {
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
