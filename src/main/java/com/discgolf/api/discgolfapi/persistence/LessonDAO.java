package com.discgolf.api.discgolfapi.persistence;

import java.io.IOException;

import com.discgolf.api.discgolfapi.model.Lesson;

/**
 * Defines the interface for Lesson object persistence
 * 
 * @author RF
 */
public interface LessonDAO {
    /**
     * Retrieves all {@linkplain Lesson lessons}
     * 
     * @return An array of {@link Lesson lesson} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Lesson[] getLessons() throws IOException;

    /**
     * Finds all {@linkplain Lesson lessons} with a matching type
     * 
     * @param text The type to match
     * 
     * @return An array of {@link Lesson lessons} that match the given type, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Lesson[] findLessons(String text) throws IOException;

    /**
     * Retrieves a {@linkplain Lesson lesson} with the given id
     * 
     * @param id The id of the {@link Lesson lesson} to get
     * 
     * @return a {@link Lesson lesson} object with the matching id
     * <br>
     * null if no {@link Lesson lesson} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Lesson getLesson(int id) throws IOException;

    /**
     * Retrieves a {@linkplain Lesson lesson} with the given username
     * 
     * @param username The username associated with the {@link Lesson lesson} to get
     * 
     * @return a {@link Lesson lesson} object with the matching id
     * <br>
     * null if no {@link Lesson lesson} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Lesson[] getLessonsByUser(String username) throws IOException;

    /**
     * Creates and saves a {@linkplain Lesson lesson}
     * 
     * @param lesson {@linkplain Lesson lesson} object to be created and saved
     * <br>
     * The id of the lesson object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Lesson lesson} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Lesson createLesson(Lesson lesson) throws IOException;

    /**
     * Updates and saves a {@linkplain Lesson lesson}
     * 
     * @param {@link Lesson lesson} object to be updated and saved
     * 
     * @return updated {@link Lesson lesson} if successful, null if
     * {@link Lesson lesson} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Lesson updateLesson(Lesson lesson) throws IOException;

    /**
     * Deletes a {@linkplain Lesson lesson} with the given id
     * 
     * @param id The id of the {@link Lesson lesson}
     * 
     * @return true if the {@link Lesson lesson} was deleted
     * <br>
     * false if lesson with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteLesson(int id) throws IOException;

    Lesson[] getLessonsOnDate(String date) throws IOException;
}
