package com.discgolf.api.discgolfapi.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * The unit test suite for the Disc class
 * 
 * @author SWEN Faculty + coolname
 */
@Tag("Model-tier")
public class DiscTest {
    @Test
    public void testCtor() {
        // Setup
        int expected_id = 99;
        String expected_color = "Blue";
        int expected_weight = 160;
        String expected_type = "Fairway Driver";
        double expectedPrice = 123;
        int expectedQuantity = 10;

        // Invoke
        Disc disc = new Disc(expected_id,expected_color,expected_weight,
                            expected_type,expectedPrice,expectedQuantity);

        // Analyze
        assertEquals(expected_id,disc.getId());
        assertEquals(expected_color,disc.getColor());
        assertEquals(expected_weight,disc.getWeight());
        assertEquals(expected_type,disc.getType());
        assertEquals(expectedPrice, disc.getPrice());
        assertEquals(expectedQuantity, disc.getQuantity());
    }

    @Test
    public void testName() {
        // Setup
        int id = 99;
        String color = "Red";
        int weight = 175;
        String type = "Distance Driver";
        double price = 5;
        int quantity = 20;

        Disc disc = new Disc(id,color,weight,type, price, quantity);

        String expected_color = "Green";
        int expected_weight = 180;
        String expected_type = "Fairway Driver";
        double expectedPrice = 50;
        int expectedQuantity = 33;
        
        // Invoke
        disc.setColor(expected_color);
        disc.setWeight(expected_weight);
        disc.setType(expected_type);
        disc.setPrice(expectedPrice);
        disc.setQuantity(expectedQuantity);
        
        // Analyze
        assertEquals(expected_color,disc.getColor());
        assertEquals(expected_weight,disc.getWeight());
        assertEquals(expected_type,disc.getType());
        assertEquals(expectedPrice, disc.getPrice());
        assertEquals(expectedQuantity, disc.getQuantity());
    }

    @Test
    public void testToString() {
        // Setup
        int id = 99;
        String color = "Red";
        int weight = 175;
        String type = "Fairway Driver";
        double price = 100;
        int quantity = 5;

        String expected_string = String.format(Disc.STRING_FORMAT,id,color,weight,type,price,quantity);
        Disc disc = new Disc(id,color,weight,type,price,quantity);

        // Invoke
        String actual_string = disc.toString();

        // Analyze
        assertEquals(expected_string,actual_string);
    }
}