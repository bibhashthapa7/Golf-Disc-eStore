package com.discgolf.api.discgolfapi.persistence;

import java.io.IOException;

import com.discgolf.api.discgolfapi.model.Disc;

/**
 * Defines the interface for Hero object persistence
 * 
 * @author SWEN Faculty + coolname
 */
public interface DiscDAO {
    /**
     * Retrieves all {@linkplain Disc discs}
     * 
     * @return An array of {@link Disc disc} objects, may be empty
     * 
     * @throws IOException if an issue with underlying storage
     */
    Disc[] getDiscs() throws IOException;

    /**
     * Returns an array of {@linkplain Disc discs} that meet the filter criteria
     * 
     * @param search The given term to search disc attributes for
     * @param mode The filter mode {0:All, 1:Type, 2:Color, 3:Weight, 4:Price, Default:Price}
     * @return  The array of {@link Disc discs}, may be empty
     */
    Disc[] findDiscs(String text, int mode) throws IOException;

    /**
     * Retrieves a {@linkplain Disc disc} with the given id
     * 
     * @param id The id of the {@link Disc disc} to get
     * 
     * @return a {@link Disc disc} object with the matching id
     * <br>
     * null if no {@link Disc disc} with a matching id is found
     * 
     * @throws IOException if an issue with underlying storage
     */
    Disc getDisc(int id) throws IOException;

    /**
     * Creates and saves a {@linkplain Disc disc}
     * 
     * @param disc {@linkplain Disc disc} object to be created and saved
     * <br>
     * The id of the disc object is ignored and a new uniqe id is assigned
     *
     * @return new {@link Disc disc} if successful, false otherwise 
     * 
     * @throws IOException if an issue with underlying storage
     */
    Disc createDisc(Disc disc) throws IOException;

    /**
     * Updates and saves a {@linkplain Disc disc}
     * 
     * @param {@link Disc disc} object to be updated and saved
     * 
     * @return updated {@link Disc disc} if successful, null if
     * {@link Disc disc} could not be found
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    Disc updateDisc(Disc disc) throws IOException;

    /**
     * Deletes a {@linkplain Disc disc} with the given id
     * 
     * @param id The id of the {@link Disc disc}
     * 
     * @return true if the {@link Disc disc} was deleted
     * <br>
     * false if disc with the given id does not exist
     * 
     * @throws IOException if underlying storage cannot be accessed
     */
    boolean deleteDisc(int id) throws IOException;
}
