package com.example.tailorapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.tailorapp.login.MainActivity;
import com.example.tailorapp.login.UserApI;
import com.example.tailorapp.nav.Myshopmain;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Launcher extends AppCompatActivity {
    private Button tailor, customer;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser current_user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference users_collection = db.collection("User");

    @Override
    protected void onStart() {
        super.onStart();
        current_user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseAuth!=null){
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        tailor = findViewById(R.id.tailor_login);
        customer = findViewById(R.id.customer_login);


        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()!=null){

                    current_user = firebaseAuth.getCurrentUser();
                    final String userid = current_user.getUid();
                    users_collection.whereEqualTo("userId",userid)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                    if (error!=null){
                                        return;
                                    }
                                    assert value != null;
                                    if (!value.isEmpty()){
                                        for (QueryDocumentSnapshot snapshot: value) {
                                            UserApI userApI = UserApI.getInstance();
                                            userApI.setUserId(snapshot.getString("userId"));
                                            userApI.setUsername(snapshot.getString("username"));
                                            Intent myshop_intent = new Intent(Launcher.this, Myshopmain.class);
                                            startActivity(myshop_intent);
                                            finish();
                                        }
                                    }
                                }
                            });
                }
            }
        };


        tailor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tailor_intent = new Intent(Launcher.this, MainActivity.class);
                tailor_intent.putExtra("mode","tailor");
                startActivity(tailor_intent);
            }
        });

        customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tailor_intent = new Intent(Launcher.this, MainActivity.class);
                tailor_intent.putExtra("mode","customer");
                startActivity(tailor_intent);
            }
        });

    }
}