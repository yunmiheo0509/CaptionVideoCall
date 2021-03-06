package com.cationvideocall.example.captionvideocall.Activity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.JsonObject;
import com.captionvideocall.example.captionvideocall.R;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitHelper;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitService;
import com.captionvideocall.example.captionvideocall.databinding.ActivitySignupBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    private ActivitySignupBinding binding;
    private RetrofitService retrofitService;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        token = task.getResult();
                    }
                });

        binding.btnSignup.setOnClickListener(view -> {
            retrofitService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

            Call<JsonObject> call = retrofitService.getRegister(binding.etId.getText().toString(),
                    binding.etPw.getText().toString(),token);

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                    if (response.isSuccessful()) {
                        Log.d("?????? ??????", response.message());
                        JsonObject jsonObject = response.body();
                        Log.d("??????????????????:", jsonObject.toString());
                        Toast.makeText(SignupActivity.this, "???????????? ???????????????.", Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(SignupActivity.this, "???????????? ??????????????? ??????????????????"
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
    }
}