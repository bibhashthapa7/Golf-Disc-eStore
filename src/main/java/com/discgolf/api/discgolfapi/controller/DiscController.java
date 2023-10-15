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

import com.discgolf.api.discgolfapi.model.Disc;
import com.discgolf.api.discgolfapi.persistence.DiscDAO;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles the REST API requests for the Disc resource
 * <p>
 * {@literal @}RestController Spring annotation identifies this class as a REST API
 * method handler to the Spring framework
 * 
 * @author SWEN Faculty + coolname
 */

@RestController
@RequestMapping("discs")
public class DiscController {
    private static final Logger LOG = Logger.getLogger(DiscController.class.getName());
    private DiscDAO discDao;

    /**
     * Creates a REST API controller to reponds to requests
     * 
     * @param discDao The {@link DiscDAO Disc Data Access Object} to perform CRUD operations
     * <br>
     * This dependency is injected by the Spring Framework
     */
    public DiscController(DiscDAO discDao) {
        this.discDao = discDao;
    }

    /**
     * Responds to the GET request for a {@linkplain Disc disc} for the given id
     * 
     * @param id The id used to locate the {@link Disc disc}
     * 
     * @return ResponseEntity with {@link Disc disc} object and HTTP status of OK if found<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<Disc> getDisc(@PathVariable int id) {
        LOG.info("GET /discs/" + id);
        try {
            Disc disc = discDao.getDisc(id);
            if (disc != null)
                return new ResponseEntity<Disc>(disc,HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Disc discs}
     * 
     * @return ResponseEntity with array of {@link Disc disc} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @GetMapping("")
    public ResponseEntity<Disc[]> getDiscs() {
        LOG.info("GET /discs");

        // Replace below with "your" implementation
        try {
            Disc[] discs = discDao.getDiscs();
            if (discs != null)
                return new ResponseEntity<Disc[]>(discs, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Disc discs} which has the same type
     * 
     * @param type The type parameter which contains the int_type used to find the {@link Disc discs}
     * 
     * @return ResponseEntity with array of {@link Disc disc} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     * Example: Find all discs that have the type 1
     * GET http://localhost:8080/discs/?type=1
     */
    @GetMapping("/")
    public ResponseEntity<Disc[]> searchDiscs(@RequestParam String type) {
        LOG.info("GET /discs/?type="+type);

        // Replace below with "your" implementation
        try {
            Disc[] discs = discDao.findDiscs(type, 1);
            if (discs != null)
                return new ResponseEntity<Disc[]>(discs, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Responds to the GET request for all {@linkplain Disc discs} filtered by the given mode
     * 
     * @param search The type parameter which contains the int_type used to find the {@link Disc discs}
     * @param mode The filter mode {0:All, 1:Type, 2:Color, 3:Weight, 4:Price, Default:Price}
     * 
     * @return ResponseEntity with array of {@link Disc disc} objects (may be empty) and
     * HTTP status of OK<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     * <p>
     */
    @GetMapping("/filter")
    public ResponseEntity<Disc[]> searchAndFilterDiscs(@RequestParam String search, @RequestParam int mode) {
        LOG.info("GET /discs/?search=" + search + ", mode=" + mode);

        // Replace below with "your" implementation
        try {
            Disc[] discs = discDao.findDiscs(search, mode);
            if (discs != null)
                return new ResponseEntity<Disc[]>(discs, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Creates a {@linkplain Disc disc} with the provided disc object
     * 
     * @param disc - The {@link Disc disc} to create
     * 
     * @return ResponseEntity with created {@link Disc disc} object and HTTP status of CREATED<br>
     * ResponseEntity with HTTP status of CONFLICT if {@link Disc disc} object already exists<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PostMapping("")
    public ResponseEntity<Disc> createDisc(@RequestBody Disc disc) {
        LOG.info("POST /discs " + disc);

        // Replace below with "your" implementation
        try {
            Disc newDisc = discDao.createDisc(disc);
            if (newDisc != null)
                return new ResponseEntity<Disc>(newDisc, HttpStatus.CREATED);
            else
                return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Updates the {@linkplain Disc disc} with the provided {@linkplain Disc disc} object, if it exists
     * 
     * @param disc The {@link Disc disc} to update
     * 
     * @return ResponseEntity with updated {@link Disc disc} object and HTTP status of OK if updated<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @PutMapping("")
    public ResponseEntity<Disc> updateDisc(@RequestBody Disc disc) {
        LOG.info("PUT /discs " + disc);

        // Replace below with "your" implementation
        try {
            Disc updatedDisc = discDao.updateDisc(disc);
            if (updatedDisc != null)
                return new ResponseEntity<Disc>(updatedDisc, HttpStatus.OK);
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch(IOException e) {
            LOG.log(Level.SEVERE,e.getLocalizedMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Deletes a {@linkplain Disc disc} with the given id
     * 
     * @param id The id of the {@link Disc disc} to deleted
     * 
     * @return ResponseEntity HTTP status of OK if deleted<br>
     * ResponseEntity with HTTP status of NOT_FOUND if not found<br>
     * ResponseEntity with HTTP status of INTERNAL_SERVER_ERROR otherwise
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Disc> deleteDisc(@PathVariable int id) {
        LOG.info("DELETE /discs/" + id);

        // Replace below with "your" implementation
        try {
            boolean deleted = discDao.deleteDisc(id);
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
}