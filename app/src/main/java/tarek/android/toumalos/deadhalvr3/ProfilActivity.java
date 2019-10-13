package tarek.android.toumalos.deadhalvr3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import tarek.android.toumalos.deadhalvr3.Adapter.MazeAdapter;
import tarek.android.toumalos.deadhalvr3.Models.Line;
import tarek.android.toumalos.deadhalvr3.Models.Maze;
import tarek.android.toumalos.deadhalvr3.Models.MazeItem;
import tarek.android.toumalos.deadhalvr3.Models.Rectangle;

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
    private EditText nameNewMaze;

    private LinearLayout addLayout;
    private LinearLayout MyMazeLayout;
    private LinearLayout followMazeLayout;

    //Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference mazebookRef = db.collection(user.getUid());
    //RecycleView
    private RecyclerView recyclerView;
    private MazeAdapter mazeAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<MazeItem> mazeItems;
    private List<Maze> mazes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        context = this;
        mazeItems = new ArrayList<>();
        mazes = new ArrayList<>();

        profilImage = (CircleImageView) findViewById(R.id.profilImage);
        logOut = (CircleImageView) findViewById(R.id.logOut);
        userName = (TextView) findViewById(R.id.userName);
        myMazes = (Button) findViewById(R.id.myMazes);
        followMaze = (Button) findViewById(R.id.followMaze);
        addMaze = (Button) findViewById(R.id.addMaze);
        addLayout = (LinearLayout) findViewById(R.id.addLayout);
        MyMazeLayout = (LinearLayout) findViewById(R.id.MyMazeLayout);
        followMazeLayout = (LinearLayout) findViewById(R.id.followMazeLayout);
        generateCode = (CircleImageView) findViewById(R.id.generateCode);
        codeEditText = (EditText)   findViewById(R.id.code);
        nameNewMaze = (EditText)   findViewById(R.id.nameNewMaze);
        valider = (Button)   findViewById(R.id.valider);

        myMazes.setBackgroundColor(Color.rgb(207,216,243));

        if (user.getPhotoUrl() != null) {
            Picasso.get().load(user.getPhotoUrl()).into(profilImage);
        } else {
            Picasso.get().load(R.drawable.unknown).into(profilImage);
        }

        userName.setText(user.getDisplayName());

        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        myMazes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myMazes.setBackgroundColor(Color.rgb(207,216,243));
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
                followMaze.setBackgroundColor(Color.rgb(207,216,243));

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
                addMaze.setBackgroundColor(Color.rgb(207,216,243));

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
        loadMazes();
    }

    private String generateCode(){
        UUID uuid = UUID.randomUUID();
        String code = uuid.toString();
        code = code.substring(0,8);
        return code;
    }
    private void addMaze() {
        Maze m1 = new Maze(user.getUid(),codeEditText.getText().toString(),nameNewMaze.getText().toString());
        mazebookRef.document(nameNewMaze.getText().toString()).set(m1);
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("MazeName",nameNewMaze.getText().toString());
        startActivity(intent);
    }
    private void loadMazes(){
        mazebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot qs : queryDocumentSnapshots){
                            Maze item = qs.toObject(Maze.class);
                            mazes.add(item);
                            setUpRecycleView();
                        }
                    }
                });
    }
    private void setUpRecycleView() {
        for (Maze maze : mazes) {
            mazeItems.add(new MazeItem(maze.getName(),R.drawable.ic_open_in_new_black_24dp,R.drawable.ic_info_black_24dp,R.drawable.ic_delete_forever_black_24dp));
        }
        /*mazeItems.add(new MazeItem("First Maze",R.drawable.ic_open_in_new_black_24dp,R.drawable.ic_info_black_24dp,R.drawable.ic_delete_forever_black_24dp));
        mazeItems.add(new MazeItem("Second Maze",R.drawable.ic_open_in_new_black_24dp,R.drawable.ic_info_black_24dp,R.drawable.ic_delete_forever_black_24dp));
        mazeItems.add(new MazeItem("Third Maze",R.drawable.ic_open_in_new_black_24dp,R.drawable.ic_info_black_24dp,R.drawable.ic_delete_forever_black_24dp));
        mazeItems.add(new MazeItem("Fourth Maze",R.drawable.ic_open_in_new_black_24dp,R.drawable.ic_info_black_24dp,R.drawable.ic_delete_forever_black_24dp));*/
        recyclerView = findViewById(R.id.myListMazes);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        mazeAdapter = new MazeAdapter(mazeItems);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mazeAdapter);

        mazeAdapter.setOnItemClickListener(new MazeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                openMaze(mazeItems.get(position).getName());
            }
        });
    }
    public void openMaze(String mazeName){
        Intent intent = new Intent(this,MainActivity.class);
        intent.putExtra("MazeName",mazeName);
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
