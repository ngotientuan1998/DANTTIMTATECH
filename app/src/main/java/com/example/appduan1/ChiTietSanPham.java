package com.example.appduan1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Random;

import DAO.DaoCuaHang;
import DAO.DaoGioHang;
import DAO.DaoKhachHang;
import DAO.Daoformat;
import DTO.CuaHang;
import DTO.KhachHang;
import DTO.SanPham;
import DTO.gioHang;
import adapter.adapterAnhSanPham;
import interfaces.SuaSanPhaminterface;

public class ChiTietSanPham extends AppCompatActivity {
    private ImageView img_thoatChiTietSP, img_ThemGioHang, imgAnhShop;
    private Button btnChayQC, btnMuaNgay, btnXemShop;
    private TextView tvtensp_ctsp, tvmaSp, giamGia, gia, luotMua, soLuong, tvcpu, tvRam, tvOcung, tvCart, tvMhinh, tvGioHang, tvTenShop;
    private TextView Tv_chiTiet, TvDCQC_ctsp;
    private RecyclerView rey_ds_anh_ct_sp;
    SanPham sanPham;
    CuaHang cuaHangTimDc;

    Bundle bundle;
    String ID, role;
    SharedPreferences sharedPreferences;
    DaoCuaHang daoCuaHang;
    DaoKhachHang daoKhachHang;
    int xacThuc = 1;
    String IDCuaHangCoSp;
    Daoformat daoformat;
    DaoGioHang daogioHang;
    gioHang gioHangs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        Gan_ID();
        bundle = getIntent().getExtras();
        IDCuaHangCoSp = bundle.getString("IDCuaHangCoSP");
        sanPham = (SanPham) bundle.getSerializable("sanPham");
        cuaHangTimDc = (CuaHang) bundle.getSerializable("cuaHang");
        PhanQuyen();
        HienThiThongTinSP();

