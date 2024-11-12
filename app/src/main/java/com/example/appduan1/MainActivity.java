package com.example.appduan1;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvHappyCode;
    private TextView tvHappyMoney;
    private TextView tvHappyLife;
    private ImageView imgCeo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Tạo Intent để chuyển đến layout B
                Intent intent = new Intent(MainActivity.this, DangNhapDangKi.class);
                startActivity(intent);

                // Đóng MainActivity để người dùng không thể quay lại nó bằng nút Back
                finish();
            }
        }, 3000); // 3000 milliseconds tương đương với 3 giây

        tvHappyCode = findViewById(R.id.tvHAPPYCODE);
        tvHappyMoney = findViewById(R.id.tvHAPPYMONEY);
        tvHappyLife = findViewById(R.id.tvHAPPYLIFE);
        imgCeo = findViewById(R.id.img_ceo);

        // Áp dụng hiệu ứng alpha dần dần
        startAlphaAnimation();
    }
    private void startAlphaAnimation() {
        // Điều chỉnh thời gian hiển thị dần dần (trong mili giây)
        int duration = 1500;

        // Tạo và khởi chạy ObjectAnimator cho từng view
        ObjectAnimator alphaAnimator1 = ObjectAnimator.ofFloat(tvHappyCode, "alpha", 0f, 1f);
        alphaAnimator1.setDuration(duration);
        alphaAnimator1.start();

        ObjectAnimator alphaAnimator2 = ObjectAnimator.ofFloat(tvHappyMoney, "alpha", 0f, 1f);
        alphaAnimator2.setDuration(duration);
        alphaAnimator2.setStartDelay(500); // Trì hoãn 500ms trước khi bắt đầu
        alphaAnimator2.start();

        ObjectAnimator alphaAnimator3 = ObjectAnimator.ofFloat(tvHappyLife, "alpha", 0f, 1f);
        alphaAnimator3.setDuration(duration);
        alphaAnimator3.setStartDelay(1000); // Trì hoãn 1000ms trước khi bắt đầu
        alphaAnimator3.start();

        ObjectAnimator alphaAnimator4 = ObjectAnimator.ofFloat(imgCeo, "alpha", 0f, 1f);
        alphaAnimator4.setDuration(duration);
        alphaAnimator4.setStartDelay(1500); // Trì hoãn 1500ms trước khi bắt đầu
        alphaAnimator4.start();
    }

}