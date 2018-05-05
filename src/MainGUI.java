import Controllers.Controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.naming.ldap.Control;

public class MainGUI extends Application{


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("Views/GUI.fxml").openStream());
        root.getStylesheets().add("Views/CSS/css.css");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        ((Controller)loader.getController()).init();
    }

}
