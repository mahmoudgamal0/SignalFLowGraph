package Models;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class DialogBox {

    public static void dialog(StackPane pane, Node heading, Node... body)
    {
        JFXButton button = new JFXButton("Redraw");

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(heading);
        content.setBody(body);
        content.setActions(button);

        JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.CENTER);
        dialog.show();

        button.setOnAction(e->{
            dialog.close();
        });
    }
}
