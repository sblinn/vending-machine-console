
package com.sblinn.vendingmachine.dto;

import java.math.BigDecimal;

import java.util.Objects;

/**
 *
 * @author sarablinn
 */
public class Item {
    
    private String name;
    private BigDecimal price; 
    private int numAvailable;
    private String locationID;
    
    
    public Item(String locationID) {
        this.locationID = locationID;
    }
    
    public Item(String locationID, String name) {
        this.locationID = locationID;
        this.name = name;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getNumAvailable() {
        return numAvailable;
    }

    public void setNumAvailable(int numAvailable) {
        this.numAvailable = numAvailable;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(this.name);
        hash = 47 * hash + Objects.hashCode(this.price);
        hash = 47 * hash + this.numAvailable;
        hash = 47 * hash + Objects.hashCode(this.locationID);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Item other = (Item) obj;
        if (this.numAvailable != other.numAvailable) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        if (!Objects.equals(this.locationID, other.locationID)) {
            return false;
        }
        if (!Objects.equals(this.price, other.price)) {
            return false;
        }
        return true;
    }


    /**
     * Compares locationID Strings of two Items and returns the Item with the 
     * lexicographically greater locationID, or else returns null if they are equal.
     * (Ex. when comparing A and D, D is greater)
     * 
     * @param item
     * @return Item
     */
    public Item compareLocationID(Item item) {
       int comparison = this.locationID.compareTo(item.getLocationID());
       
       if(comparison == 0) {
           return null;
       }
       else if(comparison > 0) {
           return this;
       }
       
       return item;
    }
    
}
