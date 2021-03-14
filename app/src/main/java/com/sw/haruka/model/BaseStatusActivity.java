package com.sw.haruka.model;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.sw.haruka.helper.utils.SPUtils;
import com.sw.haruka.helper.utils.UUtils;

public class BaseStatusActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUtils.baseStatus(this);        // 设置状态栏为白底黑字
    }

    @Override
    protected void onPause() {
        SPUtils.commit();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        SPUtils.commit();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        closeKeyboard();
    }

    /**
     * 自动关闭软键盘
     */
    public void closeKeyboard() {
        InputMethodManager imm =  (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            System.out.println("藏了");
        }
    }

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
}
