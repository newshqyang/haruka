package com.sw.haruka.helper.utils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class PortUtils {

    /**
     * 获取可用端口号
     * @return  可用端口号
     */
    public static int getPort() {
        Socket socket;
        String host = "localhost";
        for (int i = 2121;i < 12000;i++) {
            try {
                socket = new Socket(host, i);
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                return i;
            }
        }
        return 9090;
    }

}
