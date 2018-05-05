package Models;

import javafx.scene.Node;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class FormulateTextData {

    private ArrayList<String> paths;
    private ArrayList<String> loops;

    public FormulateTextData(ArrayList<String> paths, ArrayList<String> loops) {
        this.paths = paths;
        this.loops = loops;
    }

    public Node getData(){
        return formulateLoops((Text) formulatePaths(new Text()));
    }
    private Node formulatePaths(Text label)
    {
        label.setText(label.getText() + "Forward Paths: \n");
        return formulate(this.paths,label);
    }

    private Node formulateLoops(Text label)
    {
        label.setText(label.getText() + "All Loops: \n");
        return formulate(this.loops,label);
    }

    private Node formulate(ArrayList<String> list, Text label)
    {
        for (String text: list) {
            label.setText(label.getText() + text);
        }
        return label;
    }
}
