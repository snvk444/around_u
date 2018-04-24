package materialdesign.snvk.com.materialdesign.app;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import materialdesign.snvk.com.materialdesign.R;
import materialdesign.snvk.com.materialdesign.adapter.RecyclerAdapter;
import materialdesign.snvk.com.materialdesign.model.Landscape;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = "MainActivity";
    Toolbar toolbar;
    private MarkerOptions options = new MarkerOptions();
    GoogleMap mMap = null;
    BufferedReader reader = null;
    RecyclerView itemsList;
    ArrayList<ItemsListSingleItem> data = new ArrayList<>();

    Toolbar myToolbar;
    Spinner mySpinner ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i(TAG, "Toolbar");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Around U!");
        toolbar.setSubtitle("Know your city");


//        setUpRecyclerView();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerAdapter adapter = new RecyclerAdapter(this, Landscape.getData());
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //map fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Compatibility by JAVA
        //check if SDK >= 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //call some material design APIs here
            //toolbar.setElevation(10f);
        } else { //for SDK < 21
            //implement this feature without material design.
        }

        //data from db
        /////////
        DBHandler db = new DBHandler(this);
        Log.d("TAG", "Inserting");
        List<LatLng> latLngList = new ArrayList<LatLng>();
        String line = "";
        try {
            Log.i(TAG, "Reading LocationReadings.csv to db");
            InputStream is = getResources().openRawResource(R.raw.locationreadings);
            reader = new BufferedReader(new InputStreamReader(is));
        } catch (Exception e) {
            Log.i(TAG, "Reading LocationReadings.csv to db failed");
        }
        //reading data into database from csv file
        try {
            while ((line = reader.readLine()) != null) // Read until end of file
            {
                Log.i(TAG, "read lat and long");
                String[] lines = line.split(",");
                String identifier = lines[3];
                float lat = Float.parseFloat(lines[4]);
                float lon = Float.parseFloat(lines[5]);
                String brand = lines[6];
                String name = lines[7];
                String address = lines[8];
                int zipcode = Integer.parseInt(lines[2]);
                String city = lines[1];
                String district = lines[0];
                String state = "Andhra Pradesh";

                db.addPivotTableData(new PivotTableData(identifier, lat, lon, name,brand, address, zipcode, city, district, state));
                Log.i(TAG, "Reading data into table " + identifier + "," + lat + "," +lon + "," + brand +"," + name +"," + address + "," +zipcode + "," + city + "," + state);
            }
        } catch (IOException e) {
            Log.i(TAG, "Reading lat long failed");
            e.printStackTrace();
        }
        db.getIntoPivotTableData("Gas");

        //View r1 = (View) findViewById(R.id.refresh);
        //r1.setOnClickListener((View.OnClickListener) this);
        /////////

        myToolbar = (Toolbar) findViewById(R.id.toolbar);
        mySpinner = (Spinner) findViewById(R.id.spinner);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.names));
        myAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        mySpinner.setAdapter(myAdapter);

        //have to add setOnItemClickListener (Available)
        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, mySpinner.getSelectedItem().toString(),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        ////////
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        String msg = "";
        switch (item.getItemId()) {
            case R.id.location:
                Log.i(TAG, "intent start");
                Intent i = new Intent(getApplicationContext(),LocationReminder.class);
                Log.i(TAG, "intent start_1");
                startActivity(i);
                Log.i(TAG, "intent start_2");
//                setContentView(R.layout.content_location_reminder);
                Log.i(TAG, "intent end");

                msg = "Refresh";
                break;
            case R.id.spinner:
                Log.i(TAG, "intent start");
                Intent j = new Intent(getApplicationContext(),LocationReminder.class);
                Log.i(TAG, "intent start_1");
                startActivity(j);
                Log.i(TAG, "intent start_2");
//                setContentView(R.layout.content_location_reminder);
                Log.i(TAG, "intent end");

                msg = "Settings";
                break;
            case R.id.exit:
                msg = "Exit";
                break;
        }
        Toast.makeText(this, msg + " Clicked!", Toast.LENGTH_SHORT).show();
        return super.onOptionsItemSelected(item);
    }


    private void setUpRecyclerView() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        RecyclerAdapter adapter = new RecyclerAdapter(this, Landscape.getData());
        LinearLayoutManager mLinearLayoutManagerVertical = new LinearLayoutManager(this);
        mLinearLayoutManagerVertical.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(mLinearLayoutManagerVertical);
        recyclerView.setAdapter(adapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        List<LatLng> latLngList = new ArrayList<LatLng>();
        String line = "";
//        try {
//            Log.i(TAG, "Reading LocationReadings.csv");
//            InputStream is = getResources().openRawResource(R.raw.locationreadings);
//            reader = new BufferedReader(new InputStreamReader(is));
//        } catch (Exception e) {
//            Log.i(TAG, "Reading LocationReadings.csv failed");
//        }
//
//        try {
//            while ((line = reader.readLine()) != null) // Read until end of file
//            {
//                Log.i(TAG, "read lat and long");
//                String[] lines = line.split(",");
//                double lat = Double.parseDouble(lines[4]);
//                double lon = Double.parseDouble(lines[5]);
//                latLngList.add(new LatLng(lat, lon));
//
//                Log.i(TAG, "successful - obtaining lat and long");
//            }
//        } catch (IOException e) {
//            Log.i(TAG, "Reading lat long failed");
//            e.printStackTrace();
//        }
//        System.out.println(latLngList.get(0).latitude);
//        System.out.println(latLngList.get(0).longitude);

         //Reading the data from database
        DBHandler db = new DBHandler(this);
        List<PivotTableData> markers = db.getFromPivotTableData();
        for(PivotTableData marker : markers) {
            Log.i(TAG, "Reading data from table " + marker.identifier + "," + marker.latitude + "," + marker.longitude );
            double lat = marker.latitude;
            double lon = marker.longitude;
            latLngList.add(new LatLng(lat, lon));
        }

        for (LatLng point : latLngList) {
            Log.i(TAG, "Displaying map Fragment");
          LatLng test = new LatLng(point.latitude, point.longitude);
            googleMap.addMarker(new MarkerOptions().position(test)
                    .title("Marker in Sydney"));
            Log.i(TAG, "Point: " + point);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(point));
        }


        //        LatLng NEWARK = new LatLng(40.714086, -74.228697);
        //        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
        //        .image(BitmapDescriptorFactory.fromResource(R.drawable.parks))
        //                .position(NEWARK, 8600f, 6500f);
        //        googleMap.addGroundOverlay(newarkMap);
    }

    public void onClick(MainActivity v)
    {
        // TODO Auto-generated method stub
        Log.i(TAG, "in onclick function");
        Intent i = new Intent(getApplicationContext(),LocationReminder.class);
        startActivity(i);
        setContentView(R.layout.content_location_reminder);
    }
}
