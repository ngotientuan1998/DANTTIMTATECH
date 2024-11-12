package adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appduan1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import DAO.DaoCuaHang;
import DAO.DaoSanPham;

public class adapterAnhSanPham extends RecyclerView.Adapter<adapterAnhSanPham.AdapterAnhSanPhamView> {
    Context context;
    List<String> dsAnh;
    DaoSanPham daoSanPham;
    String TrangThaiXoaAnh;
    String IDcuaHang,IDSanPham;


    public adapterAnhSanPham(Context context, List<String> dsAnh,String trangThaiXoaAnh) {
        this.context = context;
        this.dsAnh = dsAnh;
        this.daoSanPham=new DaoSanPham(context);
        this.TrangThaiXoaAnh=trangThaiXoaAnh;
    }

    public adapterAnhSanPham(Context context, List<String> dsAnh, String trangThaiXoaAnh, String IDcuaHang, String IDSanPham) {
        this.context = context;
        this.dsAnh = dsAnh;
        this.daoSanPham = new DaoSanPham(context);
       this.TrangThaiXoaAnh = trangThaiXoaAnh;
        this.IDcuaHang = IDcuaHang;
        this.IDSanPham = IDSanPham;
        this.daoSanPham=new DaoSanPham(context);
    }

    @NonNull
    @Override
    public AdapterAnhSanPhamView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anh_san_pham, parent, false);
        return new AdapterAnhSanPhamView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterAnhSanPhamView holder, int position) {
        String imgurl = dsAnh.get(position);
        if(TrangThaiXoaAnh.equals("ChiTiet")){
            holder.imgXoaAnh.setVisibility(View.GONE);
        } else {
            holder.imgXoaAnh.setVisibility(View.VISIBLE);
        }
       if(imgurl!=null){
           Picasso.get().load(imgurl).into(holder.imgAnhSanPham);
       }
        holder.imgXoaAnh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TrangThaiXoaAnh.equals("ThemSanPham")){
                    daoSanPham.xoaHinhAnhFirebaseStorage(imgurl);
                    dsAnh.remove(position);
                    notifyItemRemoved(position);
                } else {
                    daoSanPham.xoaHinhAnhFirebaseStorage(imgurl);
                    daoSanPham.xoaHinhAnhSanPhamSua(IDcuaHang,IDcuaHang,IDSanPham);
                    dsAnh.remove(position);
                    notifyItemRemoved(position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return dsAnh.size();
    }

    public class AdapterAnhSanPhamView extends RecyclerView.ViewHolder {
        ImageView imgAnhSanPham,imgXoaAnh;

        public AdapterAnhSanPhamView(@NonNull View itemView) {
            super(itemView);
            imgAnhSanPham = itemView.findViewById(R.id.imgAnhItemSP);
            imgXoaAnh=itemView.findViewById(R.id.imgXoaAnh);
        }
    }

}
