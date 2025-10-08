import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AutoboxingSumCalculator {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        // This list is parameterized with the wrapper class 'Integer', 
        // meaning it stores Integer objects, not primitives.
        List<Integer> integerObjects = new ArrayList<>();
        
        System.out.println("--- Integer Summation Demonstrating Autoboxing & Unboxing ---");
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
                
                // 2. Autoboxing: The primitive 'int' (primitiveInt) is automatically converted 
                //    to an 'Integer' object when added to the List<Integer> wrapper collection.
                integerObjects.add(primitiveInt); 
                
                System.out.println(" (Added: " + primitiveInt + " | Stored as: Integer Object)");

            } catch (NumberFormatException e) {
                System.err.println(" Invalid input: '" + input + "' is not a valid integer. Please try again.");
            }
        }
        
        scanner.close();
        
        System.out.println("\n-------------------------------------------------");
        
        // Calculate the sum using unboxing
        long totalSum = calculateSum(integerObjects);

        System.out.println("Total numbers entered: " + integerObjects.size());
        System.out.println("Calculated Sum: " + totalSum);
        System.out.println("-------------------------------------------------");
    }

    /**
     * Calculates the sum of a list of Integer objects.
     * This method demonstrates automatic unboxing within the loop.
     * @param list The list of Integer objects.
     * @return The total sum.
     */
    private static long calculateSum(List<Integer> list) {
        long sum = 0;

        // 3. Unboxing: In this enhanced for-loop, the 'Integer' object 
        //    (item) is automatically converted back to a primitive 'int' 
        //    whenever it is used in the arithmetic operation 'sum += item'.
        for (Integer item : list) {
            sum += item; 
        }

        return sum;
    }
}
