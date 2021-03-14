package com.sw.haruka.model.entity;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sw.haruka.R;

public class CollectionViewHolder extends RecyclerView.ViewHolder {
    public CollectionViewHolder(@NonNull View itemView) {
        super(itemView);
        mCollectionFolder = itemView.findViewById(R.id.collection_folder);
    }

    public TextView mCollectionFolder;
}
