package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp;

import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.controller.ProductController;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dto.Clothing;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dto.Product;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.response.Response;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.util.Comparators;

public class RetailStoreApp {
	public static void main(String[] args) {
		ProductController productController=new ProductController();
		Product product=new Clothing("1","Kurtha",2000,null,"Clothing","XL","Cotton");
		addProduct(productController,product);
		viewAllProducts(productController);
		viewProductsByCategory(productController,"Electronics");
		sortProductsInAscending(productController);
		sortProductsByNames(productController);
	}
	public static void addProduct(ProductController productController,Product product) {
		Response response=productController.addProduct(product);
		
		
	}
	public static void viewAllProducts(ProductController productController) {
		Response response=productController.viewAllProducts();
//		Print the responses
	}
	public static void viewProductsByCategory(ProductController productController,String category) {
		Response response=productController.viewProductsByCategory(category);
//      Print the responses
	}
	public static void sortProductsInAscending(ProductController productController) {
		Response response=productController.sortAndDisplayProducts(Comparators.priceAscending);
//		print the responses
	}
	public static void sortProductsInDescending(ProductController productController) {
		Response response=productController.sortAndDisplayProducts(Comparators.priceDescending);
//		print the responses
	}
	
	public static void sortProductsByNames(ProductController productController) {
		Response response=productController.sortAndDisplayProducts(Comparators.nameAscending);
//		print the responses
	}
	
	
}

