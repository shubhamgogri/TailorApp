package com.example.tailorapp.nav;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tailorapp.login.UserApI;
import com.example.tailorapp.nav.cart.CartActivity;
import com.example.tailorapp.nav.marketplace.MarketPlace_fragment;
import com.example.tailorapp.nav.myshop.My_shop;
import com.example.tailorapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class Myshopmain extends AppCompatActivity implements View.OnClickListener {
    private ImageButton marketplace_button, order_button, myshop_button, partners_button, report_button;
    private AppBarConfiguration mAppBarConfiguration;
    private TextView username_drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myshopmain);

        myshop_button = findViewById(R.id.my_shop_button);
        marketplace_button = findViewById(R.id.market_place_button);
        order_button =findViewById(R.id.order_button);
        partners_button = findViewById(R.id.partners_button);
        report_button = findViewById(R.id.report_button);
        username_drawer = findViewById(R.id.username_drawer);

        myshop_button.setOnClickListener(this);
        marketplace_button.setOnClickListener(this);
        order_button.setOnClickListener(this);
        partners_button.setOnClickListener(this);
        report_button.setOnClickListener(this);

//        UserApI userApI = UserApI.getInstance();
//        username_drawer.setText(userApI.getUsername());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab_cart );
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent cart = new Intent(Myshopmain.this, CartActivity.class);
                startActivity(cart);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_order, R.id.nav_products, R.id.nav_slideshow, R.id.customers,
                R.id.reports,R.id.market, R.id.partner, R.id.settings ,R.id.refund,R.id.logout)
                .setDrawerLayout(drawer)
                .build();
//        mAppBarConfiguration.getClass()
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        val navController = navHostFragment.navController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);
        NavController navCo = navHostFragment.getNavController();

//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navCo, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navCo);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,new MarketPlace_fragment()).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.myshopmain, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onClick(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (v.getId()){
            case R.id.my_shop_button:
                Toast.makeText(Myshopmain.this, "My Shop", Toast.LENGTH_SHORT).show();
                Fragment myshop = new My_shop();
                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,myshop).commit();
                Log.d("footer", "onClick: my shop successful");
            break;
            case R.id.market_place_button:
                Toast.makeText(Myshopmain.this, "Market Place", Toast.LENGTH_SHORT).show();
                Fragment market = new MarketPlace_fragment();
                Log.d("Myshop_button", "onClick: "+ fragmentManager.getBackStackEntryCount() );
                fragmentManager.popBackStackImmediate();
                fragmentManager.beginTransaction().add(R.id.nav_host_fragment,market).commit();

                Log.d("footer", "onClick: market Place successful");
                break;
            case R.id.order_button:
                Toast.makeText(Myshopmain.this, "order", Toast.LENGTH_SHORT).show();
//                Fragment myshop = new My_shop();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,myshop).commit();
                Log.d("footer", "onClick: order successful");
                break;
            case R.id.partners_button:
                Toast.makeText(Myshopmain.this, "Partners", Toast.LENGTH_SHORT).show();
//                Fragment myshop = new My_shop();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,myshop).commit();
                Log.d("footer", "onClick: Partners successful");
                break;

            case R.id.report_button:
                Toast.makeText(Myshopmain.this, "Report", Toast.LENGTH_SHORT).show();
//                Fragment myshop = new My_shop();
//                FragmentManager fragmentManager = getSupportFragmentManager();
//                fragmentManager.beginTransaction().replace(R.id.nav_host_fragment,myshop).commit();
                Log.d("footer", "onClick: Reports successful");
                break;
        }
    }
}