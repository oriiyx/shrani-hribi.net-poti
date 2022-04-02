package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class EntryScreenActivity extends AppCompatActivity {

    private static final String TAG = "EntryScreenActivity";

    private Button btnDelete;
    private TextView entryMainTitle, entryMainStartLocation, entryMainEndLocation, entryMainTime, entryMainDifficulty, entryMainAltitudeDifferance, entryMainRoadAltitudeDifferance, entryMainStaringPointDirections, entryMainRouteDirections;

    ListView galleryListView;

    EntryDatabaseHelper entryDatabaseHelper;
    EntryImageDatabaseHelper entryImageDatabaseHelper;

    private int entityID;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry_screen_main);
        btnDelete = (Button) findViewById(R.id.entryDelete);

        entryMainTitle = (TextView) findViewById(R.id.entryMainTitle);
        entryMainStartLocation = (TextView) findViewById(R.id.entryMainStartLocation);
        entryMainEndLocation = (TextView) findViewById(R.id.entryMainEndLocation);
        entryMainTime = (TextView) findViewById(R.id.entryMainTime);
        entryMainDifficulty = (TextView) findViewById(R.id.entryMainDifficulty);
        entryMainAltitudeDifferance = (TextView) findViewById(R.id.entryMainAltitudeDifferance);
        entryMainRoadAltitudeDifferance = (TextView) findViewById(R.id.entryMainRoadAltitudeDifferance);
        entryMainStaringPointDirections = (TextView) findViewById(R.id.entryMainStaringPointDirections);
        entryMainRouteDirections = (TextView) findViewById(R.id.entryMainRouteDirections);

        entryDatabaseHelper = new EntryDatabaseHelper(this);
        entryImageDatabaseHelper = new EntryImageDatabaseHelper(this);

        Intent receivedIntent = getIntent();

        entityID = receivedIntent.getIntExtra("id", -1);

        Cursor entryData = entryDatabaseHelper.getItemData(entityID);

        while (entryData.moveToNext()) {
            entryMainTitle.setText(entryData.getString(1));
            entryMainStartLocation.setText(entryData.getString(2));
            entryMainEndLocation.setText(entryData.getString(3));
            entryMainTime.setText(entryData.getString(4));
            entryMainDifficulty.setText(entryData.getString(5));
            entryMainAltitudeDifferance.setText(entryData.getString(6));
            entryMainRoadAltitudeDifferance.setText(entryData.getString(7));
            entryMainStaringPointDirections.setText(entryData.getString(8));
            entryMainRouteDirections.setText(entryData.getString(9));
        }

        ArrayList<EntryGallery> entryGalleryArrayList = entryImageDatabaseHelper.getGalleryImagesWithDescription(entityID);


        galleryListView = (ListView) findViewById(R.id.galleryListView);
        CustomGalleryListView customGalleryListView = new CustomGalleryListView(this, entryGalleryArrayList);
        galleryListView.setAdapter(customGalleryListView);

        galleryListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int action = motionEvent.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        view.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        view.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                view.onTouchEvent(motionEvent);
                return true;
            }
        });

        galleryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object entityObject = adapterView.getItemAtPosition(i);
                int ID = ((EntryGallery) entityObject).imageID;

                Intent singleGalleryImageIntent = new Intent(EntryScreenActivity.this, SingleImageActivity.class);
                singleGalleryImageIntent.putExtra("id", ID);
                startActivity(singleGalleryImageIntent);
                Log.d(TAG, "onItemClick: You Clicked on ID " + ID);
            }
        });


        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                entryDatabaseHelper.deleteItem(entityID);

                Intent returnToMainActivity = new Intent(EntryScreenActivity.this, MainActivity.class);
                startActivity(returnToMainActivity);
            }
        });

    }
}