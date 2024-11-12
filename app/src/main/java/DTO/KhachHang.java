package DTO;

import java.io.Serializable;
import java.time.LocalDate;

public class KhachHang implements Serializable {
    private String NTNT,diaChiChiTiet,email,gioiTinh,hinhAnh,hoTen,khachHangID,role,Tinh,Huyen,Xa,trangThai;
    private int NTNS,sdt,xacThuc;

    public KhachHang() {
    }

    public KhachHang(String NTNT, String diaChiChiTiet, String email, String gioiTinh, String hinhAnh, String hoTen, String khachHangID, String role, String tinh, String huyen, String xa, String trangThai, int NTNS, int sdt, int xacThuc) {
        this.NTNT = NTNT;
        this.diaChiChiTiet = diaChiChiTiet;
        this.email = email;
        this.gioiTinh = gioiTinh;
        this.hinhAnh = hinhAnh;
        this.hoTen = hoTen;
        this.khachHangID = khachHangID;
        this.role = role;
        Tinh = tinh;
        Huyen = huyen;
        Xa = xa;
        this.trangThai = trangThai;
        this.NTNS = NTNS;
        this.sdt = sdt;
        this.xacThuc = xacThuc;
    }

    public String getNTNT() {
        return NTNT;
    }

    public void setNTNT(String NTNT) {
        this.NTNT = NTNT;
    }

    public String getDiaChiChiTiet() {
        return diaChiChiTiet;
    }

    public void setDiaChiChiTiet(String diaChiChiTiet) {
        this.diaChiChiTiet = diaChiChiTiet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getKhachHangID() {
        return khachHangID;
    }

    public void setKhachHangID(String khachHangID) {
        this.khachHangID = khachHangID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTinh() {
        return Tinh;
    }

    public void setTinh(String tinh) {
        Tinh = tinh;
    }

    public String getHuyen() {
        return Huyen;
    }

    public void setHuyen(String huyen) {
        Huyen = huyen;
    }

    public String getXa() {
        return Xa;
    }

    public void setXa(String xa) {
        Xa = xa;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public int getNTNS() {
        return NTNS;
    }

    public void setNTNS(int NTNS) {
        this.NTNS = NTNS;
    }

    public int getSdt() {
        return sdt;
    }

    public void setSdt(int sdt) {
        this.sdt = sdt;
    }

    public int getXacThuc() {
        return xacThuc;
    }

    public void setXacThuc(int xacThuc) {
        this.xacThuc = xacThuc;
    }
}
