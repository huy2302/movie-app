package com.example.moviefilmapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moviefilmapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class ForgotPassActivity extends AppCompatActivity {

    private EditText editTextForgot;
    private FirebaseAuth mAuth;
    private Button confirm2Btn;
    private ImageButton imageButtonBack;
    private int counter = 0;

    @Override
    public void onBackPressed() {
        counter++;
        if (counter == 1) {
            super.onBackPressed();
            Intent i = new Intent(ForgotPassActivity.this,LoginActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        editTextForgot = findViewById(R.id.editTextForgot);
        mAuth = FirebaseAuth.getInstance();
        confirm2Btn =findViewById(R.id.confirm2Btn);
        imageButtonBack = findViewById(R.id.imageButtonBack);

        imageButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {backstack();}
        });

        confirm2Btn.setOnClickListener(view -> {
            String email = editTextForgot.getText().toString().trim();

            // Kiểm tra nếu email không rỗng
            if (!TextUtils.isEmpty(email)) {
                // Gửi đoạn mã xác thực 6 số đến email
                sendVerificationCode(email);
            } else {
                Toast.makeText(this, "Please enter your email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void backstack() {
        Intent i = new Intent(ForgotPassActivity.this,LoginActivity.class);
        startActivity(i);
    }

    private void sendVerificationCode(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Gửi email xác thực thành công
                        Toast.makeText(this, "Verification code sent to your email", Toast.LENGTH_SHORT).show();
                    } else {
                        // Gửi email xác thực thất bại
                        Toast.makeText(this, "Failed to send verification code", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendResetPasswordEmail(String email) {
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassActivity.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                            // Add code for further actions, such as navigating to a verification code input screen
                        } else {
                            Toast.makeText(ForgotPassActivity.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}