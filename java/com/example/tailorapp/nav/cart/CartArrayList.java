package com.example.tailorapp.nav.cart;

import java.util.ArrayList;

public class CartArrayList{
    public static CartArrayList instance;

    public static CartArrayList getInstance(){
        if (instance == null){
            instance = new CartArrayList();
        }
        return instance;
    }
    public CartArrayList() {
    }

    public static ArrayList<Cart_product> cartItems = new ArrayList<>();

    public void insert_item(Cart_product cart_product){
        cartItems.add(cart_product);
    }


    public static ArrayList<Cart_product> getCartItems() {
        return cartItems;
    }

    public int get_size(){
        return cartItems.size();
    }

    public void delete(int position){
        cartItems.remove(position);
    }

    public Cart_product get_item(int position){
        return cartItems.get(position);
    }

    public int get_qty(int position){
        return cartItems.get(position).getQty();
    }
    public Cart_product add_qty(int position){
        int qty = cartItems.get(position).getQty();
        cartItems.get(position).setQty(qty+1);
        return cartItems.get(position);
    }

//    public void min(int position){
//        Cart_product cart_product = cartItems.get(position);
//        int qty = cart_product.getQty();
//        if (qty >= 1){
//            qty -= 1;
//            if (qty == 0){
//                cartItems.remove(cart_product);
//            }
//        }
//    }

    public int minus_qty(int position){
        if (!cartItems.isEmpty()){
            int qty;
            qty = cartItems.get(position).getQty();
            if (qty>0) {
                qty -= 1;
                cartItems.get(position).setQty(qty);
            }
            return qty;
//            else{
//            cartItems.remove(position);
//            }
        }
                return 0;
    }

}
