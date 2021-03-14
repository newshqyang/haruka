package com.sw.haruka.model.entity;

import androidx.core.app.NotificationCompat;

public class FileShareTask {

    private int uid;    // 云文件uid
    public int getUid() {
        return uid;
    }
    public void setUid(int uid) {
        this.uid = uid;
    }

    private String fileName;    // 文件名
    private String filePath;    // 发送者那边的文件路径
    private int fileSize;      // 文件大小
    private String fileUpdate;  // 文件最近修改日期
    private String deliverIP;   // 发送者IP
    private int deliverPort;  // 发送者监听的端口

    private int sharedSize; // 已传大小
    private int progress;   // 任务进度
    private long speed;      // 传输速度
    public static final int STATE_WAITING = 0;     // 正在等待
    public static final int STATE_CANCEL = -1;   // 已取消
    public static final int STATE_DONE = 100;     // 已完成
    public static final int STATE_RUNNING = 1;  // 正在运行
    private int state = STATE_WAITING;  // 任务状态

    private NotificationCompat.Builder builder; // 通知栏

    public NotificationCompat.Builder getBuilder() {
        return builder;
    }

    public void setBuilder(NotificationCompat.Builder builder) {
        this.builder = builder;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getFileSize() {
        return fileSize;
    }

    public void setFileSize(int fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileUpdate() {
        return fileUpdate;
    }

    public void setFileUpdate(String fileUpdate) {
        this.fileUpdate = fileUpdate;
    }

    public String getDeliverIP() {
        return deliverIP;
    }

    public void setDeliverIP(String deliverIP) {
        this.deliverIP = deliverIP;
    }

    public int getDeliverPort() {
        return deliverPort;
    }

    public void setDeliverPort(int deliverPort) {
        this.deliverPort = deliverPort;
    }

    public int getSharedSize() {
        return sharedSize;
    }

    public void setSharedSize(int sharedSize) {
        this.sharedSize = sharedSize;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public long getSpeed() {
        return speed;
    }

    public void setSpeed(long speed) {
        this.speed = speed;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
