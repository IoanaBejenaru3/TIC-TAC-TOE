import java.util.Scanner;

/**
 * This class contains the complete logic for a Tic-Tac-Toe (X and O) game
 * between a single human player and the computer.
 * The game uses a standard backtracking (minimax) algorithm to determine
 * the best move for the computer.
 */
public class Main {

    /**
     * Internal representation of the 3x3 game board as a 2D char array.
     * ' ' (space) indicates an empty cell.
     * 'X' indicates the human player's move.
     * 'O' indicates the computer's move.
     */
    private static char[][] board = {
            {' ', ' ', ' '},
            {' ', ' ', ' '},
            {' ', ' ', ' '}
    };

    private static final char PLAYER = 'X';
    private static final char COMPUTER = 'O';

    // Scanner used to read user input from the console
    private static Scanner sc = new Scanner(System.in);

    /**
     * The main method
     */
    public static void main(String[] args) {
        System.out.println("=== TIC-TAC-TOE ===");
        System.out.println("You will play with X, the computer will play with O.");
        System.out.println("Please enter row and column as: row column (numbered 1-3).");
        System.out.println("Example for the top-left corner: 1 1\n");

        // Main game loop: stops when there is a winner or the board is full
        while (true) {
            // Step 1: Display the current board
            printBoard();

            // Step 2: The human player (X) makes a move
            playerMove();
            if (checkWin(PLAYER)) {
                printBoard();
                System.out.println("Congratulations! You have won!");
                break;
            }
            if (isBoardFull()) {
                printBoard();
                System.out.println("It's a draw!");
                break;
            }

            // Step 3: The computer (O) makes a move using the minimax backtracking algorithm
            System.out.println("Computer is thinking...");
            computerMove();
            if (checkWin(COMPUTER)) {
                printBoard();
                System.out.println("The computer has won!");
                break;
            }
            if (isBoardFull()) {
                printBoard();
                System.out.println("It's a draw!");
                break;
            }
        }

        // Close the scanner
        sc.close();
    }

    /**
     * This method prints the current board state to the console.
     * Each cell of the board is displayed in text format with simple separators.
     */
    private static void printBoard() {
        System.out.println("Current board:");
        for (int i = 0; i < 3; i++) {
            // Print each row
            for (int j = 0; j < 3; j++) {
                System.out.print(" " + board[i][j]);
                if (j < 2) {
                    // Column separator
                    System.out.print(" |");
                }
            }
            System.out.println();
            if (i < 2) {
                // Horizontal separator between rows
                System.out.println("---+---+---");
            }
        }
        System.out.println();
    }

    /**
     * This method reads and executes the human player's move from the console.
     * It repeats until a valid move (in an empty cell) is entered.
     */
    private static void playerMove() {
        while (true) {
            System.out.print("Your move (row column): ");
            int row = sc.nextInt() - 1;  // Convert from 1-3 to 0-2
            int col = sc.nextInt() - 1;

            // Check if the indices are within [0, 2]
            if (row >= 0 && row < 3 && col >= 0 && col < 3) {
                // Check if the position is empty
                if (board[row][col] == ' ') {
                    board[row][col] = PLAYER;
                    break;  // Exit the loop once a valid move is made
                } else {
                    System.out.println("That position is already taken! Please try again.");
                }
            } else {
                System.out.println("Invalid index, please enter values between 1 and 3.");
            }
        }
    }
    
    private static void computerMove() {
        // Keep track of the best possible score for the computer.
        // Integer.MIN_VALUE is used as a starting point for comparison.
        int bestScore = Integer.MIN_VALUE;

        // Variables to store the row and column of the best move.
        int bestRow = -1;
        int bestCol = -1;

        // Loop over all cells of the 3x3 board to find potential moves.
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                // Check if this cell is empty (i.e., a valid move).
                if (board[i][j] == ' ') {
                    // Temporarily place the computer's move in this cell.
                    board[i][j] = COMPUTER;

                    /*
                     * Call minimax with "false" indicating that after the computer moves,
                     * it will be the opponent's turn (the player).
                     * The minimax function will return a score indicating how good
                     * this move is for the computer from that point forward.
                     */
                    int score = minimax(false);

                    // Undo the move (backtracking step), so the board state
                    // is reverted to what it was before this hypothetical move.
                    board[i][j] = ' ';

                    // If the score for this move is better than the bestScore so far,
                    // update bestScore and remember this move's position.
                    if (score > bestScore) {
                        bestScore = score;
                        bestRow = i;
                        bestCol = j;
                    }
                }
            }
        }

        // Now, we place the computer's symbol at the best move found (bestRow, bestCol).
        // If bestRow/bestCol remained -1, that means no valid move was found,
        // but in Tic-Tac-Toe, there should always be a move if the board isn't full.
        if (bestRow != -1 && bestCol != -1) {
            board[bestRow][bestCol] = COMPUTER;
        }
    }
    
    private static int minimax(boolean isMaximizing) {
        // Check if the computer has won; if so, return +1.
        if (checkWin(COMPUTER)) {
            return 1;
        }
        // Check if the player has won; if so, return -1.
        if (checkWin(PLAYER)) {
            return -1;
        }
        // Check if the board is full without any winner; if so, it's a draw, return 0.
        if (isBoardFull()) {
            return 0;
        }

        // If it's the maximizing player's turn (the computer, O)
        if (isMaximizing) {
            // Initialize the best score to the lowest possible integer
            int bestScore = Integer.MIN_VALUE;

            // Loop through each cell in the 3x3 board
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // If the cell is empty, we can "try" that move
                    if (board[i][j] == ' ') {
                        // Place the COMPUTER symbol in this cell
                        board[i][j] = COMPUTER;

                        /*
                         * Recursively call minimax with isMaximizing = false,
                         * because after the computer moves, the player gets the turn.
                         */
                        int score = minimax(false);

                        // Undo the move so we can try other possibilities (backtracking)
                        board[i][j] = ' ';

                        // Compare returned score with the current bestScore, keep the maximum
                        bestScore = Math.max(bestScore, score);
                    }
                }
            }
            // Return the highest score found, which reflects the best outcome for the computer.
            return bestScore;
        } else {
            // If it's the minimizing player's turn (the human, X)
            // Initialize the best score to the highest possible integer
            int bestScore = Integer.MAX_VALUE;

            // Loop through each cell in the 3x3 board
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    // If the cell is empty, we can "try" that move
                    if (board[i][j] == ' ') {
                        // Place the PLAYER symbol in this cell
                        board[i][j] = PLAYER;

                        /*
                         * Recursively call minimax with isMaximizing = true,
                         * because after the player moves, the computer gets the turn.
                         */
                        int score = minimax(true);

                        // Undo the move (backtracking)
                        board[i][j] = ' ';

                        // Compare returned score with the current bestScore, keep the minimum
                        bestScore = Math.min(bestScore, score);
                    }
                }
            }
            // Return the lowest score found, which reflects the worst outcome for the computer
            // (i.e., the best the player can do).
            return bestScore;
        }
    }
    
    private static boolean checkWin(char symbol) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == symbol && board[i][1] == symbol && board[i][2] == symbol) {
                return true;
            }
        }

        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == symbol && board[1][j] == symbol && board[2][j] == symbol) {
                return true;
            }
        }

        // Check diagonals
        // Main diagonal
        if (board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            return true;
        }
        // Secondary diagonal
        if (board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol) {
            return true;
        }

        // If none of the above conditions are met, there's no winner
        return false;
    }

    /* Should the game stop or not */
    private static boolean isBoardFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }
}
