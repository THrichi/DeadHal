package tarek.android.toumalos.deadhalvr3.Draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.GestureDetectorCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Models.Rectangle;

public class Drawing extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    public List<Rectangle> rectangles;
    private String mode = "";
    private int moovingRight = 0, moovingBottom = 0;
    private Rectangle selectedRect;
    private int clickedX, clickedY;
    private int screenWidth, screenHeight;
    private float scale = 1;
    private float direction = 1;
    private GestureDetector gestureDetector;
    private Context context;

    public Drawing(Context context) {
        super(context);
    }

    public Drawing(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.rectangles = new ArrayList<>();
        this.context = context;
        gestureDetector = new GestureDetector(context,this);
        gestureDetector.setOnDoubleTapListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        screenWidth = canvas.getWidth();
        screenHeight = canvas.getHeight();
        canvas.save();
        canvas.scale(scale, scale);
        for (Rectangle rect : rectangles) {
            canvas.drawRect(rect.getRectangle(), rect.getPaint());
        }
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Boolean value = super.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                clickedX = (int) event.getX();
                clickedY = (int) event.getY();
                for (Rectangle rect : rectangles) {
                    rect.setColor(Color.RED);
                    if (rect.getRectangle().contains(event.getX(), event.getY())) {
                        selectedRect = rect;
                    }
                }
                if(selectedRect!=null){
                    rectangles.remove(selectedRect);
                    selectedRect.setColor(Color.YELLOW);
                    rectangles.add(selectedRect);
                }

                if (mode.equals(Global.MOOVE)) {

                }
                postInvalidate();
            }
            return true;


            case MotionEvent.ACTION_MOVE: {
                if (mode.equals(Global.MOOVE)) {
                    if (selectedRect != null) {
                        Log.d(Global.TAG,  selectedRect.getRectangle().bottom + "<" + screenHeight + " ########## " + screenWidth + "<" + selectedRect.getRectangle().right);
                        if (selectedRect.getRectangle().right <= screenWidth && selectedRect.getRectangle().bottom <= screenHeight ) {
                        rectangles.remove(selectedRect);
                        selectedRect.getRectangle().offsetTo(event.getX(), event.getY());
                        rectangles.add(selectedRect);
                        }
                    }
                }else if (mode.equals(Global.ADD)) {
                    if (selectedRect != null) {
                        Log.d(Global.TAG,  selectedRect.getRectangle().bottom + "<" + screenHeight + " ########## " + screenWidth + "<" + selectedRect.getRectangle().right);
                        if (selectedRect.getRectangle().right <= screenWidth && selectedRect.getRectangle().bottom <= screenHeight ) {
                            rectangles.remove(selectedRect);
                            selectedRect.getRectangle().offsetTo(event.getX(), event.getY());
                            rectangles.add(selectedRect);
                        }
                    }
                }
                postInvalidate();
            }
            return true;


            case MotionEvent.ACTION_UP: {
                if (mode.equals(Global.MOOVE)) {

                }
            }
            return true;
        }
        return value;
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        boolean value =  super.onDragEvent(event);
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_ENDED: {

            }
            return true;
        }
        return  value;
    }



    public void addRectangle(Rectangle rectangle) {
        rectangles.add(rectangle);
        postInvalidate();
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public void setRectangles(List<Rectangle> rectangles) {
        this.rectangles = rectangles;
        postInvalidate();
    }

    public void setScale(float scale) {
        this.scale = scale;
        postInvalidate();
    }
    public void remove(){
        if(selectedRect!=null){
            rectangles.remove(selectedRect);
            selectedRect = null;
            postInvalidate();
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        Toast.makeText(context,"onSingleTapConfirmed",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Toast.makeText(context,"onDoubleTap",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Toast.makeText(context,"onDoubleTapEvent",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        Toast.makeText(context,"onDown",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Toast.makeText(context,"onShowPress",Toast.LENGTH_SHORT).show();

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Toast.makeText(context,"onSingleTapUp",Toast.LENGTH_SHORT).show();
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Toast.makeText(context,"onLongPress",Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Toast.makeText(context,"onFling",Toast.LENGTH_SHORT).show();
        return true;
    }
}
