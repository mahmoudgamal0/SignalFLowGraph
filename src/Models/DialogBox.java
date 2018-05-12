package Models;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.Node;
import javafx.scene.control.TextArea;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;


public class DialogBox {

    public static void dialog(StackPane pane, Node heading, Node body)
    {
        JFXButton button = new JFXButton("Redraw");

        TextArea area = new TextArea();
        area.setEditable(false);
        area.setText(((Text)body).getText());

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(heading);
        content.setBody(area);
        content.setActions(button);



        JFXDialog dialog = new JFXDialog(pane,content,JFXDialog.DialogTransition.CENTER);
        dialog.show();

        button.setOnAction(e->{
            dialog.close();
        });
    }
}
