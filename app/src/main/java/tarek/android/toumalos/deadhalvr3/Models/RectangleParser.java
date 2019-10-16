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

    public RectangleParser() {
        this.rectanglesId = new ArrayList<>();
    }

    public RectangleParser(float left, float top, float right,float bottom, String name) {

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
