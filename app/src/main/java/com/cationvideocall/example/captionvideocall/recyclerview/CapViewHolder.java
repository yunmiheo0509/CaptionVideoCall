package com.cationvideocall.example.captionvideocall.recyclerview;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.captionvideocall.example.captionvideocall.R;

class CapViewHolder extends RecyclerView.ViewHolder{
    public TextView textView;
    public CapViewHolder(Context context, @NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.textView);

    }
}
