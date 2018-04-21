package Models.Shapes;

import Models.IShape;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.scene.shape.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Edge extends javafx.scene.shape.SVGPath implements IShape {

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
        setFill(Paint.valueOf("TRANSPARENT"));
        setStrokeWidth(3);
        setPickOnBounds(false);

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

        String startX = Integer.toString(sx);
        String startY = Integer.toString(sy);
        String finishX = Integer.toString(x);
        String finishY = Integer.toString(y);
        String controlX1;
        String controlY1;
        String controlX2;
        String controlY2;

        double r = Math.sqrt(Math.pow(sx-x,2))/2;
        if(x > sx)
        {
            controlX1 =Integer.toString((int) (sx + r/2));
            controlY1 = Integer.toString((int) (sy - r));
            controlX2 = Integer.toString((int) (x - r/2));
            controlY2 = Integer.toString((int) (sy - r));
            propertiesMap.put("type",1);
        }
        else
        {
            controlX1 = Integer.toString((int) (sx - r/2));
            controlY1 = Integer.toString((int) (sy + r));
            controlX2 = Integer.toString((int) (x + r/2));
            controlY2 = Integer.toString((int) (sy + r));
            propertiesMap.put("type",0);
        }

        String path = "M " + startX + "," + startY +" ";
        path+= "C " + controlX1 + "," + controlY1 + " " + controlX2 + "," + controlY2 + " " + finishX + "," + finishY;

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
    public void setLabel(Label label) {
        this.label = label;
        int x1 =(Integer) propertiesMap.get("sx");
        int x2 = (Integer)propertiesMap.get("fx");
        double r = Math.sqrt(Math.pow(x1-x2,2))/2;

        if(x1 > x2) // back
        {

            label.setLayoutX(x1 - r - 8);
            label.setLayoutY((Integer)propertiesMap.get("sy") + this.getBoundsInParent().getHeight() - 5);
            System.out.println(label.getLayoutY());
        }
        else // next
        {
            label.setLayoutX(x1 + r - 8);
            label.setLayoutY((Integer)propertiesMap.get("sy")  - this.getBoundsInParent().getHeight() - 5);
        }

        label.setText((String) propertiesMap.get("gain"));
    }

    @Override
    public Label getLabel() {
        return this.label;
    }
}
