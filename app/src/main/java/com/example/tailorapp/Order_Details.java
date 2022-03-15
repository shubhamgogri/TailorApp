package com.example.tailorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorapp.nav.cart.OrderModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.text.MessageFormat;

public class Order_Details extends AppCompatActivity {
    private TextView order_id;
    private TextView order_date,order_cust_name,order_payment_method,order_address,order_total_amount;
    private Button order_complete_button;
    private FragmentContainerView item_fragment_container;

    OrderModel order = OrderAPI.getInstance().getOrderModel();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order__details);

        Log.d("Order_Details", "onCreate: " + order.getName());
        Log.d("Order_Details", "onCreate: " + order.getItems());
        Log.d("Order_Details", "onCreate: " + order.getPayment());
        Log.d("Order_Details", "onCreate: " + order.getTotal_amount());

        order_id = findViewById(R.id.order_id_order);
        order_date = findViewById(R.id.order_date);
        order_cust_name = findViewById(R.id.order_cust_name);
        order_payment_method = findViewById(R.id.order_payment_method);
        order_address = findViewById(R.id.order_address);
        order_total_amount = findViewById(R.id.order_total_amount);
        order_complete_button = findViewById(R.id.order_complete_button);
        item_fragment_container = findViewById(R.id.item_fragment_container);

        getSupportFragmentManager().beginTransaction().replace(item_fragment_container.getId(),new EachItemOrder()).commit();

        order_id.setText(MessageFormat.format("#{0}", order.getOrder_Id()));
        if (order.getOrder_date()!=null)
            order_date.setText(MessageFormat.format("Order Placed On {0}", order.getOrder_date()));

        order_cust_name.setText(MessageFormat.format("Customer Name: {0}", order.getName()));
        order_payment_method.setText(String.format("Payment Method: COD"));
        order_address.setText(MessageFormat.format("Shipping Address: {0}", order.getAddress()));
        order_total_amount.setText(MessageFormat.format("{0}{1}", "₹ ", order.getTotal_amount()));

        order_complete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                order.setOrder_delivered(true);
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                CollectionReference collectionReference = db.collection("Orders");
                collectionReference
                        .document(order.getOrder_Id())
                        .update(order.getOrder_delivered().toString(),true)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(Order_Details.this, "Delivery Completed", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("this", "onFailure: "+ e.getMessage() );
                                Toast.makeText(Order_Details.this, "Still Pending... due to error", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        if (order.getOrder_delivered()==true) {
            order_complete_button.setEnabled(false);
            order_complete_button.setText("Order Completed");
        }
    }
}