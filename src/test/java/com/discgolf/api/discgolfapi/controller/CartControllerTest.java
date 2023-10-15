package com.discgolf.api.discgolfapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.discgolf.api.discgolfapi.model.Cart;
import com.discgolf.api.discgolfapi.model.Disc;
import com.discgolf.api.discgolfapi.persistence.CartDAO;
import com.discgolf.api.discgolfapi.persistence.DiscDAO;

/**
 * Test the Cart Controller class
 * 
 * @author SWEN Faculty + coolname
 */
@Tag("Controller-tier")
public class CartControllerTest {
    private CartController cartController;
    private CartDAO mockCartDAO;
    private DiscDAO mockDiscDAO;

    /**
     * Before each test, create a new CartController object and inject
     * a mock Cart DAO
     */
    @BeforeEach
    public void setupCartController() {
        mockCartDAO = mock(CartDAO.class);
        mockDiscDAO = mock(DiscDAO.class);
        cartController = new CartController(mockCartDAO, mockDiscDAO);
    }

    @Test
    public void testGetCart() throws IOException {  // getCart may throw IOException
        // Setup
        Cart cart = new Cart(0, "aiden", new HashMap<>());
        // When the same id is passed in, our mock Cart DAO will return the Cart object
        when(mockCartDAO.getCart(cart.getId())).thenReturn(cart);

        // Invoke
        ResponseEntity<Cart> response = cartController.getCart(cart.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(cart,response.getBody());
    }

    @Test
    public void testGetCartNotFound() throws Exception { // createCart may throw IOException
        // Setup
        int cartId = 99;
        // When the same id is passed in, our mock Cart DAO will return null, simulating
        // no cart found
        when(mockCartDAO.getCart(cartId)).thenReturn(null);

        // Invoke
        ResponseEntity<Cart> response = cartController.getCart(cartId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetCartHandleException() throws Exception { // createCart will throw IOException
        // Setup
        int cartId = 99;
        // When getCart is called on the Mock Cart DAO, throw an IOException
        doThrow(new IOException()).when(mockCartDAO).getCart(cartId);

        // Invoke
        ResponseEntity<Cart> response = cartController.getCart(cartId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateCart() throws IOException {  // createCart may throw IOException
        // Setup
        Cart cart = new Cart(0, "aiden", new HashMap<>());
        // when createCart is called, return true simulating successful
        // creation and save
        when(mockCartDAO.createCart("aiden")).thenReturn(cart);

        // Invoke
        ResponseEntity<Cart> response = cartController.createCart(cart.getUsername());

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(cart,response.getBody());
    }

    @Test
    public void testCreateCartFailed() throws IOException {  // createCart may throw IOException
        // Setup
        Cart cart = new Cart(0, "aiden", new HashMap<>());
        // when createCart is called, return false simulating failed
        // creation and save
        when(mockCartDAO.createCart("aiden")).thenReturn(null);

        // Invoke
        ResponseEntity<Cart> response = cartController.createCart(cart.getUsername());

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCreateCartHandleException() throws IOException {  // createCart will throw IOException
        // Setup
        Cart cart = new Cart(0, "aiden", new HashMap<>());

        // When createCart is called on the Mock Cart DAO, throw an IOException
        doThrow(new IOException()).when(mockCartDAO).createCart("aiden");

        // Invoke
        ResponseEntity<Cart> response = cartController.createCart(cart.getUsername());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateCart() throws IOException { // updateCart may throw IOException
        // Setup
        Cart cart = new Cart(0, "aiden", new HashMap<>());
        // when updateCart is called, return true simulating successful
        // update and save
        when(mockCartDAO.updateCart(cart)).thenReturn(cart);
        ResponseEntity<Cart> response = cartController.updateCart(cart);
        cart.setUsername("tim");

        // Invoke
        response = cartController.updateCart(cart);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(cart,response.getBody());
    }

    @Test
    public void testUpdateCartFailed() throws IOException { // updateCart may throw IOException
        // Setup
        Cart cart = new Cart(0, "aiden", new HashMap<>());
        // when updateCart is called, return true simulating successful
        // update and save
        when(mockCartDAO.updateCart(cart)).thenReturn(null);

        // Invoke
        ResponseEntity<Cart> response = cartController.updateCart(cart);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateCartHandleException() throws IOException { // updateCart will throw IOException
        // Setup
        Cart cart = new Cart(0, "aiden", new HashMap<>());
        // When updateCart is called on the Mock Cart DAO, throw an IOException
        doThrow(new IOException()).when(mockCartDAO).updateCart(cart);

        // Invoke
        ResponseEntity<Cart> response = cartController.updateCart(cart);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetCarts() throws IOException { // getCarts may throw IOException
        // Setup
        Cart[] carts = new Cart[2];
        carts[0] = new Cart(0, "aiden", new HashMap<>());
        carts[1] = new Cart(1, "tim", new HashMap<>());
        // When getCarts is called return the carts created above
        when(mockCartDAO.getCarts()).thenReturn(carts);

        // Invoke
        ResponseEntity<Cart[]> response = cartController.getCarts();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(carts,response.getBody());
    }

    @Test
    public void testGetCartsEmpty() throws IOException { // getCarts may throw IOException
        // Setup
        when(mockCartDAO.getCarts()).thenReturn(null);

        // Invoke
        ResponseEntity<Cart[]> response = cartController.getCarts();

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetCartsHandleException() throws IOException { // getCarts will throw IOException
        // Setup
        // When getCarts is called on the Mock Cart DAO, throw an IOException
        doThrow(new IOException()).when(mockCartDAO).getCarts();

        // Invoke
        ResponseEntity<Cart[]> response = cartController.getCarts();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testSearchCarts() throws IOException { // findCarts may throw IOException
        // Setup
        String searchUsername = "aiden";
        Cart[] carts = new Cart[2];
        carts[0] = new Cart(0, "aiden", new HashMap<>());
        carts[1] = new Cart(1, "tim", new HashMap<>());
        // When findCarts is called with the search string, return the two
        /// carts above
        when(mockCartDAO.findCarts(searchUsername)).thenReturn(carts);

        // Invoke
        ResponseEntity<Cart[]> response = cartController.searchCarts(searchUsername);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(carts,response.getBody());
    }

    @Test
    public void testSearchCartsNull() throws IOException { // findCarts may throw IOException
        // Setup
        String searchUsername = "aiden";
        
        when(mockCartDAO.findCarts(searchUsername)).thenReturn(null);

        // Invoke
        ResponseEntity<Cart[]> response = cartController.searchCarts(searchUsername);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testSearchCartsHandleException() throws IOException { // findCarts will throw IOException
        // Setup
        String searchUsername = "aiden";
        // When createCart is called on the Mock Cart DAO, throw an IOException
        doThrow(new IOException()).when(mockCartDAO).findCarts(searchUsername);

        // Invoke
        ResponseEntity<Cart[]> response = cartController.searchCarts(searchUsername);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteCart() throws IOException { // deleteCart may throw IOException
        // Setup
        int cartId = 99;
        // when deleteCart is called return true, simulating successful deletion
        when(mockCartDAO.deleteCart(cartId)).thenReturn(true);

        // Invoke
        ResponseEntity<Cart> response = cartController.deleteCart(cartId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteCartNotFound() throws IOException { // deleteCart may throw IOException
        // Setup
        int cartId = 99;
        // when deleteCart is called return false, simulating failed deletion
        when(mockCartDAO.deleteCart(cartId)).thenReturn(false);

        // Invoke
        ResponseEntity<Cart> response = cartController.deleteCart(cartId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testDeleteCartHandleException() throws IOException { // deleteCart will throw IOException
        // Setup
        int cartId = 99;
        // When deleteCart is called on the Mock Cart DAO, throw an IOException
        doThrow(new IOException()).when(mockCartDAO).deleteCart(cartId);

        // Invoke
        ResponseEntity<Cart> response = cartController.deleteCart(cartId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testAddToCart() throws IOException { // updateCart may throw IOException
        // Setup
        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, 1);

        // when addDisc is called, return true simulating successful add
        when(mockCart.addDisc(disc.getId())).thenReturn(true);
        when(mockCartDAO.updateCart(mockCart)).thenReturn(mockCart);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);

        // Invoke
        ResponseEntity<Cart> response = cartController.addToCart("aiden", disc.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(mockCart,response.getBody());
    }

    @Test
    public void testAddToCartFailed() throws IOException { // updateCart may throw IOException
        // Setup
        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, 1);

        // when addDisc is called, return false simulating failed add
        when(mockCart.addDisc(disc.getId())).thenReturn(false);
        when(mockCartDAO.updateCart(mockCart)).thenReturn(mockCart);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);

        // Invoke
        ResponseEntity<Cart> response = cartController.addToCart("aiden", disc.getId());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testAddToCartHandleException() throws IOException { // updateCart will throw IOException
        // Setup
        Cart cart = new Cart(0, "aiden", new HashMap<>());
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, 1);
        // When updateCart is called on the Mock Cart DAO, throw an IOException
        doThrow(new IOException()).when(mockCartDAO).updateCart(cart);
        when(mockCartDAO.findCart("aiden")).thenReturn(cart);

        // Invoke
        ResponseEntity<Cart> response = cartController.addToCart(cart.getUsername(), disc.getId());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testRemoveFromCart() throws IOException { // updateCart may throw IOException
        // Setup
        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, 1);
        // when removeDisc is called, return true simulating successful remove
        when(mockCart.removeDisc(disc.getId())).thenReturn(true);
        when(mockCartDAO.updateCart(mockCart)).thenReturn(mockCart);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);

        // Invoke
        ResponseEntity<Cart> response = cartController.removeFromCart("aiden", disc.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(mockCart,response.getBody());
    }

    @Test
    public void testRemoveFromCartFailed() throws IOException { // updateCart may throw IOException
        // Setup
        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, 1);
        // when removeDisc is called, return false simulating failed remove
        when(mockCart.removeDisc(disc.getId())).thenReturn(false);
        when(mockCartDAO.updateCart(mockCart)).thenReturn(mockCart);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);

        // Invoke
        ResponseEntity<Cart> response = cartController.removeFromCart("aiden", disc.getId());

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testRemoveFromCartHandleException() throws IOException { // updateCart will throw IOException
        // Setup
        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, 1);
        
        // When updateCart is called on the Mock Cart DAO, throw an IOException but pass removeDisc
        when(mockCart.removeDisc(disc.getId())).thenReturn(true);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        doThrow(new IOException()).when(mockCartDAO).updateCart(mockCart);

        // Invoke
        ResponseEntity<Cart> response = cartController.removeFromCart("aiden", disc.getId());

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testUpdateQuantityInCart() throws IOException { // updateCart may throw IOException
        // Setup
        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, 1);
        // when updateCart is called, return true simulating successful update
        for (int mode = 0 ; mode < 3 ; mode++) {
            when(mockCart.updateDiscQuantity(disc.getId(), 1, mode)).thenReturn(true);
            when(mockCartDAO.updateCart(mockCart)).thenReturn(mockCart);
            when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);

            // Invoke
            ResponseEntity<Cart> response = cartController.updateQuantityInCart("aiden", disc.getId(), 1, mode);

            // Analyze
            assertEquals(HttpStatus.OK,response.getStatusCode());
            assertEquals(mockCart,response.getBody());
        }
    }

    @Test
    public void testUpdateQuantityInCartFailed() throws IOException { // updateCart may throw IOException
        // Setup
        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, 1);
        
        for (int mode = 0 ; mode < 3 ; mode++) {
            // when updateDiscQuantity is called, return false simulating failed update
            when(mockCart.updateDiscQuantity(disc.getId(), 1, mode)).thenReturn(false);
            when(mockCartDAO.updateCart(mockCart)).thenReturn(mockCart);
            when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);

            // Invoke
            ResponseEntity<Cart> response = cartController.updateQuantityInCart("aiden", disc.getId(), 1, mode);

            // Analyze
            assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    public void testUpdateQuantityInCartHandleException() throws IOException { // updateCart will throw IOException
        // Setup
        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, 1);

        for (int mode = 0 ; mode < 3 ; mode++) {
            // When updateCart is called on the Mock Cart DAO, throw an IOException and pass updateDiscQuantity
            when(mockCart.updateDiscQuantity(disc.getId(), 1, mode)).thenReturn(true);
            when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
            doThrow(new IOException()).when(mockCartDAO).updateCart(mockCart);

            // Invoke
            ResponseEntity<Cart> response = cartController.updateQuantityInCart("aiden", disc.getId(), 1, mode);

            // Analyze
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
            assertNull(response.getBody());
        }
    }

    @Test
    public void testGetCost() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        float price = 150.0f;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", price, 1);
        contents.put(disc.getId(), 1);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Float> response = cartController.getCost("aiden");

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(response.getBody(), price);
    }

    @Test
    public void testGetCostNotFound() throws IOException { // getDisc may throw IOException
        // Setup
        // when findCart is called, return the null simulating no cart
        when(mockCartDAO.findCart("aiden")).thenReturn(null);

        // Invoke
        ResponseEntity<Float> response = cartController.getCost("aiden");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetCostHandleException() throws IOException { // getDisc will throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        float price = 150.0f;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", price, 1);
        contents.put(disc.getId(), 1);

        // When getDisc is called on the Mock Disc DAO, throw an IOException and pass getContents
        doThrow(new IOException()).when(mockDiscDAO).getDisc(disc.getId());
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);
        
        // Invoke
        ResponseEntity<Float> response = cartController.getCost("aiden");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetCount() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 5;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, 1);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Integer> response = cartController.getCount("aiden");

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(response.getBody(), pQuantity);
    }

    @Test
    public void testGetCountNotFound() throws IOException { // getDisc may throw IOException
        // Setup
        // when findCart is called, return the null simulating no cart
        when(mockCartDAO.findCart("aiden")).thenReturn(null);

        // Invoke
        ResponseEntity<Integer> response = cartController.getCount("aiden");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetCountException() throws IOException { // getDisc may throw IOException
        // Setup
        // when findCart is called, return the null simulating no cart
        doThrow(new IOException()).when(mockCartDAO).findCart("aiden");

        // Invoke
        ResponseEntity<Integer> response = cartController.getCount("aiden");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetCountHandleException() throws IOException { // getDisc will throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 5;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, 1);
        contents.put(disc.getId(), pQuantity);

        // When getDisc is called on the Mock Disc DAO, throw an IOException and pass getContents
        doThrow(new IOException()).when(mockDiscDAO).getDisc(disc.getId());
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);
        
        // Invoke
        ResponseEntity<Float> response = cartController.getCost("aiden");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCheckCartConflict() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 5;
        int iQuantity = 1;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Disc[]> response = cartController.checkCart("aiden");

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCheckCartNoConflict() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 1;
        int iQuantity = 1;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Disc[]> response = cartController.checkCart("aiden");

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCheckCartNotFound() throws IOException { // getDisc may throw IOException
        // Setup
        // when findCart is called, return the null simulating no cart
        when(mockCartDAO.findCart("aiden")).thenReturn(null);

        // Invoke
        ResponseEntity<Disc[]> response = cartController.checkCart("aiden");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCheckCartHandleException() throws IOException { // getDisc will throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 1;
        int iQuantity = 1;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // When getDisc is called on the Mock Disc DAO, throw an IOException and pass getContents
        doThrow(new IOException()).when(mockDiscDAO).getDisc(disc.getId());
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);
        
        // Invoke
        ResponseEntity<Disc[]> response = cartController.checkCart("aiden");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testPurchaseCart() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 1;
        int iQuantity = 1;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Disc[]> response = cartController.purchaseCart("aiden");

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testPurchaseCartPartialInv() throws IOException { // purchase may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 1;
        int iQuantity = 2;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Disc[]> response = cartController.purchaseCart("aiden");

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testPurchaseCartNotFound() throws IOException { // getDisc may throw IOException
        // Setup
        // when findCart is called, return the null simulating no cart
        when(mockCartDAO.findCart("aiden")).thenReturn(null);

        // Invoke
        ResponseEntity<Disc[]> response = cartController.purchaseCart("aiden");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testPurchaseCartConflict() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 1;
        int iQuantity = 1;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(0, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the null simulating failed retrieval
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(null); // no inventory
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Disc[]> response = cartController.purchaseCart("aiden");

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testPurchaseCartHandleException() throws IOException { // getDisc will throw IOException
        // Setup
        // When getDisc is called on the Mock Disc DAO, throw an IOException and pass getContents
        doThrow(new IOException()).when(mockCartDAO).findCart("aiden");
        
        // Invoke
        ResponseEntity<Disc[]> response = cartController.purchaseCart("aiden");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCheckOneDiscConflict() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 5;
        int iQuantity = 1;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(1, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Disc> response = cartController.checkOneDisc("aiden", disc.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testCheckOneDiscNull() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 5;
        int iQuantity = 1;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(1, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(null);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Disc> response = cartController.checkOneDisc("aiden", disc.getId());

        // Analyze
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    public void testCheckOneDiscNoConflict() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 1;
        int iQuantity = 1;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(1, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Disc> response = cartController.checkOneDisc("aiden", disc.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCheckOneDiscNotFound() throws IOException { // getDisc may throw IOException
        // Setup
        // when findCart is called, return the null simulating no cart
        when(mockCartDAO.findCart("aiden")).thenReturn(null);

        // Invoke
        ResponseEntity<Disc> response = cartController.checkOneDisc("aiden", 0);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCheckOneDiscHandleException() throws IOException { // getDisc will throw IOException
        // Setup
        // When getDisc is called on the Mock Disc DAO, throw an IOException and pass getContents
        doThrow(new IOException()).when(mockCartDAO).findCart("aiden");
       
        // Invoke
        ResponseEntity<Disc> response = cartController.checkOneDisc("aiden", 0);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testPurchaseOneDisc() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 1;
        int iQuantity = 1;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(1, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Disc> response = cartController.purchaseOneDisc("aiden", disc.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testPurchaseOneDiscPartialInv() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 1;
        int iQuantity = 2;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(1, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Disc> response = cartController.purchaseOneDisc("aiden", disc.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testPurchaseOneDiscNotFound() throws IOException { // getDisc may throw IOException
        // Setup
        // when findCart is called, return the null simulating no cart
        when(mockCartDAO.findCart("aiden")).thenReturn(null);

        // Invoke
        ResponseEntity<Disc> response = cartController.purchaseOneDisc("aiden", 0);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testPurchaseOneDiscConflict() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 1;
        int iQuantity = 1;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(1, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the null simulating failed retrieval
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(null); // no inventory
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Disc> response = cartController.purchaseOneDisc("aiden", disc.getId());

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testPurchaseOneDiscHandleException() throws IOException { // getDisc will throw IOException
        // Setup
        // When getDisc is called on the Mock Disc DAO, throw an IOException and pass getContents
        doThrow(new IOException()).when(mockCartDAO).findCart("aiden");
        
        // Invoke
        ResponseEntity<Disc> response = cartController.purchaseOneDisc("aiden", 0);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testGetContents() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 1;
        int iQuantity = 1;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(1, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);
        when(mockCartDAO.findCart("aiden")).thenReturn(mockCart);
        when(mockCart.getContents()).thenReturn(contents);

        // Invoke
        ResponseEntity<Disc[]> response = cartController.getContents("aiden");

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    public void testGetContentsNull() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 1;
        int iQuantity = 1;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(1, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);
        when(mockCartDAO.findCart("aiden")).thenReturn(null);

        // Invoke
        ResponseEntity<Disc[]> response = cartController.getContents("aiden");

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testGetContentsError() throws IOException { // getDisc may throw IOException
        // Setup
        HashMap<Integer, Integer> contents = new HashMap<>();
        int pQuantity = 1;
        int iQuantity = 1;

        Cart mockCart = mock(Cart.class);
        Disc disc = new Disc(1, "Blue", 160, "Distance Driver", 0.0, iQuantity);
        contents.put(disc.getId(), pQuantity);

        // when getDisc is called, return the disc simulating successful retrieval and return contents
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);

        doThrow(new IOException()).when(mockCartDAO).findCart("aiden");

        // Invoke
        ResponseEntity<Disc[]> response = cartController.getContents("aiden");

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}   