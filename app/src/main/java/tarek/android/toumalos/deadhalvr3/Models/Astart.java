package tarek.android.toumalos.deadhalvr3.Models;

import android.graphics.Point;
import android.graphics.RectF;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import tarek.android.toumalos.deadhalvr3.Const.Global;

public class Astart {
    private List<Rectangle> rectangles;
    private List<Chemin> chemins;
    private Rectangle start;
    private Rectangle end;
    private Chemin choosenOne;
    private List<String> closeSet;
    private List<Rectangle> result;

    public Astart(List<Rectangle> rectangles, Rectangle start, Rectangle end) {
        this.rectangles = rectangles;
        this.chemins = new ArrayList<>();
        this.result = new ArrayList<>();
        closeSet = new ArrayList<>();
        this.start = start;
        this.end = end;
    }

    public List<Rectangle> getRectangles() {
        return rectangles;
    }

    public void setRectangles(List<Rectangle> rectangles) {
        this.rectangles = rectangles;
    }

    public Rectangle getStart() {
        return start;
    }

    public void setStart(Rectangle start) {
        this.start = start;
    }

    public Rectangle getEnd() {
        return end;
    }

    public void setEnd(Rectangle end) {
        this.end = end;
    }

    public List<Chemin> getChemins() {
        return chemins;
    }

    public void setChemins(List<Chemin> chemins) {
        this.chemins = chemins;
    }

    public Chemin getTheway(Rectangle rectangle) {
        Chemin chemin = new Chemin(rectangle);
        for (Line line : rectangle.getRectanglesId()) {
            chemin.add(getRectangle(line.getGoToId()));
        }
        return chemin;
    }

    public void getAllWays() {
        for (Rectangle rect : rectangles) {
            chemins.add(getTheway(rect));
        }
    }

    public Rectangle getRectangle(String UID) {
        for (Rectangle rect : rectangles) {
            if (rect.getUID().equals(UID))
                return rect;
        }
        return null;
    }

    public List<Chemin> calculer(Rectangle theWay) {
        getAllWays();
        List<Chemin> result = new ArrayList<>();
        for (Chemin chemin : chemins) {
            if (chemin.getChemins().contains(theWay)) {
                result.add(chemin);
            }
        }
        chemins = new ArrayList<>(result);
        return result;
    }

    public Chemin lePlusCourtChemin() {
        getAllWays();
        Log.d(Global.TAG, "Start: " + start.getName());
        Log.d(Global.TAG, "End: " + end.getName());
        boolean turn = true;
        Rectangle theWay = end;
        Rectangle oldWay = end;
        while (turn) {
            List<Chemin> result = new ArrayList<>();
            for (Chemin chemin : chemins) {
                if (chemin.getChemins().contains(theWay)) {
                    result.add(chemin);
                }
            }

            Log.d(Global.TAG, "theWay: " + theWay.getName());
            Log.d(Global.TAG, toString(result));
            //choosenOne = getMinCount(result, oldWay);

            if (result.size() == 0) {
                choosenOne = null;
                turn = false;
            } else if (choosenOne.getSource().equals(start)) {
                turn = false;
            } else {
                oldWay = theWay;
                theWay = choosenOne.getSource();
            }
        }
        return choosenOne;
    }

    public void eozighz() {
        getAllWays();
        for (Chemin chemin : chemins) {

        }
    }

    /*  public double getMinDistance(Rectangle rectangle){
          double minDistance = 99999;
          Chemin result = new Chemin();
          for (Line line : rectangle.getRectanglesId()) {
              Rectangle idRect = getRectangle(line.getGoToId());
              if (idRect != null) {
                  Point startPoint = getLinePoint(rectangle.getRectangle(), line.getDirection_first());
                  Point stopPoint = getLinePoint(idRect.getRectangle(), line.getDirection_second());
                  if( line.distance(startPoint,stopPoint) < minDistance){
                      minDistance = line.distance(startPoint,stopPoint);
                      result = new Chemin(rectangle,idRect,minDistance);
                  }
              }
          }
      }*/
    public Point getLinePoint(RectF rectangle, String direction) {
        Point result = null;
        switch (direction) {
            case Rectangle.LEFT:
                result = new Point((int) rectangle.left, (int) (rectangle.top + rectangle.bottom) / 2);
                break;
            case Rectangle.RIGHT:
                result = new Point((int) rectangle.right, (int) (rectangle.top + rectangle.bottom) / 2);
                break;
            case Rectangle.TOP:
                result = new Point((int) (rectangle.left + rectangle.right) / 2, (int) rectangle.top);
                break;
            case Rectangle.BOTTOM:
                result = new Point((int) (rectangle.left + rectangle.right) / 2, (int) rectangle.bottom);
                break;
        }
        return result;
    }

    /* public Chemin lePlusCourtChemin(){
         //calculer();
         int min = 99999;
         Chemin result =null;
             for (Chemin chemin : chemins) {
                 if(containsStart(chemin)){
                     if(chemin.getCount()<min){
                         min = chemin.getCount();
                         result = chemin;
                     }
                 }
             }
         return result;
     }
    public Chemin getMinCount(List<Chemin> chemins, Rectangle theway) {
        int min = 99999;
        Chemin result = null;
        for (Chemin chemin : chemins) {
            if (!chemin.getChemins().contains(theway)) {
                if (chemin.getCount() < min) {
                    min = chemin.getCount();
                    result = chemin;
                }
            }
        }
        return result;
    }
*/
    public void oezhogiezgh() {

        boolean turn = true;
        Rectangle rectangle = start;
        while (turn) {
            if (rectangle.getRectanglesId().contains(end)) {
                turn = false;
            } else {
                for (Line line : rectangle.getRectanglesId()) {
                    rectangle = getRectangle(line.getGoToId());
                    if (rectangle.getRectanglesId().contains(end)) {
                        turn = false;
                    }
                }
            }
        }
    }
    public void getAllWays(Rectangle rectangle) {
        Log.d(Global.TAG, "oezhogieezfzgh: " + rectangle.getName());
        closeSet.add(rectangle.getUID());
        result.add(rectangle);
            if (contains(rectangle,end.getUID())) {
                result.add(end);
                sauvgarde();
                result = new ArrayList<>();
            } else {
                for (Line line : rectangle.getRectanglesId()) {
                    if(!closeSet.contains(line.getGoToId()))
                        getAllWays(getRectangle(line.getGoToId()));
                }
            }

    }

    private void sauvgarde() {
        chemins.add(new Chemin(start,0,result));
    }

    private boolean contains(Rectangle rect,String UID){
        for (Line line : rect.getRectanglesId()) {
            if(line.getGoToId().equals(UID)){
                return true;
            }
        }
        return false;
    }
    public boolean containsStart(Chemin chemin) {
        if (chemin.getSource().equals(start))
            return true;
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        String s = "\n";
        for (Chemin chemin : chemins) {
            s += "Count : " + chemin.getDistance() + " - ";
            s += "Source : " + chemin.getSource().getName() + " --> ";
            for (Rectangle rectangle : chemin.getChemins()) {
                s += rectangle.getName() + " - ";
            }
            s += "\n";
        }
        return s;
    }

    public String toString(List<Chemin> result) {
        String s = "\n";
        for (Chemin chemin : result) {
            s += "Count : " + chemin.getDistance() + " - ";
            s += "Source : " + chemin.getSource().getName() + " --> ";
            for (Rectangle rectangle : chemin.getChemins()) {
                s += rectangle.getName() + " - ";
            }
            s += "\n";
        }
        return s;
    }
}
