package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dto.Product;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.exceptions.RetailException;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.response.Response;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.service.ProductService;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.util.Comparators;

public class ProductController {
	private ProductService productService=new ProductService();
	
	public Response addProduct(Product product) {		
//		Perform input validations
		try {
			boolean productAdded=productService.addProduct(product);
	        if(productAdded) {
	        	return new Response(200,"Product added");
	        }
	        return new Response(500,"Product not added");
		}
        catch(RetailException e) {
        	return new Response(504,"Product addition failed "+e.getMessage());
        }
    }
	
	public Response viewAllProducts() {
		try {
			List<Product> products = productService.viewAllProducts();
			if(products.isEmpty()) {
				return new Response(500,"No products available");
			}
			return new Response(200,products.stream().map(Product::toString).collect(Collectors.joining("\n")));
		}
		catch(RetailException e) {
			return new Response(504,"Fetching product details failed "+e.getMessage());
		}
	}
	
	public Response viewProductsByCategory(String category) {
//      Perform category validation
		try {
			List<Product> products = productService.viewProductsByCategory(category);
	        if(products.isEmpty()) {
				return new Response(500,"No products available for the category");
			}
	        return new Response(200,products.stream().map(Product::toString).collect(Collectors.joining("\n")));
		}
		catch(RetailException e) {
			return new Response(504,"Fetching products failed "+e.getMessage());
		}
         
    }

	public Response sortProductsInAscending() {
		Comparator<Product> comparator=Comparators.priceAscending;
		try {
			List<Product> sortedProducts = productService.sortProducts(comparator);
	        if(sortedProducts.isEmpty()) {
				return new Response(500,"No products available");
			}
	        return new Response(200,sortedProducts.stream().map(Product::toString).collect(Collectors.joining("\n")));
		}
	    catch(RetailException e) {
			return new Response(504,"Product sorting failed "+e.getMessage());
        }
	}
	
	public Response sortProductsInDescending() {
		Comparator<Product> comparator=Comparators.priceDescending;
		try {
			List<Product> sortedProducts = productService.sortProducts(comparator);
	        if(sortedProducts.isEmpty()) {
				return new Response(500,"No products available");
			}
	        return new Response(200,sortedProducts.stream().map(Product::toString).collect(Collectors.joining("\n")));
		}
	    catch(RetailException e) {
			return new Response(504,"Product sorting failed "+e.getMessage());
        }
	}
	
	public Response sortProductsByNamesInAscending() {
		Comparator<Product> comparator=Comparators.nameAscending;
		try {
			List<Product> sortedProducts = productService.sortProducts(comparator);
	        if(sortedProducts.isEmpty()) {
				return new Response(500,"No products available");
			}
	        return new Response(200,sortedProducts.stream().map(Product::toString).collect(Collectors.joining("\n")));
		}
	    catch(RetailException e) {
			return new Response(504,"Product sorting failed "+e.getMessage());
        }
	}
	
	public Response sortProductsByNamesInDescending() {
		Comparator<Product> comparator=Comparators.nameDescending;
		try {
			List<Product> sortedProducts = productService.sortProducts(comparator);
	        if(sortedProducts.isEmpty()) {
				return new Response(500,"No products available");
			}
	        return new Response(200,sortedProducts.stream().map(Product::toString).collect(Collectors.joining("\n")));
		}
	    catch(RetailException e) {
			return new Response(504,"Product sorting failed "+e.getMessage());
        }
        
	}
}

