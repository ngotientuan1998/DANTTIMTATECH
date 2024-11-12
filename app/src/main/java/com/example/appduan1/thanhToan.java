package com.example.appduan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import DAO.DaoDonHang;
import DAO.DaoGioHang;
import DAO.DaoKhachHang;
import DAO.Daoformat;
import DTO.DonHang;
import DTO.KhachHang;
import DTO.SanPham;
import DTO.gioHang;
import adapter.adapterGioHang;
import framentbottom.HomeFragment;
import framentbottom.donFragment;

public class thanhToan extends AppCompatActivity {
    DaoKhachHang daoKhachHang;
    DaoGioHang daoGioHang;
    DaoDonHang daoDonHang;
    Daoformat daoformat;
    String IDkhachHang;
    KhachHang khachHangTimDc;

    TextView TvHoTenDCNH, TvSDTDCNH, TvDCchiTietDCNH, TvXaDCNH, TvHuyenDCNH, TvTinhDCNH,
            tvTongTienHang, tvTextTonTienPVCTT, tvTongTienTT, tvTongTienTTDH;
    RecyclerView rycSanPham;
    Button btnDatHang;
    RadioButton radioButtonNhanh, radioButtonBinhThuong;
    SharedPreferences sharedPreferences;
    adapterGioHang adapterGioHangs;
    double TongTienHang = 0;
    double PhiVanChuyen = 0;
    Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanh_toan);
        Gan_ID();
        GanDuLieu();

    }

    private void ThanhToanVaLenDon(List<gioHang> ds_spTT) {

        Map<String, List<gioHang>> gioHangTheoCuaHang = new HashMap<>();
        for (gioHang x : ds_spTT) {
            String idCuaHang = x.getIDcuaHang();
            gioHangTheoCuaHang.computeIfAbsent(idCuaHang, k -> new ArrayList<>()).add(x);
        }
        Log.d("sizeds_spTT", "" + gioHangTheoCuaHang.size());
        radioButtonBinhThuong.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                double TongTienPhiVanChuyen = 30000 * gioHangTheoCuaHang.size();
                PhiVanChuyen = TongTienPhiVanChuyen;
                if (radioButtonBinhThuong.isChecked()) {
                    tvTextTonTienPVCTT.setText(daoformat.chuyenDinhDang(TongTienPhiVanChuyen));
                    tvTongTienTT.setText(daoformat.chuyenDinhDang(TongTienPhiVanChuyen + TongTienHang));
                    tvTongTienTTDH.setText(daoformat.chuyenDinhDang(TongTienPhiVanChuyen + TongTienHang));
                } else {
                    tvTongTienTT.setText(daoformat.chuyenDinhDang(TongTienHang - TongTienPhiVanChuyen));
                    tvTongTienTTDH.setText(daoformat.chuyenDinhDang(TongTienHang - TongTienPhiVanChuyen));
                }
            }
        });
        radioButtonNhanh.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                double TongTienPhiVanChuyen = 50000 * gioHangTheoCuaHang.size();
                PhiVanChuyen = TongTienPhiVanChuyen;

                if (radioButtonNhanh.isChecked()) {
                    tvTextTonTienPVCTT.setText(daoformat.chuyenDinhDang(TongTienPhiVanChuyen));
                    tvTongTienTT.setText(daoformat.chuyenDinhDang(TongTienPhiVanChuyen + TongTienHang));
                    tvTongTienTTDH.setText(daoformat.chuyenDinhDang(TongTienPhiVanChuyen + TongTienHang));
                } else {
                    tvTongTienTT.setText(daoformat.chuyenDinhDang(TongTienHang - TongTienPhiVanChuyen));
                    tvTongTienTTDH.setText(daoformat.chuyenDinhDang(TongTienHang - TongTienPhiVanChuyen));
                }
            }
        });

        btnDatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!radioButtonBinhThuong.isChecked() && !radioButtonNhanh.isChecked()) {
                    Toast.makeText(thanhToan.this, "vui lòng chọn hình thức vận chuyển", Toast.LENGTH_SHORT).show();
                } else {
                    for (Map.Entry<String, List<gioHang>> entry : gioHangTheoCuaHang.entrySet()) {
                        List<gioHang> gioHangCuaHang = entry.getValue();
                        int soLuongMua = 0;
                        for (gioHang x : gioHangCuaHang) {
                            soLuongMua += x.getSoLuongMua();
                        }

                        int IDGH = new Random().nextInt(1000);
                        String MaGH = "DH" + IDGH;
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1; // Tháng trong Calendar là từ 0 đến 11
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        String ngayThangNam = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
                        DonHang donHang = new DonHang(MaGH, IDkhachHang, gioHangCuaHang.get(0).getIDcuaHang(),
                                ngayThangNam,"","","", "ChuaDuyet", soLuongMua, PhiVanChuyen * gioHangCuaHang.size(),
                                Double.parseDouble(tvTongTienTTDH.getText().toString()),
                                gioHangCuaHang);
                        daoDonHang.themDonHang(donHang);
                    }
                    Toast.makeText(thanhToan.this, "Đặt hàng thành công", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

    }

    private void GanDuLieu() {

        daoKhachHang = new DaoKhachHang(thanhToan.this);
        daoGioHang = new DaoGioHang(thanhToan.this);
        daoDonHang = new DaoDonHang(thanhToan.this);
        sharedPreferences = getSharedPreferences("taiKhoan", getApplication().MODE_PRIVATE);
        IDkhachHang = sharedPreferences.getString("IDuser", "");


        daoKhachHang.TimKhachHang(IDkhachHang, new DaoKhachHang.TraVeKhachHangTimDc() {
            @Override
            public void onTimDcKhachHang(KhachHang khachHang) {
                khachHangTimDc = khachHang;
                TvHoTenDCNH.setText("Tên:" + khachHangTimDc.getHoTen());
                TvSDTDCNH.setText("|84+" + String.valueOf(khachHangTimDc.getSdt()));
                TvDCchiTietDCNH.setText(khachHangTimDc.getDiaChiChiTiet());
                TvXaDCNH.setText("Xã:" + khachHangTimDc.getXa());
                TvHuyenDCNH.setText("Huyện:" + khachHangTimDc.getHuyen());
                TvTinhDCNH.setText("Tỉnh:" + khachHangTimDc.getTinh());

            }

            @Override
            public void onKoTimKhachHang(String errorMessage) {

            }
        });

        daoGioHang.layGioHangTheoIDvaTrangThai(IDkhachHang, new DaoGioHang.TraDanhSachGioHangTheoIDKHvaTrangThai() {
            @Override
            public void onSuccess(List<gioHang> gioHangList) {
                adapterGioHangs = new adapterGioHang(thanhToan.this, gioHangList, "MHthanhToan");
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(thanhToan.this, RecyclerView.VERTICAL, false);
                rycSanPham.setAdapter(adapterGioHangs);
                rycSanPham.setLayoutManager(linearLayoutManager);
                for (int i = 0; i <= gioHangList.size() - 1; i++) {
                    TongTienHang += (gioHangList.get(i).getGiaSp() - gioHangList.get(i).getGiaGiamSp()) * gioHangList.get(i).getSoLuongMua();
                }
                tvTongTienHang.setText(daoformat.chuyenDinhDang(TongTienHang));
                tvTongTienTT.setText(daoformat.chuyenDinhDang(TongTienHang));
                tvTongTienTTDH.setText(daoformat.chuyenDinhDang(TongTienHang));
                ThanhToanVaLenDon(gioHangList);

            }

            @Override
            public void onError(Exception e) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        daoGioHang.layGioHangTheoIDKhachHangVaDanhTinhMuaNgay(IDkhachHang, new DaoGioHang.OnGioHangLoadListener() {
            @Override
            public void onGioHangLoadSuccess(List<gioHang> gioHangList) {
                if (!gioHangList.isEmpty()) {
                    for (gioHang x : gioHangList) {
                        daoGioHang.xoaGioHang(x.getIDgioHang());
                        Log.d("duoc xoas", "duoc xoas");
                    }
                }
            }

            @Override
            public void onGioHangLoadFailure(Exception e) {

            }
        });
        Log.d("thanhToanondestroy", "thanhToanondestroy");
    }

    @Override
    protected void onStop() {
        super.onStop();
        daoGioHang.layGioHangTheoIDKhachHangVaDanhTinhMuaNgay(IDkhachHang, new DaoGioHang.OnGioHangLoadListener() {
            @Override
            public void onGioHangLoadSuccess(List<gioHang> gioHangList) {
                if (!gioHangList.isEmpty()) {
                    for (gioHang x : gioHangList) {
                        daoGioHang.xoaGioHang(x.getIDgioHang());
                        Log.d("duoc xoas", "duoc xoas");
                    }
                }
            }

            @Override
            public void onGioHangLoadFailure(Exception e) {

            }
        });
        finish();
    }

    private void Gan_ID() {
        TvHoTenDCNH = findViewById(R.id.TvHoTenDCNH);
        TvSDTDCNH = findViewById(R.id.TvSDTDCNH);
        TvDCchiTietDCNH = findViewById(R.id.TvDCchiTietDCNH);
        TvXaDCNH = findViewById(R.id.TvXaDCNH);
        TvHuyenDCNH = findViewById(R.id.TvHuyenDCNH);
        TvTinhDCNH = findViewById(R.id.TvTinhDCNH);

        tvTongTienHang = findViewById(R.id.tvTonTienHangTT);
        tvTextTonTienPVCTT = findViewById(R.id.tvTonTienPVCTT);
        tvTongTienTT = findViewById(R.id.tvTongTienTT);
        tvTongTienTTDH = findViewById(R.id.tvTongTienTTDH);

        rycSanPham = findViewById(R.id.rycSpMua);
        btnDatHang = findViewById(R.id.btnDatHang);

        radioButtonBinhThuong = findViewById(R.id.radioButtonBinhThuong);
        radioButtonNhanh = findViewById(R.id.radioButtonNhanh);

        calendar = Calendar.getInstance();
        daoformat=new Daoformat();

    }
}