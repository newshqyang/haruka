package com.sw.haruka.model.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sw.haruka.R;


public class MyTextViewButtonDialog extends Dialog {
    public MyTextViewButtonDialog(@NonNull Context context) {
        super(context);
        initial();
    }

    public MyTextViewButtonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initial();
    }

    protected MyTextViewButtonDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initial();
    }

    private TextView mTextView;
    public TextView getTextView() {
        return mTextView;
    }

    private Button mButton;
    public Button getButton() {
        return mButton;
    }

    private void initial() {
        setContentView(R.layout.layout_text_view_button_card);
        mTextView = findViewById(R.id.textView);
        mButton = findViewById(R.id.button);
    }

}
