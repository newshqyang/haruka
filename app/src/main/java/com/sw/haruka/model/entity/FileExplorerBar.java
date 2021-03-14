package com.sw.haruka.model.entity;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.baoyz.widget.PullRefreshLayout;
import com.sw.haruka.R;
import com.sw.haruka.model.FileCore;
import com.sw.haruka.helper.adapter.ExplorerAdapter;

import java.io.File;
import java.util.List;

public class FileExplorerBar extends FrameLayout {

    private ExplorerAdapter mExplorerAdapter;
    private RecyclerView mFileExplorerRV;
    private PullRefreshLayout mPullRefreshLayout;
    private FileCore mFileCore;
    protected LinearLayoutManager mManager;
    protected Context mContext;
    protected void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.explorer_bar, this);
        mFileExplorerRV = findViewById(R.id.recyclerView_explorer);
        mManager = new LinearLayoutManager(context);
        mFileExplorerRV.setLayoutManager(mManager);
        mPullRefreshLayout = findViewById(R.id.pullRefreshLayout);
        mPullRefreshLayout.setOnRefreshListener(new PullRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mFileCore.refresh();
            }
        });
    }

    /**
     * 停止刷新
     */
    public void stopRefresh() {
        mPullRefreshLayout.setRefreshing(false);
    }

    public void setFileCore(FileCore fileCore) {
        mFileCore = fileCore;
    }

    public RecyclerView getFileExplorerRV() {
        return mFileExplorerRV;
    }

    /*
        根据坐标，让文件浏览器刷新
         */
    public void refresh(Index index) {
        mExplorerAdapter.selectCancel();
        mExplorerAdapter.refreshFileList(index.getPath());
        showEmptyView();
        mFileExplorerRV.scrollToPosition(index.getPosition());
        LinearLayoutManager manager = (LinearLayoutManager) mFileExplorerRV.getLayoutManager();
        manager.scrollToPositionWithOffset(index.getPosition(), 0);
        stopRefresh();
    }

    public void refresh(List<File> fileList) {
        mExplorerAdapter.setFileList(fileList);
        showEmptyView();
    }

    /*
    展示空视图
     */
    public void showEmptyView() {
        if (mExplorerAdapter.getFileListSize() == 0) {    // 如果文件夹展开后没有文件，就让背景透明，显示空文字
            mFileExplorerRV.setBackgroundColor(Color.TRANSPARENT);
        } else {
            mFileExplorerRV.setBackgroundColor(mContext.getColor(R.color.recyclerView_background));
        }
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mExplorerAdapter = (ExplorerAdapter) adapter;
        mFileExplorerRV.setAdapter(adapter);
    }
    public FileExplorerBar(@NonNull Context context) {
        super(context);
        init(context);
    }
    public FileExplorerBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public FileExplorerBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }
}
