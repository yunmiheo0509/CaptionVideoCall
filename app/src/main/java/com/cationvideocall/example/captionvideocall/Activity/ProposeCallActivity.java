package com.cationvideocall.example.captionvideocall.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_call);
        // bind activity layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_propose_call);

        binding.btnCall.setOnClickListener(view -> {
            Intent call = new Intent(ProposeCallActivity.this, WaitActivity.class);
            call.putExtra("counter_id", binding.etCounterid.getText().toString());
            startActivity(call);
        });
        binding.imvBack.setOnClickListener(view -> {
            Intent intent = new Intent(ProposeCallActivity.this, MainActivity.class);
            startActivity(intent);
        });

    }
}