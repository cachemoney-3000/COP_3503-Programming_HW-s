package test;

public class WordMatrix {
    public int[][] solution;
    int path = 1;

    // initialize the solution matrix in constructor.
    public WordMatrix(int row, int col) {
        solution = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                solution[i][j] = 0;
            }
        }
    }

    public boolean searchWord(char[][] matrix, String word) {
        int N = matrix.length;
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (search(matrix, word, i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean search(char[][] matrix, String word, int row, int col,
                          int index) {

        // check if current cell not already used or character in it is not not

        if (solution[row][col] != 0 || word.charAt(index) != matrix[row][col]) {
            return false;
        }

        if (index == word.length() - 1) {
            // word is found, return true
            solution[row][col] = path++;
            return true;
        }

        // mark the current cell as 1
        solution[row][col] = path++;
        // check if cell is already used

        if (row + 1 < matrix.length && search(matrix, word, row + 1, col, index + 1))
            // down
            return true;

        if (row - 1 >= 0 && search(matrix, word, row - 1, col, index + 1))
            // up
            return true;

        if (col + 1 < matrix[0].length && search(matrix, word, row, col + 1, index + 1)) {
            // go
            // right
            return true;
        }

        if (col - 1 >= 0 && search(matrix, word, row, col - 1, index + 1)) {
            // left
            return true;
        }

        if (row - 1 >= 0 && col + 1 < matrix[0].length && search(matrix, word, row - 1, col - 1, index + 1)) {
            // diagonal up right
            return true;
        }

        if (row - 1 >= 0 && col - 1 >= 0 && search(matrix, word, row - 1, col - 1, index + 1)) {
            // diagonal up left
            return true;
        }

        if (row + 1 < matrix.length && col - 1 >= 0 && search(matrix, word, row + 1, col - 1, index + 1)) {
            // diagonal down left
            return true;
        }

        if (row + 1 < matrix.length && col + 1 < matrix[0].length && search(matrix, word, row + 1, col + 1, index + 1)) {
            // diagonal down right
            return true;
        }

        // if none of the option works out, BACKTRACK and return false
        solution[row][col] = 0;
        path--;
        return false;
    }

    public void print() {
        for (int i = 0; i < solution.length; i++) {
            for (int j = 0; j < solution[0].length; j++) {
                System.out.print(" " + solution[i][j]);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        char[][] matrix = { { 'C', 'B', 'A', 'B'},
                { 'B', 'N', 'O', 'D'},
                { 'M', 'D', 'E', 'E',}};
        WordMatrix w = new WordMatrix(matrix.length, matrix[0].length);

        if (w.searchWord(matrix, "BAOED")) {
            w.print();
        } else {
            System.out.println("NO PATH FOUND");
        }

    }

}
