package aroundu.snvk.com.aroundu_template_change.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.util.DbUtils;

import aroundu.snvk.com.aroundu_template_change.database.DBHandler;
import aroundu.snvk.com.aroundu_template_change.vo.LocationInfo;

public class BackgroundService extends Service {

    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    private LocationManager locationManager;
    private DBHandler dbHandler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressWarnings({"MissingPermission"})
    @Override
    public void onCreate() {
        Toast.makeText(this, "Service created!", Toast.LENGTH_LONG).show();

        dbHandler = DBHandler.getInstance(this);
        handler = new Handler();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        runnable = new Runnable() {
            public void run() {
                String provider = "";
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    provider = LocationManager.NETWORK_PROVIDER;
                } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    provider = LocationManager.GPS_PROVIDER;
                }
                locationManager.requestLocationUpdates(provider, 100, 0, new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        Log.i("ServiceTest", "onLocationChanged");
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        long timeStamp = System.currentTimeMillis();
                        Log.i("ServiceTest", "timestamp" + timeStamp + " latitude" + latitude + " longitude" + longitude);
                        addLocationInfoToDB(latitude, longitude, timeStamp);
                    }

                    @Override
                    public void onStatusChanged(String s, int i, Bundle bundle) {

                    }

                    @Override
                    public void onProviderEnabled(String s) {

                    }

                    @Override
                    public void onProviderDisabled(String s) {
                    }
                });
                handler.postDelayed(runnable, 10000);
            }
        };

        handler.postDelayed(runnable, 15000);
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
    }

    public void addLocationInfoToDB(double latitude, double longitude, long timeStamp) {
        // adding the location data into LocationInfo table.
        Log.i("ServiceTest", "timestamp-start" + timeStamp + " latitude" + latitude + " longitude" + longitude);
        LocationInfo li = new LocationInfo();
        li.setTime_stamp(timeStamp);
        li.setLatitude(latitude);
        li.setLongitude(longitude);
        dbHandler.addLocationInfo(li);
    }
}
