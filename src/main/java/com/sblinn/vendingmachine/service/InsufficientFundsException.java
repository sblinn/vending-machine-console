
package com.sblinn.vendingmachine.service;

/**
 * Exception thrown when the user tries to purchase an item but doesn't 
 * deposit enough money.
 * 
 * @author sarablinn
 */
public class InsufficientFundsException extends Exception {
    
    public InsufficientFundsException(String message) {
        super(message);
    }

    public InsufficientFundsException(String message, Throwable cause) {
        super(message, cause);
    }
}
