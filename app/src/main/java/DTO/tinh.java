package DTO;

import java.util.List;

public class tinh {
    private String tinh;
    private List<huyen> list;

    public tinh(String tinh, List<huyen> list) {
        this.tinh = tinh;
        this.list = list;
    }

    public tinh() {
    }

    public String getTinh() {
        return tinh;
    }

    public void setTinh(String tinh) {
        this.tinh = tinh;
    }

    public List<huyen> getList() {
        return list;
    }

    public void setList(List<huyen> list) {
        this.list = list;
    }
}
