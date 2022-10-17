
package com.sblinn.vendingmachine.controller;

import com.sblinn.vendingmachine.dao.PersistenceException;
import com.sblinn.vendingmachine.dto.Change;
import com.sblinn.vendingmachine.dto.Item;
import com.sblinn.vendingmachine.service.InsufficientFundsException;
import com.sblinn.vendingmachine.service.InvalidDataException;
import com.sblinn.vendingmachine.service.NoItemInventoryException;
import com.sblinn.vendingmachine.service.VendingMachineServiceLayer;
import com.sblinn.vendingmachine.ui.VendingMachineView;
import java.math.BigDecimal;
import java.util.List;

/**
 *
 * @author sarablinn
 */
public class VendingMachineController {
    
    private VendingMachineView view;
    private VendingMachineServiceLayer service;
    private Change remBalance;
    
    
    public VendingMachineController(VendingMachineView view, 
            VendingMachineServiceLayer service) {
    
        this.view = view;
        this.service = service;
        this.remBalance = new Change(0,0,0,0);
    }
    
    public void run() {
        
        boolean keepGoing = true;
        
        try {
            while(keepGoing) {
                int menuSelection = getMenuSelection();
                
                switch(menuSelection) {
                    case 1:
                        insertCoins();
                        break;
                    case 2:
                        purchaseItem();
                        break;
                    case 3:
                        keepGoing = false;
                        break;                
                    default:
                        unknownCommand();
                }
            } exitMessage();
            
        } catch(PersistenceException e) {
            view.displayErrorMessage(e.getMessage());
        } catch(InvalidDataException e) {
            // ignore, currently no use for user 
        }
    }
    
    private int getMenuSelection() throws PersistenceException {
        List<Item> itemList = service.getAllItems();
        String remBalanceStr = remBalance.getChangeSum().toString();
        
        return view.printVendingMachineMenuAndGetSelection(
                itemList, remBalanceStr);
    }
    
    private void insertCoins() {
        view.displayInsertCoinsBanner();
        this.remBalance = this.remBalance.addChange(view.getInsertedMoney());
        viewBalance();
        view.displayContinuePrompt();
    }
    
    private void purchaseItem() throws 
            PersistenceException, 
            InvalidDataException { 
        // No need to catch InvalidDataException, method just updates stock
        view.displayPurchaseItemBanner();
        
        String locationID;
        BigDecimal balance;
        
        boolean keepGoing = false;
        while (keepGoing == false) {
            try {
                locationID = view.getItemSelection();
                service.getItem(locationID);
                balance = remBalance.getChangeSum();
                remBalance = service.sellItem(locationID, balance);
                view.displayPurchaseSuccessBanner();
                refundChange();
                view.displayContinuePrompt();
                keepGoing = true;
            } catch(InvalidDataException e) {
                view.displayErrorMessage(e.getMessage());
            // exits the purchase sale when item out of stock 
            // or user needs more money
            } catch(NoItemInventoryException e) {
                view.displayErrorMessage(e.getMessage());
                view.displayContinuePrompt();
                keepGoing = true;
            } catch(InsufficientFundsException e) {
                view.displayErrorMessage(e.getMessage());
                viewBalance();
                view.displayContinuePrompt();
                keepGoing = true;
            }
        } 
    }
    
    private void viewBalance() {
        view.displayBalance(remBalance.getChangeSum().toString());
    }
    
    private void refundChange() {
        view.displayReturnChangeBanner();
        view.displayChangeDueResult(remBalance);
        remBalance = new Change(0,0,0,0);
    }
    
    private void unknownCommand() {
        view.displayUnknownCommandBanner();
    }
    
    private void exitMessage() {
        view.displayExitBanner();
    }
    
}
