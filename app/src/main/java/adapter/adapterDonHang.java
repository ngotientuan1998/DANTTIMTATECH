package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appduan1.ChiTietDonHang;
import com.example.appduan1.ChiTietSanPham;
import com.example.appduan1.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import DAO.DaoDonHang;
import DAO.DaoGioHang;
import DAO.DaoKhachHang;
import DAO.DaoSanPham;
import DAO.Daoformat;
import DTO.DonHang;
import DTO.KhachHang;
import DTO.SanPham;
import DTO.gioHang;


public class adapterDonHang extends RecyclerView.Adapter<adapterDonHang.adapterDonHangView> {
    List<DonHang> ds_DH;
    Context context;
    String ID;
    String role;
    DaoKhachHang daoKhachHang;
    DaoGioHang daoGioHang;
    DaoDonHang daoDonHang;
    DaoSanPham daoSanPham;
    Daoformat daoformat;
    String MH;
    Calendar calendar;

    public adapterDonHang(List<DonHang> ds_DH, Context context, String ID, String role, String MH) {
        this.ds_DH = ds_DH;
        this.context = context;
        this.ID = ID;
        this.role = role;
        this.MH = MH;
        daoKhachHang = new DaoKhachHang(context);
        daoGioHang = new DaoGioHang(context);
        daoDonHang = new DaoDonHang(context);
        daoSanPham = new DaoSanPham(context);
        daoformat = new Daoformat();
        calendar = Calendar.getInstance();
    }

    @NonNull
    @Override
    public adapterDonHangView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_don_hang, parent, false);
        return new adapterDonHangView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterDonHangView holder, int position) {
        DonHang donHangs = ds_DH.get(position);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);


        String ngayThangNam = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
        if (role.equals("Cửa hàng")) {
            holder.btnKhachHayDon.setVisibility(View.GONE);
            holder.btnNhan.setVisibility(View.GONE);
            holder.btnDanhGia.setVisibility(View.GONE);
        } else {
            holder.btnDuyet.setVisibility(View.GONE);
            holder.btnCuaHangHuyDon.setVisibility(View.GONE);
        }

        if(MH.equals("LichSu")){
            holder.btnNhan.setVisibility(View.GONE);
            holder.btnCuaHangHuyDon.setVisibility(View.GONE);
            holder.btnDuyet.setVisibility(View.GONE);
            holder.btnBGCBVC.setVisibility(View.GONE);
            holder.btnKhachHayDon.setVisibility(View.GONE);
        }


        if (MH.equals("Duyet") && role.equals("Cửa hàng")) {
            holder.btnBGCBVC.setVisibility(View.VISIBLE);
            holder.btnNhan.setVisibility(View.GONE);
            holder.btnDuyet.setVisibility(View.GONE);

        }
        if (MH.equals("DangGiao") && role.equals("Cửa hàng")) {
            holder.btnNhan.setVisibility(View.GONE);
            holder.btnCuaHangHuyDon.setVisibility(View.GONE);
            holder.btnDuyet.setVisibility(View.GONE);
            holder.btnBGCBVC.setVisibility(View.GONE);
            holder.btnKhachHayDon.setVisibility(View.GONE);


        }

        if (MH.equals("DangGiao") && role.equals("Khách hàng")) {
            holder.btnNhan.setVisibility(View.VISIBLE);
            holder.btnKhachHayDon.setVisibility(View.GONE);
            holder.btnBGCBVC.setVisibility(View.GONE);
        }

//Phân quyền màn hình đợi duyệt
        if (MH.equals("DoiDuyet")) {
            holder.btnBGCBVC.setVisibility(View.GONE);
            holder.btnNhan.setVisibility(View.GONE);

            holder.tvNgayDuyet.setVisibility(View.GONE);
            holder.tvNgayGiao.setVisibility(View.GONE);
            holder.tvNgayNhan.setVisibility(View.GONE);

        }

////Phân quyền màn hình  duyệt
        if (MH.equals("Duyet")) {
            holder.tvNgayDuyet.setVisibility(View.VISIBLE);
            holder.tvNgayGiao.setVisibility(View.GONE);
            holder.tvNgayNhan.setVisibility(View.GONE);
        }

//Phân quyền màn hình đang giao
        if (MH.equals("DangGiao")) {
            holder.tvNgayGiao.setVisibility(View.VISIBLE);
            holder.tvNgayNhan.setVisibility(View.GONE);
        }

