/* COP 3503C Assignment 1
This program is written by: Joshua Samontanez */

package P1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ProgrammingOne {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader("docs/P1/in.txt"));
        // Stores the number of cases.
        int numCases = scanner.nextInt();

        // Loop through all the cases.
        for(int i = 0; i < numCases; i++){
            // Stores if the array is sorted or not (0 means unsorted, 1 means sorted).
            int sorted = scanner.nextInt();
            int caseNumber = i + 1;

            // This means that the array is sorted
            if(sorted == 1){
                int arraySize = scanner.nextInt();
                int[] arr = new int[arraySize];

                // Store the numbers into an array
                for(int j = 0; j < arraySize; j++){
                    arr[j] = scanner.nextInt();
                }
                int target = scanner.nextInt();

                // Special case where we need to use two trackers to find 2 pairs that will add up into our target
                int[] result = getCandidatePair(arr, target);
                printResults(caseNumber, target, result[0], result[1]);
            }

            // This means that the array is unsorted
            else if(sorted == 0){
                int arraySize = scanner.nextInt();
                int[] arr = new int[arraySize];

                // Store the numbers into an array
                for(int k = 0; k < arraySize; k++){
                    arr[k] = scanner.nextInt();
                }
                int target = scanner.nextInt();

                // Special case where we need to use hash sets to find 2 pairs that will add up into our target
                int[] result = getCandidatePair_unsorted(arr, target);
                printResults(caseNumber, target, result[0], result[1]);
            }
        }
    }

    public static int[] getCandidatePair_unsorted(int[] A, int target){
        Map<Integer, Integer> hash = new HashMap<>();

        // This will loop through the array
        for(int i = 0; i < A.length; i++){
            int delta = target - A[i];
            // if the number we are looking for is in our hash map:
            if(hash.containsKey(delta)){
                // Then it will return 2 values, that when we add up it will be equal to our target
                return new int[] {A[hash.get(delta)], A[i]};
            }
            hash.put(A[i], i);
        }
        // This means that we found no matching pairs, so it will return {0, 0}
        return new int[] {0, 0};
    }

    public static int[] getCandidatePair(int[] A, int target){
        // The two trackers:
        int i = 0;
        int j = A.length - 1;

        // Keep looping until the trackers do not cross.
        while(i < j){
            int sum = A[i] + A[j];

            // If we found the 2 matching pairs
            if(sum == target){
                // Return the 2 pairs
                return new int[] {A[i], A[j]};
            }
            // If the sum is smaller than our target, update the i tracker
            else if(sum < target){
                i++;
            }
            // If the sum is greater than our target, update the j tracker
            else {
                j--;
            }
        }
        // If it reached here, this means that the program found no matching pairs, so it will return {0, 0}
        return new int[] {0, 0};
    }

    // Method that will just print out the results
    public static void printResults(int caseNumber, int target, int num1, int num2){
        if(num1 == 0 && num2 == 0){
            System.out.println("Test case#" + caseNumber + ": " + "Target " + target + " is NOT achievable.");
        }
        else{
            System.out.println("Test case#" + caseNumber + ": " + "Target "
                    + target + " achievable by " + num1 + " and " + num2);
        }
    }
}
