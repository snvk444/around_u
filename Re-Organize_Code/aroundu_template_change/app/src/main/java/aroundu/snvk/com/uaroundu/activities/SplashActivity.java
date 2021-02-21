package aroundu.snvk.com.uaroundu.activities;

import android.content.Intent;
import android.location.Location;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.appcompat.app.AppCompatActivity;
import aroundu.snvk.com.uaroundu.R;

public class SplashActivity extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;
    private static final String TAG = "SplashActivity";
    private FusedLocationProviderClient mFusedLocationClient;
    private LatLng coordinate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        setLocation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashActivity.this, MainActivity.class);
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
