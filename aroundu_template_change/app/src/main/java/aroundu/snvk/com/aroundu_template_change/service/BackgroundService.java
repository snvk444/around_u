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
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import java.util.List;
import aroundu.snvk.com.aroundu_template_change.database.DBHandler;
import aroundu.snvk.com.aroundu_template_change.vo.LocationInfo;
import cz.msebera.android.httpclient.Header;

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

    @Override
    public void onCreate() {

        dbHandler = DBHandler.getInstance(this);
        handler = new Handler();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        setLocationManager();
        if((dbHandler.dbSyncCount() >= 1000)){
            //pull the data from the device where status=0 in locationInfo table.
            Log.d("Sync", "Starting SyncSQLiteMySQLDB");
            syncSQLiteMySQLDB();
            dbHandler.updateloctable();

        }else{
        //nothing to do
        }

        runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, 10000);
            }
        };

        handler.postDelayed(runnable, 15000);
    }

    @SuppressWarnings({"MissingPermission"})
    public void setLocationManager(){
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
    }

    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);
        //Toast.makeText(this, "Service stopped", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startid) {
        //Toast.makeText(this, "Service started by user.", Toast.LENGTH_LONG).show();
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

    public void syncSQLiteMySQLDB() {
        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final List userList = dbHandler.UpdateServerLocationInfo();
        Log.d("Sync 1", String.valueOf(userList.size()));
        if (userList.size() != 0) {
            if (dbHandler.dbSyncCount() != 0) {
                Log.d("Sync", "OnSuccess Function 2");
                params.add("usersJSON", dbHandler.composeJSONfromSQLite());
                Log.d("Sync", params.toString());
                client.post("http://limitmyexpense.com/arounduuserdatasync/insert_location_logs.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("Sync", "OnSuccess Function 4");
                        Log.d("Sync", String.valueOf(statusCode));
                        //prgDialog.hide();
                        try {
                            Log.d("Sync", "in try block!");
                            String str = new String(responseBody, "UTF-8");
                            Log.d("Sync str", String.valueOf(str.length()));
                            JSONArray jarray = new JSONArray(str.trim());
                            Log.d("Sync", String.valueOf(jarray.length()));

                            for (int i = 0; i < jarray.length(); i++) {
                                Log.d("Sync", String.valueOf(i));
                                JSONObject jsonobject = jarray.getJSONObject(i);
                                Log.d("Sync", String.valueOf(jsonobject.getLong("time_stamp")));
                                //jsonobject.getLong("time_stamp");
                                dbHandler.updateSyncStatus((Long) jsonobject.getLong("time_stamp"));
                            }
                            Log.d("Sync", "Am I here??");
                            Log.d("Sync", String.valueOf(jarray.length()));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        Log.d("Sync", "OnFailure Function");
                        //prgDialog.hide();
                        if (statusCode == 404) {
                            Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("Sync", error.getMessage());
                            Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            } else {
                //Toast.makeText(getApplicationContext(), "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();
            }
        } else {
            //Database is in Sync
        }
    }
}
