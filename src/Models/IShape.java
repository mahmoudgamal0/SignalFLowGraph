package Models;


import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Map;

public interface IShape {

    void setProperties(Map<String,Integer> properties);

    Map<String,Integer> getPropertiesMap();

    void init(MouseEvent event);

    void addConnectedShape(IShape shape);

    ArrayList<IShape> getConnectedShapes();

    IShape clone();
}
