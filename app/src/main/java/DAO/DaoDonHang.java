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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.CuaHang;
import DTO.DonHang;
import DTO.gioHang;
import adapter.adapterDonHang;

public class DaoDonHang {
    FirebaseFirestore db;
    Context context;

    public DaoDonHang(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public void themDonHang(DonHang donHang) {
        DocumentReference donHangDocument = db.collection("donHang").document(donHang.getIDdonHang());

        Map<String, Object> data = new HashMap<>();
        data.put("IDdonHang", donHang.getIDdonHang());
        data.put("IDkhachHang", donHang.getIDkhachHang());
        data.put("IDcuaHang", donHang.getIDcuaHang());
        data.put("ngayDat", donHang.getNgayDat());
        data.put("ngayDuyet", donHang.getNgayDuyet());
        data.put("ngayGiao", donHang.getNgayGiao());
        data.put("ngayNhan", donHang.getNgayNhan());
        data.put("TrangThaiDon", donHang.getTrangThaiDon());

        data.put("soLuongSanPham", donHang.getSoLuongSanPham());
        data.put("phiVanChuyen", donHang.getPhiVanChuyen());
        data.put("gia", donHang.getGia());

        // Chuyển đổi danh sách sản phẩm thành List<Map<String, Object>> để lưu vào Firestore
        List<Map<String, Object>> dsSanPhamMapList = new ArrayList<>();
        for (gioHang x : donHang.getDanhSachSanPhamThuocDon()) {
            Map<String, Object> sanPhamData = new HashMap<>();
            sanPhamData.put("IDgioHang()", x.getIDgioHang());
            sanPhamData.put("IDkhachHang", x.getIDkhachHang());
            sanPhamData.put("TrangThaiChon", x.getTrangThaiChon());
            sanPhamData.put("IDsanPham", x.getIDsanPham());
            sanPhamData.put("IDcuaHang", x.getIDcuaHang());
            sanPhamData.put("TenCuaHang", x.getTenCuaHang());
            sanPhamData.put("HinhAnhSanPham", x.getHinhAnhSanPham());
            sanPhamData.put("TenSanPham", x.getTenSanPham());
            sanPhamData.put("IDgioHang", x.getIDgioHang());
            sanPhamData.put("DanhTinh", x.getDanhTinh());
            sanPhamData.put("giaSp", x.getGiaSp());
            sanPhamData.put("giaGiamSp", x.getGiaGiamSp());
            sanPhamData.put("soLuongMua", x.getSoLuongMua());

            dsSanPhamMapList.add(sanPhamData);
        }
        data.put("DanhSachSanPhamThuocDon", dsSanPhamMapList);

        donHangDocument.set(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Xử lý khi thành công
                        } else {
                            // Xử lý khi có lỗi
                            Exception e = task.getException();
                            if (e != null) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void capNhatTrangThaiDonDonHang(DonHang donHang) {
        DocumentReference donHangDocument = db.collection("donHang").document(donHang.getIDdonHang());

        Map<String, Object> data = new HashMap<>();
        data.put("TrangThaiDon",donHang.getTrangThaiDon());
        data.put("ngayDuyet", donHang.getNgayDuyet());
        data.put("ngayGiao", donHang.getNgayGiao());
        data.put("ngayNhan", donHang.getNgayNhan());
        donHangDocument.update(data)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Xử lý khi thành công
                        } else {
                            // Xử lý khi có lỗi
                            Exception e = task.getException();
                            if (e != null) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }



    public void langNgheDonHangTheoIDKHvaTrangThai(String idKhachHang,String trangThai,List<DonHang> danhSachDonHang, adapterDonHang adapterDonHangs) {
        CollectionReference donHangCollection = db.collection("donHang");
        Log.d("được gọi", "được gọi");
        donHangCollection.whereEqualTo("IDkhachHang", idKhachHang)
                .whereEqualTo("TrangThaiDon", trangThai)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
                            return;
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            switch (documentChange.getType()) {
                                case ADDED:
                                    // Xử lý khi có dữ liệu mới được thêm vào
                                    DonHang donHangAdded = documentChange.getDocument().toObject(DonHang.class);
                                    danhSachDonHang.add(donHangAdded);
                                    adapterDonHangs.notifyItemInserted(danhSachDonHang.size() - 1);
                                    Log.d("được thêm", "được thêm");
                                    break;

                                case MODIFIED:
                                    // Xử lý khi có dữ liệu được sửa đổi
                                    DonHang donHangModified = documentChange.getDocument().toObject(DonHang.class);
                                    // Tìm và cập nhật đối tượng trong danh sách
                                    for (int i = 0; i < danhSachDonHang.size(); i++) {
                                        if (danhSachDonHang.get(i).getIDdonHang().equals(donHangModified.getIDdonHang())) {
                                            danhSachDonHang.set(i, donHangModified);
                                            adapterDonHangs.notifyItemChanged(i);
                                            break;
                                        }
                                    }
                                    Log.d("được sua", "được sua");
                                    Log.d("Sửa dữ liệu được gọi","Sửa dữ liệu được gọi");
                                    break;
                                case REMOVED:
                                    Log.d("xóa", "xóa được gọi ");
                                    // Xử lý khi có dữ liệu bị xóa
                                    DonHang donHang = documentChange.getDocument().toObject(DonHang.class);
                                    int index = -1;
                                    for (int i = 0; i < danhSachDonHang.size(); i++) {
                                        if (danhSachDonHang.get(i).getIDdonHang().equals(donHang.getIDdonHang())) {
                                            index = i;
                                            break;
                                        }
                                    }

                                    if (index != -1) {
                                        danhSachDonHang.remove(index);
                                        adapterDonHangs.notifyItemRemoved(index);
                                    }

                                    Log.d("Bị xóa", "Bị xóa");
                                    break;

                            }
                        }
                    }
                });

