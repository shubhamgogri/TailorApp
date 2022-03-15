package com.example.tailorapp.nav.marketplace.model;
import android.content.Context;

public class Item {
    private String item_name, description, item_image, item_price;

    public Item(){
    }
    public Item(String item_name, String description, String item_image, String item_price) {
        this.item_name = item_name;
        this.description = description;
        this.item_image = item_image;
        this.item_price = item_price;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getItem_image(Context context) {
        return context.getResources().getIdentifier(this.item_image, "drawable", context.getPackageName());
    }

    public void setItem_image(String item_image) {
        this.item_image = item_image;
    }

    public String getItem_price() {
        return item_price;
    }

    public void setItem_price(String item_price) {
        this.item_price = item_price;
    }
}
