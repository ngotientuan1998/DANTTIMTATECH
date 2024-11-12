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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import DAO.DaoCuaHang;
import DAO.DaoKhachHang;
import DTO.CuaHang;
import adapter.adapterCuaHang;
import interfaces.cuaHangInterface;

public class DscuaHang extends AppCompatActivity{
    RecyclerView ryc_Ds;
    adapterCuaHang adapterCuaHangs;
    List<CuaHang> ds_cuaHang;
    DaoCuaHang daoCuaHang;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dscua_hang);
        ryc_Ds = findViewById(R.id.ryc_DscuaHang);
        db=FirebaseFirestore.getInstance();
        ds_cuaHang=new ArrayList<>();
        daoCuaHang=new DaoCuaHang(getApplication());
        adapterCuaHangs = new adapterCuaHang(ds_cuaHang,DscuaHang.this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication(), RecyclerView.VERTICAL, false);
        ryc_Ds.setLayoutManager(linearLayoutManager);
        ryc_Ds.setAdapter(adapterCuaHangs);
        daoCuaHang.langNgheSuKienThayDoiCuaHang(adapterCuaHangs,ds_cuaHang);
    }


    public void  XacNhanThayDoiTrangThai(CuaHang cuaHang){
        if (cuaHang.getTrangThai().equals("hoatDong")) {
            new AlertDialog.Builder(this)
                    .setTitle("Xác nhận trạng thái")
                    .setMessage("Bạn có muốn khóa không?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            daoCuaHang.suaTrangThai(cuaHang.getCuaHangID(),"khoa");
                        }
                    }) .setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
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
                            daoCuaHang.suaTrangThai(cuaHang.getCuaHangID(),"hoatDong");
                        }
                    }) .setNegativeButton("Hủy bỏ", null)
                    .show();

        }
    }





}
