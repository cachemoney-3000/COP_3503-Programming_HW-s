/* COP 3503C Assignment 4
This program is written by: Joshua Samontanez */

package P4;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

public class P4 {
    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader("docs/P4/in.txt"));
        int row = scanner.nextInt();
        int col = scanner.nextInt();

        int startX = 0;
        int startY = 0;
        int endX = 0;
        int endY = 0;

        HashMap<Character, List<Integer[]>> portalLocation = new HashMap<>();

        boolean[][] path = new boolean[row][col];
        boolean[][] portal = new boolean[row][col];
        char[][] portalKey = new char[row][col];

        for (int i = 0; i < row; i++) {
            String line = scanner.next();

            if (line.contains("*")) {
                startX = i;
                startY = line.indexOf("*");
            }

            if (line.contains("$")) {
                endX = i;
                endY = line.indexOf("$");
            }

            for (int j = 0; j < col; j++) {
                char ch = line.charAt(j);

                if (ch == '.')
                    path[i][j] = true;

                if (Character.isUpperCase(ch)){
                    portal[i][j] = true;

                    if(portalLocation.containsKey(ch)){
                        portalLocation.get(ch).add(new Integer[]{i, j});
                        portalKey[i][j] = ch;
                    }
                    else {
                        portalKey[i][j] = ch;
                        List<Integer[]> list= new ArrayList<>();
                        list.add(new Integer[] {i, j});
                        portalLocation.put(ch, list);
                    }
                }
            }
        }

        // Count the steps
        int steps;

        if (startX == endX && startY == endY)
            steps = 0;

        else
            steps = countSteps(row, col, path, portal, portalLocation, portalKey,
                    new int[]{startX, startY}, new int[]{endX, endY});

        System.out.println(steps);
    }


    public static int countSteps(int row, int col, boolean[][] path,  boolean[][] portal,
                                 HashMap<Character, List<Integer[]>> portalLocation,
                                 char[][] portalKey, int[] start, int[] end) {
        int steps = 1;
        int size;
        int x;
        int y;

        LinkedList<int[]> deque = new LinkedList<>();
        int[][] direction = {{1, 0}, {-1, 0},
                {0, 1}, {0, -1}};

        int[] pop;
        deque.push(start);

        while (!deque.isEmpty()) {
            size = deque.size();
            while (size-- > 0) {
                pop = deque.pollFirst();
                for (int[] dir : direction) {
                    assert pop != null;

                    // Visit all the adjacent cells
                    x = pop[0] + dir[0];
                    y = pop[1] + dir[1];

                    // Check if we are in the boundary
                    if (x >= 0 && x < row && y >= 0 && y < col) {
                        // If we reached the end, return to amount of steps it took
                        if (x == end[0] && y == end[1])
                            return steps;

                        else if (path[x][y]) {
                            deque.addLast(new int[]{x, y});
                            path[x][y] = false;
                        }


                        else if (portal[x][y]) {
                            if(portalLocation.containsKey(portalKey[x][y])){
                                int keyCount = portalLocation.get(portalKey[x][y]).size();

                                for (int i = 0; i < keyCount; i++){
                                    Integer[] loc = portalLocation.get(portalKey[x][y]).get(0);
                                    x = loc[0];
                                    y = loc[1];

                                    deque.addLast(new int[]{x, y});
                                    portal[x][y] = false;
                                }
                            }
                        }
                    }
                }
            }
            steps++;
        }
        return -1;
    }
}
