
package com.sblinn.vendingmachine.service;

/**
 *
 * @author sarablinn
 */
public class DuplicateLocationIDException extends Exception {
    
    public DuplicateLocationIDException(String message) {
        super(message);
    }
    
    public DuplicateLocationIDException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
