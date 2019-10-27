package tarek.android.toumalos.deadhalvr3.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RectangleParser implements Serializable {
    private String id;
    private float left;
    private float top;
    private float right;
    private float bottom;
    private float leftInteret;
    private float topInteret;
    private float rightInteret;
    private float bottomInteret;
    private String name;
    private String interet;
    private List<Line> rectanglesId;
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

    public RectangleParser() {
        this.rectanglesId = new ArrayList<>();
    }

    /*public RectangleParser(float left, float top, float right,float bottom, String name) {

        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.leftInteret = getRecInteretLeft();
        this.topInteret = getRecInteretTop();
        this.rightInteret = getRecInteretRight();
        this.bottomInteret = getRecInteretBottom();
        this.name = name;
        this.interet = "";
        this.rectanglesId = new ArrayList<>();
    }
    public RectangleParser(String id, float left, float top, float right, float bottom,float leftInteret, float topInteret, float rightInteret, float bottomInteret, String name,String interet,List<Line> rectanglesId) {

        this.id = id;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.leftInteret = leftInteret;
        this.topInteret = topInteret;
        this.rightInteret = rightInteret;
        this.bottomInteret = bottomInteret;
        this.name = name;
        this.interet = interet;
        this.rectanglesId = rectanglesId;
    }*/

    public RectangleParser(String id, float left, float top, float right, float bottom,float leftInteret, float topInteret, float rightInteret, float bottomInteret, String name,String interet,List<Line> rectanglesId ,int selectedColor, int normalColor, int backgroundColor, int lineDirectionColor, int lineColor, int directionColor, int interetColor, int textColor, int textInteretColor, int textSize, int textInteretSize, int lineLargeur, int textStroke,int radiusX,int radiusY) {
        this.id = id;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.leftInteret = leftInteret;
        this.topInteret = topInteret;
        this.rightInteret = rightInteret;
        this.bottomInteret = bottomInteret;
        this.name = name;
        this.interet = interet;
        this.rectanglesId = rectanglesId;
        this.selectedColor = selectedColor;
        this.normalColor = normalColor;
        this.backgroundColor = backgroundColor;
        this.lineDirectionColor = lineDirectionColor;
        this.lineColor = lineColor;
        this.directionColor = directionColor;
        this.interetColor = interetColor;
        this.textColor = textColor;
        this.textInteretColor = textInteretColor;
        this.textSize = textSize;
        this.textInteretSize = textInteretSize;
        this.lineLargeur = lineLargeur;
        this.textStroke = textStroke;
        this.radiusX = radiusX;
        this.radiusY = radiusY;
    }
    public RectangleParser(float left, float top, float right, float bottom , String name, int selectedColor, int normalColor, int backgroundColor, int lineDirectionColor, int lineColor, int directionColor, int interetColor, int textColor, int textInteretColor, int textSize, int textInteretSize, int lineLargeur, int textStroke) {
        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.leftInteret = getRecInteretLeft();
        this.topInteret = getRecInteretTop();
        this.rightInteret = getRecInteretRight();
        this.bottomInteret = getRecInteretBottom();
        this.name = name;
        this.interet = "";
        this.rectanglesId = new ArrayList<>();
        this.selectedColor = selectedColor;
        this.normalColor = normalColor;
        this.backgroundColor = backgroundColor;
        this.lineDirectionColor = lineDirectionColor;
        this.lineColor = lineColor;
        this.directionColor = directionColor;
        this.interetColor = interetColor;
        this.textColor = textColor;
        this.textInteretColor = textInteretColor;
        this.textSize = textSize;
        this.textInteretSize = textInteretSize;
        this.lineLargeur = lineLargeur;
        this.textStroke = textStroke;
        this.radiusX = 0;
        this.radiusY = 0;
    }
    public int getSelectedColor() {
        return selectedColor;
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

    public int getNormalColor() {
        return normalColor;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getLeft() {
        return left;
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getTop() {
        return top;
    }

    public void setTop(float top) {
        this.top = top;
    }

    public float getRight() {
        return right;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getBottom() {
        return bottom;
    }

    public void setBottom(float bottom) {
        this.bottom = bottom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInteret() {
        return interet;
    }

    public void setInteret(String interet) {
        this.interet = interet;
    }

    public List<Line> getRectanglesId() {
        return rectanglesId;
    }

    public void setRectanglesId(List<Line> rectanglesId) {
        this.rectanglesId = rectanglesId;
    }
    public void add(Line goToId){
        rectanglesId.add(goToId);
    }
    public float getRecInteretLeft(){
        return left;
    }
    public float getRecInteretTop(){
        return top;
    }
    public float getRecInteretRight(){
        return (left+ ( right/4 ))*2;
    }
    public float getRecInteretBottom(){
        return (top + ( bottom/6 ));
    }
}
