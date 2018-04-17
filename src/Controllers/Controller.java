package Controllers;

import Models.Drawer;
import Models.Shapes.Circle;
import Models.IShape;
import Models.Shapes.Edge;
import Models.Utilities;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.SplitPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.SVGPath;
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
    private Pane pane;

    private Drawer drawer;
    private ArrayList<IShape> drawnCircles;
    private ArrayList<IShape> drawnEdges;

    private IShape selectedIShape;
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

        this.drawnEdges = new ArrayList<>();
        this.drawnCircles = new ArrayList<>();
        this.drawer = new Drawer(this.drawnCircles,this.drawnEdges);
    }


    @FXML
    public void initialize() {
        initButtons();
        initListView();
    }

    private void initButtons() {
        this.addNode.setOnAction(e->{
            this.selectedIShape = new Circle();
            this.drawer.setShape(this.selectedIShape);
            this.mode = "add";
        });

        this.addEdge.setOnAction(e->{
            this.selectedIShape = new Edge();
            this.drawer.setShape(this.selectedIShape);
            this.mode = "add";
        });

        this.deleteNode.setOnAction(e->{
            this.selectedIShape = new Circle();
            this.drawer.setShape(this.selectedIShape);
            this.mode = "remove";
        });

        this.removeEdge.setOnAction(e->{
            this.selectedIShape = new Edge();
            this.drawer.setShape(this.selectedIShape);
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
    public void drawEdges(MouseEvent event)
    {
        if(this.selectedIShape == null || this.selectedIShape instanceof Circle)
            return;

        if(this.mode.equals("add"))
            this.drawer.drag(this.pane,event);
        else
        {
            if(this.drawnEdges.size() == 0)
                throw new RuntimeException("Display an error message of no nodes");
            for(int i = 0 ; i < this.drawnEdges.size() ; i++){
                if(((Edge)this.drawnEdges.get(i)).contains(new Point2D(event.getX(),event.getY())))
                    this.drawer.remove(this.pane,i);
            }
        }
    }

    @FXML
    public void drawNodes(MouseEvent event){

        if(this.selectedIShape == null || this.selectedIShape instanceof Edge)
            return;

        if(this.mode.equals("add"))
            this.drawer.draw(this.pane,event);
        else
        {
            int i = 0;
            try{
                Point point = new Point((int) event.getX(),(int)event.getY());
                i = Utilities.getNearest(this.drawnCircles,point);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            if(i == -1)
                System.out.println("Error message");
            else
            {
                this.drawer.remove(this.pane,i);
            }
        }

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
