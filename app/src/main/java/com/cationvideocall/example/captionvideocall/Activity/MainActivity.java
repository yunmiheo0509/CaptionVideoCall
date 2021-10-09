package com.cationvideocall.example.captionvideocall.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.captionvideocall.example.captionvideocall.R;
import com.captionvideocall.example.captionvideocall.databinding.ActivityMainBinding;
import com.cationvideocall.example.captionvideocall.MySharedPreferences;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitHelper;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitService;
import com.cationvideocall.example.captionvideocall.recyclerview.SimpleTextAdapter;
import com.cationvideocall.example.captionvideocall.recyclerview.bookmarkAdapter;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    ArrayList<String> list;
    bookmarkAdapter adapter;
    RetrofitService retrofitService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        list = new ArrayList<>();
        binding.recyclerviewPerson.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        adapter = new bookmarkAdapter(list);
        binding.recyclerviewPerson.setAdapter(adapter);

        addCaption("막내 아들");
        addCaption("둘째 아들");
        addCaption("첫째 딸");
        addCaption("첫째 딸");
        addCaption("첫째 딸");
        addCaption("첫째 딸");
        addCaption("첫째 딸");

        binding.tvMyName.setText(MySharedPreferences.getUserId(MainActivity.this));

        binding.person1.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, ProposeCallActivity.class);
            startActivity(intent);
//            finish();
        });
        binding.imvLogout.setOnClickListener(view -> {
            // 로그아웃 진행
            MySharedPreferences.clearUser(getApplicationContext());
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
//            finish();
        });
//        binding.person2.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, TestActivity.class);
//            startActivity(intent);
//        });

        // 아이디로 전화걸기
        binding.btnCon.setOnClickListener(view -> {
            if (!binding.edtId.getText().toString().equals("")) {
                Intent call = new Intent(MainActivity.this, WaitActivity.class);
                call.putExtra("counter_id", binding.edtId.getText().toString());
                startActivity(call);
            } else {
                Toast.makeText(MainActivity.this, "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
            }

        });
        binding.ivCallbook.setOnClickListener(view -> {
            Intent callbook = new Intent(MainActivity.this, CallBookActivity.class);
            startActivity(callbook);
        });
        binding.imvAddcounter.setOnClickListener(view -> {
            Intent addCount = new Intent(MainActivity.this, NewCounterActivity.class);
            startActivity(addCount);
        });
    }

    public void addCaption(String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.add(str);
                adapter.notifyDataSetChanged();
            }
        });
    }
}