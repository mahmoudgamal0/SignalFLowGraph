package Models;

import Models.Shapes.Circle;
import Models.Shapes.Edge;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Paint;
import javafx.scene.text.TextAlignment;

import java.awt.*;
import java.util.ArrayList;

public class ShapeTracker {

    private ArrayList<IShape> drawnCircles;
    private ArrayList<IShape> drawnEdges;

    public ShapeTracker (ArrayList<IShape> drawnCircles, ArrayList<IShape> drawnEdges) {
        this.drawnCircles = drawnCircles;
        this.drawnEdges = drawnEdges;
    }

    public ArrayList<IShape> getDrawnCircles() {
        return drawnCircles;
    }

    public ArrayList<IShape> getDrawnEdges() {
        return drawnEdges;
    }


    public void add(IShape shape) {
        if(shape instanceof Circle)
            this.drawnCircles.add(shape);
        else
            this.drawnEdges.add(shape);
    }

    public void remove(IShape shape) {
        if(shape instanceof Circle)
            this.drawnCircles.remove(shape);
        else
            this.drawnEdges.remove(shape);
    }

    public int getX()
    {
        return 50 + 70*this.drawnCircles.size();
    }

    public int getY()
    {
        return 350;
    }

    public Label getNodeLabel()
    {
        Label label = new Label();
        label.setText(Integer.toString(this.drawnCircles.size()));
        label.setTextFill(Paint.valueOf("BLUE"));
        label.setTextAlignment(TextAlignment.valueOf("JUSTIFY"));
        return label;
    }

    public Label getEdgeLabel(int c)
    {
        Label label = new Label();
        if(c == 1) //right edge
            label.setGraphic(new ImageView("/Views/Images/next.png"));
        else // left edge
            label.setGraphic(new ImageView("/Views/Images/back.png"));
        return label;
    }

    public IShape[] checkNodeValidity(IShape shape) {

        int startX = (Integer)shape.getPropertiesMap().get("sx");
        int startY = (Integer)shape.getPropertiesMap().get("sy");
        int finishX = (Integer)shape.getPropertiesMap().get("fx");
        int finishY = (Integer)shape.getPropertiesMap().get("fy");

        IShape i,j;
        try{
            i = getNearest(new Point(startX,startY));
            j = getNearest(new Point(finishX,finishY));
        } catch (Exception e) {
            return null;
        }

        if(i == null || j == null)
            return null;

        return new IShape[]{i,j};
    }

    public void connectEdgesAndNodes(IShape[] validNodes, IShape shape) {
        IShape node1 = validNodes[0];
        IShape node2 = validNodes[1];

        shape.addConnectedShape(node1);
        shape.addConnectedShape(node2);

        shape.getPropertiesMap().put("sx",(int)((Circle)node1).getLayoutX());
        shape.getPropertiesMap().put("sy",(int)((Circle)node1).getLayoutY());
        shape.getPropertiesMap().put("fx",(int)((Circle)node2).getLayoutX());
        shape.getPropertiesMap().put("fy",(int)((Circle)node2).getLayoutY());

        ((Edge)shape).refresh();

        node1.addConnectedShape(shape);
        node2.addConnectedShape(shape);
    }

    public IShape getNearest(Point point) {

        if(drawnCircles.size() == 0)
            throw new RuntimeException("There are no NODES !!!");
        int minX = (int)((Circle)drawnCircles.get(0)).getLayoutX();
        int minY = (int)((Circle)drawnCircles.get(0)).getLayoutY();
        int minProp = 0;
        for(int i = 1 ; i < drawnCircles.size() ; i++)
        {
            int x = (int)((Circle)drawnCircles.get(i)).getLayoutX();
            int y = (int)((Circle)drawnCircles.get(i)).getLayoutY();
            if(isNearest(point,minX,minY,x,y))
            {
                minX = x;
                minY = y;
                minProp = i;
            }
        }
        if(contains(point, (Circle)drawnCircles.get(minProp)))
            return drawnCircles.get(minProp);
        return null;
    }

    private boolean contains(Point point, Circle circle) {
        int r = (int)circle.getRadius();
        int x = (int)circle.getLayoutX();
        int y = (int)circle.getLayoutY();

        if(point.x > (x-r) && point.x < (x+r))
            if(point.y > (y-r) && point.y < (y+r))
                return true;
        return false;
    }

    private boolean isNearest(Point point, int minX, int minY, int x, int y) {
        int diffX = Math.abs(x - point.x);
        int diffXMin = Math.abs(minX - point.x);

        if(diffXMin > diffX)
            return true;

        int diffY = Math.abs(y - point.y);
        int diffYMin = Math.abs(minY - point.y);
        if(diffYMin > diffY)
            return true;
        return false;
    }

}
