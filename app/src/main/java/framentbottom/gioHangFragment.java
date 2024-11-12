package framentbottom;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appduan1.R;
import com.example.appduan1.thanhToan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import DAO.DaoGioHang;
import DAO.Daoformat;
import DTO.gioHang;
import adapter.adapterGioHang;
import interfaces.chkGiaChonInterface;
import interfaces.giaChointerface;

public class gioHangFragment extends Fragment implements giaChointerface, chkGiaChonInterface {

    ImageView imgThoat;
    RecyclerView ryc_DsSpTRongGio;
    CheckBox chkChonTatCa;
    Button btnMua;
    TextView TvTongGia;
    adapterGioHang adapterGioHangs;
    DaoGioHang daoGioHang;
    Daoformat daoformat;
    String ID;
    List<gioHang> ds_SPtrongGio=new ArrayList<>();
    SharedPreferences sharedPreferences;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gio_hang, container, false);
        Gan_ID(view);
        SuKienClick();
        return view;
    }
    public  void Gan_ID(View view){
        imgThoat=view.findViewById(R.id.imgThoatGioHangGH);
        ryc_DsSpTRongGio=view.findViewById(R.id.ryc_RoHangGH);
        chkChonTatCa=view.findViewById(R.id.chkTatCaHangGH);
        btnMua=view.findViewById(R.id.btnMuaHangGH);
        TvTongGia=view.findViewById(R.id.TvTongThanhToanGH);

        sharedPreferences = getActivity().getSharedPreferences("taiKhoan", Context.MODE_PRIVATE);
        ID = sharedPreferences.getString("IDuser", "");

        daoGioHang=new DaoGioHang(getContext());
        daoformat=new Daoformat();
       daoGioHang.layGioHangTheoIDKhachHangVaDanhTinhThemVaoGio(ID, new DaoGioHang.OnGioHangLoadListener() {
           @Override
           public void onGioHangLoadSuccess(List<gioHang> gioHangList) {
               ds_SPtrongGio=gioHangList;
               adapterGioHangs=new adapterGioHang(getContext(),gioHangList,"MHgioHang",gioHangFragment.this,gioHangFragment.this);
               LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
               ryc_DsSpTRongGio.setAdapter(adapterGioHangs);
               ryc_DsSpTRongGio.setLayoutManager(linearLayoutManager);
           }

           @Override
           public void onGioHangLoadFailure(Exception e) {

           }
       });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        daoGioHang.layGioHangTheoIDKhachHang(ID, new DaoGioHang.OnGioHangLoadListener() {
            @Override
            public void onGioHangLoadSuccess(List<gioHang> gioHangList) {
                for (gioHang x:gioHangList){
                    x.setTrangThaiChon("ko chon");
                    daoGioHang.capNhatGioHang(x);
                }
            }

            @Override
            public void onGioHangLoadFailure(Exception e) {

            }
        });
    }





    private void SuKienClick(){
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DiaLongThoatApp();
            }
        });
        chkChonTatCa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(chkChonTatCa.isChecked()){
                    adapterGioHangs.ChonBoTatCa(true);
                } else {
                    adapterGioHangs.ChonBoTatCa(false);
                    TvTongGia.setText("0");
                }

            }
        });
        btnMua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(ds_SPtrongGio.isEmpty()){
                   Toast.makeText(getActivity(), "Giỏ hàng trống", Toast.LENGTH_SHORT).show();
               } else {
                   int soLuongPT=ds_SPtrongGio.size();
                   int checkTrangThaiSP=0;
                   for (gioHang x:ds_SPtrongGio){
                       if(x.getTrangThaiChon().equals("ko chon")){
                         checkTrangThaiSP++;
                       }
                   }
                   if(checkTrangThaiSP==soLuongPT){
                       Toast.makeText(getActivity(), "Vui lòng chọn sản phẩm ", Toast.LENGTH_SHORT).show();
                   } else {
                       Intent intent=new Intent(getActivity(), thanhToan.class);
                       startActivity(intent);

                   }
               }
            }
        });
        imgThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });
    }
    private void DiaLongThoatApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setTitle("Xác nhận thoát");
        builder.setMessage("Bạn có muốn thoát khỏi ứng dụng?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                requireActivity().finish();
            }
        });
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Đóng dialog và không làm gì cả
                dialog.dismiss();
            }
        });
        builder.show();
    }

    @Override
    public void GiaPhaiTra(gioHang x) {

        double tongGia=(x.getGiaSp()-x.getGiaGiamSp())*x.getSoLuongMua();
        Log.d("soLuongMua",""+x.getSoLuongMua());
        TvTongGia.setText("Tổng thanh toán:"+daoformat.chuyenDinhDang(tongGia));
    }

    @Override
    public void dsSanPhamChon(Map<String, gioHang> ds) {
        double  tongGia=0;
        for (String key : ds.keySet()) {
            gioHang x = ds.get(key);
            tongGia  +=(x.getGiaSp()-x.getGiaGiamSp())*x.getSoLuongMua();
            Log.d("giá sản phẩm:",""+x.getGiaSp());
            Log.d("giảm giá sản phẩm:",""+x.getGiaGiamSp());
            Log.d("số lượng mua:",""+x.getSoLuongMua());
            Log.d("Tong giá được chọn:","Tổng gia:"+tongGia);
        }
        TvTongGia.setText("Tổng thanh toán:"+daoformat.chuyenDinhDang(tongGia));

    }
}