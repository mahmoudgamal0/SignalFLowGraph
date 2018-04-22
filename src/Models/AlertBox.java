package Models;

import javafx.scene.control.Alert;

public class AlertBox {

    public static void alert(String header, String message)
    {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText(header);
        alert.setContentText(message);

        alert.showAndWait();
    }
}
