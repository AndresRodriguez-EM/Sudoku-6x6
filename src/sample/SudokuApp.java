import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.URL;

public class SudokuApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Intenta cargar el FXML
            URL fxmlURL = getClass().getResource("/sample/menu.fxml");
            if (fxmlURL == null) {
                System.out.println("El archivo FXML no se encuentra.");
                return; // O lanza una excepción si prefieres
            }
            Parent root = FXMLLoader.load(fxmlURL);
            primaryStage.setTitle("Sudoku 6x6");
            primaryStage.setScene(new Scene(root, 600, 400));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
