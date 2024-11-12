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


public class FragmentDuyet extends Fragment {

    RecyclerView ryc_donDuyet;
    List<DonHang> dsDonHang;
    adapterDonHang adapterDonHangsFragmentDuyet;
    String ID, role;
    SharedPreferences sharedPreferences;
    DaoDonHang daoDonHang;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_duyet, container, false);
        ryc_donDuyet=view.findViewById(R.id.ryc_donDuyet);
        daoDonHang = new DaoDonHang(getContext());
        sharedPreferences = getActivity().getSharedPreferences("taiKhoan", getActivity().MODE_PRIVATE);
        ID = sharedPreferences.getString("IDuser", "");
        role = sharedPreferences.getString("role", "");
        dsDonHang = new ArrayList<>();
        adapterDonHangsFragmentDuyet = new adapterDonHang(dsDonHang, getActivity(), ID, role,"Duyet");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        ryc_donDuyet.setLayoutManager(linearLayoutManager);
        ryc_donDuyet.setAdapter(adapterDonHangsFragmentDuyet);
        if (role.equals("Cửa hàng")) {
            daoDonHang.layDonHangTheoIDCHvaTrangThai(ID,"DuocDuyet",dsDonHang,adapterDonHangsFragmentDuyet);
        } else {
            daoDonHang.langNgheDonHangTheoIDKHvaTrangThai(ID,"DuocDuyet",dsDonHang,adapterDonHangsFragmentDuyet);
        }
        return view;
    }
}