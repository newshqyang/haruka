package com.sw.haruka.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;

import com.sw.haruka.R;
import com.sw.haruka.model.BaseStatusActivity;
import com.sw.haruka.model.DownloadThread;
import com.sw.haruka.helper.adapter.DownloadAdapter;
import com.sw.haruka.dal.HarukaConfig;
import com.sw.haruka.helper.utils.HarukaHandlerConstant;

public class DownloadMonitorActivity extends BaseStatusActivity {

    private ImageButton mBack;
    private RecyclerView mDownloadRanks;
    private ImageButton mHistory;
    private static DownloadThread mDownloadThread;
    private int Refresh_ListView = 1;

    public static void setDownloadThread(DownloadThread downloadThread) {
        mDownloadThread = downloadThread;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_monitor);
        initialTopBar();
        initialDownloadRanks();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    mHandler.sendEmptyMessage(HarukaHandlerConstant.REFRESH_DOWNLOAD_LIST);
                }
            }
        }).start();
    }

    /*
    加载顶栏
     */
    private void initialTopBar() {
        mBack = findViewById(R.id.imageButton_back);
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mHistory = findViewById(R.id.imageButton_history);
        mHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FolderActivity.class)
                        .putExtra("path", HarukaConfig.Download_Path));
            }
        });
    }

    /*
    加载下载队列
     */
    private void initialDownloadRanks() {
        mDownloadRanks = findViewById(R.id.recyclerView_downloadRanks);
        mDownloadRanks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(100);  // 每隔0.1s刷新一次列表
                        mHandler.sendEmptyMessage(Refresh_ListView);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (msg.what == HarukaHandlerConstant.REFRESH_DOWNLOAD_LIST) {
                mDownloadRanks.setAdapter(new DownloadAdapter(getApplicationContext(), mDownloadThread));
                if (mDownloadThread.getFileReceiveTaskList().size() > 0) {
                    mDownloadRanks.setBackgroundColor(Color.WHITE);
                } else {
                    mDownloadRanks.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        }
    };
}
