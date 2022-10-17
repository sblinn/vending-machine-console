
package com.sblinn.vendingmachine.ui;

import com.sblinn.vendingmachine.dto.Change;
import com.sblinn.vendingmachine.dto.Item;
import java.util.List;

/**
 *
 * @author sarablinn
 */
public class VendingMachineView {
    
    private UserIO io;
    
    
    public VendingMachineView(UserIO io) {
        this.io = io;
    }
 
    
    public int printVendingMachineMenuAndGetSelection(List<Item> itemList, 
            String remBalance) {
        
        io.print("=== VENDING MACHINE ===");
        displayAllItems(itemList, remBalance);
        io.print("=======================");
        io.print("1. Insert Coins");
        io.print("2. Purchase an Item");
        io.print("3. Exit");

        return io.readInt("Please select from the above choices.", 1, 3);
    }
    
    /**
     * Displays all items and remaining balance to the console.
     * 
     * @param itemList 
     */
    public void displayAllItems(List<Item> itemList, String remBalance) {
        for(Item currentItem : itemList) {
            String itemInfo = String.format("# %s : %s (%s in stock) : $%s",
                    currentItem.getLocationID(),
                    currentItem.getName(),
                    currentItem.getNumAvailable(),
                    currentItem.getPrice() );
            io.print(itemInfo);
        }
        displayBalance(remBalance);
    }
    
    public String getItemSelection() {
        return io.readString("Please enter the item's location ID: ");
    }
    
    public Change getInsertedMoney() {
        int quarters = io.readInt("Enter number of quarters: ");
        int dimes = io.readInt("Enter number of dimes: ");
        int nickels = io.readInt("Enter number of nickels: ");
        int pennies = io.readInt("Enter number of pennies: ");
        
        Change change = new Change(quarters, dimes, nickels, pennies);
        return change;
    }
    
    public void displayChangeDueResult(Change change) {
        io.print("Quarters: " + change.getNumQuarters());
        io.print("Dimes: " + change.getNumDimes());
        io.print("Nickels: " + change.getNumNickels());
        io.print("Pennies: " + change.getNumPennies());
        
        String changeStr = change.getChangeSum().toString();
        
        io.print("Total change returned: $" + changeStr);
    }
    
    public void displayBalance(String remMoney) {
        io.print("Balance: $" + remMoney);
    }
    
    public void displayContinuePrompt() {
        io.readString("Please hit ENTER to continue.");
    }
        
    public void displayErrorMessage(String errorMsg) {
        io.print(errorMsg);
    }
    
    public void displayInsertCoinsBanner() {
        io.print("=== INSERT COINS ===");
    }
    
    public void displayPurchaseItemBanner() {
        io.print("=== PURCHASE ITEM ===");
    }
    
    public void displayPurchaseSuccessBanner() {
        io.print("=== ITEM PURCHASE SUCCESSFUL ===");
    }
    
    public void displayReturnChangeBanner() {
        io.print("=== CHANGE ===");
    }
    
    public void displayAllItemsBanner() {
        io.print("=== VENDING MACHINE ITEMS ===");
    }
    
    public void displayExitBanner() {
        io.print("Good Bye.");
    }
    
    public void displayUnknownCommandBanner() {
        io.print("UNKNOWN COMMAND");
    }
    
}
