package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.VBox;

public class MenuController {

    @FXML
    private Button btnStart;

    @FXML
    private Button btnHelp;

    @FXML
    private void handleStartAction(ActionEvent event) {
        crearTableroSudoku();
    }

    @FXML
    private void handleHelpAction(ActionEvent event) {
        // Crea un cuadro de diálogo de ayuda
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Ayuda");
        alert.setHeaderText("Instrucciones para jugar");
        alert.setContentText("Este es un juego de Sudoku 6x6. "
                + "\n\nLas reglas son las siguientes:"
                + "\n1. Completa el tablero con los números del 1 al 6."
                + "\n2. Cada fila, columna y bloque de 2x3 debe contener todos los números sin repetir."
                + "\n3. Si cometes un error, el cuadro se resaltará en rojo."
                + "\n4. Para comenzar un nuevo juego, haz clic en el botón 'Iniciar Juego'."
                + "\n\n¡Buena suerte!");
        alert.showAndWait(); // Muestra el cuadro de diálogo y espera a que se cierre
    }

    @FXML
    private void crearTableroSudoku() {
        int gridSize = 6;
        int[][] board = new int[gridSize][gridSize]; // Matriz lógica del Sudoku
        GridPane gridPane = new GridPane();

        // Alinear las celdas dentro del tablero
        for (int row = 0; row < gridSize; row++) {
            for (int col = 0; col < gridSize; col++) {
                TextField textField = new TextField();
                textField.setPrefHeight(60);
                textField.setPrefWidth(60);
                textField.setAlignment(Pos.CENTER);
                textField.setFont(Font.font(18));

                // Capturar los valores de row y col en variables finales
                final int currentRow = row;
                final int currentCol = col;

                // Validación del número ingresado
                textField.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (!newValue.matches("\\d*")) {
                        textField.setText(newValue.replaceAll("[^\\d]", "")); // Solo permitir dígitos
                    }
                    if (!newValue.isEmpty()) {
                        int num = Integer.parseInt(newValue);
                        if (num < 1 || num > 6) {
                            textField.setStyle("-fx-background-color: lightcoral;"); // Número inválido (fuera de rango)
                        } else {
                            // Validar las reglas del Sudoku
                            if (!SudokuValidator.isValid(board, currentRow, currentCol, num)) {
                                textField.setStyle("-fx-background-color: lightcoral;"); // Número inválido según las reglas
                            } else {
                                board[currentRow][currentCol] = num; // Actualizar el tablero lógico
                                textField.setStyle(""); // Número válido, restablecer el estilo
                            }
                        }
                    }
                });

                gridPane.add(textField, col, row);
            }
        }

        // Centrar el GridPane (tablero)
        gridPane.setAlignment(Pos.CENTER);

        // Crear un VBox para colocar el tablero y el botón de ayuda en la parte inferior
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);

        // Agregar el GridPane (tablero) al VBox
        vbox.getChildren().add(gridPane);

        // Crear el botón de ayuda
        Button btnHelp = new Button("Sugerencia (5 restantes)");
        vbox.getChildren().add(btnHelp);

        // Inicializar el contador de ayudas
        final int[] remainingHelps = {5};

        // Acción del botón de ayuda con límite de 5 ayudas
        btnHelp.setOnAction(event -> {
            if (remainingHelps[0] > 0) {
                sugerirNumero(board, gridPane);
                remainingHelps[0]--;
                btnHelp.setText("Sugerencia (" + remainingHelps[0] + " restantes)");

                // Desactivar el botón si se alcanzan 5 ayudas
                if (remainingHelps[0] == 0) {
                    btnHelp.setDisable(true);
                    btnHelp.setText("No quedan sugerencias");
                }
            }
        });

        // Configurar la escena con el VBox y mostrarla
        Scene scene = new Scene(vbox, 600, 650);
        Stage stage = (Stage) btnStart.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("Sudoku 6x6 - Juego");
    }

    private void sugerirNumero(int[][] board, GridPane gridPane) {
        // Buscar una celda vacía y sugerir un número válido
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                if (board[row][col] == 0) { // Encontrar una celda vacía
                    for (int num = 1; num <= 6; num++) {
                        if (SudokuValidator.isValid(board, row, col, num)) {
                            // Sugerir un número válido
                            TextField suggestedField = (TextField) gridPane.getChildren().get(row * 6 + col);
                            suggestedField.setStyle("-fx-background-color: lightblue;"); // Resaltar celda sugerida
                            suggestedField.setText(String.valueOf(num)); // Mostrar la sugerencia
                            return; // Solo sugerir un número
                        }
                    }
                }
            }
        }
    }
}
