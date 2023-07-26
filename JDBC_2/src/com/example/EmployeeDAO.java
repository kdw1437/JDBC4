package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Arrays;
import java.util.stream.Collectors;

public class EmployeeDAO { //Class EmployeeDAO 선언

    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/orclpdb"; //EmployeeDAO의 Private한 필드 선언
    private static final String USER = "SYSTEM"; //EmployeeDAO의 Private한 필드 선언
    private static final String PASS = "1437";
    //private: member is only accessible within the class in which it is declared.
    //static: This keyword indicates that the member belongs to the class itself, rather than to instances of the class.
    //final: This keyword means that the value of the variable cannot be changed after it's been initialized.
    private Connection conn;
    //private한 필드 선언, Connection class
    //EmployeeDAO class의 생성자
    public EmployeeDAO() throws Exception {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        conn = DriverManager.getConnection(DB_URL, USER, PASS); //This line establishes a connection to the database.
    }
    // make instance method, createEmployee (Create in CRUD), declare method with 'throws Exception'
    //However, a more refined approach would be to specify the actual type of exception that might be thrown.
    //This provides better clarity to the callers of the method about what specific exceptions they should be prepared to handle.
    public void createEmployee(int employeeId, String firstName, String lastName, String email, String phone, String hireDate, int managerId, String jobTitle, double salary) throws Exception {
        //The resource specified within the parantheses after try is automatically closed when the try block is exited.
    	//This helps prevent resource leaks and makes the code cleaner since you don't need an explicit 'finally' block
    	//to close the resource
    	
    	//For an object to be used as a resource in try-with-resources, it must implement the 'AutoCloseable' or 'Closeable'
    	//interface, which means it has a close() method to release the resource or terminate the connection.
    	
    	try (PreparedStatement pstmt = conn.prepareStatement("INSERT INTO OT.EMPLOYEES (EMPLOYEE_ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE, HIRE_DATE, MANAGER_ID, JOB_TITLE, SALARY) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
            pstmt.setInt(1, employeeId);
            pstmt.setString(2, firstName);
            pstmt.setString(3, lastName);
            pstmt.setString(4, email);
            pstmt.setString(5, phone);
            pstmt.setString(6, hireDate);
            pstmt.setInt(7, managerId);
            pstmt.setString(8, jobTitle);
            pstmt.setDouble(9, salary);
            pstmt.executeUpdate();
        }
    }
    //retrieveEmployee method가 return하는 값은 ResultSet type이다.
    public ResultSet retrieveEmployee(int... employeeIds) throws Exception {
        Statement stmt = conn.createStatement();
        
        // Convert each individual integer to a String
        String ids = Arrays.stream(employeeIds)
                           .mapToObj(String::valueOf)
                           .collect(Collectors.joining(","));
        
        return stmt.executeQuery("SELECT * FROM OT.EMPLOYEES WHERE EMPLOYEE_ID IN (" + ids + ")");
    }

    public void updateEmployee(int employeeId, double newSalary, String newJobTitle) throws Exception {
        try (PreparedStatement pstmt = conn.prepareStatement("UPDATE OT.EMPLOYEES SET SALARY = ?, JOB_TITLE = ? WHERE EMPLOYEE_ID = ?")) {
            pstmt.setDouble(1, newSalary);
            pstmt.setString(2, newJobTitle);
            pstmt.setInt(3, employeeId);
            pstmt.executeUpdate();
        }
    }

    public void deleteEmployee(int employeeId) throws Exception {
        try (PreparedStatement pstmt = conn.prepareStatement("DELETE FROM OT.EMPLOYEES WHERE EMPLOYEE_ID = ?")) {
            pstmt.setInt(1, employeeId);
            pstmt.executeUpdate();
        }
    }

    public void close() throws Exception {
        conn.close();
    }
}