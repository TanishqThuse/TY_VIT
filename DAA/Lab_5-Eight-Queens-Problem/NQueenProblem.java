import java.util.Scanner;

public class NQueenProblem {

    /* Function to print the chessboard */
    void printSolution(int board[][], int N) {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++)
                // System.out.print(board[i][j] + " ");
                if(board[i][j] == 1)
                    System.out.print(" Q ");
                else
                    System.out.print(" . ");
            System.out.println();
        }
    }

    /* Check if a queen can be placed on board[row][col] */
    boolean isSafe(int board[][], int row, int col, int N) {
        int i, j;

        // Check this column on upper side
        for (i = 0; i < row; i++)
            if (board[i][col] == 1)
                return false;

        // Check upper left diagonal
        for (i = row, j = col; i >= 0 && j >= 0; i--, j--)
            if (board[i][j] == 1)
                return false;

        // Check upper right diagonal
        for (i = row, j = col; i >= 0 && j < N; i--, j++)
            if (board[i][j] == 1)
                return false;

        return true;
    }

    /* Recursive function to solve the N Queen problem */
    boolean solveNQUtil(int board[][], int row, int N) {
        // Base case: if all queens are placed
        if (row >= N)
            return true;

        // Try placing queen in all columns
        for (int col = 0; col < N; col++) {
            if (isSafe(board, row, col, N)) {
                board[row][col] = 1;  // place queen

                // Recursion for next row
                if (solveNQUtil(board, row + 1, N))
                    return true;

                // Backtrack
                board[row][col] = 0;
            }
        }
        return false; // no place for this row
    }

    /* Main function to solve N Queen problem */
    void solveNQ(int N) {
        int board[][] = new int[N][N];

        if (!solveNQUtil(board, 0, N)) {
            System.out.println("No solution exists for N = " + N);
        } else {
            System.out.println("Solution for " + N + " Queens:");
            printSolution(board, N);
        }
    }

    public static void main(String args[]) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter value of N (number of queens): ");
        int N = sc.nextInt();
        sc.close();

        NQueenProblem queen = new NQueenProblem();
        queen.solveNQ(N);
    }
}
