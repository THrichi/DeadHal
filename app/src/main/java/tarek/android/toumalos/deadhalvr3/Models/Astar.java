package tarek.android.toumalos.deadhalvr3.Models;

import android.graphics.Point;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tarek.android.toumalos.deadhalvr3.Const.Global;

public class Astar {

    private Rectangle start;
    private Rectangle end;
    private List<Rectangle> rectangles;
    private List<Noeud> noeuds;
    private List<Noeud> offSet;
    private Rectangle backUp;

    public Astar(List<Rectangle> rectangles, Rectangle start, Rectangle end) {
        this.rectangles = rectangles;
        this.start = start;
        this.end = end;
        this.noeuds = new ArrayList<>();
        this.offSet = new ArrayList<>();
    }

    public List<Noeud> calcule() {
        boolean turn = true;
        Noeud current = new Noeud(start, Double.MAX_VALUE, start);
        while (turn && current != null) {
            if (current.getRectangle().equals(end)) {
                turn = false;
            } else {
                Noeud noeud = calculeMinimumDistance(current);
                if (noeud == null) {
                    offSet.add(current);
                    current = getParent(current);
                } else {
                    current = noeud;
                    noeuds.add(current);
                }
            }
        }
        correction();
        return noeuds;
    }

    private void correction() {
        for (Noeud noeud : offSet) {
            if (contains(noeuds, noeud.getRectangle())) {
                noeuds.remove(noeud);
            }
        }
    }

    private Noeud getParent(Noeud current) {
        for (Noeud noeud : noeuds) {
            if (noeud.getRectangle().getUID().equals(current.getParent().getUID())) {
                return noeud;
            }
        }
        return null;
    }

    private boolean contains(List<Noeud> noeuds, Rectangle rectangle) {
        for (Noeud noeud : noeuds) {
            if (noeud.getRectangle().equals(rectangle)) {
                return true;
            }
        }
        return false;
    }


    public Noeud calculeMinimumDistance(Noeud noeud) {
        Noeud result = null;
        double min = Double.MAX_VALUE;

        for (Line line : noeud.getRectangle().getRectanglesId()) {
            if (!getRectangle(line.getGoToId()).equals(noeud.getParent()) && !contains(offSet, getRectangle(line.getGoToId()))&& !contains(noeuds, getRectangle(line.getGoToId()))) {
                double F = calculeDistance(noeud.getRectangle(), getRectangle(line.getGoToId())) + calculeDistance(getRectangle(line.getGoToId()), end);
                if (F < min) {
                    min = F;
                    result = new Noeud(getRectangle(line.getGoToId()), F, noeud.getRectangle());
                }
            }
        }
        return result;
    }

    public double calculeDistance(Rectangle A, Rectangle B) {
        Point A_Point = getPoint(A);
        Point B_Point = getPoint(B);
        return new Line().distance(A_Point, B_Point);
    }

    private Point getPoint(Rectangle rectangle) {
        Point point = new Point();
        point.x = (int) (rectangle.getRectangle().left + rectangle.getRectangle().right) / 2;
        point.y = (int) (rectangle.getRectangle().top + rectangle.getRectangle().bottom) / 2;
        return point;
    }

    private Rectangle getRectangle(String id) {
        for (Rectangle r : rectangles) {
            if (r.getUID().equals(id))
                return r;
        }
        return null;
    }
}