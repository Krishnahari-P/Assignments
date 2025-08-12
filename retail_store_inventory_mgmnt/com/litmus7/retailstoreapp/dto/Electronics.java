package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dto;

public class Electronics extends Product {
	private String brand;
    private int warrantyMonths;
    
	public Electronics(String productId, String productName, double price, String status, String productCategory,String brand, int warrantyMonths) {
		super(productId, productName, price, status, productCategory);
		this.brand = brand;
        this.warrantyMonths = warrantyMonths;
	}
	
	public String getBrand() { 
		return brand;
	}
    public int getWarrantyMonths() {
    	return warrantyMonths; 
    }

	
    
}
