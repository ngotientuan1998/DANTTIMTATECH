package frament;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.appduan1.R;

import java.util.ArrayList;
import java.util.List;

import DAO.DaoDonHang;
import DTO.DonHang;
import adapter.adapterDonHang;


public class FragmentDangGiao extends Fragment {
    RecyclerView ryc_DSdonDangGiao;
    adapterDonHang adapterDonHangs;
    DaoDonHang daoDonHang;
    String ID, role;
    SharedPreferences sharedPreferences;
    List<DonHang> ds_DonHang;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang_giao, container, false);
        ryc_DSdonDangGiao = view.findViewById(R.id.ryc_DonDangGiao);
        sharedPreferences = getActivity().getSharedPreferences("taiKhoan", getActivity().MODE_PRIVATE);
        ID = sharedPreferences.getString("IDuser", "");
        role = sharedPreferences.getString("role", "");
        ds_DonHang = new ArrayList<>();
        daoDonHang=new DaoDonHang(getContext());
        adapterDonHangs = new adapterDonHang(ds_DonHang, getActivity(), ID, role, "DangGiao");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        ryc_DSdonDangGiao.setLayoutManager(linearLayoutManager);
        ryc_DSdonDangGiao.setAdapter(adapterDonHangs);
        if (role.equals("Cửa hàng")) {
            daoDonHang.layDonHangTheoIDCHvaTrangThai(ID, "DangGiao", ds_DonHang, adapterDonHangs);
        } else {
            daoDonHang.langNgheDonHangTheoIDKHvaTrangThai(ID, "DangGiao", ds_DonHang, adapterDonHangs);
        }
        return view;
    }
}