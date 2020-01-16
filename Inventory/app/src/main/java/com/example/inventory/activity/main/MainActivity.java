package com.example.inventory.activity.main;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.inventory.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    static final int GOOGLE_SIGN = 123;
    FirebaseAuth mAuth;
    Button btn_login, btn_logout;
    TextView text;
    ImageView image;
    ProgressBar progressBar;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_login = findViewById(R.id.login);
        btn_logout = findViewById(R.id.logut);
        text = findViewById(R.id.text);
        image = findViewById(R.id.image);
        progressBar = findViewById(R.id.progress_circular);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        btn_login.setOnClickListener(v ->masuk()); //jgn diapus ye
        btn_logout.setOnClickListener(v ->Logout());

        if (mAuth.getCurrentUser() != null){
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);
        }
    }

    void SignInGoogle(){
        progressBar.setVisibility(View.VISIBLE);
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);
            }catch (ApiException e){
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log. d ("TAG","firebaseAuthWithGoogle: " +account.getId());

        AuthCredential credential= GoogleAuthProvider
                .getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()){
                        progressBar.setVisibility(View.VISIBLE);
                        Log.d("TAG", "signin sukses");

                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else{
                        progressBar.setVisibility(View.INVISIBLE);
                        Log.w ("TAG","signin gagal", task.getException());

                        Toast.makeText(this, "Sigin Gagal!", Toast.LENGTH_SHORT).show();
                        updateUI(null);

                    }

                });
    }

    private void updateUI(FirebaseUser user) {

        if (user != null){
            Intent intent = new Intent (this, AddActivity.class);
            startActivity(intent);
            finish();

//            String name = user.getDisplayName();
//            String email = user.getEmail();
//            String photo = String.valueOf(user.getPhotoUrl());
//
//            text.append("Info : \n");
//            text.append(name + "\n");
//            text.append(email);
//
//            Picasso.get().load(photo).into(image);
//            btn_login.setVisibility(View.INVISIBLE);
//            btn_logout.setVisibility(View.VISIBLE);
        }else{
            text.setText(getString(R.string.inventory_login));
            Picasso.get().load(R.drawable.ic_firebase_logo).into(image);
            btn_login.setVisibility(View.VISIBLE);
            btn_logout.setVisibility(View.INVISIBLE);
        }
    }

    void Logout(){
        FirebaseAuth.getInstance().signOut();
//        mGoogleSignInClient.signOut().addOnCompleteListener(this,
//                task -> updateUI(null));
    }

    //jgn diapus juga ye
    void masuk(){
        Intent intent = new Intent (this, AddActivity.class);
        startActivity(intent);
        finish();
    }
}
