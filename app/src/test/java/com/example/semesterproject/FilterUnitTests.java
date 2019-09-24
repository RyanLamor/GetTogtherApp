package com.example.semesterproject;
import static org.junit.Assert.*;
import org.junit.Test;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FilterUnitTests {

    @Test
    public void test_Filter_Constructor() throws NoSuchFieldException, IllegalAccessException {
        Filter f = new Filter();

        Field activated = f.getClass().getDeclaredField("activated");
        activated.setAccessible(true);

        assertTrue(activated.get(f).equals(false));
    }

    @Test
    public void test_Filter_activate() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Filter f = new Filter();

        Field activated = f.getClass().getDeclaredField("activated");
        activated.setAccessible(true);

        Method activate = f.getClass().getDeclaredMethod("activate");
        activate.invoke(f);

        assertTrue(activated.get(f).equals(true));
    }

    @Test
    public void test_Filter_deactivate() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Filter f = new Filter();

        Field activated = f.getClass().getDeclaredField("activated");
        activated.setAccessible(true);

        Method deactivate = f.getClass().getDeclaredMethod("deactivate");
        deactivate.invoke(f);

        assertTrue(activated.get(f).equals(false));
    }

    @Test
    public void test_Filter_isActivated() throws NoSuchMethodException, NoSuchFieldException, InvocationTargetException, IllegalAccessException {
        Filter f = new Filter();

        Field activated = f.getClass().getDeclaredField("activated");
        activated.setAccessible(true);

        Method isActivated = f.getClass().getDeclaredMethod("isActivated");
        Boolean isOff = (Boolean) isActivated.invoke(f);

        assertEquals(isOff, activated.get(f));

        activated.set(f, true);
        Boolean isOn = (Boolean) isActivated.invoke(f);

        assertEquals(isOn, activated.get(f));
    }

    @Test
    public void test_Filter_getFilteredEvents() throws IOException {
        User creator = new User("Ryan", "r123");//id = 1

        User going1 = new User("g1", "pass");//id = 2
        User going2 = new User("g2" , "pass");//id = 3
        Map<Long, User> usersGoing = new HashMap<>();
        usersGoing.put(new Long(1), going1);
        usersGoing.put(new Long(2), going2);

        User maybe1 = new User("m1", "pass");//id = 4
        User maybe2 = new User("m2", "pass");//id = 5
        Map<Long, User> usersMaybe = new HashMap<>();
        usersMaybe.put(new Long(4), maybe1);
        usersMaybe.put(new Long(5), maybe2);

        Event e1 = new Event("Event 1", Category.Sports, new Date(), "Ryan's House", creator, usersGoing, usersMaybe, "Swimming");//id = 1
        Event e2 = new Event("Event 2", Category.Sports, new Date(), "Tyson's House", creator, usersGoing, usersMaybe, "Basketball");//id = 2
        Event e3 = new Event("Event 3", Category.Games, new Date(), "Rio's House", creator, usersGoing, usersMaybe, "PS4");//id = 3

        FileWriter fileWriter = new FileWriter("savedEvents.txt");
        BufferedWriter out = new BufferedWriter(fileWriter);

        out.write(e1.toString());
        out.newLine();
        out.write(e2.toString());
        out.newLine();
        out.write(e3.toString());
        out.close();

        Filter f = new Filter();

        Map<Long, Event> filteredEvents = f.getFilteredEvents(Category.Sports);

        assertEquals(e1, filteredEvents.get(1));
        assertEquals(e1, filteredEvents.get(2));

    }
}
