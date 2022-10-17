
package com.sblinn.vendingmachine.dao;

import com.sblinn.vendingmachine.dto.Item;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author sarablinn
 */
public class VendingMachineDaoFileImplTest {
    
    /*
    TEST PLAN:
    
    Test all the methods in VendingMachineDaoFileImpl
    
    testCreateAndGetItem() -> createItem(), getItem(), 
                                loadInventory, writeInventory(), 
                                marshallItem(), unmarshallItem()
        Create an item and retrieve it from the test file.
    
    testGetAllItems() -> getAllItems()
        Create 2 items and retrieve them from the test file. Check that it is
        not empty, contains exactly 2 items, check that it contains both items
        precisely.
    
    testUpdateItem() -> updateItem()
        Create an item, update one of it's fields, retrieve it from the test
        file and check if the field data is updated. 
    
    testDeleteItem() -> deleteItem()
        Create 2 items, delete one of them. 
        (repeat process from testGetAllItems() ): Get a list of the items, check 
        that the list is not empty, then that it contains only 1 item. 
        Check that the list does not contain the deleted item, but does contain 
        the remaining item. 
        Delete the remaining item, get a list of items, check that it is empty. 
        Attempt to retrieve the deleted items and assert that they return null.
    
    */
    
    
    private VendingMachineDao testDao;
    private final String TEST_FILE;
    
    
    public VendingMachineDaoFileImplTest() {
        ApplicationContext appContext = 
                new ClassPathXmlApplicationContext("applicationContext.xml");
        
        testDao = appContext.getBean("dao", VendingMachineDao.class);
        TEST_FILE = appContext.getBean("testFile", String.class);
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        // Set up and clear the DAO test file so it is in an empty state 
        // before each test.        
        new FileWriter(TEST_FILE); // clears the text file
//        testDao = new VendingMachineDaoFileImpl(TEST_FILE);
    }
   

    
    @Test
    public void testCreateAndGetItem() throws Exception {
        // Create an item.
        String locationID = "A1";
        Item item = new Item(locationID , "Twinkle Pie");
        item.setNumAvailable(5);
        item.setPrice(new BigDecimal("2.50"));
        
        // Add item to the DAO file
        testDao.createItem(locationID, item);
        // Get item from DAO
        Item retrievedItem = testDao.getItem(locationID);
        // Check that the retrievedItem field values equal the original
        assertEquals(item.getLocationID(), retrievedItem.getLocationID(),
                "Checking location ID is the same.");
        assertEquals(item.getName(), retrievedItem.getName(),
                "Checking name is the same.");
        assertEquals(item.getNumAvailable(), retrievedItem.getNumAvailable(), 
                "Checking number available is the same.");
        assertEquals(item.getPrice(), retrievedItem.getPrice(), 
                "Checking price is the same.");
    }
    
    @Test
    public void testGetAllItems() throws Exception {
        // Create two items
        Item firstItem = new Item("A1" , "Twinkle Pie");
        firstItem.setNumAvailable(5);
        firstItem.setPrice(new BigDecimal("2.50"));
        
        Item secondItem = new Item("A2" , "Ginger Mints");
        secondItem.setNumAvailable(6);
        secondItem.setPrice(new BigDecimal("3.00"));
        
        // Add both items to the DAO
        testDao.createItem("A1", firstItem);
        testDao.createItem("A2", secondItem);
        // Get the list of items from the DAO
        List<Item> itemList = testDao.getAllItems();
        
        // Check that the list != null && contains exactly 2 items
        assertNotNull(itemList, "The list of items cannot be null.");
        assertEquals(2, itemList.size(), "The list of items should have 2 items.");
        // Check that the list contains both the Items created
        assertTrue(itemList.contains(firstItem));
        assertTrue(itemList.contains(secondItem));
    }
    
    @Test
    public void testDeleteItem() throws Exception {
        // Create two items
        Item firstItem = new Item("A1" , "Twinkle Pie");
        firstItem.setNumAvailable(5);
        firstItem.setPrice(new BigDecimal("2.50"));
        
        Item secondItem = new Item("A2" , "Ginger Mints");
        secondItem.setNumAvailable(6);
        secondItem.setPrice(new BigDecimal("3.00"));
        
        // Add both items to the DAO
        testDao.createItem("A1", firstItem);
        testDao.createItem("A2", secondItem);
        
        // Delete firstItem and check that the correct Item was deleted
        Item deletedItem = testDao.deleteItem(firstItem.getLocationID());
        assertEquals(firstItem, deletedItem, 
                "Removed item should be the first item: A1. ");
        
        // Get the list of items from the DAO
        List<Item> itemList = testDao.getAllItems();
        
        // Check that the list is not null and that it contains 1 item.
        assertNotNull(itemList, "List of items should be not null.");
        assertEquals(1, itemList.size(), "List of items should contain 1 item.");
        
        // Check that the list contains firstItem but not secondItem
        assertFalse(itemList.contains(firstItem), "List should not contain A1.");
        assertTrue(itemList.contains(secondItem), "List should contain A2.");
        
        // Delete secondItem and check that correct item was deleted.
        deletedItem = testDao.deleteItem(secondItem.getLocationID());
        assertEquals(deletedItem, secondItem, "Deleted item should be A2.");
        
        // Retrieve the list of Items again and check that its empty
        itemList = testDao.getAllItems();       
        assertTrue(itemList.isEmpty(), "List of items should be empty.");
        
        // Attempt to retrieve the items using their locationID and assert
        // that the items returned are null
        Item retrievedItem = testDao.getItem(firstItem.getLocationID());
        assertNull(retrievedItem, "A1 was removed and should be null.");
        retrievedItem = testDao.getItem(secondItem.getLocationID());
        assertNull(retrievedItem, "A2 was removed and should be null.");
    }
    
    @Test
    public void testUpdateItem() throws Exception {
        // Create an item.
        String locationID = "A1";
        Item item = new Item(locationID , "Twinkle Pie");
        item.setNumAvailable(5);
        item.setPrice(new BigDecimal("2.50"));
        
        testDao.createItem(locationID, item);
        // Modify the item object and update the DAO
        item.setName("Chips");
        Item updatedItem = testDao.updateItem(locationID, item);
        // Get the item from the DAO and check that it equals the the item we updated
        assertEquals(testDao.getItem(locationID), updatedItem, 
                "Updated item saved to the DAO should equal the item we updated.");
        assertEquals(testDao.getItem(locationID).getName(), "Chips",
                "Name of updated item saved to the DAO should equal 'Chips.'");
    }
    
}
