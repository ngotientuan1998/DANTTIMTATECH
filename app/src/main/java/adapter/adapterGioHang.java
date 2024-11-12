package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.appduan1.ChiTietCuaHang;
import com.example.appduan1.ChiTietSanPham;
import com.example.appduan1.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DAO.DaoCuaHang;
import DAO.DaoGioHang;
import DAO.DaoSanPham;
import DAO.Daoformat;
import DTO.CuaHang;
import DTO.SanPham;
import DTO.gioHang;
import interfaces.chkGiaChonInterface;
import interfaces.giaChointerface;

public class adapterGioHang extends RecyclerView.Adapter<adapterGioHang.adapterGioHangView> {
    Context context;
    List<gioHang> ds_gioHang;
    DaoCuaHang daoCuaHang;
    DaoGioHang daoGioHang;
    DaoSanPham daoSanPham;
    String tranthaiDC="dc chon",tranthaiKoDC="ko chon";
    boolean checkChonTatCa=false;
    giaChointerface chointerface;
    chkGiaChonInterface chkGiaChonInterfaces;
    Map<String, gioHang> ds_SpChon = new HashMap<>();
    String maHinh;
    Daoformat daoformat;


    public adapterGioHang(Context context, List<gioHang> ds_gioHang,String manHinh,giaChointerface chointerface, chkGiaChonInterface chkGiaChonInterfaces) {
        this.context = context;
        this.ds_gioHang = ds_gioHang;
        this.chointerface = chointerface;
        daoCuaHang = new DaoCuaHang(context);
        daoGioHang = new DaoGioHang(context);
        daoSanPham=new DaoSanPham(context);
        this.chkGiaChonInterfaces = chkGiaChonInterfaces;
        this.maHinh=manHinh;
        daoformat=new Daoformat();
    }

    public adapterGioHang(Context context, List<gioHang> ds_gioHang,String manHinh) {
        this.context = context;
        this.ds_gioHang = ds_gioHang;
        this.maHinh=manHinh;
        daoCuaHang = new DaoCuaHang(context);
        daoGioHang = new DaoGioHang(context);
        daoSanPham=new DaoSanPham(context);
        daoformat=new Daoformat();
    }

