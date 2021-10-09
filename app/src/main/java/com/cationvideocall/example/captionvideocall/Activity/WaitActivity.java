package com.cationvideocall.example.captionvideocall.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.captionvideocall.example.captionvideocall.R;
import com.captionvideocall.example.captionvideocall.databinding.ActivityWaitBinding;
import com.cationvideocall.example.captionvideocall.MySharedPreferences;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitHelper;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WaitActivity extends AppCompatActivity {
    RetrofitService retrofitService;
    ActivityWaitBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wait);

        String user_id = MySharedPreferences.getUserId(this);
        Log.d("로그인한 사용자 아이디: ", user_id);

        Intent getCounter = getIntent();
        String counter_id=getCounter.getExtras().getString("counter_id");

        retrofitService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

//        //테스트용으로 kk직접 할당
//        String counter_id="test";

        Log.d("상대방 아이디", counter_id);
        Call<JsonObject> call = retrofitService.getPropose(user_id, counter_id);

        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    Log.d("연결 성공", response.toString());

                    JsonObject jsonObject = response.body();
                    Log.d("바디바디", jsonObject.toString());
                    String code = jsonObject.get("code").toString();
                    Log.d("code: ",code);
                    if(code.equals("200")){
                        String room_num = jsonObject.get("room_num").toString();
                        Log.d("room_num: ", room_num);
                        Intent intent1 = new Intent(WaitActivity.this, OnCallActivity.class);
                        intent1.putExtra("counter_id", counter_id);
                        intent1.putExtra("room_num", room_num);
                        startActivity(intent1);
                        finish();
                    }else if(code.equals("201")){
                        // 거절당함
                        Toast.makeText(WaitActivity.this, "상대방이 통화를 할 수 없습니다", Toast.LENGTH_SHORT).show();
                        Intent fail = new Intent(WaitActivity.this,MainActivity.class);
                        startActivity(fail);
                        finish();
                    }else{
                        //코드 204 타임아웃
                        Toast.makeText(WaitActivity.this, "전화가 타임아웃 되었습니다", Toast.LENGTH_SHORT).show();
                        Intent fail = new Intent(WaitActivity.this,MainActivity.class);
                        startActivity(fail);
                        finish();
                    }
                } else {
                    Log.d("오류발생", response.message());
                    Intent fail = new Intent(WaitActivity.this,MainActivity.class);
                    startActivity(fail);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                Log.d("통신실패", t.getMessage());
                Intent fail = new Intent(WaitActivity.this,MainActivity.class);
                startActivity(fail);
                finish();
            }
        });

        binding.imvRejectCall.setOnClickListener(view -> {
            Toast.makeText(WaitActivity.this, "통화를 취소하셨습니다.", Toast.LENGTH_SHORT).show();

            Call<JsonObject> cancelCall = retrofitService.cancelCall(user_id);

            cancelCall.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Log.d("연결 성공", response.toString());
                        JsonObject jsonObject = response.body();
                        String code = jsonObject.get("code").toString();
                        Log.d("code: ", code);
                    }else{
                        Log.d("연결 실패", response.toString());
                    }
                }
                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    Log.d("통신실패", t.getMessage());
                }
            });

            finish();
        });
    }
}