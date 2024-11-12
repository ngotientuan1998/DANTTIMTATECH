package frament;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appduan1.R;
import com.example.appduan1.TrangChu;
import com.example.appduan1.giaoDienAdmin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.Executor;

import DAO.DaoCuaHang;
import DAO.DaoKhachHang;

public class DangNhapFrament extends Fragment {
    private EditText edtMkDN, edtEmaiDN;
    private ImageView eyeIcon;
    private TextView tvQmk;
    private Button btnDangNhap;
    private ProgressBar ProgressBarDangNhap;


    private CheckBox chk_NhoTK;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private String role = "Khách hàng";
    ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dang_nhap_frament, container, false);
        sharedPreferences = getActivity().getSharedPreferences("taiKhoan", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Gan_ID(view);
        SuKienClick();

        return view;
    }

    private void dangNhap() {
        String email, password;
        email = edtEmaiDN.getText().toString();
        password = edtMkDN.getText().toString();

        if (email.trim().isEmpty() || password.trim().isEmpty()) {
            Toast.makeText(getActivity(), "Tên tài khoản hoặc mật khẩu trống ", Toast.LENGTH_SHORT).show();
            return;
        }
        ProgressBarDangNhap.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (email.equals("admin@gmail.com")) {
                                role = "admin";
                                luuTruVaiTroVaDangNhap(role);
                                Intent intent = new Intent(getActivity(), giaoDienAdmin.class);
                                startActivity(intent);
                            } else {
                                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                editor.putString("IDuser", userId);
                                KiemTraTrangThaiTaiKhoan(userId);
                            }

                        } else {
                            ProgressBarDangNhap.setVisibility(View.GONE);
                            // Đăng nhập thất bại
                            Toast.makeText(getActivity(), "Sai tên tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();

                            // Hiển thị thông báo lỗi chi tiết nếu cần
                            if (task.getException() != null) {
                                Log.e("SignIn", "Error: " + task.getException().getMessage());
                            }
                        }
                    }
                });
    }

    private void KiemTraTrangThaiTaiKhoan(String ID) {
        DocumentReference khachHangDocRef = db.collection("khachHang").document(ID);
        DocumentReference cuaHangDocRef = db.collection("cuaHang").document(ID);
        khachHangDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot khachHangSnapshot = task.getResult();
                    if (khachHangSnapshot.exists()) {
                        // Người dùng là khách hàng
                        role = "Khách hàng";

                        DaoKhachHang daoKhachHang = new DaoKhachHang(getContext());
                        daoKhachHang.checkAccountStatus(ID, new DaoKhachHang.checkTrangThai() {
                            @Override
                            public void kiemTraTaiKhoan(boolean isActive) {
                                if (isActive) {
                                    luuTruVaiTroVaDangNhap(role);
                                } else {
                                    ProgressBarDangNhap.setVisibility(View.GONE);
                                    Toast.makeText(getActivity(), "tài khoản của bạn đã bị khóa", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else {
                        // Không phải khách hàng, kiểm tra trong cửa hàng
                        cuaHangDocRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot cuaHangSnapshot = task.getResult();
                                    if (cuaHangSnapshot.exists()) {
                                        // Người dùng là cửa hàng
                                        role = "Cửa hàng";
                                        DaoCuaHang daoCuaHang = new DaoCuaHang(getContext());
                                        daoCuaHang.checkAccountStatus(ID, new DaoCuaHang.checkTrangThai() {
                                            @Override
                                            public void kiemTraTaiKhoan(boolean isActive) {
                                                if (isActive) {
                                                    luuTruVaiTroVaDangNhap(role);
                                                } else {
                                                    ProgressBarDangNhap.setVisibility(View.GONE);
                                                    Toast.makeText(getActivity(), "tài khoản của bạn đã bị khóa", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });

                                    } else {
                                        // Không phải cả hai loại người dùng
                                        ProgressBarDangNhap.setVisibility(View.GONE);
                                        Toast.makeText(getActivity(), "Người dùng không tồn tại", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // Xử lý lỗi khi đọc dữ liệu cửa hàng
                                    Toast.makeText(getActivity(), "Lỗi" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    // Xử lý lỗi khi đọc dữ liệu khách hàng
                    Toast.makeText(getActivity(), "Lỗi" + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void luuTruVaiTroVaDangNhap(String role) {
        editor.putString("role", role);
        editor.apply();
        Intent intent;
        if ("Khách hàng".equals(role) || "Cửa hàng".equals(role)) {
            intent = new Intent(getActivity(), TrangChu.class); // Thay thế KhachHangActivity bằng tên Activity của khách hàng
            ProgressBarDangNhap.setVisibility(View.GONE);
            Toast.makeText(getActivity(), "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
            startActivity(intent);
            getActivity().finish();
        }
    }


    private void Gan_ID(View view) {
        edtEmaiDN = view.findViewById(R.id.edt_emaiDN);
        edtMkDN = view.findViewById(R.id.edt_MkDN);
        eyeIcon = view.findViewById(R.id.eye_DN);
        chk_NhoTK = view.findViewById(R.id.chk_NhoTK);
        btnDangNhap = view.findViewById(R.id.btn_DangNhap);
        tvQmk = view.findViewById(R.id.tvQmk);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        ProgressBarDangNhap=view.findViewById(R.id.ProgressBarDangNhap);
    }

    private void KiemTraNhoTKMK() {

        boolean nhoTaiKhoan = sharedPreferences.getBoolean("nhoTaiKhoan", false);
        if (nhoTaiKhoan) {
            String savedUsername = sharedPreferences.getString("username", "");
            String savedPassword = sharedPreferences.getString("password", "");

            edtEmaiDN.setText(savedUsername);
            edtMkDN.setText(savedPassword);
            chk_NhoTK.setChecked(true);
        } else {
            edtEmaiDN.setText("");
            edtMkDN.setText("");
            chk_NhoTK.setChecked(false);
        }
        chk_NhoTK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // Lưu tài khoản và mật khẩu nếu người dùng chọn nhớ
                    editor.putBoolean("nhoTaiKhoan", true);
                    editor.putString("username", edtEmaiDN.getText().toString().trim());
                    editor.putString("password", edtMkDN.getText().toString().trim());
                } else {
                    // Xóa lưu trữ nếu người dùng không chọn nhớ
                    editor.putBoolean("nhoTaiKhoan", false);
                    editor.remove("username");
                    editor.remove("password");
                }

                // Áp dụng các thay đổi
                editor.apply();
            }
        });
    }

    private void SuKienClick() {
        eyeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnHienMatKhau();
            }
        });
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dangNhap();
            }
        });
        KiemTraNhoTKMK();
        tvQmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HienThiQuenMK();
            }
        });


    }


    private void HienThiQuenMK() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        View view = inflater.inflate(R.layout.dialong_quen_mat_khau, null);
        EditText edtEmailQMK = view.findViewById(R.id.edt_emaiLayLaiMatKhau);
        Button btn_layLai = view.findViewById(R.id.btn_QMK);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        AlertDialog dialog = builder.create();
        btn_layLai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emaild = edtEmailQMK.getText().toString();
                if (emaild.trim().isEmpty()) {
                    Toast.makeText(getActivity(), "email trống", Toast.LENGTH_SHORT).show();
                } else {
                    guiMKveMail(emaild);
                }
            }
        });
        dialog.show();
    }

    private void guiMKveMail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(),
                                    "Một liên kết đặt lại mật khẩu đã được gửi đến email của bạn.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(),
                                    "Có lỗi xảy ra. Vui lòng thử lại.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    private void AnHienMatKhau() {
        if (edtMkDN.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            // Nếu đang ẩn, hiển thị mật khẩu
            edtMkDN.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            eyeIcon.setImageResource(R.drawable.baseline_remove_red_eye_24); // Thay đổi hình ảnh của biểu tượng mắt
        } else {
            // Nếu đang hiển thị, ẩn mật khẩu
            edtMkDN.setTransformationMethod(PasswordTransformationMethod.getInstance());
            eyeIcon.setImageResource(R.drawable.matnham); // Thay đổi hình ảnh của biểu tượng mắt
        }

        // Đặt con trỏ ở cuối chuỗi
        edtMkDN.setSelection(edtMkDN.getText().length());
    }


}