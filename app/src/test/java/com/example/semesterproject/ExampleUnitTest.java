package com.example.semesterproject;

import org.junit.Test;

import java.lang.reflect.Field;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    //Just testing communication between functions
    @Test
    public void test_Login_verifyUser() {
        Login testLogin = new Login();

        assertEquals(true, testLogin.verifyUser("John", "password"));
    }

    @Test
    public void test_Model_getUser() {
        //add some users to the database

        //call the function that will get users

        //create asserts to see if the result matches the users that we added on the database
    }


}