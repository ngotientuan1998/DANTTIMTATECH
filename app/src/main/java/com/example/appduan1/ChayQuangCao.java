package com.example.appduan1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

import DAO.DaoChayQuangCao;
import DTO.CuaHang;
import DTO.QuangCao;
import DTO.SanPham;

public class ChayQuangCao extends AppCompatActivity {
    ImageView imgThoat;
    Button btn_ChayCD1, btn_ChayCD2, btn_ChuyenKhoan;
    TextView tv_CapDo, tv_PhiDichVu, tv_TenCongTy, tv_STK, tv_NoiDung, tv_MaSpQC, tv_TenSpQC;
    SanPham sanPham;
    Bundle bundle;
    DaoChayQuangCao chayQuangCao;
    String IDcuaHang;
    int CapDo = 0;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chay_quang_cao);
        Gan_ID();
        SuKienOnClick();
    }

    private void SuKienOnClick() {
        imgThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btn_ChayCD1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_CapDo.setText("Cấp độ 1");
                tv_TenCongTy.setText("Tên người nhận:SunnyTeam");
                tv_PhiDichVu.setText("số tiền:300.000k");
                tv_STK.setText("stk:0387700205");
                tv_NoiDung.setText("Nội dung"+sanPham.getIDsanPham() + "|cấp độ 1");
                btn_ChuyenKhoan.setVisibility(View.VISIBLE);
                CapDo = 1;
            }
        });
        btn_ChayCD2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_CapDo.setText("Cấp độ 2");
                tv_TenCongTy.setText("Tên người nhận:SunnyTeam");
                tv_PhiDichVu.setText("số tiền:900.000k");
                tv_STK.setText("stk:0387700205");
                tv_NoiDung.setText("Nội dung"+sanPham.getIDsanPham() + "|cấp độ 2");
                btn_ChuyenKhoan.setVisibility(View.VISIBLE);
                CapDo = 2;
            }
        });
        btn_ChuyenKhoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_CapDo.setText("");
                tv_TenCongTy.setText("");
                tv_PhiDichVu.setText("");
                tv_STK.setText("");
                tv_NoiDung.setText("");
                btn_ChuyenKhoan.setVisibility(View.GONE);

                int ID = new Random().nextInt(1000);
                String MaSP = "QC" + ID;

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1; // Tháng trong Calendar là từ 0 đến 11
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                String ngayThangNam = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
                if (CapDo > 0) {
                    QuangCao quangCao = new QuangCao(MaSP, IDcuaHang,sanPham.getTenCuaHang(), sanPham.getIDsanPham(),sanPham.getTenSanPham(),"Đợi", ngayThangNam, "", "", CapDo);
                    chayQuangCao.themQuanCao(quangCao);
                }
            }
        });
    }

    private void Gan_ID() {
        imgThoat=findViewById(R.id.img_thoatChayQcSP);
        btn_ChayCD1=findViewById(R.id.btn_ChayCapDo1);
        btn_ChayCD2=findViewById(R.id.btn_ChayCapDo2);
        btn_ChuyenKhoan=findViewById(R.id.btn_XacNhanCK);
        tv_CapDo = findViewById(R.id.TvCapDo);
        tv_PhiDichVu = findViewById(R.id.TvPhiDichVu);
        tv_TenCongTy = findViewById(R.id.TvTenCongTy);
        tv_STK = findViewById(R.id.TvStk);
        tv_NoiDung = findViewById(R.id.TvNoiDung);
        tv_TenSpQC = findViewById(R.id.tvTenSanPhamQC);
        tv_MaSpQC = findViewById(R.id.tvMaSanPhamQC);
        btn_ChuyenKhoan.setVisibility(View.GONE);
        bundle = getIntent().getExtras();
        sanPham = (SanPham) bundle.getSerializable("sanPham");
        tv_TenSpQC.setText("Tên SP:"+sanPham.getTenSanPham());
        tv_MaSpQC.setText("Ma SP"+sanPham.getIDsanPham());
        sharedPreferences = getApplication().getSharedPreferences("taiKhoan", Context.MODE_PRIVATE);
        IDcuaHang = sharedPreferences.getString("IDuser", "");
        chayQuangCao = new DaoChayQuangCao(ChayQuangCao.this);
    }

}