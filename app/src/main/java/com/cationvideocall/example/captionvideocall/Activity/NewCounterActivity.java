package com.cationvideocall.example.captionvideocall.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.captionvideocall.example.captionvideocall.R;
import com.captionvideocall.example.captionvideocall.databinding.ActivityNewCounterBinding;
import com.cationvideocall.example.captionvideocall.MySharedPreferences;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitHelper;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewCounterActivity extends AppCompatActivity {
    ActivityNewCounterBinding binding;
    boolean star;
    RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_counter);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_new_counter);
        star = false;
        binding.btnCancel.setOnClickListener(view -> {
            finish();
        });
        binding.imvStar.setOnClickListener(view -> {
            if(!star){
                binding.imvStar.setImageResource(R.drawable.fullstar);
                star=true;
            }else{
                binding.imvStar.setImageResource(R.drawable.star2);
                star =false;
            }
        });
        binding.btnSave.setOnClickListener(view -> {
            String counter_id = binding.etCounterid.getText().toString();
            String name = binding.etCallbookName.getText().toString();
            String user_id =MySharedPreferences.getUserId(this);

            if(!counter_id.equals("")&&!name.equals("")){
                retrofitService = RetrofitHelper.getRetrofit().create(RetrofitService.class);
                Call<JsonObject> call = retrofitService.addCallBook(user_id,counter_id, name,star);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            Log.d("연결 성공", response.message());
                            JsonObject jsonObject = response.body();
                            Toast.makeText(NewCounterActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Log.d("ssss", response.message());
                        }
                    }
                    @Override
                    public void onFailure(Call<JsonObject> call, Throwable t) {
                        Log.d("ssss", t.getMessage());
                    }
                });
            }else{
                Toast.makeText(NewCounterActivity.this, "아이디 또는 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            }
        });

    }
}