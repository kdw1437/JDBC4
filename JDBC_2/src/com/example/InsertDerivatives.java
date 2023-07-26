package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class InsertDerivatives {

    private static final String DB_URL = "jdbc:oracle:thin:@//localhost:1521/orclpdb";
    private static final String USER = "SYSTEM";
    private static final String PASS = "1437";

    public static void main(String[] args) {
        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // Connect to the database
            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
                
                // Turn off auto-commit to handle transactions manually
                conn.setAutoCommit(false);

                // Prepare statement to insert data into the 'Derivative' table
                String insertDataSQL = "INSERT INTO OT.DERIVATIVE (ID, STRIKE_PRICE, SPOT_PRICE, VOLATILITY, INTEREST_RATE, EXPIRY_DATE) VALUES (?, ?, ?, ?, ?, ?)";
                try (PreparedStatement pstmt = conn.prepareStatement(insertDataSQL)) {

                    // First derivative
                    pstmt.setInt(1, 2);  // Assuming ID 1 is already in use from your previous code
                    pstmt.setDouble(2, 110.00);
                    pstmt.setDouble(3, 115.00);
                    pstmt.setDouble(4, 0.21);
                    pstmt.setDouble(5, 0.04);
                    pstmt.setDate(6, java.sql.Date.valueOf("2024-06-30"));
                    pstmt.addBatch();

                    // Second derivative
                    pstmt.setInt(1, 3);
                    pstmt.setDouble(2, 115.00);
                    pstmt.setDouble(3, 120.00);
                    pstmt.setDouble(4, 0.25);
                    pstmt.setDouble(5, 0.05);
                    pstmt.setDate(6, java.sql.Date.valueOf("2024-09-30"));
                    pstmt.addBatch();

                    // Third derivative
                    pstmt.setInt(1, 4);
                    pstmt.setDouble(2, 120.00);
                    pstmt.setDouble(3, 125.00);
                    pstmt.setDouble(4, 0.28);
                    pstmt.setDouble(5, 0.03);
                    pstmt.setDate(6, java.sql.Date.valueOf("2024-12-31"));
                    pstmt.addBatch();

                    // Execute the batch
                    int[] count = pstmt.executeBatch();

                    // Commit the transaction
                    conn.commit();

                    System.out.println("All derivatives inserted successfully!");

                } catch (Exception innerEx) {
                    // In case of an exception, rollback the transaction
                    conn.rollback();
                    throw innerEx;  // rethrow the exception for the outer catch block
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


