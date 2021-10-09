package com.cationvideocall.example.captionvideocall.recyclerview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.captionvideocall.example.captionvideocall.R;
import com.cationvideocall.example.captionvideocall.Activity.ProposeCallActivity;

import java.util.List;

public class bookmarkAdapter extends RecyclerView.Adapter<bookmarkAdapter.ItemViewHolder> {

    private Context c;
    private List<CallBookListModel> dataList;

    public bookmarkAdapter(Context c, List<CallBookListModel> dataList) {
        this.c = c;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public bookmarkAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.call_book_mark, parent, false);
        ItemViewHolder vh = new ItemViewHolder(view);
        return vh;
//        View view = LayoutInflater.from(c).inflate(R.layout.call_book_mark, parent, false);
//        return new bookmarkAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final bookmarkAdapter.ItemViewHolder holder, final int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수
//        holder.onBind(listData.get(position));
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


        ItemViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.et_callbookName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //writingActivity로 인텐트 전달
                    int position = getAdapterPosition();
                    Intent intent = new Intent(v.getContext(), ProposeCallActivity.class);

                    intent.putExtra("name", dataList.get(position).getName());
                    intent.putExtra("counter_id", dataList.get(position).getCounterId());
                    intent.putExtra("bookmark", dataList.get(position).getBookmark());
                    v.getContext().startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                }
            });
        }
    }
}

