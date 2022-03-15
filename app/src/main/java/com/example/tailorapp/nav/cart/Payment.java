package com.example.tailorapp.nav.cart;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorapp.OrderList_tailor;
import com.example.tailorapp.R;
import com.example.tailorapp.login.UserApI;
import com.example.tailorapp.nav.Myshopmain;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class Payment extends AppCompatActivity implements PaymentResultListener {
    private LinearLayout top, order_ll, detail_ll;
    private RadioGroup radioGroup;
    private RadioButton cod, online;
    private int cod_bool;
    private Button final_payment, browse, all_orders;
    private TextView total, name_text,order_id, product_details, total_items,total_amount_last,address_order;
    private ConstraintLayout footer;
    private ArrayList<Cart_product> cartArrayList = CartArrayList.getCartItems();
    Bundle bundle;

    private static long Order_ID = 100000;
    private String upi_id = "upid@bank";
    private String upi_name = "name";

    private static final String GPAY_PACKAGE_NAME = "com.google.android.apps.nbu.paisa.user";
    private static final int GPAY_REQUEST_CODE = 100;

    private static final int PAYTM_REQUEST_CODE = 155;
    private static final String PAYTM_PACKAGE_NAME = "net.one97.paytm";

    private String currentUserId;
    private String currentUsername;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Orders");
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        browse = findViewById(R.id.browse_prod);
        all_orders = findViewById(R.id.all_orders);
        order_ll = findViewById(R.id.order_ll);
        top = findViewById(R.id.top_ll);
        detail_ll = findViewById(R.id.detail_order_ll);
        footer = findViewById(R.id.footer_payment);
        name_text = findViewById(R.id.product_details_name);
        order_id = findViewById(R.id.order_id);
        product_details = findViewById(R.id.product_details);
        total_items = findViewById(R.id.prouct_qty);
        total_amount_last = findViewById(R.id.total_amount_last);
        address_order = findViewById(R.id.address_order);

        radioGroup = findViewById(R.id.radio_group);
        final_payment= findViewById(R.id.final_paymnet);
        total = findViewById(R.id.total_payment);
        cod = findViewById(R.id.cod);
        online = findViewById(R.id.online);
        bundle = getIntent().getExtras();
        assert bundle != null;
        total.setText(String.valueOf(bundle.get("Total")));
//        total.setText(MessageFormat.format("Total:- ₹ {0} ", bundle.get("Total")));

        firebaseAuth = FirebaseAuth.getInstance();

        if (UserApI.getInstance()!= null){
            currentUserId = UserApI.getInstance().getUserId();
            currentUsername = UserApI.getInstance().getUsername();
        }

        authStateListener = firebaseAuth -> user = firebaseAuth.getCurrentUser();

        radioGroup.setOnClickListener(v -> onRadioButtonClicked(v));

        final_payment.setOnClickListener(v -> {

            if (cod.isChecked()){
                Toast.makeText(Payment.this, "Order Placed using Cash on Delivery", Toast.LENGTH_SHORT).show();
                visiblity();

                int total_qty = 0;
                StringBuilder product_titles = new StringBuilder();

                for (Cart_product cart_product:cartArrayList) {
                    int qty = cart_product.getQty();
                    total_qty+= qty;
                    product_titles.append(cart_product.getItem().getItem_name()).append(" , ");
                }

                name_text.setText(MessageFormat.format("Name:- {0}", bundle.get("Name")));
                order_id.setText("Order Id:- 100001");
                product_details.setText(MessageFormat.format("Items:- {0}", product_titles));
                total_items.setText(MessageFormat.format("Total No. of Items:- {0}", total_qty));
                total_amount_last.setText(String.valueOf(bundle.get("Total")));
                address_order.setText(MessageFormat.format("Address:- {0}", bundle.get("Address")));

                OrderModel order = new OrderModel();
                order.setName(String.valueOf(bundle.get("Name")));
                order.setItems(String.valueOf(product_titles));
                order.setCart_product_list(cartArrayList);
                order.setTotal_amount(String.valueOf(bundle.get("Total")));
                order.setAddress(String.valueOf(bundle.get("Address")));
                order.setPayment(false);
                order.setPayment_method("Cash On Delivery");
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Date date= new Date();
                order.setOrder_date(simpleDateFormat.format(date));
                save_into_firebase(order);


            }else {
                payment_dialog();
                Toast.makeText(Payment.this, "Intent to Paying APi", Toast.LENGTH_SHORT).show();
            }

        });

        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Payment.this, Myshopmain.class);
                startActivity(intent);
            }
        });

        all_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent to all orders activity
                Intent intent = new Intent(Payment.this, OrderList_tailor.class);
                startActivity(intent);
            }
        });
    }

    private void payment_dialog(){
        AlertDialog alertDialog;
        AlertDialog.Builder builder;
        View view = LayoutInflater.from(Payment.this).inflate(R.layout.popup_option_payment,null);
        builder =new AlertDialog.Builder(Payment.this);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();

        Button gpay = view.findViewById(R.id.Gpay_button);
        Button paytm = view.findViewById(R.id.paytm_button);
        Button razorpay = view.findViewById(R.id.razorpay_button2);

        gpay.setOnClickListener(v -> {
            gpay_payment(GPAY_PACKAGE_NAME);
            alertDialog.dismiss();
        });

        paytm.setOnClickListener(v -> {
            paytm_payment(PAYTM_PACKAGE_NAME);
            alertDialog.dismiss();
        });

        razorpay.setOnClickListener(v -> {
            razorpay_payment();
            alertDialog.dismiss();
        });

    }

    private void razorpay_payment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("rzp_test_NGMXcLkYVp0L7r");

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", "Shubham Gogri");
            jsonObject.put("description","Testing Note");
            jsonObject.put("currency","INR");
            jsonObject.put("amount", 5000);
