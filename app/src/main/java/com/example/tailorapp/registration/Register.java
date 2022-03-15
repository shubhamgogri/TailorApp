package com.example.tailorapp.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorapp.R;
import com.example.tailorapp.login.MainActivity;
import com.example.tailorapp.login.UserApI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import kotlin.jvm.internal.MagicApiIntrinsics;

public class Register extends AppCompatActivity {
    private EditText email_text, username_edit;
    private EditText password_text , confirm_password ;
    private Button register;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser current_user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("User");
    private ProgressBar progressBar;
    private static String mode ;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        progressBar = findViewById(R.id.progress_register);
        username_edit = findViewById(R.id.username_edit);
        confirm_password = findViewById(R.id.confirm_password_register);
        email_text = findViewById(R.id.e_mail_register);
        password_text = findViewById(R.id.password_register);
        register = findViewById(R.id.register);
        title = findViewById(R.id.title);
        firebaseAuth = FirebaseAuth.getInstance();

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        mode = bundle.getString("mode");
        assert mode != null;
        if (mode.equals("tailor")){
            title.setText("Tailor Registration");
        }
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                if (!TextUtils.isEmpty(email_text.getText().toString())
                    && !TextUtils.isEmpty(password_text.getText().toString())
                        && !TextUtils.isEmpty(username_edit.getText().toString())
                    && !TextUtils.isEmpty(confirm_password.getText().toString())){

                    String email = email_text.getText().toString();
                    String string_password_text = password_text.getText().toString();
                    String confirm_password_text = confirm_password.getText().toString();
                    String username = username_edit.getText().toString();

                    if (string_password_text.equals(confirm_password_text)){
//                        createUserAccount(email,confirm_password_text,username);
                        progressBar.setVisibility(View.INVISIBLE);
                        Intent intent = new Intent(Register.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else {
                        Toast.makeText(Register.this, "Passwords don't match", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(Register.this, "Empty Fields Not Allowed", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void createUserAccount(String email, String confirm_password_text, String username) {
        if (!TextUtils.isEmpty(email)&&
                !TextUtils.isEmpty(confirm_password_text)
            && !TextUtils.isEmpty(username)){
            firebaseAuth
                    .createUserWithEmailAndPassword(email,confirm_password_text)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){
                                current_user = firebaseAuth.getCurrentUser();
                                String userId = current_user.getUid();

                                Map<String, String> user = new HashMap<>();
                                user.put("username",username);
                                user.put("userId",userId);

                                collectionReference
                                        .add(user)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (task.getResult().exists()){
                                                                    String name = task.getResult().getString("username");
                                                                    Log.d("CreateAccount", "onComplete: Username from cloud" + name);

                                                                    UserApI userApI = UserApI.getInstance();
                                                                    userApI.setUserId(userId);
                                                                    userApI.setUsername(name);
                                                                }
                                                            }
                                                        });
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Intent intent = new Intent(Register.this, MainActivity.class);
                                                startActivity(intent);
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Log.d("CreateAccount", "onFailure: collectionRef "+ e);
                                                Toast.makeText(Register.this, "Something went wrong" + "\n"+ e, Toast.LENGTH_SHORT).show();

                                            }
                                        });

                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("CreateAccount", "onFailure: Auth "+ e);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(Register.this, "Credentials are invalid" + "\n"+ e, Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(Register.this, "Fields are empty", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        mode = bundle.getString("mode");
        assert mode != null;
        if (mode.equals("tailor")){
            collectionReference = db.collection("Tailor");
        }
    }
}