package com.discgolf.api.discgolfapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.discgolf.api.discgolfapi.model.Cart;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Carts
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author ZVH
 */
@Component
public class CartFileDAO implements CartDAO {
    private static final Logger LOG = Logger.getLogger(CartFileDAO.class.getName());
    Map<Integer,Cart> carts;   // Provides a local cache of the cart objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Cart
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next id to assign to a new cart
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Cart File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public CartFileDAO(@Value("${carts.file}") String filename, ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the carts from the file
    }

    /**
     * Generates the next id for a new {@linkplain Cart cart}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Cart carts} from the tree map
     * 
     * @return  The array of {@link Cart carts}, may be empty
     */
    private Cart[] getCartsArray() { return getCartsArray(null); }

    /**
     * Generates an array of {@linkplain Cart carts} from the tree map for any
     * {@linkplain Cart carts} that contains the text specified by containsText
     * <br>
     * If username is null, the array contains all of the {@linkplain Cart carts}
     * in the tree map
     * 
     * @return  The array of {@link Cart carts}, may be empty
     */
    private Cart[] getCartsArray(String username) {
        ArrayList<Cart> cartArrayList = new ArrayList<>();

        for (Cart cart : carts.values())
            if (username == null || username.equals(cart.getUsername()))
                cartArrayList.add(cart);

        Cart[] cartArray = new Cart[cartArrayList.size()];
        cartArrayList.toArray(cartArray);
        return cartArray;
    }

    /**
     * Saves the {@linkplain Cart carts} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Cart carts} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Cart[] cartArray = getCartsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),cartArray);
        return true;
    }

    /**
     * Loads {@linkplain Cart carts} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        carts = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of carts
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Cart[] cartArray = objectMapper.readValue(new File(filename),Cart[].class);

        // Add each cart to the tree map and keep track of the greatest id
        for (Cart cart : cartArray) {
            carts.put(cart.getId(),cart);
            if (cart.getId() > nextId)
                nextId = cart.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Cart[] getCarts() {
        synchronized(carts) {
            return getCartsArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Cart findCart(String username) {
        synchronized(carts) {
            Cart[] carts = getCartsArray(username);
            
            return carts[0];
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Cart[] findCarts(String username) {
        synchronized(carts) {
            return getCartsArray(username);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Cart getCart(int id) {
        synchronized(carts) {
            if (carts.containsKey(id))
                return carts.get(id);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Cart createCart(String username) throws IOException {
        synchronized(carts) {
            // We create a new cart object because the id field is immutable
            // and we need to assign the next unique id
            for(Cart c : carts.values()) {
                if(c.getUsername().equalsIgnoreCase(username)) {
                    return null;
                }
            } 

            Cart newCart = new Cart(nextId(), username, new HashMap<>());
            carts.put(newCart.getId(),newCart);
            save(); // may throw an IOException
            return newCart;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Cart updateCart(Cart cart) throws IOException {
        synchronized(carts) {
            if (carts.containsKey(cart.getId()) == false)
                return null;  // cart does not exist

            carts.put(cart.getId(),cart);
            save(); // may throw an IOException
            return cart;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteCart(int id) throws IOException {
        synchronized(carts) {
            if (carts.containsKey(id)) {
                carts.remove(id);
                return save();
            } else
                return false;
        }
    }
}
