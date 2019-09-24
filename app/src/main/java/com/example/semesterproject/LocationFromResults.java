package com.example.semesterproject;

import java.util.ArrayList;
import java.util.List;

public class LocationFromResults {
    List<LatLngOrAddressFromLocation> locations;

    LocationFromResults(){
        locations = new ArrayList<>();
    }

    protected void display(){
        System.out.println(locations);
    }

    protected LatLngOrAddressFromLocation get(int index){
        return locations.get(index);
    }
}
