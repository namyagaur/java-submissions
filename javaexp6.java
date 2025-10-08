import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class for Employee Management using file I/O (text format).
 * Employee records are stored in 'employees.txt'.
 */
public class Main{
    
    // Define the file path for persistent storage
    private static final String FILE_NAME = "employees.txt";
    private static final Scanner scanner = new Scanner(System.in);

    // --- Employee Model ---
    static class Employee {
        private String name;
        private String id;
        private String designation;
        private double salary;

        public Employee(String name, String id, String designation, double salary) {
            this.name = name;
            this.id = id;
            this.designation = designation;
            this.salary = salary;
        }

        // Method to convert Employee object to a delimited string for file storage
        public String toFileString() {
            // Using a pipe '|' as a delimiter to separate fields
            return name + "|" + id + "|" + designation + "|" + salary;
        }

        // Static factory method to parse a line from the file back into an Employee object
        public static Employee fromFileString(String line) {
            String[] parts = line.split("\\|");
            if (parts.length != 4) {
                throw new IllegalArgumentException("Invalid file format: line has wrong number of fields.");
            }
            try {
                return new Employee(
                    parts[0],
                    parts[1],
                    parts[2],
                    Double.parseDouble(parts[3]) // Convert salary string back to double
                );
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid file format: salary is not a number.");
            }
        }

        @Override
        public String toString() {
            return String.format("| %-15s | %-5s | %-18s | $%,.2f |", name, id, designation, salary);
        }
    }
    
    // --- Application Logic ---

    public static void main(String[] args) {
        int choice = 0;
        
        while (choice != 3) {
            displayMenu();
            try {
                if (scanner.hasNextInt()) {
                    choice = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                } else {
                    System.out.println("\n[Error] Invalid input. Please enter a number (1, 2, or 3).");
                    scanner.nextLine(); // Consume bad input
                    continue;
                }
                
                switch (choice) {
                    case 1:
                        addEmployee();
                        break;
                    case 2:
                        displayAllEmployees();
                        break;
                    case 3:
                        System.out.println("\nExiting Application. Data saved to " + FILE_NAME);
                        break;
                    default:
                        System.out.println("\n[Warning] Choice must be 1, 2, or 3.");
                }
            } catch (InputMismatchException e) {
                // This is generally caught by the if/else above, but included for robustness
                System.err.println("\n[Error] Please enter a valid number for the menu option.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n=================================");
        System.out.println("  Employee Management System");
        System.out.println("=================================");
        System.out.println("1. Add an Employee");
        System.out.println("2. Display All Employees");
        System.out.println("3. Exit the Application");
        System.out.print("Enter your choice: ");
    }
    
    // --- CRUD Operations ---

    /**
     * Prompts user for details and saves the new employee record to the file.
     */
    private static void addEmployee() {
        System.out.println("\n--- Add New Employee ---");
        try {
            System.out.print("Enter Name: ");
            String name = scanner.nextLine();
            System.out.print("Enter ID: ");
            String id = scanner.nextLine();
            System.out.print("Enter Designation: ");
            String designation = scanner.nextLine();
            System.out.print("Enter Salary: $");
            
            double salary = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            Employee newEmployee = new Employee(name, id, designation, salary);
            saveEmployeeToFile(newEmployee);
            
            System.out.println("\n[SUCCESS] Employee record added and saved to file.");

        } catch (InputMismatchException e) {
            System.err.println("\n[Error] Invalid input for salary. Please enter a numerical value.");
            scanner.nextLine();
        }
    }
    
    /**
     * Reads all employee records from the file and displays them.
     */
    private static void displayAllEmployees() {
        System.out.println("\n--- All Employee Records ---");
        List<Employee> employees = readEmployeesFromFile();

        if (employees.isEmpty()) {
            System.out.println("No records found in the database file.");
            return;
        }

        // Display header
        System.out.println("-----------------------------------------------------------------");
        System.out.printf("| %-15s | %-5s | %-18s | %-12s |%n", "Name", "ID", "Designation", "Salary");
        System.out.println("-----------------------------------------------------------------");
        
        // Display records
        for (Employee emp : employees) {
            System.out.println(emp.toString());
        }
        System.out.println("-----------------------------------------------------------------");
    }

    // --- File Handling Methods ---
    
    /**
     * Appends a single employee record to the text file.
     * @param employee The Employee object to save.
     */
    private static void saveEmployeeToFile(Employee employee) {
        // Use true for FileWriter constructor to enable append mode
        try (FileWriter fw = new FileWriter(FILE_NAME, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            
            bw.write(employee.toFileString());
            bw.newLine();
            
        } catch (IOException e) {
            System.err.println("[File Error] Could not write to file: " + e.getMessage());
        }
    }

    /**
     * Reads all employee records from the text file.
     * @return A list of Employee objects.
     */
    private static List<Employee> readEmployeesFromFile() {
        List<Employee> employees = new ArrayList<>();
        try (FileReader fr = new FileReader(FILE_NAME);
             BufferedReader br = new BufferedReader(fr)) {
            
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; // Skip empty lines
                try {
                    // Convert the delimited string back to an Employee object
                    employees.add(Employee.fromFileString(line));
                } catch (IllegalArgumentException e) {
                    System.err.println("[Data Error] Skipping corrupted record: " + line);
                }
            }
        } catch (IOException e) {
            // Handle case where file doesn't exist yet (first run) or read error
            System.err.println("[File Notice] Database file not found or read error: " + e.getMessage());
        }
        return employees;
    }
}
