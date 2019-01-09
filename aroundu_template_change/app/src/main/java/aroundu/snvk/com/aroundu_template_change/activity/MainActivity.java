package aroundu.snvk.com.aroundu_template_change.activity;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.view.Gravity;
import android.view.ViewGroup.LayoutParams;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.android.clustering.ClusterManager;
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
import java.io.UnsupportedEncodingException;
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
import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        OnMapReadyCallback, GoogleMap.OnMarkerClickListener, RecyclerViewClickListener, BottomSheetClickListener, TrackingListener {

    ProgressDialog prgDialog;
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
    Button btn, loc_submit, loc_cancel, closePopupBtn;
    EditText eText;
    ConstraintLayout constraint_Layout_1;
    LinearLayout linear_Layout_1;
    LinearLayout submitlayout;
    private DBHandler dbHandler;
    private Handler mHandler;
    private RecyclerView bottomSheetRV, searchViewRV;
    private BottomSheetAdapter bottomSheetAdapter;
    private SearchViewAdapter searchViewAdapter;
    private Location currentLocation = null;
    private String srcLocation = "";
    private String destLocation = "";
    private String bus_no = "";
    private RecyclerViewClickListener recyclerViewClickListener;
    private BottomSheetClickListener bottomSheetClickListener;
    private ArrayList<String> busDestinationSearchResults;
    private boolean searchResultClick = false;
    private TextView version, distance_text, distance_calc;
    private Spinner core_spinner;
    private FloatingActionButton fab;
    private FloatingActionButton fab_main;
    private FloatingActionButton bus_fab;
    private FloatingActionButton coverage_fab;
    private Toolbar toolbar;
    private LinearLayout llBottomSheet, distancecalc_layout;
    private BottomSheetBehavior bottomSheetBehavior;
    ToggleButton toggle;
    private Handler backgroundHandler = null;
    private HandlerThread mHandlerThread = null;
    private int versionClickCount = 0;
    private boolean expanded = false;
    private boolean fabMenuOpen = false;
    private LinearLayout fabContainer;
    public LatLng user_loc_input;
    private LatLngBounds.Builder builder;
    private LatLngBounds bounds;
    SharedPreferences prefs, prefs_bus, prefs_coverage;
    PopupWindow popupWindow;
    boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        prefs_bus = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        prefs_coverage = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        prgDialog = new ProgressDialog(this);
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

        if (prefs.getBoolean("first_run", true)) {
            Log.d(TAG, "Populating the database");

            backgroundHandler.post(new Runnable() {
                @Override
                public void run() {
                    dbHandler.createDataBase();
                    //if running for the first time and .db file is not created yet. then exe below lines
                    /*Log.d("DestLookUp", "Before");
                    populateDatabaseWithInitialData();
                    Log.d("DestLookUp", "After");
                    populateDestinationLookUpTable();*/
                    //if we have the .db file created, use the below line to get the data into database fast. use below for release.


                    prefs.edit().putString("ad_id", getGoogleID());
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
  }

    @SuppressLint("WrongViewCast")
    public void setViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        fabContainer = (LinearLayout) findViewById(R.id.fabContainerLayout);
        submitlayout = (LinearLayout) findViewById(R.id.submitlayout);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab_main = (FloatingActionButton) findViewById(R.id.fab1);

        //ShowcaseView (first time - user) tutorial
           RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
           ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
           lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
           lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
           int margin = ((Number) (getResources().getDisplayMetrics().density * 16)).intValue();
           lps.setMargins(margin, margin, margin, margin);
           Target viewTarget = new ViewTarget(R.id.fab1, this);  // Add the control you need to focus by the ShowcaseView
           ShowcaseView sv = new ShowcaseView.Builder(this)
               .setTarget(viewTarget)
                //.setContentTitle(R.string.title_single_shot)        // Add your string file (title_single_shot) in values/strings.xml
                .setContentText(R.string.R_string_desc_single_shot1) // Add your string file (R_strings_desc_single_shot) in values/strings.xml
                .singleShot(100)
                   .blockAllTouches()
                   .useDecorViewAsParent()
                   .setStyle(R.color.colorPrimary)
                   .build();
            sv.setButtonPosition(lps);


        bus_fab = (FloatingActionButton) findViewById(R.id.bus_fab);
        coverage_fab = (FloatingActionButton) findViewById(R.id.coverage_fab);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        llBottomSheet = (LinearLayout) findViewById(R.id.bottom_sheet);
        distancecalc_layout = (LinearLayout) findViewById(R.id.distancecalc_layout);
        bottomSheetRV = (RecyclerView) findViewById(R.id.recycler_view);
        searchViewRV = (RecyclerView) findViewById(R.id.search_recycler_view);
        eText = (EditText) findViewById(R.id.editText);
        btn = (Button) findViewById(R.id.button);
        loc_submit = (Button) findViewById(R.id.loc_submit);
        loc_cancel = (Button) findViewById(R.id.loc_cancel);
        version = (TextView) findViewById(R.id.version_number);
        distance_text = (TextView) findViewById(R.id.distance_text);
        distance_calc = (TextView) findViewById(R.id.distance_calc);

        //Animations to fab
        Animation show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        Animation hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);

        //expanding fab_main (main floating action button)
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) fab_main.getLayoutParams();
        layoutParams.rightMargin += (int) (fab_main.getWidth() * 1.7);
        layoutParams.bottomMargin += (int) (fab_main.getHeight() * 0.25);
        fab_main.setLayoutParams(layoutParams);
        fab_main.startAnimation(show_fab_1);
        fab_main.setClickable(true);
        coverage_fab.setClickable(true);
        bus_fab.setClickable(true);

    }

    //This is all for the animation for fab
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void toggleFabMenu() {
        if (!fabMenuOpen) {
            //fab_main.setImageResource(R.drawable.ic_launcher_background);

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
            //fab_main.setImageResource(R.drawable.ic_launcher_background);
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

        //Floating Action
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //todo display popup instead of snackbar to ask users to perform long click for bus_stop locations.
                String message = "Long press on the map to locate any bus stop in your path!";
                int duration = Snackbar.LENGTH_INDEFINITE;
                showSnackbar(view, message, duration);
                //fab - click to point out the busstop. does this work???

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
                                Log.d(TAG, String.valueOf(busDestinationSearchResults));
                            }

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (!searchResultClick && busDestinationSearchResults != null) {
                                        //search.setState(BottomSheetBehavior.STATE_EXPANDED);
                                        Log.d(TAG, String.valueOf(busDestinationSearchResults));
                                        searchViewAdapter = new SearchViewAdapter(busDestinationSearchResults, recyclerViewClickListener);
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
        });

        loc_submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                addUserInputToDB(user_loc_input, System.currentTimeMillis());
               //todo marker created on long click should be removed but the remaining points should be still visible.
            }
        });

        loc_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                user_loc_input = null;
                //todo user_loc_input location on the map should be removed, remaining points should be still visible.
                submitlayout.setVisibility(View.GONE);

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

        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabMenu();
                Log.d(TAG, "fab_main clicked");
            }
        });
        bus_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //if (prefs_bus.getBoolean("first_run", true)) {
                    Log.d(TAG, "Displaying text");

                    //instantiate the popup.xml layout file
                    LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View customView = layoutInflater.inflate(R.layout.popup,null);
                    closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);
                    TextView displayText = (TextView) customView.findViewById(R.id.popuptext);
                    displayText.setText("Get a list of buses that travel from your closest bus stop to your destination. \n\n " +
                            "Select a bus stop on the map and provide the destination.");
                    //instantiate popup window
                    popupWindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                    popupWindow.setBackgroundDrawable(new BitmapDrawable());
                    popupWindow.setOutsideTouchable(true);
                    //display the popup window
                    popupWindow.showAtLocation(fabContainer, Gravity.CENTER, 0, 0);
                    //close the popup window on button click
                    closePopupBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupWindow.dismiss();
                        }
                    });

                //}


                googleMap.clear();
                /*googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng point) {
                        //googleMap.clear();
                        submitlayout.setVisibility(View.VISIBLE);
                        Toast.makeText(getBaseContext(),
                                point.latitude + ", " + point.longitude,
                                Toast.LENGTH_SHORT).show();
                        googleMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title("Selected Location")
                                .snippet("")).showInfoWindow();
                        user_loc_input = point;
                    }
                });*/

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
                    //17.694948, 83.292365 - old head post office
                    double latitude = 17.694948;
                    double longitude = 83.292365;
                    item_selected_1 = "Bus";
                    List<PivotTableData> markers = dbHandler.getFromPivotTableData(item_selected_1, latitude, longitude);
                    int busstops_1 = markers.size();
                    Log.d("Export", "Sizeses:" + busstops_1);
                    if (item_selected_1.equalsIgnoreCase("Bus")) {
                        googleMap.getUiSettings().setMapToolbarEnabled(false);
                        linear_Layout_1 = (LinearLayout) findViewById(R.id.linearlayout);
                        /*String message = "Long press on the map to locate any missing bus stop accurately. Thank you!";
                        int duration = Snackbar.LENGTH_INDEFINITE;
                        final Snackbar snackbar = Snackbar.make(view, message, duration);
                        showSnackbar(view, message, duration);
*/
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
                            //linear_Layout_1.setVisibility(View.VISIBLE);
                            srcLocation = markers.get(0).name.toUpperCase();
                            Toast msg = Toast.makeText(getBaseContext(), "Enter destination", Toast.LENGTH_LONG);
                            msg.show();
                        } else {
                            //linear_Layout_1.setVisibility(view.VISIBLE);
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
                }
            }
        });

        coverage_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //instantiate the popup.xml layout file
                LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popup,null);
                closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);
                TextView displayText = (TextView) customView.findViewById(R.id.popuptext);
                displayText.setText("You can see all the places you have covered in the last 1 week. \n\n" +
                        "Zoom in/out to know more about your explorations!");
                //instantiate popup window
                popupWindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                popupWindow.setOutsideTouchable(true);
                //display the popup window
                popupWindow.showAtLocation(fabContainer, Gravity.CENTER, 0, 0);
                //close the popup window on button click
                closePopupBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                //toggleFabMenu();
                googleMap.clear();
                fabContainer.setVisibility(View.GONE);
                addHeatMap_1();
                Log.d(TAG, "addHeatMap_1 passed");
            }
        });
    }

    private String getGoogleID() {
        AdvertisingIdClient.Info idInfo = null;
        try {
            idInfo = AdvertisingIdClient.getAdvertisingIdInfo(getApplicationContext());
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

    public void addUserInputToDB(LatLng userinput, long timeStamp) {
        // adding the location data into LocationInfo table.
        Log.i(TAG, String.valueOf(userinput));
        LocationInfo li = new LocationInfo();
        li.setTime_stamp(timeStamp);
        li.setLatitude(userinput.latitude);
        li.setLongitude(userinput.longitude);
        ArrayList<LocationInfo> displaypoints = new ArrayList<>();
        displaypoints.add(li);

        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final List userList = displaypoints;
        Gson gson = new GsonBuilder().create();
        Log.d(TAG, String.valueOf(userList.size()));
        if (userList.size() != 0) {
            if (dbHandler.dbSyncCount() != 0) {
                prgDialog.show();
                params.add("usersInput", gson.toJson(displaypoints));
                params.add("device_id", prefs.getString("ad_id", null));
                Log.d("Sync", params.toString());
                client.post("http://limitmyexpense.com/arounduuserdatasync/insert_userinput.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("Sync", "OnSuccess Function 4");
                        Log.d("Sync", String.valueOf(statusCode));
                        try {
                            Log.d("Sync", "in try block!");
                            String str = new String(responseBody, "UTF-8");
                            Log.d("Sync str", str);
                            JSONArray jarray = new JSONArray(str.trim());
                            Log.d("Sync", String.valueOf(jarray.length()));
                            for (int i = 0; i < jarray.length(); i++) {
                                Log.d("Sync", String.valueOf(i));
                                JSONObject jsonobject = jarray.getJSONObject(i);
                                Log.d("Sync", String.valueOf(jsonobject.getLong("time_stamp")));
                            }
                            Log.d("Sync", "Am I here??");
                            Log.d("Sync", String.valueOf(jarray.length()));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        prgDialog.hide();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        Log.d("Sync", "OnFailure Function");
                        prgDialog.hide();
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
                Toast.makeText(getApplicationContext(), "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();
            }
        }
        submitlayout.setVisibility(View.GONE);
        Toast.makeText(getApplicationContext(), "Thank you for providing Bus Stop Information.", Toast.LENGTH_LONG).show();
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

    public void syncSQLiteMySQLDB() {
        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final List userList = dbHandler.readLocationInfo_1();
        Log.d("Sync 1", String.valueOf(userList.size()));
        if (userList.size() != 0) {
            if (dbHandler.dbSyncCount() != 0) {
                Log.d("Sync", "OnSuccess Function 1");
                prgDialog.show();
                Log.d("Sync", "OnSuccess Function 2");
                params.add("usersJSON", dbHandler.composeJSONfromSQLite());
                Log.d("Sync", params.toString());
                client.post("http://limitmyexpense.com/arounduuserdatasync/insert_location_logs.php", params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Log.d("Sync", "OnSuccess Function 4");
                        Log.d("Sync", String.valueOf(statusCode));
                        prgDialog.hide();
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
                        prgDialog.hide();
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
                Toast.makeText(getApplicationContext(), "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getApplicationContext(), "No data in SQLite DB, please do enter User name to perform Sync action", Toast.LENGTH_LONG).show();
        }
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
                    //todo show the user a popup with the steps to follow to give user location permission.
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
            mProvider.setRadius(25);
            mOverlay = googleMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), 5);
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
                Log.i(TAG, "Reading data into table " + lineid + "," + busno + ","
                        + source_station + "," + destination_station + "," + direction + "," + sequence);
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
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (expanded) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else {
            super.onBackPressed();
        }
*/
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
                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
                        .show();
                Log.d("Sync", "Starting SyncSQLiteMySQLDB");
                syncSQLiteMySQLDB();
                break;
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
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
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
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
        submitlayout.setVisibility(View.GONE);
        Log.d(TAG, "selected marker info: " + marker.getTitle().toUpperCase());
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
        Log.d(TAG, src + destination);
        srcLocation = src;
        destLocation = destination;
        bus_no = busName;
        Log.d(TAG, busName);
        dialog.show();
        TextView numberOfStops = (TextView) dialog.findViewById(R.id.number_of_stops);
        int result = dbHandler.getNumberOfStopsBetween(src, destination, busName);
        numberOfStops.setText(String.valueOf(dbHandler.getNumberOfStopsBetween(src, destination, busName)));
    }

    @Override
    public void trackMyPath() {
        googleMap.clear();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        List<PivotTableData> markers = dbHandler.getDestinationCoordinates(destLocation, srcLocation, bus_no);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Log.d(TAG, String.valueOf(markers));
        distancecalc_layout.setVisibility(View.VISIBLE);
        double distance = 0.0;
        int i = 0;
        double user_lat, user_long, next_stop_lat = 0, next_stop_long = 0;

        if (markers.size() == 0) {
            Toast msg = Toast.makeText(getBaseContext(), "Source and Destination should be different!! Try a different destination location.", Toast.LENGTH_LONG);
            msg.show();
        } else {
            PivotTableData src = markers.get(0);
            PivotTableData dest = markers.get(markers.size() - 1);
            /*googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(src.latitude, src.longitude))
                    .title(src.name)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));*/
            builder.include(new LatLng(src.latitude, src.longitude));

            /*googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(dest.latitude, dest.longitude))
                    .title(dest.name));*/
            builder.include(new LatLng(dest.latitude, dest.longitude));

            for (PivotTableData marker : markers) {

                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(marker.latitude, marker.longitude))
                        .title(marker.name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                builder.include(new LatLng(marker.latitude, marker.longitude));

                if (i == 0) {

                    //17.694948, 83.292365 - old head post office
                    user_lat = 17.694948;
                    user_long = 83.292365;
                    //todo use the below user_lat and user_long for actual distance calculation.
                    //user_lat = currentLocation.getLatitude();
                    //user_long = currentLocation.getLongitude();
                    Log.d(TAG, "User Location in distance calc: " + currentLocation.getLatitude() + "," + currentLocation.getLongitude());
                    next_stop_lat = marker.latitude;
                    next_stop_long = marker.longitude;
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

            }
            distance_calc.setText(String.valueOf((int) distance) + "Km");
            LatLngBounds bounds = builder.build();
            int padding = 50;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.animateCamera(cameraUpdate);
            linear_Layout_1.setVisibility(View.GONE);

            if(fab.getVisibility() != 0) {
                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng point) {
                        googleMap.clear();
                        distancecalc_layout.setVisibility(View.GONE);
                        distancecalc_layout.setVisibility(View.GONE);
                        submitlayout.setVisibility(View.VISIBLE);
                        googleMap.addMarker(new MarkerOptions()
                                .position(point)
                                .title("Selected Location")
                                .snippet("")).showInfoWindow();
                    }
                });
            }
            else{
                googleMap.setOnMapLongClickListener(null);
            }
            setLocation();
        }
    }

    public double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }
    public double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


}
