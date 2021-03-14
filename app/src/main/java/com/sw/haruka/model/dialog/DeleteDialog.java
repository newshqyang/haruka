package com.sw.haruka.model.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sw.haruka.R;


public class DeleteDialog extends Dialog {
    public DeleteDialog(@NonNull Context context) {
        super(context);
        initial();
    }

    public DeleteDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initial();
    }

    protected DeleteDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initial();
    }

    private TextView mNotice;
    private Button mCancel;
    private Button mDelete;
    private void initial() {
        setContentView(R.layout.dialog_normal);
        mNotice = findViewById(R.id.tv_text);
        mCancel = findViewById(R.id.btn_no);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        mDelete = findViewById(R.id.btn_yes);
    }

    @SuppressLint("SetTextI18n")
    public DeleteDialog setFilePath(int size) {
        mNotice.setText("确定要删除这" + size + "个内容吗？");
        return this;
    }

    public Button getDelete() {
        return mDelete;
    }
}
