import java.io.IOException;
import java.nio.file.Paths;

import gigachad.Gigachad;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * A GUI for gigachad using FXML.
 */
public class Main extends Application {

    private Gigachad gigachad = new Gigachad(Paths.get("data/tasks.txt"));

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();
            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setGigachad(gigachad);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
