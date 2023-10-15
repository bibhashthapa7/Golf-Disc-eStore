package com.discgolf.api.discgolfapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.discgolf.api.discgolfapi.model.Lesson;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Lesson File DAO class
 * 
 * @author SWEN Faculty + coolname
 */
@Tag("Persistence-tier")
public class LessonFileDAOTest {
    LessonFileDAO lessonFileDAO;
    Lesson[] testLessons;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupLessonFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testLessons = new Lesson[3];
        testLessons[0] = new Lesson(99, null, "Throwing Form", "make form better", "MWF","10-10-22", "12-10-22", 123);
        testLessons[1] = new Lesson(100, null, "Throwing Speed", "make speed faster", "MWF","10-10-22", "12-10-22", 123);
        testLessons[2] = new Lesson(101, null, "Accuracy", "make your aim accurate", "TuTh","10-10-22", "12-10-22", 250);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the lesson array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"), Lesson[].class))
                .thenReturn(testLessons);
        lessonFileDAO = new LessonFileDAO("doesnt_matter.txt", mockObjectMapper);
    }

    @Test
    public void testGetLessons() {
        // Invoke
        Lesson[] lessons = lessonFileDAO.getLessons();

        // Analyze
        assertEquals(lessons.length,testLessons.length);
        for (int i = 0; i < testLessons.length;++i)
            assertEquals(lessons[i],testLessons[i]);
    }

    @Test
    public void testFindLessons() {
        // Invoke
        Lesson[] lessons = lessonFileDAO.findLessons("Accuracy");

        // Analyze
        assertEquals(lessons.length,1);
        assertEquals(lessons[0],testLessons[2]);
    }

    @Test
    public void testGetLesson() {
        // Invoke
        Lesson lesson = lessonFileDAO.getLesson(99);

        // Analzye
        assertEquals(lesson,testLessons[0]);
    }

    @Test
    public void testDeleteLesson() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> lessonFileDAO.deleteLesson(99),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test lessons array - 1 (because of the delete)
        // Because lessons attribute of LessonFileDAO is package private
        // we can access it directly
        assertEquals(lessonFileDAO.lessons.size(),testLessons.length-1);
    }

    @Test
    public void testCreateLesson() {
        // Setup
        Lesson lesson = new Lesson(102, "aiden", "Speed","Faster","Saturday","12-1-22","12-31-22", 125);

        // Invoke
        Lesson result = assertDoesNotThrow(() -> lessonFileDAO.createLesson(lesson),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Lesson actual = lessonFileDAO.getLesson(lesson.getId());
        assertEquals(actual.getId(),lesson.getId());
        assertEquals(actual.getTitle(),lesson.getTitle());
        assertEquals(actual.getDescription(),lesson.getDescription());
        assertEquals(actual.getDays(),lesson.getDays());
        assertEquals(actual.getStartDate(), lesson.getStartDate());
        assertEquals(actual.getEndDate(), lesson.getEndDate());
        assertEquals(actual.getPrice(), lesson.getPrice());
    }

    @Test
    public void testUpdateLesson() {
        // Setup
        Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
        ,"10-10-22", "12-10-22", 250);
        // Invoke
        Lesson result = assertDoesNotThrow(() -> lessonFileDAO.updateLesson(lesson),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Lesson actual = lessonFileDAO.getLesson(lesson.getId());
        assertEquals(actual,lesson);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Lesson[].class));

                Lesson lesson = new Lesson(102, "aiden", "Speed","Faster","Saturday","12-1-22","12-31-22", 125);

        assertThrows(IOException.class,
                        () -> lessonFileDAO.createLesson(lesson),
                        "IOException not thrown");
    }

    

    @Test
    public void testGetLessonNotFound() {
        // Invoke
        Lesson lesson = lessonFileDAO.getLesson(98);

        // Analyze
        assertEquals(lesson,null);
    }

    @Test
    public void testDeleteLessonNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> lessonFileDAO.deleteLesson(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(lessonFileDAO.lessons.size(),testLessons.length);
    }

    @Test
    public void testUpdateLessonNotFound() {
        // Setup
        Lesson lesson = new Lesson(03, "aiden", "Speed","Faster","Saturday","12-1-22","12-31-22", 125);

        // Invoke
        Lesson result = assertDoesNotThrow(() -> lessonFileDAO.updateLesson(lesson),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the LessonFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Lesson[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new LessonFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }

    @Test
    public void testValidateLesson() throws IOException{
        // Setup
        Lesson lesson = new Lesson(102, "aiden", "Speed","Faster","Saturday","12/01/2022","12/31/2022", 125);
        // Invoke
        boolean result =  lessonFileDAO.validateLesson(lesson, "12/03/2022");

        // Analyze
        assertEquals(result,true);
    }

    @Test
    public void testGetLessonsByUser() throws IOException{        

        Lesson[] userLessons = lessonFileDAO.getLessonsByUser("aiden");
        // Analyze
        assertEquals(0, userLessons.length);
    }
}