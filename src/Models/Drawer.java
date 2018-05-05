package Models;

import Models.Shapes.Circle;
import Models.Shapes.Edge;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.awt.*;
import java.util.ArrayList;
import java.util.Map;

public class Drawer {

    private IShape shape;
    private ShapeTracker tracker;

    public Drawer (ShapeTracker tracker){
        this.tracker = tracker;
    }

    public void drawNode(Pane pane, IShape shape)
    {
        ((Circle)shape).setLayoutX(this.tracker.getX());
        ((Circle)shape).setLayoutY(this.tracker.getY());
        shape.setLabel(this.tracker.getNodeLabel());
        draw(pane,shape);
    }

    private void draw(Pane pane, IShape shape){
        this.shape = shape;
        this.tracker.add(this.shape);
        pane.getChildren().add((Node) this.shape);
        pane.getChildren().add(this.shape.getLabel());
    }

    public void drag(Pane pane, MouseEvent event, IShape shape)
    {
        // When Mouse is Pressed
        if(event.getEventType().equals(MouseEvent.MOUSE_PRESSED))
        {
            this.shape = shape;
            pane.getChildren().add((Node)this.shape);
            Map<String,Object> properties = this.shape.getPropertiesMap();
            properties.put("sx",(int)event.getX());
            properties.put("sy",(int)event.getY());
            properties.put("fx",(int)event.getX());
            properties.put("fy",(int)event.getY());
        }

        // When Mouse is Dragged
        this.shape.init(event);

        // When Mouse is Released
        if(event.getEventType().equals(MouseEvent.MOUSE_RELEASED))
        {
            IShape[] validCircles = tracker.checkNodeValidity(this.shape);
            if(validCircles != null)
            {
                this.tracker.connectEdgesAndNodes(validCircles,this.shape);
                this.tracker.add(this.shape);
                this.shape.setLabel(this.tracker.getEdgeLabel((Integer)this.shape.getPropertiesMap().get("type")));
                pane.getChildren().add(this.shape.getLabel());
            }
            else
            {
                pane.getChildren().remove(this.shape);
                throw new RuntimeException("Please draw a proper edge");
            }
        }
    }

    public void removeEdge(Pane pane, int edge)
    {
        remove(pane,this.tracker.getDrawnEdges().get(edge));
    }

    public void removeNode(Pane pane, MouseEvent event) throws RuntimeException
    {
        Point point = new Point((int) event.getX(),(int)event.getY());
        IShape circle = this.tracker.getNearest(point);

        if(circle == null)
            throw new RuntimeException("Please Select a Node");

        remove(pane,circle);
    }

    private void remove(Pane pane, IShape shape){
        this.tracker.remove(shape);
        pane.getChildren().remove(shape);
        pane.getChildren().remove(shape.getLabel());
        if(shape instanceof Circle)
        {
            for(int i = 0 ; i < shape.getConnectedShapes().size() ; i++)
            {
                IShape edge = shape.getConnectedShapes().get(i);
                remove(pane,edge);
            }
            redraw(pane);
        }
    }

    private void redraw(Pane pane)
    {
        pane.getChildren().clear();
        ArrayList<IShape> nodes = this.tracker.getDrawnCircles();
        ArrayList<IShape> edges = this.tracker.getDrawnEdges();

        ArrayList<IShape> tempNodes = new ArrayList<>(nodes);
        nodes.clear();

        ArrayList<IShape> tempEdges = new ArrayList<>(edges);
        edges.clear();

        for (IShape node: tempNodes) {
            drawNode(pane,node);
        }

        for (IShape edge: tempEdges) {

            IShape node1 = edge.getConnectedShapes().get(0);
            IShape node2 = edge.getConnectedShapes().get(1);
            edge.getConnectedShapes().clear();

            IShape[] nodesConnected = {node1,node2};
            this.tracker.connectEdgesAndNodes(nodesConnected,edge);
            edge.setLabel(this.tracker.getEdgeLabel((Integer)edge.getPropertiesMap().get("type")));
            draw(pane,edge);
        }

    }
}
