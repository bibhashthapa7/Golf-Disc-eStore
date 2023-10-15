package com.discgolf.api.discgolfapi.model;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a Cart entity
 * @author ZVH
 */
public class Cart {
    private Map<Integer, Integer> contents;
    private String username;
    private int id;

    /**
    * Create a Cart with the given contents
     * @param contents The contents of the cart: {int disc_id : int quantity} pairs
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    public Cart(@JsonProperty("id") int id, @JsonProperty("username") String username, @JsonProperty("contents") HashMap<Integer, Integer> contents) {
        this.setContents(contents);
        this.username = username; // TODO Update to user_id
        this.id = id;
    }

    /**
    * Create an empty Cart
     * 
     * {@literal @}JsonProperty is used in serialization and deserialization
     * of the JSON object to the Java object in mapping the fields.  If a field
     * is not provided in the JSON object, the Java field gets the default Java
     * value, i.e. 0 for int
     */
    // public Cart(@JsonProperty("id") int id, @JsonProperty("username") String username) {
    //     this(id, username, new HashMap<>());
    // }

    /**
     * Get the associated Cart id
     * @return The id of the Cart
     */
    public int getId() { return id; }

    /**
     * Get the associated Cart owner's username
     * @return The associated username
     */
    public String getUsername() { return username; }

    /**
     * Get the contents of the Cart
     * @return A deep-copy of contents {int disc_id : int quantity}
     */
    public HashMap<Integer, Integer> getContents() {
        HashMap<Integer, Integer> copy = new HashMap<>();
        for (Integer key : contents.keySet())
            copy.put(key, contents.get(key));

        return copy;
    }

    /**
     * Get the quantity of a Disc in the Cart
     * @param disc Disc in the Cart
     * @return The quantity of the Disc
     */
    public int getQuantity(Disc disc) {
        int disc_id = disc.getId();
        int quantity = 0;

        if (contents.containsKey(disc_id)) // Update or add disc
            quantity += contents.get(disc_id);

        return quantity;
    }

    /**
     * Sets the Cart owner's username
     * @param username The username of the Cart owner
     */
    public void setUsername(String username) { this.username = username; }

    /**
     * Sets the contents of the Cart
     * @param contents The contents to set in the Cart
     */
    public void setContents(Map<Integer, Integer> contents) {
        HashMap<Integer, Integer> newContents = new HashMap<>();

        try {
            for (Integer key : contents.keySet())
                newContents.put(key, contents.get(key));
            this.contents = newContents;
        } catch (Exception e) { /** Squash */ }
    }

    /**
     * Adds the quantity of Discs to the Cart
     * @param disc_id ID of the Disc added to the Cart
     * @param quantity The number of Discs to add
     * @return Pass/Fail
     */
    public boolean addDisc(int disc_id, int quantity) {
        boolean result = false;

        if (quantity > 0) // Validate quantity
            if (contents.containsKey(disc_id)) // Update or add disc
                contents.replace(disc_id, contents.get(disc_id) + quantity);
            else
                contents.put(disc_id, quantity);
            result = true;
        return result;
    }

    /**
     * Adds the Disc to the Cart
     * @param disc_id ID of the Disc added to the Cart
     * @return Pass/Fail
     */
    public boolean addDisc(int disc_id) {
        return this.addDisc(disc_id, 1);
    }

    /**
     * Removes the Disc from the Cart
     * @param disc_id ID of the Disc removed from the Cart
     * @return Pass/Fail
     */
    public boolean removeDisc(int disc_id) {
        boolean result = false;

        if (contents.containsKey(disc_id)) {
            contents.remove(disc_id);
            result = true;
        } return result;
    }

    /**
     * Updates the quantity of Disc in the Cart (removes if quantity <= 0)
     * @param disc_id ID of the Disc updated in the Cart
     * @param quantity The number of Discs to set/add/sub
     * @param mode {0:set, 1:add, 2:subtract} the quantity
     * @return Pass/Fail
     */
    public boolean updateDiscQuantity(int disc_id, int quantity, int mode) {
        boolean result = false;

        if (contents.containsKey(disc_id) && (mode >= 0) && (mode <= 2)) { // Disc exists and Valid mode
            quantity = (mode == 0 ? quantity : contents.get(disc_id) + (mode == 1 ? quantity : -quantity));
            if (quantity > 0) // Remove disc if quantity is invalid
                contents.replace(disc_id, quantity);
            else
                removeDisc(disc_id);
            result = true;
        } return result;
    }

    /**
     * Updates the quantity of Disc in the Cart
     * @param disc_id ID of the Disc updated in the Cart
     * @param quantity The number of Discs to set
     * @return Pass/Fail
     */
    public boolean updateDiscQuantity(int disc_id, int quantity) {
        return this.updateDiscQuantity(disc_id, quantity, 0);
    }
}
