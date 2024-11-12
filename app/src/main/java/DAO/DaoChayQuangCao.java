package DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.CuaHang;
import DTO.QuangCao;
import adapter.adapterChayQuangCao;

public class DaoChayQuangCao {
    Context context;
    FirebaseFirestore db;

    public DaoChayQuangCao(Context context) {
        this.context = context;
        this.db = FirebaseFirestore.getInstance();
    }

    public interface DanhSachChayQuangCao {
        void onTimDsChayQuangCao(List<QuangCao> quangCaoList);
        void onKoTimDsChayQuangCao(String errorMessage);
    }
    public void themQuanCao(QuangCao chayQuangCao) {

        DocumentReference quangCaoDocument = db.collection("quanCao").document(chayQuangCao.getIDcuaHang());
        Map<String, Object> data = new HashMap<>();
        data.put("IDQC", chayQuangCao.getIDQC());
        data.put("IDcuaHang", chayQuangCao.getIDcuaHang());
        data.put("TenCuaHang", chayQuangCao.getTenCuaHang());
        data.put("IDsanPham", chayQuangCao.getIDsanPham());
        data.put("TenSanPham", chayQuangCao.getTenSanPham());
        data.put("TrangThai", chayQuangCao.getTrangThai());
        data.put("TimeKhoiChay", chayQuangCao.getTimeKhoiChay());
        data.put("TimeDuyet", chayQuangCao.getTimeDuyet());
        data.put("CapDo", chayQuangCao.getCapDo());
        data.put("TimeConLai", chayQuangCao.getTimeConLai());

        quangCaoDocument.set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(context, "Gửi khởi chạy thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Gửi khởi chạy thất bại ", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }

    public void layDanhSachChayQuangCaoTheoCuaHang(String idCuaHang,DanhSachChayQuangCao danhSachChayQuangCao ) {
        CollectionReference quangCaoCollection = db.collection("quanCao");

        // Thực hiện truy vấn để lấy ra danh sách chạy quảng cáo theo IDcuaHang
        Query query = quangCaoCollection.whereEqualTo("IDcuaHang", idCuaHang);

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<QuangCao> danhSachQuangCao = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        QuangCao quangCao = document.toObject(QuangCao.class);
                        danhSachQuangCao.add(quangCao);
                    }

                    danhSachChayQuangCao.onTimDsChayQuangCao(danhSachQuangCao);
                });

    }

    public void layDanhSachToanBoQuangCao(DanhSachChayQuangCao  danhSachChayQuangCao) {
        CollectionReference quangCaoCollection = db.collection("quanCao");

        quangCaoCollection.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<QuangCao> danhSachQuangCao = new ArrayList<>();

                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        QuangCao quangCao = document.toObject(QuangCao.class);
                        danhSachQuangCao.add(quangCao);
                    }

                    danhSachChayQuangCao.onTimDsChayQuangCao(danhSachQuangCao);
                });
    }


    public void capNhatQuangCao(QuangCao capNhatQuangCao) {
        DocumentReference quangCaoRef = db.collection("quanCao").document(capNhatQuangCao.getIDcuaHang());
        Map<String, Object> data = new HashMap<>();
        data.put("IDcuaHang", capNhatQuangCao.getIDcuaHang());
        data.put("TenCuaHang", capNhatQuangCao.getTenCuaHang());
        data.put("IDsanPham", capNhatQuangCao.getIDsanPham());
        data.put("TenSanPham", capNhatQuangCao.getTenSanPham());
        data.put("TrangThai", capNhatQuangCao.getTrangThai());
        data.put("TimeKhoiChay", capNhatQuangCao.getTimeKhoiChay());
        data.put("TimeDuyet", capNhatQuangCao.getTimeDuyet());
        data.put("CapDo", capNhatQuangCao.getCapDo());
        data.put("TimeConLai", capNhatQuangCao.getTimeConLai());

        quangCaoRef.update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                        } else {

                        }
                    }
                });
    }

    public void xoaQuangCao(String IDcuaHang, String IDsanPham) {
        // Xác định CollectionReference của quảng cáo
        CollectionReference quangCaoCollection = db.collection("quanCao");

        // Tạo một truy vấn để lấy tài liệu cần xóa
        Query query = quangCaoCollection
                .whereEqualTo("IDcuaHang", IDcuaHang)
                .whereEqualTo("IDsanPham", IDsanPham);

        // Thực hiện truy vấn để lấy danh sách tài liệu
        query.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Lấy danh sách tài liệu phù hợp
                            List<DocumentSnapshot> documents = task.getResult().getDocuments();

                            // Kiểm tra xem có tài liệu nào cần xóa không
                            if (!documents.isEmpty()) {
                                // Lặp qua danh sách tài liệu và xóa chúng
                                for (DocumentSnapshot document : documents) {
                                    document.getReference().delete();
                                }

                                // Đã xóa thành công
                                Toast.makeText(context, "Xóa quảng cáo thành công", Toast.LENGTH_SHORT).show();
                            } else {
                                // Không tìm thấy tài liệu cần xóa
                                Toast.makeText(context, "Không tìm thấy quảng cáo cần xóa", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Xử lý lỗi khi truy vấn không thành công
                            Toast.makeText(context, "Xóa quảng cáo thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    public void CapNhapLangNgheDuLieuCuaHang(String idCuaHang, adapterChayQuangCao adapterChayQuangCaos, List<QuangCao> ds) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference quangCaoCollection = db.collection("quanCao");

        quangCaoCollection.whereEqualTo("IDcuaHang", idCuaHang)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            // Xử lý lỗi nếu có
                            return;
                        }
                        QuangCao quangCao;
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            switch (documentChange.getType()) {
                                case ADDED:
                                    quangCao = documentChange.getDocument().toObject(QuangCao.class);
                                    ds.add(quangCao);
                                    adapterChayQuangCaos.notifyItemInserted(ds.size() - 1);
                                    break;
                                case MODIFIED:
                                    quangCao = documentChange.getDocument().toObject(QuangCao.class);
                                    for (int i = 0; i < ds.size(); i++) {
                                        if (ds.get(i).getIDQC().equals(quangCao.getIDQC())) {
                                            ds.set(i, quangCao);
                                            adapterChayQuangCaos.notifyItemChanged(i);
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    quangCao = documentChange.getDocument().toObject(QuangCao.class);
                                    for (int i = 0; i < ds.size(); i++) {
                                        if (ds.get(i).getIDQC().equals(quangCao.getIDQC())) {
                                            ds.set(i, quangCao);
                                            adapterChayQuangCaos.notifyItemRemoved(i);
                                            break;
                                        }
                                    }
                                    break;
                            }
                        }
                    }
                });
    }


    public void CapNhapLangNgheDuLieu(adapterChayQuangCao adapterChayQuangCaos, List<QuangCao> ds) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference cuaHangsCollection = db.collection("quanCao");
        cuaHangsCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    // Xử lý lỗi nếu có
                    return;
                }
                QuangCao quangCao;
                for (DocumentChange documentChange : value.getDocumentChanges()) {
                    switch (documentChange.getType()) {
                        case ADDED:
                            quangCao = documentChange.getDocument().toObject(QuangCao.class);
                            ds.add(quangCao);
                            adapterChayQuangCaos.notifyItemInserted(ds.size() - 1);
                            break;
                        case MODIFIED:
                            quangCao = documentChange.getDocument().toObject(QuangCao.class);
                            for (int i = 0; i < ds.size(); i++) {
                                if (ds.get(i).getIDQC().equals(quangCao.getIDQC())) {
                                    ds.set(i, quangCao);
                                    adapterChayQuangCaos.notifyItemChanged(i);
                                    break;
                                }
                            }
                            break;
                        case REMOVED:
                            int viTriThayDoi=-1;
                            quangCao = documentChange.getDocument().toObject(QuangCao.class);
                            for (int i = 0; i < ds.size(); i++) {
                                if (ds.get(i).getIDQC().equals(quangCao.getIDQC())) {
                                   viTriThayDoi=i;
                                    break;
                                }
                            }
                            if(viTriThayDoi!=1){
                                ds.remove(viTriThayDoi);
                                adapterChayQuangCaos.notifyItemRemoved(viTriThayDoi);

                            }

                            break;
                    }

                }


            }
        });
    }

}
