package Models.Logic;

import com.sun.org.apache.xpath.internal.SourceTree;

import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Math.pow;

public class Graph {

    ArrayList<Vertex> vertices = new ArrayList<>();
    HashMap<String,Integer> verticesNames = new HashMap<>();

    public boolean addVertex(String name) {
        if(verticesNames.containsKey(name)) {
            return false;
        }
        Vertex v = new Vertex();
        v.name = name;
        v.order = vertices.size();
        verticesNames.put(name,v.order);
        vertices.add(v);
        return true;
    }


    public Vertex getVertex(String name){
        if(verticesNames.containsKey(name)) {
            return vertices.get(verticesNames.get(name));
        }
        return null;
    }

    public boolean addEdge(String source,String destination,String weight) {
        Vertex s = getVertex(source);
        Vertex d = getVertex(destination);
        if(s == null || d == null) {
            return false;
        }
        return addEdge(s,d,weight);
    }

    ArrayList<Vertex> current ;
    ArrayList<Path> paths ;
    ArrayList<Loop> loops ;
    ArrayList<String> gains ;
    HashMap<String,Integer> visited;
    HashMap<String,Integer> found;

    public ArrayList<Loop> getLoops() {
        current = new ArrayList<>();
        loops = new ArrayList<>();
        visited = new HashMap<>();
        gains = new ArrayList<>();
        found = new HashMap<>();
        findLoops(vertices.get(0));
        for(int i = 0 ; i < loops.size() ; i++) {
            loops.get(i).loopID = i;
        }
        return loops;
    }
    private void findLoops(Vertex v){

        if(visited.containsKey(v.name)) {
            int index = current.indexOf(v);
            ArrayList<Vertex> tmp = new ArrayList<>();
            String tmpGain = "";
            Loop l = new Loop();
            for (int i = index; i < current.size();i++) {
                tmp.add(current.get(i));
                l.verticesName.put(current.get(i).name,0);
            }
            for (int i = index; i < gains.size() - 1;i++) {
                tmpGain +=  gains.get(i) +" * " ;
            }
            tmpGain +=  gains.get(current.size() - 1);
            l.gain = tmpGain;
            l.vertices = tmp;
            boolean loopClear = false;
            boolean allClear = true;
            for(Loop loop : loops){
                if(loop.vertices.size() == tmp.size()){
                    for(Vertex vertex : tmp) {
                        if(!loop.verticesName.containsKey(vertex.name)){
                            loopClear = true;
                        }
                    }
                    if (!loopClear){
                        allClear = false;
                        break;
                    }
                }
            }
            if(allClear)
                loops.add(l);
            return;
        }
        current.add(v);
        visited.put(v.name,0);
        for(Edge e : v.edges) {
            gains.add(e.weight);
            findLoops(e.destination);
            gains.remove(e.weight);
        }
        visited.remove(v.name);
        current.remove(v);
    }

    public ArrayList<Path> getForwardPaths(String v,String u) {
        current = new ArrayList<>();
        paths = new ArrayList<>();
        gains = new ArrayList<>();
        visited = new HashMap<>();
        getForwardPaths(getVertex(v),getVertex(u));
        return paths;
    }

    private void getForwardPaths(Vertex v , Vertex u ){
        if(v.equals(u)) {
            current.add(v);
            ArrayList<Vertex> tmp = (ArrayList<Vertex>) current.clone();
            String tmpGain = "";


            for (int i = 0; i < gains.size() - 1;i++) {
                tmpGain +=  gains.get(i) +" * " ;
            }
            tmpGain +=  gains.get(gains.size() - 1);

            Path path = new Path();
            path.vertices = tmp;
            path.gain = tmpGain;
            paths.add(path);
            current.remove(v);
        } else if(v.edges.isEmpty()) {
            return;
        }
        current.add(v);
        visited.put(v.name,0);
        for(Edge e : v.edges) {
            if(!visited.containsKey(e.destination.name)) {
                gains.add(e.weight);
                getForwardPaths(e.destination, u);
                gains.remove(e.weight);
            }
        }
        visited.remove(v.name);
        current.remove(v);
    }

