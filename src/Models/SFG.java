package Models;

import Models.Logic.Graph;

import java.util.ArrayList;

public class SFG {


    private ArrayList<IShape> drawnNodes;
    private ArrayList<IShape> drawnEdges;
    private Graph graph;

    public SFG(ArrayList<IShape> drawnNodes, ArrayList<IShape> drawnEdges)
    {
        this.drawnEdges = drawnEdges;
        this.drawnNodes = drawnNodes;
    }

    public void assembleGraph()
    {
        this.graph = new Graph();

        for(IShape node : drawnNodes)
            graph.addVertex(node.getLabel().getText());

        for(IShape edge : drawnEdges)
        {
            ArrayList<IShape> connectedNodes = edge.getConnectedShapes();
            String node0 = connectedNodes.get(0).getLabel().getText();
            String node1 = connectedNodes.get(1).getLabel().getText();
            String gain = (String)(edge.getPropertiesMap().get("gain"));
            graph.addEdge(node0, node1, gain);
        }

    }

}
