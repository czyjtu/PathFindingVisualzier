package GUI;

import Algorithm.*;
import org.javatuples.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.LinkedList;
import java.util.Queue;

public class Frame extends JFrame implements
        ActionListener, MouseListener, MouseMotionListener, MouseWheelListener
{
    JMenuBar menuBar;
    JMenu algorithmsMenu;
    JMenu obstaclesMenu;
    JMenu actionsMenu;
    JMenuItem DFS, BFS, dijkstra;
    JMenuItem random;
    JMenuItem start, pause, clear;
    Board board;
    private int HEIGHT = 1150;
    private int WIDTH = 1018;
    private Algorithm currentAlgo = null;
    private Pathfinding pathfinding = null;
    private int nWidth = 30;
    private Node startNode = null;
    private boolean isStartPointed = false;
    private boolean isEndPointed = false;
    private boolean isPause = false;
    private boolean isVisualizationReady = false;

    Timer startVisualizationTimer;
    Timer stepTimer;



    public Frame(){
        setupMenuBar();
        setupBoard();
        setupLegend();
        setUpPathfinding();
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setVisible(true);
        this.setJMenuBar(menuBar);
        this.add(board);
        this.pack();


        startVisualizationTimer = new Timer(5, this);
        board.addMouseListener(this);
        board.addMouseMotionListener(this);
        board.addMouseWheelListener(this);
    }
    private void setupLegend(){
        this.getContentPane().setLayout(null);

        JPanel legend = new JPanel();
        legend.setLayout(new FlowLayout());
        legend.setBounds(new Rectangle(0, 1000, 1018,150));

//      start
        JLabel start = new JLabel("");
        start.setOpaque(true);
        start.setBackground(NodeState.START.getColor());
        start.setPreferredSize(new Dimension(30,30));

        JLabel startTxt = new JLabel("- start node  ");
        startTxt.setFont(new Font("Bank Gothic",Font.PLAIN, 20));

//      end
        JLabel end = new JLabel("");
        end.setOpaque(true);
        end.setBackground(NodeState.END.getColor());
        end.setPreferredSize(new Dimension(30,30));

        JLabel endTxt = new JLabel("- end node  ");
        endTxt.setFont(new Font("Bank Gothic",Font.PLAIN, 20));

//      open
        JLabel open= new JLabel("");
        open.setOpaque(true);
        open.setBackground(NodeState.OPEN.getColor());
        open.setPreferredSize(new Dimension(30,30));

        JLabel openTxt = new JLabel("- open node  ");
        openTxt.setFont(new Font("Bank Gothic",Font.PLAIN, 20));

//      closed
        JLabel closed= new JLabel("");
        closed.setOpaque(true);
        closed.setBackground(NodeState.CLOSED.getColor());
        closed.setPreferredSize(new Dimension(30,30));

        JLabel closedTxt = new JLabel("- closed node  ");
        closedTxt.setFont(new Font("Bank Gothic",Font.PLAIN, 20));

//      wall
        JLabel wall= new JLabel("");
        wall.setOpaque(true);
        wall.setBackground(NodeState.WALL.getColor());
        wall.setPreferredSize(new Dimension(30,30));

        JLabel wallTxt = new JLabel("- wall node  ");
        wallTxt.setFont(new Font("Bank Gothic",Font.PLAIN, 20));

//     path
        JLabel path= new JLabel("");
        path.setOpaque(true);
        path.setBackground(NodeState.PATH.getColor());
        path.setPreferredSize(new Dimension(30,30));

        JLabel pathTxt = new JLabel("- path node  ");
        pathTxt.setFont(new Font("Bank Gothic",Font.PLAIN, 20));

        legend.add(start);
        legend.add(startTxt);
        legend.add(end);
        legend.add(endTxt);
        legend.add(open);
        legend.add(openTxt);
        legend.add(closed);
        legend.add(closedTxt);
        legend.add(wall);
        legend.add(wallTxt);
        legend.add(path);
        legend.add(pathTxt);
        legend.setComponentOrientation( ComponentOrientation.LEFT_TO_RIGHT );
        this.add(legend);



    }

    private void setUpPathfinding() {
        pathfinding = new Pathfinding(board.getNodes());
//        pathfinding.addPathObserver(board);
    }

    private void setupMenuBar(){
        menuBar = new JMenuBar();
        algorithmsMenu = new JMenu("Algorithms");
        obstaclesMenu = new JMenu("Obstacles");
        actionsMenu = new JMenu("Actions");


        DFS = new JMenuItem("DFS");
        BFS = new JMenuItem("BFS");
        dijkstra = new JMenuItem("Dijkstra");

        random = new JMenuItem("Random");

        start = new JMenuItem("Start");
        pause = new JMenuItem("Pause");
        clear = new JMenuItem("Clear");

        algorithmsMenu.add(DFS);
        algorithmsMenu.add(BFS);
        algorithmsMenu.add(dijkstra);

        obstaclesMenu.add(random);

        actionsMenu.add(start);
        actionsMenu.add(pause);
        actionsMenu.add(clear);

        menuBar.add(algorithmsMenu);
        menuBar.add(obstaclesMenu);
        menuBar.add(actionsMenu);


        DFS.addActionListener(this);
        BFS.addActionListener(this);
        dijkstra.addActionListener(this);
        random.addActionListener(this);
        start.addActionListener(this);
        pause.addActionListener(this);
        clear.addActionListener(this);


    }

    private void setupBoard(){
        board = new Board(nWidth);
    }

    private void setUpVisualizer(){
       stepTimer = new Timer(10, new ActionListener() {
            Queue<Pair<Node, NodeState>> searchQueue = pathfinding.getSearchQueue();
            LinkedList<Node> path = pathfinding.getPath(); // empty if path was not found
           @Override
            public void actionPerformed(ActionEvent e) {
                if(searchQueue.isEmpty() && !path.isEmpty()){
                    Node node = path.poll();
                    board.setState(node, NodeState.PATH);
                    repaint();
                }
                else if(!searchQueue.isEmpty()){
                    Pair<Node, NodeState> element = searchQueue.poll();
                    Node node = element.getValue0();
                    NodeState state = element.getValue1();
                    board.setState(node, state);
                    repaint();

                }
                else{
                    stepTimer.stop();
                    System.out.println("steptimer");
                    isVisualizationReady = false;
                }

            }
        });
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == random && !isVisualizationReady){
            board.setRandomWalls( (int)(0.3*nWidth*nWidth) );
        }
        else if(e.getSource() == DFS){
            currentAlgo = Algorithm.DFS;
        }
        else if(e.getSource() == BFS){
            currentAlgo = Algorithm.BFS;
        }
        else if(e.getSource() == dijkstra){
            currentAlgo = Algorithm.DIJKSTRA;
        }
        else if(e.getSource() == clear){
            board.clear();
            isStartPointed = false;
            isEndPointed = false;
            startNode = null;
            isPause = false;
            isVisualizationReady = false;
            if(stepTimer != null)
                stepTimer.stop();
            pathfinding.clear();
        }
        else if(e.getSource() == pause){
            if(isPause && isVisualizationReady) {
                isPause = false;
                stepTimer.start();
            }
            else if (!isPause && isVisualizationReady) {
                isPause = true;
                stepTimer.stop();
            }
        }

        else if(e.getSource() == start){
            if(currentAlgo != null && isStartPointed && isEndPointed && !isPause && !isVisualizationReady){

                pathfinding.start(currentAlgo, startNode);
                startVisualizationTimer.start(); // checks every x ms if the algorithm is finished
            }

        }
        else if(e.getSource() == startVisualizationTimer){
            if(!pathfinding.isRunning()){
                startVisualizationTimer.stop();
                isVisualizationReady = true;
                isPause = false;
                setUpVisualizer();
                stepTimer.start();
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getSource() == this.board){
            if(isPause || isVisualizationReady)
                return;

            else if( !isStartPointed ) {
                startNode = this.board.setStartNode(e.getX(), e.getY());
                repaint();
                isStartPointed = true;
            }

            else if( !isEndPointed ) {
                this.board.setEndNode(e.getX(), e.getY());
                repaint();
                isEndPointed = true;
            }
            else
                this.board.setWallNode(e.getX(), e.getY(), false);

            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(e.getSource() == this.board) {
            if(isPause || isVisualizationReady)
                return;

            else if(isStartPointed && isEndPointed){
                this.board.setWallNode(e.getX(), e.getY(), true);
            }
            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        if(!board.isClear())
            return;
        int rotation = e.getWheelRotation();
        int prevSize = nWidth;
        int scroll = 1;

        if (rotation == -1 && prevSize + scroll <= Board.MAX_SIZE) {
            nWidth += scroll;
        } else if (rotation == 1 &&prevSize - scroll >= Board.MIN_SIZE ) {
            nWidth += -scroll;
        }

        board.resize(nWidth);
    }
}
