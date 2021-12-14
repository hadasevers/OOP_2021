package Ex2_code;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.awt.*;
import javax.swing.JFrame;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Ex2_gui extends JFrame implements ActionListener {
    static final int WIDTH = 900;
    static final int HEIGHT = 700;
    public static MyAlgo algo;
    private MyGragh gragh;
    private  HashMap<Integer, MyNode> node;
    private HashMap<Integer, ArrayList<HashMap<Integer, MyEdge>>> nodeEdge;
    private List<MyEdge> myEdgeList;
    private Image mBuffer_image;
    private Graphics mBuffer_graphics;
    private boolean Right=true;
    private boolean Left=false;
    private boolean Up=false;
    private boolean Down=true;
    private double scaleX=0, scaleY=0;
    private double minX = 10000, minY = 10000;

    Ex2_gui(MyAlgo myalgo) {
        initGUI(myalgo);
    }

    private void initGUI(MyAlgo myalgo) {
        //JFrame mainFrame = new JFrame("Ex2.Ex2");
        algo=myalgo;
        gragh = algo.getGraph();
        node = gragh.nodes;
        nodeEdge = gragh.nodeEdge;
        myEdgeList = gragh.myEdgeList;

        this.setTitle("Ex2_code");
        this.setSize(WIDTH, HEIGHT);
        this.setLocation(300, 60);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        MenuBar menubar = new MenuBar();
        Menu file = new Menu("File");
        menubar.add(file);
        Menu edit = new Menu("Edit");
        menubar.add(edit);

        MenuItem save = new MenuItem("save");
        file.add(save);
        save.addActionListener(this);
        MenuItem load = new MenuItem("load");
        file.add(load);
        load.addActionListener(this);

        MenuItem addEdge = new MenuItem("addEdge");
        edit.add(addEdge);
        addEdge.addActionListener(this);
        MenuItem deleteEdge = new MenuItem("deleteEdge");
        edit.add(deleteEdge);
        deleteEdge.addActionListener(this);
        MenuItem deleteNode = new MenuItem("deleteNode");
        edit.add(deleteNode);
        deleteNode.addActionListener(this);

        this.setMenuBar(menubar);
        this.setVisible(true);

        Container contPane = this.getContentPane();
        contPane.setLayout(null);




    }

    @Override
    public void paintComponents(Graphics g) {
        double maxX = 0, maxY = 0, AbsX=0, AbsY=0;
        double dx=0, dy=0, srcX=0, srcY=0, destX=0, destY=0;
        int srcEdge=0, destEdge=0, x_src=0, y_src=0, x_dest=0, y_dest=0;
        boolean sideX=false;
        boolean sideY=false;
        if (node == null) {
            System.out.println("load a file in order to start: file->load");
            g.drawString("load  a  file  in  order  to  start:  file -> load",(int)(WIDTH/3), 100);
        }
        else{
            for (MyNode i : node.values()) {
                double x = i.getLocation().x();
                double y = i.getLocation().y();
                if (x > maxX) maxX = x;
                if (x < minX) minX = x;
                if (y > maxY) maxY = y;
                if (y < minY) minY = y;
            }
            AbsX = Math.abs(maxX - minX);
            AbsY = Math.abs(maxY - minY);
            scaleX = WIDTH*0.87 / AbsX;
            scaleY = HEIGHT*0.87 / AbsY;
            g.setColor(Color.BLACK);
            for (MyNode i : node.values()) {
                dx = i.getLocation().x();
                dy = i.getLocation().y();
                double dz = i.getLocation().z();
                int x = (int) (scaleX * (Math.abs(dx-minX)))+50;
                int y = (int) (scaleY * (Math.abs(dy-minY)))+52;
                g.fillOval(x , y , 10, 10);
                g.drawString(String.valueOf(i.getKey()),x+11, y+11);
            }
            for (MyEdge edge: myEdgeList){
                srcEdge = edge.getSrc();
                destEdge = edge.getDest();
                MyNode srcNode = node.get(srcEdge);
                MyNode destNode = node.get(destEdge);
                srcX = srcNode.getLocation().x();
                srcY = srcNode.getLocation().y();
                destX = destNode.getLocation().x();
                destY = destNode.getLocation().y();
                x_src = (int)(scaleX * (Math.abs(srcX-minX)))+55;
                y_src = (int) (scaleY * (Math.abs(srcY-minY)))+57;
                x_dest = (int)(scaleX * (Math.abs(destX-minX)))+55;
                y_dest = (int) (scaleY * (Math.abs(destY-minY)))+57;
                g.setColor(Color.RED);
                g.drawLine(x_src, y_src, x_dest, y_dest);
/**
                int x1=0, x2=0, y1=0, y2=0, dis=7;
                double x=0, y=0;
                double m1=(y_dest-y_src)/(x_dest-x_src);
                double m2=(1/m1)*(-1);
                double a=(m1*m1)+1;
                double b=a*(-2)*(x_dest);
                double c=(a*(x_dest*x_dest))-(dis*dis);
                double ans1=((-b)+(Math.sqrt((b*b)-4*(a*c))))/(2*a);
                double ans2=((-b)-(Math.sqrt((b*b)-4*(a*c))))/(2*a);
                if (ans1>ans2) x=ans2;
                else x=ans1;
                y=y_dest-(m1*(x_dest-x));

                a=(m2*m2)+1;
                b=a*(-2)*(x);
                c=(a*(x*x))-(dis*dis);
                ans1=((-b)+(Math.sqrt((b*b)-4*(a*c))))/(2*a);
                ans2=((-b)-(Math.sqrt((b*b)-4*(a*c))))/(2*a);
                x1=(int)ans1;
                y1=(int)(y-(m2*(x-x1)));
                x2=(int)ans2;
                y2=(int)(y-(m2*(x-x2)));

                int [] pointX = new int[3];
                int [] pointY = new int[3];

                pointX[0]=x_dest;
                pointX[1]=x1;
                pointX[2]=x2;

                pointY[0]=y_dest;
                pointY[1]=y1;
                pointY[2]=y2;

                g.setColor(Color.BLUE);
                g.drawPolyline(pointX,pointY,3);
                **/
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        // Create a new "canvas"
        mBuffer_image = createImage(WIDTH,HEIGHT );
        mBuffer_graphics = mBuffer_image.getGraphics();

        // Draw on the new "canvas"
        paintComponents(mBuffer_graphics);

        // "Switch" the old "canvas" for the new one
        g.drawImage(mBuffer_image, 0, 0, this);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = e.getActionCommand();

        if (str.equals("load")){
            System.out.println("enter name json file");
            String name=null;
            try {
                // Enter data using BufferReader
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                // Reading data using readLine
                name = reader.readLine();
                // Printing the read line
                System.out.println("the file '"+name+"' load");
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
            algo=(MyAlgo) Ex2.getGrapgAlgo(name);
            gragh = algo.getGraph();
            node = gragh.nodes;
            nodeEdge = gragh.nodeEdge;
            myEdgeList = gragh.myEdgeList;
            repaint();
        }
     else if (str.equals("save")){
            String name=null;
            System.out.println("enter a new name for this graph file");
            try {
                // Enter data using BufferReader
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                // Reading data using readLine
                name = reader.readLine();
                // Printing the read line
                System.out.println("you wrote: "+name);
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
            if (algo==null) System.out.println("pleas load a file in order to start: file->load");
            else algo.save(name);
            repaint();
        }
     else if (str.equals("addEdge")){
            System.out.println("enter id node for src");
            int src=0, dest=0;
            double w=0;
            try {
                // Enter data using BufferReader
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                // Reading data using readLine
                src = Integer.parseInt(reader.readLine());
                // Printing the read line
                System.out.println("your src: "+src);
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
            System.out.println("enter id node for dest");
            try {
                // Enter data using BufferReader
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                // Reading data using readLine
                dest = Integer.parseInt(reader.readLine());
                // Printing the read line
                System.out.println("your dest: "+dest);
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
            System.out.println("enter weight node");
            try {
                // Enter data using BufferReader
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                // Reading data using readLine
                w = Double.parseDouble(reader.readLine());
                // Printing the read line
                System.out.println("your weight: "+w);
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
            gragh.connect(src, dest, w);
            algo.init(gragh);
            repaint();
        }
        else if (str.equals("deleteEdge")){
            System.out.println("enter id node of edge src");
            int src=0, dest=0;
            try {
                // Enter data using BufferReader
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                // Reading data using readLine
                src = Integer.parseInt(reader.readLine());
                // Printing the read line
                System.out.println("your src: "+src);
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
            System.out.println("enter id node of edge dest");
            try {
                // Enter data using BufferReader
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                // Reading data using readLine
                dest = Integer.parseInt(reader.readLine());
                // Printing the read line
                System.out.println("your dest: "+dest);
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
            gragh.removeEdge(src,dest);
            algo.init(gragh);
            repaint();
        }
        else if (str.equals("deleteNode")){
            System.out.println("enter id node to delete");
            int id =0;
            try {
                // Enter data using BufferReader
                BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                // Reading data using readLine
                id = Integer.parseInt(reader.readLine());
                // Printing the read line
                System.out.println("your id: "+id);
            }
            catch (IOException exception) {
                exception.printStackTrace();
            }
            gragh.removeNode(id);
            algo.init(gragh);
            repaint();

        }
    }
}


