/* COP 3503C Assignment 2
This program is written by: Joshua Samontanez */

package P1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class P1_Test {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader("docs/P1/in.txt"));

        int numCases = scanner.nextInt();
        // System.out.println(numCases);

        for(int i = 0; i < numCases; i++){
            int sorted = scanner.nextInt(); // 1 means sorted, 0 means unsorted
            int caseNumber = i + 1;


            if(sorted == 1){
                // System.out.println("sorted " + sorted);
                int arraySize = scanner.nextInt();
                int[] arr = new int[arraySize];

                for(int j = 0; j < arraySize; j++){

                    arr[j] = scanner.nextInt();
                    // System.out.println(arr[j]);
                }

                int target = scanner.nextInt();
                // System.out.println("target" + target);

                int[] result = getCandidatePair_twoTrackers(arr, target);
                printResults(caseNumber, target, result[0], result[1]);

                // System.out.println("Test case#" + caseNumber + ": " + "Target " + target + " achievable by " + result[0] + " and " + result[1]);
            }

            else if(sorted == 0){
                // System.out.println("sorted " + sorted);
                int arraySize = scanner.nextInt();
                int[] arr = new int[arraySize];

                for(int k = 0; k < arraySize; k++){

                    arr[k] = scanner.nextInt();
                    // System.out.println(arr[k]);
                }

                int target = scanner.nextInt();
                // System.out.println("target" + target);

                int[] result = getCandidatePair(arr, target);
                printResults(caseNumber, target, result[0], result[1]);

                // System.out.println("Test case#" + caseNumber + ": " + "Target " + target + " achievable by " + result[0] + " and " + result[1]);
            }
        }
    }

    public static int[] getCandidatePair(int[] A, int target){
        Map<Integer, Integer> hash = new HashMap<>();

        for(int i = 0; i < A.length; i++){
            int complement = target - A[i];

            if(hash.containsKey(complement)){
                return new int[] {A[hash.get(complement)], A[i]};
            }

            hash.put(A[i], i);
        }

        // This means that we found no match pairs
        return new int[] {0, 0};
    }

    public static void printResults(int caseNumber, int target, int num1, int num2){
        if(num1 == 0 && num2 == 0){
            System.out.println("Test case#" + caseNumber + ": " + "Target " + target + " is NOT achievable.");
        }
        else{
            System.out.println("Test case#" + caseNumber + ": " + "Target "
                    + target + " achievable by " + num1 + " and " + num2);
        }
    }

    public static int[] getCandidatePair_twoTrackers(int[] A, int target){
        int i = 0;
        int j = A.length - 1;

        while(i < j){
            int sum = A[i] + A[j];

            if(sum == target){
                return new int[] {A[i], A[j]};
            }
            else if(sum < target){
                i++;
            }
            else {
                j--;
            }
        }
        return new int[] {0, 0};
    }
}
