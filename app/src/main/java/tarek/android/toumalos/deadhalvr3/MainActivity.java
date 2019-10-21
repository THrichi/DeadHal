package tarek.android.toumalos.deadhalvr3;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import tarek.android.toumalos.deadhalvr3.Adapter.LineAdapter;
import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Draw.Drawing;
import tarek.android.toumalos.deadhalvr3.Models.Line;
import tarek.android.toumalos.deadhalvr3.Models.LineItem;
import tarek.android.toumalos.deadhalvr3.Models.Maze;
import tarek.android.toumalos.deadhalvr3.Models.Rectangle;
import tarek.android.toumalos.deadhalvr3.Models.RectangleParser;

public class MainActivity extends AppCompatActivity {

    private Drawing drawing;
    private CircleImageView add, move, resize, remove, backToMenu, cancel, valideAdd, cancelRotate, cancelAdd, drow, options, save, back, relation, selected, rotate, changeInteret, changeName;
    private EditText rectW, rectH, rectName, changeNameEdit, changeInteretEdit;
    private TextView rotationEditText;
    private SeekBar seekZoom, seekY, seekX, seekRotation;
    private LinearLayout buttonsLayout, seekLayout, addingLayout, buttonsPrincipaleLayout, RotationLayout, nametLayout, interetLayout;
    private Maze theMaze;
    private int left = 0;
    private int top = 0;
    private Context context;
    private int oldYprogress = -1;
    private int oldXprogress = -1;
    private String mazeName;
    private Dialog linesDialog;
    //RecycleView
    private RecyclerView lineRecycleView;
    private LineAdapter lineAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<LineItem> lineItems;
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
        relation = (CircleImageView) findViewById(R.id.relation);
        rotate = (CircleImageView) findViewById(R.id.rotate);
        changeName = (CircleImageView) findViewById(R.id.changeName);
        changeInteret = (CircleImageView) findViewById(R.id.changeInteret);
        selected = (CircleImageView) findViewById(R.id.selected);
        backToMenu = (CircleImageView) findViewById(R.id.backToMenu);
        drow = (CircleImageView) findViewById(R.id.Drow);
        options = (CircleImageView) findViewById(R.id.Options);
        back = (CircleImageView) findViewById(R.id.Back);
        save = (CircleImageView) findViewById(R.id.Save);
        cancel = (CircleImageView) findViewById(R.id.cancel);
        cancelAdd = (CircleImageView) findViewById(R.id.cancelAdd);
        valideAdd = (CircleImageView) findViewById(R.id.valideAdd);
        cancelRotate = (CircleImageView) findViewById(R.id.cancelRotate);
        seekZoom = (SeekBar) findViewById(R.id.SeekZoom);
        seekY = (SeekBar) findViewById(R.id.SeekX);
        seekX = (SeekBar) findViewById(R.id.SeekY);
        seekRotation = (SeekBar) findViewById(R.id.SeekRotation);
        buttonsLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
        seekLayout = (LinearLayout) findViewById(R.id.SeekLayout);
        addingLayout = (LinearLayout) findViewById(R.id.addingLayout);
        nametLayout = (LinearLayout) findViewById(R.id.NametLayout);
        interetLayout = (LinearLayout) findViewById(R.id.InteretLayout);
        buttonsPrincipaleLayout = (LinearLayout) findViewById(R.id.buttonsPrincipaleLayout);
        RotationLayout = (LinearLayout) findViewById(R.id.RotationLayout);
        rectH = (EditText) findViewById(R.id.rectH);
        rectW = (EditText) findViewById(R.id.rectW);
        rectName = (EditText) findViewById(R.id.rectName);
        changeNameEdit = (EditText) findViewById(R.id.changeNameEdit);
        changeInteretEdit = (EditText) findViewById(R.id.changeInteretEdit);
        rotationEditText = (TextView) findViewById(R.id.rotationEditText);

        registerForContextMenu(drawing);

        seekZoom.setMax(20);
        seekZoom.setProgress(10);
        seekY.setProgress(0);
        seekX.setProgress(0);
        seekRotation.setProgress(0);
        seekRotation.setMax(360);

