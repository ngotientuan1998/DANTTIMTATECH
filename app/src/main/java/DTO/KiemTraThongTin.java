package DTO;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class KiemTraThongTin {
    public KiemTraThongTin() {
    }

    public  boolean isValidPhoneNumber(String phoneNumber) {
        // Biểu thức chính quy cho số điện thoại Việt Nam
        String regex = "^(\\+84|0)(9|8|7|5|3)([0-9]{8})$";

        // Tạo pattern từ biểu thức chính quy
        Pattern pattern = Pattern.compile(regex);

        // Tạo matcher để so khớp với chuỗi đầu vào
        Matcher matcher = pattern.matcher(phoneNumber);

        // Kiểm tra xem có khớp hay không
        return matcher.matches();
    }

    public  boolean KTsoThuc(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            // Nếu có ngoại lệ, chuỗi không phải là số
            return false;
        }
    }

    public  boolean KtsoNguyen(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            // Nếu có ngoại lệ, chuỗi không phải là số
            return false;
        }
    }

    public  boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
