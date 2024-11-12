package DTO;

import java.io.Serializable;
import java.util.List;

public class DonHang implements Serializable {
    private String IDdonHang, IDkhachHang, IDcuaHang,ngayDat,ngayDuyet,ngayGiao,ngayNhan,TrangThaiDon;
    private int soLuongSanPham;
    private double phiVanChuyen,gia;
    private List<gioHang> DanhSachSanPhamThuocDon;

    public DonHang(String IDdonHang, String IDkhachHang, String IDcuaHang, String ngayDat, String ngayDuyet, String ngayGiao, String ngayNhan, String trangThaiDon, int soLuongSanPham, double phiVanChuyen, double gia, List<gioHang> danhSachSanPhamThuocDon) {
        this.IDdonHang = IDdonHang;
        this.IDkhachHang = IDkhachHang;
        this.IDcuaHang = IDcuaHang;
        this.ngayDat = ngayDat;
        this.ngayDuyet = ngayDuyet;
        this.ngayGiao = ngayGiao;
        this.ngayNhan = ngayNhan;
        TrangThaiDon = trangThaiDon;
        this.soLuongSanPham = soLuongSanPham;
        this.phiVanChuyen = phiVanChuyen;
        this.gia = gia;
        DanhSachSanPhamThuocDon = danhSachSanPhamThuocDon;
    }

    public DonHang() {
    }

    public String getIDdonHang() {
        return IDdonHang;
    }

    public void setIDdonHang(String IDdonHang) {
        this.IDdonHang = IDdonHang;
    }

    public String getIDkhachHang() {
        return IDkhachHang;
    }

    public void setIDkhachHang(String IDkhachHang) {
        this.IDkhachHang = IDkhachHang;
    }

    public String getIDcuaHang() {
        return IDcuaHang;
    }

    public void setIDcuaHang(String IDcuaHang) {
        this.IDcuaHang = IDcuaHang;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public String getNgayDuyet() {
        return ngayDuyet;
    }

    public void setNgayDuyet(String ngayDuyet) {
        this.ngayDuyet = ngayDuyet;
    }

    public String getNgayGiao() {
        return ngayGiao;
    }

    public void setNgayGiao(String ngayGiao) {
        this.ngayGiao = ngayGiao;
    }

    public String getNgayNhan() {
        return ngayNhan;
    }

    public void setNgayNhan(String ngayNhan) {
        this.ngayNhan = ngayNhan;
    }

    public String getTrangThaiDon() {
        return TrangThaiDon;
    }

    public void setTrangThaiDon(String trangThaiDon) {
        TrangThaiDon = trangThaiDon;
    }

    public int getSoLuongSanPham() {
        return soLuongSanPham;
    }

    public void setSoLuongSanPham(int soLuongSanPham) {
        this.soLuongSanPham = soLuongSanPham;
    }

    public double getPhiVanChuyen() {
        return phiVanChuyen;
    }

    public void setPhiVanChuyen(double phiVanChuyen) {
        this.phiVanChuyen = phiVanChuyen;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public List<gioHang> getDanhSachSanPhamThuocDon() {
        return DanhSachSanPhamThuocDon;
    }

    public void setDanhSachSanPhamThuocDon(List<gioHang> danhSachSanPhamThuocDon) {
        DanhSachSanPhamThuocDon = danhSachSanPhamThuocDon;
    }
}
