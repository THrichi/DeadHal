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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.PopupMenu;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Models.Line;
import tarek.android.toumalos.deadhalvr3.Models.Maze;
import tarek.android.toumalos.deadhalvr3.Models.Rectangle;
import tarek.android.toumalos.deadhalvr3.Models.RectangleParser;
import tarek.android.toumalos.deadhalvr3.R;
import tarek.android.toumalos.deadhalvr3.StreamingActivity;

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
    //dialog
    private Dialog directionDialog;
    private LinearLayout onedirection,twodirections;
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
        gestureDetector = new GestureDetector(context, this);
        scaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
        gestureDetector.setOnDoubleTapListener(this);
        initDialog();

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
            canvas.save();
            canvas.rotate(rect.getRotation(), rect.getRectangle().left, rect.getRectangle().top);
            canvas.drawRect(rect.getRectangle(), rect.getPaint());
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
            canvas.drawLine(drawingMoovingLinePoint.x, drawingMoovingLinePoint.y, drawingMoovingLineX, drawingMoovingLineY, circlePaint);
        }
        for (Rectangle rect : rectangles) {
            drawLines(canvas, rect);
        }
        canvas.restore();
        save();
    }

    private void drawArrow(float X,float Y, Canvas canvas,int color){
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(30);
        paint.setAntiAlias(true);
        paint.setColor(color);
        Path path = new Path();
        path.moveTo(0,-10);
        path.lineTo(5,0);
        path.lineTo(-5,0);
        path.close();
        path.offset(X, Y);
        canvas.drawPath(path, paint);
    }
    public void rotate(Rectangle rectangle,float rotation){
        rectangles.remove(rectangle);
        rectangle.setRotation(rotation);
        rectangles.add(rectangle);
        postInvalidate();
    }
    public Rectangle getSelectedRectangle(){
        return selectedRect;
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
                drawArrow(stopPoint.x,stopPoint.y,canvas,rectangle.getLinePaint().getColor());
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
        int nbTouch = event.getPointerCount();

        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                clickedX = (int) event.getX();
                clickedY = (int) event.getY();
                for (Rectangle rect : rectangles) {
                    rect.setColor(rect.getNormalColor());
                    if (rect.getRectangle().contains(event.getX(), event.getY())) {
                        selectedRect = rect;
                    }
                }

                if (selectedRect != null) {
                    rectangles.remove(selectedRect);
                    selectedRect.setColor(selectedRect.getSelectedColor());
                    resetColorLines();
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

                    if (selectedRect != null) {
                        if (selectedRect.getRectangle().left + 50 < selectedRect.getRectangle().right && selectedRect.getRectangle().top + 50 < selectedRect.getRectangle().bottom) {
                            rectangles.remove(selectedRect);
                            selectedRect.getRectangle().left = event.getX();
                            selectedRect.getRectangle().top = event.getY();
                            selectedRect.getInteretRectangle().offsetTo(event.getX(), event.getY());
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
                    drawingMoovingLineX = event.getX();
                    drawingMoovingLineY = event.getY();
                    for (Rectangle rect : rectangles) {
                        if (!drawingMoovingLine) {
                            if (touchedCircle(rect.getCircleLeft(), event.getX(), event.getY())) {
                                drawingMoovingLine = true;
                                drawingMoovingLinePoint = rect.getCircleLeft();
                                direction_first = Rectangle.LEFT;
                            } else if (touchedCircle(rect.getCircleRight(), event.getX(), event.getY())) {
                                drawingMoovingLine = true;
                                drawingMoovingLinePoint = rect.getCircleRight();
                                direction_first = Rectangle.RIGHT;
                            } else if (touchedCircle(rect.getCircleTop(), event.getX(), event.getY())) {
                                drawingMoovingLine = true;
                                drawingMoovingLinePoint = rect.getCircleTop();
                                direction_first = Rectangle.TOP;
                            } else if (touchedCircle(rect.getCircleBottom(), event.getX(), event.getY())) {
                                drawingMoovingLine = true;
                                drawingMoovingLinePoint = rect.getCircleBottom();
                                direction_first = Rectangle.BOTTOM;
                            }
                            drawingMoovingRect = rect;
                        }
                    }

                }
                postInvalidate();
            }
            return true;


            case MotionEvent.ACTION_UP: {
                /*if (mode.equals(Global.RELATION)) {
                    for (Rectangle rect : rectangles) {
                        if (rect != longPressRect1 && rect.getRectangle().contains(event.getX(), event.getY())) {

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
                    if (touchedCircle(rect.getCircleLeft(), event.getX(), event.getY())) {
                        touched = true;
                        rectangle = rect;
                        direction_second = Rectangle.LEFT;
                    } else if (touchedCircle(rect.getCircleRight(), event.getX(), event.getY())) {
                        touched = true;
                        rectangle = rect;
                        direction_second = Rectangle.RIGHT;
                    } else if (touchedCircle(rect.getCircleTop(), event.getX(), event.getY())) {
                        touched = true;
                        rectangle = rect;
                        direction_second = Rectangle.TOP;
                    } else if (touchedCircle(rect.getCircleBottom(), event.getX(), event.getY())) {
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

    private void resetColorLines() {
        for (Rectangle rect : rectangles) {
            rect.getLinePaint().setColor(Color.BLACK);
        }
    }

    private void setLine(final Rectangle rectangle){
        directionDialog.show();
        twodirections.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setLine(rectangle,true);
                directionDialog.dismiss();
                postInvalidate();
            }
        });
        onedirection.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setLine(rectangle,false);
                directionDialog.dismiss();
                postInvalidate();
            }
        });
    }
    private void setLine(Rectangle rect,boolean biDirection) {
            rectangles.remove(drawingMoovingRect);
            drawingMoovingRect.add(new Line(rect.getUID(), direction_first, direction_second));
            rectangles.add(drawingMoovingRect);
            if(biDirection){
                rectangles.remove(rect);
                rect.add(new Line(drawingMoovingRect.getUID(),direction_second , direction_first));
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
        double dx = Math.pow(x - point.x, 2);
        double dy = Math.pow(y - point.y, 2);

        if (dx + dy < Math.pow(20, 2)) {
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


    public void refresh() {
        if (theMaze != null) {
            this.rectangles = new ArrayList<>();
            for (RectangleParser rectangleParser : theMaze.getRectangles()) {
                this.rectangles.add(new Rectangle(rectangleParser));
            }
        }

    }

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
    public void deleteLine(Rectangle rectangle,String UID){
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

    public Maze save() {
        if (theMaze != null && rectangles != null) {
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
                        rectangle.getRectanglesId());

                rectangleParserResult.add(parser);
            }
            theMaze.setRectangles(rectangleParserResult);
        }
        if (time + 1000 <= System.currentTimeMillis()) {
            if (mazeBookRefDoc != null) {
                mazeBookRefDoc.set(theMaze);
            }
            time = System.currentTimeMillis();
        }
        return theMaze;
    }


    public void setTheMaze(Maze theMaze) {
        this.theMaze = theMaze;
        if (this.theMaze != null) {
            mazeBookRefDoc = db.collection(user.getUid()).document(theMaze.getName());
            this.rectangles = new ArrayList<>();
            for (RectangleParser parser : this.theMaze.getRectangles()) {
                this.rectangles.add(new Rectangle(parser));
            }
            postInvalidate();
        }
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
        if(mode.equals(Global.SELECTED)){
            for (Rectangle rect : rectangles) {
                if (rect.getRectangle().contains(e.getX(), e.getY())) {
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

    public void changName(String newName){
        if(menuItemSelected!=null){
            rectangles.remove(menuItemSelected);
            if(newName.equals(""))
                newName = menuItemSelected.getName();
            menuItemSelected.setName(newName);
            rectangles.add(menuItemSelected);
            menuItemSelected = null;
        }
    }
    public void changInteret(String newInteret){
        if(menuItemSelected!=null){
            rectangles.remove(menuItemSelected);
            menuItemSelected.setInteret(newInteret);
            rectangles.add(menuItemSelected);
            menuItemSelected = null;
        }
    }
    public String getRectangleName(String id){
        for (Rectangle r : rectangles) {
            if(r.getUID().equals(id))
                return r.getName();
        }
        return "";
    }
    public void removeLine(){
        if(menuItemSelected!=null){
            if(menuItemSelected.getRectanglesId().size()>0){

            }
        }
    }
    private void initDialog(){
        directionDialog = new Dialog(context);
        directionDialog.setContentView(R.layout.activity_direction_dialog);
        directionDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        twodirections = (LinearLayout) directionDialog.findViewById(R.id.twodirections);
        onedirection = (LinearLayout) directionDialog.findViewById(R.id.onedirection);
    }
    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            if (mode.equals(Global.STREAMING)) {
                scale = scale * detector.getScaleFactor();
                setScale(scale);
            }
            return true;
        }
    }
}
