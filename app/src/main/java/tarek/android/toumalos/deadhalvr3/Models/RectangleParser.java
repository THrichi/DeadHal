package tarek.android.toumalos.deadhalvr3.Models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RectangleParser {
    private String id;
    private float left;
    private float top;
    private float right;
    private float bottom;
    private String name;
    private String interet;
    private List<String> rectanglesId;

    public RectangleParser() {
        this.rectanglesId = new ArrayList<>();
    }

    public RectangleParser(float left, float top, float right, float bottom, String name) {

        UUID uuid = UUID.randomUUID();
        this.id = uuid.toString();
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.name = name;
        this.interet = "";
        this.rectanglesId = new ArrayList<>();
    }
    public RectangleParser(String id, float left, float top, float right, float bottom, String name,List<String> rectanglesId) {

        this.id = id;
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.name = name;
        this.interet = "";
        this.rectanglesId = rectanglesId;
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

    public List<String> getRectanglesId() {
        return rectanglesId;
    }

    public void setRectanglesId(List<String> rectanglesId) {
        this.rectanglesId = rectanglesId;
    }
}
