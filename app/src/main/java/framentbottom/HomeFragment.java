package framentbottom;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appduan1.HoSo;
import com.example.appduan1.R;
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
import DAO.DaoLoaiHang;
import DAO.DaoSanPham;
import DTO.CuaHang;
import DTO.KhachHang;
import DTO.KiemTraThongTin;
import DTO.SanPham;
import DTO.huyen;
import DTO.tinh;
import DTO.xa;
import adapter.adapterSanPham;


public class HomeFragment extends Fragment {
    private ImageView shop, slider;
    private androidx.appcompat.widget.SearchView searchView;
    private TextView LoaiHang;
    private Button btnXacThucTK;
    String role, ID, imageUrl, Tinh, Huyen, xa;
    int trangThai = 1;
    ImageView imgAnh;
    List<tinh> dsTinh;
    Spinner spinnerTinh, spinnerHuyen, spinnerXa, spinnerLoaiHang;
    ArrayAdapter<String> adapterTinh, adapterHuyen, adapterXa;
    public static final int PICK_IMAGE_REQUEST = 1;

    SharedPreferences sharedPreferences;
    private RecyclerView ryc_hang;
    FirebaseStorage storage;
    FirebaseFirestore db;
    DaoCuaHang daoCuaHang;
    KhachHang khachHangTimDc;
    CuaHang cuaHangTimDc;
    private int[] imageArray = {R.drawable.banner, R.drawable.bannera, R.drawable.bannerc};
    private int currentIndex = 0;
    private Handler handler;
    private Runnable runnable;
    DaoSanPham daoSanPham;
    DaoKhachHang daoKhachHang;
    KiemTraThongTin kiemTraThongTin;
    List<SanPham> ds_SanPham;
    adapterSanPham adapterSanPhams;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
        Gan_ID(view);
        suKienClick();
        PhanQuyen();
        handler = new Handler();
        // Bắt đầu chuyển ảnh mỗi 3 giây
        startSliderRunnable();
        return view;
    }

    private void startSliderRunnable() {
        runnable = new Runnable() {
            @Override
            public void run() {
                // Hiển thị ảnh tiếp theo
                showNextImage();

                // Lặp lại quá trình sau 3 giây
                handler.postDelayed(this, 3000);
            }
        };

        // Chạy runnable đầu tiên sau 3 giây
        handler.postDelayed(runnable, 3000);
    }

    private void showNextImage() {
        // Kiểm tra giới hạn của mảng ảnh
        if (currentIndex < imageArray.length - 1) {
            currentIndex++;
        } else {
            currentIndex = 0;
        }

        // Gán ảnh mới cho ImageView
        slider.setImageResource(imageArray[currentIndex]);
    }


