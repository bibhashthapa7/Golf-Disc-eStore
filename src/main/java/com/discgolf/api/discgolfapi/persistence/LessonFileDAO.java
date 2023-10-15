package com.discgolf.api.discgolfapi.persistence;

import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.discgolf.api.discgolfapi.model.Lesson;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Lessons
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty + coolname
 */
@Component
public class LessonFileDAO implements LessonDAO {
    Map<Integer,Lesson> lessons;   // Provides a local cache of the lesson objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Lesson
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new lesson
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Lesson File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public LessonFileDAO(@Value("${lessons.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the lessons from the file
    }

    /**
     * Generates the next id for a new {@linkplain Lesson lesson}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Lesson lessons} from the tree map
     * 
     * @return  The array of {@link Lesson lessons}, may be empty
     */
    private Lesson[] getLessonsArray() {
        return getLessonsArray(null);
    }

    /**
     * Generates an array of {@linkplain Lesson lessons} from the tree map for any
     * {@linkplain Lesson lessons} titles that contain text specified by containsText
     * <br>
     * If textContains is Null, the array contains all of the {@linkplain Lesson lessons}
     * in the tree map
     * 
     * @return  The array of {@link Lesson lessons}, may be empty
     */
    private Lesson[] getLessonsArray(String text) { // if typeToMatch == -1, no filter
        ArrayList<Lesson> lessonArrayList = new ArrayList<>();

        for (Lesson lesson : lessons.values()) {
            if (text == null || lesson.getTitle().toLowerCase().contains(text.toLowerCase())) {
                lessonArrayList.add(lesson);
            }
        }

        Lesson[] lessonArray = new Lesson[lessonArrayList.size()];
        lessonArrayList.toArray(lessonArray);
        return lessonArray;
    }

    /**
     * Generates an array of {@linkplain Lesson lessons} from the tree map for any
     * {@linkplain Lesson lessons} titles that contain text specified by containsText that are in inventory
     * <br>
     * If textContains is Null, the array contains all of the {@linkplain Lesson lessons}
     * in the tree map
     * 
     * @return  The array of {@link Lesson lessons}, may be empty
     */
    private Lesson[] getInvenLessonsArray(String text) {
            ArrayList<Lesson> lessonArrayList = new ArrayList<>();

            for (Lesson lesson : lessons.values()) {
                if (lesson.getUsername() == null && (text == null || lesson.getTitle().toLowerCase().contains(text.toLowerCase())))
                    lessonArrayList.add(lesson);
            }

            Lesson[] lessonArray = new Lesson[lessonArrayList.size()];
            lessonArrayList.toArray(lessonArray);
            return lessonArray;
    }

    /**
     * Saves the {@linkplain Lesson lessons} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Lesson lessons} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Lesson[] lessonArray = getLessonsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),lessonArray);
        return true;
    }

    /**
     * Loads {@linkplain Lesson lessons} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        lessons = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of lessons
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Lesson[] lessonArray = objectMapper.readValue(new File(filename),Lesson[].class);

        // Add each lesson to the tree map and keep track of the greatest id
        for (Lesson lesson : lessonArray) {
            lessons.put(lesson.getId(),lesson);
            if (lesson.getId() > nextId)
                nextId = lesson.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Lesson[] getLessons() {
        synchronized(lessons) {
            return getInvenLessonsArray(null);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Lesson[] findLessons(String text) {
        synchronized(lessons) {
            return getInvenLessonsArray(text);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Lesson getLesson(int id) {
        synchronized(lessons) {
            if (lessons.containsKey(id))
                return lessons.get(id);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Lesson[] getLessonsByUser(String username) {
        synchronized(lessons) {
            ArrayList<Lesson> lessonArrayList = new ArrayList<>();

            for (Lesson lesson : lessons.values()) {
                String lesson_username = lesson.getUsername();
                if (lesson_username != null && lesson_username.equalsIgnoreCase(username))
                    lessonArrayList.add(lesson);
            }

            Lesson[] lessonArray = new Lesson[lessonArrayList.size()];
            lessonArrayList.toArray(lessonArray);
            return lessonArray;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Lesson createLesson(Lesson lesson) throws IOException {
        synchronized(lessons) {
            // We create a new lesson object because the id field is immutable
            // and we need to assign the next unique id
            Lesson newLesson = new Lesson(nextId(), lesson.getUsername(), lesson.getTitle(),
                                    lesson.getDescription(), lesson.getDays(), 
                                    lesson.getStartDate(), lesson.getEndDate(), lesson.getPrice());
            lessons.put(newLesson.getId(),newLesson);
            save(); // may throw an IOException
            return newLesson;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Lesson updateLesson(Lesson lesson) throws IOException {
        synchronized(lessons) {
            if (lessons.containsKey(lesson.getId()) == false)
                return null;  // lesson does not exist

            lessons.put(lesson.getId(),lesson);
            save(); // may throw an IOException
            return lesson;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteLesson(int id) throws IOException {
        synchronized(lessons) {
            if (lessons.containsKey(id)) {
                lessons.remove(id);
                return save();
            }
            else
                return false;
        }
    }
    
    @Override
    public Lesson[] getLessonsOnDate(String date) throws IOException {
        return Arrays.stream(getLessons()).filter(e -> validateLesson(e, date)).toArray(size -> new Lesson[size]);
    }

        /**
     * Helper method to determine if a lesson is held on a specific date
     * @param lesson lesson object in question
     * @param date date string being quieried 
     */
    boolean validateLesson(Lesson lesson, String date) {
        DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate dateHeld = LocalDate.parse(date, dateFormat); // the date being asked about
        LocalDate lessonStartDate = LocalDate.parse(lesson.getStartDate(), dateFormat); // date obj of when this specific start
        LocalDate lessonEndDate = LocalDate.parse(lesson.getEndDate(), dateFormat); // date obj when lesson ends meeting
        
        // map made and populated to help parse string representation of when lessons are held to DayOfWeek enum
        Map<String, DayOfWeek> daysKey = new HashMap<String, DayOfWeek>();
        daysKey.put("M", DayOfWeek.MONDAY);
        daysKey.put("Tu", DayOfWeek.TUESDAY);
        daysKey.put("W", DayOfWeek.WEDNESDAY);
        daysKey.put("Th", DayOfWeek.THURSDAY);
        daysKey.put("F", DayOfWeek.FRIDAY);
        daysKey.put("Sat", DayOfWeek.SATURDAY);
        daysKey.put("Sun", DayOfWeek.SUNDAY);
        
        Set<DayOfWeek> daysHeld = new HashSet<>();

        for(String x : daysKey.keySet()) {
            if(lesson.getDays().toLowerCase().contains(x.toLowerCase())) {
                daysHeld.add(daysKey.get(x));
            }
        }

        //  if date is before start          or       if date is after the end date
        if(dateHeld.compareTo(lessonStartDate) < 0 || dateHeld.compareTo(lessonEndDate) > 0) {
            return false;
        }
        // it is within valid range of dates that the lesson is taught so now return 
        // boolean of whether the date in question is a day (Day of Week) that the lesson is taught
        return daysHeld.contains(dateHeld.getDayOfWeek());
    }
}