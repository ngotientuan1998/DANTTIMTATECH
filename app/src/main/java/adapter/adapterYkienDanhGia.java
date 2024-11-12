package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appduan1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import DTO.DanhGia;

public class adapterYkienDanhGia extends RecyclerView.Adapter<adapterYkienDanhGia.adapterYkienDanhGiaView>{
    List<DanhGia> ds_DanhGia;
    Context context;

    public adapterYkienDanhGia(List<DanhGia> ds_DanhGia, Context context) {
        this.ds_DanhGia = ds_DanhGia;
        this.context = context;
    }

    @NonNull
    @Override
    public adapterYkienDanhGiaView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_danh_gia, parent, false);
        return new adapterYkienDanhGiaView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterYkienDanhGiaView holder, int position) {
        DanhGia danhGia=ds_DanhGia.get(position);
        if(danhGia!=null){
        holder.tvTenKhach.setText(danhGia.getTenKhach());
        holder.tvYkienDanhGia.setText(danhGia.getYkienDongGop());
      if(danhGia.getImgAnhKhachHang()!=null){
          Picasso.get().load(danhGia.getImgAnhKhachHang()).into(holder.imgAnhKhach);
      }
          if(danhGia.getSaoDanhGia()==1){
              holder.imgAnhSao.setImageResource(R.drawable.motsao);
          } else if (danhGia.getSaoDanhGia()==2) {
              holder.imgAnhSao.setImageResource(R.drawable.haisao);
          } else if (danhGia.getSaoDanhGia()==3) {
              holder.imgAnhSao.setImageResource(R.drawable.basao);
          } else if (danhGia.getSaoDanhGia()==4) {
              holder.imgAnhSao.setImageResource(R.drawable.bonsao);
          } else {
              holder.imgAnhSao.setImageResource(R.drawable.namsao);
          }
            adapterAnhYkienDanhGia adapterAnhYkienDanhGias=new adapterAnhYkienDanhGia(danhGia.getHinhAnhSanPhamDaMua(),context);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, RecyclerView.HORIZONTAL, false);
            holder.ryc_AnhSanPham.setLayoutManager(linearLayoutManager);
            holder.ryc_AnhSanPham.setAdapter(adapterAnhYkienDanhGias);
        }

    }

    @Override
    public int getItemCount() {
        return ds_DanhGia.size();
    }

    public class adapterYkienDanhGiaView extends RecyclerView.ViewHolder {
        TextView tvTenKhach, tvYkienDanhGia;
        ImageView imgAnhKhach,imgAnhSao;
        RecyclerView ryc_AnhSanPham;

        public adapterYkienDanhGiaView(@NonNull View itemView) {
            super(itemView);
            tvTenKhach = itemView.findViewById(R.id.tvTenKhachDG);
            tvYkienDanhGia = itemView.findViewById(R.id.tvYKienDongGopDG);
            imgAnhKhach = itemView.findViewById(R.id.img_AnhKhachDG);
            imgAnhSao = itemView.findViewById(R.id.img_saoDG);
            ryc_AnhSanPham = itemView.findViewById(R.id.rycDsAnhspDG);
        }
    }
}
