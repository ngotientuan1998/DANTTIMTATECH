package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appduan1.ChiTietCuaHang;
import com.example.appduan1.ChiTietKhachHang;
import com.example.appduan1.R;

import java.util.List;

import DAO.DaoKhachHang;
import DTO.KhachHang;
import interfaces.khachHangInterface;

public class adapterKhachHang extends RecyclerView.Adapter<adapterKhachHang.adapterKhachHangView> {

    Context context;
    List<KhachHang> ds;
    DaoKhachHang daoKhachHang;

    public adapterKhachHang(Context context, List<KhachHang> ds, khachHangInterface khachHangInterfaces) {
        this.context = context;
        this.ds = ds;
        daoKhachHang=new DaoKhachHang(context);

    }

    @NonNull
    @Override
    public adapterKhachHangView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_khach_hang_layout, parent, false);
        return new adapterKhachHangView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterKhachHangView holder, int position) {
        KhachHang khachHang = ds.get(position);
        if (khachHang != null) {
            holder.tvTen.setText("Tên:" + khachHang.getHoTen());
            holder.tvEmail.setText("Email:" + khachHang.getEmail());
            holder.tvsdt.setText(String.valueOf("SĐT:" + khachHang.getSdt()));
            holder.tvGioiTinh.setText("Giới tính:" + khachHang.getGioiTinh());
            holder.tvNamSinh.setText("Năm sinh:" + String.valueOf(khachHang.getNTNS()));
            holder.tvTrangThai.setText("Trạng thái:\n" + String.valueOf(khachHang.getTrangThai()));
            if (khachHang.getTrangThai().equals("hoatDong")) {
                holder.tvTrangThai.setTextColor(Color.GREEN);
                holder.btnDongMo.setText("Khóa");
                holder.btnDongMo.setBackgroundColor(Color.RED);
            } else {
                holder.btnDongMo.setText("Mở");
                holder.btnDongMo.setBackgroundColor(Color.GREEN);
                holder.tvTrangThai.setTextColor(Color.RED);
            }
            if (khachHang.getTrangThai().equals("hoatDong")) {
                holder.btnDongMo.setText("Đóng");
                holder.btnDongMo.setBackgroundColor(Color.RED);
            } else {
                holder.btnDongMo.setText("Mở");
                holder.btnDongMo.setBackgroundColor(Color.GREEN);
            }
            holder.btnDongMo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (khachHang.getTrangThai().equals("hoatDong")) {
                       HoiMuonKhoaKhong(khachHang);
                    } else {
                        HoiMuonMoKhoaKhong(khachHang);
                    }
                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ChiTietKhachHang.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("khachHang", khachHang);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }
    }

        public void HoiMuonKhoaKhong(KhachHang khachHang){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("sửa trang thái tài khoản")
                    .setMessage("Bạn có muốn khóa hay không")
                    .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            daoKhachHang.suaTrangThai(khachHang.getKhachHangID(),"khoa");
                            Toast.makeText(context, "Khóa thành công", Toast.LENGTH_SHORT).show();
                        }
                    }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    public void HoiMuonMoKhoaKhong(KhachHang khachHang){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("sửa trang thái tài khoản")
                .setMessage("Bạn có muốn mở khóa hay không")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        daoKhachHang.suaTrangThai(khachHang.getKhachHangID(),"hoatDong");
                        Toast.makeText(context, "Mở thành công", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public int getItemCount() {
        return ds.size();
    }

    public class adapterKhachHangView extends RecyclerView.ViewHolder {
        TextView tvTen, tvEmail, tvsdt, tvGioiTinh, tvNamSinh, tvTrangThai;
        ImageView imgKhachHang;
        Button btnDongMo;

        public adapterKhachHangView(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvTenKhachHangItem);
            tvEmail = itemView.findViewById(R.id.tvEmailKhachHangItem);
            tvsdt = itemView.findViewById(R.id.tvSdtKhachHangItem);
            tvGioiTinh = itemView.findViewById(R.id.tvGioiTinhKhachHangItem);
            tvNamSinh = itemView.findViewById(R.id.tvNamSinhKhachHangItem);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThaiKhachHangItem);

            imgKhachHang = itemView.findViewById(R.id.imgKhachHangItem);
            btnDongMo = itemView.findViewById(R.id.btnDongMoKhachHang);
        }
    }
}