//    @Override
//    public void onDestroy() {
//        handler.removeCallbacks(runnable);
//        super.onDestroy();
//    }

    private void PhanQuyen() {
        sharedPreferences = getActivity().getSharedPreferences("taiKhoan", Context.MODE_PRIVATE);
        ID = sharedPreferences.getString("IDuser", "");
        role = sharedPreferences.getString("role", "");
        if (role != null && ID != null) {
            if (role.equals("Khách hàng")) {
                shop.setImageResource(R.drawable.pepel);
                daoKhachHang = new DaoKhachHang(getContext());
                daoKhachHang.TimKhachHang(ID, new DaoKhachHang.TraVeKhachHangTimDc() {
                    @Override
                    public void onTimDcKhachHang(KhachHang khachHang) {
                        khachHangTimDc = khachHang;
                        trangThai = khachHangTimDc.getXacThuc();
                        TrangThaiXacThuc();
                    }

                    @Override
                    public void onKoTimKhachHang(String errorMessage) {

                    }
                });
            } else {
                daoCuaHang = new DaoCuaHang(getContext());
                daoCuaHang.TimCuaHang(ID, new DaoCuaHang.TraVeCuaHangTimDc() {
                    @Override
                    public void onTimDcCuaHang(CuaHang cuaHang) {
                        cuaHangTimDc = cuaHang;
                        trangThai = cuaHangTimDc.getXacThuc();
                        TrangThaiXacThuc();
                    }

                    @Override
                    public void onKoTimCuaHang(String errorMessage) {

                    }
                });
            }
        }

        if (trangThai == 2) {
            btnXacThucTK.setVisibility(View.GONE);
        } else {
            btnXacThucTK.setVisibility(View.VISIBLE);
        }

    }

    private void TrangThaiXacThuc() {
        if (trangThai == 2) {
            btnXacThucTK.setVisibility(View.GONE);
        } else {
            btnXacThucTK.setVisibility(View.VISIBLE);
        }
    }

    private void suKienClick() {

        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DiaLongThoatApp();
            }
        });

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnXacThucTK.getVisibility() == View.VISIBLE) {
                    Toast.makeText(getActivity(), "Vui lòng xác thực tài khoản trước", Toast.LENGTH_SHORT).show();
                } else {

                    Intent intent = new Intent(getActivity(), HoSo.class);
                    Bundle bundle = new Bundle();

                    if (role.equals("Khách hàng")) {
                        daoKhachHang.TimKhachHang(ID, new DaoKhachHang.TraVeKhachHangTimDc() {
                            @Override
                            public void onTimDcKhachHang(KhachHang khachHang) {
                                if (khachHang != null) {
                                    bundle.putSerializable("khachHang", khachHang);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onKoTimKhachHang(String errorMessage) {

                            }
                        });

                    } else {
                        daoCuaHang.TimCuaHang(ID, new DaoCuaHang.TraVeCuaHangTimDc() {
                            @Override
                            public void onTimDcCuaHang(CuaHang cuaHang) {
                                if (cuaHang != null) {
                                    bundle.putSerializable("cuaHang", cuaHang);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onKoTimCuaHang(String errorMessage) {

                            }
                        });

                    }


                }
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
        btnXacThucTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                XacThucTaiKhoan();
            }
        });

    }

    private void TimKiem(String s) {
        daoSanPham.layToanBoSanPham(new DaoSanPham.ToanBoDanhSachSanPham() {
            @Override
            public void ThanhCong(List<SanPham> sanPhamList) {
                ds_SanPham.clear();
                for (SanPham x : sanPhamList) {
                    if (x.getTenSanPham().contains(s) || x.getLoaiSanPham().contains(s)) {
                        ds_SanPham.add(x);
                    }
                }
                adapterSanPhams.notifyDataSetChanged();
            }

            @Override
            public void ThatBai(Exception e) {

            }
        });
    }

    private void XacThucTaiKhoan() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialong_xacminh, null);
        EditText edt_ten, edt_namSinh, edt_sdt, edt_DiaChi;
        RadioButton rbkNam, rbNu;

        Button btnXacMinh;
        imgAnh = view.findViewById(R.id.im_anhDaiDien);
        edt_ten = view.findViewById(R.id.edt_Ten);
        edt_namSinh = view.findViewById(R.id.edt_NamSinh);
        edt_sdt = view.findViewById(R.id.edt_sdt);
        edt_DiaChi = view.findViewById(R.id.edt_DiaChiChiTiet);
        rbkNam = view.findViewById(R.id.rbNam);
        rbNu = view.findViewById(R.id.rbNu);
        spinnerTinh = view.findViewById(R.id.spinner_Tinh);
        spinnerHuyen = view.findViewById(R.id.spinner_Huyen);
        spinnerXa = view.findViewById(R.id.spinner_Xa);
        btnXacMinh = view.findViewById(R.id.btnXacThuc);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog dialog = builder.create();
        if (role.equals("Cửa hàng")) {
            rbkNam.setVisibility(View.GONE);
            rbNu.setVisibility(View.GONE);
            edt_namSinh.setVisibility(View.GONE);
        }
        imgAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/anhDaiDien");
                startActivityForResult(intent, PICK_IMAGE_REQUEST);
            }
        });
        DuLieuDiaChi();
        btnXacMinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ten, ns, sdt, gioTinh, diaChiChiTiet;
                ten = edt_ten.getText().toString();
                ns = edt_namSinh.getText().toString();
                sdt = edt_sdt.getText().toString();
                diaChiChiTiet = edt_DiaChi.getText().toString();


                if (role.equals("Cửa hàng")) {
                    if (ten.trim().isEmpty() || sdt.trim().isEmpty() || diaChiChiTiet.trim().isEmpty()) {
                        Toast.makeText(getActivity(), "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                    } else if (!kiemTraThongTin.KtsoNguyen(sdt) || !kiemTraThongTin.isValidPhoneNumber(sdt)) {
                        Toast.makeText(getActivity(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                    } else {
                        daoCuaHang.xacThucThongTin(ID, imageUrl, ten, sdt, Tinh, Huyen, xa, diaChiChiTiet);
                        Toast.makeText(getActivity(), "Xác thực thành công cửa hàng", Toast.LENGTH_SHORT).show();
                        trangThai = 2;
                        TrangThaiXacThuc();
                        dialog.dismiss();
                    }

                } else {

                    if (ten.trim().isEmpty() || ns.trim().isEmpty() || sdt.trim().isEmpty() || diaChiChiTiet.trim().isEmpty()) {
                        Toast.makeText(getActivity(), "Không được để trống thông tin", Toast.LENGTH_SHORT).show();
                    } else if (!kiemTraThongTin.KtsoNguyen(ns)) {
                        Toast.makeText(getActivity(), "Năm sinh không hợp lệ", Toast.LENGTH_SHORT).show();
                    } else if (!kiemTraThongTin.KtsoNguyen(sdt)) {
                        Toast.makeText(getActivity(), "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                    } else if (2023 - Integer.parseInt(ns) < 16) {
                        Toast.makeText(getActivity(), "Bạn phải trên 16 tuổi", Toast.LENGTH_SHORT).show();
                    } else if (!kiemTraThongTin.isValidPhoneNumber(sdt)) {
                        Toast.makeText(getActivity(), "số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
                    } else if (!rbkNam.isChecked() && !rbNu.isChecked()) {
                        Toast.makeText(getActivity(), "vui lòng chọn giới tính", Toast.LENGTH_SHORT).show();
                    } else {
                        if (rbkNam.isChecked()) {
                            gioTinh = "Nam";
                        } else {
                            gioTinh = "Nữ";
                        }
                        daoKhachHang.XacThucKhachHang(ID, imageUrl, ten, ns, sdt, gioTinh, Tinh, Huyen, xa, diaChiChiTiet);
                        Toast.makeText(getActivity(), "Xác thực thành công khách hàng", Toast.LENGTH_SHORT).show();
                        trangThai = 2;
                        TrangThaiXacThuc();
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
        StorageReference imageRef = storageRef.child("anhDaiDien/" + System.currentTimeMillis() + ".jpg");


        try {
            // Đọc hình ảnh từ Uri thành byte array
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri).compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageData = baos.toByteArray();

            // Tải lên hình ảnh lên Firebase Storage
            UploadTask uploadTask = imageRef.putBytes(imageData);
            uploadTask.addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Lấy đường dẫn của hình ảnh trên Firebase Storage
                    imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Đường dẫn của hình ảnh trên Firebase Storage
                        imageUrl = uri.toString();
                        Picasso.get().load(imageUrl).into(imgAnh);
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


    private void DiaLongThoatApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xác nhận thoát");
        builder.setMessage("Bạn có muốn thoát khỏi ứng dụng?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requireActivity().finish();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng dialog và không làm gì cả
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void Gan_ID(View view) {
        kiemTraThongTin = new KiemTraThongTin();
        shop = view.findViewById(R.id.img_shopHome);
        slider = view.findViewById(R.id.img_slider);
        btnXacThucTK = view.findViewById(R.id.btnXacThucTK);
        searchView = view.findViewById(R.id.TimKiemHome);
        ryc_hang = view.findViewById(R.id.ryc_SPbc);
        spinnerLoaiHang = view.findViewById(R.id.spinnerLoaiHang);
        Adapter();
    }

    private void Adapter() {
        ds_SanPham = new ArrayList<>();
        DaoLoaiHang daoLoaiHang = new DaoLoaiHang(getContext());
        daoLoaiHang.Adapter(spinnerLoaiHang,1);

        daoSanPham = new DaoSanPham(getContext());
        daoSanPham.layToanBoSanPham(new DaoSanPham.ToanBoDanhSachSanPham() {
            @Override
            public void ThanhCong(List<SanPham> sanPhamList) {
                ds_SanPham.clear();
                ds_SanPham.addAll(sanPhamList);
                adapterSanPhams = new adapterSanPham(getActivity(), ds_SanPham, ID, role, "Home");
                GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
                ryc_hang.setLayoutManager(layoutManager);
                ryc_hang.setAdapter(adapterSanPhams);

            }

            @Override
            public void ThatBai(Exception e) {

            }
        });

        spinnerLoaiHang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedLoaiHang = adapterView.getItemAtPosition(i).toString();
                daoSanPham.layToanBoSanPham(new DaoSanPham.ToanBoDanhSachSanPham() {
                    @Override
                    public void ThanhCong(List<SanPham> sanPhamList) {
                        ds_SanPham.clear();

                        for (SanPham x : sanPhamList) {
                            if (x.getLoaiSanPham().equals(selectedLoaiHang)) {
                                ds_SanPham.add(x);
                            }
                        }
                        if (selectedLoaiHang.equals("Tất cả")) {
                            ds_SanPham.clear();
                            ds_SanPham.addAll(sanPhamList) ;
                        }
                        adapterSanPhams.notifyDataSetChanged();
                    }

                    @Override
                    public void ThatBai(Exception e) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // Xử lý khi không có mục nào được chọn
            }
        });
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
        adapterTinh = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, layDanhSachTenTinh);
        adapterTinh.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTinh.setAdapter(adapterTinh);

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
            adapterHuyen = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, danhSachTenHuyen);
            adapterHuyen.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerHuyen.setAdapter(adapterHuyen);
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

                adapterXa = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, danhSachTenXa);
                adapterXa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerXa.setAdapter(adapterXa);
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