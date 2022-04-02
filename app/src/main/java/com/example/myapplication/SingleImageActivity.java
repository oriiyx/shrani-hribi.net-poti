package com.example.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jsibbold.zoomage.ZoomageView;

public class SingleImageActivity extends AppCompatActivity {

    EntryImageDatabaseHelper entryImageDatabaseHelper;

    ZoomageView imageView;

    private int imagedID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_image);
        entryImageDatabaseHelper = new EntryImageDatabaseHelper(this);

        imageView = (ZoomageView) findViewById(R.id.singleGalleryImageView);

        Intent receivedIntent = getIntent();
        imagedID = receivedIntent.getIntExtra("id", -1);

        Bitmap image = entryImageDatabaseHelper.getOnlyImage(imagedID);
        imageView.setImageBitmap(image);
    }
}