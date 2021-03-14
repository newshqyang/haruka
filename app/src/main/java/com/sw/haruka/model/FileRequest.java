package com.sw.haruka.model;

import android.os.Handler;

import com.sw.haruka.model.entity.FileShareTask;
import com.sw.haruka.dal.HarukaConfig;
import com.sw.haruka.helper.utils.QrCodeReceiverConstant;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class FileRequest {

    private FileShareTask mTask;
    private static Handler mHandler;

    public static void setHandler(Handler handler) {
        mHandler = handler;
    }

    public FileRequest(FileShareTask task) {
        mTask = task;
    }

    public boolean go() {
        try {
            deliverInfo();  // 发送文件路径
            FileReceiver receiver = new FileReceiver(HarukaConfig.Receiver_Port);// 接收文件
            return receiver.receive();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /*
    1、先发送信息
     */
    private void deliverInfo() throws Exception {
        Socket socket = new Socket(mTask.getDeliverIP(), mTask.getDeliverPort());
        mTask.setState(FileShareTask.STATE_RUNNING);   // 任务启动
        OutputStream out = socket.getOutputStream();
        OutputStreamWriter osw = new OutputStreamWriter(out);
        BufferedWriter bw = new BufferedWriter(osw);
        JSONObject fileInfo = new JSONObject();
        fileInfo.put(QrCodeReceiverConstant.RECEIVER_IP, HarukaConfig.Local_IP);
        fileInfo.put(QrCodeReceiverConstant.RECEIVER_PORT, HarukaConfig.Receiver_Port);
        fileInfo.put("fileName", mTask.getFileName());
        fileInfo.put("filePath", mTask.getFilePath());
        fileInfo.put("fileSize", mTask.getFileSize());
        bw.write(fileInfo.toString());      // 发送文件传输信息
        bw.close();
        osw.close();
        out.close();
        socket.close();
    }

    /*
    2、接收文件
     */
    private class FileReceiver{

        private ServerSocket mServerSocket;
        FileReceiver(int port) {
            try {
                mServerSocket = new ServerSocket(port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public boolean receive() {
            try {
                Socket socket = mServerSocket.accept();
                return receiveFile(socket);
            } catch (Exception e) {
                System.out.println("接收文件socket获取失败");
                e.printStackTrace();
            }
            return false;
        }

        private boolean receiveFile(Socket socket) throws IOException {
            InputStream is = socket.getInputStream();
            File file = new File(HarukaConfig.Download_Path + "/" + mTask.getFileName());
            if (!file.exists()) {
                file.delete();
            }
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file, false);
            int totalSize = mTask.getFileSize(); //文件总共的大小
            int savedSize = 0;  //当前发送容量
            byte[] buffer = new byte[1024];
            int len;
            long startTime = System.currentTimeMillis();   //开始时间
            long startSize = 0;
            while ((mTask.getState() != FileShareTask.STATE_CANCEL)
                    && ((len=is.read(buffer)) != -1)) {
                fos.write(buffer, 0, len);
                savedSize += len;
                long nowTime = System.currentTimeMillis();
                if ((nowTime - startTime) >= 1000) {
                    long speed = (long)((savedSize - startSize) * 1000.0 / (nowTime - startTime));
                    mTask.setSpeed(speed);
                    startSize = savedSize;
                    startTime = nowTime;
                }
                mTask.setSharedSize(savedSize); // 已发大小
                mTask.setProgress((int)((float)savedSize/totalSize * 100)); // 进度
            }
            if ((mTask.getState() == FileShareTask.STATE_CANCEL)) {
                // 如果因为取消任务而跳出循环
                file.delete();//  删除文件
                return false;
            } else {
                mTask.setState(FileShareTask.STATE_DONE);
            }
            fos.close();
            is.close();
            socket.close();
            mServerSocket.close();
            return true;
        }
    }
}
