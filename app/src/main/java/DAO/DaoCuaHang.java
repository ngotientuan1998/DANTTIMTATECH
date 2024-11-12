package DAO;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import DTO.CuaHang;
import DTO.KhachHang;
import DTO.SanPham;
import adapter.adapterCuaHang;
import adapter.adapterKhachHang;

public class DaoCuaHang {
    FirebaseFirestore db;
    Context context;

    public DaoCuaHang(Context context) {
        db=FirebaseFirestore.getInstance();
        this.context =context;
    }
    public interface TraVeCuaHangTimDc {
        void onTimDcCuaHang(CuaHang cuaHang);
        void onKoTimCuaHang(String errorMessage);
    }

    public interface checkTrangThai {
        void kiemTraTaiKhoan(boolean isActive);
    }

    public void luuThongTinDangKi(String ID,String email,String ngayThangNam,String role){
        Map<String, Object> MapCuaHang = new HashMap<>();
        MapCuaHang.put("cuaHangID", ID);
        MapCuaHang.put("hinhAnh", "");
        MapCuaHang.put("tenCuaHang", "");
        MapCuaHang.put("email", email);
        MapCuaHang.put("sdt", 0);
        MapCuaHang.put("NTNT", ngayThangNam);
        MapCuaHang.put("theoDoi", 0);
        MapCuaHang.put("Tinh", "");
        MapCuaHang.put("Huyen", "");
        MapCuaHang.put("Xa", "");
        MapCuaHang.put("diaChiChiTiet", "");
        MapCuaHang.put("role", role);
        MapCuaHang.put("trangThai", "hoatDong");
        MapCuaHang.put("xacThuc", 1);
        db.collection("cuaHang")
                .document(ID)
                .set(MapCuaHang)
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



    public boolean xacThucThongTin(String ID,String img,String ten,String sdt,String Tinh,String huyen,String xa,String diaChi) {
        Map<String, Object> storeUpdates = new HashMap<>();
        List<SanPham> list=new ArrayList<>();
        storeUpdates.put("hinhAnh", img);
        storeUpdates.put("tenCuaHang", ten);
        storeUpdates.put("sdt", Integer.parseInt(sdt));
        storeUpdates.put("theoDoi", 0);
        storeUpdates.put("Tinh", Tinh);
        storeUpdates.put("Huyen", huyen);
        storeUpdates.put("Xa", xa);
        storeUpdates.put("danhSachSanPham",list);
        storeUpdates.put("diaChiChiTiet", diaChi);
        storeUpdates.put("xacThuc",2);

        try {
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentReference storeRef = db.collection("cuaHang").document(ID);
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



    public boolean suaCuaHang(String ID, String hinhAnh, String ten, String email, int sdt,String Tinh,String Huyen,String xa ,String diaChi) {
        Map<String, Object> storeUpdates = new HashMap<>();
        storeUpdates.put("hinhAnh", hinhAnh);
        storeUpdates.put("tenCuaHang", ten);
        storeUpdates.put("email", email);
        storeUpdates.put("sdt", sdt);
        storeUpdates.put("Tinh", Tinh);
        storeUpdates.put("Huyen", Huyen);
        storeUpdates.put("Xa", xa);
        storeUpdates.put("diaChi", diaChi);

        try {
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentReference storeRef = db.collection("cuaHang").document(ID);
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





    public void TimCuaHang(String ID, TraVeCuaHangTimDc  traVeCuaHangTimDc ) {
        DocumentReference khachHangDocRef = db.collection("cuaHang").document(ID);

        khachHangDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        CuaHang cuaHang1 = document.toObject(CuaHang.class);
                        if (cuaHang1 != null && ID.equals(cuaHang1.getCuaHangID())) {
                            traVeCuaHangTimDc.onTimDcCuaHang(cuaHang1);
                            Log.d("IDcuaHang tìmd đc","ID:"+cuaHang1.getCuaHangID());
                        } else {
                            traVeCuaHangTimDc.onKoTimCuaHang("Không tìm thấy khách hàng");
                        }
                    } else {
                        traVeCuaHangTimDc.onKoTimCuaHang("Không tìm thấy khách hàng");                    }
                } else {
                    traVeCuaHangTimDc.onKoTimCuaHang("Không tìm thấy khách hàng");                }
            }
        });
    }



    public void checkAccountStatus(String userId, checkTrangThai listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference userRef = db.collection("cuaHang").document(userId);

        userRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String status = document.getString("trangThai");
                    boolean isActive = "hoatDong".equals(status);
                    listener.kiemTraTaiKhoan(isActive);
                } else {
                    // Nếu không có tài khoản, trả về false
                    listener.kiemTraTaiKhoan(false);
                }
            } else {
                // Xử lý lỗi nếu có và trả về false
                listener.kiemTraTaiKhoan(false);
            }
        });
    }



    public boolean suaTrangThai(String ID,String trangThai) {
        Map<String, Object> storeUpdates = new HashMap<>();
        storeUpdates.put("trangThai", trangThai);
        try {
            db.runTransaction(new Transaction.Function<Void>() {
                @Override
                public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                    DocumentReference storeRef = db.collection("cuaHang").document(ID);
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

    public void langNgheSuKienThayDoiCuaHang(adapterCuaHang adapterCuaHangs, List<CuaHang> list) {
        CollectionReference cuaHangCollection = db.collection("cuaHang");

        cuaHangCollection.addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                            CuaHang cuaHangAdded = documentChange.getDocument().toObject(CuaHang.class);
                            // Gọi phương thức của giao diện lắng nghe
                            list.add(cuaHangAdded);
                            adapterCuaHangs.notifyItemInserted(list.size() - 1);
                            break;

                        case MODIFIED:
                            // Xử lý khi có khách hàng bị sửa đổi
                            CuaHang cuaHangModified = documentChange.getDocument().toObject(CuaHang.class);
                            // Tìm vị trí của khách hàng trong danh sách
                            int modifiedIndex = -1;
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getCuaHangID().equals(cuaHangModified.getCuaHangID())) {
                                    modifiedIndex = i;
                                    break;
                                }
                            }
                            if (modifiedIndex != -1) {
                                list.set(modifiedIndex, cuaHangModified);
                                adapterCuaHangs.notifyItemChanged(modifiedIndex);
                            }
                            break;

                        case REMOVED:
                            // Xử lý khi có khách hàng bị xóa
                            String cuaHangIdRemoved = documentChange.getDocument().getId();
                            // Tìm vị trí của khách hàng trong danh sách
                            int removedIndex = -1;
                            for (int i = 0; i < list.size(); i++) {
                                if (list.get(i).getCuaHangID().equals(cuaHangIdRemoved)) {
                                    removedIndex = i;
                                    break;
                                }
                            }
                            if (removedIndex != -1) {
                                list.remove(removedIndex);
                                adapterCuaHangs.notifyItemRemoved(removedIndex);
                            }
                            break;
                    }
                }
            }
        });
    }


}



