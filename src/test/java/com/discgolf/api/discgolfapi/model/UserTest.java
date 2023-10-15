package com.discgolf.api.discgolfapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("Model-tier")
public class UserTest {
    @Test
    public void testCtor() {
        // Setup
        int expectedId = 10;
        String expectedUsername= "joe";
        String expectedPassword = "password1234";
        
        
        // Invoke
        User user = new User(expectedId, expectedUsername, expectedPassword);
        // Analyze
        assertEquals(expectedId, user.getId());
        assertEquals(expectedUsername,user.getUsername());
        assertEquals(expectedPassword,user.getPassword());
        assertEquals(false, user.isAdmin());
    }

    @Test
    public void testName() {
        // Setup
        int id = 10;
        String username = "john";
        String password = "doe";

        User user = new User(id, username, password);

        String expectedUsername = "Jane";
        String expectedPassword = "john";

        user.setUsername(expectedUsername);
        user.setPassword(expectedPassword);

        // Analyze
        assertEquals(expectedUsername, user.getUsername());
        assertEquals(expectedPassword, user.getPassword());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String username = "john";
        String password = "doe";
        User user = new User(id, username, password);
        String expected = "User[id=" + id + ", username=" + username + "]";

        // Invoke
        String actual = user.toString();

        // Analyze
        assertEquals(expected, actual);
    }

    @Test
    public void testLoginFailed() {
        int id = 15;
        String username = "John";
        String password = "password123";

        User user = new User(id, username, password);
        boolean login = user.login("password321");
        boolean expectedLogin = false;
        
        assertEquals(expectedLogin, login);
        assertEquals(expectedLogin, user.isLoggedIn());
    }

    @Test
    public void testLoginSuccess() {
        int id = 15;
        String username = "John";
        String password = "password123";

        User user = new User(id, username, password);
        boolean login = user.login("password123");
        boolean expectedLogin = true;
        
        assertEquals(expectedLogin, login);
        assertEquals(expectedLogin, user.isLoggedIn());
    }

    @Test
    public void testAdminAccount() {
        int id = 99;
        String username = "admin";
        String password = "admin";
        User user = new User(id, username, password);
        
        boolean expectedAdmin = true;
        boolean actualAdmin = user.isAdmin();

        assertEquals(expectedAdmin, actualAdmin);
    }

    @Test
    public void testEqualsValid() {
        User user = new User(99, "test", "test");
        User user2 = new User(99, "test", "test");
        boolean actual = user.equals(user2);
        boolean expected = true;
        assertEquals(actual, expected);
    }

    @Test
    public void testEqualsNotValidType() {
        User user = new User(99, "test", "test");
        Integer test = 99;
        boolean actual = user.equals(test);
        boolean expected = false;
        assertEquals(actual, expected);
    }

    @Test
    public void testHashCode() {
        User user = new User(99, "user", "pass");
        int hash = "user".hashCode();
        
        assertEquals(user.hashCode(), hash);
    }

    @Test
    public void testEqualsNotEqual() {
        User user = new User(99, "test", "test");
        User user2 = new User(92139, "test1234", "1234test");
        boolean actual = user.equals(user2);
        boolean expected = false;
        assertEquals(actual, expected);
    }
}