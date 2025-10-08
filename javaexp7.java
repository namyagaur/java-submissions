import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // This list stores wrapper objects (ArrayList<Integer>), 
        // not primitive types.
        List<Integer> integerObjects = new ArrayList<>();
        
        System.out.println("--- Integer Summation Demonstrating Autoboxing ---");
        System.out.println("Enter integers one by one. Type 'done' when finished.");

        while (true) {
            System.out.print("Enter number (or 'done'): ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("done")) {
                break;
            }

            try {
                // 1. String Parsing: Convert String input to primitive int
                int primitiveInt = Integer.parseInt(input);
                
                // 2. Autoboxing: The primitive 'int' is automatically converted 
                //    to an 'Integer' object when added to the List<Integer>.
                integerObjects.add(primitiveInt); 
                
                System.out.println(" (Added: " + primitiveInt + " | Stored as: Integer Object)");

            } catch (NumberFormatException e) {
                System.err.println(" Invalid input: '" + input + "' is not a valid integer. Please try again.");
            }
        }
        
        scanner.close();
        
        System.out.println("\n-------------------------------------------------");
        System.out.println("Total numbers entered: " + integerObjects.size());
        
        // Calculate the sum using unboxing
        long totalSum = calculateSum(integerObjects);

        System.out.println("Calculated Sum: " + totalSum);
        System.out.println("-------------------------------------------------");
    }

    /**
     * Calculates the sum of a list of Integer objects.
     * This method demonstrates automatic unboxing.
     * @param list The list of Integer objects.
     * @return The total sum.
     */
    private static long calculateSum(List<Integer> list) {
        long sum = 0;

        // 3. Unboxing: In the enhanced for-loop, the 'Integer' object 
        //    (item) is automatically converted back to a primitive 'int' 
        //    for the addition operation with the primitive 'long' sum.
        for (Integer item : list) {
            sum += item; 
        }

        return sum;
    }
}
