package com.discgolf.api.discgolfapi.model;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Lesson object that represents lessons/coaching session for a disc golf store
 * 
 * @author RF
 */
public class Lesson {

    /* fields */
    @JsonProperty("id") private final int id;
    @JsonProperty("username") private String username;
    @JsonProperty("title") private String title;
    @JsonProperty("description") private String description;
    @JsonProperty("days") private String days;
    @JsonProperty("startDate") private String startDate;
    @JsonProperty("endDate") private String endDate;
    @JsonProperty("price") private double price;

    /**
     * Constructor to create a lesson object.
     * @param id id of lesson
     * @param title title of lesson
     * @param description description of what the lesson goes over
     * @param days what day of week lesson is held (i.e MWF)
     * @param startDate when the lesson starts
     * @param endDate when the lesson ends end
     * @param price how much it does
     */
    public Lesson(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("title") String title, 
                  @JsonProperty("description") String description, @JsonProperty("days") String days,
                  @JsonProperty("startDate") String startDate, @JsonProperty("endDate") String endDate,
                  @JsonProperty("price") double price) {    
        this.id = id;
        this.username = username;
        this.title = title;
        this.description = description;
        this.days = days;
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    /**
     * get the lesson's id
     * 
     * @return id
     */
    public int getId() {
        return this.id;
    }

    /**
     * get the associated username
     * 
     * @return The associated username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * updates the associated username
     * @param username updated associated username
     */
    public void setUsername(String username) {
        this.username = username;
    }


    /**
     * gets the lesson's title
     * 
     * @return lesson title
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * updates the lesson title
     * @param title updated lesson title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * gets the description of the lesson
     * @return gets the description of the lesson
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * sets the description of the lesson
     * @param description new desc of lesson
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * gets the string representation of when the 
     * lesson is held
     */
    public String getDays() {
        return this.days;
    }

    /**
     * updates the days of week lesson is held
     * @param days new days held
     */
    public void setDays(String days) {
        this.days = days;
    }

    /**
     * gets the start date of the lesson
     * @return start date of the lesson
     */
    public String getStartDate() {
        return this.startDate;
    }

    /**
     * Updates the start date of a lesson
     * 
     * @param startDate new start date
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * gets end date of a lesson
     * @return end date of a lesson
     */
    public String getEndDate() {
        return this.endDate;
    }

    /**
     * Updates end date of a lesson
     * @param endDate end date
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * gets price of a lesson
     * @return lesson price
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * update the lesson price
     * @param price new price of lesson
     */
    public void setPrice(double price) {
        this.price = price;
    }


    @Override
    public String toString() {
        return "{" +
            " id='" + getId() + "'" +
            ", username='" + getUsername() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", days='" + getDays() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", price='" + getPrice() + "'" +
            "}";
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Lesson)) {
            return false;
        }
        Lesson lesson = (Lesson) o;
        return id == lesson.id
        && Objects.equals(username, lesson.username)
        && Objects.equals(title, lesson.title)
        && Objects.equals(description, lesson.description)
        && Objects.equals(days, lesson.days)
        && Objects.equals(startDate, lesson.startDate)
        && Objects.equals(endDate, lesson.endDate)
        && price == lesson.price;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, title, description, days, startDate, endDate, price);
    }
}