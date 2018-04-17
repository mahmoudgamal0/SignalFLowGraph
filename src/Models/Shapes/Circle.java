package Models.Shapes;

import Models.IShape;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Circle extends javafx.scene.shape.Circle implements IShape {

    private Map<String, Integer> propertiesMap;
    private ArrayList<IShape> connectedEdges;

    public Circle(){
        super();
        this.propertiesMap = new HashMap<>();
        this.connectedEdges = new ArrayList<>();
        setStroke(Paint.valueOf("RED"));
        setFill(Paint.valueOf("WHITE"));
        setRadius(15.0);
    }

    @Override
    public void setProperties(Map<String, Integer> properties) {
        this.propertiesMap = properties;
    }

    @Override
    public Map<String, Integer> getPropertiesMap() {
        return this.propertiesMap;
    }

    @Override
    public void init(MouseEvent event) {
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
    public IShape clone(){
        IShape c = new Circle();
        Map<String, Integer> prop = new HashMap<>();

        c.setProperties(prop);
        return c;
    }
}
