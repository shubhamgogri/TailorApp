package com.example.tailorapp.nav.marketplace.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tailorapp.R;
import com.example.tailorapp.nav.marketplace.model.Category;
import com.example.tailorapp.nav.marketplace.model.Item;
import com.example.tailorapp.nav.marketplace.model.ItemData;

import java.util.ArrayList;

import static com.example.tailorapp.R.layout.category_layout;

public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.CategoryViewHolder> {
    Context context;

    ArrayList<Category> categoryArrayList;

    public CategoryRecyclerAdapter(Context context, ArrayList<Category> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(category_layout,parent,false);
        return new CategoryViewHolder(context,view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        holder.category_title.setText(categoryArrayList.get(position).getCategory_title());
        ArrayList<Item> finalList = holder.itemArrayList(categoryArrayList.get(position).getItemList(),holder.current_page);
        holder.setCategory_recycler(holder.category_recycler, finalList);

        holder.prev.setEnabled(false);
        holder.next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.current_page += 1;
                ArrayList<Item> finalList = holder.itemArrayList(categoryArrayList.get(position).getItemList(),holder.current_page);
//                holder.setCategory_recycler(holder.category_recycler, finalList);
                holder.category_recycler.setAdapter(new
                        Recycler_view_marketplace(context,finalList));
                holder.toggle();
            }

        });
        holder.prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.current_page -=1;
                ArrayList<Item> finalList = holder.itemArrayList(categoryArrayList.get(position).getItemList(),holder.current_page);
//                holder.setCategory_recycler(holder.category_recycler, finalList);
                holder.category_recycler.setAdapter(new
                        Recycler_view_marketplace(context,finalList));
                holder.toggle();
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryArrayList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        public static final int TOTAL_ITEMS = 15;
        public static final int ITEMS_PER_PAGE = 4;
        public final int ITEMS_REMAINING = TOTAL_ITEMS % ITEMS_PER_PAGE;
        public  final int LAST_PAGE = TOTAL_ITEMS / ITEMS_PER_PAGE;


        private final Context context;
        private TextView category_title, view_more;
        private RecyclerView category_recycler;
        public int current_page = 0;
        private ImageView prev, next;
        private int total_pages = ItemData.TOTAL_ITEMS /ItemData.ITEMS_PER_PAGE;

        public CategoryViewHolder(Context ctx , @NonNull View itemView) {
            super(itemView);
            this.context = ctx;
            category_recycler = itemView.findViewById(R.id.category_recyclerview);
            prev = itemView.findViewById(R.id.prev_item_);
            next = itemView.findViewById(R.id.next_item_);
            category_title = itemView.findViewById(R.id.category_title);
            view_more = itemView.findViewById(R.id.view_more);
            view_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    TODO: intent to another activity to show all items
                }
            });
        }

        public void setCategory_recycler(RecyclerView category_recycler, ArrayList<Item> itemList) {
            Recycler_view_marketplace recycler_view_marketplace = new
                    Recycler_view_marketplace(context,itemList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            category_recycler.setLayoutManager(linearLayoutManager);
            category_recycler.setAdapter(recycler_view_marketplace);
        }

        public ArrayList<Item> itemArrayList(ArrayList<Item> arrayList,int current_page){
            int startItem = current_page * ITEMS_PER_PAGE +1;
            int sumOfData = ITEMS_PER_PAGE;

            ArrayList<Item> list = new ArrayList<>();
            if (current_page ==LAST_PAGE && ITEMS_REMAINING>0){
                for (int i = startItem; i <startItem+ITEMS_REMAINING ; i++) {
//                Add to List
                    Item item = arrayList.get(i);
//                    Item item = new Item(item_name_string[i],item_description_string[i],
//                            "item" +item_name_string[i].toLowerCase(),item_price_string[i]);
                    list.add(item);
                }
            }else {
                for (int i = startItem; i < startItem+sumOfData; i++) {
//                Add to List
//                    Item item = new Item(item_name_string[i],item_description_string[i],
//                            "item" +item_name_string[i].toLowerCase(),item_price_string[i]);
                    list.add(arrayList.get(i));
                }
            }
            return list;
        }

        private void toggle() {
            if (current_page == total_pages){
                next.setEnabled(false);
                prev.setEnabled(true);
            }else if (current_page == 0){
                prev.setEnabled(false);
                next.setEnabled(true);
            }else if (current_page>= 1 && current_page<= total_pages){
                prev.setEnabled(true);
                next.setEnabled(true);
            }
        }

    }
}
