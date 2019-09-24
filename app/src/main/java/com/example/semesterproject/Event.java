package com.example.semesterproject;

import android.location.Address;
import android.provider.Telephony;

import java.util.Date;
import java.util.Map;

/**
 * Event class for the events that users will be able to see and go to.
 */
public class Event {
    private String name;
    private long id;//similar to a user id so that we can better keep track of events
    private Category category;
    private String date;
    private String location;
    private User creator;
    private Map<Long, User> going;
    private Map<Long, User> maybe;
    private String description;

    /**
     * Default Constructor
     */
    Event() {
        name = null;
        id = 0;
        category = null;
        date = null;
        location = null;
        creator = null;
        going = null;
        maybe = null;
        description = null;
    }

    /**
     * Non-Default Constructor to Auto-Generate the ID
     * @param name Name of the event
     * @param category The category the event would be in
     * @param date The date of the event
     * @param location The address of the event
     * @param createdBy the username of the person creating the event
     * @param going A list of people who have stated they are going to the event
     * @param maybe A list of people who have stated they might go to the event
     * @param desc A description of the event
     */
    Event(String name, Category category, String date, String location, User createdBy, Map<Long, User> going, Map<Long, User> maybe, String desc){
        this.name = name;
        this.category = category;
        this.date = date;
        this.location = location;
        this.creator = createdBy;
        this.going = going;
        this.maybe = maybe;
        this.description = desc;

        id = MainActivity.getEventID();
    }

    /**\
     *
     * @param id A specific id for the event
     * @param name Name of the event
     * @param category The category the event would be in
     * @param date The date of the event
     * @param location The address of the event
     * @param createdBy the username of the person creating the event
     * @param going A list of people who have stated they are going to the event
     * @param maybe A list of people who have stated they might go to the event
     * @param desc A description of the event
     */
    Event(Long id, String name, Category category, String date, String location, User createdBy, Map<Long, User> going, Map<Long, User> maybe, String desc){
        this.name = name;
        this.category = category;
        this.date = date;
        this.location = location;
        this.creator = createdBy;
        this.going = going;
        this.maybe = maybe;
        this.description = desc;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public String getDate() {
        return date;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public Map<Long, User> getGoing() { return going; }

    public Map<Long, User> getMaybe() { return maybe; }

    public User getCreator() { return creator; }

    /**
     * Allows the user to change details of an event
     */
    void updateEvent(){
    }

    /**
     * Adds a user to the list of people going to the event
     */
    protected void addGoing(User user){
        going.put(user.getID(), user);
    }

    /**
     * Adds a user to the list of people that might go to the event
     */
    protected void addMaybe(User user){
        maybe.put(user.getID(), user);
    }

    /**
     * Gives the id of the event
     * @return long id
     */
    Long getID(){
        return id;
    }

    /**
     * Converts the details of an event into strings
     * @return a string
     */
    @Override
    public String toString(){
        return id + "\t" +  name + "\t" + category.toString() + "\t" + date.toString() + "\t" + location + "\t" + creator.getName()  + "\t" + going  + "\t" + maybe  + "\t" + description;
    }

    /**
     * Confirms the id of an event
     * @param otherEvent another event to compare
     * @return whether or not they are identical.
     */
    @Override
    public boolean equals(Object otherEvent){//I hope this was okay to add - Ryan
        Event temp = (Event) otherEvent;
        if(this.id == temp.getID() )
            return true;
        else
            return false;
    }
}
