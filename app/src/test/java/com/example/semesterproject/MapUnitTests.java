package com.example.semesterproject;
import com.google.android.gms.maps.model.LatLng;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;

public class MapUnitTests {

    @Test
    public void test_getLocationFromAddress() {
        MainActivity main = new MainActivity();
        try {
           LatLng temp = main.getLocationFromAddress("269 S 5th W, Rexburg, ID, 83440");
           System.out.println(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
