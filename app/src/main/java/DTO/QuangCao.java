package DTO;

import java.io.Serializable;

public class QuangCao implements Serializable {

    String IDQC,IDcuaHang,TenCuaHang,IDsanPham,TenSanPham,TrangThai,TimeKhoiChay,TimeDuyet,TimeConLai;
    int CapDo;

    public QuangCao(String IDQC, String IDcuaHang, String tenCuaHang, String IDsanPham, String tenSanPham, String trangThai, String timeKhoiChay, String timeDuyet, String timeConLai, int capDo) {
        this.IDQC = IDQC;
        this.IDcuaHang = IDcuaHang;
        TenCuaHang = tenCuaHang;
        this.IDsanPham = IDsanPham;
        TenSanPham = tenSanPham;
        TrangThai = trangThai;
        TimeKhoiChay = timeKhoiChay;
        TimeDuyet = timeDuyet;
        TimeConLai = timeConLai;
        CapDo = capDo;
    }

    public QuangCao() {
    }

    public String getIDQC() {
        return IDQC;
    }

    public void setIDQC(String IDQC) {
        this.IDQC = IDQC;
    }

    public String getIDcuaHang() {
        return IDcuaHang;
    }

    public void setIDcuaHang(String IDcuaHang) {
        this.IDcuaHang = IDcuaHang;
    }

    public String getTenCuaHang() {
        return TenCuaHang;
    }

    public void setTenCuaHang(String tenCuaHang) {
        TenCuaHang = tenCuaHang;
    }

    public String getIDsanPham() {
        return IDsanPham;
    }

    public void setIDsanPham(String IDsanPham) {
        this.IDsanPham = IDsanPham;
    }

    public String getTenSanPham() {
        return TenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        TenSanPham = tenSanPham;
    }

    public String getTrangThai() {
        return TrangThai;
    }

    public void setTrangThai(String trangThai) {
        TrangThai = trangThai;
    }

    public String getTimeKhoiChay() {
        return TimeKhoiChay;
    }

    public void setTimeKhoiChay(String timeKhoiChay) {
        TimeKhoiChay = timeKhoiChay;
    }

    public String getTimeDuyet() {
        return TimeDuyet;
    }

    public void setTimeDuyet(String timeDuyet) {
        TimeDuyet = timeDuyet;
    }

    public String getTimeConLai() {
        return TimeConLai;
    }

    public void setTimeConLai(String timeConLai) {
        TimeConLai = timeConLai;
    }

    public int getCapDo() {
        return CapDo;
    }

    public void setCapDo(int capDo) {
        CapDo = capDo;
    }
}
