package Controllers;

import Models.Shapes.Circle;
import Models.Shape;
import Models.Shapes.Edge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.CubicCurve;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Controller{

    @FXML
    private SplitPane splitPane;
    @FXML
    private JFXListView<JFXButton> listView;
    @FXML
    private Canvas canvas;

    private ArrayList<Shape> drawnCircles;

    private Shape selectedShape;
    private String mode;
    private boolean firstPointSet;
    private Point firstPoint;

    private JFXButton addNode;
    private JFXButton deleteNode;
    private JFXButton addEdge;
    private JFXButton removeEdge;
    private JFXButton calculate;

    private boolean isExpanded;
    private Stage window;

    public Controller(){
        this.addNode = new JFXButton("Add Node");
        this.addEdge = new JFXButton("Add Edge");
        this.deleteNode = new JFXButton("Delete Node");
        this.removeEdge = new JFXButton("Remove Edge");
        this.calculate = new JFXButton("Calculate");

        this.drawnCircles = new ArrayList<>();
    }


    @FXML
    public void initialize() {
        initButtons();
        initListView();

        CubicCurve cc = new CubicCurve();

    }

    private void initButtons() {
        this.addNode.setOnAction(e->{
            this.selectedShape = new Circle();
            this.mode = "add";
        });

        this.addEdge.setOnAction(e->{
            this.selectedShape = new Edge();
            this.mode = "add";
        });

        this.deleteNode.setOnAction(e->{
            this.selectedShape = new Circle();
            this.mode = "remove";
        });
    }

    private void initListView() {
        this.listView.getItems().addAll(addNode,addEdge,deleteNode,removeEdge,calculate);
        this.listView.expandedProperty().set(true);
        this.listView.setVerticalGap(10.0);

        this.listView.setOnMouseEntered(e->{
            this.listView.setExpanded(true);
        });

        this.listView.setOnMouseExited(e->{
            this.listView.setExpanded(false);
        });
    }

    @FXML
    public void move()
    {
        this.window = (Stage)this.splitPane.getScene().getWindow();
        Node divider = this.splitPane.lookup(".split-pane-divider");
        divider.setOnMouseClicked(e->{
            if(!isExpanded)
                showSwapPane().play();
            else
                hideSwapPane().play();
        });

        this.window.widthProperty().addListener((observable,o,n)->{
            this.splitPane.setDividerPosition(0,1);
        });
    }

    @FXML
    public void cursor(MouseEvent event){

        if(this.selectedShape == null)
            return;

        if(this.mode.equals("add"))
        {
            this.selectedShape.setProperties(setShapeProperties(event));
            if(!this.firstPointSet)
                this.selectedShape.draw(this.canvas.getGraphicsContext2D());
            this.drawnCircles.add(this.selectedShape.clone());
        }
        else
        {
            int i = 0;
            try{
                i = getNearest(event);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if(i == -1)
                System.out.println("Error message");
            else
            {
                this.drawnCircles.remove(i);
                redraw();
            }
        }

    }

    private void redraw() {
        this.canvas.getGraphicsContext2D().clearRect(0,0,this.canvas.getWidth(),this.canvas.getHeight());
        for(int i = 0 ; i < this.drawnCircles.size(); i++)
            this.drawnCircles.get(i).draw(this.canvas.getGraphicsContext2D());
    }

    private int getNearest(MouseEvent event) {

        Point point = new Point((int)event.getX(),(int)event.getY());
        if(this.drawnCircles.size() == 0)
            throw new RuntimeException("Display an error message of no nodes");
        int minX = this.drawnCircles.get(0).getProperties().get("X");
        int minY = this.drawnCircles.get(0).getProperties().get("Y");
        int minProp = 0;
        for(int i = 1 ; i < this.drawnCircles.size() ; i++)
        {
            int x = this.drawnCircles.get(i).getProperties().get("X");
            int y = this.drawnCircles.get(i).getProperties().get("Y");
            if(isNearest(point,minX,minY,x,y))
            {
                minX = x;
                minY = y;
                minProp = i;
            }
        }

        if(contains(point,this.drawnCircles.get(minProp).getProperties()))
            return minProp;
        return -1;
    }

    private boolean contains(Point point, Map<String, Integer> properties) {
        int r = properties.get("R") / 2;
        int x = properties.get("X");
        int y = properties.get("Y");

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


    private Map<String,Integer> setShapeProperties(MouseEvent event) {

        Map<String,Integer> properties = new HashMap<>();
        if(this.selectedShape instanceof Circle)
        {
            properties.put("X",(int)event.getX());
            properties.put("Y",(int)event.getY());
            properties.put("R",30);
        }
        else
        {
            if(!firstPointSet)
            {
                this.firstPoint = new Point((int)event.getX(),(int)event.getY());
                firstPointSet = true;
                return properties;
            }
            properties.put("X1",this.firstPoint.x);
            properties.put("Y1",this.firstPoint.y);
            properties.put("X2",(int)event.getX());
            properties.put("Y2", (int) event.getY());
        }
        this.firstPointSet = false;
        return properties;
    }

    private Animation hideSwapPane() {

        Animation collapsePanel = new Transition() {
            {
                setCycleDuration(Duration.millis(300));
            }

            @Override
            protected void interpolate(double fraction) {
                splitPane.setDividerPosition(0,0.8 + fraction/5.0);
            }
        };

        collapsePanel.setOnFinished(e->{
            this.isExpanded = false;
            Node divider = this.splitPane.lookup(".split-pane-divider");
            if(divider != null)
                divider.setStyle("-fx-background-image: url(/Views/Images/back.png)");
        });

        return collapsePanel;
    }

    private Animation showSwapPane() {

         Animation expandPanel = new Transition() {
            {
                setCycleDuration(Duration.millis(300));
            }

            @Override
            protected void interpolate(double fraction) {
                splitPane.setDividerPosition(0,1 - fraction/5.0);
            }
        };

        expandPanel.setOnFinished(e->{
            this.isExpanded = true;
            Node divider = this.splitPane.lookup(".split-pane-divider");
            if(divider != null)
                divider.setStyle("-fx-background-image: url(/Views/Images/next.png)");
        });
        return expandPanel;
    }
}
