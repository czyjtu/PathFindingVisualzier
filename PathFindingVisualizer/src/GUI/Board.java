package GUI;

import Algorithm.Node;
import Algorithm.NodeState;
import org.javatuples.Pair;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Board extends JPanel {
    public static int WIDTH = 1000, HEIGHT = 1000, MIN_SIZE = 10, MAX_SIZE = 80;
    private int nSize;
    private double nodeWidth;
    private boolean isClear;
    private ArrayList<ArrayList<Node>> nodes;
    private HashSet< Pair<Integer, Integer> > startingCoor ;
    private HashSet< Pair<Integer, Integer> > openCoor;
    private HashSet< Pair<Integer, Integer> > closeCoor;
    private HashSet< Pair<Integer, Integer> > wallCoor;
    private HashSet< Pair<Integer, Integer> > pathCoor;

    public Board(int nSize){
        super();

        this.isClear=true;
        this.nSize = nSize;
        this.nodeWidth = (double)(this.WIDTH)/ (double)(this.nSize);

        startingCoor = new HashSet<>();
        openCoor = new HashSet<>();
        closeCoor = new HashSet<>();
        pathCoor = new HashSet<>();
        wallCoor =  new HashSet<>();
        setupNodes();

        this.setSize(WIDTH, HEIGHT);
        this.setVisible(true);
    }
    public boolean isClear(){
        return isClear;
    }

    private void setupNodes(){
        this.nodes = new ArrayList<>();

        for(int row = 0; row < this.nSize; row ++) {
            ArrayList<Node> rowList = new ArrayList<>();

            for (int col = 0; col < this.nSize; col++) {
                rowList.add(new Node(row, col));

            }
            this.nodes.add(rowList);
        }
    }

    private void removeFromPaint(Node node, NodeState state){
        Pair<Integer, Integer> coor = coor = new Pair<>(node.getRow(), node.getCol());
        switch(state) {
            case WALL:
                wallCoor.remove(coor);
                break;
            case START:
            case END:
                startingCoor.remove(coor);
                break;
            case CLOSED:
                closeCoor.remove(coor);
                break;
            case OPEN:
                openCoor.remove(coor);
                break;
            case PATH:
                pathCoor.remove(coor);
                break;
            default:
                break;
        }
    }

    private void addToPaint(Node node, NodeState state){
        Pair<Integer, Integer> coor = new Pair<>(node.getRow(), node.getCol());
        switch(state) {
            case WALL:
                wallCoor.add(coor);
                break;
            case START:
            case END:
                startingCoor.add(coor);
                break;
            case CLOSED:
                closeCoor.add(coor);
                break;
            case OPEN:
                openCoor.add(coor);
                break;
            case PATH:
                pathCoor.add(coor);
                break;
        }
    }

    public void setState(Node node, NodeState state){
        isClear=false;
        removeFromPaint(node, node.getState());
        addToPaint(node, state);
        switch(state){
            case FREE:
                node.setFree();
                break;
            case WALL:
                node.setWall();
                break;
            case START:
                node.setStart();
                break;
            case END:
                node.setEnd();
                break;
            case CLOSED:
                node.setClosed();
                break;
            case OPEN:
                node.setOpen();
                break;
            case PATH:
                node.setPath();
                break;
        }
    }

//  board preparation using mouse /////////////////////
    public Node setStartNode(int x, int y) {
        int col = (int)( x/nodeWidth);
        int row = (int)( y/nodeWidth);

        setState(nodes.get(row).get(col), NodeState.START);
        isClear=false;
        return nodes.get(row).get(col);


    }

    public void setEndNode(int x, int y) {
        int col = (int)( x/nodeWidth);
        int row = (int)( y/nodeWidth);
        isClear=false;
        if(nodes.get(row).get(col).getState() == NodeState.FREE)
            setState(nodes.get(row).get(col), NodeState.END);
    }

    public void setWallNode(int x, int y, boolean mouseDragged) {
        int col = (int)( x/nodeWidth);
        int row = (int)( y/nodeWidth);
        isClear=false;
        if(nodes.get(row).get(col).getState() == NodeState.FREE)
            setState(nodes.get(row).get(col), NodeState.WALL);
        else if(nodes.get(row).get(col).getState() == NodeState.WALL && !mouseDragged)
            setState(nodes.get(row).get(col), NodeState.FREE);
    }
////////////////////////////////////////////////////////
    public void setRandomWalls(int nWalls){
        Random rand = new Random();
        int i = 0;
        while( i < nWalls){
            Node node = nodes.get(rand.nextInt(nSize)).get(rand.nextInt(nSize));
            if( node.getState() == NodeState.FREE){
                setState(node, NodeState.WALL);
                i ++;
            }

        }
        repaint();
    }

    public void clear() {
        for (ArrayList<Node> nn: nodes) {
            for (Node n: nn) {
                n.clear();
            }
        }
        isClear = true;
        startingCoor = new HashSet<>();
        openCoor = new HashSet<>();
        closeCoor = new HashSet<>();
        pathCoor = new HashSet<>();
        wallCoor =  new HashSet<>();

        repaint();
    }

    public ArrayList<ArrayList<Node>> getNodes(){
        return nodes;
    }

    public void resize(int n){
        if(n < MIN_SIZE || n > MAX_SIZE || n == nSize)
            return;

        int oldSize = this.nSize;
        this.nSize = n;
        this.nodeWidth = (double)(WIDTH)/this.nSize;

        if(oldSize > nSize){
            int i = oldSize -1;
            while(i >= nSize){
                nodes.remove(i);
                i--;
            }
            for(ArrayList<Node> l: nodes){
                int j = oldSize -1;
                while(j >= nSize){
                    l.remove(j);
                    j--;
                }
            }
        }
        else{
            for(int row = 0; row < oldSize; row++)
                for(int col = oldSize; col < nSize; col ++)
                    nodes.get(row).add(new Node(row, col));

            for(int row = oldSize; row < nSize; row ++) {
                ArrayList<Node> rowList = new ArrayList<>();
                for (int col = 0; col < nSize; col++)
                    rowList.add(new Node(row, col));

                nodes.add(rowList);
            }
        }
        repaint();


    }


    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);


        for(int i=0; i < nSize; i++){


            for(int j=0; j < nSize; j++){
                g.setColor(nodes.get(i).get(j).getColor());
                g.fillRect(
                        (int)((double)(j) * nodeWidth) + 2,
                        (int)(i* nodeWidth) + 2,
                        (int)nodeWidth - 2,
                        (int)nodeWidth - 2
                );

            }
        }
