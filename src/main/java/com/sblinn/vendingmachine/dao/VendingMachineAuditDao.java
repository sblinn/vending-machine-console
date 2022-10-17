
package com.sblinn.vendingmachine.dao;

/**
 *
 * @author sarablinn
 */
public interface VendingMachineAuditDao {
    
    /**
     * Writes an audit entry to the audit log. 
     * 
     * @param entry - String
     * @throws PersistenceException 
     */
    public void writeAuditEntry(String entry) throws PersistenceException;
    
}
