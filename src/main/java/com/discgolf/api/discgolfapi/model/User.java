package com.discgolf.api.discgolfapi.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class User {

    @JsonProperty("id") private final int id;
    @JsonProperty("username") private String username;
    @JsonProperty("password") private String password;
    /**
     * This does not get data stored, because it should be a server-side stack 
     * instead of logged-in data being persisted.
     */
    private boolean loggedIn;

    public User(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("password") String password) {    
        this.id = id;
        this.username = username;
        this.password = password;
        this.loggedIn = false;
    }

    /**
     * Determine if a user is an admin account
     * @return admin determine
     */
    public boolean isAdmin() {
        return this.username.equalsIgnoreCase("admin");
    }


    /**
     * Logs a user in
     * @param password attempted password
     * @return boolean result of if login is acceptable
     */
    public boolean login(String password) {
        if(!this.password.equals(password)) {
            return false;
        }

        loggedIn = true;
        return true;
    }

    /**
     * Method to log a user out
     * @return result of if a user was logged out
     */
    public boolean logout() {
        if(!loggedIn) {
            return false;
        }

        loggedIn = false;

        return true;
    }

    /**
     * method to tell you if a user is logged in
     * @return if user is logged in
     */
    public boolean isLoggedIn() {
        return loggedIn;
    }

    @Override
    public String toString() {
        return "User[id=" + id + ", username=" + username + "]";
    }

    public int getId() {
        return id;
    }

    /**
     * Get password of user
     * @return password string
     */
    public String getPassword() {
        return password;
    }

    /**
     * Get username of user
     * @return username string
     */
    public String getUsername() {
        return username;
    }

    /**
     * Updates a user's username
     * @param username new username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Updates a user's password
     * @param password new password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        
        return this.id == other.id && this.username == other.username;
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