        drow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.setVisibility(View.VISIBLE);
                buttonsPrincipaleLayout.setVisibility(View.GONE);
                RotationLayout.setVisibility(View.GONE);
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
                drawing.back();
                Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });

        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.calculerLeChemin();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsPrincipaleLayout.setVisibility(View.GONE);
                buttonsLayout.setVisibility(View.GONE);
                RotationLayout.setVisibility(View.GONE);
                seekLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.VISIBLE);
                buttonColors(Global.ADD);
            }
        });
        relation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.setMode(Global.RELATION);
                buttonColors(Global.RELATION);
                drawing.postInvalidate();
            }
        });
        rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawing.getSelectedRectangle() != null) {
                    drawing.setMode(Global.ROTATE);
                    buttonColors(Global.ROTATE);

                    buttonsLayout.setVisibility(View.GONE);
                    RotationLayout.setVisibility(View.VISIBLE);
                    seekLayout.setVisibility(View.GONE);
                    buttonsPrincipaleLayout.setVisibility(View.GONE);
                    addingLayout.setVisibility(View.GONE);
                    drawing.setMode(Global.NOTHING);
                } else {
                    Toast.makeText(context, "Aucun élément n'a été sélectionné", Toast.LENGTH_SHORT).show();
                }

            }
        });
        selected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.setMode(Global.SELECTED);
                buttonColors(Global.SELECTED);
            }
        });
        cancelAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.setVisibility(View.VISIBLE);
                buttonsPrincipaleLayout.setVisibility(View.GONE);
                RotationLayout.setVisibility(View.GONE);
                seekLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
                drawing.setMode(Global.NOTHING);
            }
        });
        cancelRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.setVisibility(View.VISIBLE);
                buttonsPrincipaleLayout.setVisibility(View.GONE);
                RotationLayout.setVisibility(View.GONE);
                seekLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
                drawing.setMode(Global.NOTHING);
            }
        });
        valideAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.setMode(Global.ADD);
                RectangleParser parser = new RectangleParser(0, 0,
                        Float.parseFloat(rectW.getText().toString()),
                        Float.parseFloat(rectH.getText().toString()),
                        rectName.getText().toString());
                theMaze.getRectangles().add(parser);
                drawing.setTheMaze(theMaze);

                buttonsLayout.setVisibility(View.VISIBLE);
                seekLayout.setVisibility(View.GONE);
                buttonsPrincipaleLayout.setVisibility(View.GONE);
                RotationLayout.setVisibility(View.GONE);
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
                if (drawing.isSelectedItem()) {
                    dialogConfirmation();
                } else {
                    Toast.makeText(context, "Select the item you want to delete.", Toast.LENGTH_SHORT).show();
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
                buttonColors(Global.NOTHING);

                buttonsLayout.setVisibility(View.GONE);
                buttonsPrincipaleLayout.setVisibility(View.VISIBLE);
                seekLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
                RotationLayout.setVisibility(View.GONE);
                drawing.setMode(Global.NOTHING);
                drawing.postInvalidate();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.setVisibility(View.VISIBLE);
                RotationLayout.setVisibility(View.GONE);
                seekLayout.setVisibility(View.GONE);
                buttonsPrincipaleLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
                drawing.setMode(Global.NOTHING);
            }
        });
        changeInteret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.changInteret(changeInteretEdit.getText().toString());

                buttonsLayout.setVisibility(View.VISIBLE);
                interetLayout.setVisibility(View.GONE);
                nametLayout.setVisibility(View.GONE);
            }
        });

        changeName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.changName(changeNameEdit.getText().toString());

                buttonsLayout.setVisibility(View.VISIBLE);
                interetLayout.setVisibility(View.GONE);
                nametLayout.setVisibility(View.GONE);
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
                if (oldYprogress > seekY.getProgress()) {
                    drawing.setScaleY(-100);
                } else {
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
                if (oldXprogress > seekX.getProgress()) {
                    drawing.setScaleX(-300);
                } else {
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
        seekRotation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                rotationEditText.setText(progress + "°");
                drawing.rotate(drawing.getSelectedRectangle(), progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        initDialog();
        loadMaze();
    }

    private void save(Maze theMaze) {
        drawing.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(drawing.getDrawingCache());
        drawing.setDrawingCacheEnabled(false);
        if (bitmap != null) {
            StorageReference fileRef = storageRef.child(theMaze.getCode() + ".jpg");
            fileRef.putBytes(convertBitmap(bitmap))
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        mazeBookRefDoc.set(theMaze);
    }

    private byte[] convertBitmap(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        bmp.recycle();
        return byteArray;
    }

    private void loadMaze() {
        mazeBookRefDoc.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        theMaze = documentSnapshot.toObject(Maze.class);
                        drawing.setTheMaze(theMaze);
                    }
                });
    }

    private void buttonColors(String mode) {

        add.setImageResource(R.drawable.ic_add_box_black_24dp);
        move.setImageResource(R.drawable.ic_open_with_black_24dp);
        remove.setImageResource(R.drawable.ic_delete_forever_black_42dp);
        resize.setImageResource(R.drawable.ic_settings_overscan_black_24dp);
        relation.setImageResource(R.drawable.ic_shuffle_black_24dp);
        rotate.setImageResource(R.drawable.ic_crop_rotate_black_24dp);
        selected.setImageResource(R.drawable.ic_select_app_black_24dp);
        switch (mode) {
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
            case Global.RELATION:
                relation.setImageResource(R.drawable.ic_shuffle_black_24dp_red);
                break;
            case Global.ROTATE:
                rotate.setImageResource(R.drawable.ic_crop_rotate_black_24dp_red);
                break;
            case Global.SELECTED:
                selected.setImageResource(R.drawable.ic_touch_app_black_24dp_red);
                break;
        }
    }

    public float ZoomValue(int number) {
        switch (number) {
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

    public void dialogConfirmation() {
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
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (drawing.getMode().equals(Global.SELECTED)) {
            menu.setHeaderTitle("Options");
            getMenuInflater().inflate(R.menu.popup_menu, menu);
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.changeName:
                if (drawing.getMenuItemSelected() != null) {
                    changeNameEdit.setText(drawing.getMenuItemSelected().getName());
                    nametLayout.setVisibility(View.VISIBLE);
                    buttonsLayout.setVisibility(View.GONE);
                    interetLayout.setVisibility(View.GONE);
                    addingLayout.setVisibility(View.GONE);
                    buttonsPrincipaleLayout.setVisibility(View.GONE);
                    seekLayout.setVisibility(View.GONE);
                    RotationLayout.setVisibility(View.GONE);
                } else {
                    Toast.makeText(context, "Aucun élément n'a été sélectionné", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.changeInteret:
                if (drawing.getMenuItemSelected() != null) {
                    if (drawing.getMenuItemSelected().getInteret().length() > 0)
                        changeInteretEdit.setText(drawing.getMenuItemSelected().getInteret());
                    interetLayout.setVisibility(View.VISIBLE);
                    nametLayout.setVisibility(View.GONE);
                    buttonsLayout.setVisibility(View.GONE);
                    addingLayout.setVisibility(View.GONE);
                    buttonsPrincipaleLayout.setVisibility(View.GONE);
                    seekLayout.setVisibility(View.GONE);
                    RotationLayout.setVisibility(View.GONE);
                } else {
                    Toast.makeText(context, "Aucun élément n'a été sélectionné", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.removeLine:
                if (drawing.getMenuItemSelected() != null) {
                    if (drawing.getMenuItemSelected().getRectanglesId().size() > 0) {
                        linesDialog.show();
                        setUpRecycleView(drawing.getMenuItemSelected());
                    } else {
                        Toast.makeText(context, "Aucun lien n'est liée avec cet item", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Aucun élément n'a été sélectionné", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.gotorect:
                Rectangle rectangle = drawing.getMenuItemSelected();
                if (rectangle != null) {
                    drawing.setMode(Global.MINOTORE);
                    buttonColors(Global.MINOTORE);
                    drawing.resetMonotouch();
                    drawing.setMooveX((rectangle.getRectangle().left + rectangle.getRectangle().right) / 2);
                    drawing.setMooveY((rectangle.getRectangle().top + rectangle.getRectangle().bottom) / 2);
                } else {
                    Toast.makeText(context, "Aucun élément n'a été sélectionné", Toast.LENGTH_SHORT).show();
                }
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private void initDialog() {
        linesDialog = new Dialog(context);
        linesDialog.setContentView(R.layout.activity_line_dialog);
        linesDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        lineRecycleView = (RecyclerView) linesDialog.findViewById(R.id.lineRecycleView);
    }

    private void setUpRecycleView(final Rectangle rectangle) {
        lineItems = new ArrayList<>();
        for (Line line : rectangle.getRectanglesId()) {
            Rectangle goToRect = drawing.getRectangleById(line.getGoToId());
            if(goToRect!=null){
                if(checkBidirectionnel(rectangle,line.getGoToId())){
                    lineItems.add(new LineItem("--> " + goToRect.getName(),goToRect.getUID(), R.drawable.twodirections));
                }else {
                    lineItems.add(new LineItem("--> " + goToRect.getName(),goToRect.getUID(), R.drawable.onedirection));
                }
            }
        }
        lineRecycleView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        lineAdapter = new LineAdapter(lineItems);
        lineRecycleView.setLayoutManager(layoutManager);
        lineRecycleView.setAdapter(lineAdapter);

        lineAdapter.setOnRemoveClickListener(new LineAdapter.OnRemoveClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(context,rectangle.getName() +" ,id:"+ lineItems.get(position).getUid(),Toast.LENGTH_SHORT).show();
                drawing.deleteLine(rectangle,lineItems.get(position).getUid());
                linesDialog.dismiss();
            }
        });
    }
    private boolean checkBidirectionnel(Rectangle rId,String UID){
        Rectangle rectangle = drawing.getRectangleById(UID);
        if(rectangle != null){
            for (Line line : rectangle.getRectanglesId()) {
                if(line.getGoToId().equals(rId.getUID())){
                    return true;
                }
            }
        }
        return false;
    }
    @Override
    public void onBackPressed() {

    }
}
