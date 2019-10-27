package tarek.android.toumalos.deadhalvr3;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import tarek.android.toumalos.deadhalvr3.Adapter.AstarAdapter;
import tarek.android.toumalos.deadhalvr3.Adapter.LineAdapter;
import tarek.android.toumalos.deadhalvr3.Adapter.OptionAdapter;
import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Draw.Drawing;
import tarek.android.toumalos.deadhalvr3.Draw.PrototypeDraw;
import tarek.android.toumalos.deadhalvr3.Models.Line;
import tarek.android.toumalos.deadhalvr3.Models.LineItem;
import tarek.android.toumalos.deadhalvr3.Models.Maze;
import tarek.android.toumalos.deadhalvr3.Models.OptionItem;
import tarek.android.toumalos.deadhalvr3.Models.Rectangle;
import tarek.android.toumalos.deadhalvr3.Models.RectangleParser;

public class MainActivity extends AppCompatActivity {

    private Drawing drawing;
    private PrototypeDraw prototypeDraw;
    private EditText changeEditText;
    private LinearLayout changeLayout, changeEditLayout;
    private CircleImageView colorPicker, changePlus, changeMoins;
    private ImageView changeOk, changeCancel;
    private CircleImageView add, move, resize, remove, backToMenu, valideAdd, cancelRotate, cancelAdd, drow, options, save, back, refresh, relation, selected, rotate, changeInteret, changeName;
    private EditText rectW, rectH, rectName, changeNameEdit, changeInteretEdit;
    private TextView rotationEditText;
    private SeekBar SeekRayonX, SeekRayonY;
    private LinearLayout buttonsLayout, addingLayout, buttonsPrincipaleLayout, RayonLayout, nametLayout, interetLayout;
    private Maze theMaze;
    private int left = 0;
    private int top = 0;
    private Context context;
    private int changeMode;
    private String mazeId;
    private Dialog linesDialog;
    private Dialog astarDialog;
    private Dialog optionsDialog;
    private Dialog changeDialog;
    private Dialog infosDialog;
    private int choosenColor;
    private EditText astarEditText;

    //

    private LinearLayout layoutInteretInfos;
    private TextView interetInfos;
    private TextView nameInfos;
    private TextView gotoInfos;
    //RecycleView
    private RecyclerView lineRecycleView;
    private LineAdapter lineAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<LineItem> lineItems;

    private RecyclerView optionRecycleView;
    private OptionAdapter optionAdapter;
    private RecyclerView.LayoutManager optionLayoutManager;
    private List<OptionItem> optionItems;


    private RecyclerView astarRecycleView;
    private AstarAdapter astarAdapter;
    private RecyclerView.LayoutManager astarLayoutManager;
    private List<String> astarItems;
    //Firestore
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DocumentReference mazeBookRefDoc;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        mazeId = getIntent().getStringExtra("MazeId");
        mazeBookRefDoc = db.collection("mazes").document(mazeId);
        drawing = (Drawing) findViewById(R.id.drawing);
        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        databaseRef = FirebaseDatabase.getInstance().getReference("uploads");
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
        refresh = (CircleImageView) findViewById(R.id.Refresh);
        save = (CircleImageView) findViewById(R.id.Save);
        cancelAdd = (CircleImageView) findViewById(R.id.cancelAdd);
        valideAdd = (CircleImageView) findViewById(R.id.valideAdd);
        cancelRotate = (CircleImageView) findViewById(R.id.cancelRotate);
        SeekRayonX = (SeekBar) findViewById(R.id.SeekRayonX);
        SeekRayonY = (SeekBar) findViewById(R.id.SeekRayonY);
        buttonsLayout = (LinearLayout) findViewById(R.id.buttonsLayout);
        addingLayout = (LinearLayout) findViewById(R.id.addingLayout);
        nametLayout = (LinearLayout) findViewById(R.id.NametLayout);
        interetLayout = (LinearLayout) findViewById(R.id.InteretLayout);
        buttonsPrincipaleLayout = (LinearLayout) findViewById(R.id.buttonsPrincipaleLayout);
        RayonLayout = (LinearLayout) findViewById(R.id.RayonLayout);
        rectH = (EditText) findViewById(R.id.rectH);
        rectW = (EditText) findViewById(R.id.rectW);
        rectName = (EditText) findViewById(R.id.rectName);
        changeNameEdit = (EditText) findViewById(R.id.changeNameEdit);
        changeInteretEdit = (EditText) findViewById(R.id.changeInteretEdit);

