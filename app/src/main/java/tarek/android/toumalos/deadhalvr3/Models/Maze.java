package tarek.android.toumalos.deadhalvr3.Models;

import android.graphics.Color;

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
    private float scale;
    private List<RectangleParser> rectangles;
    private float mooveX;
    private float mooveY;
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
        this.scale = 1f;
        this.backgroundColor = Color.WHITE;
        this.selectedColor = Color.rgb(119, 187, 255);
        this.normalColor = Color.rgb(128, 128, 192);
        this.lineDirectionColor = Color.RED;
        this.lineColor =Color.BLACK;
        this.directionColor = Color.rgb(56, 224, 254);
        this.interetColor = Color.rgb(119, 119, 0);
        this.textColor = Color.BLACK;
        this.textInteretColor = Color.BLACK;
        this.textSize = 62;
        this.textInteretSize = this.textSize/2;
        this.textStroke = 10;
        this.lineLargeur = 10;
    }

    public int getSelectedColor() {
        return selectedColor;
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

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
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
