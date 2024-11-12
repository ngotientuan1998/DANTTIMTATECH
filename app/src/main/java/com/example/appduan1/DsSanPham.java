package com.example.appduan1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import DAO.DaoChayQuangCao;
import DAO.DaoCuaHang;
import DAO.DaoLoaiHang;
import DAO.DaoSanPham;
import DTO.CuaHang;
import DTO.KiemTraThongTin;
import DTO.SanPham;
import adapter.adapterAnhSanPham;
import adapter.adapterSanPham;
import interfaces.SuaSanPhaminterface;
import interfaces.XoaSanPhaminterface;


public class DsSanPham extends AppCompatActivity implements XoaSanPhaminterface, SuaSanPhaminterface {
    ImageView imgthoat;
    SearchView searchView;
    RecyclerView recyclerView;
    FloatingActionButton floatingActionButton;
    FirebaseStorage storage;
    public static final int PICK_IMAGE_REQUEST = 1;
    List<String> ds_Anh;
    String loai;
    adapterAnhSanPham adapterAnhSanPhams;
    String IDcuaHang, role;
    SharedPreferences sharedPreferences;
    adapterSanPham adapterSanPhams;

    List<SanPham> ds_spCu;
    FirebaseFirestore db;
    DaoSanPham daoSanPham;
    DaoCuaHang daoCuaHang;
    CuaHang cuaHangTimDc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ds_san_pham);
        Gan_ID();
        SuKienClick();
    }

    private void Gan_ID() {
        db = FirebaseFirestore.getInstance();
        imgthoat = findViewById(R.id.img_ThoatSanPham);
        searchView = findViewById(R.id.SearchViewTimKiemSanPham);
        recyclerView = findViewById(R.id.ryc_sanPhamSP);
        floatingActionButton = findViewById(R.id.floatBtnThemSanPham);

        storage = FirebaseStorage.getInstance();
        ds_spCu = new ArrayList<>();
        sharedPreferences = getApplication().getSharedPreferences("taiKhoan", Context.MODE_PRIVATE);
        IDcuaHang = sharedPreferences.getString("IDuser", "");
        role = sharedPreferences.getString("role", "");
        ds_Anh = new ArrayList<>();

        daoSanPham = new DaoSanPham(DsSanPham.this);
        daoCuaHang = new DaoCuaHang(DsSanPham.this);
        daoCuaHang.TimCuaHang(IDcuaHang, new DaoCuaHang.TraVeCuaHangTimDc() {
            @Override
            public void onTimDcCuaHang(CuaHang cuaHang) {
                cuaHangTimDc = cuaHang;
                adapterSanPhams = new adapterSanPham(DsSanPham.this, ds_spCu, IDcuaHang, role, DsSanPham.this, DsSanPham.this);
                GridLayoutManager layoutManager = new GridLayoutManager(DsSanPham.this, 2);
                recyclerView.setAdapter(adapterSanPhams);
                recyclerView.setLayoutManager(layoutManager);
                daoSanPham.langNgheThayDoiSanPham(IDcuaHang, adapterSanPhams, ds_spCu);
            }

            @Override
            public void onKoTimCuaHang(String errorMessage) {

            }
        });


    }


    private void SuKienClick() {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ThemSanPham();
            }
        });
        imgthoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                TimKiem(newText);
                return true;
            }
        });
    }


    private void TimKiem(String s) {
        daoSanPham.layDanhSachSanPhamThuocCuaHang(IDcuaHang, new DaoSanPham.ToanBoDanhSachSanPham() {
            @Override
            public void ThanhCong(List<SanPham> sanPhamList) {
                Log.d("size",""+sanPhamList.size());
                ds_spCu.clear();
                for (SanPham x : sanPhamList) {
                    if (x.getTenSanPham().contains(s) || x.getLoaiSanPham().contains(s)) {
                        ds_spCu.add(x);
                    }
                }
            }

            @Override
            public void ThatBai(Exception e) {

            }
        });

        adapterSanPhams.notifyDataSetChanged();

    }


    private void ThemSanPham() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dia_long_them_san_pham, null);
        ds_Anh = new ArrayList<>();
        int ID = new Random().nextInt(1000);
        String MaSP = "SN" + ID;
        adapterAnhSanPhams = new adapterAnhSanPham(this, ds_Anh, "ThemSanPham");
        ImageView imgDong;
        EditText edtTen, edtGia, edtGiamGia, edtSoLuong, edtMoTa, edtCPU, edtRam, edtOcung, edtCart, edtMhinh;
        Spinner spinnerLoai;
        RecyclerView rycAnh;
        Button btnTaiAnh, btnXacNhanThem;
        edtTen = view.findViewById(R.id.edtTenSPThem);
        edtGia = view.findViewById(R.id.edtGiaSPThem);
        edtGiamGia = view.findViewById(R.id.edtGiamGiaSPThem);
        edtSoLuong = view.findViewById(R.id.edtsoLuongSPThem);
        edtMoTa = view.findViewById(R.id.edtMoTaThemSP);
        edtCPU = view.findViewById(R.id.edtCPUSPThem);
        edtRam = view.findViewById(R.id.edtRAmSPThem);
        edtOcung = view.findViewById(R.id.edtoCungSPThem);
        edtCart = view.findViewById(R.id.edtCartSPThem);
        edtMhinh = view.findViewById(R.id.edtMhinhSPThem);

        imgDong = view.findViewById(R.id.imgDongSpThem);

        spinnerLoai = view.findViewById(R.id.spinnerLoaiSPThem);
        rycAnh = view.findViewById(R.id.rycAnhSPThem);

        btnTaiAnh = view.findViewById(R.id.btnTaiAnhSPThem);
        btnXacNhanThem = view.findViewById(R.id.btnThemSP);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        DaoLoaiHang daoLoaiHang = new DaoLoaiHang(this);
        daoLoaiHang.Adapter(spinnerLoai,2);

        imgDong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        spinnerLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                loai = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnTaiAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/anhSanPham");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        KiemTraThongTin kiemTraThongTin = new KiemTraThongTin();
        rycAnh.setAdapter(adapterAnhSanPhams);
        rycAnh.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        btnXacNhanThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten, gia, giamGia, soLuong, moTa, cpu, ram, oCung, card, mHinh;
                ten = edtTen.getText().toString();
                gia = edtGia.getText().toString();
                giamGia = edtGiamGia.getText().toString();
                soLuong = edtSoLuong.getText().toString();
                moTa = edtMoTa.getText().toString();
                cpu = edtCPU.getText().toString();
                ram = edtRam.getText().toString();
                oCung = edtOcung.getText().toString();
                card = edtCart.getText().toString();
                mHinh = edtMhinh.getText().toString();
                if (ten.trim().isEmpty() || gia.trim().isEmpty() || giamGia.trim().isEmpty() || soLuong.trim().isEmpty() || moTa.trim().isEmpty()
                        || loai.trim().isEmpty() || cpu.trim().isEmpty() || ram.trim().isEmpty() || oCung.trim().isEmpty() || card.trim().isEmpty()
                        || mHinh.trim().isEmpty()) {
                    Toast.makeText(getApplication(), "Không để trống thông tin", Toast.LENGTH_SHORT).show();
                } else if (ds_Anh.isEmpty()) {
                    Toast.makeText(getApplication(), "Vui lòng tải 1 hình anh cho sản phẩm", Toast.LENGTH_SHORT).show();
                } else if (!kiemTraThongTin.KTsoThuc(gia) || Double.parseDouble(gia) <= 0) {
                    Toast.makeText(getApplication(), "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (!kiemTraThongTin.KTsoThuc(giamGia) || Double.parseDouble(giamGia) < 0) {
                    Toast.makeText(getApplication(), "Giảm giá không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (!kiemTraThongTin.KtsoNguyen(soLuong) || Integer.parseInt(soLuong) <= 0) {
                    Toast.makeText(getApplication(), "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {

                    Calendar calendar = Calendar.getInstance();
                    int year = calendar.get(Calendar.YEAR);
                    int month = calendar.get(Calendar.MONTH) + 1; // Tháng trong Calendar là từ 0 đến 11
                    int day = calendar.get(Calendar.DAY_OF_MONTH);
                    String ngayThangNam = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
                    SanPham sanPham = new SanPham(MaSP, IDcuaHang, cuaHangTimDc.getTenCuaHang(), ten, moTa, loai, ngayThangNam, cpu, ram, oCung, card, mHinh, Double.parseDouble(gia), Double.parseDouble(giamGia), Integer.parseInt(soLuong), 0, 0, ds_Anh);
                    DaoSanPham daoSanPham = new DaoSanPham(getApplication());
                    if (IDcuaHang != null) {
                        daoSanPham.themSanPhamVaoCuaHang(IDcuaHang, sanPham);
                        dialog.dismiss();
                    }

                }
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Lấy đường dẫn của hình ảnh được chọn
            Uri uri = data.getData();
            uploadImageToFirebaseStorage(uri);
        }
    }

    private void uploadImageToFirebaseStorage(Uri imageUri) {
        StorageReference storageRef = storage.getReference();
        StorageReference imageRef = storageRef.child("anhSanPham/" + System.currentTimeMillis() + ".jpg");


        try {
            // Đọc hình ảnh từ Uri thành byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), imageUri).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();

            // Tải lên hình ảnh lên Firebase Storage
            UploadTask uploadTask = imageRef.putBytes(imageData);
            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Lấy đường dẫn của hình ảnh trên Firebase Storage
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Đường dẫn của hình ảnh trên Firebase Storage
                        String imageUrl = uri.toString();
                        ds_Anh.add(imageUrl);
                        adapterAnhSanPhams.notifyItemInserted(ds_Anh.size() - 1);
                    });
                } else {
                    // Xử lý lỗi khi tải lên
                    Exception e = task.getException();
                    e.printStackTrace();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void XoaSanPham(SanPham x) {
        if (!IDcuaHang.isEmpty() && x != null) {
            new AlertDialog.Builder(DsSanPham.this)
                    .setTitle("Xác nhận trạng thái")
                    .setMessage("Bạn có muốn xóa không?")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            daoSanPham.xoaSanPhamKhoiCuaHang(IDcuaHang, x.getIDsanPham());
                        }
                    }).setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();

        }
    }

    @Override
    public void SuaSanPham(SanPham sanPham) {
        HienThiSuaSanPham(sanPham);
    }


    private void HienThiSuaSanPham(SanPham sanPhams) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dia_long_them_san_pham, null);
