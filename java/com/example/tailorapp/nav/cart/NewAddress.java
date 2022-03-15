package com.example.tailorapp.nav.cart;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.tailorapp.R;

public class NewAddress extends AppCompatActivity {
    private EditText Name, Phone_no, Alternate_phone_no, address, landmark,pincode,state, country;
    private Button save_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);

        Name = findViewById(R.id.editTextTextPersonName);
        Phone_no = findViewById(R.id.editTextPhone3);
        Alternate_phone_no = findViewById(R.id.editTextPhone);
        address = findViewById(R.id.editTextTextPostalAddress);
        landmark = findViewById(R.id.editTextTextPostalAddress2);
        pincode = findViewById(R.id.editTextNumber);
        state = findViewById(R.id.editTextTextPersonName2);
        country = findViewById(R.id.editTextTextPersonName3);
        save_address = findViewById(R.id.save_address);


        save_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if (!Name.getText().toString().isEmpty() && !Phone_no.getText().toString().isEmpty() &&
                !address.getText().toString().isEmpty() && !landmark.getText().toString().isEmpty() &&
                !pincode.getText().toString().isEmpty() && !state.getText().toString().isEmpty() &&
                !country.getText().toString().isEmpty()){
                    intent.putExtra("name",Name.getText().toString().trim());
                    intent.putExtra("phone_no",Phone_no.getText().toString().trim());
                    intent.putExtra("alternate_phone_no",Alternate_phone_no.getText().toString().trim());
                    intent.putExtra("address", address.getText().toString().trim());
                    intent.putExtra("landmark",landmark.getText().toString().trim());
                    intent.putExtra("pincode",pincode.getText().toString().trim());
                    intent.putExtra("state",state.getText().toString().trim());
                    intent.putExtra("country",state.getText().toString().trim());
                    setResult(RESULT_OK,intent);
                    finish();
                }else {
                    Toast.makeText(NewAddress.this, "Please Enter All Details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}