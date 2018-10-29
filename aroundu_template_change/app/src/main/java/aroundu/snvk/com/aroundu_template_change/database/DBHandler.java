package aroundu.snvk.com.aroundu_template_change.database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import aroundu.snvk.com.aroundu_template_change.vo.IdentifierBusInfo;
import aroundu.snvk.com.aroundu_template_change.vo.LocationInfo;
import aroundu.snvk.com.aroundu_template_change.PivotTableData;

/**
 * Created by Venkata on 5/30/2018.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "databaseses";


    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "AroundU_DB";
    private static final String DATABASE_PATH = "/data/data/aroundu.snvk.com.aroundu_template_change/databases/";
    // table name
    private static final String TABLE_NAME = "pivottabledata";
    private static final String LOC_TABLE_NAME = "location_info";
    private static final String LINES_TABLE_NAME = "Lines";
    private static final String DEST_LOOKUP_TABLE_NAME = "destination_lookup_tbl";
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
    private static final String DIRECTION = "Direction";
    private static final String SEQUENCE = "Sequence";
    private static final String DEST_LOOK_UP = "dest_look_up";
    private static final String ACTUAL_NAME = "actual_name";

    private static DBHandler mInstance = null;

//    public DBHandler(Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }

    protected Context mContext;

    public synchronized static DBHandler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBHandler(context.getApplicationContext());
        }
        return mInstance;
    }


    protected DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
        SQLiteDatabase db = getReadableDatabase();
        db = null;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        synchronized (this) {
            Log.d("Export", "onCreate");

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

            String CREATE_LINES_TABLE = "CREATE TABLE " + LINES_TABLE_NAME + "("
                    + LINE_ID + " INTEGER, "
                    + BUS_NO + " TEXT, "
                    + SOURCE_STATION + " TEXT, "
                    + DESTINATION_STATION + " TEXT, "
                    + DIRECTION + " INTEGER, "
                    + SEQUENCE + " INTEGER "
                    + ")";
            db.execSQL(CREATE_LINES_TABLE);

            String CREATE_DEST_TABLE = "CREATE TABLE " + DEST_LOOKUP_TABLE_NAME + "("
                    + ACTUAL_NAME + " TEXT, "
                    + DEST_LOOK_UP + " TEXT "
                        +")";
            db.execSQL(CREATE_DEST_TABLE);
        }
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
        Log.d("DebugTest", "addPivotTableData");
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
        Log.i(TAG, "Result" + result);
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
    public List<PivotTableData> getFromPivotTableData(String item_selected_1, double latitude, double longitude) {
        Log.i(TAG, item_selected_1);
        List<PivotTableData> markersList = new ArrayList<PivotTableData>();
        String selectQuery = null;
        double lat1 = latitude - 0.11;
        double lat2 = latitude + 0.11;
        double lng1 = longitude - 0.11;
        double lng2 = longitude + 0.11;
        double minlat;
        double minlng;
        double maxlat;
        double maxlng;

        if (lat1 > lat2) {
            minlat = lat2;
            maxlat = lat1;
        } else {
            minlat = lat1;
            maxlat = lat2;
        }
        if (lng1 > lng2) {
            minlng = lng2;
            maxlng = lng1;
        } else {
            minlng = lng1;
            maxlng = lng2;
        }

        if (item_selected_1.equals(null)) {
            // Select All Query
            selectQuery = "SELECT * FROM " + TABLE_NAME;
            Log.i(TAG, "Select Query:" + selectQuery);
        } else {
            // Select specific identifier data Query
            selectQuery = "SELECT * FROM " + TABLE_NAME + " " +
                    " WHERE identifier= '" + item_selected_1 +
                    "' and LATITUDE >= " + minlat + " " +
                    " and LATITUDE <= " + maxlat + " " +
                    " and LONGITUDE >= " + minlng + " " +
                    "and LONGITUDE <= " + maxlng;
//            selectQuery = "SELECT * FROM " + TABLE_NAME;
        }
        Log.i(TAG, selectQuery);
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
        Log.i(TAG, "Result" + result);
        db.close(); // Closing database connection
    }

    public ArrayList<LocationInfo> readLocationInfo(double latitude, double longitude) {
        SQLiteDatabase db = this.getReadableDatabase();
        double minlat = latitude;
        double maxlat = latitude ;
        double minlng = longitude;
        double maxlng = longitude;
        //Cursor cursor = db.rawQuery("Select * from " +LOC_TABLE_NAME+ " where
        // LATITUDE >= " +minlat+ " and LATITUDE <= " +maxlat+ " and LONGITUDE >= " +minlng+ " and LONGITUDE <= " +maxlng, null);
        Cursor cursor = db.rawQuery("Select * from " + LOC_TABLE_NAME, null);

        ArrayList<LocationInfo> displaypoints = new ArrayList<>();
        LocationInfo li;
        while (cursor.moveToNext()) {
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
        Cursor cursor = db.rawQuery("Select * from " + LOC_TABLE_NAME + " limit 50", null);
        List itemIds = new ArrayList<>();
        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(
                    cursor.getColumnIndexOrThrow(LATITUDE));
            Log.i(TAG, " TimeStamp: " + cursor.getDouble(0) + "Latitude:" + cursor.getDouble(1) + "Longitude:" + cursor.getDouble(2));
            itemIds.add(itemId);
        }
        cursor.close();
        return itemIds;
    }


    @SuppressLint("LongLogTag")
    public List TotalCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select count(*) from " + TABLE_NAME, null);
        List itemIds = new ArrayList<>();
        while (cursor.moveToNext()) {
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
        values.put(DIRECTION, ib.getDirection());
        values.put(SEQUENCE, ib.getSequence());

        long result = db.insertOrThrow(LINES_TABLE_NAME, null, values);
        Log.d("DBTest", "inserting: " + result);
    }

    public List<IdentifierBusInfo> getBusLinesData(String src_location, String dest_location) {
        List<IdentifierBusInfo> markersList = new ArrayList<IdentifierBusInfo>();
        String selectQuery = null;
        Log.i(TAG, "Destination " + dest_location + " Source " + src_location);
//        selectQuery = "SELECT * FROM " + LINES_TABLE_NAME + " WHERE SOURCE_STATION= '" + src_location + "' AND DESTINATION_STATION= '" + dest_location + "'";
        //selectQuery = "SELECT * FROM " + LINES_TABLE_NAME;
        //"WHERE SOURCE_STATION= '" + src_location + "' AND DESTINATION_STATION= '" + dest_location + "'";

        selectQuery = "select L1.line_id, L1.Bus_no, L1.Source_Station, L2.Destination_Station from " + LINES_TABLE_NAME + " L1 JOIN " + LINES_TABLE_NAME + " L2 ON ("
                + "L1." + LINE_ID + "= L2." + LINE_ID +
                " and L1." + BUS_NO + "= L2." + BUS_NO + " and L1." +
                DIRECTION + "= L2." + DIRECTION + ") WHERE " +
                "L1." + SOURCE_STATION + "='" + src_location + "' AND L2." + DESTINATION_STATION + "= '" + dest_location + "' " +
                "GROUP BY L1.line_id, L1.Bus_no, L1.Source_Station, L2.Destination_Station";
        Log.d("DBTest", "Query: " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                IdentifierBusInfo ib = new IdentifierBusInfo();
                //ib.setLineid(Integer.parseInt(String.valueOf(cursor.getInt(0))));
                ib.setBusno(cursor.getString(1));
                ib.setSourceLocation(cursor.getString(2));
                ib.setDestinationLocation(cursor.getString(3));
                // Adding markers to list
                markersList.add(ib);
            } while (cursor.moveToNext());

            Log.i(TAG, "Total points available for that selected Identifier: " + markersList.size());
        }
        Log.d("DBTest", "Marker list: " + markersList.size());
        return markersList;
    }

    public int getNumberOfStopsBetween(String src_location, String dest_location, String busNo) {
        List<IdentifierBusInfo> markersList = new ArrayList<IdentifierBusInfo>();
        String selectQuery = null;

        Log.i(TAG, "Destination " + dest_location + " Source " + src_location);
//        selectQuery = "SELECT * FROM " + LINES_TABLE_NAME + " WHERE SOURCE_STATION= '" + src_location + "' AND DESTINATION_STATION= '" + dest_location + "'";
        //selectQuery = "SELECT * FROM " + LINES_TABLE_NAME;
        //"WHERE SOURCE_STATION= '" + src_location + "' AND DESTINATION_STATION= '" + dest_location + "'";

        selectQuery = "select abs(L2." + SEQUENCE + "-L1." + SEQUENCE + ") from " + LINES_TABLE_NAME + " L1 JOIN " + LINES_TABLE_NAME + " L2 ON ("
                + "L1." + LINE_ID + "= L2." + LINE_ID +
                " and L1." + BUS_NO + "= L2." + BUS_NO + " and L1." +
                DIRECTION + "= L2." + DIRECTION + ") WHERE " +
                "L1." + SOURCE_STATION + "='" + src_location + "' AND L2." +
                DESTINATION_STATION + "= '" + dest_location + "' AND L1." + BUS_NO + "= '" + busNo +
                "' AND L2." + SEQUENCE + "> L1." + SEQUENCE +"" ;
        Log.d("DBTest", "Query: " + selectQuery);

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("DBTest", "Result: " + cursor.getCount());
        if(cursor.moveToFirst())
        {
            Log.d("cursor",cursor.getString(0));
        }
        return Integer.parseInt(cursor.getString(0));
    }

    public ArrayList<String> destinationLookup(String s) {
        ArrayList<String> results = new ArrayList<>();

        String selectQuery = "SELECT DISTINCT " + ACTUAL_NAME + " FROM " + DEST_LOOKUP_TABLE_NAME + " WHERE " + DEST_LOOK_UP + " LIKE '%" + s + "%'";
        Log.d("PredictiveTest", "Query: " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("PredictiveTest", "Query results: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                results.add(cursor.getString(0));
            } while (cursor.moveToNext());

            Log.d("PredictiveTest", "Total names: " + results);
        }

        return results;
    }

    public ArrayList<String> destinationLookupByCity(String s) {
        ArrayList<String> results = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + CITY + " LIKE '%" + s + "%'";
        Log.d("PredictiveTest", "Query: " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Log.d("PredictiveTest", "Query results: " + cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                results.add(cursor.getString(0));
            } while (cursor.moveToNext());

            Log.d("PredictiveTest", "Total names: " + results);
        }

        return results;
    }

    /**
     * Exports the database to local storage on the users device under a created folder called SignalTrackerDatabases
     */
    public void exportDB() {
        // TODO Auto-generated method stub
        Log.d("Export", "Method called");

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                Log.d("Export", "Begin copy");
                String currentDBPath = "//data//" + "aroundu.snvk.com.aroundu_template_change"
                        + "//databases//" + "AroundU_DB";
                String backupDBPath = "/AroundUDatabases/AroundU_DB.sqlite";
                File direct = new File(Environment.getExternalStorageDirectory() + "/AroundUDatabases");

                if (!direct.exists())
                    Log.d("Export", "Directory does not exist");
                {
                    if (direct.mkdir()) {
                        Log.d("Export", "Directory created");
                        //directory is created;
                    }
                }
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Toast.makeText(mContext, "Database exported",
                        Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            Log.d("Export", "Error: " + e.toString());
        }
    }

    public void importDatabase() {
        //Open your local db as the input stream
        try {
            Log.d("Export", "Begin import");
            InputStream mInput = mContext.getApplicationContext().getAssets().open(DATABASE_NAME + ".sqlite");
            String outFileName = DATABASE_PATH + DATABASE_NAME;
            OutputStream mOutput = new FileOutputStream(outFileName);
            byte[] mBuffer = new byte[2024];
            int mLength;
            while ((mLength = mInput.read(mBuffer)) > 0) {
                mOutput.write(mBuffer, 0, mLength);
            }
            mOutput.flush();
            mOutput.close();
            mInput.close();
            Log.d("Export", "Import successful");
        } catch (IOException e) {
            Log.d("Export", "Failure: " + e);
            e.printStackTrace();
        }
    }

    /**
     * This method will create database in application package /databases
     * directory when first time application launched
     **/
    public void createDataBase() {
        boolean mDataBaseExist = checkDataBase();
        Log.d("Export", "Database exists: " + mDataBaseExist);
        if (!mDataBaseExist) {
            this.getReadableDatabase();
        }
        importDatabase();
        close();
    }

    /**
     * This method checks whether database is exists or not
     **/
    private boolean checkDataBase() {
        try {
            final String mPath = DATABASE_PATH + DATABASE_NAME;
            final File file = new File(mPath);
            if (file.exists())
                return true;
            else
                return false;
        } catch (SQLiteException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addDestinationLookUpInfo(String actualName, String destLookUp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ACTUAL_NAME, actualName);
        values.put(DEST_LOOK_UP, destLookUp);

        long result = db.insertOrThrow(DEST_LOOKUP_TABLE_NAME, null, values);
        Log.d("DestLookUp", "inserting: " + result);
    }

}
