
package com.sblinn.vendingmachine.service;

/**
 * Exception thrown when the user tries to purchase an item that has 
 * zero inventory.
 * 
 * @author sarablinn
 */
public class NoItemInventoryException extends Exception {
 
    public NoItemInventoryException(String message) {
        super(message);
    }

    public NoItemInventoryException(String message, Throwable cause) {
        super(message, cause);
    }
}
