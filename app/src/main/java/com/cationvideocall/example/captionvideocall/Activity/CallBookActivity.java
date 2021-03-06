package com.cationvideocall.example.captionvideocall.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.captionvideocall.example.captionvideocall.R;
import com.cationvideocall.example.captionvideocall.recyclerview.CallBookListModel;
import com.cationvideocall.example.captionvideocall.MySharedPreferences;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitHelper;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitService;
import com.cationvideocall.example.captionvideocall.recyclerview.SearchResultModel;
import com.cationvideocall.example.captionvideocall.recyclerview.callbookAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CallBookActivity extends AppCompatActivity {
    private List<CallBookListModel> dataInfo;
    private SearchResultModel dataList;
    private callbookAdapter callbookAdapter;
    private RecyclerView recyclerView;
    Button newCallbook;

    RetrofitService retrofitService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_book);

        recyclerView = findViewById(R.id.recyclerview_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newCallbook = findViewById(R.id.btn_add);


        retrofitService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        String user_id = MySharedPreferences.getUserId(this);

        retrofitService = RetrofitHelper.getRetrofit().create(RetrofitService.class);

        Call<SearchResultModel> call = retrofitService.getBook(user_id);
        call.enqueue(new Callback<SearchResultModel>() {
            @Override
            public void onResponse(Call<SearchResultModel> call, Response<SearchResultModel> response) {
                if (response.isSuccessful()) {
                    Log.d("연결 성공", response.message());
//                            SearchResultModel searchWritingResult = response.body();
//                            Log.d("검색", searchWritingResult.toString());
                    dataList = response.body();
                    dataInfo = dataList.getResult();
                    if(dataInfo!=null) {
                        Log.d("전화번호북데이터인포", dataInfo.toString());
                    }else Log.d("전화번호북데이터인포", "null");
                    if (response.body().getCode()==200) {
                        callbookAdapter= new callbookAdapter(getApplicationContext(), dataInfo);
                        recyclerView.setAdapter(callbookAdapter);
                    } else {
                        dataInfo.clear();
                        callbookAdapter= new callbookAdapter(getApplicationContext(), dataInfo);
                        recyclerView.setAdapter(callbookAdapter);
                        Log.d("받아온거 없는경우다", dataInfo.toString());
                        Toast.makeText(CallBookActivity.this, "연락처가 비어있습니다.", Toast.LENGTH_SHORT).show();                    }
                } else {
                    Log.d("ssss", response.message());
                }
            }
            @Override
            public void onFailure(Call<SearchResultModel> call, Throwable t) {
                Log.d("ssss", t.getMessage());
            }
        });

        newCallbook.setOnClickListener(view -> {
            Intent intent = new Intent(CallBookActivity.this, NewCounterActivity.class);
            startActivity(intent);
            finish();
        });

    }
}