package com.example.semesterproject;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class ResultsFromMapQuest {
    List<LocationFromResults> results;

    ResultsFromMapQuest(){
        results = new ArrayList<>();
    }

    protected LatLng getLatLng(){
        return results.get(0).get(0).getLatLng();
    }

    protected String getAddress(){
        return results.get(0).get(0).getAddress();
    }

    protected void display(){
        System.out.println(results);
    }

    protected LocationFromResults get(int index){
        return results.get(index);
    }
}
