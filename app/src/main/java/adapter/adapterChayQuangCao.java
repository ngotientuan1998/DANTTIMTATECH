package adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.appduan1.ChayQuangCao;
import com.example.appduan1.ChiTietSanPham;
import com.example.appduan1.R;
import com.google.firebase.database.collection.LLRBNode;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import DAO.DaoChayQuangCao;
import DAO.DaoCuaHang;
import DAO.DaoSanPham;
import DTO.CuaHang;
import DTO.KhachHang;
import DTO.QuangCao;
import DTO.SanPham;

public class adapterChayQuangCao extends RecyclerView.Adapter<adapterChayQuangCao.adapterChayQuangCaoView> {
    List<QuangCao> ds_QC;
    Context context;
    String role;
    SharedPreferences sharedPreferences;
    DaoChayQuangCao daoChayQuangCao;
    DaoSanPham daoSanPham;
    DaoCuaHang daoCuaHang;

    public adapterChayQuangCao(List<QuangCao> ds_QC, Context context) {
        this.ds_QC = ds_QC;
        this.context = context;
        sharedPreferences = context.getSharedPreferences("taiKhoan", Context.MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");
        daoChayQuangCao = new DaoChayQuangCao(context);
        daoSanPham = new DaoSanPham(context);
        daoCuaHang=new DaoCuaHang(context);
    }

    @NonNull
    @Override
    public adapterChayQuangCaoView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chay_quang_cao, parent, false);
        return new adapterChayQuangCaoView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapterChayQuangCaoView holder, int position) {
        if (role.equals("Cửa hàng")) {
            holder.btn_Duyet.setVisibility(View.GONE);
        }
        QuangCao chayQuangCao = ds_QC.get(position);
        if (chayQuangCao != null) {
            holder.tvIDQC.setText("Mã QC:"+chayQuangCao.getIDQC());
            holder.tvIDCH.setText("ID cửa hàng:"+chayQuangCao.getIDcuaHang());
            holder.tvTenCH.setText("Tên cửa hàng:"+chayQuangCao.getTenCuaHang());
            holder.tvIDSP.setText("ID SP:"+chayQuangCao.getIDsanPham());
            holder.tvTenSP.setText("Tên SP:"+chayQuangCao.getTenSanPham());
            holder.tvCapDo.setText("Cấp độ:"+String.valueOf(chayQuangCao.getCapDo()));
            holder.tvTrangThai.setText("Trạng thái:"+chayQuangCao.getTrangThai());
            if(chayQuangCao.getTrangThai().equals("Đợi")){
                holder.tvTrangThai.setTextColor(Color.RED);
                holder.btn_Duyet.setText("Duyệt");
                holder.btn_Duyet.setBackgroundColor(Color.GREEN);
            } else {
                holder.tvTrangThai.setTextColor(Color.GREEN);
                holder.tvTrangThai.setText("Đang chạy");
                holder.btn_Duyet.setText("Ngừng");
                holder.btn_Duyet.setBackgroundColor(Color.RED);
            }
            holder.btn_Duyet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(chayQuangCao.getTrangThai().equals("Đợi")){
                        HoiMonXacNhanChayQuangCaoKhong(chayQuangCao,position);
                    } else {
                        HoiMonXacNhanNgungChayQuangCaoKhong(chayQuangCao,position);
                    }
                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    daoSanPham.Lay1SanPham(chayQuangCao.getIDcuaHang(), chayQuangCao.getIDsanPham(), new DaoSanPham.TraVe1sanPham() {
                        @Override
                        public void onSanPhamLoaded(SanPham sanPham) {
                            Intent intent = new Intent(context, ChiTietSanPham.class);
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("sanPham", sanPham);
                            daoCuaHang.TimCuaHang(sanPham.getIDcuaHang(), new DaoCuaHang.TraVeCuaHangTimDc() {
                                @Override
                                public void onTimDcCuaHang(CuaHang cuaHang) {
                                    bundle.putSerializable("cuaHang", cuaHang);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                }

                                @Override
                                public void onKoTimCuaHang(String errorMessage) {

                                }
                            });
                        }

                        @Override
                        public void onSanPhamLoadFailed() {

                        }
                    });

                }
            });
        }
    }
    public void HoiMonXacNhanChayQuangCaoKhong(QuangCao chayQuangCao,int position ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Duyệt đơn chạy quảng cáo ")
                .setMessage("Bạn có muốn chạy quảng cáo hay không")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Calendar calendar = Calendar.getInstance();
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH) + 1; // Tháng trong Calendar là từ 0 đến 11
                        int day = calendar.get(Calendar.DAY_OF_MONTH);
                        String ngayThangNam = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
                        chayQuangCao.setTimeDuyet(ngayThangNam);
                        chayQuangCao.setTimeConLai("30day");
                        chayQuangCao.setTrangThai("Đang chạy");
                        daoChayQuangCao.capNhatQuangCao( chayQuangCao);


                            daoSanPham.Lay1SanPham(chayQuangCao.getIDcuaHang(), chayQuangCao.getIDsanPham(), new DaoSanPham.TraVe1sanPham() {
                                @Override
                                public void onSanPhamLoaded(SanPham sanPham) {
                                    int diem=0;
                                    if (chayQuangCao.getCapDo() == 1) {
                                        diem = 5;
                                    } else {
                                        diem = 16;
                                    }
                                    if (diem > 0) {
                                        daoSanPham.capNhatDiemDeXuatSanPham(chayQuangCao.getIDcuaHang(), chayQuangCao.getIDsanPham(),sanPham.getDiemChayQuangCao()+diem);
                                        Toast.makeText(context, "Bắt đầu chạy", Toast.LENGTH_SHORT).show();
                                    }

                                }
                                @Override
                                public void onSanPhamLoadFailed() {

                                }
                            });


                    }
                }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        notifyItemChanged(position);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void HoiMonXacNhanNgungChayQuangCaoKhong(QuangCao chayQuangCao,int position ){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Ngừng đơn chạy quảng cáo ")
                .setMessage("Bạn có muốn ngừng chạy quảng cáo hay không")
                .setPositiveButton("Có", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        daoChayQuangCao.xoaQuangCao(chayQuangCao.getIDcuaHang(), chayQuangCao.getIDsanPham());
                        daoSanPham.Lay1SanPham(chayQuangCao.getIDcuaHang(), chayQuangCao.getIDsanPham(), new DaoSanPham.TraVe1sanPham() {
                            @Override
                            public void onSanPhamLoaded(SanPham sanPham) {
                                int diem=0;
                                if (chayQuangCao.getCapDo() == 1) {
                                    diem = 5;
                                } else {
                                    diem = 16;
                                }
                                if (diem > 0) {
                                    daoSanPham.capNhatDiemDeXuatSanPham(chayQuangCao.getIDcuaHang(), chayQuangCao.getIDsanPham(),0);
                                    Toast.makeText(context, "Xóa điểm chạy thành công", Toast.LENGTH_SHORT).show();
                                }

                            }
                            @Override
                            public void onSanPhamLoadFailed() {

                            }
                        });
                    }
                }).setNegativeButton("Không", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        notifyItemChanged(position);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    @Override
    public int getItemCount() {
        return ds_QC.size();
    }

    public class adapterChayQuangCaoView extends RecyclerView.ViewHolder {
        TextView tvIDQC, tvIDCH, tvIDSP, tvCapDo, tvTrangThai,tvTenSP,tvTenCH;
        Button btn_Duyet;

        public adapterChayQuangCaoView(@NonNull View itemView) {
            super(itemView);
            tvIDQC = itemView.findViewById(R.id.tvIDQCItem);
            tvIDCH = itemView.findViewById(R.id.tvIDcuaHangQCItem);
            tvIDSP = itemView.findViewById(R.id.tvIDsanPhamQCItem);
            tvCapDo = itemView.findViewById(R.id.tvCapDoQCItem);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThaiQCItem);
            tvTenSP=itemView.findViewById(R.id.tvTensanPhamQCItem);
            tvTenCH=itemView.findViewById(R.id.tvTencuaHangQCItem);
            btn_Duyet = itemView.findViewById(R.id.btnDuyetQCItem);
        }
    }
}
