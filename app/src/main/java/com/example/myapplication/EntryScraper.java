package com.example.myapplication;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class EntryScraper {
    private static final String TAG = "EntryScraper";

    public static final String HRIBI = "https://www.hribi.net";

    EntryDatabaseHelper entryDatabaseHelper;

    EntryImageDatabaseHelper entryImageDatabaseHelper;

    MainActivity mainActivity;

    Button button;

    public EntryScraper(MainActivity _mainActivity) {
        entryDatabaseHelper = new EntryDatabaseHelper(_mainActivity);
        entryImageDatabaseHelper = new EntryImageDatabaseHelper(_mainActivity);
        mainActivity = _mainActivity;
    }

    public void scrapeUrl(String url, View view, MainActivity activity) throws IOException {
        button = view.findViewById(R.id.addNewEntryButton);

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    modifyButton(view, false, R.string.add_entry_button_text_loading, activity);
                    Document document;
                    document = Jsoup.connect(url).get();

                    String entryName = getEntryName(document);
                    String startLocationName = getStartLocationName(document);
                    String endLocationName = getEndLocationName(document);
                    String time = getTime(document);
                    String difficulty = getDifficulty(document);
                    String altitudeDifferance = getAltitudeDifferance(document);
                    String roadAltitudeDifferance = getRoadAltitudeDifferance(document);
                    String staringPointDirections = getStaringPointDirections(document);
                    String routeDirections = getRouteDirections(document);

                    Elements imageURLs = document.select(".main > div.main2 > div:nth-child(10) a");

                    // Data on the webpage is random and hard to pinpoint
                    if (imageURLs.size() == 0) {
                        imageURLs = document.select(".main > div.main2 > div:nth-child(11) a");
                    }
                    if (imageURLs.size() == 0) {
                        imageURLs = document.select(".main > div.main2 > div:nth-child(8) a");
                    }

                    AddData(entryName, startLocationName, endLocationName, time, difficulty, altitudeDifferance, roadAltitudeDifferance, staringPointDirections, routeDirections, imageURLs);
                    mainActivity.populateListView();
                    modifyButton(view, true, R.string.add_entry_button_text, activity);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
    }

    private void modifyButton(View view, boolean b, int p, MainActivity activity) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                button.setEnabled(b);
                button.setText(view.getResources().getString(p));
            }
        });
    }


    private String getEntryName(Document document) throws IOException {
        String entryName = document.select(".naslov1 h1").text();
        Log.d("success", "getEntryName: " + entryName);
        return entryName;
    }

    private String getStartLocationName(Document document) {
        String startLocationName = document.select(".gorasiv > div:nth-child(1) > div:nth-child(1)").text();
        Log.d("success", "getStartLocationName: " + startLocationName);
        return startLocationName;
    }

    private String getEndLocationName(Document document) {
        String startLocationName = document.select(".gorasiv > div:nth-child(1) > div:nth-child(4)").text();
        Log.d("success", "getEndLocationName: " + startLocationName);
        return startLocationName;
    }

    private String getTime(Document document) {
        String startLocationName = document.select(".gorasiv > div:nth-child(1) > div:nth-child(6)").text();
        Log.d("success", "getTime: " + startLocationName);
        return startLocationName;
    }

    private String getDifficulty(Document document) {
        String startLocationName = document.select(".gorasiv > div:nth-child(1) > div:nth-child(7)").text();
        Log.d("success", "getDifficulty: " + startLocationName);
        return startLocationName;
    }

    private String getAltitudeDifferance(Document document) {
        String startLocationName = document.select(".gorasiv > div:nth-child(1) > div:nth-child(9)").text();
        Log.d("success", "getAltitudeDifferance: " + startLocationName);
        return startLocationName;
    }

    private String getRoadAltitudeDifferance(Document document) {
        String startLocationName = document.select(".gorasiv > div:nth-child(1) > div:nth-child(10)").text();
        Log.d("success", "getRoadAltitudeDifferance: " + startLocationName);
        return startLocationName;
    }

    private String getStaringPointDirections(Document document) {
        String startLocationName = document.select(".main > div.main2 > div:nth-child(4)").text();
        Log.d("success", "getStaringPointDirections: " + startLocationName);
        return startLocationName;
    }

    private String getRouteDirections(Document document) {
        String startLocationName = document.select(".main > div.main2 > div:nth-child(5)").text();
        Log.d("success", "getRouteDirections: " + startLocationName);
        return startLocationName;
    }

    private void downloadImageAndDescription(Document document, int ID) {
        String strImageURL = document.select("#slikaslika").attr("src");
        String description = document.select("#slikaspodaj > div:nth-child(1) > div:nth-child(1)").text();
        byte[] image = downloadImage(strImageURL);
        entryImageDatabaseHelper.addData(ID, image, description);
    }

    private byte[] downloadImage(String strImageURL) {
        URL url = null;
        try {
            url = new URL("https:" + strImageURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream stream = url.openStream()) {
            byte[] buffer = new byte[4096];

            while (true) {
                int bytesRead = stream.read(buffer);
                if (bytesRead < 0) {
                    break;
                }
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return output.toByteArray();
    }

    public void AddData(String entryName,
                        String startLocationName,
                        String endLocationName,
                        String time,
                        String difficulty,
                        String altitudeDifferance,
                        String roadAltitudeDifferance,
                        String staringPointDirections,
                        String routeDirections,
                        Elements imageURLs) throws IOException {
        DataEntryResult result = entryDatabaseHelper.addData(entryName, startLocationName, endLocationName, time, difficulty, altitudeDifferance, roadAltitudeDifferance, staringPointDirections, routeDirections);

        if (result.success) {
            for (Element urlElement : imageURLs) {
                String imageUrl = urlElement.attr("href");
                Document imageDocument = Jsoup.connect(HRIBI + imageUrl).get();
                downloadImageAndDescription(imageDocument, result.id);
            }

            mainActivity.populateListView();

        } else {
            Log.d(TAG, "Add data failed");
        }
    }
}
