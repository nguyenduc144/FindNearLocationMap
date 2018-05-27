package wonolo.findnearlocationmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ListLocationActivity extends AppCompatActivity implements PlaceFinderListener,PlayerListener {
    private ListView mListView;
    private String mLocationFromMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_location);
        mListView = (ListView) findViewById(R.id.lv_list_location);
        Bundle bundle = getIntent().getExtras();
        mLocationFromMain = bundle.getString("LOCATION");
//        standingLatitude = 10.7905899;
//        standingLongitude = 106.7121544;
        DataLocationMain.sProgressDialog.dismiss();
        try {
            new PlaceFinder(this, MainActivity.sStandingLatitude, MainActivity.sStandingLongitude, mLocationFromMain,this).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onPlaceFinderStart() {

    }

    @Override
    public void onPlaceFinderSuccess(ArrayList<LocationItem> item) {
        DataListLocation dataListLocation = new DataListLocation(this,R.layout.location_item,item,this);
        mListView.setAdapter(dataListLocation);
        dataListLocation.notifyDataSetChanged();
    }

    @Override
    public void addMarkerNearMyLocation(ArrayList<LocationItem> item) {

    }

    @Override
    public void onClickPlayer(int pos, Intent intent) {
        startActivity(intent);
    }
}
