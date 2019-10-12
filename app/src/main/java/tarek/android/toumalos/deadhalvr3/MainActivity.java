package tarek.android.toumalos.deadhalvr3;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Draw.Drawing;
import tarek.android.toumalos.deadhalvr3.Models.Rectangle;

public class MainActivity extends AppCompatActivity {

    private Drawing drawing;
    private Button add, move, resize, remove,zoom,cancel;
    private SeekBar seekZoom,seekY,seekX;
    private LinearLayout buttonsLayout,seekLayout;
    private List<Rectangle> rectangles;
    private int left = 0;
    private int top = 0;
    private Context context;
    private int oldYprogress=-1;
    private int oldXprogress=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        rectangles = new ArrayList<>();
        rectangles.add(new Rectangle("7887544", left, top, 300, 300, Color.RED));

        drawing = (Drawing) findViewById(R.id.drawing);

        drawing.setRectangles(rectangles);

        add = (Button) findViewById(R.id.add);
        move = (Button) findViewById(R.id.move);
        resize = (Button) findViewById(R.id.resize);
        remove = (Button) findViewById(R.id.remove);
        zoom = (Button) findViewById(R.id.zoom);
        cancel = (Button) findViewById(R.id.cancel);
        seekZoom = (SeekBar)  findViewById(R.id.SeekZoom);
        seekY = (SeekBar)  findViewById(R.id.SeekX);
        seekX = (SeekBar)  findViewById(R.id.SeekY);
        buttonsLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
        seekLayout = (LinearLayout)  findViewById(R.id.SeekLayout);

        buttonsLayout.setBackgroundColor(Color.LTGRAY);
        seekZoom.setMax(20);
        seekZoom.setProgress(10);
        seekY.setProgress(0);
        seekX.setProgress(0);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.setMode(Global.ADD);
                Rectangle r = new Rectangle("7887544", 100, 200, 300, 800, Color.RED);
                r.setName("Flaki");
                rectangles.add(r);
                drawing.setRectangles(rectangles);
                buttonColors(Global.ADD);
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
            }
        });
        zoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.setMode(Global.ZOOM);
                buttonColors(Global.ZOOM);
                buttonsLayout.setVisibility(View.GONE);
                seekLayout.setVisibility(View.VISIBLE);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.setVisibility(View.VISIBLE);
                seekLayout.setVisibility(View.GONE);
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
        outState.putSerializable("Rectangle", (Serializable) rectangles);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        rectangles = (List<Rectangle>) savedInstanceState.getSerializable("Rectangle");
        drawing.setRectangles(rectangles);
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

}
