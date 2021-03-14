package com.sw.haruka.helper.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.sw.haruka.dal.HarukaConfig;
import com.sw.haruka.model.dialog.MyTextViewButtonDialog;

import java.io.File;

public class HarukaHandler extends Handler {

    private Context mContext;
    public HarukaHandler(Context context) {
        mContext = context;
    }
    /*
        XXX文件已开始下载
         */
    protected void toastDownloadStart(String fileName) {
        Toast.makeText(mContext, fileName + " 已开始下载！", Toast.LENGTH_SHORT).show();
    }

    /*
    XXX文件下载完成
     */
    @SuppressLint("SetTextI18n")
    protected void toastDownloadDone(final String fileName) {
        final MyTextViewButtonDialog doneDialog = new MyTextViewButtonDialog(mContext);
        doneDialog.getTextView().setText("“" + fileName + "”下载完成");
        doneDialog.getButton().setText("打开");
        doneDialog.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AndroidFileUtils.openFile(mContext, new File(HarukaConfig.Download_Path + "/" + fileName));
                doneDialog.dismiss();
            }
        });
        doneDialog.show();
    }

    /*
    取消任务播报
     */
    protected void toastDownloadCancel(String fileName) {
        Toast.makeText(mContext, fileName + " 已取消下载", Toast.LENGTH_SHORT).show();
    }
}
