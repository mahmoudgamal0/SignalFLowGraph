package Models.Shapes;

import Models.Shape;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcType;

import java.util.HashMap;
import java.util.Map;

public class Edge extends Shape{

    private Map<String, Integer> properties;

    public Edge(){
        this.properties = new HashMap<>();
        this.properties.put("Gain",0);
        this.properties.put("X",0);
        this.properties.put("Y",0);
        this.properties.put("X2",0);
        this.properties.put("Y2",0);
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
        Shape e = new Edge();
        Map<String, Integer> prop = new HashMap<>();
        prop.put("X1",this.properties.get("X"));
        prop.put("Y1",this.properties.get("Y"));
        e.setProperties(prop);
        return e;
    }

    @Override
    public void draw(GraphicsContext gc) {
        int x1 = this.properties.get("X1");
        int y1 = this.properties.get("Y1");
        int x2 = this.properties.get("X2");
        int y2 = this.properties.get("Y2");
        int r = (int)Math.sqrt(Math.pow(x2-x1,2) + Math.pow(y2-y1,2))/ 2;

        double theta = Math.atan((double)(y1-y2)/(x2-x1));
        double phi = Math.atan((double)2 * Math.sin(theta) / Math.cos(theta))*180 / Math.PI;
        int h = 2 * (int) Math.abs(2*r*Math.sin(theta)) ;
        int w = 2 * (int)Math.abs(2*r*Math.cos(theta));

        gc.setFill(Color.BLACK);
        if(x2 > x1)
        {
            if(y1 > y2)
                gc.strokeArc(x1, y1 -(h/2), w, h, 90,90 , ArcType.OPEN);
            else
                gc.strokeArc(x1, y1 -(h/2), w, h, -90,-90 , ArcType.OPEN);
        }
        else
        {
            if(y2 > y1)
                gc.strokeArc(x2, y2 -(h/2), w, h, 90,90 , ArcType.OPEN);
            else
                gc.strokeArc(x2, y2 -(h/2), w, h, -90,-90 , ArcType.OPEN);
        }
    }
}