    public void ChonBoTatCa(boolean chon) {
        this.checkChonTatCa = chon;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public adapterGioHangView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gio_hang, parent, false);
        return new adapterGioHangView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterGioHangView holder, int position) {
        gioHang gioHangs = ds_gioHang.get(position);
        if(checkChonTatCa){
            holder.chkChonSp.setChecked(true);
            gioHangs.setTrangThaiChon("dc chon");
            daoGioHang.capNhatSlMuaGioHang(gioHangs, new DaoGioHang.CapNhapSoLuongMuaGioHang() {
                @Override
                public void CapNhapThanhCong(String tc) {
                    ds_SpChon.put(gioHangs.getIDgioHang(), gioHangs);
                    if(chkGiaChonInterfaces!=null){
                        chkGiaChonInterfaces.dsSanPhamChon(ds_SpChon);
                    }
                }

                @Override
                public void CapNhapThatBai(String tb) {

                }
            });
        } else {
            holder.chkChonSp.setChecked(false);
            gioHangs.setTrangThaiChon("ko chon");
            daoGioHang.capNhatSlMuaGioHang(gioHangs, new DaoGioHang.CapNhapSoLuongMuaGioHang() {
                @Override
                public void CapNhapThanhCong(String tc) {
                    ds_SpChon.remove(gioHangs.getIDgioHang());
                   if(chkGiaChonInterfaces!=null){
                       chkGiaChonInterfaces.dsSanPhamChon(ds_SpChon);
                   }
                }

                @Override
                public void CapNhapThatBai(String tb) {

                }
            });


        }

        if(maHinh.equals("MHgioHang")){
            holder.tvSoLuong.setVisibility(View.GONE);
            holder.TvTongTienCacSP.setVisibility(View.GONE);
        } else if (maHinh.equals("MHchiTietDonHang")) {
            holder.tvSoLuong.setVisibility(View.VISIBLE);
            holder.TvTongTienCacSP.setVisibility(View.VISIBLE);
            holder.imgCong.setVisibility(View.GONE);
            holder.imgTru.setVisibility(View.GONE);
            holder.edt_soLuongSp.setVisibility(View.GONE);
            holder.chkChonSp.setVisibility(View.GONE);
        } else {
            holder.tvSoLuong.setVisibility(View.VISIBLE);
            holder.TvTongTienCacSP.setVisibility(View.VISIBLE);
            holder.imgCong.setVisibility(View.GONE);
            holder.imgTru.setVisibility(View.GONE);
            holder.edt_soLuongSp.setVisibility(View.GONE);
            holder.chkChonSp.setVisibility(View.GONE);
        }
        if (gioHangs != null) {
            if (gioHangs.getHinhAnhSanPham() != null) {
                Picasso.get().load(gioHangs.getHinhAnhSanPham()).into(holder.imAnhSP);
                holder.tvTenCH.setText("Tên cửa hàng:" + gioHangs.getTenCuaHang());
                holder.tvTenSP.setText("Tên sản phẩm:" + gioHangs.getTenSanPham());
                holder.tvGiamGia.setText("Gíá giảm:" + daoformat.chuyenDinhDang(gioHangs.getGiaGiamSp()));
                holder.tvGia.setText("Giá:" + daoformat.chuyenDinhDang(gioHangs.getGiaSp()));
                holder.tvSoLuong.setText("Số lượng:"+gioHangs.getSoLuongMua());
                holder.TvTongTienCacSP.setText("Tổng tiền:"+daoformat.chuyenDinhDang((gioHangs.getGiaSp()-gioHangs.getGiaGiamSp())*gioHangs.getSoLuongMua()));

            }

        }
        daoGioHang.capNhatSlMuaGioHang(gioHangs, new DaoGioHang.CapNhapSoLuongMuaGioHang() {
            @Override
            public void CapNhapThanhCong(String tc) {
                if(chkGiaChonInterfaces!=null){
                    chkGiaChonInterfaces.dsSanPhamChon(ds_SpChon);
                }

            }

            @Override
            public void CapNhapThatBai(String tb) {

            }
        });
        holder.tvXemCuaHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                daoCuaHang.TimCuaHang(gioHangs.getIDcuaHang(), new DaoCuaHang.TraVeCuaHangTimDc() {
                    @Override
                    public void onTimDcCuaHang(CuaHang cuaHang) {
                        Intent intent = new Intent(context, ChiTietCuaHang.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("cuaHangCtSP", cuaHang);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }

                    @Override
                    public void onKoTimCuaHang(String errorMessage) {

                    }
                });
            }
        });
        holder.edt_soLuongSp.setText(String.valueOf(gioHangs.getSoLuongMua()));


