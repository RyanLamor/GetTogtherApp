package com.example.semesterproject;

import static org.junit.Assert.*;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Objects;

public class EventUnitTests {

    /*
     Testing Constructors
     */
    @Test
    public void test_Event_DefaultConstructor() throws NoSuchFieldException, IllegalAccessException {
        Event defaultConstructor = new Event();

        Field name = defaultConstructor.getClass().getDeclaredField("name");
        name.setAccessible(true);
        Field id = defaultConstructor.getClass().getDeclaredField("id");
        id.setAccessible(true);
        Field category = defaultConstructor.getClass().getDeclaredField("category");
        category.setAccessible(true);
        Field date = defaultConstructor.getClass().getDeclaredField("date");
        date.setAccessible(true);
        Field location = defaultConstructor.getClass().getDeclaredField("location");
        location.setAccessible(true);
        Field creator = defaultConstructor.getClass().getDeclaredField("creator");
        creator.setAccessible(true);
        Field going = defaultConstructor.getClass().getDeclaredField("going");
        going.setAccessible(true);
        Field maybe = defaultConstructor.getClass().getDeclaredField("maybe");
        maybe.setAccessible(true);
        Field description = defaultConstructor.getClass().getDeclaredField("description");
        description.setAccessible(true);

        assertTrue(name.get(defaultConstructor) == null);
        assertTrue(Objects.equals(id.get(defaultConstructor), new Long(0)));
        assertTrue(category.get(defaultConstructor) == null);
        assertTrue(date.get(defaultConstructor) == null);
        assertTrue(location.get(defaultConstructor) == null);
        assertTrue(creator.get(defaultConstructor) == null);
        assertTrue(going.get(defaultConstructor) == null);
        assertTrue(maybe.get(defaultConstructor) == null);
        assertTrue(description.get(defaultConstructor) == null);
    }

}
