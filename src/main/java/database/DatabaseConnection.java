package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/rental_management?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    static {
        try {
            // Explicitly load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Handle the case when the MySQL JDBC driver is not found
            System.err.println("MySQL JDBC Driver not found. Please add the driver to the classpath.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        try {
            // Attempt to get a connection to the database
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            // Log and rethrow the SQLException
            System.err.println("Failed to connect to the database. Please check your connection settings.");
            throw e;
        }
    }
}
