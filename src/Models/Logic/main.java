package Models.Logic;

import java.util.ArrayList;

public class main {


    public static void main(String[] args) {

        Graph x = new Graph();

        x.addVertex("1");
        x.addVertex("2");
        x.addVertex("3");
        x.addVertex("4");
        x.addVertex("5");



        x.addEdge("1","2","1");
        x.addEdge("1","3","2");
        x.addEdge("1","4","3");
        x.addEdge("2","3","4");
        x.addEdge("3","4","5");
        x.addEdge("4","5","6");
        x.addEdge("3","2","7");
        x.addEdge("4","1","8");
        x.addEdge("4","4","9");

        System.out.println(x.getLoops());
        System.out.println(x.getAllLoopsCombinations(x.getLoops()));
        System.out.println(x.evaluateGain("1","5"));


//
//        ArrayList<Path> paths = x.getForwardPaths("1","5" );
//
//        for(Path p : paths) {
//
//            for(Vertex v : p.vertices) {
//
//                System.out.print(v.name+" ");
//
//            }
//            System.out.println();
//            System.out.println(p.gain);
//
//        }
//        System.out.println("=============================");


//        ArrayList<Loop> loops = x.getLoops();
////
////        x.getAllLoopsCombinations();
//
//        for(Loop l : loops) {
//
//            for(Vertex v : l.vertices) {
//
//                System.out.print(v.name+" ");
//
//            }
//            System.out.println();
//            System.out.println(l.gain);
//
//        }
    }
}
