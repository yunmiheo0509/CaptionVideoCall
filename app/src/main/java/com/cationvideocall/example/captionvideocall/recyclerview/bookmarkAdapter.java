package com.cationvideocall.example.captionvideocall.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.captionvideocall.example.captionvideocall.R;

import java.util.ArrayList;

public class bookmarkAdapter extends RecyclerView.Adapter<bookmarkAdapter.ViewHolder> {

    private ArrayList<String> mData = null;

    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_bookmarkname;
//        ImageView imv_person;

        ViewHolder(View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조. (hold strong reference)
            tv_bookmarkname = itemView.findViewById(R.id.tv_bookmarkname);
//            imv_person = itemView.findViewById(R.id.imv_bookmarkperson);
        }
    }

    // 생성자에서 데이터 리스트 객체를 전달받음.
    public bookmarkAdapter(ArrayList<String> list) {
        mData = list;
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public bookmarkAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.call_book_mark, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(bookmarkAdapter.ViewHolder holder, int position) {
        String text = mData.get(position);
        holder.tv_bookmarkname.setText(text);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
//    @Override
//    public int getItemCount() {
//        return mData.size() ;
//    }
    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }
        return 0;
    }
}

