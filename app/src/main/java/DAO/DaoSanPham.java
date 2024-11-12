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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DTO.SanPham;
import adapter.adapterSanPham;

public class DaoSanPham {
    FirebaseFirestore db;
    Context context;
    DaoChayQuangCao daoChayQuangCao;


    public DaoSanPham(Context context) {
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
        daoChayQuangCao = new DaoChayQuangCao(context);
    }


    public void themSanPhamVaoCuaHang(String cuaHangId, SanPham sanPham) {

        DocumentReference cuaHangRef = db.collection("cuaHang").document(cuaHangId);

        // Lấy dữ liệu hiện tại của cửa hàng
        cuaHangRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Lấy danh sách sản phẩm hiện tại từ Firestore
                        List<Map<String, Object>> danhSachSanPham = (List<Map<String, Object>>) document.get("danhSachSanPham");

                        // Tạo một Map mới cho sản phẩm mới
                        Map<String, Object> sanPhamMap = new HashMap<>();
                        sanPhamMap.put("IDsanPham", sanPham.getIDsanPham());
                        sanPhamMap.put("IDcuaHang", sanPham.getIDcuaHang());
                        sanPhamMap.put("tenSanPham", sanPham.getTenSanPham());
                        sanPhamMap.put("chiTietSanPham", sanPham.getChiTietSanPham());
                        sanPhamMap.put("loaiSanPham", sanPham.getLoaiSanPham());
                        sanPhamMap.put("ngayThem", sanPham.getNgayThem());
                        sanPhamMap.put("gia", sanPham.getGia());
                        sanPhamMap.put("giamGia", sanPham.getGiamGia());
                        sanPhamMap.put("soLuong", sanPham.getSoLuong());
                        sanPhamMap.put("soLuongBan", sanPham.getSoLuongBan());
                        sanPhamMap.put("hinhAnhSanPham", sanPham.getHinhAnhSanPham());
                        sanPhamMap.put("diemChayQuangCao", sanPham.getDiemChayQuangCao());
                        sanPhamMap.put("CPU", sanPham.getCPU());
                        sanPhamMap.put("RAM", sanPham.getRAM());
                        sanPhamMap.put("oCung", sanPham.getoCung());
                        sanPhamMap.put("Card", sanPham.getCard());
                        sanPhamMap.put("Mhinh", sanPham.getMhinh());
                        sanPhamMap.put("tenCuaHang", sanPham.getTenCuaHang());
//                        tenCuaHang

                        // Thêm sản phẩm mới vào danh sách sản phẩm
                        if (danhSachSanPham == null) {
                            danhSachSanPham = new ArrayList<>();
                        }
                        danhSachSanPham.add(sanPhamMap);

                        // Cập nhật dữ liệu của cửa hàng với danh sách sản phẩm mới
                        cuaHangRef.update("danhSachSanPham", danhSachSanPham)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(context, "Đã thêm SP:" + sanPham.getTenSanPham(), Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(context, "Thêm thất bại", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    } else {
                        // Handle the case where the cuaHang document does not exist
                    }
                } else {
                    // Handle the error
                }
            }
        });
    }

    public void langNgheThayDoiSanPham(String cuaHangId, adapterSanPham adapterSanPhams, List<SanPham> dsCu) {
        DocumentReference cuaHangRef = db.collection("cuaHang").document(cuaHangId);

        cuaHangRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("FirebaseError", "Lỗi khi lắng nghe dữ liệu: " + error.getMessage());
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    Map<String, Object> data = snapshot.getData();

                    // Lấy danh sách sản phẩm từ dữ liệu cửa hàng
                    List<Map<String, Object>> danhSachSanPhamMoi = (List<Map<String, Object>>) data.get("danhSachSanPham");

                    if (danhSachSanPhamMoi != null) {
                        // Tạo danh sách mới để theo dõi các thay đổi
                        List<SanPham> dsMoiTemp = new ArrayList<>();

                        // Tạo danh sách các IDs của sản phẩm trong dsCu
                        List<String> dsCuIds = new ArrayList<>();
                        for (SanPham sanPhamCu : dsCu) {
                            dsCuIds.add(sanPhamCu.getIDsanPham());
                        }

                        for (Map<String, Object> sanPhamMap : danhSachSanPhamMoi) {
                            SanPham sanPham = new SanPham();
                            sanPham.setIDsanPham((String) sanPhamMap.get("IDsanPham"));
                            sanPham.setTenSanPham((String) sanPhamMap.get("tenSanPham"));
                            sanPham.setChiTietSanPham((String) sanPhamMap.get("chiTietSanPham"));
                            sanPham.setLoaiSanPham((String) sanPhamMap.get("loaiSanPham"));
                            sanPham.setNgayThem((String) sanPhamMap.get("ngayThem"));
                            sanPham.setGia(((Number) sanPhamMap.get("gia")).doubleValue());
                            sanPham.setGiamGia(((Number) sanPhamMap.get("giamGia")).doubleValue());
                            sanPham.setSoLuong(((Number) sanPhamMap.get("soLuong")).intValue());
                            sanPham.setSoLuongBan(((Number) sanPhamMap.get("soLuongBan")).intValue());
                            sanPham.setHinhAnhSanPham((List<String>) sanPhamMap.get("hinhAnhSanPham"));
                            sanPham.setDiemChayQuangCao(((Number) sanPhamMap.get("diemChayQuangCao")).intValue());
                            sanPham.setCPU((String) sanPhamMap.get("CPU"));
                            sanPham.setRAM((String) sanPhamMap.get("RAM"));
                            sanPham.setoCung((String) sanPhamMap.get("oCung"));
                            sanPham.setCard((String) sanPhamMap.get("Card"));
                            sanPham.setMhinh((String) sanPhamMap.get("Mhinh"));
                            sanPham.setTenCuaHang((String) sanPhamMap.get("tenCuaHang"));
                            sanPham.setIDcuaHang((String) sanPhamMap.get("IDcuaHang"));
//                            IDcuaHang

                            int indexCu = dsCuIds.indexOf(sanPham.getIDsanPham());

                            if (indexCu == -1) {
                                // Sản phẩm mới chưa tồn tại trong dsCu
                                dsCu.add(sanPham);
                                adapterSanPhams.notifyItemInserted(dsCu.size() - 1);
                                Log.d("SanPhamChange", "Sản phẩm mới được thêm: " + sanPham);
                            } else {
                                // Sản phẩm đã tồn tại trong danh sách cũ
                                // Sản phẩm có trong cả dsCu và dsMoiTemp, kiểm tra xem có sự thay đổi không
                                SanPham sanPhamCu = dsCu.get(indexCu);
                                if (!sanPham.equals(sanPhamCu)) {
                                    // Sản phẩm bị sửa đổi
                                    dsCu.set(indexCu, sanPham);
                                    adapterSanPhams.notifyItemChanged(indexCu);
                                    Log.d("SanPhamChange", "Sản phẩm bị sửa: " + sanPham);
                                }
                            }
                            dsMoiTemp.add(sanPham);
                            // Thêm sản phẩm vào danh sách tạm thời dsMoiTemp

                        }

                        // Kiểm tra và xóa các sản phẩm đã bị xóa
                        for (String idCu : dsCuIds) {
                            boolean checkXoa = false;

                            for (SanPham sanPhamTemp : dsMoiTemp) {
                                if (sanPhamTemp.getIDsanPham().equals(idCu)) {
                                    checkXoa = true;
                                    break;
                                }
                            }

                            if (!checkXoa) {
                                // Nếu không tồn tại trong dsMoiTemp, sản phẩm đã bị xóa
                                int indexCu = dsCuIds.indexOf(idCu);
                                dsCu.remove(indexCu);
                                adapterSanPhams.notifyItemRemoved(indexCu);
                                Log.d("SanPhamChange", "Sản phẩm bị xóa: " + idCu);
                            }
                        }

                    }
                } else {
                    Log.e("FirebaseError", "Tài liệu cửa hàng không tồn tại hoặc có lỗi.");
                }
            }
        });
    }


    public void layDanhSachSanPhamThuocCuaHang(String cuaHangId,ToanBoDanhSachSanPham toanBoDanhSachSanPham) {
        // Tạo một tham chiếu đến document của cửa hàng trong collection "cuaHang"
        DocumentReference cuaHangRef = db.collection("cuaHang").document(cuaHangId);

        // Lấy dữ liệu hiện tại của cửa hàng
        cuaHangRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Lấy danh sách sản phẩm từ Firestore
                        List<SanPham> dsSp=new ArrayList<>();
                        List<Map<String, Object>> danhSachSanPhamMap = (List<Map<String, Object>>) document.get("danhSachSanPham");

                        // Chuyển đổi danh sách Map sang danh sách SanPham
                        if (danhSachSanPhamMap != null) {
                            for (Map<String, Object> sanPhamMap : danhSachSanPhamMap) {
                                SanPham sanPham = new SanPham();
                                sanPham.setIDsanPham((String) sanPhamMap.get("IDsanPham"));
                                sanPham.setTenSanPham((String) sanPhamMap.get("tenSanPham"));
                                sanPham.setChiTietSanPham((String) sanPhamMap.get("chiTietSanPham"));
                                sanPham.setLoaiSanPham((String) sanPhamMap.get("loaiSanPham"));
                                sanPham.setNgayThem((String) sanPhamMap.get("ngayThem"));
                                sanPham.setGia(((Number) sanPhamMap.get("gia")).doubleValue());
                                sanPham.setGiamGia(((Number) sanPhamMap.get("giamGia")).doubleValue());
                                sanPham.setSoLuong(((Number) sanPhamMap.get("soLuong")).intValue());
                                sanPham.setSoLuongBan(((Number) sanPhamMap.get("soLuongBan")).intValue());
                                sanPham.setHinhAnhSanPham((List<String>) sanPhamMap.get("hinhAnhSanPham"));
                                sanPham.setDiemChayQuangCao(((Number) sanPhamMap.get("diemChayQuangCao")).intValue());
                                sanPham.setCPU((String) sanPhamMap.get("CPU"));
                                sanPham.setRAM((String) sanPhamMap.get("RAM"));
                                sanPham.setoCung((String) sanPhamMap.get("oCung"));
                                sanPham.setCard((String) sanPhamMap.get("Card"));
                                sanPham.setMhinh((String) sanPhamMap.get("Mhinh"));
                                sanPham.setTenCuaHang((String) sanPhamMap.get("tenCuaHang"));
                                sanPham.setIDcuaHang((String) sanPhamMap.get("IDcuaHang"));
                                dsSp.add(sanPham);

                            }
                        }
                        toanBoDanhSachSanPham.ThanhCong(dsSp);

                    } else {
                        // Handle the case where the cuaHang document does not exist

                    }
                } else {
                    // Handle the error

                }
            }
        });
    }

    public void xoaHinhAnhSanPhamSua(String cuaHangId, String sanPhamId, String hinhAnhUrl) {
        // Tạo một tham chiếu đến document của cửa hàng trong collection "cuaHang"
        DocumentReference cuaHangRef = db.collection("cuaHang").document(cuaHangId);

        // Tạo một tham chiếu đến danh sách sản phẩm của cửa hàng
        CollectionReference sanPhamCollectionRef = cuaHangRef.collection("danhSachSanPham");

        // Tạo một tham chiếu đến document của sản phẩm trong danh sách sản phẩm của cửa hàng
        DocumentReference sanPhamRef = sanPhamCollectionRef.document(sanPhamId);

        // Lấy dữ liệu hiện tại của sản phẩm
        sanPhamRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Lấy danh sách hình ảnh từ Firestore
                        List<String> danhSachHinhAnh = (List<String>) document.get("hinhAnhSanPham");

                        if (danhSachHinhAnh != null) {
                            // Xóa hình ảnh từ danh sách
                            danhSachHinhAnh.remove(hinhAnhUrl);

                            // Cập nhật dữ liệu của sản phẩm với danh sách hình ảnh mới
                            sanPhamRef.update("hinhAnhSanPham", danhSachHinhAnh)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                xoaHinhAnhFirebaseStorage(hinhAnhUrl);

                                            } else {
                                                // Gọi listener khi xóa hình ảnh thất bại

                                            }
                                        }
                                    });
                        } else {
                            // Handle the case where danhSachHinhAnh is null

                        }
                    } else {
                        // Handle the case where the sanPham document does not exist

                    }
                } else {
                    // Handle the error

                }
            }
        });
    }


    public void xoaHinhAnhFirebaseStorage(String hinhAnhUrl) {
        // Tạo một tham chiếu đến Firebase Storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Xóa hình ảnh từ Firebase Storage
        StorageReference hinhAnhRef = storageRef.child(hinhAnhUrl);
        hinhAnhRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Handle success
                // Có thể log hoặc thực hiện các hành động khác khi xóa thành công
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Handle failure
                // Có thể log hoặc thực hiện các hành động khác khi xóa thất bại
            }
        });
    }

    public void capNhatSanPhamTrongCuaHang(String cuaHangId, String IDsanPham, SanPham sanPhamMoi) {
        // Tạo một tham chiếu đến document của cửa hàng trong collection "cuaHang"
        DocumentReference cuaHangRef = db.collection("cuaHang").document(cuaHangId);

        // Lấy dữ liệu hiện tại của cửa hàng
        cuaHangRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Lấy danh sách sản phẩm hiện tại từ Firestore
                        List<Map<String, Object>> danhSachSanPham = (List<Map<String, Object>>) document.get("danhSachSanPham");

                        if (danhSachSanPham != null) {
                            // Tìm sản phẩm cần cập nhật trong danh sách
                            for (Map<String, Object> sanPhamMap : danhSachSanPham) {
                                String idSanPhamHienTai = (String) sanPhamMap.get("IDsanPham");
                                if (idSanPhamHienTai != null && idSanPhamHienTai.equals(IDsanPham)) {
                                    // Cập nhật thông tin của sản phẩm
                                    sanPhamMap.put("tenSanPham", sanPhamMoi.getTenSanPham());
                                    sanPhamMap.put("chiTietSanPham", sanPhamMoi.getChiTietSanPham());
                                    sanPhamMap.put("loaiSanPham", sanPhamMoi.getLoaiSanPham());
                                    sanPhamMap.put("ngayThem", sanPhamMoi.getNgayThem());
                                    sanPhamMap.put("gia", sanPhamMoi.getGia());
                                    sanPhamMap.put("giamGia", sanPhamMoi.getGiamGia());
                                    sanPhamMap.put("soLuong", sanPhamMoi.getSoLuong());
                                    sanPhamMap.put("soLuongBan", sanPhamMoi.getSoLuongBan());
                                    sanPhamMap.put("hinhAnhSanPham", sanPhamMoi.getHinhAnhSanPham());
                                    sanPhamMap.put("diemChayQuangCao", sanPhamMoi.getDiemChayQuangCao());
                                    //                            CPU,RAM,oCung,Card,Mhinh
                                    sanPhamMap.put("CPU", sanPhamMoi.getCPU());
                                    sanPhamMap.put("RAM", sanPhamMoi.getRAM());
                                    sanPhamMap.put("oCung", sanPhamMoi.getoCung());
                                    sanPhamMap.put("Card", sanPhamMoi.getCard());
                                    sanPhamMap.put("Mhinh", sanPhamMoi.getMhinh());
                                    sanPhamMap.put("tenCuaHang", sanPhamMoi.getTenCuaHang());
                                    // Cập nhật dữ liệu của cửa hàng với danh sách sản phẩm mới
                                    cuaHangRef.update("danhSachSanPham", danhSachSanPham)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(context, "Đã cập nhật thông tin SP: " + sanPhamMoi.getTenSanPham(), Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(context, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    break; // Dừng vòng lặp sau khi cập nhật sản phẩm
                                }
                            }
                        }
                    } else {
                        // Handle the case where the cuaHang document does not exist
                    }
                } else {
                    // Handle the error
                }
            }
        });
    }

    public void xoaSanPhamKhoiCuaHang(String cuaHangId, String IDsanPham) {
        // Tạo một tham chiếu đến document của cửa hàng trong collection "cuaHang"
        DocumentReference cuaHangRef = db.collection("cuaHang").document(cuaHangId);

        // Lấy dữ liệu hiện tại của cửa hàng
        cuaHangRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Lấy danh sách sản phẩm hiện tại từ Firestore
                        List<Map<String, Object>> danhSachSanPham = (List<Map<String, Object>>) document.get("danhSachSanPham");

                        if (danhSachSanPham != null) {
                            // Tìm sản phẩm cần xóa trong danh sách
                            for (Map<String, Object> sanPhamMap : danhSachSanPham) {
                                String idSanPhamHienTai = (String) sanPhamMap.get("IDsanPham");
                                if (idSanPhamHienTai != null && idSanPhamHienTai.equals(IDsanPham)) {
                                    // Xóa sản phẩm khỏi danh sách
                                    danhSachSanPham.remove(sanPhamMap);

                                    // Cập nhật dữ liệu của cửa hàng với danh sách sản phẩm mới
                                    cuaHangRef.update("danhSachSanPham", danhSachSanPham)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        daoChayQuangCao.xoaQuangCao(cuaHangId, IDsanPham);
                                                        Toast.makeText(context, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(context, "Xóa sản phẩm thất bại", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                    break; // Dừng vòng lặp sau khi xóa sản phẩm
                                }
                            }
                        }
                    } else {
                        // Handle the case where the cuaHang document does not exist
                    }
                } else {
                    // Handle the error
                }
            }
        });
    }


    public void capNhatDiemDeXuatSanPham(String cuaHangId, String IDsanPham, int diem) {
        // Tạo một tham chiếu đến document của cửa hàng trong collection "cuaHang"
        DocumentReference cuaHangRef = db.collection("cuaHang").document(cuaHangId);

        // Lấy dữ liệu hiện tại của cửa hàng
        cuaHangRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Lấy danh sách sản phẩm hiện tại từ Firestore
                        List<Map<String, Object>> danhSachSanPham = (List<Map<String, Object>>) document.get("danhSachSanPham");

                        if (danhSachSanPham != null) {
                            // Tìm sản phẩm cần cập nhật trong danh sách
                            for (Map<String, Object> sanPhamMap : danhSachSanPham) {
                                String idSanPhamHienTai = (String) sanPhamMap.get("IDsanPham");
                                if (idSanPhamHienTai != null && idSanPhamHienTai.equals(IDsanPham)) {
                                    // Chỉ cập nhật trường diemChayQuangCao
                                    sanPhamMap.put("diemChayQuangCao", diem);

                                    // Cập nhật dữ liệu của cửa hàng với danh sách sản phẩm mới
                                    cuaHangRef.update("danhSachSanPham", danhSachSanPham)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                    } else {
                                                    }
                                                }
                                            });
                                    break; // Dừng vòng lặp sau khi cập nhật sản phẩm
                                }
                            }
                        }
                    } else {
                        // Handle the case where the cuaHang document does not exist
                    }
                } else {
                    // Handle the error
                }
            }
        });
    }

    public interface ToanBoDanhSachSanPham {
        void ThanhCong(List<SanPham> sanPhamList);

        void ThatBai(Exception e);
    }

    public void layToanBoSanPham(ToanBoDanhSachSanPham toanBoDanhSachSanPham) {
        // Thực hiện truy vấn để lấy tất cả sản phẩm
        db.collection("cuaHang")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<SanPham> DstoanBoSanPham = new ArrayList<>();

                            // Lặp qua tất cả các cửa hàng
                            for (QueryDocumentSnapshot cuaHangDocument : task.getResult()) {
                                // Lấy danh sách sản phẩm từ mỗi cửa hàng
                                List<Map<String, Object>> danhSachSanPham = (List<Map<String, Object>>) cuaHangDocument.get("danhSachSanPham");

                                // Kiểm tra xem danh sách có tồn tại không
                                if (danhSachSanPham != null) {
                                    // Lặp qua danh sách sản phẩm và chuyển đổi thành đối tượng SanPham
                                    for (Map<String, Object> sanPhamMap : danhSachSanPham) {
                                        SanPham sanPham = new SanPham();
                                        sanPham.setIDsanPham((String) sanPhamMap.get("IDsanPham"));
                                        sanPham.setIDcuaHang((String) sanPhamMap.get("IDcuaHang"));
                                        sanPham.setTenSanPham((String) sanPhamMap.get("tenSanPham"));
                                        sanPham.setChiTietSanPham((String) sanPhamMap.get("chiTietSanPham"));
                                        sanPham.setLoaiSanPham((String) sanPhamMap.get("loaiSanPham"));
                                        sanPham.setNgayThem((String) sanPhamMap.get("ngayThem"));
                                        sanPham.setGia(((Number) sanPhamMap.get("gia")).doubleValue());
                                        sanPham.setGiamGia(((Number) sanPhamMap.get("giamGia")).doubleValue());
                                        sanPham.setSoLuong(((Number) sanPhamMap.get("soLuong")).intValue());
                                        sanPham.setSoLuongBan(((Number) sanPhamMap.get("soLuongBan")).intValue());
                                        sanPham.setDiemChayQuangCao(((Number) sanPhamMap.get("diemChayQuangCao")).intValue());
                                        //                            CPU,RAM,oCung,Card,Mhinh
                                        sanPham.setCPU((String) sanPhamMap.get("CPU"));
                                        sanPham.setRAM((String) sanPhamMap.get("RAM"));
                                        sanPham.setoCung((String) sanPhamMap.get("oCung"));
                                        sanPham.setCard((String) sanPhamMap.get("Card"));
                                        sanPham.setMhinh((String) sanPhamMap.get("Mhinh"));
                                        sanPham.setTenCuaHang((String) sanPhamMap.get("tenCuaHang"));

                                        // Xử lý danh sách hình ảnh sản phẩm
                                        List<String> hinhAnhSanPham = (List<String>) sanPhamMap.get("hinhAnhSanPham");
                                        if (hinhAnhSanPham != null) {
                                            sanPham.setHinhAnhSanPham(hinhAnhSanPham);
                                        }
                                        // Thêm sản phẩm vào danh sách toàn bộ sản phẩm
                                        DstoanBoSanPham.add(sanPham);
                                    }
                                }
                            }
// Sắp xếp danh sách sản phẩm theo DiemChayQuangCao từ cao đến thấp, nếu bằng nhau thì sắp xếp theo soLuongBan, nếu bằng nhau thì sắp xếp theo soLuongSanPham, nếu bằng nhau thì sắp xếp theo giá từ thấp đến cao
                            Collections.sort(DstoanBoSanPham, new Comparator<SanPham>() {
                                @Override
                                public int compare(SanPham sp1, SanPham sp2) {
                                    int diemChayCompare = Integer.compare(sp2.getDiemChayQuangCao(), sp1.getDiemChayQuangCao());

                                    if (diemChayCompare == 0) {
                                        int soLuongBanCompare = Integer.compare(sp2.getSoLuongBan(), sp1.getSoLuongBan());

                                        if (soLuongBanCompare == 0) {
                                            int soLuongSanPhamCompare = Integer.compare(sp2.getSoLuong(), sp1.getSoLuong());

                                            if (soLuongSanPhamCompare == 0) {
                                                return Double.compare(sp1.getGia(), sp2.getGia()); // Sắp xếp theo giá từ thấp đến cao
                                            } else {
                                                return soLuongSanPhamCompare;
                                            }
                                        } else {
                                            return soLuongBanCompare;
                                        }
                                    } else {
                                        return diemChayCompare;
                                    }
                                }
                            });

                            // Gọi callback ThanhCong với danh sách sản phẩm
                            toanBoDanhSachSanPham.ThanhCong(DstoanBoSanPham);
                        } else {
                            // Gọi callback ThatBai với exception khi truy vấn không thành công
                            toanBoDanhSachSanPham.ThatBai(task.getException());
                        }
                    }
                });
    }


    public void Lay1SanPham(String cuaHangId, String sanPhamId, TraVe1sanPham traVe1sanPham) {
        DocumentReference cuaHangRef = db.collection("cuaHang").document(cuaHangId);

        cuaHangRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        List<Map<String, Object>> danhSachSanPham = (List<Map<String, Object>>) document.get("danhSachSanPham");

                        if (danhSachSanPham != null) {
                            // Lặp qua danh sách sản phẩm để tìm sản phẩm theo ID
                            for (Map<String, Object> sanPhamMap : danhSachSanPham) {
                                String currentSanPhamId = (String) sanPhamMap.get("IDsanPham");

                                if (currentSanPhamId != null && currentSanPhamId.equals(sanPhamId)) {
                                    // Tìm thấy sản phẩm theo ID
                                    SanPham sanPham = new SanPham();
                                    sanPham.setIDsanPham((String) sanPhamMap.get("IDsanPham"));
                                    sanPham.setIDcuaHang((String) sanPhamMap.get("IDcuaHang"));
                                    sanPham.setTenSanPham((String) sanPhamMap.get("tenSanPham"));
                                    sanPham.setChiTietSanPham((String) sanPhamMap.get("chiTietSanPham"));
                                    sanPham.setLoaiSanPham((String) sanPhamMap.get("loaiSanPham"));
                                    sanPham.setNgayThem((String) sanPhamMap.get("ngayThem"));
                                    sanPham.setGia(((Number) sanPhamMap.get("gia")).doubleValue());
                                    sanPham.setGiamGia(((Number) sanPhamMap.get("giamGia")).doubleValue());
                                    sanPham.setSoLuong(((Number) sanPhamMap.get("soLuong")).intValue());
                                    sanPham.setSoLuongBan(((Number) sanPhamMap.get("soLuongBan")).intValue());
                                    sanPham.setDiemChayQuangCao(((Number) sanPhamMap.get("diemChayQuangCao")).intValue());
                                    sanPham.setCPU((String) sanPhamMap.get("CPU"));
                                    sanPham.setRAM((String) sanPhamMap.get("RAM"));
                                    sanPham.setoCung((String) sanPhamMap.get("oCung"));
                                    sanPham.setCard((String) sanPhamMap.get("Card"));
                                    sanPham.setMhinh((String) sanPhamMap.get("Mhinh"));
                                    sanPham.setTenCuaHang((String) sanPhamMap.get("tenCuaHang"));

                                    // Xử lý danh sách hình ảnh sản phẩm
                                    List<String> hinhAnhSanPham = (List<String>) sanPhamMap.get("hinhAnhSanPham");
                                    if (hinhAnhSanPham != null) {
                                        sanPham.setHinhAnhSanPham(hinhAnhSanPham);
                                    }

                                    // Gọi callback khi sản phẩm được tìm thấy
                                    if (traVe1sanPham != null) {
                                        traVe1sanPham.onSanPhamLoaded(sanPham);
                                    }
                                    return;
                                }
                            }
                        }
                    }
                }

                // Gọi callback khi không tìm thấy sản phẩm hoặc xảy ra lỗi
                if (traVe1sanPham != null) {
                    traVe1sanPham.onSanPhamLoadFailed();
                }
            }
        });
    }

    // Interface để xử lý callback khi sản phẩm được tìm thấy hoặc không tìm thấy
    public interface TraVe1sanPham {
        void onSanPhamLoaded(SanPham sanPham);

        void onSanPhamLoadFailed();
    }


}
