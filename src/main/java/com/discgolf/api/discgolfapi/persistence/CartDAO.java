package com.discgolf.api.discgolfapi.persistence;

import java.io.IOException;
import com.discgolf.api.discgolfapi.model.Cart;

/**
 * Defines the interface for Cart object persistence
 * @author ZVH
 */
public interface CartDAO {
    /**
     * Retrieves all {@linkplain Cart carts}
     * 
     * @return An array of {@link Cart cart} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Cart[] getCarts() throws IOException;

    /**
     * Finds all {@linkplain Cart carts} with a matching owner's username
     * 
     * @param username The Cart owner's username to match
     * 
     * @return An array of {@link Cart carts} that match the given username, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Cart[] findCarts(String username) throws IOException;

    /**
     * Finds one {@linkplain Cart cart} with a matching owner's username
     * 
     * @param username The Cart owner's username to match
     * 
     * @return {@link Cart cart} that match the given username, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Cart findCart(String username) throws IOException;

    /**
     * Retrieves a {@linkplain Cart cart} with the given id
     * 
     * @param id The id of the {@link Cart cart} to get
     * 
     * @return a {@link Cart cart} object with the matching id
     * <br>
     * null if no {@link Cart cart} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Cart getCart(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Cart cart}
     * 
     * @param username The username associated with the {@linkplain Cart cart} object to be created and saved
     * <br>
     * The id of the cart object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Cart cart} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Cart createCart(String username) throws IOException;

    /**
     * Updates and saves a {@linkplain Cart cart}
     * 
     * @param {@link Cart cart} object to be updated and saved
     * 
     * @return updated {@link Cart cart} if successful, null if
     * {@link Cart cart} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Cart updateCart(Cart cart) throws IOException;

    /**
     * Deletes a {@linkplain Cart cart} with the given id
     * 
     * @param id The id of the {@link Cart cart}
     * 
     * @return true if the {@link Cart cart} was deleted
     * <br>
     * false if cart with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteCart(int id) throws IOException;
}
