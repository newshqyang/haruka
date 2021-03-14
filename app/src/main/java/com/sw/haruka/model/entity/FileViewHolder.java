package com.sw.haruka.model.entity;

import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sw.haruka.R;

public class FileViewHolder extends RecyclerView.ViewHolder {
    public ImageView type;
    public ImageView operate;
    public TextView name;
    public TextView updateDateTime;
    public CheckBox checkBox;
    public FileViewHolder(@NonNull View itemView) {
        super(itemView);
        type = itemView.findViewById(R.id.imageView_type);
        operate = itemView.findViewById(R.id.imageView_operate);
        name = itemView.findViewById(R.id.textView_name);
        updateDateTime = itemView.findViewById(R.id.textView_updateDate);
        checkBox = itemView.findViewById(R.id.checkBox);
    }
}
