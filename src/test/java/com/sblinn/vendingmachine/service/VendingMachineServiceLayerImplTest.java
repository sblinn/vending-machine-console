
package com.sblinn.vendingmachine.service;

import com.sblinn.vendingmachine.dao.PersistenceException;
import com.sblinn.vendingmachine.dao.VendingMachineAuditDao;
import com.sblinn.vendingmachine.dao.VendingMachineDao;
import com.sblinn.vendingmachine.dto.Change;
import com.sblinn.vendingmachine.dto.Item;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author sarablinn
 */
public class VendingMachineServiceLayerImplTest {
    
    /*
    TEST PLAN:
    
    - Test business rules and the integration between the service layer and the
        DAO, using stubs so that methods can function as normal during testing
        without depending on persistence and making any changes to the audit 
        file.
    - VendingMachineDaoStubImpl() will create a the one and only Item in the 
        Dao stub at named "Twinkle Pie", locationID "A1", quantity 5, price 2.50.
    
    testGetAllItems() - arrange a copy of onlyItem, get the list of Items.
                    - Since the stubDao only has onlyItem, there should only be
                        one item in the list and it should be onlyItem.
                   --> Assert that there is only one item in the list.
                   --> Assert that the one item's data is equal to what we expect
                        onlyItem's data to be.
   
    
    Tests for sellItem() - arrange a copy of onlyItem and variable for buyer's funds.
        - Test: locationID = A1, remMoney = 3.23 
            --> No exceptions should be thrown.
            --> Returned Change object sum value should equal (3.23 - 2.50) = 0.73
        - Test: locationID = A2, remMoney = 2.50
            --> InvalidDataException should be thrown, A2 doesn't exist.
        - Test: locationID = A1, remMoney = 2.40
            --> InsufficientFundsException should be thrown.
        - Test: Set onlyItem to a version with 0 remaining stock available.
                locationID = A1, remMoney = 2.50
            --> NoItemInventoryException should be thrown.
    
    */
    
    
    private VendingMachineServiceLayer service;
    
    
    public VendingMachineServiceLayerImplTest() {
//        VendingMachineDao dao = new VendingMachineDaoStubImpl();
//        VendingMachineAuditDao auditDao = new VendingMachineAuditDaoStubImpl();
//        
//        service = new VendingMachineServiceLayerImpl(dao, auditDao);

        ApplicationContext appContext = 
                new ClassPathXmlApplicationContext("applicationContext.xml");
        
        service = appContext.getBean("service", VendingMachineServiceLayer.class);
    }
    
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * 
     */
    @Test
    private void testCreateValidItem() {
        // Create VALID item (valid data, no duplicate ID)
        // --> should not throw any exceptions
        Item item = new Item("A2", "Ginger Mints");
        item.setNumAvailable(2);
        item.setPrice(new BigDecimal("3.00"));
        
        try {
            service.createItem(item);
        } catch(InvalidDataException 
                | DuplicateLocationIDException
                | PersistenceException e) {
            
            fail("New Item was valid, no exception should have been thrown.");
        }
    }
    
    @Test
    private void testCreateInvalidItem() {
        // Create INVALID item -- missing required name property.
        Item item = new Item("A2", "");
        item.setNumAvailable(2);
        item.setPrice(new BigDecimal("3.00"));
        
        try {
            service.createItem(item);
            fail("New Item was data invalid, expected InvalidDataException was "
                    + "not thrown.");
        } catch(DuplicateLocationIDException | PersistenceException e) {
            fail("No other exceptions should have been thrown.");
        } catch(InvalidDataException e) {
            return; // passed
        }
    }
    
    @Test
    private void testCreateItemDuplicateLocationID() {
        // Create item using duplicate locationID as onlyItem in DAO stub
        // --> should throw DuplicateLocationIDException
        Item item = new Item("A1", "Ginger Mints");
        item.setNumAvailable(2);
        item.setPrice(new BigDecimal("3.00"));
        
        try {
            service.createItem(item);
            fail("Expected DuplicateLocationIDException was not thrown.");
        } catch(InvalidDataException | PersistenceException e) {
            fail("Incorrect exception thrown. Item data is not invalid.");
        } catch(DuplicateLocationIDException e) {
            return; // passed
        }
    }
    
    @Test
    private void testGetItemValidLocationID() throws Exception {
        Item testClone = new Item("A1", "Twinkle Pies");
        testClone.setNumAvailable(5);
        testClone.setPrice(new BigDecimal("2.50"));

        Item retrievedItem = service.getItem("A1");
        assertNotNull(retrievedItem, "Getting item at A1 should not be null.");
        assertEquals(testClone, retrievedItem, 
                "Retrieved item should be A1 Twinkle Pies.");
    }
        
    @Test
    private void testGetItemInvalidLocationID() throws Exception {
        Item shouldBeNullItem = service.getItem("A5");
            assertNull(shouldBeNullItem, 
                    "No item exists at A5. Expected return is null.");  
    }

    
    @Test
    public void testGetAllItems() throws Exception {
        Item testClone = new Item("A1", "Twinkle Pies");
        testClone.setNumAvailable(5);
        testClone.setPrice(new BigDecimal("2.50"));
        
        assertEquals(1, service.getAllItems().size(), 
                "List should only have 1 Item");
        assertTrue(service.getAllItems().get(0).equals(testClone), 
                "List should contain only Twinkle Pies.");
    }
    
