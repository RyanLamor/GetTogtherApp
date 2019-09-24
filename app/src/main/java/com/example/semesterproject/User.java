package com.example.semesterproject;

import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashMap;
import java.util.Map;

/**
 * This class holds an User and all its information to be stored in the database
 */
public class User {
    private String name;
    private String password;
    private String email;
    private Long id;
    private Map<Long, Event> hosting;
    private Map<Long, Event> maybe;
    private Map<Long, Event> going;
    private boolean loggedIn;

    /**
     * default constructor to create a null user
     */
    User () {
        hosting = new HashMap<>();
        maybe = new HashMap<>();
        going = new HashMap<>();
        this.name = null;
        this.password = null;
        this.email = null;

        id = MainActivity.getUserID();
    }

    /**
     * Non-default constructor that accepts the three following parameters:
     * @param name - holds the username of the User
     * @param pass - holds the password of the User
     * @param email - holds the email of the User
     */
    User(String name, String pass, String email){
        hosting = new HashMap<>();
        maybe = new HashMap<>();
        going = new HashMap<>();
        this.name = name;
        this.password = pass;
        this.email = email;

        id = MainActivity.getUserID();
    }

    /**
     * A non-default constructor that takes the four following parameters:
     * @param id - a unique number to identify the user by
     * @param name - holds the username of the User
     * @param pass - holds the password of the User
     * @param email - holds the email of the User
     */
    User(Long id, String name, String pass, String email){
        hosting = new HashMap<>();
        maybe = new HashMap<>();
        going = new HashMap<>();
        this.name = name;
        this.password = pass;
        this.id = id;
        this.email = email;
    }

    /**
     * a method to verify if the user is logged in or not
     * @return - a boolean value for loggedIn
     */
    protected boolean isLoggedIn() { return this.loggedIn; }

    /**
     * A setter for the variable loggedIn, which is a boolean
     * @param aLoggedIn - a boolean value to be used to set the User variable
     */
    protected void setLoggedIn(boolean aLoggedIn) { this.loggedIn = aLoggedIn; }

    /**
     * A getter for the name variable
     * @return a String
     */
    protected String getName() { return name; }

    /**
     * Set the variable name to what is passed as parameter
     * @param aName a String that will be the new name
     */
    protected void setName(String aName) { name = aName; }

    /**
     * A getter for the password variable
     * @return a String
     */
    protected String getPassword() { return password; }

    /**
     * A setter for the password variable
     * @param aPassword a String that will be the new password
     */
    protected void setPassword(String aPassword) { password = aPassword; }

    /**
     * a getter for the email variable
     * @return - a string, which is the email
     */
    protected String getEmail() { return email; }

    protected void setEmail(String newEmail) {email = newEmail; }

    /**
     * a getter for the id variable
     * @return - a long, which is the id  variable
     */
    protected Long getID(){ return id; }

