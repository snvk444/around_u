package aroundu.snvk.com.aroundu_template_change;

import android.Manifest;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener, OnMapReadyCallback {

    private static final String TAG = "TestingToolbar";
    BufferedReader reader = null;
    private GoogleMap googleMap;
    private FusedLocationProviderClient mFusedLocationClient;
    public String item_selected_1 = "Select...";
    private ClusterManager<LocationInfo> mClusterManager;
    LocationManager locationManager;
    final DBHandler db = new DBHandler(this);
    TileOverlay mOverlay;
    HeatmapTileProvider mProvider;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    String provider;
    Button btn;
    EditText eText;
    ConstraintLayout constraint_Layout_1;
    LinearLayout linear_Layout_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        Spinner core_spinner = (Spinner) findViewById(R.id.core_spinner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkLocationPermission();

        //Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.core_identifiers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        core_spinner.setAdapter(adapter);
        core_spinner.setOnItemSelectedListener(this);

        //Floating Action
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo use this section to display the bus routes from source to destination that are provided by the user.
                //get the closest bus stations from the user and the destination location the user provided. Use that info to display the list in this bottom up.
                Snackbar.make(view, "Show the list of markers within 2mile radius", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Drawer Implementation (DEFAULT)
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        //map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

//        //core-data from csv file
//        DBHandler db = new DBHandler(this);
//        Log.d("TAG", "Inserting");
//        List<LatLng> latLngList = new ArrayList<LatLng>();
//        String line = "";
//        try {
//            Log.i(TAG, "Reading LocationReadings.csv to db");
//            InputStream is = getResources().openRawResource(R.raw.visakhapatnam_data);
//            reader = new BufferedReader(new InputStreamReader(is));
//        } catch (Exception e) {
//            Log.i(TAG, "Reading LocationReadings.csv to db failed");
//        }
//        //reading data into database from csv file
//        try {
//            while ((line = reader.readLine()) != null) // Read until end of file
//            {
//                Log.i(TAG, "read lat and long");
//                String[] lines = line.split(",");
//                String identifier = lines[4];
//                float lat = Float.parseFloat(lines[5]);
//                float lon = Float.parseFloat(lines[6]);
//                String brand = lines[7];
//                String name = lines[8];
//                String address = lines[9];
//                int zipcode = Integer.parseInt(lines[3]);
//                String city = lines[2];
//                String district = lines[1];
//                String state = "Andhra Pradesh";
//
//                db.addPivotTableData(new PivotTableData(identifier, lat, lon, name,brand, address, zipcode, city, district, state));
//                Log.i(TAG, "Reading data into table " + identifier + "," + lat + "," +lon + "," + brand +"," + name +"," + address + "," +zipcode + "," + city + "," + state);
//            }
//        } catch (IOException e) {
//            Log.i(TAG, "Reading lat long failed");
//            e.printStackTrace();
//        }
//        //db.getIntoPivotTableData("Gas");
//        /////////

        //initial insert of data
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        //todo get off UI thread
        if (prefs.getBoolean("first_run", true)) {
            populateDatabaseWithInitialData(prefs);
        }

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
            setLocationManagerListener(LocationManager.NETWORK_PROVIDER);
        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            setLocationManagerListener(LocationManager.GPS_PROVIDER);
        }


        //textbox - destination
        eText = (EditText) findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String str = eText.getText().toString();
                Toast msg = Toast.makeText(getBaseContext(),str,Toast.LENGTH_LONG);
                msg.show();
            }
        });

    }

    @SuppressWarnings({"MissingPermission"})
    public void setLocationManagerListener(String provider) {
        locationManager.requestLocationUpdates(provider, 100, 100, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "onLocationChanged");
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                long timeStamp = System.currentTimeMillis();
                Log.i(TAG, "timestamp" + timeStamp + " latitude" + latitude + " longitude" + longitude);
                //LatLng present_Loc = new LatLng(latitude, longitude);
                //add location data into table - locationinfo.
                addLocationInfoToDB(latitude, longitude, timeStamp);
                //reading latlong from table - locationinfo.
                setUpClusterer(latitude, longitude);


                addHeatMap(latitude, longitude);
                displayLocationInfo(latitude, longitude);
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


    public boolean checkLocationPermission() {
        Log.d(TAG, "Permission requested");
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

        } else {
            return false;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d(TAG, "Permission response: " + requestCode);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                        //Request location updates:

                        provider = locationManager.getBestProvider(new Criteria(), false);
                        setLocationManagerListener(provider);
                    }

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    //todo add pop up to re-request location, since the app is mostly pointless without it
                }
                return;
            }

        }
    }

    private void addHeatMap(double latitude, double longitude) {
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18f));
        ArrayList<LocationInfo> locationInfoList = db.readLocationInfo(latitude, longitude);
        LatLng source_loc = null;
        List<LatLng> list = new ArrayList<>();
        for (LocationInfo li : locationInfoList) {
            source_loc = new LatLng(li.getLatitude(), li.getLongitude());
            list.add(source_loc);
        }
        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
    }


    private void setUpClusterer(double latitude, double longitude) {
        // Position the map.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 18));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<LocationInfo>(this, googleMap);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        //      googleMap.setOnCameraIdleListener(mClusterManager);
        //      googleMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        displayLocationInfo(latitude, longitude);
    }


    public void addLocationInfoToDB(double latitude, double longitude, long timeStamp) {
        // adding the location data into LocationInfo table.
        Log.i(TAG, "timestamp-start" + timeStamp + " latitude" + latitude + " longitude" + longitude);
        LocationInfo li = new LocationInfo();
        li.setTime_stamp(timeStamp);
        li.setLatitude(latitude);
        li.setLongitude(longitude);
        db.addLocationInfo(li);
    }


    public void displayLocationInfo(double latitude, double longitude) {
        ArrayList<LocationInfo> locationInfoList = db.readLocationInfo(latitude, longitude);
        for (LocationInfo li : locationInfoList) {
            //mMap.addMarker(new MarkerOptions().position(new LatLng(li.getLatitude(), li.getLongitude())));
            Log.i(TAG, " Points to Display from displayLocationInfo: Latitude:" + li.getLatitude() + "longitude " + li.getLongitude());
        }
        //db.TotalCount();
        mClusterManager.addItems(locationInfoList);
    }


    private void populateDatabaseWithInitialData(SharedPreferences prefs) {
        DBHandler db = new DBHandler(this);
        List<LatLng> latLngList = new ArrayList<LatLng>();
        String line = "";
        try {
            InputStream is = getResources().openRawResource(R.raw.visakhapatnam_data_small);
            reader = new BufferedReader(new InputStreamReader(is));
        } catch (Exception e) {
            Log.i(TAG, "Reading LocationReadings.csv to db failed");
        }
        //reading data into database from csv file
        try {
            while ((line = reader.readLine()) != null) // Read until end of file
            {
                String[] lines = line.split(",");
                String identifier = lines[4];
                float lat = Float.parseFloat(lines[5]);
                float lon = Float.parseFloat(lines[6]);
                String brand = lines[8];
                String name = lines[7];
                String address = lines[9];
                int zipcode = Integer.parseInt(lines[3]);
                String city = lines[2];
                String district = lines[1];
                String state = lines[0];

                db.addPivotTableData(new PivotTableData(identifier, lat, lon, name, brand, address, zipcode, city, district, state));
//                Log.i(TAG, "Reading data into table " + identifier + "," + lat + "," + lon + "," + brand + "," + name + "," + address + "," + zipcode + "," + city + "," + state);
            }
        } catch (IOException e) {
            Log.i(TAG, "Reading lat long failed");
            e.printStackTrace();
        }
        try {
            InputStream is = getResources().openRawResource(R.raw.visakhapatnam_bus_lines_data);
            reader = new BufferedReader(new InputStreamReader(is));
        } catch (Exception e) {
            Log.i(TAG, "Reading LocationReadings.csv to db failed");
        }
        prefs.edit().putBoolean("first_run", false).apply();

        try {
            while ((line = reader.readLine()) != null) // Read until end of file
            {
                String[] lines = line.split(",");
                int lineid = Integer.parseInt(lines[0]);
                String busno = lines[1];
                String source_station = lines[2];
                String destination_station = lines[3];
                db.addBusLinesData(new IdentifierBusInfo(lineid, busno, source_station, destination_station));
                Log.i(TAG, "Reading data into table " + lineid + "," + busno + "," + source_station + "," + destination_station);
            }
        } catch (IOException e) {
            Log.i(TAG, "Reading lat long failed");
            e.printStackTrace();
        }
        prefs.edit().putBoolean("first_run", false).apply();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_refresh:
                googleMap.clear();
                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
                        .show();
                break;
//            case R.id.action_settings:
//                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
//                        .show();
//                break;
//            case R.id.action_exit:
//                Toast.makeText(this, "Exited selected", Toast.LENGTH_SHORT)
//                        .show();
//                break;
            default:
                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item_selected = adapterView.getItemAtPosition(i).toString();
        Toast.makeText(adapterView.getContext(), item_selected, Toast.LENGTH_SHORT).show();
        item_selected_1 = item_selected;
        if (item_selected_1 != "Bus"){
            //todo hide the linear layout created.

            constraint_Layout_1 = (ConstraintLayout) findViewById(R.id.constraintlayout);
            linear_Layout_1 = (LinearLayout) findViewById(R.id.linearlayout);
            linear_Layout_1.setVisibility(view.GONE);

        }
        populateMap();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        googleMap.clear();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        boolean permissionAccepted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            permissionAccepted = false;
        }

        if (permissionAccepted) {
            setLocation();
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        }

