package com.cationvideocall.example.captionvideocall.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cationvideocall.example.captionvideocall.MySharedPreferences;
import com.google.gson.JsonObject;
import com.captionvideocall.example.captionvideocall.R;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitHelper;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitService;
import com.captionvideocall.example.captionvideocall.databinding.ActivityLoginBinding;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
// 퍼미션 체크를 위한 루틴입니다.
        checkPermission();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        // SharedPreferences 안에 값이 저장되어 있지 않을 때 -> LoginActivity
        if (MySharedPreferences.getUserId(this).isEmpty()
                || MySharedPreferences.getUserPass(this).isEmpty()) {
            Login();
        }
        // SharedPreferences 안에 값이 저장되어 있을 때 -> MainActivity
        else {
            Toast.makeText(this, MySharedPreferences.getUserId(this) + "님 자동 로그인 되었습니다.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }


        //회원가입버튼 클릭시
        binding.btnResgist.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
    private void Login(){
        binding.btnLogin.setOnClickListener(view -> {
            retrofitService = RetrofitHelper.getRetrofit().create(RetrofitService.class);
            Call<JsonObject> call = retrofitService.getLoginCheck(binding.etId.getText().toString(),
                    binding.etPw.getText().toString());

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Log.d("연결 성공", response.message());
                        JsonObject jsonObject = response.body();
                        MySharedPreferences.setUserId(LoginActivity.this, binding.etId.getText().toString());
                        MySharedPreferences.setUserPass(LoginActivity.this, binding.etPw.getText().toString());
                        Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요"
                                , Toast.LENGTH_SHORT).show();

                        Log.d("오류발생", response.message());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("통신실패", t.getMessage());
                }
            });
        });
    }
    // 권한을 체크합니다.
    // 사용자에게 필수로 권한을 확인받아야 하는  요소는 CAMERA,RECORD_AUDIO,WRITE_EXTERNAL_STORAGE 입니다
    @SuppressLint("NewApi")
    private void checkPermission() {
        String[] MANDATORY_PERMISSIONS = {
                "android.permission.INTERNET",
                "android.permission.CAMERA",
                "android.permission.RECORD_AUDIO",
                "android.permission.MODIFY_AUDIO_SETTINGS",
                "android.permission.ACCESS_NETWORK_STATE",
                "android.permission.CHANGE_WIFI_STATE",
                "android.permission.CHANGE_NETWORK_STATE",
                "android.permission.ACCESS_WIFI_STATE",
                "android.permission.READ_PHONE_STATE",
                "android.permission.BLUETOOTH",
                "android.permission.BLUETOOTH_ADMIN",
                "android.permission.WRITE_EXTERNAL_STORAGE"

        };
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(MANDATORY_PERMISSIONS, 100);
        }
    }

}