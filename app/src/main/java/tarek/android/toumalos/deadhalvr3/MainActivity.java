package tarek.android.toumalos.deadhalvr3;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.Serializable;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Draw.Drawing;
import tarek.android.toumalos.deadhalvr3.Models.Maze;
import tarek.android.toumalos.deadhalvr3.Models.Rectangle;
import tarek.android.toumalos.deadhalvr3.Models.RectangleParser;

public class MainActivity extends AppCompatActivity {

    private Drawing drawing;
    private CircleImageView add, move, resize, remove,backToMenu,cancel,valideAdd,cancelAdd,drow,options,save,back;
    private EditText rectW,rectH,rectName;
    private SeekBar seekZoom,seekY,seekX;
    private LinearLayout buttonsLayout,seekLayout,addingLayout,buttonsPrincipaleLayout;
    private Maze theMaze;
    private int left = 0;
    private int top = 0;
    private Context context;
    private int oldYprogress=-1;
    private int oldXprogress=-1;
    private String mazeName;
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mazebookRef = db.collection(user.getUid());
    private DocumentReference mazeBookRefDoc;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mazeName = getIntent().getStringExtra("MazeName");
        mazeBookRefDoc = mazebookRef.document(mazeName);
        drawing = (Drawing) findViewById(R.id.drawing);

        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        databaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        drawing.setTheMaze(theMaze);
        add = (CircleImageView) findViewById(R.id.add);
        move = (CircleImageView) findViewById(R.id.move);
        resize = (CircleImageView) findViewById(R.id.resize);
        remove = (CircleImageView) findViewById(R.id.remove);
        backToMenu = (CircleImageView) findViewById(R.id.backToMenu);
        drow = (CircleImageView) findViewById(R.id.Drow);
        options = (CircleImageView) findViewById(R.id.Options);
        back = (CircleImageView) findViewById(R.id.Back);
        save = (CircleImageView) findViewById(R.id.Save);
        cancel = (CircleImageView) findViewById(R.id.cancel);
        cancelAdd = (CircleImageView) findViewById(R.id.cancelAdd);
        valideAdd = (CircleImageView) findViewById(R.id.valideAdd);
        seekZoom = (SeekBar)  findViewById(R.id.SeekZoom);
        seekY = (SeekBar)  findViewById(R.id.SeekX);
        seekX = (SeekBar)  findViewById(R.id.SeekY);
        buttonsLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
        seekLayout = (LinearLayout)  findViewById(R.id.SeekLayout);
        addingLayout = (LinearLayout)  findViewById(R.id.addingLayout);
        buttonsPrincipaleLayout = (LinearLayout)  findViewById(R.id.buttonsPrincipaleLayout);
        rectH = (EditText)  findViewById(R.id.rectH);
        rectW = (EditText)  findViewById(R.id.rectW);
        rectName = (EditText)  findViewById(R.id.rectName);

        seekZoom.setMax(20);
        seekZoom.setProgress(10);
        seekY.setProgress(0);
        seekX.setProgress(0);

        drow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.setVisibility(View.VISIBLE);
                buttonsPrincipaleLayout.setVisibility(View.GONE);
                seekLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
                drawing.setMode(Global.NOTHING);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(drawing.save());
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ProfilActivity.class);
                startActivity(intent);
            }
        });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*drawing.setMode(Global.ADD);
                Rectangle r = new Rectangle("7887544", 100, 200, 300, 800, Color.RED);
                rectangles.add(r);
                drawing.setRectangles(rectangles);*/
                buttonsPrincipaleLayout.setVisibility(View.GONE);
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
                buttonsPrincipaleLayout.setVisibility(View.GONE);
                seekLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
                drawing.setMode(Global.NOTHING);
            }
        });
        valideAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.setMode(Global.ADD);
                RectangleParser parser = new RectangleParser(0,0,
                        Float.parseFloat(rectW.getText().toString()),
                        Float.parseFloat(rectH.getText().toString()),
                        rectName.getText().toString());
                theMaze.getRectangles().add(parser);
                drawing.setTheMaze(theMaze);

                buttonsLayout.setVisibility(View.VISIBLE);
                seekLayout.setVisibility(View.GONE);
                buttonsPrincipaleLayout.setVisibility(View.GONE);
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
                if(drawing.isSelectedItem()){
                    dialogConfirmation();
                }else {
                    Toast.makeText(context,"Select the item you want to delete.",Toast.LENGTH_SHORT).show();
                }
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
        backToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                drawing.setMode(Global.ZOOM);
                buttonColors(Global.ZOOM);
                buttonsLayout.setVisibility(View.GONE);
                seekLayout.setVisibility(View.VISIBLE);
                addingLayout.setVisibility(View.GONE);*/

                buttonsLayout.setVisibility(View.GONE);
                buttonsPrincipaleLayout.setVisibility(View.VISIBLE);
                seekLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
                drawing.setMode(Global.NOTHING);
                drawing.postInvalidate();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.setVisibility(View.VISIBLE);
                seekLayout.setVisibility(View.GONE);
                buttonsPrincipaleLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
                drawing.setMode(Global.NOTHING);
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
    private String getFileExst(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(uri));
    }
    private void save(Maze theMaze){
        drawing.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(drawing.getDrawingCache());
        drawing.setDrawingCacheEnabled(false);
        if(bitmap!=null){
            StorageReference fileRef = storageRef.child(theMaze.getCode()+".jpg");
            fileRef.putBytes(convertBitmap(bitmap))
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context,e.getMessage(),Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        mazeBookRefDoc.set(theMaze);
    }
    private byte[] convertBitmap(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();
        return byteArray;
    }
    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

    private void loadMaze(){
        mazeBookRefDoc.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        theMaze = documentSnapshot.toObject(Maze.class);
                        drawing.setTheMaze(theMaze);
                    }
                });
    }
    private void buttonColors(String mode){

        add.setImageResource(R.drawable.ic_add_box_black_24dp);
        move.setImageResource(R.drawable.ic_open_with_black_24dp);
        remove.setImageResource(R.drawable.ic_delete_forever_black_42dp);
        resize.setImageResource(R.drawable.ic_settings_overscan_black_24dp);
        switch (mode)
        {
            case Global.ADD:
                add.setImageResource(R.drawable.ic_add_box_black_24dp_red);
                break;
            case Global.MOOVE:
                move.setImageResource(R.drawable.ic_open_with_black_24dp_red);
                break;
            case Global.REMOVE:
                remove.setImageResource(R.drawable.ic_delete_forever_black_24dp_red);
                break;
            case Global.RESIZE:
                resize.setImageResource(R.drawable.ic_settings_overscan_black_24dp_red);
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
        outState.putSerializable("Maze", (Serializable) theMaze);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        theMaze = ((Maze) savedInstanceState.getSerializable("Maze"));
        drawing.setTheMaze(theMaze);
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