//        for(int i=0; i < nSize; i++) {
//            g.drawLine((int) (i * nodeWidth), 0, (int) (i * nodeWidth), HEIGHT);
//            g.drawLine(0, (int) (i * nodeWidth), WIDTH, (int) (i * nodeWidth));
//        }
//
//        for(Pair<Integer, Integer> coor: startingCoor){
//            int i = coor.getValue0();
//            int j = coor.getValue1();
//            g.setColor(nodes.get(i).get(j).getColor());
//            g.fillRect(
//                    (int)(nodes.get(i).get(j).getCol() * nodeWidth) + 2,
//                    (int)(nodes.get(i).get(j).getRow() * nodeWidth) + 2,
//                    (int)nodeWidth - 3,
//                    (int)nodeWidth - 3
//            );
//        }
//        for(Pair<Integer, Integer> coor: wallCoor){
//            int i = coor.getValue0();
//            int j = coor.getValue1();
//            g.setColor(nodes.get(i).get(j).getColor());
//            g.fillRect(
//                    (int)(nodes.get(i).get(j).getCol() * nodeWidth),
//                    (int)(nodes.get(i).get(j).getRow() * nodeWidth),
//                    (int)nodeWidth ,
//                    (int)nodeWidth
//            );
//        }
//        for(Pair<Integer, Integer> coor: openCoor){
//            int i = coor.getValue0();
//            int j = coor.getValue1();
//            g.setColor(nodes.get(i).get(j).getColor());
//            g.fillRect(
//                    (int)(nodes.get(i).get(j).getCol() * nodeWidth) + 2,
//                    (int)(nodes.get(i).get(j).getRow() * nodeWidth) + 2,
//                    (int)nodeWidth - 3,
//                    (int)nodeWidth - 3
//            );
//        }
//        for(Pair<Integer, Integer> coor: closeCoor){
//            int i = coor.getValue0();
//            int j = coor.getValue1();
//            g.setColor(nodes.get(i).get(j).getColor());
//            g.fillRect(
//                    (int)(nodes.get(i).get(j).getCol() * nodeWidth) + 2,
//                    (int)(nodes.get(i).get(j).getRow() * nodeWidth) + 2,
//                    (int)nodeWidth - 3,
//                    (int)nodeWidth - 3
//            );
//        }
//        for(Pair<Integer, Integer> coor: pathCoor){
//            int i = coor.getValue0();
//            int j = coor.getValue1();
//            g.setColor(nodes.get(i).get(j).getColor());
//            g.fillRect(
//                    (int)(nodes.get(i).get(j).getCol() * nodeWidth) + 2,
//                    (int)(nodes.get(i).get(j).getRow() * nodeWidth) + 2,
//                    (int)nodeWidth - 2,
//                    (int)nodeWidth - 2
//            );
//        }


    }






}
