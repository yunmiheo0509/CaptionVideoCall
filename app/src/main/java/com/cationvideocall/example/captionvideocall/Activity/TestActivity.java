package com.cationvideocall.example.captionvideocall.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.captionvideocall.example.captionvideocall.R;
import com.cationvideocall.example.captionvideocall.recyclerview.SimpleTextAdapter;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {
    ArrayList<String> list;
    SimpleTextAdapter adapter;
    RecyclerView recyclerView;
    Button button;
    int i;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        recyclerView = findViewById(R.id.recyclerView) ;
        button = findViewById(R.id.button);
        recyclerView.setLayoutManager(new LinearLayoutManager(this)) ;
        list = new ArrayList<>();
//        for (int i=0; i<10; i++) {
//            Log.d("데이터 리스추가","추가추가");
//            list.add(String.format("Test %d", i)) ;
//        }
        adapter = new SimpleTextAdapter(list) ;
        recyclerView.setAdapter(adapter) ;
        addCaption("내일 재밌게 놀자!!");
        i=0;
        button.setOnClickListener(view -> {
            addCaption("내일 뭐하지 "+i);
            i++;
        });





    }
    public void addCaption(String str){
        Log.d("데이터 리스추가","추가추가");
        list.add(str) ;
        Log.d("데이터 수 ","${}"+adapter.getItemCount());
        adapter.notifyDataSetChanged();
//        Log.d("데이터 수 ","${}"+adapter.getItemCount());
    }
}