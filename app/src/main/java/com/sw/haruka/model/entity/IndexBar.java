package com.sw.haruka.model.entity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sw.haruka.R;
import com.sw.haruka.dal.HarukaConfig;
import com.sw.haruka.dal.Namespace;
import com.sw.haruka.helper.adapter.IndexAdapter;
import com.sw.haruka.helper.utils.SPUtils;
import com.sw.haruka.helper.utils.ListUtils;


public class IndexBar extends ConstraintLayout {

    private IndexAdapter mAdapter;
    private RecyclerView mIndexRV;
    private ImageView mCollection;
    private Context mContext;
    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_index_bar, this);
        mIndexRV = findViewById(R.id.recyclerView_index);
        LinearLayoutManager indexLinearLayoutManager = new LinearLayoutManager(context);
        indexLinearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        mIndexRV.setLayoutManager(indexLinearLayoutManager);
        mCollection = findViewById(R.id.collection);
    }

    /*
    刷新坐标列表组件
     */
    public void refresh() {
        mAdapter.notifyDataSetChanged();
    }

    public void refresh(int position) {
        ListUtils.removeLastByPosition(mAdapter.getIndexList(), position);
        mAdapter.notifyDataSetChanged();
    }

    /*
        切换收藏图标
         */
    public void switchCollectionIcon() {
        String path = mAdapter.getCurrentIndex().getPath();
        if (path.equals(HarukaConfig.ROOT_PATH)
                || SPUtils.getSetDefault0(Namespace.COLLECTION_SET).contains(path)) {
            mCollection.setImageDrawable(mContext.getDrawable(R.drawable.collected));
        } else {
            mCollection.setImageDrawable(mContext.getDrawable(R.drawable.collect));
        }
        System.out.println("日志：" + path);
        System.out.println("日志：2   " + HarukaConfig.ROOT_PATH);
    }

    public ImageView getCollection() {
        return mCollection;
    }
    public void setAdapter(RecyclerView.Adapter adapter) {
        mAdapter = (IndexAdapter) adapter;
        mIndexRV.setAdapter(adapter);
    }
    public IndexBar(@NonNull Context context) {
        super(context);
        init(context);
    }
    public IndexBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public IndexBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
}
