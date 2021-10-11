package com.cationvideocall.example.captionvideocall.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.captionvideocall.example.captionvideocall.R;

import com.captionvideocall.example.captionvideocall.databinding.ActivityProposeCallBinding;
import com.cationvideocall.example.captionvideocall.MySharedPreferences;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitHelper;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitService;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProposeCallActivity extends AppCompatActivity {
    private ActivityProposeCallBinding binding;
    boolean edit;
    RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_call);
        // bind activity layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_propose_call);
        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");
        String counter_id = intent.getExtras().getString("counter_id");
        boolean bookmark= intent.getExtras().getBoolean("bookmark");

        binding.etCallbookName.setText(name);
        binding.etCounterid.setText(counter_id);
        edit=false;

        binding.btnCall.setOnClickListener(view -> {
            Intent call = new Intent(ProposeCallActivity.this, WaitActivity.class);
            call.putExtra("counter_id", binding.etCounterid.getText().toString());
            call.putExtra("name", binding.etCallbookName.getText().toString());
            startActivity(call);
        });

        binding.imvBack.setOnClickListener(view -> {
            Intent intent1 = new Intent(ProposeCallActivity.this, MainActivity.class);
            startActivity(intent1);
        });
        binding.ibtnEdit.setOnClickListener(view -> {

            binding.btnSaveedit.setVisibility(View.VISIBLE);
            binding.ibtnEdit.setVisibility(View.INVISIBLE);
            binding.etCounterid.setFocusableInTouchMode(true);
            binding.etCallbookName.setFocusableInTouchMode(true);
            binding.btnCall.setVisibility(View.INVISIBLE);
            edit=true;
        });
        binding.btnSaveedit.setOnClickListener(view -> {
            String et_counter_id = binding.etCounterid.getText().toString();
            String et_name = binding.etCallbookName.getText().toString();
            String user_id =MySharedPreferences.getUserId(this);

            if(!counter_id.equals("")&&!name.equals("")){
                retrofitService = RetrofitHelper.getRetrofit().create(RetrofitService.class);
                Call<JsonObject> call = retrofitService.editBook(user_id,et_counter_id, et_name);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            Log.d("연결 성공", response.message());
                            JsonObject jsonObject = response.body();
                            Toast.makeText(ProposeCallActivity.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ProposeCallActivity.this, "아이디 또는 이름을 입력해주세요", Toast.LENGTH_SHORT).show();
            }

            binding.btnCall.setVisibility(View.VISIBLE);
            binding.btnSaveedit.setVisibility(View.INVISIBLE);
            binding.ibtnEdit.setVisibility(View.VISIBLE);
        });


    }
}