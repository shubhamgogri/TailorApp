package com.example.tailorapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailorapp.nav.cart.OrderModel;

import java.io.Serializable;
import java.net.ContentHandler;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {
    private List<OrderModel> orderModelList = new ArrayList<>();
    private Context context;

    public OrderListAdapter(List<OrderModel> orderModelList, Context context) {
        this.orderModelList = orderModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.card_order, parent,false);
        return new ViewHolder(v,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderModel orderModel = orderModelList.get(position);
        if (orderModel.getItems() != null){
            if (orderModel.getOrder_date() != null) {
                String date = orderModel.getOrder_date();
                holder.title.setText(MessageFormat.format("{0}{1}", orderModel.getItems(),date));
            }
            holder.title.setText(MessageFormat.format("{0}{1}", orderModel.getItems(),orderModel.getOrder_date() ));
        }else {
            holder.title.setText(MessageFormat.format("{0}", "Orders"));
        }
        holder.customer_name.setText(orderModel.getName());
        holder.amount.setText(MessageFormat.format("₹ {0}", orderModel.getTotal_amount()));
        if (orderModel.getOrder_delivered() == false){
            holder.status.setText("Pending...");
        }else {
            holder.status.setText("Delivered!!");
        }

        holder.constraintLayout.setOnClickListener(v -> {
            Intent intent = new Intent(context, Order_Details.class);
            OrderAPI orderAPI = OrderAPI.getInstance();
            orderAPI.setOrderModel(orderModel);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private ImageView imageView;
        private TextView title, customer_name, amount, status;
        private ConstraintLayout constraintLayout;

        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            this.context = ctx;

            title = itemView.findViewById(R.id.order_title);
            customer_name = itemView.findViewById(R.id.order_cutomer_name);
            amount = itemView.findViewById(R.id.order_amount);
            status = itemView.findViewById(R.id.order_status);
            imageView = itemView.findViewById(R.id.order_imageView);
            constraintLayout= itemView.findViewById(R.id.card_constraint);

        }
    }
}
