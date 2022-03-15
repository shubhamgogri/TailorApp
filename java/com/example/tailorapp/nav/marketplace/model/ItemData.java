package com.example.tailorapp.nav.marketplace.model;

import com.example.tailorapp.nav.marketplace.model.Item;

import java.util.ArrayList;

public class ItemData {
    public static final int TOTAL_ITEMS = 15;
    public static final int ITEMS_PER_PAGE = 4;
    public static final int ITEMS_REMAINING = TOTAL_ITEMS % ITEMS_PER_PAGE;
    public static final int LAST_PAGE = TOTAL_ITEMS / ITEMS_PER_PAGE;

    //    button, lace, Fabric
     static String[] item_name_string = {
        "Button","Fabric", "Lace",
        "Fabric", "Lace",
        "Button","Fabric","Button",
        "Fabric", "Lace", "Button",
        "Fabric", "Button",
        "Fabric", "Lace", "Lace"
    };

    static String[] item_description_string = {
            "Black ,Round ,Styled for Cotton Shirts",
            "Cream color, Best Material for Shirts and Pants",
            "Golden ,Embroidered ,For all Sarees",
            "Cream color, Best Material for Shirts and Pants",
            "Cream color, Best Material for Shirts and Pants",
            "Black ,Round ,Styled for Cotton Shirts",
            "Cream color, Best Material for Shirts and Pants",
            "Cream color, Best Material for Shirts and Pants",
            "Black ,Round ,Styled for Cotton Shirts",
            "Cream color, Best Material for Shirts and Pants",
            "Cream color, Best Material for Shirts and Pants",
            "Black ,Round ,Styled for Cotton Shirts",
            "Cream color, Best Material for Shirts and Pants",
            "Cream color, Best Material for Shirts and Pants",
            "Black ,Round ,Styled for Cotton Shirts",
            "Cream color, Best Material for Shirts and Pants",
    };
//    ₹
    static String[] item_price_string = {
            "1","50", "35","50", "50", "50",
            "10","50", "35","50", "50", "50",
            "10","50", "35","50"
    };

    public static ArrayList<Item> getAll(){
        ArrayList<Item> list = new ArrayList<>();
        for (int i = 0; i < item_name_string.length ; i++) {
            Item item = new Item(item_name_string[i],item_description_string[i],
                    "item" +item_name_string[i].toLowerCase(),item_price_string[i]);
            list.add(item);
        }
        return list;
    }


    public static ArrayList<Item> itemArrayList(int current_page){
        int startItem = current_page * ITEMS_PER_PAGE +1;
        int sumOfData = ITEMS_PER_PAGE;

        ArrayList<Item> list = new ArrayList<>();

        if (current_page ==LAST_PAGE && ITEMS_REMAINING>0){
            for (int i = startItem; i <startItem+ITEMS_REMAINING ; i++) {
//                Add to List
                    Item item = new Item(item_name_string[i],item_description_string[i],
                            "item" +item_name_string[i].toLowerCase(),item_price_string[i]);
                    list.add(item);
            }
        }else {
            for (int i = startItem; i < startItem+sumOfData; i++) {
//                Add to List
                Item item = new Item(item_name_string[i],item_description_string[i],
                        "item" +item_name_string[i].toLowerCase(),item_price_string[i]);
                list.add(item);
            }
        }
        return list;
    }


}