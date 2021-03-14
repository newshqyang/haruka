package com.sw.haruka.dal;

import android.os.Environment;

import java.util.HashSet;

public class HarukaConfig {

    private HarukaConfig() {}
    public static int TYPE_FILE = 1;// 0是文件，1是文件夹
    public static int TYPE_FOLDER = 0;
    public static String Download_Path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download/Haruka";
    public static String Local_IP;   // 本地IP
    public static int Deliver_Port = 9527;    // 接收方端口
    public static int Receiver_Port = 9528; // 发送方端口

    public static String ROOT_PATH = Environment.getExternalStorageDirectory().getAbsolutePath(); // 设备根路径
    public static String TEMP_PATH = ROOT_PATH; // 临时路径
    public static String ROOT_NAME = "内部存储设备";  // 根路径坐标名称

    public static HashSet<String> PATH_MAP = new HashSet<>();  // 路径地图

    /*
    云文件夹相关
     */
    public static boolean isLogin = false;  // 登录状态
}
