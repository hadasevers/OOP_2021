package Ex2_code;

import Ex2_code.api.GeoLocation;

public class MyGeoLocation implements GeoLocation {

    double x, y, z;

    public MyGeoLocation (double x, double y, double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }

    @Override
    public double x() {
        return this.x;
    }

    @Override
    public double y() {
        return this.y;
    }

    @Override
    public double z() {
        return this.z;
    }

    @Override
    public double distance(GeoLocation g) {
        double dx=Math.pow((this.x-g.x()),2);
        double dy=Math.pow((this.y-g.y()),2);
        double dz=Math.pow((this.z-g.z()),2);
        double ans=Math.sqrt((dx+dy+dz));
        return ans;
    }

    public String toString(){
        return (this.x()+","+this.y()+","+this.z());
    }
}
