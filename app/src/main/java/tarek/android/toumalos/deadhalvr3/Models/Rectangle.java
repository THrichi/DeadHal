package tarek.android.toumalos.deadhalvr3.Models;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Rectangle implements Serializable {
    public static final String LEFT = "Left";
    public static final String RIGHT = "Right";
    public static final String TOP = "Top";
    public static final String BOTTOM = "Bottom";

    private String UID;
    private Paint paint;
    private Paint linePaint;
    private RectF rectangle;
    private RectF interetRectangle;
    private String name;
    private Point circleLeft;
    private Point circleRight;
    private Point circleTop;
    private Point circleBottom;
    private Paint textPaint;
    private Paint interetPaint;
    private Paint interetRectPaint;
    private String interet;
    private List<Line> rectanglesId;
    private float leftInteret;
    private float topInteret;
    private float rightInteret;
    private float bottomInteret;

    //options
    private int selectedColor;
    private int normalColor;
    private int backgroundColor;
    private int lineDirectionColor;
    private int lineColor;
    private int directionColor;
    private int interetColor;
    private int textColor;
    private int textInteretColor;
    private int textSize;
    private int textInteretSize;
    private int lineLargeur;
    private int textStroke;
    private int radiusX;
    private int radiusY;

    public Rectangle(RectangleParser parser) {
        this.UID = parser.getId();
        this.rectanglesId = parser.getRectanglesId();
        this.normalColor = parser.getNormalColor();
        this.directionColor = parser.getDirectionColor();
        this.selectedColor = parser.getSelectedColor();
        this.backgroundColor = parser.getBackgroundColor();
        this.lineDirectionColor = parser.getLineDirectionColor();
        this.lineColor = parser.getLineColor();
        this.interetColor = parser.getInteretColor();
        this.textColor = parser.getTextColor();
        this.textInteretColor = parser.getTextInteretColor();
        this.textSize = parser.getTextSize();
        this.textInteretSize = parser.getTextInteretSize();
        this.lineLargeur = parser.getLineLargeur();
        this.textStroke = parser.getTextStroke();
        this.radiusX = parser.getRadiusX();
        this.radiusY = parser.getRadiusY();
        paint = new Paint();
        paint.reset();
        paint.setColor(normalColor);
        paint.setStrokeWidth(7);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        rectangle = new RectF(parser.getLeft(), parser.getTop(), parser.getRight(), parser.getBottom());
        this.leftInteret = parser.getLeftInteret();
        this.topInteret = parser.getTopInteret();
        this.rightInteret = parser.getRightInteret();
        this.bottomInteret = parser.getBottomInteret();
        interetRectangle = new RectF(leftInteret, topInteret, rightInteret, bottomInteret);
        this.name = parser.getName();
        this.interet = parser.getInteret();

        textPaint = new Paint();
        textPaint.reset();
        textPaint.setColor(getTextColor());
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(getTextStroke());
        textPaint.setTextSize(getTextSize());
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setAntiAlias(true);

        interetPaint = new Paint();
        interetPaint.reset();
        interetPaint.setColor(getTextInteretColor());
        interetPaint.setTextAlign(Paint.Align.CENTER);
        interetPaint.setStrokeWidth(getTextStroke()/2);
        interetPaint.setTextSize(getTextInteretSize());
        interetPaint.setStyle(Paint.Style.FILL);
        interetPaint.setAntiAlias(true);


        interetRectPaint = new Paint();
        interetRectPaint.reset();
        interetRectPaint.setColor(getInteretColor());
        interetRectPaint.setStrokeWidth(5);
        interetRectPaint.setStyle(Paint.Style.FILL);
        interetRectPaint.setAntiAlias(true);

        linePaint = new Paint();
        linePaint.reset();
        linePaint.setColor(getLineColor());
        linePaint.setStrokeWidth(getLineLargeur());
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setAntiAlias(true);
    }

    public int getRadiusX() {
        return radiusX;
    }

    public void setRadiusX(int radiusX) {
        this.radiusX = radiusX;
    }

    public int getRadiusY() {
        return radiusY;
    }

    public void setRadiusY(int radiusY) {
        this.radiusY = radiusY;
    }

    public void setSelectedColor(int selectedColor) {
        this.selectedColor = selectedColor;
    }

    public void setNormalColor(int normalColor) {
        this.normalColor = normalColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getLineDirectionColor() {
        return lineDirectionColor;
    }

    public void setLineDirectionColor(int lineDirectionColor) {
        this.lineDirectionColor = lineDirectionColor;
    }

    public int getLineColor() {
        return lineColor;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public int getDirectionColor() {
        return directionColor;
    }

    public void setDirectionColor(int directionColor) {
        this.directionColor = directionColor;
    }

    public int getInteretColor() {
        return interetColor;
    }

    public void setInteretColor(int interetColor) {
        this.interetColor = interetColor;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getTextInteretColor() {
        return textInteretColor;
    }

    public void setTextInteretColor(int textInteretColor) {
        this.textInteretColor = textInteretColor;
    }

    public int getTextSize() {
        return textSize;
    }

    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    public int getTextInteretSize() {
        return textInteretSize;
    }

    public void setTextInteretSize(int textInteretSize) {
        this.textInteretSize = textInteretSize;
    }

    public int getLineLargeur() {
        return lineLargeur;
    }

    public void setLineLargeur(int lineLargeur) {
        this.lineLargeur = lineLargeur;
    }

    public int getTextStroke() {
        return textStroke;
    }

    public void setTextStroke(int textStroke) {
        this.textStroke = textStroke;
    }

    public Point getCircleLeft() {
        circleLeft = new Point((int) rectangle.left, (int) (rectangle.top + rectangle.bottom) / 2);
        return circleLeft;
    }

    public Point getCircleRight() {
        circleRight = new Point((int) rectangle.right, (int) (rectangle.top + rectangle.bottom) / 2);
        return circleRight;
    }

    public Point getCircleTop() {
        circleTop = new Point((int) (rectangle.left + rectangle.right) / 2, (int) rectangle.top);
        return circleTop;
    }

    public Point getCircleBottom() {
        circleBottom = new Point((int) (rectangle.left + rectangle.right) / 2, (int) rectangle.bottom);
        return circleBottom;
    }

    public float getLeftInteret() {
        return leftInteret;
    }

    public void setLeftInteret(float leftInteret) {
        this.leftInteret = leftInteret;
    }

    public void setLeft(float left) {
        this.rectangle.left = left;
    }

    public void setTop(float top) {
        this.rectangle.top = top;
    }

    public void setRight(float right) {
        this.rectangle.right = right;
    }

    public void setBottom(float bottom) {
        this.rectangle.bottom = bottom;
    }

    public float getLeft() {
        return this.rectangle.left;
    }

    public float getTop() {
        return this.rectangle.top;
    }

    public float getRight() {
        return this.rectangle.right;
    }

    public float getBottom() {
        return this.rectangle.bottom;
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

    public float getRecInteretLeft() {
        return rectangle.left;
    }

    public float getRecInteretTop() {
        return rectangle.top;
    }

    public float getRecInteretRight() {
        return (rectangle.left + (rectangle.right / 4)) * 2;
    }

    public float getRecInteretBottom() {
        return (rectangle.top + (rectangle.bottom / 6));
    }

    public void remove(String UID) {
        Line line = getLine(UID);
        if (line != null) {
            rectanglesId.remove(line);
        }
    }

    private Line getLine(String UID) {
        for (Line l : rectanglesId) {
            if (l.getGoToId().equals(UID)) {
                return l;
            }
        }
        return null;
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

    public List<Line> getRectanglesId() {
        return rectanglesId;
    }

    public void setRectanglesId(List<Line> rectanglesId) {
        this.rectanglesId = new ArrayList<>(rectanglesId);
    }

    public String getUID() {
        return UID;
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public float getTextStartX() {
        return (rectangle.left + rectangle.right) / 2;
    }

    public float getTextStartY() {
        return (rectangle.top + rectangle.bottom) / 2;
    }

    public float getInteretStartX() {
        return (interetRectangle.left + interetRectangle.right) / 2;
    }

    public float getInteretStartY() {
        return (interetRectangle.top + interetRectangle.bottom) / 2;
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

    public void add(Line line) {
        rectanglesId.add(line);
    }

    @Override
    public String toString() {
        String s;
        s = "( " + getRectangle().left + " , " + getRectangle().top + " , " + getRectangle().right + " , " + getRectangle().bottom + " )";
        return s;
    }
}
