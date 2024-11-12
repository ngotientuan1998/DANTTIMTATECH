package frament;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.appduan1.R;


public class DsdonDoiDuyet extends Fragment {

private TextView tv_TongDon;
private SearchView searchView;
private Button btn_dondd,btn_ddh;
private RecyclerView ryc_dsddd;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_dsdon_doi_duyet, container, false);
        Gan_ID(view);
        return view;
    }
    private void  Gan_ID(View view){
        tv_TongDon=view.findViewById(R.id.TvTongDonDoiDuyet);
        searchView=view.findViewById(R.id.TimKiemDonDoiD);
        btn_dondd=view.findViewById(R.id.btnDonDaDuyet);
        btn_ddh=view.findViewById(R.id.btnDonDaHuy);
        ryc_dsddd=view.findViewById(R.id.ryc_donDoiDuyet);

    }
}