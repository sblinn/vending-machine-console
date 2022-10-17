
package com.sblinn.vendingmachine.dao;

/**
 *
 * @author sarablinn
 */
public class PersistenceException extends Exception {
    
    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
