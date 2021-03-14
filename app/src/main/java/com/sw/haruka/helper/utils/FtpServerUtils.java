package com.sw.haruka.helper.utils;



import org.apache.ftpserver.FtpServer;
import org.apache.ftpserver.FtpServerFactory;
import org.apache.ftpserver.ftplet.Authority;

import org.apache.ftpserver.listener.ListenerFactory;
import org.apache.ftpserver.usermanager.impl.BaseUser;
import org.apache.ftpserver.usermanager.impl.WritePermission;

import java.util.ArrayList;
import java.util.List;

public class FtpServerUtils {

    public static boolean isOpen = false;
    public static int PORT = 8080;

    private static FtpServer mFtpServer;
    public static void start(final String path) { // 初始化
        if (mFtpServer != null) {
            mFtpServer.stop();
            mFtpServer = null;
        }
        try {
            FtpServerFactory serverFactory = new FtpServerFactory();

            //设置访问用户名和密码还有共享路径
            BaseUser baseUser = new BaseUser();
            baseUser.setName("anonymous");
            baseUser.setPassword("");
            baseUser.setHomeDirectory(path);

            List<Authority> authorities = new ArrayList<>();
            authorities.add(new WritePermission());
            baseUser.setAuthorities(authorities);
            serverFactory.getUserManager().save(baseUser);

            ListenerFactory factory = new ListenerFactory();
            factory.setPort(PORT); //设置端口号 非ROOT不可使用1024以下的端口
            serverFactory.addListener("default", factory.createListener());

            mFtpServer = serverFactory.createServer();
            mFtpServer.start();
            isOpen = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止
     */
    public static void stop() {
        if (mFtpServer != null && !mFtpServer.isStopped()) {
            mFtpServer.stop();
            isOpen = false;
        }
    }
}
