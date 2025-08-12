package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.service;

import java.util.Comparator;
import java.util.List;

import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dao.ProductDAO;
import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dto.Product;

public class ProductService {
	private ProductDAO productDao=new ProductDAO();
	
	public boolean addProduct(Product product) {
		return productDao.addProduct(product);
	}
	
	public List<Product> viewAllProducts() {
        return productDao.getAllProducts();
    }
	
	public List<Product> viewProductsByCategory(String category) {
        return productDao.getProductsByCategory(category);
    }
	
	public List<Product> sortProducts(Comparator<Product> comparator) {
        List<Product> sortedProducts=productDao.getAllProducts();    
        sortedProducts.sort(comparator);
        return sortedProducts;
    }
}