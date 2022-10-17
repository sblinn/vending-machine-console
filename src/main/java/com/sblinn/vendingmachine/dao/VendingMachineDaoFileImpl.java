
package com.sblinn.vendingmachine.dao;

import com.sblinn.vendingmachine.dto.Item;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author sarablinn
 */
public class VendingMachineDaoFileImpl implements VendingMachineDao {
    
    private final String INVENTORY_FILE;
    public static final String DELIMITER = "::";
    private Map<String, Item> items = new HashMap<>();
    
    
    public VendingMachineDaoFileImpl() {
        INVENTORY_FILE = "inventory.txt";
    }
    
    /**
     * 
     * @param inventoryTextFile 
     */
    public VendingMachineDaoFileImpl(String inventoryTextFile) {
        INVENTORY_FILE = inventoryTextFile;
    }

    @Override
    public Item createItem(String locationID, Item item) throws PersistenceException {
        loadInventory();
        // make locationID uppercase 
        locationID = locationID.toUpperCase();
        Item newItem = items.put(locationID, item);
        writeInventory();
        return newItem;
    }

    /**
     * Returns an Item from inventory, else returns null if the item does not
     * exist.
     * 
     * @param locationID - String
     * @return Item or null if the item does not yet exist
     * @throws PersistenceException 
     */
    @Override
    public Item getItem(String locationID) throws PersistenceException {
        loadInventory();
        // make user input locationID uppercase
        locationID = locationID.toUpperCase();
        Item item = items.get(locationID);
        return item;
    }

    @Override
    public List<Item> getAllItems() throws PersistenceException {
        loadInventory();
        // Sort the list of items by their locationID, so they have order
        List<Item> sortedItems = new ArrayList(items.values());
        sortedItems.sort(Comparator.comparing((i) -> i.getLocationID()));
        return sortedItems;
    }

    /**
     * Updates the item at the given location ID by removing the previous item 
     * and replacing it with an updated Item, then returns the updated Item.
     * 
     * @param locationID - String
     * @param updatedItem - Item
     * @return updatedItem 
     * @throws PersistenceException 
     */
    @Override
    public Item updateItem(String locationID, Item updatedItem) 
            throws PersistenceException {
        loadInventory();
        // make user input locationID uppercase
        locationID = locationID.toUpperCase();
        items.remove(locationID);
        items.put(updatedItem.getLocationID(), updatedItem);
        writeInventory();
        return updatedItem;
    }

    @Override
    public Item deleteItem(String locationID) throws PersistenceException {
        loadInventory();
        // make user input locationID uppercase
        locationID = locationID.toUpperCase();
        Item deletedItem = items.remove(locationID);
        writeInventory();
        return deletedItem;
    }
    
    
    /**
     * Converts an Item object into a String line of text with delimiters
     * separating the object's fields.
     * 
     * @param item Item 
     * @return itemAsText - String Item object as a line of text
     */
    private String marshallItem(Item item) {
        String itemAsText = item.getLocationID() + DELIMITER;
        itemAsText += item.getName() + DELIMITER;
        itemAsText += item.getNumAvailable() + DELIMITER;
        itemAsText += item.getPrice();
        
        return itemAsText;
    }
    
    /**
     * Reads a line of text from the inventory file and converts the delimited
     * String into an Item object. 
     * 
     * @param itemAsText - delimited String representing the item
     * @return 
     */
    private Item unmarshallItem(String itemAsText) {
        String[] itemData = itemAsText.split(DELIMITER);
        
        String locationID = itemData[0];
        Item itemFromFile = new Item(locationID);
        itemFromFile.setName(itemData[1]);
        itemFromFile.setNumAvailable(Integer.parseInt(itemData[2]));
        itemFromFile.setPrice(new BigDecimal(itemData[3]));
        
        return itemFromFile;
    }
    
    /**
     * Reads the inventory file and loads the data into the HashMap.
     * 
     * @throws PersistenceException 
     */
    private void loadInventory() throws PersistenceException {
        Scanner scanner;
        
        try {
            scanner = new Scanner(new BufferedReader(
                new FileReader(INVENTORY_FILE)));    
        } catch(FileNotFoundException e) {
            throw new PersistenceException(
                    "Unable to load item data into memory.");
        }
        
        String currentLine;
        Item currentItem;
        
        while(scanner.hasNextLine()) {
            currentLine = scanner.nextLine();
            currentItem = unmarshallItem(currentLine);
            items.put(currentItem.getLocationID(), currentItem);
        }
        
        scanner.close();
    }
    
    /**
     * Writes all the Items in the HashMap into the inventory file.
     * 
     * @throws PersistenceException 
     */
    private void writeInventory() throws PersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(INVENTORY_FILE));
        } catch(IOException e) {
            throw new PersistenceException("Unable to save data "
                    + "to inventory file.");
        }
        // write the item to the file
        String itemAsText;
        List<Item> itemList = this.getAllItems();
        for(Item currentItem : itemList) {
            itemAsText = marshallItem(currentItem);
            out.println(itemAsText);
            out.flush();
        }
        out.close();
    }
}
