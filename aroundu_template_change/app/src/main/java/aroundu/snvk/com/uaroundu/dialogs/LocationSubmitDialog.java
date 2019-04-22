package aroundu.snvk.com.uaroundu.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import aroundu.snvk.com.uaroundu.R;
import aroundu.snvk.com.uaroundu.beans.LocationInfo;
import aroundu.snvk.com.uaroundu.configs.Constants;
import aroundu.snvk.com.uaroundu.db_utils.DBHandler;
import aroundu.snvk.com.uaroundu.ui.ProgressView;
import cz.msebera.android.httpclient.Header;

public class LocationSubmitDialog extends Dialog implements View.OnTouchListener {
    public Activity activity;
    private RelativeLayout cancelLayout;
    private TextView cancelTextView;
    private RelativeLayout submitLayout;
    private TextView submitTextView;

    private Animation animPressDown;
    private Animation animPressRealease;

    private int dist_metric_layout;
    private LinearLayout distanceCalcLayout;
    private LatLng user_loc_input;
    private int map_input_status;
    private int destination_input_click;
    private LinearLayout searchResultLayout;
    private HashMap<String,Marker> hashMapMarker = new HashMap<>();
    private String uuid;
    private DBHandler dbHandler;

    private ProgressView progressView;

    public LocationSubmitDialog(Activity activity, int dist_metric_layout, LinearLayout distanceCalcLayout,
                                LatLng user_loc_input, int map_input_status, int destination_input_click,
                                LinearLayout searchResultLayout, HashMap<String, Marker> hashMapMarker,
                                String uuid, DBHandler dbHandler) {
        super(activity);
        this.activity = activity;
        this.dist_metric_layout = dist_metric_layout;
        this.distanceCalcLayout = distanceCalcLayout;
        this.user_loc_input = user_loc_input;
        this.map_input_status = map_input_status;
        this.destination_input_click = destination_input_click;
        this.searchResultLayout = searchResultLayout;
        this.hashMapMarker = hashMapMarker;
        this.uuid = uuid;
        this.dbHandler = dbHandler;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_locatoin_submit);

        progressView = new ProgressView(activity);

        cancelLayout = findViewById(R.id.no_button_layout);
        cancelTextView = findViewById(R.id.btn_no_textview);
        submitLayout = findViewById(R.id.yes_button_layout);
        submitTextView = findViewById(R.id.btn_yes_textview);

        cancelLayout.setOnTouchListener(this);
        submitLayout.setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final Marker marker = hashMapMarker.get("Selected Location");
        switch (v.getId()) {
            case R.id.no_button_layout:
                if (event.getAction() != MotionEvent.ACTION_DOWN) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        animPressRealease = AnimationUtils.loadAnimation(activity, R.anim.alpha_animation_released);
                        cancelTextView.startAnimation(animPressRealease);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(dist_metric_layout == 0){
                                }
                                else if(dist_metric_layout == 1) {
                                    distanceCalcLayout.setVisibility(View.VISIBLE);
                                }
                                if(destination_input_click == 0) {
                                }
                                else if(destination_input_click ==1){
                                    destination_input_click = 0;
                                    searchResultLayout.setVisibility(View.GONE);
                                }
                                //user might have selected a location but doesnt want to submit it. in that case, the selected location on the map (marker) should be deleted.
//                                Log.d(TAG, marker.getTitle());
                                marker.remove();
                                hashMapMarker.remove("SelectedLocation");

                                user_loc_input = null;

                                if(destination_input_click == 1) {

                                }
                                map_input_status = 0;
                                dismiss();
                            }
                        }, 200);
                        break;
                    }
                }
                animPressDown = AnimationUtils.loadAnimation(activity, R.anim.alpha_animation_pressed);
                cancelTextView.startAnimation(animPressDown);
                break;
            case R.id.yes_button_layout:
                if (event.getAction() != MotionEvent.ACTION_DOWN) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        animPressRealease = AnimationUtils.loadAnimation(activity, R.anim.alpha_animation_released);
                        submitTextView.startAnimation(animPressRealease);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if(dist_metric_layout == 0){
                                }
                                else if(dist_metric_layout == 1) {
                                    distanceCalcLayout.setVisibility(View.VISIBLE);
                                }
                                addUserInputToDB(user_loc_input, System.currentTimeMillis());
                                //todo marker created on long click should be removed but the remaining points should be still visible.
                                /*googleMap.addMarker(new MarkerOptions()
                                        .position(user_loc_input)
                                        .title("Suggested BusStop Location"))
                                        .showInfoWindow();*/
                                map_input_status = 0;
                                //after user submits the location, it has to be removed from the map
//                                Log.d(TAG, marker.getTitle());
                                marker.remove();
                                hashMapMarker.remove("SelectedLocation");
                            }
                        }, 200);
                        break;
                    }
                }
                animPressDown = AnimationUtils.loadAnimation(activity, R.anim.alpha_animation_pressed);
                submitTextView.startAnimation(animPressDown);
                break;
        }
        return true;
    }

    public void addUserInputToDB(LatLng userinput, long timeStamp) {
        // adding the location data into LocationInfo table.
//        Log.i(TAG, String.valueOf(userinput));
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
//        Log.d(TAG, String.valueOf(userList.size()));
        if (userList.size() != 0) {
            if (dbHandler.dbSyncCount() != 0) {
                progressView.showLoader();
                params.add("usersInput", gson.toJson(displaypoints));
                //Toast.makeText(getApplicationContext(), "device_id:" + uuid, Toast.LENGTH_LONG).show();
                //params.add("device_id", prefs.getString("ad_id", null));
                Log.d("Sync", params.toString());
                client.post(Constants.API_Insert_UserInput, params, new AsyncHttpResponseHandler() {
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
                        progressView.hideLoader();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        Log.d("Sync", "OnFailure Function");
                        progressView.hideLoader();
                        if (statusCode == 404) {
                            Toast.makeText(activity, "Requested resource not found", Toast.LENGTH_LONG).show();
                        } else if (statusCode == 500) {
                            Toast.makeText(activity, "Something went wrong at server end", Toast.LENGTH_LONG).show();
                        } else {
                            Log.d("Sync", error.getMessage());
                            Toast.makeText(activity, "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet]", Toast.LENGTH_LONG).show();
                        }
                    }

                });
            } else {
                Toast.makeText(activity, "SQLite and Remote MySQL DBs are in Sync!", Toast.LENGTH_LONG).show();
            }
        }
        dismiss();
        Toast.makeText(activity, "Thank you for providing Bus Stop Information.", Toast.LENGTH_LONG).show();
    }
}