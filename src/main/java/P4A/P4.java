/* COP 3503C Assignment 4
This program is written by: Joshua Samontanez */

package P4A;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class P4 {

    public static void main(String[] args) throws IOException {
        BufferedReader scanner = new BufferedReader(new FileReader("docs/P4/in.txt"));

        // Get the first line of the input
        String firstLine = scanner.readLine();
        String[] arr1 = firstLine.split(" ", 3);

        // Initialize the values that store the location of the start and end point
        int startX = 0;
        int startY = 0;
        int endX = 0;
        int endY = 0;

        // Read the number of rows and columns
        int row = Integer.parseInt(arr1[0]);
        int col = Integer.parseInt(arr1[1]);

        // Initialize our board and the visited 2d array
        char[][] board = new char[row][col];
        int[][] visited = new int[row][col];

        // For implementation of portals
        HashMap<Character, List<Integer[]>> portalLocation = new HashMap<>();

        // Loop through the row and col of the maze
        for (int i = 0; i < row; i++){
            // Scan the maze row by row
            String line = scanner.readLine();
            // Put each character into a 2d array
            board[i] = line.toCharArray();

            for (int j = 0; j < col; j++){
                // Set each node to unvisited (0)
                visited[i][j] = 0;

                // Get the starting point
                if (line.contains("*")) {
                    startX = i;
                    startY = line.indexOf("*");
                }
                // Get the end point
                if (line.contains("$")) {
                    endX = i;
                    endY = line.indexOf("$");
                }

                // Implementation of portals (Uppercase letters)
                if (Character.isUpperCase(board[i][j])){
                    // 2 means it is a portal
                    visited[i][j] = 2;
                    // Get the uppercase letter and store it into the board
                    char ch = board[i][j];

                    // Add the portal locations using the characters as key into a hash map
                    if (portalLocation.containsKey(ch)){
                        portalLocation.get(ch).add(new Integer[]{i, j});
                    }
                    // If the key is not on the hash map yet, add them
                    else {
                        List<Integer[]> list= new ArrayList<>();
                        list.add(new Integer[] {i, j});
                        portalLocation.put(ch, list);
                    }
                }
            }
        }

        scanner.close();

        /*
        System.out.println(Arrays.deepToString(portalLocation.get('A').toArray()));
        System.out.println(Arrays.deepToString(portalLocation.get('B').toArray()));
        System.out.println(Arrays.deepToString(portalLocation.get('C').toArray()));
         */

        // Count how many steps it took to reach the end
        int steps = countSteps(startX, startY, endX, endY, row, col, visited, board, portalLocation);
        System.out.println(steps);
    }

    public static int countSteps (int startX, int startY, int endX, int endY, int row, int col,
                                  int[][] visited, char[][] board, HashMap<Character, List<Integer[]>> portalLocation){
        // Set the linked list for both x and y direction
        LinkedList<Integer> queue_x = new LinkedList<>();
        LinkedList<Integer> queue_y = new LinkedList<>();

        // Enqueue the starting point
        queue_x.offer(startX);
        queue_y.offer(startY);

        // Mark the starting position as visited
        visited[startX][startY] = 1;

        // Variables to track the number of steps taken
        int steps = 0;
        int nodesLeft = 1;
        int nodesNextLayer = 0;

        // Initialize the vector directions we can take for each node (up, down, left, right)
        int[] direction_r = {-1, 1, 0, 0};
        int[] direction_c = {0, 0, 1, -1};

        // Tracks if we used the program used a portal or not
        boolean portalUsed = false;

        // Keep looping until there is no more items in the queue left
        while (queue_x.size() > 0 && queue_y.size() > 0) {
            int cur_x = queue_x.poll();
            int cur_y = queue_y.poll();

            // If we reached the exit, break out the loop
            if (reachedExit(cur_x, cur_y, endX, endY, board)) {
                return steps;
            }

            // Explore its neighboring nodes
            for (int i = 0; i < 4; i++){
                int x = cur_x + direction_r[i];
                int y = cur_y + direction_c[i];

                // Make sure we don't go out of bounds
                if (inBounds (x, y, row, col)) {
                    // If the current position is not visited, is not blocked, and is not a portal
                    if (isSafe (visited, board, x, y)){
                        // Add the current position to the queue
                        queue_x.offer(x);
                        queue_y.offer(y);
                        // Change the current position as visited
                        visited[x][y] = 1;
                        nodesNextLayer++;
                        portalUsed = false;
                    }

                    // If we landed into a portal
                    else if (visited[x][y] == 2) {
                        // Mark that location as visited and close that portal
                        visited[x][y] = 1;
                        // Get the equivalent character on that location
                        char ch = board[x][y];
                        // Get the coordinates of the portals for that key
                        List<Integer[]> loc = portalLocation.get(ch);

                        // Explore all the portals
                        for (Integer[] location : loc) {
                            int u = location[0];    // Get the row value
                            int v = location[1];    // Get the col value

                            // Add them to queue and mark them as visited
                            queue_x.offer(u);
                            queue_y.offer(v);
                            visited[u][v] = 1;
                            nodesNextLayer++;
                        }
                        portalUsed = true;
                    }
                }
            }

            nodesLeft--;
            // After we explored a node with its 4 direction, increase the number of steps
            if (nodesLeft == 0) {
                nodesLeft = nodesNextLayer;
                nodesNextLayer = 0;
                steps++;

                // Add 1 step for each portal used
                if (portalUsed)
                    steps++;
            }
        }

        // If we reached here, it means to path found
        return  -1;
    }

    // Helper function to make the code more readable - checks if we are still in the bounds
    public static boolean inBounds (int x, int y, int row, int col){
        return x >= 0 && x < row && y >= 0 && y < col;
    }

    // Helper function to make the code more readable - checks if the node is not visited yet, not blocked, and not a portal
    public static boolean isSafe (int[][] visited, char[][] board, int x, int y) {
        return visited[x][y] != 1 && visited[x][y] != 2 && board[x][y] != '!';
    }

    // Helper function to make the code more readable - checks if we reached the exit
    public static boolean reachedExit (int cur_x, int cur_y, int endX, int endY, char[][] board) {
        return cur_x == endX && cur_y == endY && board[cur_x][cur_y] == '$';
    }
}
