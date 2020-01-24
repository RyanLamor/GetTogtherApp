package com.example.semesterproject;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * The Model class stores data locally and works with the database in reading and writing data.
 * @authors Ryan Lamoreaux, Tyson Ashcraft, Rio Moura
 * @version 1.0
 */
public class Model {
    private long userID;// im thinking when a user is created they are assigned id = to the model id then the model id is incremented
    private long eventID;//just like the UserID, I think an event Id would be good too.
    private Map<Long, User> users;//holds all the created users <userID, User>
    private Map<Long, Event> events;//holds all the created events by every user <eventID, Event>

    private DatabaseReference mDatabase;
    private DatabaseReference userIdRef;
    private DatabaseReference eventIdRef;
    private DatabaseReference usersRef;
    private FirebaseDatabase firebaseInstance;


    public Model(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        firebaseInstance = FirebaseDatabase.getInstance();

        userIdRef = firebaseInstance.getReference("CurrentUserID");
        userIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userID = dataSnapshot.getValue(Long.class);
                System.out.println("Current User ID From Database: " + userID);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        System.out.println(userID);
        //userID = 1;

        eventIdRef = firebaseInstance.getReference("CurrentEventID");
        eventIdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                eventID = dataSnapshot.getValue(Long.class);
                System.out.println("Current Event ID From Database: " + eventID);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        System.out.println(eventID);
        //eventID = 1;

        usersRef = firebaseInstance.getReference("Users");
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                User newUser = dataSnapshot.getValue(User.class);
//                System.out.println("Current Users From Database: " + newUser.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String prevChildKey) {}

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        users = new HashMap<>();
        events = new HashMap<>();
    }

    /**
     * @return a list of users saved in the database
     */
    public Map<Long, User> getUsers() {
        return users;
    }

    /**
     * @return a list of events saved in the database
     */
    public Map<Long, Event> getEvents() {
        return events;
    }

    /**
     * Loads the users from the database and stores them in a map data structure
     * @return A map of users
     */
    protected Map loadUsers(){
        //read from file
        Map<Long, User> list = new HashMap<>();

        try {
            FileReader fileReader = new FileReader("savedUsers.txt");
            BufferedReader in = new BufferedReader(fileReader);
            Gson gson = new Gson();

            String currentLine = in.readLine();
            while(currentLine != null)
            {
                String strUser = currentLine;
                User newUser = gson.fromJson(strUser, User.class);
                list.put(newUser.getID(), newUser);
                currentLine = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        users = list;
        return list;
    }

    /**
     * Loads the events from the database and stores them in a map data structure
     * @return A map of events
     */
    protected Map loadEvents(){
        //read from file
        Map<Long, Event> list = new HashMap<>();
        try {
            FileReader fileReader = new FileReader("savedEvents.txt");
            BufferedReader in = new BufferedReader(fileReader);
            Gson gson = new Gson();

            String currentLine = in.readLine();
            while(currentLine != null)
            {
                String strEvent = currentLine;
                Event newEvent = gson.fromJson(strEvent, Event.class);
                list.put(newEvent.getID(), newEvent);
                currentLine = in.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        events = list;
        return list;
    }

    /**
     * Takes a user and adds it to the database and locally stored variable
     * @param user
     */
    protected void addUser(User user){//add user to users map, called whenever a new user is created, should call save after adding;
        users.put(user.getID(), user);
        saveUser(user);
        writeNewUserToDatabase(user);
    }

    /**
     * Takes a event and adds it to the database and locally stored variable
     * @param event
     */
    protected void addEvent(Event event){//add event to events map,  called whenever a new event is created, should call save after adding
        events.put(event.getID(), event);
        saveEvent(event);
    }

    /**
     * Saves a user passed in by the appliation to the database. Called when a new User is created or added.
     * @param user
     */
    private void saveUser(User user){
        //write to file
        try {
        FileWriter fileWriter = new FileWriter("savedUsers.txt",true);
        BufferedWriter out = new BufferedWriter(fileWriter);

        Gson gson = new Gson();
        String strUser = gson.toJson(user);
        out.write(strUser.toString());
        out.newLine();
        out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves a event passed in by the appliation to the database. Called when a new event is created or added.
     * @param event
     */
    private void saveEvent(Event event){
        //write to file
        try {
            FileWriter fileWriter = new FileWriter("savedEvents.txt",true);
            BufferedWriter out = new BufferedWriter(fileWriter);

            Gson gson = new Gson();
            String strEvent = gson.toJson(event);
            out.write(strEvent.toString());
            out.newLine();
            out.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected boolean checkUserEmails(String emailToCheck){
        boolean hasEmail = false;
        for (Map.Entry<Long, User> entry : users.entrySet()) {
            if(emailToCheck.equals( entry.getValue().getEmail() ) ){
               hasEmail = true;
            }
        }
        return hasEmail;
    }

    /**
     * Called when a new user is created. Assisgns said user the current ID so each user will have a unique id number
     * @return userID
     */
    protected long getUserID(){
        return userID;
    }

    /**
     * userID is incremented by 1. Called after a new user is created so that a new unique id is generated.
     */
    protected void updateUserID(){//
        userID++;
    }

    /**
     * Called when a new event is created. Assisgns said event the current ID so each event will have a unique id number
     * @return eventID
     */
    protected long getEventID(){
        return eventID;
    }

    /**
     * eventID is incremented by 1. Called after a new user is created so that a new unique id is generated.
     */
    protected void updateEventID(){
        eventID++;
    }

    /**
     * Writes a newly created user to Google's database
     * @param user
     */
    private void writeNewUserToDatabase(User user) {
        mDatabase.child("Users").child(user.getName()).setValue(user)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                MainActivity.getMainActivityRef().get().errorToast("Added User to Database");
            }
        })
            .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                MainActivity.getMainActivityRef().get().errorToast("Failed to add User to Database");
            }
        });
    }

    protected User checkIfUserExists(String name, String password){
        User userFromDatabase = new User();

        return userFromDatabase;
    }

    /**
     * Writes the current event and user ID's to the database
     */
    protected void saveCurrentIDs()
    {
        mDatabase.child("CurrentUserID").setValue(userID);
        mDatabase.child("CurrentEventID").setValue(eventID);
    }
}
