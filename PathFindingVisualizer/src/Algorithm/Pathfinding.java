package Algorithm;

import Algorithm.Algorithm;
import org.javatuples.Pair;
import java.util.*;
import java.util.Comparator;

public class Pathfinding {
    private ArrayList<ArrayList<Node>> nodes;
    private boolean isRunning;
    private boolean isPath;
    private LinkedList<Node> path;
    private Queue<Pair<Node, NodeState>> searchQueue;


    public Pathfinding(ArrayList<ArrayList<Node>> grid){
        nodes = grid;
        isRunning = false;
        isPath = false;
        path = new LinkedList<>();
        searchQueue = new LinkedList<>();
    }

    public void start(Algorithm alg, Node startNode){
        switch(alg){
            case DFS:
                DFS(startNode);
                break;
            case BFS:
                BFS(startNode);
                break;
            case DIJKSTRA:
                dijkstra(startNode);
                break;
            default:
                System.out.println("not implemented yet! :( ");
        }
    }

    private void BFS(Node startNode) {
        Queue<Node> Q = new LinkedList<>();
        Q.add(startNode);
        startNode.visit();
        isRunning = true;

        while(Q.size() > 0 && isRunning){
            Node current = Q.poll();

            for(Node n: current.getNeighbours(nodes)){
                if(n.isEnd()){
                    n.setParent(current);

                    constructPath(n);
                    isRunning = false;
                    isPath = true;
                    break;
                }
                else if( !n.isVisited()){
                    n.visit();
                    n.setParent(current);
                    insertSearchQueue(n, NodeState.OPEN);

                    Q.add(n);
                }
            }
            if(!current.isStart())
                insertSearchQueue(current, NodeState.CLOSED);

        }
        isRunning = false;

    }

    private void DFS(Node startNode) {
        Stack<Node> stack = new Stack<>();
        stack.push(startNode);
        isRunning = true;

        while(!stack.isEmpty() && isRunning){
            Node current = stack.pop();
            current.visit();
            for(Node n: current.getNeighbours(nodes)){
                if(n.isEnd()){
                    n.setParent(current);
                    constructPath(n);
                    isRunning = false;
                    isPath = true;
                    break;
                }
                else if(!n.isVisited()){
                    n.setParent(current);
                    insertSearchQueue(n, NodeState.OPEN);
                    stack.push(n);
                }
            }
            if(!current.isStart())
                insertSearchQueue(current, NodeState.CLOSED);
        }
        isRunning = false;

    }

    private void dijkstra(Node startNode){
        PriorityQueue<Node> Q;
        Q = new PriorityQueue<Node>(new Comparator<Node>(){

            @Override
            public int compare(Node o1, Node o2) {
                return o1.getDist().compareTo(o2.getDist());
            }
        });
        startNode.setDist(0);
        Q.add(startNode);
        isRunning = true;
        while(!Q.isEmpty() && isRunning){
            Node current = Q.poll();
            if(current.isEnd()){
                constructPath(current);
                isRunning = false;
                isPath = true;
                break;

            }
            else if(!current.isVisited()){
                current.visit();


                for(Node n: current.getNeighbours(nodes)){
                    if(!n.isVisited() && n.getDist() > current.distTo(n) + current.getDist()){
                        n.setDist(current.distTo(n) + current.getDist());
                        n.setParent(current);
                        Q.add(n);
                        insertSearchQueue(n, NodeState.OPEN);
                    }
                }
                if(!current.isStart())
                    insertSearchQueue(current, NodeState.CLOSED);
            }
        }
        isRunning = false;


    }

    private void constructPath(Node current){
        path = new LinkedList<>();
        current = current.getParent();
        path.addFirst(current);

        while(current.getParent() != null ) {
            path.addFirst(current);
            current = current.getParent();
        }

        this.path = path;
    }

    public LinkedList<Node> getPath(){
        return path;
    }

    private void insertSearchQueue(Node node, NodeState state){
        Pair<Node, NodeState> element = new Pair<>(node, state);
        searchQueue.add(element);
    }

    public Queue<Pair<Node, NodeState>> getSearchQueue(){
        return searchQueue;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public void abort(){
        isRunning = false;
    }

    public void clear(){
        isRunning = false;
        isPath = false;
        path = new LinkedList<>();
        searchQueue = new LinkedList<>();
    }


}
