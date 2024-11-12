package frament;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.appduan1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.Executor;

import DAO.DaoCuaHang;
import DAO.DaoKhachHang;
import DTO.KiemTraThongTin;

public class DangKiFragment extends Fragment {
    private EditText edtEmail_DK, edtMk_DK, edtMk_LDK;
    private Spinner spinner_VT;
    private ImageView eyeIcon_mk, eyeIcon_lmk;
    private Button btnDangKi;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    String vaiTro;
    String[] DsVaiTro = {"Cửa hàng", "Khách hàng"};

    KiemTraThongTin kiemTraThongTin;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dang_ki, container, false);
        Gan_ID(view);
        Adapter();
        SuKienClick();
        return view;
    }

    public void DangKi() {
        final String email = edtEmail_DK.getText().toString().trim();
        String password = edtMk_DK.getText().toString().trim();
        String passwordl = edtMk_LDK.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty() || passwordl.isEmpty()) {
            Toast.makeText(getActivity(), "Không được để trống các trường thông tin", Toast.LENGTH_SHORT).show();
        } else if (!kiemTraThongTin.isValidEmail(email)) {
            Toast.makeText(getActivity(), "Email chưa đúng định dạng", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(passwordl)) {
            Toast.makeText(getActivity(), "Mật khẩu chưa giống nhau", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 6) {
            Toast.makeText(getActivity(), "Mật khẩu phải trên 6 kí tự", Toast.LENGTH_SHORT).show();
        } else {
            // Kiểm tra xem email đã tồn tại chưa
            progressBar.setVisibility(View.VISIBLE);
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                @Override
                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                    if (task.isSuccessful()) {
                        SignInMethodQueryResult result = task.getResult();
                        if (result.getSignInMethods().isEmpty()) {
                            // Email chưa được sử dụng, tiến hành đăng ký
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                String userId = user.getUid();
                                                Calendar calendar = Calendar.getInstance();
                                                int year = calendar.get(Calendar.YEAR);
                                                int month = calendar.get(Calendar.MONTH) + 1; // Tháng trong Calendar là từ 0 đến 11
                                                int day = calendar.get(Calendar.DAY_OF_MONTH);
                                                String ngayThangNam = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
                                                // Hàm lưu thông tin
                                                luuThongTinUser(userId, email, vaiTro, ngayThangNam);
                                                Toast.makeText(getActivity(), "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                                                // Làm mới các trường dữ liệu
                                                edtEmail_DK.setText("");
                                                edtMk_DK.setText("");
                                                edtMk_LDK.setText("");

                                                // Chuyển sang fragment đăng nhập
                                                ViewPager2 viewPager = getActivity().findViewById(R.id.view_pager_DNDK);
                                                viewPager.setCurrentItem(0); // Chuyển đến fragment đăng nhập
                                            } else {
                                                Toast.makeText(getActivity(), "Đăng ký thất bại", Toast.LENGTH_SHORT).show();
                                                if (task.getException() != null) {
                                                    Log.e("SignIn", "Error: " + task.getException().getMessage());
                                                }
                                            }
                                        }
                                    });
                        } else {
                            // Email đã tồn tại, thông báo cho người dùng
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "Email đã tồn tại, vui lòng chọn email khác", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Xử lý khi kiểm tra đăng nhập thất bại
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "Kiểm tra đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }



    private void luuThongTinUser(String userId, String email, String role, String NTNT) {
        if (role != null && email != null) {
            if (role.equals("Cửa hàng")) {
                DaoCuaHang daoCuaHang = new DaoCuaHang(getContext());
                daoCuaHang.luuThongTinDangKi(userId, email, NTNT, role);
            } else {
                DaoKhachHang daoKhachHang = new DaoKhachHang(getContext());
                daoKhachHang.luuThongTinDangKi(userId, email, NTNT, role);
            }

        }
    }


    private void Gan_ID(View view) {
        kiemTraThongTin = new KiemTraThongTin();
        edtEmail_DK = view.findViewById(R.id.edt_emailDK);
        edtMk_DK = view.findViewById(R.id.edt_MkDK);
        edtMk_LDK = view.findViewById(R.id.edt_LMkDK);
        eyeIcon_mk = view.findViewById(R.id.eye_iconMK);
        eyeIcon_lmk = view.findViewById(R.id.eye_iconLMK);

        spinner_VT = view.findViewById(R.id.spinnerVaiTro);
        btnDangKi = view.findViewById(R.id.btn_DangKi);
        progressBar = view.findViewById(R.id.ProgressBarDangKi);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

    }

    private void SuKienClick() {
        eyeIcon_mk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnHienMatKhau();
            }
        });
        eyeIcon_lmk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnHienMatKhau();
            }
        });


        btnDangKi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DangKi();
            }
        });

    }

    private void Adapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, DsVaiTro);
        spinner_VT.setAdapter(adapter);

        spinner_VT.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                vaiTro = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void AnHienMatKhau() {
        if (edtMk_DK.getTransformationMethod() == PasswordTransformationMethod.getInstance()) {
            // Nếu đang ẩn, hiển thị mật khẩu
            edtMk_DK.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            eyeIcon_mk.setImageResource(R.drawable.baseline_remove_red_eye_24);

            edtMk_LDK.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            eyeIcon_lmk.setImageResource(R.drawable.baseline_remove_red_eye_24); // Thay đổi hình ảnh của biểu tượng mắt
        } else {
            // Nếu đang hiển thị, ẩn mật khẩu
            edtMk_DK.setTransformationMethod(PasswordTransformationMethod.getInstance());
            eyeIcon_mk.setImageResource(R.drawable.matnham);

            edtMk_LDK.setTransformationMethod(PasswordTransformationMethod.getInstance());
            eyeIcon_lmk.setImageResource(R.drawable.matnham);// Thay đổi hình ảnh của biểu tượng mắt
        }

        edtMk_DK.setSelection(edtMk_DK.getText().length());
        edtMk_LDK.setSelection(edtMk_LDK.getText().length());
    }


}