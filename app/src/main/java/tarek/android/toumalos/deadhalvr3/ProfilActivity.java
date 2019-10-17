package tarek.android.toumalos.deadhalvr3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.firebase.firestore.FirebaseFirestore;
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
import tarek.android.toumalos.deadhalvr3.Models.Maze;
import tarek.android.toumalos.deadhalvr3.Models.MazeItem;
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
    private List<MazeItem> mazeItems;
    private List<MazeItem> mazeFollowItems;
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
        mazeItems = new ArrayList<>();
        followListMaze = new ArrayList<>();
        mazeFollowItems = new ArrayList<>();
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
        codeEditText.setText(generateCode());
        detailsCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsDialog.dismiss();
            }
        });

        loadMazes();
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
                        getFollowMazeList();
                    }
                });
    }

    private void searchMaze(String code) {
        for (User user : users) {

            db.collection(user.getId()).whereEqualTo("code", code)
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                mazeSeached = doc.toObject(Maze.class);
                                if (mazeSeached != null)
                                    setMazeSearchInfos(mazeSeached);
                            }
                        }
                    });
        }
    }

    private void getFollowMazeList() {
        for (User user : users) {
            db.collection(user.getId())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            for (DocumentSnapshot doc : queryDocumentSnapshots) {
                                Maze maze = doc.toObject(Maze.class);
                                if (currentUser.getFollowingMazes().contains(maze.getCode())) {
                                    followListMaze.add(maze);
                                }
                            }
                            setUpFollowRecycleView();
                        }
                    });
        }
    }

    private void setMazeSearchInfos(final Maze mazeSeached) {
        if (!currentUser.getFollowingMazes().contains(mazeSeached.getCode())) {
            followMazeName.setText(mazeSeached.getName());
            layoutSearchFollowMaze.setVisibility(View.VISIBLE);
            addFollowMaze.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addFollowMaze(mazeSeached);
                    layoutSearchFollowMaze.setVisibility(View.GONE);
                    followCodeEditText.setText("");
                    mazeFollowItems.add(new MazeItem(mazeSeached.getName(), R.drawable.ic_visibility_black_24dp, R.drawable.ic_info_black_24dp, R.drawable.ic_delete_forever_black_24dp));
                    mazeAdapterFollow.notifyItemInserted(mazeFollowItems.size());
                    setUpFollowRecycleView();
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

    private void addFollowMaze(final Maze mazeSeached) {
        userIds.document(user.getUid()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        List<String> followList = new ArrayList<>();
                        User u = documentSnapshot.toObject(User.class);
                        if (u != null) {
                            followList = new ArrayList<>(u.getFollowingMazes());
                        }
                        followList.add(mazeSeached.getCode());
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
    }

    private void initDetails(String name) {
        Maze maze = getMaze(name);
        detailsCode.setText("code de partage : " + maze.getCode());
        detailsName.setText("Nom : " + maze.getName());
        StorageReference reference = storageRef.child(maze.getCode() + ".jpg");
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri.toString()).into(detailsScreen);
                    }
                });
    }

    private void initDetails(Maze maze) {
        detailsDialog.show();
        detailsCode.setText("code de partage : " + maze.getCode());
        detailsName.setText("Nom : " + maze.getName());
        StorageReference reference = storageRef.child(maze.getCode() + ".jpg");
        reference.getDownloadUrl()
                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri.toString()).into(detailsScreen);
                    }
                });
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

    private String generateCode() {
        UUID uuid = UUID.randomUUID();
        String code = uuid.toString();
        code = code.substring(0, 8);
        return code;
    }

    private void addMaze() {
        if (nameNewMaze.getText().toString().length() > 0) {
            Maze m1 = new Maze(user.getUid(), codeEditText.getText().toString(), nameNewMaze.getText().toString());
            mazebookRef.document(nameNewMaze.getText().toString()).set(m1);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("MazeName", nameNewMaze.getText().toString());
            startActivity(intent);
        }
    }

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
    }

    private void setUpFollowRecycleView() {
        mazeFollowItems = new ArrayList<>();

        for (Maze maze : followListMaze) {
            mazeFollowItems.add(new MazeItem(maze.getName(), R.drawable.ic_visibility_black_24dp, R.drawable.ic_info_black_24dp, R.drawable.ic_delete_forever_black_24dp));
        }
        recyclerViewFollow = findViewById(R.id.followListMazes);
        recyclerViewFollow.setHasFixedSize(true);
        layoutManagerFollow = new LinearLayoutManager(this);
        mazeAdapterFollow = new MazeAdapter(mazeFollowItems);
        recyclerViewFollow.setLayoutManager(layoutManagerFollow);
        recyclerViewFollow.setAdapter(mazeAdapterFollow);

        mazeAdapterFollow.setOnOpenClickListener(new MazeAdapter.OnOpenClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(context, StreamingActivity.class);
                intent.putExtra("theMaze", getMaze(mazeFollowItems.get(position).getName()));
                startActivity(intent);
            }
        });
        mazeAdapterFollow.setOnDetailsClickListener(new MazeAdapter.OnDetailsClickListener() {
            @Override
            public void onItemClick(int position) {
                detailsDialog.show();
                initDetails(mazeFollowItems.get(position).getName());
            }
        });

        mazeAdapterFollow.setOnRemoveClickListener(new MazeAdapter.OnRemoveClickListener() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(context,followListMaze.get(position).getName(),Toast.LENGTH_SHORT).show();
                dialogFollowConfirmation(followListMaze.get(position).getCode(),position);
            }
        });
    }

    private void setUpRecycleView() {
        mazeItems = new ArrayList<>();
        for (Maze maze : mazes) {
            mazeItems.add(new MazeItem(maze.getName(), R.drawable.ic_open_in_new_black_24dp, R.drawable.ic_info_black_24dp, R.drawable.ic_delete_forever_black_24dp));
        }
        recyclerView = findViewById(R.id.myListMazes);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mazeAdapter = new MazeAdapter(mazeItems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mazeAdapter);

        mazeAdapter.setOnOpenClickListener(new MazeAdapter.OnOpenClickListener() {
            @Override
            public void onItemClick(int position) {
                openMaze(mazeItems.get(position).getName());
            }
        });
        mazeAdapter.setOnDetailsClickListener(new MazeAdapter.OnDetailsClickListener() {
            @Override
            public void onItemClick(int position) {
                detailsDialog.show();
                initDetails(mazeItems.get(position).getName());
            }
        });

        mazeAdapter.setOnRemoveClickListener(new MazeAdapter.OnRemoveClickListener() {
            @Override
            public void onItemClick(int position) {
                dialogConfirmation(mazeItems.get(position).getName(), position);
            }
        });
    }

    public void openMaze(String mazeName) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("MazeName", mazeName);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*mazebookRef.addSnapshotListener(this,new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    return;
                }
                for (QueryDocumentSnapshot qs : queryDocumentSnapshots){
                    Maze item = qs.toObject(Maze.class);
                    mazes.add(item);
                    setUpRecycleView();
                }
            }
        });*/
    }

    public void dialogFollowConfirmation(final String code, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Confirm removing selected object ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        currentUser.getFollowingMazes().remove(code);
                        userIds.document(user.getUid()).update("followingMazes",currentUser.getFollowingMazes());
                        mazeFollowItems.remove(position);
                        mazeAdapterFollow.notifyItemRemoved(position);
                        mazeAdapterFollow.notifyItemRangeChanged(position, mazeFollowItems.size());
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
    public void dialogConfirmation(final String mazeName, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Confirm removing selected object ?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mazebookRef.document(mazeName).delete();
                        mazeItems.remove(position);
                        mazeAdapter.notifyItemRemoved(position);
                        mazeAdapter.notifyItemRangeChanged(position, mazeItems.size());
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
