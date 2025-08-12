package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.util;

import java.util.Comparator;

import Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.dto.Product;

public class Comparators {
	public static Comparator<Product> priceAscending = (p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice());
    public static Comparator<Product> priceDescending = (p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice());
    public static Comparator<Product> nameAscending = (p1, p2) -> p1.getProductName().compareToIgnoreCase(p2.getProductName());
}