    /**
     * takes in a new Event info from textView boxes and creates a new event object with all the info
     * and adds that event to the user's hosting map
     * does not take any parameters because it grabs data from text boxes
     * @return - returns a boolean to verify if the user did input all the information necessary to create an Event
     */
    protected boolean createEvent(){ // Does not take any parameters because it grabs data from text boxes.

        EditText eventName = (EditText) MainActivity.getMainActivityRef().get().findViewById(R.id.EventName);
        Spinner eventCategory = (Spinner) MainActivity.getMainActivityRef().get().findViewById(R.id.Category);
        EditText eventDate = (EditText) MainActivity.getMainActivityRef().get().findViewById(R.id.Date);
        EditText eventDescription = (EditText) MainActivity.getMainActivityRef().get().findViewById(R.id.Description);
        EditText eventLocation = (EditText) MainActivity.getMainActivityRef().get().findViewById(R.id.Location);

        String name = eventName.getText().toString();
        Enum category = (Enum)eventCategory.getSelectedItem();
        String date = eventDate.getText().toString();
        String location = eventLocation.getText().toString();
        Map<Long, User> going = new HashMap<>();
        Map<Long, User> maybe = new HashMap<>();
        String description = eventDescription.getText().toString();

        CharSequence error = "Error: One of the fields is empty!";
        if(name.length() <= 0 || date.length() <= 0 || location.length() <= 0 || description.length() <= 0){
            MainActivity.getMainActivityRef().get().errorToast(error);
            return false;
        }

        GetLatLngFromAddress getLatLng = new GetLatLngFromAddress(location);

        Thread thread = new Thread(getLatLng);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LatLng address = getLatLng.getLatLng();

        // Category is temporary until we can figure out how to make the user select options rather than enter text.
        Event newEvent = new Event(name, Category.valueOf(category.toString()), date, location, this, going, maybe, description);//(name, category, date, location, creator, going, maybe, description);
        hosting.put(newEvent.getID(), newEvent);

//        Event newEvent2 = new Event(name, category, date, location, creator, going, maybe, description);
//        hosting.put(newEvent2.getID(), newEvent2);

        // Add a MARKER to the map for the event.
        Marker mEvent = MainActivity.getMainActivityRef().get().getgMap().addMarker(new MarkerOptions().position(address).title(name));
        mEvent.setTag(newEvent);

        MainActivity.addEvent(newEvent);
//        MainActivity.addEvent(newEvent2);

        return true;
    }

    /**
     * Creats an event when the user clicks on the map and adds information rather than the 'add' button
     * @param aPoint
     * @return
     */
    protected boolean createEvent2(LatLng aPoint){ // Does not take any parameters because it grabs data from text boxes.

        EditText eventName = (EditText) MainActivity.getMainActivityRef().get().findViewById(R.id.EventName);
        Spinner eventCategory = (Spinner) MainActivity.getMainActivityRef().get().findViewById(R.id.Category);
        EditText eventDate = (EditText) MainActivity.getMainActivityRef().get().findViewById(R.id.Date);
        EditText eventDescription = (EditText) MainActivity.getMainActivityRef().get().findViewById(R.id.Description);

        String name = eventName.getText().toString();
        Enum category = (Enum)eventCategory.getSelectedItem();
        String date = eventDate.getText().toString();
        Map<Long, User> going = new HashMap<>();
        Map<Long, User> maybe = new HashMap<>();
        String description = eventDescription.getText().toString();


        CharSequence error = "Error: One of the fields is empty!";
        if(name.length() <= 0 || date.length() <= 0 || description.length() <= 0){
            MainActivity.getMainActivityRef().get().errorToast(error);
            return false;
        }

        GetAddressfromLatLng getAddress = new GetAddressfromLatLng(aPoint);

        Thread thread = new Thread(getAddress);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String strAddress = getAddress.getStrAddress();

        // Category is temporary until we can figure out how to make the user select options rather than enter text.
        Event newEvent = new Event(name, Category.valueOf(category.toString()), date, strAddress, this, going, maybe, description);//(name, category, date, location, creator, going, maybe, description);
        hosting.put(newEvent.getID(), newEvent);

//        Event newEvent2 = new Event(name, category, date, location, creator, going, maybe, description);
//        hosting.put(newEvent2.getID(), newEvent2);

        // Add a MARKER to the map for the event.
        Marker mEvent = MainActivity.getMainActivityRef().get().getgMap().addMarker(new MarkerOptions().position(aPoint).title(name));
        mEvent.setTag(newEvent);

        MainActivity.addEvent(newEvent);
//        MainActivity.addEvent(newEvent2);

        return true;
    }

    /*
     * For future coding, perhaps we could also override the toString() method for Event and User classes
     * to make it easier to write to files?
     */
    @Override
    public String toString(){
        return id + "\t" + name + "\t" + password + "\t" + going + "\t" + maybe + "\t" + hosting;
    }

    /**
     * overriding the equals() to check if the users have the same id
     * @param otherUser - the User that will be compared to the  main User
     * @return - a boolean value if it is the same User
     */
    @Override
    public boolean equals(Object otherUser){//I hope this was okay to add - Ryan
        User temp = (User) otherUser;
        if(this.id == temp.getID() )
            return true;
        else
            return false;
    }
}
