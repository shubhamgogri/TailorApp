package com.example.tailorapp.nav.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.tailorapp.R;

public class ProductsFragment extends Fragment {

    private ProductsViewModel galleryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {

        galleryViewModel = new ViewModelProvider(this).get(ProductsViewModel.class);

        View root = inflater.inflate(R.layout.fragment_produts, container, false);

//        final TextView textView = root.findViewById(R.id.text_gallery);

//        galleryViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

//        RecyclerView recyclerView;
//        recyclerView = root.findViewById(R.id.men_recyclerview);
//        recyclerView.setHasFixedSize(true);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        recyclerView.setLayoutManager(linearLayoutManager);
//        Recycler_view_marketplace recyclerViewMarketplace = new
//                Recycler_view_marketplace(this.getContext(), ItemData.itemArrayList());
//        recyclerView.setAdapter(recyclerViewMarketplace);
//
//        RecyclerView women_recyclerView;
//        women_recyclerView = root.findViewById(R.id.women_recycler_view);
//        women_recyclerView.setHasFixedSize(true);
//        LinearLayoutManager women_linearLayoutManager = new LinearLayoutManager(this.getContext());
//        women_linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//
//        women_recyclerView.setLayoutManager(women_linearLayoutManager);
//        women_recyclerView.setAdapter(recyclerViewMarketplace);
//
//        RecyclerView kid_recyclerView;
//        kid_recyclerView = root.findViewById(R.id.kids_recycler_view);
//        kid_recyclerView.setHasFixedSize(true);
//        LinearLayoutManager kids = new LinearLayoutManager(this.getContext());
//        kids.setOrientation(LinearLayoutManager.HORIZONTAL);
//
//        kid_recyclerView.setLayoutManager(kids);
//        kid_recyclerView.setAdapter(recyclerViewMarketplace);
//
//        Log.d("MarketPlace", "onCreateView: " + ItemData.itemArrayList());
//        Log.d("MarketPlace", "onCreateView: "+ recyclerViewMarketplace.getItemCount());
        return root;
    }
}