//Phân quyền màn hình nhận
        if (MH.equals("Nhan")) {
            holder.btnNhan.setVisibility(View.GONE);
            holder.btnCuaHangHuyDon.setVisibility(View.GONE);
            holder.btnDuyet.setVisibility(View.GONE);
            holder.btnBGCBVC.setVisibility(View.GONE);
            holder.btnKhachHayDon.setVisibility(View.GONE);

            holder.tvNgayNhan.setVisibility(View.VISIBLE);
        }


        daoKhachHang.TimKhachHang(donHangs.getIDkhachHang(), new DaoKhachHang.TraVeKhachHangTimDc() {
            @Override
            public void onTimDcKhachHang(KhachHang khachHang) {
                holder.tvTenKh.setText("Tên:" + khachHang.getHoTen());
                holder.tvSDT.setText("SĐT:" + khachHang.getSdt());
                holder.tvEmail.setText("Email:" + khachHang.getEmail());
                holder.tvDiaChi.setText("Địa chỉ:" + khachHang.getDiaChiChiTiet());
                holder.tvXa.setText("Xã:" + khachHang.getXa());
                holder.tvHuyen.setText("Huyện:" + khachHang.getHuyen());
                holder.tvTinh.setText("Tỉnh:" + khachHang.getTinh());
            }

            @Override
            public void onKoTimKhachHang(String errorMessage) {

            }
        });
        holder.tvIDdonHang.setText("ID:" + donHangs.getIDdonHang());

        holder.tvNgayDat.setText("Ngày đặt:" + donHangs.getNgayDat());
        holder.tvNgayDuyet.setText("Ngày duyệt:" + donHangs.getNgayDuyet());
        holder.tvNgayGiao.setText("Ngày giao:" + donHangs.getNgayGiao());
        holder.tvNgayNhan.setText("Ngày nhận:" + donHangs.getNgayNhan());

        holder.tvSoLuongSp.setText("Số lượng:" + donHangs.getSoLuongSanPham());
        holder.tvTongGia.setText("Tổng giá:" + daoformat.chuyenDinhDang(donHangs.getGia()));
        holder.tvTrangThaiDonHang.setText("Trang thái:" + donHangs.getTrangThaiDon());
        holder.btnXemChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChiTietDonHang.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("donHang", donHangs);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        holder.btnDuyet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donHangs.setTrangThaiDon("DuocDuyet");
                donHangs.setNgayDuyet(ngayThangNam);
                daoDonHang.capNhatTrangThaiDonDonHang(donHangs);
                Toast.makeText(context, "Đã duyệt đơn hàng", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnKhachHayDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daoDonHang.xoaDonHang(donHangs.getIDdonHang());
            }
        });
        holder.btnCuaHangHuyDon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daoDonHang.xoaDonHang(donHangs.getIDdonHang());
            }
        });
        holder.btnBGCBVC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                donHangs.setTrangThaiDon("DangGiao");
                donHangs.setNgayGiao(ngayThangNam);
                daoDonHang.capNhatTrangThaiDonDonHang(donHangs);
                daoDonHang.capNhatTrangThaiDonDonHang(donHangs);
                Toast.makeText(context, "Đã bàn giao", Toast.LENGTH_SHORT).show();
            }
        });
        holder.btnNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<gioHang> ds_SPThuocDon = donHangs.getDanhSachSanPhamThuocDon();
                for (gioHang x : ds_SPThuocDon) {
                    daoSanPham.Lay1SanPham(x.getIDcuaHang(), x.getIDsanPham(), new DaoSanPham.TraVe1sanPham() {
                        @Override
                        public void onSanPhamLoaded(SanPham sanPham) {
                            sanPham.setSoLuong(sanPham.getSoLuong() - x.getSoLuongMua());
                            sanPham.setSoLuongBan(sanPham.getSoLuongBan() + x.getSoLuongMua());
                            daoSanPham.capNhatSanPhamTrongCuaHang(sanPham.getIDcuaHang(), sanPham.getIDsanPham(), sanPham);
                        }

                        @Override
                        public void onSanPhamLoadFailed() {

                        }
                    });
                }
                donHangs.setTrangThaiDon("Nhan");
                donHangs.setNgayNhan(ngayThangNam);
                daoDonHang.capNhatTrangThaiDonDonHang(donHangs);

            }
        });
    }

    @Override
    public int getItemCount() {

        return ds_DH.size();
    }

    public class adapterDonHangView extends RecyclerView.ViewHolder {
        TextView tvIDdonHang, tvTenKh, tvSDT, tvEmail, tvDiaChi, tvXa, tvHuyen, tvTinh, tvNgayDat, tvNgayDuyet, tvNgayGiao, tvNgayNhan,
                tvSoLuongSp, tvTongGia, tvTrangThaiDonHang;
        Button btnXemChiTiet, btnDuyet, btnKhachHayDon, btnCuaHangHuyDon, btnBGCBVC, btnNhan,btnDanhGia;

        public adapterDonHangView(@NonNull View itemView) {
            super(itemView);
            tvIDdonHang = itemView.findViewById(R.id.tvIDdonHangMua);
            tvTenKh = itemView.findViewById(R.id.tvTenKhachHangMua);
            tvSDT = itemView.findViewById(R.id.tvSDTKhachHangMua);
            tvEmail = itemView.findViewById(R.id.tvEmailKhachHangMua);
            tvDiaChi = itemView.findViewById(R.id.tvDiaChiTietKHMua);
            tvXa = itemView.findViewById(R.id.tvXaKHMua);
            tvHuyen = itemView.findViewById(R.id.tvHuyenKHMua);
            tvTinh = itemView.findViewById(R.id.tvTinhKHMua);

            tvNgayDat = itemView.findViewById(R.id.tvNgayDatMua);
            tvNgayDuyet = itemView.findViewById(R.id.tvNgayDuyet);
            tvNgayGiao = itemView.findViewById(R.id.tvNgayDangGiao);
            tvNgayNhan = itemView.findViewById(R.id.tvNgayNhan);

            tvSoLuongSp = itemView.findViewById(R.id.tvSoLuongSpMua);
            tvTongGia = itemView.findViewById(R.id.tvTongGiaSPMua);
            tvTrangThaiDonHang = itemView.findViewById(R.id.tvTrangThaiSPMua);

            btnXemChiTiet = itemView.findViewById(R.id.btnXemChiTietDonHang);
            btnDuyet = itemView.findViewById(R.id.btnDuyetDonHangMua);
            btnKhachHayDon = itemView.findViewById(R.id.btnHuyDonHang);
            btnCuaHangHuyDon = itemView.findViewById(R.id.btnHuyDuyetDonHangMua);
            btnBGCBVC = itemView.findViewById(R.id.btnBGBVCHangMua);
            btnNhan = itemView.findViewById(R.id.btnNhanDonHang);
            btnDanhGia=itemView.findViewById(R.id.btnDanhGiaDonHangMua);
        }
    }
}
