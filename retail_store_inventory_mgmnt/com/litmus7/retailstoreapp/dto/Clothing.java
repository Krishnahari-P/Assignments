package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dto;

public class Clothing extends Product{

	private String material;
	private String size;

	public Clothing(String productId, String productName, double price, String status, String productCategory, String size, String material) {
		super(productId, productName, price, status, productCategory);
		this.size=size;
		this.material=material;
	}
	
	public String getSize() { 
		return size;
	}
    public String getMaterial() { 
    	return material; 
    }

}

