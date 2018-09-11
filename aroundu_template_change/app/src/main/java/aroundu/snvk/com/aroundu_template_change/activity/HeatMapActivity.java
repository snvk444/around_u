package aroundu.snvk.com.aroundu_template_change.activity;

import aroundu.snvk.com.aroundu_template_change.R;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

public class HeatMapActivity extends AppCompatActivity {

    LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;
    MainActivity main = new MainActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_heat_map);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        MainActivity ma = new MainActivity();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            Log.i("InHeatMap","In the heat - network");
                    //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    //ma.setLocationManagerListener(LocationManager.NETWORK_PROVIDER);

                    if (isLocationEnabled(HeatMapActivity.this)) {
                        locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);
                        criteria = new Criteria();
                        bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

                        //You can still do this if you like, you might get lucky:
                        Location location = locationManager.getLastKnownLocation(bestProvider);
                        if (location != null) {
                            Log.e("TAG", "GPS is on");
                            double latitude = location.getLatitude();
                            double longitude = location.getLongitude();
                            Toast.makeText(HeatMapActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();
                            Log.i("addingHeatMap","HeatMapCheck");
                            main.addHeatMap(latitude, longitude);
                        }
                        else{
                            //This is what you need:
                            locationManager.requestLocationUpdates(bestProvider, 100, 100, (LocationListener) this);
                            //ma.setLocationManagerListener(LocationManager.GPS_PROVIDER);
                        }
                    }
                    else
                    {
                        //prompt user to enable location....
                        //.................
                    }
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Log.i("InHeatMap","In the heat - gps");
                    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            ma.setLocationManagerListener(LocationManager.GPS_PROVIDER);
        }

    }
    public static boolean isLocationEnabled(Context context)
    {
        //...............
        return true;
    }
}
