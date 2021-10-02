package com.cationvideocall.example.captionvideocall.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.captionvideocall.example.captionvideocall.R;
import com.captionvideocall.example.captionvideocall.databinding.ActivityMainBinding;
import com.cationvideocall.example.captionvideocall.MySharedPreferences;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.person1.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProposeCallActivity.class);
            startActivity(intent);
            finish();
        });
        binding.imvLogout.setOnClickListener(view -> {
            // 로그아웃 진행
            MySharedPreferences.clearUser(getApplicationContext());
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });


    }

}