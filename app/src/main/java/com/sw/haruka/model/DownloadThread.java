package com.sw.haruka.model;

import android.content.Context;
import android.os.Handler;
import android.os.Message;


import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.sw.haruka.R;
import com.sw.haruka.dal.HarukaNotificationId;
import com.sw.haruka.model.entity.FileShareTask;
import com.sw.haruka.helper.utils.HarukaHandlerConstant;
import com.sw.haruka.helper.utils.NotificationUtils;

import java.util.ArrayList;
import java.util.List;

public class DownloadThread {

    private List<FileShareTask> mFileReceiveTaskList;
    private Handler mHandler;
    private NotificationCompat.Builder mBuilder;
    private NotificationManagerCompat mNotificationManagerCompat;
    public DownloadThread(Context context, Handler handler) {
        mFileReceiveTaskList = new ArrayList<>();
        mHandler = handler;
        if (mBuilder == null) {
            mBuilder = NotificationUtils.createNotificationBuilder(context,
                    context.getString(R.string.channel_name),
                    context.getString(R.string.channel_description),
                    context.getString(R.string.channel_id));
            mBuilder.setSmallIcon(android.R.drawable.stat_sys_download);
        }
        if (mNotificationManagerCompat == null) {
            mNotificationManagerCompat = NotificationUtils.createNotificationManager(context);
        }
    }

    private boolean isStarted = false;
    public boolean isStarted() {
        return isStarted;
    }
    public void start() {
        isStarted = true;
        while (mFileReceiveTaskList.size() > 0) {
            FileShareTask task = getFirstReceiveTask(); // 获取列表中的第一个任务
            if (task == null) {
                break;
            }
            boolean r = receivingFirstFile(task);   // 执行任务
            if (r) {  // 执行完毕，播报
                toastDownloadDone(task.getFileName());
            }
        }
        isStarted = false;
    }

    /*
    加入新任务，返回当前任务数
     */
    public int addNewTask(FileShareTask task) {
        mFileReceiveTaskList.add(task);
        return mFileReceiveTaskList.size();
    }

    /*
    任务完成，播报
     */
    private void toastDownloadDone(String fileName) {
        Message message = new Message();
        message.what = HarukaHandlerConstant.DOWNLOAD_DONE;
        message.obj = fileName;
        mHandler.sendMessage(message);
    }

    /*
    下载列表中第一个任务
     */
    private DownloadNotificationThread mDownloadNotificationThread;
    private boolean receivingFirstFile(FileShareTask task) {
        Message message = new Message();
        message.what = HarukaHandlerConstant.DOWNLOAD_START;
        message.obj = task.getFileName();
        mHandler.sendMessage(message);
        // 将任务送入发送接收程序，开始执行
        mBuilder.setContentTitle("下载 " + task.getFileName())
                .setContentText("正在下载")
                .setProgress(100, 0, false);
        task.setBuilder(mBuilder);
        if (mDownloadNotificationThread != null) {
            mDownloadNotificationThread.isLiving = false;
        }
        mDownloadNotificationThread = new DownloadNotificationThread(task);
        mDownloadNotificationThread.start();
        return new FileRequest(task).go();
    }

    class DownloadNotificationThread extends Thread{
        boolean isLiving = false;
        private FileShareTask mTask;
        public DownloadNotificationThread(FileShareTask task) {
            mTask = task;
        }

        @Override
        public synchronized void start() {
            isLiving = true;
            super.start();
        }

        @Override
        public void run() {
            while (mTask.getProgress() < 100 && isLiving) {
                mBuilder.setProgress(100, mTask.getProgress(), false);
                mNotificationManagerCompat.notify(HarukaNotificationId.DOWNLOAD_ID, mBuilder.build());
                try {
                    Thread.sleep(996);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            String result = "下载完成";
            if (!isLiving) {
                result = "下载已取消";
            }
            mBuilder.setProgress(0, 0, false)
                    .setSmallIcon(android.R.drawable.stat_sys_download_done)
                    .setContentText(result);
            mNotificationManagerCompat.notify(HarukaNotificationId.DOWNLOAD_ID, mBuilder.build());
        }
    }

    /*
    任务取消，播报
     */
    private void toastTaskCancel(String fileName) {
        Message message = new Message();
        message.what = HarukaHandlerConstant.DOWNLOAD_CANCEL;
        message.obj = fileName;
        mHandler.sendMessage(message);
    }

    /*
    获取第一个任务，如果有任务的话
     */
    public FileShareTask getFirstReceiveTask() {
        for (int i = 0;i < mFileReceiveTaskList.size();i++) {
            int state = mFileReceiveTaskList.get(i).getState();
            if (state == FileShareTask.STATE_CANCEL
                || state == FileShareTask.STATE_DONE) {
                mFileReceiveTaskList.remove(i);
                i--;
            }
        }
        if (mFileReceiveTaskList.size() > 0) {
            return mFileReceiveTaskList.get(0); // 获取任务列表中的第一个任务
        } else {
            return null;
        }
    }

    /*
    获取任务列表
     */
    public List<FileShareTask> getFileReceiveTaskList() {
        return mFileReceiveTaskList;
    }

    /*
    取消某一个任务
     */
    public void cancelTask(int position) {
        FileShareTask task = mFileReceiveTaskList.get(position);
        if (task != null) {
            task.setState(FileShareTask.STATE_CANCEL);
            toastTaskCancel(task.getFileName());
            mDownloadNotificationThread.isLiving = false;
        }
    }

    /*
    取消所有任务
     */
    public void cancelAllTasks() {
        for (int i = 0;i < mFileReceiveTaskList.size();i++) {
            mFileReceiveTaskList.get(i).setState(FileShareTask.STATE_CANCEL);
        }
    }
}
