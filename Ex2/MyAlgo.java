package Ex2_code;

import Ex2_code.api.DirectedWeightedGraph;
import Ex2_code.api.DirectedWeightedGraphAlgorithms;
import Ex2_code.api.EdgeData;
import Ex2_code.api.NodeData;

import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.FileNotFoundException;
import java.io.FileReader;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


import java.util.*;

public class MyAlgo implements DirectedWeightedGraphAlgorithms {

    public static MyGragh graph;

    public MyAlgo(){
        graph=null;
    }

    @Override
    public void init(DirectedWeightedGraph g) {
        graph=(MyGragh) g;
    }

    @Override
    public MyGragh getGraph() {
        return graph;
    }

    @Override
    public MyGragh copy() {
        MyGragh copygraph= new MyGragh(graph);
        return copygraph;
    }

    @Override
    public boolean isConnected() {
        bfs(this.copy());
        boolean ans=true;
        for (MyNode i : this.copy().nodes.values()){
            if (i.getTag()!=1) {
                ans=false;
                break;
            }
        }
        if (ans==false) return false;
        else {
            MyGragh change = changeDirect(this.copy());
            bfs(change);
            ans=true;
            for (MyNode i : change.nodes.values()) {
                if (i.getTag() != 1) {
                    ans = false;
                    break;
                }
            }
            return ans;
        }
    }

    public void bfs(MyGragh g){
        LinkedList<Integer> queue = new LinkedList();
        MyNode start = null;
        for (MyNode i : g.nodes.values()) {
            i.setTag(0);
        }
        for (MyNode i : g.nodes.values()){
            start=i;
            break;
        }
        int current=start.getKey();
        queue.add(current);
        while (queue.isEmpty()==false){
            current=queue.poll();
            g.nodes.get(current).setTag(1);
            Iterator<EdgeData> it= g.edgeIter(current);
            while (it.hasNext()){
                MyEdge now=(MyEdge) it.next();
                if (g.nodes.get(now.getDest()).getTag()==0){
                    queue.add(now.getDest());
                    g.nodes.get(now.getDest()).setTag(1);
                }
                it.remove();
            }
        }
    }

    public MyGragh changeDirect(MyGragh g){
        for (MyNode i : g.nodes.values()) {
            i.setTag(0);
        }
        HashMap<Integer, ArrayList<HashMap<Integer, MyEdge>>> nodeEdge2 = new HashMap<Integer, ArrayList<HashMap<Integer, MyEdge>>>();
        Iterator<EdgeData> it=g.edgeIter();
        while (it.hasNext()==true){
            MyEdge srcEdge=(MyEdge) it.next();
            it.remove();
            MyEdge current= new MyEdge(srcEdge.getDest(), srcEdge.getSrc(), srcEdge.getWeight(), null);
            if (nodeEdge2.containsKey(current.getSrc())){
                HashMap<Integer, MyEdge> edge = new  HashMap<Integer, MyEdge>();
                nodeEdge2.get(current.getSrc()).add(edge);
            }
            else {
                HashMap<Integer, MyEdge> edge = new  HashMap<Integer, MyEdge>();
                edge.put(current.getDest(), current);
                ArrayList<HashMap<Integer, MyEdge>> nodeEdge1= new  ArrayList<HashMap<Integer, MyEdge>>();
                nodeEdge1.add(edge);
                nodeEdge2.put(current.getSrc(), nodeEdge1);
            }
        }
        g.nodeEdge=nodeEdge2;
        return g;
    }

    @Override
    public double shortestPathDist(int src, int dest) {

        if (graph.nodes.containsKey(src)==false) return -1;
        HashMap<Integer, MyNode> node = algo_center(src);
        MyNode current=node.get(dest);
        if (current.getTag()==0) return -1; // or current.getWeight()==1000000 -> There is no route from src to dest
        else return current.getWeight();
    }

    public HashMap<Integer, MyNode> algo_center(int id_node){

        MyGragh g =this.copy();
        // hash in order to update the weight of the nodes
        HashMap<Integer, MyNode> node = g.nodes;
        // start when the weight of the node is very high, and they don't view
        for (MyNode i : node.values()) {
            i.setTag(0);
            i.setWeight(1000000);
        }
        // queue for the connection nodes
        LinkedList<MyNode> queue = new LinkedList<MyNode>();
        // the first node
        MyNode current=node.get(id_node);
        node.get(id_node).setWeight(0);
        node.get(id_node).setTag(1);
        queue.add(current);

        while (queue.isEmpty()==false){
            current=getMin(queue);
            queue.remove(current);
            ArrayList<HashMap<Integer, MyEdge>> j = g.nodeEdge.get(current.getKey());
            for (HashMap<Integer, MyEdge> i : j){
                MyEdge e = i.values().iterator().next();
                double newWeight =current.getWeight()+e.getWeight();
                if (newWeight<node.get(e.getDest()).getWeight())
                    node.get(e.getDest()).setWeight(newWeight);
                if (node.get(e.getDest()).getTag()==0) {
                    node.get(e.getDest()).setTag(1);
                    queue.add(node.get(e.getDest()));
                }
            }
        }
        return node;
    }

