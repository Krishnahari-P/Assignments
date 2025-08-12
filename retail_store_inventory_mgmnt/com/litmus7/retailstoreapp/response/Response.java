package Assignments.retail_store_inventory_mgmnt.com.litmus7.retailstoreapp.response;

public class Response {
	int statusCode;
	String message;
	public Response(int statusCode,String message) {
		this.statusCode=statusCode;
		this.message=message;
	}
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public String getMessage() {
		return message;
	}
}