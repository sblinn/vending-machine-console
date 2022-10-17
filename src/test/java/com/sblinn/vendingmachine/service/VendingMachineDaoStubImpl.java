
package com.sblinn.vendingmachine.service;

import com.sblinn.vendingmachine.dao.PersistenceException;
import com.sblinn.vendingmachine.dao.VendingMachineDao;
import com.sblinn.vendingmachine.dto.Item;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Stubbed version of VendingMachineDao for use in testing service layer 
 * implementation without testing persistence. None of the methods will change
 * onlyItem field.
 * 
 * @author sarablinn
 */
public class VendingMachineDaoStubImpl implements VendingMachineDao {
    
    public Item onlyItem;
    
    
    /**
     * Creates a hard-coded Item for the stub.
     */
    public VendingMachineDaoStubImpl() {
        onlyItem = new Item("A1", "Twinkle Pies");
        onlyItem.setNumAvailable(5);
        onlyItem.setPrice(new BigDecimal("2.50"));
    }
    
    /**
     * Allows for a test item to be injected by the service layer test class.
     * 
     * @param testItem 
     */
    public VendingMachineDaoStubImpl(Item testItem) {
        this.onlyItem = testItem;
    }

    // Returns the onlyItem only if the input locationID matches our onlyItem,
    // else it returns null.
    @Override
    public Item createItem(String locationID, Item item) throws 
            PersistenceException {
        
        if(locationID.equals(onlyItem.getLocationID())) {
            return onlyItem;
        } else {
            return null;
        } 
    }

    // Returns the onlyItem only if the input locationID matches our onlyItem,
    // else it returns null.
    @Override
    public Item getItem(String locationID) throws PersistenceException {
        if(locationID.equals(onlyItem.getLocationID())) {
            return onlyItem;
        } else {
            return null;
        }
    }

    // Creates and returns a list containing only our onlyItem.
    @Override
    public List<Item> getAllItems() throws PersistenceException {
        List<Item> itemList = new ArrayList<>();
        itemList.add(onlyItem);
        return itemList;
    }

    // Returns the updatedItem only if input locationID equals onlyItem 
    // locationID, else returns null.
    @Override
    public Item updateItem(String locationID, Item updatedItem) throws 
            PersistenceException {
        
        if(locationID.equals(onlyItem.getLocationID())) {
            this.onlyItem = updatedItem;
            return onlyItem;
        } else {
            return null;
        }
    }

    @Override
    public Item deleteItem(String locationID) throws PersistenceException {
        if(locationID.equals(onlyItem.getLocationID())) {
            return onlyItem;
        } else {
            return null;
        }
    }
    
    
}
