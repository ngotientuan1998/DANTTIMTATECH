package com.example.appduan1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import DTO.CuaHang;
import DTO.KhachHang;

public class ChiTietKhachHang extends AppCompatActivity {
TextView tvTen,tvGioiTinh,tvEmail,tvSdt,tvDiaChi,tvNamSinh,tvNgayTao,tvID;
ImageView imgThoat,imgAnhKhach;
Button btn_LichSuMuaBan;

Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_khach_hang);
        Gan_ID();
        bundle = getIntent().getExtras();
        KhachHang khachHang = (KhachHang) bundle.getSerializable("khachHang");
        if(khachHang!=null){
            if(khachHang.getHinhAnh()!=null||khachHang.getKhachHangID().isEmpty()){
                Picasso.get().load(khachHang.getHinhAnh()).into(imgAnhKhach);
            }
            tvTen.setText("Tên:"+khachHang.getHoTen());
            tvGioiTinh.setText("Giới Tính:"+khachHang.getGioiTinh());
            tvEmail.setText("Email:"+khachHang.getEmail());
            tvSdt.setText("số điện thoại:"+String.valueOf(khachHang.getSdt()));
            tvDiaChi.setText("Địa chỉ:"+"Tỉnh"+khachHang.getTinh()+"|Huyện"+khachHang.getHuyen()+"|xã"+khachHang.getXa()+"|"+khachHang.getDiaChiChiTiet());
            tvNamSinh.setText("Năm sinh:"+String.valueOf(khachHang.getNTNS()));
            tvNgayTao.setText("Ngày thánh năm tạo:"+khachHang.getNTNT());
            tvID.setText("ID:"+khachHang.getKhachHangID());
        }
        imgThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private  void  Gan_ID(){
        tvTen=findViewById(R.id.tvTenKhachChiTiet);
        tvGioiTinh=findViewById(R.id.tvGioiTinhKhachChiTiet);
        tvEmail=findViewById(R.id.tvEmailKhachChiTiet);
        tvSdt=findViewById(R.id.tvsdtKhachChiTiet);
        tvDiaChi=findViewById(R.id.tvDiaChiKhachChiTiet);
        tvNamSinh=findViewById(R.id.tvNamSinhKhachChiTiet);
        tvNgayTao=findViewById(R.id.tvNgayTaoKhachChiTiet);
        tvID=findViewById(R.id.tvIDKhachChiTiet);

        imgThoat=findViewById(R.id.imThoatKhachChiTietTuDsKhach);
        imgAnhKhach=findViewById(R.id.imAnhKhachChiTiet);

        btn_LichSuMuaBan=findViewById(R.id.btnLichSuMuaBanKhachChiTiet);

    }

}