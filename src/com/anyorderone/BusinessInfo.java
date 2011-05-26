package com.anyorderone;

import java.util.Date;

/**
 * Simple data bean to represent a Review - no getters and setters on purpose
 * 
 */
public class BusinessInfo {

    public String address;
    public String name;
    public String desc; 
    public String type;
    public String  id;
    public String phone;
    public String state;
    public String city;    
    public String zip;
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("*Business info *\n");
        sb.append("name:" + this.name + ",");
        sb.append("Descr:" + this.desc+ ",");
        sb.append("Address" + this.address + ",");
        sb.append("city" + this.city + ",");
        sb.append("State" + this.state + ",");
        sb.append("zip" + this.zip + ",");
        sb.append("Phone" + this.phone + ",");
        
        return sb.toString();
    }
}
