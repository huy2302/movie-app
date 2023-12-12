package com.example.moviefilmapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.moviefilmapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;


public class RegisterActivity extends AppCompatActivity {
    private EditText emailEdt, passEdt, conEdt;
    private Button registerBtn;
    private ImageButton imageButtonBack;
    private boolean isValidEmail(CharSequence target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
    private int counter = 0;

    @Override
    public void onBackPressed() {
        counter++;
        if (counter == 1) {
            super.onBackPressed();
            Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
            startActivity(i);
        }
    }
    public boolean isValidPass(String password){
        int passLength = password.length();
        return passLength >= 6 && passLength <=12;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailEdt = findViewById(R.id.editTextEmail);
        passEdt = findViewById(R.id.editTextPassword);
        conEdt = findViewById(R.id.editTextConFirm);
        registerBtn = findViewById(R.id.registerBtn);
        imageButtonBack = findViewById(R.id.imageButtonBack);

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {backstack();}
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {register();}
        });
    }

    private void backstack() {
        Intent i = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(i);
    }

    private void register() {
        String email,password,confirm;
        email = emailEdt.getText().toString().trim();
        password = passEdt.getText().toString().trim();
        confirm = conEdt.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Vui lòng nhập email!",Toast.LENGTH_LONG).show();
            return;
        }
        if (!isValidEmail(email)) {
            Toast.makeText(this, "Email không đúng định dạng!", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Vui lòng nhập mật khẩu!",Toast.LENGTH_LONG).show();
            return;
        }
        if (!isValidPass(password)) {
            Toast.makeText(this, "Mật khẩu phải có 6 -> 12 kí tự", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(confirm)){
            Toast.makeText(this,"Vui lòng nhập xác nhận mật khẩu!",Toast.LENGTH_LONG).show();
            return;
        }
        if (!password.equals(confirm)) {
            Toast.makeText(this, "Xác nhận mật khẩu không khớp!", Toast.LENGTH_LONG).show();
            return;
        }

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(),"Tạo tài khoản thành công", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                        startActivity(intent);
                    } else {
                        // Xử lý lỗi khi tạo tài khoản
                        Exception exception = task.getException();
                        if (exception instanceof FirebaseAuthUserCollisionException) {
                            // Hiển thị thông báo lỗi email tồn tại
                            Toast.makeText(RegisterActivity.this, "Email đã tồn tại", Toast.LENGTH_SHORT).show();
                        } else {
                            // Xử lý các lỗi khác
                            Toast.makeText(RegisterActivity.this, "Lỗi khi tạo tài khoản: " + exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
