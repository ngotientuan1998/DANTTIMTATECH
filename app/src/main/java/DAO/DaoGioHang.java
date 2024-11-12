package DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.QuangCao;
import DTO.gioHang;

public class DaoGioHang {
    Context context;
    FirebaseFirestore db;
    public DaoGioHang(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }
    public void themVaoGioHang(gioHang gioHangs) {

        DocumentReference quangCaoDocument = db.collection("gioHang").document(gioHangs.getIDgioHang());
        Map<String, Object> data = new HashMap<>();
        data.put("IDkhachHang", gioHangs.getIDkhachHang());
        data.put("IDgioHang", gioHangs.getIDgioHang());
        data.put("TrangThaiChon", gioHangs.getTrangThaiChon());
        data.put("IDsanPham", gioHangs.getIDsanPham());
        data.put("IDcuaHang", gioHangs.getIDcuaHang());
        data.put("TenCuaHang", gioHangs.getTenCuaHang());
        data.put("HinhAnhSanPham", gioHangs.getHinhAnhSanPham());
        data.put("TenSanPham", gioHangs.getTenSanPham());
        data.put("giaSp", gioHangs.getGiaSp());
        data.put("giaGiamSp", gioHangs.getGiaGiamSp());
        data.put("soLuongMua", gioHangs.getSoLuongMua());
        data.put("DanhTinh", gioHangs.getDanhTinh());
//        DanhTinh

        quangCaoDocument.set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Đã thêm vào giỏ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void capNhatGioHang(gioHang gioHangs) {

        DocumentReference gioHangDocument = db.collection("gioHang").document(gioHangs.getIDgioHang());
        Map<String, Object> data = new HashMap<>();
        data.put("IDkhachHang", gioHangs.getIDkhachHang());
        data.put("IDgioHang", gioHangs.getIDgioHang());
        data.put("TrangThaiChon", gioHangs.getTrangThaiChon());
        data.put("IDsanPham", gioHangs.getIDsanPham());
        data.put("IDcuaHang", gioHangs.getIDcuaHang());
        data.put("TenCuaHang", gioHangs.getTenCuaHang());
        data.put("HinhAnhSanPham", gioHangs.getHinhAnhSanPham());
        data.put("TenSanPham", gioHangs.getTenSanPham());
        data.put("giaSp", gioHangs.getGiaSp());
        data.put("giaGiamSp", gioHangs.getGiaGiamSp());
        data.put("soLuongMua", gioHangs.getSoLuongMua());
        data.put("DanhTinh", gioHangs.getDanhTinh());

        gioHangDocument.update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        } else {

                        }
                    }
                });
    }

    public interface CapNhapSoLuongMuaGioHang {
        void CapNhapThanhCong(String tc);
        void CapNhapThatBai(String tb);
    }
    public void capNhatSlMuaGioHang(gioHang gioHangs, CapNhapSoLuongMuaGioHang  capNhapSoLuongMuaGioHang) {

        DocumentReference gioHangDocument = db.collection("gioHang").document(gioHangs.getIDgioHang());
        Map<String, Object> data = new HashMap<>();
        data.put("soLuongMua", gioHangs.getSoLuongMua());
        data.put("TrangThaiChon", gioHangs.getTrangThaiChon());

        gioHangDocument.update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                           capNhapSoLuongMuaGioHang.CapNhapThanhCong("Thành Công");
                            Log.d("Thành Công","Thành Công");
                        } else {
                           capNhapSoLuongMuaGioHang.CapNhapThatBai("Thất bại");
                            Log.d("Thất bại","Thất bại");
                        }
                    }
                });
    }

    public void xoaGioHang(String idGioHang) {
        DocumentReference gioHangRef = db.collection("gioHang").document(idGioHang);

        gioHangRef.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                        } else {

                        }
                    }
                });
    }

    public void layGioHangTheoIDKhachHang(String idKhachHang,  OnGioHangLoadListener listener) {
        CollectionReference gioHangRef = db.collection("gioHang");

        // Thực hiện truy vấn để lấy gioHang dựa vào IDkhachHang
        Query query = gioHangRef.whereEqualTo("IDkhachHang", idKhachHang);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<gioHang> gioHangList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        gioHang gioHangItem = document.toObject(gioHang.class);
                        gioHangList.add(gioHangItem);
                    }

                    // Gọi phương thức callback khi danh sách gioHang được tải thành công
                    listener.onGioHangLoadSuccess(gioHangList);
                } else {
                    // Gọi phương thức callback khi có lỗi xảy ra
                    listener.onGioHangLoadFailure(task.getException());
                }
            }
        });
    }

    public void layGioHangTheoIDKhachHangVaDanhTinhThemVaoGio(String idKhachHang,  OnGioHangLoadListener listener) {
        CollectionReference gioHangRef = db.collection("gioHang");

        // Thực hiện truy vấn để lấy gioHang dựa vào IDkhachHang
        Query query = gioHangRef.whereEqualTo("IDkhachHang", idKhachHang)
                .whereEqualTo("DanhTinh","ThemVaoGio");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<gioHang> gioHangList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        gioHang gioHangItem = document.toObject(gioHang.class);
                        gioHangList.add(gioHangItem);
                    }

                    // Gọi phương thức callback khi danh sách gioHang được tải thành công
                    listener.onGioHangLoadSuccess(gioHangList);
                } else {
                    // Gọi phương thức callback khi có lỗi xảy ra
                    listener.onGioHangLoadFailure(task.getException());
                }
            }
        });
    }

    public void layGioHangTheoIDKhachHangVaDanhTinhMuaNgay(String idKhachHang,  OnGioHangLoadListener listener) {
        CollectionReference gioHangRef = db.collection("gioHang");

        // Thực hiện truy vấn để lấy gioHang dựa vào IDkhachHang
        Query query = gioHangRef.whereEqualTo("IDkhachHang", idKhachHang)
                .whereEqualTo("DanhTinh","MuaNgay");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<gioHang> gioHangList = new ArrayList<>();

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        gioHang gioHangItem = document.toObject(gioHang.class);
                        gioHangList.add(gioHangItem);
                    }

                    // Gọi phương thức callback khi danh sách gioHang được tải thành công
                    listener.onGioHangLoadSuccess(gioHangList);
                } else {
                    // Gọi phương thức callback khi có lỗi xảy ra
                    listener.onGioHangLoadFailure(task.getException());
                }
            }
        });
    }

    public void layGioHangTheoIDvaTrangThai(String idKhachHang, TraDanhSachGioHangTheoIDKHvaTrangThai traDanhSachGioHangTheoIDKHvaTrangThai) {
        CollectionReference gioHangCollection = db.collection("gioHang");

        Query query = gioHangCollection
                .whereEqualTo("IDkhachHang", idKhachHang)
                .whereEqualTo("TrangThaiChon","dc chon");

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<gioHang> gioHangList = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        gioHang gioHangItem = document.toObject(gioHang.class);
                        gioHangList.add(gioHangItem);
                    }
                    traDanhSachGioHangTheoIDKHvaTrangThai.onSuccess(gioHangList);
                } else {
                    traDanhSachGioHangTheoIDKHvaTrangThai.onError(task.getException());
                }
            }
        });
    }
    public interface TraDanhSachGioHangTheoIDKHvaTrangThai {
        void onSuccess(List<gioHang> gioHangList);
        void onError(Exception e);
    }
    // Định nghĩa một interface để làm callback khi danh sách gioHang được tải
    public interface OnGioHangLoadListener {
        void onGioHangLoadSuccess(List<gioHang> gioHangList);
        void onGioHangLoadFailure(Exception e);
    }

}
