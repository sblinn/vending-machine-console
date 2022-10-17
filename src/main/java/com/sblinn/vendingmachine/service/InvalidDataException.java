
package com.sblinn.vendingmachine.service;

/**
 * Exception thrown when Item is missing required fields or invalid location ID
 * is used. 
 * 
 * @author sarablinn
 */
public class InvalidDataException extends Exception {
    
    public InvalidDataException(String message) {
        super(message);
    }
            
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
