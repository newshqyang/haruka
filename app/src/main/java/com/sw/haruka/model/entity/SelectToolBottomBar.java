package com.sw.haruka.model.entity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.sw.haruka.R;

import java.io.File;

public class SelectToolBottomBar extends LinearLayout{

    private LinearLayout mDelete;
    private LinearLayout mQrCode;
    private LinearLayout mMove;
    private LinearLayout mMore;
    private Context mContext;
    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(context).inflate(R.layout.layout_select_tool_bar, this);
        initSelectComponent();
    }

    /*
    加载多选工具栏组件
     */

    private void initSelectComponent() {
        mQrCode = findViewById(R.id.qrCode);
        mMove = findViewById(R.id.move);
        mDelete = findViewById(R.id.delete);
        mMore = findViewById(R.id.more);
        refreshSelectToolBar(0, null);
    }

    /*
    二维码功能开关
     */
    private void qrCodeOffset(boolean enabled) {
        ImageView image = findViewById(R.id.qrCode_image);
        TextView text = findViewById(R.id.qrCode_text);
        if (enabled) {
            image.setImageDrawable(mContext.getDrawable(R.drawable.qrcode_icon));
            text.setTextColor(Color.BLACK);
        } else {
            image.setImageDrawable(mContext.getDrawable(R.drawable.qrcode_icon_off));
            text.setTextColor(Color.LTGRAY);
        }
        mQrCode.setEnabled(enabled);
    }

    /*
    移动功能开关
     */
    private void moveOffset(boolean enabled) {
        ImageView image = findViewById(R.id.move_image);
        TextView text = findViewById(R.id.move_text);
        if (enabled) {
            image.setImageDrawable(mContext.getDrawable(R.drawable.move_icon));
            text.setTextColor(Color.BLACK);
        } else {
            image.setImageDrawable(mContext.getDrawable(R.drawable.move_icon_off));
            text.setTextColor(Color.LTGRAY);
        }
        mMove.setEnabled(enabled);
    }

    /*
    删除功能开关
    */
    private void deleteOffset(boolean enabled) {
        ImageView image = findViewById(R.id.delete_image);
        TextView text = findViewById(R.id.delete_text);
        if (enabled) {
            image.setImageDrawable(mContext.getDrawable(R.drawable.delete_icon));
            text.setTextColor(Color.BLACK);
        } else {
            image.setImageDrawable(mContext.getDrawable(R.drawable.delete_icon_off));
            text.setTextColor(Color.LTGRAY);
        }
        mDelete.setEnabled(enabled);
    }

    /*
    更多功能开关
    */
    private void moreOffset(boolean enabled) {
        ImageView image = findViewById(R.id.more_image);
        TextView text = findViewById(R.id.more_text);
        if (enabled) {
            image.setImageDrawable(mContext.getDrawable(R.drawable.more_menu));
            text.setTextColor(Color.BLACK);
        } else {
            image.setImageDrawable(mContext.getDrawable(R.drawable.more_menu_off));
            text.setTextColor(Color.LTGRAY);
        }
        mMore.setEnabled(enabled);
    }

    /*
    刷新多选工具栏，
    参数1：选择文件数量
    参数2：单选时，判断文件是否为文件夹
     */
    @SuppressLint("SetTextI18n")
    public void refreshSelectToolBar(int selectFileSize, String filePath) {
        switch (selectFileSize) {
            case 0:
                // 啥也没选
                qrCodeOffset(false);
                moveOffset(false);
                deleteOffset(false);
                moreOffset(false);
                break;
            case 1:
                // 单选
                if (new File(filePath).isFile()) {
                    qrCodeOffset(true);
                } else {
                    qrCodeOffset(false);
                }
                moveOffset(true);
                deleteOffset(true);
                moreOffset(true);
                break;
            default:
                // 多选
                qrCodeOffset(false);
                moveOffset(true);
                deleteOffset(true);
                moreOffset(false);
        }
    }



    public LinearLayout getDelete() {
        return mDelete;
    }
    public LinearLayout getQrCode() {
        return mQrCode;
    }
    public LinearLayout getMove() {
        return mMove;
    }
    public LinearLayout getMore() {
        return mMore;
    }
    public SelectToolBottomBar(Context context) {
        super(context);
        init(context);
    }
    public SelectToolBottomBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public SelectToolBottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public SelectToolBottomBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void show(int selectFileSize, String filePath) {
        show();
        refreshSelectToolBar(selectFileSize, filePath);
    }

    public void show() {
        if (getVisibility() != VISIBLE) {
            startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.layout_up));
            setVisibility(VISIBLE);
        }
    }

    public void hidden() {
        if (getVisibility() != GONE) {
            startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.layout_down));
            setVisibility(GONE);
            refreshSelectToolBar(0, null);
        }
    }
}
