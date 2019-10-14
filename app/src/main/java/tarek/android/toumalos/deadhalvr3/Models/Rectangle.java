package tarek.android.toumalos.deadhalvr3.Models;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class Rectangle implements Serializable {
    private String UID;
    private Paint paint;
    private Paint linePaint;
    private RectF rectangle;
    private RectF interetRectangle;
    private int normalColor;
    private int selectedColor;
    private float rotation;
    private String name;
    //private int textSize;
    //private int textColor;
    private Paint textPaint;
    private Paint interetPaint;
    private Paint interetRectPaint;
    private String interet;
    private List<String> rectanglesId;
    private float leftInteret;
    private float topInteret;
    private float rightInteret;
    private float bottomInteret;


    /*public Rectangle(String name, int right, int bottom) {
        UUID uuid = UUID.randomUUID();
        this.UID = uuid.toString();
        this.normalColor = Color.rgb(128,128,192);
        this.selectedColor = Color.rgb(119,187,255);
        paint = new Paint();
        paint.reset();
        paint.setColor(normalColor);
        paint.setStrokeWidth(7);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(false);
        rectangle = new RectF(0,0,right,bottom);
        interetRectangle = new RectF(getRecInteretLeft(),getRecInteretTop(),getRecInteretRight(),getRecInteretBottom());

        this.name = name;
        //this.textSize = textSize;
        //this.textColor = textColor;
        this.interet ="Cuisine";

        textPaint = new Paint();
        textPaint.reset();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(10);
        textPaint.setTextSize(62);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(false);

        interetPaint = new Paint();
        interetPaint.reset();
        interetPaint.setColor(Color.BLACK);
        interetPaint.setTextAlign(Paint.Align.CENTER);
        interetPaint.setStrokeWidth(5);
        interetPaint.setTextSize(62/2);
        interetPaint.setStyle(Paint.Style.FILL);
        interetPaint.setAntiAlias(false);


        interetRectPaint = new Paint();
        interetRectPaint.reset();
        interetRectPaint.setColor(Color.rgb(119,119,0));
        interetRectPaint.setStrokeWidth(5);
        interetRectPaint.setStyle(Paint.Style.FILL);
        interetRectPaint.setAntiAlias(false);
    }*/
    public Rectangle (RectangleParser parser){

        this.UID = parser.getId();
        this.rectanglesId = parser.getRectanglesId();
        this.normalColor = Color.rgb(128,128,192);
        this.selectedColor = Color.rgb(119,187,255);
        paint = new Paint();
        paint.reset();
        paint.setColor(normalColor);
        paint.setStrokeWidth(7);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(false);
        rectangle = new RectF(parser.getLeft(),parser.getTop(),parser.getRight(),parser.getBottom());
        this.leftInteret = getRecInteretLeft();
        this.topInteret = getRecInteretTop();
        this.rightInteret = getRecInteretRight();
        this.bottomInteret = getRecInteretBottom();
        interetRectangle = new RectF(leftInteret,topInteret,rightInteret,bottomInteret);
        this.name = parser.getName();
        this.interet =parser.getInteret();
        this.interet ="Cuisine";

        textPaint = new Paint();
        textPaint.reset();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(10);
        textPaint.setTextSize(62);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(false);

        interetPaint = new Paint();
        interetPaint.reset();
        interetPaint.setColor(Color.BLACK);
        interetPaint.setTextAlign(Paint.Align.CENTER);
        interetPaint.setStrokeWidth(5);
        interetPaint.setTextSize(62/2);
        interetPaint.setStyle(Paint.Style.FILL);
        interetPaint.setAntiAlias(false);


        interetRectPaint = new Paint();
        interetRectPaint.reset();
        interetRectPaint.setColor(Color.rgb(119,119,0));
        interetRectPaint.setStrokeWidth(5);
        interetRectPaint.setStyle(Paint.Style.FILL);
        interetRectPaint.setAntiAlias(false);

        linePaint = new Paint();
        linePaint.reset();
        linePaint.setColor(Color.BLUE);
        linePaint.setStrokeWidth(15);
        linePaint.setStyle(Paint.Style.FILL);
        linePaint.setAntiAlias(false);
    }

    public float getLeftInteret() {
        return leftInteret;
    }

    public void setLeftInteret(float leftInteret) {
        this.leftInteret = leftInteret;
    }

    public float getTopInteret() {
        return topInteret;
    }

    public void setTopInteret(float topInteret) {
        this.topInteret = topInteret;
    }

    public float getRightInteret() {
        return rightInteret;
    }

    public void setRightInteret(float rightInteret) {
        this.rightInteret = rightInteret;
    }

    public float getBottomInteret() {
        return bottomInteret;
    }

    public void setBottomInteret(float bottomInteret) {
        this.bottomInteret = bottomInteret;
    }

    public Paint getLinePaint() {
        return linePaint;
    }

    public void setLinePaint(Paint linePaint) {
        this.linePaint = linePaint;
    }

    public List<String> getRectanglesId() {
        return rectanglesId;
    }

    public void setRectanglesId(List<String> rectanglesId) {
        this.rectanglesId = rectanglesId;
    }

    public String getUID() {
        return UID;
    }


    public float getRecInteretLeft(){
       return interetRectangle.left;
    }
    public float getRecInteretTop(){
        return interetRectangle.top;
    }
    public float getRecInteretRight(){
        return (interetRectangle.left+ ( interetRectangle.right/4 ))*2;
    }
    public float getRecInteretBottom(){
        return (interetRectangle.top + ( interetRectangle.bottom/6 ));
    }


    public int getNormalColor() {
        return normalColor;
    }

    public int getSelectedColor() {
        return selectedColor;
    }

    public RectF getInteretRectangle() {
        return interetRectangle;
    }

    public void setInteretRectangle(RectF interetRectangle) {
        this.interetRectangle = interetRectangle;
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

    public void setColor(int color) {
        this.paint.setColor(color);
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public float getTextStartX() {
        return  (rectangle.left + rectangle.right) / 2;
    }
    public float getTextStartY() {
        return  (rectangle.top + rectangle.bottom) / 2;
    }
    public float getInteretStartX() {
        return  (leftInteret + rightInteret) / 2;
    }
    public float getInteretStartY() {
        return  (topInteret + bottomInteret) / 2;
    }

    public Paint getTextPaint() {
        return textPaint;
    }
    public Paint getInteretPaint() {
        return interetPaint;
    }

    public void setInteretPaint(Paint interetPaint) {
        this.interetPaint = interetPaint;
    }

    public Paint getInteretRectPaint() {
        return interetRectPaint;
    }

    public void setInteretRectPaint(Paint interetRectPaint) {
        this.interetRectPaint = interetRectPaint;
    }

    public String getInteret() {
        return interet;
    }

    public void setInteret(String interet) {
        this.interet = interet;
    }


    public void setTextPaint(Paint textPaint) {
        this.textPaint = textPaint;
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
