package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;

public class EntryImageDatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "EntryImgDatabaseHelper";

    private static final String TABLE_NAME = "entry_image_table";
    private static final String COL0 = "ID";
    private static final String COL1 = "entry_ID";
    private static final String COL2 = "image_blob";
    private static final String COL3 = "description";

    public EntryImageDatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL1 + " INT," +
                COL2 + " BLOB, " +
                COL3 + " TEXT)";

        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }


    public void addData(int entryID,
                        byte[] imageBlob,
                        String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL1, entryID);
        contentValues.put(COL2, imageBlob);
        contentValues.put(COL3, description);

        Log.d(TAG, "addData : " + contentValues);

        long result = db.insert(TABLE_NAME, null, contentValues);
        Log.d(TAG, "result : " + result);
        db.close();
    }

    public void deleteItem(int entryID) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE " + COL1 + " = " + entryID;
        db.execSQL(query);
        db.close();
    }

    public ArrayList<EntryGallery> getGalleryImagesWithDescription(int entryID) {
        ArrayList<EntryGallery> gallery;
        gallery = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + COL1 + " = " + entryID, null);

        while (data.moveToNext()) {
            EntryGallery entryGallery = new EntryGallery();
            byte[] bitmap = data.getBlob(2);
            entryGallery.image = BitmapFactory.decodeByteArray(bitmap, 0, bitmap.length);
            entryGallery.description = data.getString(3);
            entryGallery.imageID = data.getInt(0);
            gallery.add(entryGallery);
        }

        db.close();
        return gallery;
    }

    public Bitmap getOnlyImage(int imageID) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT " + COL2 + " FROM " + TABLE_NAME + " WHERE " + COL0 + " = " + imageID + " LIMIT 1", null);
        data.moveToFirst();
        byte[] dataBlob = data.getBlob(0);
        db.close();
        return BitmapFactory.decodeByteArray(dataBlob, 0, dataBlob.length);
    }
}
