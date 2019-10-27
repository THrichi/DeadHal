package tarek.android.toumalos.deadhalvr3.Draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import tarek.android.toumalos.deadhalvr3.Const.Global;

public class PrototypeDraw extends View {
    private int mode;
    private int color;
    private int currentRectColor;
    private int currentRectInteretColor;
    private int currentBgColor;
    private int currentTextColor;
    private int currentTextSize;
    private int currentTextStroke;
    private int currentLineStroke;
    private int currentLineColor;
    private int size;
    private int stroke;

    public PrototypeDraw(Context context) {
        super(context);
    }

    public PrototypeDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private Paint initPaint(int color, int size, int stroke) {
        Paint paint = new Paint();
        paint.reset();
        paint.setColor(color);
        paint.setTextSize(size);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(stroke);
        paint.setAntiAlias(true);
        return paint;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(currentBgColor);
        Rect rectangle = new Rect(100, 100, 670, 600);
        Rect rectangleInteret = new Rect(100, 100, 385, 200);
        switch (mode) {
            case Global.PICK_BG_CANVAS:
                canvas.drawColor(color);
                canvas.drawRect(rectangle, initPaint(currentRectColor, currentTextSize, 0));
                canvas.drawText("Test", getTextStartX(rectangle), getTextStartY(rectangle), initPaint(Color.BLACK, currentTextSize, currentTextStroke));
                break;
            case Global.PICK_RECTANGLE_DIRECTION:
                canvas.drawRect(rectangle, initPaint(color, 0, 0));
                canvas.drawText("Test", getTextStartX(rectangle), getTextStartY(rectangle), initPaint(currentTextColor, currentTextSize, currentTextStroke));
                break;
            case Global.PICK_RECTANGLE_PRINCIPALE:
                canvas.drawRect(rectangle, initPaint(color, 0, 0));
                canvas.drawText("Test", getTextStartX(rectangle), getTextStartY(rectangle), initPaint(currentTextColor, currentTextSize, currentTextStroke));
                break;
            case Global.PICK_RECTANGLE_SELECTION:
                canvas.drawRect(rectangle, initPaint(color, 0, 0));
                canvas.drawText("Test", getTextStartX(rectangle), getTextStartY(rectangle), initPaint(currentTextColor, currentTextSize, currentTextStroke));
                break;
            case Global.PICK_LINE:
                canvas.drawLine(100, 100, 670, 600, initPaint(color, size, currentLineStroke));
                drawArrow(670, 600, canvas, color);
                break;
            case Global.PICK_LINE_DIRECTION:
                canvas.drawLine(100, 100, 670, 600, initPaint(color, size, currentLineStroke));
                drawArrow(670, 600, canvas, color);
                break;
            case Global.PICK_COLOR_INTERET:
                canvas.drawRect(rectangle, initPaint(currentRectColor, currentTextSize, 0));
                canvas.drawRect(rectangleInteret, initPaint(color, currentTextSize, 0));
                canvas.drawText("Test", getTextStartX(rectangleInteret), getTextStartY(rectangleInteret), initPaint(currentTextColor, currentTextSize / 2, currentTextStroke));
                break;
            case Global.PICK_COLOR_TEXT:
                canvas.drawRect(rectangle, initPaint(currentRectColor, 0, 0));
                canvas.drawText("Test", getTextStartX(rectangle), getTextStartY(rectangle), initPaint(color, currentTextSize, currentTextStroke));
                break;
            case Global.PICK_COLOR_TEXT_INTERET:
                canvas.drawRect(rectangle, initPaint(currentRectColor, 0, 0));
                canvas.drawRect(rectangleInteret, initPaint(currentRectInteretColor, 0, 0));
                canvas.drawText("Test", getTextStartX(rectangleInteret), getTextStartY(rectangleInteret), initPaint(color, currentTextSize / 2, currentTextStroke));
                break;
            case Global.PICK_TEXT_SIZE:
                canvas.drawRect(rectangle, initPaint(currentRectColor, 0, 0));
                canvas.drawRect(rectangleInteret, initPaint(currentRectInteretColor, 0, 0));
                canvas.drawText("Test", getTextStartX(rectangleInteret), getTextStartY(rectangleInteret), initPaint(currentTextColor, size / 2, currentTextStroke));
                canvas.drawText("Test", getTextStartX(rectangle), getTextStartY(rectangle), initPaint(currentTextColor, size, currentTextStroke));
                break;
            case Global.PICK_STROKE_LINE:
                canvas.drawLine(100, 100, 670, 600, initPaint(currentLineColor, 0, stroke));
                drawArrow(670, 600, canvas, currentLineColor);
                break;

            default:
                return;
        }
    }

    public float getTextStartX(Rect rectangle) {
        return (rectangle.left + rectangle.right) / 2;
    }

    public float getTextStartY(Rect rectangle) {
        return (rectangle.top + rectangle.bottom) / 2;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
        postInvalidate();
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
        postInvalidate();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        postInvalidate();
    }

    private void drawArrow(float X, float Y, Canvas canvas, int color) {
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30);
        paint.setAntiAlias(true);
        paint.setColor(color);
        Path path = new Path();
        path.moveTo(0, -10);
        path.lineTo(5, 0);
        path.lineTo(-5, 0);
        path.close();
        path.offset(X, Y);
        canvas.drawPath(path, paint);
    }

    public int getCurrentRectColor() {
        return currentRectColor;
    }

    public void setCurrentRectColor(int currentRectColor) {
        this.currentRectColor = currentRectColor;
    }

    public int getCurrentBgColor() {
        return currentBgColor;
    }

    public void setCurrentBgColor(int currentBgColor) {
        this.currentBgColor = currentBgColor;
    }

    public int getCurrentTextColor() {
        return currentTextColor;
    }

    public void setCurrentTextColor(int currentTextColor) {
        this.currentTextColor = currentTextColor;
    }

    public int getCurrentTextSize() {
        return currentTextSize;
    }

    public void setCurrentTextSize(int currentTextSize) {
        this.currentTextSize = currentTextSize;
    }

    public int getCurrentTextStroke() {
        return currentTextStroke;
    }

    public void setCurrentTextStroke(int currentTextStroke) {
        this.currentTextStroke = currentTextStroke;
    }

    public int getCurrentLineStroke() {
        return currentLineStroke;
    }

    public void setCurrentLineStroke(int currentLineStroke) {
        this.currentLineStroke = currentLineStroke;
    }

    public int getStroke() {
        return stroke;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    public int getCurrentRectInteretColor() {
        return currentRectInteretColor;
    }

    public void setCurrentRectInteretColor(int currentRectInteretColor) {
        this.currentRectInteretColor = currentRectInteretColor;
    }

    public int getCurrentLineColor() {
        return currentLineColor;
    }

    public void setCurrentLineColor(int currentLineColor) {
        this.currentLineColor = currentLineColor;
    }

    public void setText(int parseInt) {
        switch (mode)
        {
            case Global.PICK_TEXT_SIZE:
                size = parseInt;
                break;
            case Global.PICK_STROKE_LINE:
                stroke = parseInt;
                break;

        }
        postInvalidate();
    }

}
