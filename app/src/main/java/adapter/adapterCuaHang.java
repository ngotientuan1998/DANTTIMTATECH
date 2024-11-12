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
import com.example.appduan1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import DAO.DaoCuaHang;
import DTO.CuaHang;
import DTO.KhachHang;
import interfaces.cuaHangInterface;

public class adapterCuaHang extends RecyclerView.Adapter<adapterCuaHang.adapterCuaHangView>{
    List<CuaHang> ds;
    Context context;
    DaoCuaHang daoCuaHang;



    public adapterCuaHang(List<CuaHang> ds, Context context) {
        this.ds = ds;
        this.context = context;
        daoCuaHang=new DaoCuaHang(context);
    }

    @NonNull
    @Override
    public adapterCuaHangView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cua_hang_layout, parent, false);
        return  new adapterCuaHangView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterCuaHangView holder, int position) {
        CuaHang cuaHang=ds.get(position);
        holder.tvTen.setText("Tên:"+cuaHang.getTenCuaHang());
        holder.tvEmail.setText("Email:"+cuaHang.getEmail());
        holder.tvSdt.setText("sđt:"+String.valueOf(cuaHang.getSdt()));
        holder.tvTrangThai.setText("Trạng thái:"+cuaHang.getTrangThai());
        holder.tvTheoDoi.setText("Theo dõi:"+String.valueOf(cuaHang.getTheoDoi()));
        if(cuaHang.getHinhAnh()!=null&&!cuaHang.getHinhAnh().isEmpty()){
            Picasso.get().load(cuaHang.getHinhAnh()).into(holder.imAnh);
        }
        if(cuaHang.getTrangThai().equals("hoatDong")){
            holder.btnTrangThai.setText("Khóa");
            holder.btnTrangThai.setBackgroundColor(Color.RED);
            holder.tvTrangThai.setTextColor(Color.GREEN);
        } else {
            holder.btnTrangThai.setText("Mở");
            holder.btnTrangThai.setBackgroundColor(Color.GREEN);
            holder.tvTrangThai.setTextColor(Color.RED);
        }
        holder.btnTrangThai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cuaHang.getTrangThai().equals("hoatDong")){
                    HoiMuonKhoaKhong(cuaHang);
                } else {
                    HoiMuonMoKhoaKhong(cuaHang);
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, ChiTietCuaHang.class  );
                Bundle bundle=new Bundle();
                bundle.putSerializable("cuaHang",cuaHang);
                intent.putExtras(bundle);
               context.startActivity(intent);
            }
        });
    }

    public void HoiMuonKhoaKhong(CuaHang cuaHang){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("sửa trang thái tài khoản")
                .setMessage("Bạn có muốn khóa hay không")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        daoCuaHang.suaTrangThai(cuaHang.getCuaHangID(),"khoa");
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

    public void HoiMuonMoKhoaKhong(CuaHang cuaHang){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("sửa trang thái tài khoản")
                .setMessage("Bạn có muốn mở khóa hay không")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        daoCuaHang.suaTrangThai(cuaHang.getCuaHangID(),"hoatDong");
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

    public  class adapterCuaHangView extends RecyclerView.ViewHolder{
        TextView tvTen,tvSdt,tvEmail,tvTrangThai,tvTheoDoi;
        ImageView imAnh;
        Button btnTrangThai;
        public adapterCuaHangView(@NonNull View itemView) {
            super(itemView);
            tvTen=itemView.findViewById(R.id.tvTenCuaHangShopItem);
            tvSdt=itemView.findViewById(R.id.tvSdtShopItem);
            tvEmail=itemView.findViewById(R.id.tvEmailShopItem);
            tvTrangThai=itemView.findViewById(R.id.tvTrangThaiShopItem);
            tvTheoDoi=itemView.findViewById(R.id.tvTheoDoiShopItem);
            imAnh=itemView.findViewById(R.id.imgShopItem);
            btnTrangThai=itemView.findViewById(R.id.btnDongMoShop);
        }
    }

}
