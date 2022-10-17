
package com.sblinn.vendingmachine.service;

import com.sblinn.vendingmachine.dao.PersistenceException;
import com.sblinn.vendingmachine.dto.Change;
import com.sblinn.vendingmachine.dto.Item;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author sarablinn
 */
public interface VendingMachineServiceLayer {
    
    void createItem(Item item) throws 
            PersistenceException, 
            InvalidDataException, 
            DuplicateLocationIDException;
    
    Item getItem(String locationID) throws 
            PersistenceException,
            InvalidDataException;          
    
    List<Item> getAllItems() throws PersistenceException;
    
    Item updateItem(String locationID, Item updatedItem) throws 
            PersistenceException,
            InvalidDataException;
    
    Item deleteItem(String locationID) throws 
            PersistenceException,
            InvalidDataException;
    
    Change sellItem(String locationID, BigDecimal remBalance) throws
            PersistenceException,
            NoItemInventoryException,
            InsufficientFundsException,
            InvalidDataException;
    
}
