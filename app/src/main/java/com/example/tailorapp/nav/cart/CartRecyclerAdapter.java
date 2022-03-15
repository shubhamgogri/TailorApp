package com.example.tailorapp.nav.cart;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailorapp.R;

import java.text.MessageFormat;
import java.util.ArrayList;

import static java.lang.String.format;

public class CartRecyclerAdapter extends RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder> {
    Context context;
    CartArrayList cartArrayList = CartArrayList.getInstance();
    ArrayList<Cart_product> arrayList= CartArrayList.getCartItems();


    public CartRecyclerAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CartRecyclerAdapter.CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_card, parent,false);
        return new CartViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerAdapter.CartViewHolder holder, int position) {

        holder.cart_card_image.setImageResource(arrayList.get(position).getItem().getItem_image(context));
        holder.cart_card_item_price.setText(MessageFormat.format("₹ {0}", arrayList.get(position).getItem().getItem_price()));
        holder.cart_card_desc.setText(format("%s\n%s", arrayList.get(position).getItem().getItem_name(),
                arrayList.get(position).getItem().getDescription()));
        holder.cart_card_multiplier.setText(MessageFormat.format("x {0}", cartArrayList.get_item(position).getQty()));
        Cart_product cart_product = cartArrayList.get_item(position);
        int price = Integer.parseInt(cart_product.getItem().getItem_price());
        int qty = cart_product.getQty();
        int Subtotal = price*qty;
        holder.cart_card_total_amount_card.setText(MessageFormat.format("₹ {0}", String.valueOf(Subtotal)));

        holder.cart_card_minus.setEnabled(false);

        holder.cart_card_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cart_product cart_product = cartArrayList.add_qty(position);
                holder.cart_card_minus.setEnabled(true);
                holder.cart_card_multiplier.setText(MessageFormat.format("x {0}", cart_product.getQty()));
                int price = Integer.parseInt(cart_product.getItem().getItem_price());
                int qty = cart_product.getQty();
                int Subtotal = price*qty;
                holder.cart_card_total_amount_card.setText(MessageFormat.format("₹ {0}", String.valueOf(Subtotal)));
//                holder.cart_card_multiplier.setText(MessageFormat.format("x {0}", cartArrayList.get_item(position).getQty()));
            }
        });

        holder.cart_card_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = cartArrayList.minus_qty(position);
                if (qty == 0){
                    cartArrayList.delete(position);
                }else {
                    holder.cart_card_multiplier.setText(MessageFormat.format("x {0}", qty));
                    int price = Integer.parseInt(cartArrayList.get_item(position).getItem().getItem_price());
//                    int qty = cart_product.getQty();
                    int Subtotal = price*qty;
                    holder.cart_card_total_amount_card.setText(MessageFormat.format("₹ {0}", String.valueOf(Subtotal)));
                }
            }
        });
        holder.cart_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cartArrayList.delete(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartArrayList.get_size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        Context context;
        ImageView cart_card_image, cart_card_plus,cart_card_minus, cart_delete;
        TextView cart_card_desc, cart_card_item_price, cart_card_multiplier, cart_card_total_amount_card;

        public CartViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            this.context = ctx;

            cart_delete = itemView.findViewById(R.id.cart_card_delete);
            cart_card_total_amount_card = itemView.findViewById(R.id.cart_card_total_amount_card);
            cart_card_image = itemView.findViewById(R.id.cart_card_image);
            cart_card_desc = itemView.findViewById(R.id.cart_card_desc);
            cart_card_item_price = itemView.findViewById(R.id.cart_card_item_count);
            cart_card_multiplier = itemView.findViewById(R.id.cart_card_multiplier);
            cart_card_minus = itemView.findViewById(R.id.cart_card_minus);
            cart_card_plus = itemView.findViewById(R.id.cart_card_plus);
        }
    }
}
