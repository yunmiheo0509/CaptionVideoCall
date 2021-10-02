package com.cationvideocall.example.captionvideocall.Activity;

import com.google.gson.JsonObject;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.captionvideocall.example.captionvideocall.R;
import com.captionvideocall.example.captionvideocall.databinding.ActivityNotifyCallBinding;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitHelper;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotifyCallActivity extends AppCompatActivity {
    private ActivityNotifyCallBinding binding;
    private RetrofitService retrofitService1,retrofitService2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_call);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notify_call);
        Intent intent = getIntent();
        String user_id = intent.getExtras().getString("user_id");
        String room_num = intent.getExtras().getString("room_num");

        binding.tvvWhocall.setText(user_id);

        binding.imvGetCall.setOnClickListener(view -> {
            retrofitService1 = RetrofitHelper.getRetrofit().create(RetrofitService.class);

            Call<JsonObject> call = retrofitService1.getResponse(user_id,true);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Log.d("연결 성공", response.message());
                        JsonObject jsonObject = response.body();
                        Intent intent1 = new Intent(NotifyCallActivity.this, OnCallActivity.class);
                        intent1.putExtra("user_id", user_id);
                        intent1.putExtra("room_num", room_num);
                        startActivity(intent1);
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
        });
        binding.imvRejectCall.setOnClickListener(view -> {
            retrofitService2 = RetrofitHelper.getRetrofit().create(RetrofitService.class);

            Call<JsonObject> call = retrofitService2.getResponse(user_id,false);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Log.d("연결 성공", response.message());
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
        });

    }
}