package com.cationvideocall.example.captionvideocall.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.cationvideocall.example.captionvideocall.MySharedPreferences;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
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
    private String id, pw, token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 퍼미션 체크
        checkPermission();
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        // get token
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        token = task.getResult();
                        // SharedPreferences 안에 값이 있을 때 -> LoginActivity
                        if (!MySharedPreferences.getUserId(LoginActivity.this).isEmpty()
                                && !MySharedPreferences.getUserPass(LoginActivity.this).isEmpty()) {
                            id = MySharedPreferences.getUserId(LoginActivity.this);
                            pw = MySharedPreferences.getUserPass(LoginActivity.this);
                            Login(id, pw, token);
                        }
                    }
                });


        // 로그인 버튼 클릭시
        binding.btnLogin.setOnClickListener(view -> {
            if (!binding.etId.getText().toString().equals("") && !binding.etPw.getText().toString().equals("")) {
                id = binding.etId.getText().toString();
                pw = binding.etPw.getText().toString();
            } else {
                Toast.makeText(LoginActivity.this, "아이디와 패스워드를 입력해주세요", Toast.LENGTH_SHORT).show();
            }
            Login(id, pw, token);
        });

        //회원가입버튼 클릭시
        binding.btnResgist.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }

    public void Login(String id, String pw, String token) {
        retrofitService = RetrofitHelper.getRetrofit().create(RetrofitService.class);
        Log.d("아이디비번토큰", id + pw + token);
        Call<JsonObject> call = retrofitService.getLoginCheck(id, pw, token);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("연결 성공", response.message());
                    JsonObject jsonObject = response.body();
                    String code = jsonObject.get("code").toString();
                    if (code.equals("200")) {
                        MySharedPreferences.setUserId(LoginActivity.this, id);
                        MySharedPreferences.setUserPass(LoginActivity.this, pw);
                        Toast.makeText(LoginActivity.this, "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // 204
                        Toast.makeText(LoginActivity.this, "check id and password, It's wrong", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "check the internet"
                            , Toast.LENGTH_SHORT).show();
                    Log.d("오류발생", response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("통신실패", t.getMessage());
            }
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
                "android.permission.WRITE_EXTERNAL_STORAGE",
                "android.permission.READ_EXTERNAL_STORAGE"
                , "android.permission.VIBRATE"

        };
        if (Build.VERSION.SDK_INT >= 23) {
            requestPermissions(MANDATORY_PERMISSIONS, 100);
        }
    }

}