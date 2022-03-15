package com.example.tailorapp.nav.marketplace;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tailorapp.R;
import com.example.tailorapp.nav.cart.CartActivity;
import com.example.tailorapp.nav.cart.CartArrayList;
import com.example.tailorapp.nav.cart.Cart_product;
import com.example.tailorapp.nav.marketplace.model.Item;


public class ProductDetails extends AppCompatActivity {
    private TextView item_details_title,prod_details, item_details_detail,item_detail_price;
    private Button wishlist,add_to_cart;
    private ImageView item_detail_image, cart;
    private String title, detail,price;
    private int image_res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        prod_details = findViewById(R.id.prod_details);
        item_detail_image = findViewById(R.id.item_detail_image);
        item_detail_price = findViewById(R.id.item_detail_price);
        item_details_title = findViewById(R.id.item_details_title);
        item_details_detail = findViewById(R.id.prd);
        add_to_cart = findViewById(R.id.add_to_cart);
        wishlist = findViewById(R.id.wishlist);
        cart = findViewById(R.id.cart);

        Bundle bundle = getIntent().getExtras();

        assert bundle != null;
        Log.d("Product", "onCreate: "+ (bundle.getString("title", "title")));
        Log.d("Product", "onCreate: "+ bundle.getString("description"));
        Log.d("Product", "onCreate: "+ bundle.getString("price"));
        Log.d("Product", "onCreate: "+ bundle.getInt("image"));
        Log.d("Product", "onCreate: "+ bundle.getString("title"));

        title = bundle.getString("title");
        detail = bundle.getString("description");
        price = bundle.getString("price");
        image_res = bundle.getInt("image");

        item_details_title.setText(title);
        item_details_detail.setText(detail);
        prod_details.setText(detail);
        item_detail_image.setImageResource(image_res);
        item_detail_price.setText(String.format("₹ %s", price));

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cart = new Intent(ProductDetails.this, CartActivity.class);
                startActivity(cart);
            }
        });

        add_to_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartArrayList  cart = CartArrayList.getInstance();
                Cart_product cart_product = new Cart_product();
                cart_product.setItem(new Item(title,detail,String.valueOf(image_res),price));
                cart_product.setQty(1);
                cart.insert_item(cart_product);
                Toast.makeText(ProductDetails.this, "Item Added to Cart", Toast.LENGTH_SHORT).show();

//                Intent cart_intent = new Intent(ProductDetails.this, CartActivity.class);
//                cart_intent.putExtra("title",title);
//                cart_intent.putExtra("detail", detail);
//                cart_intent.putExtra("price",price);
//                cart_intent.putExtra("image_res",image_res);
//                startActivity(cart_intent);

            }
        });


    }
}