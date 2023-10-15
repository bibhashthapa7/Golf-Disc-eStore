package com.discgolf.api.discgolfapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.discgolf.api.discgolfapi.model.User;
import com.discgolf.api.discgolfapi.persistence.UserDAO;

/**
 * Test the User Controller class
 * 
 * @author SWEN Faculty + coolname
 */
@Tag("Controller-tier")
public class UserControllerTest {
    private UserController userController;
    private UserDAO mockUserDAO;
    private User mockUser;

    /**
     * Before each test, create a new UserController object and inject
     * a mock User DAO
     */
    @BeforeEach
    public void setupUserController() {
        mockUserDAO = mock(UserDAO.class);
        userController = new UserController(mockUserDAO);

        // Simulate username and password:
        mockUser = mock(User.class);
        when(mockUser.getUsername()).thenReturn("aiden");
        when(mockUser.getPassword()).thenReturn("1234");
    }

    @Test
    public void testGetUserByID() throws IOException {  // getUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // When the same id is passed in, our mock User DAO will return the User object
        when(mockUserDAO.getUser(user.getId())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testGetUserByIDNotFound() throws Exception { // createUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // When the same id is passed in, our mock User DAO will return null, simulating
        // no user found
        when(mockUserDAO.getUser(user.getId())).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getId());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetUserByIDHandleException() throws Exception { // createUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // When getUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUser(user.getId());

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getId());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetUserByUserName() throws IOException {  // getUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // When the same id is passed in, our mock User DAO will return the User object
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testGetUserByUserNameNotFound() throws Exception { // createUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // When the same id is passed in, our mock User DAO will return null, simulating
        // no user found
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetUserByUserNameHandleException() throws Exception { // createUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // When getUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUser(user.getUsername());

        // Invoke
        ResponseEntity<User> response = userController.getUser(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetUsers() throws IOException { // getUsers may throw IOException
        // Setup
        User[] users = new User[2];
        users[0] = new User(99, "aiden", "1234");
        users[1] = new User(100, "joe", "4321");
        // When getUsers is called return the users created above
        when(mockUserDAO.getUsers()).thenReturn(users);

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(users,response.getBody());
    }

    @Test
    public void testGetUsersHandleException() throws IOException { // getUsers may throw IOException
        // Setup
        // When getUsers is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).getUsers();

        // Invoke
        ResponseEntity<User[]> response = userController.getUsers();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateUser() throws IOException {  // createUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // when createUser is called, return true simulating successful
        // creation and save
        when(mockUserDAO.createUser(user)).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testCreateUserFailed() throws IOException {  // createUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // when createUser is called, return false simulating failed
        // creation and save
        when(mockUserDAO.createUser(user)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateUserHandleException() throws IOException {  // createUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");

        // When createUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).createUser(user);

        // Invoke
        ResponseEntity<User> response = userController.createUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateUser() throws IOException { // updateUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // when updateUser is called, return true simulating successful
        // update and save
        when(mockUserDAO.updateUser(user)).thenReturn(user);
        ResponseEntity<User> response = userController.updateUser(user);
        

        // Invoke
        response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(user,response.getBody());
    }

    @Test
    public void testUpdateUserFailed() throws IOException { // updateUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // when updateUser is called, return true simulating successful
        // update and save
        when(mockUserDAO.updateUser(user)).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateUserHandleException() throws IOException { // updateUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // When updateUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).updateUser(user);

        // Invoke
        ResponseEntity<User> response = userController.updateUser(user);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteUserByID() throws IOException { // deleteUser may throw IOException
        // Setup
        int userId = 99;
        // when deleteUser is called return true, simulating successful deletion
        when(mockUserDAO.deleteUser(userId)).thenReturn(true);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteUserByIDNotFound() throws IOException { // deleteUser may throw IOException
        // Setup
        int userId = 99;
        // when deleteUser is called return false, simulating failed deletion
        when(mockUserDAO.deleteUser(userId)).thenReturn(false);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteUserByIDHandleException() throws IOException { // deleteUser may throw IOException
        // Setup
        int userId = 99;
        // When deleteUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).deleteUser(userId);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(userId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testDeleteUserByUserName() throws IOException { // deleteUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // when deleteUser is called return true, simulating successful deletion
        when(mockUserDAO.deleteUser(user.getUsername())).thenReturn(true);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteUserByUserNameNotFound() throws IOException { // deleteUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // when deleteUser is called return false, simulating failed deletion
        when(mockUserDAO.deleteUser(user.getUsername())).thenReturn(false);

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteUserByUserNameHandleException() throws IOException { // deleteUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        // When deleteUser is called on the Mock User DAO, throw an IOException
        doThrow(new IOException()).when(mockUserDAO).deleteUser(user.getUsername());

        // Invoke
        ResponseEntity<User> response = userController.deleteUser(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    
    @Test
    public void testLogin() throws IOException { // getUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");

        // when getUser is called, return user simulating successful retrieval
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.login(user.getUsername(), user.getPassword());

        // Analyze
        assertEquals(HttpStatus.ACCEPTED,response.getStatusCode());
    }

    @Test
    public void testLoginUnauthorized() throws IOException { // getUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");
        String bad_pass = "4321";

        // when getUser is called, return user simulating successful retrieval
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(user);

        // Invoke
        ResponseEntity<User> response = userController.login(user.getUsername(), bad_pass);

        // Analyze
        assertEquals(HttpStatus.UNAUTHORIZED,response.getStatusCode());
    }

    @Test
    public void testLoginNotFound() throws IOException { // getUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");

        // when getUser is called, return user simulating failed retrieval
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.login(user.getUsername(), user.getPassword());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testLogout() throws IOException { // getUser may throw IOException
        // Setup
        // when logout is called, return true simulating successful logut
        when(mockUser.logout()).thenReturn(true);

        // when getUser is called, return user simulating successful retrieval
        when(mockUserDAO.getUser(mockUser.getUsername())).thenReturn(mockUser);

        // Invoke
        ResponseEntity<User> response = userController.logout(mockUser.getUsername());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testLogoutUnauthorized() throws IOException { // getUser may throw IOException
        // Setup
        // when logout is called, return true simulating failed logut
        when(mockUser.logout()).thenReturn(false);

        // when getUser is called, return user simulating successful retrieval
        when(mockUserDAO.getUser(mockUser.getUsername())).thenReturn(mockUser);

        // Invoke
        ResponseEntity<User> response = userController.logout(mockUser.getUsername());

        // Analyze
        assertEquals(HttpStatus.UNAUTHORIZED,response.getStatusCode());
    }

    @Test
    public void testLogoutNotFound() throws IOException { // getUser may throw IOException
        // Setup
        User user = new User(99, "aiden", "1234");

        // when getUser is called, return user simulating failed retrieval
        when(mockUserDAO.getUser(user.getUsername())).thenReturn(null);

        // Invoke
        ResponseEntity<User> response = userController.logout(user.getUsername());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }
}