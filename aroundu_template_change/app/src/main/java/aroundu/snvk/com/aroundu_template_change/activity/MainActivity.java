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
import aroundu.snvk.com.aroundu_template_change.vo.BusRoutesInfo;
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
    Button btn, loc_submit, loc_cancel, closePopupBtn, feedback_submit, feedback_cancel;
    EditText eText, EditTextFeedbackBody;
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
    private String line_id;
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
    public LatLng user_loc_input, user_selected_bustop, coordinate;
    SharedPreferences prefs;
    PopupWindow popupWindow;
    boolean doubleBackToExitPressedOnce = false;
    private String uuid ="-11";
    private int main_fab_click, bus_fab_click, coverage_fab_click, destination_input_click, bottom_sheet_click, track_my_path_click, sub_location_layout_click, userinput_fab_click, map_input_status, dist_metric_layout;
    HashMap<String,Marker> hashMapMarker = new HashMap<>();
    LinearLayout feedback_layout;
    List<BusRoutesInfo> busrouteList = new ArrayList<BusRoutesInfo>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//
        prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        //prefs_bus = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        //prefs_coverage = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
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

        //Log.d(TAG, "Deleting");
        //dbHandler.deletedestinationLookup();
        //Log.d(TAG, "Populating");
        //populateDestinationLookUpTable();

        //initial insert of data
        if (prefs.getBoolean("first_run", true)) {
            Log.d(TAG, "Populating the database");

            backgroundHandler.post(new Runnable() {
                @Override
                public void run() {

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
                    editor.apply();*/


                }
            });
        }
        //uuid = UUID.randomUUID().toString().replace("-", "");
        //prefs.edit().putString("ad_id", uuid );

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
        linear_Layout_1 = (LinearLayout) findViewById(R.id.linearlayout);
        fab = (FloatingActionButton) findViewById(R.id.userinput_fab);
        fab_main = (FloatingActionButton) findViewById(R.id.fab1);

        //ShowcaseView (first time - user) tutorial
           RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(
           ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
           lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
           lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
           int margin = ((Number) (getResources().getDisplayMetrics().density * 16)).intValue();
           lps.setMargins(margin, margin, margin, margin);
           Target viewTarget = new ViewTarget(R.id.fab1, this);  // Add the control you need to focus by the ShowcaseView
           /*ShowcaseView sv = new ShowcaseView.Builder(this)
               .setTarget(viewTarget)
                //.setContentTitle(R.string.title_single_shot)        // Add your string file (title_single_shot) in values/strings.xml
                .setContentText(R.string.R_string_desc_single_shot1) // Add your string file (R_strings_desc_single_shot) in values/strings.xml
                .singleShot(100)
                   .blockAllTouches()
                   .useDecorViewAsParent()
                   .setStyle(R.color.colorPrimary)
                   .build();
            sv.setButtonPosition(lps);*/

        //feedback_layout = (LinearLayout) findViewById(R.id.feedbackLinearLayout);
        //feedback_framelayout = (LinearLayout) findViewById(R.id.feedbackFrameLayout);
        //feedback_layout.setVisibility(View.GONE);
        feedback_layout = (LinearLayout) findViewById(R.id.feedback_layout);
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
        //feedback_submit = (Button) findViewById(R.id.ButtonSendFeedback);
        //feedback_cancel = (Button) findViewById(R.id.ButtonCancelFeedback);


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
                userinput_fab_click = 1;
                String message = "Long press on the map to locate the bus stop (accurately) in your path!";
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
                //linear_Layout_1.setVisibility(View.GONE);
                if(dist_metric_layout == 0){
                }
                else if(dist_metric_layout == 1) {
                    distancecalc_layout.setVisibility(View.VISIBLE);
                }
                    addUserInputToDB(user_loc_input, System.currentTimeMillis());
                    //todo marker created on long click should be removed but the remaining points should be still visible.
                /*googleMap.addMarker(new MarkerOptions()
                        .position(user_loc_input)
                        .title("Suggested BusStop Location"))
                        .showInfoWindow();*/
                    map_input_status = 0;
                    userinput_fab_click = 0;
                    //after user submits the location, it has to be removed from the map
                    Marker marker = hashMapMarker.get("Selected Location");
                    Log.d(TAG, marker.getTitle());
                    marker.remove();
                    hashMapMarker.remove("SelectedLocation");
            }
        });


        loc_cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(dist_metric_layout == 0){
                }
                else if(dist_metric_layout == 1) {
                    distancecalc_layout.setVisibility(View.VISIBLE);
                }
                if(destination_input_click == 0) {
                }
                else if(destination_input_click ==1){
                    destination_input_click = 0;
                    linear_Layout_1.setVisibility(View.GONE);
                }

                //user might have selected a location but doesnt want to submit it. in that case, the selected location on the map (marker) should be deleted.
                Marker marker = hashMapMarker.get("Selected Location");
                Log.d(TAG, marker.getTitle());
                marker.remove();
                hashMapMarker.remove("SelectedLocation");

                user_loc_input = null;
                submitlayout.setVisibility(View.GONE);
                if(destination_input_click == 1) {

                }
                userinput_fab_click = 0;
                map_input_status = 0;

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                //working code below
                /*ArrayList busList = new ArrayList(dbHandler.getBusLinesData(srcLocation, eText.getText().toString().toUpperCase()));
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
                destLocation = eText.getText().toString();
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
                client.post("http://limitmyexpense.com/arounduuserdatasync/get_bus_list.php", params, new AsyncHttpResponseHandler() {
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
                                bottomSheetAdapter = new BottomSheetAdapter(busList, bottomSheetClickListener);
                                bottomSheetRV.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                bottomSheetRV.setAdapter(bottomSheetAdapter);
                            }

                            //fab_info.setVisibility(view.VISIBLE);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d(TAG, "success in catch");
                        }
                        //Log.d(TAG,arr.toString());
                        //Toast.makeText(getApplicationContext(), "successful - but nothing happenin on ui: ", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Log.d(TAG, "Failed to send latlong to server");
                    }
                });
//testing code above







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
            public void onClick(final View view) {
                map_input_status = 0;
                destination_input_click = 0;
                dist_metric_layout = 0;

                distancecalc_layout.setVisibility(View.GONE);

                googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng point) {

                        if(map_input_status == 0) {
                            //user can submit one bus stop at a time. map_input_status checks if the user has already onpressed on the map.
                            user_loc_input = point;
                            distancecalc_layout.setVisibility(View.GONE);
                            submitlayout.setVisibility(View.VISIBLE);
                            Marker marker = googleMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .title("Selected Location"));
                            marker.showInfoWindow();
                            map_input_status = 1;
                            hashMapMarker.put("Selected Location",marker);
                            Log.d("markers on map: ", String.valueOf(hashMapMarker.size()));
                        }
                        else {
                            Marker marker = hashMapMarker.get("Selected Location");
                            Log.d(TAG, marker.getTitle());
                            marker.remove();
                            hashMapMarker.remove("SelectedLocation");

                            user_loc_input = point;
                            distancecalc_layout.setVisibility(View.GONE);
                            submitlayout.setVisibility(View.VISIBLE);
                            marker = googleMap.addMarker(new MarkerOptions()
                                    .position(point)
                                    .title("Selected Location"));
                            marker.showInfoWindow();
                            hashMapMarker.put("Selected Location",marker);
                            Log.d("markers on map: ", String.valueOf(hashMapMarker.size()));
                        }
                    }
                });

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
                    setLocation();


                    //double latitude = coordinate.latitude;
                    //double longitude = coordinate.longitude;
                    //testing the data. Assigning latnlong manually for now.
                    //17.694948, 83.292365 - old head post office
                    double latitude = 17.724767;
                    double longitude = 83.306253;


                    ///* Working code below
                    /*item_selected_1 = "Bus";
                    List<PivotTableData> markers = dbHandler.getFromPivotTableData(item_selected_1, latitude, longitude);
                    int busstops_1 = markers.size();
                    Log.d("Export", "Sizeses:" + busstops_1);
                    if (item_selected_1.equalsIgnoreCase("Bus")) {
                        googleMap.getUiSettings().setMapToolbarEnabled(false);
                        linear_Layout_1 = (LinearLayout) findViewById(R.id.linearlayout);

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
                    client.post("http://limitmyexpense.com/arounduuserdatasync/get_pivot_data.php", params, new AsyncHttpResponseHandler() {
                        @SuppressLint("RestrictedApi")
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            JSONArray arr= null;
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
                                                .title(stop_name.toUpperCase()));
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
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "No Bus stations found around you. Locate them by long pressing on the map! ", Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(TAG, "In Catch block of userLocation");
                                }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            Log.d(TAG, "Failed to retrieve data. are you connected to internet?");
                        }
                    }); }
                    //testing above
            }
        });

        coverage_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(destination_input_click == 1) {
                    linear_Layout_1.setVisibility(View.GONE);
                    destination_input_click = 0;
                }
                if(dist_metric_layout == 1) {
                    distancecalc_layout.setVisibility(View.GONE);
                    dist_metric_layout = 0;
                }
                googleMap.setOnMapLongClickListener(null);
                linear_Layout_1.setVisibility(View.GONE);


                //instantiate the popup.xml layout file
                LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View customView = layoutInflater.inflate(R.layout.popup,null);
                closePopupBtn = (Button) customView.findViewById(R.id.closePopupBtn);
                TextView displayText = (TextView) customView.findViewById(R.id.popuptext);
                displayText.setText("You can see all the places you have covered in the last 1 month. \n\n" +
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

    public void addUserInputToDB(LatLng userinput, long timeStamp) {
        // adding the location data into LocationInfo table.
        Log.i(TAG, String.valueOf(userinput));
        LocationInfo li = new LocationInfo();
        li.setTime_stamp(timeStamp);
        li.setLatitude(userinput.latitude);
        li.setLongitude(userinput.longitude);
        li.setDevice_id(uuid);
        ArrayList<LocationInfo> displaypoints = new ArrayList<>();
        displaypoints.add(li);

        //todo uncomment the below line
        //dialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        final List userList = displaypoints;
        Gson gson = new GsonBuilder().create();
        Log.d(TAG, String.valueOf(userList.size()));
        if (userList.size() != 0) {
            if (dbHandler.dbSyncCount() != 0) {
                prgDialog.show();
                params.add("usersInput", gson.toJson(displaypoints));
                //Toast.makeText(getApplicationContext(), "device_id:" + uuid, Toast.LENGTH_LONG).show();
                //params.add("device_id", prefs.getString("ad_id", null));
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
        sbView.setBackgroundColor(Color.parseColor("#1d2498"));
        // Set an action on it, and a handler
        snackbar.setAction("DISMISS", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
        userinput_fab_click = 0;
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
        //todo check location permission
        setLocation();
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
        prefs.edit().putBoolean("first_run", false).apply();
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
                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.action_feedback:

                Intent homeIntent = new Intent(this, feedback_activity.class);
                startActivity(homeIntent);



              /*  Toast.makeText(this, "Feedback selected", Toast.LENGTH_SHORT)
                        .show();
                //todo call the Feedback.xml the user the fragment.
                Log.d("FEEDBACK", "Feedback button clicked");
                LayoutInflater layoutInflater = (LayoutInflater) MainActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View customView = layoutInflater.inflate(R.layout.Feedback,null);
                Log.d("FEEDBACK", "Feedback button clicked_1");
                feedback_submit = (Button) customView.findViewById(R.id.feedback_submit);
                feedback_cancel = (Button) customView.findViewById(R.id.feedback_cancel);
                //instantiate popup window
                popupWindow = new PopupWindow(customView, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(new BitmapDrawable());
                Log.d("FEEDBACK", "Feedback button clicked_2");
                popupWindow.setOutsideTouchable(true);
                Log.d("FEEDBACK", "Feedback button clicked_3");
                //display the popup window
                popupWindow.showAtLocation(fabContainer, Gravity.CENTER, 0, 0);
                Log.d("FEEDBACK", "Feedback button clicked_4");

                EditTextFeedbackBody = (EditText) customView.findViewById(R.id.EditTextFeedbackBody);
                EditTextFeedbackBody.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("EDITTEXT", "Has focus: " + EditTextFeedbackBody.hasFocus());
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        //imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                        *//*if (EditTextFeedbackBody.requestFocus()) {

                            customView.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
                                }
                            }, 20000);
                        }*//*
                }
                });*/
                //close the popup window on button click
                /*feedback_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                    }
                });

                feedback_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if(EditTextFeedbackBody.getText().toString().length() > 0){
                            FeedbackInfo fi = new FeedbackInfo();
                            fi.setId(uuid);
                            fi.setFeedback(EditTextFeedbackBody.getText().toString());
                            ArrayList<FeedbackInfo> displaypoints = new ArrayList<>();
                            displaypoints.add(fi);
                            AsyncHttpClient client = new AsyncHttpClient();
                            final RequestParams params = new RequestParams();
                            final List userList = displaypoints;
                            Gson gson = new GsonBuilder().create();
                            Log.d(TAG, String.valueOf(userList.size()));
                            prgDialog.show();
                            params.add("usersFeedback", gson.toJson(displaypoints));
                            client.post("http://limitmyexpense.com/arounduuserdatasync/insert_userinput.php", params, new AsyncHttpResponseHandler() {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    Log.d("Sync", "OnSuccess Function 4");
                                    Log.d("Sync", String.valueOf(statusCode));
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
                            Toast.makeText(getApplicationContext(), "Please provide your Feedback before submitting.", Toast.LENGTH_LONG).show();
                        }

                        popupWindow.dismiss();
                    }
                });
*/


                break;
            case R.id.action_exit:
                finish();
                //System.exit(0);
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
                                coordinate = new LatLng(location.getLatitude(), location.getLongitude());
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
        linear_Layout_1.setVisibility(View.VISIBLE);
            user_selected_bustop = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
            Log.d(TAG, "selected marker info: " + marker.getTitle().toUpperCase());
            srcLocation = marker.getTitle().toUpperCase();

            if (dist_metric_layout == 0) {
                if (item_selected_1.equalsIgnoreCase("Bus")) {
                    linear_Layout_1.setVisibility(View.VISIBLE);
                    Toast msg = Toast.makeText(getBaseContext(), "Enter destination", Toast.LENGTH_LONG);
                    destination_input_click = 1;
                    msg.show();
                }
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
    public void onMoreInfoClick(String busName, String src, String destination, String lineid) {
        MoreInfoDialog dialog = new MoreInfoDialog(this, this);
        Log.d(TAG, src + destination);
        srcLocation = src;
        destLocation = destination;
        bus_no = busName;
        line_id = lineid;
        Log.d(TAG, String.valueOf(srcLocation) + "," + String.valueOf(destLocation) + "," + String.valueOf(bus_no) + "," + String.valueOf(line_id) );
        dialog.show();
        final TextView numberOfStops = (TextView) dialog.findViewById(R.id.number_of_stops);
        //dbHandler.getNumberOfStopsBetween(src, destination, busName);
        //numberOfStops.setText(String.valueOf(dbHandler.getNumberOfStopsBetween(src, destination, busName)));

        //testing code below
        //todo uncomment the below line
        //dialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        ArrayList<HashMap<String, String>> wordList;
        wordList = new ArrayList<HashMap<String, String>>();
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
        client.post("http://limitmyexpense.com/arounduuserdatasync/get_bus_route.php", params, new AsyncHttpResponseHandler() {
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
                    e.printStackTrace();
                    Log.d(TAG, "success in catch");
                }
                //Log.d(TAG,arr.toString());
                //Toast.makeText(getApplicationContext(), "successful - but nothing happenin on ui: ", Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d(TAG, "Failed to send latlong to server");
            }
        });
        //testing code above
    }



    @Override
    public void trackMyPath() {
        googleMap.clear();
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        linear_Layout_1.setVisibility(View.GONE);

        //List<PivotTableData> markers = dbHandler.getDestinationCoordinates(destLocation, srcLocation, bus_no);
        List<BusRoutesInfo> markers = new ArrayList<BusRoutesInfo>();
        markers = busrouteList;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Log.d(TAG, String.valueOf(markers));

        //distancecalc_layout.setVisibility(View.VISIBLE);

        dist_metric_layout = 1;
        double distance = 0.0;
        int i = 0;
        double user_lat, user_long, next_stop_lat = 0, next_stop_long = 0;

        if (markers.size() == 0) {
            //distancecalc_layout.setVisibility(View.VISIBLE);
            Toast msg = Toast.makeText(getBaseContext(), "Try Again. If no results found, give us a feedback by clicking on FEEDBACK on top-right corner...", Toast.LENGTH_LONG);
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
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
                builder.include(new LatLng(marker.latitude, marker.longitude));

                if (i == 0) {
                    user_lat = 17.724767;
                    user_long = 83.306253;
                    //todo use the below user_lat and user_long for actual distance calculation.
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
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(user_selected_bustop.latitude, user_selected_bustop.longitude))
                    .title(srcLocation.toUpperCase())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

            distance_text.setText(" Selected Bus No:" + bus_no + "\n Distance: " + Math.round(distance*1.6) + "Km (approx)");
            LatLngBounds bounds = builder.build();
            int padding = 50;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.animateCamera(cameraUpdate);
            linear_Layout_1.setVisibility(View.GONE);


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
