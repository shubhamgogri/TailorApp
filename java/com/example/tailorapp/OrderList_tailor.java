package com.example.tailorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorapp.nav.cart.OrderModel;
import com.example.tailorapp.newproduct.AddNewProduct;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class OrderList_tailor extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<OrderModel> orderModelList = new ArrayList<>();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Orders");
    private TextView no_Order;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list_tailor);

        no_Order = findViewById(R.id.no_order);
        recyclerView = findViewById(R.id.orders_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OrderListAdapter orderListAdapter = new OrderListAdapter(orderModelList,OrderList_tailor.this);
        fab = findViewById(R.id.new_product);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OrderList_tailor.this, AddNewProduct.class);
                startActivity(intent);
            }
        });
        collectionReference
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.isEmpty()){
                            for (QueryDocumentSnapshot querySnapshot :queryDocumentSnapshots ) {
                                OrderModel order = querySnapshot.toObject(OrderModel.class);
                                orderModelList.add(order);
                            }
                            recyclerView.setAdapter(orderListAdapter);
                            orderListAdapter.notifyDataSetChanged();
                        }else{
                            no_Order.setVisibility(View.VISIBLE);
                            Toast.makeText(OrderList_tailor.this, "", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        no_Order.setVisibility(View.VISIBLE);
                        Log.d("Order_List_Tailor_activity", "onFailure: "+ e.getMessage());
                    }
                });
        orderListAdapter.notifyDataSetChanged();
    }
}