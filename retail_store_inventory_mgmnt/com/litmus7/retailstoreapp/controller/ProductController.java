package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.controller;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dto.Product;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.response.Response;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.service.ProductService;

public class ProductController {
	public ProductService productService=new ProductService();
	
	public Response addProduct(Product product) {
		
//		Perform input validations
        boolean productAdded=productService.addProduct(product);
        if(productAdded) {
        	return new Response(200,"Product added");
        }
        return new Response(500,"Product not added");
    }
	
	public Response viewAllProducts() {
		List<Product> products = productService.viewAllProducts();
		if(products.isEmpty()) {
			return new Response(500,"No products available");
		}
		return new Response(200,products.stream().map(Product::toString).collect(Collectors.joining("\n")));
	}
	
	public Response viewProductsByCategory(String category) {
//      Perform category validation
        List<Product> products = productService.viewProductsByCategory(category);
        if(products.isEmpty()) {
			return new Response(500,"No products available for the category");
		}
        return new Response(200,products.stream().map(Product::toString).collect(Collectors.joining("\n"))); 
    }
	
	public Response sortAndDisplayProducts(Comparator<Product> comparator) {
		// If no comparator is provided, return empty list
        List<Product> sortedProducts = productService.sortProducts(comparator);
        if(sortedProducts.isEmpty()) {
			return new Response(500,"No products available");
		}
        return new Response(200,sortedProducts.stream().map(Product::toString).collect(Collectors.joining("\n")));
    }
}

