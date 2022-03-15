package com.example.tailorapp.nav.cart;

import com.example.tailorapp.nav.marketplace.model.Item;

public class Cart_product {
    private Item item ;
    private int qty;

    public Cart_product() {
    }

    public Cart_product(Item item, int qty) {
        this.item = item;
        this.qty = qty;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
