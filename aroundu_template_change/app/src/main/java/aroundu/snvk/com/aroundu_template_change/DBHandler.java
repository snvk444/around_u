package aroundu.snvk.com.aroundu_template_change;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venkata on 5/30/2018.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "databaseses";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "AroundU_DB";
    // table name
    private static final String TABLE_NAME = "pivottabledata";
    private static final String LOC_TABLE_NAME = "location_info";
    private static final String LINES_TABLE_NAME = "Lines";
    // Table Columns names
    private static final String KEY_ID = "id";
    private static final String IDENTIFIER = "identifier";
    private static final String LATITUDE = "latitude";
    private static final String LONGITUDE = "longitude";
    private static final String NAME = "name";
    private static final String BRAND = "brand";
    private static final String ADDRESS = "address";
    private static final String ZIPCODE = "zipcode";
    private static final String CITY = "city";
    private static final String DISTRICT = "district";
    private static final String STATE = "state";
    private static final String TIME_STAMP = "time_stamp";
    private static final String LINE_ID = "line_id";
    private static final String BUS_NO = "Bus_no";
    private static final String SOURCE_STATION = "Source_station";
    private static final String DESTINATION_STATION = "Destination_station";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CONTENTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + IDENTIFIER + " TEXT ,"
                + LATITUDE + " DECIMAL(10,7) ,"
                + LONGITUDE + " DECIMAL(10,7) ,"
                + NAME + " TEXT ,"
                + BRAND + " TEXT,"
                + ADDRESS + " TEXT ,"
                + ZIPCODE + " TEXT ,"
                + CITY + " TEXT ,"
                + DISTRICT + " TEXT ,"
                + STATE + " TEXT"
                + ")";
        Log.i(TAG, "Create table " + CREATE_CONTENTS_TABLE);
        db.execSQL(CREATE_CONTENTS_TABLE);

        String CREATE_LOCATION_TABLE = "CREATE TABLE " + LOC_TABLE_NAME + "("
                + TIME_STAMP + " TEXT PRIMARY KEY,"
                + LATITUDE + " DECIMAL(10,7) ,"
                + LONGITUDE + " DECIMAL(10,7)"
                + ")";
        Log.i(TAG, "Create table " + CREATE_LOCATION_TABLE);
        db.execSQL(CREATE_LOCATION_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LOC_TABLE_NAME);
        // Creating tables again
        onCreate(db);
    }

    //adding source data into table TABLE_SOURCE.

    public void addPivotTableData(PivotTableData pt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(IDENTIFIER, pt.getIdentifier());
        values.put(LATITUDE, pt.getLatitude());
        values.put(LONGITUDE, pt.getLongitude());
        values.put(NAME, pt.getName());
        values.put(BRAND, pt.getBrand());
        values.put(ADDRESS, pt.getAddress());
        values.put(ZIPCODE, pt.getZipcode());
        values.put(CITY, pt.getCity());
        values.put(DISTRICT, pt.getDistrict());
        values.put(STATE, pt.getState());

        // Inserting Row
        Log.i(TAG, "Inserting data" + values);
        long result = db.insertOrThrow(TABLE_NAME, null, values);
        Log.i(TAG, "Result" + result );
        db.close(); // Closing database connection
    }

    public ArrayList<PivotTableData> getIntoPivotTableData(String identifier) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[]{KEY_ID, IDENTIFIER, LATITUDE, LONGITUDE, NAME, BRAND, ADDRESS, ZIPCODE, CITY, DISTRICT, STATE},
                IDENTIFIER + "=?", new String[]{identifier}, null, null, null, null);

        ArrayList<PivotTableData> listPivots = new ArrayList<PivotTableData>();
        if (cursor != null) {
            Log.i(TAG, "Cursor size: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    PivotTableData pivot = new PivotTableData();
                    pivot.setIdentifier(cursor.getString(cursor.getColumnIndex("identifier")));
                    pivot.setAddress(cursor.getString(cursor.getColumnIndex("address")));
                    pivot.setLatitude(cursor.getFloat(cursor.getColumnIndex("latitude")));
                    pivot.setLongitude(cursor.getFloat(cursor.getColumnIndex("longitude")));
                    pivot.setBrand(cursor.getString(cursor.getColumnIndex("brand")));
                    pivot.setCity(cursor.getString(cursor.getColumnIndex("city")));
                    pivot.setDistrict(cursor.getString(cursor.getColumnIndex("district")));
                    pivot.setName(cursor.getString(cursor.getColumnIndex("name")));
                    pivot.setState(cursor.getString(cursor.getColumnIndex("state")));
                    listPivots.add(pivot);
//                    Log.i(TAG, "Retrieving data - " + pivot);
                }
                while (cursor.moveToNext());
            }
            Log.i(TAG, "List size: " + listPivots.size());
        }