    // return the node with lowest weight
    public MyNode getMin(LinkedList<MyNode>queue){
        MyNode min=queue.peek();
        MyNode current=min;
        int size= queue.size();
        for (int i = 1; i < size; ++i) {
            current=queue.get(i);
            if (min.getWeight() > current.getWeight())
                min = current;
        }
        return min;
    }

    @Override
    public List<NodeData> shortestPath(int src, int dest) {

        if (graph.nodes.containsKey(src)==false) return null;
        MyGragh g =this.copy();
        // hash in order to update the weight of the nodes
        HashMap<Integer, MyNode> node = g.nodes;
        // key-id node, value-the node that pointed at me
        HashMap<Integer, MyNode> pointer= new HashMap<Integer, MyNode>();
        // start when the weight of the node is very high, and they don't view
        for (MyNode i : node.values()) {
            i.setTag(0);
            i.setWeight(1000000);
        }
        // queue for the connection nodes
        LinkedList<MyNode> queue = new LinkedList<MyNode>();
        // the first node
        MyNode current=node.get(src);
        node.get(src).setWeight(0);
        node.get(src).setTag(1);
        queue.add(node.get(src));
        pointer.put(src,null);

        while (queue.isEmpty()==false){
            current=getMin(queue);
            queue.remove(current);
            if (current.getKey()==dest){
                break;
            }
            ArrayList<HashMap<Integer,MyEdge>>j = g.nodeEdge.get(current.getKey());
            for (HashMap<Integer,MyEdge> i : j){
                MyEdge e = i.values().iterator().next();
                double newWeight =current.getWeight()+e.getWeight();
                if (newWeight<node.get(e.getDest()).getWeight()) {
                    node.get(e.getDest()).setWeight(newWeight);
                    pointer.put(e.getDest(),current);
                }
                if (node.get(e.getDest()).getTag()==0) {
                    node.get(e.getDest()).setTag(1);
                    queue.add(node.get(e.getDest()));
                }
            }
        }

        current=node.get(dest);
        if (current.getTag()==0) return null; // or current.getWeight()==1000000 , or route.get(dest)==null   -> There is no route from src to dest
        else{
            List<NodeData> route = new ArrayList<NodeData>();
            route.add(current);
            MyNode previous=pointer.get(current.getKey());
            while (previous!=null){
                current=previous;
                route.add(0,current);
                previous=pointer.get(current.getKey());
            }
            return route;
        }
    }

    @Override
    public NodeData center() {

        if (this.isConnected()==false) return null;

        HashMap<Integer, MyNode> nodes = graph.nodes;
        HashMap<Integer, MyNode> current_node = new HashMap<Integer, MyNode>();
        MyNode ans=new MyNode(0,null);
        double max=0;
        double min=1000000;

        for( MyNode i : nodes.values()){
            max=0;
            current_node=algo_center(i.getKey());
            // found the nax distance from node i
            for(MyNode j : current_node.values()){
                if (j.getWeight()>max) max=j.getWeight();
            }
            if (max<min){
                min=max;
                ans=i;
            }
        }
        return ans;
    }

    @Override
    public List<NodeData> tsp(List<NodeData> cities) {

        if (this.isConnected()==false) return null;
        List<NodeData> route = new ArrayList<NodeData>();

        // found the farthest node from the center. this node will be the start of the route
        MyNode center= (MyNode) this.center();
        MyNode farthest=center;
        double max=0, dis=0;
        for(NodeData i : cities){
            dis=shortestPathDist(center.getKey(), i.getKey());
            if (dis>max){
                max=dis;
                farthest=(MyNode) i;
            }
        }
        route.add(farthest);
        cities.remove(farthest);

        // the loop get the node with the min id
        MyNode current=farthest;
        MyNode min=farthest;
        int min_id=1000000;
        while (cities.isEmpty()==false){
            min_id=1000000;
            for(int i=0; i<cities.size(); ++i) {
                NodeData n = cities.get(i);
                if (n.getKey() < min_id) {
                    min_id = n.getKey();
                    min = (MyNode) n;
                }
                // found the short route from current node to min node
                List<NodeData> short_route = shortestPath(current.getKey(), min.getKey());
                short_route.remove(0);
                // add the short route to route
                route.addAll(short_route);
                cities.remove(min);

                // if there is a node in the short route that we found, remove it from the cities list
                while (short_route.isEmpty() == false) {
                    if (cities.contains(short_route.get(0))) cities.remove(short_route.get(0));
                    short_route.remove(0);
                }
                current = min;
            }
        }
        return route;
    }

