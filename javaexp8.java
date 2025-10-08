import java.io.*;

// 1. Create the Student class and implement the Serializable interface
class Student implements Serializable {
    // Recommended: Use a serialVersionUID for version control
    private static final long serialVersionUID = 1L; 

    private int studentID;
    private String name;
    private char grade;
    
    // Note: The 'transient' keyword prevents a field from being serialized. 
    // private transient String tempField = "This will not be saved";

    public Student(int studentID, String name, char grade) {
        this.studentID = studentID;
        this.name = name;
        this.grade = grade;
    }

    @Override
    public String toString() {
        return "Student [ID=" + studentID + ", Name=" + name + ", Grade=" + grade + "]";
    }
}

public class SerializationDemo {
    
    private static final String FILE_NAME = "student_data.ser";

    public static void main(String[] args) {
        
        // --- 1. Serialization (Saving the object) ---
        Student originalStudent = new Student(101, "Alice Johnson", 'A');
        System.out.println("Original Object to be Serialized: " + originalStudent);
        serializeObject(originalStudent);

        System.out.println("\n------------------------------------------------");

        // --- 2. Deserialization (Retrieving the object) ---
        Student deserializedStudent = deserializeObject();
        
        if (deserializedStudent != null) {
            System.out.println("Deserialization Successful!");
            System.out.println("Reconstructed Object: " + deserializedStudent);
            
            // Confirming the objects are different instances in memory
            System.out.println("\nVerification:");
            System.out.println("Original == Deserialized? " + (originalStudent == deserializedStudent)); 
        }
    }

    /**
     * Serializes a Student object and writes it to a file.
     * @param student The object to serialize.
     */
    public static void serializeObject(Student student) {
        try (
            // ObjectOutputStream writes objects, wrapped around FileOutputStream
            FileOutputStream fileOut = new FileOutputStream(FILE_NAME);
            ObjectOutputStream out = new ObjectOutputStream(fileOut)
        ) {
            out.writeObject(student);
            System.out.println("Object successfully serialized and saved to " + FILE_NAME);
            
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

    /**
     * Reads a serialized object from a file and returns the reconstructed Student object.
     * @return The deserialized Student object, or null on failure.
     */
    public static Student deserializeObject() {
        Student student = null;
        try (
            // ObjectInputStream reads objects, wrapped around FileInputStream
            FileInputStream fileIn = new FileInputStream(FILE_NAME);
            ObjectInputStream in = new ObjectInputStream(fileIn)
        ) {
            // Read the object and cast it back to the Student type
            student = (Student) in.readObject();
            
        } catch (IOException i) {
            // Handle file not found or read error
            System.err.println("File I/O Error during deserialization: " + i.getMessage());
        } catch (ClassNotFoundException c) {
            // Handle case where the class definition (Student.class) is not found
            System.err.println("Student class not found.");
            c.printStackTrace();
        }
        return student;
    }
}