        registerForContextMenu(drawing);


        drow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.setVisibility(View.VISIBLE);
                buttonsPrincipaleLayout.setVisibility(View.GONE);
                RayonLayout.setVisibility(View.GONE);
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
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("MazeId", theMaze.getUid());
                startActivity(intent);
            }
        });
        options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                optionsDialog.show();
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsPrincipaleLayout.setVisibility(View.GONE);
                buttonsLayout.setVisibility(View.GONE);
                RayonLayout.setVisibility(View.GONE);
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
                /*if (drawing.getSelectedRectangle() != null) {
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
                }*/
                drawing.setMode(Global.ROTATE);
                buttonColors(Global.ROTATE);

                /*buttonsLayout.setVisibility(View.GONE);
                RotationLayout.setVisibility(View.VISIBLE);
                seekLayout.setVisibility(View.GONE);
                buttonsPrincipaleLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);*/
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
                RayonLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
                drawing.setMode(Global.NOTHING);
            }
        });
        cancelRotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonsLayout.setVisibility(View.VISIBLE);
                buttonsPrincipaleLayout.setVisibility(View.GONE);
                RayonLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
                drawing.setMode(Global.NOTHING);
            }
        });
        valideAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawing.checkName(rectName.getText().toString())) {
                    drawing.setMode(Global.ADD);
                    RectangleParser parser = new RectangleParser(0, 0,
                            Float.parseFloat(rectW.getText().toString()),
                            Float.parseFloat(rectH.getText().toString()),
                            rectName.getText().toString(),
                            theMaze.getSelectedColor(),
                            theMaze.getNormalColor(),
                            theMaze.getBackgroundColor(),
                            theMaze.getLineDirectionColor(),
                            theMaze.getLineColor(),
                            theMaze.getDirectionColor(),
                            theMaze.getInteretColor(),
                            theMaze.getTextColor(),
                            theMaze.getTextInteretColor(),
                            theMaze.getTextSize(),
                            theMaze.getTextInteretSize(),
                            theMaze.getLineLargeur(),
                            theMaze.getTextStroke());
                    theMaze.getRectangles().add(parser);
                    drawing.setTheMaze(theMaze);

                    buttonsLayout.setVisibility(View.VISIBLE);
                    buttonsPrincipaleLayout.setVisibility(View.GONE);
                    RayonLayout.setVisibility(View.GONE);
                    addingLayout.setVisibility(View.GONE);
                    drawing.setMode(Global.NOTHING);
                } else {
                    Toast.makeText(context, "nom existe déjà !", Toast.LENGTH_SHORT).show();
                }

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
                RayonLayout.setVisibility(View.GONE);
                addingLayout.setVisibility(View.GONE);
                drawing.setMode(Global.NOTHING);
                drawing.postInvalidate();
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
        SeekRayonX.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawing.setRadiusX(SeekRayonX.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SeekRayonY.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                drawing.setRadiusY(SeekRayonY.getProgress());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        SeekRayonY.setMax(200);
        SeekRayonX.setMax(200);
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
                        setUpOptionsRecycleView();
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
                    RayonLayout.setVisibility(View.GONE);
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
                    RayonLayout.setVisibility(View.GONE);
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
            case R.id.infos:
                Rectangle infosRectangle = drawing.getMenuItemSelected();
                if (infosRectangle != null) {
                    infosDialog.show();
                    nameInfos.setText(infosRectangle.getName());
                    if (infosRectangle.getInteret().equals("")) {
                        layoutInteretInfos.setVisibility(View.GONE);
                    } else {
                        layoutInteretInfos.setVisibility(View.VISIBLE);
                        interetInfos.setText(infosRectangle.getInteret());
                    }
                    gotoInfos.setText(drawing.getGoTo(infosRectangle));
                } else {
                    Toast.makeText(context, "Aucun élément n'a été sélectionné", Toast.LENGTH_SHORT).show();
                }
                return true;

            case R.id.rayon:
                Rectangle rectangleRayon = drawing.getMenuItemSelected();
                if (rectangleRayon != null) {
                    RayonLayout.setVisibility(View.VISIBLE);
                    gone();
                    SeekRayonX.setProgress(rectangleRayon.getRadiusX());
                    SeekRayonY.setProgress(rectangleRayon.getRadiusY());
                } else {
                    Toast.makeText(context, "Aucun élément n'a été sélectionné", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.manuel:
                Rectangle rectangleAuto = drawing.getMenuItemSelected();
                if (rectangleAuto != null) {
                    drawing.setMode(Global.MINOTORE);
                    buttonColors(Global.MINOTORE);
                    drawing.resetMonotouch();
                    drawing.setMooveX((rectangleAuto.getRectangle().left + rectangleAuto.getRectangle().right) / 2);
                    drawing.setMooveY((rectangleAuto.getRectangle().top + rectangleAuto.getRectangle().bottom) / 2);
                } else {
                    Toast.makeText(context, "Aucun élément n'a été sélectionné", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.auto:
                Rectangle start = drawing.getMenuItemSelected();
                if (start != null) {
                    astarDialog.show();
                    setUpAstarRecycleView(drawing.getRectangles(), start);
                } else {
                    Toast.makeText(context, "Aucun élément n'a été sélectionné", Toast.LENGTH_SHORT).show();
                }

                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    private List<Rectangle> getAstarRectangles(List<Rectangle> rectangles, String name) {
        List<Rectangle> result = new ArrayList<>();
        for (Rectangle rect : rectangles) {
            if (rect.getName().toLowerCase().contains(name.toLowerCase())) {
                result.add(rect);
            }
        }
        return result;
    }

    private void gone() {
        buttonsLayout.setVisibility(View.GONE);
        addingLayout.setVisibility(View.GONE);
        buttonsPrincipaleLayout.setVisibility(View.GONE);
        nametLayout.setVisibility(View.GONE);
        interetLayout.setVisibility(View.GONE);
    }

    private void initDialog() {
        linesDialog = new Dialog(context);
        linesDialog.setContentView(R.layout.activity_line_dialog);
        linesDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        astarDialog = new Dialog(context);
        astarDialog.setContentView(R.layout.activity_astar_dialog);
        astarDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        optionsDialog = new Dialog(context);
        optionsDialog.setContentView(R.layout.activity_options_dialog);
        optionsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


        changeDialog = new Dialog(context);
        changeDialog.setContentView(R.layout.activity_change_dialog);
        changeDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        changeDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                optionsDialog.show();
            }
        });
        infosDialog = new Dialog(context);
        infosDialog.setContentView(R.layout.activity_rect_details_dialog);
        infosDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        lineRecycleView = (RecyclerView) linesDialog.findViewById(R.id.lineRecycleView);

        layoutInteretInfos = (LinearLayout) infosDialog.findViewById(R.id.layoutInteret);
        interetInfos = (TextView) infosDialog.findViewById(R.id.interet);
        gotoInfos = (TextView) infosDialog.findViewById(R.id.gotorects);
        nameInfos = (TextView) infosDialog.findViewById(R.id.name);

        optionRecycleView = (RecyclerView) optionsDialog.findViewById(R.id.optionsRecycleView);
        astarRecycleView = (RecyclerView) astarDialog.findViewById(R.id.astarRecycleView);
        astarEditText = (EditText) astarDialog.findViewById(R.id.astarEditText);

        changeOk = (ImageView) changeDialog.findViewById(R.id.ok);
        changeCancel = (ImageView) changeDialog.findViewById(R.id.cancel);
        colorPicker = (CircleImageView) changeDialog.findViewById(R.id.colorPicker);
        prototypeDraw = (PrototypeDraw) changeDialog.findViewById(R.id.prototypeDraw);

        changePlus = (CircleImageView) changeDialog.findViewById(R.id.changePlus);
        changeMoins = (CircleImageView) changeDialog.findViewById(R.id.changeMoins);
        changeEditText = (EditText) changeDialog.findViewById(R.id.changeEditText);
        changeLayout = (LinearLayout) changeDialog.findViewById(R.id.changeLayout);
        changeEditLayout = (LinearLayout) changeDialog.findViewById(R.id.changeEditLayout);

        changeOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveMazeChanges();
            }
        });

        changeCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeDialog.dismiss();
                optionsDialog.show();
            }
        });

        colorPicker.setDrawingCacheEnabled(true);
        colorPicker.buildDrawingCache(true);

        colorPicker.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN || event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_UP) {
                    Bitmap bitmap = colorPicker.getDrawingCache();
                    try {
                        choosenColor = bitmap.getPixel((int) event.getX(), (int) event.getY());
                        prototypeDraw.setColor(choosenColor);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                return true;
            }
        });

        changePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeEditText.getText().toString().length() > 0) {
                    changeEditText.setText((Integer.parseInt(changeEditText.getText().toString()) + 1) + "");
                } else {
                    changeEditText.setText("0");
                }
            }
        });
        changeMoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (changeEditText.getText().toString().length() > 0) {
                    changeEditText.setText((Integer.parseInt(changeEditText.getText().toString()) - 1) + "");
                } else {
                    changeEditText.setText("0");
                }
            }
        });

        changeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    prototypeDraw.setText(Integer.parseInt(s.toString()));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        astarEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (drawing.getMenuItemSelected() != null)
                    setUpAstarRecycleView(getAstarRectangles(drawing.getRectangles(), s.toString()), drawing.getMenuItemSelected());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void saveMazeChanges() {
        switch (changeMode) {
            case Global.PICK_BG_CANVAS:
                theMaze.setBackgroundColor(choosenColor);
                break;
            case Global.PICK_RECTANGLE_PRINCIPALE:
                theMaze.setNormalColor(choosenColor);
                break;
            case Global.PICK_RECTANGLE_SELECTION:
                theMaze.setSelectedColor(choosenColor);
                break;
            case Global.PICK_RECTANGLE_DIRECTION:
                theMaze.setDirectionColor(choosenColor);
                break;
            case Global.PICK_LINE_DIRECTION:
                theMaze.setLineDirectionColor(choosenColor);
                break;
            case Global.PICK_LINE:
                theMaze.setLineColor(choosenColor);
                break;
            case Global.PICK_COLOR_INTERET:
                theMaze.setInteretColor(choosenColor);
                break;
            case Global.PICK_COLOR_TEXT:
                theMaze.setTextColor(choosenColor);
                break;
            case Global.PICK_COLOR_TEXT_INTERET:
                theMaze.setTextInteretColor(choosenColor);
                break;
            case Global.PICK_TEXT_SIZE:
                theMaze.setTextSize(Integer.parseInt(changeEditText.getText().toString()));
                theMaze.setTextInteretSize(Integer.parseInt(changeEditText.getText().toString()) / 2);
                break;
            case Global.PICK_STROKE_LINE:
                theMaze.setLineLargeur(Integer.parseInt(changeEditText.getText().toString()));
                break;
        }
        drawing.setTheMaze(theMaze);
        drawing.postInvalidate();
        setUpOptionsRecycleView();
        optionsDialog.show();
        changeDialog.dismiss();
    }

    private void setUpRecycleView(final Rectangle rectangle) {
        lineItems = new ArrayList<>();
        for (Line line : rectangle.getRectanglesId()) {
            Rectangle goToRect = drawing.getRectangleById(line.getGoToId());
            if (goToRect != null) {
                if (checkBidirectionnel(rectangle, line.getGoToId())) {
                    lineItems.add(new LineItem("--> " + goToRect.getName(), goToRect.getUID(), R.drawable.twodirections));
                } else {
                    lineItems.add(new LineItem("--> " + goToRect.getName(), goToRect.getUID(), R.drawable.onedirection));
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
                Toast.makeText(context, rectangle.getName() + " ,id:" + lineItems.get(position).getUid(), Toast.LENGTH_SHORT).show();
                drawing.deleteLine(rectangle, lineItems.get(position).getUid());
                linesDialog.dismiss();
            }
        });
    }

    private void setUpOptionsRecycleView() {
        optionItems = new ArrayList<>();

        optionItems.add(new OptionItem("Couleur Principale", theMaze.getNormalColor(), Global.PICK_RECTANGLE_PRINCIPALE));
        optionItems.add(new OptionItem("Couleur Selection", theMaze.getSelectedColor(), Global.PICK_RECTANGLE_SELECTION));
        optionItems.add(new OptionItem("Background", theMaze.getBackgroundColor(), Global.PICK_BG_CANVAS));
        optionItems.add(new OptionItem("Ligne Direction", theMaze.getLineDirectionColor(), Global.PICK_LINE_DIRECTION));
        optionItems.add(new OptionItem("Couleur Ligne", theMaze.getLineColor(), Global.PICK_LINE));
        optionItems.add(new OptionItem("Couleur chemin", theMaze.getDirectionColor(), Global.PICK_RECTANGLE_DIRECTION));
        optionItems.add(new OptionItem("Couleur Interet", theMaze.getInteretColor(), Global.PICK_COLOR_INTERET));
        optionItems.add(new OptionItem("Couleur Text", theMaze.getTextColor(), Global.PICK_COLOR_TEXT));
        optionItems.add(new OptionItem("Couleur Text Interet", theMaze.getTextInteretColor(), Global.PICK_COLOR_TEXT_INTERET));
        optionItems.add(new OptionItem("Text Size", theMaze.getTextSize(), Global.PICK_TEXT_SIZE));
        optionItems.add(new OptionItem("Ligne largeur", theMaze.getLineLargeur(), Global.PICK_STROKE_LINE));

        optionRecycleView.setHasFixedSize(true);
        optionLayoutManager = new LinearLayoutManager(this);
        optionAdapter = new OptionAdapter(optionItems);
        optionRecycleView.setLayoutManager(optionLayoutManager);
        optionRecycleView.setAdapter(optionAdapter);

        optionAdapter.setOnClickListener(new OptionAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                changeMode = optionItems.get(position).getChoice();
                choosenColor = optionItems.get(position).getOldColor();
                prototypeDraw.setMode(optionItems.get(position).getChoice());
                setProtitypeCurrentValues();
                if (optionItems.get(position).getChoice() == Global.PICK_TEXT_SIZE || optionItems.get(position).getChoice() == Global.PICK_STROKE_LINE) {
                    changeEditLayout.setVisibility(View.VISIBLE);
                    changeEditText.setVisibility(View.VISIBLE);
                    colorPicker.setVisibility(View.GONE);
                    changeLayout.setBackground(ContextCompat.getDrawable(context, R.mipmap.rect3));
                    initEditText(optionItems.get(position).getChoice());
                } else {
                    changeEditLayout.setVisibility(View.GONE);
                    changeEditText.setVisibility(View.GONE);
                    colorPicker.setVisibility(View.VISIBLE);
                    changeLayout.setBackgroundColor(Color.TRANSPARENT);
                }
                prototypeDraw.setColor(choosenColor);
                changeDialog.show();
                optionsDialog.dismiss();
            }
        });
    }

    private void setUpAstarRecycleView(List<Rectangle> rectangles, final Rectangle start) {
        astarItems = new ArrayList<>();
        for (Rectangle rectangle : rectangles) {
            if (!rectangle.equals(start))
                astarItems.add(rectangle.getName());
        }
        astarRecycleView.setHasFixedSize(true);
        astarLayoutManager = new LinearLayoutManager(this);
        astarAdapter = new AstarAdapter(astarItems);
        astarRecycleView.setLayoutManager(astarLayoutManager);
        astarRecycleView.setAdapter(astarAdapter);

        astarAdapter.setOnClickListener(new AstarAdapter.OnClickListener() {
            @Override
            public void onItemClick(int position) {
                Rectangle end = drawing.getRectangleByName(astarItems.get(position));
                drawing.calculerLeChemin(start, end);
                astarDialog.dismiss();
                astarEditText.setText("");
            }
        });
    }

    private void initEditText(int action) {
        switch (action) {
            case Global.PICK_TEXT_SIZE:
                changeEditText.setText(theMaze.getTextSize() + "");
                break;
            case Global.PICK_STROKE_LINE:
                changeEditText.setText(theMaze.getLineLargeur() + "");
                break;
        }
    }


    private void setProtitypeCurrentValues() {
        prototypeDraw.setCurrentBgColor(theMaze.getBackgroundColor());
        prototypeDraw.setCurrentRectColor(theMaze.getNormalColor());
        prototypeDraw.setCurrentTextColor(theMaze.getTextColor());
        prototypeDraw.setCurrentTextStroke(theMaze.getTextStroke());
        prototypeDraw.setCurrentTextSize(theMaze.getTextSize());
        prototypeDraw.setCurrentLineStroke(theMaze.getLineLargeur());
        prototypeDraw.setCurrentRectInteretColor(theMaze.getInteretColor());
        prototypeDraw.setCurrentLineColor(theMaze.getLineColor());
    }

    private boolean checkBidirectionnel(Rectangle rId, String UID) {
        Rectangle rectangle = drawing.getRectangleById(UID);
        if (rectangle != null) {
            for (Line line : rectangle.getRectanglesId()) {
                if (line.getGoToId().equals(rId.getUID())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        drawing.back();
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        drawing.back();
        super.onStop();
    }

    @Override
    public void onBackPressed() {

    }
}
