package com.sw.haruka.model.entity;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sw.haruka.R;
import com.sw.haruka.helper.utils.FileUtils;
import com.sw.haruka.helper.utils.ShowUtils;

public class StorageBar extends LinearLayout {

    private TextView mUsefulSize;
    private TextView mTotalSize;
    private ProgressBar mSizePercent;
    private Context mContext;
    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.storage_bar, this);
        initComponent();
        refreshSizeInfo();
    }

    /*
    加载设备存储空间栏组件
     */
    private void initComponent() {
        mUsefulSize = findViewById(R.id.textView_usefulSize);
        mTotalSize = findViewById(R.id.textView_totalSize);
        mSizePercent = findViewById(R.id.progressBar_sizePercent);
    }
    public void refreshSizeInfo() {
        long usefulSize = FileUtils.getUsefulSize();
        mUsefulSize.setText(FileUtils.getFormatSize(usefulSize));
        long totalSize = FileUtils.getTotalSize();
        mTotalSize.setText(FileUtils.getFormatSize(totalSize));
        int usedPercent = (int)((totalSize - usefulSize) * 100 / totalSize);
        mSizePercent.setProgress(usedPercent);
    }


    public StorageBar(Context context) {
        super(context);
        init(context);
    }
    public StorageBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public StorageBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public StorageBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    boolean isShow = false;
    public void show() {
        if (isShow) {
            return;
        }
        ShowUtils.pop(this);
        isShow = true;
    }

    public void hidden() {
        if (!isShow) {
            return;
        }
        ShowUtils.hide(this);
        isShow = false;
    }
}
