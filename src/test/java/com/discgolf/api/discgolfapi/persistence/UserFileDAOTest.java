package com.discgolf.api.discgolfapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.discgolf.api.discgolfapi.model.Disc;
import com.discgolf.api.discgolfapi.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UserFileDAOTest {
    UserFileDAO userFileDAO;
    User[] testUsers;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupDiscFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testUsers = new User[3];
        testUsers[0] = new User(99,"admin","supersecureadmin");
        testUsers[1] = new User(100,"johndoe","DoeMan123");
        testUsers[2] = new User(101,"janedoe", "JaneDoe12");

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the disc array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),User[].class))
                .thenReturn(testUsers);
        userFileDAO = new UserFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetUsers() {
        // Invoke
        User[] users = userFileDAO.getUsers();

        // Analyze
        assertEquals(users.length, testUsers.length);
        for (int i = 0; i < testUsers.length;++i)
            assertEquals(users[i],testUsers[i]);
    }

    @Test
    public void testGetUserById() {
        // Invoke
        User user = userFileDAO.getUser(99);

        // Analzye
        assertEquals(user, testUsers[0]);
    }

    @Test
    public void testGetUserByUsername() {
        // Invoke
        User user = userFileDAO.getUser("admin");

        // Analzye
        assertEquals(user, testUsers[0]);
    }

    @Test
    public void testDeleteUserById() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(99),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test discs array - 1 (because of the delete)
        // Because discs attribute of DiscFileDAO is package private
        // we can access it directly
        assertEquals(userFileDAO.users.size(), testUsers.length-1);
    }


    @Test
    public void testDeleteUserByUsername() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser("admin"),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test discs array - 1 (because of the delete)
        // Because discs attribute of DiscFileDAO is package private
        // we can access it directly
        assertEquals(userFileDAO.users.size(), testUsers.length-1);
    }

    @Test
    public void testCreateUsers() {
        // Setup
        User user = new User(102,"kyle","kyle123");

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.createUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = userFileDAO.getUser(user.getId());
        assertEquals(actual.getUsername(), user.getUsername());
        assertEquals(actual.getPassword(), user.getPassword());
    }

    @Test
    public void testUpdateUser() {
        // Setup
        User user = new User(100,"johndoe12","DoeMan123");

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        User actual = userFileDAO.getUser(user.getId());
        assertEquals(actual, user);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(User[].class));

        User user = new User(102,"Grey","white");

        assertThrows(IOException.class,
                        () -> userFileDAO.createUser(user),
                        "IOException not thrown");
    }

    @Test
    public void testGetUserNotFoundById() {
        // Invoke
        User user = userFileDAO.getUser(98);

        // Analyze
        assertEquals(user, null);
    }


    @Test
    public void testGetUserNotFoundByUsername() {
        // Invoke
        User user = userFileDAO.getUser("joeschmoe");

        // Analyze
        assertEquals(user, null);
    }

    @Test
    public void testDeleteUserNotFoundId() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(userFileDAO.users.size(), testUsers.length);
    }

    @Test
    public void testDeleteUserNotFoundUsername() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> userFileDAO.deleteUser("kyle"),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(userFileDAO.users.size(), testUsers.length);
    }

    @Test
    public void testUpdateUserNotFound() {
        // Setup
        User user = new User(98,"koolaidman", "ohyeah!");

        // Invoke
        User result = assertDoesNotThrow(() -> userFileDAO.updateUser(user),
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
        // from the DiscFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Disc[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new DiscFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }    
}
