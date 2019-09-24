package com.example.semesterproject;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GetAddressfromLatLng implements Runnable {
    private LatLng latLng;
    private String strAddress;

    GetAddressfromLatLng(LatLng latLng) {
        this.latLng = latLng;
        strAddress = new String();
    }

    protected LatLng getLatLng() {
        return latLng;
    }

    protected String getStrAddress(){
        return strAddress;
    }

    @Override
    public void run() {
        try {
            Scanner strScan = new Scanner(strAddress);
            List<String> strList = new ArrayList<>();

            String url = "http://www.mapquestapi.com/geocoding/v1/reverse?key=VaKnMuCEwBCEMrAhva7VB6KdH6taKgsm&location=";
            String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()

            String strLat = String.format("%f,", latLng.latitude);
            String strLng = String.format("%f", latLng.longitude);

            url += strLat + strLng;

            System.out.println(url);

            //Example: http://www.mapquestapi.com/geocoding/v1/reverse?key=KEY&location=30.333472,-81.470448

            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            InputStream response = connection.getInputStream();


            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(response, "UTF-8"));
            ResultsFromMapQuest results = gson.fromJson(reader, ResultsFromMapQuest.class);

            strAddress = results.getAddress();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
