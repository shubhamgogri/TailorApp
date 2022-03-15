package com.example.tailorapp.nav.marketplace.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailorapp.R;
import com.example.tailorapp.nav.marketplace.ProductDetails;
import com.example.tailorapp.nav.marketplace.model.Item;

import java.text.MessageFormat;
import java.util.ArrayList;

public class Recycler_view_marketplace extends RecyclerView.Adapter<Recycler_view_marketplace.ViewHolder>{
    private Context context;
    private ArrayList<Item> itemArrayList;

    public Recycler_view_marketplace(Context context, ArrayList<Item> list){
    this.context = context;
    this.itemArrayList = list;
    }

    @NonNull
    @Override
    public Recycler_view_marketplace.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_marketplace, parent,false);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item item = itemArrayList.get(position);
//        holder.imageView.setImageResource(R.drawable.buttons);
        holder.imageView.setImageResource(item.getItem_image(context));
        holder.item.setText(item.getItem_name());
        holder.description.setText(item.getDescription());
        holder.price.setText(MessageFormat.format("₹ {0}", item.getItem_price()));
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                    Todo: Show item details.
//                Intent intent = new Intent(context,newActivity.class)

//                String title = itemArrayList.get(position).getItem_name();
//                if (title.isEmpty()){
//                    Log.d("recycler", "onClick: "+ "title empty");
//                }

                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("title",itemArrayList.get(position).getItem_name());
                intent.putExtra("description", itemArrayList.get(position).getDescription());
                intent.putExtra("image", itemArrayList.get(position).getItem_image(context));
                intent.putExtra("price", itemArrayList.get(position).getItem_price());
                context.startActivity(intent);

            }
        });


    }

    @Override
    public int getItemCount() {
        return itemArrayList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView item, description, price;
        private LinearLayout card;

        public ViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            Context ctx = context;

            item  = itemView.findViewById(R.id.card_item_name);
            description = itemView.findViewById(R.id.card_item_description);
            price = itemView.findViewById(R.id.card_item_price);
            imageView = itemView.findViewById(R.id.image_card_view);
            card = itemView.findViewById(R.id.card_view);


        }
    }

}
