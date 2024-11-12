package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appduan1.ChiTietKhachHang;
import com.example.appduan1.ChiTietSanPham;
import com.example.appduan1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import DAO.DaoCuaHang;
import DAO.DaoKhachHang;
import DAO.DaoSanPham;
import DAO.Daoformat;
import DTO.CuaHang;
import DTO.KhachHang;
import DTO.SanPham;
import interfaces.SuaSanPhaminterface;
import interfaces.XoaSanPhaminterface;

public class adapterSanPham extends RecyclerView.Adapter<adapterSanPham.adapterSanPhamView> {
    Context context;
    List<SanPham> ds_sanPham;
    DaoSanPham daoSanPham;
    DaoCuaHang daoCuaHang;
    DaoKhachHang daoKhachHang;
    Daoformat daoformat;
    String ID, role, MH = "Khac";

    SuaSanPhaminterface suasanPhamInterfaces;
    XoaSanPhaminterface xoaSanPhaminterface;


    public adapterSanPham(Context context, List<SanPham> ds_sanPham, String ID, String role, SuaSanPhaminterface sanPhamInterfaces, XoaSanPhaminterface xoaSanPhaminterface) {
        this.context = context;
        this.ds_sanPham = ds_sanPham;
        this.daoSanPham = new DaoSanPham(context);
        this.daoKhachHang = new DaoKhachHang(context);
        this.daoCuaHang = new DaoCuaHang(context);
        this.daoformat = new Daoformat();
        this.ID = ID;
        this.role = role;
        this.suasanPhamInterfaces = sanPhamInterfaces;
        this.xoaSanPhaminterface = xoaSanPhaminterface;

    }

    public adapterSanPham(Context context, List<SanPham> ds_sanPham, String ID, String role, String MH) {
        this.context = context;
        this.ds_sanPham = ds_sanPham;
        this.daoSanPham = new DaoSanPham(context);
        this.daoKhachHang = new DaoKhachHang(context);
        this.daoCuaHang = new DaoCuaHang(context);
        this.daoformat = new Daoformat();
        this.ID = ID;
        this.MH = MH;
        this.role = role;

    }


    @NonNull
    @Override
    public adapterSanPhamView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_san_pham, parent, false);
        return new adapterSanPhamView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterSanPhamView holder, int position) {
        SanPham sanPham = ds_sanPham.get(position);
        if (!sanPham.getHinhAnhSanPham().isEmpty()) {
            String imgurl = sanPham.getHinhAnhSanPham().get(0);
            if (imgurl != null) {
                Picasso.get().load(imgurl).into(holder.imgAnhSanPham);
            }
        }

        if (MH.equals("Home")) {
            holder.btnSua.setVisibility(View.GONE);
            holder.imgxoa.setVisibility(View.GONE);
        }

        holder.tvTen.setText ( sanPham.getTenSanPham());
        holder.tvGiamGia.setText("Giảm:" + daoformat.chuyenDinhDang(sanPham.getGiamGia())+"đ");
        holder.tvGia.setText(daoformat.chuyenDinhDang(sanPham.getGia()));
        holder.tvDaBan.setText("Đã bán:" + String.valueOf(sanPham.getSoLuongBan())+"đ");
        holder.btnSua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suasanPhamInterfaces.SuaSanPham(sanPham);
            }
        });
        holder.imgxoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xoaSanPhaminterface.XoaSanPham(sanPham);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sanPham.getIDcuaHang() != null) {
                    daoCuaHang.TimCuaHang(sanPham.getIDcuaHang(), new DaoCuaHang.TraVeCuaHangTimDc() {
                        @Override
                        public void onTimDcCuaHang(CuaHang cuaHang) {
                            if (cuaHang != null) {
                                Intent intent = new Intent(context, ChiTietSanPham.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("IDCuaHangCoSP", sanPham.getIDcuaHang());
                                bundle.putSerializable("sanPham", sanPham);
                                bundle.putSerializable("cuaHang", cuaHang);
                                intent.putExtras(bundle);
                                context.startActivity(intent);
                            }
                        }

                        @Override
                        public void onKoTimCuaHang(String errorMessage) {

                        }
                    });
                } else {
                    if (sanPham != null) {
                        Log.d("sanPham bị null", " null" + sanPham.getIDcuaHang());
                    } else {
                        Log.d("ID sanPham bị null", " null null null null null");
                    }
                }

            }
        });
    }


    @Override
    public int getItemCount() {
        return ds_sanPham.size();
    }

    public class adapterSanPhamView extends RecyclerView.ViewHolder {
        ImageView imgxoa, imgAnhSanPham;
        TextView tvTen, tvGiamGia, tvGia, tvDaBan;
        Button btnSua;

        public adapterSanPhamView(@NonNull View itemView) {
            super(itemView);
            imgxoa = itemView.findViewById(R.id.imXoaSanPhamSP);
            imgAnhSanPham = itemView.findViewById(R.id.imAnhSP);

            tvTen = itemView.findViewById(R.id.tvTenSP);
            tvGiamGia = itemView.findViewById(R.id.tvGiamGiaSP);
            tvGia = itemView.findViewById(R.id.tvGiaSP);
            tvDaBan = itemView.findViewById(R.id.tvSoLuongBanSP);

            btnSua = itemView.findViewById(R.id.btnSuaSanPham);
        }
    }

}
