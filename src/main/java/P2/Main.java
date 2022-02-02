package P2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public char[][] solution;

    // initialize the solution matrix in constructor.
    public Main(int row, int col) {
        solution = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                solution[i][j] = '_';
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new FileReader("docs/P2/in.txt"));
        int numCases = scanner.nextInt();

        for (int i = 0; i < numCases; i++) {
            int numRows = scanner.nextInt();
            int numCol = scanner.nextInt();
            int numWords = scanner.nextInt();

            char[][] board = new char[numRows][numCol];



            for (int a = 0; a < numRows; a++) {
                for (int b = 0; b < numCol; b++) {
                    board[a][b] = scanner.next().charAt(0);
                }
            }


            String[] findWords = new String[numWords + 1];
            for (int k = 0; k < findWords.length; k++) {
                findWords[k] = scanner.nextLine();
            }


            for (int l = 1; l <= numWords; l++) {
                Main test = new Main(board.length, board[0].length);

                if (test.searchWord(board, findWords[l])) {
                    test.print();
                    System.out.println("true");
                }
                else {
                    System.out.println("not found");
                }
            }
        }
    }

    public void print() {
        for (int i = 0; i < solution.length; i++) {
            System.out.println(solution);

            System.out.println();
        }


    }

    public boolean searchWord(char[][] board, String word) {
        int row = board.length;
        int col = board[0].length;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (searchWordUtil(board, word, i, j, 0))
                    return true;
            }
        }
        return false;
    }

    public boolean searchWordUtil(char[][] board, String word, int row, int col, int index) {
        if (solution[row][col] != '_' || word.charAt(index) != board[row][col])
            return false;

        if (index == word.length() - 1) {
            solution[row][col] = board[row][col];
            return true;
        }

        solution[row][col] = board[row][col];
        if (row + 1 < board.length && searchWordUtil(board, word, row + 1, col, index + 1))
            // down
            return true;

        if (row - 1 >= 0 && searchWordUtil(board, word, row - 1, col, index + 1))
            // up
            return true;

        if (col + 1 < board[0].length && searchWordUtil(board, word, row, col + 1, index + 1)) {
            // go
            // right
            return true;
        }

        if (col - 1 >= 0 && searchWordUtil(board, word, row, col - 1, index + 1)) {
            // left
            return true;
        }

        if (row - 1 >= 0 && col + 1 < board[0].length && searchWordUtil(board, word, row - 1, col - 1, index + 1)) {
            // diagonal up right
            return true;
        }

        if (row - 1 >= 0 && col - 1 >= 0 && searchWordUtil(board, word, row - 1, col - 1, index + 1)) {
            // diagonal up left
            return true;
        }

        if (row + 1 < board.length && col - 1 >= 0 && searchWordUtil(board, word, row + 1, col - 1, index + 1)) {
            // diagonal down left
            return true;
        }

        if (row + 1 < board.length && col + 1 < board[0].length && searchWordUtil(board, word, row + 1, col + 1, index + 1)) {
            // diagonal down right
            return true;
        }

        solution[row][col] = '_';
        return false;
    }


}

