package Ex2_code;

import Ex2_code.api.GeoLocation;
import Ex2_code.api.NodeData;

public class MyNode implements NodeData {

    int id;
    MyGeoLocation gl;
    double weight;
    String remark;
    int tag;

    public MyNode (int id, MyGeoLocation gl){
        this.id=id;
        this.gl=gl;
        this.weight=0;
        this.remark=null;
        this.tag=0;
    }

    @Override
    public int getKey() {
        return id;
    }

    @Override
    public GeoLocation getLocation() {
        return gl;
    }

    @Override
    public void setLocation(GeoLocation p) {
        gl.x=p.x();
        gl.y=p.y();
        gl.z=p.z();
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double w) {
        weight=w;
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
        return ("[id:"+this.id+", pos:"+this.getLocation().toString()+"]");
    }
}