    @Override
    public boolean save(String file) {

        // add edges
        JSONArray edgelist=new  JSONArray();
        for ( ArrayList<HashMap<Integer,MyEdge>> al: graph.nodeEdge.values()){
            for(HashMap<Integer,MyEdge> i : al) {
                for (MyEdge j : i.values()) {
                    JSONObject graph_edge = new JSONObject();
                    graph_edge.put("src", j.getSrc());
                    graph_edge.put("w", j.getWeight());
                    graph_edge.put("dest", j.getDest());
                    edgelist.add(graph_edge);
                }
            }
        }
        // add nodes
        JSONArray nodelist=new JSONArray();
        for(MyNode i : graph.nodes.values()){
            JSONObject graph_node = new JSONObject();
            graph_node.put("pos", i.getLocation().toString());
            graph_node.put("id", i.getKey());
            nodelist.add(graph_node);
        }

        // write all the graph data to HashMap
        JSONObject allGraph = new JSONObject();
        allGraph.put("Edges", edgelist);
        allGraph.put("Nodes", nodelist);

        //Write JSON file
        try (FileWriter fileWriter = new FileWriter(file)) {
            fileWriter.write(allGraph.toJSONString());
            fileWriter.flush();
            return true;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean load(String file) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(file)) {
            //Read JSON file
            Object graphobj = jsonParser.parse(reader);

            // cost to hashmap
            HashMap<String,HashMap<String, String>> hash= (HashMap<String, HashMap<String, String>>) jsonParser.parse(String.valueOf(graphobj));
            // cost to list of edge
            List<String> edgeList= (List<String>)hash.get("Edges");
            // cost to list of edge
            List<String> nodeList= (List<String>)hash.get("Nodes");

            List<MyEdge>myEdgeList= new ArrayList<MyEdge>();

            int src=0, dest =0;
            double weight=0;
            HashMap<Integer, ArrayList<HashMap<Integer,MyEdge>>> allEdge= new HashMap<Integer,ArrayList<HashMap<Integer,MyEdge>>>();

            // create HashMap of HashMap to the graph edge
            while (edgeList.isEmpty()==false){
                HashMap<String, String> edgeObj=(HashMap<String, String>) jsonParser.parse(String.valueOf(edgeList.get(0)));
                src=Integer.parseInt(String.valueOf(edgeObj.get("src")));
                dest=Integer.parseInt(String.valueOf(edgeObj.get("dest")));
                weight=Double.parseDouble(String.valueOf(edgeObj.get("w")));
                MyEdge e = new MyEdge(src, dest, weight, null);
                myEdgeList.add(e);
                HashMap<Integer,MyEdge> edge = new HashMap<Integer,MyEdge>();
                edge.put(e.getDest(), e);
                if (allEdge.containsKey(e.getSrc())) allEdge.get(e.getSrc()).add(edge);
                else {
                    ArrayList<HashMap<Integer,MyEdge>> edgeArray= new ArrayList<HashMap<Integer,MyEdge>>();
                    edgeArray.add(edge);
                    allEdge.put(e.getSrc(), edgeArray);
                }
                edgeList.remove(0);
            }

            int id=0;
            double x=0, y=0, z=0;
            HashMap<Integer,MyNode> allNode=new HashMap<Integer,MyNode>();

            // create HashMap to the graph node
            while (nodeList.isEmpty()==false) {
                HashMap<String, String> nodeObj=(HashMap<String, String>) jsonParser.parse(String.valueOf(nodeList.get(0)));
                id = Integer.parseInt(String.valueOf(nodeObj.get("id")));
                String pos = String.valueOf(nodeObj.get("pos"));
                String[] s = pos.split(",", 3);
                x = Double.parseDouble(s[0]);
                y = Double.parseDouble(s[1]);
                z = Double.parseDouble(s[2]);
                MyGeoLocation geo = new MyGeoLocation(x,y,z);
                MyNode n = new MyNode(id, geo);
                allNode.put(n.getKey(), n);
                nodeList.remove(0);
            }

            MyGragh thisGraph=new MyGragh(allEdge, allNode, myEdgeList);
            this.init(thisGraph);
            return true;

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
        catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

    }
}
