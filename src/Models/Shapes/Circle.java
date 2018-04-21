package Models.Shapes;

import Models.IShape;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Circle extends javafx.scene.shape.Circle implements IShape {

    private Map<String, Object> propertiesMap;
    private ArrayList<IShape> connectedEdges;
    private Label label;

    public Circle(){
        super();
        this.propertiesMap = new HashMap<>();
        this.connectedEdges = new ArrayList<>();
        setStroke(Paint.valueOf("RED"));
        setFill(Paint.valueOf("TRANSPARENT"));
        setAccessibleText("hi");
        setRadius(15.0);
    }

    @Override
    public void setProperties(Map<String, Object> properties) {
        this.propertiesMap = properties;
    }

    @Override
    public Map<String, Object> getPropertiesMap() {
        return this.propertiesMap;
    }

    @Override
    public void init(MouseEvent event) { // Not used in here
        setLayoutX(event.getX());
        setLayoutY(event.getY());
    }

    @Override
    public void addConnectedShape(IShape shape) {
        this.connectedEdges.add(shape);
    }

    @Override
    public ArrayList<IShape> getConnectedShapes() {
        return this.connectedEdges;
    }

    @Override
    public void setLabel(Label label) {
        this.label = label;
        label.setLayoutX(this.getLayoutX() - this.getRadius()/2);
        label.setLayoutY(this.getLayoutY() - this.getRadius()/2);
    }

    @Override
    public Label getLabel() {
        return this.label;
    }
}
