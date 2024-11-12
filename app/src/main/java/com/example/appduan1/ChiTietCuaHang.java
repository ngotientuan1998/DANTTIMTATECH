package com.example.appduan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import DAO.DaoSanPham;
import DTO.CuaHang;
import DTO.SanPham;
import adapter.adapterSanPham;

public class ChiTietCuaHang extends AppCompatActivity {
    ImageView imgThoatDsShop, imAnhShop;
    TextView tvTen, tvTheoDoi, tvSdt, tvNgayTao, tvEmail, tvDiaChi, tvIDshop;
    Bundle bundle;
    RecyclerView ryc_dsSanPham;
    adapterSanPham adapterSanPhams;
    List<SanPham> ds_SP;
    String role;
    CuaHang cuaHang,cuaHangAdmin;
    DaoSanPham daoSanPham;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_cua_hang);
        Gan_ID();
        bundle = getIntent().getExtras();
        cuaHang = (CuaHang) bundle.getSerializable("cuaHangCtSP");
        cuaHangAdmin=(CuaHang) bundle.getSerializable("cuaHang");
        ds_SP = new ArrayList<>();
        if(cuaHang!=null){
            adapterSanPhams = new adapterSanPham(ChiTietCuaHang.this, ds_SP, cuaHang.getCuaHangID(), role,"Home");
            GridLayoutManager layoutManager = new GridLayoutManager(ChiTietCuaHang.this, 2);
            ryc_dsSanPham.setLayoutManager(layoutManager);
            ryc_dsSanPham.setAdapter(adapterSanPhams);

            if (cuaHang.getHinhAnh() != null && !cuaHang.getHinhAnh().isEmpty()) {
                Picasso.get().load(cuaHang.getHinhAnh()).into(imAnhShop);
            }
            tvTen.setText("Tên shop:" + cuaHang.getTenCuaHang());
            tvTheoDoi.setText("Theo dõi:" + String.valueOf(cuaHang.getTheoDoi()));
            tvSdt.setText("sđt:" + String.valueOf(cuaHang.getSdt()));
            tvEmail.setText("email:" + cuaHang.getEmail());
            tvDiaChi.setText("Địa chỉ:" + "Tỉnh:" + cuaHang.getTinh() + "|Huyện:" + cuaHang.getHuyen() + "|Xã:" + cuaHang.getXa() + "|" + cuaHang.getDiaChiChiTiet());
            tvNgayTao.setText("ngày tháng năm tạo:" + cuaHang.getNTNT());
            tvIDshop.setText("ID:" + cuaHang.getCuaHangID());

            daoSanPham.langNgheThayDoiSanPham(cuaHang.getCuaHangID(), adapterSanPhams, ds_SP);

        } else {
            if(cuaHangAdmin!=null){
                adapterSanPhams = new adapterSanPham(ChiTietCuaHang.this, ds_SP, cuaHangAdmin.getCuaHangID(), role,"Home");
                GridLayoutManager layoutManager = new GridLayoutManager(ChiTietCuaHang.this, 2);
                ryc_dsSanPham.setLayoutManager(layoutManager);
                ryc_dsSanPham.setAdapter(adapterSanPhams);

                if (cuaHangAdmin.getHinhAnh() != null && !cuaHangAdmin.getHinhAnh().isEmpty()) {
                    Picasso.get().load(cuaHangAdmin.getHinhAnh()).into(imAnhShop);
                }
                tvTen.setText("Tên shop:" + cuaHangAdmin.getTenCuaHang());
                tvTheoDoi.setText("Theo dõi:" + String.valueOf(cuaHangAdmin.getTheoDoi()));
                tvSdt.setText("sđt:" + String.valueOf(cuaHangAdmin.getSdt()));
                tvEmail.setText("email:" + cuaHangAdmin.getEmail());
                tvDiaChi.setText("Địa chỉ:" + "Tỉnh:" + cuaHangAdmin.getTinh() + "|Huyện:" + cuaHangAdmin.getHuyen() + "|Xã:" + cuaHangAdmin.getXa() + "|" + cuaHangAdmin.getDiaChiChiTiet());
                tvNgayTao.setText("ngày tháng năm tạo:" + cuaHangAdmin.getNTNT());
                tvIDshop.setText("ID:" + cuaHangAdmin.getCuaHangID());

                daoSanPham.langNgheThayDoiSanPham(cuaHangAdmin.getCuaHangID(), adapterSanPhams, ds_SP);
            }

        }
        SuKienOnclick();

    }

    public void SuKienOnclick() {
        imgThoatDsShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }


    public void Gan_ID() {
        imAnhShop = findViewById(R.id.imAnhShopChiTiet);
        imgThoatDsShop = findViewById(R.id.imThoatShopChiTietTuDsShop);
        tvTen = findViewById(R.id.tvTenShopChiTiet);
        tvTheoDoi = findViewById(R.id.tvTheoDoiShopChiTiet);
        tvSdt = findViewById(R.id.tvsdtShopChiTiet);
        tvEmail = findViewById(R.id.tvEmailShopChiTiet);
        tvDiaChi = findViewById(R.id.tvDiaChiShopChiTiet);
        tvIDshop = findViewById(R.id.tvIDshopChiTiet);
        tvNgayTao = findViewById(R.id.tvNgayTaoShopChiTiet);
        ryc_dsSanPham = findViewById(R.id.ryc_SanPhamShopChiTiet);

        sharedPreferences = ChiTietCuaHang.this.getSharedPreferences("taiKhoan", Context.MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");

        daoSanPham = new DaoSanPham(ChiTietCuaHang.this);
    }

}