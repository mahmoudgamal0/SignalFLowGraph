package Models;

import Models.Shapes.Circle;
import Models.Shapes.Edge;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Drawer {

    private IShape shape;
    private ArrayList<IShape> drawnCircles;
    private ArrayList<IShape> drawnEdges;

    public Drawer (ArrayList<IShape> drawnCircles, ArrayList<IShape> drawnEdges){
        this.drawnCircles = drawnCircles;
        this.drawnEdges = drawnEdges;
    }

    public void setShape(IShape shape)
    {
        this.shape = shape;
    }

    public void draw(Pane pane, MouseEvent event){
        this.shape = new Circle();
        this.drawnCircles.add(this.shape);
        this.shape.init(event);
        pane.getChildren().add((Node) this.shape);
    }

    public void drag(Pane pane, MouseEvent event)
    {
        if(event.getEventType().equals(MouseEvent.MOUSE_PRESSED))
        {
            pane.getChildren().add((Node)this.shape);
            Map<String,Integer> properties = new HashMap<>();
            properties.put("sx",(int)event.getX());
            properties.put("sy",(int)event.getY());
            this.shape.setProperties(properties);
        }

        this.shape.init(event);

        if(event.getEventType().equals(MouseEvent.MOUSE_RELEASED))
        {
            int[] validCircles = checkNodeValidity();
            if(validCircles != null)
            {
                connectEdgesAndNodes(validCircles);
                this.drawnEdges.add(this.shape);
                this.shape = new Edge();
                return;
            }
            else
            {
                pane.getChildren().remove(this.shape);
                this.shape = new Edge();
                throw new RuntimeException("Please draw a proper edge");
            }
        }
    }

    public void remove(Pane pane, int index){
        IShape shapeToDelete;
        if(this.shape instanceof Circle)
            shapeToDelete = this.drawnCircles.get(index);
        else
            shapeToDelete = this.drawnEdges.get(index);

        pane.getChildren().remove(shapeToDelete);

        if(this.shape instanceof Circle)
        {
            ArrayList<IShape> connectedEdges = shapeToDelete.getConnectedShapes();
            for(int i = 0 ; i < connectedEdges.size() ; i++)
            {
                IShape edge = connectedEdges.get(i);
                this.drawnEdges.remove(edge);
                pane.getChildren().remove(edge);
            }
            this.drawnCircles.remove(index);
        }
        else
            this.drawnEdges.remove(index);

    }

    private int[] checkNodeValidity() {

        int startX = this.shape.getPropertiesMap().get("sx");
        int startY = this.shape.getPropertiesMap().get("sy");
        int finishX = this.shape.getPropertiesMap().get("fx");
        int finishY = this.shape.getPropertiesMap().get("fy");

        int i,j;

        try{
            i = Utilities.getNearest(this.drawnCircles,new Point(startX,startY));
            j = Utilities.getNearest(this.drawnCircles,new Point(finishX,finishY));
        } catch (Exception e) {
            return null;
        }

        if(i == -1 || j == -1)
            return null;

        return new int[]{i,j};
    }

    private void connectEdgesAndNodes(int[] validNodes) {
        IShape node1 = this.drawnCircles.get(validNodes[0]);
        IShape node2 = this.drawnCircles.get(validNodes[1]);

        this.shape.addConnectedShape(node1);
        this.shape.addConnectedShape(node2);

        this.shape.getPropertiesMap().put("sx",(int)((Circle)node1).getLayoutX());
        this.shape.getPropertiesMap().put("sy",(int)((Circle)node1).getLayoutY());
        this.shape.getPropertiesMap().put("fx",(int)((Circle)node2).getLayoutX());
        this.shape.getPropertiesMap().put("fy",(int)((Circle)node2).getLayoutY());

        ((Edge)this.shape).refresh();

        node1.addConnectedShape(this.shape);
        node2.addConnectedShape(this.shape);
    }
}
