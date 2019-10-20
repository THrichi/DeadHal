package tarek.android.toumalos.deadhalvr3.Models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Chemin {
    private Rectangle source;
    private double distance;
    private List<Rectangle> chemins;

    public Chemin() {
        chemins = new ArrayList<>();
    }
    public Chemin(Rectangle source) {
        this.source = source;
        chemins = new ArrayList<>();
    }

    public Chemin(Rectangle source, double distance, List<Rectangle> chemins) {
        this.distance = distance;
        this.chemins = chemins;
        this.source = source;
    }


    public List<Rectangle> getChemins() {
        return chemins;
    }
    public void add(Rectangle rectangle){
        chemins.add(rectangle);
    }

    public Rectangle getSource() {
        return source;
    }

    public void setSource(Rectangle source) {
        this.source = source;
    }

    public void setChemins(List<Rectangle> chemins) {
        this.chemins = chemins;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @NonNull
    @Override
    public String toString() {
        String s = "Distance : " + distance + " Chemin --> ";

        for (Rectangle rectangle : chemins) {
            s+= rectangle.getName() + " -> " ;
        }
        s+="\n";
        return s;
    }
}
