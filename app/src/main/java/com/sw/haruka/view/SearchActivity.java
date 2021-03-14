package com.sw.haruka.view;

import androidx.annotation.NonNull;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sw.haruka.R;
import com.sw.haruka.dal.Namespace;
import com.sw.haruka.model.BaseStatusActivity;
import com.sw.haruka.model.FileCore;
import com.sw.haruka.model.entity.FileExplorerBase;
import com.sw.haruka.ui.search.SearchCore;
import com.sw.haruka.helper.utils.SPUtils;
import com.sw.haruka.helper.utils.AndroidFileUtils;
import com.sw.haruka.helper.utils.FileUtils;
import com.sw.haruka.dal.HarukaConfig;
import com.sw.haruka.helper.utils.HarukaHandler;
import com.sw.haruka.helper.utils.HarukaHandlerConstant;

import java.io.File;
import java.util.List;

public class SearchActivity extends BaseStatusActivity {

    private RelativeLayout mSearchBox;
    private EditText mSearchInput;
    private ImageButton mClear;
    private TextView mResultTextView;
    private TextView mNotice;
    private FileExplorerBase mExplorer;


    private boolean isSearching = false;    // 是否正在搜索的标志
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initialTopBar();    // 加载顶栏
        initialResult();    // 初始化搜索结果组件

        System.out.println("嗯?");
        mMapLoading.setVisibility(View.VISIBLE);
        mSearchInput.setEnabled(false);
        mSearchInput.setHint("正在扫描文件..");
        mHandler.sendEmptyMessage(HarukaHandlerConstant.SCAN_OVER);
        mExplorer.setFileCore(new FileCore() {
            @Override
            public void refresh() {
                FileUtils.scan();
                startScan();
            }
        });
    }

    private void startScan() {
        mMapLoading.setVisibility(View.VISIBLE);
        mSearchInput.setEnabled(false);
        mSearchInput.setHint("正在扫描文件..");
        mHandler.sendEmptyMessage(HarukaHandlerConstant.SCAN_OVER);
    }

    /*
    加载顶栏
     */
    private void initialTopBar() {
        mSearchBox = findViewById(R.id.relativeLayout_search);
        mSearchBox.startAnimation(AnimationUtils.makeInAnimation(getApplicationContext(), true));
        mSearchInput = findViewById(R.id.editText_input);
        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                if (mHandler.hasMessages(HarukaHandlerConstant.SEARCH_FILE)) {
                    mHandler.removeMessages(HarukaHandlerConstant.SEARCH_FILE);
                    FileUtils.isSearching = false;
                }
                if (s.toString().trim().length() > 0) {
                    searchingPrompt();
                    mHandler.sendEmptyMessageDelayed(HarukaHandlerConstant.SEARCH_FILE, 1000);
                    mClear.setVisibility(View.VISIBLE);
                } else {
                    clearAll();
                }
            }
        });
        mClear = findViewById(R.id.imageButton_clear);
        mClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHandler.hasMessages(HarukaHandlerConstant.SEARCH_FILE)) {
                    mHandler.removeMessages(HarukaHandlerConstant.SEARCH_FILE);
                }
                mSearchInput.setText("");
                clearAll();
            }
        });
        Button cancel = findViewById(R.id.btn_no);
        cancel.startAnimation(AnimationUtils.makeInAnimation(getApplicationContext(), false));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mMapLoading = findViewById(R.id.progressBar_mapLoading);
    }

    /**
     * 不搜索，取消搜索，隐藏搜索相关组件
     */
    private void clearAll() {
        FileUtils.isSearching = false;
        mClear.setVisibility(View.GONE);
        mNotice.setVisibility(View.GONE);
        mExplorer.setVisibility(View.GONE);
        mResultTextView.setVisibility(View.GONE);
    }

    /*
    初始化搜索结果组件
     */
    private void initialResult() {
        mResultTextView = findViewById(R.id.textView_result);
        mNotice = findViewById(R.id.textView_notice);
        mExplorer = findViewById(R.id.search_explorer);
        mExplorer.setSearchCore(new SearchCore() {
            @Override
            public void openFolder(String path) {
                startActivity(new Intent(SearchActivity.this, FolderActivity.class).putExtra("path", path));
            }
        });
    }
    /*
    搜索进行中，显示提示信息
     */
    private void searchingPrompt() {
        mNotice.setText("正在搜索..");
        mNotice.setVisibility(View.VISIBLE);
        mExplorer.setVisibility(View.GONE);
        mResultTextView.setVisibility(View.GONE);
    }

    @SuppressLint("SetTextI18n")
    private void search() {
        String key = mSearchInput.getText().toString();
        List<File> fileList = FileUtils.searchFile(key, HarukaConfig.PATH_MAP);
        mNotice.setVisibility(View.GONE);
        mResultTextView.setVisibility(View.VISIBLE);
        if (!SPUtils.getBooleanValueDefaultFalse(Namespace.SHOW_HIDDEN_FILE_OFFSET)) {
            AndroidFileUtils.removeHiddenFile(fileList);
        }
        mResultTextView.setText("包含\"" + mSearchInput.getText().toString() + "\"的关键字的文件(" + fileList.size() +"项)");
        mExplorer.setVisibility(View.VISIBLE);
        mExplorer.load(fileList);
    }

    private HarukaHandler mHandler = new HarukaHandler(this) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case HarukaHandlerConstant.SEARCH_FILE:
                    search();
                    break;
                case HarukaHandlerConstant.SCAN_OVER:
                    reset();
                    break;
            }
        }
    };

    /**
     * 搜索准备
     */
    private void reset() {
        if (FileUtils.scanningThread != null) {
            mHandler.sendEmptyMessageDelayed(HarukaHandlerConstant.SCAN_OVER, 100);
            return;
        }
        mExplorer.stopRefresh();
        mSearchInput.setEnabled(true);
        mSearchInput.setHint("请输入搜索关键字");
        if (mSearchInput.getText().toString().trim().length() > 0) {
            mHandler.sendEmptyMessage(HarukaHandlerConstant.SEARCH_FILE);
        }
        mMapLoading.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mExplorer != null && isSearching) {
            mExplorer.resume();
        }
    }

    @Override
    public void onBackPressed() {
        if (mExplorer.back()) {
            return;
        }
        super.onBackPressed();
    }


    /*
            扫描文件
             */
    private ProgressBar mMapLoading;

}
