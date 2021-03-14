package com.sw.haruka.view;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sw.haruka.R;
import com.sw.haruka.dal.HarukaConfig;
import com.sw.haruka.model.BaseStatusActivity;
import com.sw.haruka.helper.utils.FtpServerUtils;
import com.sw.haruka.helper.utils.NetUtils;
import com.sw.haruka.helper.utils.PortUtils;

public class FtpActivity extends BaseStatusActivity {

    private TextView mTVFtpInfo;
    private Button mBtnOffset;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftp);

        mTVFtpInfo = findViewById(R.id.tv_ftpInfo);
        mBtnOffset = findViewById(R.id.btn_offset);
        if (FtpServerUtils.isOpen) {
            opened();
        } else {
            closed();
        }
    }

    /**
     * 已关闭的情况
     */
    private void closed() {
        String temp = "FTP服务已关闭";
        mTVFtpInfo.setText(temp);
        mBtnOffset.setText("开启服务");
        mBtnOffset.setTextColor(getColor(android.R.color.holo_red_dark));
        mBtnOffset.setBackground(getDrawable(R.drawable.button_ellipse_background));
        mBtnOffset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(1);
            }
        });
    }

    public void back(View view) {
        finish();
    }

    /**
     * 已开启的情况
     */
    private void opened() {
        String temp = "FTP服务已开启\n请在浏览器地址栏输入：\nftp://" + NetUtils.getLocalIP(this) +
                ":" + FtpServerUtils.PORT;
        mTVFtpInfo.setText(temp);
        mBtnOffset.setText("关闭服务");
        mBtnOffset.setTextColor(getColor(android.R.color.white));
        mBtnOffset.setBackground(getDrawable(R.drawable.button_ellipse_background_close));
        mBtnOffset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(2);
            }
        });
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    open();
                    break;
                case 2:
                    FtpServerUtils.stop();
                    closed();
                    break;
            }
        }
    };

    private void open() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FtpServerUtils.PORT = PortUtils.getPort();
                FtpServerUtils.start(HarukaConfig.ROOT_PATH);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        opened();
                    }
                });
            }
        }).start();
    }
}