    @Test 
    private void testUpdateItem() throws Exception {
        Item testClone = new Item("A1", "Twinkle Pies");
        testClone.setNumAvailable(5);
        testClone.setPrice(new BigDecimal("2.50"));
        
        Item updatedItem = testClone;
        updatedItem.setName("Ginger Pies");
        
        Item retrievedItem;
        // Valid locationID A1
        try {
            retrievedItem = service.updateItem("A1", updatedItem);
            assertNotNull(retrievedItem, "Returned item should not be null, "
                    + "locationID was correct.");
            assertEquals(retrievedItem.getName(), updatedItem.getName(), 
                    "Name of item should be Ginger Pies.");
            assertEquals(retrievedItem, updatedItem, "Returned item should be"
                    + " the same as the updated item.");
        } catch(InvalidDataException | PersistenceException e) {
            fail("No Exception should have been thrown for update to A1.");
        }
        // Invalid LocationID A5
        try {
            retrievedItem = service.updateItem("A5", updatedItem);
            fail("Expected InvalidDataException was not thrown for update to A5.");
        } catch(InvalidDataException e) {
            return; // passed
        }
    }
    
    @Test
    public void testDeleteItem() throws Exception {
        Item testClone = new Item("A1", "Twinkle Pies");
        testClone.setNumAvailable(5);
        testClone.setPrice(new BigDecimal("2.50"));
        
        Item deletedItem;
        // Correct locationID
        deletedItem = service.deleteItem(testClone.getLocationID());
        assertNotNull(deletedItem, "Returned value should not be null.");
        assertEquals(testClone, deletedItem, 
                "Deleted item should be A1 Twinkle Pies.");
        
        // Incorrect locationID --> should throw InvalidDataException
        try {
            deletedItem = service.deleteItem("A5");
            fail("Expected InvalidDataException was not thrown for deletion "
                    + "to A5.");
        } catch(InvalidDataException e) {
            return; // passed
        }
    }
    
    @Test
    public void testSellItem() {
        Item testClone = new Item("A1", "Twinkle Pies");
        testClone.setNumAvailable(5);
        testClone.setPrice(new BigDecimal("2.50"));
        
        Item retrievedSoldItem;
        
        BigDecimal testMoney = new BigDecimal("3.23");
        BigDecimal expectedChangeSum = new BigDecimal("0.73");
        BigDecimal actualChangeSum;
        
        try {
            Change changeCoins = service.sellItem(testClone.getLocationID(), 
                    testMoney);
            actualChangeSum = changeCoins.getChangeSum();
            assertTrue(expectedChangeSum.compareTo(actualChangeSum) == 0);
            
            retrievedSoldItem = service.getItem(testClone.getLocationID());
            assertEquals(4, retrievedSoldItem.getNumAvailable(), 
                    "Updated quantity for A1 should be 4.");
        } catch(PersistenceException 
                | NoItemInventoryException
                | InsufficientFundsException
                | InvalidDataException e) {
            fail("No exceptions should have been thrown.");       
        }  
    }
    
    @Test
    public void testSellItemInvalidLocationID() throws Exception {
        Item testClone = new Item("A1", "Twinkle Pies");
        testClone.setNumAvailable(5);
        testClone.setPrice(new BigDecimal("2.50"));
        
        Item retrievedUnsoldItem;
        BigDecimal testMoney = new BigDecimal("3.23");
        
        try {
            service.sellItem("A5", testMoney);
            fail("Expected InvalidDataException was not thrown.");
        } catch(PersistenceException 
                | NoItemInventoryException
                | InsufficientFundsException e) {
            fail("Wrong exceptions thrown.");  
        } catch(InvalidDataException e) {
            
        }
        
        retrievedUnsoldItem = service.getItem(testClone.getLocationID());
        assertEquals(5, retrievedUnsoldItem.getNumAvailable(), 
                    "Updated quantity for A1 should be 5.");
    }
    
    @Test
    public void testSellItemInsufficientFunds() throws Exception {
        Item testClone = new Item("A1", "Twinkle Pies");
        testClone.setNumAvailable(5);
        testClone.setPrice(new BigDecimal("2.50"));
        
        Item retrievedUnsoldItem;
        
        BigDecimal testMoney = new BigDecimal("2.00");
        
        try {
            service.sellItem(testClone.getLocationID(), testMoney);
            fail("Expected InsufficientFundsException was not thrown.");
        } catch(PersistenceException 
                | NoItemInventoryException
                | InvalidDataException e) {
            fail("Wrong exception thrown.");       
        } catch(InsufficientFundsException e) {
            retrievedUnsoldItem = service.getItem(testClone.getLocationID());
            assertEquals(5, retrievedUnsoldItem.getNumAvailable(), 
                    "Updated quantity for A1 should be 5.");
            return; // passed
        }
    }

    @Test
    public void testSellItemOutOfStock() {
        Item item = new Item("A1", "Twinkle Pies");
        item.setNumAvailable(0);
        item.setPrice(new BigDecimal("2.50"));
        
        BigDecimal testMoney = new BigDecimal("2.50");
        
        // Change the only DAO item so that it is an item with stock of 0.
        VendingMachineDao dao = new VendingMachineDaoStubImpl(item);
        VendingMachineAuditDao auditDao = new VendingMachineAuditDaoStubImpl();
        this.service = new VendingMachineServiceLayerImpl(dao, auditDao);
        
        try {
            service.sellItem(item.getLocationID(), testMoney);
            fail("Expected NoItemInventoryException was not thrown."); 
        } catch(PersistenceException 
                | InsufficientFundsException 
                | InvalidDataException e) {
            fail("Wrong exception thrown.");
        } catch(NoItemInventoryException e) {
            return; // passed
        }
    }
}
