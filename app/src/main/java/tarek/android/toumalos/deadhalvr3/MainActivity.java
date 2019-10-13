package tarek.android.toumalos.deadhalvr3;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.List;

import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Draw.Drawing;
import tarek.android.toumalos.deadhalvr3.Models.Maze;
import tarek.android.toumalos.deadhalvr3.Models.Rectangle;
import tarek.android.toumalos.deadhalvr3.Models.RectangleParser;

public class MainActivity extends AppCompatActivity {

    private Drawing drawing;
    private Button add, move, resize, remove,zoom,cancel,valideAdd,cancelAdd;
    private EditText rectW,rectH,rectName;
    private SeekBar seekZoom,seekY,seekX;
    private LinearLayout buttonsLayout,seekLayout,addingLayout;
    private Maze theMaze;
    private int left = 0;
    private int top = 0;
    private Context context;
    private int oldYprogress=-1;
    private int oldXprogress=-1;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mazebookRef = db.collection(user.getUid());
    private DocumentReference mazeBookRefDoc;
    private String mazeName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mazeName = getIntent().getStringExtra("MazeName");
        mazeBookRefDoc = mazebookRef.document(mazeName);
        drawing = (Drawing) findViewById(R.id.drawing);

        drawing.setTheMaze(theMaze);
        add = (Button) findViewById(R.id.add);
        move = (Button) findViewById(R.id.move);
        resize = (Button) findViewById(R.id.resize);
        remove = (Button) findViewById(R.id.remove);
        zoom = (Button) findViewById(R.id.zoom);
        cancel = (Button) findViewById(R.id.cancel);
        cancelAdd = (Button) findViewById(R.id.cancelAdd);
        valideAdd = (Button) findViewById(R.id.valideAdd);
        seekZoom = (SeekBar)  findViewById(R.id.SeekZoom);
        seekY = (SeekBar)  findViewById(R.id.SeekX);
        seekX = (SeekBar)  findViewById(R.id.SeekY);
        buttonsLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
        seekLayout = (LinearLayout)  findViewById(R.id.SeekLayout);
        addingLayout = (LinearLayout)  findViewById(R.id.addingLayout);

        rectH = (EditText)  findViewById(R.id.rectH);
        rectW = (EditText)  findViewById(R.id.rectW);
        rectName = (EditText)  findViewById(R.id.rectName);

        buttonsLayout.setBackgroundColor(Color.LTGRAY);
        seekZoom.setMax(20);
        seekZoom.setProgress(10);
        seekY.setProgress(0);
        seekX.setProgress(0);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*drawing.setMode(Global.ADD);
                Rectangle r = new Rectangle("7887544", 100, 200, 300, 800, Color.RED);
                rectangles.add(r);
                drawing.setRectangles(rectangles);*/
                buttonsLayout.setVisibility(View.GONE);
                seekLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.VISIBLE);
                buttonColors(Global.ADD);
            }
        });
        cancelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.setVisibility(View.VISIBLE);
                seekLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
                drawing.setMode(Global.NOTHING);
            }
        });
        valideAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.setMode(Global.ADD);
                RectangleParser parser = new RectangleParser(0,0,Float.parseFloat(rectW.getText().toString()),Float.parseFloat(rectH.getText().toString()),rectName.getText().toString());
                theMaze.getRectangles().add(parser);
                drawing.setRectangles(theMaze.getRectangles());

                buttonsLayout.setVisibility(View.VISIBLE);
                seekLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
                drawing.setMode(Global.NOTHING);
            }
        });
        move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.setMode(Global.MOOVE);
                buttonColors(Global.MOOVE);
            }
        });
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.setMode(Global.REMOVE);
                buttonColors(Global.REMOVE);
                dialogConfirmation();
            }
        });
        resize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.setMode(Global.RESIZE);
                buttonColors(Global.RESIZE);
                drawing.invalidate();
                /*Intent intent = new Intent(context , ProfilActivity.class);
                startActivity(intent);*/
            }
        });
        zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                drawing.setMode(Global.ZOOM);
                buttonColors(Global.ZOOM);
                buttonsLayout.setVisibility(View.GONE);
                seekLayout.setVisibility(View.VISIBLE);
                addingLayout.setVisibility(View.GONE);*/
                save(drawing.save());
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.setVisibility(View.VISIBLE);
                seekLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
            }
        });

        seekZoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawing.setScale(ZoomValue(seekZoom.getProgress()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(oldYprogress > seekY.getProgress()){
                    drawing.setScaleY(-100);
                }else{
                    drawing.setScaleY(+100);
                }
                oldYprogress = seekY.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        seekX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(oldXprogress > seekX.getProgress()){
                    drawing.setScaleX(-300);
                }else{
                    drawing.setScaleX(+300);
                }
                oldXprogress = seekX.getProgress();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        loadMaze();
    }
    private void save(Maze theMaze){
        mazeBookRefDoc.set(theMaze);
    }
    private void loadMaze(){
        mazeBookRefDoc.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        theMaze = documentSnapshot.toObject(Maze.class);
                        Log.d(Global.TAG, "onSuccess: " + theMaze.toString());
                        drawing.setTheMaze(theMaze);
                        drawing.setRectangles(theMaze.getRectangles());
                    }
                });
    }
    private void buttonColors(String mode){

        add.setBackgroundColor(Color.LTGRAY);
        buttonsLayout.setBackgroundColor(Color.LTGRAY);
        move.setBackgroundColor(Color.LTGRAY);
        remove.setBackgroundColor(Color.LTGRAY);
        resize.setBackgroundColor(Color.LTGRAY);
        zoom.setBackgroundColor(Color.LTGRAY);
        switch (mode)
        {
            case Global.ADD:
                add.setBackgroundColor(Color.WHITE);
                break;
            case Global.MOOVE:
                move.setBackgroundColor(Color.WHITE);
                break;
            case Global.REMOVE:
                remove.setBackgroundColor(Color.WHITE);
                break;
            case Global.RESIZE:
                resize.setBackgroundColor(Color.WHITE);
                break;
            case Global.ZOOM:
                zoom.setBackgroundColor(Color.WHITE);
                break;
        }
    }

    public float ZoomValue(int number){
        switch (number)
        {
            case 0:
                return 0.05f;
            case 1:
                return 0.1f;
            case 2:
                return 0.2f;
            case 3:
                return 0.3f;
            case 4:
                return 0.4f;
            case 5:
                return 0.5f;
            case 6:
                return 0.6f;
            case 7:
                return 0.7f;
            case 8:
                return 0.8f;
            case 9:
                return 0.9f;
            case 10:
                return 1f;
            case 11:
                return 1.1f;
            case 12:
                return 1.2f;
            case 13:
                return 1.3f;
            case 14:
                return 1.4f;
            case 15:
                return 1.5f;
            case 16:
                return 1.6f;
            case 17:
                return 1.7f;
            case 18:
                return 1.8f;
            case 19:
                return 1.9f;
            case 20:
                return 2f;
        }
        return 1f;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Rectangle", (Serializable) theMaze.getRectangles());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        theMaze.setRectangles((List<RectangleParser>) savedInstanceState.getSerializable("Rectangle"));
        drawing.setRectangles(theMaze.getRectangles());
    }

    public void dialogConfirmation(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Confirm removing selected object ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        drawing.remove();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {

    }
}
