package com.sw.haruka.view;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Message;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sw.haruka.R;
import com.sw.haruka.model.BaseStatusActivity;
import com.sw.haruka.model.dialog.AboutMe;
import com.sw.haruka.model.dialog.DownloadDialog;
import com.sw.haruka.model.DownloadThread;
import com.sw.haruka.model.FileListenerThread;
import com.sw.haruka.model.dialog.NormalDialog;
import com.sw.haruka.model.entity.FileExplorer;
import com.sw.haruka.helper.utils.SPUtils;
import com.sw.haruka.helper.utils.FileUtils;
import com.sw.haruka.dal.HarukaConfig;
import com.sw.haruka.helper.utils.HarukaHandler;
import com.sw.haruka.helper.utils.HarukaHandlerConstant;
import com.sw.haruka.helper.utils.NetUtils;
import com.sw.haruka.helper.utils.StringUtil;
import com.sw.haruka.helper.utils.ToastUtils;
import com.yzq.zxinglibrary.android.CaptureActivity;
import com.yzq.zxinglibrary.common.Constant;


import tech.haruka.openutils.PermissionUtil;


public class MainActivity extends BaseStatusActivity implements View.OnClickListener {

    private Button mSearchBox;
    private ImageButton mScanQrCode;
    private ImageButton mDownloadMonitor;
    private ProgressBar mDownloading;

    private ImageButton mSettingMoreImageButton;
    private FileExplorer mFileExplorer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ToastUtils.setContext(getApplicationContext());
        SPUtils.setContext(this);
        init();
    }

    private void init() {
        mFileExplorer = findViewById(R.id.fileExplorer);
        mSearchBox = findViewById(R.id.button_search);
        mScanQrCode = findViewById(R.id.imageButton_scan);
        mDownloadMonitor = findViewById(R.id.imageButton_download);
        mDownloading = findViewById(R.id.progressBar_downloading);
        findViewById(R.id.imageButton_more).setOnClickListener(this);
        mSettingMoreImageButton = findViewById(R.id.imageButton_more);
        mSearchBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
        mScanQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED) {
                    PermissionUtil.getCameraPermission(MainActivity.this);
                    return;
                }
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        });
        mDownloadMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DownloadMonitorActivity.setDownloadThread(mDownloadThread);
                startActivity(new Intent(MainActivity.this, DownloadMonitorActivity.class));
            }
        });

        HarukaConfig.Local_IP = NetUtils.getLocalIP(this);
        loadFileList();
    }

    /*
    加载文件列表
     */
    private void loadFileList() {
        mFileExplorer.initList(HarukaConfig.ROOT_PATH);
    }

    /*
    下载文件服务
     */
    private DownloadThread mDownloadThread;
    private FileListenerThread mFileListenerThread;
    private void downloadService() {
        if (mDownloadThread == null || mFileListenerThread == null) {
            mDownloadThread = new DownloadThread(this, mHandler);
            mFileListenerThread = new FileListenerThread(HarukaConfig.Deliver_Port);
            mFileListenerThread.start();
        }
        FileUtils.createNewFolder(HarukaConfig.Download_Path);
    }

    /*
    动画
     */
    private void animationGo() {
        mSearchBox.startAnimation(AnimationUtils.makeInAnimation(getApplicationContext(), true));
        mScanQrCode.startAnimation(AnimationUtils.makeInAnimation(getApplicationContext(), false));
        mDownloadMonitor.startAnimation(AnimationUtils.makeInAnimation(getApplicationContext(), false));
    }

    private HarukaHandler mHandler = new HarukaHandler(this) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case HarukaHandlerConstant.DOWNLOAD_DONE:
                    mDownloading.setVisibility(View.INVISIBLE);
                    toastDownloadDone((String)msg.obj);
                    break;
                case HarukaHandlerConstant.DOWNLOAD_START:
                    mDownloading.setVisibility(View.VISIBLE);
                    toastDownloadStart((String)msg.obj);
                    break;
                case HarukaHandlerConstant.DOWNLOAD_CANCEL:
                    mDownloading.setVisibility(View.GONE);
                    toastDownloadCancel((String)msg.obj);
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PermissionUtil.CAMERA_PERMISSION_CODE) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                // 没有获取相关权限，不予加载
                Toast.makeText(this, "没有相机权限，无法使用扫码功能", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SCAN);
            }
        }
    }

    private static final int REQUEST_CODE_SCAN = 1000;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_SCAN && resultCode == RESULT_OK) {
            if (data != null) {
                final String text = data.getStringExtra(Constant.CODED_CONTENT);
                try {
                    DownloadDialog dialog = new DownloadDialog(this, mHandler);
                    dialog.setDownloadThread(mDownloadThread);
                    dialog.setQrCodeInfo(text);
                    dialog.show();
                } catch (Exception e) {
                    final NormalDialog dialog = new NormalDialog(MainActivity.this);
                    dialog.getNoButton().setText("关闭");
                    dialog.getYesButton().setText("复制");
                    dialog.setText(text).setYesClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            StringUtil.setClipData(MainActivity.this, text);
                            Toast.makeText(MainActivity.this, "已复制", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        if (mFileExplorer.back()) {
            return;
        }
        finishAffinity();
    }

    @Override
    protected void onResume() {
        super.onResume();
        FileUtils.scan();   // 开始扫描
        downloadService();      // 下载文件服务
        animationGo();      // 加载动画
        if (mFileExplorer != null) {
            mFileExplorer.refresh();
        }
    }

    @Override
    protected void onDestroy() {
//        FtpServerUtils.stop();
        super.onDestroy();
        if (mFileListenerThread != null) {
            mFileListenerThread.close();
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.imageButton_more) {
            PopupMenu menu = new PopupMenu(this, mSettingMoreImageButton);
            menu.getMenuInflater().inflate(R.menu.main_more, menu.getMenu());
            menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.main_more_aboutMe:
                            new AboutMe(MainActivity.this).show();
                            break;
                        case R.id.main_more_setting:
                            startActivity(new Intent(MainActivity.this, SettingActivity.class));
                            break;
                        case R.id.main_more_ftp:
                            startActivity(new Intent(MainActivity.this, FtpActivity.class));
                            break;
                    }
                    return false;
                }
            });
            menu.show();
        }
    }
}
