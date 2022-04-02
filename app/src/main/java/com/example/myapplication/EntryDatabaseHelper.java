package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class EntryDatabaseHelper extends SQLiteOpenHelper {

    EntryImageDatabaseHelper imageDBHelper;

    private static final String TAG = "EntryDatabaseHelper";

    private static final String TABLE_NAME = "entry_table";
    private static final String COL0 = "ID";
    private static final String COL1 = "entry_name";
    private static final String COL2 = "start_location_name";
    private static final String COL3 = "end_location_name";
    private static final String COL4 = "time";
    private static final String COL5 = "difficulty";
    private static final String COL6 = "altitude_differance";
    private static final String COL7 = "road_altitude_differance";
    private static final String COL8 = "staring_point_directions";
    private static final String COL9 = "route_directions";


    public EntryDatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
        imageDBHelper = new EntryImageDatabaseHelper(context);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 + " TEXT," +
                COL2 + " TEXT, " +
                COL3 + " TEXT, " +
                COL4 + " TEXT, " +
                COL5 + " TEXT, " +
                COL6 + " TEXT, " +
                COL7 + " TEXT, " +
                COL8 + " TEXT, " +
                COL9 + " TEXT)";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public DataEntryResult addData(String entryName,
                                   String startLocationName,
                                   String endLocationName,
                                   String time,
                                   String difficulty,
                                   String altitudeDifferance,
                                   String roadAltitudeDifferance,
                                   String staringPointDirections,
                                   String routeDirections) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL1, entryName);
        contentValues.put(COL2, startLocationName);
        contentValues.put(COL3, endLocationName);
        contentValues.put(COL4, time);
        contentValues.put(COL5, difficulty);
        contentValues.put(COL6, altitudeDifferance);
        contentValues.put(COL7, roadAltitudeDifferance);
        contentValues.put(COL8, staringPointDirections);
        contentValues.put(COL9, routeDirections);

        Log.d(TAG, "addData : " + contentValues);

        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.d(TAG, "result : " + result);

        DataEntryResult dataEntryResult = new DataEntryResult();

        if (result != -1) {
            dataEntryResult.id = (int) result;
            dataEntryResult.success = true;
        } else {
            dataEntryResult.success = false;
        }
        return dataEntryResult;
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public Cursor getItemData(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COL0 + " = " + id;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public void deleteItem(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL0 + " = " + id;
        db.execSQL(query);
        imageDBHelper.deleteItem(id);
    }
}
