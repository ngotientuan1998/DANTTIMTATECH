package com.example.appduan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import DAO.DaoChayQuangCao;
import DAO.DaoSanPham;
import DTO.QuangCao;
import DTO.SanPham;
import adapter.adapterChayQuangCao;

public class DsDonChayQuangCao extends AppCompatActivity {
    ImageView imgDSthoat;
    RecyclerView rycDsDon;
    String role, IDcuaHang;
    SharedPreferences sharedPreferences;
    DaoChayQuangCao daoChayQuangCao;
    List<QuangCao> ds_Qc;
    SearchView  searchViewDsDon;
    adapterChayQuangCao chayQuangCao;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ds_quang_cao_cua_hang);
        Gan_ID();
        PhanQuyen();
        imgDSthoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchViewDsDon.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                TimKiem(s);
                return true;
            }
        });
    }

    private void TimKiem(String s) {
        if (role.equals("Cửa hàng")) {
           daoChayQuangCao.layDanhSachChayQuangCaoTheoCuaHang(IDcuaHang, new DaoChayQuangCao.DanhSachChayQuangCao() {
               @Override
               public void onTimDsChayQuangCao(List<QuangCao> quangCaoList) {
                   ds_Qc.clear();
                   for (QuangCao x : quangCaoList) {
                       if (x.getTenSanPham().contains(s) || x.getIDsanPham().contains(s)||x.getTenCuaHang().contains(s)) {
                           ds_Qc.add(x);
                       }
                   }
                   chayQuangCao.notifyDataSetChanged();

               }

               @Override
               public void onKoTimDsChayQuangCao(String errorMessage) {

               }
           });
        } else {
           daoChayQuangCao.layDanhSachToanBoQuangCao(new DaoChayQuangCao.DanhSachChayQuangCao() {
               @Override
               public void onTimDsChayQuangCao(List<QuangCao> quangCaoList) {
                   ds_Qc.clear();
                   for (QuangCao x : quangCaoList) {
                       if (x.getTenSanPham().contains(s) || x.getIDsanPham().contains(s)||x.getTenCuaHang().contains(s)) {
                           ds_Qc.add(x);
                       }
                   }
                   chayQuangCao.notifyDataSetChanged();
               }

               @Override
               public void onKoTimDsChayQuangCao(String errorMessage) {

               }
           });
        }

    }

    private void Gan_ID() {
        imgDSthoat = findViewById(R.id.imgThoatDSQC);
        searchViewDsDon = findViewById(R.id.SearchViewDSQC);
        rycDsDon = findViewById(R.id.rycDSQC);
        sharedPreferences = DsDonChayQuangCao.this.getSharedPreferences("taiKhoan", Context.MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");
        IDcuaHang = sharedPreferences.getString("IDuser", "");
        daoChayQuangCao = new DaoChayQuangCao(DsDonChayQuangCao.this);
    }

    private void PhanQuyen() {
        ds_Qc = new ArrayList<>();
        chayQuangCao = new adapterChayQuangCao(ds_Qc, DsDonChayQuangCao.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(DsDonChayQuangCao.this, RecyclerView.VERTICAL, false);
        rycDsDon.setLayoutManager(linearLayoutManager);
        rycDsDon.setAdapter(chayQuangCao);
        if (role.equals("Cửa hàng")) {
            daoChayQuangCao.CapNhapLangNgheDuLieuCuaHang(IDcuaHang, chayQuangCao, ds_Qc);
        } else {
            daoChayQuangCao.CapNhapLangNgheDuLieu(chayQuangCao, ds_Qc);
        }
    }

}