package com.example.appduan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import DAO.DaoKhachHang;
import DAO.Daoformat;
import DTO.DonHang;
import DTO.KhachHang;
import DTO.SanPham;
import adapter.adapterGioHang;

public class ChiTietDonHang extends AppCompatActivity {
    TextView tvTenKh, tvSDT, tvEmail, tvDiaChi, tvXa, tvHuyen, tvTinh, tvNgayDat, tvSoLuongSp, tvTongGia, tvTrangThaiDonHang;
    RecyclerView ryc_dsSpTrongDon;
    Bundle bundle;
    DonHang donHangs;
    DaoKhachHang daoKhachHang;
    Daoformat daoformat;
    adapterGioHang adapterGioHangs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_don_hang);
        bundle = getIntent().getExtras();
        donHangs = (DonHang) bundle.getSerializable("donHang");
        Gan_ID();
        GanDuLieu();
    }

    private void Gan_ID() {
        tvTenKh = findViewById(R.id.tvTenKhachHangCTDT);
        tvSDT = findViewById(R.id.tvSDTKhachHangMuaCTDT);
        tvEmail = findViewById(R.id.tvEmailKhachHangMuaCTDT);
        tvDiaChi = findViewById(R.id.tvDiaChiTietKHMuaCTDT);
        tvXa = findViewById(R.id.tvXaKHMuaCTDT);
        tvHuyen = findViewById(R.id.tvHuyenKHMuaCTDT);
        tvTinh = findViewById(R.id.tvTinhKHMuaCTDT);
        tvNgayDat = findViewById(R.id.tvNgayDatMuaCTDT);
        tvSoLuongSp = findViewById(R.id.tvSoLuongSpMuaCTDT);
        tvTongGia = findViewById(R.id.tvTongGiaSPMuaCTDT);
        tvTrangThaiDonHang = findViewById(R.id.tvTrangThaiSPMuaCTDT);
        ryc_dsSpTrongDon = findViewById(R.id.ryc_DsSpTrongDonHangCTDT);
        daoKhachHang = new DaoKhachHang(ChiTietDonHang.this);
        daoformat=new Daoformat();
    }

    private void GanDuLieu() {
        if (donHangs != null) {
            daoKhachHang.TimKhachHang(donHangs.getIDkhachHang(), new DaoKhachHang.TraVeKhachHangTimDc() {
                @Override
                public void onTimDcKhachHang(KhachHang khachHang) {
                    tvTenKh.setText("Tên:" + khachHang.getHoTen());
                    tvSDT.setText("SĐT:" + khachHang.getSdt());
                    tvEmail.setText("Email:" + khachHang.getEmail());
                    tvDiaChi.setText("Địa chỉ:" + khachHang.getDiaChiChiTiet());
                    tvXa.setText("Xã:" + khachHang.getXa());
                    tvHuyen.setText("Huyện:" + khachHang.getHuyen());
                    tvTinh.setText("Tỉnh:" + khachHang.getTinh());
                }

                @Override
                public void onKoTimKhachHang(String errorMessage) {

                }
            });
            tvNgayDat.setText("Ngày đặt:" + donHangs.getNgayDat());
            tvSoLuongSp.setText("Số lượng:" + donHangs.getSoLuongSanPham());
            tvTongGia.setText("Tổng giá:" + daoformat.chuyenDinhDang(donHangs.getGia()));
            tvTrangThaiDonHang.setText("Trang thái:" + donHangs.getTrangThaiDon());
            if(donHangs.getDanhSachSanPhamThuocDon()==null||!donHangs.getDanhSachSanPhamThuocDon().isEmpty()){
                adapterGioHangs=new adapterGioHang(ChiTietDonHang.this,donHangs.getDanhSachSanPhamThuocDon(),"MHchiTietDonHang");
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChiTietDonHang.this, RecyclerView.VERTICAL, false);
                ryc_dsSpTrongDon.setLayoutManager(linearLayoutManager);
                ryc_dsSpTrongDon.setAdapter(adapterGioHangs);
                Log.d("Dang  ","Dang sách giỏ hàng trong đơn hàng rỗng ");
            } else {
                Log.d("Dang  ","Dang sách giỏ hàng trong đơn hàng rỗng ");

            }
        }

    }
}