package com.cationvideocall.example.captionvideocall.Activity;

import com.google.gson.JsonObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

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
    Ringtone rt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notify_call);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_notify_call);
        Intent intent = getIntent();
        String counter_id = intent.getExtras().getString("user_id");
        String room_num = intent.getExtras().getString("room_num");

        // 애니매이션
        @SuppressLint("ResourceType") Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.animator.ani_calling);
        binding.imvGetCall.startAnimation(animation);
        binding.imvRejectCall.startAnimation(animation);

        //진동 및 소리
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        final Vibrator vibrator = (Vibrator)getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(new long[]{500,1000,500,1000},0);
        rt = RingtoneManager.getRingtone(getApplicationContext(),notification);
        rt.play();


        binding.tvWhocall.setText(counter_id);
        binding.imvGetCall.setOnClickListener(view -> {
            rt.stop();
            vibrator.vibrate(500);
            retrofitService1 = RetrofitHelper.getRetrofit().create(RetrofitService.class);

            Call<JsonObject> call = retrofitService1.getResponse(counter_id,true);
            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Log.d("연결 성공", response.message());
                        JsonObject jsonObject = response.body();
                        Intent intent1 = new Intent(NotifyCallActivity.this, OnCallActivity.class);
                        intent1.putExtra("counter_id", counter_id);
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
            rt.stop();
            retrofitService2 = RetrofitHelper.getRetrofit().create(RetrofitService.class);

            Call<JsonObject> call = retrofitService2.getResponse(counter_id,false);

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