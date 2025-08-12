package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dto;

public abstract class Product {
	private String productId;
	private String productName;
	private double price;
	private String status;
	private String productCategory;
	
	public Product(String productId, String productName, double price, String status, String productCategory) {
	    this.productId = productId;
	    this.productName = productName;
	    this.price = price;
	    this.status = status;
	    this.productCategory=productCategory;
	}
	
	public String getProductId() { 
		return productId;
	}
    public String getProductName() { 
    	return productName;
    }
    public double getPrice() { 
    	return price; 
    }
    public String getStatus() {
    	return status;
    }
    public String getProductCategory() {
    	return productCategory;
    }

    public void setProductId(String productId) { 
    	this.productId = productId;
    }
    public void setProductName(String productName) { 
    	this.productName = productName;
    }
    public void setPrice(double price) { 
    	this.price = price;
    }
    public void setStatus(String status) { 
    	this.status = status;
    }
    public void setProductCategory(String productCategory) {
    	this.productCategory=productCategory;
    }
}
