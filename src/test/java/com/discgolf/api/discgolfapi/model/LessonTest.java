package com.discgolf.api.discgolfapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Objects;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Disc class
 * 
 * @author SWEN Faculty + coolname
 */
@Tag("Model-tier")
public class LessonTest {
    @Test
    public void testCtor() {
        // Setup
        int expectedId = 99;
        String expectedUsername = "aiden";
        String expectedTitle = "Better Form";
        String expectedDesc = "make form better";
        String expectedDays = "MWF";
        String expectedStartDate = "10-10-22";
        String expectedEndDate = "12-10-22";
        double expectedPrice = 123;

        // Invoke
        Lesson lesson = new Lesson(expectedId, expectedUsername, expectedTitle, expectedDesc, expectedDays,
        expectedStartDate, expectedEndDate, expectedPrice);

        // Analyze
        assertEquals(expectedId,  lesson.getId());
        assertEquals(expectedTitle,lesson.getTitle());
        assertEquals(expectedDesc,lesson.getDescription());
        assertEquals(expectedDays,lesson.getDays());
        assertEquals(expectedStartDate, lesson.getStartDate());
        assertEquals(expectedEndDate, lesson.getEndDate());
        assertEquals(expectedPrice, lesson.getPrice());
    }

    @Test
    public void testName() {
        // Setup
        Lesson lesson = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF",
        "10-10-22", "12-10-22", 299);

        String expectedUsername = "aiden";
        String expectedTitle = "Better Form";
        String expectedDesc = "make form better";
        String expectedDays = "MWF";
        String expectedStartDate = "10-10-22";
        String expectedEndDate = "12-10-22";
        double expectedPrice = 123;

        // Invoke
        lesson.setUsername(expectedUsername);
        lesson.setTitle(expectedTitle);
        lesson.setDescription(expectedDesc);
        lesson.setDays(expectedDays);
        lesson.setStartDate(expectedStartDate);
        lesson.setEndDate(expectedEndDate);
        lesson.setPrice(expectedPrice);
        // Analyze
        assertEquals(expectedUsername,lesson.getUsername());
        assertEquals(expectedTitle,lesson.getTitle());
        assertEquals(expectedDesc,lesson.getDescription());
        assertEquals(expectedDays,lesson.getDays());
        assertEquals(expectedStartDate, lesson.getStartDate());
        assertEquals(expectedEndDate, lesson.getEndDate());
        assertEquals(expectedPrice, lesson.getPrice());
    }

    @Test
    public void testToString() {
        // Setup
        String expectedUsername = "aiden";
        String expectedTitle = "Better Form";
        String expectedDesc = "make form better";
        String expectedDays = "MWF";
        String expectedStartDate = "10-10-22";
        String expectedEndDate = "12-10-22";
        double expectedPrice = 123;

        Lesson lesson = new Lesson(99, expectedUsername, expectedTitle, expectedDesc, expectedDays, expectedStartDate,
        expectedEndDate, expectedPrice);

        String expected = "{" +
        " id='" + lesson.getId() + "'" +
        ", username='" + lesson.getUsername() + "'" +
        ", title='" + lesson.getTitle() + "'" +
        ", description='" + lesson.getDescription() + "'" +
        ", days='" + lesson.getDays() + "'" +
        ", startDate='" + lesson.getStartDate() + "'" +
        ", endDate='" + lesson.getEndDate() + "'" +
        ", price='" + lesson.getPrice() + "'" +
        "}";

        // Invoke
        String actual_string = lesson.toString();

        // Analyze
        assertEquals(expected ,actual_string);
    }

    @Test
    public void testEqualsValid() {
        Lesson lesson = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF",
        "10-10-22", "12-10-22", 299);
        Lesson lesson2 = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF",
        "10-10-22", "12-10-22", 299);

        boolean actual = lesson.equals(lesson2);
        boolean expected = true;
        assertEquals(actual, expected);
    }

    @Test
    public void testEqualsNotValidType() {
        Lesson lesson = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF",
        "10-10-22", "12-10-22", 299);
        Integer test = 99;
        boolean actual = lesson.equals(test);
        boolean expected = false;
        assertEquals(actual, expected);
    }

    @Test
    public void testHashCode() {
        Lesson lesson = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF",
        "10-10-22", "12-10-22", 299);
        int hash = Objects.hash(lesson.getId(), lesson.getUsername(), lesson.getTitle(), lesson.getDescription(), lesson.getDays(), lesson.getStartDate(), lesson.getEndDate(), lesson.getPrice());

        assertEquals(lesson.hashCode(), hash);
    }

    @Test
    public void testEqualsNotEqual() {
        Lesson lesson = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF",
        "10-10-22", "12-10-22", 299);
        Lesson lesson2 = new Lesson(100, "aiden", "Accuracy", "make accurate", "MWF",
        "10-10-22", "12-10-22", 299);
        boolean actual = lesson.equals(lesson2);
        boolean expected = false;
        assertEquals(actual, expected);
    }
}