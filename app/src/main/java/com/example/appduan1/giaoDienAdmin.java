package com.example.appduan1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class giaoDienAdmin extends AppCompatActivity {
    Button btnDsCuaHang, btnDsKhachHang, btnDsChayQuangCao, btnDangXuat;
    SharedPreferences sharedPreferences;
     SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giao_dien_admin);
        Gan_ID();
        SuKienOnclick();


    }

    private void SuKienOnclick() {
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangXuat();
            }
        });
        btnDsCuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplication(), DscuaHang.class);
                startActivity(intent);
            }
        });
        btnDsKhachHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplication(), DskhachHang.class);
                startActivity(intent);
            }
        });
        btnDsChayQuangCao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(giaoDienAdmin.this,DsDonChayQuangCao.class);
                startActivity(intent);
            }
        });
    }

    private void dangXuat() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Đăng xuất người dùng hiện tại
                        FirebaseAuth.getInstance().signOut();
                        // Lấy trạng thái của chk_NhoTK
                        boolean nhoTaiKhoan = sharedPreferences.getBoolean("nhoTaiKhoan", false);

                        editor.remove("taiKhoan");
                        editor.remove("username");
                        editor.remove("password");
                        if (nhoTaiKhoan) {
                            editor.putBoolean("nhoTaiKhoan", false);
                        }
                        editor.apply();

                        // Chuyển người dùng đến màn hình đăng nhập hoặc màn hình khác
                        Intent intent = new Intent(getApplication(), DangNhapDangKi.class);
                        startActivity(intent);

                        // Kết thúc Activity hiện tại
                        finish();
                    }
                })
                .setNegativeButton("Hủy bỏ", null)
                .show();
    }

    private void Gan_ID() {
        btnDsCuaHang = findViewById(R.id.btnDScuaHang);
        btnDsKhachHang = findViewById(R.id.btnDSkhachHang);
        btnDsChayQuangCao = findViewById(R.id.btnDScChayQuangCaoAdmin);
        btnDangXuat = findViewById(R.id.btnDangXuatAdmin);
        sharedPreferences = getSharedPreferences("taiKhoan", getApplication().MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

}