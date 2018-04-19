package Models.Logic;

import java.util.ArrayList;
import java.util.HashMap;

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
    HashMap<String,Integer> visited;
    HashMap<String,Integer> found;

    public ArrayList<Loop> getLoops() {
        current = new ArrayList<>();
        loops = new ArrayList<>();
        visited = new HashMap<>();
        found = new HashMap<>();
        findLoops(vertices.get(0));
        return loops;
    }
    private void findLoops(Vertex v){

        if(visited.containsKey(v.name)) {
            int index = current.indexOf(v);
            ArrayList<Vertex> tmp = new ArrayList<>();
            Loop l = new Loop();
            for (int i = index; i < current.size();i++) {
                tmp.add(current.get(i));
                l.verticesName.put(current.get(i).name,0);
            }
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
            findLoops(e.destination);
        }
        visited.remove(v.name);
        current.remove(v);
    }

    public ArrayList<Path> getForwardPaths(String v,String u) {
        current = new ArrayList<>();
        paths = new ArrayList<>();
        visited = new HashMap<>();
        getForwardPaths(getVertex(v),getVertex(u));
        return paths;
    }

    private void getForwardPaths(Vertex v , Vertex u ){
        if(v.equals(u)) {
            current.add(v);
            ArrayList<Vertex> tmp = (ArrayList<Vertex>) current.clone();
            Path path = new Path();
            path.vertices = tmp;
            paths.add(path);
            current.remove(v);
        } else if(v.edges.isEmpty()) {
            return;
        }
        current.add(v);
        visited.put(v.name,0);
        for(Edge e : v.edges) {
            if(!visited.containsKey(e.destination.name))
                getForwardPaths(e.destination,u);
        }
        visited.remove(v.name);
        current.remove(v);
    }
//    public String evaluateGain(String v,String u) {
//
//        ArrayList<Path> paths = getForwardPaths(v,u);
//
//        ArrayList<Loop> loops = getLoops();
//
//        ArrayList<Loop> loopsAvailable = new ArrayList<>();
//        for(Path path:paths) {
//            for (Loop loop : loops) {
//                boolean intersects = false;
//                for(String vertexName : path.vertices){
//                    if(loop.verticesName.containsKey(vertexName)){
//                        intersects = true;
//                        break;
//
//
//                    }
//
//                }
//                if(!intersects) {
//                    loopsAvailable.add(loop);
//                }
//            }
//
//
//
//
//        }
//
//
//
//    }
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
