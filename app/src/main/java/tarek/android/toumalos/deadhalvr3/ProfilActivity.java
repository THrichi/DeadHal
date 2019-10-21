package tarek.android.toumalos.deadhalvr3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import tarek.android.toumalos.deadhalvr3.Adapter.MazeAdapter;
import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Models.Maze;
import tarek.android.toumalos.deadhalvr3.Models.User;

public class ProfilActivity extends AppCompatActivity {

    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private CircleImageView profilImage;
    private CircleImageView logOut;
    private TextView userName;
    private Context context;
    private Button myMazes;
    private Button followMaze;
    private Button addMaze;
    private CircleImageView generateCode;
    private EditText codeEditText;
    private Button valider;
    private EditText nameNewMaze, followCodeEditText;
    private CircleImageView followCodeValide;
    private List<User> users;
    private LinearLayout addLayout;
    private LinearLayout MyMazeLayout;
    private LinearLayout followMazeLayout;
    private LinearLayout layoutSearchFollowMaze;
    private CircleImageView addFollowMaze, detailsFollowMaze;
    private TextView followMazeName;
    private CircleImageView detailsCancel;
    private TextView detailsCode;
    private TextView detailsName;
    private ImageView detailsScreen;
    private ImageView copyCodeDetails;
    private CircleImageView shareCode;
    private ImageView copyEditCode;
    private TextView detailsEditCode;
    private CircleImageView shareEditCode;
    private LinearLayout editCodeLayout;
    private ImageView copyCode;
    private User currentUser;

    //dialog
    private Dialog detailsDialog;
    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mazebookRef = db.collection(user.getUid());
    private CollectionReference userIds = db.collection("userIDs");
    //RecycleView
    private RecyclerView recyclerView, recyclerViewFollow;
    private MazeAdapter mazeAdapter, mazeAdapterFollow;
    private RecyclerView.LayoutManager layoutManager, layoutManagerFollow;
    private List<Maze> mazes;
    private Maze mazeSeached;
    private List<Maze> followListMaze;
    private StorageReference storageRef;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        context = this;
        users = new ArrayList<>();
        followListMaze = new ArrayList<>();
        mazes = new ArrayList<>();
        storageRef = FirebaseStorage.getInstance().getReference("uploads");
        databaseRef = FirebaseDatabase.getInstance().getReference("uploads");
        initDialog();
        profilImage = (CircleImageView) findViewById(R.id.profilImage);
        logOut = (CircleImageView) findViewById(R.id.logOut);
        userName = (TextView) findViewById(R.id.userName);
        myMazes = (Button) findViewById(R.id.myMazes);
        followMaze = (Button) findViewById(R.id.followMaze);
        addMaze = (Button) findViewById(R.id.addMaze);
        addLayout = (LinearLayout) findViewById(R.id.addLayout);
        MyMazeLayout = (LinearLayout) findViewById(R.id.MyMazeLayout);
        followMazeLayout = (LinearLayout) findViewById(R.id.followMazeLayout);
        layoutSearchFollowMaze = (LinearLayout) findViewById(R.id.layoutSearchFollowMaze);
        detailsFollowMaze = (CircleImageView) findViewById(R.id.detailsFollowMaze);
        addFollowMaze = (CircleImageView) findViewById(R.id.addFollowMaze);
        followMazeName = (TextView) findViewById(R.id.followMazeName);
        copyCode = (ImageView) findViewById(R.id.copyCode);
        generateCode = (CircleImageView) findViewById(R.id.generateCode);
        codeEditText = (EditText) findViewById(R.id.code);
        nameNewMaze = (EditText) findViewById(R.id.nameNewMaze);
        followCodeEditText = (EditText) findViewById(R.id.followCodeEditText);
        followCodeValide = (CircleImageView) findViewById(R.id.followCodeValide);
        valider = (Button) findViewById(R.id.valider);

        myMazes.setBackgroundColor(Color.rgb(207, 216, 243));

        if (user.getPhotoUrl() != null) {
            Picasso.get().load(user.getPhotoUrl()).into(profilImage);
        } else {
            Picasso.get().load(R.drawable.unknown).into(profilImage);
        }
        if (user.getDisplayName() == null) {
            userName.setText("Guest");
        } else {
            userName.setText(user.getDisplayName());
        }

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        myMazes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMazes.setBackgroundColor(Color.rgb(207, 216, 243));
                addMaze.setBackgroundColor(Color.TRANSPARENT);
                followMaze.setBackgroundColor(Color.TRANSPARENT);

