package com.discgolf.api.discgolfapi.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Array;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.discgolf.api.discgolfapi.model.Disc;
import com.discgolf.api.discgolfapi.persistence.DiscDAO;

/**
 * Test the Disc Controller class
 * 
 * @author SWEN Faculty + coolname
 */
@Tag("Controller-tier")
public class DiscControllerTest {
    private DiscController discController;
    private DiscDAO mockDiscDAO;

    /**
     * Before each test, create a new DiscController object and inject
     * a mock Disc DAO
     */
    @BeforeEach
    public void setupDiscController() {
        mockDiscDAO = mock(DiscDAO.class);
        discController = new DiscController(mockDiscDAO);
    }

    @Test
    public void testGetDisc() throws IOException {  // getDisc may throw IOException
        // Setup
        Disc disc = new Disc(99,"Blue",160,"Distance Driver",100,100);
        // When the same id is passed in, our mock Disc DAO will return the Disc object
        when(mockDiscDAO.getDisc(disc.getId())).thenReturn(disc);

        // Invoke
        ResponseEntity<Disc> response = discController.getDisc(disc.getId());

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(disc,response.getBody());
    }

    @Test
    public void testGetDiscNotFound() throws Exception { // createDisc may throw IOException
        // Setup
        int discId = 99;
        // When the same id is passed in, our mock Disc DAO will return null, simulating
        // no disc found
        when(mockDiscDAO.getDisc(discId)).thenReturn(null);

        // Invoke
        ResponseEntity<Disc> response = discController.getDisc(discId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testGetDiscHandleException() throws Exception { // createDisc may throw IOException
        // Setup
        int discId = 99;
        // When getDisc is called on the Mock Disc DAO, throw an IOException
        doThrow(new IOException()).when(mockDiscDAO).getDisc(discId);

        // Invoke
        ResponseEntity<Disc> response = discController.getDisc(discId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testCreateDisc() throws IOException {  // createDisc may throw IOException
        // Setup
        Disc disc = new Disc(99,"Blue",160, "Fairway Driver",100,100);
        // when createDisc is called, return true simulating successful
        // creation and save
        when(mockDiscDAO.createDisc(disc)).thenReturn(disc);

        // Invoke
        ResponseEntity<Disc> response = discController.createDisc(disc);

        // Analyze
        assertEquals(HttpStatus.CREATED,response.getStatusCode());
        assertEquals(disc,response.getBody());
    }

    @Test
    public void testCreateDiscFailed() throws IOException {  // createDisc may throw IOException
        // Setup
        Disc disc = new Disc(99,"Red",175,"Fairway Driver", 12.34, 1);
        // when createDisc is called, return false simulating failed
        // creation and save
        when(mockDiscDAO.createDisc(disc)).thenReturn(null);

        // Invoke
        ResponseEntity<Disc> response = discController.createDisc(disc);

        // Analyze
        assertEquals(HttpStatus.CONFLICT,response.getStatusCode());
    }

    @Test
    public void testCreateDiscHandleException() throws IOException {  // createDisc may throw IOException
        // Setup
        Disc disc = new Disc(99,"Green",180,"Fairway Driver", 12.34, 1);

        // When createDisc is called on the Mock Disc DAO, throw an IOException
        doThrow(new IOException()).when(mockDiscDAO).createDisc(disc);

        // Invoke
        ResponseEntity<Disc> response = discController.createDisc(disc);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testUpdateDisc() throws IOException { // updateDisc may throw IOException
        // Setup
        Disc disc = new Disc(99,"Blue",160,"Fairway Driver", 4.50, 10);
        // when updateDisc is called, return true simulating successful
        // update and save
        when(mockDiscDAO.updateDisc(disc)).thenReturn(disc);
        ResponseEntity<Disc> response = discController.updateDisc(disc);
        disc.setColor("Aqua");

        // Invoke
        response = discController.updateDisc(disc);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(disc,response.getBody());
    }

    @Test
    public void testUpdateDiscFailed() throws IOException { // updateDisc may throw IOException
        // Setup
        Disc disc = new Disc(99,"Black",200,"Fairway Driver", 100, 100);
        // when updateDisc is called, return true simulating successful
        // update and save
        when(mockDiscDAO.updateDisc(disc)).thenReturn(null);

        // Invoke
        ResponseEntity<Disc> response = discController.updateDisc(disc);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testUpdateDiscHandleException() throws IOException { // updateDisc may throw IOException
        // Setup
        Disc disc = new Disc(99,"Black",200,"Fairway Driver", 150, 250);
        // When updateDisc is called on the Mock Disc DAO, throw an IOException
        doThrow(new IOException()).when(mockDiscDAO).updateDisc(disc);

        // Invoke
        ResponseEntity<Disc> response = discController.updateDisc(disc);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetDiscs() throws IOException { // getDiscs may throw IOException
        // Setup
        Disc[] discs = new Disc[2];
        discs[0] = new Disc(99,"Aqua",160,"Distance Driver", 100,100);
        discs[1] = new Disc(100,"Green",175,"Distance Driver",100,100);
        // When getDiscs is called return the discs created above
        when(mockDiscDAO.getDiscs()).thenReturn(discs);

        // Invoke
        ResponseEntity<Disc[]> response = discController.getDiscs();

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(discs,response.getBody());
    }

    @Test
    public void testGetDiscsHandleException() throws IOException { // getDiscs may throw IOException
        // Setup
        // When getDiscs is called on the Mock Disc DAO, throw an IOException
        doThrow(new IOException()).when(mockDiscDAO).getDiscs();

        // Invoke
        ResponseEntity<Disc[]> response = discController.getDiscs();

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testGetDiscsFailed() throws IOException { // getDiscs may throw IOException
        // Setup
        Disc[] discs = new Disc[2];
        discs[0] = new Disc(99,"Aqua",160,"Distance Driver", 100,100);
        discs[1] = new Disc(100,"Green",175,"Distance Driver",100,100);
        // When getDiscs is called return the discs created above
        when(mockDiscDAO.getDiscs()).thenReturn(null);

        // Invoke
        ResponseEntity<Disc[]> response = discController.getDiscs();

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());

    }

    @Test
    public void testSearchDiscs() throws IOException { // findDiscs may throw IOException
        // Setup
        String searchType = "Fairway Driver";
        Disc[] discs = new Disc[2];
        discs[0] = new Disc(99,"Black",200,"Fairway Driver",100,100);
        discs[1] = new Disc(100,"White",200,"Fairway Driver", 100, 100);
        // When findDiscs is called with the search string, return the two
        /// discs above
        when(mockDiscDAO.findDiscs(searchType, 1)).thenReturn(discs);

        // Invoke
        ResponseEntity<Disc[]> response = discController.searchDiscs(searchType);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(discs,response.getBody());
    }

    @Test
    public void testSearchDiscsHandleException() throws IOException { // findDiscs may throw IOException
        // Setup
        String searchType = "Driver";
        // When createDisc is called on the Mock Disc DAO, throw an IOException
        doThrow(new IOException()).when(mockDiscDAO).findDiscs(searchType, 1);

        // Invoke
        ResponseEntity<Disc[]> response = discController.searchDiscs(searchType);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchDiscsFailed() throws IOException { // findDiscs may throw IOException
        // Setup
        String searchType = "Unknown driver";
        Disc[] discs = new Disc[2];
        discs[0] = new Disc(99,"Black",200,"Fairway Driver",100,100);
        discs[1] = new Disc(100,"White",200,"Fairway Driver", 100, 100);
        // When findDiscs is called with the search string, return the two
        /// discs above
        when(mockDiscDAO.findDiscs(searchType, 1)).thenReturn(null);

        // Invoke
        ResponseEntity<Disc[]> response = discController.searchDiscs(searchType);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    

    }
    @Test
    public void testDeleteDisc() throws IOException { // deleteDisc may throw IOException
        // Setup
        int discId = 99;
        // when deleteDisc is called return true, simulating successful deletion
        when(mockDiscDAO.deleteDisc(discId)).thenReturn(true);

        // Invoke
        ResponseEntity<Disc> response = discController.deleteDisc(discId);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
    }

    @Test
    public void testDeleteDiscNotFound() throws IOException { // deleteDisc may throw IOException
        // Setup
        int discId = 99;
        // when deleteDisc is called return false, simulating failed deletion
        when(mockDiscDAO.deleteDisc(discId)).thenReturn(false);

        // Invoke
        ResponseEntity<Disc> response = discController.deleteDisc(discId);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND,response.getStatusCode());
    }

    @Test
    public void testDeleteDiscHandleException() throws IOException { // deleteDisc may throw IOException
        // Setup
        int discId = 99;
        // When deleteDisc is called on the Mock Disc DAO, throw an IOException
        doThrow(new IOException()).when(mockDiscDAO).deleteDisc(discId);

        // Invoke
        ResponseEntity<Disc> response = discController.deleteDisc(discId);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,response.getStatusCode());
    }

    @Test
    public void testSearchAndFilter() throws IOException { // findDiscs may throw IOException
        // Setup
        String searchType = "Fairway Driver";
        Disc[] discs = new Disc[2];
        discs[0] = new Disc(99,"Black",200,"Fairway Driver",100,100);
        discs[1] = new Disc(100,"White",200,"Fairway Driver", 100, 100);
        // When findDiscs is called with the search string, return the two
        /// discs above
        when(mockDiscDAO.findDiscs(searchType, 0)).thenReturn(discs);


        // Invoke
        ResponseEntity<Disc[]> response = discController.searchAndFilterDiscs(searchType, 0);

        // Analyze
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(discs,response.getBody());
    }

    @Test
    public void testSearchAndFilterNotFound() throws IOException { // findDiscs may throw IOException
        // Setup
        String searchType = "asdsad";
        Disc[] discs = new Disc[2];
        discs[0] = new Disc(99,"Black",200,"Fairway Driver",100,100);
        discs[1] = new Disc(100,"White",200,"Fairway Driver", 100, 100);
        // When findDiscs is called with the search string, return the two
        /// discs above
        when(mockDiscDAO.findDiscs(searchType, 1)).thenReturn(null);


        // Invoke
        ResponseEntity<Disc[]> response = discController.searchAndFilterDiscs(searchType, 1);

        // Analyze
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testSearchAndFilterException() throws IOException { // findDiscs may throw IOException
        // Setup
        String searchType = "asdsad";
        Disc[] discs = new Disc[2];
        discs[0] = new Disc(99,"Black",200,"Fairway Driver",100,100);
        discs[1] = new Disc(100,"White",200,"Fairway Driver", 100, 100);
        // When findDiscs is called with the search string, return the two
        /// discs above
        doThrow(new IOException()).when(mockDiscDAO).findDiscs(searchType, 1);

        // Invoke
        ResponseEntity<Disc[]> response = discController.searchAndFilterDiscs(searchType, 1);

        // Analyze
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
    
}