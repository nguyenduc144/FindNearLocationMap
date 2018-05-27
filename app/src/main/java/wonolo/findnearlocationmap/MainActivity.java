package wonolo.findnearlocationmap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static wonolo.findnearlocationmap.LoginActivity.PREFS_NAME;

public class MainActivity extends AppCompatActivity implements PlayerListener, PlaceFinderListener{
    private ArrayList<NameLocation> mLocation;
    private ListView mListLocationMain;
    public static Double sStandingLatitude, sStandingLongitude;
    private GPSTracker mGps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListLocationMain = (ListView) findViewById(R.id.lv_location);
        mLocation = new ArrayList<>();
        NameLocation location0 = new NameLocation("RESTAURANT");
        NameLocation location1 = new NameLocation("HOTEL");
        NameLocation location2 = new NameLocation("SCHOOL");
        NameLocation location3 = new NameLocation("HOSPITAL");
        NameLocation location4 = new NameLocation("ATM");
        mLocation.add(location0);
        mLocation.add(location1);
        mLocation.add(location2);
        mLocation.add(location3);
        mLocation.add(location4);
        mGps = new GPSTracker(MainActivity.this);
        if(mGps.canGetLocation()){
            sStandingLatitude = mGps.getLatitude();
            sStandingLongitude = mGps.getLongitude();
            //Toast.makeText(this,sStandingLatitude +"\n" +sStandingLongitude,Toast.LENGTH_LONG).show();
        } else {
            mGps.showSettingsAlert();
        }
        DataLocationMain dataLocationMain = new DataLocationMain(this, R.layout.location_main_item, mLocation, this, this);
        mListLocationMain.setAdapter(dataLocationMain);
    }

    @Override
    public void onClickPlayer(int pos, Intent intent) {
        startActivity(intent);
    }

    @Override
    public void onPlaceFinderStart() {

    }

    @Override
    public void onPlaceFinderSuccess(ArrayList<LocationItem> item) {

    }

    @Override
    public void addMarkerNearMyLocation(ArrayList<LocationItem> item) {
    }
    public void onClickLogout(View view){
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove("logged");
        editor.commit();
        Toast.makeText(getApplicationContext(), "Successfull Logout", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
    }

}
