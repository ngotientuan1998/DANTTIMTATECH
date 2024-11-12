package DTO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Locale;

public class CuaHang implements Serializable {
  private String NTNT,cuaHangID,diaChiChiTiet,email,hinhAnh,role,tenCuaHang,Tinh,Huyen,Xa,trangThai;
  private int sdt,theoDoi,xacThuc;

    public CuaHang() {
    }

    public CuaHang(String NTNT, String cuaHangID, String diaChiChiTiet, String email, String hinhAnh, String role, String tenCuaHang, String tinh, String huyen, String xa, String trangThai, int sdt, int theoDoi, int xacThuc) {
        this.NTNT = NTNT;
        this.cuaHangID = cuaHangID;
        this.diaChiChiTiet = diaChiChiTiet;
        this.email = email;
        this.hinhAnh = hinhAnh;
        this.role = role;
        this.tenCuaHang = tenCuaHang;
        Tinh = tinh;
        Huyen = huyen;
        Xa = xa;
        this.trangThai = trangThai;
        this.sdt = sdt;
        this.theoDoi = theoDoi;
        this.xacThuc = xacThuc;
    }

    public String getNTNT() {
        return NTNT;
    }

    public void setNTNT(String NTNT) {
        this.NTNT = NTNT;
    }

    public String getCuaHangID() {
        return cuaHangID;
    }

    public void setCuaHangID(String cuaHangID) {
        this.cuaHangID = cuaHangID;
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

    public String getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTenCuaHang() {
        return tenCuaHang;
    }

    public void setTenCuaHang(String tenCuaHang) {
        this.tenCuaHang = tenCuaHang;
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

    public int getSdt() {
        return sdt;
    }

    public void setSdt(int sdt) {
        this.sdt = sdt;
    }

    public int getTheoDoi() {
        return theoDoi;
    }

    public void setTheoDoi(int theoDoi) {
        this.theoDoi = theoDoi;
    }

    public int getXacThuc() {
        return xacThuc;
    }

    public void setXacThuc(int xacThuc) {
        this.xacThuc = xacThuc;
    }
}
