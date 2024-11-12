package DTO;

public class DoanhThu {
    private String Thang;
    private double Tien;

    public DoanhThu(String thang, double tien) {
        Thang = thang;
        Tien = tien;
    }

    public DoanhThu() {
    }

    public String getThang() {
        return Thang;
    }

    public void setThang(String thang) {
        Thang = thang;
    }

    public double getTien() {
        return Tien;
    }

    public void setTien(double tien) {
        Tien = tien;
    }
}
