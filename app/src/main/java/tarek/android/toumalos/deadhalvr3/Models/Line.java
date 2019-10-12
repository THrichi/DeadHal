package tarek.android.toumalos.deadhalvr3.Models;

import android.graphics.Paint;

import java.util.ArrayList;
import java.util.List;

public class Line {
    private Rectangle rectangle1;
    private Rectangle rectangle2;
    private int color;
    private Paint paint;

    public Line(Rectangle rectangle1, Rectangle rectangle2 ,int color) {
        this.color = color;
        this.rectangle1 = rectangle1;
        this.rectangle2 = rectangle2;
        paint = new Paint();
        paint.reset();
        paint.setColor(color);
        paint.setStrokeWidth(25);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(false);
    }

    public float getStartX() {
        return (rectangle1.getRectangle().left + rectangle1.getRectangle().right) / 2;
    }


    public float getStartY() {
        return (rectangle1.getRectangle().top + rectangle1.getRectangle().bottom) / 2;
    }

    public float getStopX() {
        return (rectangle2.getRectangle().left + rectangle2.getRectangle().right) / 2;
    }

    public float getStopY() {
        return (rectangle2.getRectangle().top + rectangle2.getRectangle().bottom) / 2;
    }


    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        this.paint.setColor(color);
    }

    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public boolean contains(Rectangle rectangle){
            if(rectangle.equals(rectangle1)){
                return true;
            }else if(rectangle.equals(rectangle2)){
                return true;
            }
        return false;
    }

    public Rectangle getRectangle1() {
        return rectangle1;
    }

    public void setRectangle1(Rectangle rectangle1) {
        this.rectangle1 = rectangle1;
    }

    public Rectangle getRectangle2() {
        return rectangle2;
    }

    public void setRectangle2(Rectangle rectangle2) {
        this.rectangle2 = rectangle2;
    }

    @Override
    public String toString() {
        return "Rec 1 : " + rectangle1.toString() +"\nRec 2 : " + rectangle2.toString() + "\n";
    }
}
