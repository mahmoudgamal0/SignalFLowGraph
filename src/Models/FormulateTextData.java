package Models;

import javafx.scene.Node;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class FormulateTextData {

    private ArrayList<String> paths;
    private ArrayList<String> loops;
    private ArrayList<String> deltas;
    private String gain;

    public FormulateTextData(ArrayList<String> paths, ArrayList<String> loops, ArrayList<String> deltas, String gain) {
        this.paths = paths;
        this.loops = loops;
        this.deltas = deltas;
        this.gain = gain;
    }

    public Node getData(){
        return formulateGain((Text) formulateLoops((Text)formulateDeltas((Text) formulatePaths(new Text()))));
    }

    private Node formulateGain(Text label){
        label.setText(label.getText() + "Overall Gain: \n" + this.gain);
        return label;
    }

    private Node formulatePaths(Text label)
    {
        label.setText(label.getText() + "Forward Paths: \n");
        return formulate(this.paths,label);
    }

    private Node formulateDeltas(Text label)
    {
        label.setText(label.getText() + "Deltas: \n");
        return formulate(this.deltas,label);
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
