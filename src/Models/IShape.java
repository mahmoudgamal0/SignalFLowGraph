package Models;


import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;
import java.util.Map;

public interface IShape {

    void setProperties(Map<String,Object> properties);

    Map<String,Object> getPropertiesMap();

    void init(MouseEvent event);

    void addConnectedShape(IShape shape);

    ArrayList<IShape> getConnectedShapes();

    void setLabel(Label label);

    Label getLabel();
}
