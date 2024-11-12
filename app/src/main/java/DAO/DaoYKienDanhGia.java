package DAO;

import android.content.Context;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import DTO.DanhGia;

public class DaoYKienDanhGia {
    Context context;
    FirebaseFirestore db;

    public DaoYKienDanhGia(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
    }
    public void themDanhGia(DanhGia danhGia) {
        CollectionReference danhGiaCollection=db.collection("DanhGia");
        Map<String, Object> danhGiaData = new HashMap<>();
        danhGiaData.put("ID", danhGia.getID());
        danhGiaData.put("IDkhach", danhGia.getIDkhach());
        danhGiaData.put("IDSanPham", danhGia.getIDSanPham());
        danhGiaData.put("imgAnhKhachHang", danhGia.getImgAnhKhachHang());
        danhGiaData.put("TenKhach", danhGia.getTenKhach());
        danhGiaData.put("YkienDongGop", danhGia.getYkienDongGop());
        danhGiaData.put("saoDanhGia", danhGia.getSaoDanhGia());
        danhGiaData.put("hinhAnhSanPhamDaMua", danhGia.getHinhAnhSanPhamDaMua());

        // Thêm đối tượng vào Firestore
        danhGiaCollection.add(danhGiaData)
                .addOnSuccessListener(documentReference -> {
                    // Xử lý thành công
                })
                .addOnFailureListener(e -> {
                    // Xử lý khi thêm không thành công
                });
    }
    }