//        ArrayList<DisplayInfo> displayInfo = null;
//
//        List<PivotTableData> markers = db.getFromPivotTableData(item_selected_1);
////
////            for (PivotTableData marker : markers) {
////                double lat = marker.latitude;
////                double lon = marker.longitude;
////                String add = marker.address;
////                latLngList.add(new LatLng(lat, lon));
////            }
//
//        for (DisplayInfo object : displayInfo) {
//            m = googleMap.addMarker(new MarkerOptions()
//                    .position(object.getLatLng())
//                    .title(object.getTitle()));
//        }
    }


    public void populateMap() {

        googleMap.clear();


        List<LatLng> latLngList = new ArrayList<LatLng>();
        String line = "";
        DBHandler db = new DBHandler(this);
        HashMap<LatLng, String> mData = new HashMap<>();
        Marker m;

        if (!item_selected_1.equals("Select...")) {
            List<PivotTableData> markers = db.getFromPivotTableData(item_selected_1);
            LatLng latLng = null;
            Log.d(TAG, String.valueOf(markers));
            for (PivotTableData marker : markers) {
                latLng = new LatLng(marker.latitude, marker.longitude);
                String name = marker.name;

                mData.put(latLng, name);

                latLngList.add(latLng);
            }
            Log.d(TAG, "Here I am" + String.valueOf(mData));

            for (LatLng li : mData.keySet()) {
                Log.d(TAG, "Display" + mData.get(li) + "" + li);
                googleMap.addMarker(new MarkerOptions().position(li).title(String.valueOf(mData.get(li))));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(li, 18));
            }
//            for (LatLng point : latLngList) {
//                googleMap.clear();
//                LatLng test = new LatLng(point.latitude, point.longitude);
//                googleMap.addMarker(new MarkerOptions().position(test).title(point.latitude+ "\n" +point.longitude)
//                .snippet("My Snippet"+"\n"+"1st Line Text"+"\n"+"2nd Line Text"+"\n"+"3rd Line Text").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                Log.i(TAG, "Point: " + point);
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
//            }
        } else googleMap.clear();
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
                                // Logic to handle location object
                                LatLng coordinate = new LatLng(location.getLatitude(), location.getLongitude());
                                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 18);
                                googleMap.animateCamera(yourLocation);
                            }
                        }
                    });
        } catch (SecurityException e) {
            Log.d(TAG, "Location exception: " + e.getMessage());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}