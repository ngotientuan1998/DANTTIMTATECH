package frament;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appduan1.R;

import java.util.ArrayList;
import java.util.List;

import DAO.DaoDonHang;
import DTO.DonHang;
import adapter.adapterDonHang;


public class FragmentDoiDuyet extends Fragment {
    RecyclerView ryc_doiDuyet;
    List<DonHang> dsDonHang;
    adapterDonHang adapterDonHangs;
    String ID, role;
    SharedPreferences sharedPreferences;
    DaoDonHang daoDonHang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doi_duyet, container, false);
        daoDonHang = new DaoDonHang(getContext());

        sharedPreferences = getActivity().getSharedPreferences("taiKhoan", getActivity().MODE_PRIVATE);
        ID = sharedPreferences.getString("IDuser", "");
        role = sharedPreferences.getString("role", "");

        Log.d("IDkhachHang IDkhachHang IDkhachHang",ID);
        ryc_doiDuyet = view.findViewById(R.id.ryc_DonDoiDuyet);
        dsDonHang = new ArrayList<>();
        adapterDonHangs = new adapterDonHang(dsDonHang, getActivity(), ID, role,"DoiDuyet");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        ryc_doiDuyet.setLayoutManager(linearLayoutManager);
        ryc_doiDuyet.setAdapter(adapterDonHangs);


        if (role.equals("Cửa hàng")) {
            daoDonHang.layDonHangTheoIDCHvaTrangThai(ID,"ChuaDuyet",dsDonHang,adapterDonHangs);
        } else {
           daoDonHang.langNgheDonHangTheoIDKHvaTrangThai(ID,"ChuaDuyet",dsDonHang,adapterDonHangs);
        }
        Log.d("sizedsDonHang","số lượng phần tử"+dsDonHang.size());
        return view;
    }
}