        Log.d("sizedsDonHang","số lượng phần tử:"+danhSachDonHang.size());
    }



    public void layDonHangTheoIDCHvaTrangThai(String idCuaHang,String trangThai,List<DonHang> danhSachDonHang, adapterDonHang adapterDonHangs) {
        CollectionReference donHangCollection = db.collection("donHang");

        donHangCollection.whereEqualTo("IDcuaHang", idCuaHang)
                .whereEqualTo("TrangThaiDon", trangThai)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            return;
                        }
                        for (DocumentChange documentChange : value.getDocumentChanges()) {
                            switch (documentChange.getType()) {
                                case ADDED:
                                    // Xử lý khi có dữ liệu mới được thêm vào
                                    DonHang donHangAdded = documentChange.getDocument().toObject(DonHang.class);
                                    danhSachDonHang.add(donHangAdded);
                                    adapterDonHangs.notifyItemInserted(danhSachDonHang.size() - 1);
                                    Log.d("IDdonHang",donHangAdded.getIDdonHang());
                                    Log.d("Thêm được gọi","Sửa dữ liệu được gọi");
                                    break;
                                case MODIFIED:
                                    // Xử lý khi có dữ liệu được sửa đổi
                                    DonHang donHangModified = documentChange.getDocument().toObject(DonHang.class);
                                    // Tìm và cập nhật đối tượng trong danh sách
                                    for (int i = 0; i < danhSachDonHang.size(); i++) {
                                        if (danhSachDonHang.get(i).getIDdonHang().equals(donHangModified.getIDdonHang())) {
                                            danhSachDonHang.set(i, donHangModified);
                                            adapterDonHangs.notifyItemChanged(i);
                                            Log.d("Sửa dữ liệu được gọi","Sửa dữ liệu được gọi");
                                            break;
                                        }
                                    }
                                    break;
                                case REMOVED:
                                    Log.d("xóa", "xóa được gọi ");
                                    // Xử lý khi có dữ liệu bị xóa
                                    DonHang donHang = documentChange.getDocument().toObject(DonHang.class);
                                    int index = -1;
                                    for (int i = 0; i < danhSachDonHang.size(); i++) {
                                        if (danhSachDonHang.get(i).getIDdonHang().equals(donHang.getIDdonHang())) {
                                            index = i;
                                            break;
                                        }
                                    }

                                    if (index != -1) {
                                        danhSachDonHang.remove(index);
                                        adapterDonHangs.notifyItemRemoved(index);
                                    }

                                    Log.d("Bị xóa", "Bị xóa");
                                    break;
                            }
                        }
                    }
                });
    }


    public void xoaDonHang(String idDonHang) {
        DocumentReference donHangDocument = db.collection("donHang").document(idDonHang);

        donHangDocument.delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // Xử lý khi xóa thành công
                        } else {
                            // Xử lý khi có lỗi
                            Exception e = task.getException();
                            if (e != null) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
    }

    public void layDanhSachDonHangTheoCuaHang(String IDcuaHang, TraVeDonHangTimDc callback) {
        db.collection("donHang")
                .whereEqualTo("IDcuaHang", IDcuaHang)
                .whereEqualTo("TrangThaiDon", "Nhan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DonHang> danhSachDonHang = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DonHang donHang = document.toObject(DonHang.class);
                                danhSachDonHang.add(donHang);
                            }

                            // Gọi phương thức callback khi danh sách đơn hàng được lấy thành công
                            callback.onTimDcDonHang(danhSachDonHang);
                        } else {
                            // Gọi phương thức callback khi có lỗi
                            Exception e = task.getException();
                            if (e != null) {
                                callback.onKoTimDcDonHang(e.getMessage());
                            }
                        }
                    }
                });
    }

    public void layDanhSachDonHangTheoKhacHang(String IDKhacHang, TraVeDonHangTimDc callback) {
        db.collection("donHang")
                .whereEqualTo("IDkhachHang", IDKhacHang)
                .whereEqualTo("TrangThaiDon", "Nhan")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<DonHang> danhSachDonHang = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DonHang donHang = document.toObject(DonHang.class);
                                danhSachDonHang.add(donHang);
                            }

                            // Gọi phương thức callback khi danh sách đơn hàng được lấy thành công
                            callback.onTimDcDonHang(danhSachDonHang);
                        } else {
                            // Gọi phương thức callback khi có lỗi
                            Exception e = task.getException();
                            if (e != null) {
                                callback.onKoTimDcDonHang(e.getMessage());
                            }
                        }
                    }
                });
    }

    public interface TraVeDonHangTimDc {
        void onTimDcDonHang(List<DonHang> donHangs);

        void onKoTimDcDonHang(String errorMessage);
    }
}
