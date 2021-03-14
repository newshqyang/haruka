package com.sw.haruka.helper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sw.haruka.R;
import com.sw.haruka.model.entity.Index;
import com.sw.haruka.helper.utils.FileUtils;
import com.sw.haruka.dal.HarukaConfig;
import com.sw.haruka.helper.utils.ListUtils;

import java.util.ArrayList;
import java.util.List;

public class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.IndexViewHolder> {

    private List<Index> mIndexList;
    private Context mContext;
    public IndexAdapter(Context context) {
        mContext = context;
    }

    /*
    初始化坐标列表
     */
    public void initIndexList(String path) {
        mIndexList = new ArrayList<>();
        addList(path);
        notifyDataSetChanged();
    }

    /*
    根据路径，添加坐标到列表中
     */
    private void addList(String path) {
        path = path.replace(HarukaConfig.ROOT_PATH, "");
        String[] folderName = path.split("/");
        StringBuilder tempPath = new StringBuilder(HarukaConfig.ROOT_PATH);
        for (String fn : folderName) {
            if (fn.equals("")) {
                addIndex(tempPath.toString());
                continue;
            }
            tempPath.append("/").append(fn);
            addIndex(tempPath.toString());
        }
    }

    /*
    删除最后一个坐标
     */
    public void removeLastIndex() {
        ListUtils.removeLast(mIndexList);
        notifyDataSetChanged();
    }

    /*
    获取坐标列表
     */
    public List<Index> getIndexList() {
        return mIndexList;
    }

    /*
    打开文件夹时，需要在坐标列表中加入
     */
    public void addIndex(String path) {
        mIndexList.add(new Index(path));
    }

    /*
    获取最后一个坐标，也就是当前坐标
     */
    public Index getCurrentIndex() {
        return mIndexList.get(mIndexList.size() - 1);
    }

    @Override
    public void onBindViewHolder(@NonNull final IndexViewHolder holder, final int position) {
        Index index = mIndexList.get(position);
        String folderName = FileUtils.getLastFolderOrFileName(index.getPath().replace(HarukaConfig.ROOT_PATH, ""));
        if (folderName.length() < 1) {
            folderName = HarukaConfig.ROOT_NAME;
        }
        holder.indexName.setText(folderName);
        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIndexList.get(mIndexList.size() - 1);  // 当用户点击坐标列表的目录时，坐标列表会删除该坐标后面的坐标
                    notifyDataSetChanged();
                    int position = holder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mIndexList.size();
    }
    @NonNull
    @Override
    public IndexViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new IndexViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_index, parent, false));
    }
    public class IndexViewHolder extends RecyclerView.ViewHolder{
        private TextView indexName;
        public IndexViewHolder(@NonNull View itemView) {
            super(itemView);
            indexName = itemView.findViewById(R.id.textView_index);
        }
    }
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
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
}
