package com.cationvideocall.example.captionvideocall.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.captionvideocall.example.captionvideocall.R;
import com.cationvideocall.example.captionvideocall.Activity.ProposeCallActivity;
import com.cationvideocall.example.captionvideocall.MySharedPreferences;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitHelper;
import com.cationvideocall.example.captionvideocall.Retrofit.RetrofitService;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class callbookAdapter extends RecyclerView.Adapter<callbookAdapter.ItemViewHolder> {
    private Context c;
    private List<CallBookListModel> dataList;

    public callbookAdapter(Context c, List<CallBookListModel> dataList) {
        this.c = c;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public callbookAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.callbook_list, parent, false);
        callbookAdapter.ItemViewHolder vh = new callbookAdapter.ItemViewHolder(view);
        return vh;
//        View view = LayoutInflater.from(c).inflate(R.layout.callbook_list, parent, false);
//        return new callbookAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
//        holder.onBind(listData.get(position));
        holder.counter_id.setText(dataList.get(position).getCounterId());
        holder.name.setText(dataList.get(position).getName());
        if (dataList.get(position).getBookmark()) {
            holder.bookmark.setImageResource(R.drawable.full_star);
        } else {
            holder.bookmark.setImageResource(R.drawable.star2);
        }


    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수
        if (dataList != null) {
            return dataList.size();
        }
        return 0;
    }


    // RecyclerView의 핵심인 ViewHolder
    // 여기서 subView를 setting
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView counter_id;
        private ImageButton bookmark;


        ItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name);
            counter_id = itemView.findViewById(R.id.tv_ID);
            bookmark = itemView.findViewById(R.id.ibtn_star);

            itemView.setOnClickListener(v -> {
                //writingActivity로 인텐트 전달
                int position = getAdapterPosition();
                Intent intent = new Intent(v.getContext(), ProposeCallActivity.class);

                intent.putExtra("name", dataList.get(position).getName());
                intent.putExtra("counter_id", dataList.get(position).getCounterId());
                intent.putExtra("bookmark", dataList.get(position).getBookmark());
                v.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            });
            bookmark.setOnClickListener(view -> {
                boolean setbookmark;
                int position = getAdapterPosition();
                if (dataList.get(position).getBookmark()) {
                    setbookmark = false;
                } else {
                    setbookmark = true;
                }
                String user_id = MySharedPreferences.getUserId(c);
                RetrofitService retrofitService;
                retrofitService = RetrofitHelper.getRetrofit().create(RetrofitService.class);
                Call<JsonObject> call = retrofitService.editBookMark(user_id, dataList.get(position).getCounterId(), setbookmark);
                call.enqueue(new Callback<JsonObject>() {
                    @Override
                    public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                        if (response.isSuccessful()) {
                            Log.d("연결 성공", response.message());
                            JsonObject jsonObject = response.body();
                            String code = jsonObject.get("code").toString();
                            if (code.equals("200")) {
                                if (setbookmark) {
                                    bookmark.setImageResource(R.drawable.full_star);
                                } else {
                                    bookmark.setImageResource(R.drawable.star2);
                                }
                            }
                        } else {
                            Toast.makeText(c, "북마크 변경 실패.", Toast.LENGTH_SHORT).show();
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
}
