package aroundu.snvk.com.uaroundu.activities;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.MotionEvent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;

import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import aroundu.snvk.com.uaroundu.beans.PivotTableData;
import aroundu.snvk.com.uaroundu.R;
import aroundu.snvk.com.uaroundu.adapters.BottomSheetAdapter;
import aroundu.snvk.com.uaroundu.adapters.SearchViewAdapter;
import aroundu.snvk.com.uaroundu.configs.Constants;
import aroundu.snvk.com.uaroundu.db_utils.DBHandler;
import aroundu.snvk.com.uaroundu.dialogs.LocationSubmitDialog;
import aroundu.snvk.com.uaroundu.dialogs.PopupDialog;
import aroundu.snvk.com.uaroundu.interfaces.BottomSheetClickListener;
import aroundu.snvk.com.uaroundu.interfaces.RecyclerViewClickListener;
import aroundu.snvk.com.uaroundu.interfaces.TrackingListener;
import aroundu.snvk.com.uaroundu.services.BackgroundService;
import aroundu.snvk.com.uaroundu.ui.MoreInfoDialog;
import aroundu.snvk.com.uaroundu.beans.IdentifierBusInfo;
import aroundu.snvk.com.uaroundu.beans.LocationInfo;
import aroundu.snvk.com.uaroundu.beans.BusRoutesInfo;
import aroundu.snvk.com.uaroundu.ui.ProgressView;
import aroundu.snvk.com.uaroundu.utils.AppUtils;
import cz.msebera.android.httpclient.Header;
import smartdevelop.ir.eram.showcaseviewlib.GuideView;
import smartdevelop.ir.eram.showcaseviewlib.config.Gravity;
import smartdevelop.ir.eram.showcaseviewlib.config.DismissType;
import smartdevelop.ir.eram.showcaseviewlib.listener.GuideListener;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
        RecyclerViewClickListener, BottomSheetClickListener, TrackingListener, View.OnTouchListener, View.OnClickListener,
        TextWatcher, GoogleMap.OnMapLongClickListener {

    private static final String TAG = "TestingToolbar";
    private ProgressView progressView;

    private BufferedReader reader = null;
    private DBHandler dbHandler;
    private Handler mHandler;

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    private Location currentLocation = null;
    private TileOverlay mOverlay;
    private HeatmapTileProvider mProvider;
    private String provider;

    public String item_selected_1 = "Select...";

    private Toolbar toolbar;

    private RelativeLayout submitButtonLayout;
    private TextView submitButtonTextView;
    private EditText searchEditText;
    private ImageView closeImageView;
    private LinearLayout searchResultLayout;
    private RelativeLayout searchBoxLayout;

    private LinearLayout distanceCalcLayout;
    private TextView versionTextView;
    private TextView distanceTextView;

    private LinearLayout fabContainer;
    private FloatingActionButton snackViewFab;
    private FloatingActionButton bottomSheetFab;
    private FloatingActionButton mainFab;
    private FloatingActionButton busFab;
    private FloatingActionButton coverageFab;

    private BottomSheetBehavior bottomSheetBehavior;
    private LinearLayout bottomSheetLayout;

    private RecyclerView bottomSheetRV, searchViewRV;
    private BottomSheetAdapter bottomSheetAdapter;
    private SearchViewAdapter searchViewAdapter;

    private String srcLocation = "";
    private String destLocation = "";
    private String bus_no = "";
    private String line_id;
    private ArrayList<String> busDestinationSearchResults;
    private boolean searchResultClick = false;

    private int versionClickCount = 0;
    private boolean fabMenuOpen = false;

    private LatLng user_loc_input, user_selected_bustop, coordinate;
    private boolean doubleBackToExitPressedOnce = false;
    private String uuid ="-11";
    private int destination_input_click, map_input_status, dist_metric_layout;
    private HashMap<String,Marker> hashMapMarker = new HashMap<>();
    private List<BusRoutesInfo> busrouteList = new ArrayList<BusRoutesInfo>();

    private Animation animPressDown;
    private Animation animPressRealease;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressView = new ProgressView(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        initViews();
        initHandlers();
        setListenersAndBehaviors();
        checkLocationPermission();

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        checkSelfPermission();
    }

    @SuppressLint("WrongViewCast")
    public void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fabContainer = findViewById(R.id.fabContainerLayout);
        searchResultLayout = findViewById(R.id.search_result_layout);
        searchBoxLayout = findViewById(R.id.searchbox_layout);
        snackViewFab = findViewById(R.id.snack_view_fab);
        bottomSheetFab = findViewById(R.id.show_bottomsheet_fab);
        mainFab = findViewById(R.id.main_fab);
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
        ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 16)).intValue();
        lps.setMargins(margin, margin, margin, margin);

        busFab = findViewById(R.id.bus_fab);
        coverageFab = findViewById(R.id.coverage_fab);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bottomSheetLayout = findViewById(R.id.bottom_sheet);
        closeImageView = bottomSheetLayout.findViewById(R.id.close_bottomsheet_imageview);

        distanceCalcLayout = findViewById(R.id.distancecalc_layout);
        bottomSheetRV = findViewById(R.id.recyclerview);
        searchViewRV = findViewById(R.id.search_recyclerview);
        searchEditText = findViewById(R.id.search_edittext);
        versionTextView = findViewById(R.id.version_textview);
        distanceTextView = findViewById(R.id.distance_textview);

        submitButtonLayout = findViewById(R.id.submit_button_layout);
        submitButtonTextView = findViewById(R.id.submit_textview);

        //Animations to snackViewFab
        Animation show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);

        //expanding mainFab (main floating action button)
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mainFab.getLayoutParams();
        layoutParams.rightMargin += (int) (mainFab.getWidth() * 1.7);
        layoutParams.bottomMargin += (int) (mainFab.getHeight() * 0.25);
        mainFab.setLayoutParams(layoutParams);
        mainFab.startAnimation(show_fab_1);
    }

    private void initHandlers(){
        dbHandler = DBHandler.getInstance(this);
        mHandler = new Handler(Looper.getMainLooper());

        HandlerThread mHandlerThread = new HandlerThread("BackgroundThread");
        mHandlerThread.start();
        Handler backgroundHandler = new Handler(mHandlerThread.getLooper());

        //initial insert of data
        if (AppUtils.getFirstRun(this)) {
            /*if(prefs.getString("ad_id", uuid) == "-11"){
                uuid = dbHandler.uuid_check();
            }*/
            Log.d(TAG, "Populating the database");
            backgroundHandler.post(new Runnable() {
                @Override
                public void run() {
                    uuid = dbHandler.uuid_check();
                    Log.d(TAG, "In MainActivity" + uuid);
                    populateDestinationLookUpTable();
                    //working code
                    /*dbHandler.createDataBase();
                    //if running for the first time and .db file is not created yet. then exe below lines
                    //*Log.d("DestLookUp", "Before");
                    populateDatabaseWithInitialData();
                    Log.d("DestLookUp", "After");
                   populateDestinationLookUpTable();
                    //if we have the .db file created, use the below line to get the data into database fast. use below for release.

                    final String uuid = UUID.randomUUID().toString().replace("-", "");
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("ad_id", uuid );
                    editor.putBoolean("first_run", false);
                    editor.apply();
                    Log.d(TAG, "This is the uuid: " + uuid);
*/
                    AppUtils.setFirstRun(MainActivity.this, false);
                }
            });
        }
        //uuid = UUID.randomUUID().toString().replace("-", "");
        //prefs.edit().putString("ad_id", uuid );
    }

    //This is all for the animation for snackViewFab
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toggleFabMenu() {
        if (!fabMenuOpen) {
            //mainFab.setImageResource(R.drawable.ic_launcher_background);

            int centerX = fabContainer.getWidth() / 2;
            int centerY = fabContainer.getHeight() / 2;
            int startRadius = 0;
            int endRadius = (int) Math.hypot(fabContainer.getWidth(), fabContainer.getHeight()) / 2;

            fabContainer.setVisibility(View.VISIBLE);
            Log.d("visibility -s", String.valueOf(fabContainer.getVisibility()));
            ViewAnimationUtils
                    .createCircularReveal(fabContainer,centerX,centerY,startRadius,endRadius)
                    .setDuration(500)
                    .start();
        } else {
            //mainFab.setImageResource(R.drawable.ic_launcher_background);
            Log.d(TAG, "else of fabmenuoption");
            int centerX = fabContainer.getWidth() / 2;
            int centerY = fabContainer.getHeight() / 2;
            int startRadius = (int) Math.hypot(fabContainer.getWidth(), fabContainer.getHeight()) / 2;
            int endRadius = 0;
            Animator animator = ViewAnimationUtils.createCircularReveal(fabContainer,centerX,centerY,startRadius,endRadius);
            animator.setDuration(500);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    fabContainer.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            animator.start();
        }
        fabMenuOpen = !fabMenuOpen;
    }


    public void setListenersAndBehaviors() {
        //Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.core_identifiers, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    snackViewFab.hide();
                    bottomSheetFab.hide();
                    mainFab.hide();
                    fabMenuOpen = true;
                    toggleFabMenu();
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    snackViewFab.show();
                    bottomSheetFab.show();
                    mainFab.show();
                }
            }
            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        mainFab.setOnClickListener(this);
        coverageFab.setOnClickListener(this);
        busFab.setOnClickListener(this);

        snackViewFab.setOnClickListener(this);
        bottomSheetFab.setOnClickListener(this);

        closeImageView.setOnTouchListener(this);
        submitButtonLayout.setOnTouchListener(this);
        searchEditText.addTextChangedListener(this);
        versionTextView.setOnClickListener(this);
    }

    private String getGoogleID() {
        AdvertisingIdClient.Info idInfo = null;
        try {
            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
            Log.d(TAG, String.valueOf(idInfo));
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String advertId = null;
        try {
            advertId = idInfo.getId();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        return advertId;
    }

    private void showSnackbar(View view, String message, int duration) {
        final Snackbar snackbar = Snackbar.make(view, message, duration);
        // styling for action text
        snackbar.setActionTextColor(Color.WHITE);
        // styling for rest of text
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(R.id.snackbar_text);
        textView.setTextColor(Color.RED);
        // styling for background of snackbar
        View sbView = snackbarView;
        sbView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        // Set an action on it, and a handler
        snackbar.setAction("DISMISS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }



    @SuppressWarnings({"MissingPermission"})
    public void setLocationManagerListener(String provider) {
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(provider, 1000, 50, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "onLocationChanged");
                currentLocation = location;
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                long timeStamp = System.currentTimeMillis();
                Log.i(TAG, "timestamp" + timeStamp + " latitude" + latitude + " longitude" + longitude);
                coordinate = new LatLng(latitude, longitude);
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
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
            startService(new Intent(this, BackgroundService.class));
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
                    if (ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {
                        googleMap.setMyLocationEnabled(true);
                        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                        provider = locationManager.getBestProvider(new Criteria(), false);
                        setLocationManagerListener(provider);
                        startService(new Intent(this, BackgroundService.class));
                    }

                } else {
                    //todo add pop up to re-request location, since the app is mostly pointless without it
                    //todo show the user a dialog_popup with the steps to follow to give user location permission.

                }
                return;
            }
        }
    }

    public void addHeatMap(double latitude, double longitude) {
        ArrayList<LocationInfo> locationInfoList = dbHandler.readLocationInfo(latitude, longitude);
        LatLng source_loc = null;
        List<LatLng> list = new ArrayList<>();
        for (LocationInfo li : locationInfoList) {
            source_loc = new LatLng(li.getLatitude(), li.getLongitude());
            list.add(source_loc);
        }
        if (list.size() == 0) {
        } else {
            Log.d("HeatMapTileProvider", "Permission response: " + list);
            mProvider = new HeatmapTileProvider.Builder().data(list).build();
            mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }

    public void addHeatMap_1() {
        Log.d(TAG, "HeatMAp");
        //todo check location permission
        coordinate = setLocation();
        ArrayList<LocationInfo> locationInfoList = (ArrayList<LocationInfo>) dbHandler.readLocationInfo_1();
        LatLng source_loc = null;
        List<LatLng> list = new ArrayList<>();
        for (LocationInfo li : locationInfoList) {
            source_loc = new LatLng(li.getLatitude(), li.getLongitude());
            list.add(source_loc);
        }
        if (list.size() == 0) {
            Log.d(TAG, "Whats happening at this point?? What should I do??");
        } else {
            Log.d("HeatMapTileProvider", "Permission response: " + list);
            mProvider = new HeatmapTileProvider.Builder().data(list).build();
            mProvider.setRadius(30);
            mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        new LatLng(coordinate.latitude, coordinate.longitude), 11);
                googleMap.animateCamera(cameraUpdate);

        }

    private void populateDestinationLookUpTable() {
        Log.d("DestLookUp", "Begin");
        String line = "";
        try {
            InputStream is = getResources().openRawResource(R.raw.destination_lookup);
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null) {
                String[] str = line.split(",");
                Log.d("DestLookUp", str[1]);
                dbHandler.addDestinationLookUpInfo(str[0], str[1]);
            }
        } catch (Exception e) {
            Log.d("DestLookUp", "Error: " + e.getLocalizedMessage());
        }
    }

    private void populateDatabaseWithInitialData() {
        Log.d("DBTest", "populateData");
        String line = "";
        try {
            InputStream is = getResources().openRawResource(R.raw.busstop_validation_v1);
            reader = new BufferedReader(new InputStreamReader(is));
        } catch (Exception e) {
            Log.d("DBTest", "Error creating input stream visakhapatnam: " + e.getLocalizedMessage());
            Log.i(TAG, "Reading LocationReadings.csv to db failed");
        }
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

                dbHandler.addPivotTableData(new PivotTableData(identifier, lat, lon, name, brand, address, zipcode, city, district, state));
                Log.i(TAG, "Reading data into table " + identifier + "," + lat + "," + lon + "," + brand + "," + name + "," + address + "," + zipcode + "," + city + "," + state);
            }
        } catch (IOException e) {
            Log.i(TAG, "Reading lat long failed");
            Log.d("DebugTest", "Parsing file: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        try {
            InputStream is = getResources().openRawResource(R.raw.busroutes_validation_v1);
            reader = new BufferedReader(new InputStreamReader(is));
        } catch (Exception e) {
            Log.d("DebugTest", "Error creating input stream bus lines: " + e.getLocalizedMessage());
            Log.i(TAG, "Reading LocationReadings.csv to db failed");
        }
        //prefs.edit().putBoolean("first_run", false).apply();

        try {
            while ((line = reader.readLine()) != null) // Read until end of file
            {
                String[] lines = line.split(",");
                int lineid = Integer.parseInt(lines[0]);
                String busno = lines[1];
                String source_station = lines[2];
                String destination_station = lines[3];
                int direction = Integer.parseInt(lines[4]);
                int sequence = Integer.parseInt(lines[5]);
                //uncommented because of the string to integer cast issue that was arising for line_id.
                //line_id is int initially but the data retrieval from server is done as string.
                //dbHandler.addBusLinesData(new IdentifierBusInfo(lineid, busno, source_station, destination_station, direction, sequence));
                Log.i(TAG, "Reading data into table " + lineid + "," + busno + ","
                        + source_station + "," + destination_station + "," + direction + "," + sequence);
            }
        } catch (IOException e) {
            Log.i(TAG, "Reading lat long failed");
            Log.d("DebugTest", "I/O Error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        //prefs.edit().putBoolean("first_run", false).apply();
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click the button again to exit the application.", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
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
                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_feedback:
                Intent homeIntent = new Intent(this, FeedbackActivity.class);
                startActivity(homeIntent);
                break;
            case R.id.action_exit:
                finish();
            default:
                break;
        }
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.profile) {
        } else if (id == R.id.bus_stations) {

        } else if (id == R.id.explored_loc) {

        } else if (id == R.id.massage) {

        } else if (id == R.id.terms_conditions) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        this.googleMap.setOnMapLongClickListener(this);
        this.googleMap.setOnMarkerClickListener(this);

        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));
            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        boolean permissionAccepted = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            permissionAccepted = false;
        }
        if (permissionAccepted) {
            coordinate = setLocation();
            googleMap.setMyLocationEnabled(true);
            googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        }

        View locationButton = ((View) mapFragment.getView().findViewById(Integer.parseInt("1")).
                getParent()).findViewById(Integer.parseInt("2"));
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        rlp.setMargins(0, 200, 40, 0);

        final String popupText = "Welcome, \n Navigate your way through public transit bus stations (APSRTC) to anywhere in Visakhapatnam.\n ";
        final PopupDialog popupDialog = new PopupDialog(MainActivity.this, popupText, 2);
        popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupDialog.setCancelable(false);
        popupDialog.show();
    }

    private LatLng setLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.d(TAG, "Location: " + location);
                            // Got last known location. In some rare situations this can be null.
                            if (location != null) {
                                // Logic to handle location object
                                coordinate = new LatLng(location.getLatitude(), location.getLongitude());
                                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
                                googleMap.animateCamera(yourLocation);

                            }
                            else{
                                Log.d(TAG, "Location First time: " + location);
                                coordinate = new LatLng(location.getLatitude(), location.getLongitude());
                            }
                        }
                    });
        } catch (SecurityException e) {
            Log.d(TAG, "Location exception: " + e.getMessage());
        }
        return coordinate;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        searchResultLayout.setVisibility(View.VISIBLE);
        if(AppUtils.getFirstGuide(this)) {
            new GuideView.Builder(this)
                    .setTitle("Location Search Box")
                    .setContentText("Please enter the destination location here\n" +
                            "It will automatically fill up the locations\n" +
                            "that you are looking for")
                    .setGravity(Gravity.center)
                    .setTargetView(searchBoxLayout)
                    .setDismissType(DismissType.outside) //optional - default dismissible by TargetView
                    .setGuideListener(new GuideListener() {
                        @Override
                        public void onDismiss(View view) {
                            //TODO ...
                            AppUtils.setFirstGuide(MainActivity.this, false);
                        }
                    })
                    .build()
                    .show();

        }
        user_selected_bustop = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
        Log.d(TAG, "selected marker info: " + marker.getTitle().toUpperCase());
        srcLocation = marker.getTitle().toUpperCase();

        if (dist_metric_layout == 0) {
            if (item_selected_1.equalsIgnoreCase("Bus")) {
                searchResultLayout.setVisibility(View.VISIBLE);
                Toast msg = Toast.makeText(getBaseContext(), "Enter destination", Toast.LENGTH_LONG);
                destination_input_click = 1;
                msg.show();
            }
        }

        if(marker.getTag().toString().equals("Middle")) {
            if(!AppUtils.getIsBusStopTipRemembered(this)) {
                final String popupText = "You can get the bus-numbers that travel " +
                        "to a different destination from this selected bus-stop.";
                final PopupDialog popupDialog = new PopupDialog(MainActivity.this, popupText, 2);
                popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupDialog.setCancelable(false);
                popupDialog.show();
            }
        }
        return false;
    }

    @Override
    public void onDestinationSearchClick(String destination) {
        searchResultClick = true;
        searchEditText.setText(destination);
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        busDestinationSearchResults.clear();
        searchViewAdapter.updateData(busDestinationSearchResults);
        searchViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onMoreInfoClick(String busName, String src, String destination, String lineid) {
        final MoreInfoDialog dialog = new MoreInfoDialog(this, this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Log.d(TAG, src + destination);
        srcLocation = src;
        destLocation = destination;
        bus_no = busName;
        line_id = lineid;
        Log.d(TAG, String.valueOf(srcLocation) + "," + String.valueOf(destLocation) + "," + String.valueOf(bus_no) + "," + String.valueOf(line_id) );
        //dbHandler.getNumberOfStopsBetween(src, destination, busName);
        //numberOfStops.setText(String.valueOf(dbHandler.getNumberOfStopsBetween(src, destination, busName)));

        //testing code below
        //todo uncomment the below line
        //dialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<>();
        Gson gson = new GsonBuilder().create();
        HashMap<String, String> map = new HashMap<String, String>();
        map.put("srcLocation", srcLocation.toUpperCase());
        map.put("destLocation", destLocation.toUpperCase());
        map.put("line_id", String.valueOf(lineid));
        map.put("bus_no", String.valueOf(bus_no));
        wordList.add(map);
        Log.d(TAG, "Sending Data: " + destLocation + "," + srcLocation + "," + line_id);
        params.put("routeList",gson.toJson(wordList));
        Log.d(TAG, params.toString());

        progressView.showLoader();

        client.post(Constants.API_Get_Bus_Route, params, new AsyncHttpResponseHandler() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.d(TAG, "success");
                JSONArray arr= null;
                try {
                    busrouteList.clear();
                    Log.d(TAG, "Getting Bus Route");
                    //List<BusRoutesInfo> markersList = new ArrayList<BusRoutesInfo>();
                    arr = new JSONArray(new String(responseBody));
                    Log.d(TAG, String.valueOf(arr.length()));
                    for(int i=0; i<arr.length();i++) {
                        BusRoutesInfo ib = new BusRoutesInfo();
                        JSONObject obj = (JSONObject)arr.get(i);
                        ib.setStop_name((String) obj.get("stop_name"));
                        ib.setLatitude(Double.parseDouble(String.valueOf(obj.get("latitude"))));
                        ib.setLongitude(Double.parseDouble(String.valueOf(obj.get("longitude"))));
                        Log.d(TAG, "Stop:" + i + "");
                        busrouteList.add(ib);
                    }
                    dialog.show();
                    final TextView numberOfStops = dialog.findViewById(R.id.number_of_stops);
                    numberOfStops.setText(String.valueOf(busrouteList.size()));
                    /*ArrayList busList = new ArrayList(markersList);
                    Log.d("BottomSheetTest", "Size: " + busList.size());
                    if (busList.size() == 0) {
//                    showAlertDialog();
                        Toast msg = Toast.makeText(getBaseContext(), "No results", Toast.LENGTH_LONG);
                        msg.show();
                    } else if (busList.size() > 0) {
                        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        bottomSheetAdapter = new BottomSheetAdapter(busList, bottomSheetClickListener);
                        bottomSheetRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                        bottomSheetRV.setAdapter(bottomSheetAdapter);
                    }*/

                    //fab_info.setVisibility(view.VISIBLE);
                } catch (JSONException e) {
                    progressView.hideLoader();
                    e.printStackTrace();
                    Log.d(TAG, "success in catch");
                }
                //Log.d(TAG,arr.toString());
                //Toast.makeText(getApplicationContext(), "successful - but nothing happenin on ui: ", Toast.LENGTH_SHORT).show();
                progressView.hideLoader();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "Failed to send latlong to server");
                progressView.hideLoader();
            }
        });
        //testing code above
    }



    @Override
    public void trackMyPath() {
        googleMap.clear();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        searchResultLayout.setVisibility(View.GONE);

        //List<PivotTableData> markers = dbHandler.getDestinationCoordinates(destLocation, srcLocation, bus_no);
        List<BusRoutesInfo> markers = new ArrayList<BusRoutesInfo>();
        markers = busrouteList;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Log.d(TAG, String.valueOf(markers));

        //distanceCalcLayout.setVisibility(View.VISIBLE);

        dist_metric_layout = 1;
        double distance = 0.0;
        int i = 0;
        double user_lat, user_long, next_stop_lat = 0, next_stop_long = 0;

        if (markers.size() == 0) {
            //distanceCalcLayout.setVisibility(View.VISIBLE);
            Toast msg = Toast.makeText(getBaseContext(), "Try Again. If no results found, give us a fragment_feedback by clicking on FEEDBACK on top-right corner...", Toast.LENGTH_LONG);
            msg.show();
        } else {
            BusRoutesInfo src = markers.get(0);
            BusRoutesInfo dest = markers.get(markers.size() - 1);
            builder.include(new LatLng(src.latitude+0.015, src.longitude+0.015));
            builder.include(new LatLng(dest.latitude+0.01, dest.longitude+0.01));

            for (BusRoutesInfo marker : markers) {
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(marker.latitude, marker.longitude))
                        .title(marker.stop_name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))).setTag("Middle");
                builder.include(new LatLng(marker.latitude, marker.longitude));

                if (i == 0) {
                    //comment the below line when going live.
                    user_lat = 17.724767;
                    user_long = 83.306253;
                    //todo use the below user_lat and user_long for actual distance calculation.

                    //comment the below line when going live.
                    //user_lat = coordinate.latitude;
                    //user_long = coordinate.longitude;
                    //user_lat = currentLocation.getLatitude();
                    //user_long = currentLocation.getLongitude();
                    //Log.d(TAG, "User Location in distance calc: " + currentLocation.getLatitude() + "," + currentLocation.getLongitude());
                    Log.d(TAG, "User Location in distance calc: " + user_lat + "," + user_long);
                    next_stop_lat = marker.latitude;
                    next_stop_long = marker.longitude;
                    i++;
                } else {
                    user_lat = next_stop_lat;
                    user_long = next_stop_long;
                    next_stop_lat = marker.latitude;
                    next_stop_long = marker.longitude;
                }

                double theta = user_long - next_stop_long;
                double dist = Math.sin(deg2rad(next_stop_lat)) * Math.sin(deg2rad(user_lat)) + Math.cos(deg2rad(user_lat)) *
                        Math.cos(deg2rad(next_stop_lat)) * Math.cos(deg2rad(theta));
                dist = Math.acos(dist);
                dist = rad2deg(dist);
                distance = distance + (dist*60*1.1515);
                Log.d(TAG, String.valueOf(dist*60*1.1515));
            }

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(dest.latitude, dest.longitude))
                    .title(dest.stop_name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))).setTag("Dest");

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(user_selected_bustop.latitude, user_selected_bustop.longitude))
                    .title(srcLocation.toUpperCase())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))).setTag("Src");

            distanceTextView.setText(" Selected Bus No:" + bus_no + "\n Distance: " + Math.round(distance*1.6) + "Km (approx)");
            LatLngBounds bounds = builder.build();
            int padding = 50;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.animateCamera(cameraUpdate);
            searchResultLayout.setVisibility(View.GONE);

            coordinate = setLocation();
        }
    }

    public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    public double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (v.getId()) {
            case R.id.submit_button_layout:
                if (event.getAction() != MotionEvent.ACTION_DOWN) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        animPressRealease = AnimationUtils.loadAnimation(this, R.anim.alpha_animation_released);
                        submitButtonTextView.startAnimation(animPressRealease);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                View view = getCurrentFocus();
                                if (view != null) {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }

                                //working code below
                                /*ArrayList busList = new ArrayList(dbHandler.getBusLinesData(srcLocation, searchEditText.getText().toString().toUpperCase()));
                                Log.d("BottomSheetTest", "Size: " + busList.size());
                                if (busList.size() == 0) {
                //                    showAlertDialog();
                                    Toast msg = Toast.makeText(getBaseContext(), "No results", Toast.LENGTH_LONG);
                                    msg.show();
                                } else if (busList.size() > 0) {
                                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                    bottomSheetAdapter = new BottomSheetAdapter(busList, bottomSheetClickListener);
                                    bottomSheetRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                    bottomSheetRV.setAdapter(bottomSheetAdapter);
                                }*/
                                //working code above

                                //testing code below
                                //srcLocation = "BHIMILI POLICE STATION";
                                //todo add a dialog window (uncomment the below)
                                //dialog.show();
                                destLocation = searchEditText.getText().toString();
                                AsyncHttpClient client = new AsyncHttpClient();
                                final RequestParams params = new RequestParams();
                                ArrayList<HashMap<String, String>> wordList;
                                wordList = new ArrayList<HashMap<String, String>>();
                                Gson gson = new GsonBuilder().create();
                                HashMap<String, String> map = new HashMap<String, String>();
                                map.put("srcLocation", srcLocation.toUpperCase());
                                map.put("destLocation", destLocation.toUpperCase());
                                wordList.add(map);
                                Log.d(TAG, "Sending Data: " + destLocation + "," + srcLocation);
                                params.put("busList",gson.toJson(wordList));
                                Log.d(TAG, params.toString());
                                progressView.showLoader();
                                client.post(Constants.API_Get_Bus_List, params, new AsyncHttpResponseHandler() {
                                    @SuppressLint("RestrictedApi")
                                    @Override
                                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                        JSONArray arr= null;
                                        try {
                                            Log.d(TAG, "success in try");
                                            List<IdentifierBusInfo> markersList = new ArrayList<IdentifierBusInfo>();
                                            arr = new JSONArray(new String(responseBody));
                                            Log.d(TAG, String.valueOf(arr.length()));
                                            for(int i=0; i<arr.length();i++) {
                                                IdentifierBusInfo ib = new IdentifierBusInfo();
                                                JSONObject obj = (JSONObject)arr.get(i);
                                                line_id = (String) obj.get("line_id");
                                                bus_no = (String) obj.get("bus_no");
                                                ib.setLineid((String) obj.get("line_id"));
                                                ib.setBusno((String) obj.get("bus_no"));
                                                ib.setSourceLocation((String) obj.get("source_station"));
                                                ib.setDestinationLocation((String) obj.get("destination_station"));
                                                markersList.add(ib);
                                            }

                                            ArrayList busList = new ArrayList(markersList);
                                            if (busList.size() == 0) {
                                                Toast msg = Toast.makeText(getBaseContext(), "No results", Toast.LENGTH_LONG);
                                                msg.show();
                                            } else if (busList.size() > 0) {
                                                Log.d(TAG, String.valueOf(busList.get(0)));
                                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                                                bottomSheetAdapter = new BottomSheetAdapter(busList, MainActivity.this);
                                                bottomSheetRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                                bottomSheetRV.setAdapter(bottomSheetAdapter);
                                            }

                                            //fab_info.setVisibility(view.VISIBLE);
                                        } catch (JSONException e) {
                                            progressView.hideLoader();
                                            e.printStackTrace();
                                            Log.d(TAG, "success in catch");
                                        }
                                        progressView.hideLoader();
                                        //Log.d(TAG,arr.toString());
                                        //Toast.makeText(getApplicationContext(), "successful - but nothing happenin on ui: ", Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                        progressView.hideLoader();
                                        Log.d(TAG, "Failed to send latlong to server");
                                    }
                                });
                                //testing code above
                            }
                        }, 200);
                        break;
                    }
                }
                animPressDown = AnimationUtils.loadAnimation(this, R.anim.alpha_animation_pressed);
                submitButtonTextView.startAnimation(animPressDown);
                break;
            case R.id.close_bottomsheet_imageview:
                if (event.getAction() != MotionEvent.ACTION_DOWN) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        animPressRealease = AnimationUtils.loadAnimation(this, R.anim.alpha_animation_released);
                        closeImageView.startAnimation(animPressRealease);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                            }
                        }, 200);
                        break;
                    }
                }
                animPressDown = AnimationUtils.loadAnimation(this, R.anim.alpha_animation_pressed);
                closeImageView.startAnimation(animPressDown);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.snack_view_fab:
                //todo display dialog_popup instead of snackbar to ask users to perform long click for bus_stop locations.
                String message = "Long press on the map to locate the bus stop (accurately) in your path!";
                int duration = Snackbar.LENGTH_INDEFINITE;
                showSnackbar(v, message, duration);
                //snackViewFab - click to point out the busstop. does this work???
                break;
            case R.id.show_bottomsheet_fab:
                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
            case R.id.main_fab:
                toggleFabMenu();
                Log.d(TAG, "mainFab clicked");
                break;
            case R.id.bus_fab:
                googleMap.setOnMapLongClickListener(this);
                getBusList();
                break;
            case R.id.coverage_fab:
                if(destination_input_click == 1) {
                    searchResultLayout.setVisibility(View.GONE);
                    destination_input_click = 0;
                }
                if(dist_metric_layout == 1) {
                    distanceCalcLayout.setVisibility(View.GONE);
                    dist_metric_layout = 0;
                }
                googleMap.setOnMapLongClickListener(null);
                searchResultLayout.setVisibility(View.GONE);

                if(!AppUtils.getIsCoverageTipRemembered(this)) {
                    final String popupText = "You can see all the places you have covered in the last 1 month. \n\n" +
                            "Zoom in/out to know more about your explorations!";
                    final PopupDialog popupDialog = new PopupDialog(MainActivity.this, popupText, 1);
                    popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    popupDialog.setCancelable(false);
                    popupDialog.show();
                }

                //toggleFabMenu();
                googleMap.clear();
                fabContainer.setVisibility(View.GONE);
                addHeatMap_1();
                Log.d(TAG, "addHeatMap_1 passed");
                break;
            case R.id.version_textview:
                Log.d("VersionMenu", "Count: " + versionClickCount);
                versionClickCount++;
                if (versionClickCount == 10) {
                    Log.d("VersionMenu", "Success");
                    dbHandler.exportDB();
                }
                break;
        }
    }

    //Search EditText Watcher
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (!searchResultClick && searchEditText.getText().toString().length() > 2) {
                        busDestinationSearchResults = dbHandler.destinationLookup(searchEditText.getText().toString());
                        Log.d(TAG, String.valueOf(busDestinationSearchResults));
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!searchResultClick && busDestinationSearchResults != null) {
                                //search.setState(BottomSheetBehavior.STATE_EXPANDED);
                                Log.d(TAG, String.valueOf(busDestinationSearchResults));
                                searchViewAdapter = new SearchViewAdapter(busDestinationSearchResults, MainActivity.this);
                                Log.d(TAG, String.valueOf(searchViewAdapter));
                                searchViewRV.setAdapter(searchViewAdapter);
                                searchViewRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                searchViewAdapter.notifyDataSetChanged();
                                Log.d(TAG, String.valueOf(busDestinationSearchResults.size()));
                            }
                        }
                    });
                    searchResultClick = false;
                } catch (Exception e) {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
        thread.start();
    }

    //Check Permission
    private void checkSelfPermission() {
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
    }

    private void getBusList(){
        map_input_status = 0;
        destination_input_click = 0;
        dist_metric_layout = 0;
        distanceCalcLayout.setVisibility(View.GONE);
        //if (prefs_bus.getBoolean("first_run", true)) {
//        Log.d(TAG, "Displaying text");
        googleMap.clear();
        //toggleFabMenu();
        if (fabMenuOpen) {
            Log.d(TAG, "fabMenuOpen is false");
            fabContainer.setVisibility(View.GONE);
            toggleFabMenu();
            List<LatLng> latLngList = new ArrayList<LatLng>();
            String line = "";
            HashMap<LatLng, String> mData = new HashMap<>();
            Marker m;
            coordinate = setLocation();
            //coordinate= new LatLng(17.724767,83.306253);
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                setLocationManagerListener(LocationManager.NETWORK_PROVIDER);
            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                setLocationManagerListener(LocationManager.GPS_PROVIDER);
            }
            //uncomment the below when going live.
            //double latitude = coordinate.latitude;
            //double longitude = coordinate.longitude;
            //testing the data. Assigning latnlong manually for now.
            //17.694948, 83.292365 - old head post office

            //comment the below when going live
            double latitude = 17.724767;
            double longitude = 83.306253;
            ///* Working code below
                    /*item_selected_1 = "Bus";
                    List<PivotTableData> markers = dbHandler.getFromPivotTableData(item_selected_1, latitude, longitude);
                    int busstops_1 = markers.size();
                    Log.d("Export", "Sizeses:" + busstops_1);
                    if (item_selected_1.equalsIgnoreCase("Bus")) {
                        googleMap.getUiSettings().setMapToolbarEnabled(false);
                        searchResultLayout = (LinearLayout) findViewById(R.id.linearlayout);

                        //todo get rid of this when done testing (because the camera is already moved to the user location.
                        LatLng latLng = new LatLng(latitude, longitude);
                        Log.d(TAG, "Selected location " + latitude + "," + longitude);
                        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                        googleMap.animateCamera(cameraUpdate);
                        //todo no seriously, get rid of this block

                        if (markers.size() == 0) {
                            Toast msg = Toast.makeText(getBaseContext(), "No results found", Toast.LENGTH_LONG);
                            msg.show();
                        } else if (markers.size() == 1) {
                            //searchResultLayout.setVisibility(View.VISIBLE);
                            srcLocation = markers.get(0).name.toUpperCase();
                            Toast msg = Toast.makeText(getBaseContext(), "Enter destination", Toast.LENGTH_LONG);
                            msg.show();
                        } else {
                            //searchResultLayout.setVisibility(view.VISIBLE);
                            Toast msg = Toast.makeText(getBaseContext(), "Select source bus stop marker", Toast.LENGTH_LONG);
                            msg.show();
                        }
                    }

                    LatLng latLng = null;
                    Log.d(TAG, String.valueOf(markers));
                    for (PivotTableData marker : markers) {
                        latLng = new LatLng(marker.latitude, marker.longitude);
                        String name = marker.name;
                        googleMap.addMarker(new MarkerOptions().position(latLng)
                                .title(name.toUpperCase()));
                        mData.put(latLng, name);
                        Log.d(TAG, "Displayed Stops " + name);
                        latLngList.add(latLng);
                    }
                    setLocation();
                } else {
                    fabContainer.setVisibility(View.GONE);
                }*/
            //*/ working Code (above)

            //testing below
            //List<LatLng> latlong = new ArrayList<LatLng>();
            //uncomment the below line
            //dialog.show();
            LatLng latlong = new LatLng(latitude, longitude);
            AsyncHttpClient client = new AsyncHttpClient();
            final RequestParams params = new RequestParams();
            Gson gson = new GsonBuilder().create();
            //Use GSON to serialize Array List to JSON
            Log.d(TAG, gson.toJson(latlong));
            //params.put("latitude", latlong.latitude);
            //params.put("longitude", latlong.longitude);
            params.put("userLocation",gson.toJson(latlong));
            Log.d(TAG, "params " + params.toString());
            progressView.showLoader();
            client.post(Constants.API_Get_Pivot_Data, params, new AsyncHttpResponseHandler() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    JSONArray arr= null;
                    progressView.hideLoader();
                    Log.d(TAG, "Successful retrival of bustops around the user");
                    try {
                        Log.d(TAG, "no of busstops around the passed coordinates");
                        arr = new JSONArray(new String(responseBody));
                        if(arr.length() > 0) {
                            for (int i = 0; i < arr.length(); i++) {
                                JSONObject obj = (JSONObject) arr.get(i);
                                String stop_lat = (String) obj.get("latitude");
                                String stop_long = (String) obj.get("longitude");
                                LatLng stop_details = new LatLng(Double.parseDouble(stop_lat), Double.parseDouble(stop_long));
                                String stop_name = (String) obj.get("stop_name");
                                Log.d(TAG, "Busstop_AroundUser:" + (String) obj.get("stop_name") + "," + (String) obj.get("latitude") + "," + (String) obj.get("longitude") );

                                googleMap.addMarker(new MarkerOptions().position(stop_details)
                                        .title(stop_name.toUpperCase())).setTag("Normal");
                                srcLocation = (String) ((String) obj.get("stop_name")).toUpperCase();
                                if(i == arr.length()-1){
                                    //todo get rid of this when done testing (because the camera is already moved to the user location.
                                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(stop_details, 15);
                                    googleMap.animateCamera(cameraUpdate);
                                    //todo no seriously, get rid of this block
                                }
                            }

                            int markers = arr.length();
                            if (markers == 0) {
                                Toast msg = Toast.makeText(getBaseContext(), "No results found", Toast.LENGTH_LONG);
                                msg.show();
                            } else if (markers >= 1) {
                                Toast msg = Toast.makeText(getBaseContext(), "Select source bus stop marker", Toast.LENGTH_LONG);
                                msg.show();
                            }
                            if(!AppUtils.getIsBusTipRemembered(MainActivity.this)) {
                                final String popupText = "Select a bus stop on the map and provide the destination to get a list of buses " +
                                        "(bus numbers) that travel from the selected bus stop to your destination. \n \n " +
                                        "You can also get the path the selected bus takes to reach your destination!!";
                                final PopupDialog popupDialog = new PopupDialog(MainActivity.this, popupText, 0);
                                popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                popupDialog.setCancelable(false);
                                popupDialog.show();
                            }
                        }
                        else {
                            final String popupText = "We do not have a bus-stop around you.\n" +
                                    "If we are missing a bus stop near you, please long press on the map " +
                                    "at the location of the bus-stop and share it with us.";
                            final PopupDialog popupDialog = new PopupDialog(MainActivity.this, popupText, 3);
                            popupDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                            popupDialog.setCancelable(false);
                            popupDialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.d(TAG, "In Catch block of userLocation");
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    progressView.hideLoader();
                    Log.d(TAG, "Failed to retrieve data. are you connected to internet?");
                }
            });
        }
        //testing above
    }

    @Override
    public void onMapLongClick(LatLng point) {
        if(map_input_status == 0) {
            //user can submit one bus stop at a time. map_input_status checks if the user has already onpressed on the map.
            user_loc_input = point;
            distanceCalcLayout.setVisibility(View.GONE);
            Marker marker = googleMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title("Selected Location"));
            marker.setTag("Added");
            marker.showInfoWindow();
            map_input_status = 1;
            hashMapMarker.put("Selected Location", marker);
            Log.d("markers on map: ", String.valueOf(hashMapMarker.size()));
//            submitLayout.setVisibility(View.VISIBLE);
        } else {
            Marker marker = hashMapMarker.get("Selected Location");
            Log.d(TAG, marker.getTitle());
            marker.remove();
            hashMapMarker.remove("SelectedLocation");

            user_loc_input = point;
            distanceCalcLayout.setVisibility(View.GONE);
//            submitLayout.setVisibility(View.VISIBLE);
            marker = googleMap.addMarker(new MarkerOptions()
                    .position(point)
                    .title("Selected Location"));
            marker.setTag("Added");
            marker.showInfoWindow();
            hashMapMarker.put("Selected Location", marker);
            Log.d("markers on map: ", String.valueOf(hashMapMarker.size()));
        }

        final LocationSubmitDialog locationSubmitDialog = new LocationSubmitDialog(this,
                dist_metric_layout, distanceCalcLayout, user_loc_input, map_input_status, destination_input_click,
                searchResultLayout, hashMapMarker, uuid, dbHandler);
        locationSubmitDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        locationSubmitDialog.setCancelable(false);
        locationSubmitDialog.show();
    }
}
