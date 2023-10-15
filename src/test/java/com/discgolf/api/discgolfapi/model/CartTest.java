package com.discgolf.api.discgolfapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Disc class
 * 
 * @author SWEN Faculty + coolname
 */
@Tag("Model-tier")
public class CartTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_username = "test_user";
        HashMap<Integer, Integer> expected_contents = new HashMap<>();
        expected_contents.put(1, 2);
        expected_contents.put(2, 1);
        
        // Invoke
        Cart cart = new Cart(expected_id, expected_username, expected_contents);

        // Analyze
        assertEquals(expected_id,cart.getId());
        assertEquals(expected_username,cart.getUsername());
        assertEquals(expected_contents,cart.getContents());
    }

    @Test
    public void testAddDisc() {
        // Setup
        int expected_id = 99;
        String expected_username = "test_user";
        Cart cart = new Cart(expected_id, expected_username, new HashMap<>());

        Disc disc = new Disc(1, "Blue", 160, "Fairway Driver", 9.99, 1);
        Disc disc2 = new Disc(2, "Red", 160, "Distance Driver", 11.99, 1);

        HashMap<Integer, Integer> expected_contents = new HashMap<>();
        expected_contents.put(1, 1);
        expected_contents.put(2, 1);

        // Invoke (low key analyze)
        assertTrue(cart.addDisc(disc.getId())); // Test adding disc
        assertTrue(cart.addDisc(disc2.getId())); // Test adding 2nd disc
        
        // Analyze
        assertEquals(expected_contents, cart.getContents());
    }

    @Test
    public void testRemoveDisc() {
        // Setup
        int expected_id = 99;
        String expected_username = "test_user";
        Disc disc = new Disc(1, "Blue", 160, "Distance Driver", 9.99, 1);
        Disc disc2 = new Disc(2, "Red", 160, "Fairway Driver", 11.99, 1);

        HashMap<Integer, Integer> expected_contents = new HashMap<>();
        expected_contents.put(disc.getId(), 1);
        expected_contents.put(disc2.getId(), 1);

        Cart cart = new Cart(expected_id, expected_username, expected_contents);

        // Invoke and Analyze
        assertEquals(2, cart.getContents().size()); // Test default

        assertTrue(cart.removeDisc(disc.getId()));
        assertEquals(1, cart.getContents().size()); // Test removing

        assertFalse(cart.removeDisc(disc.getId())); // Test removing non-existent disc
        assertEquals(1, cart.getContents().size());
    }

    @Test
    public void testUpdateDisc() {
        // Setup
        int expected_id = 99;
        String expected_username = "test_user";
        Disc disc = new Disc(1, "Blue", 160, "Fairway Driver", 9.99, 1);
        Disc disc2 = new Disc(2, "Red", 160, "Distance Driver", 11.99, 1);

        HashMap<Integer, Integer> expected_contents = new HashMap<>();
        expected_contents.put(disc.getId(), 1);

        Cart cart = new Cart(expected_id, expected_username, expected_contents);

        // Invoke and Analyze
        int expected = 1;
        assertEquals(expected, cart.getQuantity(disc)); // Test default quantity

        expected = 5;
        assertTrue(cart.updateDiscQuantity(disc.getId(), 5));
        assertEquals(expected, cart.getQuantity(disc)); // Test set quantity

        expected = 7;
        assertTrue(cart.updateDiscQuantity(disc.getId(), 2, 1));
        assertEquals(expected, cart.getQuantity(disc)); // Test adding quantity

        expected = 1;
        assertTrue(cart.updateDiscQuantity(disc.getId(), 6, 2));
        assertEquals(expected, cart.getQuantity(disc)); // Test subtracting quantity

        expected = 0;
        assertTrue(cart.updateDiscQuantity(disc.getId(), 2, 2));
        assertEquals(expected, cart.getQuantity(disc)); // Test disc removal when (quantity <= 0)

        assertFalse(cart.updateDiscQuantity(disc2.getId(), 2, 2)); // Test updating non-existent disc
    }
}