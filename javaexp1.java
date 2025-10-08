import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

// 1. Create a Student class with name and marks.
class Student {
    private String name;
    private double marks;

    public Student(String name, double marks) {
        this.name = name;
        this.marks = marks;
    }

    public String getName() {
        return name;
    }

    public double getMarks() {
        return marks;
    }

    @Override
    public String toString() {
        return name + " (" + marks + "%)";
    }
}

public class Main{
    public static void main(String[] args) {
        // 2. Use List<Student> to store data.
        List<Student> students = Arrays.asList(
            new Student("Alice", 85.5),
            new Student("Bob", 62.0),
            new Student("Charlie", 91.2),
            new Student("David", 78.8),
            new Student("Eve", 75.0),
            new Student("Frank", 99.1)
        );

        System.out.println("--- All Students ---");
        students.forEach(s -> System.out.println(s));
        System.out.println("--------------------");

        // Stream Operations
        System.out.println("\n--- Students scoring above 75% (Sorted by Marks) ---");

        List<String> topStudents = students.stream()
            // 3. Filter students with marks > 75%.
            // Lambda: s -> s.getMarks() > 75.0
            .filter(s -> s.getMarks() > 75.0) 

            // 4. Sort the filtered list by marks.
            // Lambda: Comparator.comparingDouble(Student::getMarks)
            .sorted(Comparator.comparingDouble(Student::getMarks))

            // 5. Mapping to a specific field using map() to extract student names.
            // Lambda: Student::getName (Method Reference)
            .map(Student::getName) 

            // 6. Display names using collect().
            .collect(Collectors.toList()); 

        // Display results
        topStudents.forEach(System.out::println); 
        
        System.out.println("-----------------------------------------------------");

        // Alternative display using forEach (without collecting to a List first)
        System.out.println("\n--- Alternative Display (Direct forEach) ---");
        students.stream()
            .filter(s -> s.getMarks() > 75.0) 
            .sorted(Comparator.comparingDouble(Student::getMarks))
            .map(Student::getName) 
            .forEach(System.out::println);
    }
}
