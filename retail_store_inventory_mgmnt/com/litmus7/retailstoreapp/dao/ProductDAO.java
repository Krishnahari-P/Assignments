package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dao;

import java.util.ArrayList;
import java.util.List;

import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dto.Product;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.exceptions.RetailException;

public class ProductDAO {
	
	public boolean addProduct(Product product) throws RetailException {
		try {
//			Write product details to a file using BufferedWriter
//			verify whether the data entered for the productId of the product
//			Use productExists() method. If no, throw RetailDaoException
			return true;
		}
		catch(Exception e) {
			throw new RetailException("Adding product failed",e);
		}
	}
	
	public List<Product> getAllProducts() throws RetailException{
		List<Product> productList=new ArrayList<>();
		try {
			//Fetch details of all products and add to list
		}
		catch(Exception e) {
			//catch proper exception and handle it 
			throw new RetailException("Fetching failed",e);
		}
		return productList;
	}
	
	public List<Product> getProductsByCategory(String category) throws RetailException{
		try {
			List<Product> products=new ArrayList<>();
			//Filter products by category from file
			//add the products details to list
			return products;
		}
		catch(Exception e) {
			throw new RetailException("Fetching failed",e);
		}
		
	}
	
	public boolean productExists(Product product) {
		String productId=product.getProductId();
//		check if the productId exists
//		If exists return true
		return false;
	}
	
	public boolean categoryExists(String category) {
//		Check if provided category is present in the file
//		if present return true
		return false;
	}
}
