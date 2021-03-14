package com.sw.haruka.model;

import com.sw.haruka.helper.utils.QrCodeReceiverConstant;

import org.json.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class FileListenerThread extends Thread {

    private ServerSocket mServerSocket;

    public FileListenerThread(int port) {
        try {
            mServerSocket = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean lifeState = false;
    @Override
    public void run() {
        lifeState = true;
        while (lifeState) {
            try {
                JSONObject info = getInfo();
                FileDeliver deliver = new FileDeliver();
                deliver.deliver(info);
            } catch (Exception e) {
                System.out.println("接收文件socket获取失败");
                e.printStackTrace();
            }
        }
        lifeState = false;
        if (!mServerSocket.isClosed()) {
            try {
                mServerSocket.close();
            } catch (IOException e) {
                System.out.println("日志：关闭失败");
                e.printStackTrace();
            }
        }
    }

    /*
    关闭服务器
     */
    public void close() {
        lifeState = false;
        try {
            mServerSocket.close();
        } catch (IOException e) {
            System.out.println("日志：关闭成功!");
            e.printStackTrace();
        }
    }

    /*
    1、获取信息
     */
    private JSONObject getInfo() throws Exception {
        Socket socket = mServerSocket.accept();
        InputStream is = socket.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String temp = "";
        StringBuilder sb = new StringBuilder();
        while ((temp=br.readLine()) != null) {
            sb.append(temp);
        }
        br.close();
        isr.close();
        is.close();
        socket.close();
        return new JSONObject(sb.toString());
    }

    private class FileDeliver{

        public void deliver(JSONObject info) throws Exception {
            Socket socket = new Socket(info.getString(QrCodeReceiverConstant.RECEIVER_IP), info.getInt(QrCodeReceiverConstant.RECEIVER_PORT));
            BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
            FileInputStream fis = new FileInputStream(info.getString("filePath"));
            int totalSize = fis.available(); //文件总共的大小
            int savedSize = 0;  //当前发送容量
            byte[] buffer = new byte[1024];
            int len;
            while ((len=fis.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
                bos.flush();
                savedSize += len;
                // 进度：(int)((float)savedSize/totalSize * 100)
            }
            fis.close();
            bos.close();
            socket.close();
        }
    }
}
