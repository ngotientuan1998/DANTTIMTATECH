package DTO;

import java.io.Serializable;
import java.util.List;

public class gioHang implements Serializable {
  private String IDkhachHang,TrangThaiChon,IDsanPham,IDcuaHang,TenCuaHang,HinhAnhSanPham,TenSanPham,IDgioHang,
          DanhTinh;
  private double giaSp,giaGiamSp;
  private int soLuongMua;

    public gioHang() {
    }

    public gioHang(String IDkhachHang, String trangThaiChon, String IDsanPham, String IDcuaHang, String tenCuaHang, String hinhAnhSanPham, String tenSanPham, String IDgioHang, double giaSp, double giaGiamSp, int soLuongMua,String DanhTinh) {
        this.IDkhachHang = IDkhachHang;
        TrangThaiChon = trangThaiChon;
        this.IDsanPham = IDsanPham;
        this.IDcuaHang = IDcuaHang;
        TenCuaHang = tenCuaHang;
        HinhAnhSanPham = hinhAnhSanPham;
        TenSanPham = tenSanPham;
        this.IDgioHang = IDgioHang;
        this.giaSp = giaSp;
        this.giaGiamSp = giaGiamSp;
        this.soLuongMua = soLuongMua;
        this.DanhTinh=DanhTinh;
    }

    public String getDanhTinh() {
        return DanhTinh;
    }

    public void setDanhTinh(String danhTinh) {
        DanhTinh = danhTinh;
    }

    public String getIDkhachHang() {
        return IDkhachHang;
    }

    public void setIDkhachHang(String IDkhachHang) {
        this.IDkhachHang = IDkhachHang;
    }

    public String getTrangThaiChon() {
        return TrangThaiChon;
    }

    public void setTrangThaiChon(String trangThaiChon) {
        TrangThaiChon = trangThaiChon;
    }

    public String getIDsanPham() {
        return IDsanPham;
    }

    public void setIDsanPham(String IDsanPham) {
        this.IDsanPham = IDsanPham;
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

    public String getIDgioHang() {
        return IDgioHang;
    }

    public void setIDgioHang(String IDgioHang) {
        this.IDgioHang = IDgioHang;
    }

    public void setTenCuaHang(String tenCuaHang) {
        TenCuaHang = tenCuaHang;
    }

    public String getHinhAnhSanPham() {
        return HinhAnhSanPham;
    }

    public void setHinhAnhSanPham(String hinhAnhSanPham) {
        HinhAnhSanPham = hinhAnhSanPham;
    }

    public String getTenSanPham() {
        return TenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        TenSanPham = tenSanPham;
    }

    public double getGiaSp() {
        return giaSp;
    }

    public void setGiaSp(double giaSp) {
        this.giaSp = giaSp;
    }

    public double getGiaGiamSp() {
        return giaGiamSp;
    }

    public void setGiaGiamSp(double giaGiamSp) {
        this.giaGiamSp = giaGiamSp;
    }

    public int getSoLuongMua() {
        return soLuongMua;
    }

    public void setSoLuongMua(int soLuongMua) {
        this.soLuongMua = soLuongMua;
    }
}
