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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
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
    @FXML
    private StackPane stackPane;

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
        this.gain = new JFXButton();
        this.addNode = new JFXButton();
        this.addEdge = new JFXButton();
        this.deleteNode = new JFXButton();
        this.removeEdge = new JFXButton();
        this.calculate = new JFXButton();
        this.selectorUp = new JFXButton();
        this.selectorDown = new JFXButton();
        this.select = new JFXButton();

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
            if(removeSelectors()) {
                this.currentEdge = 0;
                this.selectedIShape = null;
                initSelectors("label");
            }
        });

        this.gain.setGraphic(new ImageView(new Image("/Views/Images/gain.png")));

        this.addNode.setOnAction(e->{
            if(removeSelectors()){
                this.selectedIShape = new Circle();
                this.mode = "add";
                drawNode();
            }
        });

        this.addNode.setGraphic(new ImageView(new Image("/Views/Images/plus.png")));

        this.addEdge.setOnAction(e->{
            if(removeSelectors()) {
                this.selectedIShape = new Edge();
                this.mode = "add";
            }
        });

        this.addEdge.setGraphic(new ImageView(new Image("/Views/Images/plus-edge.png")));

        this.deleteNode.setOnAction(e->{
            if(removeSelectors()){
                this.selectedIShape = new Circle();
                this.mode = "remove";
            }
        });

        this.deleteNode.setGraphic(new ImageView(new Image("/Views/Images/minus.png")));

        this.removeEdge.setOnAction(e->{
            if(removeSelectors()) {
                this.selectedIShape = new Edge();
                this.mode = "remove";
                this.currentEdge = 0;
                initSelectors("edge");
            }
        });

        this.removeEdge.setGraphic(new ImageView(new Image("/Views/Images/minus-edge.png")));


        this.calculate.setOnAction(e->{
            if(removeSelectors()) {
                calculate();
            }
        });

        this.calculate.setGraphic(new ImageView(new Image("/Views/Images/calculate.png")));

        this.selectorUp.setGraphic(new ImageView(new Image("/Views/Images/up.png")));

        this.selectorDown.setGraphic(new ImageView(new Image("/Views/Images/down.png")));

        this.select.setGraphic(new ImageView(new Image("/Views/Images/select.png")));
    }

    private void initListView() {
        this.listView.getItems().addAll(gain,addNode,addEdge,deleteNode,removeEdge,calculate);
        this.listView.setVerticalGap(5.0);
    }

    public void init()
    {
        this.window = (Stage)this.splitPane.getScene().getWindow();
        Node divider = this.splitPane.lookup(".split-pane-divider");
        divider.setOnMouseClicked(e->{
            if(!isExpanded)
                showSwapPane().play();
            else
                hideSwapPane().play();
        });

        this.window.widthProperty().addListener(observable ->{
            this.stackPane.setPrefWidth(this.window.getWidth());
            this.splitPane.setPrefWidth(this.window.getWidth() - 16);
            this.pane.setPrefWidth(this.window.getWidth());
            if(isExpanded)
                hideSwapPane().play();
            this.splitPane.setDividerPosition(0,1);
        });

        this.window.heightProperty().addListener((observable -> {
            this.stackPane.setPrefHeight(this.window.getHeight());
            this.splitPane.setPrefHeight(this.window.getHeight() - 40);
            this.pane.setPrefHeight(this.window.getHeight());
            this.splitPane.setDividerPosition(0,1);
            if(isExpanded)
                hideSwapPane().play();
        }));
    }

    private boolean removeSelectors()
    {
        try{
            checkOpenLabels();
        } catch (RuntimeException e) {
            AlertBox.alert("Label Warning", e.getMessage());
            return false;
        }

        if(this.listView.getItems().contains(this.select))
            this.listView.getItems().removeAll(this.selectorUp,this.select,this.selectorDown);
        deselect();
        return true;
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
                if(this.drawnEdges.isEmpty())
                    throw new RuntimeException("No edges exist");
                if(item.equals("edge"))
                    this.drawer.removeEdge(this.pane,this.currentEdge);
                else
                    showText((Edge) this.drawnEdges.get(this.currentEdge));
            } catch (RuntimeException ex) {
                AlertBox.alert("Selection Warning",ex.getMessage());
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
        if(this.drawnEdges.isEmpty() || this.drawnNodes.isEmpty())
        {
            AlertBox.alert("Calculation Error", "There are no sufficient nodes and edges");
            return;
        }
        SFG sfg = new SFG(this.drawnNodes,this.drawnEdges);
        sfg.assembleGraph();

        String gain = sfg.getGain();
        ArrayList<String> loops = sfg.getLoops();
        ArrayList<String> paths = sfg.getForwardPaths();
        ArrayList<String> deltas = sfg.getDeltas();

        FormulateTextData ftd = new FormulateTextData(paths,loops,deltas,gain);

        DialogBox.dialog(this.stackPane,new Text("Gain Calculations"),ftd.getData());

    }

    private Animation hideSwapPane() {

        Animation collapsePanel = new Transition() {
            {
                setCycleDuration(Duration.millis(300));
            }

            @Override
            protected void interpolate(double fraction) {
                splitPane.setDividerPosition(0,1 - (60/window.getWidth()) + fraction/(window.getWidth()/60));
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
                splitPane.setDividerPosition(0,1 - fraction/(window.getWidth()/60));
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
