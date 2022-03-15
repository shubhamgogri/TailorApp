package com.example.tailorapp;

import com.example.tailorapp.login.UserApI;
import com.example.tailorapp.nav.cart.OrderModel;

public class OrderAPI {
    private OrderModel orderModel;

    private static OrderAPI instance;
    public static OrderAPI getInstance(){
        if (instance == null){
            instance = new OrderAPI();
        }
        return instance;
    }

    public OrderAPI() {
    }

    public OrderModel getOrderModel() {
        return orderModel;
    }

    public void setOrderModel(OrderModel orderModel) {
        this.orderModel = orderModel;
    }
}
