package com.example.semesterproject;

import android.view.View;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class UserUnitTests {

    //     Make sure The create event screen with all buttons and text fields shows up and
    // the event is added to the user's hosting map. Call model's add event to add the
    // event to the mock database.
    @Test
    public void test_Event_createEvent() throws NoSuchFieldException {
        Field id = Event.class.getDeclaredField("id");
        id.setAccessible(true);

        Field hosting = Event.class.getDeclaredField("hosting");
        hosting.setAccessible(true);

        Map<Long, Event> hosting1 = new HashMap<>();
        Map<Long, User> going = new HashMap<>();
        Map<Long, User> maybe = new HashMap<>();
        User me = new User("Tyson", "DoAGoodTurn");
        Event FirstEvent = new Event("Catan Party", Category.Games, new Date(), "Manwaring Center", me, going, maybe, "We will" +
                "aim for a 3 hour game. It will be with the Cities and Knights expantion and we'll have toast and pretzels. If you don't like that " +
                "stuff then don't come!");
        Long key = FirstEvent.getID();
        hosting1.put(FirstEvent.getID(), FirstEvent);

        Event SecondEvent = new Event("Pool Party", Category.Party, new Date(), "Rexburg Rapids", me, going, maybe, "We'll be" +
                " there for 3 hours, with food and drink provided! There will be lots of interactive water games, so be ready to get soaked!");
        Long key2 = SecondEvent.getID();
        hosting1.put(SecondEvent.getID(), SecondEvent);

        assertEquals(FirstEvent, hosting1.get(key));
        assertEquals(SecondEvent, hosting1.get(key2));

        //me.createEvent();
        //me.createEvent();



    }
}
