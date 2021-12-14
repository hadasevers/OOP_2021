package Ex2_code;

import Ex2_code.api.EdgeData;

public class MyEdge implements EdgeData {

    int src;
    int dest;
    double weight;
    String remark;
    int tag;

    public MyEdge (int src, int dest, double weight, String remark){
        this.src=src;
        this.dest=dest;
        this.weight=weight;
        this.remark=remark;
        this.tag=0;
    }

    @Override
    public int getSrc() {
        return src;
    }

    @Override
    public int getDest() {
        return dest;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public String getInfo() {
        return remark;
    }

    @Override
    public void setInfo(String s) {
        remark=s;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
        tag=t;
    }

    public String toString(){
        return ("src:"+this.getSrc()+", dest:"+this.getDest()+", w:"+this.getWeight());
    }
}
