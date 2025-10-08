import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Java program demonstrating basic JDBC connectivity to a MySQL database
 * to read records from an 'Employee' table.
 * * IMPORTANT: Before running this program, you must:
 * 1. Ensure a MySQL server is running.
 * 2. Create a database (e.g., 'mydatabase') with an 'Employee' table 
 * (columns: EmpID, Name, Salary).
 * 3. Update the DB_URL, USER, and PASS constants below with your actual credentials.
 * 4. Have the MySQL JDBC Connector JAR file included in your project's classpath.
 */
public class Main {

    // --- JDBC Connection Parameters (MUST BE UPDATED) ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydatabase";
    private static final String USER = "root";       // Your MySQL username
    private static final String PASS = "password";   // Your MySQL password

    // --- SQL Query ---
    private static final String SQL_SELECT = "SELECT EmpID, Name, Salary FROM Employee";

    public static void main(String[] args) {
        // Using try-with-resources ensures that the Connection, Statement, and ResultSet 
        // objects are automatically closed, preventing resource leaks.
        try (
            // 1. Establish the connection to the database
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            
            // 2. Create a Statement object for executing the SQL query
            Statement stmt = conn.createStatement();
            
            // 3. Execute the SELECT query and get the ResultSet
            ResultSet rs = stmt.executeQuery(SQL_SELECT);
        ) {
            
            System.out.println("--- Employee Records Retrieved via JDBC ---");
            System.out.println("-------------------------------------------");
            System.out.printf("%-10s %-20s %-10s\n", "EmpID", "Name", "Salary");
            System.out.println("-------------------------------------------");

            // 4. Loop through the ResultSet to retrieve and display data
            while (rs.next()) {
                // Retrieve data by column name, matching the table schema
                int id = rs.getInt("EmpID");
                String name = rs.getString("Name");
                // Using getDouble for currency representation
                double salary = rs.getDouble("Salary");

                // Display the results clearly in a formatted table
                System.out.printf("%-10d %-20s $%,.2f\n", id, name, salary);
            }
            
            System.out.println("-------------------------------------------");

        } catch (SQLException e) {
            // Handle exceptions like connection failures or table/query errors
            System.err.println("A database error occurred during the JDBC operation.");
            System.err.println("SQL State: " + e.getSQLState());
            System.err.println("Error Message: " + e.getMessage());
            // e.printStackTrace(); // Uncomment for full debugging stack trace
        }
    }
}
