package Algorithm;

import java.awt.*;
import java.util.ArrayList;
import static java.lang.Math.max;


public class Node {

    private NodeState state;
    private Node parent;
    private int row, col;
    private boolean visited; // attribute used only by pathfinding algorithms ;
                            // not to be confused with state(used by visualizer)
    private int dist;
    private int weight;


    public Node(int row, int col){
        this.state = NodeState.FREE;
        this.parent = null;
        this.row = row;
        this.col = col;
//        this.width = width;
        this.visited = false;
        this.dist = Integer.MAX_VALUE ;
        this.weight = 1;
    }


    public int distTo(Node v){
        return max(this.weight, v.getWeight());
    }

    public int getWeight(){
        return this.weight;
    }

    public void setWeight(int w){
        this.weight = w;
    }

    public void setDist(int d){
        this.dist = d;
    }

    public Integer getDist(){
        return this.dist;
    }
    public void visit(){
        visited = true;
    }

    public boolean isVisited(){
        return visited;
    }

    public void setWall(){
        this.state = NodeState.WALL;
    }
    public void setFree(){
        this.state = NodeState.FREE;
    }
    public void setStart(){
        this.state = NodeState.START;
    }
    public void setEnd(){
        this.state = NodeState.END;
    }
    public void setOpen(){
        this.state = NodeState.OPEN;
    }
    public void setClosed(){
        this.state = NodeState.CLOSED;
    }
    public void setPath(){
        this.state = NodeState.PATH;
    }
    public boolean isOpen(){
        return this.state == NodeState.OPEN;
    }
    public boolean isClosed(){
        return this.state == NodeState.CLOSED;
    }
    public boolean isWALL(){
        return this.state == NodeState.WALL;
    }
    public boolean isStart(){
        return this.state == NodeState.START;
    }
    public boolean isEnd(){
        return this.state == NodeState.END;
    }

    public NodeState getState(){
        return this.state;
    }

    public Color getColor(){
        return this.state.getColor();
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }

    public void clear() {
        state = NodeState.FREE;
        parent = null;
        visited = false;
        dist = Integer.MAX_VALUE;
        weight = 1 ;
    }

    public void setParent(Node node){
        parent = node;
    }

    public Node getParent(){
        return parent;
    }

    public ArrayList<Node> getNeighbours(ArrayList<ArrayList<Node>> nodes){
        ArrayList<Node> neighbours = new ArrayList<Node>();

        if (row + 1 < nodes.size() && nodes.get(row + 1).get(col).getState() != NodeState.WALL)
            neighbours.add(nodes.get(row + 1).get(col));
        if (col + 1 < nodes.get(0).size() && nodes.get(row).get(col + 1).getState() != NodeState.WALL)
            neighbours.add(nodes.get(row).get(col + 1));
        if (row - 1 >= 0 && nodes.get(row - 1).get(col).getState() != NodeState.WALL)
            neighbours.add(nodes.get(row - 1).get(col));
        if (col - 1 >= 0 && nodes.get(row).get(col - 1).getState() != NodeState.WALL)
            neighbours.add(nodes.get(row).get(col - 1));

        return neighbours;
    }




}
