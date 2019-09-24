package com.example.semesterproject;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    static private Model model = new Model();
    static private WeakReference<MainActivity> mainActivity;
    private GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationClient;
    private ConstraintLayout CreateEventPopUp;
    private ConstraintLayout loginPopUp;
    private ConstraintLayout createNewUserPopUp;
    private ConstraintLayout eventInfoScreen;
    private Boolean clickedOnMap;
    private LatLng pointClicked;
    private Event selectedEvent;


    // initialize a empty User object to use along the functions
    private User rootUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);//Sets up Google Map

        mainActivity = new WeakReference<>(this);

        model.loadEvents();
        model.loadUsers();
        populateMap();

        CreateEventPopUp = (ConstraintLayout) findViewById(R.id.CreateEventPopUp);
        CreateEventPopUp.setVisibility(View.GONE);

        loginPopUp = (ConstraintLayout) findViewById(R.id.loginPopUp);
        loginPopUp.setVisibility(View.GONE);

        createNewUserPopUp = (ConstraintLayout) findViewById(R.id.createNewUser);
        createNewUserPopUp.setVisibility(View.GONE);

        eventInfoScreen = (ConstraintLayout) findViewById(R.id.EventInfoScreen);
        eventInfoScreen.setVisibility(View.GONE);

        Spinner mySpinner = (Spinner) findViewById(R.id.Category);
        mySpinner.setAdapter(new ArrayAdapter<Category>(this, android.R.layout.simple_spinner_item, Category.values()));

        selectedEvent = null;

        rootUser = new User();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        model.saveCurrentIDs();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMarkerClickListener(this);

        //Adding event on click on the map
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                clickedOnMap = true;
                pointClicked = point;

                EditText location = findViewById(R.id.Location);
                location.setVisibility(View.GONE);

                //Check if the user is logged in or not
                if(!rootUser.isLoggedIn()) {
                    loginPopUp.setVisibility(View.VISIBLE);
                }
                //if the user is logged in, opens the createEventPopUp page
                else {
                    CreateEventPopUp.setVisibility(View.VISIBLE);
                }

                ImageView btn = findViewById(R.id.NewEvent);
                btn.setVisibility(View.GONE);

            }
        });

        // Test maker at latitude and longitude (0, 0).
        gMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("New Marker"));

        /*
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        final LatLng[] myLocation = new LatLng[1];
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            myLocation[0] = new LatLng(location.getLatitude(), location.getLongitude());
                        }
                    }
                });

        System.out.println(myLocation[0]);
        */

        LatLng BYUI = new LatLng(43.814666, -111.784402);
        gMap.addMarker(new MarkerOptions().position(BYUI).title("STC"));

        //testing GetLatLngFromAddress

        GetLatLngFromAddress getLatLng = new GetLatLngFromAddress("269 S 5th W, Rexburg, ID, 83440");

        Thread thread = new Thread(getLatLng);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        LatLng Ryan = getLatLng.getLatLng();


        //Testing Marker info (Just an Example, will be deleted on completion of project)
        Event testEvent = new Event("Event 1", Category.Other, "06-30-19", "269 S 5th W, Rexburg, ID, 83440", new User((long) 0, "Ryan", "r123", "Ry@gmail"), new HashMap<>(), new HashMap<>(), "Swimming");
        Marker mEvent = gMap.addMarker(new MarkerOptions().position(Ryan).title("Ryan's House"));
        mEvent.setTag(testEvent);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Ryan, 14));//zoom level can go up to 21, Higher zoom = closer

    }

    /**
     * This method takes a String and displays it to the user through a toast message
     * @param error
     */
    public void errorToast(CharSequence error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
        System.out.println(error);
    }

    public void goBack(View view){//goBack to main screen when on the event info screen
        eventInfoScreen.setVisibility(View.GONE);
        findViewById(R.id.NewEvent).setVisibility(View.VISIBLE);
        selectedEvent = null;
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if(marker.getTag() != null) {
            if(marker.getTag().getClass() == Event.class) {
                //Toast.makeText(this, marker.getTag().toString(), Toast.LENGTH_LONG).show();
                eventInfoScreen.setVisibility(View.VISIBLE);
                findViewById(R.id.NewEvent).setVisibility(View.GONE);

                TextView title = findViewById(R.id.Title_Info);
                TextView location = findViewById(R.id.Location_Info);
                TextView date = findViewById(R.id.Date_Info);
                TextView desc = findViewById(R.id.Description_Info);
                TextView category = findViewById(R.id.Category_Info);
                TextView going = findViewById(R.id.Going_Info);
                TextView maybe = findViewById(R.id.Maybe_Info);

                Event event = (Event) marker.getTag();
                title.setText(event.getName());
                location.setText("Location: " + event.getLocation().toString());
                date.setText("Date: " + event.getDate());
                category.setText("Category: " + event.getCategory().toString());
                going.setText("People Going: " + event.getGoing().size());
                maybe.setText("People Maybe Going: " + event.getMaybe().size());
                desc.setText(event.getDescription());

                selectedEvent = event;
            }
        }
        else
            Toast.makeText(this, "No Event Associated", Toast.LENGTH_SHORT).show();

        return false;
    }

    /**
     * This function records that the user is going to a certain event.
     * @param view enables the function to be seen by the xml file.
     */
    public void userGoing(View view){
        //Check if the user is logged in or not
        if(!rootUser.isLoggedIn()) {
            loginPopUp.setVisibility(View.VISIBLE);
        }
        else{
            if (selectedEvent == null){
                errorToast("No Event Selected");
                return;
            }
            else{
                if (rootUser == selectedEvent.getCreator()){
                    return;
                }
                else {
                    selectedEvent.addGoing(rootUser);
                    TextView going = findViewById(R.id.Going_Info);
                    going.setText("People Going: " + selectedEvent.getGoing().size());
                    if (selectedEvent.getMaybe().containsKey(rootUser.getID())) {
                        selectedEvent.getMaybe().remove((rootUser.getID()));
                    }
                    TextView maybe = findViewById(R.id.Maybe_Info);
                    maybe.setText("People Maybe Going: " + selectedEvent.getMaybe().size());
                }
            }
        }
    }

    /**
     * This function records that the user might go to a certain event.
     * @param view enables the function to be seen by the xml file.
     */
    public void userMaybe(View view){
        //Check if the user is logged in or not
        if(!rootUser.isLoggedIn()) {
            loginPopUp.setVisibility(View.VISIBLE);
        }
        else{
            if (selectedEvent == null){
                errorToast("No Event Selected");
                return;
            }
            else{
                if (rootUser == selectedEvent.getCreator()){
                    return;
                }
                else {
                    selectedEvent.addMaybe(rootUser);
                    TextView maybe = findViewById(R.id.Maybe_Info);
                    maybe.setText("People Maybe Going: " + selectedEvent.getMaybe().size());
                    if (selectedEvent.getGoing().containsKey(rootUser.getID())) {
                        selectedEvent.getGoing().remove((rootUser.getID()));
                    }
                    TextView going = findViewById(R.id.Going_Info);
                    going.setText("People Going: " + selectedEvent.getGoing().size());
                }
            }
        }
    }

    /**
    * button inside createEventPopUp
    * it will check if the user is logged in
    * if the user is not logged, in it will call the login() function
    * if the user is logged in, it will place the market on the map
      */
    public void createAnEvent(View view){
        //User testUser = new User("Kaden", "hideyokids", "myFavoriteEmail@gmail.com");
        boolean success;
        if (clickedOnMap != null) {
            success = rootUser.createEvent2(pointClicked);
        }
        else {
            success = rootUser.createEvent();
        }

        if(success == true){
            CreateEventPopUp.setVisibility(View.GONE);
            EditText eventName = findViewById(R.id.EventName);
            eventName.setText("");
            EditText location = findViewById(R.id.Location);
            location.setText("");
            EditText date = findViewById(R.id.Date);
            date.setText("");
            EditText description = findViewById(R.id.Description);
            description.setText("");
            ImageView btn = findViewById(R.id.NewEvent);
            btn.setVisibility(View.VISIBLE);
        }

        clickedOnMap = false;
        EditText location = findViewById(R.id.Location);
        location.setVisibility(View.VISIBLE);
        hideKeyboard();
    }

    /**
     * Method used to hide the keyboard after creating a new event
     */
    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // Getter for gMap
    public GoogleMap getgMap(){
        return gMap;
    }

    /*
    pop up a new view so the user can input the information about the event
    that is about to be created.
     */
    public void setCreateEventPopUp(View view){
        //Check if the user is logged in or not
        if(!rootUser.isLoggedIn()) {
            loginPopUp.setVisibility(View.VISIBLE);
        }
        //if the user is logged in, opens the createEventPopUp page
        else {
            CreateEventPopUp.setVisibility(View.VISIBLE);
        }

        ImageView btn = findViewById(R.id.NewEvent);
        btn.setVisibility(View.GONE);
    }

    /**
     * Should be called when a new User is created to get a unique ID
     */
    static protected long getEventID(){
        long tempID = model.getEventID();
        model.updateEventID();
        return tempID;
    }

    /**
     * Should be called when a new User is created to get a unique ID
     */
    static protected long getUserID(){
        long tempID = model.getUserID();
        model.updateUserID();
        return tempID;
    }

    /**
     * Authenticate the user to match the root User for now
     * @param view the view that it is on
     */
    public void login(View view){
        EditText usernameToCheck = findViewById(R.id.username_login);
        EditText passwordToCheck = findViewById(R.id.password_login);

        String strUsernameToCheck = usernameToCheck.getText().toString();
        String strPasswordToCheck = passwordToCheck.getText().toString();

        if (strUsernameToCheck.equals(rootUser.getName()) && strPasswordToCheck.equals(rootUser.getPassword())) {
            rootUser.setLoggedIn(true);
            loginPopUp.setVisibility(View.GONE);
            CreateEventPopUp.setVisibility(View.VISIBLE);
            usernameToCheck.setText("");
            passwordToCheck.setText("");
        }
        else {
            Toast toast = Toast.makeText(this, "Username or Password does not match", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            usernameToCheck.setText("");
            passwordToCheck.setText("");
        }

    }

    /**
     * Pop up the window for creating a new user
     * @param view this view
     */
    public void createNewLogin(View view) {
        loginPopUp.setVisibility(View.GONE);
        createNewUserPopUp.setVisibility(View.VISIBLE);
    }

    /**
     * Confirm if the new User match
     * @param view this view
     */
    public void confirmNewUser(View view) {
        EditText newUsername = findViewById(R.id.newUsername);
        EditText createNewPass = findViewById(R.id.createNewPassword);
        EditText confirmPass = findViewById(R.id.createNewPasswordConfirm);
        EditText newEmail = findViewById(R.id.createNewEmail);

        String strNewUsername = newUsername.getText().toString();
        String strCreateNewPass = createNewPass.getText().toString();
        String strConfirmPass = confirmPass.getText().toString();
        String strNewEmail = newEmail.getText().toString();

        if (strNewUsername.length() <= 0) {
            Toast toast = Toast.makeText(this, "Missing Username Info", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return;
        }
        else if (strCreateNewPass.length() <= 0) {
            Toast toast = Toast.makeText(this, "Missing Password Info", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return;
        }
        else if (strConfirmPass.length() <= 0) {
            Toast toast = Toast.makeText(this, "Missing Confirm Password Info", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return;
        }
        else if( strNewEmail.length() <= 0){
            Toast toast = Toast.makeText(this, "Missing Email Info", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return;
        }

        //User newUser;

        if(strCreateNewPass.equals(strConfirmPass)) {
            //newUser = new User(strNewUsername, strCreateNewPass, strNewEmail);
            rootUser.setPassword(strCreateNewPass);
            rootUser.setName(strNewUsername);
            rootUser.setEmail(strNewEmail);
        }
        else {
            Toast toast = Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return;
        }

        if(model.checkUserEmails(strNewEmail)){
            Toast toast = Toast.makeText(this, "User with Email Already Exists", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER,0,0);
            toast.show();
            return;
        }

        model.addUser(rootUser);
        //rootUser = newUser;
        rootUser.setLoggedIn(true);

        createNewUserPopUp.setVisibility(View.GONE);
        newUsername.setText("");
        createNewPass.setText("");
        confirmPass.setText("");
        newEmail.setText("");
        loginPopUp.setVisibility(View.VISIBLE);
    }

    /**
     * Called after the User creates a new Event. This method will add said event to the 'database'
     */
    static protected void addEvent(Event event){
        model.addEvent(event);
    }

    /**
     * From any class you can get the MainActivity by the following line of code:
     * MainActivity.getMainActivityRef().get()."What ever method you want to use"
     */
    static protected WeakReference<MainActivity> getMainActivityRef(){
        return mainActivity;
    }

    /**
     * changes the visibility of the CreateEventPopUp window to GONE
     * and brings back the Create Event button
     * @param view The view that show up
     */
    public void createEventBackButton(View view) {
        CreateEventPopUp.setVisibility(View.GONE);
        EditText eventName = findViewById(R.id.EventName);
        eventName.setText("");
        EditText location = findViewById(R.id.Location);
        location.setText("");
        EditText date = findViewById(R.id.Date);
        date.setText("");
        EditText description = findViewById(R.id.Description);
        description.setText("");

        ImageView btn = findViewById(R.id.NewEvent);
        btn.setVisibility(View.VISIBLE);

        clickedOnMap = false;
        location.setVisibility(View.VISIBLE);
    }

    /**
     * Uses the events map from the Model class to add markers to GMap
     */
    protected void populateMap(){
        Event tempEvent;
        for (Map.Entry<Long, Event> entry : model.getEvents().entrySet()) {
            tempEvent = entry.getValue();
            GetLatLngFromAddress getLatLng = new GetLatLngFromAddress(tempEvent.getLocation());

            Thread thread = new Thread(getLatLng);
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            LatLng location = getLatLng.getLatLng();
            Marker mEvent = gMap.addMarker(new MarkerOptions().position(location).title(tempEvent.getName()));
            mEvent.setTag(tempEvent);
        }
    }

}
