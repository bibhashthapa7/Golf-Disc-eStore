package com.discgolf.api.discgolfapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.discgolf.api.discgolfapi.model.Lesson;
import com.discgolf.api.discgolfapi.persistence.LessonDAO;

/**
 * Test the Lesson Controller class
 * 
 * @author SWEN Faculty + coolname
 */
@Tag("Controller-tier")
public class LessonControllerTest {
    private LessonController lessonController;
    private LessonDAO mockLessonDAO;

    /**
     * Before each test, create a new LessonController object and inject
     * a mock Lesson DAO
     */
    @BeforeEach
    public void setupLessonController() {
        mockLessonDAO = mock(LessonDAO.class);
        lessonController = new LessonController(mockLessonDAO);
    }

    @Test
    public void testGetLesson() throws IOException {  // getLesson may throw IOException
        // Setup
        Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
        ,"10-10-22", "12-10-22", 123);
        // When the same id is passed in, our mock Lesson DAO will return the Lesson object
        when(mockLessonDAO.getLesson(lesson.getId())).thenReturn(lesson);

        // Invoke
        ResponseEntity<Lesson> response = lessonController.getLesson(lesson.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(lesson,response.getBody());
    }

    @Test
    public void testGetLessonNotFound() throws Exception { // createLesson may throw IOException
        // Setup
        int lessonId = 99;
        // When the same id is passed in, our mock Lesson DAO will return null, simulating
        // no lesson found
        when(mockLessonDAO.getLesson(lessonId)).thenReturn(null);

        // Invoke
        ResponseEntity<Lesson> response = lessonController.getLesson(lessonId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetLessonHandleException() throws Exception { // createLesson may throw IOException
        // Setup
        int lessonId = 99;
        // When getLesson is called on the Mock Lesson DAO, throw an IOException
        doThrow(new IOException()).when(mockLessonDAO).getLesson(lessonId);

        // Invoke
        ResponseEntity<Lesson> response = lessonController.getLesson(lessonId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateLesson() throws IOException {  // createLesson may throw IOException
        // Setup
        Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
        ,"10-10-22", "12-10-22", 123);        
        // when createLesson is called, return true simulating successful
        // creation and save
        when(mockLessonDAO.createLesson(lesson)).thenReturn(lesson);

        // Invoke
        ResponseEntity<Lesson> response = lessonController.createLesson(lesson);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(lesson,response.getBody());
    }

    @Test
    public void testCreateLessonFailed() throws IOException {  // createLesson may throw IOException
        // Setup
        Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
        ,"10-10-22", "12-10-22", 123);
        // when createLesson is called, return false simulating failed
        // creation and save
        when(mockLessonDAO.createLesson(lesson)).thenReturn(null);

        // Invoke
        ResponseEntity<Lesson> response = lessonController.createLesson(lesson);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateLessonHandleException() throws IOException {  // createLesson may throw IOException
        // Setup
        Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
        ,"10-10-22", "12-10-22", 123);
        // When createLesson is called on the Mock Lesson DAO, throw an IOException
        doThrow(new IOException()).when(mockLessonDAO).createLesson(lesson);

        // Invoke
        ResponseEntity<Lesson> response = lessonController.createLesson(lesson);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateLesson() throws IOException { // updateLesson may throw IOException
        // Setup
        Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
        ,"10-10-22", "12-10-22", 123);
        // when updateLesson is called, return true simulating successful
        // update and save
        when(mockLessonDAO.updateLesson(lesson)).thenReturn(lesson);
        ResponseEntity<Lesson> response = lessonController.updateLesson(lesson);
        lesson.setDays("TuTh");

        // Invoke
        response = lessonController.updateLesson(lesson);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(lesson,response.getBody());
    }

    @Test
    public void testUpdateLessonFailed() throws IOException { // updateLesson may throw IOException
        // Setup
        Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
        ,"10-10-22", "12-10-22", 123);
        // when updateLesson is called, return true simulating successful
        // update and save
        when(mockLessonDAO.updateLesson(lesson)).thenReturn(null);

        // Invoke
        ResponseEntity<Lesson> response = lessonController.updateLesson(lesson);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateLessonHandleException() throws IOException { // updateLesson may throw IOException
        // Setup
        Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
        ,"10-10-22", "12-10-22", 123);
        // When updateLesson is called on the Mock Lesson DAO, throw an IOException
        doThrow(new IOException()).when(mockLessonDAO).updateLesson(lesson);

        // Invoke
        ResponseEntity<Lesson> response = lessonController.updateLesson(lesson);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetLessons() throws IOException { // getLessons may throw IOException
        // Setup
        Lesson[] lessons = new Lesson[2];
        Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
        ,"10-10-22", "12-10-22", 123);
        Lesson lesson2 = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF"
        ,"10-10-22", "12-10-22", 123);
        lessons[0] = lesson;
        lessons[1] = lesson2;
        // When getLessons is called return the lessons created above
        when(mockLessonDAO.getLessons()).thenReturn(lessons);

        // Invoke
        ResponseEntity<Lesson[]> response = lessonController.getLessons();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(lessons,response.getBody());
    }

