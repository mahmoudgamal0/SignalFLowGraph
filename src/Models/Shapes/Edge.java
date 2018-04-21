package Models.Shapes;

import Models.IShape;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.Path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Edge extends javafx.scene.shape.CubicCurve implements IShape {

    private Map<String, Object> propertiesMap;
    private ArrayList<IShape> connectedNodes;
    private Label label;
    private int sx;
    private int sy;

    public Edge(){
        super();
        this.propertiesMap = new HashMap<>();
        this.propertiesMap.put("sx",0);
        this.propertiesMap.put("sy",0);
        this.propertiesMap.put("fx",0);
        this.propertiesMap.put("fy",0);
        this.propertiesMap.put("type",0);
        this.propertiesMap.put("gain"," ");
        this.connectedNodes = new ArrayList<>();
        setStroke(Paint.valueOf("CYAN"));
        setFill(Paint.valueOf("Transparent"));
        setStrokeWidth(3);
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
    public void init(MouseEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();
        sx = (Integer)propertiesMap.get("sx");
        sy = (Integer)propertiesMap.get("sy");
        drawCurve(x,y);
        this.propertiesMap.put("fx",x);
        this.propertiesMap.put("fy",y);
    }

    public void refresh()
    {
        int x = (Integer)this.propertiesMap.get("fx");
        int y = (Integer)this.propertiesMap.get("fy");
        sx = (Integer)propertiesMap.get("sx");
        sy = (Integer)propertiesMap.get("sy");
        drawCurve(x,y);
    }

    private void drawCurve(int x , int y)
    {
       setStartX(sx);
       setStartY(sy);
       setEndX(x);
       setEndY(y);

       double r = Math.sqrt(Math.pow(sx-x,2))/2;
       if(x > sx)
       {
           setControlX1(sx + r/2);
           setControlY1(sy - r);
           setControlX2(x - r/2);
           setControlY2(sy - r);
           propertiesMap.put("type",1);
       }
       else
       {
           setControlX1(sx - r/2);
           setControlY1(sy + r);
           setControlX2(x + r/2);
           setControlY2(sy + r);
           propertiesMap.put("type",0);
       }
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
    public void setLabel(Label label) {
        this.label = label;
        int x1 =(Integer) propertiesMap.get("sx");
        int x2 = (Integer)propertiesMap.get("fx");
        double r = Math.sqrt(Math.pow(x1-x2,2))/2;

        if(x1 > x2) // back
        {

            label.setLayoutX(x1 - r - (label.getWidth() / 2));
            label.setLayoutY((Integer)propertiesMap.get("sy") + this.getBoundsInParent().getHeight() - (label.getHeight() / 2));
            System.out.println(label.getLayoutY());
        }
        else // next
        {
            label.setLayoutX(x1 + r -(label.getWidth() / 2));
            label.setLayoutY((Integer)propertiesMap.get("sy")  - this.getBoundsInParent().getHeight() - (label.getHeight() / 2));
        }

        label.setText((String) propertiesMap.get("gain"));
    }

    @Override
    public Label getLabel() {
        return this.label;
    }
}
