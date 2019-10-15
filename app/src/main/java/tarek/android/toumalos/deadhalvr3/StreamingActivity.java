package tarek.android.toumalos.deadhalvr3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Draw.Drawing;
import tarek.android.toumalos.deadhalvr3.Models.Maze;

public class StreamingActivity extends AppCompatActivity {

    private Drawing drawing;
    private Maze theMaze;
    private float scale = 1f;
    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mazebookRef;
    private DocumentReference mazeBookRefDoc;
    private ScaleGestureDetector scaleGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming);
        drawing = (Drawing) findViewById(R.id.drawing);
        theMaze = (Maze) getIntent().getSerializableExtra("theMaze");
        mazebookRef = db.collection(theMaze.getUserId());
        mazeBookRefDoc = mazebookRef.document(theMaze.getName());
        drawing.setTheMaze(theMaze);
        scaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());
    }

    @Override
    protected void onStart() {
        super.onStart();
        mazeBookRefDoc.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Maze maze = documentSnapshot.toObject(Maze.class);
                drawing.setTheMaze(maze);
            }
        });
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            Log.d(Global.TAG, "onScale: " + scale);
            scale = scale * detector.getScaleFactor();
            Log.d(Global.TAG, "onScale: " + scale);
            Log.d(Global.TAG, "detector.getScaleFactor(): " + detector.getScaleFactor());
            drawing.setScale(scale);
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }
}
