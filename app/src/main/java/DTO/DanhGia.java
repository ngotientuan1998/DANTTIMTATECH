package DTO;

import java.io.Serializable;
import java.util.List;

public class DanhGia implements Serializable {
    private String ID,IDkhach,IDSanPham,imgAnhKhachHang,TenKhach,YkienDongGop;
    private int saoDanhGia;
    private List<String> hinhAnhSanPhamDaMua;

    public DanhGia(String ID, String IDkhach, String IDSanPham, String imgAnhKhachHang, String tenKhach, String ykienDongGop, int saoDanhGia, List<String> hinhAnhSanPhamDaMua) {
        this.ID = ID;
        this.IDkhach = IDkhach;
        this.IDSanPham = IDSanPham;
        this.imgAnhKhachHang = imgAnhKhachHang;
        TenKhach = tenKhach;
        YkienDongGop = ykienDongGop;
        this.saoDanhGia = saoDanhGia;
        this.hinhAnhSanPhamDaMua = hinhAnhSanPhamDaMua;
    }

    public DanhGia() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getIDkhach() {
        return IDkhach;
    }

    public void setIDkhach(String IDkhach) {
        this.IDkhach = IDkhach;
    }

    public String getIDSanPham() {
        return IDSanPham;
    }

    public void setIDSanPham(String IDSanPham) {
        this.IDSanPham = IDSanPham;
    }

    public String getImgAnhKhachHang() {
        return imgAnhKhachHang;
    }

    public void setImgAnhKhachHang(String imgAnhKhachHang) {
        this.imgAnhKhachHang = imgAnhKhachHang;
    }

    public String getTenKhach() {
        return TenKhach;
    }

    public void setTenKhach(String tenKhach) {
        TenKhach = tenKhach;
    }

    public String getYkienDongGop() {
        return YkienDongGop;
    }

    public void setYkienDongGop(String ykienDongGop) {
        YkienDongGop = ykienDongGop;
    }

    public int getSaoDanhGia() {
        return saoDanhGia;
    }

    public void setSaoDanhGia(int saoDanhGia) {
        this.saoDanhGia = saoDanhGia;
    }

    public List<String> getHinhAnhSanPhamDaMua() {
        return hinhAnhSanPhamDaMua;
    }

    public void setHinhAnhSanPhamDaMua(List<String> hinhAnhSanPhamDaMua) {
        this.hinhAnhSanPhamDaMua = hinhAnhSanPhamDaMua;
    }
}
