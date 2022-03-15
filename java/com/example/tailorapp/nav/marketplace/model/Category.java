package com.example.tailorapp.nav.marketplace.model;

import java.util.ArrayList;

public class Category {
    private String category_title;
    private ArrayList<Item> itemList;

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public ArrayList<Item> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<Item> itemList) {
        this.itemList = itemList;
    }

    public Category(String category_title, ArrayList<Item> itemList) {
        this.category_title = category_title;
        this.itemList = itemList;
    }
}
