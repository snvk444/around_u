package aroundu.snvk.com.aroundu_template_change.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import aroundu.snvk.com.aroundu_template_change.R;
import aroundu.snvk.com.aroundu_template_change.service.BackgroundService;

import static aroundu.snvk.com.aroundu_template_change.activity.MainActivity.MY_PERMISSIONS_REQUEST_LOCATION;

public class HomeActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 1000;
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },SPLASH_TIME_OUT);

    }



}
