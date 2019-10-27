package tarek.android.toumalos.deadhalvr3.Models;

import androidx.annotation.NonNull;

public class Noeud {
    private Rectangle rectangle;
    private double distance;
    private Rectangle parent;


    public Noeud(Rectangle rectangle, double distance, Rectangle parent) {
        this.rectangle = rectangle;
        this.distance = distance;
        this.parent = parent;
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public Rectangle getParent() {
        return parent;
    }

    public void setParent(Rectangle parent) {
        this.parent = parent;
    }

    @NonNull
    @Override
    public String toString() {
        return "rectangle : " + rectangle.getName() + " distance = " + distance +" parent : " + parent.getName() +  "\n";
    }
}
