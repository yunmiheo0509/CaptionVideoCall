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
    boolean bookmark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_propose_call);
        // bind activity layout
        binding = DataBindingUtil.setContentView(this, R.layout.activity_propose_call);
        edit=false;

        binding.btnCall.setOnClickListener(view -> {
            Intent call = new Intent(ProposeCallActivity.this, WaitActivity.class);
            call.putExtra("counter_id", binding.etCounterid.getText().toString());
            startActivity(call);
        });
        binding.imvBack.setOnClickListener(view -> {
            Intent intent = new Intent(ProposeCallActivity.this, MainActivity.class);
            startActivity(intent);
        });
        binding.ibtnEdit.setOnClickListener(view -> {

            binding.btnSaveedit.setVisibility(View.VISIBLE);
            binding.ibtnEdit.setVisibility(View.INVISIBLE);
            binding.etCounterid.setFocusableInTouchMode(true);
            binding.etCallbookName.setFocusableInTouchMode(true);
            edit=true;
        });
        binding.imvStar.setOnClickListener(view -> {
            if(edit&&bookmark){
                binding.imvStar.setImageResource(R.drawable.star2);
            }else if(edit&&!bookmark){
                binding.imvStar.setImageResource(R.drawable.fullstar);
            }
        });


    }
}