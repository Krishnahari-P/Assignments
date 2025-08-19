package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.exceptions;

public class RetailException extends Exception{
	public RetailException(String errorMessage) {
		super(errorMessage);
	}
	
	public RetailException(String errorMessage,Throwable cause) {
		super(errorMessage,cause);
	}
}