package com.cationvideocall.example.captionvideocall;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.captionvideocall.example.captionvideocall.R;
import com.captionvideocall.example.captionvideocall.databinding.ActivityMainBinding;
import com.captionvideocall.example.captionvideocall.databinding.ActivityProposeCallBinding;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitHelper;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProposeCallActivity extends AppCompatActivity {
    private ActivityProposeCallBinding binding;
    RetrofitService retrofitService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_call);
        Intent intent = getIntent();
        String user_id = intent.getExtras().getString("user_id");

        // bind activity layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_propose_call);

        binding.btnCall.setOnClickListener(view -> {
            retrofitService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

            Call<JsonObject> call = retrofitService.getPropose(user_id,binding.etPhoneNum.getText().toString());

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Log.d("연결 성공", response.toString());

                        JsonObject jsonObject = response.body();
                        Log.d("바디바디", jsonObject.toString());
                        String code = jsonObject.get("code").toString();
                        Log.d("code: ",code);
                        if(!code.equals("205")){
                            String roomNum = jsonObject.getAsJsonObject("room_num").toString();
                            Log.d("room_num: ", roomNum);
                        }

                    } else {

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
}