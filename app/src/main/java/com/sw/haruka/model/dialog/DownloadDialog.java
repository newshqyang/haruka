package com.sw.haruka.model.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sw.haruka.R;
import com.sw.haruka.model.DownloadThread;
import com.sw.haruka.model.entity.FileShareTask;
import com.sw.haruka.helper.utils.FileUtils;
import com.sw.haruka.dal.HarukaConfig;
import com.sw.haruka.helper.utils.HarukaHandler;
import com.sw.haruka.helper.utils.NetUtils;
import com.sw.haruka.helper.utils.QrCodeDeliverConstant;

import org.json.JSONException;
import org.json.JSONObject;

public class DownloadDialog extends Dialog {
    public DownloadDialog(@NonNull Context context, HarukaHandler handler) {
        super(context);
        initial(context, handler);
    }

    public DownloadDialog(@NonNull Context context, int themeResId, HarukaHandler handler) {
        super(context, themeResId);
        initial(context, handler);
    }

    protected DownloadDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener, HarukaHandler handler) {
        super(context, cancelable, cancelListener);
        initial(context, handler);
    }

    private ImageView mType;
    private TextView mFileNameTextView;
    private TextView mSize;
    private Button mDownload;
    private Context mContext;
    private void initial(final Context context, final HarukaHandler handler) {
        mContext = context;
        setContentView(R.layout.layout_download_dialog);
        mType = findViewById(R.id.imageView_fileType);
        mFileNameTextView = findViewById(R.id.textView_fileName);
        mSize = findViewById(R.id.textView_fileSize);
        mDownload = findViewById(R.id.button_download);
        mDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HarukaConfig.Local_IP = NetUtils.getLocalIP(getContext());
                if (HarukaConfig.Local_IP.equals("0.0.0.0")) {
                    Toast.makeText(getContext(), "请先连接到局域网", Toast.LENGTH_SHORT).show();
                    return;
                }
                FileShareTask task = new FileShareTask();
                task.setDeliverIP(mDeliverIP);
                task.setDeliverPort(mDeliverPort);   // 暂时大家的Port都一样
                task.setFileName(mFileName);
                task.setFilePath(mFilePath);
                task.setFileSize(mFileSize);
                mDownloadThread.addNewTask(task);
                if (!mDownloadThread.isStarted()) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mDownloadThread.start();
                        }
                    }).start();
                }
                dismiss();
            }
        });
    }

    private String mDeliverIP;
    private int mDeliverPort;
    private String mFileName;
    private String mFilePath;
    private int mFileSize;
    public void setQrCodeInfo(String qrCodeInfo) throws JSONException {
        JSONObject info = new JSONObject(qrCodeInfo);
        mDeliverIP = info.getString(QrCodeDeliverConstant.DELIVER_IP);
        mDeliverPort = info.getInt(QrCodeDeliverConstant.DELIVER_PORT);
        mFilePath = info.getString(QrCodeDeliverConstant.FILE_PATH);
        mFileName = mFilePath.split("/")[mFilePath.split("/").length - 1];// 根据文件类型切换图片
        mFileNameTextView.setText(mFileName);
        if (mFileName.contains(".")) {
            mType.setImageDrawable(mContext.getDrawable(FileUtils.getTypeImage(FileUtils.getSuffix(mFileName))));
        }
        mFileSize = info.getInt(QrCodeDeliverConstant.FILE_SIZE);
        mSize.setText(FileUtils.getFormatSize(info.getLong(QrCodeDeliverConstant.FILE_SIZE)));
        if (mDeliverIP.equals(NetUtils.getLocalIP(mContext))) {
            mDownload.setText("自己发送的文件不可以下载");
            mDownload.setBackgroundColor(mContext.getColor(R.color.colorDateFont));
            mDownload.setEnabled(false);
        }
    }

    private DownloadThread mDownloadThread;
    public void setDownloadThread(DownloadThread downloadThread) {
        mDownloadThread = downloadThread;
    }
}
