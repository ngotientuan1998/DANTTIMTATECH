package framentbottom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appduan1.R;
import com.google.android.material.tabs.TabLayout;

import viewPagerAdapter.viewPageAdapterDNDK;
import viewPagerAdapter.viewPageAdapterDon;


public class donFragment extends Fragment {
    viewPageAdapterDon pageAdapterDon;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_don, container, false);
        tabLayout = view.findViewById(R.id.tab_layout_Don);
        viewPager2 = view.findViewById(R.id.view_pager_Don);
        tabLayout.addTab(tabLayout.newTab().setText("Đợi duyệt"));
        tabLayout.addTab(tabLayout.newTab().setText("Duyệt"));
        tabLayout.addTab(tabLayout.newTab().setText("Đang giao"));
        tabLayout.addTab(tabLayout.newTab().setText("Nhận"));

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        pageAdapterDon = new viewPageAdapterDon(fragmentManager, getLifecycle());
        viewPager2.setAdapter(pageAdapterDon);
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
        SuKienOnClick();
        return view;
    }

    private void SuKienOnClick() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DiaLongThoatApp();
            }
        });

    }
    private void DiaLongThoatApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xác nhận thoát");
        builder.setMessage("Bạn có muốn thoát khỏi ứng dụng?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requireActivity().finish();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng dialog và không làm gì cả
                dialog.dismiss();
            }
        });
        builder.show();
    }

}