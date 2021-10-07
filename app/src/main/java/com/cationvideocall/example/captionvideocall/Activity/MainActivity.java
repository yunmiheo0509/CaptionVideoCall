package com.cationvideocall.example.captionvideocall.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.captionvideocall.example.captionvideocall.R;
import com.captionvideocall.example.captionvideocall.databinding.ActivityMainBinding;
import com.cationvideocall.example.captionvideocall.MySharedPreferences;
import com.cationvideocall.example.captionvideocall.recyclerview.SimpleTextAdapter;
import com.cationvideocall.example.captionvideocall.recyclerview.bookmarkAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    ArrayList<String> list;
    bookmarkAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        list = new ArrayList<>();
        binding.recyclerviewPerson.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)) ;
        adapter = new bookmarkAdapter(list) ;
        binding.recyclerviewPerson.setAdapter(adapter);

        addCaption("막내 아들");
        addCaption("둘째 아들");
        addCaption("첫째 딸");
        addCaption("첫째 딸");
        addCaption("첫째 딸");
        addCaption("첫째 딸");
        addCaption("첫째 딸");
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
        binding.person2.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, TestActivity.class);
            startActivity(intent);
        });


    }
    public void addCaption(String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                list.add(str) ;
                binding.recyclerviewPerson.smoothScrollToPosition(binding.recyclerviewPerson.getAdapter().getItemCount() - 1);
                adapter.notifyDataSetChanged();
            }
        });
    }
}