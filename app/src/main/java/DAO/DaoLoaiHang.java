package DAO;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.List;

import DTO.CuaHang;

public class DaoLoaiHang {
    Context context;

    public DaoLoaiHang(Context context) {
        this.context = context;
    }

    String[] dsLoaiHangLoc = {"Tất cả", "MacBook", "Lenovo", "Asus", "Dell", "HP",
            "Acer", "MSI", "Razer", "Microsoft", "Samsung"};
    String[] dsLoaiHangThem = { "MacBook", "Lenovo", "Asus", "Dell", "HP",
            "Acer", "MSI", "Razer", "Microsoft", "Samsung"};

    public void Adapter(Spinner spinner, int listType) {
        String[] dataToUse;
        if (listType == 1) {
            dataToUse = dsLoaiHangLoc;
        } else if (listType == 2) {
            dataToUse = dsLoaiHangThem;
        } else {
            throw new IllegalArgumentException("Invalid list type");
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, dataToUse);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    public String[] getDsLoaiHang1() {
        return dsLoaiHangLoc;
    }

    public String[] getDsLoaiHang2() {
        return dsLoaiHangThem;
    }
}
