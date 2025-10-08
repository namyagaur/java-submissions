import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

// 1. Create the Employee Model Class
class Employee {
    private String name;
    private int age;
    private double salary;

    public Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    // Getters are necessary for accessing fields in the Comparator
    public String getName() { return name; }
    public int getAge() { return age; }
    public double getSalary() { return salary; }

    @Override
    public String toString() {
        return String.format("%-10s | Age: %-3d | Salary: $%,.2f", name, age, salary);
    }
}

public class Main{

    public static void main(String[] args) {
        // 2. Store multiple Employee objects in a List<Employee>
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("Charlie", 30, 60000.00));
        employees.add(new Employee("Alice", 25, 75000.50));
        employees.add(new Employee("Bob", 40, 50000.00));
        employees.add(new Employee("David", 25, 90000.00));
        
        System.out.println("--- Original List ---");
        employees.forEach(System.out::println);
        System.out.println("---------------------\n");

        
        // ----------------------------------------------------
        // 3. Sort by Name (Alphabetically - Ascending)
        // ----------------------------------------------------
        // We use List.sort() with a lambda expression
        employees.sort((e1, e2) -> e1.getName().compareTo(e2.getName()));

        System.out.println("--- Sorted by Name (A-Z) ---");
        employees.forEach(System.out::println);
        System.out.println("----------------------------\n");


        // ----------------------------------------------------
        // 4. Sort by Age (Ascending)
        // ----------------------------------------------------
        // Using Comparator.comparingInt (best practice for primitives)
        employees.sort(Comparator.comparingInt(Employee::getAge));

        System.out.println("--- Sorted by Age (Ascending) ---");
        employees.forEach(System.out::println);
        System.out.println("---------------------------------\n");

        
        // ----------------------------------------------------
        // 5. Sort by Salary (Descending)
        // ----------------------------------------------------
        // We use Comparator.comparingDouble and reverse() for descending order
        employees.sort(Comparator.comparingDouble(Employee::getSalary).reversed());

        System.out.println("--- Sorted by Salary (Descending) ---");
        employees.forEach(System.out::println);
        System.out.println("-------------------------------------\n");
        
        
        // ----------------------------------------------------
        // 6. Multi-Field Sort: By Age (Ascending), then by Name
        // ----------------------------------------------------
        // Using the thenComparing method for secondary sort criteria
        employees.sort(
            Comparator.comparingInt(Employee::getAge)
            .thenComparing(Employee::getName)
        );

        System.out.println("--- Sorted by Age then Name ---");
        employees.forEach(System.out::println);
        System.out.println("-------------------------------\n");
    }
}
