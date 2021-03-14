package com.sw.haruka.model.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.sw.haruka.R;


public class NormalDialog extends Dialog {
    public NormalDialog(@NonNull Context context) {
        super(context);
        initial();
    }

    public Button getNoButton() {
        return findViewById(R.id.btn_no);
    }

    public Button getYesButton() {
        return findViewById(R.id.btn_yes);
    }

    private void initial() {
        setContentView(R.layout.dialog_normal);
        findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    /**
     * 设置对话框的文本
     * @param text  要显示的文本
     */
    public NormalDialog setText(String text) {
        TextView textView = findViewById(R.id.tv_text);
        textView.setText(text);
        return this;
    }

    /**
     * 绑定yes按钮监听器
     * @param listener  监听器
     */
    public NormalDialog setYesClickListener(View.OnClickListener listener) {
        findViewById(R.id.btn_yes).setOnClickListener(listener);
        return this;
    }
}
