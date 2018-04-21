package Controllers;

import Models.Drawer;
import Models.Logic.Graph;
import Models.SFG;
import Models.ShapeTracker;
import Models.Shapes.Circle;
import Models.IShape;
import Models.Shapes.Edge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;


public class Controller{

    @FXML
    private SplitPane splitPane;
    @FXML
    private JFXListView<JFXButton> listView;
    @FXML
    private Pane pane;

    private Drawer drawer;
    private ShapeTracker tracker;
    private ArrayList<IShape> drawnNodes;
    private ArrayList<IShape> drawnEdges;

    private IShape selectedIShape;
    private String mode;

    private JFXButton select;
    private JFXButton addNode;
    private JFXButton deleteNode;
    private JFXButton addEdge;
    private JFXButton removeEdge;
    private JFXButton calculate;

    private boolean isExpanded;
    private Stage window;

    public Controller(){
        this.select = new JFXButton("Select");
        this.addNode = new JFXButton("Add Node");
        this.addEdge = new JFXButton("Add Edge");
        this.deleteNode = new JFXButton("Delete Node");
        this.removeEdge = new JFXButton("Remove Edge");
        this.calculate = new JFXButton("Calculate");

        this.drawnEdges = new ArrayList<>();
        this.drawnNodes = new ArrayList<>();
        this.tracker = new ShapeTracker(this.drawnNodes,this.drawnEdges);
        this.drawer = new Drawer(this.tracker);
    }


    @FXML
    public void initialize() {
        initButtons();
        initListView();
    }

    private void initButtons() {

        this.select.setOnAction(e->{
            this.selectedIShape = null;
            this.mode = "select";
        });

        this.addNode.setOnAction(e->{
            this.selectedIShape = new Circle();
            this.mode = "add";
        });

        this.addEdge.setOnAction(e->{
            this.selectedIShape = new Edge();
            this.mode = "add";
        });

        this.deleteNode.setOnAction(e->{
            this.selectedIShape = new Circle();
            this.mode = "remove";
        });

        this.removeEdge.setOnAction(e->{
            this.selectedIShape = new Edge();
            this.mode = "remove";
        });

        this.calculate.setOnAction(e->calculate());

    }

    private void initListView() {
        this.listView.getItems().addAll(select,addNode,addEdge,deleteNode,removeEdge,calculate);
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
            this.drawer.drag(this.pane,event,new Edge());
        else
            this.drawer.removeEdge(pane,event);
    }

    @FXML
    public void drawNodes(MouseEvent event){

        if(this.selectedIShape == null || this.selectedIShape instanceof Edge)
            return;

        if(this.mode.equals("add"))
            this.drawer.drawNode(this.pane,new Circle());
        else
            this.drawer.removeNode(pane,event);
    }

    @FXML
    public void select(MouseEvent event){
        if(this.selectedIShape != null && !this.mode.equals("select"))
            return;

        for(IShape edge : drawnEdges) {

            if(((Edge)edge).contains(new Point2D(event.getX(),event.getY()))){
                Label label = edge.getLabel();
                TextField text = new TextField(label.getText());
                text.setLayoutX(label.getLayoutX());
                text.setLayoutY(label.getLayoutY());
                text.setPrefHeight(label.getHeight());
                text.setPrefWidth(label.getWidth());
                text.setEditable(true);
                pane.getChildren().remove(label);
                text.setOnAction(e->{
                    label.setText(text.getText());
                    edge.getPropertiesMap().put("gain",label.getText());
                    pane.getChildren().remove(text);
                    pane.getChildren().add(label);
                });

                pane.getChildren().add(text);
                return;
            }
        }
        throw new RuntimeException("Please select a Label");

    }
    public void calculate()
    {
        SFG sfg = new SFG(this.drawnNodes,this.drawnEdges);
        sfg.assembleGraph();

        /*
        * assemble graph
        * pass instance
        * get result
        * display it
        */
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
