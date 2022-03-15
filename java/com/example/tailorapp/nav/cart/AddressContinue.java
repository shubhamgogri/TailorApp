package com.example.tailorapp.nav.cart;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorapp.R;

import java.text.MessageFormat;

public class AddressContinue extends AppCompatActivity implements View.OnClickListener {
    private TextView total, name, address;
    private Button new_address, continue_address;
    private static final int RequestCode = 15;
    private ImageButton edit_address, delete_address;
    private RelativeLayout relativeLayout;
    private String add,landmark, state, pin,full, name_data;
    private float Total_int;
//    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_continue);

        new_address = findViewById(R.id.new_address);
        total = findViewById(R.id.textView18);
        name = findViewById(R.id.name_text_view);
        address = findViewById(R.id.address_detail);
        continue_address = findViewById(R.id.continue_address);
        edit_address = findViewById(R.id.edit_address);
        delete_address = findViewById(R.id.delete_address);


        continue_address.setEnabled(false);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        Total_int = (float) bundle.get("Total");
        total.setText(MessageFormat.format("Total:- ₹ {0} ", bundle.get("Total")));

        new_address.setOnClickListener(this);
        continue_address.setOnClickListener(this);
        edit_address.setOnClickListener(this);
        delete_address.setOnClickListener(this);

        edit_address.setEnabled(false);
        delete_address.setEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        TODO: ON RESULT show the address card.
        if (requestCode == RequestCode){
            relativeLayout = findViewById(R.id.address_card);
            relativeLayout.setVisibility(View.VISIBLE);
            Bundle bundle_data = data.getExtras();
            name_data = String.valueOf(bundle_data.get("name"));
            name.setText(name_data);
            add = String.valueOf(bundle_data.get("address"));
            landmark = String.valueOf(bundle_data.get("landmark"));
            state = String.valueOf(bundle_data.get("state"));
            pin = String.valueOf(bundle_data.get("pincode"));
            full = MessageFormat.format("{0}, {1}, {2}, {3}", add, landmark, pin, state);
            address.setText(full);
            new_address.setVisibility(View.INVISIBLE);
            continue_address.setEnabled(true);
            edit_address.setEnabled(true);
            delete_address.setEnabled(true);

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.new_address:
                Intent new_address_intent = new Intent(AddressContinue.this, NewAddress.class);
                startActivityForResult(new_address_intent,RequestCode);
                break;

            case R.id.edit_address:
//                TODO: AlertDialog
                Toast.makeText(this, "Alert Dialog for editing the address", Toast.LENGTH_SHORT).show();
                edit_add();
                break;

            case R.id.delete_address:
//                TODO: Delete
                relativeLayout.setVisibility(View.INVISIBLE);
                new_address.setVisibility(View.VISIBLE);
                continue_address.setEnabled(false);
                add= "";
                landmark = "";
                state = "";
                pin= "";
                full= "";
                name_data = "";
                break;

            case R.id.continue_address:
//                ToDo: intent to payment.
                Intent payment_intent = new Intent(AddressContinue.this,Payment.class);
                payment_intent.putExtra("Total",Total_int);
                payment_intent.putExtra("Name",name_data);
                payment_intent.putExtra("Address",full);
                startActivity(payment_intent);
                Toast.makeText(this, "Intent to Payment Activity", Toast.LENGTH_SHORT).show();
            break;
        }
    }

    private void edit_add() {
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View popup = getLayoutInflater().inflate(R.layout.popup_address, null);
        EditText add_popup = popup.findViewById(R.id.popup_add);
        add_popup.setText(full);
        EditText name_popup = popup.findViewById(R.id.popup_name);
        name_popup.setText(name_data);
        Button save_popup = popup.findViewById(R.id.popup_save);

        builder.setView(popup);
        alertDialog = builder.create();
        alertDialog.show();

        save_popup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!add_popup.getText().toString().isEmpty() &&
                        !name_popup.getText().toString().isEmpty()){
                    add = add_popup.getText().toString().trim();
                    full = add;
                    name_data = name_popup.getText().toString().trim();
                    alertDialog.dismiss();
                    name.setText(name_data);
                    address.setText(add);

                }else {
                    Toast.makeText(AddressContinue.this, "Empty Fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}