package com.example.appduan1;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import DAO.DaoKhachHang;
import DTO.CuaHang;
import DTO.KhachHang;
import adapter.adapterCuaHang;
import adapter.adapterKhachHang;
import interfaces.khachHangInterface;

public class DskhachHang extends AppCompatActivity implements khachHangInterface {
    RecyclerView rycKhachHang;
    List<KhachHang> ds;
    DaoKhachHang daoKhachHang;
    adapterKhachHang adapterKhachHangs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dskhach_hang);
        rycKhachHang = findViewById(R.id.ryc_DskhachHang);
        daoKhachHang = new DaoKhachHang(DskhachHang.this);
        ds = new ArrayList<>();
        adapterKhachHangs = new adapterKhachHang(DskhachHang.this, ds, DskhachHang.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication(), RecyclerView.VERTICAL, false);
        rycKhachHang.setLayoutManager(linearLayoutManager);
        rycKhachHang.setAdapter(adapterKhachHangs);
        daoKhachHang.langNgheSuKienThayDoiKhachHang(adapterKhachHangs,ds);
    }

    @Override
    public void khachHang(KhachHang khachHang) {
        if (khachHang != null) {
            XacNhanThayDoiTrangThai(khachHang);
        }
    }

    public void XacNhanThayDoiTrangThai(KhachHang khachHang) {
        if (khachHang.getTrangThai().equals("hoatDong")) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận trạng thái")
                    .setMessage("Bạn có muốn khóa không?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            daoKhachHang.suaTrangThai(khachHang.getKhachHangID(), "khoa");
                        }
                    }).setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận trạng thái")
                    .setMessage("Bạn có muốn mở khóa không?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            daoKhachHang.suaTrangThai(khachHang.getKhachHangID(), "hoatDong");
                        }
                    }).setNegativeButton("Hủy bỏ", null)
                    .show();

        }
    }
}