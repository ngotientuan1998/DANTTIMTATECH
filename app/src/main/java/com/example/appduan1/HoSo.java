package com.example.appduan1;

import static framentbottom.HomeFragment.PICK_IMAGE_REQUEST;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import DAO.DaoCuaHang;
import DAO.DaoKhachHang;
import DTO.CuaHang;
import DTO.KhachHang;
import DTO.KiemTraThongTin;
import DTO.huyen;
import DTO.tinh;
import DTO.xa;

public class HoSo extends AppCompatActivity {
    private TextView tvID, tvTen, tvTinh, tvHuyen, tvXa, tvDiaChi, tvSDT, tvEmail, tvVaiTro, tvNTNT, tvGioiTinh, tvNamSinh, tvTheoDoi;
    private Button btnDangXuat, btnSua, btnDsSanPham, btnDSSPchayQuangCaoHoSo, btnDoiMatKhau;
    private ImageView imgAnh, imgThoat;
    String ID, role, Tinh, Huyen, xa;
    KhachHang khachHang;
    CuaHang cuaHang;
    SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    Bundle bundle;
    FirebaseStorage storage;
    FirebaseFirestore db;
    String imageUrl;
    KiemTraThongTin kiemTraThongTin;
    int checkVaiTro = 0;
    ImageView imgAnhSua;
    List<tinh> dsTinh;
    Spinner spinnerTinh, spinnerHuyen, spinnerXa;
    ArrayAdapter<String> adapterTinh, adapterHuyen, adapterXa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ho_so);
        Gan_ID();
        PhanQuyen();
        SuKienOnclick();
    }

    private void Gan_ID() {
        kiemTraThongTin = new KiemTraThongTin();
        db = FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        tvID = findViewById(R.id.tvID);
        tvTen = findViewById(R.id.tvHoVaTen);
        tvTinh = findViewById(R.id.tvTinh);
        tvHuyen = findViewById(R.id.tvHuyen);
        tvXa = findViewById(R.id.tvXa);
        tvDiaChi = findViewById(R.id.tvDiaChiChiTiet);
        tvSDT = findViewById(R.id.tvSoDienThoai);
        tvEmail = findViewById(R.id.tvEmail);
        tvVaiTro = findViewById(R.id.tvVaiTro);
        tvNTNT = findViewById(R.id.tvNTNT);
        tvGioiTinh = findViewById(R.id.tvGioiTinh);
        tvNamSinh = findViewById(R.id.tvNamSinh);
        tvTheoDoi = findViewById(R.id.tvTheoDoi);

        btnSua = findViewById(R.id.btnSuaHoSo);
        btnDangXuat = findViewById(R.id.btnDangXuat);
        btnDsSanPham = findViewById(R.id.btnDSSPShopHoSo);
        btnDSSPchayQuangCaoHoSo = findViewById(R.id.btnDSSPchayQuangCaoHoSo);
        btnDoiMatKhau = findViewById(R.id.btnDoiMatKhauHoSo);

        imgAnh = findViewById(R.id.imAnhHoSo);
        imgThoat = findViewById(R.id.imThoatHoSo);
    }

    public void SuKienOnclick() {
        btnDoiMatKhau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DoiMatKhau();
            }
        });
        imgThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suaHoSo();
            }
        });
        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangXuat();
            }
        });
        btnDsSanPham.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HoSo.this, DsSanPham.class);
                startActivity(intent);
            }
        });
        btnDSSPchayQuangCaoHoSo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HoSo.this, DsDonChayQuangCao.class);
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
                        editor.remove("IDuser");
                        editor.remove("role");
                        if (nhoTaiKhoan) {
                            editor.putBoolean("nhoTaiKhoan", false);
                        }
                        editor.apply();

                        // Chuyển người dùng đến màn hình đăng nhập hoặc màn hình khác
                        Intent intent = new Intent(getApplication(), DangNhapDangKi.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Hủy bỏ", null)
                .show();
    }


    private void suaHoSo() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialong_sua_hoso, null);
        EditText edtTenSua, edtSDTSua, edtEmailSua, edtNamSinhSua, edtDiaChi;

        Button btnXacNhanSua;
        RadioButton rbNamSua, rbNuSua;
        imgAnhSua = view.findViewById(R.id.imAnhHoSoSua);
        edtTenSua = view.findViewById(R.id.EdtHoVaTenSua);
        edtSDTSua = view.findViewById(R.id.EdtSoDienThoaiSua);
        edtEmailSua = view.findViewById(R.id.EdtEmailSua);
        edtNamSinhSua = view.findViewById(R.id.EdtNamSinhSua);
        edtDiaChi = view.findViewById(R.id.edt_DiaChiChiTietSua);

        spinnerTinh = view.findViewById(R.id.spinner_TinhSua);
        spinnerHuyen = view.findViewById(R.id.spinner_HuyenSua);
        spinnerXa = view.findViewById(R.id.spinner_XaSua);

        rbNamSua = view.findViewById(R.id.rbNamSua);
        rbNuSua = view.findViewById(R.id.rbNuSua);

        btnXacNhanSua = view.findViewById(R.id.btnSuaHoSoSua);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (role != null) {
            if (role.equals("Cửa hàng")) {
                edtNamSinhSua.setVisibility(View.GONE);
                rbNuSua.setVisibility(View.GONE);
                rbNamSua.setVisibility(View.GONE);
                if (cuaHang != null) {
                    if (cuaHang.getHinhAnh() != null) {
                        Picasso.get().load(cuaHang.getHinhAnh()).into(imgAnhSua);
                    }
                    edtTenSua.setText(cuaHang.getTenCuaHang());
                    edtEmailSua.setText(cuaHang.getEmail());
                    edtSDTSua.setText("0" + String.valueOf(cuaHang.getSdt()));
                    edtDiaChi.setText(String.valueOf(cuaHang.getDiaChiChiTiet()));

                    checkVaiTro = 1;
                }
            } else {
                if (khachHang != null) {
                    Picasso.get().load(khachHang.getHinhAnh()).into(imgAnhSua);
                }
                edtTenSua.setText(khachHang.getHoTen());
                edtEmailSua.setText(khachHang.getEmail());
                edtSDTSua.setText("0" + String.valueOf(khachHang.getSdt()));
                edtDiaChi.setText(String.valueOf(khachHang.getDiaChiChiTiet()));
                edtNamSinhSua.setText(String.valueOf(khachHang.getNTNS()));
                if (khachHang.getGioiTinh().equals("Nam")) {
                    rbNamSua.setChecked(true);
                } else {
                    rbNuSua.setChecked(true);
                }
            }
        }
        imgAnhSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/anhDaiDien");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);


            }
        });
        DuLieuDiaChi();
        btnXacNhanSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten, diaChi, sdt, email, ns, gioiTinh;
                ten = edtTenSua.getText().toString();
                diaChi = edtDiaChi.getText().toString();
                sdt = edtSDTSua.getText().toString();
                email = edtEmailSua.getText().toString();
                ns = edtNamSinhSua.getText().toString();
                if (rbNuSua.isChecked()) {
                    gioiTinh = "Nữ";
                } else {
                    gioiTinh = "Nam";
                }
                if (ten.trim().isEmpty() || diaChi.trim().isEmpty() || sdt.trim().isEmpty() || email.trim().isEmpty()) {
                    Toast.makeText(HoSo.this, "không để trống các trường thông tin", Toast.LENGTH_SHORT).show();
                } else if (!kiemTraThongTin.isValidPhoneNumber(sdt)) {
                    Toast.makeText(HoSo.this, "số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                } else if (!kiemTraThongTin.isValidEmail(email)) {
                    Toast.makeText(HoSo.this, "email không hợp lệ", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog.Builder confirmBuilder = new AlertDialog.Builder(HoSo.this);
                    confirmBuilder.setTitle("Xác nhận sửa thông tin");
                    confirmBuilder.setMessage("Bạn có chắc muốn sửa thông tin không?");
                    confirmBuilder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (ID != null) {
                                if (checkVaiTro == 1) {
                                    DaoCuaHang daoCuaHang = new DaoCuaHang(getApplication());
                                    if (daoCuaHang.suaCuaHang(ID, imageUrl, ten, email, Integer.parseInt(sdt), Tinh, Huyen, xa, diaChi)) {
                                        tvTen.setText("Tên:" + ten);
                                        tvEmail.setText("email:" + email);
                                        tvSDT.setText("số điện thoại:" + sdt);
                                        tvTinh.setText("Tỉnh:" + Tinh);
                                        tvHuyen.setText("Huyện:" + Huyen);
                                        tvXa.setText("Xã:" + xa);
                                        tvDiaChi.setText("Địa chỉ:" + diaChi);

                                        cuaHang.setTenCuaHang(ten);
                                        cuaHang.setEmail(email);
                                        cuaHang.setSdt(Integer.parseInt(sdt));
                                        cuaHang.setTinh(Tinh);
                                        cuaHang.setHuyen(Huyen);
                                        cuaHang.setXa(xa);
                                        cuaHang.setDiaChiChiTiet(diaChi);
                                        if (imageUrl != null) {
                                            cuaHang.setHinhAnh(imageUrl);
                                            Picasso.get().load(imageUrl).into(imgAnhSua);
                                            Picasso.get().load(imageUrl).into(imgAnh);
                                        }

                                        Toast.makeText(HoSo.this, "Sửa thông tin cửa hàng thành công", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    }
                                } else {
                                    if (ns.trim().isEmpty() || gioiTinh.trim().isEmpty()) {
                                        Toast.makeText(HoSo.this, "không để trống các trường thông tin", Toast.LENGTH_SHORT).show();
                                    } else if (!kiemTraThongTin.KtsoNguyen(ns)) {
                                        Toast.makeText(HoSo.this, "ns không hợp lệ", Toast.LENGTH_SHORT).show();
                                    } else if (2023 - Integer.parseInt(ns) < 16) {
                                        Toast.makeText(HoSo.this, "tuổi phải lớn hơn 16", Toast.LENGTH_SHORT).show();
                                    } else {
                                        DaoKhachHang daoKhachHang = new DaoKhachHang(getApplication());
                                        if (daoKhachHang.suaKhachHang(ID, imageUrl, ten, email, Integer.parseInt(sdt), Integer.parseInt(ns), gioiTinh, Tinh, Huyen, xa, diaChi)) {

                                            tvTen.setText("Tênokok:" + ten);
                                            tvEmail.setText("Email:" + email);
                                            tvSDT.setText("Số điện thoại:" + sdt);
                                            tvTinh.setText("Tỉnh:" + Tinh);
                                            tvHuyen.setText("Huyện:" + Huyen);
                                            tvXa.setText("xã:" + xa);
                                            tvDiaChi.setText("Địa chỉ:" + diaChi);
                                            tvNamSinh.setText("Năm sinh:" + ns);
                                            tvGioiTinh.setText("Giới tính:" + gioiTinh);

                                            khachHang.setHoTen(ten);
                                            khachHang.setEmail(email);
                                            khachHang.setSdt(Integer.parseInt(sdt));
                                            khachHang.setTinh(Tinh);
                                            khachHang.setHuyen(Huyen);
                                            khachHang.setXa(xa);
                                            khachHang.setDiaChiChiTiet(diaChi);
                                            khachHang.setNTNS(Integer.parseInt(ns));
                                            khachHang.setGioiTinh(gioiTinh);

                                            if (imageUrl != null) {
                                                Picasso.get().load(imageUrl).into(imgAnhSua);
                                                Picasso.get().load(imageUrl).into(imgAnh);
                                                khachHang.setHinhAnh(imageUrl);
                                            }


                                            Toast.makeText(HoSo.this, "Sửa thông tin khách hàng thành công", Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();
                                        }

                                    }

                                }


                            }//ID

                        }
                    });
                    confirmBuilder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    confirmBuilder.show();
                }

            }

        });
        dialog.show();

    }

    private void DoiMatKhau() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialong_doi_mat_khau, null);
        EditText edtMkCu, edtMKmoi, edtlaiMkmoi;
        Button btnDoiMK;

        edtMkCu = view.findViewById(R.id.edtMKcuDMK);
        edtMKmoi = view.findViewById(R.id.edtMKmoiDMK);
        edtlaiMkmoi = view.findViewById(R.id.edtMKlaiMKmoiDMK);
        btnDoiMK = view.findViewById(R.id.btnDoiMatKhau);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        btnDoiMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentPassword = edtMkCu.getText().toString();
                String newPassword = edtMKmoi.getText().toString();
                String confirmPassword = edtlaiMkmoi.getText().toString();
                if (currentPassword.trim().isEmpty() || newPassword.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
                    Toast.makeText(HoSo.this, "Mật khẩu cũ, mật khẩu mới hoặc xác nhận mật khẩu trống", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

//                     ProgressBarDangNhap.setVisibility(View.VISIBLE);

                        // Kiểm tra mật khẩu cũ
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> reauthTask) {
                                        if (reauthTask.isSuccessful()) {
                                            // Nếu mật khẩu cũ đúng, tiến hành đổi mật khẩu
                                            user.updatePassword(newPassword)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {


                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(HoSo.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
                                                            } else {
                                                                Toast.makeText(HoSo.this, "Đổi mật khẩu thất bại", Toast.LENGTH_SHORT).show();

                                                                // Hiển thị thông báo lỗi chi tiết nếu cần
                                                                if (task.getException() != null) {
                                                                    Log.e("ChangePassword", "Error: " + task.getException().getMessage());
                                                                }
                                                            }
                                                        }
                                                    });
                                        } else {
//                                         ProgressBarDangNhap.setVisibility(View.GONE);
                                            Toast.makeText(HoSo.this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
            }

        });

        dialog.show();

    }


    public void PhanQuyen() {
        sharedPreferences = getSharedPreferences("taiKhoan", getApplication().MODE_PRIVATE);
        editor = sharedPreferences.edit();
        ID = sharedPreferences.getString("IDuser", "");
        role = sharedPreferences.getString("role", "");
        bundle = getIntent().getExtras();
        if (bundle != null) {
            if (role.equals("Cửa hàng")) {
                cuaHang = (CuaHang) bundle.getSerializable("cuaHang");
                tvNamSinh.setVisibility(View.GONE);
                tvGioiTinh.setVisibility(View.GONE);
                if (cuaHang != null) {
                    if (cuaHang.getHinhAnh() != null && !cuaHang.getHinhAnh().isEmpty()) {
                        Picasso.get().load(cuaHang.getHinhAnh()).into(imgAnh);
                    }

                    tvID.setText("ID:" + cuaHang.getCuaHangID());
                    tvTen.setText("Tên:" + cuaHang.getTenCuaHang());
                    tvTinh.setText("Tỉnh:" + cuaHang.getTinh());
                    tvHuyen.setText("Huyện:" + cuaHang.getHuyen());
                    tvXa.setText("Xã:" + cuaHang.getXa());
                    tvDiaChi.setText("Địa chỉ:" + cuaHang.getDiaChiChiTiet());
                    tvSDT.setText("số điện thoại:" + String.valueOf(cuaHang.getSdt()));
                    tvEmail.setText("email:" + cuaHang.getEmail());
                    tvVaiTro.setText("vai trò:" + cuaHang.getRole());
                    tvNTNT.setText("Thời gian tạo:" + cuaHang.getNTNT());
                    tvTheoDoi.setText("Theo dõi:" + String.valueOf(cuaHang.getTheoDoi()));
                } else {
                    Toast.makeText(this, "cửa hàng null", Toast.LENGTH_SHORT).show();
                }
            } else {
                khachHang = (KhachHang) bundle.getSerializable("khachHang");
                tvTheoDoi.setVisibility(View.GONE);
                btnDsSanPham.setVisibility(View.GONE);
                btnDSSPchayQuangCaoHoSo.setVisibility(View.GONE);
                if (khachHang != null) {
                    if (khachHang.getHinhAnh() != null && !khachHang.getHinhAnh().isEmpty()) {
                        Picasso.get().load(khachHang.getHinhAnh()).into(imgAnh);
                    }

                    tvID.setText("ID:" + khachHang.getKhachHangID());
                    tvTen.setText("Tên:" + khachHang.getHoTen());
                    tvTinh.setText("Tỉnh:" + khachHang.getTinh());
                    tvHuyen.setText("Huyện:" + khachHang.getHuyen());
                    tvXa.setText("Xã:" + khachHang.getXa());
                    tvDiaChi.setText("Địa chỉ:" + khachHang.getDiaChiChiTiet());
                    tvSDT.setText("Số điện thoại:" + String.valueOf(khachHang.getSdt()));
                    tvEmail.setText("email:" + khachHang.getEmail());
                    tvVaiTro.setText("Vai trò:" + khachHang.getRole());
                    tvNTNT.setText("Thời gian tạo" + khachHang.getNTNT());
                    tvGioiTinh.setText("Giới tính:" + khachHang.getGioiTinh());
                    tvNamSinh.setText("Năm sinh:" + String.valueOf(khachHang.getNTNS()));
                } else {
                    Toast.makeText(this, "khách hàng null", Toast.LENGTH_SHORT).show();
                }
            }
        }
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
        StorageReference imageRef = storageRef.child("anhDaiDien/" + System.currentTimeMillis() + ".jpg");


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
                        imageUrl = uri.toString();
                        Picasso.get().load(imageUrl).into(imgAnhSua);
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

    private void DuLieuDiaChi() {

        dsTinh = new ArrayList<>();

        List<xa> dsXa_NhoQuan = new ArrayList<>();
        List<xa> dsXa_HoaLu = new ArrayList<>();
        List<xa> dsXa_KimSon = new ArrayList<>();
        //Tỉnh Ninh Bình
        huyen NhoQuan = new huyen("Nho Quan", dsXa_NhoQuan);
        dsXa_NhoQuan.add(new xa("Phú Long"));
        dsXa_NhoQuan.add(new xa("Quỳnh Lưu"));
        dsXa_NhoQuan.add(new xa("Văn Phú"));

        huyen HoaLu = new huyen("Hoa Lư", dsXa_HoaLu);
        dsXa_HoaLu.add(new xa("Trường Yên"));
        dsXa_HoaLu.add(new xa("Chùa Bái Đính"));
        dsXa_HoaLu.add(new xa("Lê Đại Hành"));

        huyen KimSon = new huyen("Kim Sơn", dsXa_KimSon);
        dsXa_KimSon.add(new xa("Hồi Ninh"));
        dsXa_KimSon.add(new xa("Chất Bình"));
        dsXa_KimSon.add(new xa("Yên Mật"));

        List<huyen> ds_Huyen_NinhBinh = new ArrayList<>();
        ds_Huyen_NinhBinh.add(NhoQuan);
        ds_Huyen_NinhBinh.add(HoaLu);
        ds_Huyen_NinhBinh.add(KimSon);

        tinh NinhBinh = new tinh("Ninh Bình", ds_Huyen_NinhBinh);

        //Tỉnh Thanh Hóa
        List<xa> dsXa_BaPhuoc = new ArrayList<>();
        dsXa_BaPhuoc.add(new xa("Ái Thượng"));
        dsXa_BaPhuoc.add(new xa("Ban Công"));
        dsXa_BaPhuoc.add(new xa("Cổ Lũng"));
        huyen BaPhuoc = new huyen("Bá Thước", dsXa_BaPhuoc);

        List<xa> dsXa_CamThuy = new ArrayList<>();
        dsXa_CamThuy.add(new xa("Cẩm Bình"));
        dsXa_CamThuy.add(new xa("Cẩm Châu"));
        dsXa_CamThuy.add(new xa("Cẩm Giang"));
        huyen CamThuy = new huyen("Cẩm Thủy", dsXa_BaPhuoc);

        List<xa> dsXa_DongSon = new ArrayList<>();
        dsXa_DongSon.add(new xa("Đông Hòa"));
        dsXa_DongSon.add(new xa(" Đông Hoàng"));
        dsXa_DongSon.add(new xa("Đông Khê"));
        huyen DongSon = new huyen("Đông Sơn", dsXa_BaPhuoc);

        List<huyen> ds_Huyen_ThanhHoa = new ArrayList<>();
        ds_Huyen_ThanhHoa.add(BaPhuoc);
        ds_Huyen_ThanhHoa.add(CamThuy);
        ds_Huyen_ThanhHoa.add(DongSon);
        tinh ThanhHoa = new tinh("Thanh Hóa", ds_Huyen_ThanhHoa);

        dsTinh.add(NinhBinh);
        dsTinh.add(ThanhHoa);

        ArrayList<String> layDanhSachTenTinh = new ArrayList<>();
        for (tinh x : dsTinh) {
            layDanhSachTenTinh.add(x.getTinh());
        }
        adapterTinh = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_item, layDanhSachTenTinh);
        adapterTinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTinh.setAdapter(adapterTinh);
        if (role.equals("Cửa hàng")) {
            if (cuaHang != null) {
                int Index = dsTinh.indexOf(cuaHang.getTinh());
                spinnerTinh.setSelection(Index);
            }
        } else {
            if (cuaHang != null) {
                int Index = dsTinh.indexOf(khachHang.getTinh());
                spinnerTinh.setSelection(Index);
            }
        }
        spinnerTinh.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Tinh = adapterView.getItemAtPosition(i).toString();
                capNhatSpinnerHuyen(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void capNhatSpinnerHuyen(int viTriTinh) {
        if (viTriTinh >= 0 && viTriTinh < dsTinh.size()) {
            tinh tinhs = dsTinh.get(viTriTinh);
            List<huyen> danhSachHuyen = tinhs.getList();
            ArrayList<String> danhSachTenHuyen = new ArrayList<>();

            for (huyen x : danhSachHuyen) {
                danhSachTenHuyen.add(x.getTen());
            }
            adapterHuyen = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_item, danhSachTenHuyen);
            adapterHuyen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerHuyen.setAdapter(adapterHuyen);
            if (role.equals("Cửa hàng")) {
                if (cuaHang != null) {
                    int Index = danhSachHuyen.indexOf(cuaHang.getHuyen());
                    spinnerHuyen.setSelection(Index);
                }
            } else {
                if (cuaHang != null) {
                    int Index = danhSachHuyen.indexOf(khachHang.getHuyen());
                    spinnerHuyen.setSelection(Index);
                }
            }
            spinnerHuyen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    Huyen = adapterView.getItemAtPosition(i).toString();
                    capNhatSpinnerXa(i);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    private void capNhatSpinnerXa(int viTriHuyen) {
        int viTriTinh = spinnerTinh.getSelectedItemPosition();

        if (viTriTinh >= 0 && viTriTinh < dsTinh.size() && viTriHuyen >= 0) {
            tinh tinhs = dsTinh.get(viTriTinh);
            List<huyen> danhSachHuyen = tinhs.getList();

            if (viTriHuyen < danhSachHuyen.size()) {
                huyen huyens = danhSachHuyen.get(viTriHuyen);
                List<xa> danhSachXa = huyens.getList();
                ArrayList<String> danhSachTenXa = new ArrayList<>();

                for (xa x : danhSachXa) {
                    danhSachTenXa.add(x.getTen());
                }

                adapterXa = new ArrayAdapter<>(getApplication(), android.R.layout.simple_spinner_item, danhSachTenXa);
                adapterXa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerXa.setAdapter(adapterXa);

                if (role.equals("Cửa hàng")) {
                    if (cuaHang != null) {
                        int Index = danhSachXa.indexOf(cuaHang.getXa());
                        spinnerXa.setSelection(Index);
                    }
                } else {
                    if (cuaHang != null) {
                        int Index = danhSachXa.indexOf(khachHang.getXa());
                        spinnerXa.setSelection(Index);
                    }
                }
                spinnerXa.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        xa = adapterView.getItemAtPosition(i).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }
    }


}