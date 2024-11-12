package framentbottom;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appduan1.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import DAO.DaoDonHang;
import DTO.DonHang;
import adapter.adapterDonHang;


public class lichSuFragment extends Fragment {
    RecyclerView ryc_DsDonLichSu;
    Button btnNBDLichSu, btnNKTLichSu, btnTimKiemLS;
    TextView tvNgayBatDau, tvNgayKetThuc;
    private int selectedYear, selectedMonth, selectedDay;
    adapterDonHang adapterDonHangs;
    private String startDate, endDate;
    private String ID, role;
    private SharedPreferences sharedPreferences;
    List<DonHang> ds_DonHang;
    DaoDonHang daoDonHang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lich_su, container, false);
        Gan_ID(view);
        SuKienOnClick();
        return view;
    }

    private void Gan_ID(View view) {
        ryc_DsDonLichSu = view.findViewById(R.id.ryc_LichSuFrament);
        btnNBDLichSu = view.findViewById(R.id.btnNBDLichSu);
        btnNKTLichSu = view.findViewById(R.id.btnNKTLichSu);
        btnTimKiemLS = view.findViewById(R.id.btnTimKiemLichSu);
        tvNgayBatDau = view.findViewById(R.id.tvNgayBatDauLichSu);
        tvNgayKetThuc = view.findViewById(R.id.tvNgayKetThucLichSu);
        ds_DonHang = new ArrayList<>();
        daoDonHang = new DaoDonHang(getContext());

        sharedPreferences = getActivity().getSharedPreferences("taiKhoan", getActivity().MODE_PRIVATE);
        ID = sharedPreferences.getString("IDuser", "");
        role = sharedPreferences.getString("role", "");
        adapterDonHangs = new adapterDonHang(ds_DonHang, getContext(), ID, role, "LichSu");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        ryc_DsDonLichSu.setLayoutManager(linearLayoutManager);
        ryc_DsDonLichSu.setAdapter(adapterDonHangs);
        if (role.equals("Cửa hàng")) {
            daoDonHang.layDonHangTheoIDCHvaTrangThai(ID, "Nhan", ds_DonHang, adapterDonHangs);
        } else {
            daoDonHang.langNgheDonHangTheoIDKHvaTrangThai(ID, "Nhan", ds_DonHang, adapterDonHangs);
        }


    }

    private void SuKienOnClick() {
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                DiaLongThoatApp();
            }
        });

        btnNBDLichSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(true);
            }
        });

        btnNKTLichSu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(false);
            }
        });
        btnTimKiemLS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startDate != null && endDate != null) {
                    List<DonHang> dsLoc = new ArrayList<>();
                    for (DonHang donHang : ds_DonHang) {
                        Log.d("startDate",startDate);
                        Log.d("endDate",endDate);
                        Log.d("NgayNhanCuaDonHang",donHang.getNgayNhan());
                        if (KiemTraXemCoTrongKhoangThoiGianTimKo(donHang.getNgayNhan(), startDate, endDate)) {
                            dsLoc.add(donHang);
                            Toast.makeText(getActivity(), "Tìm thấy", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), " ko Tìm thấy", Toast.LENGTH_SHORT).show();
                        }
                    }
                    ds_DonHang.clear();
                    ds_DonHang.addAll(dsLoc);
                    adapterDonHangs.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "Vui lòng chọn ngày bắt đầu hoặc kết thúc", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean KiemTraXemCoTrongKhoangThoiGianTimKo(String dateString, String startDateString, String endDateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            Date date = dateFormat.parse(dateString);
            Date startDate = dateFormat.parse(startDateString);
            Date endDate = dateFormat.parse(endDateString);

            if (date != null && startDate != null && endDate != null) {
                boolean isWithinRange = !date.before(startDate) && !date.after(endDate);

                Log.d("Date Comparison", "Date: " + date + ", StartDate: " + startDate + ", EndDate: " + endDate + ", isWithinRange: " + isWithinRange);

                return isWithinRange;
            } else {
                Log.d("Date Comparison", "One or more dates are null");
                return false;
            }

        } catch (ParseException e) {
            e.printStackTrace();
            Log.d("Date Comparison", "Error parsing dates");
            return false; // Xảy ra lỗi khi chuyển đổi định dạng ngày
        }
    }


    public void showDatePickerDialog(final boolean isStartDate) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        selectedYear = year;
                        selectedMonth = monthOfYear;
                        selectedDay = dayOfMonth;

                        String selectedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;


                        if (isStartDate) {
                            startDate = selectedDate;
                            tvNgayBatDau.setText(startDate);
                        } else {
                            endDate = selectedDate;
                            tvNgayKetThuc.setText(endDate);
                        }
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
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