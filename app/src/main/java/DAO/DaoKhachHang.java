package DAO;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import org.checkerframework.checker.units.qual.K;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.CuaHang;
import DTO.DonHang;
import DTO.KhachHang;
import adapter.adapterKhachHang;

public class DaoKhachHang {

    FirebaseFirestore db;
    Context context;

    public DaoKhachHang(Context context) {
        this.context = context;
        db = FirebaseFirestore.getInstance();
    }

    public void luuThongTinDangKi(String ID, String email, String NTNT, String role) {
        Map<String, Object> MapKhachHang = new HashMap<>();
        MapKhachHang.put("hinhAnh", "");
        MapKhachHang.put("khachHangID", ID);
        MapKhachHang.put("hoTen", "");
        MapKhachHang.put("email", email);
        MapKhachHang.put("sdt", 0);
        MapKhachHang.put("NTNS", 0);
        MapKhachHang.put("NTNT", NTNT);
        MapKhachHang.put("gioiTinh", "");
        MapKhachHang.put("Tinh", "");
        MapKhachHang.put("Huyen", "");
        MapKhachHang.put("Xa", "");
        MapKhachHang.put("diaChiChiTiet", "");
        MapKhachHang.put("role", role);
        MapKhachHang.put("trangThai", "hoatDong");
        MapKhachHang.put("xacThuc", 1);
        db.collection("khachHang")
                .document(ID)
                .set(MapKhachHang)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }

