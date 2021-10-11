package com.cationvideocall.example.captionvideocall.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.captionvideocall.example.captionvideocall.R;
import com.captionvideocall.example.captionvideocall.databinding.ActivityMainBinding;
import com.cationvideocall.example.captionvideocall.recyclerview.CallBookListModel;
import com.cationvideocall.example.captionvideocall.MySharedPreferences;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitHelper;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitService;
import com.cationvideocall.example.captionvideocall.recyclerview.SearchResultModel;
import com.cationvideocall.example.captionvideocall.recyclerview.bookmarkAdapter;
import com.cationvideocall.example.captionvideocall.recyclerview.callbookAdapter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    bookmarkAdapter bookmarkAdapter;
    RetrofitService retrofitService;
    private List<CallBookListModel> dataInfo;
    private SearchResultModel dataList;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        textView = binding.tvNosearchresult;

        //북마크 부분 화면에 나타내기
        binding.recyclerviewPerson.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        getBookMarked();


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
            finish();
        });
//        binding.person2.setOnClickListener(view -> {
//            Intent intent = new Intent(MainActivity.this, TestActivity.class);
//            startActivity(intent);
//        });

        // 아이디로 전화걸기
        binding.btnCon.setOnClickListener(view -> {
            if (!binding.edtId.getText().toString().equals("")) {
                Intent intent = new Intent(MainActivity.this, WaitActivity.class);
                intent.putExtra("counter_id", binding.edtId.getText().toString());
                startActivity(intent);
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


    @Override
    protected void onRestart() {
        super.onRestart();
        getBookMarked();
    }

    void getBookMarked() {
        retrofitService = RetrofitHelper.getRetrofit().create(RetrofitService.class);
        String user_id = MySharedPreferences.getUserId(this);

        Call<SearchResultModel> call = retrofitService.getBookMark(user_id);
        call.enqueue(new Callback<SearchResultModel>() {
            @Override
            public void onResponse(Call<SearchResultModel> call, Response<SearchResultModel> response) {
                if (response.isSuccessful()) {
                    Log.d("연결 성공", response.message());
//                            SearchResultModel searchWritingResult = response.body();
//                            Log.d("검색", searchWritingResult.toString());
                    dataList = response.body();
                    dataInfo = dataList.getResult();

                    if (dataInfo != null) {
                        Log.d("전화번호북데이터인포", dataInfo.toString());
                    } else Log.d("전화번호북데이터인포", "null");
                    if (response.body().getCode() == 200) {
                        bookmarkAdapter = new bookmarkAdapter(getApplicationContext(), dataInfo);
                        binding.recyclerviewPerson.setAdapter(bookmarkAdapter);

                    } else {
                        dataInfo.clear();
                        bookmarkAdapter = new bookmarkAdapter(getApplicationContext(), dataInfo);
                        binding.recyclerviewPerson.setAdapter(bookmarkAdapter);
                        Log.d("받아온거 없는경우다", dataInfo.toString());
                    }
                } else {
                    Log.d("ssss", response.message());
                }
            }

            @Override
            public void onFailure(Call<SearchResultModel> call, Throwable t) {
                Log.d("ssss", t.getMessage());
            }
        });
    }
}