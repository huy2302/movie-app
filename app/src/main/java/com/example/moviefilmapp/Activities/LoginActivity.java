package com.example.moviefilmapp.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moviefilmapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private AutoCompleteTextView userEdt;
    private EditText passEdt;
    private Button loginBtn;
    private FirebaseAuth mAuth;
    private TextView registerText;
    private TextView forgotText;
    private SharedPreferences sharedPreferences;
    private String[] emailList;
    private String savedEmails;
    private int counter = 0;

    @Override
    public void onBackPressed() {
        counter++;
        if (counter == 1) {
            super.onBackPressed();
            Intent i = new Intent(LoginActivity.this, IntroActivity.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        userEdt = findViewById(R.id.autoCompleteTextViewEmail);
        passEdt = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.loginBtn);
        registerText = findViewById(R.id.textRegister);
        forgotText = findViewById(R.id.textForgotPass);

        // Initialization of savedEmails
        savedEmails = sharedPreferences.getString("emailList", "");

        emailList = savedEmails.split(",");

        // Display the email list in AutoCompleteTextView
        ArrayAdapter<String> emailAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, emailList);
        userEdt.setAdapter(emailAdapter);

        // Check if the user is already logged in
        if (mAuth.getCurrentUser() != null) {
            // User is already logged in, redirect to MainActivity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish(); // Finish LoginActivity to prevent going back to it from MainActivity
        }

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });

        forgotText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgot();
            }
        });
    }

    private void forgot() {
        Intent i = new Intent(LoginActivity.this, ForgotPassActivity.class);
        startActivity(i);
    }

    private void register() {
        Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(i);
    }

    private void login() {
        String email = userEdt.getText().toString();
        String password = passEdt.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập email!", Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Vui lòng nhập mật khẩu!", Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Save the entered email to SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("isLoggedIn", true);
                    editor.putString("email", email);

                    // Check if the email already exists in the list
                    boolean isEmailExist = false;
                    for (String savedEmail : emailList) {
                        if (savedEmail.equals(email)) {
                            isEmailExist = true;
                            break;
                        }
                    }

                    // If the email does not exist, add it to the list
                    if (!isEmailExist) {
                        String updatedEmailList = sharedPreferences.getString("emailList", "");
                        if (!TextUtils.isEmpty(updatedEmailList)) {
                            updatedEmailList += ",";
                        }
                        updatedEmailList += email;
                        editor.putString("emailList", updatedEmailList);
                    }

                    editor.apply();

                    Toast.makeText(getApplicationContext(), "Đăng nhập thành công!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Finish LoginActivity to prevent going back to it from MainActivity
                } else {
                    Toast.makeText(getApplicationContext(), "Sai email hoặc mật khẩu", Toast.LENGTH_LONG).show();
                    passEdt.setText("");
                }
            }
        });
    }
}
