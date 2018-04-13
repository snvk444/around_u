package materialdesign.snvk.com.materialdesign.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Venkata on 3/28/2018.
 */

public class DBHandler extends SQLiteOpenHelper {

    private static final String TAG = "databaseses";

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "AroundU_DB";
    // table name
    private static final String TABLE_NAME = "pivottabledata";
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
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
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
    public List<PivotTableData> getFromPivotTableData() {
        List<PivotTableData> markersList = new ArrayList<PivotTableData>();
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_NAME;
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


}