        SuKienClick();
    }

    private void HienThiThongTinSP() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplication(), RecyclerView.HORIZONTAL, false);
        adapterAnhSanPham adapterAnhSanPhams = new adapterAnhSanPham(ChiTietSanPham.this, sanPham.getHinhAnhSanPham(), "ChiTiet");
        rey_ds_anh_ct_sp.setLayoutManager(linearLayoutManager);
        rey_ds_anh_ct_sp.setAdapter(adapterAnhSanPhams);

        tvtensp_ctsp.setText("Tên sp:" + sanPham.getTenSanPham());
        tvmaSp.setText("Mã sp:" + sanPham.getIDsanPham());
        giamGia.setText("Giảm:" + daoformat.chuyenDinhDang(sanPham.getGiamGia())+"đ");
        gia.setText("Gía:" + daoformat.chuyenDinhDang(sanPham.getGia())+"đ");
        luotMua.setText("Lượt mua:" + String.valueOf(sanPham.getSoLuongBan()));
        soLuong.setText("Số lượng:" + String.valueOf(sanPham.getSoLuong()));
        Tv_chiTiet.setText("Chi tiết:" + sanPham.getChiTietSanPham());
        tvcpu.setText("CPU:" + sanPham.getCPU());
        tvRam.setText("RAM:" + sanPham.getRAM());
        tvOcung.setText("Ổ cứng:" + sanPham.getoCung());
        tvCart.setText("Card MH:" + sanPham.getCard());
        tvMhinh.setText("Màn hình:" + sanPham.getMhinh());
        TvDCQC_ctsp.setText("Điểm chạy quảng cáo:" + sanPham.getDiemChayQuangCao());
        if (cuaHangTimDc != null) {
            if (cuaHangTimDc.getHinhAnh() != null) {
                Picasso.get().load(cuaHangTimDc.getHinhAnh()).into(imgAnhShop);
            }
            tvTenShop.setText(cuaHangTimDc.getTenCuaHang());
        }
    }


    private void SuKienClick() {

        img_thoatChiTietSP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnChayQC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietSanPham.this, ChayQuangCao.class);
                Bundle bundleSPQC = new Bundle();
                bundleSPQC.putSerializable("sanPham", sanPham);
                intent.putExtras(bundleSPQC);
                startActivity(intent);
            }
        });
        img_ThemGioHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (xacThuc == 2) {
                    daogioHang.layGioHangTheoIDKhachHang(ID, new DaoGioHang.OnGioHangLoadListener() {
                        @Override
                        public void onGioHangLoadSuccess(List<gioHang> gioHangList) {
                            boolean daCoTrongGio = false;
                            for (gioHang x : gioHangList) {
                                if (x.getIDsanPham().equals(sanPham.getIDsanPham())) {
                                    Toast.makeText(ChiTietSanPham.this, "Sản phẩm đã có trong giỏ", Toast.LENGTH_SHORT).show();
                                    daCoTrongGio = true;
                                    break;
                                }
                            }

                            if (!daCoTrongGio) {
                                int IDGH = new Random().nextInt(1000);
                                String MaGH = "GH" + IDGH;
                                gioHang gioHangs = new gioHang(ID, "ko chon", sanPham.getIDsanPham(), sanPham.getIDcuaHang(), sanPham.getTenCuaHang(),
                                        sanPham.getHinhAnhSanPham().get(0).toString(), sanPham.getTenSanPham(), MaGH, sanPham.getGia(), sanPham.getGiamGia(),
                                        1, "ThemVaoGio");
                                daogioHang.themVaoGioHang(gioHangs);
                            }
                        }

                        @Override
                        public void onGioHangLoadFailure(Exception e) {
                            // Xử lý lỗi tại đây (ví dụ: hiển thị thông báo lỗi)
                            Toast.makeText(ChiTietSanPham.this, "Lỗi khi tải giỏ hàng", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (role.equals("admin")) {
                    Toast.makeText(ChiTietSanPham.this, "Tài khoản admin chỉ quản trị", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChiTietSanPham.this, "Vui lòng xác thực tài khoản", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnMuaNgay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (xacThuc == 2) {
                    muaNgay(sanPham);
                } else if (role.equals("admin")) {
                    Toast.makeText(ChiTietSanPham.this, "Tài khoản admin chỉ quan trị", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ChiTietSanPham.this, "Vui lòng xác thực tài khoản", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnXemShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cuaHangTimDc != null) {
                    Log.d("cua hàng tìm thấy", cuaHangTimDc.getCuaHangID());
                    Intent intent = new Intent(ChiTietSanPham.this, ChiTietCuaHang.class);
                    Bundle bundleCuaHang = new Bundle();
                    bundleCuaHang.putSerializable("cuaHangCtSP", cuaHangTimDc);
                    intent.putExtras(bundleCuaHang);
                    startActivity(intent);
                }
            }
        });
    }

    private void muaNgay(SanPham sanPham) {

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dia_long_mua_ngay, null);
        ImageView imgAnhspMN, imgDongspMN, imgCongspMN, imgTruspMN;
        TextView tvGiaGiamspMN, tvGiaspMN;
        EditText edtsoLuongMuaMN;
        Button btnMuaNgayMN;
        imgAnhspMN = view.findViewById(R.id.imgAnhSPMN);
        imgDongspMN = view.findViewById(R.id.imgDongMN);

        imgCongspMN = view.findViewById(R.id.imgCongMN);
        imgTruspMN = view.findViewById(R.id.imgTruMN);

        tvGiaGiamspMN = view.findViewById(R.id.tvGiaGiamSPMN);
        tvGiaspMN = view.findViewById(R.id.tvGiaSPMN);

        edtsoLuongMuaMN = view.findViewById(R.id.edtsoLuongMuaMN);

        btnMuaNgayMN = view.findViewById(R.id.btnMuaMN);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM; // Đặt vị trí ở phía dưới
        window.setAttributes(wlp);

        if (sanPham != null || sanPham.getHinhAnhSanPham() != null) {
            Picasso.get().load(sanPham.getHinhAnhSanPham().get(0)).into(imgAnhspMN);
            tvGiaGiamspMN.setText("Giảm:" + daoformat.chuyenDinhDang(sanPham.getGiamGia()));
            tvGiaspMN.setText("Gía:" + daoformat.chuyenDinhDang(sanPham.getGia()));
            imgCongspMN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int soLuong = Integer.parseInt(edtsoLuongMuaMN.getText().toString());
                    
                    if (soLuong > sanPham.getSoLuong()) {
                        Log.d("sol",soLuong +"mua");
                        Log.d("sol co san",sanPham.getSoLuong() +"có san");
                        Log.d("1","1");
                        Log.d("2","2");

                        Toast.makeText(ChiTietSanPham.this, "Vượt quá số lượng có sẵn", Toast.LENGTH_SHORT).show();
                    }
                    edtsoLuongMuaMN.setText(String.valueOf(soLuong + 1));

                }
            });
            imgTruspMN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int soLuong = Integer.parseInt(edtsoLuongMuaMN.getText().toString());
                    if (soLuong > 1) {
                        edtsoLuongMuaMN.setText(String.valueOf(soLuong - 1));
                    }
                }
            });
            imgDongspMN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });

