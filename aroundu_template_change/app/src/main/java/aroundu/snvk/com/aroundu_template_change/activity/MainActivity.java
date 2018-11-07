package aroundu.snvk.com.aroundu_template_change.activity;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aroundu.snvk.com.aroundu_template_change.PivotTableData;
import aroundu.snvk.com.aroundu_template_change.R;
import aroundu.snvk.com.aroundu_template_change.adapters.BottomSheetAdapter;
import aroundu.snvk.com.aroundu_template_change.adapters.SearchViewAdapter;
import aroundu.snvk.com.aroundu_template_change.database.DBHandler;
import aroundu.snvk.com.aroundu_template_change.interfaces.BottomSheetClickListener;
import aroundu.snvk.com.aroundu_template_change.interfaces.RecyclerViewClickListener;
import aroundu.snvk.com.aroundu_template_change.interfaces.TrackingListener;
import aroundu.snvk.com.aroundu_template_change.service.BackgroundService;
import aroundu.snvk.com.aroundu_template_change.view.MoreInfoDialog;
import aroundu.snvk.com.aroundu_template_change.vo.IdentifierBusInfo;
import aroundu.snvk.com.aroundu_template_change.vo.LocationInfo;
import aroundu.snvk.com.aroundu_template_change.vo.ReadUserLocation;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemSelectedListener,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener, RecyclerViewClickListener, BottomSheetClickListener, TrackingListener {

    private static final String TAG = "TestingToolbar";
    BufferedReader reader = null;
    private GoogleMap googleMap;
    private FusedLocationProviderClient mFusedLocationClient;
    public String item_selected_1 = "Select...";
    private ClusterManager<LocationInfo> mClusterManager;
    LocationManager locationManager;
    TileOverlay mOverlay;
    HeatmapTileProvider mProvider;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    String provider;
    Button btn;
    EditText eText;
    ConstraintLayout constraint_Layout_1;
    LinearLayout linear_Layout_1;
    private DBHandler dbHandler;
    private Handler mHandler;
    private RecyclerView bottomSheetRV, searchViewRV;
    private BottomSheetAdapter bottomSheetAdapter;
    private SearchViewAdapter searchViewAdapter;
    private Location currentLocation = null;
    private String srcLocation = "";
    private String destLocation = "";
    private RecyclerViewClickListener recyclerViewClickListener;
    private BottomSheetClickListener bottomSheetClickListener;
    private ArrayList<String> busDestinationSearchResults;
    private boolean searchResultClick = false;
    private TextView version;
    private Spinner core_spinner;
    private FloatingActionButton fab;
    private FloatingActionButton fab1;
    private FloatingActionButton bus_fab;
    private FloatingActionButton coverage_fab;
    private Toolbar toolbar;
    private LinearLayout llBottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    ToggleButton toggle;
    private Handler backgroundHandler = null;
    private HandlerThread mHandlerThread = null;
    private int versionClickCount = 0;
    private boolean expanded = false;
    private boolean fabMenuOpen = false;
    private LinearLayout fabContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//
        mHandler = new Handler(Looper.getMainLooper());
        mHandlerThread = new HandlerThread("BackgroundThread");
        mHandlerThread.start();
        backgroundHandler = new Handler(mHandlerThread.getLooper());

        recyclerViewClickListener = this;
        bottomSheetClickListener = this;

        dbHandler = DBHandler.getInstance(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        setViews();
        setListenersAndBehaviors();
        setSupportActionBar(toolbar);

        checkLocationPermission();

        //initial insert of data
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        if (prefs.getBoolean("first_run", true)) {
            Log.d("Export", "Populating the database");

            backgroundHandler.post(new Runnable() {
                @Override
                public void run() {
//                    Log.d("DestLookUp", "Before");
//                    populateDatabaseWithInitialData(prefs);
//                    Log.d("DestLookUp", "After");
//                    populateDestinationLookUpTable();

                    dbHandler.createDataBase();

                    prefs.edit().putBoolean("first_run", false).apply();
                }
            });
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

        //todo check the number of bustops available within 1mile radius of the user. if there is 1 busstop, consider that busstop as the sourcelocation. if there are none,
        //display a window saying "No Bustops near u!". If there are more than 1, show a popup window asking the user to select a bus stop from the map.

        //todo if there are no buslines from the source to the destination, show a popup window saying, 'no information is available. If there is a bus line, let us know...'
        //when the user clicks yes, he is directed to a different activity that is used to collect the missing data from the users.


    }

    @SuppressLint("WrongViewCast")
    public void setViews() {
        //commented core_spinner to remove spinner. If fails, uncomment
        core_spinner = (Spinner) findViewById(R.id.core_spinner);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fabContainer = (LinearLayout) findViewById(R.id.fabContainerLayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        bus_fab = (FloatingActionButton) findViewById(R.id.bus_fab);
        coverage_fab = (FloatingActionButton) findViewById(R.id.coverage_fab);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        bottomSheetRV = (RecyclerView) findViewById(R.id.recycler_view);
        searchViewRV = (RecyclerView) findViewById(R.id.search_recycler_view);

        eText = (EditText) findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.button);
        version = (TextView) findViewById(R.id.version_number);
//        toggle = (ToggleButton) findViewById(R.id.toggBtn);

        //Animations to fab
        Animation show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        Animation hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);

        //expanding fab
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin += (int) (fab1.getWidth() * 1.7);
        layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(show_fab_1);
        fab1.setClickable(true);
        coverage_fab.setClickable(true);
        bus_fab.setClickable(true);

        //main_fab1
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabMenu();
                Log.d(TAG, "fab1 clicked");
            }
        });

        bus_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toggleFabMenu();
                if (fabMenuOpen) {
                    Log.d(TAG, "fabMenuOpen is false");
                    fabContainer.setVisibility(View.GONE);
                    toggleFabMenu();

                    List<LatLng> latLngList = new ArrayList<LatLng>();
                    String line = "";
                    HashMap<LatLng, String> mData = new HashMap<>();
                    Marker m;

                    //testing the data. Assigning latnlong manually for now.
                    double latitude = 17.724742;
                    double longitude = 83.306074;

//below coordinates are to test the scinario where there are no bus stations near the user location.
                    //double latitude = 29.587433;
                    //double longitude = -95.682365;
                    item_selected_1 = "Bus";
                    List<PivotTableData> markers = dbHandler.getFromPivotTableData(item_selected_1, latitude, longitude);
                    int busstops_1 = markers.size();
                    Log.d("Export", "Sizeses:" + busstops_1);

                    Log.d("Export", "item_selected:" + item_selected_1);
                    if (item_selected_1.equalsIgnoreCase("Bus")) {
                        googleMap.getUiSettings().setMapToolbarEnabled(false);
                        fab.setVisibility(view.VISIBLE);
                        String message = "Bus Stop not located accurately? Long click (2sec) on the map to Locate it";
                        int duration = Snackbar.LENGTH_INDEFINITE;
                        final Snackbar snackbar = Snackbar.make(view, message, duration);
                        showSnackbar(view, message, duration);

                        //todo get rid of this when done testing
                        //LatLng latLng = new LatLng(latitude, longitude);
                        //CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                        //googleMap.animateCamera(cameraUpdate);
                        //todo no seriously, get rid of this block

                        if (markers.size() == 0) {
                            Toast msg = Toast.makeText(getBaseContext(), "No results found", Toast.LENGTH_LONG);
                            msg.show();
                        } else if (markers.size() == 1) {
                            linear_Layout_1.setVisibility(View.VISIBLE);
                            srcLocation = markers.get(0).name.toUpperCase();
                            Toast msg = Toast.makeText(getBaseContext(), "Enter destination", Toast.LENGTH_LONG);
                            msg.show();
                        } else {
                            Toast msg = Toast.makeText(getBaseContext(), "Select source bus stop marker", Toast.LENGTH_LONG);
                            msg.show();
                        }
                    }

                    LatLng latLng = null;
                    Log.d(TAG, String.valueOf(markers));
                    for (PivotTableData marker : markers) {
                        latLng = new LatLng(marker.latitude, marker.longitude);
                        String name = marker.name;

                        mData.put(latLng, name);

                        latLngList.add(latLng);
                    }
                    Log.d(TAG, "Here I am" + String.valueOf(mData));
                    Log.d(TAG, "Rock you like a hurricane");

                    for (LatLng li : mData.keySet()) {
                        Log.d(TAG, "Display" + mData.get(li) + "" + li);
                        googleMap.addMarker(new MarkerOptions().position(li).title(String.valueOf(mData.get(li))));
                        Log.d(TAG, "Line drawing! possible?");
                /*lines = googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(17.74748, 83.346268), new LatLng(17.74766, 83.34633))
                        .width(5)
                        .color(Color.RED));*/
                        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(li, 15));
                    }
                    setLocation();

                }
                else {
                    fabContainer.setVisibility(View.GONE);

                }
            }
        });

        coverage_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //toggleFabMenu();
                fabContainer.setVisibility(View.GONE);
                addHeatMap_1();
                Log.d(TAG, "addHeatMap_1 passed");
            }
        });
    }

    //This is all for the animation for fab
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toggleFabMenu() {
        if (!fabMenuOpen) {
            //fab1.setImageResource(R.drawable.ic_launcher_background);

            int centerX = fabContainer.getWidth()/2;
            int centerY = fabContainer.getHeight()/2;
            int startRadius = 0;
            int endRadius = (int) Math.hypot(fabContainer.getWidth(), fabContainer.getHeight()) / 2;

            fabContainer.setVisibility(View.VISIBLE);
            Log.d("visibility -s", String.valueOf(fabContainer.getVisibility()));
            ViewAnimationUtils
                    .createCircularReveal(
                            fabContainer,
                            centerX,
                            centerY,
                            startRadius,
                            endRadius
                    )
                    .setDuration(1000)
                    .start();
        } else {
            //fab1.setImageResource(R.drawable.ic_launcher_background);
            Log.d(TAG, "else of fabmenuoption");
            int centerX = fabContainer.getWidth() / 2;
            int centerY = fabContainer.getHeight() / 2;
            int startRadius = (int) Math.hypot(fabContainer.getWidth(), fabContainer.getHeight()) / 2;
            int endRadius = 0;

            Animator animator = ViewAnimationUtils
                    .createCircularReveal(
                            fabContainer,
                            centerX,
                            centerY,
                            startRadius,
                            endRadius
                    );
            animator.setDuration(1000);
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
        //commented core_spinner to remove spinner. If fails, uncomment
        core_spinner.setAdapter(adapter);
        core_spinner.setOnItemSelectedListener(this);

        //Floating Action
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo use this section to display the bus routes from source to destination that are provided by the user.
                //get the closest bus stations from the user and the destination location the user provided. Use that info to display the list in this bottom up.
                //Snackbar.make(view, "Missing a BusStop? Locate it on the map!", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                String message = "Missing Bus stop? or Bus stop location not accurate? Long click (2sec) on the map to locate it. Thank you!";
                int duration = Snackbar.LENGTH_INDEFINITE;

                showSnackbar(view, message, duration);

                //fab.setBackgroundTintList(ColorStateList.valueOf(5)); //in normal state
                //fab.setRippleColor(10); //in pressed state

                //display toggle
                //toggle.toggle();
                //toggle.setTextOff("TOGGLE ON");

                //fab - click to point out the busstop. does this work???
                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    public void onMapClick(LatLng point) {
                        Toast.makeText(getBaseContext(),
                                point.latitude + ", " + point.longitude,
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);
        bottomSheetBehavior.setHideable(true);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    fab.hide();
                    expanded = true;
                }
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    fab.show();
                    expanded = false;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        eText.addTextChangedListener(new TextWatcher() {
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
                            if (!searchResultClick && eText.getText().toString().length() > 2) {
                                busDestinationSearchResults = dbHandler.destinationLookup(eText.getText().toString());
                            }

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (!searchResultClick && busDestinationSearchResults != null) {
                                        searchViewAdapter = new SearchViewAdapter(busDestinationSearchResults, recyclerViewClickListener);
                                        searchViewRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                        searchViewRV.setAdapter(searchViewAdapter);
                                        searchViewAdapter.notifyDataSetChanged();
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
        });

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                ArrayList busList = new ArrayList(dbHandler.getBusLinesData(srcLocation, eText.getText().toString().toUpperCase()));
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
                }

//                Intent intent = new Intent(v.getContext(), HeatMapActivity.class);
//                startActivity(intent);
            }
        });

        version.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("VersionMenu", "Count: " + versionClickCount);
                versionClickCount++;
                if (versionClickCount == 10) {
                    Log.d("VersionMenu", "Success");
                    dbHandler.exportDB();
                }
            }
        });

    }

    private void showSnackbar(View view, String message, int duration) {
        final Snackbar snackbar = Snackbar.make(view, message, duration);

        // styling for action text
        snackbar.setActionTextColor(Color.WHITE);

// styling for rest of text
        View snackbarView = snackbar.getView();
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(Color.RED);

// styling for background of snackbar
        View sbView = snackbarView;
        sbView.setBackgroundColor(Color.BLUE);

        // Set an action on it, and a handler
        snackbar.setAction("DISMISS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });

        snackbar.show();

    }

    public void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Title");

// Set up the input
        final EditText input = new EditText(this);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        builder.setView(input);

// Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                m_Text = input.getText().toString();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    @SuppressWarnings({"MissingPermission"})
    public void setLocationManagerListener(String provider) {
        //locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(provider, 100, 0, new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.i(TAG, "onLocationChanged");
                currentLocation = location;
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                long timeStamp = System.currentTimeMillis();
                Log.i(TAG, "timestamp" + timeStamp + " latitude" + latitude + " longitude" + longitude);
                //LatLng present_Loc = new LatLng(latitude, longitude);
                //add location data into table - locationinfo.
                //addLocationInfoToDB(latitude, longitude, timeStamp);
                //reading latlong from table - locationinfo.
                //setUpClusterer(latitude, longitude);

                //todo rework logic to prevent massive bogging down of system
                //addHeatMap(latitude, longitude);
                //displayLocationInfo(latitude, longitude);
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
                        startService(new Intent(this, BackgroundService.class));
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

    public void addHeatMap(double latitude, double longitude) {
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15f));
        ArrayList<LocationInfo> locationInfoList = dbHandler.readLocationInfo(latitude, longitude);
        LatLng source_loc = null;
        List<LatLng> list = new ArrayList<>();
        for (LocationInfo li : locationInfoList) {
            source_loc = new LatLng(li.getLatitude(), li.getLongitude());
            list.add(source_loc);
        }
        // Create a heat map tile provider, passing it the latlngs.

        if (list.size() == 0) {

        } else {
            Log.d("HeatMapTileProvider", "Permission response: " + list);
            mProvider = new HeatmapTileProvider.Builder()
                    .data(list)
                    .build();
            // Add a tile overlay to the map, using the heat map tile provider.
            mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }

    public void addHeatMap_1() {
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15f));
        Log.d("HeatMap", "HeatMAp");
        ArrayList<LocationInfo> locationInfoList = (ArrayList<LocationInfo>) dbHandler.readLocationInfo_1();
        //Log.d("HeatMapTileProvider", locationInfoList.size());
        LatLng source_loc = null;
        List<LatLng> list = new ArrayList<>();
        for (LocationInfo li : locationInfoList) {
            source_loc = new LatLng(li.getLatitude(), li.getLongitude());
            list.add(source_loc);
        }
        // Create a heat map tile provider, passing it the latlngs.

        if (list.size() == 0) {

        } else {
            Log.d("HeatMapTileProvider", "Permission response: " + list);
            mProvider = new HeatmapTileProvider.Builder()
                    .data(list)
                    .build();
            // Add a tile overlay to the map, using the heat map tile provider.
            mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
    }


    private void setUpClusterer(double latitude, double longitude) {
        // Position the map.
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15f));

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


    public void addLocationInfoToDB(final double latitude, final double longitude, final long timeStamp) {
        // adding the location data into LocationInfo table.
        backgroundHandler.post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "timestamp-start" + timeStamp + " latitude" + latitude + " longitude" + longitude);
                LocationInfo li = new LocationInfo();
                li.setTime_stamp(timeStamp);
                li.setLatitude(latitude);
                li.setLongitude(longitude);
                dbHandler.addLocationInfo(li);
            }
        });
    }


    public void displayLocationInfo(double latitude, double longitude) {
        ArrayList<LocationInfo> locationInfoList = dbHandler.readLocationInfo(latitude, longitude);
        for (LocationInfo li : locationInfoList) {
            //mMap.addMarker(new MarkerOptions().position(new LatLng(li.getLatitude(), li.getLongitude())));
            Log.i(TAG, " Points to Display from displayLocationInfo: Latitude:" + li.getLatitude() + "longitude " + li.getLongitude());
        }
        //db.TotalCount();
        mClusterManager.addItems(locationInfoList);
    }

    private void populateDestinationLookUpTable(){
        Log.d("DestLookUp", "Begin");
        String line = "";
        try {
            InputStream is = getResources().openRawResource(R.raw.destination_lookup);
            reader = new BufferedReader(new InputStreamReader(is));
            while ((line = reader.readLine()) != null)
            {
                String[] str = line.split(",");
                dbHandler.addDestinationLookUpInfo(str[0], str[1]);
            }
        } catch (Exception e) {
            Log.d("DestLookUp", "Error: " + e.getLocalizedMessage());
        }
    }


    private void populateDatabaseWithInitialData(SharedPreferences prefs) {
        Log.d("DebugTest", "populateData");
//        DBHandler db = new DBHandler(this);
        List<LatLng> latLngList = new ArrayList<LatLng>();
        String line = "";
        try {
            InputStream is = getResources().openRawResource(R.raw.visakhapatnam_data_small);
            reader = new BufferedReader(new InputStreamReader(is));
        } catch (Exception e) {
            Log.d("DebugTest", "Error creating input stream visakhapatnam: " + e.getLocalizedMessage());
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

                dbHandler.addPivotTableData(new PivotTableData(identifier, lat, lon, name, brand, address, zipcode, city, district, state));
//                Log.i(TAG, "Reading data into table " + identifier + "," + lat + "," + lon + "," + brand + "," + name + "," + address + "," + zipcode + "," + city + "," + state);
            }
        } catch (IOException e) {
            Log.i(TAG, "Reading lat long failed");
            Log.d("DebugTest", "Parsing file: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        try {
            InputStream is = getResources().openRawResource(R.raw.visakhapatnam_bus_lines_data);
            reader = new BufferedReader(new InputStreamReader(is));
        } catch (Exception e) {
            Log.d("DebugTest", "Error creating input stream bus lines: " + e.getLocalizedMessage());
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
                int direction = Integer.parseInt(lines[4]);
                int sequence = Integer.parseInt(lines[5]);
                dbHandler.addBusLinesData(new IdentifierBusInfo(lineid, busno, source_station, destination_station, direction, sequence));
                Log.i(TAG, "Reading data into table " + lineid + "," + busno + "," + source_station + "," + destination_station);
            }
        } catch (IOException e) {
            Log.i(TAG, "Reading lat long failed");
            Log.d("DebugTest", "I/O Error: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        prefs.edit().putBoolean("first_run", false).apply();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if(expanded){
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }
        else {
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
            case R.id.action_settings:
                //Intent heatmap = new Intent(this, HeatMapActivity.class);
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                //startActivity(heatmap);
                break;

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

        if (id == R.id.profile) {
            // Handle the camera action
        } else if (id == R.id.bus_stations) {

        } else if (id == R.id.explored_loc) {

        } else if (id == R.id.massage) {

        } else if (id == R.id.terms_conditions) {

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
//        Toast.makeText(adapterView.getContext(), item_selected, Toast.LENGTH_SHORT).show();
        Log.d("Export", "Item selected: " + item_selected);
        item_selected_1 = item_selected;

        populateMap(view);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        googleMap.clear();
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;

        //mapstyle code below
       try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        //mapstyle code above

        this.googleMap.setOnMarkerClickListener(this);

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


    public void populateMap(View view) {

        googleMap.clear();
        linear_Layout_1 = (LinearLayout) findViewById(R.id.linearlayout);

        if (!item_selected_1.equalsIgnoreCase("Bus")) {
            //todo hide the linear layout created.

            constraint_Layout_1 = (ConstraintLayout) findViewById(R.id.constraintlayout);
            linear_Layout_1.setVisibility(view.GONE);
        }

        // TODO Set the Location to avoid crashing of app.
//        Location location = null;
//        double latitude = 33.587105;
//        double longitude = -117.719604;

        List<LatLng> latLngList = new ArrayList<LatLng>();
        String line = "";
        HashMap<LatLng, String> mData = new HashMap<>();
        Marker m;

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && currentLocation == null){
            String provider = null;
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                provider = LocationManager.NETWORK_PROVIDER;
            } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                provider = LocationManager.GPS_PROVIDER;
            }
            if(provider != null) {
                locationManager.getLastKnownLocation(provider);
            }
        }

        if (!item_selected_1.equals("Select...") && currentLocation != null) {

            //List<PivotTableData> markers = dbHandler.getFromPivotTableData(item_selected_1, currentLocation.getLatitude(), currentLocation.getLongitude());

            //testing the data. Assigning latnlong manually for now.
            double latitude = 17.736706;
            double longitude = 83.307610;
            //below coordinates are to test the scinario where there are no bus stations near the user location.
            //double latitude = 29.587433;
            //double longitude = -95.682365;
            List<PivotTableData> markers = dbHandler.getFromPivotTableData(item_selected_1, latitude, longitude);
            int busstops_1 = markers.size();
            Log.d("Export", "Sizeses:" + busstops_1);

            if(busstops_1 == 0) {
                fab.setVisibility(view.VISIBLE);

                String message = "Missing Bus stop? Long click (2sec) on the map to locate it. Thank you!";
                int duration = Snackbar.LENGTH_LONG;
                final Snackbar snackbar = Snackbar.make(view, message, duration);

                showSnackbar(view, message, duration);

            }
            //We ask user to locate the bus station near him.
            //////
            if (item_selected_1.equalsIgnoreCase("Bus")) {
                googleMap.getUiSettings().setMapToolbarEnabled(false);
                fab.setVisibility(view.VISIBLE);
                String message = "Bus Stop not located accurately? Long click (2sec) on the map to Locate it";
                int duration = Snackbar.LENGTH_INDEFINITE;
                final Snackbar snackbar = Snackbar.make(view, message, duration);
                showSnackbar(view, message, duration);

                //todo get rid of this when done testing
                LatLng latLng = new LatLng(latitude, longitude);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                googleMap.animateCamera(cameraUpdate);
                //todo no seriously, get rid of this block

                if (markers.size() == 0) {
                    Toast msg = Toast.makeText(getBaseContext(), "No results found", Toast.LENGTH_LONG);
                    msg.show();
                } else if (markers.size() == 1) {
                    linear_Layout_1.setVisibility(View.VISIBLE);
                    srcLocation = markers.get(0).name.toUpperCase();
                    Toast msg = Toast.makeText(getBaseContext(), "Enter destination", Toast.LENGTH_LONG);
                    msg.show();
                } else {
                    Toast msg = Toast.makeText(getBaseContext(), "Select source bus stop marker", Toast.LENGTH_LONG);
                    msg.show();
                }
            }

            LatLng latLng = null;
            Log.d(TAG, String.valueOf(markers));
            for (PivotTableData marker : markers) {
                latLng = new LatLng(marker.latitude, marker.longitude);
                String name = marker.name;

                mData.put(latLng, name);

                latLngList.add(latLng);
            }
            Log.d(TAG, "Here I am" + String.valueOf(mData));
            Log.d(TAG, "Rock you like a hurricane");

            //Drawing lines
            Polyline lines;
            for (LatLng li : mData.keySet()) {
                Log.d(TAG, "Display" + mData.get(li) + "" + li);
                googleMap.addMarker(new MarkerOptions().position(li).title(String.valueOf(mData.get(li))));
                Log.d(TAG, "Line drawing! possible?");
                /*lines = googleMap.addPolyline(new PolylineOptions()
                        .add(new LatLng(17.74748, 83.346268), new LatLng(17.74766, 83.34633))
                        .width(5)
                        .color(Color.RED));*/
                //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(li, 15));
            }
            setLocation();

//            for (LatLng point : latLngList) {
//                googleMap.clear();
//                LatLng test = new LatLng(point.latitude, point.longitude);
//                googleMap.addMarker(new MarkerOptions().position(test).title(point.latitude+ "\n" +point.longitude)
//                .snippet("My Snippet"+"\n"+"1st Line Text"+"\n"+"2nd Line Text"+"\n"+"3rd Line Text").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
//                Log.i(TAG, "Point: " + point);
//                googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
//            }
            //todo if the currentlocation is null, popup a window.
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
                                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(coordinate, 15);
                                //todo uncomment this...maybe
//                                googleMap.animateCamera(yourLocation);
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

    @Override
    public boolean onMarkerClick(Marker marker) {

        srcLocation = marker.getTitle().toUpperCase();

        if (item_selected_1.equalsIgnoreCase("Bus")) {
            linear_Layout_1.setVisibility(View.VISIBLE);
            Toast msg = Toast.makeText(getBaseContext(), "Enter destination", Toast.LENGTH_LONG);
            msg.show();
        }

        return false;

    }

    @Override
    public void onDestinationSearchClick(String destination) {
        searchResultClick = true;
        eText.setText(destination);
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
    public void onMoreInfoClick(String busName, String src, String destination) {
        MoreInfoDialog dialog = new MoreInfoDialog(this, this);
        srcLocation = src;
        destLocation = destination;
        dialog.show();
        TextView numberOfStops = (TextView) dialog.findViewById(R.id.number_of_stops);
        numberOfStops.setText(String.valueOf(dbHandler.getNumberOfStopsBetween(src, destination, busName)));
    }


    @Override
    public void trackMyPath() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        //todo get from database latlngs for source (variable srcLocation) and destination (variable destLocation). Clear map of markers. Draw two markers.
    }
}
