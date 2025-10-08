import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Menu-driven Java program for performing CRUD operations (Create, Read, Update, Delete)
 * on a MySQL 'Product' table, featuring explicit transaction handling.
 * * IMPORTANT SETUP NOTES:
 * 1. Ensure MySQL is running and you have a database (e.g., 'retail_db').
 * 2. Create the Product table:
 * CREATE TABLE Product (
 * ProductID INT PRIMARY KEY AUTO_INCREMENT,
 * ProductName VARCHAR(255) NOT NULL,
 * Price DECIMAL(10, 2) NOT NULL,
 * Quantity INT NOT NULL
 * );
 * 3. Update the DB_URL, USER, and PASS constants below with your credentials.
 * 4. Include the MySQL JDBC Connector JAR in your classpath.
 */
public class Main{

    // --- JDBC Connection Parameters (Update these!) ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/retail_db";
    private static final String USER = "root";       // Your MySQL username
    private static final String PASS = "password";   // Your MySQL password

    // --- SQL Statements ---
    private static final String SQL_INSERT = 
        "INSERT INTO Product (ProductName, Price, Quantity) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_ALL = 
        "SELECT ProductID, ProductName, Price, Quantity FROM Product ORDER BY ProductID";
    private static final String SQL_UPDATE = 
        "UPDATE Product SET ProductName = ?, Price = ?, Quantity = ? WHERE ProductID = ?";
    private static final String SQL_DELETE = 
        "DELETE FROM Product WHERE ProductID = ?";

    private static Connection connection = null;
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        
        try {
            // 1. Establish the connection
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
            
            // 2. Disable auto-commit to enable manual transaction management
            connection.setAutoCommit(false);
            System.out.println("Database connection established. AutoCommit set to false.");

            int choice = 0;
            while (choice != 5) {
                displayMenu();
                try {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    
                    switch (choice) {
                        case 1: createProduct(); break;
                        case 2: readAllProducts(); break;
                        case 3: updateProduct(); break;
                        case 4: deleteProduct(); break;
                        case 5: System.out.println("Exiting application. Goodbye!"); break;
                        default: System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                    }
                } catch (InputMismatchException e) {
                    System.err.println("Invalid input. Please enter a number.");
                    scanner.nextLine(); // Clear the bad input
                }
            }

        } catch (SQLException e) {
            System.err.println("\n[FATAL ERROR] Could not connect to the database.");
            System.err.println("Message: " + e.getMessage());
            // In case of initial connection failure, we cannot continue.
        } finally {
            closeConnection();
        }
    }

    private static void displayMenu() {
        System.out.println("\n==================================");
        System.out.println("      Product CRUD Menu");
        System.out.println("==================================");
        System.out.println("1. Create New Product");
        System.out.println("2. Read All Products");
        System.out.println("3. Update Product Details");
        System.out.println("4. Delete Product");
        System.out.println("5. Exit");
        System.out.print("Enter your choice: ");
    }

    private static void createProduct() {
        System.out.println("\n--- Create New Product ---");
        try {
            System.out.print("Enter Product Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter Quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try (PreparedStatement pstmt = connection.prepareStatement(SQL_INSERT)) {
                pstmt.setString(1, name);
                pstmt.setDouble(2, price);
                pstmt.setInt(3, quantity);

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    // Transaction Success: Commit the change
                    connection.commit();
                    System.out.println("[SUCCESS] Product '" + name + "' created and transaction committed.");
                } else {
                    // If no rows were affected unexpectedly, rollback
                    connection.rollback();
                    System.out.println("[WARNING] Product creation failed. Transaction rolled back.");
                }
            }
        } catch (SQLException e) {
            handleTransactionFailure(e, "Create Product");
        } catch (InputMismatchException e) {
            System.err.println("[ERROR] Invalid number format for Price or Quantity.");
            scanner.nextLine();
        }
    }

    private static void readAllProducts() {
        System.out.println("\n--- All Products in Database ---");
        // Read operations do not require commit/rollback logic, as they don't modify data.
        try (
            PreparedStatement pstmt = connection.prepareStatement(SQL_SELECT_ALL);
            ResultSet rs = pstmt.executeQuery()
        ) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No products found.");
                return;
            }

            System.out.println("---------------------------------------------------------------");
            System.out.printf("%-10s | %-25s | %-10s | %-10s\n", "ID", "Name", "Price", "Quantity");
            System.out.println("---------------------------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("ProductID");
                String name = rs.getString("ProductName");
                double price = rs.getDouble("Price");
                int quantity = rs.getInt("Quantity");
                
                System.out.printf("%-10d | %-25s | $%-9.2f | %-10d\n", id, name, price, quantity);
            }
            System.out.println("---------------------------------------------------------------");

        } catch (SQLException e) {
            System.err.println("[ERROR] Failed to read products: " + e.getMessage());
        }
    }

    private static void updateProduct() {
        System.out.println("\n--- Update Product ---");
        try {
            System.out.print("Enter Product ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter New Product Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter New Price: ");
            double price = scanner.nextDouble();
            System.out.print("Enter New Quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try (PreparedStatement pstmt = connection.prepareStatement(SQL_UPDATE)) {
                pstmt.setString(1, name);
                pstmt.setDouble(2, price);
                pstmt.setInt(3, quantity);
                pstmt.setInt(4, id);

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    // Transaction Success: Commit the change
                    connection.commit();
                    System.out.println("[SUCCESS] Product ID " + id + " updated and transaction committed.");
                } else {
                    // If ID was invalid or not found, no rows affected.
                    connection.rollback();
                    System.out.println("[WARNING] No product found with ID " + id + ". Transaction rolled back.");
                }
            }
        } catch (SQLException e) {
            handleTransactionFailure(e, "Update Product");
        } catch (InputMismatchException e) {
            System.err.println("[ERROR] Invalid number format for ID, Price, or Quantity.");
            scanner.nextLine();
        }
    }

    private static void deleteProduct() {
        System.out.println("\n--- Delete Product ---");
        try {
            System.out.print("Enter Product ID to delete: ");
            int id = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            try (PreparedStatement pstmt = connection.prepareStatement(SQL_DELETE)) {
                pstmt.setInt(1, id);

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    // Transaction Success: Commit the change
                    connection.commit();
                    System.out.println("[SUCCESS] Product ID " + id + " deleted and transaction committed.");
                } else {
                    // If ID was invalid or not found, no rows affected.
                    connection.rollback();
                    System.out.println("[WARNING] No product found with ID " + id + ". Transaction rolled back.");
                }
            }
        } catch (SQLException e) {
            handleTransactionFailure(e, "Delete Product");
        } catch (InputMismatchException e) {
            System.err.println("[ERROR] Invalid number format for ID.");
            scanner.nextLine();
        }
    }
    
    /**
     * Helper method to handle database errors and execute a rollback.
     */
    private static void handleTransactionFailure(SQLException e, String operationName) {
        System.err.println("[ERROR] " + operationName + " failed: " + e.getMessage());
        try {
            if (connection != null) {
                connection.rollback();
                System.out.println("[TRANSACTION] Rollback successful due to error.");
            }
        } catch (SQLException rollbackEx) {
            System.err.println("[CRITICAL] Could not execute rollback: " + rollbackEx.getMessage());
        }
    }

    /**
     * Safely closes the database connection.
     */
    private static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("\nDatabase connection closed.");
            } catch (SQLException e) {
                System.err.println("[ERROR] Failed to close connection: " + e.getMessage());
            }
        }
        scanner.close();
    }
}