    public boolean suaKhachHang(String ID, String hinhAnh, String hoTen, String email, int sdt, int ns, String gioiTinh, String Tinh, String huyen, String xa, String diaChi) {
        Map<String, Object> customerUpdates = new HashMap<>();
        customerUpdates.put("hinhAnh", hinhAnh);
        customerUpdates.put("hoTen", hoTen);
        customerUpdates.put("email", email);
        customerUpdates.put("sdt", sdt);
        customerUpdates.put("NTNS", ns);
        customerUpdates.put("gioiTinh", gioiTinh);
        customerUpdates.put("Tinh", Tinh);
        customerUpdates.put("Huyen", huyen);
        customerUpdates.put("Xa", xa);
        customerUpdates.put("diaChiChiTiet", diaChi);

        try {
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentReference customerRef = db.collection("khachHang").document(ID);
                    DocumentSnapshot snapshot = transaction.get(customerRef);

                    // Kiểm tra xem tài liệu có tồn tại không
                    if (snapshot.exists()) {
                        // Thực hiện cập nhật trong giao dịch
                        transaction.update(customerRef, customerUpdates);
                    } else {
                        // Xử lý tình huống khi tài liệu không tồn tại
                        Log.w("TAG", "Customer with ID " + ID + " does not exist");
                    }

                    return null;
                }
            });

            return true;
        } catch (Exception e) {
            Log.w("TAG", "Failed to update customer information", e);
            return false;
        }
    }

    public boolean XacThucKhachHang(String ID, String img, String ten, String ns, String sdt, String gioiTinh, String Tinh, String Huyen, String xa, String diaChi) {
        Map<String, Object> customerUpdates = new HashMap<>();
        customerUpdates.put("hinhAnh", img);
        customerUpdates.put("hoTen", ten);
        customerUpdates.put("sdt", Integer.parseInt(sdt));
        customerUpdates.put("NTNS", Integer.parseInt(ns));
        customerUpdates.put("gioiTinh", gioiTinh);
        customerUpdates.put("Tinh", Tinh);
        customerUpdates.put("Huyen", Huyen);
        customerUpdates.put("Xa", xa);
        customerUpdates.put("diaChiChiTiet", diaChi);
        customerUpdates.put("xacThuc", 2);

        try {
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentReference customerRef = db.collection("khachHang").document(ID);
                    DocumentSnapshot snapshot = transaction.get(customerRef);

                    // Kiểm tra xem tài liệu có tồn tại không
                    if (snapshot.exists()) {
                        // Thực hiện cập nhật trong giao dịch
                        transaction.update(customerRef, customerUpdates);
                    } else {
                        // Xử lý tình huống khi tài liệu không tồn tại
                        Log.w("TAG", "Customer with ID " + ID + " does not exist");
                    }

                    return null;
                }
            });

            return true;
        } catch (Exception e) {
            Log.w("TAG", "Failed to update customer information", e);
            return false;
        }
    }


    // Trong HomeFragment


    public interface TraVeKhachHangTimDc {
        void onTimDcKhachHang(KhachHang khachHang);

        void onKoTimKhachHang(String errorMessage);
    }

    public void TimKhachHang(String ID, TraVeKhachHangTimDc traVeKhachHangTimDc) {
        DocumentReference khachHangDocRef = db.collection("khachHang").document(ID);

        khachHangDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        KhachHang khachHang = document.toObject(KhachHang.class);
                        if (khachHang != null && ID.equals(khachHang.getKhachHangID())) {
                            traVeKhachHangTimDc.onTimDcKhachHang(khachHang);
                        } else {
                            traVeKhachHangTimDc.onKoTimKhachHang("Không tìm thấy khách hàng");
                        }
                    } else {
                        traVeKhachHangTimDc.onKoTimKhachHang("Không tìm thấy khách hàng");
                    }
                } else {
                    traVeKhachHangTimDc.onKoTimKhachHang("Không tìm thấy khách hàng");
                }
            }
        });
    }

    public interface checkTrangThai {
        void kiemTraTaiKhoan(boolean isActive);
    }

    public void checkAccountStatus(String userId, checkTrangThai checkTrangThai) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("khachHang").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String status = document.getString("trangThai");
                    boolean isActive = "hoatDong".equals(status);
                    checkTrangThai.kiemTraTaiKhoan(isActive);
                } else {
                    // Nếu không có tài khoản, trả về false
                    checkTrangThai.kiemTraTaiKhoan(false);
                }
            } else {
                // Xử lý lỗi nếu có và trả về false
                checkTrangThai.kiemTraTaiKhoan(false);
            }
        });
    }


    public boolean suaTrangThai(String ID, String trangThai) {
        Map<String, Object> storeUpdates = new HashMap<>();
        storeUpdates.put("trangThai", trangThai);
        try {
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentReference storeRef = db.collection("khachHang").document(ID);
                    DocumentSnapshot snapshot = transaction.get(storeRef);

                    // Kiểm tra xem tài liệu có tồn tại không
                    if (snapshot.exists()) {
                        // Thực hiện cập nhật trong giao dịch
                        transaction.update(storeRef, storeUpdates);
                    } else {
                        // Xử lý tình huống khi tài liệu không tồn tại
                        Log.w("TAG", "Store with ID " + ID + " does not exist");
                    }

                    return null;
                }
            });

            return true;
        } catch (Exception e) {
            Log.w("TAG", "Failed to update store information", e);
            return false;
        }
    }

    public void langNgheSuKienThayDoiKhachHang(adapterKhachHang adapterKhachHangs, List<KhachHang> list) {
        CollectionReference khachHangCollection = db.collection("khachHang");

        khachHangCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Xử lý lỗi nếu có
                    return;
                }

                for (DocumentChange documentChange : snapshots.getDocumentChanges()) {
                    switch (documentChange.getType()) {
                        case ADDED:
                            // Xử lý khi có khách hàng được thêm mới
                            KhachHang khachHangAdded = documentChange.getDocument().toObject(KhachHang.class);
                            // Gọi phương thức của giao diện lắng nghe
                            list.add(khachHangAdded);
                            adapterKhachHangs.notifyItemInserted(list.size() - 1);
                            break;

                        case MODIFIED:
                            // Xử lý khi có khách hàng bị sửa đổi
                            KhachHang khachHangModified = documentChange.getDocument().toObject(KhachHang.class);
                            // Tìm vị trí của khách hàng trong danh sách
                            int modifiedIndex = -1;
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getKhachHangID().equals(khachHangModified.getKhachHangID())) {
                                    modifiedIndex = i;
                                    break;
                                }
                            }
                            if (modifiedIndex != -1) {
                                list.set(modifiedIndex, khachHangModified);
                                adapterKhachHangs.notifyItemChanged(modifiedIndex);
                            }
                            break;

                        case REMOVED:
                            // Xử lý khi có khách hàng bị xóa
                            String khachHangIdRemoved = documentChange.getDocument().getId();
                            // Tìm vị trí của khách hàng trong danh sách
                            int removedIndex = -1;
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getKhachHangID().equals(khachHangIdRemoved)) {
                                    removedIndex = i;
                                    break;
                                }
                            }
                            if (removedIndex != -1) {
                                list.remove(removedIndex);
                                adapterKhachHangs.notifyItemRemoved(removedIndex);
                            }
                            break;
                    }
                }
            }
        });
    }


}


