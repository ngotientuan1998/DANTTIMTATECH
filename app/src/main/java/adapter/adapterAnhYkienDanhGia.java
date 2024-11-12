package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appduan1.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import DTO.DanhGia;

public class adapterAnhYkienDanhGia extends RecyclerView.Adapter<adapterAnhYkienDanhGia.adapterAnhYkienDanhGiaView>{
List<String> danhSachAnh;
Context  context;

    public adapterAnhYkienDanhGia( List<String> danhSachAnh, Context context) {
        this.context = context;
        this.danhSachAnh = danhSachAnh;
    }

    @NonNull
    @Override
    public adapterAnhYkienDanhGiaView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_anh_y_kien_dg, parent, false);
        return new adapterAnhYkienDanhGiaView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterAnhYkienDanhGiaView holder, int position) {
         if(danhSachAnh!=null&&!danhSachAnh.isEmpty()){
             String imgurl=danhSachAnh.get(position);
             if(imgurl!=null){
                 Picasso.get().load(imgurl).into(holder.img_Anh);
             }
         }
    }

    @Override
    public int getItemCount() {
        return danhSachAnh.size();
    }

    public class adapterAnhYkienDanhGiaView extends RecyclerView.ViewHolder {
        ImageView img_Anh;

        public adapterAnhYkienDanhGiaView(@NonNull View itemView) {
            super(itemView);
            img_Anh=itemView.findViewById(R.id.img_anhSanPhamTuDanhGia);

        }
    }
}
