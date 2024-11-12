package framentbottom;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appduan1.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import DAO.DaoDonHang;
import DTO.DoanhThu;
import DTO.DonHang;


public class doanhThuFragment extends Fragment {
    BarChart barChart;
    List<DoanhThu> ds;
    private String ID, role;
    private SharedPreferences sharedPreferences;
    List<DonHang> ds_DonHang;
    DaoDonHang daoDonHang;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doanh_thu, container, false);
        Gan_ID(view);
        SuKienOnClick();
        return view;
    }

    private void Gan_ID(View view) {
        barChart = view.findViewById(R.id.barChart);
        sharedPreferences = getActivity().getSharedPreferences("taiKhoan", getActivity().MODE_PRIVATE);
        ID = sharedPreferences.getString("IDuser", "");
        role = sharedPreferences.getString("role", "");
        daoDonHang = new DaoDonHang(getContext());
        ds_DonHang = new ArrayList<>();
        if (role.equals("Cửa hàng")) {
            daoDonHang.layDanhSachDonHangTheoCuaHang(ID, new DaoDonHang.TraVeDonHangTimDc() {
                @Override
                public void onTimDcDonHang(List<DonHang> donHangs) {
                    CapNhapDuLieuBanDo(donHangs);
                }

                @Override
                public void onKoTimDcDonHang(String errorMessage) {

                }
            });
        } else {
            daoDonHang.layDanhSachDonHangTheoKhacHang(ID, new DaoDonHang.TraVeDonHangTimDc() {
                @Override
                public void onTimDcDonHang(List<DonHang> donHangs) {
                    CapNhapDuLieuBanDo(donHangs);
                }

                @Override
                public void onKoTimDcDonHang(String errorMessage) {

                }
            });
        }

    }

    private  void CapNhapDuLieuBanDo(List<DonHang> DanhSachDonHang){
        barChart.getAxisRight().setDrawLabels(false);
        ds = new ArrayList<>();
        ds.add(new DoanhThu("T1", 50));
        ds.add(new DoanhThu("T2", 45));
        ds.add(new DoanhThu("T3", 78));
        ds.add(new DoanhThu("T4", 90));
        ds.add(new DoanhThu("T5", 0));
        ds.add(new DoanhThu("T", 0));
        ds.add(new DoanhThu("T7", 0));
        ds.add(new DoanhThu("T8", 0));
        ds.add(new DoanhThu("T9", 0));
        ds.add(new DoanhThu("T10", 0));
        ds.add(new DoanhThu("T11", 0));
        ds.add(new DoanhThu("T12", 0));
        if (DanhSachDonHang != null) {
            for (DonHang x : DanhSachDonHang) {
                String dateString = x.getNgayNhan();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(dateString, formatter);
                int month = localDate.getMonthValue();
                double TienDon = x.getGia();
                for (DoanhThu doanhThu : ds) {
                    if (doanhThu.getThang().equals("T" + month)) {
                        doanhThu.setTien(doanhThu.getTien() + TienDon);
                        Log.d("Doanh thu", "" + doanhThu.getTien());
                    } else {
                        Log.d("Không giống", "Không giống");
                    }
                }

            }
        }

        List<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < ds.size(); i++) {
            barEntries.add(new BarEntry(i, (float) ds.get(i).getTien()));
        }

        YAxis yAxis = barChart.getAxisLeft();
        yAxis.setAxisMaximum(0f);
        yAxis.setAxisMaximum(1000000000f);
        yAxis.setAxisLineWidth(2f);
        yAxis.setAxisLineColor(Color.BLACK);
        yAxis.setLabelCount(10);

        BarDataSet barDataSet = new BarDataSet(barEntries, "biểu đồ");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);

        barChart.getDescription().setEnabled(false);
        barChart.invalidate();
        List<String> dsThang = new ArrayList<>();
        if (dsThang.isEmpty()) {
            for (DoanhThu x : ds) {
                dsThang.add(x.getThang());
            }

        }

        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(dsThang));
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setLabelCount(12);
    }


    private void SuKienOnClick() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DiaLongThoatApp();
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
}