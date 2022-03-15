package com.example.tailorapp.nav.cart;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailorapp.R;

import java.text.MessageFormat;
import java.util.ArrayList;

public class CheckoutArrayAdapter extends RecyclerView.Adapter<CheckoutArrayAdapter.ViewHolder> {
    Context context;
    ArrayList<Cart_product> cart_productArrayList;

    public CheckoutArrayAdapter(@NonNull Context context, ArrayList<Cart_product> cart_productArrayList) {
        this.context = context;
        this.cart_productArrayList = cart_productArrayList;
        Log.d("Checkout_array_adapter", "CheckoutArrayAdapter: " + "Constructor");
    }

    @NonNull
    @Override
    public CheckoutArrayAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.checkout_cart, parent,false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull CheckoutArrayAdapter.ViewHolder holder, int position) {

        int int_qty = cart_productArrayList.get(position).getQty();
        int int_price= Integer.parseInt(cart_productArrayList.get(position).getItem().getItem_price());
        int int_subtotal = int_price*int_qty;

        Log.d("Ck", "onBindViewHolder: " + MessageFormat.format("{0}\n {1}",
                cart_productArrayList.get(position).getItem().getItem_name(),
                cart_productArrayList.get(position).getItem().getDescription()) + " "+ int_price
                + " "+int_qty);

        holder.prod_details.setText(MessageFormat.format("{0}\n{1}", cart_productArrayList.get(position).getItem().getItem_name(), cart_productArrayList.get(position).getItem().getDescription()));
        holder.price.setText(MessageFormat.format("₹ {0} ", cart_productArrayList.get(position).getItem().getItem_price()));
        holder.qty.setText(MessageFormat.format("{0} ", cart_productArrayList.get(position).getQty()));
        holder.individual_subtotal.setText(MessageFormat.format("₹ {0} ", String.valueOf(int_subtotal)));

    }

    @Override
    public int getItemCount() {
        return cart_productArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView prod_details, price ,qty, individual_subtotal;
        public ViewHolder(@NonNull View view, Context context) {
            super(view);
            this.context = context;

            price = view.findViewById(R.id.checkout_price);
            prod_details = view.findViewById(R.id.checkout_prod_detail);
            qty = view.findViewById(R.id.checkout_qty);
            individual_subtotal = view.findViewById(R.id.individual_subtotal_checkout);
        }
    }
}
