
package com.sblinn.vendingmachine.dao;

import com.sblinn.vendingmachine.dto.Item;
import java.util.List;

/**
 *
 * @author sarablinn
 */
public interface VendingMachineDao {
    // CRUD
    
    Item createItem(String locationID, Item item) throws PersistenceException;
    
    Item getItem(String locationID) throws PersistenceException;
    
    List<Item> getAllItems() throws PersistenceException;
    
    Item updateItem(String locationID, Item updatedItem) throws PersistenceException;
    
    Item deleteItem(String locationID) throws PersistenceException;
}
