package sample;

import java.util.Random;

public class SudokuBoard {
    private int[][] board;
    private static final int SIZE = 6; // Tamaño del tablero 6x6
    private static final int SUBGRID_ROWS = 2; // Tamaño de los bloques (2x3)
    private static final int SUBGRID_COLS = 3;

    public SudokuBoard() {
        board = new int[SIZE][SIZE];
        generateBoard();
    }

    private void generateBoard() {
        // Inicializa el tablero vacío
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = 0; // Inicializamos con celdas vacías
            }
        }

        // Lógica para llenar el tablero de Sudoku
        fillBoard(0, 0);  // Comienza llenando desde la posición (0, 0)
    }

    // Llenado del tablero usando backtracking
    private boolean fillBoard(int row, int col) {
        if (row == SIZE) {
            return true; // Si llegamos al final, el tablero está completo
        }

        int nextRow = (col == SIZE - 1) ? row + 1 : row; // Pasa a la siguiente fila si estamos al final de la columna
        int nextCol = (col == SIZE - 1) ? 0 : col + 1;

        // Prueba con números del 1 al 6
        Random rand = new Random();
        int[] numbers = {1, 2, 3, 4, 5, 6};
        shuffleArray(numbers); // Mezcla los números para mayor aleatoriedad

        for (int num : numbers) {
            if (isValidMove(row, col, num)) {
                board[row][col] = num;
                if (fillBoard(nextRow, nextCol)) {
                    return true;
                }
                board[row][col] = 0; // Si no funciona, deshacer el movimiento
            }
        }

        return false; // No se encontró una solución
    }

    // Algoritmo para mezclar un array (Fisher-Yates)
    private void shuffleArray(int[] array) {
        Random rand = new Random();
        for (int i = array.length - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }
    }

    // Verifica si un número puede colocarse en la posición dada
    public boolean isValidMove(int row, int col, int value) {
        // Verificar fila
        for (int j = 0; j < SIZE; j++) {
            if (board[row][j] == value) {
                return false;
            }
        }

        // Verificar columna
        for (int i = 0; i < SIZE; i++) {
            if (board[i][col] == value) {
                return false;
            }
        }

        // Verificar bloque de 2x3
        int blockRow = (row / SUBGRID_ROWS) * SUBGRID_ROWS;
        int blockCol = (col / SUBGRID_COLS) * SUBGRID_COLS;
        for (int i = blockRow; i < blockRow + SUBGRID_ROWS; i++) {
            for (int j = blockCol; j < blockCol + SUBGRID_COLS; j++) {
                if (board[i][j] == value) {
                    return false;
                }
            }
        }

        return true; // El movimiento es válido
    }

    public int[][] getBoard() {
        return board;
    }
}