package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.service;

import java.util.Comparator;
import java.util.List;

import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dao.ProductDAO;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dto.Product;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.exceptions.RetailException;

public class ProductService {
	private ProductDAO productDao=new ProductDAO();
	
	public boolean addProduct(Product product) throws RetailException {
		try {
			if(productDao.productExists(product)) {
				throw new RetailException("Product already exists");
			}	
			return productDao.addProduct(product);
		}
		catch(Exception e) {
//			Catch suitable exception
			throw new RetailException("Produt addition failed",e);
		}
	}
	
	public List<Product> viewAllProducts() throws RetailException {
		try {
			List<Product> products=productDao.getAllProducts();
			return products;
		}
		catch(Exception e) {
//			Catch suitable exception
			throw new RetailException("Product fetching failed",e);
		}
    }
	
	public List<Product> viewProductsByCategory(String category) throws RetailException {
		try {
			if(!productDao.categoryExists(category)) {
				throw new RetailException("Category don't exists");
			}
			List<Product> products=productDao.getProductsByCategory(category);
			return products;
		}
		catch(Exception e) {
//			Catch suitable exception
			throw new RetailException("Product fetching failed",e);
		} 
    }
	
	public List<Product> sortProducts(Comparator<Product> comparator) throws RetailException {
		try {
//			If comparator is empty, throw custom exception
			List<Product> products=productDao.getAllProducts();    
	        products.sort(comparator);
	        return products;
		}
        catch(Exception e) {
//			Catch suitable exception
			throw new RetailException("Product sorting failed",e);
        }
    }
}