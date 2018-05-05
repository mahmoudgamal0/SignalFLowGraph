package Models;

import Models.Logic.Graph;
import Models.Logic.Loop;
import Models.Logic.Path;
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

    public ArrayList<String> getForwardPaths()
    {
        String source = this.drawnNodes.get(0).getLabel().getText();
        String sink = this.drawnNodes.get(this.drawnNodes.size()-1).getLabel().getText();
        ArrayList<Path> paths = this.graph.getForwardPaths(source,sink);

        int numOfPaths = paths.size();

        ArrayList<String> result = new ArrayList<>();

        result.add("# of forward paths = " + Integer.toString(numOfPaths) + "\n");

        for(int i = 0 ; i < paths.size() ; i++)
        {
            String path = "Path #" + Integer.toString(i+1) + "\n" +
                        "\t Gain= " + paths.get(i).gain + "\n";
            path += "\t Vertices= ";
            for (int j = 0 ; j < paths.get(i).vertices.size(); j++)
                path += paths.get(i).vertices.get(j).name + " ";
            result.add(path + "\n\n");
        }

        result.add("-------------------------------");
        return result;
    }

    public ArrayList<String> getLoops(){
        return getLoops(this.graph.getLoops());
    }

    private ArrayList<String> getLoops(ArrayList<Loop> loops)
    {
        int numOfLoops = loops.size();

        ArrayList<String> result = new ArrayList<>();

        result.add("# of loops = " + Integer.toString(numOfLoops) + "\n");

        for(int i = 0 ; i < loops.size() ; i++)
        {
            String loop = "Loop #" + Integer.toString(i+1)  + "\n" +
                    "\t Gain= " + loops.get(i).gain + "\n";
            loop += "\t Vertices: ";
            for (int j = 0 ; j < loops.get(i).vertices.size(); j++)
                loop += loops.get(i).vertices.get(j).name + " ";
            result.add(loop + "\n\n");
        }

        result.add("-------------------------------\n");
        return result;

    }

    public ArrayList<String> getAllLoopsCombinations(){
        ArrayList<ArrayList<Loop>>[] loops = this.graph.getAllLoopsCombinations(this.graph.getLoops());

        ArrayList<String> result = new ArrayList<>();

        for(int i = 0 ; i < loops.length ; i++)
        {
            result.add("Loop Combination #" + Integer.toString(i) + "\n");
         //   result.addAll(getLoops(loops[i]));
        }
        return result;
    }


}
