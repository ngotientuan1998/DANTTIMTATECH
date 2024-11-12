package com.example.appduan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import frament.DangNhapFrament;
import viewPagerAdapter.viewPageAdapterDNDK;

public class DangNhapDangKi extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private viewPageAdapterDNDK adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap_dang_ki);
        tabLayout = findViewById(R.id.tab_layout_DNDK);
        viewPager2 = findViewById(R.id.view_pager_DNDK);

        tabLayout.addTab(tabLayout.newTab().setText("Đăng nhập"));
        tabLayout.addTab(tabLayout.newTab().setText("Đăng kí"));

        FragmentManager fragmentManager = getSupportFragmentManager();
        adapter = new viewPageAdapterDNDK(fragmentManager, getLifecycle());
        viewPager2.setAdapter(adapter);
        // Trong MainActivity


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}