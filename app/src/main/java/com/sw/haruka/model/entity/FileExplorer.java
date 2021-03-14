package com.sw.haruka.model.entity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.sw.haruka.R;
import com.sw.haruka.dal.HarukaConfig;
import com.sw.haruka.dal.Namespace;
import com.sw.haruka.model.FileCore;
import com.sw.haruka.helper.adapter.IndexAdapter;
import com.sw.haruka.ui.search.SearchCore;
import com.sw.haruka.helper.utils.SPUtils;
import com.sw.haruka.helper.utils.FileUtils;
import com.sw.haruka.helper.utils.ToastUtils;

import java.util.Set;

public class FileExplorer extends ConstraintLayout {

    private IndexBar mIndexBar;
    private IndexAdapter mIndexAdapter;
    private FileExplorerBase mExplorer;
    private StorageBar mStorageBar;
    private Context mContext;

    /**
     * 设置是否显示多选工具栏
     * @param noSelectTool  是否显示
     */
    public void setNoSelectTool(boolean noSelectTool) {
        mExplorer.setNoSelectTool(noSelectTool);
    }

    private boolean mNoStorageBar = false;  // 不使用空间容量功能
    public void setNoStorageBar(boolean noStorageBar) {
        mNoStorageBar = noStorageBar;
        if (mNoStorageBar) {
            mStorageBar.setVisibility(GONE);
        }
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_explorer_bar, this, true);
        mIndexBar = findViewById(R.id.index);
        mExplorer = findViewById(R.id.explorerBar);
        mIndexAdapter = new IndexAdapter(context);
        mIndexAdapter.setOnItemClickListener(new IndexAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                mIndexBar.refresh(position);
                mExplorer.refresh(mIndexAdapter.getCurrentIndex());
                mIndexBar.switchCollectionIcon();
                refreshCollection();
                mExplorer.resume();
            }
        });
        mStorageBar = findViewById(R.id.storageBar);
        mExplorer.getFileExplorerRV().addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                boolean canScrollUp = mExplorer.getFileExplorerRV().canScrollVertically(-1);
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {   // 当不滚动时记录位置
                    int position = mExplorer.findFirstVisibleItemPosition();
                    mIndexAdapter.getCurrentIndex().setPosition(position);
                    storageBarVisibilityOperate(canScrollUp);
                } else {
                    storageBarVisibilityOperate(canScrollUp);
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
        mExplorer.setSearchCore(new SearchCore() {
            @Override
            public void openFolder(String path) {
                mIndexAdapter.addIndex(path);
                mIndexAdapter.notifyDataSetChanged();
                mExplorer.refresh(mIndexAdapter.getCurrentIndex());
                mIndexBar.switchCollectionIcon();
                refreshCollection();
            }
        });
        mExplorer.setFileCore(new FileCore() {
            @Override
            public void refresh() {
                FileExplorer.this.refresh();
            }
        });
    }

    /*
    获取当前路径
     */
    public String getCurrentPath() {
        return mIndexAdapter.getCurrentIndex().getPath();
    }

    /*
    空间模块显示状态
     */
    private void storageBarVisibilityOperate(boolean canScrollUp) {
        if (mNoStorageBar) {
            return;
        }
        if (!canScrollUp) {
            mStorageBar.show();
        } else {
            mStorageBar.hidden();
        }
    }

    /*
    初始化文件浏览器模块的坐标列表和文件列表
     */
    public void initList(String path) {
        mIndexAdapter.initIndexList(path);
        mIndexBar.setAdapter(mIndexAdapter);
        mExplorer.refresh(mIndexAdapter.getCurrentIndex());
        mIndexBar.switchCollectionIcon();
        refreshCollection();
    }

    /*
    返回上一级
     */
    public boolean back() {
        if (mExplorer.back()) {
            return true;
        }
        if (mIndexAdapter.getIndexList().size() <= 1) {
            return false;
        }
        mIndexAdapter.removeLastIndex();
        mExplorer.refresh(mIndexAdapter.getCurrentIndex());
        mIndexBar.switchCollectionIcon();
        refreshCollection();
        return true;
    }

    /*
    刷新
     */
    public void refresh() {
        mIndexBar.refresh();
        mExplorer.refresh(mIndexAdapter.getCurrentIndex());
    }

    /*
    刷新收藏夹
     */
    public void refreshCollection() {
        mIndexBar.getCollection().setOnClickListener(new View.OnClickListener() {
            private Set<String> mFolderSet;
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(mContext, mIndexBar.getCollection());
                mFolderSet = SPUtils.getSetDefault0(Namespace.COLLECTION_SET);
                String path = mIndexAdapter.getCurrentIndex().getPath();
                if (!path.equals(HarukaConfig.ROOT_PATH)
                        && !mFolderSet.contains(path)) {
                    MenuItem item = menu.getMenu().add("☆收藏当前路径");
                    SubMenu subMenu = menu.getMenu().addSubMenu("收藏夹");
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            mFolderSet.add(mIndexAdapter.getCurrentIndex().getPath());
                            SPUtils.saveSet(Namespace.COLLECTION_SET, mFolderSet);
                            refresh();
                            mIndexBar.getCollection().setImageDrawable(mContext.getDrawable(R.drawable.collected));
                            ToastUtils.toastS("已收藏");
                            return false;
                        }
                    });
                    refreshMenuItem(subMenu);
                } else {
                    refreshMenuItem(menu.getMenu());
                }
                menu.show();
            }

            private void refreshMenuItem(Menu m) {
                for (String path : mFolderSet) {
                    if (path.equals(mIndexAdapter.getCurrentIndex().getPath())) {
                        continue;
                    }
                    String name = FileUtils.getNameFromPath(path);
                    MenuItem item = m.add(name);
                    final String finalPath = path;
                    item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            initList(finalPath);
                            return false;
                        }
                    });
                }
            }
        });
    }


    public FileExplorer(@NonNull Context context) {
        super(context);
        init(context);
    }
    public FileExplorer(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
}
