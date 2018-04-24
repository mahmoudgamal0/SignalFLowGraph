package Controllers;

import Models.*;
import Models.Logic.Graph;
import Models.Shapes.Circle;
import Models.Shapes.Edge;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.animation.Animation;
import javafx.animation.Transition;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;


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

    private JFXButton gain;
    private JFXButton addNode;
    private JFXButton deleteNode;
    private JFXButton addEdge;
    private JFXButton removeEdge;
    private JFXButton calculate;
    private JFXButton selectorUp;
    private JFXButton selectorDown;
    private JFXButton select;

    private int currentEdge;
    private boolean isExpanded;
    private Stage window;

    public Controller(){
        this.gain = new JFXButton("Gain");
        this.addNode = new JFXButton("Add Node");
        this.addEdge = new JFXButton("Add Edge");
        this.deleteNode = new JFXButton("Delete Node");
        this.removeEdge = new JFXButton("Remove Edge");
        this.calculate = new JFXButton("Calculate");
        this.selectorUp = new JFXButton("Up");
        this.selectorDown = new JFXButton("Down");
        this.select = new JFXButton("Select");

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

        this.gain.setOnAction(e->{
            removeSelectors();
            this.currentEdge = 0;
            this.selectedIShape = null;
            initSelectors("label");
        });

        this.addNode.setOnAction(e->{
            removeSelectors();
            this.selectedIShape = new Circle();
            this.mode = "add";
            drawNode();
        });

        this.addEdge.setOnAction(e->{
            removeSelectors();
            this.selectedIShape = new Edge();
            this.mode = "add";
        });

        this.deleteNode.setOnAction(e->{
            removeSelectors();
            this.selectedIShape = new Circle();
            this.mode = "remove";
        });

        this.removeEdge.setOnAction(e->{
            removeSelectors();
            this.selectedIShape = new Edge();
            this.mode = "remove";
            this.currentEdge = 0;
            initSelectors("edge");
        });

        this.calculate.setOnAction(e->{
            removeSelectors();
            calculate();
        });

    }

    private void initListView() {
        this.listView.getItems().addAll(gain,addNode,addEdge,deleteNode,removeEdge,calculate);
        this.listView.expandedProperty().set(true);
        this.listView.setVerticalGap(5.0);

        this.listView.setOnMouseEntered(e->this.listView.setExpanded(true));

        this.listView.setOnMouseExited(e->this.listView.setExpanded(false));
    }

    private void removeSelectors()
    {
        if(this.listView.getItems().contains(this.select))
            this.listView.getItems().removeAll(this.selectorUp,this.select,this.selectorDown);
        deselect();
    }

    private void initSelectors(String item) {

        this.listView.getItems().addAll(this.selectorUp,this.select,this.selectorDown);

        this.selectorUp.setOnAction(e->{
            try{
                checkOpenLabels();
                this.currentEdge++;
                try{
                    nextEdge();
                } catch (RuntimeException ex) {
                    AlertBox.alert("Edge Warning",ex.getMessage());
                }
            } catch (RuntimeException ex) {
                AlertBox.alert("Label Warning",ex.getMessage());
            }

        });

        this.select.setOnAction(e->{
            try{
                checkOpenLabels();
                if(item.equals("edge"))
                    this.drawer.removeEdge(this.pane,this.currentEdge);
                else
                    showText((Edge) this.drawnEdges.get(this.currentEdge));
            } catch (RuntimeException ex) {
                AlertBox.alert("Label Warning",ex.getMessage());
            }
        });

        this.selectorDown.setOnAction(e->{
            try{
                checkOpenLabels();
                this.currentEdge--;
                try{
                    prevEdge();
                } catch (RuntimeException ex) {
                    AlertBox.alert("Edge Warning",ex.getMessage());
                }
            } catch (RuntimeException ex) {
                AlertBox.alert("Label Warning",ex.getMessage());
            }
        });
    }

    private void checkOpenLabels() {
        Set textFields = this.pane.lookupAll(".text-field");
        if(textFields.size() == 1)
            throw new RuntimeException("Please close all labels first");
    }

    private void prevEdge() {

        if(this.drawnEdges.size() == 0)
            throw new RuntimeException("No edges exist");

        if(this.currentEdge == -1)
            this.currentEdge = this.drawnEdges.size()-1;
        Edge edge = (Edge) this.drawnEdges.get(this.currentEdge);

        int nextIndex = (this.currentEdge + 1) % this.drawnEdges.size();
        Edge nextEdge = (Edge) this.drawnEdges.get(nextIndex);

        nextEdge.setStroke(Paint.valueOf("CYAN"));
        edge.setStroke(Paint.valueOf("GREEN"));
    }

    private void nextEdge() {

        if(this.drawnEdges.size() == 0)
            throw new RuntimeException("No edges exist");

        if(this.currentEdge == this.drawnEdges.size())
            this.currentEdge = 0;

        Edge edge = (Edge) this.drawnEdges.get(this.currentEdge);

        int prevIndex;
        if(this.currentEdge == 0)
            prevIndex = this.drawnEdges.size()-1;
        else
            prevIndex = this.currentEdge - 1;
        Edge prevEdge = (Edge) this.drawnEdges.get(prevIndex);

        prevEdge.setStroke(Paint.valueOf("CYAN"));
        edge.setStroke(Paint.valueOf("GREEN"));
    }

    private void deselect()
    {
        for (IShape edge: this.drawnEdges) {
            ((Edge)edge).setStroke(Paint.valueOf("CYAN"));
        }
    }

    private void showText(Edge edge){
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
        {
            try{
                this.drawer.drag(this.pane,event,new Edge());
            } catch (RuntimeException e){
                AlertBox.alert("Edge Warning",e.getMessage());
            }
        }
    }

    @FXML
    public void removeNodes(MouseEvent event){

        if(this.selectedIShape == null || this.selectedIShape instanceof Edge)
            return;

        if(this.mode.equals("remove"))
        {
            try{
                this.drawer.removeNode(pane,event);
            } catch (RuntimeException e) {
                AlertBox.alert("Node warning",e.getMessage());
            }
        }
    }

    private void drawNode()
    {
        this.drawer.drawNode(this.pane,new Circle());
    }

    public void calculate()
    {
        SFG sfg = new SFG(this.drawnNodes,this.drawnEdges);
        sfg.assembleGraph();

        ArrayList<String> loops = sfg.getLoops();
        ArrayList<String> paths = sfg.getForwardPaths();

        String result = "";
        for(int i = 0 ; i < paths.size() ; i++)
            result += paths.get(i);

        AlertBox.alert("gain",result);

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
