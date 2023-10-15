package com.discgolf.api.discgolfapi.model;

import java.util.logging.Logger;

import com.fasterxml.jackson.annotation.JsonProperty;
/**
 * Represents a Hero entity
 * 
 * @author SWEN Faculty + ZVH
 */
public class Disc {
    private static final Logger LOG = Logger.getLogger(Disc.class.getName());

    // Package private for tests
    public static final String STRING_FORMAT = "Disc [id=%d, color=%s, weight=%d, type=%s, price=%g, quantity=%d]";

    @JsonProperty("id") private int id;
    @JsonProperty("color") private String color;
    @JsonProperty("weight") private int weight;
    @JsonProperty("type") private String type;
    @JsonProperty("price") double price; 
    @JsonProperty("quantity") int quantity;

    /**
     * Create a disc with the given id, color, weight, and type
     * @param id The id of the disc
     * @param color The color of the disc
     * @param weight The weight of the disc
     * @param type The type of the disc
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Disc(@JsonProperty("id") int id, @JsonProperty("color") String color, 
                @JsonProperty("weight") int weight, @JsonProperty("type") String type,
                @JsonProperty("price") double price, @JsonProperty("quantity") int quantity) {
        this.id = id;
        this.color = color;
        this.weight = weight;
        this.type = type;
        this.price = price;
        this.quantity = quantity;
    }

    /**
     * Retrieves the id of the disc
     * @return The id of the disc
     */
    public int getId() {return id;}

    /**
     * Sets the color of the disc - necessary for JSON object to Java object deserialization
     * @param color The color of the disc
     */
    public void setColor(String color) {this.color = color;}

    /**
     * Sets the weight of the disc - necessary for JSON object to Java object deserialization
     * @param weight The weight of the disc
     */
    public void setWeight(int weight) {this.weight = weight;}

    /**
     * Sets the type of the disc - necessary for JSON object to Java object deserialization
     * @param type The type of the disc
     */
    public void setType(String type) {this.type = type;}

    /**
     * Retrieves the color of the disc
     * @return The color of the disc
     */
    public String getColor() {return this.color;}

    /**
     * Retrieves the weight of the disc
     * @return The weight of the disc
     */
    public int getWeight() {return this.weight;}

    /**
     * Retrieves the type of the disc
     * @return The type of the disc
     */
    public String getType() {return this.type;}

    /**
     * retrieves the price of the disc
     * @return price of disc
     */
    public double getPrice() { return this.price; }

    /**
     * retrieves quantity of disc in inv
     * @return quantity in inventory
     */
    public int getQuantity() { return this.quantity; }

    /**
     * update the price of the disc
     * @param price the new price of disc
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * update the quantity of discs in inv
     * @param quantity new quantity of discs in inv
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return String.format(STRING_FORMAT,id,color,weight,type,price,quantity);
    }
}