package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dto;

import java.util.Date;

public class Grocery extends Product{
	private Date expiryDate;
    private double weightKg;
    
	public Grocery(String productId, String productName, double price, String status, String productCategory,Date expiryDate,double weightKg) {
		super(productId, productName, price, status, productCategory);
		this.expiryDate=expiryDate;
		this.weightKg=weightKg;
	}
	
	public Date getExpiryDate() { 
		return expiryDate; 
	}
    public double getWeightKg() {
    	return weightKg;
    }
}

