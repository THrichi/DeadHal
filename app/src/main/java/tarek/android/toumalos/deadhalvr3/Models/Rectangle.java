package tarek.android.toumalos.deadhalvr3.Models;

import android.graphics.Paint;
import android.graphics.RectF;

import java.io.Serializable;

public class Rectangle implements Serializable {
    private String UID;
    private Paint paint;
    private RectF rectangle;
    private int color;

    public Rectangle(String UID, int left, int top, int right, int bottom, int color) {
        this.UID = UID;
        paint = new Paint();
        paint.reset();
        paint.setColor(color);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(false);
        rectangle = new RectF(left,top,right,bottom);
    }
    public Paint getPaint() {
        return paint;
    }

    public void setPaint(Paint paint) {
        this.paint = paint;
    }

    public RectF getRectangle() {
        return rectangle;
    }

    public void setRectangle(RectF rectangle) {
        this.rectangle = rectangle;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        this.paint.setColor(color);
    }

    public String UID() {
        return UID;
    }

    @Override
    public String toString() {
        String s;
        s = "( "+getRectangle().left+" , " + getRectangle().top +" , " + getRectangle().right + " , " + getRectangle().bottom + " )";
        return s;
    }
}
