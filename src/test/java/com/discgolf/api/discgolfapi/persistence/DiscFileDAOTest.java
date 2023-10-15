package com.discgolf.api.discgolfapi.persistence;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import com.discgolf.api.discgolfapi.model.Disc;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Test the Disc File DAO class
 * 
 * @author SWEN Faculty + coolname
 */
@Tag("Persistence-tier")
public class DiscFileDAOTest {
    DiscFileDAO discFileDAO;
    Disc[] testDiscs;
    ObjectMapper mockObjectMapper;

    /**
     * Before each test, we will create and inject a Mock Object Mapper to
     * isolate the tests from the underlying file
     * @throws IOException
     */
    @BeforeEach
    public void setupDiscFileDAO() throws IOException {
        mockObjectMapper = mock(ObjectMapper.class);
        testDiscs = new Disc[3];
        testDiscs[0] = new Disc(99,"Blue",160,"Fairway Driver",30,20);
        testDiscs[1] = new Disc(100,"Green",175,"Distance Driver",30,20);
        testDiscs[2] = new Disc(101,"Red",180,"Putter",30,20);


        // When the object mapper is supposed to read from the file
        // the mock object mapper will return the disc array above
        when(mockObjectMapper
            .readValue(new File("doesnt_matter.txt"),Disc[].class))
                .thenReturn(testDiscs);
        discFileDAO = new DiscFileDAO("doesnt_matter.txt",mockObjectMapper);
    }

    @Test
    public void testGetDiscs() {
        // Invoke
        Disc[] discs = discFileDAO.getDiscs();

        // Analyze
        assertEquals(discs.length,testDiscs.length);
        for (int i = 0; i < testDiscs.length;++i)
            assertEquals(discs[i],testDiscs[i]);
    }

    @Test
    public void testFindDiscs() {
        // Invoke
        Disc[] discs = discFileDAO.findDiscs("Putter", 1);

        // Analyze
        assertEquals(discs.length,1);
        assertEquals(discs[0],testDiscs[2]);
    }

    @Test
    public void testGetDisc() {
        // Invoke
        Disc disc = discFileDAO.getDisc(99);

        // Analzye
        assertEquals(disc,testDiscs[0]);
    }

    @Test
    public void testDeleteDisc() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> discFileDAO.deleteDisc(99),
                            "Unexpected exception thrown");

        // Analzye
        assertEquals(result,true);
        // We check the internal tree map size against the length
        // of the test discs array - 1 (because of the delete)
        // Because discs attribute of DiscFileDAO is package private
        // we can access it directly
        assertEquals(discFileDAO.discs.size(),testDiscs.length-1);
    }

    @Test
    public void testCreateDisc() {
        // Setup
        Disc disc = new Disc(102,"Grey",170,"Fairway Driver",100,150);

        // Invoke
        Disc result = assertDoesNotThrow(() -> discFileDAO.createDisc(disc),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Disc actual = discFileDAO.getDisc(disc.getId());
        assertEquals(actual.getId(),disc.getId());
        assertEquals(actual.getColor(),disc.getColor());
        assertEquals(actual.getWeight(),disc.getWeight());
        assertEquals(actual.getType(),disc.getType());
        assertEquals(actual.getPrice(), disc.getPrice());
        assertEquals(actual.getQuantity(), disc.getQuantity());
    }

    @Test
    public void testUpdateDisc() {
        // Setup
        Disc disc = new Disc(99,"Blue",160,"Fairway Driver",50,90);

        // Invoke
        Disc result = assertDoesNotThrow(() -> discFileDAO.updateDisc(disc),
                                "Unexpected exception thrown");

        // Analyze
        assertNotNull(result);
        Disc actual = discFileDAO.getDisc(disc.getId());
        assertEquals(actual,disc);
    }

    @Test
    public void testSaveException() throws IOException{
        doThrow(new IOException())
            .when(mockObjectMapper)
                .writeValue(any(File.class),any(Disc[].class));

        Disc disc = new Disc(102,"Grey",170,"Fairway Driver", 40, 50);

        assertThrows(IOException.class,
                        () -> discFileDAO.createDisc(disc),
                        "IOException not thrown");
    }

    

    @Test
    public void testGetDiscNotFound() {
        // Invoke
        Disc disc = discFileDAO.getDisc(98);

        // Analyze
        assertEquals(disc,null);
    }

    @Test
    public void testDeleteDiscNotFound() {
        // Invoke
        boolean result = assertDoesNotThrow(() -> discFileDAO.deleteDisc(98),
                                                "Unexpected exception thrown");

        // Analyze
        assertEquals(result,false);
        assertEquals(discFileDAO.discs.size(),testDiscs.length);
    }

    @Test
    public void testUpdateDiscNotFound() {
        // Setup
        Disc disc = new Disc(98,"Yellow",150,"Putter",30, 100);

        // Invoke
        Disc result = assertDoesNotThrow(() -> discFileDAO.updateDisc(disc),
                                                "Unexpected exception thrown");

        // Analyze
        assertNull(result);
    }

    @Test
    public void testConstructorException() throws IOException {
        // Setup
        ObjectMapper mockObjectMapper = mock(ObjectMapper.class);
        // We want to simulate with a Mock Object Mapper that an
        // exception was raised during JSON object deseerialization
        // into Java objects
        // When the Mock Object Mapper readValue method is called
        // from the DiscFileDAO load method, an IOException is
        // raised
        doThrow(new IOException())
            .when(mockObjectMapper)
                .readValue(new File("doesnt_matter.txt"),Disc[].class);

        // Invoke & Analyze
        assertThrows(IOException.class,
                        () -> new DiscFileDAO("doesnt_matter.txt",mockObjectMapper),
                        "IOException not thrown");
    }
}