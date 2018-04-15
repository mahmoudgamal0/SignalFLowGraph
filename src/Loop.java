import java.util.ArrayList;

public class Loop {

    private int gain;
    private ArrayList<Integer> indices;

    public Loop() {this.indices = new ArrayList<>();}

    public int getGain() {return gain;}

    public ArrayList<Integer> getIndices() {return indices;}

    public void addIndex(int index){this.indices.add(index);}
}
