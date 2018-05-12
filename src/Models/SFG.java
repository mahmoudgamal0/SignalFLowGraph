package Models;

import Models.Logic.Gain;
import Models.Logic.Graph;
import Models.Logic.Loop;
import Models.Logic.Path;
import java.util.ArrayList;
import java.util.concurrent.atomic.DoubleAccumulator;

public class SFG {


    private ArrayList<IShape> drawnNodes;
    private ArrayList<IShape> drawnEdges;
    private ArrayList<Path> paths;
    private ArrayList<Loop> loops;
    private ArrayList<ArrayList<Loop>>[] loopsCombinations;
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
        int numOfPaths = paths.size();

        ArrayList<String> result = new ArrayList<>();

        result.add("# of forward paths = " + Integer.toString(numOfPaths) + "\n");

        for(int i = 0 ; i < paths.size() ; i++)
        {
            String path = "Path #" + Integer.toString(i+1) + "\n" +
                        "\t Gain= " + Double.toString(paths.get(i).gainValue) + "\n";
            path += "\t Vertices= ";
            for (int j = 0 ; j < paths.get(i).vertices.size(); j++)
                path += paths.get(i).vertices.get(j).name + " ";
            result.add(path + "\n\n");
        }

        result.add("-------------------------------\n");
        return result;
    }

    public ArrayList<String> getLoops()
    {
        ArrayList<String> result = new ArrayList<>();
        result.add("Individual Loops: \n");
        result.addAll(getLoops(this.loops));
        result.addAll(getLoopsCombinations());
        return result;
    }

    private ArrayList<String> getLoops(ArrayList<Loop> loops)
    {

        int numOfLoops = loops.size();

        ArrayList<String> result = new ArrayList<>();

        result.add("# of loops = " + Integer.toString(numOfLoops) + "\n");

        for(int i = 0 ; i < loops.size() ; i++)
        {
            String loop = "Loop #" + Integer.toString(i+1)  + "\n" +
                    "\t Gain= " + Double.toString(loops.get(i).gainValue) + "\n";
            loop += "\t Vertices: ";
            for (int j = 0 ; j < loops.get(i).vertices.size(); j++)
                loop += loops.get(i).vertices.get(j).name + " ";
            result.add(loop + "\n\n");
        }

        result.add("-------------------------------\n");
        return result;

    }

    private ArrayList<String> getLoopsCombinations()
    {
        int numOfCombinations = loopsCombinations.length;
        ArrayList<String> result = new ArrayList<>();
        for(int i = 0 ; i < numOfCombinations ; i++)
        {
            ArrayList<ArrayList<Loop>> ithCombination = loopsCombinations[i];

            result.add("Combination #" + Integer.toString(i + 2) + " :\n");
            for(int j = 0 ; j < ithCombination.size() ; j++)
            {
                result.add("Set #" + Integer.toString(j+1) + " :\n");
                ArrayList<String> ithCombinationJthLoop = getLoops(ithCombination.get(j));
                result.addAll(ithCombinationJthLoop);
            }
        }
        return result;
    }

    public ArrayList<String> getDeltas()
    {

        int numOfPaths = paths.size();

        ArrayList<String> result = new ArrayList<>();

        result.add("# of Deltas = " + Integer.toString(numOfPaths) + "\n");

        for(int i = 0 ; i < paths.size() ; i++)
        {
            String delta = "Delta #" + Integer.toString(i+1) + "\n" +
                    "\t Gain= " + paths.get(i).delta + "\n" +
                    "\t     = " + Double.toString(paths.get(i).deltaValue);
            result.add(delta + "\n\n");
        }

        result.add("-------------------------------\n");
        return result;
    }

    public String getGain()
    {
        String source = this.drawnNodes.get(0).getLabel().getText();
        String sink = this.drawnNodes.get(this.drawnNodes.size()-1).getLabel().getText();
        Gain gain = this.graph.evaluateGain(source,sink);
        this.paths = gain.paths;
        this.loopsCombinations = gain.loopCombinations;
        this.loops = this.graph.getLoops();
        return "\t Gain= " + gain.gain+ "\n" + "\t    =" + Double.toString(gain.gainValue);
    }
}
