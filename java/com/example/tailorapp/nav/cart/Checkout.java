package com.example.tailorapp.nav.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tailorapp.R;

import java.text.MessageFormat;
import java.util.ArrayList;

public class Checkout extends AppCompatActivity {
    private static final String TAG = "Checkout Activity";
    private TextView prod_details, price ,qty, individual_subtotal, subtotal, tax, delivery, total;
    private Button continue_button;
    private  ArrayList<Cart_product> cartArrayList = CartArrayList.getCartItems();

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        subtotal = findViewById(R.id.subtotal_checkout);
        total = findViewById(R.id.total_checkout);
        tax = findViewById(R.id.tax_checkout);
        delivery = findViewById(R.id.delivery_checkout);
        continue_button = findViewById(R.id.contiue_checkout);
//        recyclerView = findViewById(R.id.recycler_checkout);

        float int_total = 0,sub_total = 0;
        for (Cart_product cart_product:cartArrayList) {
            int qty = cart_product.getQty();
            int price = Integer.parseInt(cart_product.getItem().getItem_price());
            sub_total += (qty*price);
        }
        subtotal.setText(MessageFormat.format("₹ {0} ", sub_total));
        float int_tax = (float) (sub_total*0.18);
        tax.setText(MessageFormat.format("₹ {0} ", int_tax));
        float del_charge = (float) (sub_total*0.05);
        delivery.setText(MessageFormat.format("₹ {0}", del_charge ));
        int_total = sub_total+int_tax+del_charge;

        total.setText(MessageFormat.format("₹ {0} ", int_total));

        float finalInt_total = int_total;

        continue_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent continue_intent = new Intent(Checkout.this,AddressContinue.class);
                continue_intent.putExtra("Total", finalInt_total);
                startActivity(continue_intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recyclerView = findViewById(R.id.recycler_checkout);
        CheckoutArrayAdapter checkoutArrayAdapter = new CheckoutArrayAdapter(Checkout.this, cartArrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(Checkout.this));
        recyclerView.setAdapter(checkoutArrayAdapter);

    }
}