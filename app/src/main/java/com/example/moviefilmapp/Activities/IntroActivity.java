package com.example.moviefilmapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.moviefilmapp.R;

public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        Button getBtn = findViewById(R.id.getBtn);
        getBtn.setOnClickListener(view -> startActivity(new Intent(IntroActivity.this,LoginActivity.class)));
    }
}