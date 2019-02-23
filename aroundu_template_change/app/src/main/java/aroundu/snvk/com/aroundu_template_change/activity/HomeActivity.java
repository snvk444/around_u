package aroundu.snvk.com.aroundu_template_change.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import aroundu.snvk.com.aroundu_template_change.R;
import aroundu.snvk.com.aroundu_template_change.service.BackgroundService;

import static aroundu.snvk.com.aroundu_template_change.activity.MainActivity.MY_PERMISSIONS_REQUEST_LOCATION;

public class HomeActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    private static final String TAG = "HomeActivity";
    private FusedLocationProviderClient mFusedLocationClient;
    LatLng coordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setLocation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(HomeActivity.this, MainActivity.class);
                homeIntent.putExtra("userLocation", coordinate);
                Toast.makeText(getBaseContext(), "No results found", Toast.LENGTH_LONG);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }

    private void setLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d(TAG, "Location: " + location);
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                coordinate  = new LatLng(location.getLatitude(), location.getLongitude());
                            }
                        }
                    });
        } catch (SecurityException e) {
            Log.d(TAG, "Location exception: " + e.getMessage());
        }
    }

}
