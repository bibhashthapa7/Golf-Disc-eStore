package com.discgolf.api.discgolfapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.discgolf.api.discgolfapi.model.Lesson;
import com.discgolf.api.discgolfapi.persistence.LessonDAO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for the Lesson resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author SWEN Faculty + coolname
 */

@RestController
@RequestMapping("lessons")
public class LessonController {
    private static final Logger LOG = Logger.getLogger(LessonController.class.getName());
    private LessonDAO lessonDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param lessonDao The {@link LessonDAO Lesson Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public LessonController(LessonDAO lessonDao) {
        this.lessonDao = lessonDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Lesson lesson} for the given id
     * 
     * @param id The id used to locate the {@link Lesson lesson}
     * 
     * @return ResponseEntity with {@link Lesson lesson} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Lesson> getLesson(@PathVariable int id) {
        LOG.info("GET /lessons/" + id);
        try {
            Lesson lesson = lessonDao.getLesson(id);
            if (lesson != null)
                return new ResponseEntity<Lesson>(lesson,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/dates")
    public ResponseEntity<Lesson[]> getLessonsOnDate(@RequestParam String date) {
        LOG.info("GET /lessons/dates?date="+date);

        // Replace below with "your" implementation
        try {
            Lesson[] lessons = lessonDao.getLessonsOnDate(date);
            if (lessons != null)
                return new ResponseEntity<Lesson[]>(lessons, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    /**
     * Responds to the GET request for {@linkplain Lesson lessons} associated with the given username
     * 
     * @param username The username used to locate the {@link Lesson lesson}
     * @return ResponseEntity with array of {@link Lesson lesson} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<Lesson[]> getLessonByUser(@PathVariable String username) {
        LOG.info("GET /lessons/user/" + username);
        try {
            Lesson[] lessons = lessonDao.getLessonsByUser(username);
            return new ResponseEntity<Lesson[]>(lessons, HttpStatus.OK);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Lesson lessons}
     * 
     * @return ResponseEntity with array of {@link Lesson lesson} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Lesson[]> getLessons() {
        LOG.info("GET /lessons");

        // Replace below with "your" implementation
        try {
            Lesson[] lessons = lessonDao.getLessons();
            if (lessons != null)
                return new ResponseEntity<Lesson[]>(lessons, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Lesson lessons} which has the same type
     * 
     * @param type The type parameter which contains the int_type used to find the {@link Lesson lessons}
     * 
     * @return ResponseEntity with array of {@link Lesson lesson} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all lessons that have the type 1
     * GET http://localhost:8080/lessons/?title=form
     */
    @GetMapping("/")
    public ResponseEntity<Lesson[]> searchLessons(@RequestParam String title) {
        LOG.info("GET /lessons/?title="+title);

        // Replace below with "your" implementation
        try {
            Lesson[] lessons = lessonDao.findLessons(title);
            if (lessons != null)
                return new ResponseEntity<Lesson[]>(lessons, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Lesson lesson} with the provided lesson object
     * 
     * @param lesson - The {@link Lesson lesson} to create
     * 
     * @return ResponseEntity with created {@link Lesson lesson} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Lesson lesson} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Lesson> createLesson(@RequestBody Lesson lesson) {
        LOG.info("POST /lessons " + lesson);

        // Replace below with "your" implementation
        try {
            Lesson newLesson = lessonDao.createLesson(lesson);
            if (newLesson != null)
                return new ResponseEntity<Lesson>(newLesson, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Lesson lesson} with the provided {@linkplain Lesson lesson} object, if it exists
     * 
     * @param lesson The {@link Lesson lesson} to update
     * 
     * @return ResponseEntity with updated {@link Lesson lesson} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Lesson> updateLesson(@RequestBody Lesson lesson) {
        LOG.info("PUT /lessons " + lesson);

        // Replace below with "your" implementation
        try {
            Lesson updatedLesson = lessonDao.updateLesson(lesson);
            if (updatedLesson != null)
                return new ResponseEntity<Lesson>(updatedLesson, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Lesson lesson} with the given id
     * 
     * @param id The id of the {@link Lesson lesson} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Lesson> deleteLesson(@PathVariable int id) {
        LOG.info("DELETE /lessons/" + id);

        // Replace below with "your" implementation
        try {
            boolean deleted = lessonDao.deleteLesson(id);
            if (deleted)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}