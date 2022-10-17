
package com.sblinn.vendingmachine.service;

import com.sblinn.vendingmachine.dao.PersistenceException;
import com.sblinn.vendingmachine.dao.VendingMachineAuditDao;

/**
 * AuditDao Stub implementation used in lieu of the actual AuditDao for testing 
 * so that the service layer will be able to be tested without making changes to
 * the audit file. 
 * 
 * @author sarablinn
 */
public class VendingMachineAuditDaoStubImpl implements VendingMachineAuditDao {

    @Override
    public void writeAuditEntry(String entry) throws PersistenceException {

        // do nothing

    }
    
    
}