//            edtsoLuongMuaMN.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable) {
//                    String data = editable.toString().trim();
//                    if (Integer.parseInt(data) > sanPham.getSoLuong()) {
//                        checksoLuong = false;
//                        Toast.makeText(ChiTietSanPham.this, "Số lượng mua vượt quá số lượng có sẵn", Toast.LENGTH_SHORT).show();
//                    } else {
//                        checksoLuong = true;
//                    }
//                }
//            });
            btnMuaNgayMN.setOnClickListener(new View.OnClickListener() {
              int  soLuongMuonMua=Integer.parseInt(edtsoLuongMuaMN.getText().toString());
                @Override
                public void onClick(View view) {
                    if (edtsoLuongMuaMN.getText().toString().isEmpty()) {
                        Toast.makeText(ChiTietSanPham.this, "Vui lòng nhập số lượng mua", Toast.LENGTH_SHORT).show();
                    } else if (soLuongMuonMua > sanPham.getSoLuong() ) {
                        Toast.makeText(ChiTietSanPham.this, "Số lượng mua vượt quá số lượng có sẵn", Toast.LENGTH_SHORT).show();
                    } else {
                        int IDGH = new Random().nextInt(1000);
                        String MaGH = "GH" + IDGH;
                        gioHangs = new gioHang(ID, "dc chon", sanPham.getIDsanPham(),
                                sanPham.getIDcuaHang(), sanPham.getTenCuaHang(), sanPham.getHinhAnhSanPham().get(0), sanPham.getTenSanPham(), MaGH,
                                sanPham.getGia(), sanPham.getGiamGia(), Integer.parseInt(edtsoLuongMuaMN.getText().toString()), "MuaNgay");
                        daogioHang.themVaoGioHang(gioHangs);
                        Intent intent = new Intent(ChiTietSanPham.this, thanhToan.class);
                        startActivity(intent);

                    }
                }
            });
        }
        dialog.show();

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gioHangs != null) {
            daogioHang.xoaGioHang(gioHangs.getIDgioHang());
        }
        Log.d("gioHangonDestroy", "bị gioHangonDestroy");
    }

    private void PhanQuyen() {
        if (role.equals("Cửa hàng")) {
            tvGioHang.setVisibility(View.GONE);
            btnMuaNgay.setVisibility(View.GONE);
            img_ThemGioHang.setVisibility(View.GONE);
            if (!IDCuaHangCoSp.equals(ID)) {
                btnChayQC.setVisibility(View.GONE);
                TvDCQC_ctsp.setVisibility(View.GONE);
            }
        } else if(role.equals("Khách hàng")) {
            TvDCQC_ctsp.setVisibility(View.GONE);
            btnChayQC.setVisibility(View.GONE);
            if(ID!=null||ID.isEmpty()){
                daoKhachHang.TimKhachHang(ID, new DaoKhachHang.TraVeKhachHangTimDc() {
                    @Override
                    public void onTimDcKhachHang(KhachHang khachHang) {
                        xacThuc = khachHang.getXacThuc();
                    }

                    @Override
                    public void onKoTimKhachHang(String errorMessage) {

                    }
                });
            }
        } else {
            TvDCQC_ctsp.setVisibility(View.GONE);
            btnChayQC.setVisibility(View.GONE);
        }

    }

    private void Gan_ID() {
        sharedPreferences = ChiTietSanPham.this.getSharedPreferences("taiKhoan", Context.MODE_PRIVATE);
        ID = sharedPreferences.getString("IDuser", "");
        role = sharedPreferences.getString("role", "");
        img_thoatChiTietSP = findViewById(R.id.img_thoatChiTietSP);
        img_ThemGioHang = findViewById(R.id.imgThemVaoGioHang);
        imgAnhShop = findViewById(R.id.imgAnhShopctsp);

        btnChayQC = findViewById(R.id.btnChayQC);
        btnMuaNgay = findViewById(R.id.btnMuaSP);
        btnXemShop = findViewById(R.id.btnXemShopctsp);

        tvtensp_ctsp = findViewById(R.id.Tvtensp_ctsp);
        tvmaSp = findViewById(R.id.Tvmasp_ctsp);
        giamGia = findViewById(R.id.TvgiamGiasp_ctsp);
        gia = findViewById(R.id.TvGiasp_ctsp);
        luotMua = findViewById(R.id.TvluotMuasp_ctsp);
        soLuong = findViewById(R.id.TvcoSansp_ctsp);
        tvcpu = findViewById(R.id.TvCPUsp_ctsp);
        tvRam = findViewById(R.id.TvRAMsp_ctsp);
        tvOcung = findViewById(R.id.TvOcungsp_ctsp);
        tvCart = findViewById(R.id.TvCartsp_ctsp);
        tvMhinh = findViewById(R.id.TvMhinhsp_ctsp);
        tvGioHang = findViewById(R.id.tvGioHang);
        Tv_chiTiet = findViewById(R.id.Tv_ChiTietSanPham_ctsp);
        tvTenShop = findViewById(R.id.tvTenShopctsp);
        TvDCQC_ctsp = findViewById(R.id.TvDCQC_ctsp);

        rey_ds_anh_ct_sp = findViewById(R.id.ryc_ds_anh_sp);

        daoCuaHang = new DaoCuaHang(ChiTietSanPham.this);
        daoKhachHang = new DaoKhachHang(ChiTietSanPham.this);
        daogioHang = new DaoGioHang(ChiTietSanPham.this);
        daoformat = new Daoformat();


    }

}