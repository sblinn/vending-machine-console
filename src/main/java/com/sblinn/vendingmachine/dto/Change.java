
package com.sblinn.vendingmachine.dto;

import java.math.BigDecimal;


/**
 * A Change object stores the number of quarters, dimes, nickels, and pennies 
 * in change. When created using an integer or a BigDecimal value, the change is
 * calculated using the fewest number of coins necessary.
 * 
 * @author sarablinn
 */
public class Change {
    
    private int numQuarters, numDimes, numNickels, numPennies = 0;
 
    
    public Change(int changeInPennies) {
        calculateChange(changeInPennies);
    }
    
    public Change(BigDecimal change) {
        String changeStr = change.toString().replace(".", "");
        int changeInPennies = Integer.parseInt(changeStr);
        calculateChange(changeInPennies);
    }
    
    public Change(int numQuarters, int numDimes, int numNickels, int numPennies) {
        this.numQuarters = numQuarters;
        this.numDimes = numDimes;
        this.numNickels = numNickels;
        this.numPennies = numPennies;
    }
           
    
    private void calculateChange(int changeInPennies) {
        int remChange = changeInPennies;
        
        while(remChange != 0) {
            while(remChange - 25 >= 0) {
                this.numQuarters++;
                remChange -= 25;
            }
            while(remChange - 10 >= 0) {
                this.numDimes++;
                remChange -= 10;
            }
            while(remChange - 5 >= 0) {
                this.numNickels++;
                remChange -= 5; 
            }
            while(remChange - 1 >= 0) {
                this.numPennies++;
                remChange -= 1;
            }
        }   
    }
    
    
    /**
     * Calculates and returns the BigDecimal sum value of the change.
     * 
     * @return sum - BigDecimal 
     */
    public BigDecimal getChangeSum() {
        BigDecimal quarters = new BigDecimal(this.numQuarters);
        BigDecimal dimes = new BigDecimal(this.numDimes);
        BigDecimal nickels = new BigDecimal(this.numNickels);
        BigDecimal pennies = new BigDecimal(this.numPennies);
        
        quarters = quarters.multiply(Coin.QUARTER.getValue()).setScale(2);
        dimes = dimes.multiply(Coin.DIME.getValue()).setScale(2);
        nickels = nickels.multiply(Coin.NICKEL.getValue()).setScale(2);
        pennies = pennies.multiply(Coin.PENNY.getValue()).setScale(2);
        
        BigDecimal sum = quarters.add(dimes).add(nickels).add(pennies);
        
        return sum;
    }
    
    /**
     * Combines this Change object with the change from another Change object. 
     * 
     * @param change
     * @return Change - returns this Change object
     */
    public Change addChange(Change change) {
        this.numQuarters += change.getNumQuarters();
        this.numDimes += change.getNumDimes();
        this.numNickels += change.getNumNickels();
        this.numPennies += change.getNumPennies();
        
        return this;    
    }
    

    public int getNumQuarters() {
        return numQuarters;
    }

    public int getNumDimes() {
        return numDimes;
    }

    public int getNumNickels() {
        return numNickels;
    }

    public int getNumPennies() {
        return numPennies;
    }
    
}