//        sharedPreferences = getApplication().getSharedPreferences("taiKhoan", Context.MODE_PRIVATE);
//        IDcuaHang = sharedPreferences.getString("IDuser", "");
        ds_Anh = sanPhams.getHinhAnhSanPham();
        adapterAnhSanPhams = new adapterAnhSanPham(DsSanPham.this, ds_Anh, "SuaSanPham", IDcuaHang, sanPhams.getIDsanPham());
        EditText edtTen, edtGia, edtGiamGia, edtSoLuong, edtMoTa, edtCPU, edtRam, edtOcung, edtCart, edtMhinh;
        Spinner spinnerLoai;
        RecyclerView rycAnh;
        Button btnTaiAnh, btnXacNhanThem;
        edtTen = view.findViewById(R.id.edtTenSPThem);
        edtGia = view.findViewById(R.id.edtGiaSPThem);
        edtGiamGia = view.findViewById(R.id.edtGiamGiaSPThem);
        edtSoLuong = view.findViewById(R.id.edtsoLuongSPThem);
        edtMoTa = view.findViewById(R.id.edtMoTaThemSP);
        edtCPU = view.findViewById(R.id.edtCPUSPThem);
        edtRam = view.findViewById(R.id.edtRAmSPThem);
        edtOcung = view.findViewById(R.id.edtoCungSPThem);
        edtCart = view.findViewById(R.id.edtCartSPThem);
        edtMhinh = view.findViewById(R.id.edtMhinhSPThem);


        spinnerLoai = view.findViewById(R.id.spinnerLoaiSPThem);
        rycAnh = view.findViewById(R.id.rycAnhSPThem);

        btnTaiAnh = view.findViewById(R.id.btnTaiAnhSPThem);
        btnXacNhanThem = view.findViewById(R.id.btnThemSP);
        btnXacNhanThem.setText("Sửa sản Phảm");


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        DaoLoaiHang daoLoaiHang = new DaoLoaiHang(this);
        String[] dsLoai = daoLoaiHang.getDsLoaiHang2();
        daoLoaiHang.Adapter(spinnerLoai,2);
        edtTen.setText(sanPhams.getTenSanPham());
        edtGia.setText(String.valueOf(sanPhams.getGia()));
        edtGiamGia.setText(String.valueOf(sanPhams.getGiamGia()));
        edtSoLuong.setText(String.valueOf(sanPhams.getSoLuong()));
        edtMoTa.setText(String.valueOf(sanPhams.getChiTietSanPham()));
        edtCPU.setText(String.valueOf(sanPhams.getCPU()));
        edtRam.setText(String.valueOf(sanPhams.getRAM()));
        edtCart.setText(String.valueOf(sanPhams.getCard()));
        edtOcung.setText(String.valueOf(sanPhams.getoCung()));
        edtMhinh.setText(String.valueOf(sanPhams.getMhinh()));
        spinnerLoai.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (dsLoai != null) {
                    int index = -1;
                    for (i = 0; i < dsLoai.length; i++) {
                        if (dsLoai[i].equals(sanPhams.getLoaiSanPham())) {
                            index = i;
                            break;
                        }
                    }
                    if (index != -1) {
                        spinnerLoai.setSelection(index);
                    }
                }
                loai = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        btnTaiAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/anhSanPham");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        KiemTraThongTin kiemTraThongTin = new KiemTraThongTin();
        rycAnh.setAdapter(adapterAnhSanPhams);
        rycAnh.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        btnXacNhanThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten, gia, giamGia, soLuong, moTa, cpu, ram, oCung, card, mHinh;
                ten = edtTen.getText().toString();
                gia = edtGia.getText().toString();
                giamGia = edtGiamGia.getText().toString();
                soLuong = edtSoLuong.getText().toString();
                moTa = edtMoTa.getText().toString();
                cpu = edtCPU.getText().toString();
                ram = edtRam.getText().toString();
                oCung = edtOcung.getText().toString();
                card = edtCart.getText().toString();
                mHinh = edtMhinh.getText().toString();
                if (ten.trim().isEmpty() || gia.trim().isEmpty() || giamGia.trim().isEmpty() || soLuong.trim().isEmpty() || moTa.trim().isEmpty()
                        || loai.trim().isEmpty() || cpu.trim().isEmpty() || ram.trim().isEmpty() || oCung.trim().isEmpty() || card.trim().isEmpty()
                        || mHinh.trim().isEmpty()) {
                    Toast.makeText(getApplication(), "Không để trống thông tin", Toast.LENGTH_SHORT).show();
                } else if (ds_Anh.isEmpty()) {
                    Toast.makeText(getApplication(), "Vui lòng tải 1 hình anh cho sản phẩm", Toast.LENGTH_SHORT).show();
                } else if (!kiemTraThongTin.KTsoThuc(gia) || Double.parseDouble(gia) <= 0) {
                    Toast.makeText(getApplication(), "Giá không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (!kiemTraThongTin.KTsoThuc(giamGia) || Double.parseDouble(giamGia) < 0) {
                    Toast.makeText(getApplication(), "Giảm giá không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (!kiemTraThongTin.KtsoNguyen(soLuong) || Integer.parseInt(soLuong) <= 0) {
                    Toast.makeText(getApplication(), "Số lượng không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(DsSanPham.this)
                            .setTitle("Xác nhận sửa sản phẩm")
                            .setMessage("Bạn có muốn sửa không?")
                            .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    SanPham sanPham = new SanPham(sanPhams.getIDsanPham(), cuaHangTimDc.getTenCuaHang(), IDcuaHang, ten, moTa, loai, sanPhams.getNgayThem(), cpu, ram, oCung, card, mHinh, Double.parseDouble(gia), Double.parseDouble(giamGia), Integer.parseInt(soLuong), 0, 0, ds_Anh);
                                    DaoSanPham daoSanPham = new DaoSanPham(getApplication());
                                    if (IDcuaHang != null) {
                                        daoSanPham.capNhatSanPhamTrongCuaHang(IDcuaHang, sanPhams.getIDsanPham(), sanPham);
                                    }
                                }
                            }).setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            })
                            .show();


                }
            }
        });
        dialog.show();
    }

}