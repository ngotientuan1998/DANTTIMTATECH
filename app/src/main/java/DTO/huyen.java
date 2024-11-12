package DTO;

import java.util.List;

public class huyen {
    private String ten;
    private List<xa>  list;

    public huyen(String ten, List<xa> list) {
        this.ten = ten;
        this.list = list;
    }

    public huyen() {
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public List<xa> getList() {
        return list;
    }

    public void setList(List<xa> list) {
        this.list = list;
    }
}
