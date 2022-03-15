package com.example.tailorapp.nav.marketplace;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.example.tailorapp.R;
import com.example.tailorapp.nav.marketplace.adapter.CategoryRecyclerAdapter;
import com.example.tailorapp.nav.marketplace.model.Category;
import com.example.tailorapp.nav.marketplace.model.ItemData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MarketPlace_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MarketPlace_fragment extends Fragment {


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters

    private int current_page_first = 0;
    private int current_page_second = 0;
    private int current_page_third = 0;

    private int total_pages = ItemData.TOTAL_ITEMS /ItemData.ITEMS_PER_PAGE;

    public MarketPlace_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MarketPlace_fragment.
     */
    // TODO: Rename and change types and number of parameters

    public static MarketPlace_fragment newInstance(String param1, String param2) {
        MarketPlace_fragment fragment = new MarketPlace_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String mParam1 = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_market_place_fragment, container, false);

        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
//                ArrayList<Item> itemArrayList = ItemData.getAll();
                return false;
            }
        });

        RecyclerView main = view.findViewById(R.id.main_recycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

        main.setLayoutManager(linearLayoutManager);

        ArrayList<Category> category = new ArrayList<>();
        category.add(new Category("Men's Wear",ItemData.getAll()));
        category.add(new Category("WoMen's Wear",ItemData.getAll()));
        category.add(new Category("Kid's Wear",ItemData.getAll()));

        CategoryRecyclerAdapter categoryRecyclerAdapter = new CategoryRecyclerAdapter(getContext(),category);
        main.setAdapter(categoryRecyclerAdapter);

        return view;
    }

}