// return shop
        return listPivots;
    }

    // Getting All Markers from database
    public List<PivotTableData> getFromPivotTableData(String item_selected_1) {
        Log.i(TAG,item_selected_1);
        List<PivotTableData> markersList = new ArrayList<PivotTableData>();
        String selectQuery = null;

        if (item_selected_1.equals(null)){
            // Select All Query
            selectQuery = "SELECT * FROM " + TABLE_NAME;
        }
        else {
            // Select specific identifier data Query
            selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE identifier= '" + item_selected_1 + "'";
//            selectQuery = "SELECT * FROM " + TABLE_NAME;
        }
        Log.i(TAG,selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
// looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PivotTableData pt = new PivotTableData();
                pt.setId(Integer.parseInt(cursor.getString(0)));
                pt.setIdentifier(cursor.getString(1));
                pt.setLatitude(cursor.getFloat(2));
                pt.setLongitude(cursor.getFloat(3));
                pt.setName(cursor.getString(4));
                pt.setAddress(cursor.getString(5));
                pt.setZipcode(cursor.getInt(6));
                pt.setCity(cursor.getString(7));
                pt.setDistrict(cursor.getString(8));
                pt.setState(cursor.getString(9));

// Adding markers to list
                markersList.add(pt);
            } while (cursor.moveToNext());
            Log.i(TAG, "List size display points: " + markersList.size());
        }
// return contact list

        return markersList;
    }

    @SuppressLint("LongLogTag")
    public void addLocationInfo(LocationInfo pt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TIME_STAMP, pt.getTime_stamp());
        values.put(LATITUDE, pt.getLatitude());
        values.put(LONGITUDE, pt.getLongitude());

        // Inserting Row
        Log.i(TAG, "Inserting data" + values);
        long result = db.insertOrThrow(LOC_TABLE_NAME, null, values);
        Log.i(TAG, "Result" + result );
        db.close(); // Closing database connection
    }

    public ArrayList<LocationInfo> readLocationInfo(double latitude, double longitude) {
        SQLiteDatabase db = this.getReadableDatabase();
        double minlat = latitude-0.01;
        double maxlat = latitude+0.01;
        double minlng = longitude-0.01;
        double maxlng = longitude+0.01;
        //Cursor cursor = db.rawQuery("Select * from " +LOC_TABLE_NAME+ " where LATITUDE >= " +minlat+ " and LATITUDE <= " +maxlat+ " and LONGITUDE >= " +minlng+ " and LONGITUDE <= " +maxlng, null);
        Cursor cursor = db.rawQuery("Select * from " + LOC_TABLE_NAME, null);

        ArrayList<LocationInfo> displaypoints = new ArrayList<>();
        LocationInfo li;
        while(cursor.moveToNext()) {
            li = new LocationInfo();
            //li = new LocationInfo(cursor.getDouble(1), cursor.getDouble(2));
            li.setTime_stamp(cursor.getLong(0));
            li.setLatitude(cursor.getDouble(1));
            li.setLongitude(cursor.getDouble(2));

            displaypoints.add(li);
            Log.i(TAG, " Points to Display from readLocationInfo:" + li.getLongitude());
        }

        cursor.close();
        return displaypoints;
    }

    @SuppressLint("LongLogTag")
    public List readLocationInfo_1() {
        SQLiteDatabase db = this.getReadableDatabase();
        //Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
        Cursor cursor = db.rawQuery("Select * from " +LOC_TABLE_NAME + " limit 50", null);
        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(LATITUDE));
            Log.i(TAG, " TimeStamp: " +cursor.getDouble(0)+ "Latitude:" +cursor.getDouble(1)+ "Longitude:" +cursor.getDouble(2));
            itemIds.add(itemId);
        }
        cursor.close();
        return itemIds;
    }


    @SuppressLint("LongLogTag")
    public List TotalCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select count(*) from " +TABLE_NAME, null);
        List itemIds = new ArrayList<>();
        while(cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(LATITUDE));
            Log.i(TAG, String.valueOf(cursor.getInt(0)));
            itemIds.add(itemId);
        }
        cursor.close();
        return itemIds;
    }



    //******** BusRoutes RELATED DBHANDLERS *******//
    public void addBusLinesData(IdentifierBusInfo ib) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LINE_ID, ib.getLineid());
        values.put(BUS_NO, ib.getBusno());
        values.put(SOURCE_STATION, ib.getSourceLocation());
        values.put(DESTINATION_STATION, ib.getDestinationLocation());

        long result = db.insertOrThrow(LINES_TABLE_NAME, null, values);
        db.close(); // Closing database connection
    }

    public List<IdentifierBusInfo> getBusLinesData(String src_location, String dest_location) {
        List<IdentifierBusInfo> markersList = new ArrayList<IdentifierBusInfo>();
        String selectQuery = null;

        selectQuery = "SELECT * FROM " + LINES_TABLE_NAME + " WHERE SOURCE_STATION= '" + src_location + "' AND DESTINATION_STATION= '" + dest_location + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                IdentifierBusInfo ib = new IdentifierBusInfo();
                ib.setLineid(Integer.parseInt(String.valueOf(cursor.getInt(0))));
                ib.setBusno(cursor.getString(1));
                ib.setSourceLocation(cursor.getString(2));
                ib.setDestinationLocation(cursor.getString(3));
                // Adding markers to list
                markersList.add(ib);
            } while (cursor.moveToNext());

            Log.i(TAG, "Total points available for that selected Identifier: " + markersList.size());
        }
        return markersList;
    }



}