package com.example.tailorapp.registration;

import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class NewProductModel {

    private String username;
    private String userId;
    private String Title;
    private String Description;
    private String Qty;
    private String Price;
    private String Discount;
    private String Material_Details;
    private String Category;
    private String SubCategory;
    private String Type_Cloth;
    private int color;
    private List<String> Size = new ArrayList<>();
    private List<String> Image = new ArrayList<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return Title;
    }

    public String getMaterial_Details() {
        return Material_Details;
    }

    public void setMaterial_Details(String material_Details) {
        Material_Details = material_Details;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getDiscount() {
        return Discount;
    }

    public void setDiscount(String discount) {
        Discount = discount;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getSubCategory() {
        return SubCategory;
    }

    public void setSubCategory(String subCategory) {
        SubCategory = subCategory;
    }

    public String getType_Cloth() {
        return Type_Cloth;
    }

    public void setType_Cloth(String type_Cloth) {
        Type_Cloth = type_Cloth;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public List<String> getSize() {
        return Size;
    }

    public void setSize(List<String> size) {
        Size = size;
    }

    public NewProductModel(String username, String userId, String title, String description, String qty, String price, String discount, String material_Details, String category, String subCategory, String type_Cloth, int color, List<String> size, List<String> image) {
        this.username = username;
        this.userId = userId;
        Title = title;
        Description = description;
        Qty = qty;
        Price = price;
        Discount = discount;
        Material_Details = material_Details;
        Category = category;
        SubCategory = subCategory;
        Type_Cloth = type_Cloth;
        this.color = color;
        Size = size;
        Image = image;
    }

    public List<String> getImage() {
        return Image;
    }

    public void setImage(List<String> image) {
        Image = image;
    }

    public NewProductModel() {
    }

}
