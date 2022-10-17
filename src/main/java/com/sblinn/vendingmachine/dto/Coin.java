
package com.sblinn.vendingmachine.dto;

import java.math.BigDecimal;

/**
 *
 * @author sarablinn
 */
public enum Coin {
    
    PENNY(new BigDecimal("0.01")), 
    NICKEL(new BigDecimal("0.05")), 
    DIME(new BigDecimal("0.10")), 
    QUARTER(new BigDecimal("0.25"));
    
    
    private final BigDecimal value;
    
    private Coin(BigDecimal value) {
        this.value = value;
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
}
