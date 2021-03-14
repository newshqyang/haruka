package com.sw.haruka.model.dialog;

import android.app.Dialog;
import android.content.Context;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sw.haruka.R;

public class MyEditTextButtonDialog extends Dialog {
    public MyEditTextButtonDialog(@NonNull Context context) {
        super(context);
        initial();
    }

    public MyEditTextButtonDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initial();
    }

    protected MyEditTextButtonDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initial();
    }

    private EditText mEditText;

    public EditText getEditText() {
        return mEditText;
    }

    private Button mButton;
    public Button getButton() {
        return mButton;
    }
    private void initial() {
        setContentView(R.layout.layout_edit_text_button_dialog);
        mEditText = findViewById(R.id.editText);
        mButton = findViewById(R.id.button);
    }
}
