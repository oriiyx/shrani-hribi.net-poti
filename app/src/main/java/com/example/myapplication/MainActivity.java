package com.example.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    EntryDatabaseHelper entryDatabaseHelper;

    EntryImageDatabaseHelper entryImageDatabaseHelper;

    private ListView mListView;

    EntryScraper entryScraper;

    private EditText inputField;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        entryDatabaseHelper = new EntryDatabaseHelper(this);
        entryImageDatabaseHelper = new EntryImageDatabaseHelper(this);
        mListView = (ListView) findViewById(R.id.listView);
        inputField = (EditText) findViewById(R.id.source);
        button = (Button) findViewById(R.id.addNewEntryButton);
        populateListView();
        entryScraper = new EntryScraper(this);
        inputField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                button.setBackgroundColor(getResources().getColor(R.color.green));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    public void populateListView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Cursor data = entryDatabaseHelper.getData();
                ArrayList<Entity> listData = new ArrayList<>();

                while (data.moveToNext()) {
                    Entity entity = new Entity(data.getInt(0), data.getString(1));
                    listData.add(entity);
                }

                AdapterEntity entityAdapter = new AdapterEntity(MainActivity.this, 0, listData);

                mListView.setAdapter(entityAdapter);

                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Object entityObject = adapterView.getItemAtPosition(i);
                        int ID = ((Entity) entityObject).getId();

                        Intent entryScreenIntent = new Intent(MainActivity.this, EntryScreenActivity.class);
                        entryScreenIntent.putExtra("id", ID);
                        startActivity(entryScreenIntent);
                        Log.d("testing", "onItemClick: You Clicked on ID " + ID);
                    }
                });
            }
        });
    }

    public void addNewEntry(View view) {
        // Find by ID and get data from input field
        String urlSourceString;
        urlSourceString = inputField.getText().toString();
        boolean isURILegit = verifyURL(urlSourceString);

        if (isURILegit) {
            try {
                entryScraper.scrapeUrl(urlSourceString, view, this);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            button.setBackgroundColor(getResources().getColor(R.color.red));
        }
    }


    private boolean verifyURL(String url) {
        if (url.isEmpty()) {
            return false;
        }

        try {
            URI u = new URI(url);
            u.toURL();
        } catch (Exception e) {
            return false;
        }

        String[] tokens = url.split("/izlet/");

        if (tokens.length != 2) {
            return false;
        }

        String[] navigationArrays = tokens[1].split("/");

        return navigationArrays.length == 4;
    }


}