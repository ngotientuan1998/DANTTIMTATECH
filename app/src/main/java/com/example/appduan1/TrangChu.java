package com.example.appduan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import framentbottom.HomeFragment;
import framentbottom.doanhThuFragment;
import framentbottom.donFragment;
import framentbottom.gioHangFragment;
import framentbottom.lichSuFragment;

public class TrangChu extends AppCompatActivity {
    FirebaseFirestore db;
    HomeFragment homeFragment;
    private SharedPreferences sharedPreferences;
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trang_chu);
        db = FirebaseFirestore.getInstance();

        if (savedInstanceState == null) {
            homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.Frament_container, homeFragment)
                    .commit();
        }
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment;
                if (item.getItemId() == R.id.bottom_home) {
                    fragment = new HomeFragment();
                } else if (item.getItemId() == R.id.bottom_gioHang) {
                    fragment = new gioHangFragment();
                } else if (item.getItemId() == R.id.bottom_don) {
                    fragment = new donFragment();
                } else if (item.getItemId() == R.id.bottom_licSu) {
                    fragment = new lichSuFragment();
                } else if (item.getItemId() == R.id.bottom_ThongKe) {
                    fragment = new doanhThuFragment();
                } else {
                    fragment = new HomeFragment();
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.Frament_container, fragment).commit();
                return true;
            }
        });
        PhanQuyen();

    }

    private void PhanQuyen() {
        sharedPreferences = getSharedPreferences("taiKhoan", Context.MODE_PRIVATE);
        String role = sharedPreferences.getString("role", "");
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.bottom_gioHang);
        MenuItem menuItemThongKe = menu.findItem(R.id.bottom_ThongKe);
        if ("Cửa hàng".equals(role)) {
            menuItem.setVisible(false);
        } else {
            menuItem.setVisible(true);
            menuItemThongKe.setTitle("Chi Tiêu");
        }
    }

}