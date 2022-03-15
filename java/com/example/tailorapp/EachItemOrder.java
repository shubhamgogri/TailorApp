package com.example.tailorapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tailorapp.nav.cart.Cart_product;
import com.example.tailorapp.nav.cart.OrderModel;
import com.example.tailorapp.nav.marketplace.model.Item;

import java.text.MessageFormat;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EachItemOrder#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EachItemOrder extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public EachItemOrder() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EachItemOrder.
     */
    // TODO: Rename and change types and number of parameters
    public static EachItemOrder newInstance(String param1, String param2) {
        EachItemOrder fragment = new EachItemOrder();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView item_name;
    private TextView item_details;
    private TextView item_qty;
    private TextView item_price;
    private ImageButton prev;
    private ImageButton next;
    private ImageView image;
    private OrderModel order;

    private int Counter = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    private void show_item(int Counter) {
        Cart_product cart_product = order.getCart_product_list().get(Counter);
        Item item = cart_product.getItem();
        item_name.setText(MessageFormat.format("Item: {0}", item.getItem_name()));
        item_details.setText(MessageFormat.format("Product Details: {0}", item.getDescription()));
        item_price.setText(MessageFormat.format("Item Price: {0}", item.getItem_price()));
        item_qty.setText(MessageFormat.format("Item Qty: {0}", cart_product.getQty()));
//        image.setImageResource(R.drawable.buttons);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_each_item_order, container, false);
        item_name = view.findViewById(R.id.frag_Item_name);
        item_details = view.findViewById(R.id.frag_prod_details);
        item_qty = view.findViewById(R.id.frag_qty_order);
        item_price = view.findViewById(R.id.frag_item_price);
        prev = view.findViewById(R.id.frag_prev_item);
        next = view.findViewById(R.id.frag_next_item);
        image = view.findViewById(R.id.frag_image_order);

        prev.setEnabled(false);
        OrderAPI orderAPI = OrderAPI.getInstance();
        order = orderAPI.getOrderModel();
        show_item(0);

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Counter += 1;
                if (Counter!=order.getCart_product_list().size() ){
                    show_item(Counter);
                    toggle(Counter);
                }
            }
        });

        prev.setOnClickListener(v -> {
            Counter -=1;
            show_item(Counter);
            toggle(Counter);
        });

        return view;
    }
    private void toggle( int Counter) {
        if (Counter == order.getCart_product_list().size()){
            next.setEnabled(false);
            prev.setEnabled(true);
        }else if (Counter == 0){
            prev.setEnabled(false);
            next.setEnabled(true);
        }else if (Counter >= 1 && Counter<= order.getCart_product_list().size()){
            prev.setEnabled(true);
            next.setEnabled(true);
        }
    }

}