    public ArrayList<ArrayList<Loop>>[] getFormulaLoops(ArrayList<Loop> loops) {
        ArrayList<ArrayList<Loop>>[] result = getAllLoopsCombinations(loops);
        if (result.length == 0)
            return result;
        int finalIndex = result.length - 1;

        for(int i = 0 ; i < result.length ; i++) {
            boolean touching = false;
            boolean added = false;
            for (int j = 0; j < result[i].size() ; j++) {
                for (int k = 0; k < result[i].get(j).size() ; k++) {
                    for (int u = k+1; u < result[i].get(j).size() ; u++) {
                        for(Vertex v : result[i].get(j).get(k).vertices) {
                            if(result[i].get(j).get(u).verticesName.containsKey(v.name)) {
                                touching = true;
                                break;
                            }
                        }
                        if (touching)
                            break;
                    }
                    if(touching)
                        break;
                }
                if(touching) {
                    result[i].remove(j);
                    j--;
                    touching = false;
                } else {
                    added = true;
                }
            }
            if(!added) {
                finalIndex = i;
                break;
            }

        }

        ArrayList<ArrayList<Loop>>[] finalResult = new ArrayList[finalIndex ];

        for (int i = 0; i < finalResult.length; i++) {
            finalResult[i] = result[i];
        }

        return finalResult;
    }






    public ArrayList<ArrayList<Loop>>[] getAllLoopsCombinations(ArrayList<Loop> loops){
        System.out.println();
        ArrayList<ArrayList<Loop>>[] result = new ArrayList[loops.size() - 1];
        for(int i = 3 ; i < pow(2,loops.size()); i++) {
            int shiftCount = 0;
            int loopIndex = 0;
            ArrayList<Loop> tmp = new ArrayList<>();
            int tmpCount = i;
            while(tmpCount > 0) {
                if( (tmpCount & 1) == 1) {
                    tmp.add(loops.get(loopIndex));
                    shiftCount++;
                }
                loopIndex++;
                tmpCount >>= 1;
            }
            if(shiftCount > 1) {
                if(result[shiftCount - 2] == null) {
                    result[shiftCount - 2] = new ArrayList<>();
                }
                result[shiftCount - 2].add(tmp);
            }
        }
        return result;
    }

    public String evaluateGain(String v,String u) {

        ArrayList<Path> paths = getForwardPaths(v, u);
        String finalValue = "";

        for (Path path : paths) {
            String totalGain = "1 ";
            ArrayList<Loop> loops = (ArrayList<Loop>) getLoops().clone();
            for (Vertex vertex : path.vertices) {
                for (int i = 0 ; i < loops.size();i++) {
                    if (loops.get(i).verticesName.containsKey(vertex.name)) {
                        loops.remove(i);
                        i--;
                    }
                }
            }
            if(loops.size() !=0) {
                ArrayList<ArrayList<Loop>>[] loopCombinations = getFormulaLoops(loops);
                for (int i = 0; i < loops.size(); i++) {
                    totalGain += "- " + "(" + loops.get(i).gain + ") ";
                }
                for (int i = 0; i < loopCombinations.length; i++) {
                    for (int j = 0; j < loopCombinations[i].size(); j++) {
                        if ((i & 1) == 0) {
                            totalGain += "+ (";

                        } else {
                            totalGain += "+ (";
                        }
                        for (int k = 0; k < loopCombinations[i].get(j).size() - 1; k++) {
                            totalGain += loopCombinations[i].get(j).get(k).gain + " x ";
                        }
                        totalGain += loopCombinations[i].get(j).get(loopCombinations[i].get(j).size() - 1).gain + ") ";
                    }
                }
            }
            if(finalValue.length() != 0)
                finalValue += " + ";
                finalValue += "(" + path.gain +")*(" +totalGain+")";
            }
            String totalGain="1 ";
            ArrayList<ArrayList<Loop>>[] loopCombinations = getFormulaLoops(getLoops());
            for (int i = 0; i < loops.size(); i++) {
                totalGain += "- " + "(" + loops.get(i).gain + ") ";
            }
            for (int i = 0; i < loopCombinations.length; i++) {
                for (int j = 0; j < loopCombinations[i].size(); j++) {
                    if ((i & 1) == 0) {
                        totalGain += "+ (";

                    } else {
                        totalGain += "+ (";
                    }
                    for (int k = 0; k < loopCombinations[i].get(j).size() - 1; k++) {
                        totalGain += loopCombinations[i].get(j).get(k).gain + " x ";
                    }
                    totalGain += loopCombinations[i].get(j).get(loopCombinations[i].get(j).size() - 1).gain + " ) ";
                }
            }
            finalValue = "( " +finalValue +" )/(" + totalGain +")";


        return finalValue;
    }




    public boolean addEdge(Vertex source, Vertex destination, String weight) {
        for(int i = 0 ; i < source.edges.size() ; i++){
            if(source.edges.get(i).destination == destination) {
                source.edges.get(i).weight += " " + " "+weight;
                return true;
            }
        }
        Edge e = new Edge();
        e.weight = weight;
        e.source = source;
        e.destination = destination;
        source.edges.add(e);
        return true;
    }
}
