package com.example.tailorapp.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tailorapp.R;

public class Forgotpassword extends AppCompatActivity {
    private EditText password_text, confirm_password;
    private Button change_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        password_text = findViewById(R.id.password_text_forgot);
        confirm_password = findViewById(R.id.confirm_password_forgot);
        change_password = findViewById(R.id.sign_in_button);

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!password_text.getText().toString().isEmpty() &&
                !confirm_password.getText().toString().isEmpty()){
                    if (password_text.getText().toString().trim().equals(confirm_password.getText().toString().trim())){
                        Toast.makeText(Forgotpassword.this, "Password Changed", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(Forgotpassword.this, "Password don't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


    }
}