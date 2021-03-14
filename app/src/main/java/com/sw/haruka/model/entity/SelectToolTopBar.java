package com.sw.haruka.model.entity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sw.haruka.R;

public class SelectToolTopBar extends LinearLayout{


    private ImageButton mCancel;
    private TextView mTextSelectSize;
    private ImageButton mSelectAll;


    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.select_tool_top, this);
        initSelectComponent();
    }

    /*
    加载多选工具栏组件
     */

    private void initSelectComponent() {
        mTextSelectSize = findViewById(R.id.text_selectSize);
        mCancel = findViewById(R.id.imageButton_cancel);
        mSelectAll = findViewById(R.id.imageButton_selectAll);
    }


    public ImageButton getCancel() {
        return mCancel;
    }
    @SuppressLint("SetTextI18n")
    public void setSelectSize(int size) {
        mTextSelectSize.setText("已选中 " + size + " 项");
    }
    public ImageButton getSelectAll() {
        return mSelectAll;
    }
    public SelectToolTopBar(Context context) {
        super(context);
        init(context);
    }
    public SelectToolTopBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public SelectToolTopBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public SelectToolTopBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }
    public void hidden() {
        setSelectSize(0);
        setVisibility(GONE);
    }
    public void show() {
        setVisibility(VISIBLE);
    }
}