//            jsonObject.put("amount", bundle.get("Total"));
            jsonObject.put("prefill.contact","9699957337");
            jsonObject.put("prefill.email","gogrishubham10@gmail.com");
            checkout.open(Payment.this,jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void paytm_payment(String paytmPackageName) {
        if (isAppIntalled(this,paytmPackageName)){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(get_paytm_uri());
            intent.setPackage(paytmPackageName);
            startActivityForResult(intent,PAYTM_REQUEST_CODE);
        }else {
            Toast.makeText(this, "App is Not Installed, Please Install or select another Option", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data!=null){
            if (requestCode == GPAY_REQUEST_CODE){
                if (resultCode == RESULT_OK){
//                    if (Objects.requireNonNull(data.getStringExtra("status")).toLowerCase().equals("success")) {
                    visiblity();
                    int total_qty = 0;
                    StringBuilder product_titles = new StringBuilder();

                    for (Cart_product cart_product : cartArrayList) {
                        int qty = cart_product.getQty();
                        total_qty += qty;
                        product_titles.append(cart_product.getItem().getItem_name()).append(" , ");
                    }

                    name_text.setText(MessageFormat.format("Name:- {0}", bundle.get("Name")));
                    order_id.setText(MessageFormat.format("Transaction Id:- {0}", data.getStringExtra("txnRef")));
                    product_details.setText(MessageFormat.format("Items:- {0}", product_titles));
                    total_items.setText(MessageFormat.format("Total No. of Items:- {0}", total_qty));
                    total_amount_last.setText(String.valueOf(bundle.get("Total")));
                    address_order.setText(MessageFormat.format("Address:- {0}", bundle.get("Address")));
                    Toast.makeText(this, "Payment Done using Gpay", Toast.LENGTH_SHORT).show();

                    OrderModel order = new OrderModel();
                    order.setName(String.valueOf(bundle.get("Name")));
                    order.setItems(String.valueOf(product_titles));
                    order.setCart_product_list(cartArrayList);
                    order.setTotal_amount(String.valueOf(bundle.get("Total")));
                    order.setAddress(String.valueOf(bundle.get("Address")));
                    order.setPayment(true);
                    order.setPayment_method("GPay");
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date= new Date();
                    order.setOrder_date(simpleDateFormat.format(date));
                    order.setTxn_id(data.getStringExtra("txnRef"));
                        save_into_firebase(order);

//                    }
                }else{
                    Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                }

            }
            if (requestCode == PAYTM_REQUEST_CODE){
                if (Objects.requireNonNull(data.getStringExtra("Status")).toLowerCase().equals("success")) {
                    Toast.makeText(this, "Payment Done using Paytm", Toast.LENGTH_SHORT).show();

                    visiblity();
                    int total_qty = 0;
                    StringBuilder product_titles = new StringBuilder();

                    for (Cart_product cart_product : cartArrayList) {
                        int qty = cart_product.getQty();
                        total_qty += qty;
                        product_titles.append(cart_product.getItem().getItem_name()).append(" , ");
                    }

                    name_text.setText(MessageFormat.format("Name:- {0}", bundle.get("Name")));
                    order_id.setText(MessageFormat.format("Transaction Id:- {0}", data.getStringExtra("ApprovalRefNo")));
                    product_details.setText(MessageFormat.format("Items:- {0}", product_titles));
                    total_items.setText(MessageFormat.format("Total No. of Items:- {0}", total_qty));
                    total_amount_last.setText(String.valueOf(bundle.get("Total")));
                    address_order.setText(MessageFormat.format("Address:- {0}", bundle.get("Address")));

                    OrderModel order = new OrderModel();
                    order.setName(String.valueOf(bundle.get("Name")));
                    order.setItems(String.valueOf(product_titles));
                    order.setCart_product_list(cartArrayList);
                    order.setTotal_amount(String.valueOf(bundle.get("Total")));
                    order.setAddress(String.valueOf(bundle.get("Address")));
                    order.setPayment(true);
                    order.setPayment_method("PayTm");
                    @SuppressLint("SimpleDateFormat")
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                    Date date= new Date();
                    order.setOrder_date(simpleDateFormat.format(date));
                    order.setTxn_id(data.getStringExtra("ApprovalRefNo"));
                    save_into_firebase(order);
                } else {
                    Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void gpay_payment(String gpayPackageName) {
        if (isAppIntalled(this,gpayPackageName)){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(get_gpay_uri());
            intent.setPackage(gpayPackageName);
            startActivityForResult(intent,GPAY_REQUEST_CODE);
        }else {
            Toast.makeText(this, "App is Not Installed, Please Install or select another Option", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAppIntalled(Context context, String package_name) {
        try {
            context.getPackageManager().getApplicationInfo(package_name,0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
            return false;
        }
    }

    private Uri get_paytm_uri() {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upi_id)
                .appendQueryParameter("pn",upi_name)
                .appendQueryParameter("am", String.valueOf(bundle.get("Total")))
                .appendQueryParameter("cu","INR")
                .build();
    }

    private Uri get_gpay_uri() {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa", upi_id)
                .appendQueryParameter("pn", upi_name)
                .appendQueryParameter("tn","Note")
                .appendQueryParameter("am", String.valueOf(bundle.get("Total")))
//                .appendQueryParameter("am", "1")
                .appendQueryParameter("cu","INR")
                .build();
    }

    private void visiblity() {
        top.setVisibility(View.INVISIBLE);
        order_ll.setVisibility(View.VISIBLE);
        radioGroup.setVisibility(View.INVISIBLE);
        detail_ll.setVisibility(View.VISIBLE);
        footer.setVisibility(View.INVISIBLE);
    }

    private void save_into_firebase(OrderModel order){

        order.setOrder_Id(create_ID());
        collectionReference
                .add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(Payment.this, "Order Saved in Firestore", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Payment.this, "Order Did not Save in FireStore Error: "+ e, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private static synchronized String create_ID() {
        return String.valueOf(Order_ID++);
    }

    public void onRadioButtonClicked(View v) {
        boolean checked = ((RadioButton) v).isChecked();
        switch (v.getId()){
            case R.id.cod:
                if (checked)
                    cod_bool = 1;
                break;
            case R.id.online:
                if (checked)
                    cod_bool = 0;
                break;
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        visiblity();
        int total_qty = 0;
        StringBuilder product_titles = new StringBuilder();

        for (Cart_product cart_product : cartArrayList) {
            int qty = cart_product.getQty();
            total_qty += qty;
            product_titles.append(cart_product.getItem().getItem_name()).append(" , ");
        }

        name_text.setText(MessageFormat.format("Name:- {0}", bundle.get("Name")));
        order_id.setText(MessageFormat.format("Transaction Id:- {0}", s));
        product_details.setText(MessageFormat.format("Items:- {0}", product_titles));
        total_items.setText(MessageFormat.format("Total No. of Items:- {0}", total_qty));
        total_amount_last.setText(String.valueOf(bundle.get("Total")));
        address_order.setText(MessageFormat.format("Address:- {0}", bundle.get("Address")));

        OrderModel order = new OrderModel();
        order.setName(String.valueOf(bundle.get("Name")));
        order.setItems(String.valueOf(product_titles));
        order.setCart_product_list(cartArrayList);
        order.setTotal_amount(String.valueOf(bundle.get("Total")));
        order.setAddress(String.valueOf(bundle.get("Address")));
        order.setPayment(true);
        order.setTxn_id(s);
        order.setPayment_method("RazorPay");
        save_into_firebase(order);


    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment Failed: " + s , Toast.LENGTH_SHORT).show();
        Log.d("Payment Activity", "onPaymentError: "+ s);
    }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth!=null)
            firebaseAuth.removeAuthStateListener(authStateListener);
    }

}