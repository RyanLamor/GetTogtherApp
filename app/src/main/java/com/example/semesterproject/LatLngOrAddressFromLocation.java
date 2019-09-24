package com.example.semesterproject;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

public class LatLngOrAddressFromLocation {
    Map<String, Float> displayLatLng; //latLng
    String street; // street address
    String adminArea5; // city name
    String adminArea3; // state name

    LatLngOrAddressFromLocation(){
        displayLatLng = new HashMap<>();
    }

    protected LatLng getLatLng(){
        return new LatLng(displayLatLng.get("lat"), displayLatLng.get("lng"));
    }

    protected String getAddress(){
        return new String(street + ", " + adminArea5 + ", " + adminArea3);
    }

    protected void display(){
        System.out.println(displayLatLng);
    }
}
