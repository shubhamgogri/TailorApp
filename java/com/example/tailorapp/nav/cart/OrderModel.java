package com.example.tailorapp.nav.cart;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OrderModel {
    private String Name;
    private String Txn_id;
    private String Items;
    private ArrayList<Cart_product> cart_product_list;
    private String Address;
    private String Order_Id;
    private String Total_amount;
    private Boolean Payment;
    private String Payment_method;
    private String order_date;
    private Boolean order_delivered = false;
//    String username;


    public OrderModel() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTxn_id() {
        return Txn_id;
    }

    public void setTxn_id(String txn_id) {
        Txn_id = txn_id;
    }

    public String getItems() {
        return Items;
    }

    public void setItems(String items) {
        Items = items;
    }

    public ArrayList<Cart_product> getCart_product_list() {
        return cart_product_list;
    }

    public void setCart_product_list(ArrayList<Cart_product> cart_product_list) {
        this.cart_product_list = cart_product_list;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getOrder_Id() {
        return Order_Id;
    }

    public void setOrder_Id(String order_Id) {
        Order_Id = order_Id;
    }

    public String getTotal_amount() {
        return Total_amount;
    }

    public void setTotal_amount(String total_amount) {
        Total_amount = total_amount;
    }

    public Boolean getPayment() {
        return Payment;
    }

    public void setPayment(Boolean payment) {
        Payment = payment;
    }

    public String getPayment_method() {
        return Payment_method;
    }

    public void setPayment_method(String payment_method) {
        Payment_method = payment_method;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public Boolean getOrder_delivered() {
        return order_delivered;
    }

    public void setOrder_delivered(Boolean order_delivered) {
        this.order_delivered = order_delivered;
    }
}
