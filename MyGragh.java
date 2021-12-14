package Ex2_code;

import Ex2_code.api.DirectedWeightedGraph;
import Ex2_code.api.EdgeData;
import Ex2_code.api.NodeData;

import java.util.*;
import java.util.List;

public class MyGragh implements DirectedWeightedGraph {

    HashMap<Integer, ArrayList<HashMap<Integer,MyEdge>>> nodeEdge;
    HashMap<Integer, MyNode> nodes;
    int node_Size;
    int edge_Size;
    int mc;
    List<MyEdge> myEdgeList;

    public MyGragh(HashMap<Integer, ArrayList<HashMap<Integer,MyEdge>>> nodeEdge, HashMap<Integer, MyNode> nodes, List<MyEdge>myEdgeList) {
        this.nodeEdge = nodeEdge;
        this.nodes = nodes;
        this.node_Size=nodes.values().size();
        this.mc=0;
        this.myEdgeList=myEdgeList;
        this.edge_Size=myEdgeList.size();
    }


        // copy constructor
    public MyGragh (MyGragh g){
        this.nodeEdge=g.nodeEdge;
        this.nodes=g.nodes;
        this.node_Size=g.node_Size;
        this.edge_Size=g.edge_Size;
        this.mc=g.mc;
        this.myEdgeList=g.myEdgeList;
    }


    @Override
    public NodeData getNode(int key) {
        if (nodes.containsKey(key)==true) {
            MyNode currentNode = nodes.get(key);
            return currentNode;
        }
        else return null;
    }


    @Override
    public EdgeData getEdge(int src, int dest) {
        MyEdge currentEdge = null;
        if ((nodeEdge.containsKey(src))){
            for (HashMap<Integer,MyEdge> i : nodeEdge.get(src)) {
                if (i.containsKey(dest))
                    currentEdge = i.get(dest);
            }
        }
        return currentEdge;
    }

    @Override
    public void addNode(NodeData n) {
        this.mc++;
        MyNode newNode=(MyNode) n;
        // if exist node with this id, update it
        if (this.nodes.containsKey(newNode.getKey())==true){
            boolean ans = false;
            for(MyNode nd : this.nodes.values()) {
               if (nd.getLocation()==newNode.getLocation()) ans=true;
            }
            if (ans==false) {
                this.nodes.get(newNode.getKey()).setLocation(newNode.getLocation());
                this.nodes.get(newNode.getKey()).setWeight(newNode.getWeight());
                this.nodes.get(newNode.getKey()).setInfo(newNode.getInfo());
                this.nodes.get(newNode.getKey()).setTag(newNode.getTag());
            }
        }
        else {
            this.nodes.put(newNode.getKey(), newNode);
            ArrayList<HashMap<Integer, MyEdge>> newlist= new  ArrayList<HashMap<Integer, MyEdge>>();
            this.nodeEdge.put(newNode.getKey(), newlist);
            this.node_Size++;
        }
    }

    @Override
    public void connect(int src, int dest, double w) {
        mc++;
        if ((this.getNode(src)!=null)&&(this.getNode(dest)!=null)){
            if (this.getEdge(src, dest)==null) {
                MyEdge newEdge=new MyEdge(src, dest, w, null);
                myEdgeList.add(newEdge);
                HashMap<Integer, MyEdge> edges=new HashMap<Integer, MyEdge>();
                edges.put(dest, newEdge);
                nodeEdge.get(src).add(edges);
                edge_Size++;
            }
        }
    }

    @Override
    public Iterator<NodeData> nodeIter() {
        LinkedList<NodeData> n = (LinkedList) nodes.values();
        Iterator<NodeData> it= n.iterator();
        return it;
    }

    @Override
    public Iterator<EdgeData> edgeIter() {
        List<EdgeData> listedge= new ArrayList<EdgeData>();
        for(MyEdge ed: this.myEdgeList){
            listedge.add(ed);
        }
        return listedge.iterator();
    }

    @Override
    public Iterator<EdgeData> edgeIter(int node_id) {
        List<EdgeData> listedge= new ArrayList<EdgeData>();
        for(MyEdge ed: this.myEdgeList){
            if (ed.getSrc()==node_id)
                listedge.add(ed);
        }
        return listedge.iterator();
    }

    @Override
    public NodeData removeNode(int key) {
        MyNode deleteNode=null;
        if (nodeEdge.containsKey(key)==true){
            deleteNode=(MyNode)this.getNode(key);
            // remove edges that their src is this node
            nodeEdge.remove(key);
            nodes.remove(key);
            node_Size--;
            mc++;
                // remove edges that their dest is this node
            for (ArrayList<HashMap<Integer,MyEdge>> j : nodeEdge.values()){
                if (j!=null)
                    for (int i =0; i< j.size(); ++i){
                        HashMap<Integer,MyEdge> h = j.get(i);
                     // if the dest of the edge is this node- delete it
                        if (h.containsKey(key))  j.remove(h);
                    }
            }
            //remove from the edge list all the edge that their src or dest is this node
            for (int i =0; i < myEdgeList.size(); ++i) {
                    MyEdge e = myEdgeList.get(i);
                    if ((e.getSrc() == key) || (e.getDest() == key)) {
                        myEdgeList.remove(i);
                        --i;
                    }
            }
            edge_Size=myEdgeList.size();
        }
        return deleteNode;
    }

    @Override
    public EdgeData removeEdge(int src, int dest) {
        MyEdge deleteEdge = null;
        if (nodeEdge.containsKey(src)==true){
            for (HashMap<Integer,MyEdge> i : nodeEdge.get(src)){
                if (i.containsKey(dest)){
                    deleteEdge=i.get(dest);
                    myEdgeList.remove(deleteEdge);
                    nodeEdge.get(src).remove(i);
                    break;
                }
            }
            edge_Size--;
            mc++;
            }
        return deleteEdge;
    }

    @Override
    public int nodeSize() {
        return node_Size;
    }

    @Override
    public int edgeSize() {
        return edge_Size;
    }

    @Override
    public int getMC() {
        return mc;
    }

    public String toString(){
        ArrayList <String> ans= new ArrayList<String>();
        for(ArrayList<HashMap<Integer, MyEdge>> i : this.nodeEdge.values()){
            if (i!=null)
                for (HashMap<Integer, MyEdge> j : i){
                    ans.add(j.values().toString());
                }
        }
        return ("Edge: "+ans.toString()+",  Nodes: "+nodes.values().toString());
    }
}