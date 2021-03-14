package com.sw.haruka.model.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sw.haruka.R;
import com.sw.haruka.helper.utils.QrCodeDeliverConstant;
import com.yzq.zxinglibrary.encode.CodeCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class OperateDialog extends Dialog {

    private void initial() {
        setContentView(R.layout.layout_share_dialog);
    }

    private static final int QrCODE_WIDTH = 700;
    public OperateDialog createQrCode(String path, String ip, int port) {
        try {
            File file = new File(path);
            JSONObject content = new JSONObject();
            content.put(QrCodeDeliverConstant.DELIVER_IP, ip);
            content.put(QrCodeDeliverConstant.DELIVER_PORT, port);
            content.put(QrCodeDeliverConstant.FILE_PATH, file.getAbsolutePath());
            content.put(QrCodeDeliverConstant.FILE_SIZE, new FileInputStream(file).available());
            ImageView imageView = findViewById(R.id.imageView_qrCode);
            imageView.setImageBitmap(CodeCreator.createQRCode(content.toString(), QrCODE_WIDTH, QrCODE_WIDTH, null));
        } catch (JSONException | IOException e) {
            e.printStackTrace();
        }
        return this;
    }



    public OperateDialog(@NonNull Context context) {
        super(context);
        initial();
    }
    public OperateDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initial();
    }
    protected OperateDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initial();
    }
}
