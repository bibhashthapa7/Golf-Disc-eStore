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
import java.util.HashMap;

import com.discgolf.api.discgolfapi.model.Cart;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Cart File DAO class
 * @author ZHV
 */
@Tag("Persistence-tier")
public class CartFileDAOTest {
    CartFileDAO cartFileDAO;
    Cart[] testCarts;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupCartFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testCarts = new Cart[3];
        HashMap<Integer, Integer> contents = new HashMap<>();

        testCarts[0] = new Cart(1, "test_user1", contents);
        testCarts[1] = new Cart(2, "test_user2", contents);
        testCarts[2] = new Cart(3, "test_user3", contents);

        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the cart array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Cart[].class))
                .thenReturn(testCarts);
        cartFileDAO = new CartFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetCarts() {
        // Invoke
        Cart[] carts = cartFileDAO.getCarts();

        // Analyze
        assertEquals(carts.length,testCarts.length);
        for (int i = 0; i < testCarts.length;++i)
            assertEquals(carts[i],testCarts[i]);
    }

    @Test
    public void testFindCarts() {
        // Invoke
        Cart[] carts = cartFileDAO.findCarts("test_user1");

        // Analyze
        assertEquals(1,carts.length);
        assertEquals(carts[0],testCarts[0]);
    }

    @Test
    public void testGetCart() {
        // Invoke
        Cart cart = cartFileDAO.getCart(2);

        // Analzye
        assertEquals(cart,testCarts[1]);
    }

    @Test
    public void testDeleteCart() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> cartFileDAO.deleteCart(1),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test carts array - 1 (because of the delete)
        // Because carts attribute of CartFileDAO is package private
        // we can access it directly
        assertEquals(cartFileDAO.carts.size(),testCarts.length-1);
    }

    @Test
    public void testCreateCart() {
        // Setup
        Cart cart = new Cart(4, "test_user55", new HashMap<>());

        // Invoke
        Cart result = assertDoesNotThrow(() -> cartFileDAO.createCart(cart.getUsername()),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Cart actual = cartFileDAO.getCart(cart.getId());
        assertEquals(actual.getId(),cart.getId());
        assertEquals(actual.getUsername(),cart.getUsername());
        assertEquals(actual.getContents(),cart.getContents());
    }

    @Test
    public void testUpdateCart() {
        // Setup
        Cart cart = new Cart(1, "test_user7", new HashMap<>()); // Change user (owner)

        // Invoke
        Cart result = assertDoesNotThrow(() -> cartFileDAO.updateCart(cart),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Cart actual = cartFileDAO.getCart(cart.getId());
        assertEquals(actual,cart);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Cart[].class));

        Cart cart = new Cart(99, "test_user55", new HashMap<>());

        assertThrows(IOException.class,
                        () -> cartFileDAO.createCart(cart.getUsername()),
                        "IOException not thrown");
    }

    @Test
    public void testGetCartNotFound() {
        // Invoke
        Cart cart = cartFileDAO.getCart(98);

        // Analyze
        assertEquals(cart,null);
    }

    @Test
    public void testDeleteCartNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> cartFileDAO.deleteCart(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(cartFileDAO.carts.size(),testCarts.length);
    }

    @Test
    public void testUpdateCartNotFound() {
        // Setup
        Cart cart = new Cart(101, "anon_user", new HashMap<>());

        // Invoke
        Cart result = assertDoesNotThrow(() -> cartFileDAO.updateCart(cart),
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
        // from the CartFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Cart[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new CartFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}