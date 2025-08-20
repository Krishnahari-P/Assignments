package com.litmus7.employeeManager.util;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.LogManager;

import com.litmus7.employeeManager.constants.ErrorCode;
import com.litmus7.employeeManager.exceptions.EmployeeManagerException;

public class DBConnection {
    private static final Logger logger=LogManager.getLogger(DBConnection.class);
    public static Connection getConnection() throws EmployeeManagerException {
		logger.trace("Entering getConnection()");
		
	    Properties properties = new Properties();
	    try (FileInputStream fis = new FileInputStream("src\\com\\litmus7\\resources\\db.properties")) {
	        properties.load(fis);
	    }
	    catch (IOException e) {
	    	logger.error("Failed to load database properties", e);
	        throw new EmployeeManagerException(ErrorCode.DATABASE_ERROR, e);
	    }

	    String URL = properties.getProperty("jdbc.url");
	    String USERNAME = properties.getProperty("jdbc.username");
	    String PASSWORD = properties.getProperty("jdbc.password");

	    try {
	    	logger.debug("Connecting to DB with URL: {}",URL);
			return DriverManager.getConnection(URL, USERNAME, PASSWORD);
		} 
	    catch(SQLException e) {
	    	logger.error("Database connection failed",e);
			throw new EmployeeManagerException(ErrorCode.DATABASE_ERROR, e);
		}
	}

}
