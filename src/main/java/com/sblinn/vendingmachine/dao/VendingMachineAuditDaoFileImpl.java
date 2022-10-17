
package com.sblinn.vendingmachine.dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;

/**
 *
 * @author sarablinn
 */
public class VendingMachineAuditDaoFileImpl implements VendingMachineAuditDao {

    public static final String AUDIT_FILE = "audit.txt";
    
    /**
     * Writes a timestamped audit entry to the audit log text file.
     * 
     * @param entry - String
     * @throws PersistenceException 
     */
    @Override
    public void writeAuditEntry(String entry) throws PersistenceException {
        PrintWriter out;
        
        try {
            out = new PrintWriter(new FileWriter(AUDIT_FILE, true));
        } catch(IOException e) {
            throw new PersistenceException(
                    "Unable to persist audit information.", e);
        }
        
        LocalDateTime timestamp = LocalDateTime.now();
        out.println(timestamp.toString() + " : " + entry);
        out.flush();
    }
    
    
}
