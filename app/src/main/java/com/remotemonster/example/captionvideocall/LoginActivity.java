package com.remotemonster.example.captionvideocall;

import androidx.appcompat.app.AppCompatActivity
;
import androidx.databinding.DataBindingUtil;

import com.google.gson.JsonObject;
import com.remotemonster.example.captionvideocall.databinding.ActivityLoginBinding;

import android.content.Intent;
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

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
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



                        Toast.makeText(LoginActivity.this, "로그인 되었습니다.".toString(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, "아이디와 비밀번호를 확인해주세요"
                                , Toast.LENGTH_SHORT).show();

                        Log.d("ssss", response.message());
                    }
                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("ssss", t.getMessage());
                }
            });
        });

        //회원가입버튼 클릭시
        binding.btnResgist.setOnClickListener(view -> {
            Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(intent);
        });
    }
}