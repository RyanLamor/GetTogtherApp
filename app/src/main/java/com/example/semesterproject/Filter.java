package com.example.semesterproject;

import java.util.HashMap;
import java.util.Map;

public class Filter {
    private Boolean activated;

    public Filter(){
        activated = false;
    }

    protected void activate(){
        activated = true;
    }

    protected void deactivate(){
        activated = false;
    }

    protected Boolean isActivated(){
        return activated;
    }

    protected Map<Long, Event> getFilteredEvents(Category category){
        Map<Long, Event> filteredEvents = new HashMap<>();
        //Search through database to find which events have the same category as the one passed in
        //What do we do if filter not activated?
        return filteredEvents;
    }
}
