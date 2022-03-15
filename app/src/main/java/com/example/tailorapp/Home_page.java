package com.example.tailorapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.tailorapp.nav.myshop.My_shop;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home_page extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private FragmentContainerView fragmentContainerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fragmentContainerView = findViewById(R.id.container_home);
//        getSupportFragmentManager().beginTransaction().replace(R.id.container_home,home_fragment);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.home_navigation:
                        getSupportFragmentManager().beginTransaction().replace(fragmentContainerView.getId(),new Home_Fragment()).commit();
                        return true;
//                        break;
                    case R.id.categories_navigation:
                        Toast.makeText(Home_page.this, "Categories", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.my_order_bottom_navigation:
                        Toast.makeText(Home_page.this, "My Orders", Toast.LENGTH_SHORT).show();
                        return true;
                    case R.id.profile_bottom_navigation:
                        Toast.makeText(Home_page.this, "Profile", Toast.LENGTH_SHORT).show();
                        return true;

                }
                return false;
            }
        });
    }
}