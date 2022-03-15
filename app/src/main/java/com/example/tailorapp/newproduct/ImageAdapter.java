package com.example.tailorapp.newproduct;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailorapp.R;

import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    List<Uri> uris;
    Context context;

    public ImageAdapter(List<Uri> uris, Context context) {
        this.uris = uris;
        this.context = context;
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.multipleimages,null);
        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder holder, int position) {
        Uri uri = uris.get(position);
        holder.imageView.setImageURI(uri);
        Log.d("AddnewProductRecycler", "onBindViewHolder: "+ uri );
    }

    @Override
    public int getItemCount() {
        return uris.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        Context context;
        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            this.context = ctx;

            imageView = itemView.findViewById(R.id.recycler_images);
        }
    }
}
