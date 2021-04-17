package com.shqyang.yexplorer;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.listener.OnItemLongClickListener;
import com.shqyang.yexplorer.adapter.FileAdapter;
import com.shqyang.yexplorer.adapter.RouteAdapter;
import com.shqyang.yexplorer.util.FileUtils;
import com.shqyang.yexplorer.util.PermissionUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

/**
 * Created by swsbty on 2021/04/17
 */
public class ExplorerFragment extends Fragment implements FragmentStatus {

    private final String TAG = getClass().getName();

    private Context mContext;

    private View mRootView;
    private RecyclerView mRouteRv;
    private RecyclerView mExplorerRv;
    private TextView mEmptyTv;

    private FileClickAction mClickAction;

    private RouteAdapter mRouteAdapter;
    private FileAdapter mFileAdapter;
    private List<File> mFileList;
    private String mRootPath;
    private Stack<File> mRecentList;

    private boolean mHasPermission = false;

    public ExplorerFragment(String rootPath) {
        mRootPath = rootPath;
    }

    public void setClickAction(FileClickAction clickAction) {
        mClickAction = clickAction;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getContext();
        mRootView = inflater.inflate(R.layout.explorer_fragment, container, false);
        initView();
        initEvent();
        loadData(true);
        return mRootView;
    }

    /**
     * set explorer root path
     * @param rootPath  explorer root path
     */
    private void setRootPath(String rootPath) {
        if (!mHasPermission) {
            Log.i(TAG, "setRootPath: no permission");
            return;
        }
        File file = new File(rootPath);
        if (!file.exists() || !file.isDirectory()) {
            Toast.makeText(mContext, R.string.rootPathError, Toast.LENGTH_SHORT).show();
            return;
        }
        mRecentList.push(file);
        File[] files = file.listFiles();
        if (files == null) {
            Toast.makeText(mContext, R.string.rootPathError, Toast.LENGTH_SHORT).show();
            return;
        }
        if (mFileList.size() > 0) {
            mFileList.clear();
        }
        mFileList.addAll(Arrays.asList(files));
        FileUtils.sortFileByName(mFileList);
        mFileAdapter.notifyDataSetChanged();
        checkEmpty();
    }

    /**
     * check if file list is empty, show empty textView if empty
     */
    private void checkEmpty() {
        if (mFileList.size() == 0) {
            mEmptyTv.setVisibility(View.VISIBLE);
        } else {
            mEmptyTv.setVisibility(View.GONE);
        }
    }

    /**
     * refresh route recyclerView and adapter
     */
    private void refreshRoute() {
        mRouteAdapter.notifyDataSetChanged();
        mRouteRv.scrollToPosition(mRecentList.size() - 1);
    }

    /**
     * refresh explorer recyclerView and adapter
     * @param file directory
     */
    private void refreshExplorer(File file) {
        if (!file.exists() || !file.isDirectory()) {
            return;
        }
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        if (mFileList.size() > 0) {
            mFileList.clear();
        }
        mFileList.addAll(Arrays.asList(files));
        FileUtils.sortFileByName(mFileList);
        mFileAdapter.notifyDataSetChanged();
        checkEmpty();
    }

    @Override
    public void initView() {
        mHasPermission = PermissionUtils.hasReadFile(getActivity());
        mRouteRv = mRootView.findViewById(R.id.route_rv);
        mRouteRv.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mRecentList = new Stack<>();
        mRouteAdapter = new RouteAdapter(mRecentList);
        mRouteRv.setAdapter(mRouteAdapter);
        mExplorerRv = mRootView.findViewById(R.id.explorer_rv);
        mExplorerRv.setLayoutManager(new LinearLayoutManager(mContext));
        mFileList = new ArrayList<>();
        mFileAdapter = new FileAdapter(mFileList);
        mExplorerRv.setAdapter(mFileAdapter);
        mEmptyTv = mRootView.findViewById(R.id.notice_tv);
    }

    @Override
    public void initEvent() {
        mRouteAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                int t = mRecentList.size() - position - 1;
                while (t > 0) {
                    mRecentList.pop();
                    t--;
                }
                File file = mRecentList.get(position);
                refreshRoute();
                refreshExplorer(file);
            }
        });
        mFileAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
                if (mFileAdapter.isCheckMode()) {
                    mFileAdapter.check(position);
                    List<File> cfl = getCheckedFileList();
                    Log.i(TAG, "onItemClick: " + cfl.size());
                    for (File f : cfl) {
                        Log.i(TAG, "onItemClick: file " + f.getName());
                    }
                    return;
                }
                File file = mFileList.get(position);
                if (file.isDirectory()) {
                    mRecentList.push(file);
                    refreshRoute();
                    refreshExplorer(file);
                } else {
                    mClickAction.openFile(file);
                }
            }
        });
        mFileAdapter.setOnItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(@NonNull BaseQuickAdapter adapter, @NonNull View view, int position) {
                mFileAdapter.setCheckMode(true, position);
                return false;
            }
        });
    }


    /**
     * get checked file list
     * @return checked file list
     */
    public List<File> getCheckedFileList() {
        if (!mFileAdapter.isCheckMode() || mFileList.size() == 0) {
            return null;
        }
        boolean[] checkArr = mFileAdapter.getCheckedArray();
        List<File> checkedFileList = new ArrayList<>();
        for (int i = 0;i < checkArr.length;i++) {
            if (!checkArr[i]) {
                continue;
            }
            checkedFileList.add(mFileList.get(i));
        }
        return checkedFileList;
    }

    /**
     * back to previous directory
     * @return false if no previous directory
     */
    public boolean back2previous() {
        if (mFileAdapter.isCheckMode()) {
            mFileAdapter.setCheckMode(false);
            return true;
        }
        if (mRecentList.size() == 1) {
            return false;
        }
        mRecentList.pop();
        refreshRoute();
        File file = mRecentList.get(mRecentList.size() - 1);
        refreshExplorer(file);
        return true;
    }

    @Override
    public void loadData(boolean isRefresh) {
        if (isRefresh) {
            mFileList.clear();
        }
        setRootPath(mRootPath);
    }


    public interface FileClickAction {
        void openFile(File file);
    }
}