//        holder.edt_soLuongSp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String textSoLuong=holder.edt_soLuongSp.getText().toString();
//                if(textSoLuong.equals("0")){
//                    holder.edt_soLuongSp.setText("1");
//                    xoaGioHang(gioHangs.getIDgioHang(),position);
//                } else if (holder.edt_soLuongSp.getText().toString().isEmpty()) {
//                    xoaGioHang(gioHangs.getIDgioHang(),position);
//                    holder.edt_soLuongSp.setText("1");
//                } else {
//                    holder.chkChonSp.setChecked(true);
//                    daoSanPham.Lay1SanPham(gioHangs.getTenCuaHang(), gioHangs.getIDsanPham(), new DaoSanPham.TraVe1sanPham() {
//
//                        @Override
//                        public void onSanPhamLoaded(SanPham sanPham) {
//                            if (Double.parseDouble(textSoLuong)>sanPham.getSoLuong()){
//                                Toast.makeText(context, "số lượng vượt quá số lượng sẵn có", Toast.LENGTH_SHORT).show();
//                            } else {
//                                gioHangs.setSoLuongMua(Integer.parseInt(holder.edt_soLuongSp.getText().toString()));
//                                gioHangs.setTrangThaiChon(tranthaiDC);
//                                ds_SpChon.put(gioHangs.getIDgioHang(), gioHangs);
//                                daoGioHang.capNhatSlMuaGioHang(gioHangs, new DaoGioHang.CapNhapSoLuongMuaGioHang() {
//                                    @Override
//                                    public void CapNhapThanhCong(String tc) {
//                                        if(chkGiaChonInterfaces!=null){
//                                            Toast.makeText(context, "được gọi", Toast.LENGTH_SHORT).show();
//                                            chkGiaChonInterfaces.dsSanPhamChon(ds_SpChon);
//                                        }
//
//                                    }
//
//                                    @Override
//                                    public void CapNhapThatBai(String tb) {
//
//                                    }
//                                });
//                            }
//                        }
//
//                        @Override
//                        public void onSanPhamLoadFailed() {
//
//                        }
//                    });
//
//                }
//            }
//        });
//        holder.edt_soLuongSp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                if(i== EditorInfo.IME_ACTION_DONE ){
//                    String duLieu=holder.edt_soLuongSp.getText().toString();
//                    if(duLieu.equals("")){
//                        xoaGioHang(gioHangs.getIDgioHang(),position);
//                    }
//                    return true;
//                }
//                return false;
//            }
//        });

       holder.imgCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gioHangs.setTrangThaiChon(tranthaiDC);
                int soLuongMua = Integer.parseInt(holder.edt_soLuongSp.getText().toString());
               if(!holder.chkChonSp.isChecked()){
                   holder.chkChonSp.setChecked(true);
                }
                    holder.edt_soLuongSp.setText(String.valueOf(soLuongMua + 1));
                    daoSanPham.Lay1SanPham(gioHangs.getIDcuaHang(), gioHangs.getIDsanPham(), new DaoSanPham.TraVe1sanPham() {
                        @Override
                        public void onSanPhamLoaded(SanPham sanPham) {
                            if(soLuongMua>sanPham.getSoLuong()){
                                Toast.makeText(context, "số lượng mua vượt quá số lượng sắn có", Toast.LENGTH_SHORT).show();
                            } else {
                                gioHangs.setSoLuongMua(Integer.parseInt(holder.edt_soLuongSp.getText().toString()));
                                ds_SpChon.put(gioHangs.getIDgioHang(), gioHangs);
                                daoGioHang.capNhatSlMuaGioHang(gioHangs, new DaoGioHang.CapNhapSoLuongMuaGioHang() {
                                    @Override
                                    public void CapNhapThanhCong(String tc) {
                                        if(chkGiaChonInterfaces!=null){
                                            chkGiaChonInterfaces.dsSanPhamChon(ds_SpChon);
                                        }

                                    }

                                    @Override
                                    public void CapNhapThatBai(String tb) {

                                    }
                                });
                            }
                        }

                        @Override
                        public void onSanPhamLoadFailed() {

                        }
                    });

            }
        });
        holder.imgTru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int soLuongMua = Integer.parseInt(holder.edt_soLuongSp.getText().toString());
                if (soLuongMua >1) {
                        soLuongMua=soLuongMua-1;
                        holder.edt_soLuongSp.setText(String.valueOf(soLuongMua));
                        gioHangs.setSoLuongMua(Integer.parseInt(holder.edt_soLuongSp.getText().toString()));
                        ds_SpChon.put(gioHangs.getIDgioHang(), gioHangs);
                        daoGioHang.capNhatSlMuaGioHang(gioHangs, new DaoGioHang.CapNhapSoLuongMuaGioHang() {
                            @Override
                            public void CapNhapThanhCong(String tc) {
                                if(chkGiaChonInterfaces!=null){
                                    chkGiaChonInterfaces.dsSanPhamChon(ds_SpChon);
                                }

                            }

                            @Override
                            public void CapNhapThatBai(String tb) {

                            }
                        });

                    }  else {
                    xoaGioHang(gioHangs.getIDgioHang(),position);
                }

            }
        });
        holder.chkChonSp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.chkChonSp.isChecked()) {
                    gioHangs.setSoLuongMua(Integer.parseInt(holder.edt_soLuongSp.getText().toString()));
                    gioHangs.setTrangThaiChon(tranthaiDC);
                    ds_SpChon.put(gioHangs.getIDgioHang(), gioHangs);
                    daoGioHang.capNhatSlMuaGioHang(gioHangs, new DaoGioHang.CapNhapSoLuongMuaGioHang() {
                        @Override
                        public void CapNhapThanhCong(String tc) {
                            if(chkGiaChonInterfaces!=null){
                                chkGiaChonInterfaces.dsSanPhamChon(ds_SpChon);
                            }
                        }

                        @Override
                        public void CapNhapThatBai(String tb) {

                        }
                    });

                } else {
                    gioHangs.setTrangThaiChon(tranthaiKoDC);
                    ds_SpChon.remove(gioHangs.getIDgioHang());
                    daoGioHang.capNhatSlMuaGioHang(gioHangs, new DaoGioHang.CapNhapSoLuongMuaGioHang() {
                        @Override
                        public void CapNhapThanhCong(String tc) {
                            if(chkGiaChonInterfaces!=null){
                                chkGiaChonInterfaces.dsSanPhamChon(ds_SpChon);
                            }
                        }

                        @Override
                        public void CapNhapThatBai(String tb) {

                        }
                    });

                }
            }
        });

    }


    public void xoaGioHang(String IDgioHang,int i) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("xóa sản phẩm khỏi giỏ");
        builder.setMessage("Bạn có muốn xóa khỏi giỏ hàng không?");
        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                daoGioHang.xoaGioHang(IDgioHang);
                ds_gioHang.remove(i);
                notifyItemRemoved(i);
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
    public int getItemCount() {
        if (ds_gioHang != null) {
            return ds_gioHang.size();
        } else {
            return 0; // hoặc một giá trị mặc định khác
        }

    }

    public class adapterGioHangView extends RecyclerView.ViewHolder {
        TextView tvTenCH, tvTenSP, tvGiamGia, tvGia, tvXemCuaHang,tvSoLuong,TvTongTienCacSP;
        ImageView imAnhSP, imgCong, imgTru;
        EditText edt_soLuongSp;
        CheckBox chkChonSp;

        public adapterGioHangView(@NonNull View itemView) {
            super(itemView);
            tvTenCH = itemView.findViewById(R.id.TvTenCuaHangGHItem);
            tvTenSP = itemView.findViewById(R.id.TvTenSpGHItem);
            tvGiamGia = itemView.findViewById(R.id.TvGiaGiamSpGHItem);
            tvGia = itemView.findViewById(R.id.TvGiaSpGHItem);
            tvXemCuaHang = itemView.findViewById(R.id.TvXemCuaHangGHItem);
            tvSoLuong=itemView.findViewById(R.id.tvsoLuongGHItem);
            TvTongTienCacSP=itemView.findViewById(R.id.tvTongTienSPGHItem);

            imAnhSP = itemView.findViewById(R.id.imgAnhSPGHItem);
            imgCong = itemView.findViewById(R.id.imgCongSPGHItem);
            imgTru = itemView.findViewById(R.id.imgTruSPGHItem);

            edt_soLuongSp = itemView.findViewById(R.id.edtSoLuongMuaSPGHItem);

            chkChonSp = itemView.findViewById(R.id.chkChonSPGHItem);
        }
    }
}
