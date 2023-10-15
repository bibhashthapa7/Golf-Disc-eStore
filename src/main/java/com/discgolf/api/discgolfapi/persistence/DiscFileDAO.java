package com.discgolf.api.discgolfapi.persistence;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.discgolf.api.discgolfapi.model.Disc;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Implements the functionality for JSON file-based peristance for Discs
 * 
 * {@literal @}Component Spring annotation instantiates a single instance of this
 * class and injects the instance into other classes as needed
 * 
 * @author SWEN Faculty + coolname
 */
@Component
public class DiscFileDAO implements DiscDAO {
    private static final Logger LOG = Logger.getLogger(DiscFileDAO.class.getName());
    Map<Integer,Disc> discs;   // Provides a local cache of the disc objects
                                // so that we don't need to read from the file
                                // each time
    private ObjectMapper objectMapper;  // Provides conversion between Disc
                                        // objects and JSON text format written
                                        // to the file
    private static int nextId;  // The next Id to assign to a new disc
    private String filename;    // Filename to read from and write to

    /**
     * Creates a Disc File Data Access Object
     * 
     * @param filename Filename to read from and write to
     * @param objectMapper Provides JSON Object to/from Java Object serialization and deserialization
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    public DiscFileDAO(@Value("${discs.file}") String filename,ObjectMapper objectMapper) throws IOException {
        this.filename = filename;
        this.objectMapper = objectMapper;
        load();  // load the discs from the file
    }

    /**
     * Generates the next id for a new {@linkplain Disc disc}
     * 
     * @return The next id
     */
    private synchronized static int nextId() {
        int id = nextId;
        ++nextId;
        return id;
    }

    /**
     * Generates an array of {@linkplain Disc discs} from the tree map
     * 
     * @return  The array of {@link Disc discs}, may be empty
     */
    private Disc[] getDiscsArray() {
        return getDiscsArray(null, 0);
    }

    /**
     * Generates an array of {@linkplain Disc discs} from the tree map FILTERING using the mode
     * 
     * @param search The given term to search disc attributes for
     * @param mode The filter mode {0:All, 1:Type, 2:Color, 3:Weight, 4:Price, Default:Price}
     * @return  The array of {@link Disc discs}, may be empty
     */
    private Disc[] getDiscsArray(String search, int mode) {
        ArrayList<Disc> discArrayList = new ArrayList<>();

        for (Disc disc : discs.values()) {
            if (search == null || mode == 0) {
                discArrayList.add(disc); // Default add all
            } else {
                String discAttr = (mode == 1 ? disc.getType() : (mode == 2 ? disc.getColor() : (mode == 3 ? Integer.toString(disc.getWeight()) : Double.toString(disc.getPrice()))));
                if (discAttr.toLowerCase().contains(search.toLowerCase()))
                    discArrayList.add(disc);
            }
        }

        Disc[] discArray = new Disc[discArrayList.size()];
        discArrayList.toArray(discArray);
        return discArray;
    }

    /**
     * Saves the {@linkplain Disc discs} from the map into the file as an array of JSON objects
     * 
     * @return true if the {@link Disc discs} were written successfully
     * 
     * @throws IOException when file cannot be accessed or written to
     */
    private boolean save() throws IOException {
        Disc[] discArray = getDiscsArray();

        // Serializes the Java Objects to JSON objects into the file
        // writeValue will thrown an IOException if there is an issue
        // with the file or reading from the file
        objectMapper.writeValue(new File(filename),discArray);
        return true;
    }

    /**
     * Loads {@linkplain Disc discs} from the JSON file into the map
     * <br>
     * Also sets next id to one more than the greatest id found in the file
     * 
     * @return true if the file was read successfully
     * 
     * @throws IOException when file cannot be accessed or read from
     */
    private boolean load() throws IOException {
        discs = new TreeMap<>();
        nextId = 0;

        // Deserializes the JSON objects from the file into an array of discs
        // readValue will throw an IOException if there's an issue with the file
        // or reading from the file
        Disc[] discArray = objectMapper.readValue(new File(filename),Disc[].class);

        // Add each disc to the tree map and keep track of the greatest id
        for (Disc disc : discArray) {
            discs.put(disc.getId(),disc);
            if (disc.getId() > nextId)
                nextId = disc.getId();
        }
        // Make the next id one greater than the maximum from the file
        ++nextId;
        return true;
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Disc[] getDiscs() {
        synchronized(discs) {
            return getDiscsArray();
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override  
    public Disc[] findDiscs(String search, int mode) {
        synchronized(discs) {
            return getDiscsArray(search, mode);
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Disc getDisc(int id) {
        synchronized(discs) {
            if (discs.containsKey(id))
                return discs.get(id);
            else
                return null;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Disc createDisc(Disc disc) throws IOException {
        synchronized(discs) {
            // We create a new disc object because the id field is immutable
            // and we need to assign the next unique id
            Disc newDisc = new Disc(nextId(), disc.getColor(),
                                    disc.getWeight(), disc.getType(), 
                                    disc.getPrice(), disc.getQuantity());
            discs.put(newDisc.getId(),newDisc);
            save(); // may throw an IOException
            return newDisc;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public Disc updateDisc(Disc disc) throws IOException {
        synchronized(discs) {
            if (discs.containsKey(disc.getId()) == false)
                return null;  // disc does not exist

            discs.put(disc.getId(),disc);
            save(); // may throw an IOException
            return disc;
        }
    }

    /**
    ** {@inheritDoc}
     */
    @Override
    public boolean deleteDisc(int id) throws IOException {
        synchronized(discs) {
            if (discs.containsKey(id)) {
                discs.remove(id);
                return save();
            }
            else
                return false;
        }
    }
}