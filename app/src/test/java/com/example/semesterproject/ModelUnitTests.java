package com.example.semesterproject;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class ModelUnitTests {

    /***********************************************************************************************
     * Unit Tests for the Model Class
     **********************************************************************************************/
    @Test
    public void test_Model_Constructor() throws NoSuchFieldException, IllegalAccessException {
        Model m = new Model();

        Field userID = m.getClass().getDeclaredField("userID");
        userID.setAccessible(true);
        Field eventID = m.getClass().getDeclaredField("eventID");
        eventID.setAccessible(true);
        Field users = m.getClass().getDeclaredField("users");
        users.setAccessible(true);
        Field events = m.getClass().getDeclaredField("events");
        events.setAccessible(true);

        Long tempUID = new Long(1);
        Long tempEID = new Long(1);
        Map<Long, User> temp1 = new HashMap<>();
        Map<Long, Event> temp2 = new HashMap<>();

        assertEquals(tempUID, userID.get(m));
        assertEquals(tempEID, eventID.get(m));
        assertTrue(users.get(m).equals(temp1));
        assertTrue(events.get(m).equals(temp2));
    }

    @Test
    public void test_Model_loadUsers() {
        //add some users to the "database"
        File file = new File("testUsers.txt");
        if (file.exists())
            file.delete();

        try {
            FileWriter fileWriter = new FileWriter("testUsers.txt");
            BufferedWriter out = new BufferedWriter(fileWriter);

            User u1 = new User("Ryan L", "r123", "email@gmail.com");
            User u2 = new User("Tyson A", "t123", "email@gmail.com");
            User u3 = new User("Rio M", "r123", "email@gmail.com");

            Gson gson = new Gson();

            String user1 = gson.toJson(u1);
            String user2 = gson.toJson(u2);
            String user3 = gson.toJson(u3);

            out.write(user1.toString());
            out.newLine();
            out.write(user2.toString());
            out.newLine();
            out.write(user3.toString());
            out.close();


        //call the function that will get users
        Model m = new Model();
        Map<Long, User> map = m.loadUsers();

        //create asserts to see if the result matches the users that we added on the database
        assertEquals(u1, map.get(new Long(1)));
        assertEquals(u2, map.get(new Long(2)));
        assertEquals(u3, map.get(new Long(3)));
        }
        catch(IOException e){
            System.out.print(e.getMessage());
        }
    }

    @Test
    public void test_Model_loadEvents()throws IOException{
        //add some events to the database
        File file = new File("testEvents.txt");
        if (file.exists())
            file.delete();

        FileWriter fileWriter = new FileWriter("testEvents.txt");
        BufferedWriter out = new BufferedWriter(fileWriter);

        User creator = new User("Ryan", "r123", "email@gmail.com");//id = 1

        User going1 = new User("g1", "pass", "email@gmail.com");//id = 2
        User going2 = new User("g2" , "pass", "email@gmail.com");//id = 3
        Map<Long, User> usersGoing = new HashMap<>();
        usersGoing.put(new Long(1), going1);
        usersGoing.put(new Long(2), going2);

        User maybe1 = new User("m1", "pass", "email@gmail.com");//id = 4
        User maybe2 = new User("m2", "pass", "email@gmail.com");//id = 5
        Map<Long, User> usersMaybe = new HashMap<>();
        usersMaybe.put(new Long(4), maybe1);
        usersMaybe.put(new Long(5), maybe2);

        GetLatLngFromAddress getLatLng = new GetLatLngFromAddress("269 S 5th W, Rexburg, ID, 83440");

        Thread thread = new Thread(getLatLng);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LatLng location = getLatLng.getLatLng();

        Event e1 = new Event("Event 1", Category.Other, "07-29-19", "269 S 5th W, Rexburg, ID, 83440", creator, usersGoing, usersMaybe, "Swimming");//id = 1
        Event e2 = new Event("Event 2", Category.Sports, "07-29-19", "269 S 5th W, Rexburg, ID, 83440", creator, usersGoing, usersMaybe, "Basketball");//id = 2
        Event e3 = new Event("Event 3", Category.Games, "07-29-19", "269 S 5th W, Rexburg, ID, 83440", creator, usersGoing, usersMaybe, "PS4");//id = 3

        Gson gson = new Gson();
        String strEvent1 = gson.toJson(e1);
        String strEvent2 = gson.toJson(e2);
        String strEvent3 = gson.toJson(e3);

        out.write(strEvent1.toString());
        out.newLine();
        out.write(strEvent2.toString());
        out.newLine();
        out.write(strEvent3.toString());
        out.close();

        //call the function that will load Events
        Model m = new Model();
        Map<Long, Event> list = m.loadEvents();

        //create asserts to see if the result matches the users that we added on the database
        assertEquals(e1, list.get(new Long(1)));
        assertEquals(e2, list.get(new Long(2)));
        assertEquals(e3, list.get(new Long(3)));
    }

    @Test
    public void test_Model_addUser() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        User u1 = new User(new Long(1),"Ryan L", "r123", "email@gmail.com");
        User u2 = new User(new Long(2), "Tyson A", "t123", "email@gmail.com");
        User u3 = new User(new Long(3), "Rio M", "r123", "email@gmail.com");
        Map<Long, User> map = new HashMap<>();
        Long tempId = new Long(1);
        map.put(tempId, u1);
        map.put(tempId+=1, u2);
        map.put(tempId+=1, u3);

        Model testModel = new Model();
        Class myClass = testModel.getClass();

        Method addUser = myClass.getDeclaredMethod("addUser", User.class);
        addUser.invoke(testModel, u1);
        addUser.invoke(testModel, u2);
        addUser.invoke(testModel, u3);

        Field users = myClass.getDeclaredField("users");
        users.setAccessible(true);

        assertEquals(map, users.get(testModel));
    }

    @Test
    public void test_Model_addEvent() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        User creator = new User("Ryan", "r123", "email@gmail.com");//id = 1

        User going1 = new User("g1", "pass", "email@gmail.com");//id = 2
        User going2 = new User("g2" , "pass", "email@gmail.com");//id = 3
        Map<Long, User> usersGoing = new HashMap<>();
        usersGoing.put(new Long(1), going1);
        usersGoing.put(new Long(2), going2);

        User maybe1 = new User("m1", "pass", "email@gmail.com");//id = 4
        User maybe2 = new User("m2", "pass", "email@gmail.com");//id = 5
        Map<Long, User> usersMaybe = new HashMap<>();
        usersMaybe.put(new Long(4), maybe1);
        usersMaybe.put(new Long(5), maybe2);

        GetLatLngFromAddress getLatLng = new GetLatLngFromAddress("269 S 5th W, Rexburg, ID, 83440");

        Thread thread = new Thread(getLatLng);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LatLng location = getLatLng.getLatLng();
        
        Event e1 = new Event(new Long(1), "Event 1", Category.Other, "07-29-19", "269 S 5th W, Rexburg, ID, 83440", creator, usersGoing, usersMaybe, "Swimming");//id = 1
        Event e2 = new Event(new Long(2), "Event 2", Category.Sports, "07-29-19", "269 S 5th W, Rexburg, ID, 83440", creator, usersGoing, usersMaybe, "Basketball");//id = 2
        Event e3 = new Event(new Long(3), "Event 3", Category.Games, "07-29-19", "269 S 5th W, Rexburg, ID, 83440", creator, usersGoing, usersMaybe, "PS4");//id = 3

        Map<Long, Event> map = new HashMap<>();
        Long tempId = new Long(1);
        map.put(tempId, e1);
        map.put(tempId+=1, e2);
        map.put(tempId+=1, e3);

        Model testModel = new Model();
        Class myClass = testModel.getClass();

        Method addEvent = myClass.getDeclaredMethod("addEvent", Event.class);
        addEvent.invoke(testModel, e1);
        addEvent.invoke(testModel, e2);
        addEvent.invoke(testModel, e3);

        Field events = myClass.getDeclaredField("events");
        events.setAccessible(true);

        assertEquals(map, events.get(testModel));
    }

    @Test
    public void test_Model_saveUsers() throws NoSuchFieldException, IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        File file = new File("savedUsers.txt");
        if (file.exists())
            file.delete();

        User u1 = new User("Ryan", "r123", "email@gmail.com");
        User u2 = new User("Tyson", "t123", "email@gmail.com");
        User u3 = new User("Rio", "r123", "email@gmail.com");

        Model m = new Model();

        Method saveUser = m.getClass().getDeclaredMethod("saveUser", User.class);
        saveUser.setAccessible(true);
        saveUser.invoke(m, u1);
        saveUser.invoke(m, u2);
        saveUser.invoke(m, u3);

        //read from file that saveUser wrote to
        FileReader fileReader = new FileReader("savedUsers.txt");
        BufferedReader in = new BufferedReader(fileReader);

        String tmpUser1 = in.readLine();
        String tmpUser2 = in.readLine();
        String tmpUser3 = in.readLine();

        Gson gson = new Gson();

        User newUser1 = gson.fromJson(tmpUser1, User.class);
        User newUser2 = gson.fromJson(tmpUser2, User.class);
        User newUser3 = gson.fromJson(tmpUser3, User.class);

        assertEquals(u1, newUser1);//Need to change the Equals method for events and users
        assertEquals(u2, newUser2);
        assertEquals(u3, newUser3);
    }

    @Test
    public void test_Model_saveEvents() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        File file = new File("savedEvents.txt");
        if (file.exists())
            file.delete();
        User creator = new User("Ryan", "r123", "email@gmail.com");//id = 1

        User going1 = new User("g1", "pass", "email@gmail.com");//id = 2
        User going2 = new User("g2" , "pass", "email@gmail.com");//id = 3
        Map<Long, User> usersGoing = new HashMap<>();
        usersGoing.put(new Long(1), going1);
        usersGoing.put(new Long(2), going2);

        User maybe1 = new User("m1", "pass", "email@gmail.com");//id = 4
        User maybe2 = new User("m2", "pass", "email@gmail.com");//id = 5
        Map<Long, User> usersMaybe = new HashMap<>();
        usersMaybe.put(new Long(4), maybe1);
        usersMaybe.put(new Long(5), maybe2);

        GetLatLngFromAddress getLatLng = new GetLatLngFromAddress("269 S 5th W, Rexburg, ID, 83440");

        Thread thread = new Thread(getLatLng);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LatLng location = getLatLng.getLatLng();

        Event e1 = new Event("Event 1", Category.Other, "07-29-19", "269 S 5th W, Rexburg, ID, 83440", creator, usersGoing, usersMaybe, "Swimming");//id = 1
        Event e2 = new Event("Event 2", Category.Sports, "07-29-19", "269 S 5th W, Rexburg, ID, 83440", creator, usersGoing, usersMaybe, "Basketball");//id = 2
        Event e3 = new Event("Event 3", Category.Games, "07-29-19", "269 S 5th W, Rexburg, ID, 83440", creator, usersGoing, usersMaybe, "PS4");//id = 3

        Model m = new Model();

        Method saveEvent = m.getClass().getDeclaredMethod("saveEvent", Event.class);
        saveEvent.setAccessible(true);
        saveEvent.invoke(m, e1);
        saveEvent.invoke(m, e2);
        saveEvent.invoke(m, e3);

        //read from file that saveEvent wrote to
        FileReader fileReader = new FileReader("savedEvents.txt");
        BufferedReader in = new BufferedReader(fileReader);

        String tmpEvent1 = in.readLine();
        String tmpEvent2 = in.readLine();
        String tmpEvent3 = in.readLine();

        Gson gson = new Gson();

        Event newEvent1 = gson.fromJson(tmpEvent1, Event.class);
        Event newEvent2 = gson.fromJson(tmpEvent2, Event.class);
        Event newEvent3 = gson.fromJson(tmpEvent3, Event.class);

        assertEquals(e1, newEvent1);//Need to change the Equals method for events and Events
        assertEquals(e2, newEvent2);
        assertEquals(e3, newEvent3);
    }

    @Test
    public void test_Model_getUserID(){
        Model m = new Model();
        assert(m.getUserID() > 0);
        assert(m.getUserID() < Long.MAX_VALUE);
    }

    @Test
    public void test_Model_updateUserID(){
        Model m = new Model();
        assert(m.getUserID() > 0);

        long temp = m.getUserID() + 1;
        m.updateUserID();

        assertEquals(temp, m.getUserID());
    }

    @Test
    public void test_Model_getEventID(){
        Model m = new Model();
        assert(m.getEventID() > 0);
        assert(m.getEventID() < Long.MAX_VALUE);
    }

    @Test
    public void test_Model_updateEventID(){
        Model m = new Model();
        assert(m.getEventID() > 0);

        long temp = m.getEventID() + 1;
        m.updateEventID();

        assertEquals(temp, m.getEventID());
    }
}
