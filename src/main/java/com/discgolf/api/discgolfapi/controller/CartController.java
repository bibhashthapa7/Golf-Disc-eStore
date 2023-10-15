package com.discgolf.api.discgolfapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.discgolf.api.discgolfapi.model.Cart;
import com.discgolf.api.discgolfapi.model.Disc;
import com.discgolf.api.discgolfapi.persistence.CartDAO;
import com.discgolf.api.discgolfapi.persistence.DiscDAO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for the Cart resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author ZVH
 */
@RestController
@RequestMapping("carts")
public class CartController {
    private static final Logger LOG = Logger.getLogger(CartController.class.getName());
    private CartDAO cartDao;
    private DiscDAO discDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param cartDao The {@link CartDAO Cart Data Access Object} to perform CRUD operations
     * @param discDao The {@link DiscDAO Disc Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public CartController(CartDAO cartDao, DiscDAO discDao) {
        this.cartDao = cartDao;
        this.discDao = discDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Cart cart} for the given id
     * 
     * @param id The id used to locate the {@link Cart cart}
     * 
     * @return ResponseEntity with {@link Cart cart} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Cart> getCart(@PathVariable int id) {
        LOG.info("GET /carts/" + id);
        try {
            Cart cart = cartDao.getCart(id);
            if (cart != null)
                return new ResponseEntity<Cart>(cart,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Cart carts}
     * 
     * @return ResponseEntity with array of {@link Cart cart} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Cart[]> getCarts() {
        LOG.info("GET /carts");
        try {
            Cart[] carts = cartDao.getCarts();
            if (carts != null)
                return new ResponseEntity<Cart[]>(carts, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Cart carts} which has the same type
     * 
     * @param username The username parameter which contains the str_type used to find the {@link Cart carts}
     * 
     * @return ResponseEntity with array of {@link Cart cart} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all carts that have the username
     * GET http://localhost:8080/carts/?username="john_doe"
     */
    @GetMapping("/")
    public ResponseEntity<Cart[]> searchCarts(@RequestParam String username) {
        LOG.info("GET /carts/?username=" + username);
        try {
            Cart[] carts = cartDao.findCarts(username);
            if (carts != null)
                return new ResponseEntity<Cart[]>(carts, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{username}/contents")
    public ResponseEntity<Disc[]> getContents(@PathVariable String username) {
        LOG.info("GET /carts/" + username + "/contents");
        try {
            Cart cart = cartDao.findCart(username);
            ArrayList<Disc> discs = new ArrayList<>();

            if (cart != null) {
                Map<Integer, Integer> contents = cart.getContents();
                for (int disc_id : contents.keySet()) {
                    Disc disc = discDao.getDisc(disc_id);
                    if (disc != null) // Give cart quantity not inventory's
                        discs.add(new Disc(disc_id, disc.getColor(), disc.getWeight(), disc.getType(), disc.getPrice(), contents.get(disc_id)));
                }

                Disc[] discArray = new Disc[discs.size()];
                discs.toArray(discArray);

                return new ResponseEntity<Disc[]>(discArray, HttpStatus.OK);
            } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Cart cart} with the provided cart object
     * 
     * @param username - The username associated with the {@link Cart cart} to create
     * 
     * @return ResponseEntity with created {@link Cart cart} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Cart cart} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Cart> createCart(@RequestBody String username) {
        LOG.info("POST /carts " + username);
        try {
            Cart newCart = cartDao.createCart(username);
            if (newCart != null)
                return new ResponseEntity<Cart>(newCart, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Cart cart} with the provided {@linkplain Cart cart} object, if it exists
     * 
     * @param cart The {@link Cart cart} to update
     * 
     * @return ResponseEntity with updated {@link Cart cart} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Cart> updateCart(@RequestBody Cart cart) {
        LOG.info("PUT /carts " + cart);
        try {
            Cart updatedCart = cartDao.updateCart(cart);
            if (updatedCart != null)
                return new ResponseEntity<Cart>(updatedCart, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Cart cart} with the given id
     * 
     * @param id The id of the {@link Cart cart} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Cart> deleteCart(@PathVariable int id) {
        LOG.info("DELETE /carts/" + id);

        // Replace below with "your" implementation
        try {
            boolean deleted = cartDao.deleteCart(id);
            if (deleted)
                return new ResponseEntity<>(HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Adds a {@linkplain Disc disc} to the {@linkplain Cart cart} with the provided {@linkplain Cart cart} and {@linkplain Disc disc} objects, if they exist
     * 
     * @param cart_username The username associated with the {@link Cart cart} to update
     * @param disc_id The ID of the {@linkplain Disc disc} to add to the {@link Cart cart}
     * 
     * @return ResponseEntity with updated {@link Cart cart} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/addDisc/{cart_username}/{disc_id}")
    public ResponseEntity<Cart> addToCart(@PathVariable String cart_username, @PathVariable int disc_id) {
        LOG.info("PUT /carts/addDisc " + cart_username + " +" + disc_id);
        try {
            Cart cart = cartDao.findCart(cart_username);
            if (cart != null) {
                boolean result = cart.addDisc(disc_id);
                if (result) { // ensure disc was added to cart
                    Cart updatedCart = cartDao.updateCart(cart);
                    if (updatedCart != null)
                        return new ResponseEntity<Cart>(updatedCart, HttpStatus.OK);
                } 
            } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Removes a {@linkplain Disc disc} from the {@linkplain Cart cart} with the provided {@linkplain Cart cart} and {@linkplain Disc disc} objects, if they exist
     * 
     * @param cart_username The username associated with the {@link Cart cart} to update
     * @param disc_id The ID of the {@linkplain Disc disc} to add to the {@link Cart cart}
     * 
     * @return ResponseEntity with updated {@link Cart cart} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/removeDisc/{cart_username}/{disc_id}")
    public ResponseEntity<Cart> removeFromCart(@PathVariable String cart_username, @PathVariable int disc_id) {
        LOG.info("PUT /carts/removeDisc/" + cart_username + "/" + disc_id);
        try {
            Cart cart = cartDao.findCart(cart_username);
            if (cart != null) {
                boolean result = cart.removeDisc(disc_id);
                if (result) { // ensure disc was added to cart
                    Cart updatedCart = cartDao.updateCart(cart);
                    if (updatedCart != null)
                        return new ResponseEntity<Cart>(updatedCart, HttpStatus.OK);
                }
            } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the quantity of a {@linkplain Disc disc} in the {@linkplain Cart cart} with the provided {@linkplain Cart cart} and {@linkplain Disc disc} objects, if they exist
     * 
     * @param cart_username The username associated with the {@link Cart cart} to update
     * @param disc_id The ID of the {@linkplain Disc disc} to add to the {@link Cart cart}
     * @param amount The amount to set/add/sub the {@linkplain Disc disc} quantity in the {@link Cart cart}
     * @param mode {0:set, 1:add, 2:subtract} the quantity
     * 
     * @return ResponseEntity with updated {@link Cart cart} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/updateDiscQuantity/{cart_username}/{disc_id}/{amount}/{mode}")
    public ResponseEntity<Cart> updateQuantityInCart(@PathVariable String cart_username, @PathVariable int disc_id, @PathVariable int amount, @PathVariable int mode) {
        LOG.info("PUT /carts/updateDiscQuantity/" + cart_username + "/" + disc_id + "/" + amount + "/" + mode);
        try {
            Cart cart = cartDao.findCart(cart_username);
            if (cart != null) {
                boolean result = cart.updateDiscQuantity(disc_id, amount, mode);

                if (result) { // ensure disc was updated in cart
                    Cart updatedCart = cartDao.updateCart(cart);
                    if (updatedCart != null)
                        return new ResponseEntity<Cart>(updatedCart, HttpStatus.OK);
                }
            } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the total cost of {@linkplain Disc disc's} in the {@linkplain Cart cart} with the provided cart_username
     * 
     * @param cart_username The username associated with the {@link Cart cart}
     * 
     * @return ResponseEntity with {@link Float cost} and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/getCost/{cart_username}")
    public ResponseEntity<Float> getCost(@PathVariable String cart_username) {
        LOG.info("PUT /carts/getCost/" + cart_username);
        try {
            Cart cart = cartDao.findCart(cart_username);
            if (cart != null) {
                HashMap<Integer, Integer> contents = cart.getContents();
                float cost = 0.0f;

                if (contents != null && contents.size() > 0) {
                    for (int disc_id : contents.keySet()) {
                        Disc disc = discDao.getDisc(disc_id);
                        if (disc != null)
                            cost += disc.getPrice() * contents.get(disc_id);
                    }
                } return new ResponseEntity<Float>(cost, HttpStatus.OK);

            } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Gets the total count of {@linkplain Disc disc's} in the {@linkplain Cart cart} with the provided cart_username
     * 
     * @param cart_username The username associated with the {@link Cart cart}
     * 
     * @return ResponseEntity with {@link Integer count} and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/getCount/{cart_username}")
    public ResponseEntity<Integer> getCount(@PathVariable String cart_username) {
        LOG.info("PUT /carts/getCount/" + cart_username);
        try {
            Cart cart = cartDao.findCart(cart_username);
            if (cart != null) {
                HashMap<Integer, Integer> contents = cart.getContents();
                int count = 0;

                if (contents != null && contents.size() > 0) {
                    for (int quantity : contents.values())
                        count += quantity;

                } return new ResponseEntity<Integer>(count, HttpStatus.OK);

            } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Checks for conflicts in the {@linkplain Cart cart} with the provided cart_username
     * 
     * @param cart_username The username associated with the {@link Cart cart}
     * 
     * @return ResponseEntity with purchases {@link Cart cart} object and HTTP status of OK if discs could be purchased<br>
     * ResponseEntity with HTTP status of CONFLICT if no discs could be purchased<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/checkCart/{cart_username}")
    public ResponseEntity<Disc[]> checkCart(@PathVariable String cart_username) {
        LOG.info("PUT /carts/checkCart/" + cart_username);
        try {
            Cart cart = cartDao.findCart(cart_username);
            if (cart != null) {
                HashMap<Integer, Integer> contents = cart.getContents();
                ArrayList<Disc> conflicts = new ArrayList<>();

                if (contents != null && contents.size() > 0) {
                    for (int disc_id : contents.keySet()) {
                        Disc disc = discDao.getDisc(disc_id);

                        if (disc != null) {
                            // Get inventory and purchase quantities:
                            int iQuantity = disc.getQuantity();
                            int pQuantity = contents.get(disc_id);

                            if (iQuantity < pQuantity) // Trying to purchase more than available
                                conflicts.add(new Disc(disc.getId(), disc.getColor(), disc.getWeight(), disc.getType(), disc.getPrice(), iQuantity));
                        }
                    }

                    Disc[] conflictingDiscs = new Disc[conflicts.size()];
                    conflicts.toArray(conflictingDiscs);
                    
                    return new ResponseEntity<Disc[]>(conflictingDiscs, HttpStatus.OK);
                }
            } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Purchases {@linkplain Disc disc}'s in the {@linkplain Cart cart} with the provided {@linkplain Cart cart} object, if it exists
     * 
     * @param cart_username The username associated with the {@link Cart cart} to update
     * 
     * @return ResponseEntity with purchases {@link Cart cart} object and HTTP status of OK if discs could be purchased<br>
     * ResponseEntity with HTTP status of CONFLICT if no discs could be purchased<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/purchase/{cart_username}")
    public ResponseEntity<Disc[]> purchaseCart(@PathVariable String cart_username) {
        LOG.info("PUT /carts/purchase/" + cart_username);
        try {
            Cart cart = cartDao.findCart(cart_username);
            if (cart != null) {
                HashMap<Integer, Integer> contents = cart.getContents();
                ArrayList<Disc> purchases = new ArrayList<>();
                int unpurchasable = 0;

                if (contents != null && contents.size() > 0) {
                    for (int disc_id : contents.keySet()) {
                        Disc disc = discDao.getDisc(disc_id);

                        if (disc != null) {
                            // Get inventory and purchase quantities:
                            int iQuantity = disc.getQuantity();
                            int pQuantity = Math.min(contents.get(disc_id), iQuantity);

                            purchases.add(new Disc(disc.getId(), disc.getColor(), disc.getWeight(), disc.getType(), disc.getPrice(), pQuantity)); // Store purchase
                            cart.removeDisc(disc_id); // Update cart
                            
                            if (iQuantity == pQuantity) // Delete disc if bought out
                                discDao.deleteDisc(disc_id);
                            else // Otherwise update inventory
                                discDao.updateDisc(new Disc(disc.getId(), disc.getColor(), disc.getWeight(), disc.getType(), disc.getPrice(), iQuantity - pQuantity));
                            
                        } else unpurchasable++;
                    } cartDao.updateCart(cart);
                    
                    if (contents.size() == unpurchasable) // If no discs could be purchased
                        return new ResponseEntity<>(HttpStatus.CONFLICT);

                    Disc[] purchasedDiscs = new Disc[purchases.size()];
                    purchases.toArray(purchasedDiscs);
                    return new ResponseEntity<Disc[]>(purchasedDiscs, HttpStatus.OK);
                }
            } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Checks a {@linkplain Disc disc} in the {@linkplain Cart cart} with the provided username and disc_id
     * 
     * @param cart_username The username associated with the {@link Cart cart} to update
     * @param disc_id The ID of the {@linkplain Disc disc} to purchase in the {@link Cart cart}
     * 
     * @return ResponseEntity with purchases {@link Cart cart} object and HTTP status of OK if discs could be purchased<br>
     * ResponseEntity with HTTP status of CONFLICT if no discs could be purchased<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/checkOne/{cart_username}/{disc_id}")
    public ResponseEntity<Disc> checkOneDisc(@PathVariable String cart_username, @PathVariable int disc_id) {
        LOG.info("PUT /carts/checkOne/" + cart_username + "/" + disc_id);
        try {
            Cart cart = cartDao.findCart(cart_username);
            if (cart != null) {
                Disc disc = discDao.getDisc(disc_id);

                if (disc != null) {
                    int iQuantity = disc.getQuantity();
                    int pQuantity = cart.getContents().get(disc_id);

                    if (iQuantity < pQuantity)
                        return new ResponseEntity<Disc>(new Disc(disc.getId(), disc.getColor(), disc.getWeight(), disc.getType(), disc.getPrice(), iQuantity), HttpStatus.OK);
                    return new ResponseEntity<>(HttpStatus.OK);

                } else return new ResponseEntity<>(HttpStatus.CONFLICT);

            } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Purchases a {@linkplain Disc disc} in the {@linkplain Cart cart} with the provided username and disc_id
     * 
     * @param cart_username The username associated with the {@link Cart cart} to update
     * @param disc_id The ID of the {@linkplain Disc disc} to purchase in the {@link Cart cart}
     * 
     * @return ResponseEntity with purchases {@link Cart cart} object and HTTP status of OK if discs could be purchased<br>
     * ResponseEntity with HTTP status of CONFLICT if no discs could be purchased<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("/purchaseOne/{cart_username}/{disc_id}")
    public ResponseEntity<Disc> purchaseOneDisc(@PathVariable String cart_username, @PathVariable int disc_id) {
        LOG.info("PUT /carts/purchaseOne/" + cart_username + "/" + disc_id);
        try {
            Cart cart = cartDao.findCart(cart_username);
            if (cart != null) {
                Disc disc = discDao.getDisc(disc_id);

                if (disc != null) {
                    int iQuantity = disc.getQuantity();
                    int pQuantity = Math.min(cart.getContents().get(disc_id), iQuantity);
                    Disc updatedDisc = new Disc(disc.getId(), disc.getColor(), disc.getWeight(), disc.getType(), disc.getPrice(), iQuantity - pQuantity);

                    if (iQuantity == pQuantity) // Delete disc if bought out
                        discDao.deleteDisc(disc_id);
                    else // Otherwise update inventory
                        discDao.updateDisc(updatedDisc); // Difference in quantity for inventory

                    cart.removeDisc(disc_id); // Update cart
                    cartDao.updateCart(cart); // Update DAO

                    updatedDisc.setQuantity(pQuantity); // Quantity purchased for return
                    return new ResponseEntity<Disc>(updatedDisc, HttpStatus.OK);

                } else return new ResponseEntity<>(HttpStatus.CONFLICT);

            } return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}