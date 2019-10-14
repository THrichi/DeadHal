package tarek.android.toumalos.deadhalvr3.Draw;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Models.Line;
import tarek.android.toumalos.deadhalvr3.Models.Maze;
import tarek.android.toumalos.deadhalvr3.Models.Rectangle;
import tarek.android.toumalos.deadhalvr3.Models.RectangleParser;

public class Drawing extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    public Maze theMaze;
    public List<Rectangle> rectangles;
    private String mode = "";
    private int moovingRight = 0, moovingBottom = 0;
    private Rectangle selectedRect;
    private int clickedX, clickedY;
    private int screenWidth, screenHeight;
    private float scale = 1;
    private float scaleY = 0;
    private float scaleX = 0;
    private float direction = 1;
    private Rectangle longPressRect1;
    private boolean movingScalY = false, movingScalX = false;
    private GestureDetector gestureDetector;
    private Line selectedLine;
    private Context context;

    public Drawing(Context context) {
        super(context);
    }

    public Drawing(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.theMaze = new Maze();
        this.rectangles = new ArrayList<>();
        gestureDetector = new GestureDetector(context, this);
        gestureDetector.setOnDoubleTapListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //refresh();
        screenWidth = canvas.getWidth();
        screenHeight = canvas.getHeight();
        canvas.save();
        canvas.scale(scale, scale);

        for (Rectangle rect : rectangles) {
            drawLines(canvas,rect);
            if (mode.equals(Global.RELATION) && rect != longPressRect1) {
                rect.getPaint().setStyle(Paint.Style.STROKE);
            } else {
                rect.getPaint().setStyle(Paint.Style.FILL);
            }
            canvas.save();
            canvas.rotate(rect.getRotation(), rect.getRectangle().left, rect.getRectangle().top);
            canvas.drawRect(rect.getRectangle(), rect.getPaint());
            canvas.drawText(rect.getName(), rect.getTextStartX(), rect.getTextStartY(), rect.getTextPaint());
            if (!rect.getInteret().equals("")) {
                canvas.drawRect(rect.getRecInteretLeft(),
                        rect.getRecInteretTop(),
                        rect.getRecInteretRight(),
                        rect.getRecInteretBottom(),
                        rect.getInteretRectPaint());
                canvas.drawText(rect.getInteret(), rect.getInteretStartX(), rect.getInteretStartY(), rect.getInteretPaint());
            }
            canvas.restore();
        }
        canvas.restore();

    }

    private void drawLines(Canvas canvas,Rectangle rectangle) {
        for (String id : rectangle.getRectanglesId()) {
            Rectangle idRect = getRectangleById(id);
            canvas.drawLine(getStartX(rectangle), getStartY(rectangle), getStopX(idRect), getStopY(idRect), rectangle.getLinePaint());
        }
    }
    public float getStartX(Rectangle rectangle) {
        return (rectangle.getRectangle().left + rectangle.getRectangle().right) / 2;
    }
    public float getStartY(Rectangle rectangle) {
        return (rectangle.getRectangle().top + rectangle.getRectangle().bottom) / 2;
    }

    public float getStopX(Rectangle rectangle) {
        return (rectangle.getRectangle().left + rectangle.getRectangle().right) / 2;
    }

    public float getStopY(Rectangle rectangle) {
        return (rectangle.getRectangle().top + rectangle.getRectangle().bottom) / 2;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Boolean value = super.onTouchEvent(event);
        int nbTouch = event.getPointerCount();

        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                clickedX = (int) event.getX();
                clickedY = (int) event.getY();
                for (Rectangle rect : rectangles) {
                    rect.setColor(rect.getNormalColor());
                    Toast.makeText(context,rect.getName(),Toast.LENGTH_SHORT).show();
                    if (rect.getRectangle().contains(event.getX(), event.getY())) {
                        selectedRect = rect;
                    }
                }

                if (selectedRect != null) {
                    Toast.makeText(context,"selectedRect",Toast.LENGTH_SHORT).show();
                    rectangles.remove(selectedRect);
                    selectedRect.setColor(selectedRect.getSelectedColor());
                    selectedRect.getLinePaint().setColor(Color.RED);
                    //changesRelationColors(selectedRect);
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
                        rectangles.remove(selectedRect);
                        selectedRect.getRectangle().offsetTo(event.getX(), event.getY());
                        selectedRect.getInteretRectangle().offsetTo(event.getX(), event.getY());
                        rectangles.add(selectedRect);
                    }
                } else if (mode.equals(Global.ADD)) {

                } else if (mode.equals(Global.RESIZE)) {
                    if (nbTouch == 2) {
                        int X1 = (int) event.getX(0);
                        int X2 = (int) event.getX(1);
                        if (selectedRect != null) {
                            if (X1 != event.getX(0) && X2 != event.getX(1)) {
                                rectangles.remove(selectedRect);
                                if (event.getX(0) < event.getX(1)) {
                                    selectedRect.setRotation(selectedRect.getRotation() + 2);
                                } else {
                                    selectedRect.setRotation(selectedRect.getRotation() - 2);
                                }
                                rectangles.add(selectedRect);
                            }
                        }
                    }

                }
                postInvalidate();
            }
            return true;


            case MotionEvent.ACTION_UP: {
                if (mode.equals(Global.RELATION)) {
                    for (Rectangle rect : rectangles) {
                        if (rect != longPressRect1 && rect.getRectangle().contains(event.getX(), event.getY())) {
                            //getRectangleById(longPressRect1.getUID()).getRectanglesId().add(rect.getUID());
                            //getRectangleParserById(longPressRect1.getUID()).getRectanglesId().add(rect.getUID());
                            theMaze.addLine(longPressRect1.getUID(), rect.getUID());
                            mode = "";
                        }
                    }
                }
                postInvalidate();
            }
            return true;
        }
        return value;
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        boolean value = super.onDragEvent(event);
        switch (event.getAction()) {
            case DragEvent.ACTION_DRAG_ENDED: {

            }
            return true;
        }
        return value;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }


    public void setScale(float scale) {
        this.scale = scale;
        postInvalidate();
    }

    @Override
    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
        movingScalY = true;
        movingScalX = false;
        scale();
        postInvalidate();
    }

    @Override
    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
        movingScalY = false;
        movingScalX = true;
        scale();
        postInvalidate();
    }

    @Override
    public float getScaleY() {
        return scaleY;
    }

    @Override
    public float getScaleX() {
        return scaleX;
    }

    public void setRectangles(List<RectangleParser> rectangles) {
        theMaze.setRectangles(rectangles);
        for (RectangleParser parser : rectangles) {
            this.rectangles.add(new Rectangle(parser));
        }
        /*for (RectangleParser parser : rectangles) {
            for (String s : parser.getRectanglesId()) {
                lines.add(new Line(getRectangleById(parser.getId()), getRectangleById(s), Color.BLUE));
            }
        }*/

        postInvalidate();
    }
    public void refresh(){
        if(theMaze!=null){
            this.rectangles = new ArrayList<>();
            for (RectangleParser rectangleParser : theMaze.getRectangles()) {
                this.rectangles.add(new Rectangle(rectangleParser));
            }
        }

    }
    public Rectangle getRectangleById(String id) {
        for (Rectangle rec : rectangles) {
            if (rec.getUID().equals(id))
                return rec;
        }
        return null;
    }
    public RectangleParser getRectangleParserById(String id) {
        for (RectangleParser rec : theMaze.getRectangles()) {
            if (rec.getId().equals(id))
                return rec;
        }
        return null;
    }
    public Maze save() {
        List<RectangleParser> rectangleParserResult = new ArrayList<>();
        for (Rectangle rectangle : rectangles) {
            rectangleParserResult.add(new RectangleParser(rectangle.getUID(),
                    rectangle.getRectangle().left,
                    rectangle.getRectangle().top,
                    rectangle.getRectangle().right,
                    rectangle.getRectangle().bottom,
                    rectangle.getInteretRectangle().left,
                    rectangle.getInteretRectangle().top,
                    rectangle.getInteretRectangle().right,
                    rectangle.getInteretRectangle().bottom,
                    rectangle.getName(),
                    rectangle.getRectanglesId()));
        }
        theMaze.setRectangles(rectangleParserResult);
        return theMaze;
    }


    public void setTheMaze(Maze theMaze) {
        this.theMaze = theMaze;
        postInvalidate();
    }

    public void scale() {
        List<Rectangle> rectangles = new ArrayList<>();
        for (Rectangle rect : rectangles) {
            if (movingScalY) {
                rect.getRectangle().top = rect.getRectangle().top + scaleY;
                rect.getRectangle().bottom = rect.getRectangle().bottom + scaleY;
            } else if (movingScalX) {
                rect.getRectangle().left = rect.getRectangle().left + scaleX;
                rect.getRectangle().right = rect.getRectangle().right + scaleX;
            }
            rectangles.add(rect);
        }
        this.rectangles = new ArrayList<>(rectangles);
    }

    public void remove() {
        if (selectedRect != null) {
            rectangles.remove(selectedRect);
            /*if (selectedRect.getRectanglesId().size() > 0)
                removeLines(selectedRect);*/
            selectedRect = null;
            postInvalidate();
        }
    }

    /*public void changesRelationColors(Rectangle rectangle) {
        List<Line> result = new ArrayList<>();
        reset();
        for (String s : rectangle.getRectanglesId()) {
            if (line.getRectangle1().equals(rectangle)) {
                Rectangle tmp = line.getRectangle2();
                rectangles.remove(tmp);
                tmp.setColor(Color.rgb(255, 166, 255));
                rectangles.add(tmp);
                result.add(line);
            }
            if (line.getRectangle1().equals(rectangle) || line.getRectangle2().equals(rectangle)) {
                line.setColor(Color.RED);
            }
        }
    }*/

    private void reset() {
        for (Rectangle rectangle : rectangles) {
            rectangle.getLinePaint().setColor(Color.BLUE);
        }
    }


    /*public void removeLines(Rectangle rectangle) {
        List<String> result = new ArrayList<>();
        for (String id : rectangle.get) {
            if (!line.contains(rectangle)) {
                result.add(line);
            }
        }
        lines = new ArrayList<>(result);
    }*/

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        for (Rectangle rect : rectangles) {
            if (rect.getRectangle().contains(e.getX(), e.getY())) {
                mode = Global.RELATION;
                longPressRect1 = rect;
                postInvalidate();
            }
        }
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }
}