                addLayout.setVisibility(View.GONE);
                followMazeLayout.setVisibility(View.GONE);
                MyMazeLayout.setVisibility(View.VISIBLE);

            }
        });
        followMaze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMazes.setBackgroundColor(Color.TRANSPARENT);
                addMaze.setBackgroundColor(Color.TRANSPARENT);
                followMaze.setBackgroundColor(Color.rgb(207, 216, 243));

                addLayout.setVisibility(View.GONE);
                followMazeLayout.setVisibility(View.VISIBLE);
                MyMazeLayout.setVisibility(View.GONE);
            }
        });
        addMaze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMazes.setBackgroundColor(Color.TRANSPARENT);
                followMaze.setBackgroundColor(Color.TRANSPARENT);
                addMaze.setBackgroundColor(Color.rgb(207, 216, 243));

                addLayout.setVisibility(View.VISIBLE);
                followMazeLayout.setVisibility(View.GONE);
                MyMazeLayout.setVisibility(View.GONE);
            }
        });
        generateCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeEditText.setText(generateCode());
            }
        });

        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMaze();
            }
        });
        followCodeValide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (followCodeEditText.getText().toString().length() > 0) {
                    searchMaze(followCodeEditText.getText().toString());
                }
            }
        });
        copyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("code", codeEditText.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copier avec succès", Toast.LENGTH_SHORT).show();
            }
        });
        codeEditText.setText(generateCode());
        detailsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsDialog.dismiss();
            }
        });
        addUserId();

    }

    private void addUserId() {
        userIds.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        boolean found = false;
                        for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                            User u = doc.toObject(User.class);
                            users.add(u);
                            if (u.getId().equals(user.getUid())) {
                                found = true;
                                currentUser = u;
                            }
                        }
                        if (!found) {
                            String name = user.getDisplayName();
                            if (name == null) {
                                name = "Guest";
                            }
                            User u = new User(name, user.getUid(), new ArrayList<String>());
                            userIds.document(user.getUid()).set(u);
                            users.add(u);
                            currentUser = u;
                        }
                        refreshFollowList();
                    }
                });
    }

    private void searchMaze(final String code) {
        for (User user : users) {
            db.collection(user.getId()).whereEqualTo("code", code)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                mazeSeached = doc.toObject(Maze.class);
                                if (mazeSeached != null)
                                    setMazeSearchInfos(mazeSeached, false);
                            }
                            if (mazeSeached == null)
                                searchEditedMaze(code);
                        }
                    });
        }
    }

    private void searchEditedMaze(String editCode) {
        for (User user : users) {
            db.collection(user.getId()).whereEqualTo("editCode", editCode)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                mazeSeached = doc.toObject(Maze.class);
                                if (mazeSeached != null)
                                    setMazeSearchInfos(mazeSeached, true);
                            }
                        }
                    });
        }
    }

    private void getFollowMazeList() {
        followListMaze = new ArrayList<>();
        for (User user : users) {
            db.collection(user.getId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                Maze maze = doc.toObject(Maze.class);
                                    if (currentUser.getFollowingMazes().contains(maze.getCode())) {
                                        maze.setStatus(Global.VIEWER);
                                    } else if (currentUser.getFollowingMazes().contains(maze.getEditCode())) {
                                        maze.setStatus(Global.EDITER);
                                    }
                                    if (!contains(maze))
                                        followListMaze.add(maze);
                            }
                            setUpFollowRecycleView();
                        }
                    });
        }
    }

    private boolean contains(Maze maze) {
        for (Maze m : followListMaze) {
            if (m.getCode().equals(maze.getCode()))
                return true;
        }
        return false;
    }

    @Override
    protected void onStart() {
        super.onStart();

        mazebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                mazes = new ArrayList<>();
                for (QueryDocumentSnapshot qs : queryDocumentSnapshots) {
                    Maze item = qs.toObject(Maze.class);
                    mazes.add(item);
                }
                setUpRecycleView();
            }
        });
    }

    private void refreshFollowList() {
        for (User u : users) {
            db.collection(u.getId()).addSnapshotListener(this, new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    getFollowMazeList();
                }
            });
        }
    }

    private void setMazeSearchInfos(final Maze mazeSeached, boolean isEditable) {
        if (!currentUser.getFollowingMazes().contains(mazeSeached.getCode()) && !mazes.contains(mazeSeached) && !currentUser.getFollowingMazes().contains(mazeSeached.getEditCode())) {
            followMazeName.setText(mazeSeached.getName());
            final String code;
            if (isEditable) {
                mazeSeached.setStatus(Global.EDITER);
                code = mazeSeached.getEditCode();
            } else {
                mazeSeached.setStatus(Global.VIEWER);
                code = mazeSeached.getCode();
            }
            layoutSearchFollowMaze.setVisibility(View.VISIBLE);
            addFollowMaze.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFollowMaze(mazeSeached, code);
                    layoutSearchFollowMaze.setVisibility(View.GONE);
                    followCodeEditText.setText("");
                    followListMaze.add(mazeSeached);
                    mazeAdapterFollow.notifyItemInserted(followListMaze.size());
                }
            });
            detailsFollowMaze.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    initDetails(mazeSeached);
                }
            });
        } else {
            Toast.makeText(context, "Ce Labyrinthe existe déjà dans votre liste", Toast.LENGTH_SHORT).show();
        }
    }

    private void addFollowMaze(final Maze mazeSeached, final String code) {
        userIds.document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<String> followList = new ArrayList<>();
                        User u = documentSnapshot.toObject(User.class);
                        if (u != null) {
                            followList = new ArrayList<>(u.getFollowingMazes());
                        }
                        followList.add(code);
                        userIds.document(user.getUid()).update("followingMazes", followList);
                    }
                });
    }

    private void initDialog() {
        detailsDialog = new Dialog(context);
        detailsDialog.setContentView(R.layout.activity_details_dialog);
        detailsDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        detailsCancel = (CircleImageView) detailsDialog.findViewById(R.id.detailsCancel);
        detailsCode = (TextView) detailsDialog.findViewById(R.id.detailsCode);
        detailsName = (TextView) detailsDialog.findViewById(R.id.detailsName);
        detailsScreen = (ImageView) detailsDialog.findViewById(R.id.detailsScreen);
        copyCodeDetails = (ImageView) detailsDialog.findViewById(R.id.copyCode);
        shareCode = (CircleImageView) detailsDialog.findViewById(R.id.shareCode);
        detailsEditCode = (TextView) detailsDialog.findViewById(R.id.detailsEditCode);
        shareEditCode = (CircleImageView) detailsDialog.findViewById(R.id.shareEditCode);
        copyEditCode = (ImageView) detailsDialog.findViewById(R.id.copyEditCode);
        editCodeLayout = (LinearLayout) detailsDialog.findViewById(R.id.editCodeLayout);

        copyCodeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("codeDetails", detailsCode.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copier avec succès", Toast.LENGTH_SHORT).show();
            }
        });

        shareCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();

                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                String text = "Mon code de partage : " + detailsCode.getText().toString();
                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(waIntent, "Share with"));

            }
        });

        copyEditCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("codeDetails", detailsEditCode.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Copier avec succès", Toast.LENGTH_SHORT).show();
            }
        });

        shareEditCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager pm = getPackageManager();

                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                String text = "Mon code de partage : " + detailsEditCode.getText().toString();
                waIntent.putExtra(Intent.EXTRA_TEXT, text);
                startActivity(Intent.createChooser(waIntent, "Share with"));

            }
        });
    }

    private void initDetails(Maze maze) {
        if (maze != null) {
            detailsDialog.show();
            detailsCode.setText(maze.getCode());
            detailsName.setText(maze.getName().toUpperCase());
            detailsEditCode.setText(maze.getEditCode());
            if (maze.getUserId().equals(user.getUid())) {
                editCodeLayout.setVisibility(View.VISIBLE);
            } else {
                editCodeLayout.setVisibility(View.GONE);
            }
            StorageReference reference = storageRef.child(maze.getCode() + ".jpg");
            reference.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri.toString()).into(detailsScreen);
                        }
                    });
        }
    }

    private void downloadFiles() {

    }

    private Maze getMaze(String name) {
        for (Maze maze : mazes) {
            if (maze.getName().equals(name))
                return maze;
        }
        return null;
    }

    private Maze getFollowMaze(String name) {
        for (Maze maze : followListMaze) {
            if (maze.getName().equals(name))
                return maze;
        }
        return null;
    }

    private String generateCode() {
        UUID uuid = UUID.randomUUID();
        String code = uuid.toString();
        return code;
    }

    private void addMaze() {
        if (nameNewMaze.getText().toString().length() > 0) {
            Maze m1 = new Maze(user.getUid(), codeEditText.getText().toString(), generateCode(), nameNewMaze.getText().toString(), true);
            mazebookRef.document(nameNewMaze.getText().toString()).set(m1);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("MazeName", nameNewMaze.getText().toString());
            startActivity(intent);
        }
    }

    /*
        private void loadMazes() {
            mazes = new ArrayList<>();
            mazebookRef.get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot qs : queryDocumentSnapshots) {
                                Maze item = qs.toObject(Maze.class);
                                mazes.add(item);
                            }
                            setUpRecycleView();
                     }
                    });
        }*/

    private void setUpFollowRecycleView() {
        recyclerViewFollow = findViewById(R.id.followListMazes);
        recyclerViewFollow.setHasFixedSize(true);
        layoutManagerFollow = new LinearLayoutManager(this);
        mazeAdapterFollow = new MazeAdapter(followListMaze, Global.FOLLOW_LIST, user.getUid());
        recyclerViewFollow.setLayoutManager(layoutManagerFollow);
        recyclerViewFollow.setAdapter(mazeAdapterFollow);

        mazeAdapterFollow.setOnOpenClickListener(new MazeAdapter.OnOpenClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(context, StreamingActivity.class);
                intent.putExtra("theMaze", getFollowMaze(followListMaze.get(position).getName()));
                startActivity(intent);
            }
        });
        mazeAdapterFollow.setOnDetailsClickListener(new MazeAdapter.OnDetailsClickListener() {
            @Override
            public void onItemClick(int position) {
                initDetails(getMaze(followListMaze.get(position).getName()));
            }
        });

        mazeAdapterFollow.setOnRemoveClickListener(new MazeAdapter.OnRemoveClickListener() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(context,followListMaze.get(position).getName(),Toast.LENGTH_SHORT).show();
                dialogFollowConfirmation(followListMaze.get(position), position);
            }
        });
    }

    private void setUpRecycleView() {
        recyclerView = findViewById(R.id.myListMazes);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mazeAdapter = new MazeAdapter(mazes, Global.NORMAL_LIST, user.getUid());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mazeAdapter);

        mazeAdapter.setOnOpenClickListener(new MazeAdapter.OnOpenClickListener() {
            @Override
            public void onItemClick(int position) {
                openMaze(mazes.get(position).getName());
            }
        });
        mazeAdapter.setOnDetailsClickListener(new MazeAdapter.OnDetailsClickListener() {
            @Override
            public void onItemClick(int position) {
                initDetails(getMaze(mazes.get(position).getName()));
            }
        });

        mazeAdapter.setOnRemoveClickListener(new MazeAdapter.OnRemoveClickListener() {
            @Override
            public void onItemClick(int position) {
                dialogConfirmation(mazes.get(position).getName(), position);
            }
        });
    }

    public void openMaze(String mazeName) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("MazeName", mazeName);
        startActivity(intent);
    }


    public void dialogFollowConfirmation(final Maze removedMaze, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Confirm removing selected object ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (removedMaze.getStatus().equals(Global.VIEWER))
                            currentUser.getFollowingMazes().remove(removedMaze.getCode());
                        else if (removedMaze.getStatus().equals(Global.EDITER))
                            currentUser.getFollowingMazes().remove(removedMaze.getEditCode());
                        userIds.document(user.getUid()).update("followingMazes", currentUser.getFollowingMazes());
                        removeFollowedMaze(removedMaze.getCode());
                        mazeAdapterFollow.notifyItemRemoved(position);
                        mazeAdapterFollow.notifyItemRangeChanged(position, followListMaze.size());
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

    private void removeFollowedMaze(String code) {
        List<Maze> result = new ArrayList<>();
        for (Maze m : followListMaze) {
            if (!m.getCode().equals(code))
                result.add(m);
        }
        followListMaze = new ArrayList<>(result);
    }

    public void dialogConfirmation(final String mazeName, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Confirm removing selected object ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mazebookRef.document(mazeName).delete();
                        mazes.remove(position);
                        mazeAdapter.notifyItemRemoved(position);
                        mazeAdapter.notifyItemRangeChanged(position, mazes.size());
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
    protected void onStop() {
        super.onStop();
    }

    private void logOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, e.getMessage() + "", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
