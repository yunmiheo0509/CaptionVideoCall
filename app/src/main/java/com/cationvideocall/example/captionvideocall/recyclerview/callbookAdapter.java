package com.cationvideocall.example.captionvideocall.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.captionvideocall.example.captionvideocall.R;
import com.cationvideocall.example.captionvideocall.Activity.ProposeCallActivity;
import com.cationvideocall.example.captionvideocall.CallBookListModel;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class callbookAdapter extends RecyclerView.Adapter<callbookAdapter.ItemViewHolder>{
    private Context c;
    private List<CallBookListModel> dataList;

    public callbookAdapter(Context c, List<CallBookListModel> dataList) {
        this.c = c;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public callbookAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.callbook_list, parent, false);
        return new callbookAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
//        holder.onBind(listData.get(position));
        holder.counter_id.setText(dataList.get(position).getCounter_id());
        holder.name.setText(dataList.get(position).getName());

    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수
        return dataList.size();
    }


    // RecyclerView의 핵심인 ViewHolder
    // 여기서 subView를 setting
    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView counter_id;


        ItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name);
            counter_id = itemView.findViewById(R.id.tv_ID);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //writingActivity로 인텐트 전달
                    int position = getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), ProposeCallActivity.class);

                    intent.putExtra("name", dataList.get(position).getName());
                    intent.putExtra("counter_id", dataList.get(position).getCounter_id());
                    intent.putExtra("bookmark", dataList.get(position).getBookmark());
                    v.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }
    }
}