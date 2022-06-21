/* COP 3503C Assignment 6
This program is written by: Joshua Samontanez */

package P6;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class P6 {
    public static void main(String[] args) throws IOException {
        BufferedReader scanner = new BufferedReader(new FileReader("docs/P6/in6.txt"));

        // Get the number of contestants per row
        int contestants = Integer.parseInt(scanner.readLine());

        // Array that will store the number of solution(s) a candidate can solve
        int[][] grid = new int[2][contestants];

        // Get the information and store it into the "grid" array
        for (int i = 0; i < 2; i++) {
            // Scan information per row and store it into the grid array
            String row = scanner.readLine();
            String[] arr = row.split(" ", contestants);
            for (int j = 0; j < contestants; j++) {
                grid[i][j] = Integer.parseInt(arr[j]);
            }
        }
        scanner.close();

        // Solve the problem
        System.out.println(solve(grid, contestants));
    }

    // Finds the max sum of an array where there are no consecutive elements
    public static int solve (int[][] grid, int n) {
        // Base case
        // If there are no candidates return 0
        if (n == 0) return 0;
        // If there's only 1 candidate per row, only compare the first column
        if (n == 1) return Math.max(grid[0][0], grid[1][0]);

        // 2d array that will store the max sum per iteration
        int[][] ans = new int[2][n];

        // Initialize the first column
        ans[0][0] = grid[0][0];
        ans[1][0] = grid[1][0];
        // Get the second column by adding its diagonals
        ans[0][1] = ans[1][0] + grid[0][1];
        ans[1][1] = ans[0][0] + grid[1][1];

        for (int i = 2; i < n; i++) {
            // Find the max by comparing the current element + second prev elements
            int temp_top = Math.max(grid[0][i] + ans[0][i - 2], grid[0][i] + ans[1][i - 2]);
            int temp_down = Math.max(grid[1][i] + ans[0][i - 2], grid[1][i] + ans[1][i - 2]);

            // Find the max by comparing the previous diagonal and the result from above
            ans[0][i] = Math.max(temp_top, ans[1][i - 1] + grid[0][i]);
            ans[1][i] = Math.max(temp_down, ans[0][i - 1] + grid[1][i]);
        }
        // Return the final result
        return Math.max(ans[0][n-1], ans[1][n-1]);
    }
}
