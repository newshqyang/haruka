package com.sw.haruka.helper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sw.haruka.R;
import com.sw.haruka.model.entity.CollectionViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class CollectionAdapter extends RecyclerView.Adapter<CollectionViewHolder> {

    private Context mContext;
    private List<String> mFolderList;
    public CollectionAdapter(Context context, Set<String> folderSet) {
        mContext = context;
        String[] array = (String[]) folderSet.toArray();
        mFolderList = new ArrayList<>(Arrays.asList(array));
    }

    @NonNull
    @Override
    public CollectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CollectionViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_collection, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull final CollectionViewHolder holder, int position) {
        String path = mFolderList.get(position);
        if (path.endsWith("/")) {
            path = path.substring(0, path.length() - 1);
        }
        String name = path.split("/")[path.split("/").length - 1];
        holder.mCollectionFolder.setText(name);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View holder, int position);
    }
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }
    private OnItemLongClickListener mOnItemLongClickListener;
    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }
    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }
    @Override
    public int getItemCount() {
        return mFolderList.size();
    }
}
