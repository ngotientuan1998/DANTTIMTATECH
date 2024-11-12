package frament;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.appduan1.R;


public class chiTietSanPham extends Fragment {
private ImageView img_thoatChiTietSP,img_anhSanPham;
private Button btnChayQC;
private TextView tvtensp_ctsp,tvmaSp,giamGia,gia,danhGia,luotMua,soLuong;
private EditText edt_chiTiet;
private RecyclerView rey_ds_ct_sp,ryc_DSdanhGia;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_chi_tiet_san_pham, container, false);
        Gan_ID(view);
        return view;
    }
    private  void  Gan_ID(View view){
        img_thoatChiTietSP=view.findViewById(R.id.img_thoatChiTietSP);
        img_anhSanPham=view.findViewById(R.id.img_anhSanPham);
        btnChayQC=view.findViewById(R.id.btnChayQC);
        tvtensp_ctsp=view.findViewById(R.id.Tvtensp_ctsp);
        tvmaSp=view.findViewById(R.id.Tvmasp_ctsp);
        giamGia=view.findViewById(R.id.TvgiamGiasp_ctsp);
        gia=view.findViewById(R.id.TvGiasp_ctsp);
        danhGia=view.findViewById(R.id.TvDanhGiasp_ctsp);
        luotMua=view.findViewById(R.id.TvluotMuasp_ctsp);
        soLuong=view.findViewById(R.id.TvcoSansp_ctsp);
        edt_chiTiet=view.findViewById(R.id.edt_ChiTietSanPham_ctsp);
        rey_ds_ct_sp=view.findViewById(R.id.ryc_ds_ct_sp);
        ryc_DSdanhGia=view.findViewById(R.id.ryc_DSdanhGia);

    }

}