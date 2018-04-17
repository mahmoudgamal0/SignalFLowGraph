package Models.Shapes;

import Models.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Circle extends Shape {

    private Map<String, Integer> properties;

    public Circle(){
        this.properties = new HashMap<>();
        this.properties.put("R",0);
        this.properties.put("X",0);
        this.properties.put("Y",0);
    }

    @Override
    public void setProperties(Map<String, Integer> properties) {
        this.properties = properties;
    }

    @Override
    public Map<String, Integer> getProperties() {
        return this.properties;
    }

    @Override
    public Shape clone(){
        Shape c = new Circle();
        Map<String, Integer> prop = new HashMap<>();
        prop.put("X",this.properties.get("X"));
        prop.put("Y",this.properties.get("Y"));
        prop.put("R",this.properties.get("R"));
        c.setProperties(prop);
        return c;
    }

    @Override
    public void draw(GraphicsContext gc) {
        int r = this.properties.get("R") / 2;
        int x = this.properties.get("X");
        int y = this.properties.get("Y");
        gc.setFill(Color.BLACK);
        gc.strokeOval(x-r,y-r,2*r,2*r);
    }
}
