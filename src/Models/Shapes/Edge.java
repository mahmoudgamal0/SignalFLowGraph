package Models.Shapes;

import Models.IShape;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Edge extends javafx.scene.shape.SVGPath implements IShape {

    private Map<String, Integer> propertiesMap;
    private ArrayList<IShape> connectedNodes;
    private String bx;
    private String by;
    private int sx;
    private int sy;

    public Edge(){
        super();
        this.propertiesMap = new HashMap<>();
        this.connectedNodes = new ArrayList<>();
        setStroke(Paint.valueOf("CYAN"));
        setFill(Paint.valueOf("Transparent"));
        setStrokeWidth(3);
    }

    @Override
    public void setProperties(Map<String, Integer> properties) {
        this.propertiesMap = properties;
        this.sx = this.propertiesMap.get("sx");
        this.bx = Integer.toString(this.sx);
        this.sy = this.propertiesMap.get("sy");
        this.by = Integer.toString( this.sy + 50);
    }

    @Override
    public Map<String, Integer> getPropertiesMap() {
        return this.propertiesMap;
    }

    @Override
    public void init(MouseEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        String ex = Integer.toString(x);
        String ey = Integer.toString(y + 50);
        String path = "M " + this.sx + "," + this.sy + " ";
        path += "C " + this.bx + "," + this.by + " " + ex + "," + ey + " " + ex + "," + Integer.toString(y);

        this.propertiesMap.put("fx",x);
        this.propertiesMap.put("fy",y);
        setContent(path);
    }

    public void refresh()
    {
        String ex = Integer.toString(this.propertiesMap.get("fx"));
        String ey = Integer.toString(this.propertiesMap.get("fy") + 50);
        String path = "M " + this.sx + "," + this.sy + " ";
        path += "C " + this.bx + "," + this.by + " " + ex + "," + ey + " " + ex + "," + Integer.toString(this.propertiesMap.get("fy"));

        setContent(path);
    }
    @Override
    public void addConnectedShape(IShape shape) {
        this.connectedNodes.add(shape);
    }

    @Override
    public ArrayList<IShape> getConnectedShapes() {
        return this.connectedNodes;
    }

    @Override
    public IShape clone(){
        IShape e = new Edge();
        Map<String, Integer> prop = new HashMap<>();
        prop.put("sx",propertiesMap.get("sx"));
        prop.put("sy",propertiesMap.get("sy"));
        prop.put("fx",propertiesMap.get("fx"));
        prop.put("fy",propertiesMap.get("fy"));
        e.setProperties(prop);
        return e;
    }
}
