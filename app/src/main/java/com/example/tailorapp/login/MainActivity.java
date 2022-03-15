package com.example.tailorapp.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorapp.OrderList_tailor;
import com.example.tailorapp.nav.Myshopmain;
import com.example.tailorapp.R;
import com.example.tailorapp.registration.Register;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookActivity;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final int GOOGLE_REQUEST_CODE = 101;
    private EditText email_text;
    private EditText password_text;
    private Button sign_in;
    private SignInButton google;
    private LoginButton facebook;
    private TextView sign_up;
    private TextView forgot_password;
    private TextView title;
    private ProgressBar progressBar;
    AtomicReference<String> name = new AtomicReference<>();

    private static String mode;
    private CallbackManager callbackManager;
    private static final String EMAIL = "email";

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser current_user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference users_collection ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progress_bar_login);
        email_text = findViewById(R.id.e_mail_login);
        password_text = findViewById(R.id.password_login);
        sign_in = findViewById(R.id.sign_in_button_login);
        sign_up = findViewById(R.id.Sign_up_text_button);
        google = findViewById(R.id.google_button_main);
        facebook = findViewById(R.id.facebook_button_main);
        forgot_password = findViewById(R.id.forgot_password);
        title = findViewById(R.id.textView5);

        mode = getIntent().getStringExtra("mode");
        if (mode.equals("customer"))
            title.setText("Customer Login");

        forgot_password.setOnClickListener(this);
        sign_in.setOnClickListener(this);
        google.setOnClickListener(this);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Register.class);
                intent.putExtra("mode", mode);
                startActivity(intent);
                finish();
            }
        });

        callbackManager = CallbackManager.Factory.create();
        facebook.setPermissions(Arrays.asList(EMAIL, "name"));
        facebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                handlerFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Log.d("MainActivityFacebook", "onError: Facebook" + error.getMessage());
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_in_button_login:
                if (!email_text.getText().toString().trim().isEmpty() &&
                        !password_text.getText().toString().trim().isEmpty()){
//                    login_auth(email_text.getText().toString().trim(), password_text.getText().toString().trim());

                    mode = getIntent().getStringExtra("mode");
                    if (mode.equals("customer")){
                        Intent login_intent = new Intent(MainActivity.this,Myshopmain.class);
                        startActivity(login_intent);
                        finish();
                    }else {
                        Intent login_intent = new Intent(MainActivity.this,OrderList_tailor.class);
                        startActivity(login_intent);
                        finish();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Enter All Fields", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.forgot_password:
                Intent intent = new Intent(getBaseContext(),Forgotpassword.class);
                startActivity(intent);
                break;

            case R.id.google_button_main:
                Toast.makeText(this, "Google API", Toast.LENGTH_SHORT).show();
                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail().build();

                GoogleSignInClient client = GoogleSignIn.getClient(MainActivity.this,googleSignInOptions);
                Intent signinwithGoogle = client.getSignInIntent();
                startActivityForResult(signinwithGoogle,GOOGLE_REQUEST_CODE);
                break;

            case R.id.facebook_button_main:
                Toast.makeText(this, "Facebook API", Toast.LENGTH_SHORT).show();
                Intent facebook_intent = new Intent(MainActivity.this, FacebookActivity.class);
                startActivity(facebook_intent);
                finish();
                break;
        }
    }

    private void handlerFacebookAccessToken(AccessToken token){

        GraphRequest request = GraphRequest.newMeRequest(token, (object, response) -> {
            try {
                String email = object.getString("email");
                String n = object.getString("first_name");
                String id = object.getString("id");
                name.set(n);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields","id,email,name");
        request.setParameters(parameters);
        request.executeAsync();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        firebaseAuth
                .signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            current_user = firebaseAuth.getCurrentUser();
                            Toast.makeText(MainActivity.this, "Log is Completed with user", Toast.LENGTH_SHORT).show();

                            Map<String,String> user = new HashMap<>();
//                            Log.d("MainActivityFacebook", "onComplete: facebook" + name);
                            user.put("username", name.get());
                            user.put("userId", current_user.getUid());
                            users_collection
                                    .add(user)
                                    .addOnSuccessListener(MainActivity.this, new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            documentReference.get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            UserApI userApI = UserApI.getInstance();
                                                            userApI.setUserId(String.valueOf(documentSnapshot.get("userId")));
                                                            userApI.setUsername(String.valueOf(documentSnapshot.get("username")));
                                                            Intent intent = new Intent(MainActivity.this, Myshopmain.class);
                                                            startActivity(intent);
                                                            finish();
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Log.d("MainActivityFacebook", "onFailure: Facebook " + e);
                                        }
                                    });


                        }else
                            Toast.makeText(MainActivity.this, "Log Failed with user", Toast.LENGTH_SHORT).show();
                    }
                });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_REQUEST_CODE){
            progressBar.setVisibility(View.VISIBLE);
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                signinwith_google(account.getIdToken(),account.getDisplayName());

            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
            callbackManager.onActivityResult(requestCode,resultCode,data);

    }

    private void signinwith_google(String idToken, String displayName) {

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken,null);
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d("MainActivityGoogle", "onComplete: "+ "signinwithCredentioal: Successful");
                            current_user = firebaseAuth.getCurrentUser();

                            Map<String, String> user = new HashMap<>();
                            user.put("username",displayName);
                            user.put("userId",current_user.getUid());

                            users_collection
                                    .add(user)
                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                        @Override
                                        public void onSuccess(DocumentReference documentReference) {

                                            documentReference.get()
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                            UserApI userApI = UserApI.getInstance();
                                                            userApI.setUserId(String.valueOf(documentSnapshot.get("userId")));
                                                            userApI.setUsername(String.valueOf(documentSnapshot.get("username")));
                                                            Intent intent = new Intent(MainActivity.this, Myshopmain.class);
                                                            startActivity(intent);
                                                            finish();
                                                            progressBar.setVisibility(View.INVISIBLE);
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressBar.setVisibility(View.INVISIBLE);
                                            Log.d("MainActivityGoogle", "onFailure: Saving user into Firebase"+ e);
                                        }
                                    });

                        }else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Log.d("MainActivityGoogle", "onComplete: "+ "signinwithCredentioal: Failed");
                        }
                    }
                });

            Log.d("MainActivityGoogle", "onActivityResult: "+ idToken);

    }


    @Override
    protected void onStart() {
        super.onStart();
        mode = getIntent().getStringExtra("mode");
        if (mode.equals("customer")){
            users_collection = db.collection("User");
        }
        else{
            users_collection = db.collection("Tailor");
        }
    }

    private void login_auth(String email, String password) {

        if (!TextUtils.isEmpty(email)
            && !TextUtils.isEmpty(password)){
            progressBar.setVisibility(View.VISIBLE);
            firebaseAuth
                    .signInWithEmailAndPassword(email,password)
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            current_user = firebaseAuth.getCurrentUser();
                            assert current_user != null;
                            String current_user_id = current_user.getUid();

                            users_collection
                                    .whereEqualTo("userId",current_user_id)
                                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                        @Override
                                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                                            @Nullable FirebaseFirestoreException e) {
                                            if (e!=null){
                                                Log.d("Login Activity", "onEvent: usercollection: "+ e.getMessage());
                                                return;
                                            }

                                            assert queryDocumentSnapshots != null;
                                            if (!queryDocumentSnapshots.isEmpty()){
                                                for (QueryDocumentSnapshot q:queryDocumentSnapshots) {
                                                    UserApI userApI = UserApI.getInstance();
                                                    userApI.setUserId(q.getString("userId"));
                                                    userApI.setUsername(q.getString("username"));
                                                }
                                                progressBar.setVisibility(View.INVISIBLE);
                                                Intent intent = new Intent(MainActivity.this, OrderList_tailor.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("Login Activity", "onFailure: Failure for login"+ e);
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(MainActivity.this, "Failed To Login Invalid Credentials", Toast.LENGTH_SHORT).show();
                        }
                    });
        }else {
            Toast.makeText(MainActivity.this, "Enter All Fields", Toast.LENGTH_SHORT).show();
        }
    }
}




