package tarek.android.toumalos.deadhalvr3.Models;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import tarek.android.toumalos.deadhalvr3.Const.Global;

public class Maze implements Serializable {
    private String uid;
    private String code;
    private String editCode;
    private String status;
    private String name;
    private String userId;
    private boolean onLine;
    private List<RectangleParser> rectangles;
    private float mooveX;
    private float mooveY;

    public Maze() {
        rectangles = new ArrayList<>();
    }

    public Maze(String userId, String code, String editCode, String name, boolean onLine) {
        UUID uuid = UUID.randomUUID();
        this.uid = uuid.toString();
        this.userId = userId;
        this.code = code;
        this.name = name;
        this.editCode = editCode;
        this.onLine = onLine;
        this.rectangles = new ArrayList<>();
        this.status = Global.ADMIN;
    }

    public String getUid() {
        return uid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getEditCode() {
        return editCode;
    }

    public void setEditCode(String editCode) {
        this.editCode = editCode;
    }

    public boolean isOnLine() {
        return onLine;
    }

    public void setOnLine(boolean onLine) {
        this.onLine = onLine;
    }

    public void setRectangles(List<RectangleParser> rectangles) {
        this.rectangles = rectangles;
    }

    public float getMooveX() {
        return mooveX;
    }

    public void setMooveX(float mooveX) {
        this.mooveX = mooveX;
    }

    public float getMooveY() {
        return mooveY;
    }

    public void setMooveY(float mooveY) {
        this.mooveY = mooveY;
    }

    @NonNull
    @Override
    public String toString() {
        return "Maze: " + name + " rectangle size : " + rectangles.size();
    }
}
