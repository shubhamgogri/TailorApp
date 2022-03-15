package com.example.tailorapp.registration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tailorapp.R;
import com.example.tailorapp.login.MainActivity;

public class Otp_Registration extends AppCompatActivity {
    private EditText phone, otp;
    private Button verify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_registration);

        phone = findViewById(R.id.phone_text);
        otp = findViewById(R.id.otp_text);
        verify = findViewById(R.id.verify_button);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!phone.getText().toString().trim().isEmpty() &&
                !otp.getText().toString().isEmpty()){
                    Toast.makeText(Otp_Registration.this, "Verified", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Otp_Registration.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}