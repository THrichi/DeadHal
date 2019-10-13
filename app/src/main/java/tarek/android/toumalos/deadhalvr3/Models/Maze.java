package tarek.android.toumalos.deadhalvr3.Models;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Maze {
    private String code;
    private String name;
    private String userId;
    private List<RectangleParser> rectangles;

    public Maze() {
        rectangles = new ArrayList<>();
    }

    public Maze(String userId,String code,String name) {
        this.userId = userId;
        this.code = code;
        this.name = name;
        this.rectangles = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<RectangleParser> getRectangles() {
        return rectangles;
    }

    public void setRectangles(List<RectangleParser> rectangles) {
        this.rectangles = rectangles;
    }

    public void addLine(String idRect1,String idRect2) {
        for (RectangleParser parser : rectangles) {
            if(parser.getId().equals(idRect1)){
                parser.getRectanglesId().add(idRect2);
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        return "Maze: " + name + " rectangle size : " + rectangles.size();
    }
}
