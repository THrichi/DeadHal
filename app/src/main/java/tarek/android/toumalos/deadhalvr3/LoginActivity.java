package tarek.android.toumalos.deadhalvr3;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Arrays;
import java.util.List;

import tarek.android.toumalos.deadhalvr3.Const.Global;
import tarek.android.toumalos.deadhalvr3.Models.User;

public class LoginActivity extends AppCompatActivity {

    private List<AuthUI.IdpConfig> providers;
    private FirebaseFirestore db;
    private CollectionReference collectionRef;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = FirebaseFirestore.getInstance();
        collectionRef = db.collection("userIDs");
        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.AnonymousBuilder().build()
        );


        showSingInOptions();
    }

    private void showSingInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.LoginTheme)
                        .build(), Global.MY_REQUEST_CODE
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Global.MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                checkUser();
            }
        }
    }

    private void checkUser() {
        collectionRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        boolean found = false;
                        for (QueryDocumentSnapshot qs : queryDocumentSnapshots) {
                            User u = qs.toObject(User.class);
                            if (u != null) {
                                if (u.getUid().equals(user.getUid())) {
                                    found = true;
                                }
                            }
                        }
                        if (!found) {
                            String name;
                            if (user.getDisplayName() == null) {
                                name = "Guest";
                            } else {
                                name = user.getDisplayName();
                            }
                            collectionRef.document(user.getUid()).set(new User(user.getUid(), name));
                        }
                        Intent intent = new Intent(LoginActivity.this, ProfilActivity.class);
                        startActivity(intent);
                    }
                });
    }
}
