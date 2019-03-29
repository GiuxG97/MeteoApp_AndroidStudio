package com.example.meteo20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.UUID;

public class DBAdapter {

    private static final String TAG = "DBAdapter"; //used for logging database version changes
    // Field Names:
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_CAPITAL = "capital";
    public static final String KEY_LAT = "lat";
    public static final String KEY_LON = "lon";
    public static final String KEY_ACTUAL = "actual";
    public static final String[] ALL_KEYS = new String[]{KEY_ROWID, KEY_NAME, KEY_CAPITAL, KEY_LAT, KEY_LON, KEY_ACTUAL};

    public static String DATABASE_NAME = "cit19.db";
    public static final String DATABASE_TABLE = "mainToDo";
    public static final int DATABASE_VERSION = 2; // The version number must be incremented each time a change to DB structure occurs.
    //SQL statement to create database

    private static final String DATABASE_CREATE_SQL =
            "CREATE TABLE " + DATABASE_TABLE
                    + " (" + KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_NAME + " TEXT NOT NULL, "
                    + KEY_CAPITAL + " TEXT,"
                    + KEY_LAT + " NUMBER,"
                    + KEY_LON + " NUMBER,"
                    + KEY_ACTUAL + " NUMBER" +
                    ");";


    private final Context context;
    private DatabaseHelper myDBHelper;
    private SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        myDBHelper = new DatabaseHelper(context);
    }

    // Open the database connection.
    public DBAdapter open() {
        db = myDBHelper.getWritableDatabase();
        return this;
    }

    // Close the database connection.
    public void close() {
        myDBHelper.close();
    }

    // Add a new set of values to be inserted into the database.
    public long insertRow(String task, String date) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, task);
        initialValues.put(KEY_CAPITAL, date);
        // Insert the data into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }


    public long insertRowPro(City c) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, c.getmCity());
        initialValues.put(KEY_CAPITAL, c.getmNation());
        initialValues.put(KEY_LAT, c.getmLat());
        initialValues.put(KEY_LON, c.getmLon());
        initialValues.put(KEY_ACTUAL, c.getmActualTemperature());
        // Insert the data into the database.
        return db.insert(DATABASE_TABLE, null, initialValues);
    }


    // Change an existing row to be equal to new data.
    public boolean updateRow(long rowId, City c) {
        String where = KEY_ROWID + "=" + rowId;
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME, c.getmCity());
        newValues.put(KEY_CAPITAL, c.getmNation());
        newValues.put(KEY_LAT, c.getmLat());
        newValues.put(KEY_LON, c.getmLon());
        newValues.put(KEY_ACTUAL, c.getmActualTemperature());
        // Insert it into the database.
        return db.update(DATABASE_TABLE, newValues, where, null) != 0;
    }//KEY_LAT,KEY_LON,KEY_ACTUAL


    public void stampaTutto() {
        for (City v : this.getAll()
        ) {
            Log.e("DB ", v.toString());
        }
    }


    // Delete a row from the database, by rowId (primary key)
    public boolean deleteRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        return db.delete(DATABASE_TABLE, where, null) != 0;
    }

    public void deleteAll() {
        Cursor c = getAllRows();
        long rowId = c.getColumnIndexOrThrow(KEY_ROWID);
        if (c.moveToFirst()) {
            do {
                deleteRow(c.getLong((int) rowId));
            } while (c.moveToNext());
        }
        c.close();
    }


    // Return all data in the database.
    public Cursor getAllRows() {
        String where = null;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }

    // Get a specific row (by rowId)
    public Cursor getRow(long rowId) {
        String where = KEY_ROWID + "=" + rowId;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if (c != null) {
            c.moveToFirst();
        }
        return c;
    }


    public ArrayList<City> getAll() {
        ArrayList<City> result = new ArrayList<>();
        Cursor c = getAllRows();
        while (c.moveToNext()) {
            //String id = c.getString(c.getColumnIndex(KEY_ROWID));
            String name = c.getString(c.getColumnIndex(KEY_NAME));
            String nation = c.getString(c.getColumnIndex(KEY_CAPITAL));
            Double lat = c.getDouble(c.getColumnIndex(KEY_LAT));
            Double lon = c.getDouble(c.getColumnIndex(KEY_LON));
            Double actual = c.getDouble(c.getColumnIndex(KEY_ACTUAL));
            City ob;
            try {
                ob = new City(name, nation, lat, lon, actual);
                result.add(ob);
            } catch (Exception e) {
                Log.e("Errore", "Error " + e.toString());
            }

        }
        return result;
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATE_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application's database from version " + oldVersion
                    + " to " + newVersion + ", which will destroy all old data!");
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(_db);
        }
    }
}

