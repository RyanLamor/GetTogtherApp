package com.example.semesterproject;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GetLatLngFromAddress implements Runnable{
    private LatLng latLng;
    private String strAddress;

    GetLatLngFromAddress(String address){
        latLng = new LatLng(0,0);
        strAddress = address;
    }

    protected LatLng getLatLng(){
        return latLng;
    }

    @Override
    public void run() {
        try {
            Scanner strScan = new Scanner(strAddress);
            List<String> strList = new ArrayList<>();

            String url = "http://www.mapquestapi.com/geocoding/v1/address?key=VaKnMuCEwBCEMrAhva7VB6KdH6taKgsm&location=";
            String charset = "UTF-8";  // Or in Java 7 and later, use the constant: java.nio.charset.StandardCharsets.UTF_8.name()

            while (strScan.hasNext()) {
                String strTemp = strScan.next();
                String param;
                if (strTemp.contains(",")) {
                    param = URLEncoder.encode(strTemp, charset);
                } else {
                    param = String.format("%s+", URLEncoder.encode(strTemp, charset));
                }

                url += param;
            }

            url = url.substring(0, url.length() - 1);
            System.out.println(url);

            //Example: http://www.mapquestapi.com/geocoding/v1/address?key=KEY&location=1600+Pennsylvania+Ave+NW,Washington,DC,20500

            URLConnection connection = new URL(url).openConnection();
            connection.setRequestProperty("Accept-Charset", charset);
            InputStream response = connection.getInputStream();


            Gson gson = new Gson();
            JsonReader reader = new JsonReader(new InputStreamReader(response, "UTF-8"));
            ResultsFromMapQuest results = gson.fromJson(reader, ResultsFromMapQuest.class);

            latLng = results.getLatLng();

        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
