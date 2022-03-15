package com.example.tailorapp.nav.cart;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.tailorapp.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private Button checkout;
    private TextView subtotal_text,total_text,delivery;
    private ConstraintLayout card_checkout;
    private TextView noItem;
    ArrayList<Cart_product> arrayList= CartArrayList.getCartItems();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        Log.d("Cart", "onCreate: "+ "Cart Activity");

        recyclerView = findViewById(R.id.cart_recycler);

        noItem = findViewById(R.id.no_item);
        checkout = findViewById(R.id.checkout);
        subtotal_text = findViewById(R.id.subtotal_cart);
        total_text = findViewById(R.id.total_cart);
        delivery = findViewById(R.id.delivery_cart);
        card_checkout = findViewById(R.id.checkout_card_cart);

        if (arrayList.isEmpty()){
            noItem.setVisibility(View.VISIBLE);
        }else {
            CartRecyclerAdapter cart_adapter = new CartRecyclerAdapter(CartActivity.this);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CartActivity.this);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(cart_adapter);
        }

        int subtotal = 0;
        if (!arrayList.isEmpty()){
            for (Cart_product cartproduct:arrayList) {
                Log.d("Cart items", "onCreate: " + cartproduct.getItem());
                int qty, price;
                qty = cartproduct.getQty();
                price = Integer.parseInt(cartproduct.getItem().getItem_price());
                subtotal = subtotal + qty*price;
            }
            subtotal_text.setText("₹ " + subtotal);
            delivery.setText("₹ " + "20");
            total_text.setText("₹ " + (subtotal+20));
        }

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent checkout = new Intent(CartActivity.this, Checkout.class);
                startActivity(checkout);
            }
        });

    }
}