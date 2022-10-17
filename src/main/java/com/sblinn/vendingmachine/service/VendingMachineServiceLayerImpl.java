
package com.sblinn.vendingmachine.service;

import com.sblinn.vendingmachine.dao.PersistenceException;
import com.sblinn.vendingmachine.dao.VendingMachineAuditDao;
import com.sblinn.vendingmachine.dao.VendingMachineDao;
import com.sblinn.vendingmachine.dto.Change;
import com.sblinn.vendingmachine.dto.Item;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author sarablinn
 */
public class VendingMachineServiceLayerImpl implements VendingMachineServiceLayer {

    private VendingMachineDao dao;
    private VendingMachineAuditDao auditDao;
    
    
    public VendingMachineServiceLayerImpl(VendingMachineDao dao, 
            VendingMachineAuditDao auditDao) {
        
        this.dao = dao;
        this.auditDao = auditDao;
    }
    
    
    @Override
    public void createItem(Item item) throws 
            PersistenceException, 
            InvalidDataException, 
            DuplicateLocationIDException {
        
        // dao will return null if the that locationID hasn't been used yet
        if(dao.getItem(item.getLocationID()) != null) {
            throw new DuplicateLocationIDException(
                "ERROR: Duplicate location ID: " + item.getLocationID() +
                        ". Unable to create item in that location.");
        }
        // validate the item's data, method will throw InvalidDataException
        validateRequiredItemData(item);
        dao.createItem(item.getLocationID(), item);
        auditDao.writeAuditEntry("Item " + item.getLocationID() + " : " +
                item.getName() + " CREATED.");     
    }

    @Override
    public Item getItem(String locationID) throws 
            PersistenceException,
            InvalidDataException {
        
        Item item;
        // Check if Item at locationID exists ( != null)
        try {
            item = dao.getItem(locationID);
        } catch(NullPointerException e) {
            throw new InvalidDataException("ERROR: Invalid location ID: "
                + locationID + ". No existing item at " + locationID + ".");
        }
        
        return item;
    }

    @Override
    public List<Item> getAllItems() throws PersistenceException {
        // add filter stream for out of stock items.
        List<Item> itemList = dao.getAllItems();
        List<Item> filteredItemList = itemList.stream()
                .filter((i) -> i.getNumAvailable() != 0)
                .collect(Collectors.toList());
        return filteredItemList;
    }

    @Override
    public Item updateItem(String locationID, Item updatedItem) throws 
            PersistenceException,
            InvalidDataException {
        
        // check if an item at that locationID exists
        if(dao.getItem(locationID) == null) {
            throw new InvalidDataException (
                "ERROR: Invalid location ID: " + locationID 
                + ". No existing item to update at " + locationID + ".");
        }
        
        Item oldItem = dao.getItem(locationID);
        // check if the updatedItem's data is valid
        validateRequiredItemData(updatedItem);
        dao.updateItem(locationID, updatedItem);
        
        String oldItemInfo = String.format("[%s (%s in stock) : $%s]",
                    oldItem.getName(),
                    oldItem.getNumAvailable(),
                    oldItem.getPrice() );
        String updatedItemInfo = String.format("[%s (%s in stock) : $%s]",
                    updatedItem.getName(),
                    updatedItem.getNumAvailable(),
                    updatedItem.getPrice() );
        auditDao.writeAuditEntry("Item at " + locationID + ": " + oldItemInfo
                + " UPDATED to: " + updatedItemInfo); 
        
        return updatedItem;
    }

    @Override
    public Item deleteItem(String locationID) throws 
            PersistenceException,
            InvalidDataException {
        // check if an item at that locationID exists
        if(dao.getItem(locationID) == null) {
            throw new InvalidDataException (
                "ERROR: Invalid location ID: " + locationID 
                + ". No existing item to update at " + locationID + ".");
        }
        Item deletedItem = dao.deleteItem(locationID);
        auditDao.writeAuditEntry("Item " + deletedItem.getLocationID() + " : " +
                deletedItem.getName() + " DELETED.");
        return deletedItem;
    }
    
   
    /**
     * Checks that item is available and that buyer has paid enough, then
     * updates the Item quantity available and returns a Change object.
     * 
     * @param locationID - String
     * @param remMoney - BigDecimal representing monetary value
     * @return Change 
     * @throws PersistenceException
     * @throws NoItemInventoryException
     * @throws InsufficientFundsException
     * @throws InvalidDataException 
     */
    @Override
    public Change sellItem(String locationID, BigDecimal remMoney) throws 
            PersistenceException,
            NoItemInventoryException,
            InsufficientFundsException,
            InvalidDataException {
        
        Item itemForSale;
        BigDecimal itemPrice;
        
        // Check if Item at locationID exists ( != null)
        try {
            itemForSale = dao.getItem(locationID);
            itemPrice = itemForSale.getPrice();
        } catch (NullPointerException e) {
            throw new InvalidDataException (
                    "ERROR: Invalid location ID: " + locationID 
                        + ". No item exists at that location.");
        }
              
        // Check if Item at locationID has quantity available
        if(itemForSale.getNumAvailable() == 0) {
            throw new NoItemInventoryException("ERROR: Item OUT OF STOCK.");
        }
        
        // Check if remMoney < item.getPrice()
        if(remMoney.compareTo(itemPrice) < 0 ) {
            throw new InsufficientFundsException("ERROR: Insufficient Funds. "
                    + "Insert more money to purchase " 
                    + itemForSale.getName() + ".");
        }  
        
        // update the item quantity
        itemForSale.setNumAvailable(itemForSale.getNumAvailable() - 1);
        updateItem(locationID, itemForSale);
        
        // Create a Change object to calculate change and return it
        Change change = new Change(remMoney.subtract(itemPrice));
        
        return change;
    }
    
    
    /**
     * Checks that all required Item data fields have been gathered. 
     * 
     * @param item - Item
     * @throws InvalidDataException 
     */
    private void validateRequiredItemData(Item item) throws InvalidDataException {
        if(item.getName() == null 
                || item.getName().trim().length() == 0
                || item.getPrice() == null) {
            
            throw new InvalidDataException(
                    "ERROR: Missing required Item fields [Name, Price].");
        }     
    }
    
}
