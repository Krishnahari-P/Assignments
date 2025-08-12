package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dao;

import java.util.ArrayList;
import java.util.List;

import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dto.Product;

public class ProductDAO {
	
	public boolean addProduct(Product product) {
		try {
			//Write product details to a file using BufferedWriter
		}
		catch(Exception e) {
			return false;
		}
		
		//verify the insertion
		
		try {
			//verify whether the data exists for the productId of the product
			return true;
		}
		catch(Exception e) {
			return false;
		}
	}
	
	public List<Product> getAllProducts(){
		List<Product> productList=new ArrayList<>();
		try {
			//Fetch details of all products and add to list
		}
		catch(Exception e) {
			//catch proper exception and handle it 
		}
		return productList;
	}
	
	public List<Product> getProductsByCategory(String category){
		List<Product> productList=new ArrayList<>();
		//Filter products by category from file
		//add the products details to list
		return productList;
		
	}
}
