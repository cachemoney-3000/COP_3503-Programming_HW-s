package P2;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public char[][] solution;

    public Main(int row, int col) {
        // Initialize the solution matrix
        solution = new char[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                solution[i][j] = ' ';
            }
        }
    }

    public static void main(String[] args) throws FileNotFoundException {
        // Get the file ready to read the input
        Scanner scanner = new Scanner(new FileReader("docs/P2/in.txt"));
        // Reads the first line of the input file
        int numCases = scanner.nextInt();

        // Loop through the cases
        for (int i = 0; i < numCases; i++) {
            // Reads the second line
            int numRows = scanner.nextInt();
            int numCol = scanner.nextInt();
            int numWords = scanner.nextInt();

            char[][] board = new char[numRows][numCol];

            // Populate the 2d char array
            for (int row = 0; row < numRows; row++) {
                for (int column = 0; column < numCol; column++) {
                    board[row][column] = scanner.next().charAt(0);
                }
            }

            // Stores the words to find into an array of strings
            String[] findWords = new String[numWords + 1];
            for (int k = 0; k < findWords.length; k++) {
                findWords[k] = scanner.nextLine();
            }

            // Produce the final output
            printResult(i, numWords, board, findWords);
        }
    }

    public static void printResult(int numCase, int numWords, char[][]board, String[] findWords){
        System.out.println("Test#" + (numCase + 1));

        // Loop through all the words we need to find
        for (int l = 1; l <= numWords; l++) {
            // This will initialize the solution 2d array
            Main wordSearch = new Main(board.length, board[0].length);
            // Calls the wrapper function for finding a word in our 2d array of chars
            boolean found = wordSearch.searchWord(board, findWords[l]);
            // Prints the final result
            wordSearch.printResultUtil(findWords[l], found);
        }
        System.out.println();
    }

    public void printResultUtil(String word, boolean found) {
        System.out.println("Looking for " + word);

        // If a word is found in the 2d array
        if(found) {
            // Loop through the "solution" 2d array to print its contents
            for (char[] chars : solution) {
                System.out.println(Arrays.toString(chars));
            }
            System.out.println();
        }

        // If a word is not found
        else {
            System.out.println(word + " not found!\n");
        }

    }

    public boolean searchWord(char[][] board, String word) {
        int row = board.length;
        int col = board[0].length;

        // Loop through the contents of our 2d array "board" to search for the matching word
        // If the program found the matching word, it will return true, or else, it will return false
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (searchWordUtil(board, word, i, j, 0)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean searchWordUtil(char[][] board, String word, int row, int col, int index) {
        // Checks if the current cell is already used, or if it doesn't match the specific character of the word we are finding
        if (solution[row][col] != ' ' || word.charAt(index) != board[row][col]){
            return false;
        }

        // If we found all the matching characters for the word we are finding, return true
        if (index == word.length() - 1) {
            solution[row][col] = board[row][col];
            return true;
        }

        // Mark the current cell on our "solution" by copying the character from current cell on our "board"
        solution[row][col] = board[row][col];

        // Find the next character by going to the right
        if (col + 1 < board[0].length &&
                searchWordUtil(board, word, row, col + 1, index + 1)) {
            return true;
        }

        // Find the next character by going to the left
        if (col - 1 >= 0 &&
                searchWordUtil(board, word, row, col - 1, index + 1)) {
            return true;
        }

        // Find the next character by going down
        if (row + 1 < board.length &&
                searchWordUtil(board, word, row + 1, col, index + 1)){
            return true;
        }

        // Find the next character by going up
        if (row - 1 >= 0 &&
                searchWordUtil(board, word, row - 1, col, index + 1)){
            return true;
        }

        // Find the next character by going diagonally (up -> right)
        if (row - 1 >= 0 && col + 1 < board[0].length &&
                searchWordUtil(board, word, row - 1, col - 1, index + 1)) {
            return true;
        }

        // Find the next character by going diagonally (down -> right)
        if (row + 1 < board.length && col + 1 < board[0].length &&
                searchWordUtil(board, word, row + 1, col + 1, index + 1)) {
            return true;
        }

        // Find the next character by going diagonally (up -> left)
        if (row - 1 >= 0 && col - 1 >= 0 &&
                searchWordUtil(board, word, row - 1, col - 1, index + 1)) {
            return true;
        }

        // Find the next character by going diagonally (down -> left)
        if (row + 1 < board.length && col - 1 >= 0 &&
                searchWordUtil(board, word, row + 1, col - 1, index + 1)) {
            return true;
        }

        // If we can't find the character by going through all the steps above, remove the mark on "solution"
        solution[row][col] = ' ';
        // Backtrack
        return false;
    }
}