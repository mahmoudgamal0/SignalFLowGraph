import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainGUI extends Application{


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("Views/GUI.fxml"));
        root.getStylesheets().add("Views/CSS/css.css");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

}
