package tarek.android.toumalos.deadhalvr3.Draw;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Models.Astart;
import tarek.android.toumalos.deadhalvr3.Models.Chemin;
import tarek.android.toumalos.deadhalvr3.Models.Line;
import tarek.android.toumalos.deadhalvr3.Models.Maze;
import tarek.android.toumalos.deadhalvr3.Models.Rectangle;
import tarek.android.toumalos.deadhalvr3.Models.RectangleParser;
import tarek.android.toumalos.deadhalvr3.R;

public class Drawing extends View implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    public Maze theMaze;
    public List<Rectangle> rectangles;
    private String mode = "";
    private float mooveX, mooveY;
    private Rectangle selectedRect;
    private int screenWidth, screenHeight;
    private float scale = 1;
    private float scaleY = 1;
    private float scaleX = 1;
    private float oldX = 0;
    private float oldY = 0;
    private Rectangle longPressRect1;
    private boolean movingScalY = false, movingScalX = false;
    private GestureDetector gestureDetector;
    private ScaleGestureDetector scaleGestureDetector;
    private Context context;
    private double time = System.currentTimeMillis();
    private Boolean drawingMoovingLine = false;
    private float drawingMoovingLineX;
    private float drawingMoovingLineY;
    private Point drawingMoovingLinePoint;
    private Paint circlePaint;
    private Rectangle drawingMoovingRect;
    private String direction_first;
    private String direction_second;
    private Rectangle menuItemSelected;
    private float previousScal = 0;
    private List<Rectangle> monoTouchList;
    private Rectangle notInTheRoad;
    private String savedName = "", savedInteret = "";
    private Astart astart;
    //dialog
    private Dialog directionDialog;
    private LinearLayout onedirection, twodirections;
    //Firebase
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mazeBookRefDoc;

    public Drawing(Context context) {
        super(context);
    }

    public Drawing(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.theMaze = new Maze();

        circlePaint = new Paint();
        circlePaint.reset();
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStrokeWidth(15);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);

        this.rectangles = new ArrayList<>();
        this.monoTouchList = new ArrayList<>();
        gestureDetector = new GestureDetector(context, this);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector.setOnDoubleTapListener(this);
        initDialog();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(theMaze.getBackgroundColor());
        //refresh();
        screenWidth = canvas.getWidth();
        screenHeight = canvas.getHeight();
        canvas.save();
        canvas.scale(scale, scale);

        for (Rectangle rect : rectangles) {
            canvas.save();
            canvas.drawRoundRect(rect.getRectangle(), rect.getRadiusX(), rect.getRadiusY(), rect.getPaint());
            canvas.drawText(rect.getName(), rect.getTextStartX(), rect.getTextStartY(), rect.getTextPaint());
            if (!rect.getInteret().equals("")) {
                canvas.drawRect(rect.getInteretRectangle(), rect.getInteretRectPaint());
                canvas.drawText(rect.getInteret(), rect.getInteretStartX(), rect.getInteretStartY(), rect.getInteretPaint());
            }
            if (mode.equals(Global.RESIZE)) {
                canvas.drawBitmap(convertBitmap(R.drawable.circle), rect.getRectangle().left - 25, rect.getRectangle().top - 25, rect.getPaint());
            }
            if (mode.equals(Global.RELATION)) {
                drawRelationSquars(canvas, rect);
            }
            canvas.restore();
        }
        if (drawingMoovingLine) {
            Paint p = new Paint();
            p.reset();
            p.setColor(theMaze.getLineColor());
            p.setStrokeWidth(theMaze.getLineLargeur());
            p.setStyle(Paint.Style.STROKE);
            p.setAntiAlias(true);
            canvas.drawLine(drawingMoovingLinePoint.x, drawingMoovingLinePoint.y, drawingMoovingLineX, drawingMoovingLineY, p);
        }
        for (Rectangle rect : rectangles) {
            drawLines(canvas, rect);
        }
        if (mode.equals(Global.STREAMING) && theMaze != null) {

            if (theMaze.getMooveX() != 0 && theMaze.getMooveY() != 0) {
                drawFigurine(canvas, theMaze.getMooveX(), theMaze.getMooveY());
                drawXrect(canvas, getEmptyRect());
                if (!checkIfTouch()) {
                    reset();
                    notInTheRoad = null;
                }
            }

            menuItemSelected = getSelectedRectangle(theMaze.getMooveX(), theMaze.getMooveY());
            if (menuItemSelected != null) {
                addMonoTouchRect();
                colorMonoTouch();
                for (Rectangle rect : rectangles) {
                    if (rect.getRectangle().contains(theMaze.getMooveX(), theMaze.getMooveY())) {
                        if (contains(menuItemSelected, rect.getUID())) {
                            menuItemSelected = rect;
                            colorSelected(rect);
                        } else {
                            if (!rect.equals(menuItemSelected)) {
                                notInTheRoad = rect;
                                removeMonoTouchRect(rect);
                            }
                        }
                    }
                }
            }
        }

        if (mode.equals(Global.MINOTORE)) {
            drawFigurine(canvas, mooveX, mooveY);
            if (notInTheRoad != null) {
                drawXrect(canvas, notInTheRoad);
            }
            if (!checkIfTouch()) {
                reset();
                notInTheRoad = null;
            }
        }
        canvas.restore();
        save();
    }

    private Rectangle getEmptyRect() {
        for (Rectangle rect : rectangles) {
            if (rect.getName().equals("")) {
                return rect;
            }
        }
        return null;
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


    public Rectangle getSelectedRectangle() {
        return selectedRect;
    }

    public Rectangle getSelectedRectangle(float X, float Y) {
        for (Rectangle rect : rectangles) {
            if (rect.getRectangle().contains(X, Y))
                return rect;
        }
        return null;
    }

    private void drawRelationSquars(Canvas canvas, Rectangle rectangle) {
        canvas.drawCircle(rectangle.getCircleLeft().x, rectangle.getCircleLeft().y, 10, circlePaint);
        canvas.drawCircle(rectangle.getCircleRight().x, rectangle.getCircleRight().y, 10, circlePaint);
        canvas.drawCircle(rectangle.getCircleTop().x, rectangle.getCircleTop().y, 10, circlePaint);
        canvas.drawCircle(rectangle.getCircleBottom().x, rectangle.getCircleBottom().y, 10, circlePaint);
    }

    private Bitmap convertBitmap(int image) {
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),
                image);
        return icon;
    }

    private void drawLines(Canvas canvas, Rectangle rectangle) {
        for (Line line : rectangle.getRectanglesId()) {
            Rectangle idRect = getRectangleById(line.getGoToId());
            if (idRect != null) {
                Point startPoint = getLinePoint(rectangle.getRectangle(), line.getDirection_first());
                Point stopPoint = getLinePoint(idRect.getRectangle(), line.getDirection_second());
                canvas.drawLine(startPoint.x, startPoint.y, stopPoint.x, stopPoint.y, rectangle.getLinePaint());
                drawArrow(stopPoint.x, stopPoint.y, canvas, rectangle.getLinePaint().getColor());
            }
        }
    }

    public Point getLinePoint(RectF rectangle, String direction) {
        Point result = null;
        switch (direction) {
            case Rectangle.LEFT:
                result = new Point((int) rectangle.left, (int) (rectangle.top + rectangle.bottom) / 2);
                break;
            case Rectangle.RIGHT:
                result = new Point((int) rectangle.right, (int) (rectangle.top + rectangle.bottom) / 2);
                break;
            case Rectangle.TOP:
                result = new Point((int) (rectangle.left + rectangle.right) / 2, (int) rectangle.top);
                break;
            case Rectangle.BOTTOM:
                result = new Point((int) (rectangle.left + rectangle.right) / 2, (int) rectangle.bottom);
                break;
        }
        return result;
    }

    public float getMooveX() {
        return mooveX;
    }

    public void setMooveX(float mooveX) {
        this.mooveX = mooveX;
        postInvalidate();
    }

    public float getMooveY() {
        return mooveY;
    }

    public void setMooveY(float mooveY) {
        this.mooveY = mooveY;
        postInvalidate();
    }

    public float getLinrY(Rectangle rectangle, String direction) {
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
        scaleGestureDetector.onTouchEvent(event);
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                if (mode != Global.MINOTORE) {

                    for (Rectangle rect : rectangles) {
                        rect.setColor(rect.getNormalColor());
                        if (rect.getRectangle().contains(event.getX() / scale, event.getY() / scale)) {
                            selectedRect = rect;
                        }
                    }

                    if (selectedRect != null) {
                        rectangles.remove(selectedRect);
                        selectedRect.setColor(selectedRect.getSelectedColor());
                        resetColorLines();
                        selectedRect.getLinePaint().setColor(theMaze.getLineDirectionColor());
                        //changesRelationColors(selectedRect);
                        rectangles.add(selectedRect);
                    }

                }
                postInvalidate();
            }
            return true;


            case MotionEvent.ACTION_MOVE: {
                mooveX = event.getX() / scale;
                mooveY = event.getY() / scale;
                if (mode.equals(Global.MOOVE)) {
                    if (selectedRect != null) {
                        rectangles.remove(selectedRect);
                        selectedRect.getRectangle().offsetTo(event.getX() / scale, event.getY() / scale);
                        selectedRect.getInteretRectangle().offsetTo(event.getX() / scale, event.getY() / scale);
                        rectangles.add(selectedRect);
                    }
                } else if (mode.equals(Global.MINOTORE)) {
                    if (menuItemSelected != null) {
                        addMonoTouchRect();
                        colorMonoTouch();
                        for (Rectangle rect : rectangles) {
                            if (rect.getRectangle().contains(event.getX() / scale, event.getY() / scale)) {
                                if (contains(menuItemSelected, rect.getUID())) {
                                    menuItemSelected = rect;
                                    colorSelected(rect);
                                    return true;
                                } else {
                                    if (!rect.equals(menuItemSelected)) {
                                        notInTheRoad = rect;
                                        removeMonoTouchRect(rect);
                                    }
                                }
                            }
                        }
                    }

                } else if (mode.equals(Global.RESIZE)) {

                    if (selectedRect != null) {
                        if (selectedRect.getRectangle().left + 50 < selectedRect.getRectangle().right && selectedRect.getRectangle().top + 50 < selectedRect.getRectangle().bottom) {
                            rectangles.remove(selectedRect);
                            selectedRect.getRectangle().left = event.getX() / scale;
                            selectedRect.getRectangle().top = event.getY() / scale;
                            selectedRect.getInteretRectangle().offsetTo(event.getX() / scale, event.getY() / scale);
                            rectangles.add(selectedRect);
                        } else {
                            selectedRect.getRectangle().left = selectedRect.getRectangle().left - 10;
                            selectedRect.getRectangle().top = selectedRect.getRectangle().top - 10;
                        }
                    }
                    /*
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
                        }*/
                } else if (mode.equals(Global.RELATION)) {
                    drawingMoovingLineX = event.getX() / scale;
                    drawingMoovingLineY = event.getY() / scale;
                    for (Rectangle rect : rectangles) {
                        if (!drawingMoovingLine) {
                            if (touchedCircle(rect.getCircleLeft(), event.getX() / scale, event.getY() / scale)) {
                                drawingMoovingLine = true;
                                drawingMoovingLinePoint = rect.getCircleLeft();
                                direction_first = Rectangle.LEFT;
                            } else if (touchedCircle(rect.getCircleRight(), event.getX() / scale, event.getY() / scale)) {
                                drawingMoovingLine = true;
                                drawingMoovingLinePoint = rect.getCircleRight();
                                direction_first = Rectangle.RIGHT;
                            } else if (touchedCircle(rect.getCircleTop(), event.getX() / scale, event.getY() / scale)) {
                                drawingMoovingLine = true;
                                drawingMoovingLinePoint = rect.getCircleTop();
                                direction_first = Rectangle.TOP;
                            } else if (touchedCircle(rect.getCircleBottom(), event.getX() / scale, event.getY() / scale)) {
                                drawingMoovingLine = true;
                                drawingMoovingLinePoint = rect.getCircleBottom();
                                direction_first = Rectangle.BOTTOM;
                            }
                            drawingMoovingRect = rect;
                        }
                    }

                } else if (mode.equals(Global.ROTATE)) {
                    if (oldX < event.getX()) {
                        setDrawingScaleX(5f / scale);
                    } else {
                        setDrawingScaleX((-5f) / scale);
                    }

                    if (oldY < event.getY()) {
                        setDrawingScaleY(5f / scale);
                    } else {
                        setDrawingScaleY((-5f) / scale);
                    }
                    oldX = event.getX();
                    oldY = event.getY();
                }
                postInvalidate();
            }
            return true;


            case MotionEvent.ACTION_UP: {
                /*if (mode.equals(Global.RELATION)) {
                    for (Rectangle rect : rectangles) {
                        if (rect != longPressRect1 && rect.getRectangle().contains(event.getX()/scale, event.getY()/scale)) {

                            //getRectangleById(longPressRect1.getUID()).getRectanglesId().add(rect.getUID());
                            //getRectangleParserById(longPressRect1.getUID()).getRectanglesId().add(rect.getUID());
                            theMaze.addLine(longPressRect1.getUID(), new Line(rect.getUID(),longPressRect1.getRectangle().left,longPressRect1.getRectangle().top,rect.getRectangle().left,rect.getRectangle().top));
                            mode = Global.NOTHING;
                        }
                    }
                }*/

                boolean touched = false;
                Rectangle rectangle = null;
                for (Rectangle rect : rectangles) {
                    drawingMoovingLine = false;
                    if (touchedCircle(rect.getCircleLeft(), event.getX() / scale, event.getY() / scale)) {
                        touched = true;
                        rectangle = rect;
                        direction_second = Rectangle.LEFT;
                    } else if (touchedCircle(rect.getCircleRight(), event.getX() / scale, event.getY() / scale)) {
                        touched = true;
                        rectangle = rect;
                        direction_second = Rectangle.RIGHT;
                    } else if (touchedCircle(rect.getCircleTop(), event.getX() / scale, event.getY() / scale)) {
                        touched = true;
                        rectangle = rect;
                        direction_second = Rectangle.TOP;
                    } else if (touchedCircle(rect.getCircleBottom(), event.getX() / scale, event.getY() / scale)) {
                        touched = true;
                        rectangle = rect;
                        direction_second = Rectangle.BOTTOM;
                    }
                }
                if (rectangle != null && touched) {
                    //setLine(rectangle);
                    if (drawingMoovingRect != null && drawingMoovingRect != rectangle && notInRelation(drawingMoovingRect, rectangle)) {
                        setLine(rectangle);
                    }
                }
                postInvalidate();
            }
            return true;
        }
        return value;
    }

    private void colorSelected(Rectangle rect) {
        if (rect != null) {
            rectangles.remove(rect);
            resetColorLines();
            rect.getLinePaint().setColor(theMaze.getLineDirectionColor());
            //changesRelationColors(selectedRect);
            rectangles.add(rect);
        }
    }

    private void addMonoTouchRect() {
        if (!monoTouchList.contains(menuItemSelected))
            monoTouchList.add(menuItemSelected);
    }

    private void removeMonoTouchRect(Rectangle rectangle) {
        if (monoTouchList.contains(rectangle))
            monoTouchList.remove(rectangle);
    }

    private void reset() {
        if (notInTheRoad != null) {
            rectangles.remove(notInTheRoad);
            notInTheRoad.setInteret(savedInteret);
            notInTheRoad.setName(savedName);
            rectangles.add(notInTheRoad);
            savedInteret = "";
            savedInteret = "";
        }
        for (Rectangle rect : rectangles) {
            if (!monoTouchList.contains(rect)) {
                rect.getPaint().setStyle(Paint.Style.FILL);
                rect.setColor(rect.getNormalColor());
            }
        }
        postInvalidate();
    }

    public void resetMonotouch() {
        monoTouchList = new ArrayList<>();
    }

    private Boolean checkIfTouch() {
        for (Rectangle rect : rectangles) {
            if (rect.getRectangle().contains(mooveX, mooveY)) {
                return true;
            }
        }
        return false;
    }

    private void drawXrect(Canvas canvas, Rectangle rectangle) {
        if (rectangle != null) {
            Paint paint = new Paint();
            paint.reset();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(7);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            if (!rectangle.getName().equals("")) {
                savedName = rectangle.getName();
            }
            if (!rectangle.getInteret().equals("")) {
                savedInteret = rectangle.getInteret();
            }
            rectangles.remove(rectangle);
            rectangle.setName("");
            rectangle.setInteret("");
            rectangle.getPaint().setStyle(Paint.Style.STROKE);
            canvas.drawLine(rectangle.getRectangle().left, rectangle.getRectangle().top, rectangle.getRectangle().right, rectangle.getRectangle().bottom, paint);
            canvas.drawLine(rectangle.getRectangle().right, rectangle.getRectangle().top, rectangle.getRectangle().left, rectangle.getRectangle().bottom, paint);
            rectangles.add(rectangle);
            postInvalidate();
        }
    }

    private void colorMonoTouch() {
        for (Rectangle rect : monoTouchList) {
            rect.setColor(rect.getDirectionColor());
        }
        postInvalidate();
    }

    private void resetColorLines() {
        for (Rectangle rect : rectangles) {
            rect.getLinePaint().setColor(theMaze.getLineColor());
        }
    }

    private boolean contains(Rectangle rect, String UID) {
        for (Line line : rect.getRectanglesId()) {
            if (line.getGoToId().equals(UID)) {
                return true;
            }
        }
        return false;
    }

    private void setLine(final Rectangle rectangle) {
        directionDialog.show();
        twodirections.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setLine(rectangle, true);
                directionDialog.dismiss();
                postInvalidate();
            }
        });
        onedirection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setLine(rectangle, false);
                directionDialog.dismiss();
                postInvalidate();
            }
        });
    }

    private void setLine(Rectangle rect, boolean biDirection) {
        rectangles.remove(drawingMoovingRect);
        drawingMoovingRect.add(new Line(rect.getUID(), direction_first, direction_second));
        rectangles.add(drawingMoovingRect);
        if (biDirection) {
            rectangles.remove(rect);
            rect.add(new Line(drawingMoovingRect.getUID(), direction_second, direction_first));
            rectangles.add(rect);
        }
        direction_first = direction_second = "";
        drawingMoovingRect = null;
    }

    private boolean notInRelation(Rectangle drawingMoovingRect, Rectangle rect) {
        boolean result = true;
        for (Line line : drawingMoovingRect.getRectanglesId()) {
            if (line.getGoToId().equals(rect.getUID())) {
                result = false;
            }
        }

        for (Line line : rect.getRectanglesId()) {
            if (line.getGoToId().equals(drawingMoovingRect.getUID())) {
                result = false;
            }
        }

        return result;
    }

    private boolean touchedCircle(Point point, float x, float y) {
        double dx = Math.pow(x - point.x, 10);
        double dy = Math.pow(y - point.y, 10);

        if (dx + dy < Math.pow(50, 10)) {
            return true;
        }
        return false;
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
        postInvalidate();
    }


    public void setScale(float scale) {
        if (!mode.equals(Global.STREAMING))
            theMaze.setScale(scale);
        this.scale = scale;
        postInvalidate();
    }

    /*@Override
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
*/

    public boolean isSelectedItem() {
        if (selectedRect != null)
            return true;
        return false;
    }

    public Rectangle getRectangleById(String id) {
        for (Rectangle rec : rectangles) {
            if (rec.getUID().equals(id))
                return rec;
        }
        return null;
    }

    public void deleteLine(Rectangle rectangle, String UID) {
        rectangles.remove(rectangle);
        rectangle.remove(UID);
        rectangles.add(rectangle);
        postInvalidate();
    }

    public RectangleParser getRectangleParserById(String id) {
        for (RectangleParser rec : theMaze.getRectangles()) {
            if (rec.getId().equals(id))
                return rec;
        }
        return null;
    }

    public void back() {
        if (theMaze != null && mazeBookRefDoc != null) {
            this.theMaze.setOnLine(false);
            mazeBookRefDoc.set(this.theMaze);
        }
    }

    public Maze save() {
        if (theMaze != null && rectangles != null && !mode.equals(Global.STREAMING)) {
            List<RectangleParser> rectangleParserResult = new ArrayList<>();
            for (Rectangle rectangle : rectangles) {
                RectangleParser parser = new RectangleParser(rectangle.getUID(),
                        rectangle.getRectangle().left,
                        rectangle.getRectangle().top,
                        rectangle.getRectangle().right,
                        rectangle.getRectangle().bottom,
                        rectangle.getInteretRectangle().left,
                        rectangle.getInteretRectangle().top,
                        rectangle.getInteretRectangle().right,
                        rectangle.getInteretRectangle().bottom,
                        rectangle.getName(),
                        rectangle.getInteret(),
                        rectangle.getRectanglesId(),
                        rectangle.getSelectedColor(),
                        rectangle.getNormalColor(),
                        rectangle.getBackgroundColor(),
                        rectangle.getLineDirectionColor(),
                        rectangle.getLineColor(),
                        rectangle.getDirectionColor(),
                        rectangle.getInteretColor(),
                        rectangle.getTextColor(),
                        rectangle.getTextInteretColor(),
                        rectangle.getTextSize(),
                        rectangle.getTextInteretSize(),
                        rectangle.getLineLargeur(),
                        rectangle.getTextStroke(),
                        rectangle.getRadiusX(),
                        rectangle.getRadiusY()
                );

                rectangleParserResult.add(parser);
            }
            theMaze.setRectangles(rectangleParserResult);
        }
        if (time + Global.FREEZE <= System.currentTimeMillis()) {
            if (mazeBookRefDoc != null && !mode.equals(Global.STREAMING)) {
                if (mode.equals(Global.MINOTORE)) {
                    theMaze.setMooveX(mooveX);
                    theMaze.setMooveY(mooveY);
                }
                mazeBookRefDoc.set(theMaze);
            }
            time = System.currentTimeMillis();
        }
        return theMaze;
    }


    public void setTheMaze(Maze theMaze) {
        this.theMaze = theMaze;
        if (this.theMaze != null) {
            mazeBookRefDoc = db.collection("mazes").document(theMaze.getUid());
            this.theMaze.setOnLine(true);
            mazeBookRefDoc.set(this.theMaze);
            this.rectangles = new ArrayList<>();
            for (RectangleParser parser : this.theMaze.getRectangles()) {
                Rectangle tmp = new Rectangle(parser);
                tmp.setBackgroundColor(theMaze.getBackgroundColor());
                tmp.setNormalColor(theMaze.getNormalColor());
                tmp.setSelectedColor(theMaze.getSelectedColor());
                tmp.setLineDirectionColor(theMaze.getLineDirectionColor());
                tmp.setLineColor(theMaze.getLineColor());
                tmp.setDirectionColor(theMaze.getDirectionColor());
                tmp.setInteretColor(theMaze.getInteretColor());
                tmp.setTextColor(theMaze.getTextColor());
                tmp.setTextInteretColor(theMaze.getTextInteretColor());
                tmp.setTextSize(theMaze.getTextSize());
                tmp.setTextInteretSize(theMaze.getTextInteretSize());
                tmp.setTextStroke(theMaze.getTextStroke());
                tmp.setLineLargeur(theMaze.getLineLargeur());
                this.rectangles.add(tmp);
            }
            if (!mode.equals(Global.STREAMING))
                scale = this.theMaze.getScale();
            postInvalidate();
        }
    }

    public void setDrawingScaleY(float scaleY) {
        this.scaleY = scaleY;
        movingScalY = true;
        movingScalX = false;
        scale();
        postInvalidate();
    }

    public void setDrawingScaleX(float scaleX) {
        this.scaleX = scaleX;
        movingScalY = false;
        movingScalX = true;
        scale();
        postInvalidate();
    }

    public float getDrawingScaleX() {
        return this.scaleX;
    }

    public float getDrawingScaleY() {
        return this.scaleY;
    }

    public void scale() {
        List<Rectangle> rectangles = new ArrayList<>();
        for (Rectangle rect : this.rectangles) {
            if (movingScalY) {
                rect.getRectangle().top = rect.getRectangle().top + scaleY;
                rect.getInteretRectangle().top = rect.getInteretRectangle().top + scaleY;
                rect.getRectangle().bottom = rect.getRectangle().bottom + scaleY;
                rect.getInteretRectangle().bottom = rect.getInteretRectangle().bottom + scaleY;
            } else if (movingScalX) {
                rect.getRectangle().left = rect.getRectangle().left + scaleX;
                rect.getInteretRectangle().left = rect.getInteretRectangle().left + scaleX;
                rect.getRectangle().right = rect.getRectangle().right + scaleX;
                rect.getInteretRectangle().right = rect.getInteretRectangle().right + scaleX;
            }
            rectangles.add(rect);
        }
        this.rectangles = new ArrayList<>(rectangles);
    }

    public void remove() {
        if (selectedRect != null) {
            rectangles.remove(selectedRect);
            removeLines(selectedRect);
            selectedRect = null;
            postInvalidate();
        }
    }

    private void removeLines(Rectangle selectedRect) {
        for (Rectangle rect : rectangles) {
            rect.remove(selectedRect.UID());
        }
    }

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
        if (mode.equals(Global.SELECTED)) {
            for (Rectangle rect : rectangles) {
                if (rect.getRectangle().contains(e.getX() / scale, e.getY() / scale)) {
                    menuItemSelected = rect;
                }
            }
        }

    }

    public String getMode() {
        return mode;
    }

    public Rectangle getMenuItemSelected() {
        return menuItemSelected;
    }

    public void setMenuItemSelected(Rectangle menuItemSelected) {
        this.menuItemSelected = menuItemSelected;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    public void changName(String newName) {
        if (menuItemSelected != null) {
            rectangles.remove(menuItemSelected);
            if (newName.equals(""))
                newName = menuItemSelected.getName();
            menuItemSelected.setName(newName);
            rectangles.add(menuItemSelected);
            menuItemSelected = null;
        }
    }

    public void changInteret(String newInteret) {
        if (menuItemSelected != null) {
            rectangles.remove(menuItemSelected);
            menuItemSelected.setInteret(newInteret);
            rectangles.add(menuItemSelected);
            menuItemSelected = null;
        }
    }

    public String getRectangleName(String id) {
        for (Rectangle r : rectangles) {
            if (r.getUID().equals(id))
                return r.getName();
        }
        return "";
    }

    public void removeLine() {
        if (menuItemSelected != null) {
            if (menuItemSelected.getRectanglesId().size() > 0) {

            }
        }
    }

    private void initDialog() {
        directionDialog = new Dialog(context);
        directionDialog.setContentView(R.layout.activity_direction_dialog);
        directionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        twodirections = (LinearLayout) directionDialog.findViewById(R.id.twodirections);
        onedirection = (LinearLayout) directionDialog.findViewById(R.id.onedirection);
    }

    public Rectangle getRectangleByName(String name) {
        for (Rectangle rect : rectangles) {
            if (rect.getName().equals(name))
                return rect;
        }
        return null;
    }

    public void calculerLeChemin(Rectangle start) {
        Rectangle end = getRectangleByName("Toto8");
        astart = new Astart(rectangles, start, end);
        Log.d(Global.TAG, "rectangles.get(0): " + start.getName());
        Log.d(Global.TAG, "rectangles.get(2): " + end.getName());
        astart.getAllWays(start);
        Log.d(Global.TAG, "astar : " + astart.toString());
        Log.d(Global.TAG, "getMeilleurChemin : " + getMeilleurChemin().toString());

    }

    private Chemin getMeilleurChemin() {
        double min = Double.MAX_VALUE;
        Chemin resut = null;
        for (Chemin chemin : astart.getChemins()) {
            double distance = calculeDistance(chemin);
            chemin.setDistance(distance);
            if(distance<min){
                distance = min;
                resut = chemin;
            }
        }
        return resut;
    }

    private double calculeDistance(Chemin chemin) {
        double result = 0;
        List<Rectangle> tmp = chemin.getChemins();
        for (int i = 0; i < tmp.size() - 2; i++) {
            Point startPoint = getPoint(tmp.get(i));
            Point stopPoint = getPoint(tmp.get(i+1));
            result += new Line().distance(startPoint,stopPoint);
        }
        return result;
    }

    private Point getPoint(Rectangle rectangle) {
        Point point = new Point();
        point.x = (int)(rectangle.getRectangle().left + rectangle.getRectangle().right )/ 2;
        point.y = (int)(rectangle.getRectangle().top + rectangle.getRectangle().bottom )/ 2;
        return point;
    }

    private void drawFigurine(Canvas canvas, float X, float Y) {
        Bitmap b = BitmapFactory.decodeResource(getResources(), R.mipmap.minotoror);
        Paint paint = new Paint();
        paint.reset();
        paint.setAntiAlias(true);
        canvas.drawBitmap(b, X - 50, Y - 60, paint);

    }

    public boolean checkName(String name) {
        for (Rectangle rect : rectangles) {
            if (rect.getName().toLowerCase().equals(name.toLowerCase())) {
                return false;
            }
        }
        return true;
    }

    public String getGoTo(Rectangle infosRectangle) {
        String s = "";
        for (Line rect : infosRectangle.getRectanglesId()) {
            s += getRectangleById(rect.getGoToId()).getName() + "\n";
        }
        return s;
    }

    public void setRadiusX(int progress) {
        if (menuItemSelected != null) {
            rectangles.remove(menuItemSelected);
            menuItemSelected.setRadiusX(progress);
            rectangles.add(menuItemSelected);
            postInvalidate();
        }
    }

    public void setRadiusY(int progress) {
        if (menuItemSelected != null) {
            rectangles.remove(menuItemSelected);
            menuItemSelected.setRadiusY(progress);
            rectangles.add(menuItemSelected);
            postInvalidate();
        }
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale * detector.getScaleFactor();
            setScale(scale);
            return true;
        }
    }
}
