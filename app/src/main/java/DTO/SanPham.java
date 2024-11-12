package DTO;

import java.io.Serializable;
import java.util.List;

public class SanPham implements Serializable {
    private String IDsanPham,IDcuaHang,tenSanPham,chiTietSanPham,loaiSanPham,ngayThem,CPU,RAM,oCung,Card,Mhinh,tenCuaHang;
    private Double gia,giamGia;
    private int soLuong,soLuongBan,diemChayQuangCao;
    private List<String> hinhAnhSanPham;

    public SanPham() {
    }

    public SanPham(String IDsanPham, String IDcuaHang,String tenCuaHang,String tenSanPham, String chiTietSanPham, String loaiSanPham, String ngayThem, String CPU, String RAM, String oCung, String card, String mhinh, Double gia, Double giamGia, int soLuong, int soLuongBan, int diemChayQuangCao, List<String> hinhAnhSanPham) {
        this.IDsanPham = IDsanPham;
        this.IDcuaHang = IDcuaHang;
        this.tenCuaHang=tenCuaHang;
        this.tenSanPham = tenSanPham;
        this.chiTietSanPham = chiTietSanPham;
        this.loaiSanPham = loaiSanPham;
        this.ngayThem = ngayThem;
        this.CPU = CPU;
        this.RAM = RAM;
        this.oCung = oCung;
        Card = card;
        Mhinh = mhinh;
        this.gia = gia;
        this.giamGia = giamGia;
        this.soLuong = soLuong;
        this.soLuongBan = soLuongBan;
        this.diemChayQuangCao = diemChayQuangCao;
        this.hinhAnhSanPham = hinhAnhSanPham;
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

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public String getChiTietSanPham() {
        return chiTietSanPham;
    }

    public void setChiTietSanPham(String chiTietSanPham) {
        this.chiTietSanPham = chiTietSanPham;
    }

    public String getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(String loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public String getNgayThem() {
        return ngayThem;
    }

    public void setNgayThem(String ngayThem) {
        this.ngayThem = ngayThem;
    }

    public String getCPU() {
        return CPU;
    }

    public void setCPU(String CPU) {
        this.CPU = CPU;
    }

    public String getRAM() {
        return RAM;
    }

    public void setRAM(String RAM) {
        this.RAM = RAM;
    }

    public String getoCung() {
        return oCung;
    }

    public void setoCung(String oCung) {
        this.oCung = oCung;
    }

    public String getCard() {
        return Card;
    }

    public void setCard(String card) {
        Card = card;
    }

    public String getMhinh() {
        return Mhinh;
    }

    public void setMhinh(String mhinh) {
        Mhinh = mhinh;
    }

    public Double getGia() {
        return gia;
    }

    public void setGia(Double gia) {
        this.gia = gia;
    }

    public Double getGiamGia() {
        return giamGia;
    }

    public void setGiamGia(Double giamGia) {
        this.giamGia = giamGia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getSoLuongBan() {
        return soLuongBan;
    }

    public void setSoLuongBan(int soLuongBan) {
        this.soLuongBan = soLuongBan;
    }

    public int getDiemChayQuangCao() {
        return diemChayQuangCao;
    }

    public void setDiemChayQuangCao(int diemChayQuangCao) {
        this.diemChayQuangCao = diemChayQuangCao;
    }

    public List<String> getHinhAnhSanPham() {
        return hinhAnhSanPham;
    }

    public void setHinhAnhSanPham(List<String> hinhAnhSanPham) {
        this.hinhAnhSanPham = hinhAnhSanPham;
    }

    public String getTenCuaHang() {
        return tenCuaHang;
    }

    public void setTenCuaHang(String tenCuaHang) {
        this.tenCuaHang = tenCuaHang;
    }
}