    @Test
    public void testGetLessonsHandleException() throws IOException { // getLessons may throw IOException
        // Setup
        // When getLessons is called on the Mock Lesson DAO, throw an IOException
        doThrow(new IOException()).when(mockLessonDAO).getLessons();

        // Invoke
        ResponseEntity<Lesson[]> response = lessonController.getLessons();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetLessonsFailed() throws IOException { // getLessons may throw IOException
        // Setup
        Lesson[] lessons = new Lesson[2];
        Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
        ,"10-10-22", "12-10-22", 123);
        Lesson lesson2 = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF"
        ,"10-10-22", "12-10-22", 123);
        lessons[0] = lesson;
        lessons[1] = lesson2;
        // When getLessons is called return the lessons created above
        when(mockLessonDAO.getLessons()).thenReturn(null);

        // Invoke
        ResponseEntity<Lesson[]> response = lessonController.getLessons();

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

    }

    @Test
    public void testSearchLessons() throws IOException { // findLessons may throw IOException
        // Setup
        String searchType = "throwing";
        Lesson[] lessons = new Lesson[2];
        Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
        ,"10-10-22", "12-10-22", 123);
        Lesson lesson2 = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF"
        ,"10-10-22", "12-10-22", 123);
        lessons[0] = lesson;
        lessons[1] = lesson2;
        // When findLessons is called with the search string, return the two
        /// lessons above
        when(mockLessonDAO.findLessons(searchType)).thenReturn(lessons);

        // Invoke
        ResponseEntity<Lesson[]> response = lessonController.searchLessons(searchType);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(lessons,response.getBody());
    }

    @Test
    public void testSearchLessonsHandleException() throws IOException { // findLessons may throw IOException
        // Setup
        String searchType = "lesson";
        // When createLesson is called on the Mock Lesson DAO, throw an IOException
        doThrow(new IOException()).when(mockLessonDAO).findLessons(searchType);

        // Invoke
        ResponseEntity<Lesson[]> response = lessonController.searchLessons(searchType);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchLessonsFailed() throws IOException { // findLessons may throw IOException
        // Setup
        String searchType = "Unknown driver";
        Lesson[] lessons = new Lesson[2];
        Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
        ,"10-10-22", "12-10-22", 123);
        Lesson lesson2 = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF"
        ,"10-10-22", "12-10-22", 123);
        lessons[0] = lesson;
        lessons[1] = lesson2;
        // When findLessons is called with the search string, return the two
        /// lessons above
        when(mockLessonDAO.findLessons(searchType)).thenReturn(null);

        // Invoke
        ResponseEntity<Lesson[]> response = lessonController.searchLessons(searchType);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    

    }
    @Test
    public void testDeleteLesson() throws IOException { // deleteLesson may throw IOException
        // Setup
        int lessonId = 99;
        // when deleteLesson is called return true, simulating successful deletion
        when(mockLessonDAO.deleteLesson(lessonId)).thenReturn(true);

        // Invoke
        ResponseEntity<Lesson> response = lessonController.deleteLesson(lessonId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteLessonNotFound() throws IOException { // deleteLesson may throw IOException
        // Setup
        int lessonId = 99;
        // when deleteLesson is called return false, simulating failed deletion
        when(mockLessonDAO.deleteLesson(lessonId)).thenReturn(false);

        // Invoke
        ResponseEntity<Lesson> response = lessonController.deleteLesson(lessonId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteLessonHandleException() throws IOException { // deleteLesson may throw IOException
        // Setup
        int lessonId = 99;
        // When deleteLesson is called on the Mock Lesson DAO, throw an IOException
        doThrow(new IOException()).when(mockLessonDAO).deleteLesson(lessonId);

        // Invoke
        ResponseEntity<Lesson> response = lessonController.deleteLesson(lessonId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
  public void testGetLessonsOnDate() { // getLessonsOnDate may throw IOException
      // Setup
      String date = "10-10-22";
      Lesson[] lessons = new Lesson[2];
      Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
      ,"10-10-22", "12-10-22", 123);
      Lesson lesson2 = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF"
      ,"10-10-22", "12-10-22", 123);
      lessons[0] = lesson;
      lessons[1] = lesson2;
      //When findLessons is called with the date string, return the two 
      //lessons above
      try {
        when(mockLessonDAO.getLessonsOnDate(date)).thenReturn(lessons);

      } catch (Exception e) {/*squash */}
      // Invoke
      ResponseEntity<Lesson[]> response = lessonController.getLessonsOnDate(date);

      // Analyze
      assertEquals(HttpStatus.OK,response.getStatusCode());
      assertEquals(lessons,response.getBody());
  }

  @Test
  public void testGetLessonsOnDateNotFound() throws IOException { // findLessons may throw IOException
      // Setup
      String date = "12-11-22";
      Lesson[] lessons = new Lesson[2];
      Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
      ,"10-10-22", "12-10-22", 123);
      Lesson lesson2 = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF"
      ,"10-10-22", "12-10-22", 123);
      lessons[0] = lesson;
      lessons[1] = lesson2;
      // When findLessons is called with the date string, return the two
      /// lessons above
      when(mockLessonDAO.findLessons(date)).thenReturn(null);

      // Invoke
      ResponseEntity<Lesson[]> response = lessonController.getLessonsOnDate(date);

      // Analyze
      assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  } 

  @Test
  public void testGetLessonsOnDateHandleException() throws IOException { // findLessons may throw IOException
      // Setup
      String date = "10-10-22";
      // When findLessons is called on the Mock Lesson DAO, throw an IOException
      doThrow(new IOException()).when(mockLessonDAO).getLessonsOnDate(date);

      // Invoke
      ResponseEntity<Lesson[]> response = lessonController.getLessonsOnDate(date);

      // Analyze
      assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
  }

  @Test
  public void testGetLesonsForUser() throws IOException { // findLessons may throw IOException
      // Setup
      Lesson[] lessons = new Lesson[2];
      Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
      ,"10-10-22", "12-10-22", 123);
      Lesson lesson2 = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF"
      ,"10-10-22", "12-10-22", 123);
      lessons[0] = lesson;
      lessons[1] = lesson2;
      // When findLessons is called with the search string, return the two
      /// lessons above
      when(mockLessonDAO.getLessonsByUser("aiden")).thenReturn(lessons);

      // Invoke
      ResponseEntity<Lesson[]> response = lessonController.getLessonByUser("aiden");

      // Analyze
      assertEquals(HttpStatus.OK,response.getStatusCode());
      assertEquals(lessons,response.getBody());
  }

  @Test
  public void testGetLesonsForUserException() throws IOException { // findLessons may throw IOException
      // Setup
      Lesson[] lessons = new Lesson[2];
      Lesson lesson = new Lesson(99, "aiden", "Throwing Form", "make form better", "MWF"
      ,"10-10-22", "12-10-22", 123);
      Lesson lesson2 = new Lesson(100, "aiden", "Throwing Speed", "make speed faster", "MWF"
      ,"10-10-22", "12-10-22", 123);
      lessons[0] = lesson;
      lessons[1] = lesson2;
      // When findLessons is called with the search string, return the two
      /// lessons above
      doThrow(new IOException()).when(mockLessonDAO).getLessonsByUser("aiden");

      // Invoke
      ResponseEntity<Lesson[]> response = lessonController.getLessonByUser("aiden");

      // Analyze
      assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
  }
}